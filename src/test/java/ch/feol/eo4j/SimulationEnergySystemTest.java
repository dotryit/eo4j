package ch.feol.eo4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimulationEnergySystemTest {

	@Test
	void test_getPower() {

		// Prepare
		SimulationTimerService timerService = new SimulationTimerService();

		// Act & assert
		assertEquals(0.0, SimulationEnergySystem.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(6);
		assertEquals(2.55, SimulationEnergySystem.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(6.91, SimulationEnergySystem.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(11.78, SimulationEnergySystem.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(14.0, SimulationEnergySystem.getPower(timerService.getActualTimestamp(), 14), 0.01);
	}
}
