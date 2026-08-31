// port-lint: tests rama-net/src/address/mod.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.user.Basic
import io.github.kotlinmania.ramanet.user.ProxyCredential
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AddressTest {
    @Test
    fun testDomain() {
        val domain = Domain.parse("example.com")
        assertEquals("example.com", domain.asString())
        assertFalse(domain.isFqdn())
        assertFalse(domain.isWildcard())

        val fqdn = Domain.parse("example.com.")
        assertTrue(fqdn.isFqdn())
        assertEquals("example.com.", fqdn.asString())

        val wildcard = Domain.parse("*.example.com")
        assertTrue(wildcard.isWildcard())
        assertEquals(domain, wildcard.asWildcardParent())

        val sub = domain.tryAsSub("api")
        assertEquals("api.example.com", sub.asString())
        assertTrue(sub.isSubOf(domain))
        assertTrue(domain.isParentOf(sub))

        assertEquals("com", domain.suffix())
        assertTrue(domain.isSld())
    }

    @Test
    fun testHost() {
        val hostDomain = Host.parse("example.com")
        assertTrue(hostDomain.isName())
        assertFalse(hostDomain.isAddress())
        assertEquals("example.com", hostDomain.asDomain()?.asString())

        val hostIp4 = Host.parse("127.0.0.1")
        assertTrue(hostIp4.isAddress())
        assertEquals("127.0.0.1", hostIp4.asIp())

        val hostIp6 = Host.parse("::1")
        assertTrue(hostIp6.isAddress())
        assertEquals("::1", hostIp6.asIp())
    }

    @Test
    fun testSocketAddress() {
        val saIpv4 = SocketAddress.parse("127.0.0.1:80")
        assertEquals("127.0.0.1", saIpv4.ip)
        assertEquals(80u.toUShort(), saIpv4.port)
        assertEquals("127.0.0.1:80", saIpv4.toString())

        val saIpv6 = SocketAddress.parse("[::1]:443")
        assertEquals("::1", saIpv6.ip)
        assertEquals(443u.toUShort(), saIpv6.port)
        assertEquals("[::1]:443", saIpv6.toString())

        assertFailsWith<IllegalArgumentException> {
            SocketAddress.parse("::1:80") // Missing brackets for IPv6 with port
        }
    }

    @Test
    fun testDomainAddress() {
        val da = DomainAddress.parse("example.com:8080")
        assertEquals("example.com", da.domain.asString())
        assertEquals(8080u.toUShort(), da.port)
        assertEquals("example.com:8080", da.toString())
    }

    @Test
    fun testHostWithPort() {
        val hpDomain = HostWithPort.parse("example.com:443")
        assertEquals("example.com:443", hpDomain.toString())

        val hpIpv6 = HostWithPort.parse("[::1]:80")
        assertEquals("[::1]:80", hpIpv6.toString())
    }

    @Test
    fun testHostWithOptPort() {
        val hopDomainNoPort = HostWithOptPort.parse("example.com")
        assertNull(hopDomainNoPort.port)
        assertEquals("example.com", hopDomainNoPort.toString())

        val hopDomainWithPort = HostWithOptPort.parse("example.com:80")
        assertEquals(80u.toUShort(), hopDomainWithPort.port)
        assertEquals("example.com:80", hopDomainWithPort.toString())

        val hopIpv6NoPort = HostWithOptPort.parse("[::1]")
        assertNull(hopIpv6NoPort.port)

        val hopIpv6WithPort = HostWithOptPort.parse("[::1]:8080")
        assertEquals(8080u.toUShort(), hopIpv6WithPort.port)
        assertEquals("[::1]:8080", hopIpv6WithPort.toString())
    }

    @Test
    fun testAuthority() {
        val auth1 = Authority.parse("example.com:80")
        assertNull(auth1.userInfo)
        assertEquals("example.com:80", auth1.toString())

        val auth2 = Authority.parse("joe:secret@example.com:80")
        assertEquals(Basic("joe", "secret"), auth2.userInfo)
        assertEquals("joe:secret@example.com:80", auth2.toString())

        val auth3 = Authority.parse("joe@example.com")
        assertEquals(Basic("joe", null), auth3.userInfo)
        assertEquals("joe@example.com", auth3.toString())
    }

    @Test
    fun testProxyAddress() {
        val proxy = ProxyAddress.parse("http://user:pass@127.0.0.1:8080")
        assertEquals(NetProtocol.Http, proxy.protocol)
        assertEquals(ProxyCredential.BasicAuth(Basic("user", "pass")), proxy.credential)
        assertEquals(8080u.toUShort(), proxy.address.port)
        assertEquals("http://user:pass@127.0.0.1:8080", proxy.toString())

        val proxyDefaultPort = ProxyAddress.parse("https://proxy.example.com")
        assertEquals(NetProtocol.Https, proxyDefaultPort.protocol)
        assertEquals(NetProtocol.HTTPS_DEFAULT_PORT, proxyDefaultPort.address.port)
    }

    @Test
    fun testDomainTrie() {
        val trie = DomainTrie<String>()
        assertTrue(trie.isEmpty())

        trie.insert(Domain.parse("example.com"), "val_example")
        trie.insert(Domain.parse("api.example.com"), "val_api")
        trie.insert(Domain.parse("org"), "val_org")

        assertEquals(3, trie.size)
        assertEquals("val_example", trie.get(Domain.parse("example.com")))
        assertEquals("val_api", trie.get(Domain.parse("api.example.com")))
        assertNull(trie.get(Domain.parse("other.com")))

        val match1 = trie.matchParent(Domain.parse("v1.api.example.com"))
        assertEquals("val_api", match1?.value)
        assertFalse(match1?.isExact ?: true)

        val match2 = trie.matchParent(Domain.parse("example.com"))
        assertEquals("val_example", match2?.value)
        assertTrue(match2?.isExact ?: false)
    }
}
