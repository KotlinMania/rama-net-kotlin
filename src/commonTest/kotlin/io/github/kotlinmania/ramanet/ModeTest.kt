package io.github.kotlinmania.ramanet

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModeTest {
    @Test
    fun dnsResolveIpModeReportsIpVersionSupport() {
        assertTrue(DnsResolveIpMode.Dual.ipv4Supported())
        assertTrue(DnsResolveIpMode.Dual.ipv6Supported())

        assertTrue(DnsResolveIpMode.SingleIpV4.ipv4Supported())
        assertFalse(DnsResolveIpMode.SingleIpV4.ipv6Supported())

        assertFalse(DnsResolveIpMode.SingleIpV6.ipv4Supported())
        assertTrue(DnsResolveIpMode.SingleIpV6.ipv6Supported())

        assertTrue(DnsResolveIpMode.DualPreferIpV4.ipv4Supported())
        assertTrue(DnsResolveIpMode.DualPreferIpV4.ipv6Supported())
    }
}
