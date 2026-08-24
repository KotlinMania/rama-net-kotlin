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
}
