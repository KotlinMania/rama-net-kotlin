// port-lint: source fingerprint/ja4/mod.rs
package io.github.kotlinmania.ramanet.fingerprint

import io.github.kotlinmania.itertools.intersperse
import io.github.kotlinmania.sha2.Sha256

/**
 * JA4 fingerprint hashing utilities using SHA-256 truncated to the first 12 hex characters.
 */
public object Ja4 {
    private const val HEX_CHARS = "0123456789abcdef"

    /**
     * Compute the 12-character truncated SHA-256 hex hash of a comma-joined list of fields.
     * Returns `"000000000000"` when [fields] is empty per the JA4 specification.
     */
    public fun hashFields(fields: List<String>): String {
        if (fields.isEmpty()) {
            return "000000000000"
        }
        val joined = StringBuilder()
        for (part in intersperse(fields.iterator(), ",")) {
            joined.append(part)
        }
        return hashString(joined.toString())
    }

    /**
     * Compute the 12-character truncated SHA-256 hex hash of [input].
     */
    public fun hashString(input: String): String {
        if (input.isEmpty()) {
            return "000000000000"
        }
        val digest = Sha256.digest(input.encodeToByteArray())
        val sb = StringBuilder(12)
        for (i in 0 until 6) {
            val b = digest[i].toInt() and 0xFF
            sb.append(HEX_CHARS[b ushr 4])
            sb.append(HEX_CHARS[b and 0x0F])
        }
        return sb.toString()
    }
}
