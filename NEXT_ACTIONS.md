# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 35/104 (33.7%)
- **Function parity:** 119/1077 matched (target 316) — 11.0%
- **Class/type parity:** 32/290 matched (target 91) — 11.0%
- **Combined symbol parity:** 151/1367 matched (target 407) — 11.0%
- **Average inline-code cosine:** 0.20 (function body across 29 matched files)
- **Average documentation cosine:** 0.53 (doc text across 29 matched files)
- **Cheat-zeroed Files:** 8
- **Critical Issues:** 32 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

1. **match_replace.fmt** (11 deps)
   - Path: `rama-net/src/http/uri/match_replace/fmt.rs`
   - Essential for 11 other files

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. address.domain

- **Target:** `address.Domain [PROVENANCE-FALLBACK]`
- **Similarity:** 0.09
- **Dependents:** 3
- **Priority Score:** 3385709.0
- **Functions:** 18/50 matched (target 24)
- **Missing functions:** `from_maybe_borrowed_unchecked`, `into_host`, `have_same_registrable_domain`, `len`, `as_str`, `into_inner`, `hash`, `as_ref`, `fmt`, `from_str`, `try_from`, `cmp_domain`, `partial_cmp`, `cmp`, `partial_eq_domain`, `eq`, `is_valid_label`, `domain_as_str`, `into_domain`, `test_specials`, `test_domain_parse_valid`, `test_domain_is_wildcard`, `test_domain_as_wildcard_parent`, `test_domain_parse_invalid`, `is_parent`, `as_wildcard_sub`, `as_sub_success`, `as_sub_failure`, `is_not_parent`, `is_equal`, `is_not_equal`, `test_hash`
- **Types:** 1/7 matched (target 2)
- **Missing types:** `Err`, `Error`, `AsDomainRef`, `IntoDomain`, `AsDomainRefPrivate`, `IntoDomainImpl`
- **Tests:** 0/13 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/domain.rs` vs expected `address/domain.rs`
- **Proposed provenance header:** `// port-lint: source address/domain.rs` (current: `// port-lint: source address/domain.rs`)
- **Lint issues:** 1

### 2. address.host_with_port

- **Target:** `address.HostWithPort [PROVENANCE-FALLBACK]`
- **Similarity:** 0.02
- **Dependents:** 3
- **Priority Score:** 3222509.8
- **Functions:** 2/22 matched (target 10)
- **Missing functions:** `new`, `local_ipv4`, `local_ipv6`, `default_ipv4`, `default_ipv6`, `broadcast_ipv4`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/host_with_port.rs` vs expected `address/host_with_port.rs`
- **Proposed provenance header:** `// port-lint: source address/host_with_port.rs` (current: `// port-lint: source address/host_with_port.rs`)
- **Lint issues:** 1

### 3. address.host_with_opt_port

- **Target:** `address.HostWithOptPort [PROVENANCE-FALLBACK]`
- **Similarity:** 0.06
- **Dependents:** 2
- **Priority Score:** 2273409.5
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_port`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/host_with_opt_port.rs` vs expected `address/host_with_opt_port.rs`
- **Proposed provenance header:** `// port-lint: source address/host_with_opt_port.rs` (current: `// port-lint: source address/host_with_opt_port.rs`)
- **Lint issues:** 1

### 4. address.host

- **Target:** `address.Host [PROVENANCE-FALLBACK]`
- **Similarity:** 0.03
- **Dependents:** 2
- **Priority Score:** 2242909.8
- **Functions:** 4/25 matched (target 20)
- **Missing functions:** `is_domain`, `into_domain`, `is_ip`, `into_ip`, `is_ipv4`, `is_ipv6`, `to_str`, `eq`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_bytes_to_ip`, `assert_is`, `test_parse_specials`, `test_parse_bytes_valid`, `test_parse_valid`, `test_parse_str_invalid`, `compare_host_with_ipv4_bidirectional`, `compare_host_with_ipv6_bidirectional`, `compare_host_with_ip_bidirectional`
- **Types:** 1/4 matched
- **Missing types:** `Err`, `Error`, `Is`
- **Tests:** 0/8 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/host.rs` vs expected `address/host.rs`
- **Proposed provenance header:** `// port-lint: source address/host.rs` (current: `// port-lint: source address/host.rs`)
- **Lint issues:** 1

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

- **Target:** `address.SocketAddress [PROVENANCE-FALLBACK]`
- **Similarity:** 0.12
- **Dependents:** 2
- **Priority Score:** 2142208.8
- **Functions:** 7/19 matched (target 11)
- **Missing functions:** `eq`, `new`, `from_std`, `into_std`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/socket_address.rs` vs expected `address/socket_address.rs`
- **Proposed provenance header:** `// port-lint: source address/socket_address.rs` (current: `// port-lint: source address/socket_address.rs`)
- **Lint issues:** 1

### 7. address.domain_address

- **Target:** `address.DomainAddress [PROVENANCE-FALLBACK]`
- **Similarity:** 0.25
- **Dependents:** 1
- **Priority Score:** 1112007.5
- **Functions:** 8/17 matched (target 11)
- **Missing functions:** `new`, `from`, `fmt`, `from_str`, `try_from`, `assert_eq`, `test_valid_domain_address`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/domain_address.rs` vs expected `address/domain_address.rs`
- **Proposed provenance header:** `// port-lint: source address/domain_address.rs` (current: `// port-lint: source address/domain_address.rs`)
- **Lint issues:** 1

### 8. stream.socket

- **Target:** `stream.Socket [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1091110.0
- **Functions:** 0/7 matched (target 0)
- **Missing functions:** `local_addr`, `peer_addr`, `as_ref`, `as_mut`, `deref`, `deref_mut`, `new`
- **Types:** 2/4 matched (target 2)
- **Missing types:** `ClientSocketInfo`, `Target`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream/socket.rs` vs expected `stream/socket.rs`
- **Proposed provenance header:** `// port-lint: source stream/socket.rs` (current: `// port-lint: source stream/socket.rs`)
- **Lint issues:** 1

### 9. forwarded.version

- **Target:** `forwarded.ForwardedVersion [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1050610.0
- **Functions:** 0/3 matched
- **Missing functions:** `as_http`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 1)
- **Missing types:** `VersionKind`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `forwarded/version.rs` vs expected `forwarded/version.rs`
- **Proposed provenance header:** `// port-lint: source forwarded/version.rs` (current: `// port-lint: source forwarded/version.rs`)
- **Lint issues:** 1

### 10. address.authority

- **Target:** `address.Authority [PROVENANCE-FALLBACK]`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 273409.3
- **Functions:** 6/31 matched (target 13)
- **Missing functions:** `new`, `new_with_user_info`, `default_ipv4`, `default_ipv4_with_port`, `default_ipv6`, `default_ipv6_with_port`, `broadcast_ipv4`, `broadcast_ipv4_with_port`, `example_domain`, `example_domain_http`, `example_domain_https`, `example_domain_with_port`, `localhost_domain`, `localhost_domain_http`, `localhost_domain_https`, `localhost_domain_with_port`, `from`, `fmt`, `from_str`, `try_from`, `try_from_maybe_borrowed_str`, `assert_eq`, `test_parse_valid`, `test_parse_invalid`, `test_parse_display`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/authority.rs` vs expected `address/authority.rs`
- **Proposed provenance header:** `// port-lint: source address/authority.rs` (current: `// port-lint: source address/authority.rs`)
- **Lint issues:** 1

### 11. forwarded.mod

- **Target:** `forwarded.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 242410.0
- **Functions:** 0/19 matched (target 13)
- **Missing functions:** `new`, `client_host`, `client_socket_addr`, `client_port`, `client_ip`, `client_proto`, `client_version`, `append`, `extend`, `iter`, `into_iter`, `from`, `fmt`, `from_str`, `try_from`, `test_forwarded_parse_invalid`, `test_forwarded_parse_happy_spec`, `test_forwarded_client_authority`, `test_forwarded_client_protoy`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `Forwarded`, `Item`, `IntoIter`, `Err`, `Error`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:forwarded/mod.rs` vs expected `forwarded/mod.rs`
- **Proposed provenance header:** `// port-lint: tests forwarded/mod.rs` (current: `// port-lint: tests forwarded/mod.rs`)
- **Lint issues:** 1

### 12. address.domain_trie

- **Target:** `address.DomainTrie [PROVENANCE-FALLBACK]`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 212509.7
- **Functions:** 2/23 matched (target 6)
- **Missing functions:** `default`, `fmt`, `eq`, `new`, `len`, `with_insert_domain`, `insert_domain`, `with_insert_domain_iter`, `insert_domain_iter`, `extend`, `is_match_parent`, `is_match_exact`, `match_exact`, `iter`, `reverse_domain`, `from_iter`, `test_reverse_domain`, `test_trie_most_specific_matching_parent`, `test_trie_matching_parent`, `test_trie_matching_exact`, `test_trie_iter_domain_correct_direction`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/domain_trie.rs` vs expected `address/domain_trie.rs`
- **Proposed provenance header:** `// port-lint: source address/domain_trie.rs` (current: `// port-lint: source address/domain_trie.rs`)
- **Lint issues:** 1

### 13. address.proxy

- **Target:** `address.Proxy [PROVENANCE-FALLBACK]`
- **Similarity:** 0.03
- **Dependents:** 0
- **Priority Score:** 172009.7
- **Functions:** 2/17 matched (target 4)
- **Missing functions:** `try_from`, `from_str`, `fmt`, `test_valid_proxy`, `test_valid_domain_proxy`, `test_valid_proxy_with_credential`, `test_valid_proxy_with_insecure_credential`, `test_valid_http_proxy`, `test_valid_http_proxy_with_credential`, `test_valid_http_proxy_with_insecure_credential`, `test_valid_https_proxy`, `test_valid_https_proxy_with_insecure_credentials`, `test_valid_socks5h_proxy`, `test_valid_socks5h_proxy_trailing_colon`, `test_valid_proxy_address_symmetric`
- **Types:** 1/3 matched (target 2)
- **Missing types:** `Error`, `Err`
- **Tests:** 0/12 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/proxy.rs` vs expected `address/proxy.rs`
- **Proposed provenance header:** `// port-lint: source address/proxy.rs` (current: `// port-lint: source address/proxy.rs`)
- **Lint issues:** 1

### 14. rama-net.proto

- **Target:** `ramanet.Proto [PROVENANCE-FALLBACK]`
- **Similarity:** 0.08
- **Dependents:** 0
- **Priority Score:** 142409.2
- **Functions:** 10/20 matched (target 31)
- **Missing functions:** `as_str`, `try_to_convert_str_to_non_custom_protocol`, `try_from`, `from_str`, `from`, `eq`, `fmt`, `validate_scheme_str`, `validate_scheme_slice`, `test_from_http_scheme`
- **Types:** 0/4 matched (target 11)
- **Missing types:** `Protocol`, `ProtocolKind`, `Error`, `Err`
- **Tests:** 3/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `proto.rs` vs expected `proto.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:proto.rs` vs expected `proto.rs`
- **Proposed provenance header:** `// port-lint: source proto.rs` (current: `// port-lint: source proto.rs`)
- **Proposed provenance header:** `// port-lint: tests proto.rs` (current: `// port-lint: tests proto.rs`)
- **Lint issues:** 2

### 15. forwarded.node

- **Target:** `forwarded.Node [PROVENANCE-FALLBACK]`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 132508.6
- **Functions:** 9/20 matched (target 27)
- **Missing functions:** `port`, `from`, `fmt`, `from_str`, `try_from`, `try_to_parse_str_to_ip`, `try_to_split_node_port_from_str`, `try_to_split_node_port_lossy_from_str`, `test_parse_node_id_valid`, `test_parse_node_id_invalid`, `test_parse_node_id_lossy`
- **Types:** 3/5 matched (target 8)
- **Missing types:** `Err`, `Error`
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `forwarded/node.rs` vs expected `forwarded/node.rs`
- **Proposed provenance header:** `// port-lint: source forwarded/node.rs` (current: `// port-lint: source forwarded/node.rs`)
- **Lint issues:** 1

### 16. forwarded.proto

- **Target:** `forwarded.ForwardedProtocol [PROVENANCE-FALLBACK]`
- **Similarity:** 0.05
- **Dependents:** 0
- **Priority Score:** 111609.5
- **Functions:** 4/12 matched (target 8)
- **Missing functions:** `into_protocol`, `from`, `try_from`, `from_str`, `eq`, `fmt`, `test_protocol_from_str`, `test_protocol_secure`
- **Types:** 1/4 matched (target 1)
- **Missing types:** `ProtocolKind`, `Error`, `Err`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `forwarded/proto.rs` vs expected `forwarded/proto.rs`
- **Proposed provenance header:** `// port-lint: source forwarded/proto.rs` (current: `// port-lint: source forwarded/proto.rs`)
- **Lint issues:** 1

### 17. client.conn

- **Target:** `client.Conn [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 91010.0
- **Functions:** 0/4 matched (target 0)
- **Missing functions:** `fmt`, `connect`, `new`, `serve`
- **Types:** 1/6 matched (target 1)
- **Missing types:** `ConnectorService`, `Connection`, `Error`, `BoxedConnectorService`, `Output`

### 18. matcher.private_ip

- **Target:** `matcher.PrivateIp [PROVENANCE-FALLBACK]`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 71109.0
- **Functions:** 3/9 matched (target 4)
- **Missing functions:** `inner_new`, `default`, `test_local_ip_net_matcher_http`, `test_local_ip_net_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream/matcher/private_ip.rs` vs expected `stream/matcher/private_ip.rs`
- **Proposed provenance header:** `// port-lint: source stream/matcher/private_ip.rs` (current: `// port-lint: source stream/matcher/private_ip.rs`)
- **Lint issues:** 1

### 19. forwarded.obfuscated

- **Target:** `forwarded.Obfuscated [PROVENANCE-FALLBACK]`
- **Similarity:** 0.12
- **Dependents:** 0
- **Priority Score:** 61008.8
- **Functions:** 4/10 matched (target 23)
- **Missing functions:** `test_obf_node_parse_valid`, `test_obf_node_parse_lossy`, `test_obf_node_parse_invalid`, `test_obf_port_parse_valid`, `test_obf_port_parse_lossy`, `test_obf_port_parse_invalid`
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Tests:** 0/6 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `forwarded/obfuscated.rs` vs expected `forwarded/obfuscated.rs`
- **Proposed provenance header:** `// port-lint: source forwarded/obfuscated.rs` (current: `// port-lint: source forwarded/obfuscated.rs`)
- **Lint issues:** 1

### 20. rama-net.asn

- **Target:** `ramanet.Asn [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 51208.1
- **Functions:** 6/9 matched (target 16)
- **Missing functions:** `as_u32`, `try_from`, `fmt`
- **Types:** 1/3 matched (target 4)
- **Missing types:** `AsnData`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `asn.rs` vs expected `asn.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:asn.rs` vs expected `asn.rs`
- **Proposed provenance header:** `// port-lint: source asn.rs` (current: `// port-lint: source asn.rs`)
- **Proposed provenance header:** `// port-lint: tests asn.rs` (current: `// port-lint: tests asn.rs`)
- **Lint issues:** 2

### 21. matcher.loopback

- **Target:** `matcher.Loopback [PROVENANCE-FALLBACK]`
- **Similarity:** 0.23
- **Dependents:** 0
- **Priority Score:** 51007.7
- **Functions:** 3/8 matched (target 10)
- **Missing functions:** `default`, `test_loopback_matcher_http`, `test_loopback_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream/matcher/loopback.rs` vs expected `stream/matcher/loopback.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:stream/matcher/loopback.rs` vs expected `stream/matcher/loopback.rs`
- **Proposed provenance header:** `// port-lint: source stream/matcher/loopback.rs` (current: `// port-lint: source stream/matcher/loopback.rs`)
- **Proposed provenance header:** `// port-lint: tests stream/matcher/loopback.rs` (current: `// port-lint: tests stream/matcher/loopback.rs`)
- **Lint issues:** 2

### 22. matcher.socket

- **Target:** `matcher.Socket [PROVENANCE-FALLBACK]`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 50907.5
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_socket_matcher_http`, `test_socket_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream/matcher/socket.rs` vs expected `stream/matcher/socket.rs`
- **Proposed provenance header:** `// port-lint: source stream/matcher/socket.rs` (current: `// port-lint: source stream/matcher/socket.rs`)
- **Lint issues:** 1

### 23. matcher.port

- **Target:** `matcher.Port [PROVENANCE-FALLBACK]`
- **Similarity:** 0.29
- **Dependents:** 0
- **Priority Score:** 50907.1
- **Functions:** 3/7 matched (target 3)
- **Missing functions:** `test_port_matcher_http`, `test_port_matcher_socket_trait`, `local_addr`, `peer_addr`
- **Types:** 1/2 matched (target 1)
- **Missing types:** `FakeSocket`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream/matcher/port.rs` vs expected `stream/matcher/port.rs`
- **Proposed provenance header:** `// port-lint: source stream/matcher/port.rs` (current: `// port-lint: source stream/matcher/port.rs`)
- **Lint issues:** 1

### 24. rama-net.transport

- **Target:** `ramanet.Transport [PROVENANCE-FALLBACK]`
- **Similarity:** 0.25
- **Dependents:** 0
- **Priority Score:** 30607.5
- **Functions:** 1/2 matched (target 3)
- **Missing functions:** `try_from`
- **Types:** 2/4 matched (target 3)
- **Missing types:** `TryRefIntoTransportContext`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `transport.rs` vs expected `transport.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:transport.rs` vs expected `transport.rs`
- **Proposed provenance header:** `// port-lint: source transport.rs` (current: `// port-lint: source transport.rs`)
- **Proposed provenance header:** `// port-lint: tests transport.rs` (current: `// port-lint: tests transport.rs`)
- **Lint issues:** 2

### 25. user.id

- **Target:** `user.UserId [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10210.0
- **Functions:** 0/1 matched (target 5)
- **Missing functions:** `eq`
- **Types:** 1/1 matched (target 4)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `user/id.rs` vs expected `user/id.rs`
- **Proposed provenance header:** `// port-lint: source user/id.rs` (current: `// port-lint: source user/id.rs`)
- **Lint issues:** 1

### 26. rama-net.mode

- **Target:** `ramanet.Mode [PROVENANCE-FALLBACK]`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 404.8
- **Functions:** 2/2 matched (target 3)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 3)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `mode.rs` vs expected `mode.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:mode.rs` vs expected `mode.rs`
- **Proposed provenance header:** `// port-lint: source mode.rs` (current: `// port-lint: source mode.rs`)
- **Proposed provenance header:** `// port-lint: tests mode.rs` (current: `// port-lint: tests mode.rs`)
- **Lint issues:** 2

### 27. address.parse_utils

- **Target:** `address.ParseUtils [PROVENANCE-FALLBACK]`
- **Similarity:** 0.68
- **Dependents:** 0
- **Priority Score:** 203.2
- **Functions:** 2/2 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/parse_utils.rs` vs expected `address/parse_utils.rs`
- **Proposed provenance header:** `// port-lint: source address/parse_utils.rs` (current: `// port-lint: source address/parse_utils.rs`)
- **Lint issues:** 1

### 28. client.mod

- **Target:** `client.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 110.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 29. rama-net.conn

- **Target:** `ramanet.Conn`
- **Similarity:** 0.10
- **Dependents:** 0
- **Priority Score:** 109.0
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 30. credentials.mod

- **Target:** `user.Credentials [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 9)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `user/credentials/mod.rs` vs expected `user/credentials/mod.rs`
- **Proposed provenance header:** `// port-lint: source user/credentials/mod.rs` (current: `// port-lint: source user/credentials/mod.rs`)
- **Lint issues:** 1

### 31. address.mod

- **Target:** `address.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 9)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:address/mod.rs` vs expected `address/mod.rs`
- **Proposed provenance header:** `// port-lint: tests address/mod.rs` (current: `// port-lint: tests address/mod.rs`)
- **Lint issues:** 1

### 32. stream.mod

- **Target:** `stream.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 33. user.mod

- **Target:** `user.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 3)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:user/mod.rs` vs expected `user/mod.rs`
- **Proposed provenance header:** `// port-lint: tests user/mod.rs` (current: `// port-lint: tests user/mod.rs`)
- **Lint issues:** 1

### 34. address.ip

- **Target:** `address.Ip [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `address/ip.rs` vs expected `address/ip.rs`
- **Proposed provenance header:** `// port-lint: source address/ip.rs` (current: `// port-lint: source address/ip.rs`)
- **Lint issues:** 1

### 35. client.either_conn

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

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `rama-net.lib` | `ramanet.src.Lib` | 0 | `rama-net/src/lib.rs` | `ramanet/src/Lib.kt` |

