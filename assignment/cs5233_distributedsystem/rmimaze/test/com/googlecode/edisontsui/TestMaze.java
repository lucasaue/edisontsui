package com.googlecode.edisontsui;

import java.util.*;

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
		} catch(Exception e) {
			fail("Exception: ");
		}
	}

	public void testAddPlayer() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		// add some player
		try {
			for(int i =0; i<5; i++) {
				maze.addPlayer(new Player(m_testPlayerId[i]));
			}
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}
		// add duplicated player
		try {
			maze.addPlayer(new Player(m_testPlayerId[0]));
			fail("Catch: should be throwing exception");
		} catch (MazeServerException e) {
			assertEquals("Player exist", e.getError());
		}
	}

	public void testRemovePlayerInt() {
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			// add some player
			for(int i =0; i<5; i++) {
				maze.addPlayer(new Player(m_testPlayerId[i]));
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
		Player playerList[] = {};
		int x = 4;
		int y = 5;
		int index;
		try {
			// add some player
			index = y*m_mazeSize + x;
			Player player = new Player(m_testPlayerId[0]);
			player.setPos(index);
			maze.addPlayer(player);
			// Get current location
			Player tmpPlayer = maze.getPlayer(m_testPlayerId[0]);
			assertEquals(index, tmpPlayer.getPos());
			// move
			maze.move(m_testPlayerId[0], EnumDirection.NORTH);
			assertEquals((y+1)*m_mazeSize+x, tmpPlayer.getPos());
			
		} catch (MazeServerException e) {
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
	
	public void TestGetPlayer() {
		int playerId = 10;
		int playerPos = 34;
		Player player = new Player(playerId);
		player.setPos(playerPos);
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			maze.addPlayer(player);
			Player testPlayer = maze.getPlayer(playerId);
			assertEquals(player, testPlayer);
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}

	}
	
	public void testGetPlayerPos() {
		// add a player and get its pos 
		// check if pos equal
		int playerId = 10;
		int playerPos = 34;
		Player player = new Player(playerId);
		player.setPos(playerPos);
		Maze maze = new Maze(m_mazeSize, m_totalTreasure);
		try {
			maze.addPlayer(player);
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
}
