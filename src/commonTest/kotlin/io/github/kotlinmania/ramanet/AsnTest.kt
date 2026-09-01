// port-lint: tests asn.rs
package io.github.kotlinmania.ramanet

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AsnTest {
    @Test
    fun testValidAsn() {
        val asn1 = Asn.parse(13335u)
        assertEquals("AS13335", asn1.toString())
        assertEquals(13335u, asn1.asUInt())
        assertFalse(asn1.isAny())

        val asnParsed = Asn.parse("AS13335")
        assertEquals(asn1, asnParsed)

        val asnFromStatic = Asn.fromStatic(13335u)
        assertEquals(asn1, asnFromStatic)

        val unspec = Asn.unspecified()
        assertEquals("unspecified", unspec.toString())
        assertEquals(0u, unspec.asUInt())
        assertTrue(unspec.isAny())
    }

    @Test
    fun testInvalidAsn() {
        assertFailsWith<InvalidAsnException> {
            Asn.parse(23456u) // AS_TRANS (reserved)
        }
        assertFailsWith<InvalidAsnException> {
            Asn.parse(0xFFFFFFFFu)
        }
        assertFailsWith<InvalidAsnException> {
            Asn.parse("AS_INVALID")
        }
    }
}
