// port-lint: source socket/svc.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.socket

import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Glue trait that is used as the trait bound for code creating/preparing a socket on one layer or another.
 */
@HiddenFromObjC
public fun interface SocketService<Socket : Any, Error : Any> {
    /**
     * Create a binding to a Unix/Linux/Windows socket.
     */
    public suspend fun bind(interfaceValue: Interface): RamaResult<Socket, Error>
}

/**
 * Adapt any [Service] accepting an [Interface] into a [SocketService].
 */
@HiddenFromObjC
public fun <Socket : Any, Error : Any> Service<Interface, Socket, Error>.asSocketService(): SocketService<Socket, Error> =
    SocketService { interfaceValue -> serve(interfaceValue) }
