package ch.feol.eo4j;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SimulatedBoiler implements Boiler {

   B1L("Boiler 1L", LocalTime.of(22, 30), LocalTime.of(02, 30)),
   B1R("Boiler 1R", LocalTime.of(23, 15), LocalTime.of(03, 15)),
   B2L("Boiler 2L", LocalTime.of(00, 00), LocalTime.of(04, 00)),
   B2R("Boiler 2R", LocalTime.of(00, 45), LocalTime.of(04, 45)),
   B3L("Boiler 3L", LocalTime.of(01, 30), LocalTime.of(05, 30)),
   B3R("Boiler 3R", LocalTime.of(02, 15), LocalTime.of(06, 15));

   private static final Logger LOG = LoggerFactory.getLogger(SimulatedBoiler.class);

   private boolean on;
   
   private String name;

   private LocalTime lowStart;

   private LocalTime lowEnd;

   private SimulatedBoiler(String name, LocalTime lowStart, LocalTime lowEnd) {
      this.name = name;
      this.lowStart = lowStart;
      this.lowEnd = lowEnd;
   }

   public void on(LocalTime at) {
      if (!on) {
         on = true;
         LOG.info(name + " changed to on at " + at);
      }
   }

   public void off(LocalTime at) {
      if (on) {
         on = false;
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
            on(now);
         } else {
            off(now);
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
