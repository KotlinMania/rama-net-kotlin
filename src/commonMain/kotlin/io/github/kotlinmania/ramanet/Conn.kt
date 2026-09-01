// port-lint: source rama-net/src/conn.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * Check if the error is a connection error, in which case the error can be ignored.
 */
@HiddenFromObjC
public fun isConnectionError(e: Throwable): Boolean {
    val message = e.message?.lowercase() ?: ""
    val className = e::class.simpleName?.lowercase() ?: ""
    return className.contains("connectionrefused") ||
        className.contains("connectionreset") ||
        className.contains("connectionaborted") ||
        className.contains("notconnected") ||
        className.contains("brokenpipe") ||
        className.contains("eof") ||
        className.contains("closed") ||
        message.contains("connection refused") ||
        message.contains("connection reset") ||
        message.contains("connection aborted") ||
        message.contains("not connected") ||
        message.contains("broken pipe") ||
        message.contains("unexpected eof") ||
        message.contains("end of file") ||
        message.contains("channel closed") ||
        message.contains("socket closed")
}
