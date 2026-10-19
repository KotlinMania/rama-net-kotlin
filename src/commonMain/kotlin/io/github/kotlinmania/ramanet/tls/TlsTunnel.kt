package io.github.kotlinmania.ramanet.tls

import io.github.kotlinmania.ramanet.address.Host

/**
 * Context information that can be provided by `tls` connectors,
 * to configure the connection in function on a tls tunnel.
 */
public data class TlsTunnel(
    /** The server name to use for the connection. */
    public val serverHost: Host,
) {
    public companion object {
        public fun new(serverHost: Host): TlsTunnel = TlsTunnel(serverHost)
    }
}
