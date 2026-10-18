// port-lint: source http/request_context.rs
package io.github.kotlinmania.ramanet.http

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramanet.NetProtocol
import io.github.kotlinmania.ramanet.TransportContext
import io.github.kotlinmania.ramanet.TransportProtocol
import io.github.kotlinmania.ramanet.address.Host
import io.github.kotlinmania.ramanet.address.HostWithOptPort
import io.github.kotlinmania.ramanet.address.HostWithPort
import io.github.kotlinmania.ramanet.proxy.ProxyTarget
import io.github.kotlinmania.ramanet.tls.SecureTransport

/**
 * The context of the HTTP `Request`.
 */
public data class RequestContext(
    /** The HTTP Version (e.g. `"HTTP/1.1"`, `"HTTP/2.0"`, `"HTTP/3.0"`). */
    public val httpVersion: String = "HTTP/1.1",
    /** The [NetProtocol] of the `Request`. */
    public val protocol: NetProtocol = NetProtocol.HTTP,
    /** The authority of the `Request`. */
    public val authority: HostWithOptPort,
) {
    /**
     * Secondary constructor matching `(authority, protocol)` convenience order.
     */
    public constructor(
        authority: HostWithOptPort,
        protocol: NetProtocol,
    ) : this(
        httpVersion = "HTTP/1.1",
        protocol = protocol,
        authority = authority,
    )

    /**
     * Check if the authority is using the default port for the [Protocol] set in this [RequestContext].
     */
    public fun authorityHasDefaultPort(): Boolean =
        protocol.defaultPort() == authority.port

    /**
     * Return the [HostWithPort] for this request context, falling back to the protocol default port or 80.
     */
    public fun hostWithPort(): HostWithPort {
        val port =
            authority.port
                ?: protocol.defaultPort()
                ?: NetProtocol.HTTP_DEFAULT_PORT
        return HostWithPort(authority.host, port)
    }

    /**
     * Convert this [RequestContext] into a [TransportContext].
     */
    public fun toTransportContext(): TransportContext =
        TransportContext(
            protocol =
                if (httpVersion == "HTTP/3.0" || httpVersion == "HTTP/3") {
                    TransportProtocol.Udp
                } else {
                    TransportProtocol.Tcp
                },
            authority = authority,
            appProtocol = protocol,
            httpVersion = httpVersion,
        )

    public companion object {
        /**
         * Construct a [RequestContext] from raw HTTP request components and [Extensions].
         */
        public fun fromParts(
            uriScheme: String? = null,
            uriHost: String? = null,
            uriPort: UShort? = null,
            hostHeader: String? = null,
            httpVersion: String = "HTTP/1.1",
            extensions: Extensions? = null,
        ): Result<RequestContext> =
            runCatching {
                val detectedProtocol =
                    uriScheme?.takeIf { it.isNotEmpty() }?.let { NetProtocol.parse(it) }
                        ?: extensions?.get<NetProtocol>()
                        ?: if (extensions?.contains<SecureTransport>() == true) {
                            NetProtocol.HTTPS
                        } else {
                            NetProtocol.HTTP
                        }

                val defaultPort = uriPort ?: detectedProtocol.defaultPort() ?: NetProtocol.HTTP_DEFAULT_PORT

                val detectedAuthority: HostWithOptPort =
                    when {
                        !uriHost.isNullOrEmpty() -> {
                            val host = Host.parse(uriHost)
                            if (uriPort != null) {
                                HostWithOptPort(host, uriPort)
                            } else {
                                HostWithOptPort(host, defaultPort)
                            }
                        }
                        extensions?.get<ProxyTarget>() != null -> {
                            val proxyTarget = extensions.get<ProxyTarget>()!!
                            HostWithOptPort(proxyTarget.target.host, proxyTarget.target.port)
                        }
                        extensions?.get<SecureTransport>()?.clientHello()?.serverName != null -> {
                            val sni = extensions.get<SecureTransport>()!!.clientHello()!!.serverName!!
                            HostWithOptPort(Host.Name(sni), defaultPort)
                        }
                        !hostHeader.isNullOrEmpty() -> {
                            HostWithOptPort.parse(hostHeader)
                        }
                        else -> throw IllegalArgumentException("RequestContext: no authority found in http::Request")
                    }

                RequestContext(
                    httpVersion = httpVersion,
                    protocol = detectedProtocol,
                    authority = detectedAuthority,
                )
            }
    }
}
