// port-lint: source address/domain.rs
package io.github.kotlinmania.ramanet.address

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A domain name.
 */
@Serializable(with = DomainSerializer::class)
class Domain private constructor(
    private val value: String,
) : Comparable<Domain> {
    init {
        require(isValidName(value)) { "invalid domain: $value" }
    }

    /**
     * Returns true if this domain is a Fully Qualified Domain Name (ends with dot).
     */
    fun isFqdn(): Boolean = value.endsWith('.')

    /**
     * Returns true if this domain is a wildcard domain (starts with '*.'):
     */
    fun isWildcard(): Boolean = value.startsWith("*.")

    /**
     * Returns the parent of this wildcard domain, if it is a wildcard.
     */
    fun asWildcardParent(): Domain? =
        if (isWildcard()) {
            Domain(value.substring(2))
        } else {
            null
        }

    /**
     * Creates a subdomain from the current domain by prefixing sub.
     */
    fun tryAsSub(sub: String): Domain {
        val full = "$sub.$value"
        require(isValidName(full)) { "invalid subdomain: $full" }
        return Domain(full)
    }

    /**
     * Promote this domain to a wildcard.
     */
    fun tryAsWildcard(): Domain {
        val full = "*.$value"
        require(isValidName(full)) { "invalid wildcard domain: $full" }
        return Domain(full)
    }

    /**
     * Strips the prefix subdomain if present.
     */
    fun stripSub(prefix: String): Domain? {
        if (!value.startsWith(prefix)) return null
        val remaining = value.substring(prefix.length).trimStart('.')
        return if (remaining.isNotEmpty() && isValidName(remaining)) {
            Domain(remaining)
        } else {
            null
        }
    }

    /**
     * Returns true if this domain is a sub of (or equal to) the other domain.
     */
    fun isSubOf(other: Domain): Boolean {
        val a = value.trim('.')
        val b = other.value.trim('.')
        return when {
            a.length == b.length -> a.equals(b, ignoreCase = true)
            a.length > b.length -> {
                val n = a.length - b.length
                a[n - 1] == '.' && a.substring(n).equals(b, ignoreCase = true)
            }
            else -> false
        }
    }

    /**
     * Returns true if this domain is a parent of (or equal to) the other domain.
     */
    fun isParentOf(other: Domain): Boolean = other.isSubOf(this)

    /**
     * Returns the public suffix (TLD) of the domain.
     */
    fun suffix(): String? {
        val trimmed = value.trim('.').lowercase()
        val dot = trimmed.lastIndexOf('.')
        return if (dot >= 0) {
            trimmed.substring(dot + 1)
        } else if (trimmed.isNotEmpty()) {
            trimmed
        } else {
            null
        }
    }

    /**
     * Returns true if this domain is a TLD.
     */
    fun isTld(): Boolean {
        val s = suffix() ?: return false
        val trimmed = value.trim('.').lowercase()
        return trimmed == s
    }

    /**
     * Returns true if this domain is a Second-Level Domain (SLD).
     */
    fun isSld(): Boolean {
        val s = suffix() ?: return false
        val trimmed = value.trim('.').lowercase()
        if (!trimmed.endsWith(s)) return false
        val rest = trimmed.substring(0, trimmed.length - s.length).trimEnd('.')
        return rest.isNotEmpty() && !rest.contains('.')
    }

    fun asString(): String = value

    override fun toString(): String = value

    override fun compareTo(other: Domain): Int = value.compareTo(other.value, ignoreCase = true)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Domain) return false
        val a = value.removePrefix(".")
        val b = other.value.removePrefix(".")
        return a.equals(b, ignoreCase = true)
    }

    override fun hashCode(): Int {
        val normalized = value.removePrefix(".").lowercase()
        return normalized.hashCode()
    }

    companion object {
        fun isValidName(s: String): Boolean {
            if (s.isEmpty() || s.length > 253) return false
            var check = s
            if (check.startsWith("*.")) {
                check = check.substring(2)
            }
            val trimmed = check.trim('.')
            if (trimmed.isEmpty()) return false

            val labels = trimmed.split('.')
            for (label in labels) {
                if (label.isEmpty() || label.length > 63) return false
                if (label.startsWith('-') || label.endsWith('-')) return false
                for (c in label) {
                    val valid = (c in 'a'..'z') || (c in 'A'..'Z') || (c in '0'..'9') || c == '-'
                    if (!valid) return false
                }
            }
            return true
        }

        fun fromStatic(s: String): Domain = parse(s)

        fun parse(s: String): Domain = Domain(s)

        fun example(): Domain = Domain("example.com")

        fun tldPrivate(): Domain = Domain("internal")

        fun tldLocalhost(): Domain = Domain("localhost")
    }
}

object DomainSerializer : KSerializer<Domain> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.Domain", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Domain) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): Domain = Domain.parse(decoder.decodeString())
}
