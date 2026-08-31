// port-lint: source rama-net/src/asn.rs
package io.github.kotlinmania.ramanet

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Autonomous system number (ASN).
 */
@Serializable(with = AsnSerializer::class)
class Asn private constructor(
    private val value: UInt,
) : Comparable<Asn> {
    /**
     * Return ASN as UInt (0 if unspecified).
     */
    fun asUInt(): UInt = value

    /**
     * Returns true if this value is considered to be "any" value (unspecified).
     */
    fun isAny(): Boolean = value == 0u

    override fun compareTo(other: Asn): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Asn) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String =
        if (value == 0u) {
            "unspecified"
        } else {
            "AS$value"
        }

    companion object {
        /**
         * Validates if the given UInt is within a valid ASN range.
         */
        fun isValidAsnRange(value: UInt): Boolean =
            (value in 1u..23455u) ||
                (value in 23457u..64495u) ||
                (value in 131072u..4294967294u)

        /**
         * Creates an Asn from a static number.
         *
         * @throws IllegalArgumentException if the ASN is invalid.
         */
        fun fromStatic(value: UInt): Asn {
            if (value == 0u) return Asn(0u)
            require(isValidAsnRange(value)) { "invalid ASN range" }
            return Asn(value)
        }

        /**
         * Creates an unspecified ASN.
         */
        fun unspecified(): Asn = Asn(0u)

        /**
         * Parses a UInt into an Asn.
         */
        fun parse(value: UInt): Asn {
            if (value == 0u) return Asn(0u)
            if (!isValidAsnRange(value)) {
                throw InvalidAsnException("invalid ASN: $value")
            }
            return Asn(value)
        }

        /**
         * Parses a string representation into an Asn.
         */
        fun parse(value: String): Asn {
            val trimmed = value.trim()
            val numStr =
                if (trimmed.startsWith("AS", ignoreCase = true)) {
                    trimmed.substring(2)
                } else {
                    trimmed
                }
            val num = numStr.toUIntOrNull() ?: throw InvalidAsnException("invalid ASN string: $value")
            return parse(num)
        }

        /**
         * Parses a byte array into an Asn.
         */
        fun parse(bytes: ByteArray): Asn = parse(bytes.decodeToString())
    }
}

/**
 * Exception thrown when an invalid ASN value is encountered.
 */
class InvalidAsnException(
    message: String = "invalid ASN",
) : IllegalArgumentException(message)

object AsnSerializer : KSerializer<Asn> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("io.github.kotlinmania.ramanet.Asn", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Asn) {
        encoder.encodeInt(value.asUInt().toInt())
    }

    override fun deserialize(decoder: Decoder): Asn {
        val num = decoder.decodeInt().toUInt()
        return Asn.parse(num)
    }
}
