package io.github.kotlinmania.ramanet

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConnTest {
    @Test
    fun testIsConnectionError() {
        assertTrue(isConnectionError(RuntimeException("Connection reset by peer")))
        assertTrue(isConnectionError(IllegalStateException("Connection refused")))
        assertTrue(isConnectionError(Exception("Broken pipe")))
        assertTrue(isConnectionError(Exception("Unexpected EOF")))
        assertTrue(isConnectionError(Exception("Socket closed")))

        assertFalse(isConnectionError(IllegalArgumentException("invalid input format")))
        assertFalse(isConnectionError(NullPointerException("null value encountered")))
    }
}
