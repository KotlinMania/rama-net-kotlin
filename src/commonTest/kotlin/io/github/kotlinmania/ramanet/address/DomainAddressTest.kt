// port-lint: tests address/domain_address.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DomainAddressTest {
    private fun assertEq(s: String, domainAddress: DomainAddress, domain: String, port: UShort) {
        assertEquals(domain, domainAddress.domain.asStr(), "parsing: $s")
        assertEquals(port, domainAddress.port, "parsing: $s")
    }

    @Test
    fun testValidDomainAddress() {
        val cases =
            listOf(
                Triple("example.com:80", "example.com", 80.toUShort()),
                Triple("subdomain.example.com:443", "subdomain.example.com", 443.toUShort()),
                Triple("127.0.0.1:8080", "127.0.0.1", 8080.toUShort()),
            )
        for ((s, expectedDomain, expectedPort) in cases) {
            assertEq(s, DomainAddress.parse(s), expectedDomain, expectedPort)
            assertEq(s, DomainAddress.tryFrom(s), expectedDomain, expectedPort)
            assertEq(s, DomainAddress.tryFrom(s.encodeToByteArray()), expectedDomain, expectedPort)
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
                "::1:8080",
                "[::1]",
                "example",
                "exa\$mple.com:8080",
                "2001:db8:3333:4444:5555:6666:7777:8888:8080",
                "2001:db8:3333:4444:5555:6666:7777:8888",
                "[2001:db8:3333:4444:5555:6666:7777:8888]",
                "[2001:db8:3333:4444:5555:6666:7777:8888]:8080",
                "example.com",
                "example.com:",
                "example.com:-1",
                "example.com:999999",
                "example:com",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
            )
        for (s in invalidCases) {
            assertFailsWith<IllegalArgumentException>("parsing '$s'") {
                DomainAddress.parse(s)
            }
            assertFailsWith<IllegalArgumentException>("parsing bytes '$s'") {
                DomainAddress.tryFrom(s.encodeToByteArray())
            }
        }
    }

    @Test
    fun testParseDisplay() {
        val cases =
            listOf(
                "example.com:80" to "example.com:80",
                "subdomain.example.com:443" to "subdomain.example.com:443",
            )
        for ((s, expected) in cases) {
            val domainAddress = DomainAddress.parse(s)
            assertEquals(expected, domainAddress.toString(), "parsing '$s'")
        }
    }
}
