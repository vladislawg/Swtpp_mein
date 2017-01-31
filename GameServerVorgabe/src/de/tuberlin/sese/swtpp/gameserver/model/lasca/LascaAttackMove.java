package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class LascaAttackMove extends LascaMove {
	protected static LascaAttackMove fromValidIds(String start, String end) {
		LascaAttackMove move = new LascaAttackMove();
		move.setValidStartAndEnd(start, end);
		return move;
	}
	
	public String getMiddle() {
		return LascaBoardUtils.getMiddleId(getStart(), getEnd());
	}
}
