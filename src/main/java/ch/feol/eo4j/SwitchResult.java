package ch.feol.eo4j;

public class SwitchResult {

	private ManagedBoiler turnedOn;

	private ManagedBoiler turnedOff;

	public SwitchResult(ManagedBoiler turnedOn, ManagedBoiler turnedOff) {
		this.turnedOn = turnedOn;
		this.turnedOff = turnedOff;
	}

	public ManagedBoiler getTurnedOn() {
		return turnedOn;
	}

	public ManagedBoiler getTurnedOff() {
		return turnedOff;
	}

}
