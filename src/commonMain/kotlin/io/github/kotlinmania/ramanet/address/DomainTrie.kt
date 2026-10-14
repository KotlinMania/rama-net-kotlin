// port-lint: source address/domain_trie.rs
package io.github.kotlinmania.ramanet.address

import io.github.kotlinmania.itertools.intersperse

/**
 * Result of [DomainTrie.matchParent].
 */
public data class DomainParentMatch<T>(
    public val value: T,
    public val isExact: Boolean,
)

/**
 * Reverses a domain string into trailing-dot reversed label order (e.g. `"example.com"` -> `"com.example."`).
 */
public fun reverseDomain(domain: String): String {
    val from = domain.trim('.')
    if (from.isEmpty()) return "."
    val parts = from.split('.').reversed() + listOf("")
    val sb = StringBuilder()
    for (part in intersperse(parts.iterator(), ".")) {
        sb.append(part)
    }
    return sb.toString()
}

/**
 * An efficient radix tree that can be used to match (sub)domains.
 */
public class DomainTrie<T> {
    private class Node<T> {
        val children: MutableMap<String, Node<T>> = mutableMapOf()
        var value: T? = null
    }

    private val root = Node<T>()
    private var count: Int = 0

    public val size: Int get() = count

    public fun len(): Int = count

    public fun isEmpty(): Boolean = count == 0

    private fun reverseLabels(domain: Domain): List<String> {
        val str = domain.asString().trim('.').lowercase()
        return str.split('.').reversed()
    }

    /**
     * Inserts the given domain paired with the value [T].
     */
    public fun insert(domain: Domain, value: T) {
        val labels = reverseLabels(domain)
        var curr = root
        for (label in labels) {
            curr = curr.children.getOrPut(label) { Node() }
        }
        if (curr.value == null) {
            count++
        }
        curr.value = value
    }

    /**
     * Inserts the given domain paired with the value [T] and returns `this`.
     */
    public fun insertDomain(domain: Domain, value: T): DomainTrie<T> {
        insert(domain, value)
        return this
    }

    /**
     * Inserts the given domain paired with the value [T] and returns `this`.
     */
    public fun withInsertDomain(domain: Domain, value: T): DomainTrie<T> = insertDomain(domain, value)

    /**
     * Retrieves the exact match for the domain if present.
     */
    public fun get(domain: Domain): T? {
        val labels = reverseLabels(domain)
        var curr = root
        for (label in labels) {
            curr = curr.children[label] ?: return null
        }
        return curr.value
    }

    /**
     * Returns the value stored for an exact domain match.
     */
    public fun matchExact(domain: Domain): T? = get(domain)

    /**
     * Returns true if the input domain is an exact domain stored in this [DomainTrie].
     */
    public fun isMatchExact(domain: Domain): Boolean = get(domain) != null

    /**
     * Returns true if the input domain is a subdomain of at least one node in this [DomainTrie].
     */
    public fun isMatchParent(domain: Domain): Boolean = matchParent(domain) != null

    /**
     * Matches the closest parent (or exact match) of the given domain in the trie.
     */
    public fun matchParent(domain: Domain): DomainParentMatch<T>? {
        val labels = reverseLabels(domain)
        var curr = root
        var lastMatch: T? = null
        var lastMatchDepth = 0

        var depth = 0
        for (label in labels) {
            curr = curr.children[label] ?: break
            depth++
            if (curr.value != null) {
                lastMatch = curr.value
                lastMatchDepth = depth
            }
        }

        return lastMatch?.let {
            DomainParentMatch(
                value = it,
                isExact = lastMatchDepth == labels.size,
            )
        }
    }

    public fun clear() {
        root.children.clear()
        root.value = null
        count = 0
    }

    public companion object {
        public fun <T> new(): DomainTrie<T> = DomainTrie()
    }
}
