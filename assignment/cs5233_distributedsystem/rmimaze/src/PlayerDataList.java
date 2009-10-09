/**
 * Handle communciation with client
 * 
 * @author Edison
 */

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerDataList {
	/**
	 *  Internal class: Data class for player Data
	 */
	public class PlayerData {

		public PlayerData(String name, MazeNotifyInterface notify, int playerId) {
			m_name 		= name;
			m_notify 	= notify;
			m_playerId 	= playerId;
		}

		public int getId() {
			return m_playerId;
		}
		
		public String getName() {
			return m_name;
		}

		public MazeNotifyInterface getNotify() {
			return m_notify;
		}

		// field
		private int m_playerId;
		private MazeNotifyInterface m_notify;
		private String m_name;
	}

	// ctor
	public PlayerDataList() {
		m_playerDataList = new HashMap<Object, PlayerData>();
	}
	
	/**
	 * (sync protected)
	 * @param playerId
	 * @param name
	 * @param callback
	 * @throws MazeServerException
	 */
	public synchronized void addPlayer(int playerId, String name, MazeNotifyInterface callback) throws MazeServerException {
		if(m_playerDataList.containsKey(playerId) == true)
			throw new MazeServerException("Player already exist");

		PlayerData playerData = new PlayerData(name, callback, playerId);
		m_playerDataList.put(playerId, playerData);
	}
	
	/**
	 * (sync protected)
	 * @param playerId
	 * @throws MazeServerException
	 */
	public synchronized void removePlayer(int playerId) throws MazeServerException {
		if(m_playerDataList.containsKey(playerId) == false)
			throw new MazeServerException("Player not exist");
		m_playerDataList.remove(playerId);
	}
	
	/**
	 * (sync protected)
	 */
	public synchronized void removeAll() {
		m_playerDataList.clear();
	}

	/**
	 * getNotify for unicast (sync protected)
	 * @param playerId
	 * @return
	 * @throws MazeServerException
	 */
	public MazeNotifyInterface getNotify(int playerId) throws MazeServerException {
		MazeNotifyInterface notify = null;
		if(m_playerDataList.containsKey(playerId) == false)
			throw new MazeServerException("Player not exist");
		PlayerData playerData = m_playerDataList.get(playerId);
		notify = playerData.getNotify();
		return notify;
	}

	// boardcast (sync protected)
	public synchronized void broadcastJoinSuccessNotify(String msg, Server.BroadcastFailHandler handler) {
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().joinSuccessNotify(msg);
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_JOIN] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
		
	public synchronized void broadcastQuitNotify(String msg, Server.BroadcastFailHandler handler) {
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().quitNotify(msg);
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_QUIT] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
	
	public synchronized void broadcastGameStartNotify(Server.BroadcastFailHandler handler){
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().gameStartNotify();
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_GAME_START] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
	
	public synchronized void broadcastGameEndNotify(Server.BroadcastFailHandler handler){
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().gameEndNotify();
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_GAME_END] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
	
	public synchronized void broadcastCheckAlive(Server.BroadcastFailHandler handler) {
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().checkAlive();
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_CHECK_ALIVE] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
	
	public synchronized void broadcastSynchronizeMaze(MazeData mazeData, Server.BroadcastFailHandler handler) {
		PlayerData player = null;
		try {
			this.resetItr();
			while ( (player=this.getNextPlayer()) != null ) {
				player.getNotify().synchronizeMaze(mazeData);
			}
		} catch (RemoteException e) {
			System.err.println("[BROADCAST_SYNC] errMsg:"+e.getMessage());
			if(player != null)
				handler.run(player.getId());
		}
	}
	
	// getter
	public int getSize() {
		return m_playerDataList.size();
	}
	
	// helper
	private synchronized void resetItr() {
		m_itr = m_playerDataList.entrySet().iterator();
	}
	private synchronized PlayerData getNextPlayer() {
		if(m_itr.hasNext() == false)
			return null;
		Map.Entry pairs = (Map.Entry)m_itr.next();
		PlayerDataList.PlayerData playerData = (PlayerDataList.PlayerData) pairs.getValue();
		return playerData;
	}
	// field
	private Map<Object, PlayerData> m_playerDataList;
	private Iterator m_itr;
}