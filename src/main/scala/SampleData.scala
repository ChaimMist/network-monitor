object SampleData {
  private val myIp: String = "192.168.1.100"
  private val dnsServer: String = "1.1.1.1"
  private val attackerIp: String = "203.0.113.66"

  val packets: List[Packet] = List(
    createHttpPacket(Direction.Outbound, "142.250.75.14", 105),
    createHttpPacket(Direction.Inbound,  "142.250.75.14", 300),
    createHttpPacket(Direction.Outbound, "140.82.121.4", 512),
    createHttpPacket(Direction.Outbound, "140.82.121.4", 1024),
    createHttpPacket(Direction.Outbound, "140.82.121.4", 100),
    createHttpPacket(Direction.Inbound,  "140.82.121.4", 512),
    createHttpPacket(Direction.Inbound,  "140.82.121.4", 1024),
    createHttpPacket(Direction.Outbound, "123.250.75.14", 105),
    createHttpPacket(Direction.Inbound,  "123.250.75.14", 1024),
    createHttpPacket(Direction.Inbound,  "123.250.75.14", 105),

    createDnsPacket(67, "example.com"),
    createDnsPacket(80, "youtube.com"),
    createDnsPacket(80, "youtube.com"),
    createDnsPacket(73, "google.com"),

    createPingPacket(Direction.Internal, myIp, 64),
    createPingPacket(Direction.Outbound, "142.250.75.14", 98),

    createScanPacket(22),
    createScanPacket(23),
    createScanPacket(80),
    createScanPacket(443),
    createScanPacket(3389),
    createScanPacket(8080)
  )

  private def orient(
                      direction: Direction,
                      remoteIp: String,
                      localPort: Option[Int],
                      remotePort: Option[Int]
                    ): (String, String, Option[Int], Option[Int]) =
    direction match {
      case Direction.Inbound                       => (remoteIp, myIp, remotePort, localPort)
      case Direction.Outbound | Direction.Internal => (myIp, remoteIp, localPort, remotePort)
    }

  private def build(
                     direction: Direction,
                     remoteIp: String,
                     protocol: Protocol,
                     localPort: Option[Int],
                     remotePort: Option[Int],
                     size: Int,
                     dnsQuery: Option[String] = None
                   ): Packet = {
    val (srcIp, dstIp, srcPort, dstPort) = orient(direction, remoteIp, localPort, remotePort)
    Packet(
      timestamp = System.currentTimeMillis(),
      direction = direction,
      srcIp = srcIp,
      dstIp = dstIp,
      length = size,
      protocol = protocol,
      srcPort = srcPort,
      dstPort = dstPort,
      dnsQuery = dnsQuery
    )
  }


  private def createHttpPacket(direction: Direction, remoteIp: String, size: Int): Packet =
    build(direction, remoteIp, Protocol.TCP, localPort = Some(8080), remotePort = Some(80), size)

  private def createDnsPacket(size: Int, query: String): Packet =
    build(Direction.Outbound, dnsServer, Protocol.UDP, localPort = Some(54012), remotePort = Some(53), size, Some(query))

  private def createPingPacket(direction: Direction, remoteIp: String, size: Int): Packet =
    build(direction, remoteIp, Protocol.ICMP, localPort = None, remotePort = None, size)

  private def createScanPacket(targetPort: Int): Packet =
    build(Direction.Inbound, attackerIp, Protocol.TCP, localPort = Some(targetPort), remotePort = Some(61000), size = 60)
}