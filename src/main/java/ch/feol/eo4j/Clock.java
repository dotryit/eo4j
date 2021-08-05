package ch.feol.eo4j;

import java.time.LocalTime;

public interface Clock {

   /**
    * @return the actual local time.
    */
   LocalTime now();

   /**
    * Wait for the next interval to expire.
    */
   void next();

   /**
    * @return true if this clock is expired and the simulation should be stopped.
    */
   boolean isExpired();

}
