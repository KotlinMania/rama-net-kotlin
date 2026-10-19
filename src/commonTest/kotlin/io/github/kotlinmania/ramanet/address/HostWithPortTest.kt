// port-lint: tests address/host_with_port.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HostWithPortTest {
    private fun assertEq(
        s: String,
        hostWithPort: HostWithPort,
        expectedHost: String,
        expectedPort: UShort,
    ) {
        assertEquals(expectedHost, hostWithPort.host.toString(), "parsing: $s")
        assertEquals(expectedPort, hostWithPort.port, "parsing: $s")
    }

    @Test
    fun testParseValid() {
        val validCases =
            listOf(
                "example.com:80" to ("example.com" to 80u.toUShort()),
                "[::1]:80" to ("::1" to 80u.toUShort()),
                "127.0.0.1:80" to ("127.0.0.1" to 80u.toUShort()),
                "[2001:db8:3333:4444:5555:6666:7777:8888]:80" to ("2001:db8:3333:4444:5555:6666:7777:8888" to 80u.toUShort()),
            )

        for ((s, expected) in validCases) {
            val (expectedHost, expectedPort) = expected
            val parsed = HostWithPort.parse(s)
            assertEq(s, parsed, expectedHost, expectedPort)
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
                "::1",
                "127.0.0.1",
                "[::1]",
                "2001:db8:3333:4444:5555:6666:7777:8888",
                "[2001:db8:3333:4444:5555:6666:7777:8888]",
                "example.com",
                "example.com:",
                "example.com:-1",
                "example.com:999999",
                "example:com",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
            )

        for (s in invalidCases) {
            assertFailsWith<Exception>("parsing '$s' should fail") {
                HostWithPort.parse(s)
            }
        }
    }

    @Test
    fun testParseDisplay() {
        val displayCases =
            listOf(
                "example.com:80" to "example.com:80",
                "[::1]:80" to "[::1]:80",
                "127.0.0.1:80" to "127.0.0.1:80",
            )

        for ((s, expected) in displayCases) {
            val parsed = HostWithPort.parse(s)
            assertEquals(expected, parsed.toString(), "parsing: $s")
        }
    }

    @Test
    fun testFactoryConstructors() {
        val local4 = HostWithPort.localIpv4(8080u)
        assertEquals("127.0.0.1:8080", local4.toString())

        val local6 = HostWithPort.localIpv6(8080u)
        assertEquals("[::1]:8080", local6.toString())

        val default4 = HostWithPort.defaultIpv4(8080u)
        assertEquals("0.0.0.0:8080", default4.toString())

        val default6 = HostWithPort.defaultIpv6(8080u)
        assertEquals("[::]:8080", default6.toString())

        val bcast4 = HostWithPort.broadcastIpv4(8080u)
        assertEquals("255.255.255.255:8080", bcast4.toString())

        val exHttp = HostWithPort.exampleDomainHttp()
        assertEquals("example.com:80", exHttp.toString())

        val exHttps = HostWithPort.exampleDomainHttps()
        assertEquals("example.com:443", exHttps.toString())

        val exPort = HostWithPort.exampleDomainWithPort(8080u)
        assertEquals("example.com:8080", exPort.toString())

        val localHttp = HostWithPort.localhostDomainHttp()
        assertEquals("localhost:80", localHttp.toString())

        val localHttps = HostWithPort.localhostDomainHttps()
        assertEquals("localhost:443", localHttps.toString())

        val localPort = HostWithPort.localhostDomainWithPort(8080u)
        assertEquals("localhost:8080", localPort.toString())

        val withHost = local4.withHost(Host.example())
        assertEquals("example.com:8080", withHost.toString())

        val withPort = local4.withPort(9090u)
        assertEquals("127.0.0.1:9090", withPort.toString())
    }
}
