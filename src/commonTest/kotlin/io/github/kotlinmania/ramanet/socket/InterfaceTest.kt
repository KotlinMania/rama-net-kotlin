package io.github.kotlinmania.ramanet.socket

import io.github.kotlinmania.ramanet.address.SocketAddress
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class InterfaceTest {
    private fun assertEqSocketAddress(
        s: String,
        bindAddress: Interface,
        expectedIp: String,
        expectedPort: UShort,
    ) {
        when (bindAddress) {
            is Interface.Address -> {
                assertEquals(expectedIp, bindAddress.socketAddress.ip, "parsing: $s")
                assertEquals(expectedPort, bindAddress.socketAddress.port, "parsing: $s")
            }
            is Interface.Device -> {
                throw AssertionError("unexpected device name '${bindAddress.deviceName}': parsing '$s'")
            }
        }
    }

    private fun assertEqDeviceName(s: String, bindAddress: Interface) {
        when (bindAddress) {
            is Interface.Address -> {
                throw AssertionError("unexpected socket address '${bindAddress.socketAddress}': parsing '$s'")
            }
            is Interface.Device -> {
                assertEquals(s, bindAddress.deviceName.asStr(), "parsing: $s")
            }
        }
    }

    @Test
    fun testParseValidSocketAddress() {
        val cases =
            listOf(
                "[::1]:80" to Pair("::1", 80.toUShort()),
                "127.0.0.1:80" to Pair("127.0.0.1", 80.toUShort()),
                "[2001:db8:3333:4444:5555:6666:7777:8888]:80" to
                    Pair("2001:db8:3333:4444:5555:6666:7777:8888", 80.toUShort()),
            )

        for ((s, expected) in cases) {
            val (expectedIp, expectedPort) = expected
            assertEqSocketAddress(s, Interface.parse(s), expectedIp, expectedPort)
            assertEqSocketAddress(s, Interface.tryFrom(s).getOrThrow(), expectedIp, expectedPort)
            assertEqSocketAddress(
                s,
                Interface.tryFrom(s.encodeToByteArray()).getOrThrow(),
                expectedIp,
                expectedPort,
            )
        }
    }

    @Test
    fun testParseValidDeviceName() {
        val names =
            listOf(
                "eth0",
                "eth0.100",
                "br-lan",
                "ens192",
                "veth_abcd1234",
                "lo",
            )

        for (s in names) {
            assertEqDeviceName(s, Interface.parse(s))
            assertEqDeviceName(s, Interface.tryFrom(s).getOrThrow())
            assertEqDeviceName(
                s,
                Interface.tryFrom(s.encodeToByteArray()).getOrThrow(),
            )
            assertTrue(DeviceName.isValid(s))
            assertEquals(s, DeviceName.of(s).asStr())
        }
    }

    @Test
    fun testParseInvalid() {
        val invalid =
            listOf(
                "",
                "-",
                ".",
                ":",
                ":80",
                "-.",
                ".-",
                "::1",
                "127.0.0.1",
                "[::1]",
                "2001:db8:3333:4444:5555:6666:7777:8888",
                "[2001:db8:3333:4444:5555:6666:7777:8888]",
                "example.com:999999",
                "[127.0.0.1]:80",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
                "eth#0",
                "abcdefghijklmnopqrstuvwxyz",
                "GigabitEthernet0/1",
                "ge-0/0/0",
            )

        for (s in invalid) {
            assertFails("expected invalid for '$s'") { Interface.parse(s) }
            assertTrue(Interface.tryFrom(s).isFailure, "expected failure for '$s'")
            assertTrue(
                Interface.tryFrom(s.encodeToByteArray()).isFailure,
                "expected failure for '$s'",
            )
        }
    }

    @Test
    fun testParseDisplayAddress() {
        val cases =
            listOf(
                "[::1]:80" to "[::1]:80",
                "127.0.0.1:80" to "127.0.0.1:80",
            )

        for ((s, expected) in cases) {
            val bindAddress = Interface.parse(s)
            assertEquals(expected, bindAddress.toString())
        }
    }

    @Test
    fun testConstructors() {
        assertEquals("127.0.0.1:8080", Interface.localIpv4(8080u).toString())
        assertEquals("[::1]:8080", Interface.localIpv6(8080u).toString())
        assertEquals("0.0.0.0:8080", Interface.defaultIpv4(8080u).toString())
        assertEquals("[::]:8080", Interface.defaultIpv6(8080u).toString())
        assertEquals("255.255.255.255:8080", Interface.broadcastIpv4(8080u).toString())
        assertEquals("eth0", Interface.newDevice("eth0").toString())
        assertEquals("127.0.0.1:9000", Interface.newAddress(SocketAddress.localIpv4(9000u)).toString())
    }
}
