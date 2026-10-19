// port-lint: source address/authority.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.user.credentials.Basic
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

    fun host(host: Host): Authority = copy(address = address.withHost(host))

    fun port(port: UShort?): Authority = copy(address = address.withPort(port))

    fun userInfo(userInfo: Basic?): Authority = copy(userInfo = userInfo)

    fun withHost(host: Host): Authority = host(host)

    fun withPort(port: UShort?): Authority = port(port)

    fun withUserInfo(userInfo: Basic?): Authority = userInfo(userInfo)

    fun fmt(): String = toString()

    companion object {
        fun new(addr: HostWithOptPort): Authority = Authority(addr)

        fun newWithUserInfo(addr: HostWithOptPort, userInfo: Basic): Authority = Authority(addr, userInfo)

        fun localIpv4(): Authority = new(HostWithOptPort.localIpv4())

        fun localIpv4WithPort(port: UShort): Authority = new(HostWithOptPort.localIpv4WithPort(port))

        fun localIpv6(): Authority = new(HostWithOptPort.localIpv6())

        fun localIpv6WithPort(port: UShort): Authority = new(HostWithOptPort.localIpv6WithPort(port))

        fun defaultIpv4(): Authority = new(HostWithOptPort.defaultIpv4())

        fun defaultIpv4WithPort(port: UShort): Authority = new(HostWithOptPort.defaultIpv4WithPort(port))

        fun defaultIpv6(): Authority = new(HostWithOptPort.defaultIpv6())

        fun defaultIpv6WithPort(port: UShort): Authority = new(HostWithOptPort.defaultIpv6WithPort(port))

        fun broadcastIpv4(): Authority = new(HostWithOptPort.broadcastIpv4())

        fun broadcastIpv4WithPort(port: UShort): Authority = new(HostWithOptPort.broadcastIpv4WithPort(port))

        fun exampleDomain(): Authority = new(HostWithOptPort.exampleDomain())

        fun exampleDomainHttp(): Authority = new(HostWithOptPort.exampleDomainHttp())

        fun exampleDomainHttps(): Authority = new(HostWithOptPort.exampleDomainHttps())

        fun exampleDomainWithPort(port: UShort): Authority = new(HostWithOptPort.exampleDomainWithPort(port))

        fun localhostDomain(): Authority = new(HostWithOptPort.localhostDomain())

        fun localhostDomainHttp(): Authority = new(HostWithOptPort.localhostDomainHttp())

        fun localhostDomainHttps(): Authority = new(HostWithOptPort.localhostDomainHttps())

        fun localhostDomainWithPort(port: UShort): Authority = new(HostWithOptPort.localhostDomainWithPort(port))

        fun example(): Authority = exampleDomain()

        fun exampleWithPort(port: UShort): Authority = exampleDomainWithPort(port)

        fun exampleHttp(): Authority = exampleDomainHttp()

        fun exampleHttps(): Authority = exampleDomainHttps()

        fun from(domain: Domain, port: UShort): Authority = Authority(HostWithOptPort.from(domain, port))

        fun from(ip: String, port: UShort): Authority = Authority(HostWithOptPort.from(ip, port))

        fun from(host: Host): Authority = new(HostWithOptPort.new(host))

        fun from(host: Host, port: UShort): Authority = new(HostWithOptPort.newWithPort(host, port))

        fun from(hostWithPort: HostWithPort): Authority = new(HostWithOptPort.from(hostWithPort))

        fun from(socketAddress: SocketAddress): Authority = new(HostWithOptPort.from(socketAddress))

        fun from(domainAddress: DomainAddress): Authority = new(HostWithOptPort.from(domainAddress))

        fun from(addr: HostWithOptPort): Authority = new(addr)

        fun fmt(authority: Authority): String = authority.toString()

        fun fromStr(s: String): Authority = parse(s)

        fun tryFrom(s: String): Authority = parse(s)

        fun tryFrom(bytes: ByteArray): Authority = parse(bytes.decodeToString())

        fun tryFromMaybeBorrowedStr(s: String): Authority = parse(s)

        fun parse(s: String): Authority {
            if (s.isEmpty()) {
                throw IllegalArgumentException("empty string is invalid authority")
            }
            val atIdx = s.indexOf('@')
            var userInfo: Basic? = null
            var rest = s
            if (atIdx >= 0) {
                val userStr = s.substring(0, atIdx)
                if (userStr.isEmpty()) {
                    throw IllegalArgumentException("empty user info in authority: $s")
                }
                userInfo = Basic.parse(userStr)
                rest = s.substring(atIdx + 1)
            }
            val address = HostWithOptPort.parse(rest)
            return Authority(address, userInfo)
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
