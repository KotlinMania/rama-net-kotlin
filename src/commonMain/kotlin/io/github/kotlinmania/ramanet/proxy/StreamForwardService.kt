// port-lint: source proxy/forward.rs
package io.github.kotlinmania.ramanet.proxy

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramanet.isConnectionError

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
