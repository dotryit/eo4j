package ch.feol.eo4j;

public class BoilerFactory {

   public static Boiler[] instances() {
      return SimulatedBoiler.values();
   }
}
