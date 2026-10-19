// port-lint: source user/credentials/basic.rs
package io.github.kotlinmania.ramanet.user.credentials

import kotlinx.serialization.Serializable

/**
 * Basic credentials consisting of a username and optional password.
 */
@Serializable
public data class Basic(
    public val username: String,
    public val password: String? = null,
) {
    init {
        require(username.isNotEmpty()) { "username cannot be empty" }
        if (password != null) {
            require(password.isNotEmpty()) { "password cannot be empty" }
        }
    }

    public fun username(): String = username

    public fun password(): String? = password

    public fun hasPassword(): Boolean = password != null

    public fun cloneWithNewUsername(newUsername: String): Basic =
        copy(username = newUsername)

    public fun cloneWithNewPassword(newPassword: String): Basic =
        copy(password = newPassword)

    override fun toString(): String =
        if (password != null) {
            "$username:$password"
        } else {
            "$username:"
        }

    public companion object {
        public fun new(username: String, password: String): Basic {
            require(username.isNotEmpty()) { "username cannot be empty" }
            require(password.isNotEmpty()) { "password cannot be empty" }
            return Basic(username, password)
        }

        public fun newInsecure(username: String): Basic {
            require(username.isNotEmpty()) { "username cannot be empty" }
            return Basic(username, null)
        }

        public fun parse(s: String): Basic = fromStr(s)

        public fun fromStr(s: String): Basic {
            val colonIdx = s.indexOf(':')
            return if (colonIdx >= 0) {
                val username = s.substring(0, colonIdx)
                val password = s.substring(colonIdx + 1)
                require(username.isNotEmpty()) { "missing username in basic credential" }
                Basic(username, password.takeIf { it.isNotEmpty() })
            } else {
                require(s.isNotEmpty()) { "missing username in basic credential" }
                Basic(s, null)
            }
        }
    }
}
