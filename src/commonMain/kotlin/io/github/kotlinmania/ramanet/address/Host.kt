// port-lint: source rama-net/src/address/host.rs
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
    }

    open fun isName(): Boolean = false

    open fun isAddress(): Boolean = false

    open fun asDomain(): Domain? = null

    open fun asIp(): String? = null

    override fun compareTo(other: Host): Int = toString().compareTo(other.toString(), ignoreCase = true)

    companion object {
        fun localIpv4(): Host = Address(IpConstants.IPV4_LOCALHOST)

        fun localIpv6(): Host = Address(IpConstants.IPV6_LOCALHOST)

        fun defaultIpv4(): Host = Address(IpConstants.IPV4_UNSPECIFIED)

        fun defaultIpv6(): Host = Address(IpConstants.IPV6_UNSPECIFIED)

        fun example(): Host = Name(Domain.example())

        fun fromStatic(s: String): Host = parse(s)

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
