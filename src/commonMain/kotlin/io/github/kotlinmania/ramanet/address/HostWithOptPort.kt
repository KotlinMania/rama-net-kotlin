// port-lint: source address/host_with_opt_port.rs
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
 * A [Host] with an optional port.
 */
@Serializable(with = HostWithOptPortSerializer::class)
data class HostWithOptPort(
    val host: Host,
    val port: UShort? = null,
) : Comparable<HostWithOptPort> {
    override fun toString(): String =
        if (port != null) {
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
        } else {
            host.toString()
        }

    override fun compareTo(other: HostWithOptPort): Int {
        val hCmp = host.compareTo(other.host)
        if (hCmp != 0) return hCmp
        return (port ?: 0u).compareTo(other.port ?: 0u)
    }

    companion object {
        fun localIpv4(): HostWithOptPort = HostWithOptPort(Host.localIpv4())

        fun localIpv4WithPort(port: UShort): HostWithOptPort = HostWithOptPort(Host.localIpv4(), port)

        fun localIpv6(): HostWithOptPort = HostWithOptPort(Host.localIpv6())

        fun localIpv6WithPort(port: UShort): HostWithOptPort = HostWithOptPort(Host.localIpv6(), port)

        fun example(): HostWithOptPort = HostWithOptPort(Host.example())

        fun exampleWithPort(port: UShort): HostWithOptPort = HostWithOptPort(Host.example(), port)

        fun exampleHttp(): HostWithOptPort = HostWithOptPort(Host.example(), NetProtocol.HTTP_DEFAULT_PORT)

        fun exampleHttps(): HostWithOptPort = HostWithOptPort(Host.example(), NetProtocol.HTTPS_DEFAULT_PORT)

        fun parse(s: String): HostWithOptPort {
            if (s.isEmpty()) {
                throw IllegalArgumentException("cannot parse empty string to HostWithOptPort")
            }

            // Case 1: Bracketed IPv6 e.g. [::1] or [::1]:8080
            if (s.startsWith('[')) {
                val closeBracket = s.indexOf(']')
                if (closeBracket < 0) {
                    throw IllegalArgumentException("unclosed bracket in IPv6 address: $s")
                }
                val ip = s.substring(1, closeBracket)
                if (!ParseUtils.isValidIpv6(ip)) {
                    throw IllegalArgumentException("invalid IPv6 in brackets: $ip")
                }
                val remaining = s.substring(closeBracket + 1)
                val port =
                    if (remaining.isNotEmpty()) {
                        if (!remaining.startsWith(':')) {
                            throw IllegalArgumentException("expected ':' after bracketed IPv6: $s")
                        }
                        remaining.substring(1).toUShortOrNull()
                            ?: throw IllegalArgumentException("invalid port in address: $s")
                    } else {
                        null
                    }
                return HostWithOptPort(Host.Address(ip), port)
            }

            // Case 2: Unbracketed IPv6 without port e.g. 2001:db8::1
            if (s.contains(':') && s.indexOf(':') != s.lastIndexOf(':')) {
                if (ParseUtils.isValidIpv6(s)) {
                    return HostWithOptPort(Host.Address(s), null)
                }
            }

            // Case 3: Contains single ':' -> host:port (IPv4 or domain)
            if (s.contains(':')) {
                val colon = s.lastIndexOf(':')
                val hostPart = s.substring(0, colon)
                val portPart = s.substring(colon + 1)
                val port =
                    portPart.toUShortOrNull()
                        ?: throw IllegalArgumentException("invalid port in address: $s")
                val host = Host.parse(hostPart)
                return HostWithOptPort(host, port)
            }

            // Case 4: No colon -> Host without port
            val host = Host.parse(s)
            return HostWithOptPort(host, null)
        }
    }
}

object HostWithOptPortSerializer : KSerializer<HostWithOptPort> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.HostWithOptPort", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HostWithOptPort) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): HostWithOptPort = HostWithOptPort.parse(decoder.decodeString())
}
