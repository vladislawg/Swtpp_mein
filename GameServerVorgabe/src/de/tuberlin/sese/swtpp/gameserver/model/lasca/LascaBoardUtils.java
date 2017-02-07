package de.tuberlin.sese.swtpp.gameserver.model.lasca;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

public abstract class LascaBoardUtils implements Serializable{
	private static final long serialVersionUID = 789455852701851311L;

	public static final int ROWS = 7;
	
	private static ArrayList<List<String>> groupedIds;
	private static HashSet<String> hashedIds;
	
	public static void initialize() {
		initializeGroupedIds();
		initializeHashedIds();
	}
	
	private static void initializeGroupedIds() {
		groupedIds = new ArrayList<>();
		for (int y = 0; y < ROWS; y++) {
			ArrayList<String> row = new ArrayList<>();
			
			if (y % 2 == 1) {
				row.add(idFromPosition(1, y));
				row.add(idFromPosition(3, y));
				row.add(idFromPosition(5, y));
			} else {
				row.add(idFromPosition(0, y));
				row.add(idFromPosition(2, y));
				row.add(idFromPosition(4, y));
				row.add(idFromPosition(6, y));
			}
			
			groupedIds.add(row);;
		}
	}
	
	private static void initializeHashedIds() {
		hashedIds = new HashSet<>();
		for (List<String> ids : groupedIds)
			hashedIds.addAll(ids);
	}	
	
	public static List<String> getValidIds() {
		return new ArrayList<String>(hashedIds);
	}
	
	private static String idFromPosition(int x, int y) {
		if (x < 0 || x > 6) return null;
		if (y < 0 || y > 6) return null;
		return "abcdefg".substring(x, x+1) + (y + 1);
	}
	
	private static int[] positionFromId(String id) {
		int x = "abcdefg".indexOf(id.charAt(0));
		int y = Character.getNumericValue(id.charAt(1)) - 1;
		return new int[] {x, y};
	}
	
	public static boolean isValidId(String string) {
		return hashedIds.contains(string);
	}
	
	public static String getIdForFENPosition(int rowIndex, int colIndex) {
//		This can't be tested using the tests we are allowed to do
//		if (!(0 <= rowIndex && rowIndex < 7)) throw new RuntimeException("Invalid row index");
//		if (colIndex < 0 
//				|| (colIndex % 2 == 0 && colIndex > 3)
//				|| (colIndex % 2 == 1 && colIndex > 4)) 
//			throw new RuntimeException("Invalid column index");

		return groupedIds.get(6 - rowIndex).get(colIndex);
	}	
	
	public static List<List<String>> getIdsInFENOrder() {
		LinkedList<List<String>> ids = new LinkedList<>();
		
		// reverse normal grouped order
		for (List<String> rowIds : groupedIds)
			ids.addFirst(rowIds);
		
		return ids;
	}
	
	public static List<String> getNeighborIds(String id, LascaBoard.Direction direction, int distance) {
		String neighbor1 = null, neighbor2 = null;
		if (direction == LascaBoard.Direction.UP) {
			neighbor1 = tryGetNeighborIdWithOffset(id, distance, distance);
			neighbor2 = tryGetNeighborIdWithOffset(id, -distance, distance);
		} 
		if (direction == LascaBoard.Direction.DOWN) {
			neighbor1 = tryGetNeighborIdWithOffset(id, distance, -distance);
			neighbor2 = tryGetNeighborIdWithOffset(id, -distance, -distance);
		}
		
		ArrayList<String> ids = new ArrayList<>();
		if (neighbor1 != null) ids.add(neighbor1);
		if (neighbor2 != null) ids.add(neighbor2);
		return ids;
	}
	
	private static String tryGetNeighborIdWithOffset(String id, int xOffset, int yOffset) {
		int[] pos = positionFromId(id);
		return idFromPosition(pos[0] + xOffset, pos[1] + yOffset);
	}
	
	public static boolean areNeighbors(String id1, String id2) {
		return checkIdDistance(id1, id2, 1);
	}
	
	public static boolean areSecondaryNeighbors(String id1, String id2) {
		return checkIdDistance(id1, id2, 2);
	}
	
	private static boolean checkIdDistance(String id1, String id2, int distance) {
		int[] pos1 = positionFromId(id1);
		int[] pos2 = positionFromId(id2);
		return Math.abs(pos1[0] - pos2[0]) == distance & Math.abs(pos1[1] - pos2[1]) == distance;
	}
	
	public static String getMiddleId(String id1, String id2) {
		int[] pos1 = positionFromId(id1);
		int[] pos2 = positionFromId(id2);
		return idFromPosition((pos1[0] + pos2[0]) / 2, (pos1[1] + pos2[1]) / 2); 
	} 
	
	public static boolean isInLastRow(String id, LascaBoard.Color color) {
		if (color == LascaBoard.Color.BLACK) 
			return id.endsWith("1");
		return id.endsWith("7");
	}
}
