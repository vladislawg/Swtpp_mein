package de.tuberlin.sese.swtpp.gameserver.test.lasca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaBoard;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaGame;

public class TryMoveTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	LascaGame game = null;
	GameController controller;
	LascaBoard board ;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "");
		
		game = (LascaGame) controller.getGame(gameID);
		whitePlayer = game.getPlayer(user1);

	}
	
	
	public void startGame(String initialBoard, boolean whiteNext) {
		controller.joinGame(user2);		
		blackPlayer = game.getPlayer(user2);
		
		game.setState(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(game.tryMove(move, whitePlayer), expectedResult);
		else 
			assertEquals(game.tryMove(move, blackPlayer), expectedResult);
	}
	
	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		assertEquals(game.getState(), expectedBoard);
		assertEquals(game.isWhiteNext(), whiteNext);

		assertEquals(game.isFinished(), finished);
		if (game.isFinished()) {
			assertEquals(whitePlayer.isWinner(), whiteWon);
			assertEquals(blackPlayer.isWinner(), !whiteWon);
		}
	}
	
	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
//	private final String defaultBoard = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w";
//	
//	@Test
//	public void moveUp() {
//		startGame(defaultBoard, true);
//		assertMove("a3-b4", true, true);
//		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,w,w,w/w,w,w/w,w,w,w", false, false, false);
//	}
//	
//	@Test
//	public void moveDown() {
//		startGame(defaultBoard, false);
//		assertMove("e5-d4", false, true);
//		assertGameState("b,b,b,b/b,b,b/b,b,,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
//	}
//	
//	@Test
//	public void testInvalidMoveStrings() {
//		startGame(defaultBoard, true);
//		assertMove("a3-b4-c5", false, false);
//		assertMove("g3-h4", false, false);
//		assertMove("b0-c1", false, false);
//	}
//	
//	@Test
//	public void testInvalidMoveDistance() {
//		startGame(",,b,w/,,/,,,/,,/,,,/,w,/,,,", true);
//		assertMove("d2-g5", true, false);
//	}
//	
//	@Test
//	public void makeOfficer() {
//		startGame(",,,/,w,/,,,/,b,/,,,/,,/,,,", true);
//		assertMove("d6-e7", true, true);
//		assertGameState(",,W,/,,/,,,/,b,/,,,/,,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testMoveWithoutStone() {
//		startGame(",,,/,w,/,,,/,b,/,,,/,,/,,,", true);
//		assertMove("c3-b4", true, false);
//	}
//	
//	@Test
//	public void testSimpleAttack() {
//		startGame(",,,/,,/,,,/,b,/,w,,/,b,/,,,", true);
//		assertMove("c3-e5", true, true);
//		assertGameState(",,,/,,/,,wb,/,,/,,,/,b,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testBlackOfficerStoneMovement() {
//		startGame(",,,/,,/,w,,/,b,/,,,/,B,/,,,", false);
//		assertMove("d2-e3", false, true);
//		assertGameState(",,,/,,/,w,,/,b,/,,B,/,,/,,,", true, false, false);
//	}
//	
//	@Test
//	public void testDoubleAttack() {
//		startGame(",,b,/,w,/,,,/,,b/,,,/,b,/,w,,", true);
//		assertMove("c1-e3", true, true);
//		assertGameState(",,b,/,w,/,,,/,,b/,,wb,/,,/,,,", true, false, false);
//		assertMove("e3-g5", true, true);
//		assertGameState(",,b,/,w,/,,,wbb/,,/,,,/,,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testAttackOwnColor() {
//		startGame(",,b,w/,,/,,,/,,/,,w,/,w,/,,,", true);
//		assertMove("d2-f4", true, false);
//	}
//	
//	@Test
//	public void testMoveWrongColor() {
//		startGame(",,b,w/,,/,,,/,b,/,,,/,,/,,,", true);
//		assertMove("d4-e3", true, false);
//	}
//	
//	@Test
//	public void testMoveWhiteOfficer() {
//		startGame(",,b,w/,,/,,,/,W,/,,,/,,/,,,", true);
//		assertMove("d4-e3", true, true);
//		assertGameState(",,b,w/,,/,,,/,,/,,W,/,,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testMoveMultipleStonesDirectly() {
//		startGame(",,b,w/,,/,,,/,wbb,/,,,/,,/,,,", true);
//		assertMove("d4-e5", true, true);
//		assertGameState(",,b,w/,,/,,wbb,/,,/,,,/,,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testDoubleAttackInDifferentDirections() {
//		startGame(",,b,w/,,/,,,/b,b,/W,,,/,,/,,,", true);
//		assertMove("a3-c5", true, true);
//		assertGameState(",,b,w/,,/,Wb,,/,b,/,,,/,,/,,,", false, false, false);
//	}
//	
//	@Test
//	public void testSimpleWinningMove() {
//		startGame(",,,/,,/,,,/b,,/w,,,/,,/,,,", true);
//		assertMove("a3-c5", true, true);
//		assertGameState(",,,/,,/,wb,,/,,/,,,/,,/,,,", false, true, true);
//	}
//	
//	@Test
//	public void testDoubleAttackWinningMove() {
//		startGame(",,,/,,b/,,w,/,,/,,w,/,,/,,,", false);
//		assertMove("f6-d4", false, true);
//		assertGameState(",,,/,,/,,,/,bw,/,,w,/,,/,,,", false, false, false);
//		assertMove("d4-f2", false, true);
//		assertGameState(",,,/,,/,,,/,,/,,,/,,bww/,,,", true, true, false);
//	}
//	
//	@Test
//	public void testDirectMoveWhileAttackIsPossible() {
//		startGame(",b,w,/,,/,,,/b,,/w,,,w/,,/,,,", false);
//		assertMove("g3-f4", true, false);
//	}
//	
//	@Test
//	public void testWrongPlayerMoving() {
//		startGame(",b,,/,,w/,,,/,b,/,,,/,,/,,,", true);
//		assertMove("d4-c3", false, false);
//		assertGameState(",b,,/,,w/,,,/,b,/,,,/,,/,,,", true, false, false);
//	}
//	
//	@Test
//	public void testDoubleAttackWithDifferentStones() {
//		startGame(",b,,/,,w/,,,/,b,/,,,/b,,b/w,,w,", true);
//		assertMove("a1-c3", true, true);
//		assertGameState(",b,,/,,w/,,,/,b,/,wb,,/,,b/,,w,", true, false, false);
//		assertMove("e1-g3", true, false);
//	}
//	
//	@Test
//	public void testExampleFromTaskDescription() {
//		startGame("b,b,,/b,b,bb/,b,bw,b/,,wb/w,wb,w,w/ww,,w/,w,,w", false);
//		assertMove("c5-b4", false, true);
//		assertGameState("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w", true, false, false);
//		assertMove("a3-c5", true, true);
//		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", true, false, false);
//		assertMove("c5-e7", true, true);
//		assertGameState("b,b,Wbb,/b,,bb/,,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", false, false, false);
//	}
//	
//	@Test
//	public void fullGame(){
//		startGame(defaultBoard, true);
//		assertMove("c3-b4", true, true);
//		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w", false, false, false);
//		assertMove("a5-c3", false, true);
//		assertGameState("b,b,b,b/b,b,b/,b,b,b/,,/w,bw,w,w/w,w,w/w,w,w,w", true, false, false);
//		assertMove("d2-b4", true, true);
//		assertGameState("b,b,b,b/b,b,b/,b,b,b/wb,,/w,w,w,w/w,,w/w,w,w,w", false, false, false);
//		assertMove("c5-d4", false, true);
//		assertGameState("b,b,b,b/b,b,b/,,b,b/wb,b,/w,w,w,w/w,,w/w,w,w,w", true, false, false);
//		assertMove("e3-c5", true, true);
//		assertMove("b6-d4", false, true);
//		assertGameState("b,b,b,b/,b,b/,b,b,b/wb,bw,/w,w,,w/w,,w/w,w,w,w", true, false, false);
//		assertMove("b4-a5", true, true);
//		assertMove("c5-b4", false, true);
//		assertGameState("b,b,b,b/,b,b/wb,,b,b/b,bw,/w,w,,w/w,,w/w,w,w,w", true, false, false);
//		assertMove("a3-c5", true, true);
//		assertMove("d6-b4", false, true);
//		assertMove("b4-d2", false, true);
//		assertGameState("b,b,b,b/,,b/wb,b,b,b/,bw,/,,,w/w,bww,w/w,w,w,w", true, false, false);		
//		assertMove("e1-c3", true, true);
//		assertMove("d4-e3", false, true);
//		assertGameState("b,b,b,b/,,b/wb,b,b,b/,,/,wb,bw,w/w,ww,w/w,w,,w", true, false, false);
//		assertMove("f2-d4", true, true);
//		assertMove("d4-b6", true, true);
//		assertMove("a7-c5", false, true);
//		assertGameState(",b,b,b/bb,,b/wb,bw,b,b/,,/,wb,w,w/w,ww,/w,w,,w", true, false, false);
//		assertMove("g1-f2", true, true);
//		assertMove("e5-d4", false, true);
//		assertGameState(",b,b,b/bb,,b/wb,bw,,b/,b,/,wb,w,w/w,ww,w/w,w,,", true, false, false);
//		assertMove("c3-e5", true, true);
//		assertMove("f6-d4", false, true);
//		assertGameState(",b,b,b/bb,,/wb,bw,bb,b/,bw,/,,w,w/w,ww,w/w,w,,", true, false, false);
//		assertMove("d2-c3", true, true);
//		assertMove("e5-f4", false, true);
//		assertGameState(",b,b,b/bb,,/wb,bw,,b/,bw,bb/,ww,w,w/w,,w/w,w,,", true, false, false);
//		assertMove("c3-e5", true, true);
//		assertMove("f4-d2", false, true);
//		assertGameState(",b,b,b/bb,,/wb,bw,wwb,b/,w,/,,,w/w,bbw,w/w,w,,", true, false, false);
//		assertMove("c1-e3", true, true);
//		assertMove("g5-f4", false, true);
//		assertGameState(",b,b,b/bb,,/wb,bw,wwb,/,w,b/,,wb,w/w,bw,w/w,,,", true, false, false);
//		assertMove("e3-g5", true, true);
//		assertMove("c5-e3", false, true);
//		assertMove("e3-g1",	false, true);
//		assertGameState(",b,b,b/bb,,/wb,,wwb,wbb/,,/,,,w/w,bw,/w,,,Bwww", true, false, false);
//		assertMove("b2-c3",true, true);
//		assertMove("g1-f2", false, true);
//		assertGameState(",b,b,b/bb,,/wb,,wwb,wbb/,,/,w,,w/,bw,Bwww/w,,,", true, false, false);
//		assertMove("c3-b4",true, true);
//		assertMove("d2-c1", false, true);
//		assertGameState(",b,b,b/bb,,/wb,,wwb,wbb/w,,/,,,w/,,Bwww/w,Bw,,", true, false, false);
//		assertMove("g5-f6", true, true);
//		assertMove("e7-g5",false , true);
//		assertGameState(",b,,b/bb,,bb/wb,,wwb,bw/w,,/,,,w/,,Bwww/w,Bw,,", true, false, false);
//		assertMove("b4-c5", true, true);
//		assertMove("b6-d4", false, true);
//		assertGameState(",b,,b/,,bb/wb,,wwb,bw/,bbw,/,,,w/,,Bwww/w,Bw,,", true, false, false);
//		assertMove("a5-b6", true, true);
//		assertMove("c7-a5", false, true);
//		assertGameState(",,,b/b,,bb/bw,,wwb,bw/,bbw,/,,,w/,,Bwww/w,Bw,,", true, false, false);
//		assertMove("e5-d6", true, true);
//		assertMove("c1-d2", false, true);
//		assertGameState(",,,b/b,wwb,bb/bw,,,bw/,bbw,/,,,w/,Bw,Bwww/w,,,", true, false, false);
//		assertMove("d6-c7", true, true);
//		assertMove("a5-b4",false, true);
//		assertGameState(",Wwb,,b/b,,bb/,,,bw/bw,bbw,/,,,w/,Bw,Bwww/w,,,", true, false, false);
//		assertMove("c7-a5", true, true);
//		assertMove("a5-c3", true, true);
//		assertMove("c3-e1", true, true);
//		assertMove("f6-e5", false, true);
//		assertGameState(",,,b/,,/,,bb,bw/w,bbw,/,,,w/,w,Bwww/w,,WwbbbB,", true, false, false);
//		assertMove("b4-a5", true, true);
//		assertMove("g5-f4", false, true);
//		assertGameState(",,,b/,,/w,,bb,/,bbw,bw/,,,w/,w,Bwww/w,,WwbbbB,",true, false, false);
//		assertMove("a1-b2", true, true);
//		assertMove("d4-c3",false, true);
//		assertGameState(",,,b/,,/w,,bb,/,,bw/,bbw,,w/w,w,Bwww/,,WwbbbB,",true, false, false);
//		assertMove("b2-d4", true, true);
//		assertMove("d4-f6",true, true);
//		assertMove("c3-b2", false, true);
//		assertGameState(",,,b/,,wbb/w,,b,/,,bw/,,,w/bw,w,Bwww/,,WwbbbB,", true, false, false);
//		assertMove("f6-e7", true, true);
//		assertMove("b2-a1", false, true);
//		assertGameState(",,Wbb,b/,,/w,,b,/,,bw/,,,w/,w,Bwww/Bw,,WwbbbB,", true, false, false);
//		assertMove("a5-b6",true, true);
//		assertMove("a1-b2", false, true);
//		assertGameState(",,Wbb,b/w,,/,,b,/,,bw/,,,w/Bw,w,Bwww/,,WwbbbB,", true,false, false);
//		assertMove("b6-c7", true, true);
//		assertMove("f2-e3", false, true);
//		assertGameState(",W,Wbb,b/,,/,,b,/,,bw/,,Bwww,w/Bw,w,/,,WwbbbB,", true, false, false);
//		assertMove("e7-d6", true, true);
//		assertMove("e3-c1", false, true);
//		assertGameState(",W,,b/,Wbb,/,,b,/,,bw/,,,w/Bw,,/,Bwwww,WwbbbB,", true, false, false);
//		assertMove("d6-c5", true, true);
//		assertMove("e5-d4",false, true);
//		assertGameState(",W,,b/,,/,Wbb,,/,b,bw/,,,w/Bw,,/,Bwwww,WwbbbB,", true, false, false);
//		assertMove("c5-e3", true, true);
//		assertGameState(",W,,b/,,/,,,/,,bw/,,Wbb,w/Bw,,/,Bwwww,WwbbbB,", true, false, false);
//		
		//assertMove("b2-c3", false, true);
		//assertGameState(",W,,b/,,/,,,Wbbbb/,,w/,Bw,,w/,,/,Bwwww,WwbbbB,", true, false, false);
		
		//assertMove(move, white, expectedResult);
		//assertGameState(expectedBoard, whiteNext, finished, whiteWon);
	//}
	
	
	@Test
	public void test(){
		startGame(",W,,b/,,/,Wbb,,/,b,bw/,,,w/Bw,,/,Bwwww,WwbbbB,", true);
		assertMove("c5-e3", true, true);
		assertGameState(",W,,b/,,/,,,/,,bw/,,Wbbb,w/Bw,,/,Bwwww,WwbbbB,", true, false, false);
		assertMove("e3-g4", true, true);
		
	}

//	Cannot test this yet because we are not allowed to check for exceptions
//	@Test
//	public void testInvalidGameState() {
//		startGame(",,b,w/,,/,,,/,q,/,,,/,,/,,,", true);
//	}

	//TODO: implement test cases of same kind as example here
}
