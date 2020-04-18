package ch.feol.eo4j.simulate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ch.feol.eo4j.optimize.TimerService;

public class SimulationTimerService implements TimerService {

	private LocalDateTime localDateTime;

	public SimulationTimerService() {
		// Start at midnight
		this.localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
	}

	public SimulationTimerService(LocalDateTime start) {
		this.localDateTime = start;
	}

	@Override
	public LocalDateTime getActualTimestamp() {
		return localDateTime;
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
