// port-lint: tests forwarded/proto.rs
package io.github.kotlinmania.ramanet.forwarded

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForwardedProtocolTest {
    @Test
    fun testProtocolFromStr() {
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.fromStr("http"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.fromStr("https"))
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.parse("http"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.parse("https"))
    }

    @Test
    fun testProtocolSecure() {
        assertFalse(ForwardedProtocol.HTTP.isSecure())
        assertTrue(ForwardedProtocol.HTTPS.isSecure())
    }
}
