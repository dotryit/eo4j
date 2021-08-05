package ch.feol.eo4j;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.feol.eo4j.channel.RelayChannel;

public enum RealBoiler implements Boiler {

   B1L(RelayChannel.C1, "Boiler 1L", LocalTime.of(22, 30), LocalTime.of(02, 30)),
   B1R(RelayChannel.C2, "Boiler 1R", LocalTime.of(23, 15), LocalTime.of(03, 15)),
   B2L(RelayChannel.C3, "Boiler 2L", LocalTime.of(00, 00), LocalTime.of(04, 00)),
   B2R(RelayChannel.C4, "Boiler 2R", LocalTime.of(00, 45), LocalTime.of(04, 45)),
   B3L(RelayChannel.C5, "Boiler 3L", LocalTime.of(01, 30), LocalTime.of(05, 30)),
   B3R(RelayChannel.C6, "Boiler 3R", LocalTime.of(02, 15), LocalTime.of(06, 15));

   private static final Logger LOG = LoggerFactory.getLogger(RealBoiler.class);

   private RelayChannel channel;

   private String name;

   private LocalTime lowStart;

   private LocalTime lowEnd;

   private RealBoiler(RelayChannel channel, String name, LocalTime lowStart, LocalTime lowEnd) {
      this.channel = channel;
      this.name = name;
      this.lowStart = lowStart;
      this.lowEnd = lowEnd;
   }

   public void on(LocalTime at) {
      if (channel.high()) {
         LOG.info(name + " changed to on at " + at);
      }
   }

   public void off(LocalTime at) {
      if (channel.low()) {
         LOG.info(name + " changed to off at " + at);
      }
   }

   public LocalTime startAtNight() {
      return lowStart;
   }

   public LocalTime endAtNight() {
      return lowEnd;
   }
   
   @Override
   public void manage(LocalTime now) {
      if (lowStart.isBefore(lowEnd)) {
         if (now.isAfter(lowStart) && now.isBefore(lowEnd)) {
            off(now);
         } else {
            on(now);
         }
      } else {
         if (now.isAfter(lowEnd) && now.isBefore(lowStart)) {
            off(now);
         } else {
            on(now);
         }
      }
   }
}
