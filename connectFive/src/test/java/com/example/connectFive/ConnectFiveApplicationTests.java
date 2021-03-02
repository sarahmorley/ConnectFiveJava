package com.example.connectFive;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ConnectFiveApplicationTests {

	@Autowired
	private Game game;

	@Test
	public void testVerticalWin() {
		String colour = "[X]";
		String[][] board = createBoard();
		board[5][5] = "[X]";
		board[4][5] = "[X]";
		board[3][5] = "[X]";
		board[2][5] = "[X]";
		board[1][5] = "[X]";
		Boolean verticalWin = game.CheckForVerticalWin(board, 1, 5, colour);
		assertEquals(true, verticalWin);
	}

	@Test
	public void testHorizontalWin() {
		String colour = "[X]";
		String[][] board = createBoard();
		board[5][4] = "[X]";
		board[5][3] = "[X]";
		board[5][2] = "[X]";
		board[5][1] = "[X]";
		board[5][0] = "[X]";
		Boolean horizontalWin = game.CheckForHorizontalWin(board, 5, 0, colour);
		assertEquals(true, horizontalWin);
	}

	// direction = / From top down left
	@Test
	public void testDiagonalLeftWin() {
		String colour = "[X]";
		String[][] board = createBoard();
		board[5][0] = "[X]";
		board[4][1] = "[X]";
		board[3][2] = "[X]";
		board[2][3] = "[X]";
		board[1][4] = "[X]";
		Boolean horizontalWin = game.CheckForDiagonalLeftWin(board, 1, 4, colour);
		assertEquals(true, horizontalWin);
	}

	// direction = \ From top down right
   @Test
	public void DiagonalRightWinTest() {
		String colour = "[X]";
		String[][] board = createBoard();
		board[5][8] = "[X]";
		board[4][7] = "[X]";
		board[3][6] = "[X]";
		board[2][5] = "[X]";
		board[1][4] = "[X]";
		Boolean horizontalWin = game.CheckForDiagonalRightWin(board, 1, 4, colour);
		assertEquals(true, horizontalWin);
	}

	@Test
	public void FullColumnTest() {
		String colour = "[X]";
		String[][] board = createBoard();
		board[5][5] = "[X]";
		board[4][5] = "[X]";
		board[3][5] = "[X]";
		board[2][5] = "[X]";
		board[1][5] = "[X]";
		board[0][5] = "[X]";
		int[] actualCoordinates = game.AddTurn(6, board, colour);
		int[] expectedCoordinates = new int[2];
		expectedCoordinates[0] = 10;
		assertEquals(expectedCoordinates[0], actualCoordinates[0]);
	}

	@Test
	public void addTurnTest() {
		String colour = "[X]";
		String[][] board = new String[game.Rows][game.Cols];
		int[] expectedCoordinates = new int[2];
		expectedCoordinates[0] = 5;
		expectedCoordinates[1] = 4;

		int[] actualCoordinates = game.AddTurn(4, board, colour);
		assertEquals(expectedCoordinates[0], actualCoordinates[0]);
		assertEquals(expectedCoordinates[1], actualCoordinates[1]);
	}

	public String[][] createBoard()
	{
		String[][] board = new String[game.Rows][game.Cols];
		for (int i = 0; i < game.Rows; i++)
		{
			for (int z = 0; z < game.Cols; z++)
			{
				if (board[i][z] == null)
				board[i][z] = "[ ]";
			}
		}
		return board;
	}
}
