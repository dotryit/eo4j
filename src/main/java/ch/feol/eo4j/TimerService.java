package ch.feol.eo4j;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimerService {

	public LocalDateTime getActualTimestamp();

	public LocalTime getActualTime();

	public void sleepSeconds(int seconds);

	public void sleepMinutes(int minutes);

	public void sleepHours(int hours);

}
