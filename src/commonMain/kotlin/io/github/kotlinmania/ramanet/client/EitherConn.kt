// port-lint: source client/either_conn.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.client

import io.github.kotlinmania.ramacore.Extensions
import io.github.kotlinmania.ramacore.ExtensionsRef
import io.github.kotlinmania.tokio.io.AsyncRead
import io.github.kotlinmania.tokio.io.AsyncWrite
import io.github.kotlinmania.tokio.io.Context
import io.github.kotlinmania.tokio.io.Poll
import io.github.kotlinmania.tokio.io.ReadBuf
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * `EitherConn` can be used like you would normally use `Either`, but works with different
 * return types, which is needed when combining different connectors.
 */
@HiddenFromObjC
public sealed interface EitherConn<out A, out B> {
    public data class A<out T>(
        public val value: T,
    ) : EitherConn<T, Nothing>

    public data class B<out T>(
        public val value: T,
    ) : EitherConn<Nothing, T>
}

/**
 * `EitherConn3` for 3 branches.
 */
@HiddenFromObjC
public sealed interface EitherConn3<out A, out B, out C> {
    public data class A<out T>(
        public val value: T,
    ) : EitherConn3<T, Nothing, Nothing>

    public data class B<out T>(
        public val value: T,
    ) : EitherConn3<Nothing, T, Nothing>

    public data class C<out T>(
        public val value: T,
    ) : EitherConn3<Nothing, Nothing, T>
}

/**
 * `EitherConnConnected` is created when `EitherConn` has been connected and we now have an actual
 * connection instead of a connector.
 */
@HiddenFromObjC
public sealed interface EitherConnConnected<out A, out B> {
    public data class A<out T>(
        public val value: T,
    ) : EitherConnConnected<T, Nothing>

    public data class B<out T>(
        public val value: T,
    ) : EitherConnConnected<Nothing, T>
}

/**
 * Retrieve [Extensions] from an [EitherConnConnected] whose branches implement [ExtensionsRef].
 */
@HiddenFromObjC
public fun <A : ExtensionsRef, B : ExtensionsRef> EitherConnConnected<A, B>.extensions(): Extensions =
    when (this) {
        is EitherConnConnected.A -> value.extensions()
        is EitherConnConnected.B -> value.extensions()
    }

/**
 * Poll read on an [EitherConnConnected] whose branches implement [AsyncRead].
 */
@HiddenFromObjC
public fun <A : AsyncRead, B : AsyncRead> EitherConnConnected<A, B>.pollRead(
    cx: Context,
    buf: ReadBuf,
): Poll<Result<Unit>> =
    when (this) {
        is EitherConnConnected.A -> value.pollRead(cx, buf)
        is EitherConnConnected.B -> value.pollRead(cx, buf)
    }

/**
 * Poll write on an [EitherConnConnected] whose branches implement [AsyncWrite].
 */
@HiddenFromObjC
public fun <A : AsyncWrite, B : AsyncWrite> EitherConnConnected<A, B>.pollWrite(
    cx: Context,
    buf: UByteArray,
): Poll<Result<Int>> =
    when (this) {
        is EitherConnConnected.A -> value.pollWrite(cx, buf)
        is EitherConnConnected.B -> value.pollWrite(cx, buf)
    }

/**
 * `EitherConn3Connected` for 3 branches.
 */
@HiddenFromObjC
public sealed interface EitherConn3Connected<out A, out B, out C> {
    public data class A<out T>(
        public val value: T,
    ) : EitherConn3Connected<T, Nothing, Nothing>

    public data class B<out T>(
        public val value: T,
    ) : EitherConn3Connected<Nothing, T, Nothing>

    public data class C<out T>(
        public val value: T,
    ) : EitherConn3Connected<Nothing, Nothing, T>
}
