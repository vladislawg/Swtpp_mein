package de.tuberlin.sese.swtpp.gameserver.test.lasca;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaGame;

public class TryMoveTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	LascaGame game = null;
	GameController controller;
	
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
	
	private final String defaultBoard = "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w";
	
	@Test
	public void moveUp() {
		startGame(defaultBoard, true);
		assertMove("a3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void moveDown() {
		startGame(defaultBoard, false);
		assertMove("e5-d4", false, true);
		assertGameState("b,b,b,b/b,b,b/b,b,,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void testInvalidMoveStrings() {
		startGame(defaultBoard, true);
		assertMove("a3-b4-c5", false, false);
		assertMove("g3-h4", false, false);
		assertMove("b0-c1", false, false);
	}
	
	@Test
	public void testInvalidMoveDistance() {
		startGame(",,b,w/,,/,,,/,,/,,,/,w,/,,,", true);
		assertMove("d2-g5", true, false);
	}
	
	@Test
	public void makeOfficer() {
		startGame(",,,/,w,/,,,/,b,/,,,/,,/,,,", true);
		assertMove("d6-e7", true, true);
		assertGameState(",,W,/,,/,,,/,b,/,,,/,,/,,,", false, false, false);
	}
	
	@Test
	public void testMoveWithoutStone() {
		startGame(",,,/,w,/,,,/,b,/,,,/,,/,,,", true);
		assertMove("c3-b4", true, false);
	}
	
	@Test
	public void testSimpleAttack() {
		startGame(",,,/,,/,,,/,b,/,w,,/,b,/,,,", true);
		assertMove("c3-e5", true, true);
		assertGameState(",,,/,,/,,wb,/,,/,,,/,b,/,,,", false, false, false);
	}
	
	@Test
	public void testBlackOfficerStoneMovement() {
		startGame(",,,/,,/,w,,/,b,/,,,/,B,/,,,", false);
		assertMove("d2-e3", false, true);
		assertGameState(",,,/,,/,w,,/,b,/,,B,/,,/,,,", true, false, false);
	}
	
	@Test
	public void testDoubleAttack() {
		startGame(",,b,/,w,/,,,/,,b/,,,/,b,/,w,,", true);
		assertMove("c1-e3", true, true);
		assertGameState(",,b,/,w,/,,,/,,b/,,wb,/,,/,,,", true, false, false);
		assertMove("e3-g5", true, true);
		assertGameState(",,b,/,w,/,,,wbb/,,/,,,/,,/,,,", false, false, false);
	}
	
	@Test
	public void testAttackOwnColor() {
		startGame(",,b,w/,,/,,,/,,/,,w,/,w,/,,,", true);
		assertMove("d2-f4", true, false);
	}
	
	@Test
	public void testMoveWrongColor() {
		startGame(",,b,w/,,/,,,/,b,/,,,/,,/,,,", true);
		assertMove("d4-e3", true, false);
	}
	
	@Test
	public void testMoveWhiteOfficer() {
		startGame(",,b,w/,,/,,,/,W,/,,,/,,/,,,", true);
		assertMove("d4-e3", true, true);
		assertGameState(",,b,w/,,/,,,/,,/,,W,/,,/,,,", false, false, false);
	}
	
	@Test
	public void testMoveMultipleStonesDirectly() {
		startGame(",,b,w/,,/,,,/,wbb,/,,,/,,/,,,", true);
		assertMove("d4-e5", true, true);
		assertGameState(",,b,w/,,/,,wbb,/,,/,,,/,,/,,,", false, false, false);
	}
	
	@Test
	public void testDoubleAttackInDifferentDirections() {
		startGame(",,b,w/,,/,,,/b,b,/W,,,/,,/,,,", true);
		assertMove("a3-c5", true, true);
		assertGameState(",,b,w/,,/,Wb,,/,b,/,,,/,,/,,,", false, false, false);
	}
	
	@Test
	public void testSimpleWinningMove() {
		startGame(",,,/,,/,,,/b,,/w,,,/,,/,,,", true);
		assertMove("a3-c5", true, true);
		assertGameState(",,,/,,/,wb,,/,,/,,,/,,/,,,", false, true, true);
	}
	
	@Test
	public void testDoubleAttackWinningMove() {
		startGame(",,,/,,b/,,w,/,,/,,w,/,,/,,,", false);
		assertMove("f6-d4", false, true);
		assertGameState(",,,/,,/,,,/,bw,/,,w,/,,/,,,", false, false, false);
		assertMove("d4-f2", false, true);
		assertGameState(",,,/,,/,,,/,,/,,,/,,bww/,,,", true, true, false);
	}
	
	@Test
	public void testDirectMoveWhileAttackIsPossible() {
		startGame(",b,w,/,,/,,,/b,,/w,,,w/,,/,,,", false);
		assertMove("g3-f4", true, false);
	}
	
	@Test
	public void testWrongPlayerMoving() {
		startGame(",b,,/,,w/,,,/,b,/,,,/,,/,,,", true);
		assertMove("d4-c3", false, false);
		assertGameState(",b,,/,,w/,,,/,b,/,,,/,,/,,,", true, false, false);
	}
	
	@Test
	public void testDoubleAttackWithDifferentStones() {
		startGame(",b,,/,,w/,,,/,b,/,,,/b,,b/w,,w,", true);
		assertMove("a1-c3", true, true);
		assertGameState(",b,,/,,w/,,,/,b,/,wb,,/,,b/,,w,", true, false, false);
		assertMove("e1-g3", true, false);
	}
	
	@Test
	public void testExampleFromTaskDescription() {
		startGame("b,b,,/b,b,bb/,b,bw,b/,,wb/w,wb,w,w/ww,,w/,w,,w", false);
		assertMove("c5-b4", false, true);
		assertGameState("b,b,,/b,b,bb/,,bw,b/b,,wb/w,wb,w,w/ww,,w/,w,,w", true, false, false);
		assertMove("a3-c5", true, true);
		assertGameState("b,b,,/b,b,bb/,wb,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", true, false, false);
		assertMove("c5-e7", true, true);
		assertGameState("b,b,Wbb,/b,,bb/,,bw,b/,,wb/,wb,w,w/ww,,w/,w,,w", false, false, false);
	}
	
//	Cannot test this yet because we are not allowed to check for exceptions
//	@Test
//	public void testInvalidGameState() {
//		startGame(",,b,w/,,/,,,/,q,/,,,/,,/,,,", true);
//	}

	//TODO: implement test cases of same kind as example here
}
