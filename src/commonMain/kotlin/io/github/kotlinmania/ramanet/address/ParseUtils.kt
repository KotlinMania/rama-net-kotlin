// port-lint: source rama-net/src/address/parse_utils.rs
package io.github.kotlinmania.ramanet.address

internal object ParseUtils {
    /**
     * Splits a `host:port` or `[ipv6]:port` string into host and port parts.
     */
    fun splitPortFromStr(s: String): Pair<String, UShort> {
        val colon = s.lastIndexOf(':')
        if (colon < 0) {
            throw IllegalArgumentException("missing port in address: $s")
        }
        val portStr = s.substring(colon + 1)
        val port =
            portStr.toUShortOrNull()
                ?: throw IllegalArgumentException("invalid port '$portStr' in address: $s")
        val host = s.substring(0, colon)
        return Pair(host, port)
    }

    /**
     * Validates and normalizes an IP string (stripping brackets if IPv6).
     */
    fun tryToParseStrToIp(value: String): String? {
        val clean =
            if (value.startsWith('[') && value.endsWith(']')) {
                value.substring(1, value.length - 1)
            } else {
                value
            }

        return if (isValidIpv4(clean) || isValidIpv6(clean)) {
            clean
        } else {
            null
        }
    }

    fun isValidIpv4(s: String): Boolean {
        val parts = s.split('.')
        if (parts.size != 4) return false
        for (part in parts) {
            if (part.isEmpty() || (part.length > 1 && part.startsWith('0'))) return false
            val num = part.toIntOrNull() ?: return false
            if (num !in 0..255) return false
        }
        return true
    }

    fun isValidIpv6(s: String): Boolean {
        if (s.isEmpty()) return false
        val doubleColon = s.indexOf("::")
        if (doubleColon != -1 && s.indexOf("::", doubleColon + 2) != -1) {
            return false // Only one "::" allowed
        }
        val parts = s.split(':')
        if (parts.size > 8) return false
        if (doubleColon == -1 && parts.size != 8) return false
        for (part in parts) {
            if (part.isEmpty()) continue
            if (part.length > 4) return false
            for (c in part) {
                val valid = (c in '0'..'9') || (c in 'a'..'f') || (c in 'A'..'F')
                if (!valid) return false
            }
        }
        return true
    }
}
