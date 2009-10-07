

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.lang.Math;

// Maze has to store
// 1) Maze properties: 
//		i) 	size
//		ii) treasure left
// 2) Player list (pos, treasure obtained)

public class Maze{

	// ctor	
	public Maze(int size, int numTreasure){	
		m_mazeData 	= new MazeData(size, numTreasure);
	}
	
	public void setEndGameCallback(Runnable endGameCallback) {
		m_endGameCallback	= endGameCallback;
	}
		
	// getter
	public MazeStatus getStatus() {
		return m_mazeData.m_status;
	}
	
	public int getNumPlayer() {
		return m_mazeData.m_playerList.size();
	}

	public int getLeftTreasure() {
		return m_mazeData.m_leftTreasure;
	}
	
	public int getMazeSize() {
		return m_mazeData.m_mazeSize;
	}
	
	public MazeData getData() {
		return m_mazeData;
	}
	
	// method
	public void start() throws MazeServerException {
		if(this.getStatus() != MazeStatus.MAZE_WAITING)
			throw new MazeServerException("Maze cannot be started");
		m_mazeData.m_status = MazeStatus.MAZE_STARTED;
	}
	
	public void end() throws MazeServerException {
		System.out.println("[END] ending...");
		if(this.getStatus() != MazeStatus.MAZE_STARTED)
			throw new MazeServerException("Maze cannot be ended");
		m_mazeData.m_status = MazeStatus.MAZE_END;
	
		if(m_endGameCallback != null) {
			System.out.println("[END] entering callback");
			m_endGameCallback.run();
			System.out.println("[END] callback done");			
		}
	}
	
	public synchronized void addPlayer(int playerId, int defaultPos) throws MazeServerException {
		if(this.getPlayer(playerId) != null)
			throw new MazeServerException("Player exist");
		if(this.getStatus() != MazeStatus.MAZE_WAITING)
			throw new MazeServerException("Maze is closed for join");

		// add to list
		MazeData.Player player = m_mazeData.addPlayer(playerId, defaultPos);
		// assign location
		int randPos = player.getPos();
		boolean init = true;
		boolean isPosAssigned = false;
		int numTry = 0;
		
		int mazeTotalSize = m_mazeData.m_mazeSize*m_mazeData.m_mazeSize;
		do {
			try {
				if(randPos == -1 || init == false)
				{
					randPos = (int)Math.floor(Math.random()*mazeTotalSize);
					init = false;
				}
				enterMazeElement(player.getId(), randPos);
				isPosAssigned = true;
				numTry++;
			} catch (MazeServerException e) {
				// pos is not assigned
				if(numTry >= m_maxPlayerInitTry)
				{
					isPosAssigned = true;
					throw new MazeServerException("too many try");
				}
			}
		} while (isPosAssigned == false);
	}
	
	public synchronized void removePlayer(int playerId) throws MazeServerException {
		if(this.getPlayer(playerId) == null)
			throw new MazeServerException("Player not exist");
		
		int pos=0;
		pos = this.getPlayerPos(playerId);
		this.leaveMazeElement(pos);
		m_mazeData.m_playerList.remove(playerId);
	}
	
	// wrapper
	public void removePlayer(MazeData.Player player) throws MazeServerException {
		this.removePlayer(player.getId());
	}
	
	// method
	public void move(int playerId, EnumDirection direction) throws MazeServerException {
		if(this.getStatus() != MazeStatus.MAZE_STARTED)
			throw new MazeServerException("Maze is not started yet");

		int oriPos = this.getPlayerPos(playerId);
		int newPos = this.getMazeElementId(oriPos, direction);
	
		// enter new maze element
		this.enterMazeElement(playerId, newPos);			
		// leave old maze element
		m_mazeData.m_mazeEleList.get(oriPos).leave();
	
		System.out.println("[MOVE] id:"+playerId+", last:"+oriPos+", new:"+newPos);
	}
	
	// helper
	public int getMazeElementId(int pos, EnumDirection direction) throws MazeServerException {
		int posX 	= pos % m_mazeData.m_mazeSize;
		int posY 	= (int) Math.floor(pos/m_mazeData.m_mazeSize);
		
		if( (posX<0 || posX>=m_mazeData.m_mazeSize) || (posY<0 || posY>=m_mazeData.m_mazeSize) )
			throw new MazeServerException("Input Position out of bound");
		
		switch(direction) {
		case EAST: 	posX += 1; break;
		case WEST: 	posX -= 1; break;
		case NORTH:	posY -= 1; break;
		case SOUTH: posY += 1; break;
		case NOMOVE: break;
		default: 
		}

		if( (posX<0 || posX>=m_mazeData.m_mazeSize) || (posY<0 || posY>=m_mazeData.m_mazeSize) )
			throw new MazeServerException("New Position out of bound");

		return posY * m_mazeData.m_mazeSize + posX;
	}
	
	// return pos
	public int getPlayerPos(int playerId) throws MazeServerException {
		if(m_mazeData.m_playerList.containsKey(playerId) == false)
			throw new MazeServerException("Player not exist");
		
		MazeData.Player tmpPlayer = m_mazeData.m_playerList.get(playerId);
		return tmpPlayer.getPos();
	}
	
	public MazeData.Player getPlayer(int playerId) {
		if(m_mazeData.m_playerList.containsKey(playerId) == false)
			return null;
		return m_mazeData.m_playerList.get(playerId);
	}
	
	// only 1 thread can access to maze Element
	private void enterMazeElement(int playerId, int newPos) throws MazeServerException {
		// Check if new pos is occupied, if not,update
		// get lock (sync block)
		getLock();
		try {
			MazeData.Player tmpPlayer = this.getPlayer(playerId);
			if(tmpPlayer == null)
				throw new MazeServerException("Player not exist");
			// Check if maze element occupied
			int earnTreasure = m_mazeData.m_mazeEleList.get(newPos).enter();
			// Success, update total treasure left
			m_mazeData.m_leftTreasure -= earnTreasure;		
			System.out.println("Treasure Left:"+m_mazeData.m_leftTreasure);
			if(m_mazeData.m_leftTreasure <= 0){
				// End game
				this.end();
			}
			// update player
			tmpPlayer.addEarnTreasure(earnTreasure);
			tmpPlayer.setPos(newPos);
		} finally {
			// release lock
			releaseLock();			
		}
	}
	
	public void leaveMazeElement(int pos) throws MazeServerException {
		if(m_mazeData.m_mazeEleList.containsKey(pos) == false)
			throw new MazeServerException("Maze element not exist");
		m_mazeData.m_mazeEleList.get(pos).leave();
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
	MazeData m_mazeData 				= null;
	public int m_counter				= 0;
	public int m_maxPlayerInitTry		= 20;		// max num of try for getting a new pos when join
	
	private Runnable m_endGameCallback	= null; 
}
