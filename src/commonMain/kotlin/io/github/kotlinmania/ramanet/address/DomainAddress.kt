// port-lint: source rama-net/src/address/domain_address.rs
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
data class DomainAddress(
    val domain: Domain,
    val port: UShort,
) : Comparable<DomainAddress> {
    override fun toString(): String = "$domain:$port"

    override fun compareTo(other: DomainAddress): Int {
        val dCmp = domain.compareTo(other.domain)
        if (dCmp != 0) return dCmp
        return port.compareTo(other.port)
    }

    companion object {
        fun exampleHttp(): DomainAddress = DomainAddress(Domain.example(), NetProtocol.HTTP_DEFAULT_PORT)

        fun exampleHttps(): DomainAddress = DomainAddress(Domain.example(), NetProtocol.HTTPS_DEFAULT_PORT)

        fun exampleWithPort(port: UShort): DomainAddress = DomainAddress(Domain.example(), port)

        fun localhostHttp(): DomainAddress = DomainAddress(Domain.tldLocalhost(), NetProtocol.HTTP_DEFAULT_PORT)

        fun localhostHttps(): DomainAddress = DomainAddress(Domain.tldLocalhost(), NetProtocol.HTTPS_DEFAULT_PORT)

        fun localhostWithPort(port: UShort): DomainAddress = DomainAddress(Domain.tldLocalhost(), port)

        fun parse(s: String): DomainAddress {
            val (domainStr, port) = ParseUtils.splitPortFromStr(s)
            val domain = Domain.parse(domainStr)
            return DomainAddress(domain, port)
        }
    }
}

object DomainAddressSerializer : KSerializer<DomainAddress> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.DomainAddress", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DomainAddress) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): DomainAddress = DomainAddress.parse(decoder.decodeString())
}
