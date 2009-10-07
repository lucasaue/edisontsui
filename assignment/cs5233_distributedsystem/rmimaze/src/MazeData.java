import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class MazeData implements Serializable {
	// Data class that hold maze elemenet
	public class MazeElement implements Serializable{
		private static final long serialVersionUID = 3589012288839147280L;
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

		public int getTreasure() {
			return m_leftTreasure;
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
		
	};

	private static final long serialVersionUID = 2775035690205995307L;

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
	
	public boolean isMazeElementOccupied(int pos) {
		return m_mazeEleList.get(pos).isOccupied();
	}
	public int enquireMazeElementTreasure(int pos) {
		return m_mazeEleList.get(pos).getTreasure();
	}
	public int getMazeSize() {
		return m_mazeSize;
	}
	public int getLeftTreasure() {
		return m_leftTreasure;
	}
	public Player addPlayer(int playerId, int defaultPos) {
		Player player = new Player(playerId);
		player.setPos(defaultPos);
		// add to list
		m_playerList.put(player.getId(), player);
		return player;
	}

	public int m_mazeSize;						// maze size
	public int m_totalTreasure;				// total num of treasure
	public int m_leftTreasure;					// num of treasure left
	public Map<Object, Player> m_playerList;	// Player info, <playerid, player>
	public Map<Object, MazeElement> m_mazeEleList;	// All the maze ele, <eleId, ele>, eleId(i,j)=j*m_mazeSize + i;

	public MazeStatus m_status			= MazeStatus.MAZE_WAITING;	// status
}