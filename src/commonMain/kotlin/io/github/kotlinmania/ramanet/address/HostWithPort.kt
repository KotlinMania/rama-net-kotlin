// port-lint: source address/host_with_port.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.NetProtocol
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A [Host] with an associated port.
 */
@Serializable(with = HostWithPortSerializer::class)
data class HostWithPort(
    val host: Host,
    val port: UShort,
) : Comparable<HostWithPort> {
    override fun toString(): String =
        when (host) {
            is Host.Address -> {
                if (ParseUtils.isValidIpv6(host.ip)) {
                    "[${host.ip}]:$port"
                } else {
                    "${host.ip}:$port"
                }
            }
            is Host.Name -> "${host.domain}:$port"
        }

    override fun compareTo(other: HostWithPort): Int {
        val hCmp = host.compareTo(other.host)
        if (hCmp != 0) return hCmp
        return port.compareTo(other.port)
    }

    fun withHost(host: Host): HostWithPort = copy(host = host)

    fun withPort(port: UShort): HostWithPort = copy(port = port)

    companion object {
        fun new(host: Host, port: UShort): HostWithPort = HostWithPort(host, port)

        fun localIpv4(port: UShort): HostWithPort = HostWithPort(Host.localIpv4(), port)

        fun localIpv6(port: UShort): HostWithPort = HostWithPort(Host.localIpv6(), port)

        fun defaultIpv4(port: UShort): HostWithPort = HostWithPort(Host.defaultIpv4(), port)

        fun defaultIpv6(port: UShort): HostWithPort = HostWithPort(Host.defaultIpv6(), port)

        fun broadcastIpv4(port: UShort): HostWithPort = HostWithPort(Host.broadcastIpv4(), port)

        fun exampleDomainHttp(): HostWithPort = exampleDomainWithPort(NetProtocol.HTTP_DEFAULT_PORT)

        fun exampleDomainHttps(): HostWithPort = exampleDomainWithPort(NetProtocol.HTTPS_DEFAULT_PORT)

        fun exampleDomainWithPort(port: UShort): HostWithPort = HostWithPort(Host.example(), port)

        fun localhostDomainHttp(): HostWithPort = localhostDomainWithPort(NetProtocol.HTTP_DEFAULT_PORT)

        fun localhostDomainHttps(): HostWithPort = localhostDomainWithPort(NetProtocol.HTTPS_DEFAULT_PORT)

        fun localhostDomainWithPort(port: UShort): HostWithPort = HostWithPort(Host.localhost(), port)

        fun localIpv4WithPort(port: UShort): HostWithPort = localIpv4(port)

        fun localIpv6WithPort(port: UShort): HostWithPort = localIpv6(port)

        fun exampleWithPort(port: UShort): HostWithPort = exampleDomainWithPort(port)

        fun exampleHttp(): HostWithPort = exampleDomainHttp()

        fun exampleHttps(): HostWithPort = exampleDomainHttps()

        fun parse(s: String): HostWithPort {
            val (rawHost, port) = ParseUtils.splitPortFromStr(s)
            val host = Host.parse(rawHost)
            if (host is Host.Address && ParseUtils.isValidIpv6(host.ip) && !s.startsWith('[')) {
                throw IllegalArgumentException("missing brackets for IPv6 address with port: $s")
            }
            return HostWithPort(host, port)
        }
    }
}

object HostWithPortSerializer : KSerializer<HostWithPort> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.HostWithPort", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HostWithPort) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): HostWithPort = HostWithPort.parse(decoder.decodeString())
}
