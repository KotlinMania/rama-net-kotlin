// port-lint: source transport.rs
package io.github.kotlinmania.ramanet

import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort

/**
 * The protocol used for the transport layer.
 */
enum class TransportProtocol {
    Tcp,
    Udp,
}

/**
 * The context as relevant to the transport layer.
 */
data class TransportContext(
    val protocol: TransportProtocol,
    val authority: HostWithOptPort,
    val appProtocol: Protocol? = null,
) {
    fun hostWithPort(): HostWithPort? {
        val port =
            authority.port
                ?: appProtocol?.defaultPort()
                ?: return null
        return HostWithPort(authority.host, port)
    }
}
