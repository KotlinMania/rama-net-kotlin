// port-lint: source rama-net/src/client/mod.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.client

import io.github.kotlinmania.ramanet.address.HostWithPort
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Target [HostWithPort] which if found in extensions is to be used by a connector
 * such as a TCPConnector instead of the requested address, unless a proxy is requested.
 */
@HiddenFromObjC
public data class ConnectorTarget(
    val hostWithPort: HostWithPort,
)
