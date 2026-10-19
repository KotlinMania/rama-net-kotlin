// port-lint: tests address/proxy.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.user.credentials.Basic
import io.github.kotlinmania.ramanet.user.credentials.ProxyCredential
import kotlin.test.Test
import kotlin.test.assertEquals

class ProxyTest {
    @Test
    fun testValidProxy() {
        val addr = ProxyAddress.tryFrom("127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = null,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = null,
            ),
            addr,
        )
    }

    @Test
    fun testValidDomainProxy() {
        val addr = ProxyAddress.tryFrom("proxy.example.com:80")
        assertEquals(
            ProxyAddress(
                protocol = null,
                address =
                    HostWithPort.new(
                        Host.Name(Domain.fromStatic("proxy.example.com")),
                        80.toUShort(),
                    ),
                credential = null,
            ),
            addr,
        )
    }

    @Test
    fun testValidProxyWithCredential() {
        val addr = ProxyAddress.tryFrom("foo:bar@127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = null,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = ProxyCredential.from(Basic.new("foo", "bar")),
            ),
            addr,
        )
    }

    @Test
    fun testValidProxyWithInsecureCredential() {
        val addr = ProxyAddress.tryFrom("foo@127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = null,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = ProxyCredential.from(Basic.newInsecure("foo")),
            ),
            addr,
        )
    }

    @Test
    fun testValidHttpProxy() {
        val addr = ProxyAddress.tryFrom("http://127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.HTTP,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = null,
            ),
            addr,
        )
    }

    @Test
    fun testValidHttpProxyWithCredential() {
        val addr = ProxyAddress.tryFrom("http://foo:bar@127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.HTTP,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = ProxyCredential.from(Basic.new("foo", "bar")),
            ),
            addr,
        )
    }

    @Test
    fun testValidHttpProxyWithInsecureCredential() {
        val addr = ProxyAddress.tryFrom("http://foo@127.0.0.1:8080")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.HTTP,
                address = HostWithPort.localIpv4(8080.toUShort()),
                credential = ProxyCredential.from(Basic.newInsecure("foo")),
            ),
            addr,
        )
    }

    @Test
    fun testValidHttpsProxy() {
        val addr = ProxyAddress.tryFrom("https://foo-cc-be:baz@my.proxy.io.:9999")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.HTTPS,
                address =
                    HostWithPort.new(
                        Host.Name(Domain.fromStatic("my.proxy.io.")),
                        9999.toUShort(),
                    ),
                credential = ProxyCredential.from(Basic.new("foo-cc-be", "baz")),
            ),
            addr,
        )
    }

    @Test
    fun testValidHttpsProxyWithInsecureCredentials() {
        val addr = ProxyAddress.tryFrom("https://foo-cc-be@my.proxy.io.:9999")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.HTTPS,
                address =
                    HostWithPort.new(
                        Host.Name(Domain.fromStatic("my.proxy.io.")),
                        9999.toUShort(),
                    ),
                credential = ProxyCredential.from(Basic.newInsecure("foo-cc-be")),
            ),
            addr,
        )
    }

    @Test
    fun testValidSocks5hProxy() {
        val addr = ProxyAddress.tryFrom("socks5h://foo@[::1]:60000")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.SOCKS5H,
                address = HostWithPort.localIpv6(60000.toUShort()),
                credential = ProxyCredential.from(Basic.newInsecure("foo")),
            ),
            addr,
        )
    }

    @Test
    fun testValidSocks5hProxyTrailingColon() {
        val addr = ProxyAddress.tryFrom("socks5h://foo:@[::1]:60000")
        assertEquals(
            ProxyAddress(
                protocol = NetProtocol.SOCKS5H,
                address = HostWithPort.localIpv6(60000.toUShort()),
                credential = ProxyCredential.from(Basic.newInsecure("foo")),
            ),
            addr,
        )
    }

    @Test
    fun testValidProxyAddressSymmetric() {
        val cases =
            listOf(
                "http://proxy.io" to "http://proxy.io:80",
                "proxy.io:8080" to "proxy.io:8080",
                "127.0.0.1:8080" to "127.0.0.1:8080",
                "[::1]:8080" to "[::1]:8080",
                "socks5://proxy.io" to "socks5://proxy.io:1080",
                "socks5://proxy.io:8080" to "socks5://proxy.io:8080",
                "socks5://127.0.0.1" to "socks5://127.0.0.1:1080",
                "socks5://127.0.0.1:8080" to "socks5://127.0.0.1:8080",
                "socks5://::1" to "socks5://[::1]:1080",
                "socks5://[::1]:8080" to "socks5://[::1]:8080",
                "socks5://foo@proxy.io" to "socks5://foo@proxy.io:1080",
                "socks5://foo@proxy.io:8080" to "socks5://foo@proxy.io:8080",
                "socks5://foo@127.0.0.1" to "socks5://foo@127.0.0.1:1080",
                "socks5://foo@127.0.0.1:8080" to "socks5://foo@127.0.0.1:8080",
                "socks5://foo@::1" to "socks5://foo@[::1]:1080",
                "socks5://foo@[::1]:8080" to "socks5://foo@[::1]:8080",
                "socks5://foo:@127.0.0.1:8080" to "socks5://foo@127.0.0.1:8080",
                "socks5://foo:@::1" to "socks5://foo@[::1]:1080",
                "socks5://foo:@[::1]:8080" to "socks5://foo@[::1]:8080",
                "socks5://foo:bar@proxy.io" to "socks5://foo:bar@proxy.io:1080",
                "socks5://foo:bar@proxy.io:8080" to "socks5://foo:bar@proxy.io:8080",
                "socks5://foo:bar@127.0.0.1" to "socks5://foo:bar@127.0.0.1:1080",
                "socks5://foo:bar@127.0.0.1:8080" to "socks5://foo:bar@127.0.0.1:8080",
                "socks5://foo:bar@::1" to "socks5://foo:bar@[::1]:1080",
                "socks5://foo:bar@[::1]:8080" to "socks5://foo:bar@[::1]:8080",
            )
        for ((s, expected) in cases) {
            val addr = ProxyAddress.tryFrom(s)
            val out = addr.toString()
            assertEquals(expected, out, "addr: $addr")
        }
    }
}
