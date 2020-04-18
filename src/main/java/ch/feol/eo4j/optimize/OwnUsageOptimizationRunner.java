package ch.feol.eo4j.optimize;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class OwnUsageOptimizationRunner {

	private static final int MINUTES_INCREMENT = 1;

	private EnergyProducers producers = null;

	private ManagedBoilers managedBoilers;

	private EnergyConsumers energyConsumers;

	private TimerService timerService;

	private ReportingSystem reportingSystem;

	private RunController runController;

	public OwnUsageOptimizationRunner(TimerService timerService, EnergyProducers producers, ManagedBoilers managedBoilers,
			EnergyConsumers energyConsumers, ReportingSystem reportingSystem, RunController runController) {
		this.timerService = timerService;
		this.producers = producers;
		this.managedBoilers = managedBoilers;
		this.energyConsumers = energyConsumers;
		this.reportingSystem = reportingSystem;
		this.runController = runController;
	}

	public void run() {

		System.out.println(reportingSystem.getStatsHeader());

		while (true) {
			LocalDateTime actualTimestamp = timerService.getActualTimestamp();
			LocalTime actualTime = LocalTime.from(actualTimestamp);

			double consumptionPowerKiloW = energyConsumers.getActualConsumptionPowerKiloW();

			if (actualTime.isBefore(LocalTime.of(4, 0)) || actualTime.isAfter(LocalTime.of(22, 0))) {
				managedBoilers.turnNextOn();
			} else {

				double productionPowerKiloW = producers.getActualProductionPowerKiloW();
				double surplusPowerKiloW = productionPowerKiloW - consumptionPowerKiloW;

				// Check if a boiler can be turned ON
				Optional<ManagedBoiler> turnedOn = managedBoilers.turnOn(surplusPowerKiloW);
				if (!turnedOn.isPresent()) {
					// Check if a boiler must be turned OFF
					managedBoilers.turnOff(surplusPowerKiloW);
				}

				// Check if a boiler must be switched
				managedBoilers.switchBoiler();
			}

			energyConsumers.update(managedBoilers);

			System.out.println(reportingSystem.getStatsData());

			if (runController.isBreak(timerService.getActualTimestamp())) {
				break;
			}

			timerService.sleepMinutes(MINUTES_INCREMENT);
		}
	}
}
