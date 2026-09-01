package io.github.kotlinmania.ramanet.client

import io.github.kotlinmania.ramanet.address.HostWithPort
import kotlin.test.Test
import kotlin.test.assertEquals

class ClientTest {
    @Test
    fun testEstablishedClientConnection() {
        val established = EstablishedClientConnection(input = "example.com:80", conn = 42)
        assertEquals("example.com:80", established.input)
        assertEquals(42, established.conn)
    }

    @Test
    fun testConnectorServiceImplementation() {
        val connector =
            ConnectorService<String, Int, Throwable> { input ->
                Result.success(EstablishedClientConnection(input, 12345))
            }
        // Verify interface contract definition
        val established = EstablishedClientConnection("test-input", 12345)
        assertEquals("test-input", established.input)
        assertEquals(12345, established.conn)
    }

    @Test
    fun testConnectorTarget() {
        val target = ConnectorTarget(HostWithPort.exampleHttp())
        assertEquals("example.com:80", target.hostWithPort.toString())
    }

    @Test
    fun testEitherConn() {
        val connA: EitherConn<String, Int> = EitherConn.A("hello")
        val connB: EitherConn<String, Int> = EitherConn.B(123)
        assertEquals("hello", (connA as EitherConn.A).value)
        assertEquals(123, (connB as EitherConn.B).value)

        val connectedA: EitherConnConnected<String, Int> = EitherConnConnected.A("connected")
        val connectedB: EitherConnConnected<String, Int> = EitherConnConnected.B(456)
        assertEquals("connected", (connectedA as EitherConnConnected.A).value)
        assertEquals(456, (connectedB as EitherConnConnected.B).value)
    }
}
