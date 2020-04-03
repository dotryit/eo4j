package ch.feol.eo4j;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Boiler {

	private static final long TEMPERATURE_DIFFERENCE = 50;

	private static final double THERMAL_CAPACITY_KILOJOULE_PER_LITER_KELVIN = 4.2;

	private String id = null;

	private double nominalPowerKiloW;

	private Duration fullHeatingDuration;

	private double volumeLiter;

	public Boiler(String id, double volumeLiter, double nominalPowerKiloW) {
		this.id = id;
		this.volumeLiter = volumeLiter;
		this.nominalPowerKiloW = nominalPowerKiloW;

		double fullEnergyKiloJ = TEMPERATURE_DIFFERENCE * volumeLiter * THERMAL_CAPACITY_KILOJOULE_PER_LITER_KELVIN;
		double fullHeatingDurationSeconds = fullEnergyKiloJ / nominalPowerKiloW;
		fullHeatingDuration = Duration.of(Math.round(fullHeatingDurationSeconds), ChronoUnit.SECONDS);
	}

	public Duration getFullHeatingDuration() {
		return fullHeatingDuration;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return id;
	}

	public double getNominalPowerKiloW() {
		return nominalPowerKiloW;
	}

	public double getVolumeLiter() {
		return volumeLiter;
	}
}
