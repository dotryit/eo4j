package ch.feol.eo4j.solarlog;

public class ReadSolektra {

   public static void main(String[] args) throws SolarLogException, InterruptedException {

      SolarLog solarLog = new SolarLog("192.168.178.36");
      while (true) {
         System.out.println(solarLog.read());
         Thread.sleep(1000);
      }
   }
}
