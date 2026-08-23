// port-lint: source user/credentials/mod.rs
package io.github.kotlinmania.ramanet.user

import kotlinx.serialization.Serializable

/**
 * Basic credentials consisting of a username and optional password.
 */
@Serializable
data class Basic(
    val username: String,
    val password: String? = null,
) {
    init {
        require(username.isNotEmpty()) { "username cannot be empty" }
    }

    override fun toString(): String =
        if (password != null) {
            "$username:***"
        } else {
            username
        }

    companion object {
        fun new(username: String, password: String): Basic {
            require(username.isNotEmpty()) { "username cannot be empty" }
            require(password.isNotEmpty()) { "password cannot be empty" }
            return Basic(username, password)
        }

        fun newInsecure(username: String): Basic {
            require(username.isNotEmpty()) { "username cannot be empty" }
            return Basic(username, null)
        }

        fun parse(s: String): Basic {
            val colonIdx = s.indexOf(':')
            return if (colonIdx >= 0) {
                val username = s.substring(0, colonIdx)
                val password = s.substring(colonIdx + 1)
                require(username.isNotEmpty()) { "username cannot be empty" }
                Basic(username, password.takeIf { it.isNotEmpty() })
            } else {
                require(s.isNotEmpty()) { "username cannot be empty" }
                Basic(s, null)
            }
        }
    }
}

/**
 * Bearer credentials consisting of an ASCII token.
 */
@Serializable
data class Bearer(
    val token: String,
) {
    init {
        require(token.isNotEmpty()) { "empty string cannot be used as Bearer token" }
        for (c in token) {
            val code = c.code
            require(code in 32..126) { "string contains non visible ASCII characters" }
        }
    }

    override fun toString(): String = "***"

    fun unmasked(): String = token

    companion object {
        fun parse(s: String): Bearer = Bearer(s)
    }
}

/**
 * Proxy credentials supporting Basic and Bearer auth.
 */
@Serializable
sealed class ProxyCredential {
    data class BasicAuth(
        val basic: Basic,
    ) : ProxyCredential() {
        override fun toString(): String = basic.toString()
    }

    data class BearerAuth(
        val bearer: Bearer,
    ) : ProxyCredential() {
        override fun toString(): String = bearer.toString()
    }
}
