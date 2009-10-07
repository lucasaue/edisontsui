package com.googlecode.edisontsui;

import junit.framework.TestCase;

public class TestMaze extends TestCase {
	private int m_mazeSize 		= 10;
	private int m_totalTreasure = 5;
	private int m_testPlayerId[] = { 0, 1, 3, 9, 10};

	public void testMaze() {
		try {
			Maze maze = new Maze(m_mazeSize, m_totalTreasure);
			assertEquals(0, maze.getNumPlayer());
			assertEquals(m_mazeSize, maze.getMazeSize());
			assertEquals(m_totalTreasure, maze.getLeftTreasure());
			assertEquals(MazeStatus.MAZE_WAITING, maze.getStatus());
		} catch(Exception e) {
			fail("Exception: ");
		}
	}

	public void testAddPlayer() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		// add some player
		try {
			for(int i =0; i<2; i++) {
				maze.addPlayer(m_testPlayerId[i], -1);
			}
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}
		// add duplicated player
		try {
			maze.addPlayer(m_testPlayerId[0], -1);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Player exist", e.getError());
		}
		// add player after starting server
		try {
			maze.start();
			maze.addPlayer(m_testPlayerId[3], -1);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Maze is closed for join", e.getError());
		}
	}

	public void testRemovePlayerInt() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			// add some player
			for(int i =0; i<5; i++) {
				maze.addPlayer(m_testPlayerId[i], -1);
			}
			// remove all player
			for(int i =0; i<5; i++) {
				maze.removePlayer(m_testPlayerId[i]);
			}
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}
		// duplicate remove
		try {
			maze.removePlayer(0);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Player not exist", e.getError());
		}
		// remove null
		try {
			maze.removePlayer(100);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Player not exist", e.getError());
		}
	}

	public void testRemovePlayerPlayer() {
	}

	public void testMove() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		int x = 4;
		int y = 5;
		int index;
		// Move b4 game start - error
		try {
			// add some player
			index = y*m_mazeSize + x;
			maze.addPlayer(m_testPlayerId[0], index);
			// Get current location
			Maze.Player tmpPlayer = maze.getPlayer(m_testPlayerId[0]);
			assertEquals(index, tmpPlayer.getPos());
			// move
			maze.move(m_testPlayerId[0], EnumDirection.NORTH);
			assertEquals((y+1)*m_mazeSize+x, tmpPlayer.getPos());
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Maze is not started yet", e.getError());
		}
		// Proper
		try {
			// add some player
			x = 2;
			y = 3;
			index = y*m_mazeSize + x;
			maze.addPlayer(m_testPlayerId[1], index);

			// start maze
			maze.start();

			// Get current location
			Maze.Player tmpPlayer = maze.getPlayer(m_testPlayerId[1]);
			assertEquals(index, tmpPlayer.getPos());
			// move
			maze.move(m_testPlayerId[1], EnumDirection.NORTH);
			assertEquals((y+1)*m_mazeSize+x, tmpPlayer.getPos());
			
		} catch (MazeServerException e) {
			assertEquals("Player not exist", e.getError());
			fail("Exception: " + e.getError());
		}
	}

	
	// Test helper
	public void testGetMazeElementId() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		int x, y, index;
		try {
			x = 5;
			y = 8;
			index = y*m_mazeSize+x;
			assertEquals((y+1)*m_mazeSize+x, maze.getMazeElementId(index, EnumDirection.NORTH));

			x = 5;
			y = 7;
			index = y*m_mazeSize+x;
			assertEquals((y)*m_mazeSize+(x+1), maze.getMazeElementId(index, EnumDirection.EAST));
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}

		try {
			x = m_mazeSize;
			y = m_mazeSize;
			index = y*m_mazeSize+x;
			assertEquals((y+1)*m_mazeSize+x, maze.getMazeElementId(index, EnumDirection.NORTH));
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Input Position out of bound", e.getError());
		}
		
		try {
			x = m_mazeSize-1;
			y = 0;
			index = y*m_mazeSize+x;
			assertEquals((y)*m_mazeSize+(x+1), maze.getMazeElementId(index, EnumDirection.EAST));
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("New Position out of bound", e.getError());
		}
	}
		
	public void testGetPlayerPos() {
		// add a player and get its pos 
		// check if pos equal
		int playerId = 10;
		int playerPos = 34;
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			maze.addPlayer(playerId, playerPos);
			int testPlayerPos = maze.getPlayerPos(playerId);
			assertEquals(playerPos, testPlayerPos);
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}
	}
	
	public void testLeaveMazeElement() {
		int playerPos;
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			playerPos = m_mazeSize*m_mazeSize + 3;
			maze.leaveMazeElement(playerPos);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Maze element not exist", e.getError());
		}
		try {
			playerPos = 0*m_mazeSize + 3;
			maze.leaveMazeElement(playerPos);
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Maze Element is not occupied", e.getError());
		}
	}
	
	public void testEndGameCallback() {
		class TestObj implements Runnable {
			public int test = 10;
			@Override
			public void run() {
				test =20;				
			}
		}
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		TestObj testObj = new TestObj();
		maze.setEndGameCallback(testObj);
		try {
			maze.start();
			maze.end();
			assertEquals(20, testObj.test);
		} catch (MazeServerException e) {
			fail("Exception: "+e.getError());
		}
	}
}
