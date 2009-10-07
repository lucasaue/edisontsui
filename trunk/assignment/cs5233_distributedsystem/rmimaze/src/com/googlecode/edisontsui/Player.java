package com.googlecode.edisontsui;

public class Player {
	
	// ctor
	public Player(int id) {
		m_id = id;
	}
	
	public int getId() {
		return m_id;
	}
	public int getEarnTreasure() {
		return m_earnTreasure;
	}
	public void addEarnTreasure(int deltaEarnTreasure) {
		m_earnTreasure += deltaEarnTreasure;
	}
	public int getPos() {
		return m_pos;
	}
	public void setPos(int pos) {
		m_pos = pos;
	}

	// field
	private int m_id 			= -1;
	private int m_earnTreasure 	= 0;
	private int m_pos 			= -1;
	
}
