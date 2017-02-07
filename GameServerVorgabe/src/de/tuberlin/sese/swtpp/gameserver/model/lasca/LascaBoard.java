package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;

public class LascaBoard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6877891466483362925L;

	public enum Color { BLACK, WHITE }
	public enum Direction { UP, DOWN }
	
	private HashMap<String, LascaPosition> positions;
	
	private LascaBoard() {
		LascaBoardUtils.initialize();
		positions = new HashMap<>();
		for (String id : LascaBoardUtils.getValidIds()) {
			positions.put(id, new LascaPosition());
		}
	}
	
	public static LascaBoard fromString(String string) {
		LascaBoard board = new LascaBoard();
		
		String[] rows = string.split("/");
		for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
			String[] cols = rows[rowIndex].split(",");
			for (int colIndex = 0; colIndex < cols.length; colIndex++) {
				
				String id = LascaBoardUtils.getIdForFENPosition(rowIndex, colIndex);
				LascaPosition position = LascaPosition.fromString(cols[colIndex]);
				board.positions.put(id, position);
			}
		}
		
		return board;
	}
	
	/**
	 * Perform the given move if possible, otherwise raise an Exception
	 * 
	 * @return True, when same player can move again, otherwise false
	 */
	public boolean performMove(LascaMove move, Color color) {
		if (!isValidMove(move, color))
			throw new IllegalArgumentException("Cannot make this move on the board");
		
		performValidMove(move, color);
		boolean madeOfficer = makeOfficerIfNecessary(move, color);
		
		if (!madeOfficer && move instanceof LascaAttackMove) 
			return canAttackExceptReverse(move.getEnd(), color, move);
		return false;
	}
	
	private boolean isValidMove(LascaMove move, Color color) {
		for (LascaMove validMove : getValidMoves(color))
			if (move.equals(validMove)) return true;
		return false;
	}
	
	// Find possible moves
	// ------------------------------------------------
	
	private List<LascaMove> getValidMoves(Color color) {		
	    List<LascaMove> attackMoves = getValidAttackMoves(color);
	    if (attackMoves.size() > 0) return attackMoves;
	    return getValidDirectMoves(color);
	}
	
	private List<LascaMove> getValidAttackMoves(Color color) {
		ArrayList<LascaMove> moves = new ArrayList<>();
		for (String startId : LascaBoardUtils.getValidIds()) 
			moves.addAll(getValidAttackMovesFromStart(startId, color));
		return moves;
	}
	
	private List<LascaMove> getValidAttackMovesFromStart(String startId, Color color) {		
		ArrayList<LascaMove> moves = new ArrayList<>();
		for (String target : getPossibleTargets(startId, color, 2)) {
			LascaAttackMove move = LascaAttackMove.fromValidIds(startId, target);
			LascaPosition middlePos = positions.get(move.getMiddle());
			LascaPosition endPos = positions.get(move.getEnd());
			
			if (middlePos.isEmpty()) continue;
			if (!endPos.isEmpty()) continue;
			
			LascaStone middleStone = middlePos.peekTopStone();
			if (middleStone.getColor() == color) continue;
			
			moves.add(move);
		}
		return moves;
	}
	
	private List<LascaMove> getValidDirectMoves(Color color) {
		ArrayList<LascaMove> moves = new ArrayList<>();
		for (String startId : LascaBoardUtils.getValidIds()) 
			moves.addAll(getValidDirectMovesFromStart(startId, color));
		return moves;
	}
	 
	private List<LascaMove> getValidDirectMovesFromStart(String startId, Color color) {
		ArrayList<LascaMove> moves = new ArrayList<>();
		for (String target : getPossibleTargets(startId, color, 1)) {
			if (positions.get(target).isEmpty()) {
				moves.add(LascaMove.fromStartAndEnd(startId, target));				
			}
		}
		return moves;
	}
	
	private List<String> getPossibleTargets(String startId, Color color, int distance) {
		LascaStone stone = getMovableTopStone(startId, color);
		if (stone == null) return new ArrayList<>();
		
		ArrayList<String> targets = new ArrayList<>();
		if (stone.getIsOfficer()) {
			targets.addAll(LascaBoardUtils.getNeighborIds(startId, Direction.UP, distance));
			targets.addAll(LascaBoardUtils.getNeighborIds(startId, Direction.DOWN, distance));
		} else {
			targets.addAll(LascaBoardUtils.getNeighborIds(startId, directionFromColor(color), distance));
		}
		return targets;
	}
	
	private boolean canAttackExceptReverse(String startId, Color color, LascaMove lastMove) {
		for (LascaMove move : getValidAttackMovesFromStart(startId, color)) {
			if (!move.isReverse(lastMove)) 
				return true;
		}
		return false;
	}
	
	private Direction directionFromColor(Color color) {
		if (color == Color.BLACK) 
			return Direction.DOWN;
		return Direction.UP;
	}
	
	private LascaStone getMovableTopStone(String id, Color color) {
		LascaPosition position = positions.get(id);
		if (position.isEmpty()) return null;
		LascaStone stone = position.peekTopStone();
		if (stone.getColor() == color) return stone;
		return null;
	}
	
	
	// Perform valid move
	// ------------------------------------------------
	
	private void performValidMove(LascaMove move, Color color) {		
		if (move instanceof LascaDirectMove) {
			performDirectMove((LascaDirectMove)move);
		}
		
		if (move instanceof LascaAttackMove) {
			performAttackMove((LascaAttackMove)move);
		}
	}
	
	private void performDirectMove(LascaDirectMove move) {
		LascaPosition start = positions.get(move.getStart());
		LascaPosition end = positions.get(move.getEnd());
		
		end.putStonesOnTop(start.popAll());
	}
	
	private void performAttackMove(LascaAttackMove move) {
		LascaPosition start = positions.get(move.getStart());
		LascaPosition middle = positions.get(move.getMiddle());
		LascaPosition end = positions.get(move.getEnd());
		
		List<LascaStone> stones = start.popAll();
		stones.add(middle.popTopStone());
		end.putStonesOnTop(stones);
	}
	
	private boolean makeOfficerIfNecessary(LascaMove move, Color color) {
		String endId = move.getEnd();
		LascaPosition endPos = positions.get(endId);
		LascaStone stone = endPos.peekTopStone();
		
		if (!stone.getIsOfficer() && LascaBoardUtils.isInLastRow(move.getEnd(), color)) {
			stone.makeOfficer();
			return true;
		}
		return false;
	}
	
	
	// Check Winner
	// ------------------------------------------------
	
	public boolean hasWon(Color color) {
		if (color == Color.BLACK) 
			return !canMakeValidMove(Color.WHITE);
		return !canMakeValidMove(Color.BLACK);
	}
	
	private boolean canMakeValidMove(Color color) {
		return getValidMoves(color).size() > 0;
	}
	
	
	// State to String conversion
	// ------------------------------------------------
	
	public String toString() {
		ArrayList<String> parts = new ArrayList<>();
		
		for (List<String> rowIds : LascaBoardUtils.getIdsInFENOrder()) {
			parts.add(getFENStringForIds(rowIds));
		}
		
		return String.join("/", parts);
	}
	
	private String getFENStringForIds(List<String> ids) {
		ArrayList<String> parts = new ArrayList<>();
		
		for (String id : ids) 
			parts.add(positions.get(id).toString());
		
		return String.join(",", parts);
	}
}
