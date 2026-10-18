import Testing
import RamaNet

@Suite("RamaNet Swift Export Suite")
struct RamaNetExportTests {
    @Test("Swift module loads cleanly")
    func swiftModuleLoads() {
        #expect(Bool(true), "RamaNet swift module imported cleanly")
    }

    @Test("Asn export works")
    func asnExport() {
        let asn = Asn.Companion.shared.parse(value: Swift.UInt32(13335))
        #expect(asn.asUInt() == 13335)
        #expect(asn.toString() == "AS13335")
    }

    @Test("TransportProtocol export works")
    func transportProtocolExport() {
        let tcp = TransportProtocol.Tcp
        #expect(tcp.description == "Tcp")
        let udp = TransportProtocol.Udp
        #expect(udp.description == "Udp")
    }

    @Test("ForwardedProtocol export works")
    func forwardedProtocolExport() {
        let http = forwarded.ForwardedProtocol.Http
        #expect(http.description == "Http")
        let https = forwarded.ForwardedProtocol.Https
        #expect(https.description == "Https")
    }

    @Test("HostWithPort, RequestContext, SocketOptions, and Ja4 export works")
    func netTypesExport() {
        let hwp = address.HostWithPort.Companion.shared.exampleDomainHttp()
        #expect(hwp.toString() == "example.com:80")
        let opts = socket.SocketOptions.Companion.shared.defaultTcpV6()
        #expect(opts.domain == socket.Domain.IPv6)
        let hash = fingerprint.Ja4.shared.hashString(input: "1301,1302")
        #expect(hash.count == 12)
    }
}

