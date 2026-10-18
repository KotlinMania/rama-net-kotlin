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

    companion object {
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

        fun parse(s: String): SocketAddress {
            val (rawHost, port) = ParseUtils.splitPortFromStr(s)
            val isBracketed = rawHost.startsWith('[') && rawHost.endsWith(']')
            val cleanHost = if (isBracketed) rawHost.substring(1, rawHost.length - 1) else rawHost

            if (ParseUtils.isValidIpv6(cleanHost) && !isBracketed) {
                throw IllegalArgumentException("missing brackets for IPv6 address with port: $s")
            }

            val ip =
                ParseUtils.tryToParseStrToIp(cleanHost)
                    ?: throw IllegalArgumentException("invalid IP address in socket address: $s")
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
