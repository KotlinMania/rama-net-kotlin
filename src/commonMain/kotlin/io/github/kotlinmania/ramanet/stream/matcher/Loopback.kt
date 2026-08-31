// port-lint: source rama-net/src/stream/matcher/loopback.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.ParseUtils
import io.github.kotlinmania.ramanet.stream.Socket

/**
 * Matcher based on the IP part of the socket address of the peer,
 * matching only if the IP is a loopback address.
 */
data class LoopbackMatcher(
    val optional: Boolean = false,
) {
    fun matches(stream: Socket?): Boolean {
        val peer = stream?.peerAddr() ?: return optional
        return isLoopback(peer.ip)
    }

    companion object {
        fun new(): LoopbackMatcher = LoopbackMatcher(optional = false)

        fun optional(): LoopbackMatcher = LoopbackMatcher(optional = true)

        fun isLoopback(ip: String): Boolean {
            val clean = if (ip.startsWith('[') && ip.endsWith(']')) ip.substring(1, ip.length - 1) else ip
            if (ParseUtils.isValidIpv4(clean)) {
                return clean.startsWith("127.")
            }
            if (ParseUtils.isValidIpv6(clean)) {
                return clean == "::1" || clean == "0:0:0:0:0:0:0:1" || clean == "0000:0000:0000:0000:0000:0000:0000:0001"
            }
            return false
        }
    }
}
