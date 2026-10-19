package io.github.kotlinmania.ramanet.tls

/**
 * Intent for a (tls) keylogger to be used.
 */
public sealed class KeyLogIntent {
    /** By default `SSLKEYLOGFILE` env variable is respected as the path to key log to, if defined. */
    public data object Environment : KeyLogIntent()

    /** Choose to disable the key logging explicitly. */
    public data object Disabled : KeyLogIntent()

    /** Request keys to be logged to the given file path. */
    public data class File(
        public val path: String,
    ) : KeyLogIntent()

    public fun filePath(): String? =
        when (this) {
            Disabled -> null
            Environment -> null
            is File -> path
        }

    public fun intoFilePath(): String? = filePath()
}
