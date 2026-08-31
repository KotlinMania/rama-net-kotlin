// port-lint: tests rama-net/src/user/mod.rs
package io.github.kotlinmania.ramanet.user

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class UserTest {
    @Test
    fun testUserId() {
        val user1 = UserId.Username("alice")
        assertEquals("alice", user1.toString())

        val user2 = UserId.Token(byteArrayOf(1, 2, 3))
        assertEquals("***", user2.toString())

        val anon = UserId.Anonymous
        assertEquals("anonymous", anon.toString())
    }

    @Test
    fun testBasic() {
        val b1 = Basic.parse("alice:secret")
        assertEquals("alice", b1.username)
        assertEquals("secret", b1.password)
        assertEquals("alice:***", b1.toString())

        val b2 = Basic.parse("bob")
        assertEquals("bob", b2.username)
        assertNull(b2.password)
        assertEquals("bob", b2.toString())

        assertFailsWith<IllegalArgumentException> {
            Basic.parse("")
        }
    }

    @Test
    fun testBearer() {
        val bearer = Bearer.parse("token123")
        assertEquals("token123", bearer.unmasked())
        assertEquals("***", bearer.toString())

        assertFailsWith<IllegalArgumentException> {
            Bearer.parse("")
        }
        assertFailsWith<IllegalArgumentException> {
            Bearer.parse("token\u0000bad")
        }
    }
}
