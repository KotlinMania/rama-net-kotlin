// port-lint: source tls/mod.rs
package io.github.kotlinmania.ramanet.tls

import io.github.kotlinmania.ramanet.address.Domain
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

/**
 * Intent for a (tls) keylogger to be used.
 */
public sealed class KeyLogIntent {
    /** By default `SSLKEYLOGFILE` env variable is respected as the path to key log to, if defined. */
    public data object Environment : KeyLogIntent()

    /** Choose to disable the key logging explicitly. */
    public data object Disabled : KeyLogIntent()

    /** Request keys to be logged to the given file path. */
    public data class File(
        public val path: String,
    ) : KeyLogIntent()

    public fun filePath(): String? =
        when (this) {
            Disabled -> null
            Environment -> null
            is File -> path
        }

    public fun intoFilePath(): String? = filePath()
}

/**
 * Implementation agnostic encoding of common data such as certificates and keys.
 */
public sealed class DataEncoding {
    /** Distinguished Encoding Rules (DER) (binary). */
    public data class Der(
        public val bytes: ByteArray,
    ) : DataEncoding() {
        override fun equals(other: Any?): Boolean =
            this === other || (other is Der && bytes.contentEquals(other.bytes))

        override fun hashCode(): Int = bytes.contentHashCode()
    }

    /** Same as [DataEncoding.Der], but multiple. */
    public data class DerStack(
        public val stack: List<ByteArray>,
    ) : DataEncoding()

    /** Privacy Enhanced Mail (PEM) (plain text). */
    public data class Pem(
        public val text: String,
    ) : DataEncoding() {
        init {
            require(text.isNotEmpty()) { "PEM text must not be empty" }
        }
    }
}
