package de.tuberlin.sese.swtpp.gameserver.model.lasca;
import java.util.LinkedList;
import java.util.List;

public class LascaPosition {
	
	private LinkedList<LascaStone> stones;
	
	public LascaPosition() {
		this.stones = new LinkedList<>();
	}
	
	public static LascaPosition fromString(String string) {
		LascaPosition position = new LascaPosition();
		for (int i = string.length() - 1; i >= 0; i--) {
			char c = string.charAt(i);
			position.putStoneOnTop(LascaStone.fromChar(c));
		}
		return position;
	}
	
	public boolean isEmpty() {
		return stones.size() == 0;
	}
	
	public LascaStone peekTopStone() {
		return stones.getFirst();
		
	}
	
	public LascaStone popTopStone() {
		return stones.removeFirst();
	}
	
	public List<LascaStone> popAll() {
		@SuppressWarnings("unchecked")
		List<LascaStone> allStones = (List<LascaStone>)stones.clone();
		stones.clear();
		return allStones;
	}
	
	public void putStoneOnTop(LascaStone stone) {
		stones.addFirst(stone);
	}
	
	public void putStonesOnTop(List<LascaStone> stones) {
		this.stones.addAll(0, stones);
	}
	
	public String toString() {
		String string = "";
		for (LascaStone stone : this.stones) {
			string += stone.toString();
		}
		return string;
	}
}
