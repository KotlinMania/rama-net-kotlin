// port-lint: source rama-net/src/address/proxy.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.user.ProxyCredential
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Address of a proxy that can be connected to.
 */
@Serializable(with = ProxyAddressSerializer::class)
data class ProxyAddress(
    val address: HostWithPort,
    val protocol: NetProtocol? = null,
    val credential: ProxyCredential? = null,
) {
    override fun toString(): String {
        val sb = StringBuilder()
        if (protocol != null) {
            sb.append(protocol.scheme).append("://")
        }
        if (credential != null) {
            when (credential) {
                is ProxyCredential.BasicAuth -> {
                    val basic = credential.basic
                    if (basic.password != null) {
                        sb
                            .append(basic.username)
                            .append(':')
                            .append(basic.password)
                            .append('@')
                    } else {
                        sb.append(basic.username).append('@')
                    }
                }
                is ProxyCredential.BearerAuth -> {
                    // Bearer tokens are ignored in ProxyAddress URL display per upstream spec
                }
            }
        }
        sb.append(address.toString())
        return sb.toString()
    }

    companion object {
        fun parse(s: String): ProxyAddress {
            val bytes = s.encodeToByteArray()
            val (protocol, consumed) = NetProtocol.tryToExtractProtocolFromUriScheme(bytes)
            val rest = if (consumed > 0) s.substring(consumed) else s

            val authority = Authority.parse(rest)
            val port =
                authority.address.port
                    ?: protocol?.defaultPort()
                    ?: throw IllegalArgumentException("proxy address contains no port or scheme with known port: $s")

            val hostWithPort = HostWithPort(authority.address.host, port)
            val credential = authority.userInfo?.let { ProxyCredential.BasicAuth(it) }

            return ProxyAddress(
                address = hostWithPort,
                protocol = protocol,
                credential = credential,
            )
        }
    }
}

object ProxyAddressSerializer : KSerializer<ProxyAddress> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.ProxyAddress", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ProxyAddress) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ProxyAddress = ProxyAddress.parse(decoder.decodeString())
}
