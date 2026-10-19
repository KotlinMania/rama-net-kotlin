package io.github.kotlinmania.ramanet.client

import io.github.kotlinmania.ramanet.address.HostWithPort

/**
 * Target [HostWithPort] which if found in extensions is to be used by a connector
 * such as a TCPConnector instead of the requested address, unless a proxy is requested.
 */
public data class ConnectorTarget(
    public val hostWithPort: HostWithPort,
) {
    public val target: HostWithPort
        get() = hostWithPort

    public companion object {
        public fun new(hostWithPort: HostWithPort): ConnectorTarget = ConnectorTarget(hostWithPort)
    }
}
