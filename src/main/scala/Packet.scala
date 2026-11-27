case class Packet(timestamp: Long,
                  direction: Direction,
                  srcIp: String,
                  dstIp: String,
                  length: Int,
                  protocol: Protocol,
                  srcPort: Option[Int],
                  dstPort: Option[Int],
                  dnsQuery: Option[String]) {
  def remoteIp: String = direction match {
    case Direction.Inbound => srcIp
    case Direction.Outbound | Direction.Internal => dstIp
  }
}


sealed trait Direction
object Direction {
  case object Inbound extends Direction
  case object Outbound extends Direction
  case object Internal extends Direction

}

sealed trait Protocol
object Protocol {
  case object TCP extends Protocol
  case object UDP extends Protocol
  case object ICMP extends Protocol
}
