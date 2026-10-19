// port-lint: tests address/host_with_opt_port.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HostWithOptPortTest {
    private fun assertEq(
        s: String,
        hwop: HostWithOptPort,
        expectedHost: String,
        expectedPort: UShort?,
    ) {
        assertEquals(expectedHost, hwop.host.toString(), "parsing: $s")
        assertEquals(expectedPort, hwop.port, "parsing: $s")
    }

    @Test
    fun testParseValid() {
        val cases =
            listOf(
                "example.com" to ("example.com" to null),
                "example.com:80" to ("example.com" to 80u.toUShort()),
                "::1" to ("::1" to null),
                "[::1]:80" to ("::1" to 80u.toUShort()),
                "127.0.0.1" to ("127.0.0.1" to null),
                "127.0.0.1:80" to ("127.0.0.1" to 80u.toUShort()),
                "2001:db8:3333:4444:5555:6666:7777:8888" to ("2001:db8:3333:4444:5555:6666:7777:8888" to null),
                "[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    ("2001:db8:3333:4444:5555:6666:7777:8888" to 80u.toUShort()),
            )

        for ((s, expected) in cases) {
            val (expectedHost, expectedPort) = expected
            val parsed = HostWithOptPort.parse(s)
            assertEq(s, parsed, expectedHost, expectedPort)
            assertEq(s, HostWithOptPort.tryFrom(s), expectedHost, expectedPort)
            assertEq(s, HostWithOptPort.fromStr(s), expectedHost, expectedPort)
            assertEq(s, HostWithOptPort.tryFrom(s.encodeToByteArray()), expectedHost, expectedPort)
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
                "2001:db8:3333:4444:5555:6666:7777:8888]",
                "[2001:db8:3333:4444:5555:6666:7777:8888]",
                "[2001:db8:3333:4444:5555:6666:7777:8888",
                "example.com:",
                "example.com:-1",
                "example.com:999999",
                "example:com",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
            )

        for (s in invalidCases) {
            assertFailsWith<Exception>("parsing '$s'") {
                HostWithOptPort.parse(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom") {
                HostWithOptPort.tryFrom(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom(bytes)") {
                HostWithOptPort.tryFrom(s.encodeToByteArray())
            }
        }
    }

    @Test
    fun testParseDisplay() {
        val cases =
            listOf(
                "example.com" to "example.com",
                "example.com:80" to "example.com:80",
                "[::1]:80" to "[::1]:80",
                "::1" to "::1",
                "127.0.0.1:80" to "127.0.0.1:80",
                "127.0.0.1" to "127.0.0.1",
            )

        for ((s, expected) in cases) {
            val hwop = HostWithOptPort.parse(s)
            assertEquals(expected, hwop.toString(), "parsing: $s")
        }
    }

    @Test
    fun testFactoryConstructors() {
        val local4 = HostWithOptPort.localIpv4()
        assertEquals("127.0.0.1", local4.toString())

        val local4Port = HostWithOptPort.localIpv4WithPort(8080u)
        assertEquals("127.0.0.1:8080", local4Port.toString())

        val local6 = HostWithOptPort.localIpv6()
        assertEquals("::1", local6.toString())

        val local6Port = HostWithOptPort.localIpv6WithPort(8080u)
        assertEquals("[::1]:8080", local6Port.toString())

        val default4 = HostWithOptPort.defaultIpv4()
        assertEquals("0.0.0.0", default4.toString())

        val default4Port = HostWithOptPort.defaultIpv4WithPort(8080u)
        assertEquals("0.0.0.0:8080", default4Port.toString())

        val default6 = HostWithOptPort.defaultIpv6()
        assertEquals("::", default6.toString())

        val default6Port = HostWithOptPort.defaultIpv6WithPort(8080u)
        assertEquals("[::]:8080", default6Port.toString())

        val bcast4 = HostWithOptPort.broadcastIpv4()
        assertEquals("255.255.255.255", bcast4.toString())

        val bcast4Port = HostWithOptPort.broadcastIpv4WithPort(8080u)
        assertEquals("255.255.255.255:8080", bcast4Port.toString())

        val ex = HostWithOptPort.exampleDomain()
        assertEquals("example.com", ex.toString())

        val exHttp = HostWithOptPort.exampleDomainHttp()
        assertEquals("example.com:80", exHttp.toString())

        val exHttps = HostWithOptPort.exampleDomainHttps()
        assertEquals("example.com:443", exHttps.toString())

        val exPort = HostWithOptPort.exampleDomainWithPort(8080u)
        assertEquals("example.com:8080", exPort.toString())

        val lh = HostWithOptPort.localhostDomain()
        assertEquals("localhost", lh.toString())

        val lhHttp = HostWithOptPort.localhostDomainHttp()
        assertEquals("localhost:80", lhHttp.toString())

        val lhHttps = HostWithOptPort.localhostDomainHttps()
        assertEquals("localhost:443", lhHttps.toString())

        val lhPort = HostWithOptPort.localhostDomainWithPort(8080u)
        assertEquals("localhost:8080", lhPort.toString())
    }
}
