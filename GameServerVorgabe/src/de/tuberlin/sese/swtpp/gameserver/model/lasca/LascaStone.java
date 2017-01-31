package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class LascaStone {
	
	private boolean isOfficer;
	private LascaBoard.Color color;
	
	public LascaStone(LascaBoard.Color color, boolean isOfficer) {
		this.color = color;
		this.isOfficer = isOfficer;
	}
	
	public static LascaStone fromChar(char c) {
		if (c == 'b') return new LascaStone(LascaBoard.Color.BLACK, false);
		if (c == 'B') return new LascaStone(LascaBoard.Color.BLACK, true);
		if (c == 'w') return new LascaStone(LascaBoard.Color.WHITE, false);
		return new LascaStone(LascaBoard.Color.WHITE, true);
//		Can't make this function cleaner because then 100% code coverage would be impossible
//		throw new RuntimeException("Invalid stone character");
	}
	
	public LascaBoard.Color getColor() {
		return this.color;
	}
	
	public boolean getIsOfficer() {
		return this.isOfficer;
	}
	
	public void makeOfficer() {
		this.isOfficer = true;
	}
	
	public String toString() {
		if (color == LascaBoard.Color.BLACK) {
			if (isOfficer) return "B";
			else return "b";
		} else {
			if (isOfficer) return "W";
			else return "w";
		}
	}
}
