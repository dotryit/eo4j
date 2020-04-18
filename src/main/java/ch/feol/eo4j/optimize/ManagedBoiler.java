package ch.feol.eo4j.optimize;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ManagedBoiler {

	private static final int ON_PERIOD_KEEP_DAYS = 5;

	private static final double UPPER_TRESHOLD_FACTOR = 1.3;

	private static final double LOWER_TRESHOLD_FACTOR = 0.1;

	private Boiler boiler;

	private TimerService timerService = null;

	private List<Period> onPeriods = new ArrayList<>();

	private Period lastOnPeriod = null;

	private boolean power = false;

	private int persons;

	private int dailyUsagePerPerson;

	public ManagedBoiler(Boiler boiler, TimerService timerService, int persons, int dailyUsagePerPerson) {
		this.boiler = boiler;
		this.timerService = timerService;
		this.persons = persons;
		this.dailyUsagePerPerson = dailyUsagePerPerson;
	}

	public void turnOn() {
		if (power) {
			return;
		} else {
			power = true;
			// Create new period
			lastOnPeriod = new Period(timerService.getActualTimestamp());
			onPeriods.add(lastOnPeriod);
			cleanPeriods();
		}
	}

	private void cleanPeriods() {
		List<Period> newList = new ArrayList<>();
		for (Period onPeriod : onPeriods) {
			if (onPeriod.isOpen()) {
				newList.add(onPeriod);
			} else {
				long ageInDays = onPeriod.getEndAt().get().until(timerService.getActualTimestamp(), ChronoUnit.DAYS);
				if (ageInDays < ON_PERIOD_KEEP_DAYS) {
					newList.add(onPeriod);
				}
			}
		}
		onPeriods = newList;
	}

	public void turnOff() {
		if (!power) {
			return;
		} else {
			power = false;
			lastOnPeriod.end(timerService.getActualTimestamp());
		}
	}

	public Boiler getBoiler() {
		return boiler;
	}

	public Duration getTotalActive() {
		return getTotalActive(null);
	}

	public Duration getTotalActive(Period limit) {
		Duration duration = Duration.ZERO;
		for (Period onPeriod : onPeriods) {
			duration = duration.plus(onPeriod.getDuration(limit, timerService.getActualTimestamp()));
		}
		return duration;
	}

	/**
	 * Determine if boiler is eligible to turn on.
	 * <p>
	 * If the boilers is OFF and it's nominal power plus the threshold are less than
	 * the surplus power this boiler is eligible to power on.
	 * 
	 * @param surplusPowerKiloW The actual surplus power.
	 * @return true if this boiler is eligible.
	 */
	public boolean isEligibleToSwitchOn(double surplusPowerKiloW) {
		if (power) {
			return false;
		}
		return getNominalPowerUpperThresholdKiloW() < surplusPowerKiloW;
	}

	public double getNominalPowerUpperThresholdKiloW() {
		return boiler.getNominalPowerKiloW() * UPPER_TRESHOLD_FACTOR;
	}

	public double getNominalPowerLowerThresholdKiloW() {
		return boiler.getNominalPowerKiloW() * LOWER_TRESHOLD_FACTOR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boiler == null) ? 0 : boiler.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagedBoiler other = (ManagedBoiler) obj;
		if (boiler == null) {
			if (other.boiler != null)
				return false;
		} else if (!boiler.equals(other.boiler))
			return false;
		return true;
	}

	public int getPersons() {
		return persons;
	}

	public int getDailyUsagePerPerson() {
		return dailyUsagePerPerson;
	}

	public boolean isTurnedOn() {
		return power;
	}

	public boolean isPower() {
		return power;
	}
}
