// port-lint: tests rama-net/src/proto.rs
package io.github.kotlinmania.ramanet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProtoTest {
    @Test
    fun testFromStr() {
        assertEquals(NetProtocol.Http, NetProtocol.parse("http"))
        assertEquals(NetProtocol.Http, NetProtocol.parse(""))
        assertEquals(NetProtocol.Https, NetProtocol.parse("https"))
        assertEquals(NetProtocol.Ws, NetProtocol.parse("ws"))
        assertEquals(NetProtocol.Wss, NetProtocol.parse("wss"))
        assertEquals(NetProtocol.Socks5, NetProtocol.parse("socks5"))
        assertEquals(NetProtocol.Socks5h, NetProtocol.parse("socks5h"))
        assertEquals(NetProtocol.Custom("custom"), NetProtocol.parse("custom"))
    }

    @Test
    fun testSchemeIsSecure() {
        assertFalse(NetProtocol.Http.isSecure())
        assertTrue(NetProtocol.Https.isSecure())
        assertFalse(NetProtocol.Socks5.isSecure())
        assertFalse(NetProtocol.Socks5h.isSecure())
        assertFalse(NetProtocol.Ws.isSecure())
        assertTrue(NetProtocol.Wss.isSecure())
        assertFalse(NetProtocol.fromStatic("custom").isSecure())
    }

    @Test
    fun testTryToExtractProtocolFromUriScheme() {
        assertEquals(Pair(NetProtocol.Http, 7), NetProtocol.tryToExtractProtocolFromUriScheme("http://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.Https, 8), NetProtocol.tryToExtractProtocolFromUriScheme("https://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.Ws, 5), NetProtocol.tryToExtractProtocolFromUriScheme("ws://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.Wss, 6), NetProtocol.tryToExtractProtocolFromUriScheme("wss://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.Socks5, 9), NetProtocol.tryToExtractProtocolFromUriScheme("socks5://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.Socks5h, 10), NetProtocol.tryToExtractProtocolFromUriScheme("socks5h://example.com".encodeToByteArray()))
        assertEquals(Pair(NetProtocol.fromStatic("custom"), 9), NetProtocol.tryToExtractProtocolFromUriScheme("custom://example.com".encodeToByteArray()))
        assertEquals(Pair(null, 0), NetProtocol.tryToExtractProtocolFromUriScheme("example.com".encodeToByteArray()))
        assertEquals(Pair(null, 0), NetProtocol.tryToExtractProtocolFromUriScheme("127.0.0.1".encodeToByteArray()))
        assertEquals(Pair(null, 0), NetProtocol.tryToExtractProtocolFromUriScheme("127.0.0.1:8080".encodeToByteArray()))

        assertFailsWith<IllegalArgumentException> {
            NetProtocol.tryToExtractProtocolFromUriScheme(ByteArray(0))
        }
    }
}
