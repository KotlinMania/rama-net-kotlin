package io.github.kotlinmania.ramanet.tls

import io.github.kotlinmania.ramanet.address.Domain

/**
 * Minimal client hello representation for SNI inspection.
 */
public data class ClientHello(
    public val serverName: Domain? = null,
)

/**
 * An `Extensions` value that can be added to the `Context`
 * of a transport layer to signal that the transport is secure.
 */
public data class SecureTransport(
    private val hello: ClientHello? = null,
) {
    public fun clientHello(): ClientHello? = hello

    public companion object {
        public fun default(): SecureTransport = SecureTransport()

        public fun withClientHello(hello: ClientHello): SecureTransport = SecureTransport(hello)
    }
}
