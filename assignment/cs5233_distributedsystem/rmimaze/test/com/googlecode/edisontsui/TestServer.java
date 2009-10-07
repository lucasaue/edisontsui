package com.googlecode.edisontsui;

import java.rmi.RemoteException;

import junit.framework.TestCase;

public class TestServer extends TestCase {
	private int m_mazeSize 		= 20;
	private int m_totalTreasure	= 10;
	private int m_waitTimMs		= 10000;	// 10 sec
	
	// test local version
	public void testServer() {
		try {
			Server server = new Server(m_mazeSize, m_totalTreasure, m_waitTimMs);
		} catch (RemoteException e) {
			fail("Exception:" +e.getMessage());
		}
	}

	public void testJoin() {
		try {
			Server server = new Server(m_mazeSize, m_totalTreasure, m_waitTimMs);
			MazeNotifyInterface notify = new ClientListener();
			int id = server.join(notify, "Test1");
		} catch (RemoteException e) {
			fail("Exception:" +e.getMessage());
		}
	}

	public void testMove() {
		fail("Not yet implemented");
	}

	public void testQuit() {
		try {
			Server server = new Server(m_mazeSize, m_totalTreasure, m_waitTimMs);
			MazeNotifyInterface notify = new ClientListener();
			int id = server.join(notify, "Test1");

			server.quit(id);
		} catch (RemoteException e) {
			
		}
	}

}
