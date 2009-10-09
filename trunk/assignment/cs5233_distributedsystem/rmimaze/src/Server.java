/**
 * main server class
 * 
 * @author Edison
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends UnicastRemoteObject implements MazeGameInterface {
	private static final long serialVersionUID = -5908230830047850207L;

	/**
	 * Timer Task class as callback for start game
	 */
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
	/**
	 * Timer Task class as callback for end game
	 */
	private final class EndGame extends  TimerTask {
		private Server m_server;
		public EndGame(Server server) {
			m_server = server;
		}
		@Override
		public void run() {
			m_server.endGame();
		}
	}	
	/** 
	 * Timer Task class as callback checking client every m_checkAlivePeriod ms
	 */
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
	
	public final class BroadcastFailHandler {
		private Server m_server;
		public BroadcastFailHandler(Server server) {
			m_server = server;
		}
		public void run(int playerId) {
			m_server.removePlayer(playerId);
		}
	}
	
	// ctor
	public Server(int mazeSize, int totalTreasure, int serverWaitMS) throws RemoteException {
		m_mazeSize 		= mazeSize;
		m_totalTreasure = totalTreasure;
		m_serverWaitMS	= serverWaitMS;
		m_playerList 	= new PlayerDataList();
		m_fail			= new BroadcastFailHandler(this);
	}
	
	public void setCheckAlivePeriod(int checkAlivePeriodMS) {
		m_checkAlivePeriod = checkAlivePeriodMS;
	}

	/**
	 * createMaze (sync protected - as new Maze & Timer)
	 */
	public synchronized void createMaze() {
		if(m_maze != null)
			return;
		System.out.println("[CreateMaze] creating maze....");
		// create new maze and wait for serverWaitMs sec
		m_maze = new Maze(m_mazeSize, m_totalTreasure);
		m_maze.setEndGameCallback(new EndGame(this), m_mazeTerminateWaitMS);
		System.out.println("[CreateMaze] done. adding timer...");
		if(m_timer == null)
			m_timer = new Timer();
		m_timer.schedule(new StartGame(this), m_serverWaitMS);
		m_timer.scheduleAtFixedRate(new CheckAlive(this), m_serverWaitMS, m_checkAlivePeriod);
		System.out.println("[CreateMaze] timer ready");
	}
	
	public synchronized void startGame() {
		try {
			System.out.println("[StartGame] starting...");
			m_maze.start();
			//inform all player
			m_playerList.broadcastGameStartNotify(m_fail);
			m_playerList.broadcastSynchronizeMaze(m_maze.getData(), m_fail);
			System.out.println("[StartGame] Success");
		} catch (MazeServerException e) {
			System.err.println("[StartGame] errMsg:"+e.getError());
		} catch (Exception e) {
			System.err.println("[StartGame] Anormality: "+e.getMessage()+"Possible cause: All player quit before maze start");
		}
	}
	
	public synchronized void endGame() {
		System.out.println("[ServerEndGame]");
		//inform all player
		m_playerList.broadcastGameEndNotify(m_fail);
		// cleanup
		m_maze = null;
		m_playerList.removeAll();
	}
	
	public void checkAlive() {
		//inform all player
		m_playerList.broadcastCheckAlive(m_fail);
	}
	
	// critical region are all protected so no need to be sync 
	public int join(MazeNotifyInterface notify, String name) throws RemoteException {
		System.out.println("[JOIN] name:"+name);
		
		createMaze();
		// get unique id
		int playerId = IdGenerator.getNewId();

		try {
			m_playerList.addPlayer(playerId, name, notify);
			m_maze.addPlayer(playerId, -1);
			notify.joinSuccessNotify("Join Success!! Please wait for " + m_serverWaitMS/1000+ "sec for game start");		
		} catch (MazeServerException e) {
			notify.joinFailNotify(e.getError());
			System.err.println("[Join Err] id:"+playerId+", ErrMsg:"+e.getError()+", cleaning up...");
			// cleanup
			this.removePlayer(playerId);
		}
		return playerId;
	}

	// critical region are all protected so no need to be sync 
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

	/**
	 * (sync protected) prevent sync mismatch mazeData
	 */
	public synchronized void move(int playerId, EnumDirection direction) throws RemoteException {
		System.out.println("[MOVE] id:"+playerId+" dir:"+direction);
		MazeNotifyInterface notify = null;
		try {
			notify = m_playerList.getNotify(playerId);
			m_maze.move(playerId, direction);
			notify.moveSuccessNotify("[Player_" + playerId + "] Move Suceess");
			MazeData data = m_maze.getData();
			//inform all player
			m_playerList.broadcastSynchronizeMaze(m_maze.getData(), m_fail);
		} catch (MazeServerException e) {
			if(notify != null)
				notify.moveFailNotify("[Player_" + playerId + "] Move Fail:"+e.getError());
			System.err.println("[Move Err] id:"+playerId+", ErrMsg:"+e.getError());
		}
	}

	// critical region are all protected so no need to be sync 
	// playerList is removed 1st to prevent updating non-exist client
	public synchronized void removePlayer(int playerId) {
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
	private int m_mazeTerminateWaitMS	= 50;			// 0.01s
	private int m_checkAlivePeriod 		= 5000;			// 5s
	private int m_serverWaitMS			= 10000;		// 10s

	private int m_mazeSize 				= 0;
	private int m_totalTreasure			= 0;
	
	private Maze m_maze 				= null;
	private PlayerDataList m_playerList = null;
	
	private Timer m_timer				= null;	
	private BroadcastFailHandler m_fail	= null;

	/**
	 * main func
	 * @param args
	 */
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
    		
    		
    		LocateRegistry.getRegistry().bind("edisontsui_rmimazegame", server);
    		
    		System.out.println("GameServer started");

    	} catch (RemoteException e) {
    		System.err.println("Communication error " + e.toString());
		} catch (Exception e) {
    		System.err.println("Communication error " + e.toString());
		}
    }

}
