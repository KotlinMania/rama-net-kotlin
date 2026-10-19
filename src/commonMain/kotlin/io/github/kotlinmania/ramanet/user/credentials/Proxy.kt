// port-lint: source user/credentials/proxy.rs
package io.github.kotlinmania.ramanet.user.credentials

import kotlinx.serialization.Serializable

/**
 * Proxy credentials supporting Basic and Bearer auth.
 */
@Serializable
public sealed class ProxyCredential {
    public data class BasicAuth(
        public val basic: Basic,
    ) : ProxyCredential() {
        override fun toString(): String = basic.toString()
    }

    public data class BearerAuth(
        public val bearer: Bearer,
    ) : ProxyCredential() {
        override fun toString(): String = bearer.toString()
    }

    public companion object {
        public fun from(basic: Basic): ProxyCredential = BasicAuth(basic)

        public fun from(bearer: Bearer): ProxyCredential = BearerAuth(bearer)
    }
}
