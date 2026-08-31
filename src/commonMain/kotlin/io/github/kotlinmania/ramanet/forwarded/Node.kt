// port-lint: source rama-net/src/forwarded/node.rs
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

import io.github.kotlinmania.ramanet.address.Domain
import io.github.kotlinmania.ramanet.address.Host
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.address.ParseUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private const val UNKNOWN_STR: String = "unknown"

/**
 * Name component of a [NodeId].
 */
sealed interface NodeName {
    data object Unknown : NodeName {
        override fun toString(): String = UNKNOWN_STR
    }

    data class Ip(
        val ip: String,
    ) : NodeName {
        init {
            val valid = ParseUtils.tryToParseStrToIp(ip)
            require(valid != null) { "invalid IP address: $ip" }
        }

        override fun toString(): String = ip
    }

    data class Obf(
        val node: ObfNode,
    ) : NodeName {
        override fun toString(): String = node.asStr()
    }
}

/**
 * Port component of a [NodeId].
 */
sealed interface NodePort {
    data class Num(
        val port: UShort,
    ) : NodePort {
        override fun toString(): String = port.toString()
    }

    data class Obf(
        val port: ObfPort,
    ) : NodePort {
        override fun toString(): String = port.asStr()
    }

    companion object {
        fun tryFromStr(s: String): Result<NodePort> {
            val num = s.toUShortOrNull()
            if (num != null) return Result.success(Num(num))
            val obf = ObfPort.tryFromStr(s)
            return if (obf.isSuccess) {
                Result.success(Obf(obf.getOrThrow()))
            } else {
                Result.failure(IllegalArgumentException("invalid NodePort: $s"))
            }
        }

        fun fromStrLossy(s: String): NodePort {
            val num = s.toUShortOrNull()
            return if (num != null) {
                Num(num)
            } else {
                Obf(ObfPort.fromStrLossy(s))
            }
        }
    }
}

/**
 * Node Identifier used in Forwarded headers.
 *
 * See <https://datatracker.ietf.org/doc/html/rfc7239#section-6>.
 */
@Serializable(with = NodeIdSerializer::class)
data class NodeId(
    val name: NodeName,
    val port: NodePort? = null,
) : Comparable<NodeId> {
    fun ip(): String? =
        when (name) {
            is NodeName.Ip -> name.ip
            NodeName.Unknown, is NodeName.Obf -> null
        }

    fun hasAnyPort(): Boolean = port != null

    fun numericPort(): UShort? = (port as? NodePort.Num)?.port

    fun authority(): HostWithPort? {
        val p = numericPort() ?: return null
        return when (val n = name) {
            is NodeName.Ip -> HostWithPort(Host.Address(n.ip), p)
            is NodeName.Obf -> {
                runCatching { Domain.parse(n.node.asStr()) }
                    .map { domain -> HostWithPort(Host.Name(domain), p) }
                    .getOrNull()
            }
            NodeName.Unknown -> null
        }
    }

    override fun toString(): String =
        when (val n = name) {
            NodeName.Unknown -> UNKNOWN_STR
            is NodeName.Ip -> {
                if (port == null) {
                    n.ip
                } else {
                    if (ParseUtils.isValidIpv6(n.ip)) {
                        "[${n.ip}]:$port"
                    } else {
                        "${n.ip}:$port"
                    }
                }
            }
            is NodeName.Obf -> {
                if (port == null) {
                    n.node.asStr()
                } else {
                    "${n.node.asStr()}:$port"
                }
            }
        }

    override fun compareTo(other: NodeId): Int = toString().compareTo(other.toString())

    companion object {
        fun unknown(): NodeId = NodeId(NodeName.Unknown, null)

        fun fromIp(ip: String, port: UShort? = null): NodeId {
            val cleanIp =
                ParseUtils.tryToParseStrToIp(ip)
                    ?: throw IllegalArgumentException("invalid IP address: $ip")
            return NodeId(NodeName.Ip(cleanIp), port?.let { NodePort.Num(it) })
        }

        fun fromDomain(domain: Domain, port: UShort? = null): NodeId =
            NodeId(NodeName.Obf(ObfNode.fromInner(domain.asString())), port?.let { NodePort.Num(it) })

        fun fromHostWithOptPort(hostWithOptPort: HostWithOptPort): NodeId =
            when (val host = hostWithOptPort.host) {
                is Host.Name -> fromDomain(host.domain, hostWithOptPort.port)
                is Host.Address -> fromIp(host.ip, hostWithOptPort.port)
            }

        fun fromHostWithPort(hostWithPort: HostWithPort): NodeId =
            when (val host = hostWithPort.host) {
                is Host.Name -> fromDomain(host.domain, hostWithPort.port)
                is Host.Address -> fromIp(host.ip, hostWithPort.port)
            }

        fun tryFromBytes(bytes: ByteArray): Result<NodeId> =
            runCatching { parse(bytes.decodeToString()) }

        fun tryFromStr(s: String): Result<NodeId> =
            runCatching { parse(s) }

        fun fromBytesLossy(bytes: ByteArray): NodeId =
            fromStrLossy(bytes.decodeToString())

        fun fromStrLossy(s: String): NodeId {
            val sOriginal = s

            if (s.equals(UNKNOWN_STR, ignoreCase = true)) {
                return NodeId(NodeName.Unknown, null)
            }

            val directIp = ParseUtils.tryToParseStrToIp(s)
            if (directIp != null) {
                return NodeId(NodeName.Ip(directIp), null)
            }

            val (hostPart, portPart) = splitNodePortLossy(s)
            val ip = ParseUtils.tryToParseStrToIp(hostPart)
            val name =
                if (ip != null) {
                    NodeName.Ip(ip)
                } else {
                    NodeName.Obf(ObfNode.fromStrLossy(hostPart))
                }

            return if (name is NodeName.Ip && ParseUtils.isValidIpv6(name.ip) && portPart != null && !hostPart.startsWith('[')) {
                NodeId(NodeName.Obf(ObfNode.fromStrLossy(sOriginal)), null)
            } else {
                NodeId(name, portPart)
            }
        }

        fun parse(s: String): NodeId {
            if (s.equals(UNKNOWN_STR, ignoreCase = true)) {
                return NodeId(NodeName.Unknown, null)
            }

            val directIp = ParseUtils.tryToParseStrToIp(s)
            if (directIp != null) {
                return NodeId(NodeName.Ip(directIp), null)
            }

            val (hostPart, portPart) = splitNodePort(s)
            val ip = ParseUtils.tryToParseStrToIp(hostPart)
            val name =
                if (ip != null) {
                    NodeName.Ip(ip)
                } else {
                    val obf = ObfNode.tryFromStr(hostPart).getOrThrow()
                    NodeName.Obf(obf)
                }

            if (name is NodeName.Ip && ParseUtils.isValidIpv6(name.ip) && portPart != null && !hostPart.startsWith('[')) {
                throw IllegalArgumentException("missing brackets for node IPv6 address with port")
            }

            return NodeId(name, portPart)
        }

        private fun splitNodePort(s: String): Pair<String, NodePort?> {
            val colon = s.lastIndexOf(':')
            if (colon >= 0) {
                val portStr = s.substring(colon + 1)
                val portRes = NodePort.tryFromStr(portStr)
                if (portRes.isSuccess) {
                    return Pair(s.substring(0, colon), portRes.getOrNull())
                }
            }
            return Pair(s, null)
        }

        private fun splitNodePortLossy(s: String): Pair<String, NodePort?> {
            val colon = s.lastIndexOf(':')
            if (colon >= 0) {
                val portStr = s.substring(colon + 1)
                val port = NodePort.fromStrLossy(portStr)
                return Pair(s.substring(0, colon), port)
            }
            return Pair(s, null)
        }
    }
}

object NodeIdSerializer : KSerializer<NodeId> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.forwarded.NodeId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: NodeId) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): NodeId = NodeId.parse(decoder.decodeString())
}
