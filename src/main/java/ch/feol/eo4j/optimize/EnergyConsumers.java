package ch.feol.eo4j.optimize;

public interface EnergyConsumers {

	public double getActualConsumptionPowerKiloW();

	public void update(ManagedBoilers managedBoilers);

}
