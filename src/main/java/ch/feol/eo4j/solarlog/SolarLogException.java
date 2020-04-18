package ch.feol.eo4j.solarlog;

public class SolarLogException extends Exception {

   public SolarLogException(String message) {
      super(message);
   }

   public SolarLogException(String message, Exception cause) {
      super(message, cause);
   }
}
