package ch.feol.eo4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ManagedBoiler {

	private static final double UPPER_TRESHOLD_FACTOR = 1.2;

	private static final double LOWER_TRESHOLD_FACTOR = 0.3;

	private Boiler boiler;

	private TimerService timerService = null;

	private List<Period> activePeriods = new ArrayList<>();

	private Period lastOnPeriod = null;

	private BoilerState state = BoilerState.OFF;

	public ManagedBoiler(Boiler boiler, TimerService timerService) {
		this.boiler = boiler;
		this.timerService = timerService;
	}

	public void turnOn() {
		if (state == BoilerState.ON) {
			return;
		} else {
			state = BoilerState.ON;
			// Create new period
			lastOnPeriod = new Period(timerService.getActualTimestamp());
			activePeriods.add(lastOnPeriod);
		}
	}

	public void turnOff() {
		if (state == BoilerState.OFF) {
			return;
		} else {
			state = BoilerState.OFF;
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
		for (Period onPeriod : activePeriods) {
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
		if (state == BoilerState.ON) {
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

	public boolean isTurnedOn() {
		return state == BoilerState.ON;
	}

	public BoilerState getState() {
		return state;
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
}
