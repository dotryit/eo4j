package ch.feol.eo4j;

import java.time.Duration;
import java.time.LocalTime;

public class SimulatedClock implements Clock {

   private LocalTime now;

   private Duration interval;

   private long count;

   private long calls = 0;

   public SimulatedClock(LocalTime start, Duration interval, long count) {
      this.now = start;
      this.interval = interval;
      this.count = count;
   }

   @Override
   public LocalTime now() {
      return now;
   }

   @Override
   public void next() {
      now = now.plus(interval);
      calls++;
   }

   @Override
   public boolean isExpired() {
      return calls >= count;
   }
}
