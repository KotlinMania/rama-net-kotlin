// port-lint: source socket/opts.rs
package io.github.kotlinmania.ramanet.socket

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.socket2.Socket
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
    public val headerIncluded: Boolean? = null,
    public val headerIncludedV6: Boolean? = null,
    public val ipTransparent: Boolean? = null,
    public val ttl: UInt? = null,
    public val tos: UInt? = null,
    public val recvTos: Boolean? = null,
    public val multicastHopsV6: UInt? = null,
    public val multicastAllV4: Boolean? = null,
    public val multicastAllV6: Boolean? = null,
    public val multicastInterfaceV4: String? = null,
    public val multicastInterfaceV6: UInt? = null,
    public val multicastLoopV4: Boolean? = null,
    public val multicastLoopV6: Boolean? = null,
    public val multicastTtlV4: UInt? = null,
    public val unicastHopsV6: UInt? = null,
    public val onlyV6: Boolean? = null,
    public val recvTclassV6: Boolean? = null,
    public val tclassV6: UInt? = null,
    public val recvHoplimitV6: Boolean? = null,
    public val tcpNoDelay: Boolean? = null,
    public val tcpKeepAlive: TcpKeepAlive? = null,
    public val tcpMaxSegments: UInt? = null,
    public val tcpCongestion: String? = null,
    public val mark: UInt? = null,
    public val tcpCork: Boolean? = null,
    public val tcpQuickAck: Boolean? = null,
    public val tcpThinLinearTimeouts: Boolean? = null,
    public val tcpUserTimeout: Duration? = null,
    public val freebind: Boolean? = null,
    public val freebindIpv6: Boolean? = null,
    public val cpuAffinity: Int? = null,
    public val dccpService: UInt? = null,
    public val dccpCcid: UByte? = null,
    public val dccpServerTimewait: Boolean? = null,
    public val dccpSendCscov: UInt? = null,
    public val dccpRecvCscov: UInt? = null,
    public val dccpQpolicyTxqlen: UInt? = null,
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

    /**
     * Build and configure a [Socket] according to these options.
     */
    public fun tryBuildSocket(): Result<Socket> {
        val socket =
            Socket
                .new(
                    domain = domain.asSocketDomain(),
                    type = type.asSocketType(),
                    protocol = protocol?.asSocketProtocol(),
                ).getOrElse { return Result.failure(it) }

        if (address != null) {
            socket.bind(address.toSockAddr()).getOrElse { return Result.failure(it) }
        }
        if (broadcast != null) {
            socket.setBroadcast(broadcast).getOrElse { return Result.failure(it) }
        }
        if (keepAlive != null) {
            socket.setKeepalive(keepAlive).getOrElse { return Result.failure(it) }
        }
        if (linger != null) {
            socket.setLinger(linger).getOrElse { return Result.failure(it) }
        }
        if (outOfBandInline != null) {
            socket.setOutOfBandInline(outOfBandInline).getOrElse { return Result.failure(it) }
        }
        if (passcred != null) {
            socket.setPasscred(passcred).getOrElse { return Result.failure(it) }
        }
        if (recvBufferSize != null) {
            socket.setRecvBufferSize(recvBufferSize).getOrElse { return Result.failure(it) }
        }
        if (readTimeout != null) {
            socket.setReadTimeout(readTimeout).getOrElse { return Result.failure(it) }
        }
        if (reuseAddress != null) {
            socket.setReuseAddress(reuseAddress).getOrElse { return Result.failure(it) }
        }
        if (sendBufferSize != null) {
            socket.setSendBufferSize(sendBufferSize).getOrElse { return Result.failure(it) }
        }
        if (writeTimeout != null) {
            socket.setWriteTimeout(writeTimeout).getOrElse { return Result.failure(it) }
        }
        if (headerIncluded != null) {
            socket.setHeaderIncludedV4(headerIncluded).getOrElse { return Result.failure(it) }
        }
        if (headerIncludedV6 != null) {
            socket.setHeaderIncludedV6(headerIncludedV6).getOrElse { return Result.failure(it) }
        }
        if (ipTransparent != null) {
            socket.setIpTransparentV4(ipTransparent).getOrElse { return Result.failure(it) }
        }
        if (ttl != null) {
            socket.setTtlV4(ttl).getOrElse { return Result.failure(it) }
        }
        if (tos != null) {
            socket.setTosV4(tos).getOrElse { return Result.failure(it) }
        }
        if (recvTos != null) {
            socket.setRecvTosV4(recvTos).getOrElse { return Result.failure(it) }
        }
        if (multicastLoopV4 != null) {
            socket.setMulticastLoopV4(multicastLoopV4).getOrElse { return Result.failure(it) }
        }
        if (multicastLoopV6 != null) {
            socket.setMulticastLoopV6(multicastLoopV6).getOrElse { return Result.failure(it) }
        }
        if (multicastTtlV4 != null) {
            socket.setMulticastTtlV4(multicastTtlV4).getOrElse { return Result.failure(it) }
        }
        if (multicastHopsV6 != null) {
            socket.setMulticastHopsV6(multicastHopsV6).getOrElse { return Result.failure(it) }
        }
        if (multicastAllV4 != null) {
            socket.setMulticastAllV4(multicastAllV4).getOrElse { return Result.failure(it) }
        }
        if (multicastAllV6 != null) {
            socket.setMulticastAllV6(multicastAllV6).getOrElse { return Result.failure(it) }
        }
        if (multicastInterfaceV4 != null) {
            socket.setMulticastIfV4(multicastInterfaceV4).getOrElse { return Result.failure(it) }
        }
        if (multicastInterfaceV6 != null) {
            socket.setMulticastIfV6(multicastInterfaceV6).getOrElse { return Result.failure(it) }
        }
        if (unicastHopsV6 != null) {
            socket.setUnicastHopsV6(unicastHopsV6).getOrElse { return Result.failure(it) }
        }
        if (onlyV6 != null) {
            socket.setOnlyV6(onlyV6).getOrElse { return Result.failure(it) }
        }
        if (recvHoplimitV6 != null) {
            socket.setRecvHoplimitV6(recvHoplimitV6).getOrElse { return Result.failure(it) }
        }
        if (recvTclassV6 != null) {
            socket.setRecvTclassV6(recvTclassV6).getOrElse { return Result.failure(it) }
        }
        if (tcpKeepAlive != null) {
            socket.setTcpKeepalive(tcpKeepAlive.intoSocketKeepAlive()).getOrElse { return Result.failure(it) }
        }
        if (tcpNoDelay != null) {
            socket.setTcpNodelay(tcpNoDelay).getOrElse { return Result.failure(it) }
        }
        if (reusePort != null) {
            socket.setReusePort(reusePort).getOrElse { return Result.failure(it) }
        }

        return Result.success(socket)
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
