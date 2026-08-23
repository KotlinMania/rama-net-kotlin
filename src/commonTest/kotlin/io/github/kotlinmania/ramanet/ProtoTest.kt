// port-lint: tests proto.rs
package io.github.kotlinmania.ramanet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProtoTest {
    @Test
    fun testFromStr() {
        assertEquals(Protocol.Http, Protocol.parse("http"))
        assertEquals(Protocol.Http, Protocol.parse(""))
        assertEquals(Protocol.Https, Protocol.parse("https"))
        assertEquals(Protocol.Ws, Protocol.parse("ws"))
        assertEquals(Protocol.Wss, Protocol.parse("wss"))
        assertEquals(Protocol.Socks5, Protocol.parse("socks5"))
        assertEquals(Protocol.Socks5h, Protocol.parse("socks5h"))
        assertEquals(Protocol.Custom("custom"), Protocol.parse("custom"))
    }

    @Test
    fun testSchemeIsSecure() {
        assertFalse(Protocol.Http.isSecure())
        assertTrue(Protocol.Https.isSecure())
        assertFalse(Protocol.Socks5.isSecure())
        assertFalse(Protocol.Socks5h.isSecure())
        assertFalse(Protocol.Ws.isSecure())
        assertTrue(Protocol.Wss.isSecure())
        assertFalse(Protocol.fromStatic("custom").isSecure())
    }

    @Test
    fun testTryToExtractProtocolFromUriScheme() {
        assertEquals(Pair(Protocol.Http, 7), Protocol.tryToExtractProtocolFromUriScheme("http://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.Https, 8), Protocol.tryToExtractProtocolFromUriScheme("https://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.Ws, 5), Protocol.tryToExtractProtocolFromUriScheme("ws://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.Wss, 6), Protocol.tryToExtractProtocolFromUriScheme("wss://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.Socks5, 9), Protocol.tryToExtractProtocolFromUriScheme("socks5://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.Socks5h, 10), Protocol.tryToExtractProtocolFromUriScheme("socks5h://example.com".encodeToByteArray()))
        assertEquals(Pair(Protocol.fromStatic("custom"), 9), Protocol.tryToExtractProtocolFromUriScheme("custom://example.com".encodeToByteArray()))
        assertEquals(Pair(null, 0), Protocol.tryToExtractProtocolFromUriScheme("example.com".encodeToByteArray()))
        assertEquals(Pair(null, 0), Protocol.tryToExtractProtocolFromUriScheme("127.0.0.1".encodeToByteArray()))
        assertEquals(Pair(null, 0), Protocol.tryToExtractProtocolFromUriScheme("127.0.0.1:8080".encodeToByteArray()))

        assertFailsWith<IllegalArgumentException> {
            Protocol.tryToExtractProtocolFromUriScheme(ByteArray(0))
        }
    }
}
