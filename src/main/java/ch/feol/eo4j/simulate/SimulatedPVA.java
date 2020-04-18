package ch.feol.eo4j.simulate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import ch.feol.eo4j.optimize.EnergyProducers;
import ch.feol.eo4j.optimize.TimerService;

public class SimulatedPVA implements EnergyProducers {

	private TimerService timerService;

	public SimulatedPVA(TimerService timerService) {
		this.timerService = timerService;
	}

	@Override
	public double getActualProductionPowerKiloW() {

		LocalTime acutalTime = LocalTime.from(timerService.getActualTimestamp());

		// Simulate PV power outage between 13:30 and 15:00
		if (acutalTime.isAfter(LocalTime.of(13, 30)) && acutalTime.isBefore(LocalTime.of(15, 0))) {
			return 0.0;
		}
		return getPower(timerService.getActualTimestamp(), 14);
	}

	public static double getPower(LocalDateTime time, int max) {
		int minuteOfDay = time.get(ChronoField.MINUTE_OF_DAY);
		int midDayMinute = 12 * 60;
		double offset = max * 0.3;
		double power = (max + offset) * Math.exp(-1 * Math.pow((minuteOfDay - midDayMinute) / 300.0, 2)) - offset;
		if (power > 0) {
			return power;
		} else {
			return 0.0;
		}
	}
}
