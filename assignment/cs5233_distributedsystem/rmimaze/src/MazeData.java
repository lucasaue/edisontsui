/**
 * Maze Data class - serializable
 * 
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MazeData implements Serializable {
	// Data class that hold maze element
	public class MazeElement implements Serializable{
		private static final long serialVersionUID = 3589012288839147280L;
		// ctor
		MazeElement(int numTreasure) {
			m_leftTreasure = numTreasure;
		}

		// getter are not protected by synchronization
		public int getTreasure() {
			return m_leftTreasure;
		}
		
		public boolean isOccupied() {
			return m_isOccupied;
		}

		// setter
		/**
		 * (sync protected)
		 * @param deltaNumTreasure
		 */
		public synchronized void addTreasure(int deltaNumTreasure) {
			m_leftTreasure += deltaNumTreasure;
		}
		
		/**
		 * enter (sync protected)
		 * i) 	when player enter this element, treasure will be clean and returned
		 * ii)	isOccupied is set to make sure other player cant enter
		 * @return
		 * @throws MazeServerException
		 */
		public synchronized int enter() throws MazeServerException {
			if(isOccupied() == true)
				throw new MazeServerException("Maze Element is occupied");
			m_isOccupied = true;
			int earnTreasure = m_leftTreasure;
			m_leftTreasure = 0;
			return earnTreasure;
		}
		
		/**
		 * leave (sync protected)
		 * @throws MazeServerException
		 */
		public synchronized void leave() throws MazeServerException {
			if(isOccupied() == false)
				throw new MazeServerException("Maze Element is not occupied");
			m_isOccupied = false;
		}
		
		// field
		private boolean m_isOccupied	= false;
		private int m_leftTreasure		= 0;
	}

	public class Player implements Serializable {
		private static final long serialVersionUID = 5003213998378077197L;
		
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
		/**
		 * addEarnTreasure (sync protected)
		 * @param deltaEarnTreasure
		 */
		public synchronized void addEarnTreasure(int deltaEarnTreasure) {
			m_earnTreasure += deltaEarnTreasure;
		}
		public int getPos() {
			return m_pos;
		}
		/**
		 * setpos (sync protected)
		 * @param pos
		 */
		public synchronized void setPos(int pos) {
			m_pos = pos;
		}

		// field
		private int m_id 			= -1;
		private int m_earnTreasure 	= 0;
		private int m_pos 			= -1;
	};

	private static final long serialVersionUID = 2775035690205995307L;

	// ctor
	public MazeData(int size, int numTreasure) {
		m_mazeSize 		= size;
		m_totalTreasure = numTreasure;
		m_leftTreasure 	= m_totalTreasure;	
		
		m_playerList = new HashMap<Object, Player>();	// Player info, <playerid, player>
		m_mazeEleList = new HashMap<Object, MazeElement>();	// All the maze ele, <eleId, ele>, eleId(i,j)=j*m_mazeSize + i;
	
		// setup maze
		for(int y=0;y<m_mazeSize;++y) {
			for(int x=0;x<m_mazeSize;++x) {
				MazeElement mazeEle = new MazeElement(0);
				m_mazeEleList.put(y*m_mazeSize+x, mazeEle);
			}
		}
		// add treasure
		for(int index=0;index<m_totalTreasure;++index) {
			int randMazeEleIndex = (int)(Math.random()* (m_mazeEleList.size()-1));
			MazeElement tmpMazeEle = m_mazeEleList.get(randMazeEleIndex);
			tmpMazeEle.addTreasure(1);
		}

	}

	// getter
	public MazeElement getMazeElement(int pos) throws MazeServerException {
		if(m_mazeEleList.containsKey(pos) == false)
			throw new MazeServerException("[GET_MAZE_ELEMENT] Maze Element not found");
		return m_mazeEleList.get(pos);
	}

	public Player getPlayer(int playerId) throws MazeServerException {
		if(m_playerList.containsKey(playerId) == false)
			throw new MazeServerException("[GET_PLAYER] Player not exist");
		return m_playerList.get(playerId);
	}
	
	public void resetPlayerItr() {
		m_playerItr = m_playerList.entrySet().iterator();

	}
	
	public Player getNextPlayer() {
		if(!m_playerItr.hasNext())
			return null;
		Map.Entry pairs = (Map.Entry)m_playerItr.next();
		Player player = (MazeData.Player) pairs.getValue();
		return player;
	}
	
	public int getMazeSize() {
		return m_mazeSize;
	}
	
	public int getLeftTreasure() {
		return m_leftTreasure;
	}
	
	public int getTotalTreasure() {
		return m_totalTreasure;
	}
	
	public int getNumPlayer(){
		return m_playerList.size();
	}
	
	public MazeStatus getMazeStatus() {
		return m_status;
	}

	/**
	 * (sync protected)
	 * @param earnTreasure
	 */
	public synchronized void addLeftTreasure(int earnTreasure) {
		m_leftTreasure += earnTreasure;
	}

	/**
	 * (sync protected)
	 * @param playerId
	 * @param defaultPos
	 * @return
	 * @throws MazeServerException
	 */
	public synchronized Player addPlayer(int playerId, int defaultPos) throws MazeServerException {
		if(m_playerList.containsKey(playerId) == true)
			throw new MazeServerException("[ADD_PLAYER] Player exist");
			
		Player player = new Player(playerId);
		player.setPos(defaultPos);
		// add to list
		m_playerList.put(player.getId(), player);
		return player;
	}
	/**
	 * (sync protected)
	 * @param playerId
	 */
	public synchronized void removePlayer(int playerId) {
		m_playerList.remove(playerId);
	}
	/**
	 * (sync protected)
	 * @param status
	 */
	public synchronized void setMazeStatus(MazeStatus status) {
		m_status = status;
	}
	
	// field
	private int m_mazeSize;						// maze size
	private int m_totalTreasure;				// total num of treasure
	private int m_leftTreasure;					// num of treasure left
	private Map<Object, Player> m_playerList;	// Player info, <playerid, player>
	private Map<Object, MazeElement> m_mazeEleList;	// All the maze ele, <eleId, ele>, eleId(i,j)=j*m_mazeSize + i;

	private MazeStatus m_status			= MazeStatus.MAZE_WAITING;	// status
	private Iterator m_playerItr;
}