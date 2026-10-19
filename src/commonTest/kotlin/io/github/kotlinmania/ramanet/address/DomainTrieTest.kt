// port-lint: tests address/domain_trie.rs
package io.github.kotlinmania.ramanet.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DomainTrieTest {
    @Test
    fun testReverseDomain() {
        assertEquals("com.example.", reverseDomain("example.com."))
        assertEquals("com.example.", reverseDomain("example.com.."))
        assertEquals("com.example.", reverseDomain("example.com"))
        assertEquals("com.example.", reverseDomain(".example.com"))
        assertEquals("com.example.", reverseDomain("..example.com"))
        assertEquals("com.example.", reverseDomain(".example.com."))
        assertEquals("com.example.", reverseDomain("...example.com..."))
        assertEquals("com.example.sub.", reverseDomain("sub.example.com"))
        assertEquals("localhost.", reverseDomain("localhost"))
        assertEquals(".", reverseDomain(""))
    }

    @Test
    fun testTrieMostSpecificMatchingParent() {
        val matcher =
            DomainTrie<String>()
                .withInsertDomain("bar.example.com", "bar")
                .withInsertDomain("example.com", "root")
                .withInsertDomain("foo.bar.example.com", "foo.bar")

        assertEquals(
            DomainParentMatch(value = "root", isExact = true),
            matcher.matchParent("example.com"),
        )
        assertEquals(
            DomainParentMatch(value = "bar", isExact = true),
            matcher.matchParent("bar.example.com"),
        )
        assertEquals(
            DomainParentMatch(value = "bar", isExact = false),
            matcher.matchParent("baz.bar.example.com"),
        )
        assertEquals(
            DomainParentMatch(value = "root", isExact = false),
            matcher.matchParent("other.example.com"),
        )
        assertEquals(
            DomainParentMatch(value = "foo.bar", isExact = true),
            matcher.matchParent("foo.bar.example.com"),
        )
        assertEquals(
            DomainParentMatch(value = "foo.bar", isExact = false),
            matcher.matchParent("sub.foo.bar.example.com"),
        )
        assertNull(matcher.matchParent("example.org"))
        assertNull(matcher.matchParent("com"))
    }

    @Test
    fun testTrieMatchingParent() {
        val matcher =
            DomainTrie<String>()
                .withInsertDomain("example.com", "root")

        assertTrue(matcher.isMatchParent("example.com"))
        assertTrue(matcher.isMatchParent("sub.example.com"))
        assertTrue(matcher.isMatchParent("a.b.c.example.com"))
        assertFalse(matcher.isMatchParent("example.org"))
        assertFalse(matcher.isMatchParent("notexample.com"))
    }

    @Test
    fun testTrieMatchingExact() {
        val matcher =
            DomainTrie<String>()
                .withInsertDomain("example.com", "root")

        assertTrue(matcher.isMatchExact("example.com"))
        assertFalse(matcher.isMatchExact("sub.example.com"))
        assertEquals("root", matcher.matchExact("example.com"))
        assertNull(matcher.matchExact("sub.example.com"))
    }

    @Test
    fun testTrieIterDomainCorrectDirection() {
        val trie =
            DomainTrie<Int>()
                .withInsertDomain("a.b.c", 1)
                .withInsertDomain("d.e.f", 2)

        val pairs = trie.iter()
        assertEquals(2, pairs.size)
        val map = pairs.associate { it.first.asStr() to it.second }
        assertEquals(1, map["a.b.c"])
        assertEquals(2, map["d.e.f"])
    }
}
