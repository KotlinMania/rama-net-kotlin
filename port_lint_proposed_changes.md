# port-lint Proposed Changes

**Generated:** 2026-08-24
**Source:** tmp/rama-net/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/ramanet

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/address/AddressTest.kt` | `// port-lint: tests address/mod.rs` | `// port-lint: tests user/credentials/mod.rs` | `user/credentials/mod.rs` | `port-lint provenance header matched only by basename: 'tests:address/mod.rs' vs expected 'user/credentials/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/forwarded/ForwardedTest.kt` | `// port-lint: tests forwarded/mod.rs` | `// port-lint: tests user/credentials/mod.rs` | `user/credentials/mod.rs` | `port-lint provenance header matched only by basename: 'tests:forwarded/mod.rs' vs expected 'user/credentials/mod.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/ramanet/user/UserTest.kt` | `// port-lint: tests user/mod.rs` | `// port-lint: tests user/credentials/mod.rs` | `user/credentials/mod.rs` | `port-lint provenance header matched only by basename: 'tests:user/mod.rs' vs expected 'user/credentials/mod.rs'` |
