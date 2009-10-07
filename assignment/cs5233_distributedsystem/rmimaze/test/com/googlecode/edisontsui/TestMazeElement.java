package com.googlecode.edisontsui;

import junit.framework.TestCase;

public class TestMazeElement extends TestCase {
	private int m_treasure = 20;
	
	public void testMazeElement() {
		try {
			MazeElement mazeEle = new MazeElement(m_treasure);
			// test default value
			assertEquals(false, mazeEle.isOccupied());
			int earnTreasure = 0;
			earnTreasure = mazeEle.enter();
			assertEquals(m_treasure, earnTreasure);
		} catch (MazeServerException e) {
			fail("Exception: " + e.getError());
		}
	}

	public void testSetTreasure() {
		try {
			MazeElement mazeEle = new MazeElement(m_treasure);
			int earnTreasure = 0;
			mazeEle.setTreasure(m_treasure+15);
			earnTreasure = mazeEle.enter();
			assertEquals(m_treasure+15, earnTreasure);
			mazeEle.leave();
			
			mazeEle.addTreasure(5);
			earnTreasure = mazeEle.enter();
			assertEquals(5, earnTreasure);

		} catch (MazeServerException e) {
			fail("Exception: "+e.getError());
		}
	}

	public void testIsOccupied() {
		MazeElement mazeEle = new MazeElement(m_treasure);
		try {
			mazeEle.enter();
			assertEquals(true, mazeEle.isOccupied());
			mazeEle.leave();
			assertEquals(false, mazeEle.isOccupied());
		} catch (MazeServerException e) {
			fail("Excpetion: "+e.getError());
		}
	}

	public void testEnter() {
		// enter an emtpy maze ele
		try {
			MazeElement mazeEle = new MazeElement(m_treasure);
			mazeEle.enter();
		} catch (MazeServerException e) {
			fail("Exception: "+e.getError());
		}
		// enter an occupied maze ele
		try {
			MazeElement mazeEle = new MazeElement(m_treasure);
			mazeEle.enter();
			mazeEle.enter();
		} catch (MazeServerException e) {
			assertEquals("Maze Element is occupied", e.getError());
		}
	}

	public void testLeave() {
		MazeElement mazeEle = new MazeElement(m_treasure);
		// Leave a empty maze element
		try {
			mazeEle.leave();
		} catch (MazeServerException e) {
			assertEquals("Maze Element is not occupied", e.getError());
		}
		// Leave a occupied maze element
		try {
			mazeEle.enter();
			mazeEle.leave();
		} catch (MazeServerException e) {
			fail("Excpetion: "+e.getError());
		}
	}

}
