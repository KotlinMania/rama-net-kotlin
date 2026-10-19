// port-lint: tests address/domain.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DomainTest {
    @Test
    fun testSpecials() {
        assertEquals("localhost", Domain.tldLocalhost().asStr())
        assertEquals("internal", Domain.tldPrivate().asStr())
        assertEquals("example.com", Domain.example().asStr())
    }

    @Test
    fun testDomainParseValid() {
        val validCases =
            listOf(
                "example.com",
                "_acme-challenge.example.com",
                "_acme-challenge_.example.com",
                "_acme_challenge_.example.com",
                "www.example.com",
                "*.com",
                "*.example.com",
                "a-b-c.com",
                "a-b-c.example.com",
                "a-b-c.example",
                "aA1",
                ".example.com",
                "example.com.",
                ".example.com.",
                "rr5---sn-q4fl6n6s.video.com",
                "127.0.0.1",
            )
        for (str in validCases) {
            assertEquals(str, Domain.tryFrom(str).asStr(), "to parse: $str")
            assertEquals(str, Domain.tryFrom(str.encodeToByteArray()).asStr(), "to parse bytes: $str")
        }
    }

    @Test
    fun testDomainIsWildcard() {
        assertFalse(Domain.fromStatic("localhost").isWildcard())
        assertFalse(Domain.fromStatic("example.com").isWildcard())
        assertFalse(Domain.fromStatic("foo.example.com").isWildcard())

        assertTrue(Domain.fromStatic("*.com").isWildcard())
        assertTrue(Domain.fromStatic("*.example.com").isWildcard())
        assertTrue(Domain.fromStatic("*.foo.example.com").isWildcard())
    }

    @Test
    fun testDomainAsWildcardParent() {
        assertNull(Domain.fromStatic("localhost").asWildcardParent())
        assertNull(Domain.fromStatic("example.com").asWildcardParent())
        assertNull(Domain.fromStatic("foo.example.com").asWildcardParent())

        assertEquals(Domain.fromStatic("com"), Domain.fromStatic("*.com").asWildcardParent())
        assertEquals(Domain.fromStatic("example.com"), Domain.fromStatic("*.example.com").asWildcardParent())
        assertEquals(Domain.fromStatic("foo.example.com"), Domain.fromStatic("*.foo.example.com").asWildcardParent())
    }

    @Test
    fun testDomainParseInvalid() {
        val invalidCases =
            listOf(
                "",
                ".",
                "..",
                "-",
                "*",
                ".*",
                "*.",
                ".*.",
                ".-",
                "-.",
                ".-.",
                "-.-.",
                "-.-.-",
                ".-.-",
                "2001:db8:3333:4444:5555:6666:7777:8888",
                "-example.com",
                "foo.*.com",
                "*example.com",
                "*foo",
                "o*o",
                "fo*",
                "local!host",
                "thislabeliswaytoolongforbeingeversomethingwewishtocareabout-example.com",
                "example-thislabeliswaytoolongforbeingeversomethingwewishtocareabout.com",
                "example..com",
                "example dot com",
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
            )
        for (str in invalidCases) {
            assertFailsWith<IllegalArgumentException>("input = '$str'") {
                Domain.tryFrom(str)
            }
            assertFailsWith<IllegalArgumentException>("input bytes = '$str'") {
                Domain.tryFrom(str.encodeToByteArray())
            }
        }
    }

    @Test
    fun isParent() {
        val testCases =
            listOf(
                "www.example.com" to "www.example.com",
                "www.example.com" to "www.example.com.",
                "www.example.com" to ".www.example.com.",
                ".www.example.com" to "www.example.com",
                ".www.example.com" to "www.example.com.",
                ".www.example.com." to "www.example.com.",
                "www.example.com" to "WwW.ExamplE.COM",
                "example.com" to "www.example.com",
                "example.com" to "m.example.com",
                "example.com" to "www.EXAMPLE.com",
                "example.com" to "M.example.com",
            )
        for ((a, b) in testCases) {
            val da = Domain.fromStatic(a)
            val db = Domain.fromStatic(b)
            assertTrue(da.isParentOf(db), "($da).isParentOf($db)")
        }
    }

    @Test
    fun asWildcardSub() {
        val testCases =
            listOf(
                "example.com" to "*.example.com",
                "fp.ramaproxy.org" to "*.fp.ramaproxy.org",
                "print.co.uk" to "*.print.co.uk",
            )
        for ((domainRaw, expectedOutput) in testCases) {
            val domain = Domain.fromStatic(domainRaw)
            val subdomain = domain.tryAsWildcard()
            assertEquals(Domain.fromStatic(expectedOutput), subdomain)
            assertTrue(subdomain.isWildcard())
            assertEquals(domain, subdomain.asWildcardParent())
        }
    }

    @Test
    fun asSubSuccess() {
        val testCases =
            listOf(
                Triple("example.com", "www", "www.example.com"),
                Triple("fp.ramaproxy.org", "h1", "h1.fp.ramaproxy.org"),
                Triple(
                    "dadadadadadadadadad.llgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.co.uk",
                    "a",
                    "a.dadadadadadadadadad.llgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.co.uk",
                ),
            )
        for ((domainRaw, sub, expectedOutput) in testCases) {
            val domain = Domain.fromStatic(domainRaw)
            val subdomain = domain.tryAsSub(sub)
            assertEquals(Domain.fromStatic(expectedOutput), subdomain)
        }
    }

    @Test
    fun asSubFailure() {
        val testCases =
            listOf(
                "adadadadadadadadadad.llgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch.co.uk" to "a",
            )
        for ((domainRaw, sub) in testCases) {
            val domain = Domain.fromStatic(domainRaw)
            assertFailsWith<IllegalArgumentException> {
                domain.tryAsSub(sub)
            }
        }
    }

    @Test
    fun testStripSub() {
        val testCases =
            listOf(
                Triple("www.example.com", "www", "example.com"),
                Triple("example.com", "www", null),
                Triple("www.www.example.com", "www", "www.example.com"),
            )
        for ((subRaw, prefix, expectedOutput) in testCases) {
            val sub = Domain.fromStatic(subRaw)
            val result = sub.stripSub(prefix)
            val expectedResult = expectedOutput?.let { Domain.fromStatic(it) }
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun isNotParent() {
        val testCases =
            listOf(
                "www.example.com" to "www.example.co",
                "www.example.com" to "www.ejemplo.com",
                "www.example.com" to "www3.example.com",
                "w.example.com" to "www.example.com",
                "gel.com" to "kegel.com",
            )
        for ((a, b) in testCases) {
            val da = Domain.fromStatic(a)
            val db = Domain.fromStatic(b)
            assertFalse(da.isParentOf(db), "!($da).isParentOf($db)")
        }
    }

    @Test
    fun isEqual() {
        val testCases =
            listOf(
                "example.com" to "example.com",
                "example.com" to "EXAMPLE.com",
                ".example.com" to ".example.com",
                ".example.com" to "example.com",
                "example.com" to ".example.com",
            )
        for ((a, b) in testCases) {
            val da = Domain.fromStatic(a)
            val db = Domain.fromStatic(b)
            assertEquals(da, db)
            assertEquals(db, da)
        }
    }

    @Test
    fun testIsTld() {
        val cases =
            listOf(
                true to ".com",
                true to "com",
                true to "co.uk",
                true to ".co.uk",
                false to "example.com",
                false to "foo.uk",
                false to "foo.example.com",
            )
        for ((expected, input) in cases) {
            assertEquals(expected, Domain.fromStatic(input).isTld(), "input: $input")
        }
    }

    @Test
    fun testIsSld() {
        val cases =
            listOf(
                false to "com",
                false to "co.uk",
                true to "example.com",
                true to ".example.com",
                true to "foo.uk",
                true to ".foo.uk",
                false to "foo.example.com",
            )
        for ((expected, input) in cases) {
            assertEquals(expected, Domain.fromStatic(input).isSld(), "input: $input")
        }
    }

    @Test
    fun isNotEqual() {
        val testCases =
            listOf(
                "example.com" to "localhost",
                "example.com" to "example.com.",
                "example.com" to "example.co",
                "example.com" to "examine.com",
                "example.com" to "example.com.us",
                "example.com" to "www.example.com",
            )
        for ((a, b) in testCases) {
            val da = Domain.fromStatic(a)
            val db = Domain.fromStatic(b)
            assertNotEquals(da, db)
        }
    }

    @Test
    fun testCmp() {
        val testCases =
            listOf(
                Triple("example.com", "example.com", 0),
                Triple("example.com", "EXAMPLE.com", 0),
                Triple(".example.com", ".example.com", 0),
                Triple(".example.com", "example.com", 0),
                Triple("example.com", ".example.com", 0),
                Triple("example.com", "localhost", -1),
                Triple("example.com", "example.com.", -1),
                Triple("example.com", "example.co", 1),
                Triple("example.com", "examine.com", 1),
                Triple("example.com", "example.com.us", -1),
                Triple("example.com", "www.example.com", -1),
            )
        for ((a, b, expected) in testCases) {
            val da = Domain.fromStatic(a)
            val db = Domain.fromStatic(b)
            val cmp = da.compareTo(db)
            val sign =
                if (cmp < 0) {
                    -1
                } else if (cmp > 0) {
                    1
                } else {
                    0
                }
            assertEquals(expected, sign, "cmp of $a and $b")
        }
    }

    @Test
    fun testHash() {
        val map = HashMap<Domain, Unit>()
        assertFalse(map.containsKey(Domain.fromStatic("example.com")))
        assertFalse(map.containsKey(Domain.fromStatic("EXAMPLE.COM")))
        assertFalse(map.containsKey(Domain.fromStatic(".example.com")))
        assertFalse(map.containsKey(Domain.fromStatic(".example.COM")))

        map[Domain.fromStatic("eXaMpLe.COm")] = Unit

        assertTrue(map.containsKey(Domain.fromStatic("example.com")))
        assertTrue(map.containsKey(Domain.fromStatic("EXAMPLE.COM")))
        assertTrue(map.containsKey(Domain.fromStatic(".example.com")))
        assertTrue(map.containsKey(Domain.fromStatic(".example.COM")))

        assertFalse(map.containsKey(Domain.fromStatic("www.example.com")))
        assertFalse(map.containsKey(Domain.fromStatic("examine.com")))
        assertFalse(map.containsKey(Domain.fromStatic("example.com.")))
        assertFalse(map.containsKey(Domain.fromStatic("example.co")))
        assertFalse(map.containsKey(Domain.fromStatic("example.commerce")))
    }
}
