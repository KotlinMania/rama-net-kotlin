// port-lint: tests forwarded/mod.rs
package io.github.kotlinmania.ramanet.forwarded

import io.github.kotlinmania.ramanet.Protocol
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ForwardedTest {
    @Test
    fun testProtocolFromStr() {
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.parse("http"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.parse("https"))
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.parse("HTTP"))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.parse("HTTPS"))
        assertFailsWith<IllegalArgumentException> {
            ForwardedProtocol.parse("ftp")
        }
    }

    @Test
    fun testProtocolSecure() {
        assertFalse(ForwardedProtocol.HTTP.isSecure())
        assertTrue(ForwardedProtocol.HTTPS.isSecure())
        assertTrue(ForwardedProtocol.HTTP.isHttp())
        assertTrue(ForwardedProtocol.HTTPS.isHttp())
    }

    @Test
    fun testProtocolConversions() {
        assertEquals(Protocol.Http, ForwardedProtocol.HTTP.toProtocol())
        assertEquals(Protocol.Https, ForwardedProtocol.HTTPS.toProtocol())
        assertEquals(ForwardedProtocol.HTTP, ForwardedProtocol.fromProtocol(Protocol.Http))
        assertEquals(ForwardedProtocol.HTTPS, ForwardedProtocol.fromProtocol(Protocol.Https))
    }

    @Test
    fun testForwardedVersionParse() {
        assertEquals(ForwardedVersion.Http09, ForwardedVersion.parse("0.9"))
        assertEquals(ForwardedVersion.Http10, ForwardedVersion.parse("1"))
        assertEquals(ForwardedVersion.Http10, ForwardedVersion.parse("1.0"))
        assertEquals(ForwardedVersion.Http11, ForwardedVersion.parse("1.1"))
        assertEquals(ForwardedVersion.H2, ForwardedVersion.parse("2"))
        assertEquals(ForwardedVersion.H2, ForwardedVersion.parse("2.0"))
        assertEquals(ForwardedVersion.H3, ForwardedVersion.parse("3"))
        assertEquals(ForwardedVersion.H3, ForwardedVersion.parse("3.0"))

        assertFailsWith<IllegalArgumentException> {
            ForwardedVersion.parse("4")
        }
    }

    @Test
    fun testObfNodeParseValid() {
        val valid =
            listOf(
                "_gazonk",
                "foo",
                "_foo-bar.baz",
                "-",
                "_",
                ".",
                "1",
                "a",
                "A",
                "-FoA-F-sdada_321A---",
            )
        for (str in valid) {
            assertEquals(str, ObfNode.tryFromStr(str).getOrThrow().asStr())
            assertEquals(str, ObfNode.tryFromBytes(str.encodeToByteArray()).getOrThrow().asStr())
        }
    }

    @Test
    fun testObfNodeParseLossy() {
        val cases =
            listOf(
                Pair("_gazonk", "_gazonk"),
                Pair("foo", "foo"),
                Pair("", "_"),
                Pair("@", "_"),
                Pair("wh@t", "wh_t"),
                Pair("😀", "____"),
                Pair("a".repeat(300), "a".repeat(256)),
            )
        for ((str, expected) in cases) {
            assertEquals(expected, ObfNode.fromStrLossy(str).asStr())
            assertEquals(expected, ObfNode.fromBytesLossy(str.encodeToByteArray()).asStr())
        }
    }

    @Test
    fun testObfNodeParseInvalid() {
        val invalid =
            listOf(
                "",
                "@",
                "😀",
                "a".repeat(300),
            )
        for (str in invalid) {
            assertTrue(ObfNode.tryFromStr(str).isFailure)
            assertTrue(ObfNode.tryFromBytes(str.encodeToByteArray()).isFailure)
        }
    }

    @Test
    fun testObfPortParseValid() {
        val valid =
            listOf(
                "_gazonk",
                "_83",
                "_foo-bar.baz",
                "_-",
                "_",
                "_.",
                "_1",
                "_a",
                "_A",
                "_-FoA-F-sdada_321A---",
            )
        for (str in valid) {
            assertEquals(str, ObfPort.tryFromStr(str).getOrThrow().asStr())
            assertEquals(str, ObfPort.tryFromBytes(str.encodeToByteArray()).getOrThrow().asStr())
        }
    }

    @Test
    fun testObfPortParseLossy() {
        val cases =
            listOf(
                Pair("_gazonk", "_gazonk"),
                Pair("_83", "_83"),
                Pair("83", "_83"),
                Pair("-", "_-"),
                Pair("", "_"),
                Pair("@", "__"),
                Pair("wh@t", "_wh_t"),
                Pair("😀", "_____"),
                Pair("a".repeat(300), "_" + "a".repeat(255)),
            )
        for ((str, expected) in cases) {
            assertEquals(expected, ObfPort.fromStrLossy(str).asStr())
            assertEquals(expected, ObfPort.fromBytesLossy(str.encodeToByteArray()).asStr())
        }
    }

    @Test
    fun testObfPortParseInvalid() {
        val invalid =
            listOf(
                "",
                "-",
                "a",
                "1",
                "@",
                "😀",
                "a".repeat(300),
            )
        for (str in invalid) {
            assertTrue(ObfPort.tryFromStr(str).isFailure)
            assertTrue(ObfPort.tryFromBytes(str.encodeToByteArray()).isFailure)
        }
    }

    @Test
    fun testParseNodeIdValid() {
        val cases =
            listOf(
                Pair("unknown", NodeId(NodeName.Unknown, null)),
                Pair("::1", NodeId(NodeName.Ip("::1"), null)),
                Pair("127.0.0.1", NodeId(NodeName.Ip("127.0.0.1"), null)),
                Pair("192.0.2.43:47011", NodeId(NodeName.Ip("192.0.2.43"), NodePort.Num(47011u))),
                Pair("[2001:db8:cafe::17]:47011", NodeId(NodeName.Ip("2001:db8:cafe::17"), NodePort.Num(47011u))),
                Pair("192.0.2.43:_foo", NodeId(NodeName.Ip("192.0.2.43"), NodePort.Obf(ObfPort.fromStatic("_foo")))),
                Pair("[2001:db8:cafe::17]:_bar", NodeId(NodeName.Ip("2001:db8:cafe::17"), NodePort.Obf(ObfPort.fromStatic("_bar")))),
                Pair("foo", NodeId(NodeName.Obf(ObfNode.fromStatic("foo")), null)),
                Pair("_foo", NodeId(NodeName.Obf(ObfNode.fromStatic("_foo")), null)),
                Pair("foo:_bar", NodeId(NodeName.Obf(ObfNode.fromStatic("foo")), NodePort.Obf(ObfPort.fromStatic("_bar")))),
                Pair("foo:42", NodeId(NodeName.Obf(ObfNode.fromStatic("foo")), NodePort.Num(42u))),
            )
        for ((s, expected) in cases) {
            val parsed = NodeId.tryFromStr(s).getOrThrow()
            assertEquals(expected, parsed)
            assertEquals(s, parsed.toString())
        }
    }

    @Test
    fun testParseNodeIdInvalid() {
        val invalid =
            listOf(
                "",
                "@",
                "2001:db8:3333:4444:5555:6666:7777:8888:80",
                "foo:bar",
                "foo:_b+r",
                "😀",
                "a".repeat(300),
            )
        for (s in invalid) {
            assertTrue(NodeId.tryFromStr(s).isFailure, "expected failure for: $s")
        }
    }

    @Test
    fun testParseNodeIdLossy() {
        val cases =
            listOf(
                Pair("", NodeId(NodeName.Obf(ObfNode.fromStatic("_")), null)),
                Pair("@", NodeId(NodeName.Obf(ObfNode.fromStatic("_")), null)),
                Pair(
                    "2001:db8:3333:4444:5555:6666:7777:8888:80",
                    NodeId(NodeName.Obf(ObfNode.fromStatic("2001_db8_3333_4444_5555_6666_7777_8888_80")), null),
                ),
                Pair("foo:bar", NodeId(NodeName.Obf(ObfNode.fromStatic("foo")), NodePort.Obf(ObfPort.fromStatic("_bar")))),
                Pair("foo:_b+r", NodeId(NodeName.Obf(ObfNode.fromStatic("foo")), NodePort.Obf(ObfPort.fromStatic("_b_r")))),
                Pair("😀", NodeId(NodeName.Obf(ObfNode.fromStatic("____")), null)),
            )
        for ((s, expected) in cases) {
            assertEquals(expected, NodeId.fromStrLossy(s), "parse str: $s")
            assertEquals(expected, NodeId.fromBytesLossy(s.encodeToByteArray()), "parse bytes: $s")
        }
    }
}
