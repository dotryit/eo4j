package ch.feol.eo4j.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

class PeriodTest {

	private static final int YEAR = 2020;

	private static final LocalDateTime APRIL = LocalDateTime.of(YEAR, Month.APRIL, 1, 0, 0);

	private static final LocalDateTime MAY = LocalDateTime.of(YEAR, Month.MAY, 1, 0, 0);

	private static final LocalDateTime JUNE = LocalDateTime.of(YEAR, Month.JUNE, 1, 0, 0);

	private static final LocalDateTime JULY = LocalDateTime.of(YEAR, Month.JULY, 1, 0, 0);

	@Test
	void test_getDuration_Period_Timestamp_open() {

		// Prepare
		Period openPeriod = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 35));
		Period closedPeriod = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 35),
				LocalDateTime.of(2020, Month.APRIL, 2, 11, 40));
		LocalDateTime now = LocalDateTime.of(2020, Month.APRIL, 2, 11, 45);

		Period testee = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 30));

		// Act & assert
		assertEquals(Duration.of(10, ChronoUnit.MINUTES), testee.getDuration(openPeriod, now));
		assertEquals(Duration.of(5, ChronoUnit.MINUTES), testee.getDuration(closedPeriod, now));
	}

	@Test
	void test_getDuration_Period_Timestamp_closed() {

		// Prepare
		Period openPeriod = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 35));
		Period closedPeriod = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 35),
				LocalDateTime.of(2020, Month.APRIL, 2, 11, 40));
		LocalDateTime now = LocalDateTime.of(2020, Month.APRIL, 2, 11, 45);

		Period testee = new Period(LocalDateTime.of(2020, Month.APRIL, 2, 11, 30),
				LocalDateTime.of(2020, Month.APRIL, 2, 11, 40));

		// Act & assert
		assertEquals(Duration.of(5, ChronoUnit.MINUTES), testee.getDuration(openPeriod, now));
		assertEquals(Duration.of(5, ChronoUnit.MINUTES), testee.getDuration(closedPeriod, now));
	}

	@Test
	void test_getDays() {

		// Assert
		assertEquals(30, new Period(APRIL, MAY).getDays());
		assertEquals(91, new Period(APRIL, JULY).getDays());
	}

	@Test
	void test_endBeforeStart() {

		// Act
		assertThrows(IllegalArgumentException.class, () -> {
			new Period(JULY, MAY);
		});
	}

	@Test
	void test_getDuration_open_overlap_right() {
		// Prepare
		Period limit = new Period(JUNE, JULY);
		Period testee = new Period(APRIL);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(30), result);
	}

	@Test
	void test_getDuration_open_overlap_left() {
		// Prepare
		Period limit = new Period(APRIL, JULY);
		Period testee = new Period(MAY);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(61), result);
	}

	@Test
	void test_getDuration_open_no_overlap() {
		// Prepare
		Period limit = new Period(APRIL, MAY);
		Period testee = new Period(JUNE);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(0), result);
	}

	@Test
	void test_getDuration_closed_overlap_right() {
		// Prepare
		Period limit = new Period(MAY, JULY);
		Period testee = new Period(APRIL, JUNE);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(31), result);
	}

	@Test
	void test_getDuration_closed_overlap_left() {
		// Prepare
		Period limit = new Period(APRIL, JUNE);
		Period testee = new Period(MAY, JULY);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(31), result);
	}

	@Test
	void test_getDuration_closed_no_overlap() {
		// Prepare
		Period limit = new Period(APRIL, MAY);
		Period testee = new Period(JUNE, JULY);

		// Act
		Duration result = testee.getDuration(limit);

		// Assert
		assertEquals(Duration.ofDays(0), result);
	}

	@Test
	void test_equals() {
		// Prepare
		Period mayOpen = new Period(MAY);
		Period mayToJuly = new Period(MAY, JULY);
		Period julyOpen = new Period(JULY);
		Period otherMayOpen = new Period(MAY);

		// Assert
		assertTrue(mayOpen.equals(mayOpen));
		assertFalse(mayOpen.equals(mayToJuly));
		assertFalse(mayOpen.equals(julyOpen));
		assertTrue(mayToJuly.equals(mayToJuly));
		assertTrue(mayOpen.equals(otherMayOpen));

	}

}
