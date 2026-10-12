// port-lint: source client/conn.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.client

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * The established connection to a server returned for the http client to be used.
 */
@HiddenFromObjC
public data class EstablishedClientConnection<S, Input>(
    val input: Input,
    val conn: S,
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
    val connector: S,
) {
    companion object {
        fun <S> new(connector: S): BoxedConnectorService<S> = BoxedConnectorService(connector)
    }
}
