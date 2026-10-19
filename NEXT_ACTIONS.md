# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 38/104 (36.5%)
- **Function parity:** 329/1042 matched (target 618) — 31.6%
- **Class/type parity:** 50/285 matched (target 120) — 17.5%
- **Combined symbol parity:** 379/1327 matched (target 738) — 28.6%
- **Average inline-code cosine:** 0.35 (function body across 38 matched files)
- **Average documentation cosine:** 0.56 (doc text across 38 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 33 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **match_replace.fmt** (11 deps)
   - Path: `http/uri/match_replace/fmt.rs`
   - Essential for 11 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. address.domain

- **Target:** `address.Domain`
- **Similarity:** 0.36
- **Dependents:** 3
- **Priority Score:** 3105706.2
- **Functions:** 44/50 matched (target 60)
- **Missing functions:** `hash`, `as_ref`, `fmt`, `partial_cmp`, `cmp`, `eq`
- **Types:** 3/7 matched (target 5)
- **Missing types:** `Err`, `Error`, `AsDomainRefPrivate`, `IntoDomainImpl`
- **Tests:** 13/13 matched

### 2. address.host_with_port

- **Target:** `address.HostWithPort`
- **Similarity:** 0.47
- **Dependents:** 3
- **Priority Score:** 3032505.2
- **Functions:** 21/22 matched (target 40)
- **Missing functions:** `assert_eq`
- **Types:** 1/3 matched
- **Missing types:** `Err`, `Error`
- **Tests:** 3/4 matched

### 3. proxy.request

- **Target:** `proxy.ProxyRequest`
- **Similarity:** 1.00
- **Dependents:** 3
- **Priority Score:** 3000100.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 4. socket.interface

- **Target:** `socket.Interface`
- **Similarity:** 0.20
- **Dependents:** 2
- **Priority Score:** 2142908.0
- **Functions:** 13/24 matched (target 28)
- **Missing functions:** `new`, `fmt`, `from_str`, `from`, `assert_eq_socket_address`, `test_parse_valid_socket_address`, `assert_eq_device_name`, `test_parse_valid_device_name`, `test_parse_invalid`, `test_parse_display_address`, `test_parse_display_device_name`
- **Types:** 2/5 matched (target 7)
- **Missing types:** `Err`, `Error`, `Variants`
- **Tests:** 0/7 matched

### 5. credentials.basic

- **Target:** `credentials.Basic`
- **Similarity:** 0.21
- **Dependents:** 2
- **Priority Score:** 2081407.9
- **Functions:** 5/11 matched (target 8)
- **Missing functions:** `fmt`, `username`, `password`, `into_authorizer`, `hash`, `try_from`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Error`, `Err`

### 6. address.socket_address

- **Target:** `address.SocketAddress`
- **Similarity:** 0.31
- **Dependents:** 2
- **Priority Score:** 2042206.9
- **Functions:** 17/19 matched (target 33)
- **Missing functions:** `eq`, `assert_eq`
- **Types:** 1/3 matched
- **Missing types:** `Err`, `Error`
- **Tests:** 3/4 matched

### 7. address.host_with_opt_port

- **Target:** `address.HostWithOptPort`
- **Similarity:** 0.54
- **Dependents:** 2
- **Priority Score:** 2033404.6
- **Functions:** 30/31 matched (target 51)
- **Missing functions:** `assert_eq`
- **Types:** 1/3 matched
- **Missing types:** `Err`, `Error`
- **Tests:** 3/4 matched

### 8. address.host

- **Target:** `address.Host`
- **Similarity:** 0.31
- **Dependents:** 2
- **Priority Score:** 2032907.0
- **Functions:** 24/25 matched (target 52)
- **Missing functions:** `assert_is`
- **Types:** 2/4 matched (target 8)
- **Missing types:** `Err`, `Error`
- **Tests:** 7/8 matched

### 9. http.request_context

- **Target:** `http.RequestContext`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1131609.0
- **Functions:** 2/13 matched (target 4)
- **Missing functions:** `try_get_sni_from_secure_transport`, `try_from`, `try_request_ctx_from_http_parts`, `protocol_from_uri_or_extensions`, `from`, `try_ref_into_transport_ctx`, `test_request_context_from_request`, `test_request_context_from_parts`, `test_request_context_authority`, `forwarded_parsing`, `test_request_ctx_https_request_behind_haproxy_plain`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `SecureTransport`, `Error`
- **Tests:** 0/5 matched

### 10. address.domain_address

- **Target:** `address.DomainAddress`
- **Similarity:** 0.51
- **Dependents:** 1
- **Priority Score:** 1042004.9
- **Functions:** 15/17 matched (target 25)
- **Missing functions:** `fmt`, `assert_eq`
- **Types:** 1/3 matched
- **Missing types:** `Err`, `Error`
- **Tests:** 3/4 matched

### 11. stream.socket

- **Target:** `stream.Socket`
- **Similarity:** 0.10
- **Dependents:** 1
- **Priority Score:** 1021109.1
- **Functions:** 5/7 matched
- **Missing functions:** `local_addr`, `peer_addr`
- **Types:** 4/4 matched
- **Missing types:** _none_

### 12. forwarded.version

- **Target:** `forwarded.ForwardedVersion`
- **Similarity:** 0.18
- **Dependents:** 1
- **Priority Score:** 1010608.2
- **Functions:** 3/3 matched (target 10)
- **Missing functions:** _none_
- **Types:** 2/3 matched
- **Missing types:** `Error`

### 13. proto

- **Target:** `ramanet.Proto`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 142409.2
- **Functions:** 10/20 matched (target 31)
- **Missing functions:** `as_str`, `try_to_convert_str_to_non_custom_protocol`, `try_from`, `from_str`, `from`, `eq`, `fmt`, `validate_scheme_str`, `validate_scheme_slice`, `test_from_http_scheme`
- **Types:** 0/4 matched (target 11)
- **Missing types:** `Protocol`, `ProtocolKind`, `Error`, `Err`
- **Tests:** 3/4 matched

### 14. forwarded.node

- **Target:** `forwarded.Node`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 132508.6
- **Functions:** 9/20 matched (target 27)
- **Missing functions:** `port`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_str_to_ip`, `try_to_split_node_port_from_str`, `try_to_split_node_port_lossy_from_str`, `test_parse_node_id_valid`, `test_parse_node_id_invalid`, `test_parse_node_id_lossy`
- **Types:** 3/5 matched (target 8)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/3 matched

### 15. matcher.private_ip

- **Target:** `matcher.PrivateIp`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 71109.0
- **Functions:** 3/9 matched (target 4)
- **Missing functions:** `inner_new`, `default`, `test_local_ip_net_matcher_http`, `test_local_ip_net_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 16. credentials.bearer

- **Target:** `credentials.Bearer`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 71009.6
- **Functions:** 2/7 matched (target 5)
- **Missing functions:** `fmt`, `new_unchecked`, `token`, `into_authorizer`, `try_from`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Error`, `Err`

### 17. client.conn

- **Target:** `client.Conn`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 71007.9
- **Functions:** 1/4 matched (target 1)
- **Missing functions:** `fmt`, `connect`, `serve`
- **Types:** 2/6 matched (target 2)
- **Missing types:** `ConnectorService`, `Connection`, `Error`, `Output`

### 18. forwarded.obfuscated

- **Target:** `forwarded.Obfuscated`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 61008.8
- **Functions:** 4/10 matched (target 23)
- **Missing functions:** `test_obf_node_parse_valid`, `test_obf_node_parse_lossy`, `test_obf_node_parse_invalid`, `test_obf_port_parse_valid`, `test_obf_port_parse_lossy`, `test_obf_port_parse_invalid`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/6 matched

### 19. socket.opts

- **Target:** `socket.Opts`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 51707.4
- **Functions:** 9/11 matched
- **Missing functions:** `from`, `deserialize`
- **Types:** 3/6 matched (target 5)
- **Missing types:** `Protocol`, `Type`, `Variants`

### 20. asn

- **Target:** `ramanet.Asn`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 51208.1
- **Functions:** 6/9 matched (target 16)
- **Missing functions:** `as_u32`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `AsnData`, `Error`

### 21. matcher.loopback

- **Target:** `matcher.Loopback`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 51007.7
- **Functions:** 3/8 matched (target 11)
- **Missing functions:** `default`, `test_loopback_matcher_http`, `test_loopback_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/4 matched

### 22. matcher.socket

- **Target:** `matcher.Socket`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 50907.5
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_socket_matcher_http`, `test_socket_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 23. matcher.port

- **Target:** `matcher.Port`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 50907.1
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_port_matcher_http`, `test_port_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 24. socket.svc

- **Target:** `socket.Svc`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40410.0
- **Functions:** 0/1 matched
- **Missing functions:** `bind`
- **Types:** 0/3 matched (target 0)
- **Missing types:** `SocketService`, `Socket`, `Error`

### 25. address.authority

- **Target:** `address.Authority`
- **Similarity:** 0.57
- **Dependents:** 0
- **Priority Score:** 33404.3
- **Functions:** 30/31 matched (target 54)
- **Missing functions:** `assert_eq`
- **Types:** 1/3 matched
- **Missing types:** `Err`, `Error`
- **Tests:** 3/4 matched

### 26. address.proxy

- **Target:** `address.Proxy`
- **Similarity:** 0.55
- **Dependents:** 0
- **Priority Score:** 32004.5
- **Functions:** 16/17 matched (target 19)
- **Missing functions:** `fmt`
- **Types:** 1/3 matched
- **Missing types:** `Error`, `Err`
- **Tests:** 12/12 matched

### 27. forwarded.proto

- **Target:** `forwarded.ForwardedProtocol`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 31607.9
- **Functions:** 11/12 matched (target 17)
- **Missing functions:** `eq`
- **Types:** 2/4 matched (target 5)
- **Missing types:** `Error`, `Err`
- **Tests:** 2/2 matched

### 28. transport

- **Target:** `ramanet.Transport`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 30607.5
- **Functions:** 1/2 matched (target 3)
- **Missing functions:** `try_from`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `TryRefIntoTransportContext`, `Error`

### 29. address.domain_trie

- **Target:** `address.DomainTrie`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 22504.7
- **Functions:** 21/23 matched (target 40)
- **Missing functions:** `fmt`, `eq`
- **Types:** 2/2 matched (target 4)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 30. client.mock_connector

- **Target:** `client.MockConnector`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 21604.9
- **Functions:** 12/12 matched (target 15)
- **Missing functions:** _none_
- **Types:** 2/4 matched (target 2)
- **Missing types:** `Error`, `Output`

### 31. proxy.forward

- **Target:** `proxy.StreamForwardService`
- **Similarity:** 0.51
- **Dependents:** 0
- **Priority Score:** 20504.9
- **Functions:** 2/2 matched (target 3)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Output`, `Error`

### 32. credentials.proxy

- **Target:** `credentials.Proxy`
- **Similarity:** 0.61
- **Dependents:** 0
- **Priority Score:** 10303.9
- **Functions:** 1/2 matched (target 4)
- **Missing functions:** `fmt`
- **Types:** 1/1 matched (target 3)
- **Missing types:** _none_

### 33. user.id

- **Target:** `user.UserId`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10210.0
- **Functions:** 0/1 matched (target 5)
- **Missing functions:** `eq`
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_

### 34. mode

- **Target:** `ramanet.Mode`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 404.8
- **Functions:** 2/2 matched (target 3)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 35. conn

- **Target:** `ramanet.Conn`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 109.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 36. client.either_conn

- **Target:** `client.EitherConn`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
- **Missing types:** _none_

### 37. address.ip

- **Target:** `address.Ip`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `address.parse_utils` | `address.ParseUtils` | `address/parse_utils` |

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `address.mod` | `address.Mod` | 0 | `address/mod.rs` | `address/Mod.kt` |
| `forwarded.mod` | `forwarded.Mod` | 0 | `forwarded/mod.rs` | `forwarded/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `stream.mod` | `stream.Mod` | 0 | `stream/mod.rs` | `stream/Mod.kt` |
| `credentials.mod` | `user.credentials.Mod` | 0 | `user/credentials/mod.rs` | `user/credentials/Mod.kt` |
| `user.mod` | `user.Mod` | 0 | `user/mod.rs` | `user/Mod.kt` |

