// port-lint: source proto.rs
package io.github.kotlinmania.ramanet

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Web protocols that are relevant to Rama.
 */
@Serializable(with = NetProtocolSerializer::class)
sealed class NetProtocol : Comparable<NetProtocol> {
    abstract val scheme: String

    /**
     * Returns true if this protocol is HTTP or HTTPS.
     */
    open fun isHttp(): Boolean = false

    /**
     * Returns true if this protocol is WS or WSS.
     */
    open fun isWs(): Boolean = false

    /**
     * Returns true if this protocol is SOCKS5 or SOCKS5H.
     */
    open fun isSocks5(): Boolean = false

    /**
     * Returns true if this protocol is secure (HTTPS, WSS).
     */
    open fun isSecure(): Boolean = false

    /**
     * Returns the default port for this protocol if known.
     */
    open fun defaultPort(): UShort? = null

    fun asString(): String = scheme

    override fun toString(): String = scheme

    override fun compareTo(other: NetProtocol): Int = scheme.compareTo(other.scheme, ignoreCase = true)

    data object Http : NetProtocol() {
        override val scheme: String = HTTP_SCHEME

        override fun isHttp(): Boolean = true

        override fun defaultPort(): UShort = HTTP_DEFAULT_PORT
    }

    data object Https : NetProtocol() {
        override val scheme: String = HTTPS_SCHEME

        override fun isHttp(): Boolean = true

        override fun isSecure(): Boolean = true

        override fun defaultPort(): UShort = HTTPS_DEFAULT_PORT
    }

    data object Ws : NetProtocol() {
        override val scheme: String = WS_SCHEME

        override fun isWs(): Boolean = true

        override fun defaultPort(): UShort = WS_DEFAULT_PORT
    }

    data object Wss : NetProtocol() {
        override val scheme: String = WSS_SCHEME

        override fun isWs(): Boolean = true

        override fun isSecure(): Boolean = true

        override fun defaultPort(): UShort = WSS_DEFAULT_PORT
    }

    data object Socks5 : NetProtocol() {
        override val scheme: String = SOCKS5_SCHEME

        override fun isSocks5(): Boolean = true

        override fun defaultPort(): UShort = SOCKS5_DEFAULT_PORT
    }

    data object Socks5h : NetProtocol() {
        override val scheme: String = SOCKS5H_SCHEME

        override fun isSocks5(): Boolean = true

        override fun defaultPort(): UShort = SOCKS5H_DEFAULT_PORT
    }

    data class Custom(
        val customScheme: String,
    ) : NetProtocol() {
        init {
            require(validateScheme(customScheme)) { "invalid custom scheme: $customScheme" }
        }

        override val scheme: String get() = customScheme
    }

    companion object {
        const val HTTP_SCHEME: String = "http"
        const val HTTP_DEFAULT_PORT: UShort = 80u
        val HTTP: NetProtocol = Http

        const val HTTPS_SCHEME: String = "https"
        const val HTTPS_DEFAULT_PORT: UShort = 443u
        val HTTPS: NetProtocol = Https

        const val WS_SCHEME: String = "ws"
        const val WS_DEFAULT_PORT: UShort = HTTP_DEFAULT_PORT
        val WS: NetProtocol = Ws

        const val WSS_SCHEME: String = "wss"
        const val WSS_DEFAULT_PORT: UShort = HTTPS_DEFAULT_PORT
        val WSS: NetProtocol = Wss

        const val SOCKS5_SCHEME: String = "socks5"
        const val SOCKS5_DEFAULT_PORT: UShort = 1080u
        val SOCKS5: NetProtocol = Socks5

        const val SOCKS5H_SCHEME: String = "socks5h"
        const val SOCKS5H_DEFAULT_PORT: UShort = SOCKS5_DEFAULT_PORT
        val SOCKS5H: NetProtocol = Socks5h

        private const val MAX_SCHEME_LEN: Int = 64

        /**
         * Validates scheme string per RFC 3986 (ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )).
         */
        fun validateScheme(scheme: String): Boolean {
            if (scheme.isEmpty() || scheme.length > MAX_SCHEME_LEN) return false
            val first = scheme[0]
            if (!((first in 'a'..'z') || (first in 'A'..'Z'))) return false
            for (i in 1 until scheme.length) {
                val c = scheme[i]
                val valid = (c in 'a'..'z') || (c in 'A'..'Z') || (c in '0'..'9') || c == '+' || c == '-' || c == '.'
                if (!valid) return false
            }
            return true
        }

        /**
         * Creates a NetProtocol from a static string.
         */
        fun fromStatic(s: String): NetProtocol = parse(s)

        /**
         * Parses a protocol string.
         */
        fun parse(s: String): NetProtocol =
            when {
                s.isEmpty() || s.equals(HTTP_SCHEME, ignoreCase = true) -> Http
                s.equals(HTTPS_SCHEME, ignoreCase = true) -> Https
                s.equals(WS_SCHEME, ignoreCase = true) -> Ws
                s.equals(WSS_SCHEME, ignoreCase = true) -> Wss
                s.equals(SOCKS5_SCHEME, ignoreCase = true) -> Socks5
                s.equals(SOCKS5H_SCHEME, ignoreCase = true) -> Socks5h
                validateScheme(s) -> Custom(s)
                else -> throw InvalidNetProtocolException("invalid protocol string: $s")
            }

        /**
         * Extracts protocol from URI scheme bytes.
         *
         * Returns (NetProtocol?, bytesConsumed).
         */
        fun tryToExtractProtocolFromUriScheme(bytes: ByteArray): Pair<NetProtocol?, Int> {
            if (bytes.isEmpty()) {
                throw IllegalArgumentException("empty uri contains no scheme")
            }
            val maxLen = minOf(bytes.size, 512)
            for (i in 0 until maxLen) {
                if (bytes[i] == ':'.code.toByte()) {
                    if (bytes.size < i + 3) {
                        break
                    }
                    if (bytes[i + 1] != '/'.code.toByte() || bytes[i + 2] != '/'.code.toByte()) {
                        break
                    }
                    val schemeStr = bytes.decodeToString(0, i)
                    val protocol = parse(schemeStr)
                    return Pair(protocol, i + 3)
                }
            }
            return Pair(null, 0)
        }
    }
}

/**
 * Exception thrown when an invalid protocol string is parsed.
 */
class InvalidNetProtocolException(
    message: String = "invalid protocol string",
) : IllegalArgumentException(message)

object NetProtocolSerializer : KSerializer<NetProtocol> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.NetProtocol", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: NetProtocol) {
        encoder.encodeString(value.scheme)
    }

    override fun deserialize(decoder: Decoder): NetProtocol = NetProtocol.parse(decoder.decodeString())
}


