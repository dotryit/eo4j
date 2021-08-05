package ch.feol.eo4j;

import java.time.LocalTime;

public class DayNightController implements Controller {

   public void run(Clock clock) {

      // Indicate start on board
      // RealBoiler.allFlipper();

      while (!clock.isExpired()) {
         LocalTime now = clock.now();
         for (Boiler boiler : BoilerFactory.instances()) {
            boiler.manage(now);
         }
         clock.next();
      }
   }
}
