package ch.feol.eo4j.simulate;

import ch.feol.eo4j.optimize.UpdateResult;

public interface SimulatedBoiler {

	UpdateResult update(boolean power);

	double getActualConsumptionKiloW();

}
