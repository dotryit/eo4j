package ch.feol.eo4j.optimize;

public class UpdateResult {

	private double waterUsage = Double.MAX_VALUE;

	private double energyUsage = Double.MAX_VALUE;

	public UpdateResult(double waterUsage, double energyUsage) {
		this.waterUsage = waterUsage;
		this.energyUsage = energyUsage;
	}

	public double getWaterUsage() {
		return waterUsage;
	}

	public double getEnergyUsage() {
		return energyUsage;
	}

}
