package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class LascaDirectMove extends LascaMove {
	private static final long serialVersionUID = -4260435739761214421L;

	protected static LascaDirectMove fromValidIds(String start, String end) {
		LascaDirectMove move = new LascaDirectMove();
		move.setValidStartAndEnd(start, end);
		return move;
	}
}
