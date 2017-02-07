package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class LascaAttackMove extends LascaMove {
	private static final long serialVersionUID = -3144921490536801307L;

	protected static LascaAttackMove fromValidIds(String start, String end) {
		LascaAttackMove move = new LascaAttackMove();
		move.setValidStartAndEnd(start, end);
		return move;
	}
	
	public String getMiddle() {
		return LascaBoardUtils.getMiddleId(getStart(), getEnd());
	}
}
