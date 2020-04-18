package ch.feol.eo4j.simulate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimulatedPVATest {

	@Test
	void test_getPower() {

		// Prepare
		SimulationTimerService timerService = new SimulationTimerService();

		// Act & assert
		assertEquals(0.0, SimulatedPVA.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(6);
		assertEquals(0.11, SimulatedPVA.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(5.39, SimulatedPVA.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(11.31, SimulatedPVA.getPower(timerService.getActualTimestamp(), 14), 0.01);
		timerService.sleepHours(2);
		assertEquals(14.0, SimulatedPVA.getPower(timerService.getActualTimestamp(), 14), 0.01);
	}
}
