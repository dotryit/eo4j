package ch.feol.eo4j;

import java.time.LocalTime;

public interface EnergySystem {

	public double getActualProductionPowerKiloW();

	public double getActualConsumptionPowerKiloW();

	public double getActualSurplusPowerKiloW();

	public LocalTime getActualTime();

	public String getStatsData();

	public String getStatsHeader();

}
