package ch.feol.eo4j;

import java.time.Duration;

import ch.feol.eo4j.optimize.Boiler;
import ch.feol.eo4j.optimize.CountRunController;
import ch.feol.eo4j.optimize.EnergyConsumers;
import ch.feol.eo4j.optimize.EnergyProducers;
import ch.feol.eo4j.optimize.ManagedBoiler;
import ch.feol.eo4j.optimize.ManagedBoilers;
import ch.feol.eo4j.optimize.OwnUsageOptimizationRunner;
import ch.feol.eo4j.optimize.ReportingSystem;
import ch.feol.eo4j.optimize.RunController;
import ch.feol.eo4j.optimize.TimerService;
import ch.feol.eo4j.simulate.SimulatedBoilers;
import ch.feol.eo4j.simulate.SimulatedPVA;
import ch.feol.eo4j.simulate.SimulationTimerService;
import ch.feol.eo4j.simulate.UsageSimulatedBoilerFactory;

public class OneDaySimulation {

	public static void main(String[] args) {

		TimerService timerService = new SimulationTimerService();

		ManagedBoilers boilers = new ManagedBoilers(Duration.ofMinutes(30), 4, timerService);
		boilers.add(new ManagedBoiler(new Boiler("1L", 120, 2.4), timerService, 2, 60));
		boilers.add(new ManagedBoiler(new Boiler("1R", 120, 2.4), timerService, 1, 50));
		boilers.add(new ManagedBoiler(new Boiler("2L", 120, 2.4), timerService, 1, 40));
		boilers.add(new ManagedBoiler(new Boiler("2R", 120, 2.4), timerService, 1, 60));
		boilers.add(new ManagedBoiler(new Boiler("3L", 120, 2.4), timerService, 2, 60));
		boilers.add(new ManagedBoiler(new Boiler("3R", 120, 2.4), timerService, 2, 50));
		boilers.turnAllOn();

		UsageSimulatedBoilerFactory factory = new UsageSimulatedBoilerFactory();
		EnergyConsumers simulatedBoilers = new SimulatedBoilers(boilers, timerService, factory);
		EnergyProducers producers = new SimulatedPVA(timerService);
		ReportingSystem reportingSystem = new ReportingSystem(producers, simulatedBoilers, boilers, timerService);

		RunController runController = new CountRunController(288 * 5);

		new OwnUsageOptimizationRunner(timerService, producers, boilers, simulatedBoilers, reportingSystem, runController).run();
	}

}
