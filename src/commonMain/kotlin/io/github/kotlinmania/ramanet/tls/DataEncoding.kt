package io.github.kotlinmania.ramanet.tls

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
