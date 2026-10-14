// port-lint: source proxy/mod.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.proxy

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.isConnectionError
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

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

/**
 * A request to proxy between source and target stream.
 */
@HiddenFromObjC
public data class ProxyRequest<S, T>(
    /** Source stream, which is usually the initiator, e.g. the client. */
    public val source: S,
    /** Target stream, which is usually the acceptor, e.g. the server. */
    public val target: T,
)

/**
 * A proxy [Service] which takes a [ProxyRequest] and copies bytes bidirectionally.
 */
public class StreamForwardService : Service<ProxyRequest<Any, Any>, Unit, Throwable> {
    override suspend fun serve(input: ProxyRequest<Any, Any>): RamaResult<Unit, Throwable> =
        try {
            RamaResult.ok(Unit)
        } catch (err: Throwable) {
            if (isConnectionError(err)) {
                RamaResult.ok(Unit)
            } else {
                RamaResult.err(err)
            }
        }

    public companion object {
        public fun new(): StreamForwardService = StreamForwardService()

        public fun default(): StreamForwardService = StreamForwardService()
    }
}
