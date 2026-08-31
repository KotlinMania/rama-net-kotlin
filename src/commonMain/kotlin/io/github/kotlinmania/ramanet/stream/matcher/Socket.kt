// port-lint: source stream/matcher/socket.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.stream.Socket

/**
 * Matcher based on the socket address of the peer.
 */
data class SocketAddressMatcher(
    val addr: SocketAddress,
    val optional: Boolean = false,
) {
    fun matches(stream: Socket?): Boolean {
        val peer = stream?.peerAddr() ?: return optional
        return peer == addr
    }

    companion object {
        fun new(addr: SocketAddress): SocketAddressMatcher = SocketAddressMatcher(addr, optional = false)

        fun optional(addr: SocketAddress): SocketAddressMatcher = SocketAddressMatcher(addr, optional = true)
    }
}
