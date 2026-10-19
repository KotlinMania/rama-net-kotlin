# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 38/104 (36.5%)
- **Function parity:** 213/1042 matched (target 415) — 20.4%
- **Class/type parity:** 46/285 matched (target 103) — 16.1%
- **Combined symbol parity:** 259/1327 matched (target 518) — 19.5%
- **Average inline-code cosine:** 0.27 (function body across 38 matched files)
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
- **Similarity:** 0.30
- **Dependents:** 3
- **Priority Score:** 3072507.0
- **Functions:** 17/22 matched (target 29)
- **Missing functions:** `from`, `fmt`, `from_str`, `try_from`, `assert_eq`
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

### 4. address.host_with_opt_port

- **Target:** `address.HostWithOptPort`
- **Similarity:** 0.06
- **Dependents:** 2
- **Priority Score:** 2273409.5
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_port`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 5. address.host

- **Target:** `address.Host`
- **Similarity:** 0.07
- **Dependents:** 2
- **Priority Score:** 2172909.2
- **Functions:** 11/25 matched (target 29)
- **Missing functions:** `eq`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_bytes_to_ip`, `assert_is`, `test_parse_specials`, `test_parse_bytes_valid`, `test_parse_valid`, `test_parse_str_invalid`, `compare_host_with_ipv4_bidirectional`, `compare_host_with_ipv6_bidirectional`, `compare_host_with_ip_bidirectional`
- **Types:** 1/4 matched
- **Missing types:** `Err`, `Error`, `Is`
- **Tests:** 0/8 matched

### 6. socket.interface

- **Target:** `socket.Interface`
- **Similarity:** 0.20
- **Dependents:** 2
- **Priority Score:** 2142908.0
- **Functions:** 13/24 matched (target 28)
- **Missing functions:** `new`, `fmt`, `from_str`, `from`, `assert_eq_socket_address`, `test_parse_valid_socket_address`, `assert_eq_device_name`, `test_parse_valid_device_name`, `test_parse_invalid`, `test_parse_display_address`, `test_parse_display_device_name`
- **Types:** 2/5 matched (target 7)
- **Missing types:** `Err`, `Error`, `Variants`
- **Tests:** 0/7 matched

### 7. address.socket_address

- **Target:** `address.SocketAddress`
- **Similarity:** 0.14
- **Dependents:** 2
- **Priority Score:** 2122208.5
- **Functions:** 9/19 matched (target 15)
- **Missing functions:** `eq`, `new`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 8. credentials.basic

- **Target:** `credentials.Basic`
- **Similarity:** 0.33
- **Dependents:** 2
- **Priority Score:** 2061406.8
- **Functions:** 7/11 matched (target 10)
- **Missing functions:** `fmt`, `into_authorizer`, `hash`, `try_from`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Error`, `Err`

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
- **Similarity:** 0.25
- **Dependents:** 1
- **Priority Score:** 1112007.5
- **Functions:** 8/17 matched (target 11)
- **Missing functions:** `new`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_valid_domain_address`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 11. stream.socket

- **Target:** `stream.Socket`
- **Similarity:** 0.04
- **Dependents:** 1
- **Priority Score:** 1071109.6
- **Functions:** 1/7 matched (target 3)
- **Missing functions:** `local_addr`, `peer_addr`, `as_ref`, `as_mut`, `deref`, `deref_mut`
- **Types:** 3/4 matched (target 3)
- **Missing types:** `Target`

### 12. forwarded.version

- **Target:** `forwarded.ForwardedVersion`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1050610.0
- **Functions:** 0/3 matched
- **Missing functions:** `as_http`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `VersionKind`, `Error`

### 13. address.authority

- **Target:** `address.Authority`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 273409.3
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_user_info`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 14. address.proxy

- **Target:** `address.Proxy`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 172009.7
- **Functions:** 2/17 matched (target 4)
- **Missing functions:** `try_from`, `from_str`, `fmt`, `test_valid_proxy`, `test_valid_domain_proxy`, `test_valid_proxy_with_credential`, `test_valid_proxy_with_insecure_credential`, `test_valid_http_proxy`, `test_valid_http_proxy_with_credential`, `test_valid_http_proxy_with_insecure_credential`, `test_valid_https_proxy`, `test_valid_https_proxy_with_insecure_credentials`, `test_valid_socks5h_proxy`, `test_valid_socks5h_proxy_trailing_colon`, `test_valid_proxy_address_symmetric`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Error`, `Err`
- **Tests:** 0/12 matched

### 15. proto

- **Target:** `ramanet.Proto`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 142409.2
- **Functions:** 10/20 matched (target 31)
- **Missing functions:** `as_str`, `try_to_convert_str_to_non_custom_protocol`, `try_from`, `from_str`, `from`, `eq`, `fmt`, `validate_scheme_str`, `validate_scheme_slice`, `test_from_http_scheme`
- **Types:** 0/4 matched (target 11)
- **Missing types:** `Protocol`, `ProtocolKind`, `Error`, `Err`
- **Tests:** 3/4 matched

### 16. forwarded.node

- **Target:** `forwarded.Node`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 132508.6
- **Functions:** 9/20 matched (target 27)
- **Missing functions:** `port`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_str_to_ip`, `try_to_split_node_port_from_str`, `try_to_split_node_port_lossy_from_str`, `test_parse_node_id_valid`, `test_parse_node_id_invalid`, `test_parse_node_id_lossy`
- **Types:** 3/5 matched (target 8)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/3 matched

### 17. address.domain_trie

- **Target:** `address.DomainTrie`
- **Similarity:** 0.24
- **Dependents:** 0
- **Priority Score:** 132507.6
- **Functions:** 10/23 matched (target 14)
- **Missing functions:** `default`, `fmt`, `eq`, `with_insert_domain_iter`, `insert_domain_iter`, `extend`, `iter`, `from_iter`, `test_reverse_domain`, `test_trie_most_specific_matching_parent`, `test_trie_matching_parent`, `test_trie_matching_exact`, `test_trie_iter_domain_correct_direction`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/5 matched

### 18. forwarded.proto

- **Target:** `forwarded.ForwardedProtocol`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 111609.5
- **Functions:** 4/12 matched (target 8)
- **Missing functions:** `into_protocol`, `from`, `try_from`, `from_str`, `eq`, `fmt`, `test_protocol_from_str`, `test_protocol_secure`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `ProtocolKind`, `Error`, `Err`
- **Tests:** 0/2 matched

### 19. matcher.private_ip

- **Target:** `matcher.PrivateIp`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 71109.0
- **Functions:** 3/9 matched (target 4)
- **Missing functions:** `inner_new`, `default`, `test_local_ip_net_matcher_http`, `test_local_ip_net_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 20. client.conn

- **Target:** `client.Conn`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 71007.9
- **Functions:** 1/4 matched (target 1)
- **Missing functions:** `fmt`, `connect`, `serve`
- **Types:** 2/6 matched (target 2)
- **Missing types:** `ConnectorService`, `Connection`, `Error`, `Output`

### 21. credentials.bearer

- **Target:** `credentials.Bearer`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 61009.3
- **Functions:** 3/7 matched (target 6)
- **Missing functions:** `fmt`, `new_unchecked`, `into_authorizer`, `try_from`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `Error`, `Err`

### 22. forwarded.obfuscated

- **Target:** `forwarded.Obfuscated`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 61008.8
- **Functions:** 4/10 matched (target 23)
- **Missing functions:** `test_obf_node_parse_valid`, `test_obf_node_parse_lossy`, `test_obf_node_parse_invalid`, `test_obf_port_parse_valid`, `test_obf_port_parse_lossy`, `test_obf_port_parse_invalid`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/6 matched

### 23. socket.opts

- **Target:** `socket.Opts`
- **Similarity:** 0.26
- **Dependents:** 0
- **Priority Score:** 51707.4
- **Functions:** 9/11 matched
- **Missing functions:** `from`, `deserialize`
- **Types:** 3/6 matched (target 5)
- **Missing types:** `Protocol`, `Type`, `Variants`

### 24. asn

- **Target:** `ramanet.Asn`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 51208.1
- **Functions:** 6/9 matched (target 16)
- **Missing functions:** `as_u32`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `AsnData`, `Error`

### 25. matcher.loopback

- **Target:** `matcher.Loopback`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 51007.7
- **Functions:** 3/8 matched (target 11)
- **Missing functions:** `default`, `test_loopback_matcher_http`, `test_loopback_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/4 matched

### 26. matcher.socket

- **Target:** `matcher.Socket`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 50907.5
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_socket_matcher_http`, `test_socket_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 27. matcher.port

- **Target:** `matcher.Port`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 50907.1
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_port_matcher_http`, `test_port_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 28. socket.svc

- **Target:** `socket.Svc`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 40410.0
- **Functions:** 0/1 matched
- **Missing functions:** `bind`
- **Types:** 0/3 matched (target 0)
- **Missing types:** `SocketService`, `Socket`, `Error`

### 29. transport

- **Target:** `ramanet.Transport`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 30607.5
- **Functions:** 1/2 matched (target 3)
- **Missing functions:** `try_from`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `TryRefIntoTransportContext`, `Error`

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

