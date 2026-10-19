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

    fun host(host: Host): HostWithOptPort = withHost(host)

    fun port(port: UShort?): HostWithOptPort = withPort(port)

    fun withHost(host: Host): HostWithOptPort = copy(host = host)

    fun withPort(port: UShort?): HostWithOptPort = copy(port = port)

    fun fmt(): String = toString()

    companion object {
        fun new(host: Host): HostWithOptPort = HostWithOptPort(host, null)

        fun newWithPort(host: Host, port: UShort): HostWithOptPort = HostWithOptPort(host, port)

        fun localIpv4(): HostWithOptPort = new(Host.localIpv4())

        fun localIpv4WithPort(port: UShort): HostWithOptPort = newWithPort(Host.localIpv4(), port)

        fun localIpv6(): HostWithOptPort = new(Host.localIpv6())

        fun localIpv6WithPort(port: UShort): HostWithOptPort = newWithPort(Host.localIpv6(), port)

        fun defaultIpv4(): HostWithOptPort = new(Host.defaultIpv4())

        fun defaultIpv4WithPort(port: UShort): HostWithOptPort = newWithPort(Host.defaultIpv4(), port)

        fun defaultIpv6(): HostWithOptPort = new(Host.defaultIpv6())

        fun defaultIpv6WithPort(port: UShort): HostWithOptPort = newWithPort(Host.defaultIpv6(), port)

        fun broadcastIpv4(): HostWithOptPort = new(Host.broadcastIpv4())

        fun broadcastIpv4WithPort(port: UShort): HostWithOptPort = newWithPort(Host.broadcastIpv4(), port)

        fun exampleDomain(): HostWithOptPort = HostWithOptPort(Host.example(), null)

        fun exampleDomainHttp(): HostWithOptPort = exampleDomainWithPort(NetProtocol.HTTP_DEFAULT_PORT)

        fun exampleDomainHttps(): HostWithOptPort = exampleDomainWithPort(NetProtocol.HTTPS_DEFAULT_PORT)

        fun exampleDomainWithPort(port: UShort): HostWithOptPort = HostWithOptPort(Host.example(), port)

        fun localhostDomain(): HostWithOptPort = HostWithOptPort(Host.localhost(), null)

        fun localhostDomainHttp(): HostWithOptPort = localhostDomainWithPort(NetProtocol.HTTP_DEFAULT_PORT)

        fun localhostDomainHttps(): HostWithOptPort = localhostDomainWithPort(NetProtocol.HTTPS_DEFAULT_PORT)

        fun localhostDomainWithPort(port: UShort): HostWithOptPort = HostWithOptPort(Host.localhost(), port)

        fun example(): HostWithOptPort = exampleDomain()

        fun exampleWithPort(port: UShort): HostWithOptPort = exampleDomainWithPort(port)

        fun exampleHttp(): HostWithOptPort = exampleDomainHttp()

        fun exampleHttps(): HostWithOptPort = exampleDomainHttps()

        fun from(domain: Domain, port: UShort): HostWithOptPort = HostWithOptPort(Host.Name(domain), port)

        fun from(ip: String, port: UShort): HostWithOptPort = HostWithOptPort(Host.Address(ip), port)

        fun from(host: Host): HostWithOptPort = new(host)

        fun from(host: Host, port: UShort): HostWithOptPort = newWithPort(host, port)

        fun from(hostWithPort: HostWithPort): HostWithOptPort = newWithPort(hostWithPort.host, hostWithPort.port)

        fun from(socketAddress: SocketAddress): HostWithOptPort =
            newWithPort(Host.Address(socketAddress.ip), socketAddress.port)

        fun from(domainAddress: DomainAddress): HostWithOptPort =
            newWithPort(Host.Name(domainAddress.domain), domainAddress.port)

        fun fmt(hwop: HostWithOptPort): String = hwop.toString()

        fun fromStr(s: String): HostWithOptPort = parse(s)

        fun tryFrom(s: String): HostWithOptPort = parse(s)

        fun tryFrom(bytes: ByteArray): HostWithOptPort = parse(bytes.decodeToString())

        fun tryFromMaybeBorrowedStr(s: String): HostWithOptPort = parse(s)

        fun parse(s: String): HostWithOptPort {
            if (s.isEmpty()) {
                throw IllegalArgumentException("empty string is invalid host (with opt port)")
            }

            val lastColon = s.lastIndexOf(':')
            if (lastColon >= 0) {
                val firstPart = s.substring(0, lastColon)
                if (firstPart.contains(':')) {
                    // ipv6
                    if (firstPart.startsWith('[') && firstPart.endsWith(']')) {
                        val value = firstPart.substring(1, firstPart.length - 1)
                        if (!ParseUtils.isValidIpv6(value)) {
                            throw IllegalArgumentException("invalid ipv6 with brackets: $value")
                        }
                        val portStr = s.substring(lastColon + 1)
                        val port =
                            portStr.toUShortOrNull()
                                ?: throw IllegalArgumentException("parse host-with-opt-port port string as u16: $portStr")
                        return HostWithOptPort(Host.Address(value), port)
                    } else {
                        if (!ParseUtils.isValidIpv6(s)) {
                            throw IllegalArgumentException("invalid ipv6 without trailing port: $s")
                        }
                        return HostWithOptPort(Host.Address(s), null)
                    }
                } else {
                    val portStr = s.substring(lastColon + 1)
                    val port =
                        portStr.toUShortOrNull()
                            ?: throw IllegalArgumentException("parse host-with-opt-port port string as u16: $portStr")

                    val host =
                        if (ParseUtils.isValidIpv4(firstPart)) {
                            Host.Address(firstPart)
                        } else {
                            Host.Name(Domain.parse(firstPart))
                        }
                    return HostWithOptPort(host, port)
                }
            } else {
                val host =
                    if (ParseUtils.isValidIpv4(s) || ParseUtils.isValidIpv6(s)) {
                        Host.Address(s)
                    } else {
                        Host.Name(Domain.parse(s))
                    }
                return HostWithOptPort(host, null)
            }
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
