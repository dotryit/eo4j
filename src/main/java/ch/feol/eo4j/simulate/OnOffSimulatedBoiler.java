package ch.feol.eo4j.simulate;

import ch.feol.eo4j.optimize.Boiler;
import ch.feol.eo4j.optimize.UpdateResult;

public class OnOffSimulatedBoiler implements SimulatedBoiler {

	private Boiler boiler;

	private boolean power = false;

	public OnOffSimulatedBoiler(Boiler boiler) {
		this.boiler = boiler;
	}

	@Override
	public UpdateResult update(boolean power) {
		this.power = power;
		return new UpdateResult(0, 0);
	}

	@Override
	public double getActualConsumptionKiloW() {
		if (power) {
			return boiler.getNominalPowerKiloW();
		} else {
			return 0;
		}
	}
}
