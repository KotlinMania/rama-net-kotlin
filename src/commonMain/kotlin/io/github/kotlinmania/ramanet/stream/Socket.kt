// port-lint: source stream/mod.rs
package io.github.kotlinmania.ramanet.stream

import io.github.kotlinmania.ramanet.address.SocketAddress

/**
 * Socket interface providing local and peer address access.
 */
interface Socket {
    fun localAddr(): SocketAddress?
    fun peerAddr(): SocketAddress?
}

/**
 * Information about a connected socket.
 */
data class SocketInfo(
    val localAddr: SocketAddress? = null,
    val peerAddr: SocketAddress,
)
