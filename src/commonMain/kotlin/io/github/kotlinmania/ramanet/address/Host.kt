// port-lint: source address/host.rs
package io.github.kotlinmania.ramanet.address

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A host which can be either a [Domain] name or an IP [Address].
 */
@Serializable(with = HostSerializer::class)
sealed class Host : Comparable<Host> {
    data class Name(
        val domain: Domain,
    ) : Host() {
        override fun toString(): String = domain.toString()

        override fun isName(): Boolean = true

        override fun asDomain(): Domain = domain

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other is String) return domain.asString().equals(other, ignoreCase = true)
            if (other !is Name) return false
            return domain == other.domain
        }

        override fun hashCode(): Int = domain.hashCode()
    }

    data class Address(
        val ip: String,
    ) : Host() {
        init {
            val valid = ParseUtils.tryToParseStrToIp(ip)
            require(valid != null) { "invalid IP address: $ip" }
        }

        override fun toString(): String = ip

        override fun isAddress(): Boolean = true

        override fun asIp(): String = ip

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other is String) return ip.equals(other, ignoreCase = true)
            if (other !is Address) return false
            return ip == other.ip
        }

        override fun hashCode(): Int = ip.lowercase().hashCode()
    }

    open fun isName(): Boolean = false

    open fun isAddress(): Boolean = false

    open fun asDomain(): Domain? = null

    open fun asIp(): String? = null

    fun isDomain(): Boolean = isName()

    fun intoDomain(): Domain? = asDomain()

    fun isIp(): Boolean = isAddress()

    fun intoIp(): String? = asIp()

    fun isIpv4(): Boolean = isAddress() && ParseUtils.isValidIpv4(asIp() ?: "")

    fun isIpv6(): Boolean = isAddress() && ParseUtils.isValidIpv6(asIp() ?: "")

    fun toStr(): String = toString()

    override fun compareTo(other: Host): Int = toString().compareTo(other.toString(), ignoreCase = true)

    fun fmt(): String = toString()

    fun eq(other: String): Boolean = equals(other)

    fun eq(other: Host): Boolean = equals(other)

    companion object {
        val LOCALHOST_IPV4: Host = Address(IpConstants.IPV4_LOCALHOST)
        val LOCALHOST_IPV6: Host = Address(IpConstants.IPV6_LOCALHOST)
        val LOCALHOST_NAME: Host = Name(Domain.fromStatic("localhost"))
        val DEFAULT_IPV4: Host = Address(IpConstants.IPV4_UNSPECIFIED)
        val DEFAULT_IPV6: Host = Address(IpConstants.IPV6_UNSPECIFIED)
        val BROADCAST_IPV4: Host = Address(IpConstants.IPV4_BROADCAST)
        val EXAMPLE_NAME: Host = Name(Domain.fromStatic("example.com"))

        fun localIpv4(): Host = LOCALHOST_IPV4

        fun localIpv6(): Host = LOCALHOST_IPV6

        fun defaultIpv4(): Host = DEFAULT_IPV4

        fun defaultIpv6(): Host = DEFAULT_IPV6

        fun broadcastIpv4(): Host = BROADCAST_IPV4

        fun localhost(): Host = LOCALHOST_NAME

        fun example(): Host = EXAMPLE_NAME

        fun fromStatic(s: String): Host = parse(s)

        fun from(domain: Domain): Host = Name(domain)

        fun from(ip: String): Host = Address(ip)

        fun fmt(host: Host): String = host.toString()

        fun fromStr(s: String): Host = parse(s)

        fun tryFrom(s: String): Host = parse(s)

        fun tryFrom(bytes: ByteArray): Host {
            val ip = tryToParseBytesToIp(bytes)
            if (ip != null) return Address(ip)
            val s = bytes.decodeToString()
            return Name(Domain.parse(s))
        }

        fun tryToParseBytesToIp(value: ByteArray): String? {
            try {
                val s = value.decodeToString()
                val ip = ParseUtils.tryToParseStrToIp(s)
                if (ip != null) return ip
            } catch (_: Exception) {
            }

            if (value.size == 4) {
                val a = value[0].toInt() and 0xFF
                val b = value[1].toInt() and 0xFF
                val c = value[2].toInt() and 0xFF
                val d = value[3].toInt() and 0xFF
                return "$a.$b.$c.$d"
            }

            if (value.size == 16) {
                return formatIpv6Bytes(value)
            }

            return null
        }

        private fun formatIpv6Bytes(bytes: ByteArray): String {
            val words =
                IntArray(8) { i ->
                    val b1 = bytes[i * 2].toInt() and 0xFF
                    val b2 = bytes[i * 2 + 1].toInt() and 0xFF
                    (b1 shl 8) or b2
                }

            var bestStart = -1
            var bestLen = 0
            var curStart = -1
            var curLen = 0
            for (i in 0 until 8) {
                if (words[i] == 0) {
                    if (curStart == -1) {
                        curStart = i
                        curLen = 1
                    } else {
                        curLen++
                    }
                    if (curLen > bestLen) {
                        bestStart = curStart
                        bestLen = curLen
                    }
                } else {
                    curStart = -1
                    curLen = 0
                }
            }

            if (bestLen >= 2 || (bestLen == 7 && bestStart == 0) || bestLen == 8) {
                val before = words.slice(0 until bestStart).joinToString(":") { it.toString(16) }
                val after = words.slice((bestStart + bestLen) until 8).joinToString(":") { it.toString(16) }
                return "$before::$after"
            }

            return words.joinToString(":") { it.toString(16) }
        }

        fun parse(s: String): Host {
            val ip = ParseUtils.tryToParseStrToIp(s)
            return if (ip != null) {
                Address(ip)
            } else {
                Name(Domain.parse(s))
            }
        }
    }
}

object HostSerializer : KSerializer<Host> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.Host", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Host) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Host = Host.parse(decoder.decodeString())
}
