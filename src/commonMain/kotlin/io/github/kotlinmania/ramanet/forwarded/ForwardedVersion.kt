// port-lint: source forwarded/version.rs
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

enum class VersionKind {
    Http09,
    Http10,
    Http11,
    H2,
    H3,
}

class InvalidForwardedVersion(
    message: String = "invalid forwarded version",
) : IllegalArgumentException(message)

/**
 * Version of the forwarded protocol.
 */
data class ForwardedVersion(
    val kind: VersionKind,
) : Comparable<ForwardedVersion> {
    val value: String
        get() =
            when (kind) {
                VersionKind.Http09 -> "0.9"
                VersionKind.Http10 -> "1.0"
                VersionKind.Http11 -> "1.1"
                VersionKind.H2 -> "2"
                VersionKind.H3 -> "3"
            }

    fun asHttp(): String = value

    fun fmt(): String = toString()

    override fun toString(): String = value

    override fun compareTo(other: ForwardedVersion): Int = kind.compareTo(other.kind)

    companion object {
        val HTTP_09: ForwardedVersion = ForwardedVersion(VersionKind.Http09)
        val HTTP_10: ForwardedVersion = ForwardedVersion(VersionKind.Http10)
        val HTTP_11: ForwardedVersion = ForwardedVersion(VersionKind.Http11)
        val HTTP_2: ForwardedVersion = ForwardedVersion(VersionKind.H2)
        val HTTP_3: ForwardedVersion = ForwardedVersion(VersionKind.H3)

        val Http09: ForwardedVersion = HTTP_09
        val Http10: ForwardedVersion = HTTP_10
        val Http11: ForwardedVersion = HTTP_11
        val H2: ForwardedVersion = HTTP_2
        val H3: ForwardedVersion = HTTP_3

        fun fromStr(s: String): ForwardedVersion = tryFrom(s)

        fun tryFrom(s: String): ForwardedVersion =
            when (s) {
                "0.9" -> HTTP_09
                "1", "1.0" -> HTTP_10
                "1.1" -> HTTP_11
                "2", "2.0" -> HTTP_2
                "3", "3.0" -> HTTP_3
                else -> throw InvalidForwardedVersion("invalid forwarded version: $s")
            }

        fun tryFrom(bytes: ByteArray): ForwardedVersion = tryFrom(bytes.decodeToString())

        fun fmt(version: ForwardedVersion): String = version.toString()

        fun parse(s: String): ForwardedVersion = tryFrom(s)

        fun fromBytes(bytes: ByteArray): ForwardedVersion = tryFrom(bytes)
    }
}
