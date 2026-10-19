// port-lint: source user/credentials/bearer.rs
package io.github.kotlinmania.ramanet.user.credentials

import kotlinx.serialization.Serializable

/**
 * Bearer credentials consisting of an ASCII token.
 */
@Serializable
public data class Bearer(
    public val token: String,
) {
    init {
        require(token.isNotEmpty()) { "empty string cannot be used as Bearer token" }
        for (c in token) {
            val code = c.code
            require(code in 32..126) { "string contains non visible ASCII characters" }
        }
    }

    public fun token(): String = token

    public fun unmasked(): String = token

    override fun toString(): String = "***"

    public companion object {
        public fun parse(s: String): Bearer = fromStr(s)

        public fun fromStr(s: String): Bearer = Bearer(s)

        public fun tryNew(s: String): Bearer = Bearer(s)
    }
}
