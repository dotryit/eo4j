package ch.feol.eo4j.optimize;

import java.time.LocalTime;

public class ReportingSystem {

	private ManagedBoilers managedBoilers = null;

	private EnergyProducers energyProducers = null;

	private TimerService timerService = null;

	private EnergyConsumers energyConsumers = null;

	public ReportingSystem(EnergyProducers energyProducers, EnergyConsumers energyConsumers,
			ManagedBoilers managedBoilers, TimerService timerService) {
		this.energyProducers = energyProducers;
		this.energyConsumers = energyConsumers;
		this.managedBoilers = managedBoilers;
		this.timerService = timerService;
	}

	public String getStatsData() {

		double production = energyProducers.getActualProductionPowerKiloW();
		double consumption = energyConsumers.getActualConsumptionPowerKiloW();
		double surplus = production - consumption;

		StringBuilder builder = new StringBuilder();
		builder.append(LocalTime.from(timerService.getActualTimestamp()));
		builder.append(",");
		builder.append(String.format("%f", production));
		builder.append(",");
		builder.append(String.format("%f", consumption));
		builder.append(",");
		builder.append(String.format("%f", surplus));
		int offset = 0;
		for (ManagedBoiler boiler : managedBoilers.getBoilerList()) {
			builder.append(",");
			if (boiler.isTurnedOn()) {
				builder.append(String.format("%d", offset - 1));
			} else {
				builder.append(String.format("%d", offset));
			}
			offset--;
		}
		return builder.toString();
	}

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
}
