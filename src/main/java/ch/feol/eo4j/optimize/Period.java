package ch.feol.eo4j.optimize;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

public class Period {

	private LocalDateTime startAt;

	/**
	 * If endAt is not present, the period is open ended.
	 */
	private Optional<LocalDateTime> endAt;

	public Period(LocalDateTime startAt, Optional<LocalDateTime> endAt) {
		this.startAt = Objects.requireNonNull(startAt);
		this.endAt = endAt;

		if (endAt.isPresent() && startAt.isAfter(endAt.get())) {
			throw new IllegalArgumentException("Start at must be before end at");
		}
	}

	public Period(LocalDateTime startAt, LocalDateTime endAt) {
		this(startAt, Optional.of(endAt));
	}

	public Period(LocalDateTime startAt) {
		this(startAt, Optional.empty());
	}

	public LocalDateTime getStartAt() {
		return this.startAt;
	}

	public Optional<LocalDateTime> getEndAt() {
		return this.endAt;
	}

	public static Period shorten(Period first, Period other) {
		if (other == null) {
			return first;
		}

		LocalDateTime start = other.startAt.isBefore(first.startAt) ? first.startAt : other.startAt;

		Optional<LocalDateTime> end;
		if (first.endAt.isPresent()) {

			if (other.endAt.isPresent()) {
				if (other.getEndAt().get().isAfter(first.endAt.get())) {
					end = first.endAt;
				} else {
					end = other.endAt;
				}
			} else {
				end = first.endAt;
			}
		} else {
			end = other.endAt;
		}
		return new Period(start, end);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endAt == null) ? 0 : endAt.hashCode());
		result = prime * result + ((startAt == null) ? 0 : startAt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (endAt == null) {
			if (other.endAt != null)
				return false;
		} else if (!endAt.equals(other.endAt))
			return false;
		if (startAt == null) {
			if (other.startAt != null)
				return false;
		} else if (!startAt.equals(other.startAt))
			return false;
		return true;
	}

	public long getDays() {
		return startAt.until(endAt.get(), ChronoUnit.DAYS);
	}

	public void end(LocalDateTime localDateTime) {
		endAt = Optional.of(localDateTime);
	}

	public Duration getDuration() {
		if (endAt == null || !endAt.isPresent()) {
			throw new IllegalStateException("End at timestamp must be set to calculate period");
		}
		return Duration.between(startAt, endAt.get());
	}

	public Duration getDuration(LocalDateTime defaultEndAt) {
		if (endAt == null || !endAt.isPresent()) {
			return Duration.between(startAt, defaultEndAt);
		}
		return Duration.between(startAt, endAt.get());
	}

	public boolean isOpen() {
		return endAt == null || !endAt.isPresent();
	}

	public Duration getDuration(Period limit, LocalDateTime defaultEndAt) {

		if (limit == null) {
			return getDuration(defaultEndAt);
		}

		LocalDateTime limitEndAt;
		if (limit.endAt.isPresent()) {
			limitEndAt = limit.endAt.get();
		} else {
			limitEndAt = defaultEndAt;
		}

		if (endAt.isPresent()) {
			return getDuration(startAt, limit.startAt, endAt.get(), limitEndAt);
		} else {
			return getDuration(startAt, limit.startAt, defaultEndAt, limitEndAt);
		}
	}

	public Duration getDuration(Period limit) {

		if (!limit.endAt.isPresent()) {
			throw new IllegalArgumentException("End timestamp of limit must be set");
		}

		if (endAt.isPresent()) {
			return getDuration(startAt, limit.startAt, endAt.get(), limit.endAt.get());
		} else {
			return getDuration(startAt, limit.startAt, limit.endAt.get(), limit.endAt.get());
		}
	}

	private static Duration getDuration(LocalDateTime startAt, LocalDateTime limitStart, LocalDateTime endAt,
			LocalDateTime limitEnd) {
		LocalDateTime start;
		LocalDateTime end;
		if (startAt.isBefore(limitStart)) {
			start = limitStart;
		} else {
			start = startAt;
		}
		if (endAt.isAfter(limitEnd)) {
			end = limitEnd;
		} else {
			end = endAt;
		}
		if (start.isBefore(end)) {
			return new Period(start, end).getDuration();
		} else {
			return Duration.ZERO;
		}
	}
}
