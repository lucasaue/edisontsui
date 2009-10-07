

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListener extends UnicastRemoteObject implements MazeNotifyInterface {
	private static final long serialVersionUID = -7204060028020671154L;

	protected ClientListener() throws RemoteException {
	}
	
	public void joinSuccessNotify(String msg) throws RemoteException {
		System.out.println(msg);
	}
	public void joinFailNotify(String msg) throws RemoteException {
		System.out.println(msg);
		m_isTerminated = true;
	}

	public void moveSuccessNotify(String msg) throws RemoteException {
		System.out.println(msg);
	}
	public void moveFailNotify(String msg) throws RemoteException {
		System.out.println(msg);
	}

	public void quitNotify(String msg) throws RemoteException {
		System.out.println(msg);
		m_isTerminated = true;
	}

	// To keep track if client is lost by catching RemoteException
	public void checkAlive() throws RemoteException {
		//System.out.println("[CheckAlive]");
	}

	public void gameEndNotify() throws RemoteException {
		System.out.println("GameEnd");
		m_isTerminated = true;
	}

	public void gameStartNotify() throws RemoteException {
		System.out.println("GameStart");

	}

	public void synchronizeMaze(MazeData mazeData) throws RemoteException {
		System.out.println("Sync");
		m_mazeData = mazeData;
		System.out.println("mazesize:"+m_mazeData.getMazeSize());
		printMaze();
	}

	public boolean isTerminated() {
		return m_isTerminated;
	}
	// helper
	private void printMaze() {
		if(m_mazeData == null)
			return;
		System.out.println("Treasure Left:"+ m_mazeData.m_leftTreasure);
		int size = m_mazeData.getMazeSize();
		for(int y=0;y<size;++y) {
			for(int x=0;x<size;++x) {
				int pos = y*size+x;
				System.out.print("|");
				if(m_mazeData.isMazeElementOccupied(pos) == true) {
					System.out.print("X");
				} else if (m_mazeData.enquireMazeElementTreasure(pos)!= 0){
					System.out.print("$");
				} else {
					System.out.print(" ");
				}
			}
			System.out.print("|\n");
		}
	}
	
	private boolean m_isTerminated = false;
	private MazeData m_mazeData = null;
}
