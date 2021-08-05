package ch.feol.eo4j;

import ch.feol.eo4j.channel.RelayChannel;

public class ChannelFlipper {

   public static void main(String[] args) throws InterruptedException {

      RelayChannel[] channels = RelayChannel.values();
      
      for (int i = 0; i < channels.length; i++) {
         RelayChannel channel = channels[i];
         channel.low();
         Thread.sleep(200);
         channel.high();
         Thread.sleep(200);
      }

      for (int i = 0; i < channels.length; i++) {
         RelayChannel channel = channels[i];
         channel.low();
         Thread.sleep(200);
      }

      Thread.sleep(500);

      for (int i = channels.length - 1; i >= 0; i--) {
         RelayChannel channel = channels[i];
         channel.high();
         Thread.sleep(200);
      }

      for (int i = 0; i < channels.length; i++) {
         RelayChannel channel = channels[i];
         channel.low();
      }

      Thread.sleep(500);
   }
}
