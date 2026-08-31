// port-lint: source rama-net/src/stream/matcher/private_ip.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.ParseUtils
import io.github.kotlinmania.ramanet.stream.Socket

/**
 * Matcher based on the IP part of the socket address of the peer,
 * matching only if the IP is considered a private address.
 */
data class PrivateIpNetMatcher(
    val optional: Boolean = false,
) {
    fun matches(stream: Socket?): Boolean {
        val peer = stream?.peerAddr() ?: return optional
        return isPrivateIp(peer.ip)
    }

    companion object {
        fun new(): PrivateIpNetMatcher = PrivateIpNetMatcher(optional = false)

        fun optional(): PrivateIpNetMatcher = PrivateIpNetMatcher(optional = true)

        fun isPrivateIp(ip: String): Boolean {
            val clean = if (ip.startsWith('[') && ip.endsWith(']')) ip.substring(1, ip.length - 1) else ip
            if (ParseUtils.isValidIpv4(clean)) {
                val parts = clean.split('.').map { it.toInt() }
                if (parts.size != 4) return false
                val a = parts[0]
                val b = parts[1]
                return when {
                    a == 0 -> true // 0.0.0.0/8
                    a == 10 -> true // 10.0.0.0/8
                    a == 100 && (b in 64..127) -> true // 100.64.0.0/10
                    a == 127 -> true // 127.0.0.0/8
                    a == 169 && b == 254 -> true // 169.254.0.0/16
                    a == 172 && (b in 16..31) -> true // 172.16.0.0/12
                    a == 192 && b == 168 -> true // 192.168.0.0/16
                    else -> false
                }
            }
            if (ParseUtils.isValidIpv6(clean)) {
                val lower = clean.lowercase()
                return when {
                    lower == "::" -> true // ::/128 unspecified
                    lower == "::1" || lower == "0:0:0:0:0:0:0:1" -> true // ::1/128 loopback
                    lower.startsWith("fc") || lower.startsWith("fd") -> true // fc00::/7 unique local
                    lower.startsWith("fe80:") || lower.startsWith("fe8") || lower.startsWith("fe9") || lower.startsWith("fea") || lower.startsWith("feb") -> true // fe80::/10 link-local
                    else -> false
                }
            }
            return false
        }
    }
}
