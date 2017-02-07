package de.tuberlin.sese.swtpp.gameserver.model.lasca;
import java.io.Serializable;

public abstract class LascaMove implements Serializable{	

	private static final long serialVersionUID = -5039800816348334532L;
	private String start;
	private String end;
	
	protected LascaMove() {
		LascaBoardUtils.initialize();
	}
	
	public static LascaMove fromString(String string) {
		String[] parts = string.split("-");

		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid lasca move string");
		}
	
		return fromStartAndEnd(parts[0], parts[1]);
	}
	
	public static LascaMove fromStartAndEnd(String startId, String endId) {
		if (!LascaBoardUtils.isValidId(startId) || !LascaBoardUtils.isValidId(endId)) {
			throw new IllegalArgumentException("Invalid start or end id");
		}
		
		if (LascaBoardUtils.areNeighbors(startId, endId)) {
			return LascaDirectMove.fromValidIds(startId, endId);
		}
		if (LascaBoardUtils.areSecondaryNeighbors(startId, endId)) {
			return LascaAttackMove.fromValidIds(startId, endId);
		}

		throw new IllegalArgumentException("Move is no direct nor an attack move");
	}
	
	protected void setValidStartAndEnd(String start, String end) {
		this.start = start;
		this.end = end;
	}
	
	public String getStart() {
		return this.start;
	}
	
	public String getEnd() {
		return this.end;
	}
	
	public boolean isReverse(LascaMove other) {
		return start.equals(other.end) & end.equals(other.start);
	}
	
	public boolean extendsMove(LascaMove other) {
		return start.equals(other.end);
	}
	
	public boolean equals(LascaMove other) {
		return this.toString().equals(other.toString());
	}
	
	public String toString() {
		return start + "-" + end;
	}
}
