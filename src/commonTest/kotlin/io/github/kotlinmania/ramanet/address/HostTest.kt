// port-lint: tests address/host.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HostTest {
    sealed class Is {
        data class Domain(
            val value: String,
        ) : Is()

        data class Ip(
            val value: String,
        ) : Is()
    }

    private fun assertIsExpected(host: Host, expected: Is) {
        when (expected) {
            is Is.Domain -> {
                when (host) {
                    is Host.Address -> error("expected host address ${host.ip} to be the domain: ${expected.value}")
                    is Host.Name -> assertEquals(expected.value, host.domain.asString())
                }
            }
            is Is.Ip -> {
                when (host) {
                    is Host.Address -> assertEquals(expected.value, host.ip)
                    is Host.Name -> error("expected host domain ${host.domain} to be the ip: ${expected.value}")
                }
            }
        }
    }

    @Test
    fun testParseSpecials() {
        val cases =
            listOf(
                "localhost" to Is.Domain("localhost"),
                "0.0.0.0" to Is.Ip("0.0.0.0"),
                "::1" to Is.Ip("::1"),
                "[::1]" to Is.Ip("::1"),
                "127.0.0.1" to Is.Ip("127.0.0.1"),
                "::" to Is.Ip("::"),
                "[::]" to Is.Ip("::"),
            )

        for ((str, expected) in cases) {
            assertIsExpected(Host.tryFrom(str), expected)
            assertIsExpected(Host.parse(str), expected)
            assertIsExpected(Host.tryFrom(str.encodeToByteArray()), expected)
        }
    }

    @Test
    fun testParseBytesValid() {
        val cases =
            listOf(
                "example.com".encodeToByteArray() to Is.Domain("example.com"),
                "aA1".encodeToByteArray() to Is.Domain("aA1"),
                byteArrayOf(127, 0, 0, 1) to Is.Ip("127.0.0.1"),
                byteArrayOf(19, 117, 63, 126) to Is.Ip("19.117.63.126"),
                byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1) to Is.Ip("::1"),
                byteArrayOf(
                    32,
                    1,
                    13,
                    184.toByte(),
                    51,
                    51,
                    68,
                    68,
                    85,
                    85,
                    102,
                    102,
                    119,
                    119,
                    136.toByte(),
                    136.toByte(),
                ) to Is.Ip("2001:db8:3333:4444:5555:6666:7777:8888"),
            )

        for ((bytes, expected) in cases) {
            assertIsExpected(Host.tryFrom(bytes), expected)
        }
    }

    @Test
    fun testParseValid() {
        val cases =
            listOf(
                "example.com" to Is.Domain("example.com"),
                "www.example.com" to Is.Domain("www.example.com"),
                "a-b-c.com" to Is.Domain("a-b-c.com"),
                "a-b-c.example.com" to Is.Domain("a-b-c.example.com"),
                "a-b-c.example" to Is.Domain("a-b-c.example"),
                "aA1" to Is.Domain("aA1"),
                ".example.com" to Is.Domain(".example.com"),
                "example.com." to Is.Domain("example.com."),
                ".example.com." to Is.Domain(".example.com."),
                "127.0.0.1" to Is.Ip("127.0.0.1"),
                "127.00.1" to Is.Domain("127.00.1"),
                "::1" to Is.Ip("::1"),
                "[::1]" to Is.Ip("::1"),
                "2001:db8:3333:4444:5555:6666:7777:8888" to Is.Ip("2001:db8:3333:4444:5555:6666:7777:8888"),
                "[2001:db8:3333:4444:5555:6666:7777:8888]" to Is.Ip("2001:db8:3333:4444:5555:6666:7777:8888"),
                "::" to Is.Ip("::"),
                "[::]" to Is.Ip("::"),
                "19.117.63.126" to Is.Ip("19.117.63.126"),
            )

        for ((str, expected) in cases) {
            assertIsExpected(Host.tryFrom(str), expected)
            assertIsExpected(Host.parse(str), expected)
            assertIsExpected(Host.tryFrom(str.encodeToByteArray()), expected)
        }
    }

    @Test
    fun testParseStrInvalid() {
        val invalid =
            listOf(
                "",
                ".",
                "-",
                ".-",
                "-.",
                ".-.",
                "[::",
                "::]",
                "@",
                "こんにちは",
                "こんにちは.com",
            )

        for (str in invalid) {
            assertFailsWith<Exception>("parsing $str") {
                Host.parse(str)
            }
            assertFailsWith<Exception>("parsing $str via tryFrom") {
                Host.tryFrom(str)
            }
        }
    }

    @Test
    fun compareHostWithIpv4Bidirectional() {
        val testCases =
            listOf(
                Triple(true, Host.parse("127.0.0.1"), "127.0.0.1"),
                Triple(false, Host.parse("127.0.0.2"), "127.0.0.1"),
                Triple(false, Host.parse("127.0.0.1"), "127.0.0.2"),
            )

        for ((expected, a, b) in testCases) {
            assertEquals(expected, a.equals(b), "a[$a] == b[$b]")
            assertEquals(expected, a == Host.parse(b), "a[$a] == Host.parse(b[$b])")
        }
    }

    @Test
    fun compareHostWithIpv6Bidirectional() {
        val testCases =
            listOf(
                Triple(true, Host.parse("::1"), "::1"),
                Triple(false, Host.parse("::2"), "::1"),
                Triple(false, Host.parse("::1"), "::2"),
            )

        for ((expected, a, b) in testCases) {
            assertEquals(expected, a.equals(b), "a[$a] == b[$b]")
            assertEquals(expected, a == Host.parse(b), "a[$a] == Host.parse(b[$b])")
        }
    }

    @Test
    fun compareHostWithIpBidirectional() {
        val testCases =
            listOf(
                Triple(true, Host.parse("127.0.0.1"), IpConstants.IPV4_LOCALHOST),
                Triple(false, Host.parse("127.0.0.2"), IpConstants.IPV4_LOCALHOST),
                Triple(false, Host.parse("127.0.0.1"), "127.0.0.2"),
                Triple(false, Host.parse("::2"), IpConstants.IPV4_LOCALHOST),
            )

        for ((expected, a, b) in testCases) {
            assertEquals(expected, a.equals(b), "a[$a] == b[$b]")
            assertEquals(expected, a == Host.parse(b), "a[$a] == Host.parse(b[$b])")
        }
    }
}
