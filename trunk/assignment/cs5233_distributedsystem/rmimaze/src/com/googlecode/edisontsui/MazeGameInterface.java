package com.googlecode.edisontsui;
import java.rmi.*;

public interface MazeGameInterface extends Remote {

	// return 1 for success, else for error no 
	public int join(MazeNotifyInterface notify, String username) throws RemoteException;
	
	public void move(String username, EnumDirection direction) throws RemoteException;
	
	public void quit(MazeNotifyInterface notify, String username) throws RemoteException;

}
