# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 36/104 (34.6%)
- **Function parity:** 176/1050 matched (target 346) — 16.8%
- **Class/type parity:** 35/285 matched (target 86) — 12.3%
- **Combined symbol parity:** 211/1335 matched (target 432) — 15.8%
- **Average inline-code cosine:** 0.22 (function body across 29 matched files)
- **Average documentation cosine:** 0.53 (doc text across 29 matched files)
- **Cheat-zeroed Files:** 2
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
- **Similarity:** 0.12
- **Dependents:** 3
- **Priority Score:** 3345708.8
- **Functions:** 22/50 matched (target 28)
- **Missing functions:** `from_maybe_borrowed_unchecked`, `have_same_registrable_domain`, `hash`, `as_ref`, `fmt`, `from_str`, `try_from`, `cmp_domain`, `partial_cmp`, `cmp`, `partial_eq_domain`, `eq`, `is_valid_label`, `domain_as_str`, `into_domain`, `test_specials`, `test_domain_parse_valid`, `test_domain_is_wildcard`, `test_domain_as_wildcard_parent`, `test_domain_parse_invalid`, `is_parent`, `as_wildcard_sub`, `as_sub_success`, `as_sub_failure`, `is_not_parent`, `is_equal`, `is_not_equal`, `test_hash`
- **Types:** 1/7 matched (target 2)
- **Missing types:** `Err`, `Error`, `AsDomainRef`, `IntoDomain`, `AsDomainRefPrivate`, `IntoDomainImpl`
- **Tests:** 0/13 matched

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

### 3. address.host_with_opt_port

- **Target:** `address.HostWithOptPort`
- **Similarity:** 0.06
- **Dependents:** 2
- **Priority Score:** 2273409.5
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_port`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 4. address.host

- **Target:** `address.Host`
- **Similarity:** 0.07
- **Dependents:** 2
- **Priority Score:** 2172909.2
- **Functions:** 11/25 matched (target 29)
- **Missing functions:** `eq`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_bytes_to_ip`, `assert_is`, `test_parse_specials`, `test_parse_bytes_valid`, `test_parse_valid`, `test_parse_str_invalid`, `compare_host_with_ipv4_bidirectional`, `compare_host_with_ipv6_bidirectional`, `compare_host_with_ip_bidirectional`
- **Types:** 1/4 matched
- **Missing types:** `Err`, `Error`, `Is`
- **Tests:** 0/8 matched

### 5. socket.interface

- **Target:** `socket.Interface`
- **Similarity:** 0.20
- **Dependents:** 2
- **Priority Score:** 2142908.0
- **Functions:** 13/24 matched (target 26)
- **Missing functions:** `new`, `fmt`, `from_str`, `from`, `assert_eq_socket_address`, `test_parse_valid_socket_address`, `assert_eq_device_name`, `test_parse_valid_device_name`, `test_parse_invalid`, `test_parse_display_address`, `test_parse_display_device_name`
- **Types:** 2/5 matched (target 6)
- **Missing types:** `Err`, `Error`, `Variants`
- **Tests:** 0/7 matched

### 6. address.socket_address

- **Target:** `address.SocketAddress`
- **Similarity:** 0.12
- **Dependents:** 2
- **Priority Score:** 2142208.8
- **Functions:** 7/19 matched (target 11)
- **Missing functions:** `eq`, `new`, `from_std`, `into_std`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 7. address.domain_address

- **Target:** `address.DomainAddress`
- **Similarity:** 0.25
- **Dependents:** 1
- **Priority Score:** 1112007.5
- **Functions:** 8/17 matched (target 11)
- **Missing functions:** `new`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_valid_domain_address`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 8. stream.socket

- **Target:** `stream.Socket`
- **Similarity:** 0.04
- **Dependents:** 1
- **Priority Score:** 1071109.6
- **Functions:** 1/7 matched (target 3)
- **Missing functions:** `local_addr`, `peer_addr`, `as_ref`, `as_mut`, `deref`, `deref_mut`
- **Types:** 3/4 matched (target 3)
- **Missing types:** `Target`

### 9. forwarded.version

- **Target:** `forwarded.ForwardedVersion`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1050610.0
- **Functions:** 0/3 matched
- **Missing functions:** `as_http`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `VersionKind`, `Error`

### 10. address.authority

- **Target:** `address.Authority`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 273409.3
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_user_info`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched

### 11. address.domain_trie

- **Target:** `address.DomainTrie`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 212509.7
- **Functions:** 2/23 matched (target 6)
- **Missing functions:** `default`, `fmt`, `eq`, `new`, `len`, `with_insert_domain`, `insert_domain`, `with_insert_domain_iter`, `insert_domain_iter`, `extend`, `is_match_parent`, `is_match_exact`, `match_exact`, `iter`, `reverse_domain`, `from_iter`, `test_reverse_domain`, `test_trie_most_specific_matching_parent`, `test_trie_matching_parent`, `test_trie_matching_exact`, `test_trie_iter_domain_correct_direction`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/5 matched

### 12. matcher.mod

- **Target:** `matcher.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 174910.0
- **Functions:** 31/46 matched (target 31)
- **Missing functions:** `clone`, `fmt`, `ip_net`, `optional_ip_net`, `and_ip_net`, `and_optional_ip_net`, `or_ip_net`, `or_optional_ip_net`, `test_matcher_and_combination`, `test_matcher_negation_with_and_combination`, `test_matcher_and_combination_negated`, `test_matcher_ors_combination`, `test_matcher_negation_with_ors_combination`, `test_matcher_ors_combination_negated`, `test_matcher_or_and_or_and_negation`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `SocketMatcherKind`, `BooleanMatcher`
- **Tests:** 0/7 matched

### 13. address.proxy

- **Target:** `address.Proxy`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 172009.7
- **Functions:** 2/17 matched (target 4)
- **Missing functions:** `try_from`, `from_str`, `fmt`, `test_valid_proxy`, `test_valid_domain_proxy`, `test_valid_proxy_with_credential`, `test_valid_proxy_with_insecure_credential`, `test_valid_http_proxy`, `test_valid_http_proxy_with_credential`, `test_valid_http_proxy_with_insecure_credential`, `test_valid_https_proxy`, `test_valid_https_proxy_with_insecure_credentials`, `test_valid_socks5h_proxy`, `test_valid_socks5h_proxy_trailing_colon`, `test_valid_proxy_address_symmetric`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Error`, `Err`
- **Tests:** 0/12 matched

### 14. proto

- **Target:** `ramanet.Proto`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 142409.2
- **Functions:** 10/20 matched (target 31)
- **Missing functions:** `as_str`, `try_to_convert_str_to_non_custom_protocol`, `try_from`, `from_str`, `from`, `eq`, `fmt`, `validate_scheme_str`, `validate_scheme_slice`, `test_from_http_scheme`
- **Types:** 0/4 matched (target 11)
- **Missing types:** `Protocol`, `ProtocolKind`, `Error`, `Err`
- **Tests:** 3/4 matched

### 15. forwarded.node

- **Target:** `forwarded.Node`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 132508.6
- **Functions:** 9/20 matched (target 27)
- **Missing functions:** `port`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_str_to_ip`, `try_to_split_node_port_from_str`, `try_to_split_node_port_lossy_from_str`, `test_parse_node_id_valid`, `test_parse_node_id_invalid`, `test_parse_node_id_lossy`
- **Types:** 3/5 matched (target 8)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/3 matched

### 16. forwarded.proto

- **Target:** `forwarded.ForwardedProtocol`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 111609.5
- **Functions:** 4/12 matched (target 8)
- **Missing functions:** `into_protocol`, `from`, `try_from`, `from_str`, `eq`, `fmt`, `test_protocol_from_str`, `test_protocol_secure`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `ProtocolKind`, `Error`, `Err`
- **Tests:** 0/2 matched

### 17. matcher.private_ip

- **Target:** `matcher.PrivateIp`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 71109.0
- **Functions:** 3/9 matched (target 4)
- **Missing functions:** `inner_new`, `default`, `test_local_ip_net_matcher_http`, `test_local_ip_net_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched

### 18. client.conn

- **Target:** `client.Conn`
- **Similarity:** 0.21
- **Dependents:** 0
- **Priority Score:** 71007.9
- **Functions:** 1/4 matched (target 1)
- **Missing functions:** `fmt`, `connect`, `serve`
- **Types:** 2/6 matched (target 2)
- **Missing types:** `ConnectorService`, `Connection`, `Error`, `Output`

### 19. forwarded.obfuscated

- **Target:** `forwarded.Obfuscated`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 61008.8
- **Functions:** 4/10 matched (target 23)
- **Missing functions:** `test_obf_node_parse_valid`, `test_obf_node_parse_lossy`, `test_obf_node_parse_invalid`, `test_obf_port_parse_valid`, `test_obf_port_parse_lossy`, `test_obf_port_parse_invalid`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/6 matched

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

### 24. transport

- **Target:** `ramanet.Transport`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 30607.5
- **Functions:** 1/2 matched (target 3)
- **Missing functions:** `try_from`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `TryRefIntoTransportContext`, `Error`

### 25. user.id

- **Target:** `user.UserId`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10210.0
- **Functions:** 0/1 matched (target 5)
- **Missing functions:** `eq`
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_

### 26. mode

- **Target:** `ramanet.Mode`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 404.8
- **Functions:** 2/2 matched (target 3)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_

### 27. client.mod

- **Target:** `client.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 28. conn

- **Target:** `ramanet.Conn`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 109.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 29. address.ip

- **Target:** `address.Ip`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 30. client.either_conn

- **Target:** `client.EitherConn`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 7)
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
| `forwarded.mod` | `forwarded.Mod` | `forwarded/mod` |
| `address.parse_utils` | `address.ParseUtils` | `address/parse_utils` |
| `credentials.mod` | `user.Credentials` | `user/credentials/mod` |
| `stream.mod` | `stream.Mod` | `stream/mod` |
| `user.mod` | `user.Mod` | `user/mod` |
| `address.mod` | `address.Mod` | `address/mod` |

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

