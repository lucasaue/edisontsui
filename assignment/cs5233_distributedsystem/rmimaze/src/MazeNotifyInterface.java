
import java.rmi.*;

public interface MazeNotifyInterface {

	public void joinNotify(String msg) throws RemoteException;
	
	public void moveNotify(String msg) throws RemoteException;
	
	public void quitNotify(String msg) throws RemoteException;
	
	public void gameStartNotify() throws RemoteException;
	
	public void gameEndNotify() throws RemoteException;
	
	// What if client fail? exception? timeout?
	public void checkAlive() throws RemoteException;
	
	public void synchronizeMaze() throws RemoteException;
	
	
}
