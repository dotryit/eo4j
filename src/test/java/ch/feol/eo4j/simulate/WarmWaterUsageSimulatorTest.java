package ch.feol.eo4j.simulate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class WarmWaterUsageSimulatorTest {

	@Test
	public void test_usage_day() {
		// Prepate
		LocalDateTime from = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		LocalDateTime to = from.plusDays(1);

		WarmWaterUsageSimulator testee = new WarmWaterUsageSimulator(2, 40);

		// Act
		double result = testee.simulateUsage(from, to);

		// Assert
		assertEquals(80.0, result, 0.01);

	}

	@Test
	public void test_usage_week() {
		// Prepate
		LocalDateTime from = LocalDateTime.now();
		LocalDateTime to = from.plusDays(7);

		WarmWaterUsageSimulator testee = new WarmWaterUsageSimulator(2, 40);

		// Act
		double result = testee.simulateUsage(from, to);

		// Assert
		assertEquals(560.0, result, 0.01);

	}

	@Test
	public void test_usage_tenToNineteen() {
		// Prepate
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime from = now.truncatedTo(ChronoUnit.DAYS).plusHours(9);
		LocalDateTime to = now.truncatedTo(ChronoUnit.DAYS).plusHours(19);

		WarmWaterUsageSimulator testee = new WarmWaterUsageSimulator(4, 100);

		// Act
		double result = testee.simulateUsage(from, to);

		// Assert
		assertEquals(140.95, result, 0.01);
	}
}
