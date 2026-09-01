// port-lint: source address/ip.rs
package io.github.kotlinmania.ramanet.address

/**
 * IP constants and utilities.
 */
object IpConstants {
    /**
     * An IPv4 address pointing to localhost: `127.0.0.1`.
     */
    const val IPV4_LOCALHOST: String = "127.0.0.1"

    /**
     * An IPv4 address representing an unspecified address: `0.0.0.0`.
     */
    const val IPV4_UNSPECIFIED: String = "0.0.0.0"

    /**
     * An IPv4 address representing the broadcast address: `255.255.255.255`.
     */
    const val IPV4_BROADCAST: String = "255.255.255.255"

    /**
     * An IPv6 address representing localhost: `::1`.
     */
    const val IPV6_LOCALHOST: String = "::1"

    /**
     * An IPv6 address representing the unspecified address: `::`.
     */
    const val IPV6_UNSPECIFIED: String = "::"

    /**
     * The IPv6 All Nodes multicast address in link-local scope: `ff02::1`.
     */
    const val IPV6_ALL_NODES_LINK_LOCAL: String = "ff02::1"

    /**
     * The IPv6 All Routers multicast address in link-local scope: `ff02::2`.
     */
    const val IPV6_ALL_ROUTERS_LINK_LOCAL: String = "ff02::2"
}
