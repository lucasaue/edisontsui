package com.googlecode.edisontsui;

import junit.framework.TestCase;

public class TestPlayer extends TestCase {
	private int m_playerId =11;
	
	public void testPlayer() {
		// check default value, playerId, pos, earnTreasure
		Player player = new Player(m_playerId);
		assertEquals(m_playerId, player.getId());
		assertEquals(-1, player.getPos());
		assertEquals(0, player.getEarnTreasure());
	}

	public void testAddEarnTreasure() {
		int incr1 = 12;
		int incr2 = 23;
		Player player = new Player(m_playerId);
		player.addEarnTreasure(incr1);
		assertEquals(incr1, player.getEarnTreasure());
		player.addEarnTreasure(incr2);
		assertEquals(incr1+incr2, player.getEarnTreasure());
	}

	public void testSetPos() {
		int newPos = 34;
		Player player = new Player(m_playerId);
		player.setPos(newPos);
		assertEquals(newPos, player.getPos());
	}

}
