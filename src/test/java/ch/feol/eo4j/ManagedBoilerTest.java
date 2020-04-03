package ch.feol.eo4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class ManagedBoilerTest {

	@Test
	void test_open() {

		SimulationTimerService timerService = new SimulationTimerService();
		Boiler boiler = new Boiler("test", 500, 10);

		ManagedBoiler testee = new ManagedBoiler(boiler, timerService);

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

		ManagedBoiler testee = new ManagedBoiler(boiler, timerService);

		// Act
		testee.turnOn();
		timerService.sleepSeconds(17 * 60);
		testee.turnOff();
		timerService.sleepSeconds(4 * 60);
		testee.turnOn();
		timerService.sleepSeconds(3 * 60);
		testee.turnOff();

		// Assert
		Duration duration = testee.getTotalActive();
		assertEquals(Duration.of(20, ChronoUnit.MINUTES), duration);
	}
}
