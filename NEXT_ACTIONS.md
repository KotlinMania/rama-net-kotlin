# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/104 (1.0%)
- **Function parity:** 2/1075 matched (target 2) — 0.2%
- **Class/type parity:** 2/228 matched (target 2) — 0.9%
- **Combined symbol parity:** 4/1303 matched (target 4) — 0.3%
- **Average inline-code cosine:** 0.52 (function body across 1 matched files)
- **Average documentation cosine:** 0.84 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **match_replace.fmt** (11 deps)
   - Path: `http/uri/match_replace/fmt.rs`
   - Essential for 11 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. mode

- **Target:** `ramanet.Mode`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 404.8
- **Functions:** 2/2 matched
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Next Commands

```bash
# Initialize task queue for systematic porting
cd tools/ast_distance
./ast_distance --init-tasks ../../tmp/rama-net/src rust ../../src/commonMain/kotlin/io/github/kotlinmania/ramanet kotlin tasks.json ../../AGENTS.md

# Get next high-priority task
./ast_distance --assign tasks.json <agent-id>
```
## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `address.mod` | `address.Mod` | 0 | `address/mod.rs` | `address/Mod.kt` |
| `client.mod` | `client.Mod` | 0 | `client/mod.rs` | `client/Mod.kt` |
| `pool.mod` | `client.pool.Mod` | 0 | `client/pool/mod.rs` | `client/pool/Mod.kt` |
| `akamai.mod` | `fingerprint.akamai.Mod` | 0 | `fingerprint/akamai/mod.rs` | `fingerprint/akamai/Mod.kt` |
| `ja4.mod` | `fingerprint.ja4.Mod` | 0 | `fingerprint/ja4/mod.rs` | `fingerprint/ja4/Mod.kt` |
| `fingerprint.mod` | `fingerprint.Mod` | 0 | `fingerprint/mod.rs` | `fingerprint/Mod.kt` |
| `peet.mod` | `fingerprint.peet.Mod` | 0 | `fingerprint/peet/mod.rs` | `fingerprint/peet/Mod.kt` |
| `element.mod` | `forwarded.element.Mod` | 0 | `forwarded/element/mod.rs` | `forwarded/element/Mod.kt` |
| `forwarded.mod` | `forwarded.Mod` | 0 | `forwarded/mod.rs` | `forwarded/Mod.kt` |
| `http.mod` | `http.Mod` | 0 | `http/mod.rs` | `http/Mod.kt` |
| `http.server.mod` | `http.server.Mod` | 0 | `http/server/mod.rs` | `http/server/Mod.kt` |
| `match_replace.mod` | `http.uri.matchreplace.Mod` | 0 | `http/uri/match_replace/mod.rs` | `http/uri/matchreplace/Mod.kt` |
| `uri.mod` | `http.uri.Mod` | 0 | `http/uri/mod.rs` | `http/uri/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `proxy.mod` | `proxy.Mod` | 0 | `proxy/mod.rs` | `proxy/Mod.kt` |
| `socket.mod` | `socket.Mod` | 0 | `socket/mod.rs` | `socket/Mod.kt` |
| `stream.layer.http.mod` | `stream.layer.http.Mod` | 0 | `stream/layer/http/mod.rs` | `stream/layer/http/Mod.kt` |
| `stream.layer.mod` | `stream.layer.Mod` | 0 | `stream/layer/mod.rs` | `stream/layer/Mod.kt` |
| `tracker.mod` | `stream.layer.tracker.Mod` | 0 | `stream/layer/tracker/mod.rs` | `stream/layer/tracker/Mod.kt` |
| `matcher.mod` | `stream.matcher.Mod` | 0 | `stream/matcher/mod.rs` | `stream/matcher/Mod.kt` |
| `stream.mod` | `stream.Mod` | 0 | `stream/mod.rs` | `stream/Mod.kt` |
| `service.mod` | `stream.service.Mod` | 0 | `stream/service/mod.rs` | `stream/service/Mod.kt` |
| `test_utils.client.mod` | `testutils.client.Mod` | 0 | `test_utils/client/mod.rs` | `testutils/client/Mod.kt` |
| `test_utils.mod` | `testutils.Mod` | 0 | `test_utils/mod.rs` | `testutils/Mod.kt` |
| `tls.client.mod` | `tls.client.Mod` | 0 | `tls/client/mod.rs` | `tls/client/Mod.kt` |
| `tls.mod` | `tls.Mod` | 0 | `tls/mod.rs` | `tls/Mod.kt` |
| `server.mod` | `tls.server.Mod` | 0 | `tls/server/mod.rs` | `tls/server/Mod.kt` |
| `credentials.mod` | `user.credentials.Mod` | 0 | `user/credentials/mod.rs` | `user/credentials/Mod.kt` |
| `layer.mod` | `user.layer.Mod` | 0 | `user/layer/mod.rs` | `user/layer/Mod.kt` |
| `user.mod` | `user.Mod` | 0 | `user/mod.rs` | `user/Mod.kt` |

