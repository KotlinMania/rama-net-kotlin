// port-lint: source socket/opts.rs
package io.github.kotlinmania.ramanet.socket

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import io.github.kotlinmania.socket2.Domain as Socket2Domain
import io.github.kotlinmania.socket2.SocketProtocol as Socket2Protocol
import io.github.kotlinmania.socket2.SocketType as Socket2Type
import io.github.kotlinmania.socket2.TcpKeepalive as SocketTcpKeepAlive

/**
 * Specification of the communication domain for a socket.
 */
@Serializable
public enum class Domain {
    /** Domain for IPv4 communication, corresponding to `AF_INET`. */
    IPv4,

    /** Domain for IPv6 communication, corresponding to `AF_INET6`. */
    IPv6,

    /** Domain for Unix socket communication, corresponding to `AF_UNIX`. */
    Unix,
    ;

    public fun asSocketDomain(): Socket2Domain =
        when (this) {
            IPv4 -> Socket2Domain.IPV4
            IPv6 -> Socket2Domain.IPV6
            Unix -> Socket2Domain.UNIX
        }
}

/**
 * Protocol specification used for creating sockets.
 */
@Serializable
public enum class SocketOptProtocol {
    ICMPV4,
    ICMPV6,
    TCP,
    UDP,
    MPTCP,
    DCCP,
    SCTP,
    ;

    public fun asSocketProtocol(): Socket2Protocol =
        when (this) {
            ICMPV4 -> Socket2Protocol.ICMPV4
            ICMPV6 -> Socket2Protocol.ICMPV6
            TCP -> Socket2Protocol.TCP
            UDP -> Socket2Protocol.UDP
            MPTCP -> Socket2Protocol.MPTCP
            DCCP -> Socket2Protocol.DCCP
            SCTP -> Socket2Protocol.SCTP
        }
}

/**
 * Specification of communication semantics on a socket.
 */
@Serializable
public enum class SocketOptType {
    Stream,
    Datagram,
    DCCP,
    SequencePacket,
    Raw,
    ;

    public fun asSocketType(): Socket2Type =
        when (this) {
            Stream -> Socket2Type.STREAM
            Datagram -> Socket2Type.DGRAM
            DCCP -> Socket2Type.DCCP
            SequencePacket -> Socket2Type.SEQPACKET
            Raw -> Socket2Type.RAW
        }
}

/**
 * Configures a socket's TCP keepalive parameters.
 */
@Serializable
public data class TcpKeepAlive(
    public val time: Duration? = null,
    public val interval: Duration? = null,
    public val retries: UInt? = null,
) {
    public fun intoSocketKeepAlive(): SocketTcpKeepAlive {
        var ka = SocketTcpKeepAlive.new()
        if (time != null) {
            ka = ka.withTime(time)
        }
        if (interval != null) {
            ka = ka.withInterval(interval)
        }
        if (retries != null) {
            ka = ka.withRetries(retries)
        }
        return ka
    }

    public companion object {
        public fun default(): TcpKeepAlive = TcpKeepAlive()
    }
}

/**
 * Options and types in function of creating sockets.
 */
@Serializable
public data class SocketOptions(
    public val domain: Domain = Domain.IPv4,
    public val type: SocketOptType = SocketOptType.Stream,
    public val protocol: SocketOptProtocol? = null,
    public val address: SocketAddress? = null,
    public val device: DeviceName? = null,
    public val broadcast: Boolean? = null,
    public val keepAlive: Boolean? = null,
    public val linger: Duration? = null,
    public val outOfBandInline: Boolean? = null,
    public val passcred: Boolean? = null,
    public val recvBufferSize: Int? = null,
    public val readTimeout: Duration? = null,
    public val reuseAddress: Boolean? = null,
    public val reusePort: Boolean? = null,
    public val sendBufferSize: Int? = null,
    public val writeTimeout: Duration? = null,
    public val ttl: UInt? = null,
    public val tos: UInt? = null,
    public val recvTos: Boolean? = null,
    public val onlyV6: Boolean? = null,
    public val tcpNoDelay: Boolean? = null,
    public val tcpKeepAlive: TcpKeepAlive? = null,
) {
    /**
     * Validate options and return a [SerdeResult].
     */
    public fun validate(): SerdeResult<SocketOptions> =
        if (recvBufferSize != null && recvBufferSize < 0) {
            SerdeResult.failure(SerdeError.custom("recvBufferSize must be non-negative"))
        } else if (sendBufferSize != null && sendBufferSize < 0) {
            SerdeResult.failure(SerdeError.custom("sendBufferSize must be non-negative"))
        } else {
            SerdeResult.success(this)
        }

    public companion object {
        public fun defaultTcp(): SocketOptions = SocketOptions()

        public fun defaultTcpV6(): SocketOptions =
            SocketOptions(
                domain = Domain.IPv6,
                type = SocketOptType.Stream,
                protocol = SocketOptProtocol.TCP,
            )

        public fun defaultUdp(): SocketOptions =
            SocketOptions(
                domain = Domain.IPv4,
                type = SocketOptType.Datagram,
                protocol = SocketOptProtocol.UDP,
            )

        public fun defaultUdpV6(): SocketOptions =
            SocketOptions(
                domain = Domain.IPv6,
                type = SocketOptType.Datagram,
                protocol = SocketOptProtocol.UDP,
            )
    }
}
