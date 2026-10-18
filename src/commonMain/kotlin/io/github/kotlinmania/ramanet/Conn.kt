// port-lint: source conn.rs
package io.github.kotlinmania.ramanet

/**
 * Check if the error is a connection error, in which case the error can be ignored.
 */
public fun isConnectionError(e: Throwable): Boolean {
    val message = e.message?.lowercase().orEmpty()
    val className = e::class.simpleName?.lowercase().orEmpty()
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
