import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.OverflowStrategy
import org.apache.pekko.stream.scaladsl.Source

import scala.concurrent.duration.DurationInt

@main
def main(): Unit = {
//  println("--------------------------------------")
//  println(s"Total packets streamed: ${SampleData.packets.size}")
//  println(s"packets Inbound length: ${Analysis.totalBytesIn(SampleData.packets)} bytes")
//  println(s"packets Outbound length: ${Analysis.totalBytesOut(SampleData.packets)} bytes")
//  println("--------------------------------------")
//  println(s"Bytes per remote host: ${Analysis.bytesPerHost(SampleData.packets)}")
//  println("--------------------------------------")
//  println(s"Top 3 Bytes per remote host: ${Analysis.topNHosts(SampleData.packets, 3)}")
//  println("--------------------------------------")
//  println(s"Packets per protocol: ${Analysis.packetsByProtocol(SampleData.packets)}")
//  println("--------------------------------------")
//  println(s"Probed ports per host: ${Analysis.portsPerHost(SampleData.packets)}")
//  println("--------------------------------------")
//  println(s"Suspected scanner hosts with threshold 5: ${Analysis.suspectedScanners(SampleData.packets, 5)}")
//  println(s"Suspected scanner hosts with threshold 1: ${Analysis.suspectedScanners(SampleData.packets, 1)}")

  given system: ActorSystem = ActorSystem("netwatch")
  import system.dispatcher

//  val done = FakeTraffic.createSource().groupedWithin(1000, 5.seconds).take(4).runForeach(seqPacket => printPacketsSummary(seqPacket.toList))
  val done = FakeTraffic.createSource().zipWithIndex.buffer(10, OverflowStrategy.dropNew).async.map(p => {Thread.sleep(500); p })
    .take(40)
    .runForeach((packet, index) => println(s"#$index $packet"))

  done.onComplete(_ => system.terminate())

}

private def printPacketsSummary(packets: List[Packet]): Unit =
    println("----------------START SUMMARY----------------------")
    println(s"Total packets streamed: ${packets.size}")
    println(s"packets Inbound length: ${Analysis.totalBytesIn(packets)} bytes")
    println(s"packets Outbound length: ${Analysis.totalBytesOut(packets)} bytes")
    println(s"Top 3 Bytes per remote host: ${Analysis.topNHosts(packets, 3)}")
    println(s"Suspected scanner hosts with threshold 3: ${Analysis.suspectedScanners(packets, 3)}")
    println("----------------END SUMMARY----------------------\n\n")
