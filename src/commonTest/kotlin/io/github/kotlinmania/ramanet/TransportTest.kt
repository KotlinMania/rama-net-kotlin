// port-lint: tests rama-net/src/transport.rs
package io.github.kotlinmania.ramanet

import io.github.kotlinmania.ramanet.address.Domain
import io.github.kotlinmania.ramanet.address.Host
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TransportTest {
    @Test
    fun testTransportProtocolEntries() {
        val entries = TransportProtocol.entries
        assertEquals(2, entries.size)
        assertEquals(TransportProtocol.Tcp, entries[0])
        assertEquals(TransportProtocol.Udp, entries[1])
    }

    @Test
    fun testTransportContextHostWithPort() {
        val domain = Domain.parse("example.com")
        val host = Host.Name(domain)
        val authWithPort = HostWithOptPort(host, 8080u)
        val ctxWithPort = TransportContext(TransportProtocol.Tcp, authWithPort)
        val hostPort = ctxWithPort.hostWithPort()
        assertEquals(8080u.toUShort(), hostPort?.port)

        val authWithoutPort = HostWithOptPort(host, null)
        val ctxWithAppProto = TransportContext(TransportProtocol.Tcp, authWithoutPort, NetProtocol.Http)
        assertEquals(80u.toUShort(), ctxWithAppProto.hostWithPort()?.port)

        val ctxWithoutAppProto = TransportContext(TransportProtocol.Tcp, authWithoutPort, null)
        assertNull(ctxWithoutAppProto.hostWithPort())
    }
}
