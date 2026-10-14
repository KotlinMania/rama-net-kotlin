// port-lint: source client/conn.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.client

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.BoxService
import io.github.kotlinmania.ramacore.service.Service
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * The established connection to a server returned for the http client to be used.
 */
@HiddenFromObjC
public data class EstablishedClientConnection<out S, out Input>(
    public val input: Input,
    public val conn: S,
)

/**
 * Glue trait that is used as the Connector trait bound for clients establishing
 * a connection on one layer or another.
 */
@HiddenFromObjC
public fun interface ConnectorService<Input, Connection, Error> {
    /**
     * Establish a connection, which often involves some kind of handshake or connection revival.
     */
    public suspend fun connect(input: Input): Result<EstablishedClientConnection<Connection, Input>>
}

/**
 * A [ConnectorService] which only job is to box the created service by the inner connector.
 */
@HiddenFromObjC
public data class BoxedConnectorService<S>(
    public val connector: S,
) {
    public companion object {
        public fun <S> new(connector: S): BoxedConnectorService<S> = BoxedConnectorService(connector)
    }
}

/**
 * Serve an input by delegating to a [Service] connector and boxing its inner service.
 */
@HiddenFromObjC
public suspend fun <S, Input, Output : Any, SvcError : Any, ConnError : Any, Svc> BoxedConnectorService<S>.serve(
    input: Input,
): RamaResult<EstablishedClientConnection<BoxService<Input, Output, SvcError>, Input>, ConnError>
    where S : Service<Input, EstablishedClientConnection<Svc, Input>, ConnError>,
          Svc : Service<Input, Output, SvcError> {
    val res = connector.serve(input)
    if (res.isFailure()) {
        return RamaResult.err(res.errorOrNull()!!)
    }
    val established = res.getOrNull()!!
    return RamaResult.ok(
        EstablishedClientConnection(
            input = established.input,
            conn = established.conn.boxed(),
        ),
    )
}
