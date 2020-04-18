package ch.feol.eo4j.optimize;

import java.time.LocalDateTime;

public class CountRunController implements RunController {

	private int max = 0;

	private int runs = 0;

	public CountRunController(int max) {
		this.max = max;
	}

	@Override
	public boolean isBreak(LocalDateTime timestamp) {
		runs++;
		if (runs >= max) {
			return true;
		} else {
			return false;
		}
	}

}
