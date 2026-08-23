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

/**
 * Version of the forwarded protocol.
 */
enum class ForwardedVersion(
    val value: String,
) {
    Http09("0.9"),
    Http10("1.0"),
    Http11("1.1"),
    H2("2"),
    H3("3"),
    ;

    override fun toString(): String = value

    companion object {
        val HTTP_09: ForwardedVersion = Http09
        val HTTP_10: ForwardedVersion = Http10
        val HTTP_11: ForwardedVersion = Http11
        val HTTP_2: ForwardedVersion = H2
        val HTTP_3: ForwardedVersion = H3

        fun parse(s: String): ForwardedVersion =
            when (s) {
                "0.9" -> Http09
                "1", "1.0" -> Http10
                "1.1" -> Http11
                "2", "2.0" -> H2
                "3", "3.0" -> H3
                else -> throw IllegalArgumentException("invalid forwarded version: $s")
            }

        fun fromBytes(bytes: ByteArray): ForwardedVersion = parse(bytes.decodeToString())
    }
}
