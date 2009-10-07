package com.googlecode.edisontsui;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
		//TODO: inform all player
	}
	
	public void endGame() {
		//TODO:	inform all player
	}
	
	public void checkAlive() {
		//TODO: check all player (thread)
	}
	
	@Override
	public synchronized int join(MazeNotifyInterface notify, String name) throws RemoteException {
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
	
}
