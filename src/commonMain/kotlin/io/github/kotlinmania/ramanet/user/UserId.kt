// port-lint: source rama-net/src/user/id.rs
package io.github.kotlinmania.ramanet.user

import kotlinx.serialization.Serializable

/**
 * The identifier of a user.
 */
@Serializable
sealed class UserId {
    /**
     * User identified by a username (e.g. Basic auth).
     */
    data class Username(
        val name: String,
    ) : UserId() {
        override fun toString(): String = name
    }

    /**
     * User identified by a token (e.g. Bearer auth).
     */
    data class Token(
        val token: ByteArray,
    ) : UserId() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Token) return false
            return token.contentEquals(other.token)
        }

        override fun hashCode(): Int = token.contentHashCode()

        override fun toString(): String = "***"
    }

    /**
     * User remains anonymous (not authenticated).
     */
    data object Anonymous : UserId() {
        override fun toString(): String = "anonymous"
    }
}
