package ch.feol.eo4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

public class SimulationEnergySystem implements EnergySystem {

	private TimerService timerService;

	private ManagedBoilers managedBoilers;

	public SimulationEnergySystem(TimerService timerService, ManagedBoilers managedBoilers) {
		this.timerService = timerService;
		this.managedBoilers = managedBoilers;
	}

	public double getActualSurplusPowerKiloW() {
		return getActualProductionPowerKiloW() - getActualConsumptionPowerKiloW();
	}

	@Override
	public double getActualConsumptionPowerKiloW() {
		return managedBoilers.getActualConsumptionKiloW();
	}

	@Override
	public double getActualProductionPowerKiloW() {
		// Simulate PV power outage between 13:30 and 15:00
		if (timerService.getActualTime().isAfter(LocalTime.of(13, 30))
				&& timerService.getActualTime().isBefore(LocalTime.of(15, 0))) {
			return 0.0;
		}
		return getPower(timerService.getActualTimestamp(), 14);
	}

	public LocalTime getActualTime() {
		return LocalTime.from(timerService.getActualTimestamp());
	}

	@Override
	public String getStatsData() {
		StringBuilder builder = new StringBuilder();
		builder.append(LocalTime.from(timerService.getActualTimestamp()));
		builder.append(",");
		builder.append(getActualProductionPowerKiloW());
		builder.append(",");
		builder.append(getActualConsumptionPowerKiloW());
		builder.append(",");
		builder.append(getActualSurplusPowerKiloW());
		int offset = 0;
		for (ManagedBoiler boiler : managedBoilers.getBoilerList()) {
			builder.append(",");
			if (boiler.isTurnedOn()) {
				builder.append(offset - 1);
			} else {
				builder.append(offset);
			}
			offset--;
		}
		return builder.toString();
	}

	@Override
	public String getStatsHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("Zeit");
		builder.append(",");
		builder.append("Produktion PV");
		builder.append(",");
		builder.append("Verbrauch");
		builder.append(",");
		builder.append("Rückspeisung / Fremdbezug");
		for (ManagedBoiler boiler : managedBoilers.getBoilerList()) {
			builder.append(",");
			builder.append("Boiler " + boiler.getBoiler().getId() + " OFF/ON");
		}
		return builder.toString();
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
