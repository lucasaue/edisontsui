/**
 * Maze 
 * i)  Handle Player Join/Leave
 * ii) Handle Game logic e.g. move, earn Treasure
 * 
 * @author Edison (edisontsui@gmail.com)
 */

import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;

public class Maze{

	// ctor	
	public Maze(int size, int numTreasure){	
		m_mazeData 	= new MazeData(size, numTreasure);
	}
	
	/**
	 * endGameCallback will be executed after endCallbackWaitMS when game terminate
	 * @param endGameCallback	- func to be executed 
	 * @param endCallbackWaitMS	- time waited after game terminate
	 */
	public void setEndGameCallback(TimerTask endGameCallback, int endCallbackWaitMS) {
		m_endGameCallback	= endGameCallback;
		m_endCallbackWaitMS = endCallbackWaitMS;
	}
		
	// getter
	public MazeStatus getStatus() {
		return m_mazeData.getMazeStatus();
	}

	public int getLeftTreasure() {
		return m_mazeData.getLeftTreasure();
	}
	
	public int getMazeSize() {
		return m_mazeData.getMazeSize();
	}
	
	public MazeData getData() {
		return m_mazeData;
	}
	
	/**
	 * Start Maze
	 * @throws MazeServerException
	 */
	public void start() throws MazeServerException {
		if(this.getStatus() != MazeStatus.MAZE_WAITING)
			throw new MazeServerException("Maze cannot be started");
		m_mazeData.setMazeStatus(MazeStatus.MAZE_STARTED);
	}
	
	/**
	 * End Maze
	 * @throws MazeServerException
	 */
	public void end() throws MazeServerException {
		System.out.println("[END] ending...");
		if(this.getStatus() != MazeStatus.MAZE_STARTED)
			System.out.println("[END] Anormality, end status:"+m_mazeData.getMazeStatus());
		m_mazeData.setMazeStatus(MazeStatus.MAZE_END);
	
		if(m_endGameCallback != null) {
			Timer timer = new Timer();
			timer.schedule(m_endGameCallback, m_endCallbackWaitMS);
		}
	}
	
	/**
	 * Add Player and assign player a position
	 * @param playerId
	 * @param defaultPos
	 * @throws MazeServerException
	 */
	public void addPlayer(int playerId, int defaultPos) throws MazeServerException {
		if(this.getStatus() != MazeStatus.MAZE_WAITING)
			throw new MazeServerException("Maze is closed for join");

		// add to list
		MazeData.Player player = m_mazeData.addPlayer(playerId, defaultPos);
		// assign location
		int randPos = player.getPos();
		boolean genNewPos = (randPos==-1)?true:false;
		boolean isPosAssigned = false;
		int numTry = 0;
		
		int mazeTotalSize = m_mazeData.getMazeSize()^2;
		do {
			try {
				if(genNewPos == true)
					randPos = (int)Math.floor(Math.random()*mazeTotalSize);

				enterMazeElement(player.getId(), randPos);
				genNewPos = true;
				isPosAssigned = true;
				numTry++;
			} catch (MazeServerException e) {
				// position is not assigned
				if(numTry >= m_maxPlayerInitTry)
					throw new MazeServerException("too many try");
			}
		} while (isPosAssigned == false);
	}
	
	/**
	 * Remove Player, cleanup position in maze
	 * @param playerId
	 * @throws MazeServerException
	 */
	public void removePlayer(int playerId) throws MazeServerException {
		int pos = m_mazeData.getPlayer(playerId).getPos();
		this.leaveMazeElement(pos);
		m_mazeData.removePlayer(playerId);
	}
		
	/**
	 * Move a player in the maze
	 * @param playerId
	 * @param direction
	 * @throws MazeServerException
	 */
	public void move(int playerId, EnumDirection direction) throws MazeServerException {
		if(this.getStatus() != MazeStatus.MAZE_STARTED)
			throw new MazeServerException("Maze is not started yet");

		int oriPos = m_mazeData.getPlayer(playerId).getPos();
		int newPos = this.getMazeElementId(oriPos, direction);
	
		// enter new maze element
		this.enterMazeElement(playerId, newPos);			
		// leave old maze element
		m_mazeData.getMazeElement(oriPos).leave();
	
		System.out.println("[MOVE] id:"+playerId+", last:"+oriPos+", new:"+newPos);
	}
	
	/**
	 * Return new MazeElementId after moving direction
	 * @param pos
	 * @param direction
	 * @return
	 * @throws MazeServerException
	 */
	protected int getMazeElementId(int pos, EnumDirection direction) throws MazeServerException {
		int mazeSize= m_mazeData.getMazeSize();
		int posX 	= pos % mazeSize;
		int posY 	= (int) Math.floor(pos/mazeSize);
		
		if( (posX<0 || posX>=mazeSize) || (posY<0 || posY>=mazeSize) )
			throw new MazeServerException("Input Position out of bound");
		
		switch(direction) {
		case EAST: 	posX += 1; break;
		case WEST: 	posX -= 1; break;
		case NORTH:	posY -= 1; break;
		case SOUTH: posY += 1; break;
		case NOMOVE: break;
		default: 
		}

		if( (posX<0 || posX>=mazeSize) || (posY<0 || posY>=mazeSize) )
			throw new MazeServerException("New Position out of bound");

		return posY * mazeSize + posX;
	}
	
	/**
	 *  enterMazeElement (sync protected)
	 * @param playerId
	 * @param newPos
	 * @throws MazeServerException
	 */
	protected synchronized void enterMazeElement(int playerId, int newPos) throws MazeServerException {
		// Check if new pos is occupied, if not,update
		MazeData.Player tmpPlayer = m_mazeData.getPlayer(playerId);
		if(tmpPlayer == null)
			throw new MazeServerException("Player not exist");
		// Check if maze element occupied
		int earnTreasure = m_mazeData.getMazeElement(newPos).enter();

		// Success, update player
		tmpPlayer.addEarnTreasure(earnTreasure);
		tmpPlayer.setPos(newPos);
		// Success, update total treasure left
		m_mazeData.addLeftTreasure(-earnTreasure);		
		System.out.println("Treasure Left:"+m_mazeData.getLeftTreasure());
		// Check EndGame condition (leftTreasure <= 0)
		if(m_mazeData.getLeftTreasure() <= 0){
			this.end();
		}
	}
	
	/**
	 * leaveMazeElement (sync protected)
	 * @param pos
	 * @throws MazeServerException
	 */
	protected synchronized void leaveMazeElement(int pos) throws MazeServerException {
		m_mazeData.getMazeElement(pos).leave();
	}

	// field
	MazeData m_mazeData 				= null;
	private int m_maxPlayerInitTry		= 20;		// max num of try for getting a new pos when join
	private int m_endCallbackWaitMS		= 50;
	private TimerTask m_endGameCallback	= null; 
}
