

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends UnicastRemoteObject implements MazeGameInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5908230830047850207L;


	private final class StartGame extends  TimerTask {
		private Server m_server;
		public StartGame(Server server) {
			m_server = server;
		}
		@Override
		public void run() {
			m_server.startGame();
		}
	}
	private final class EndGame implements Runnable {
		private Server m_server;
		public EndGame(Server server) {
			m_server = server;
		}
		@Override
		public void run() {
			m_server.endGame();
		}
	}	
	private final class CheckAlive extends TimerTask {
		private Server m_server;
		public CheckAlive(Server server) {
			m_server = server;
		}
		@Override
		public void run() {
			m_server.checkAlive();
		}
	}
	
	// ctor
	public Server(int mazeSize, int totalTreasure, int serverWaitMS) throws RemoteException {
		m_mazeSize 		= mazeSize;
		m_totalTreasure = totalTreasure;
		m_serverWaitMS	= serverWaitMS;
		m_playerList 	= new PlayerDataList();
	}
	
	public void setCheckAlivePeriod(int checkAlivePeriodMS) {
		m_checkAlivePeriod = checkAlivePeriodMS;
	}
	
	public synchronized void createMaze() {
		if(m_maze != null)
			return;
		System.out.println("[CreateMaze] creating maze....");
		// create new maze and wait for serverWaitMs sec
		m_maze = new Maze(m_mazeSize, m_totalTreasure);
		m_maze.setEndGameCallback(new EndGame(this));
		System.out.println("[CreateMaze] done. adding timer...");
		if(m_timer == null)
			m_timer = new Timer();
		m_timer.schedule(new StartGame(this), m_serverWaitMS);
		m_timer.scheduleAtFixedRate(new CheckAlive(this), m_serverWaitMS, m_checkAlivePeriod);
		System.out.println("[CreateMaze] timer ready");
	}
	
	public void startGame() {
		System.out.println("[StartGame] starting...");

		try {
			m_maze.start();
		} catch (MazeServerException e) {
			System.err.println("[StartGame] errMsg:"+e.getError());
		}
		
		//inform all player
		m_playerList.getLock();
		try {
		   Iterator itr = m_playerList.getPlayerDataList().entrySet().iterator();
		   while (itr.hasNext()) {
			   Map.Entry pairs = (Map.Entry)itr.next();
			   PlayerDataList.PlayerData playerData = (PlayerDataList.PlayerData) pairs.getValue();
			   MazeNotifyInterface notify = playerData.getNotify();
			   notify.gameStartNotify();
		   }
		} catch (RemoteException e) {
			System.err.println("[StartGame] errMsg:"+e.getMessage());
		} finally {
			m_playerList.releaseLock();
		}
		
		System.out.println("[StartGame] Success");
	}
	
	public void endGame() {
		System.out.println("[ServerEndGame]");
		//inform all player
		m_playerList.getLock();
		try {
		   Iterator itr = m_playerList.getPlayerDataList().entrySet().iterator();
		   while (itr.hasNext()) {
			   Map.Entry pairs = (Map.Entry)itr.next();
			   PlayerDataList.PlayerData playerData = (PlayerDataList.PlayerData) pairs.getValue();
			   MazeNotifyInterface notify = playerData.getNotify();
			   notify.gameEndNotify();
		   }
		} catch (RemoteException e) {
			System.err.println("[EndGame] errMsg:"+e.getMessage());
		} finally {
			m_playerList.releaseLock();
		}
		m_maze = null;
		m_playerList.removeAll();
	}
	
	public void checkAlive() {
		//check all player (thread)
		//inform all player
		m_playerList.getLock();
		int playerId = -1;
		PlayerDataList.PlayerData playerData;
		try {
		   Iterator itr = m_playerList.getPlayerDataList().entrySet().iterator();
		   while (itr.hasNext()) {
			   Map.Entry pairs 	= (Map.Entry)itr.next();
			   playerId 		= (Integer) pairs.getKey();
			   playerData 		= (PlayerDataList.PlayerData) pairs.getValue();
			   MazeNotifyInterface notify = playerData.getNotify();
			   notify.checkAlive();
		   }
		} catch (RemoteException e) {
			System.err.println("[CheckAlive] errMsg:"+e.getMessage());
			//remove
			if(playerId != -1)
				this.removePlayer(playerId);
		} catch (Exception e) {
			System.err.println("[CheckAlive] errMsg:"+e.getMessage());
		} finally {
			m_playerList.releaseLock();
		}	
	}
	
	public int join(MazeNotifyInterface notify, String name) throws RemoteException {
		System.out.println("[JOIN] name:"+name);
		
		createMaze();
		// get unique id
		int playerId = IdGenerator.getNewId();

		try {
			m_playerList.addPlayer(playerId, name, notify);
			m_maze.addPlayer(playerId, -1);
			notify.joinSuccessNotify("Join Success!! Please wait for the game start");		
		} catch (MazeServerException e) {
			notify.joinFailNotify(e.getError());
			System.err.println("[Join Err] id:"+playerId+", ErrMsg:"+e.getError()+", cleaning up...");
			// cleanup
			this.removePlayer(playerId);
		}
		return playerId;
	}

	public void quit(int playerId) throws RemoteException {
		System.out.println("[QUIT] id:"+playerId);
		try {
			MazeNotifyInterface notify = m_playerList.getNotify(playerId);
			notify.quitNotify("Bye. Thx for playing, see you next time!");
			this.removePlayer(playerId);
		} catch (MazeServerException e) {
			System.err.println("[Quit Err] id:" + playerId + ", ErrMsg:" + e.getError());
		}
	}

	
	public void move(int playerId, EnumDirection direction) throws RemoteException {
		System.out.println("[MOVE] id:"+playerId+" dir:"+direction);
		MazeNotifyInterface notify = null;
		try {
			notify = m_playerList.getNotify(playerId);
			m_maze.move(playerId, direction);
			notify.moveSuccessNotify("Move Suceess");
			MazeData data = m_maze.getData();
			notify.synchronizeMaze(data);
		} catch (MazeServerException e) {
			if(notify != null)
				notify.moveFailNotify(e.getError());
			System.err.println("[Move Err] id:"+playerId+", ErrMsg:"+e.getError());
		}
	}

	public void removePlayer(int playerId) {
		try {
			m_playerList.removePlayer(playerId);
			m_maze.removePlayer(playerId);
			if(m_playerList.getSize() == 0) {
				endGame();
			}
			System.out.println("[RemovePlayer] success id:"+playerId);
		} catch (MazeServerException e) {
			System.err.println("[RemovePlayer] id:"+playerId+", errMsg:"+e.getError());
		}
	}
	
	// field
	private int m_checkAlivePeriod 	= 5000;			// 5s
	private int m_serverWaitMS		= 10000;		// 10s
	private int m_mazeSize 			= 0;
	private int m_totalTreasure		= 0;
	private Maze m_maze 			= null;
	private PlayerDataList m_playerList = null;
	
	private Timer m_timer			= null;	


    public static void main(String[] args) {
    	try {
    		if(args.length < 3)
    		{
    			System.err.println("Usage: java server <mazeSize> <totTreasure> <waitMS>");
    			System.exit(1);
    		}
    		int mazeSize 	= Integer.parseInt(args[0]);
    		int totTreasure = Integer.parseInt(args[1]);
    		int waitMS 		= Integer.parseInt(args[2]);
    		Server server = new Server(mazeSize, totTreasure, waitMS);

    		if(args.length >= 4)
    			server.setCheckAlivePeriod(Integer.parseInt(args[3]));
    		
    		Naming.rebind("rmimazegame", server);
    		
    		System.out.println("GameServer started");

    	}
    	catch (java.net.MalformedURLException e) {
    		System.err.println("Malformed URL for MessageServer name "
    				+ e.toString());
    	}
    	catch (RemoteException e) {
    		System.err.println("Communication error " + e.toString());
		}
    }

}
