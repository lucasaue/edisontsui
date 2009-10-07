package com.googlecode.edisontsui;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataList {
	// Internal class: Data class for player Data
	public class PlayerData {

		public PlayerData(String name, MazeNotifyInterface notify) {
			m_name 		= name;
			m_notify 	= notify;
		}

		public String getName() {
			return m_name;
		}

		public MazeNotifyInterface getNotify() {
			return m_notify;
		}

		// field
		private MazeNotifyInterface m_notify;
		private String m_name;
	}
	
	public PlayerDataList() {
		m_playerDataList = new HashMap<Object, PlayerData>();
	}
	
	public void addPlayer(int playerId, String name, MazeNotifyInterface callback) throws MazeServerException {
		// sync block
		getLock();
		try {
			if(m_playerDataList.containsKey(playerId) == true)
				throw new MazeServerException("Player already exist");

			PlayerData playerData = new PlayerData(name, callback);
			m_playerDataList.put(playerId, playerData);
		} finally {
			releaseLock();
		}
	}
	
	public void removePlayer(int playerId) throws MazeServerException {
		// sync block
		getLock();
		try {
			if(m_playerDataList.containsKey(playerId) == false)
				throw new MazeServerException("Player not exist");
			m_playerDataList.remove(playerId);
		} finally {
			releaseLock();
		}
	}
	
	public void removeAll() {
		m_playerDataList.clear();
	}
	
	public MazeNotifyInterface getNotify(int playerId) throws MazeServerException {
		MazeNotifyInterface notify = null;
		// sync block
		getLock();
		try {
			if(m_playerDataList.containsKey(playerId) == false)
				throw new MazeServerException("Player not exist");
			PlayerData playerData = m_playerDataList.get(playerId);
			notify = playerData.getNotify();
		} finally {
			releaseLock();
		}
		return notify;
	}
	
	public Map<Object, PlayerData> getPlayerDataList() {
		return m_playerDataList;
	}
	// mutex
	public synchronized void getLock() {
		m_counter++;
		notifyAll();
	}
	public synchronized void releaseLock() {
		m_counter--;
		notifyAll();
	}
	
	// field
	private Map<Object, PlayerData> m_playerDataList;
	private int m_counter = 0;
}