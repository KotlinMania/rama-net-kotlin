// port-lint: source forwarded/obfuscated.rs
package io.github.kotlinmania.ramanet.forwarded

/*
 * Copyright (c) 2024 Plabayo
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

private const val OBF_MAX_LEN: Int = 256

private fun isValidObfChar(b: Byte): Boolean {
    val c = b.toInt().toChar()
    return (c in 'a'..'z') || (c in 'A'..'Z') || (c in '0'..'9') || c == '.' || c == '_' || c == '-'
}

private fun isValidObfNode(bytes: ByteArray): Boolean {
    if (bytes.isEmpty() || bytes.size > OBF_MAX_LEN) return false
    for (b in bytes) {
        if (!isValidObfChar(b)) return false
    }
    return true
}

private fun fixObfNode(bytes: ByteArray): ByteArray {
    val src = if (bytes.isEmpty()) byteArrayOf('_'.code.toByte()) else bytes
    val len = if (src.size > OBF_MAX_LEN) OBF_MAX_LEN else src.size
    val result = ByteArray(len)
    for (i in 0 until len) {
        val b = src[i]
        result[i] = if (isValidObfChar(b)) b else '_'.code.toByte()
    }
    return result
}

private fun isValidObfPort(bytes: ByteArray): Boolean =
    isValidObfNode(bytes) && bytes[0] == '_'.code.toByte()

private fun fixObfPort(bytes: ByteArray): ByteArray {
    val prefixed =
        if (bytes.isEmpty()) {
            byteArrayOf('_'.code.toByte())
        } else if (bytes[0] != '_'.code.toByte()) {
            val next = ByteArray(bytes.size + 1)
            next[0] = '_'.code.toByte()
            bytes.copyInto(next, 1)
            next
        } else {
            bytes
        }
    val len = if (prefixed.size > OBF_MAX_LEN) OBF_MAX_LEN else prefixed.size
    val result = ByteArray(len)
    for (i in 0 until len) {
        val b = prefixed[i]
        result[i] = if (isValidObfChar(b)) b else '_'.code.toByte()
    }
    return result
}

/**
 * Obfuscated node identifier used by Forwarded extension.
 *
 * See <https://datatracker.ietf.org/doc/html/rfc7239#section-6>.
 */
data class ObfNode(
    val value: String,
) : Comparable<ObfNode> {
    init {
        require(isValidObfNode(value.encodeToByteArray())) { "invalid ObfNode: $value" }
    }

    fun asStr(): String = value

    override fun toString(): String = value

    override fun compareTo(other: ObfNode): Int = value.compareTo(other.value)

    companion object {
        fun fromStatic(s: String): ObfNode = ObfNode(s)

        fun tryFromStr(s: String): Result<ObfNode> = runCatching { ObfNode(s) }

        fun tryFromBytes(bytes: ByteArray): Result<ObfNode> =
            runCatching {
                require(isValidObfNode(bytes)) { "invalid ObfNode bytes" }
                ObfNode(bytes.decodeToString())
            }

        fun fromBytesLossy(bytes: ByteArray): ObfNode {
            val fixed = fixObfNode(bytes)
            return ObfNode(fixed.decodeToString())
        }

        fun fromStrLossy(s: String): ObfNode = fromBytesLossy(s.encodeToByteArray())

        fun fromInner(inner: String): ObfNode = ObfNode(inner)
    }
}

/**
 * Obfuscated port identifier used by Forwarded extension.
 *
 * See <https://datatracker.ietf.org/doc/html/rfc7239#section-6>.
 */
data class ObfPort(
    val value: String,
) : Comparable<ObfPort> {
    init {
        require(isValidObfPort(value.encodeToByteArray())) { "invalid ObfPort: $value" }
    }

    fun asStr(): String = value

    override fun toString(): String = value

    override fun compareTo(other: ObfPort): Int = value.compareTo(other.value)

    companion object {
        fun fromStatic(s: String): ObfPort = ObfPort(s)

        fun tryFromStr(s: String): Result<ObfPort> = runCatching { ObfPort(s) }

        fun tryFromBytes(bytes: ByteArray): Result<ObfPort> =
            runCatching {
                require(isValidObfPort(bytes)) { "invalid ObfPort bytes" }
                ObfPort(bytes.decodeToString())
            }

        fun fromBytesLossy(bytes: ByteArray): ObfPort {
            val fixed = fixObfPort(bytes)
            return ObfPort(fixed.decodeToString())
        }

        fun fromStrLossy(s: String): ObfPort = fromBytesLossy(s.encodeToByteArray())

        fun fromInner(inner: String): ObfPort = ObfPort(inner)
    }
}
