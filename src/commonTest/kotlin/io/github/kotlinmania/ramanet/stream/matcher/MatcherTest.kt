// port-lint: tests rama-net/src/stream/matcher/loopback.rs
package io.github.kotlinmania.ramanet.stream.matcher

import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.stream.Socket
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MatcherTest {
    private data class FakeSocket(
        val local: SocketAddress?,
        val peer: SocketAddress?,
    ) : Socket {
        override fun localAddr(): SocketAddress? = local
        override fun peerAddr(): SocketAddress? = peer
    }

    @Test
    fun testLoopbackMatcher() {
        val matcher = LoopbackMatcher.new()
        val optMatcher = LoopbackMatcher.optional()

        val nullSocket = FakeSocket(null, null)
        assertFalse(matcher.matches(nullSocket))
        assertTrue(optMatcher.matches(nullSocket))

        val nonLoopbackV4 = FakeSocket(null, SocketAddress("192.168.0.1", 8080u))
        assertFalse(matcher.matches(nonLoopbackV4))

        val loopbackV4 = FakeSocket(null, SocketAddress("127.0.0.1", 8080u))
        assertTrue(matcher.matches(loopbackV4))

        val loopbackV4Alt = FakeSocket(null, SocketAddress("127.3.2.1", 8080u))
        assertTrue(matcher.matches(loopbackV4Alt))

        val loopbackV6 = FakeSocket(null, SocketAddress("::1", 8080u))
        assertTrue(matcher.matches(loopbackV6))
    }

    @Test
    fun testPortMatcher() {
        val matcher = PortMatcher.new(8080u)
        val optMatcher = PortMatcher.optional(8080u)

        val nullSocket = FakeSocket(null, null)
        assertFalse(matcher.matches(nullSocket))
        assertTrue(optMatcher.matches(nullSocket))

        val wrongPort = FakeSocket(null, SocketAddress("127.0.0.1", 8081u))
        assertFalse(matcher.matches(wrongPort))

        val correctPort = FakeSocket(null, SocketAddress("127.0.0.1", 8080u))
        assertTrue(matcher.matches(correctPort))

        val differentIpSamePort = FakeSocket(null, SocketAddress("10.0.0.1", 8080u))
        assertTrue(matcher.matches(differentIpSamePort))
    }

    @Test
    fun testPrivateIpNetMatcher() {
        val matcher = PrivateIpNetMatcher.new()
        val optMatcher = PrivateIpNetMatcher.optional()

        val nullSocket = FakeSocket(null, null)
        assertFalse(matcher.matches(nullSocket))
        assertTrue(optMatcher.matches(nullSocket))

        val publicV4 = FakeSocket(null, SocketAddress("1.1.1.1", 8080u))
        assertFalse(matcher.matches(publicV4))

        val privateV4Loopback = FakeSocket(null, SocketAddress("127.0.0.1", 8080u))
        assertTrue(matcher.matches(privateV4Loopback))

        val privateV4ClassC = FakeSocket(null, SocketAddress("192.168.0.24", 8080u))
        assertTrue(matcher.matches(privateV4ClassC))

        val privateV4ClassA = FakeSocket(null, SocketAddress("10.0.1.5", 8080u))
        assertTrue(matcher.matches(privateV4ClassA))

        val privateV6Loopback = FakeSocket(null, SocketAddress("::1", 8080u))
        assertTrue(matcher.matches(privateV6Loopback))
    }

    @Test
    fun testSocketAddressMatcher() {
        val targetAddr = SocketAddress("127.0.0.1", 8080u)
        val matcher = SocketAddressMatcher.new(targetAddr)
        val optMatcher = SocketAddressMatcher.optional(targetAddr)

        val nullSocket = FakeSocket(null, null)
        assertFalse(matcher.matches(nullSocket))
        assertTrue(optMatcher.matches(nullSocket))

        val diffPort = FakeSocket(null, SocketAddress("127.0.0.1", 8081u))
        assertFalse(matcher.matches(diffPort))

        val diffIp = FakeSocket(null, SocketAddress("127.0.0.2", 8080u))
        assertFalse(matcher.matches(diffIp))

        val matching = FakeSocket(null, SocketAddress("127.0.0.1", 8080u))
        assertTrue(matcher.matches(matching))
    }
}
