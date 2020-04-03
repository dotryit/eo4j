package ch.feol.eo4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class BoilerOptimizer {

	// TODO Make configurable
	private static final Duration MAX_ON_DURATION = Duration.ofMinutes(10);

	private static final int LIMIT_HOURS = 4;

	private static final int MINUTES_INCREMENT = 5;

	private EnergySystem energySystem = null;

	private ManagedBoilers boilers;

	private TimerService timerService;

	public BoilerOptimizer(TimerService timerService, EnergySystem energySystem, ManagedBoilers boilers) {
		this.timerService = timerService;
		this.energySystem = energySystem;
		this.boilers = boilers;
	}

	private void optimize(int count) {

		System.out.println(energySystem.getStatsHeader());
		
		for (int i = 0; i < count; i++) {

			LocalDateTime actualTimestamp = timerService.getActualTimestamp();
			LocalTime actualTime = LocalTime.from(actualTimestamp);

			if (actualTime.isBefore(LocalTime.of(4, 0)) || actualTime.isAfter(LocalTime.of(22, 0))) {
				boilers.turnNextOn();
			} else {

				double surplusPowerKiloW = energySystem.getActualSurplusPowerKiloW();

				// Calculate the actual limit period
				Period limit = new Period(actualTimestamp.minusHours(LIMIT_HOURS), actualTimestamp);

				// Check if a boiler can be turned ON
				Optional<ManagedBoiler> turnedOn = boilers.turnOn(surplusPowerKiloW, limit);
				if (!turnedOn.isPresent()) {
					// Check if a boiler must be turned OFF
					boilers.turnOff(surplusPowerKiloW, limit);
				}

				// Check if a boiler must be switched
				boilers.switchBoiler(limit);
			}

			System.out.println(energySystem.getStatsData());

			timerService.sleepSeconds(60 * MINUTES_INCREMENT);
		}
	}

	public static void main(String[] args) {

		TimerService timerService = new SimulationTimerService();

		ManagedBoilers boilers = new ManagedBoilers(MAX_ON_DURATION);
		boilers.add(new ManagedBoiler(new Boiler("1L", 120, 2.4), timerService));
		boilers.add(new ManagedBoiler(new Boiler("1R", 120, 2.4), timerService));
		boilers.add(new ManagedBoiler(new Boiler("2L", 120, 2.4), timerService));
		boilers.add(new ManagedBoiler(new Boiler("2R", 120, 2.4), timerService));
		boilers.add(new ManagedBoiler(new Boiler("3L", 120, 2.4), timerService));
		boilers.add(new ManagedBoiler(new Boiler("3R", 120, 2.4), timerService));
		boilers.turnAllOn();

		EnergySystem energySystem = new SimulationEnergySystem(timerService, boilers);
		new BoilerOptimizer(timerService, energySystem, boilers).optimize(288);
	}
}
