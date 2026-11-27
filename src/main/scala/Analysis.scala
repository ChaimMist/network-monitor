object Analysis {

  def totalBytesIn(packets: List[Packet]): Long =
    packets.filter(packet => packet.direction == Direction.Inbound).map(packet => packet.length.toLong).sum

  def totalBytesOut(packets: List[Packet]): Long =
    packets.filter(packet => packet.direction == Direction.Outbound).map(packet => packet.length.toLong).sum

  def bytesPerHost(packets: List[Packet]): Map[String, Long] =
    packets.groupBy(packet => packet.remoteIp).map((host, hostPackets) => (host, hostPackets.map(_.length.toLong).sum))

  def topNHosts(packets: List[Packet], amount: Int): List[(String, Long)] =
    bytesPerHost(packets).toList.sortBy((_, bytes) => -bytes).take(amount)

  def packetsByProtocol(packets: List[Packet]): Map[Protocol, Int] =
    packets.groupBy(packet => packet.protocol).map((protocol, packets) => (protocol, packets.size))
    
  def portsPerHost(packets: List[Packet]): Map[String, Set[Int]] =
    packets.filter(packet => packet.direction == Direction.Inbound)
      .groupBy(packet => packet.remoteIp)
      .map((remoteHost, hostPackets) => (remoteHost, hostPackets.flatMap(packet => packet.dstPort).toSet))
      
  def suspectedScanners(packets: List[Packet], threshold: Int): Map[String, Set[Int]] =
    portsPerHost(packets).filter((host, hostPorts) => hostPorts.size >= threshold)
}
