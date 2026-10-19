// port-lint: source address/socket_address.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.socket2.SockAddr
import io.github.kotlinmania.socket2.SocketAddr
import io.github.kotlinmania.socket2.toSockAddr
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * An IP address with an associated port.
 */
@Serializable(with = SocketAddressSerializer::class)
data class SocketAddress(
    val ip: String,
    val port: UShort,
) : Comparable<SocketAddress> {
    init {
        val valid = ParseUtils.tryToParseStrToIp(ip)
        require(valid != null) { "invalid IP address: $ip" }
    }

    fun isIpv4(): Boolean = !isIpv6()

    fun isIpv6(): Boolean = ParseUtils.isValidIpv6(ip)

    fun intoStd(): SocketAddr =
        if (isIpv6()) {
            SocketAddr.V6(ip, port.toInt())
        } else {
            SocketAddr.V4(ip, port.toInt())
        }

    fun toSockAddr(): SockAddr = intoStd().toSockAddr()

    override fun toString(): String =
        if (isIpv6()) {
            "[$ip]:$port"
        } else {
            "$ip:$port"
        }

    override fun compareTo(other: SocketAddress): Int {
        val ipCmp = ip.compareTo(other.ip, ignoreCase = true)
        if (ipCmp != 0) return ipCmp
        return port.compareTo(other.port)
    }

    fun ip(ipAddr: String): SocketAddress = withIp(ipAddr)

    fun ipv4(ipAddr: String): SocketAddress = withIp(ipAddr)

    fun ipv6(ipAddr: String): SocketAddress = withIp(ipAddr)

    fun port(port: UShort): SocketAddress = withPort(port)

    fun withIp(ipAddr: String): SocketAddress = copy(ip = ipAddr)

    fun withPort(port: UShort): SocketAddress = copy(port = port)

    fun fmt(): String = toString()

    companion object {
        fun new(ipAddr: String, port: UShort): SocketAddress = SocketAddress(ipAddr, port)

        fun fromStd(addr: SocketAddr): SocketAddress =
            when (addr) {
                is SocketAddr.V4 -> SocketAddress(addr.address, addr.port.toUShort())
                is SocketAddr.V6 -> SocketAddress(addr.address, addr.port.toUShort())
            }

        fun localIpv4(port: UShort = 8080u): SocketAddress = SocketAddress(IpConstants.IPV4_LOCALHOST, port)

        fun localIpv6(port: UShort = 8080u): SocketAddress = SocketAddress(IpConstants.IPV6_LOCALHOST, port)

        fun defaultIpv4(port: UShort = 8080u): SocketAddress = SocketAddress(IpConstants.IPV4_UNSPECIFIED, port)

        fun defaultIpv6(port: UShort = 8080u): SocketAddress = SocketAddress(IpConstants.IPV6_UNSPECIFIED, port)

        fun broadcastIpv4(port: UShort = 8080u): SocketAddress = SocketAddress(IpConstants.IPV4_BROADCAST, port)

        fun from(addr: SocketAddr): SocketAddress = fromStd(addr)

        fun from(ipAddr: String, port: UShort): SocketAddress = new(ipAddr, port)

        fun fmt(addr: SocketAddress): String = addr.toString()

        fun fromStr(s: String): SocketAddress = parse(s)

        fun tryFrom(s: String): SocketAddress = parse(s)

        fun tryFrom(bytes: ByteArray): SocketAddress = parse(bytes.decodeToString())

        fun parse(s: String): SocketAddress {
            val (rawHost, port) = ParseUtils.splitPortFromStr(s)
            val ip =
                ParseUtils.tryToParseStrToIp(rawHost)
                    ?: throw IllegalArgumentException("invalid IP address in socket address: $s")

            if (ParseUtils.isValidIpv6(ip) && !s.startsWith('[')) {
                throw IllegalArgumentException("missing brackets for IPv6 address with port: $s")
            }

            return SocketAddress(ip, port)
        }
    }
}

object SocketAddressSerializer : KSerializer<SocketAddress> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.SocketAddress", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SocketAddress) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): SocketAddress = SocketAddress.parse(decoder.decodeString())
}
