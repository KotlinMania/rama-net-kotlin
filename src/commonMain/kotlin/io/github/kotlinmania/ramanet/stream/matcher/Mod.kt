// port-lint: source stream/matcher/mod.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.stream.Socket

/**
 * A matcher to match on a [Socket].
 */
class SocketMatcher(
    private val predicate: (Socket?) -> Boolean,
    private val negate: Boolean = false,
) {
    fun matches(stream: Socket?): Boolean {
        val result = predicate(stream)
        return if (negate) !result else result
    }

    fun and(other: SocketMatcher): SocketMatcher =
        SocketMatcher(
            predicate = { s -> this.matches(s) && other.matches(s) },
            negate = false,
        )

    fun or(other: SocketMatcher): SocketMatcher =
        SocketMatcher(
            predicate = { s -> this.matches(s) || other.matches(s) },
            negate = false,
        )

    fun negate(): SocketMatcher =
        SocketMatcher(
            predicate = this.predicate,
            negate = !this.negate,
        )

    fun andSocketAddr(addr: SocketAddress): SocketMatcher = and(socketAddr(addr))

    fun andOptionalSocketAddr(addr: SocketAddress): SocketMatcher = and(optionalSocketAddr(addr))

    fun orSocketAddr(addr: SocketAddress): SocketMatcher = or(socketAddr(addr))

    fun orOptionalSocketAddr(addr: SocketAddress): SocketMatcher = or(optionalSocketAddr(addr))

    fun andLoopback(): SocketMatcher = and(loopback())

    fun andOptionalLoopback(): SocketMatcher = and(optionalLoopback())

    fun orLoopback(): SocketMatcher = or(loopback())

    fun orOptionalLoopback(): SocketMatcher = or(optionalLoopback())

    fun andPort(port: UShort): SocketMatcher = and(port(port))

    fun andOptionalPort(port: UShort): SocketMatcher = and(optionalPort(port))

    fun orPort(port: UShort): SocketMatcher = or(port(port))

    fun orOptionalPort(port: UShort): SocketMatcher = or(optionalPort(port))

    fun andPrivateIpNet(): SocketMatcher = and(privateIpNet())

    fun andOptionalPrivateIpNet(): SocketMatcher = and(optionalPrivateIpNet())

    fun orPrivateIpNet(): SocketMatcher = or(privateIpNet())

    fun orOptionalPrivateIpNet(): SocketMatcher = or(optionalPrivateIpNet())

    fun andCustom(predicate: (Socket?) -> Boolean): SocketMatcher = and(custom(predicate))

    fun orCustom(predicate: (Socket?) -> Boolean): SocketMatcher = or(custom(predicate))

    companion object {
        fun socketAddr(addr: SocketAddress): SocketMatcher {
            val m = SocketAddressMatcher.new(addr)
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun optionalSocketAddr(addr: SocketAddress): SocketMatcher {
            val m = SocketAddressMatcher.optional(addr)
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun loopback(): SocketMatcher {
            val m = LoopbackMatcher.new()
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun optionalLoopback(): SocketMatcher {
            val m = LoopbackMatcher.optional()
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun port(port: UShort): SocketMatcher {
            val m = PortMatcher.new(port)
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun optionalPort(port: UShort): SocketMatcher {
            val m = PortMatcher.optional(port)
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun privateIpNet(): SocketMatcher {
            val m = PrivateIpNetMatcher.new()
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun optionalPrivateIpNet(): SocketMatcher {
            val m = PrivateIpNetMatcher.optional()
            return SocketMatcher({ s -> m.matches(s) })
        }

        fun custom(predicate: (Socket?) -> Boolean): SocketMatcher =
            SocketMatcher(predicate)
    }
}
