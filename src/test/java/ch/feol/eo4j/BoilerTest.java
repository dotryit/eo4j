package ch.feol.eo4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class BoilerTest {

	@Test
	public void test_fullHeatingDuration() {

		// Prepare
		Boiler testee = new Boiler("Test", 120, 2.4);

		// Act
		Duration result = testee.getFullHeatingDuration();

		// Assert
		assertEquals(Duration.of(10500, ChronoUnit.SECONDS), result);

	}

}
