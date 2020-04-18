package ch.feol.eo4j.optimize;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ManagedBoilers {

	List<ManagedBoiler> boilers = new ArrayList<>();

	private Duration onDurationOffset = null;

	private TimerService timerService;

	private int limitHours;

	/**
	 * @param onDurationOffset The offset to add to the least active boilers when
	 *                         determining if a boiler switch is to take place.
	 */
	public ManagedBoilers(Duration onDurationOffset, int limitHours, TimerService timerService) {
		this.onDurationOffset = onDurationOffset;
		this.limitHours = limitHours;
		this.timerService = timerService;
	}

	public void add(ManagedBoiler managedBoiler) {
		boilers.add(managedBoiler);
	}

	/**
	 * Get the list of boilers eligible to power on.
	 * <p>
	 * These are the boilers that are not already powered on an whose nominal power
	 * upper threshold is less than the surplus power available at the moment.
	 * 
	 * @param surplusPowerKiloW The surplus power available at the moment.
	 * @return the list of managed boilers eligible to power on.
	 */
	private List<ManagedBoiler> getEligibleBoilersToTurnOn(double surplusPowerKiloW) {
		List<ManagedBoiler> eligible = new ArrayList<>();
		for (ManagedBoiler boiler : boilers) {
			if (boiler.isEligibleToSwitchOn(surplusPowerKiloW)) {
				eligible.add(boiler);
			}
		}
		return eligible;
	}

	/**
	 * Get the list of ON boilers.
	 * 
	 * @return the list of ON boilers.
	 */
	private List<ManagedBoiler> getTurnedOnBoilers() {
		List<ManagedBoiler> onList = new ArrayList<>();
		for (ManagedBoiler boiler : boilers) {
			if (boiler.isTurnedOn()) {
				onList.add(boiler);
			}
		}
		return onList;
	}

	/**
	 * Get the list of OFF boilers.
	 * 
	 * @return the list of OFF boilers.
	 */
	private List<ManagedBoiler> getTurnedOffBoilers() {
		List<ManagedBoiler> offList = new ArrayList<>();
		for (ManagedBoiler boiler : boilers) {
			if (!boiler.isTurnedOn()) {
				offList.add(boiler);
			}
		}
		return offList;
	}

	/**
	 * Turn on the most eligible boiler.
	 * 
	 * @param surplusPowerKiloW The actual surplus power.
	 * @return the boiler turned on or an empty optional.
	 * 
	 */
	public Optional<ManagedBoiler> turnOn(double surplusPowerKiloW) {

		Period limit = getLimit();

		ManagedBoiler leastActive = null;
		for (ManagedBoiler boiler : getEligibleBoilersToTurnOn(surplusPowerKiloW)) {
			if (leastActive == null) {
				leastActive = boiler;
			} else {
				if (boiler.getTotalActive(limit).compareTo(leastActive.getTotalActive(limit)) < 1) {
					leastActive = boiler;
				}
			}
		}
		if (leastActive != null) {
			leastActive.turnOn();
			return Optional.of(leastActive);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Calculate the actual limit period.
	 * <p>
	 * The limit period is the period in the past, which is used to measure the
	 * activity of the boiler, e.g. the duration during which it was turned on.
	 * 
	 * @return
	 */
	private Period getLimit() {

		// Calculate the actual limit period
		LocalDateTime actualTimestamp = timerService.getActualTimestamp();
		return new Period(actualTimestamp.minusHours(limitHours), actualTimestamp);
	}

	/**
	 * Turn the most active and ON boiler OFF if at least one boilers lower
	 * threshold power is bigger than the surplus power.
	 * 
	 * @param surplusPowerKiloW The actual surplus power.
	 * @param limit             The limit to determine the total on duration.
	 * @return the boiler turned off or an empty optional.
	 */
	public Optional<ManagedBoiler> turnOff(double surplusPowerKiloW) {

		Period limit = getLimit();

		boolean turnOff = false;
		ManagedBoiler mostActive = null;
		for (ManagedBoiler boiler : getTurnedOnBoilers()) {
			if (surplusPowerKiloW < boiler.getNominalPowerLowerThresholdKiloW()) {
				turnOff = true;
			}
			if (mostActive == null) {
				mostActive = boiler;
			} else {
				if (boiler.getTotalActive(limit).compareTo(mostActive.getTotalActive(limit)) > 0) {
					mostActive = boiler;
				}
			}
		}
		if (turnOff && mostActive != null) {
			mostActive.turnOff();
			return Optional.of(mostActive);
		} else {
			return Optional.empty();
		}
	}

	public void switchBoiler() {

		Period limit = getLimit();

		// No neet to turn active boiler OFF if there are no OFF boilers
		if (getTurnedOffBoilers().size() == 0) {
			return;
		}

		// Determine boiler which was the least active
		ManagedBoiler leastActive = null;
		for (ManagedBoiler boiler : getTurnedOffBoilers()) {
			if (leastActive == null) {
				leastActive = boiler;
			} else {
				if (leastActive.getTotalActive(limit).compareTo(boiler.getTotalActive(limit)) > 0) {
					leastActive = boiler;
				}
			}
		}
		if (leastActive == null) {
			throw new IllegalStateException("Least active boiler can not be null");
		}
		// The ON duration of the least active boiler
		Duration leastOnDuration = leastActive.getTotalActive(limit);

		// Determine boiler to turn OFF. This is the case if one of the turned ON
		// boilers exceeds the ON duration of the least active boiler plus a offset.
		Duration turnOffDuration = leastOnDuration.plus(onDurationOffset);
		ManagedBoiler turnOffCandidate = null;
		for (ManagedBoiler boiler : getTurnedOnBoilers()) {
			Duration boilerOnDuration = boiler.getTotalActive(limit);
			// Has this boiler reached the maximal active duration
			if (boilerOnDuration.compareTo(turnOffDuration) > 0) {
				if (turnOffCandidate == null) {
					turnOffCandidate = boiler;
				} else {
					// Is this boiler even longer active than the previous one
					if (boilerOnDuration.compareTo(turnOffCandidate.getTotalActive(limit)) > 0) {
						turnOffCandidate = boiler;
					}
				}
			}
		}
		if (turnOffCandidate != null) {
			turnOffCandidate.turnOff();
			leastActive.turnOn();
		}
	}

	public double getActualConsumptionKiloW() {
		double actualConsumptionKiloW = 0;
		for (ManagedBoiler boiler : boilers) {
			if (boiler.isTurnedOn()) {
				actualConsumptionKiloW += boiler.getBoiler().getNominalPowerKiloW();
			}
		}
		return actualConsumptionKiloW;
	}

	/**
	 * Get the list of managed boilers sorted by id.
	 * 
	 * @return the sorted boiler list.
	 */
	public List<ManagedBoiler> getBoilerList() {
		Comparator<? super ManagedBoiler> comparator = new Comparator<ManagedBoiler>() {
			@Override
			public int compare(ManagedBoiler o1, ManagedBoiler o2) {
				return o1.getBoiler().getId().compareTo(o2.getBoiler().getId());
			}
		};
		boilers.sort(comparator);
		return Collections.unmodifiableList(boilers);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Boilers(ON=");
		boolean first = true;
		for (ManagedBoiler boiler : boilers) {
			if (boiler.isTurnedOn()) {
				if (!first) {
					builder.append(",");
				} else {
					first = false;
				}
				builder.append(boiler.getBoiler());
			}
		}
		builder.append(")");
		return builder.toString();
	}

	public Optional<ManagedBoiler> turnNextOn() {
		for (ManagedBoiler boiler : boilers) {
			if (!boiler.isTurnedOn()) {
				boiler.turnOn();
				return Optional.of(boiler);
			}
		}
		return Optional.empty();
	}

	public void turnAllOn() {
		for (ManagedBoiler boiler : boilers) {
			boiler.turnOn();
		}
	}

	public void turnAllOff() {
		for (ManagedBoiler boiler : boilers) {
			boiler.turnOff();
		}
	}
}
