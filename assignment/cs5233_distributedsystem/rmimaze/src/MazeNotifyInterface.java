/**
 * Interface for MazeGame client
 * 
 * @author Edison(edisontsui@gmail.com)
 */
import java.rmi.*;

public interface MazeNotifyInterface extends Remote {

	public void joinSuccessNotify(String msg) throws RemoteException;
	public void joinFailNotify(String msg) throws RemoteException;
		
	public void moveSuccessNotify(String msg) throws RemoteException;
	public void moveFailNotify(String msg) throws RemoteException;
	
	public void quitNotify(String msg) throws RemoteException;
	
	public void gameStartNotify() throws RemoteException;
	
	public void gameEndNotify() throws RemoteException;
	
	public void checkAlive() throws RemoteException;
	
	public void synchronizeMaze(MazeData mazeData) throws RemoteException;
	
	
}
