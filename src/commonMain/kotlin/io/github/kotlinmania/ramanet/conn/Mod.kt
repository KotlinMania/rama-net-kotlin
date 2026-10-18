// port-lint: source conn.rs
package io.github.kotlinmania.ramanet.conn

import io.github.kotlinmania.ramanet.isConnectionError as rootIsConnectionError

/**
 * Check if the error is a connection error, in which case the error can be ignored.
 */
public fun isConnectionError(e: Throwable): Boolean = rootIsConnectionError(e)
