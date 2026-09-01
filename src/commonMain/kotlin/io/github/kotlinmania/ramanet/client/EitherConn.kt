// port-lint: source rama-net/src/client/either_conn.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.ramanet.client

import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

/**
 * `EitherConn` can be used like you would normally use `Either`, but works with different
 * return types, which is needed when combining different connectors.
 */
@HiddenFromObjC
public sealed interface EitherConn<out A, out B> {
    public data class A<out T>(val value: T) : EitherConn<T, Nothing>
    public data class B<out T>(val value: T) : EitherConn<Nothing, T>
}

/**
 * `EitherConn3` for 3 branches.
 */
@HiddenFromObjC
public sealed interface EitherConn3<out A, out B, out C> {
    public data class A<out T>(val value: T) : EitherConn3<T, Nothing, Nothing>
    public data class B<out T>(val value: T) : EitherConn3<Nothing, T, Nothing>
    public data class C<out T>(val value: T) : EitherConn3<Nothing, Nothing, T>
}

/**
 * `EitherConnConnected` is created when `EitherConn` has been connected and we now have an actual
 * connection instead of a connector.
 */
@HiddenFromObjC
public sealed interface EitherConnConnected<out A, out B> {
    public data class A<out T>(val value: T) : EitherConnConnected<T, Nothing>
    public data class B<out T>(val value: T) : EitherConnConnected<Nothing, T>
}

/**
 * `EitherConn3Connected` for 3 branches.
 */
@HiddenFromObjC
public sealed interface EitherConn3Connected<out A, out B, out C> {
    public data class A<out T>(val value: T) : EitherConn3Connected<T, Nothing, Nothing>
    public data class B<out T>(val value: T) : EitherConn3Connected<Nothing, T, Nothing>
    public data class C<out T>(val value: T) : EitherConn3Connected<Nothing, Nothing, T>
}
