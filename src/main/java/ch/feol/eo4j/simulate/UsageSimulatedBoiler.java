package ch.feol.eo4j.simulate;

import java.time.LocalDateTime;

import ch.feol.eo4j.optimize.Boiler;
import ch.feol.eo4j.optimize.TimerService;
import ch.feol.eo4j.optimize.UpdateResult;

public class UsageSimulatedBoiler implements SimulatedBoiler {

	private static final double TEMPERATURE_DIFFERENCE = 65 - 15;

	private static final double TEMPERATURE_HYSTERESIS = 1;

	private LocalDateTime timestamp = null;

	// The temperature offset from the temperature of the water input
	private double offset = Double.MAX_VALUE;

	private double totalCapacity = Double.MAX_VALUE;

	private Boiler boiler = null;

	private UsageSimulator usageSimulator;

	private boolean power = false;

	private boolean heating = false;

	private TimerService timerService = null;

	public UsageSimulatedBoiler(UsageSimulator usageSimulator, Boiler boiler, TimerService timerService) {
		this.usageSimulator = usageSimulator;
		this.timestamp = timerService.getActualTimestamp();
		this.boiler = boiler;
		this.timerService = timerService;

		offset = TEMPERATURE_DIFFERENCE;
		totalCapacity = boiler.getVolumeLiter() * Boiler.THERMAL_CAPACITY_KILOJOULE_PER_LITER_KELVIN;
	}

	/**
	 * Simulate water usage and heating.
	 * 
	 * @param until The time stamp until the simulation ends.
	 * @return the water and the energy usage during the update.
	 */
	private UpdateResult update() {
		LocalDateTime until = timerService.getActualTimestamp();
		if (timestamp.equals(until)) {
			return new UpdateResult(0, 0);
		}
		if (timestamp.isAfter(until)) {
			throw new IllegalArgumentException("From timestamp is after to timestamp");
		}

		double waterUsage = 0;
		double heatUsage = 0.0;
		do {
			double minuteUsage = usageSimulator.getMinuteUsage(timestamp);
			double oldCapacity = offset * totalCapacity;
			double usedCapacity = minuteUsage * offset * Boiler.THERMAL_CAPACITY_KILOJOULE_PER_LITER_KELVIN;

			// Hysteresis
			if (heating) {
				if (offset >= TEMPERATURE_DIFFERENCE) {
					heating = false;
				}
			} else {
				if (offset < TEMPERATURE_DIFFERENCE - TEMPERATURE_HYSTERESIS) {
					heating = true;
				}
			}

			double heatCapacity = 0.0;
			if (power && heating) {
				heatCapacity = boiler.getNominalPowerKiloW() * 60;
			}

			offset = (oldCapacity - usedCapacity + heatCapacity) / (totalCapacity);
			waterUsage += minuteUsage;

			heatUsage += heatCapacity;

			timestamp = timestamp.plusMinutes(1);
		} while (timestamp.isBefore(until));
		return new UpdateResult(waterUsage, heatUsage);
	}

	public double getOffset() {
		return offset;
	}

	public UpdateResult update(boolean power) {
		UpdateResult result = update();
		this.power = power;
		return result;
	}

	public double getActualConsumptionKiloW() {
		update();
		if (power && heating) {
			return boiler.getNominalPowerKiloW();
		} else {
			return 0;
		}
	}

	public boolean isHeating() {
		return heating;
	}

	public boolean isPower() {
		return power;
	}

	public void setPower(boolean power) {
		this.power = power;
	}
}
