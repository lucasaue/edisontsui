package com.googlecode.edisontsui;

// Data class that hold maze elemenet
public class MazeElement {
	
	// ctor
	MazeElement(int numTreasure) {
		m_leftTreasure = numTreasure;
	}

	public void setTreasure(int numTreasure) {
		m_leftTreasure = numTreasure;
	}

	public void addTreasure(int deltaNumTreasure) {
		m_leftTreasure += deltaNumTreasure;
	}

	
	public boolean isOccupied() {
		return m_isOccupied;
	}
	
	public int enter() throws MazeServerException {
		if(isOccupied() == true)
			throw new MazeServerException("Maze Element is occupied");
		m_isOccupied = true;
		int earnTreasure = m_leftTreasure;
		m_leftTreasure = 0;
		return earnTreasure;
	}
	
	public void leave() throws MazeServerException {
		if(isOccupied() == false)
			throw new MazeServerException("Maze Element is not occupied");
		m_isOccupied = false;
	}
	
	// field
	boolean m_isOccupied	= false;
	int m_leftTreasure		= 0;
}
