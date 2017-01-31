package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class LascaDirectMove extends LascaMove {
	protected static LascaDirectMove fromValidIds(String start, String end) {
		LascaDirectMove move = new LascaDirectMove();
		move.setValidStartAndEnd(start, end);
		return move;
	}
}
