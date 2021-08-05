package ch.feol.eo4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class SimulatedClockTest {

   @Test
   public void test_midnight() {

      // Prepare
      SimulatedClock testee = new SimulatedClock(LocalTime.of(14, 10), Duration.of(1, ChronoUnit.MINUTES), 24 * 60);

      // Act & Assert
      for (int count = 0; count < 10 * 60; count++) {
         testee.next();
      }
      assertEquals(LocalTime.of(0, 10), testee.now());
      assertFalse(testee.isExpired());
      
      int count = 0;
      while (!testee.isExpired()) {
         testee.next();
         count++;
      }
      assertEquals(14 * 60, count);
   }
   
   @Test
   public void test() {

      // Prepare
      SimulatedClock testee = new SimulatedClock(LocalTime.of(10, 23), Duration.of(5, ChronoUnit.MINUTES), 3);

      // Act & Assert
      assertEquals(LocalTime.of(10, 23), testee.now());
      testee.next();
      assertEquals(LocalTime.of(10, 28), testee.now());
      testee.next();
      assertEquals(LocalTime.of(10, 33), testee.now());
      testee.next();
      assertTrue(testee.isExpired());
   }
}
