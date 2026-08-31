// port-lint: source rama-net/src/stream/matcher/port.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.stream.Socket

/**
 * Matcher based on the port part of the socket address of the peer.
 */
data class PortMatcher(
    val port: UShort,
    val optional: Boolean = false,
) {
    fun matches(stream: Socket?): Boolean {
        val peer = stream?.peerAddr() ?: return optional
        return peer.port == port
    }

    companion object {
        fun new(port: UShort): PortMatcher = PortMatcher(port, optional = false)

        fun optional(port: UShort): PortMatcher = PortMatcher(port, optional = true)
    }
}
