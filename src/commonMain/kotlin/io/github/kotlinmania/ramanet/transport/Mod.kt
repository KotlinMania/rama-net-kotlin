// port-lint: source transport.rs
package io.github.kotlinmania.ramanet.transport

/**
 * The protocol used for the transport layer.
 */
public typealias TransportProtocol = io.github.kotlinmania.ramanet.TransportProtocol

/**
 * The context as relevant to the transport layer.
 */
public typealias TransportContext = io.github.kotlinmania.ramanet.TransportContext

/**
 * Utility interface to turn combined types into a [TransportContext].
 */
public typealias TryRefIntoTransportContext = io.github.kotlinmania.ramanet.TryRefIntoTransportContext
