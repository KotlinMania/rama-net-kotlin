// port-lint: source mode.rs
package io.github.kotlinmania.ramanet

/**
 * IP modes that can be used by the DNS resolver.
 */
enum class DnsResolveIpMode {
    Dual,
    SingleIpV4,
    SingleIpV6,
    DualPreferIpV4,
    ;

    /**
     * Checks whether IPv4 is supported in this mode.
     */
    fun ipv4Supported(): Boolean =
        this == Dual || this == SingleIpV4 || this == DualPreferIpV4

    /**
     * Checks whether IPv6 is supported in this mode.
     */
    fun ipv6Supported(): Boolean =
        this == Dual || this == SingleIpV6 || this == DualPreferIpV4
}

/**
 * Mode for establishing a connection.
 */
enum class ConnectIpMode {
    Dual,
    Ipv4,
    Ipv6,
}
