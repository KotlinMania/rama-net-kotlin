package io.github.kotlinmania.ramanet.proxy

import io.github.kotlinmania.ramanet.address.HostWithPort

/**
 * Target [HostWithPort] for a proxy/forwarder service.
 */
public data class ProxyTarget(
    public val target: HostWithPort,
) {
    public val hostWithPort: HostWithPort
        get() = target

    public companion object {
        public fun new(target: HostWithPort): ProxyTarget = ProxyTarget(target)
    }
}
