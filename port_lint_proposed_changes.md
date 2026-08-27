# port-lint Proposed Changes

**Generated:** 2026-08-26
**Source:** tmp
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/address/AddressTest.kt` | `// port-lint: tests rama-net/src/address/mod.rs` | `// port-lint: tests stream/mod.rs` | `stream/mod.rs` | `port-lint provenance header matched only by basename: 'tests:rama-net/src/address/mod.rs' vs expected 'stream/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/forwarded/ForwardedTest.kt` | `// port-lint: tests rama-net/src/forwarded/mod.rs` | `// port-lint: tests stream/mod.rs` | `stream/mod.rs` | `port-lint provenance header matched only by basename: 'tests:rama-net/src/forwarded/mod.rs' vs expected 'stream/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/user/UserTest.kt` | `// port-lint: tests rama-net/src/user/mod.rs` | `// port-lint: tests stream/mod.rs` | `stream/mod.rs` | `port-lint provenance header matched only by basename: 'tests:rama-net/src/user/mod.rs' vs expected 'stream/mod.rs'` |
