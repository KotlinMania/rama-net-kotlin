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

    companion object {
        fun localIpv4WithPort(port: UShort): HostWithPort = HostWithPort(Host.localIpv4(), port)

        fun localIpv6WithPort(port: UShort): HostWithPort = HostWithPort(Host.localIpv6(), port)

        fun exampleWithPort(port: UShort): HostWithPort = HostWithPort(Host.example(), port)

        fun exampleHttp(): HostWithPort = HostWithPort(Host.example(), NetProtocol.HTTP_DEFAULT_PORT)

        fun exampleHttps(): HostWithPort = HostWithPort(Host.example(), NetProtocol.HTTPS_DEFAULT_PORT)

        fun parse(s: String): HostWithPort {
            val (rawHost, port) = ParseUtils.splitPortFromStr(s)
            val isBracketed = rawHost.startsWith('[') && rawHost.endsWith(']')
            val cleanHost = if (isBracketed) rawHost.substring(1, rawHost.length - 1) else rawHost

            if (ParseUtils.isValidIpv6(cleanHost) && !isBracketed) {
                throw IllegalArgumentException("missing brackets for IPv6 address with port: $s")
            }

            val host = Host.parse(cleanHost)
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
