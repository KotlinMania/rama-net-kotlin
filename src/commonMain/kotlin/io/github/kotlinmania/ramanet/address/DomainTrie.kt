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

    private fun reverseLabels(domainStr: String): List<String> {
        val str = domainStr.trim('.').lowercase()
        return str.split('.').reversed()
    }

    /**
     * Inserts the given domain paired with the value [T].
     */
    public fun insert(domain: Domain, value: T) {
        insert(domain.asStr(), value)
    }

    public fun insert(domainStr: String, value: T) {
        val labels = reverseLabels(domainStr)
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

    public fun insertDomain(domainStr: String, value: T): DomainTrie<T> {
        insert(domainStr, value)
        return this
    }

    public fun insertDomain(domainRef: AsDomainRef, value: T): DomainTrie<T> =
        insertDomain(domainRef.domainAsStr(), value)

    /**
     * Inserts the given domain paired with the value [T] and returns `this`.
     */
    public fun withInsertDomain(domain: Domain, value: T): DomainTrie<T> = insertDomain(domain, value)

    public fun withInsertDomain(domainStr: String, value: T): DomainTrie<T> = insertDomain(domainStr, value)

    public fun withInsertDomain(domainRef: AsDomainRef, value: T): DomainTrie<T> = insertDomain(domainRef, value)

    public fun withInsertDomainIter(domains: Iterable<String>, value: T): DomainTrie<T> {
        insertDomainIter(domains, value)
        return this
    }

    public fun insertDomainIter(domains: Iterable<String>, value: T): DomainTrie<T> {
        for (domain in domains) {
            insertDomain(domain, value)
        }
        return this
    }

    public fun extend(iter: Iterable<Pair<String, T>>): DomainTrie<T> {
        for ((domain, value) in iter) {
            insertDomain(domain, value)
        }
        return this
    }

    /**
     * Retrieves the exact match for the domain if present.
     */
    public fun get(domain: Domain): T? = get(domain.asStr())

    public fun get(domainStr: String): T? {
        val labels = reverseLabels(domainStr)
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

    public fun matchExact(domainStr: String): T? = get(domainStr)

    public fun matchExact(domainRef: AsDomainRef): T? = get(domainRef.domainAsStr())

    /**
     * Returns true if the input domain is an exact domain stored in this [DomainTrie].
     */
    public fun isMatchExact(domain: Domain): Boolean = get(domain) != null

    public fun isMatchExact(domainStr: String): Boolean = get(domainStr) != null

    public fun isMatchExact(domainRef: AsDomainRef): Boolean = get(domainRef.domainAsStr()) != null

    /**
     * Returns true if the input domain is a subdomain of at least one node in this [DomainTrie].
     */
    public fun isMatchParent(domain: Domain): Boolean = matchParent(domain) != null

    public fun isMatchParent(domainStr: String): Boolean = matchParent(domainStr) != null

    public fun isMatchParent(domainRef: AsDomainRef): Boolean = matchParent(domainRef.domainAsStr()) != null

    /**
     * Matches the closest parent (or exact match) of the given domain in the trie.
     */
    public fun matchParent(domain: Domain): DomainParentMatch<T>? = matchParent(domain.asStr())

    public fun matchParent(domainRef: AsDomainRef): DomainParentMatch<T>? = matchParent(domainRef.domainAsStr())

    public fun matchParent(domainStr: String): DomainParentMatch<T>? {
        val labels = reverseLabels(domainStr)
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

    public fun iter(): List<Pair<Domain, T>> {
        val result = mutableListOf<Pair<Domain, T>>()

        fun collect(node: Node<T>, path: List<String>) {
            val v = node.value
            if (v != null) {
                val originalDomainStr = path.reversed().joinToString(".")
                result.add(Domain.fromMaybeBorrowedUnchecked(originalDomainStr) to v)
            }
            for ((label, child) in node.children) {
                collect(child, path + label)
            }
        }
        collect(root, emptyList())
        return result
    }

    public fun clear() {
        root.children.clear()
        root.value = null
        count = 0
    }

    public companion object {
        public fun <T> new(): DomainTrie<T> = DomainTrie()

        public fun <T> default(): DomainTrie<T> = DomainTrie()

        public fun <T> fromIter(iter: Iterable<Pair<String, T>>): DomainTrie<T> {
            val trie = DomainTrie<T>()
            trie.extend(iter)
            return trie
        }
    }
}
