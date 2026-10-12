// port-lint: source stream/socket.rs
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
) {
    companion object {
        fun new(localAddr: SocketAddress?, peerAddr: SocketAddress): SocketInfo =
            SocketInfo(localAddr = localAddr, peerAddr = peerAddr)
    }
}

/**
 * Information about the socket on the egress end.
 */
data class ClientSocketInfo(
    val inner: SocketInfo,
) {
    val localAddr: SocketAddress? get() = inner.localAddr

    val peerAddr: SocketAddress get() = inner.peerAddr

    fun asSocketInfo(): SocketInfo = inner

    companion object {
        fun new(info: SocketInfo): ClientSocketInfo = ClientSocketInfo(inner = info)
    }
}
