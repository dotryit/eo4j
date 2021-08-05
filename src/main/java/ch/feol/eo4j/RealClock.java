package ch.feol.eo4j;

import java.time.Duration;
import java.time.LocalTime;

public class RealClock implements Clock {

   private Duration interval;

   public RealClock(Duration interval) {
      if (interval.getSeconds() < 1) {
         throw new IllegalArgumentException("Minimal interval duration must be more than 1 second");
      }
      this.interval = interval;
   }

   @Override
   public LocalTime now() {
      return LocalTime.now();
   }

   @Override
   public void next() {
      sleep(interval);
   }

   private void sleep(Duration interval) {
      long seconds = interval.getSeconds();
      if (seconds <= 1) {
         seconds = 1;
      }
      try {
         Thread.sleep(seconds * 1000);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

   @Override
   public boolean isExpired() {
      // Real clock never expires.
      return false;
   }
}
