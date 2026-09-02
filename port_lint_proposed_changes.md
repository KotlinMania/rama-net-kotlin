# port-lint Proposed Changes

**Generated:** 2026-09-02
**Source:** tmp/rama-net/src
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/ramanet/socket/Interface.kt` | `// port-lint: source rama-net/src/socket/interface.rs` | `// port-lint: source socket/interface.rs` | `socket/interface.rs` | `port-lint provenance header matched only after fallback normalization: 'rama-net/src/socket/interface.rs' vs expected 'socket/interface.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/ramanet/client/Conn.kt` | `// port-lint: source rama-net/src/client/conn.rs` | `// port-lint: source client/conn.rs` | `client/conn.rs` | `port-lint provenance header matched only after fallback normalization: 'rama-net/src/client/conn.rs' vs expected 'client/conn.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/ramanet/client/Mod.kt` | `// port-lint: source rama-net/src/client/mod.rs` | `// port-lint: source client/mod.rs` | `client/mod.rs` | `port-lint provenance header matched only after fallback normalization: 'rama-net/src/client/mod.rs' vs expected 'client/mod.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/ramanet/Conn.kt` | `// port-lint: source rama-net/src/conn.rs` | `// port-lint: source conn.rs` | `conn.rs` | `port-lint provenance header matched only after fallback normalization: 'rama-net/src/conn.rs' vs expected 'conn.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/ramanet/client/EitherConn.kt` | `// port-lint: source rama-net/src/client/either_conn.rs` | `// port-lint: source client/either_conn.rs` | `client/either_conn.rs` | `port-lint provenance header matched only after fallback normalization: 'rama-net/src/client/either_conn.rs' vs expected 'client/either_conn.rs'` |
