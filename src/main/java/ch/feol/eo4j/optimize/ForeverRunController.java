package ch.feol.eo4j.optimize;

import java.time.LocalDateTime;

public class ForeverRunController implements RunController {

	@Override
	public boolean isBreak(LocalDateTime timestamp) {
		return false;
	}
}
