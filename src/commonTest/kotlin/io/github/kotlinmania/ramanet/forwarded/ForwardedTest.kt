package io.github.kotlinmania.ramanet.forwarded

import io.github.kotlinmania.ramanet.Protocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForwardedTest {
    @Test
    fun testProtocolFromStr() {
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.parse("http"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.parse("https"))
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.parse("HTTP"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.parse("HTTPS"))
        assertFailsWith<IllegalArgumentException> {
            ForwardedProtocol.parse("ftp")
        }
    }

    @Test
    fun testProtocolSecure() {
        assertFalse(ForwardedProtocol.HTTP.isSecure())
        assertTrue(ForwardedProtocol.HTTPS.isSecure())
        assertTrue(ForwardedProtocol.HTTP.isHttp())
        assertTrue(ForwardedProtocol.HTTPS.isHttp())
    }

    @Test
    fun testProtocolConversions() {
        assertEquals(Protocol.Http, ForwardedProtocol.HTTP.toProtocol())
        assertEquals(Protocol.Https, ForwardedProtocol.HTTPS.toProtocol())
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.fromProtocol(Protocol.Http))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.fromProtocol(Protocol.Https))
    }

    @Test
    fun testForwardedVersionParse() {
        assertEquals(ForwardedVersion.Http09, ForwardedVersion.parse("0.9"))
        assertEquals(ForwardedVersion.Http10, ForwardedVersion.parse("1"))
        assertEquals(ForwardedVersion.Http10, ForwardedVersion.parse("1.0"))
        assertEquals(ForwardedVersion.Http11, ForwardedVersion.parse("1.1"))
        assertEquals(ForwardedVersion.H2, ForwardedVersion.parse("2"))
        assertEquals(ForwardedVersion.H2, ForwardedVersion.parse("2.0"))
        assertEquals(ForwardedVersion.H3, ForwardedVersion.parse("3"))
        assertEquals(ForwardedVersion.H3, ForwardedVersion.parse("3.0"))

        assertFailsWith<IllegalArgumentException> {
            ForwardedVersion.parse("4")
        }
    }
}
