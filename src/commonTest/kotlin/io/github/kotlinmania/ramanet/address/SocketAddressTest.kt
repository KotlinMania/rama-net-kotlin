// port-lint: tests address/socket_address.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SocketAddressTest {
    private fun assertEq(
        s: String,
        sockAddress: SocketAddress,
        ipAddr: String,
        port: UShort,
    ) {
        assertEquals(ipAddr, sockAddress.ip, "parsing: $s")
        assertEquals(port, sockAddress.port, "parsing: $s")
    }

    @Test
    fun testParseValid() {
        val cases =
            listOf(
                "[::1]:80" to ("::1" to 80u.toUShort()),
                "127.0.0.1:80" to ("127.0.0.1" to 80u.toUShort()),
                "[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    ("2001:db8:3333:4444:5555:6666:7777:8888" to 80u.toUShort()),
            )

        for ((s, expected) in cases) {
            val (expectedIp, expectedPort) = expected
            val parsed = SocketAddress.parse(s)
            assertEq(s, parsed, expectedIp, expectedPort)
            assertEq(s, SocketAddress.tryFrom(s), expectedIp, expectedPort)
            assertEq(s, SocketAddress.fromStr(s), expectedIp, expectedPort)
            assertEq(s, SocketAddress.tryFrom(s.encodeToByteArray()), expectedIp, expectedPort)
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
                "example.com:80",
                "example:com",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
            )

        for (s in invalidCases) {
            assertFailsWith<Exception>("parsing '$s'") {
                SocketAddress.parse(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom") {
                SocketAddress.tryFrom(s)
            }
            assertFailsWith<Exception>("parsing '$s' via tryFrom(bytes)") {
                SocketAddress.tryFrom(s.encodeToByteArray())
            }
        }
    }

    @Test
    fun testParseDisplay() {
        val cases =
            listOf(
                "[::1]:80" to "[::1]:80",
                "127.0.0.1:80" to "127.0.0.1:80",
            )

        for ((s, expected) in cases) {
            val sa = SocketAddress.parse(s)
            assertEquals(expected, sa.toString(), "parsing: $s")
        }
    }
}
