package ch.feol.eo4j;

import java.time.LocalTime;

public interface Boiler {

   LocalTime endAtNight();

   LocalTime startAtNight();

   void off(LocalTime at);

   void on(LocalTime at);

   void manage(LocalTime now);

}
