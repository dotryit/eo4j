package ch.feol.eo4j.simulate;

import ch.feol.eo4j.optimize.ManagedBoiler;
import ch.feol.eo4j.optimize.TimerService;

public interface SimulatedBoilerFactory {

	public SimulatedBoiler getSimulatedBoiler(ManagedBoiler managedBoiler, TimerService timerService);

}
