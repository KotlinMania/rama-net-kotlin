// port-lint: source rama-net/src/socket/interface.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.socket

import io.github.kotlinmania.ramanet.address.ParseUtils
import io.github.kotlinmania.ramanet.address.SocketAddress
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Name of a (network) interface device name, e.g. `eth0`.
 */
@HiddenFromObjC
@Serializable(with = DeviceNameSerializer::class)
public data class DeviceName(
    val value: String,
) : Comparable<DeviceName> {
    init {
        require(isValid(value)) { "invalid (interface) device name: $value" }
    }

    public fun asBytes(): ByteArray = value.encodeToByteArray()

    public fun asStr(): String = value

    override fun toString(): String = value

    override fun compareTo(other: DeviceName): Int = value.compareTo(other.value)

    public companion object {
        public const val DEVICE_MAX_LEN: Int = 15

        public fun isValid(s: String): Boolean {
            if (s.isEmpty() || s.length > DEVICE_MAX_LEN) return false
            val first = s[0]
            if (!isValidFirstChar(first)) return false
            for (c in s) {
                if (!isValidChar(c)) return false
            }
            return true
        }

        private fun isValidFirstChar(c: Char): Boolean =
            (c in 'a'..'z') || (c in 'A'..'Z')

        private fun isValidChar(c: Char): Boolean =
            (c in 'a'..'z') ||
                (c in 'A'..'Z') ||
                (c in '0'..'9') ||
                c == '-' ||
                c == '.' ||
                c == ':' ||
                c == '_'

        public fun of(name: String): DeviceName = DeviceName(name)

        public fun tryFrom(s: String): Result<DeviceName> = runCatching { DeviceName(s) }

        public fun tryFrom(bytes: ByteArray): Result<DeviceName> =
            runCatching {
                val s = bytes.decodeToString()
                DeviceName(s)
            }
    }
}

public object DeviceNameSerializer : KSerializer<DeviceName> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.socket.DeviceName", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DeviceName) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): DeviceName = DeviceName(decoder.decodeString())
}

/**
 * The interface to bind a socket to.
 */
@HiddenFromObjC
@Serializable(with = InterfaceSerializer::class)
public sealed interface Interface {
    public data class Address(
        val socketAddress: SocketAddress,
    ) : Interface {
        override fun toString(): String = socketAddress.toString()
    }

    public data class Device(
        val deviceName: DeviceName,
    ) : Interface {
        override fun toString(): String = deviceName.toString()
    }

    public companion object {
        public fun newAddress(addr: SocketAddress): Interface = Address(addr)

        public fun newDevice(name: String): Interface = Device(DeviceName(name))

        public fun localIpv4(port: UShort = 8080u): Interface = Address(SocketAddress.localIpv4(port))

        public fun localIpv6(port: UShort = 8080u): Interface = Address(SocketAddress.localIpv6(port))

        public fun defaultIpv4(port: UShort = 8080u): Interface = Address(SocketAddress.defaultIpv4(port))

        public fun defaultIpv6(port: UShort = 8080u): Interface = Address(SocketAddress.defaultIpv6(port))

        public fun broadcastIpv4(port: UShort = 8080u): Interface = Address(SocketAddress.broadcastIpv4(port))

        public fun parse(s: String): Interface {
            val (ipAddrStr, port) =
                try {
                    ParseUtils.splitPortFromStr(s)
                } catch (e: IllegalArgumentException) {
                    if (DeviceName.isValid(s)) {
                        return Device(DeviceName(s))
                    }
                    throw e
                }

            val parsedIp = ParseUtils.tryToParseStrToIp(ipAddrStr)
            if (parsedIp != null) {
                if (ParseUtils.isValidIpv6(parsedIp) && !s.startsWith('[')) {
                    throw IllegalArgumentException("missing brackets for IPv6 address with port: $s")
                }
                return Address(SocketAddress(parsedIp, port))
            } else {
                if (DeviceName.isValid(s)) {
                    return Device(DeviceName(s))
                }
                throw IllegalArgumentException("invalid bind interface: $s")
            }
        }

        public fun tryFrom(s: String): Result<Interface> = runCatching { parse(s) }

        public fun tryFrom(bytes: ByteArray): Result<Interface> =
            runCatching {
                val s = bytes.decodeToString()
                parse(s)
            }
    }
}

public object InterfaceSerializer : KSerializer<Interface> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.socket.Interface", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Interface) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Interface = Interface.parse(decoder.decodeString())
}
