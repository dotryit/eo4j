package ch.feol.eo4j.simulate;

import ch.feol.eo4j.optimize.ManagedBoiler;
import ch.feol.eo4j.optimize.TimerService;

public class UsageSimulatedBoilerFactory implements SimulatedBoilerFactory {

	@Override
	public SimulatedBoiler getSimulatedBoiler(ManagedBoiler managedBoiler, TimerService timerService) {
		WarmWaterUsageSimulator usageSimulator = new WarmWaterUsageSimulator(managedBoiler.getPersons(),
				managedBoiler.getDailyUsagePerPerson());
		return new UsageSimulatedBoiler(usageSimulator, managedBoiler.getBoiler(), timerService);
	}

}
