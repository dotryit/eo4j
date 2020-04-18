package ch.feol.eo4j.optimize;

import java.time.LocalDateTime;

public interface TimerService {

	public LocalDateTime getActualTimestamp();

	public void sleepMinutes(int minutes);

	public void sleepHours(int hours);

}
