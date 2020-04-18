package ch.feol.eo4j.optimize;

import java.time.LocalDateTime;

public class SystemTimerService implements TimerService {

	@Override
	public LocalDateTime getActualTimestamp() {
		return LocalDateTime.now();
	}

	@Override
	public void sleepMinutes(int minutes) {
		try {
			Thread.sleep(1000 * 60);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void sleepHours(int hours) {
		sleepMinutes(60);
	}
}
