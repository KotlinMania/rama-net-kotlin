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
 * A trait which is used by the rama-net crate for places where we wish to have access to
 * a reference to a Domain, directly or indirectly, for non-move purposes.
 */
public interface AsDomainRef {
    public fun domainAsStr(): String

    public fun asWildcardParent(): Domain? =
        domainAsStr().removePrefix("*.").takeIf { domainAsStr().startsWith("*.") }?.let { Domain.fromStr(it) }
}

/**
 * A trait which can be used where a Domain is expected.
 */
public interface IntoDomain {
    public fun intoDomain(): Domain
}

/**
 * Compare two domain names case-insensitively, ignoring a leading dot.
 */
public fun cmpDomain(aStr: String, bStr: String): Int {
    val a = if (aStr.startsWith('.')) aStr.substring(1) else aStr
    val b = if (bStr.startsWith('.')) bStr.substring(1) else bStr
    val minLen = minOf(a.length, b.length)
    for (i in 0 until minLen) {
        val ca = a[i].lowercaseChar()
        val cb = b[i].lowercaseChar()
        if (ca != cb) {
            return ca.compareTo(cb)
        }
    }
    return a.length.compareTo(b.length)
}

/**
 * Compare two domain names for equality case-insensitively, ignoring a leading dot.
 */
public fun partialEqDomain(aStr: String, bStr: String): Boolean {
    val a = if (aStr.startsWith('.')) aStr.substring(1) else aStr
    val b = if (bStr.startsWith('.')) bStr.substring(1) else bStr
    return a.equals(b, ignoreCase = true)
}

/**
 * Checks if a domain label is valid.
 */
public fun isValidLabel(name: ByteArray, start: Int, stop: Int): Boolean {
    if (start >= stop || stop - start > 63 || name[start] == '-'.code.toByte() || name[stop - 1] == '-'.code.toByte()) {
        return false
    }
    var i = start
    while (i < stop) {
        val c = name[i].toInt().toChar()
        val isAlnum = (c in 'a'..'z') || (c in 'A'..'Z') || (c in '0'..'9')
        if (!isAlnum && c != '_' && (c != '-' || i == start)) {
            return false
        }
        i++
    }
    return true
}

/**
 * Checks if the domain name is valid.
 */
public fun isValidName(name: ByteArray): Boolean {
    if (name.isEmpty() || name.size > 253) {
        return false
    }
    var nonEmptyGroups = 0
    var i = 0
    var offset = 0

    if (name[0] == '*'.code.toByte()) {
        if (name.size <= 2 || name[1] != '.'.code.toByte()) {
            return false
        }
        offset = 2
        i = 2
        nonEmptyGroups = 1
    }

    while (i < name.size) {
        val c = name[i]
        if (c == '.'.code.toByte()) {
            if (offset == i) {
                if (i == 0 || i == name.size - 1) {
                    i++
                    offset = i
                    continue
                } else {
                    return false
                }
            }
            if (!isValidLabel(name, offset, i)) {
                return false
            }
            offset = i + 1
            nonEmptyGroups++
        }
        i++
    }
    return if (offset == i) {
        nonEmptyGroups > 0
    } else {
        isValidLabel(name, offset, i)
    }
}

/**
 * A domain name.
 */
@Serializable(with = DomainSerializer::class)
public class Domain private constructor(
    private val value: String,
) : Comparable<Domain>, AsDomainRef, IntoDomain {
    init {
        require(isValidName(value.encodeToByteArray())) { "invalid domain" }
    }

    override fun domainAsStr(): String = value

    override fun intoDomain(): Domain = this

    /**
     * Consumes the domain as a host.
     */
    public fun intoHost(): Host = Host.Name(this)

    /**
     * Returns true if this domain is a Fully Qualified Domain Name (ends with dot).
     */
    public fun isFqdn(): Boolean = value.endsWith('.')

    /**
     * Returns true if this domain is a wildcard domain (starts with '*.'):
     */
    public fun isWildcard(): Boolean = value.startsWith("*.")

    /**
     * Returns true if this domain is a Top-Level Domain (TLD).
     */
    public fun isTld(): Boolean {
        val s = suffix() ?: return false
        return partialEqDomain(value, s)
    }

    /**
     * Returns true if this domain is a Second-Level Domain (SLD).
     */
    public fun isSld(): Boolean {
        val s = suffix() ?: return false
        val trimmed = value.trim('.').lowercase()
        if (!trimmed.endsWith(s)) return false
        val rest = trimmed.substring(0, trimmed.length - s.length).trimEnd('.')
        return rest.isNotEmpty() && !rest.contains('.')
    }

    /**
     * Returns the parent of this wildcard domain, if it is a wildcard.
     */
    override fun asWildcardParent(): Domain? =
        if (isWildcard()) {
            Domain(value.substring(2))
        } else {
            null
        }

    /**
     * Creates a subdomain from the current domain by prefixing sub.
     */
    public fun tryAsSub(sub: String): Domain {
        val full = "$sub.$value"
        require(isValidName(full.encodeToByteArray())) { "invalid subdomain" }
        return Domain(full)
    }

    public fun tryAsSub(sub: AsDomainRef): Domain = tryAsSub(sub.domainAsStr())

    /**
     * Promote this domain to a wildcard.
     */
    public fun tryAsWildcard(): Domain {
        val full = "*.$value"
        require(isValidName(full.encodeToByteArray())) { "invalid subdomain" }
        return Domain(full)
    }

    /**
     * Strips the prefix subdomain if present.
     */
    public fun stripSub(prefix: String): Domain? {
        if (!value.startsWith(prefix)) return null
        val remaining = value.substring(prefix.length).trimStart('.')
        return if (remaining.isNotEmpty() && isValidName(remaining.encodeToByteArray())) {
            Domain(remaining)
        } else {
            null
        }
    }

    public fun stripSub(prefix: AsDomainRef): Domain? = stripSub(prefix.domainAsStr())

    /**
     * Returns true if this domain is a sub of (or equal to) the other domain.
     */
    public fun isSubOf(other: Domain): Boolean {
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
    public fun isParentOf(other: Domain): Boolean = other.isSubOf(this)

    /**
     * Compare the registrable domain.
     */
    public fun haveSameRegistrableDomain(other: Domain): Boolean {
        val thisRd = registrableDomain() ?: asStr()
        val otherRd = other.registrableDomain() ?: other.asStr()
        return thisRd.equals(otherRd, ignoreCase = true)
    }

    private fun registrableDomain(): String? {
        val s = suffix() ?: return null
        val trimmed = value.trim('.').lowercase()
        if (!trimmed.endsWith(s)) return null
        val beforeSuffix = trimmed.substring(0, trimmed.length - s.length).trimEnd('.')
        if (beforeSuffix.isEmpty()) {
            return s
        }
        val lastDot = beforeSuffix.lastIndexOf('.')
        val sld = if (lastDot >= 0) beforeSuffix.substring(lastDot + 1) else beforeSuffix
        return "$sld.$s"
    }

    /**
     * Returns the public suffix (TLD) of the domain.
     */
    public fun suffix(): String? {
        val trimmed = value.trim('.').lowercase()
        val parts = trimmed.split('.')
        if (parts.size >= 2) {
            val lastTwo = "${parts[parts.size - 2]}.${parts[parts.size - 1]}"
            if (lastTwo in KNOWN_TWO_PART_SUFFIXES) {
                return lastTwo
            }
        }
        val dot = trimmed.lastIndexOf('.')
        return if (dot >= 0) {
            trimmed.substring(dot + 1)
        } else if (trimmed.isNotEmpty()) {
            trimmed
        } else {
            null
        }
    }

    public fun len(): Int = value.length

    public fun asStr(): String = value

    public fun asString(): String = value

    public fun intoInner(): String = value

    override fun toString(): String = value

    override fun compareTo(other: Domain): Int = cmpDomain(value, other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is String) return partialEqDomain(value, other)
        if (other !is Domain) return false
        return partialEqDomain(value, other.value)
    }

    override fun hashCode(): Int {
        val trimmed = if (value.startsWith('.')) value.substring(1) else value
        var result = 1
        for (i in 0 until trimmed.length) {
            val b = trimmed[i].lowercaseChar().code
            result = 31 * result + b
        }
        return result
    }

    public companion object {
        private val KNOWN_TWO_PART_SUFFIXES = setOf(
            "co.uk", "org.uk", "me.uk", "ltd.uk", "plc.uk", "net.uk",
            "com.au", "net.au", "org.au", "edu.au", "gov.au",
            "co.nz", "net.nz", "org.nz",
            "co.jp", "ne.jp", "or.jp",
            "com.br", "net.br", "org.br",
        )

        public fun isValidName(s: String): Boolean = isValidName(s.encodeToByteArray())

        public fun fromStatic(s: String): Domain = Domain(s)

        public fun fromMaybeBorrowedUnchecked(s: String): Domain = Domain(s)

        public fun parse(s: String): Domain = Domain(s)

        public fun fromStr(s: String): Domain = Domain(s)

        public fun tryFrom(name: String): Domain = Domain(name)

        public fun tryFrom(name: ByteArray): Domain = Domain(name.decodeToString())

        public fun example(): Domain = fromStatic("example.com")

        public fun tldPrivate(): Domain = fromStatic("internal")

        public fun tldLocalhost(): Domain = fromStatic("localhost")
    }
}

public object DomainSerializer : KSerializer<Domain> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.address.Domain", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Domain) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): Domain = Domain.parse(decoder.decodeString())
}
