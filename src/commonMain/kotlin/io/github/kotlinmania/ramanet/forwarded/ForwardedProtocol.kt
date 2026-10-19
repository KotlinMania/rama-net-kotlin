// port-lint: source forwarded/proto.rs
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

import io.github.kotlinmania.ramanet.NetProtocol

enum class ProtocolKind {
    Http,
    Https,
}

class UnknownProtocol(
    message: String = "unknown protocol",
) : IllegalArgumentException(message)

class InvalidProtocolStr(
    message: String = "invalid forwarded protocol string",
) : IllegalArgumentException(message)

/**
 * Protocols that were forwarded.
 *
 * These are a subset of [NetProtocol].
 */
enum class ForwardedProtocol(
    val scheme: String,
) {
    Http("http"),
    Https("https"),
    ;

    fun isHttp(): Boolean = true

    fun isSecure(): Boolean = this == Https

    fun asScheme(): String = scheme

    fun asStr(): String = scheme

    fun intoProtocol(): NetProtocol = toProtocol()

    fun toProtocol(): NetProtocol =
        when (this) {
            Http -> NetProtocol.Http
            Https -> NetProtocol.Https
        }

    fun fmt(): String = scheme

    override fun toString(): String = scheme

    companion object {
        val HTTP: ForwardedProtocol = Http
        val HTTPS: ForwardedProtocol = Https

        fun from(p: ForwardedProtocol): NetProtocol = p.toProtocol()

        fun fromProtocol(protocol: NetProtocol): ForwardedProtocol = tryFrom(protocol)

        fun tryFrom(protocol: NetProtocol): ForwardedProtocol =
            when {
                protocol.isHttp() && protocol.isSecure() -> Https
                protocol.isHttp() -> Http
                else -> throw UnknownProtocol("unknown protocol: $protocol")
            }

        fun tryFrom(s: String): ForwardedProtocol =
            when {
                s.equals("http", ignoreCase = true) -> Http
                s.equals("https", ignoreCase = true) -> Https
                else -> throw InvalidProtocolStr("invalid forwarded protocol string: $s")
            }

        fun fromStr(s: String): ForwardedProtocol = tryFrom(s)

        fun parse(s: String): ForwardedProtocol = tryFrom(s)

        fun fmt(p: ForwardedProtocol): String = p.toString()
    }
}
