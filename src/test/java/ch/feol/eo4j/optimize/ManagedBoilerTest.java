package ch.feol.eo4j.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import ch.feol.eo4j.simulate.SimulationTimerService;

class ManagedBoilerTest {

	@Test
	void test_open() {

		SimulationTimerService timerService = new SimulationTimerService();
		Boiler boiler = new Boiler("test", 500, 10);

		ManagedBoiler testee = new ManagedBoiler(boiler, timerService, 1, 40);

		// Act
		testee.turnOn();
		timerService.sleepMinutes(12);

		// Assert
		Duration duration = testee.getTotalActive();
		assertEquals(Duration.of(12, ChronoUnit.MINUTES), duration);
	}

	@Test
	void test() {

		SimulationTimerService timerService = new SimulationTimerService();
		Boiler boiler = new Boiler("test", 500, 10);

		ManagedBoiler testee = new ManagedBoiler(boiler, timerService, 1, 40);

		// Act
		testee.turnOn();
		timerService.sleepMinutes(17);
		testee.turnOff();
		timerService.sleepMinutes(4);
		testee.turnOn();
		timerService.sleepMinutes(3);
		testee.turnOff();

		// Assert
		Duration duration = testee.getTotalActive();
		assertEquals(Duration.of(20, ChronoUnit.MINUTES), duration);
	}
}
