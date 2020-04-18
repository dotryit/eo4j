package ch.feol.eo4j.simulate;

import java.time.LocalDateTime;

public interface UsageSimulator {

	public double simulateUsage(LocalDateTime from, LocalDateTime to);

	public double getMinuteUsage(LocalDateTime timestamp);

}
