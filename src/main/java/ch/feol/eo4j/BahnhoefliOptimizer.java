package ch.feol.eo4j;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BahnhoefliOptimizer {

   private static final Logger LOG = LoggerFactory.getLogger(BahnhoefliOptimizer.class);

   public static void main(String[] args) {

      Clock clock = new SimulatedClock(LocalTime.of(12, 00), Duration.of(5, ChronoUnit.MINUTES), 24 * 24);
      try {
         new DayNightController().run(clock);
      } catch (Throwable t) {
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            Thread.interrupted();
         }
         LOG.error("BahnhoefliOptimizer failed", t);
         System.exit(-1);
      }
   }
}
