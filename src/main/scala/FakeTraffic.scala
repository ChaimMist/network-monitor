import org.apache.pekko.NotUsed
import org.apache.pekko.stream.OverflowStrategy
import org.apache.pekko.stream.scaladsl.Source

import scala.concurrent.duration.*
import scala.util.Random


object FakeTraffic {

  def createSource(): Source[Packet, NotUsed] =
    Source.repeat(()).throttle(50, 1.second)
      .map(_ => randomPacket()
        .copy(timestamp = System.currentTimeMillis()))

  private def randomPacket(): Packet =
    SampleData.packets(Random.nextInt(SampleData.packets.size))

}