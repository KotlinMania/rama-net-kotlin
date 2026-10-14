// port-lint: source test_utils/client/mock_connector.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.testutils.client

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsMut
import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.ramacore.RamaResult
import io.github.kotlinmania.ramacore.service.Service
import io.github.kotlinmania.ramanet.address.SocketAddress
import io.github.kotlinmania.ramanet.client.EstablishedClientConnection
import io.github.kotlinmania.ramanet.stream.Socket
import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.AsyncWrite
import io.github.kotlinmania.tokio.io.Context
import io.github.kotlinmania.tokio.io.IoSlice
import io.github.kotlinmania.tokio.io.Poll
import io.github.kotlinmania.tokio.io.ReadBuf
import io.github.kotlinmania.tokio.io.util.DuplexStream
import io.github.kotlinmania.tokio.io.util.duplex
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * In-memory socket backed by a [DuplexStream] for testing connectors and servers.
 */
@HiddenFromObjC
public class MockSocket(
    public val stream: DuplexStream,
    private val extensionsStore: Extensions = Extensions.new(),
) : Socket,
    ExtensionsRef,
    ExtensionsMut,
    AsyncRead,
    AsyncWrite {
    override fun extensions(): Extensions = extensionsStore

    override fun extensionsMut(): Extensions = extensionsStore

    override fun localAddr(): SocketAddress = SocketAddress.localIpv4(0u)

    override fun peerAddr(): SocketAddress = SocketAddress.localIpv4(0u)

    override fun pollRead(
        cx: Context,
        buf: ReadBuf,
    ): Poll<Result<Unit>> = stream.pollRead(cx, buf)

    override fun pollWrite(
        cx: Context,
        buf: UByteArray,
    ): Poll<Result<Int>> = stream.pollWrite(cx, buf)

    override fun pollFlush(cx: Context): Poll<Result<Unit>> = stream.pollFlush(cx)

    override fun pollShutdown(cx: Context): Poll<Result<Unit>> = stream.pollShutdown(cx)

    override fun isWriteVectored(): Boolean = stream.isWriteVectored()

    override fun pollWriteVectored(
        cx: Context,
        bufs: List<IoSlice>,
    ): Poll<Result<Int>> = stream.pollWriteVectored(cx, bufs)

    public companion object {
        public fun new(stream: DuplexStream): MockSocket = MockSocket(stream)
    }
}

/**
 * Mock connector that can be used in tests to simulate connectors without opening actual connections.
 */
@HiddenFromObjC
public class MockConnectorService<Input : Any, Output : Any, Error : Any>(
    private val createServer: () -> Service<MockSocket, Output, Error>,
    public var maxBufferSize: Int = 1024,
) : Service<Input, EstablishedClientConnection<MockSocket, Input>, Error> {
    public fun setMaxBufferSize(size: Int): MockConnectorService<Input, Output, Error> {
        maxBufferSize = size
        return this
    }

    public fun withMaxBufferSize(size: Int): MockConnectorService<Input, Output, Error> =
        MockConnectorService(createServer, size)

    override suspend fun serve(
        input: Input,
    ): RamaResult<EstablishedClientConnection<MockSocket, Input>, Error> {
        val (client, server) = duplex(maxBufferSize)
        val clientSocket = MockSocket.new(client)
        val serverSocket = MockSocket.new(server)
        val serverSvc = createServer()
        val res = serverSvc.serve(serverSocket)
        if (res.isFailure()) {
            return RamaResult.err(res.errorOrNull()!!)
        }
        return RamaResult.ok(
            EstablishedClientConnection(
                input = input,
                conn = clientSocket,
            ),
        )
    }

    public companion object {
        public fun <Input : Any, Output : Any, Error : Any> new(
            createServer: () -> Service<MockSocket, Output, Error>,
        ): MockConnectorService<Input, Output, Error> = MockConnectorService(createServer)
    }
}
