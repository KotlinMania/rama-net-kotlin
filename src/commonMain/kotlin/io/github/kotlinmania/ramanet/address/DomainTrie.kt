// port-lint: source address/domain_trie.rs
package io.github.kotlinmania.ramanet.address

/**
 * Result of [DomainTrie.matchParent].
 */
data class DomainParentMatch<T>(
    val value: T,
    val isExact: Boolean,
)

/**
 * An efficient radix tree that can be used to match (sub)domains.
 */
class DomainTrie<T> {
    private class Node<T> {
        val children: MutableMap<String, Node<T>> = mutableMapOf()
        var value: T? = null
    }

    private val root = Node<T>()
    private var count: Int = 0

    val size: Int get() = count

    fun isEmpty(): Boolean = count == 0

    private fun reverseLabels(domain: Domain): List<String> {
        val str = domain.asString().trim('.').lowercase()
        return str.split('.').reversed()
    }

    /**
     * Inserts the given domain paired with the value [T].
     */
    fun insert(domain: Domain, value: T) {
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
     * Retrieves the exact match for the domain if present.
     */
    fun get(domain: Domain): T? {
        val labels = reverseLabels(domain)
        var curr = root
        for (label in labels) {
            curr = curr.children[label] ?: return null
        }
        return curr.value
    }

    /**
     * Matches the closest parent (or exact match) of the given domain in the trie.
     */
    fun matchParent(domain: Domain): DomainParentMatch<T>? {
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

    fun clear() {
        root.children.clear()
        root.value = null
        count = 0
    }
}
