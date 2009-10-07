package com.googlecode.edisontsui;

import java.rmi.RemoteException;

public class ClientListener implements MazeNotifyInterface {

	@Override
	public void joinNotify(String msg) throws RemoteException {
		System.out.println(msg);

	}

	@Override
	public void moveNotify(String msg) throws RemoteException {
		System.out.println(msg);

	}

	@Override
	public void quitNotify(String msg) throws RemoteException {
		System.out.println(msg);

	}

	
	@Override
	public void checkAlive() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameEndNotify() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStartNotify() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void synchronizeMaze() throws RemoteException {
		// TODO Auto-generated method stub

	}

}
