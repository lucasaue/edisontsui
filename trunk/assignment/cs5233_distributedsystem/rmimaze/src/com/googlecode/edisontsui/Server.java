package com.googlecode.edisontsui;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Server extends UnicastRemoteObject implements MazeGameInterface {
	
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
	
	public synchronized void createMaze() {
		if(m_maze != null)
			return;
		// create new maze and wait for serverWaitMs sec
		m_maze = new Maze(m_mazeSize, m_totalTreasure);
		m_maze.setEndGameCallback(new EndGame(this));
		if(m_timer == null)
			m_timer = new Timer();
		m_timer.schedule(new StartGame(this), m_serverWaitMS);
		m_timer.scheduleAtFixedRate(new CheckAlive(this), m_serverWaitMS, m_checkAlivePeriod);
	}
	
	public void startGame() {
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
	}
	
	public void endGame() {
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
			try {
				if(playerId == -1) {
					m_maze.removePlayer(playerId);
					m_playerList.removePlayer(playerId);
				}
			} catch (MazeServerException e1) {
				System.err.println("[CheckAlive] errMsg:"+e1.getMessage());
			}
		} catch (Exception e) {
			System.err.println("[CheckAlive] errMsg:"+e.getMessage());
		} finally {
			m_playerList.releaseLock();
		}	
	}
	
	@Override
	public synchronized int join(MazeNotifyInterface notify, String name) throws RemoteException {
		System.out.println("[JOIN] name:"+name);
		createMaze();
		// get unique id
		int playerId = IdGenerator.getNewId();

		try {
			m_playerList.addPlayer(playerId, name, notify);
			m_maze.addPlayer(playerId, -1);
		} catch (MazeServerException e) {
			notify.joinNotify(e.getError());
			System.err.println("[Join Err] id:"+playerId+", ErrMsg:"+e.getError());
		}
		notify.joinNotify("Join Success!! Please wait for the game start");		
		return playerId;
	}

	@Override
	public void quit(int playerId) throws RemoteException {
		System.out.println("[QUIT] id:"+playerId);
		try {
			MazeNotifyInterface notify = m_playerList.getNotify(playerId);
			notify.quitNotify("Bye. Thx for playing, see you next time!");
			m_playerList.removePlayer(playerId);
			m_maze.removePlayer(playerId);
		} catch (MazeServerException e) {
			System.err.println("[Quit Err] id:" + playerId + ", ErrMsg:" + e.getError());
		}
	}

	
	@Override
	public void move(int playerId, EnumDirection direction) throws RemoteException {
		System.out.println("[MOVE] id:"+playerId+" dir:"+direction);
		MazeNotifyInterface notify = null;
		try {
			notify = m_playerList.getNotify(playerId);
			m_maze.move(playerId, direction);
		} catch (MazeServerException e) {
			if(notify != null)
				notify.moveNotify(e.getError());
			System.err.println("[Move Err] id:"+playerId+", ErrMsg:"+e.getError());
		}
		if(notify != null)
			notify.moveNotify("Move Suceess");
	}

	
	// field
	private int m_checkAlivePeriod 	= 1000;			// 1s
	private int m_serverWaitMS		= 10000;		// 10s
	private int m_mazeSize 			=0;
	private int m_totalTreasure		= 0;
	private Maze m_maze 			= null;
	private PlayerDataList m_playerList = null;
	
	private Timer m_timer			= null;	


    public static void main(String[] args) {
    	try {
    		if(args.length != 3)
    		{
    			System.err.println("Usage: java server <mazeSize> <totTreasure> <waitMS>");
    			System.exit(1);
    		}
    		int mazeSize 	= Integer.parseInt(args[0]);
    		int totTreasure = Integer.parseInt(args[1]);
    		int waitMS 		= Integer.parseInt(args[2]);
    		Server server = new Server(mazeSize, totTreasure, waitMS);

    		Naming.rebind("rmimazegame", server);

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
