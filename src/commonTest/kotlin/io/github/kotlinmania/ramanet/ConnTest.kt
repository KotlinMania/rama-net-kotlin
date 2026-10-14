package io.github.kotlinmania.ramanet

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramanet.address.Domain
import io.github.kotlinmania.ramanet.address.Host
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.address.reverseDomain
import io.github.kotlinmania.ramanet.fingerprint.Ja4
import io.github.kotlinmania.ramanet.http.RequestContext
import io.github.kotlinmania.ramanet.proxy.ProxyTarget
import io.github.kotlinmania.ramanet.socket.SocketOptProtocol
import io.github.kotlinmania.ramanet.socket.SocketOptType
import io.github.kotlinmania.ramanet.socket.SocketOptions
import io.github.kotlinmania.ramanet.tls.ClientHello
import io.github.kotlinmania.ramanet.tls.SecureTransport
import io.github.kotlinmania.ramanet.tls.TlsTunnel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import io.github.kotlinmania.ramanet.conn.isConnectionError as connIsConnectionError
import io.github.kotlinmania.ramanet.socket.Domain as SocketOptDomain

class ConnTest {
    @Test
    fun testIsConnectionError() {
        assertTrue(isConnectionError(RuntimeException("Connection reset by peer")))
        assertTrue(isConnectionError(IllegalStateException("Connection refused")))
        assertTrue(isConnectionError(Exception("Broken pipe")))
        assertTrue(isConnectionError(Exception("Unexpected EOF")))
        assertTrue(isConnectionError(Exception("Socket closed")))
        assertTrue(connIsConnectionError(Exception("Socket closed")))

        assertFalse(isConnectionError(IllegalArgumentException("invalid input format")))
        assertFalse(isConnectionError(NullPointerException("null value encountered")))
    }

    @Test
    fun testRequestContext() {
        val ctx =
            RequestContext
                .fromParts(
                    uriScheme = "http",
                    uriHost = "example.com",
                    uriPort = 8080u,
                ).getOrThrow()
        assertEquals("HTTP/1.1", ctx.httpVersion)
        assertEquals(NetProtocol.HTTP, ctx.protocol)
        assertEquals("example.com:8080", ctx.authority.toString())
        assertFalse(ctx.authorityHasDefaultPort())

        val ext = Extensions.new()
        ext.insert(SecureTransport.withClientHello(ClientHello(Domain.parse("secure.example.com"))))
        val sniCtx = RequestContext.fromParts(extensions = ext).getOrThrow()
        assertEquals(NetProtocol.HTTPS, sniCtx.protocol)
        assertEquals("secure.example.com:443", sniCtx.authority.toString())
        assertTrue(sniCtx.authorityHasDefaultPort())

        val extProxy = Extensions.new()
        extProxy.insert(ProxyTarget.new(HostWithPort.exampleDomainHttp()))
        val proxyCtx = RequestContext.fromParts(extensions = extProxy).getOrThrow()
        assertEquals("example.com:80", proxyCtx.authority.toString())

        val hwp =
            RequestContext(
                authority = HostWithOptPort.parse("echo.ramaproxy.org"),
                protocol = NetProtocol.HTTP,
            ).hostWithPort()
        assertEquals("echo.ramaproxy.org:80", hwp.toString())
    }

    @Test
    fun testSocketOptionsAndJa4AndReverseDomain() {
        val opts = SocketOptions.defaultTcpV6()
        assertEquals(SocketOptDomain.IPv6, opts.domain)
        assertEquals(SocketOptType.Stream, opts.type)
        assertEquals(SocketOptProtocol.TCP, opts.protocol)
        assertTrue(opts.validate().isSuccess)

        assertEquals("com.example.", reverseDomain("example.com."))
        assertEquals("com.example.sub.", reverseDomain("sub.example.com"))
        assertEquals(".", reverseDomain(""))

        assertEquals("000000000000", Ja4.hashFields(emptyList()))
        assertEquals(12, Ja4.hashFields(listOf("1301", "1302")).length)

        val tunnel = TlsTunnel.new(Host.example())
        assertEquals("example.com", tunnel.serverHost.toString())
    }
}
