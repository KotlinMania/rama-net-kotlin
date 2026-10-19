// port-lint: source proxy/request.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.proxy

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

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
