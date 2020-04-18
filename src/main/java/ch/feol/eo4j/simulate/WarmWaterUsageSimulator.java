package ch.feol.eo4j.simulate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class WarmWaterUsageSimulator implements UsageSimulator {

	private static final LocalTime SIX = LocalTime.of(6, 0);

	private static final LocalTime EIGHT = LocalTime.of(8, 0);

	private static final LocalTime ELEVEN = LocalTime.of(11, 0);

	private static final LocalTime THIRTHEEN = LocalTime.of(13, 0);

	private static final LocalTime TWENTY = LocalTime.of(20, 0);

	private static final LocalTime TWENTYTWO = LocalTime.of(22, 0);

	private static final Map<Integer, Double> MINUTE_USAGE_PER_PERSON = new HashMap<>();

	private int persons;

	public WarmWaterUsageSimulator(int persons, int dailyUsagePerPerson) {
		this.persons = persons;
		init(dailyUsagePerPerson);
	}

	private static void init(int dailyUsagePerPerson) {
		LocalTime t = LocalTime.MIDNIGHT;
		do {
			double usage = 0;
			usage += addUsagePerMinute(t, SIX, EIGHT, 30, dailyUsagePerPerson);
			usage += addUsagePerMinute(t, EIGHT, ELEVEN, 10, dailyUsagePerPerson);
			usage += addUsagePerMinute(t, ELEVEN, THIRTHEEN, 20, dailyUsagePerPerson);
			usage += addUsagePerMinute(t, THIRTHEEN, TWENTY, 10, dailyUsagePerPerson);
			usage += addUsagePerMinute(t, TWENTY, TWENTYTWO, 30, dailyUsagePerPerson);

			int minute = t.get(ChronoField.MINUTE_OF_DAY);
			MINUTE_USAGE_PER_PERSON.put(Integer.valueOf(minute), Double.valueOf(usage));

			t = t.plusMinutes(1);
		} while (t.isBefore(LocalTime.of(23, 59)));
	}

	private static double addUsagePerMinute(LocalTime actual, LocalTime from, LocalTime to, int percent,
			int daylyUsagePerPerson) {
		if (actual.isAfter(from.minusMinutes(1)) && actual.isBefore(to)) {
			long minutes = from.until(to, ChronoUnit.MINUTES);
			return daylyUsagePerPerson / 100.0 * percent / minutes;
		} else {
			return 0;
		}
	}

	public double simulateUsage(LocalDateTime from, LocalDateTime to) {

		double usage = 0.0;
		LocalDateTime actual = from;
		do {
			usage += getMinuteUsage(actual);
			actual = actual.plusMinutes(1);
		} while (actual.isBefore(to));

		return usage;
	}

	@Override
	public double getMinuteUsage(LocalDateTime timestamp) {
		Integer minuteOfDay = Integer.valueOf(timestamp.get(ChronoField.MINUTE_OF_DAY));
		Double minuteUsage = MINUTE_USAGE_PER_PERSON.get(minuteOfDay);
		if (minuteUsage != null) {
			return minuteUsage.doubleValue() * persons;
		} else {
			return 0;
		}
	}
}
