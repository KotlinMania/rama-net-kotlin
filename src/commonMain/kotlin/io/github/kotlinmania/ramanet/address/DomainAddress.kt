// port-lint: source address/domain_address.rs
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
 * A [Domain] with an associated port.
 *
 * Example: `example.com:80`
 */
@Serializable(with = DomainAddressSerializer::class)
public data class DomainAddress(
    public val domain: Domain,
    public val port: UShort,
) : Comparable<DomainAddress> {
    public fun domain(domain: Domain): DomainAddress = copy(domain = domain)

    public fun withDomain(domain: Domain): DomainAddress = copy(domain = domain)

    public fun port(port: UShort): DomainAddress = copy(port = port)

    public fun withPort(port: UShort): DomainAddress = copy(port = port)

    override fun toString(): String = "$domain:$port"

    override fun compareTo(other: DomainAddress): Int {
        val dCmp = domain.compareTo(other.domain)
        if (dCmp != 0) return dCmp
        return port.compareTo(other.port)
    }

    public companion object {
        public fun new(domain: Domain, port: UShort): DomainAddress = DomainAddress(domain, port)

        public fun new(domain: Domain, port: Int): DomainAddress = DomainAddress(domain, port.toUShort())

        public fun exampleHttp(): DomainAddress = DomainAddress(Domain.example(), NetProtocol.HTTP_DEFAULT_PORT)

        public fun exampleHttps(): DomainAddress = DomainAddress(Domain.example(), NetProtocol.HTTPS_DEFAULT_PORT)

        public fun exampleWithPort(port: UShort): DomainAddress = DomainAddress(Domain.example(), port)

        public fun localhostHttp(): DomainAddress = DomainAddress(Domain.tldLocalhost(), NetProtocol.HTTP_DEFAULT_PORT)

        public fun localhostHttps(): DomainAddress = DomainAddress(Domain.tldLocalhost(), NetProtocol.HTTPS_DEFAULT_PORT)

        public fun localhostWithPort(port: UShort): DomainAddress = DomainAddress(Domain.tldLocalhost(), port)

        public fun from(pair: Pair<Domain, UShort>): DomainAddress = DomainAddress(pair.first, pair.second)

        public fun parse(s: String): DomainAddress = fromStr(s)

        public fun fromStr(s: String): DomainAddress {
            val (domainStr, port) = ParseUtils.splitPortFromStr(s)
            val domain = Domain.fromStr(domainStr)
            return DomainAddress(domain, port)
        }

        public fun tryFrom(s: String): DomainAddress = fromStr(s)

        public fun tryFrom(bytes: ByteArray): DomainAddress = fromStr(bytes.decodeToString())
    }
}

public object DomainAddressSerializer : KSerializer<DomainAddress> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.DomainAddress", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DomainAddress) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): DomainAddress = DomainAddress.parse(decoder.decodeString())
}
