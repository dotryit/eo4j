package ch.feol.eo4j.simulate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ch.feol.eo4j.optimize.Boiler;
import ch.feol.eo4j.optimize.EnergyConsumers;
import ch.feol.eo4j.optimize.ManagedBoiler;
import ch.feol.eo4j.optimize.ManagedBoilers;
import ch.feol.eo4j.optimize.TimerService;

public class SimulatedBoilers implements EnergyConsumers {

	Map<Boiler, SimulatedBoiler> simulatedBoilerMap = new HashMap<>();

	public SimulatedBoilers(ManagedBoilers managedBoilers, TimerService timerService,
			SimulatedBoilerFactory simulatedBoilerFactory) {
		for (ManagedBoiler managed : managedBoilers.getBoilerList()) {
			SimulatedBoiler simulated = simulatedBoilerFactory.getSimulatedBoiler(managed, timerService);
			simulatedBoilerMap.put(managed.getBoiler(), simulated);
		}
	}

	private void update(ManagedBoiler managed) {
		Boiler boiler = managed.getBoiler();
		SimulatedBoiler simulated = simulatedBoilerMap.get(boiler);
		simulated.update(managed.isPower());
	}

	public double getActualConsumptionPowerKiloW() {
		double actualConsumption = 0;
		for (Entry<Boiler, SimulatedBoiler> entry : simulatedBoilerMap.entrySet()) {
			SimulatedBoiler simulated = entry.getValue();
			actualConsumption += simulated.getActualConsumptionKiloW();
		}
		return actualConsumption;
	}

	public void update(ManagedBoilers managedBoilers) {
		for (ManagedBoiler managed : managedBoilers.getBoilerList()) {
			update(managed);
		}
	}
}
