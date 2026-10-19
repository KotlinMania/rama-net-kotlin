// port-lint: tests address/authority.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.ramanet.user.credentials.Basic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthorityTest {
    private fun assertEq(
        s: String,
        authority: Authority,
        userInfo: Basic?,
        host: String,
        port: UShort?,
    ) {
        assertEquals(userInfo, authority.userInfo, "parsing userInfo: $s")
        assertEquals(host, authority.address.host.toString(), "parsing host: $s")
        assertEquals(port, authority.address.port, "parsing port: $s")
    }

    @Test
    fun testParseValid() {
        val cases =
            listOf(
                "example.com" to Triple(null, "example.com", null),
                "user@example.com" to Triple(Basic.newInsecure("user"), "example.com", null),
                "user:password@example.com" to Triple(Basic.new("user", "password"), "example.com", null),
                "example.com:80" to Triple(null, "example.com", 80u.toUShort()),
                "user@example.com:80" to Triple(Basic.newInsecure("user"), "example.com", 80u.toUShort()),
                "user:secret@example.com:80" to Triple(Basic.new("user", "secret"), "example.com", 80u.toUShort()),
                "user@::1" to Triple(Basic.newInsecure("user"), "::1", null),
                "user:password@::1" to Triple(Basic.new("user", "password"), "::1", null),
                "::1" to Triple(null, "::1", null),
                "[::1]:80" to Triple(null, "::1", 80u.toUShort()),
                "user@[::1]:80" to Triple(Basic.newInsecure("user"), "::1", 80u.toUShort()),
                "user:password@[::1]:80" to Triple(Basic.new("user", "password"), "::1", 80u.toUShort()),
                "127.0.0.1" to Triple(null, "127.0.0.1", null),
                "user@127.0.0.1" to Triple(Basic.newInsecure("user"), "127.0.0.1", null),
                "user:password@127.0.0.1" to Triple(Basic.new("user", "password"), "127.0.0.1", null),
                "127.0.0.1:80" to Triple(null, "127.0.0.1", 80u.toUShort()),
                "user@127.0.0.1:80" to Triple(Basic.newInsecure("user"), "127.0.0.1", 80u.toUShort()),
                "user:secret@127.0.0.1:80" to Triple(Basic.new("user", "secret"), "127.0.0.1", 80u.toUShort()),
                "2001:db8:3333:4444:5555:6666:7777:8888" to
                    Triple(null, "2001:db8:3333:4444:5555:6666:7777:8888", null),
                "user@2001:db8:3333:4444:5555:6666:7777:8888" to
                    Triple(Basic.newInsecure("user"), "2001:db8:3333:4444:5555:6666:7777:8888", null),
                "user:secret@2001:db8:3333:4444:5555:6666:7777:8888" to
                    Triple(Basic.new("user", "secret"), "2001:db8:3333:4444:5555:6666:7777:8888", null),
                "[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    Triple(null, "2001:db8:3333:4444:5555:6666:7777:8888", 80u.toUShort()),
                "user@[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    Triple(Basic.newInsecure("user"), "2001:db8:3333:4444:5555:6666:7777:8888", 80u.toUShort()),
                "user:secret@[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    Triple(Basic.new("user", "secret"), "2001:db8:3333:4444:5555:6666:7777:8888", 80u.toUShort()),
            )

        for ((s, expected) in cases) {
            val (expectedUserInfo, expectedHost, expectedPort) = expected
            val parsed = Authority.parse(s)
            assertEq(s, parsed, expectedUserInfo, expectedHost, expectedPort)
            assertEq(s, Authority.tryFrom(s), expectedUserInfo, expectedHost, expectedPort)
            assertEq(s, Authority.fromStr(s), expectedUserInfo, expectedHost, expectedPort)
            assertEq(s, Authority.tryFrom(s.encodeToByteArray()), expectedUserInfo, expectedHost, expectedPort)
        }
    }

    @Test
    fun testParseInvalid() {
        val invalidCases =
            listOf(
                "",
                "-",
                ".",
                ":",
                ":80",
                "-.",
                ".-",
                "[::1]",
                "[2001:db8:3333:4444:5555:6666:7777:8888",
                "2001:db8:3333:4444:5555:6666:7777:8888]",
                "[2001:db8:3333:4444:5555:6666:7777:8888]",
                "example.com:",
                "example.com:-1",
                "example.com:999999",
                "example:com",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
                ":foo@80",
                ":foo@example.com",
                ":foo@127.0.0.1",
                ":foo@example.com:80",
                ":foo@127.0.0.1:80",
                ":foo@:80",
            )

        for (s in invalidCases) {
            assertFailsWith<Exception>("parsing '$s'") {
                Authority.parse(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom") {
                Authority.tryFrom(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom(bytes)") {
                Authority.tryFrom(s.encodeToByteArray())
            }
        }
    }

    @Test
    fun testParseDisplay() {
        val cases =
            listOf(
                "example.com" to "example.com",
                "user@example.com" to "user@example.com",
                "user:secret@example.com" to "user:secret@example.com",
                "example.com:80" to "example.com:80",
                "user@example.com:80" to "user@example.com:80",
                "user:secret@example.com:80" to "user:secret@example.com:80",
                "[::1]:80" to "[::1]:80",
                "user@[::1]:80" to "user@[::1]:80",
                "secret:user@[::1]:80" to "secret:user@[::1]:80",
                "::1" to "::1",
                "user@::1" to "user@::1",
                "user:secret@::1" to "user:secret@::1",
                "127.0.0.1:80" to "127.0.0.1:80",
                "user@127.0.0.1:80" to "user@127.0.0.1:80",
                "user:secret@127.0.0.1:80" to "user:secret@127.0.0.1:80",
                "127.0.0.1" to "127.0.0.1",
                "user@127.0.0.1" to "user@127.0.0.1",
                "user:secret@127.0.0.1" to "user:secret@127.0.0.1",
            )

        for ((s, expected) in cases) {
            val authority = Authority.parse(s)
            assertEquals(expected, authority.toString(), "parsing: $s")
        }
    }

    @Test
    fun testFactoryConstructors() {
        val local4 = Authority.localIpv4()
        assertEquals("127.0.0.1", local4.toString())

        val local4Port = Authority.localIpv4WithPort(8080u)
        assertEquals("127.0.0.1:8080", local4Port.toString())

        val local6 = Authority.localIpv6()
        assertEquals("::1", local6.toString())

        val local6Port = Authority.localIpv6WithPort(8080u)
        assertEquals("[::1]:8080", local6Port.toString())

        val default4 = Authority.defaultIpv4()
        assertEquals("0.0.0.0", default4.toString())

        val default4Port = Authority.defaultIpv4WithPort(8080u)
        assertEquals("0.0.0.0:8080", default4Port.toString())

        val default6 = Authority.defaultIpv6()
        assertEquals("::", default6.toString())

        val default6Port = Authority.defaultIpv6WithPort(8080u)
        assertEquals("[::]:8080", default6Port.toString())

        val bcast4 = Authority.broadcastIpv4()
        assertEquals("255.255.255.255", bcast4.toString())

        val bcast4Port = Authority.broadcastIpv4WithPort(8080u)
        assertEquals("255.255.255.255:8080", bcast4Port.toString())

        val ex = Authority.exampleDomain()
        assertEquals("example.com", ex.toString())

        val exHttp = Authority.exampleDomainHttp()
        assertEquals("example.com:80", exHttp.toString())

        val exHttps = Authority.exampleDomainHttps()
        assertEquals("example.com:443", exHttps.toString())

        val exPort = Authority.exampleDomainWithPort(8080u)
        assertEquals("example.com:8080", exPort.toString())

        val lh = Authority.localhostDomain()
        assertEquals("localhost", lh.toString())

        val lhHttp = Authority.localhostDomainHttp()
        assertEquals("localhost:80", lhHttp.toString())

        val lhHttps = Authority.localhostDomainHttps()
        assertEquals("localhost:443", lhHttps.toString())

        val lhPort = Authority.localhostDomainWithPort(8080u)
        assertEquals("localhost:8080", lhPort.toString())
    }
}
