
@main
def main(): Unit = {
  println("--------------------------------------")
  println(s"Total packets streamed: ${SampleData.packets.size}")
  println(s"packets Inbound length: ${Analysis.totalBytesIn(SampleData.packets)} bytes")
  println(s"packets Outbound length: ${Analysis.totalBytesOut(SampleData.packets)} bytes")
  println("--------------------------------------")
  println(s"Bytes per remote host: ${Analysis.bytesPerHost(SampleData.packets)}")
  println("--------------------------------------")
  println(s"Top 3 Bytes per remote host: ${Analysis.topNHosts(SampleData.packets, 3)}")
  println("--------------------------------------")
  println(s"Packets per protocol: ${Analysis.packetsByProtocol(SampleData.packets)}")
  println("--------------------------------------")
  println(s"Probed ports per host: ${Analysis.portsPerHost(SampleData.packets)}")
  println("--------------------------------------")
  println(s"Suspected scanner hosts with threshold 5: ${Analysis.suspectedScanners(SampleData.packets, 5)}")
  println(s"Suspected scanner hosts with threshold 1: ${Analysis.suspectedScanners(SampleData.packets, 1)}")
}