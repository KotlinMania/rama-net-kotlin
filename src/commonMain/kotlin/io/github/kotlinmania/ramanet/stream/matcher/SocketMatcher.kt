package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.stream.Socket

/**
 * A matcher to match on a [Socket].
 */
public class SocketMatcher(
    private val predicate: (Socket?) -> Boolean,
    private val negate: Boolean = false,
) {
    public fun matches(stream: Socket?): Boolean {
        val result = predicate(stream)
        return if (negate) !result else result
    }

    public fun and(other: SocketMatcher): SocketMatcher =
        SocketMatcher(
            predicate = { s -> this.matches(s) && other.matches(s) },
            negate = false,
        )

    public fun or(other: SocketMatcher): SocketMatcher =
        SocketMatcher(
            predicate = { s -> this.matches(s) || other.matches(s) },
            negate = false,
        )

    public fun negate(): SocketMatcher =
        SocketMatcher(
            predicate = this.predicate,
            negate = !this.negate,
        )

    public fun andSocketAddr(addr: SocketAddress): SocketMatcher = and(socketAddr(addr))

    public fun andOptionalSocketAddr(addr: SocketAddress): SocketMatcher = and(optionalSocketAddr(addr))

    public fun orSocketAddr(addr: SocketAddress): SocketMatcher = or(socketAddr(addr))

    public fun orOptionalSocketAddr(addr: SocketAddress): SocketMatcher = or(optionalSocketAddr(addr))

    public fun andLoopback(): SocketMatcher = and(loopback())

    public fun andOptionalLoopback(): SocketMatcher = and(optionalLoopback())

    public fun orLoopback(): SocketMatcher = or(loopback())

    public fun orOptionalLoopback(): SocketMatcher = or(optionalLoopback())

    public fun andPort(port: UShort): SocketMatcher = and(port(port))

    public fun andOptionalPort(port: UShort): SocketMatcher = and(optionalPort(port))

    public fun orPort(port: UShort): SocketMatcher = or(port(port))

    public fun orOptionalPort(port: UShort): SocketMatcher = or(optionalPort(port))

    public fun andPrivateIpNet(): SocketMatcher = and(privateIpNet())

    public fun andOptionalPrivateIpNet(): SocketMatcher = and(optionalPrivateIpNet())

    public fun orPrivateIpNet(): SocketMatcher = or(privateIpNet())

    public fun orOptionalPrivateIpNet(): SocketMatcher = or(optionalPrivateIpNet())

    public fun andCustom(predicate: (Socket?) -> Boolean): SocketMatcher = and(custom(predicate))

    public fun orCustom(predicate: (Socket?) -> Boolean): SocketMatcher = or(custom(predicate))

    public fun clone(): SocketMatcher =
        SocketMatcher(
            predicate = this.predicate,
            negate = this.negate,
        )

    public companion object {
        public fun socketAddr(addr: SocketAddress): SocketMatcher {
            val m = SocketAddressMatcher.new(addr)
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun optionalSocketAddr(addr: SocketAddress): SocketMatcher {
            val m = SocketAddressMatcher.optional(addr)
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun loopback(): SocketMatcher {
            val m = LoopbackMatcher.new()
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun optionalLoopback(): SocketMatcher {
            val m = LoopbackMatcher.optional()
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun port(port: UShort): SocketMatcher {
            val m = PortMatcher.new(port)
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun optionalPort(port: UShort): SocketMatcher {
            val m = PortMatcher.optional(port)
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun privateIpNet(): SocketMatcher {
            val m = PrivateIpNetMatcher.new()
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun optionalPrivateIpNet(): SocketMatcher {
            val m = PrivateIpNetMatcher.optional()
            return SocketMatcher({ s -> m.matches(s) })
        }

        public fun custom(predicate: (Socket?) -> Boolean): SocketMatcher =
            SocketMatcher(predicate)
    }
}
