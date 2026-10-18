// port-lint: source transport.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet

import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * The protocol used for the transport layer.
 */
public enum class TransportProtocol {
    Tcp,
    Udp,
}

/**
 * The context as relevant to the transport layer.
 */
public data class TransportContext(
    public val protocol: TransportProtocol,
    public val authority: HostWithOptPort,
    public val appProtocol: NetProtocol? = null,
    public val httpVersion: String? = null,
) {
    public fun hostWithPort(): HostWithPort? {
        val port =
            authority.port
                ?: appProtocol?.defaultPort()
                ?: if (httpVersion != null) NetProtocol.HTTP_DEFAULT_PORT else return null
        return HostWithPort(authority.host, port)
    }
}

/**
 * Utility interface to turn combined types into a [TransportContext].
 */
@HiddenFromObjC
public fun interface TryRefIntoTransportContext {
    public fun tryRefIntoTransportCtx(): Result<TransportContext>
}
