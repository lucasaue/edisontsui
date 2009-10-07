package com.googlecode.edisontsui;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

// Maze has to store
// 1) Maze properties: 
//		i) 	size
//		ii) treasure left
// 2) Player list (pos, treasure obtained)

public class Maze {
	
	// ctor
	public Maze(int size, int numTreasure){
		m_mazeSize 		= size;
		m_totalTreasure = numTreasure;
		m_leftTreasure 	= m_totalTreasure;
		
		m_playerList = new HashMap<Object, Player>();	// Player info, <playerid, player>
		m_maze = new HashMap<Object, MazeElement>();	// All the maze ele, <eleId, ele>, eleId(i,j)=j*m_mazeSize + i;
	
		m_leftTreasure 	= m_totalTreasure;
		// setup maze
		for(int x=0;x<m_mazeSize;++x) {
			for(int y=0;y<m_mazeSize;++y) {
				MazeElement mazeEle = new MazeElement(0);
				m_maze.put(y*m_mazeSize+x, mazeEle);
			}
		}
		// add treasure
		for(int index=0;index<m_totalTreasure;++index) {
			int randMazeEleIndex = (int)(Math.random()* (m_maze.size()-1));
			MazeElement tmpMazeEle = m_maze.get(randMazeEleIndex);
			tmpMazeEle.addTreasure(1);
		}
	}
	
	// getter
	public int getNumPlayer() {
		return m_playerList.size();
	}

	public int getLeftTreasure() {
		return m_leftTreasure;
	}
	
	public int getMazeSize() {
		return m_mazeSize;
	}
	
	// method
	public synchronized void addPlayer(Player player) throws MazeServerException {
		if(getPlayer(player.getId()) != null)
			throw new MazeServerException("Player exist");
		
		// add to list
		m_playerList.put(player.getId(), player);
		// assign location
		int randPos = player.getPos();
		boolean init = true;
		boolean isPosAssigned = false;
		do {
			try {
				if(randPos == -1 || init == false)
				{
					randPos = (int)Math.floor(Math.random()*m_mazeSize*m_mazeSize);
					init = false;
				}
				enterMazeElement(player.getId(), randPos);
				isPosAssigned = true;
			} catch (MazeServerException e) {
				// pos is not assigned
			}
		} while (isPosAssigned == false);
	}
	
	public synchronized void removePlayer(int playerId) throws MazeServerException {
		if(getPlayer(playerId) == null)
			throw new MazeServerException("Player not exist");
		
		int pos=0;
		pos = this.getPlayerPos(playerId);
		this.leaveMazeElement(pos);
		m_playerList.remove(playerId);
	}
	
	// wrapper
	public void removePlayer(Player player) throws MazeServerException {
		this.removePlayer(player.getId());
	}
	
	public void move(int playerId, EnumDirection direction) throws MazeServerException {
		int oriPos = this.getPlayerPos(playerId);
		int newPos = this.getMazeElementId(oriPos, direction);

		// enter new maze element
		this.enterMazeElement(playerId, newPos);			
		// leave old maze element
		m_maze.get(oriPos).leave();
	}
	
	// helper
	public int getMazeElementId(int pos, EnumDirection direction) throws MazeServerException {
		int posX 	= pos % m_mazeSize;
		int posY 	= (int) Math.floor(pos/m_mazeSize);
		
		if( (posX<0 || posX>=m_mazeSize) || (posY<0 || posY>=m_mazeSize) )
			throw new MazeServerException("Input Position out of bound");
		
		switch(direction) {
		case EAST: 	posX += 1; break;
		case WEST: 	posX -= 1; break;
		case NORTH:	posY += 1; break;
		case SOUTH: posY -= 1; break;
		case NOMOVE: break;
		default: 
		}

		if( (posX<0 || posX>=m_mazeSize) || (posY<0 || posY>=m_mazeSize) )
			throw new MazeServerException("New Position out of bound");

		return posY * m_mazeSize + posX;
	}
	
	// return pos
	public int getPlayerPos(int playerId) throws MazeServerException {
		if(m_playerList.containsKey(playerId) == false)
			throw new MazeServerException("Player not exist");
		
		Player tmpPlayer = m_playerList.get(playerId);
		return tmpPlayer.getPos();
	}
	
	public Player getPlayer(int playerId) {
		if(m_playerList.containsKey(playerId) == false)
			return null;
		return m_playerList.get(playerId);
	}
	
	// only 1 thread can access to maze Element
	private void enterMazeElement(int playerId, int newPos) throws MazeServerException {
		// Check if new pos is occupied, if not,update
		// get lock (sync block)
		getLock();
		try {
			Player tmpPlayer = this.getPlayer(playerId);
			if(tmpPlayer == null)
				throw new MazeServerException("Player not exist");
			
			// Check if maze element occupied
			int earnTreasure = m_maze.get(newPos).enter();
			// Success, update total treasure left
			m_leftTreasure -= earnTreasure;			
			// update player
			tmpPlayer.addEarnTreasure(earnTreasure);
			tmpPlayer.setPos(newPos);
		} finally {
			// release lock
			releaseLock();			
		}
	}
	
	public void leaveMazeElement(int pos) throws MazeServerException {
		if(m_maze.containsKey(pos) == false)
			throw new MazeServerException("Maze element not exist");
		m_maze.get(pos).leave();
	}

	// mutex method
	private synchronized void getLock() {
		m_counter++;
		notifyAll();		
	}
	
	private synchronized void releaseLock() {
		m_counter--;
		notifyAll();		
	}

	// field
	private int m_mazeSize;						// maze size
	private int m_totalTreasure;				// total num of treasure
	private int m_leftTreasure;					// num of treasure left
	private Map<Object, Player> m_playerList;	// Player info, <playerid, player>
	private Map<Object, MazeElement> m_maze;	// All the maze ele, <eleId, ele>, eleId(i,j)=j*m_mazeSize + i;

	private int m_counter		= 0;
}
