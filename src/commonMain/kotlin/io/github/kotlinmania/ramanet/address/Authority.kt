// port-lint: source rama-net/src/address/authority.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.user.Basic
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A [Host] with optionally a port and/or user-info ([Basic]).
 *
 * Examples:
 * - `example.com`
 * - `127.0.0.1`
 * - `example.com:80`
 * - `127.0.0.1:80`
 * - `joe@example.com:80`
 * - `joe:secret@example.com`
 */
@Serializable(with = AuthoritySerializer::class)
data class Authority(
    val address: HostWithOptPort,
    val userInfo: Basic? = null,
) : Comparable<Authority> {
    override fun toString(): String =
        if (userInfo != null) {
            val userPart =
                if (userInfo.password != null) {
                    "${userInfo.username}:${userInfo.password}@"
                } else {
                    "${userInfo.username}@"
                }
            "$userPart$address"
        } else {
            address.toString()
        }

    override fun compareTo(other: Authority): Int {
        val aCmp = address.compareTo(other.address)
        if (aCmp != 0) return aCmp
        return (userInfo?.username ?: "").compareTo(other.userInfo?.username ?: "")
    }

    companion object {
        fun localIpv4(): Authority = Authority(HostWithOptPort.localIpv4())

        fun localIpv4WithPort(port: UShort): Authority = Authority(HostWithOptPort.localIpv4WithPort(port))

        fun localIpv6(): Authority = Authority(HostWithOptPort.localIpv6())

        fun localIpv6WithPort(port: UShort): Authority = Authority(HostWithOptPort.localIpv6WithPort(port))

        fun example(): Authority = Authority(HostWithOptPort.example())

        fun exampleWithPort(port: UShort): Authority = Authority(HostWithOptPort.exampleWithPort(port))

        fun exampleHttp(): Authority = Authority(HostWithOptPort.exampleHttp())

        fun exampleHttps(): Authority = Authority(HostWithOptPort.exampleHttps())

        fun parse(s: String): Authority {
            val atIdx = s.lastIndexOf('@')
            return if (atIdx >= 0) {
                val userStr = s.substring(0, atIdx)
                val addrStr = s.substring(atIdx + 1)
                val userInfo = Basic.parse(userStr)
                val address = HostWithOptPort.parse(addrStr)
                Authority(address, userInfo)
            } else {
                val address = HostWithOptPort.parse(s)
                Authority(address, null)
            }
        }
    }
}

object AuthoritySerializer : KSerializer<Authority> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.Authority", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Authority) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Authority = Authority.parse(decoder.decodeString())
}
