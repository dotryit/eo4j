package ch.feol.eo4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SimulationTimerService implements TimerService {

	private LocalDateTime localDateTime;

	public SimulationTimerService() {
		// Start at midnight
		this.localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
	}

	@Override
	public LocalDateTime getActualTimestamp() {
		return localDateTime;
	}

	@Override
	public LocalTime getActualTime() {
		return LocalTime.from(localDateTime);
	}

	@Override
	public void sleepSeconds(int seconds) {
		localDateTime = localDateTime.plusSeconds(seconds);
	}

	@Override
	public void sleepMinutes(int minutes) {
		localDateTime = localDateTime.plusMinutes(minutes);
	}

	@Override
	public void sleepHours(int hours) {
		localDateTime = localDateTime.plusHours(hours);
	}
}
