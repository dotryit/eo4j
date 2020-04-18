package ch.feol.eo4j.simulate;

import ch.feol.eo4j.optimize.ManagedBoiler;
import ch.feol.eo4j.optimize.TimerService;

public class OnOffSimulatedBoilerFactory implements SimulatedBoilerFactory {

	@Override
	public SimulatedBoiler getSimulatedBoiler(ManagedBoiler managedBoiler, TimerService timerService) {
		return new OnOffSimulatedBoiler(managedBoiler.getBoiler());
	}
}
