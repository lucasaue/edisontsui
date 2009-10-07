


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientListener extends UnicastRemoteObject implements MazeNotifyInterface {

	protected ClientListener() throws RemoteException {
		super();
	}
	
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
		m_isTerminated = true;
	}

	
	@Override
	public void checkAlive() throws RemoteException {
		System.out.println("[CheckAlive]");

	}

	@Override
	public void gameEndNotify() throws RemoteException {
		System.out.println("GameEnd");
		m_isTerminated = true;
	}

	@Override
	public void gameStartNotify() throws RemoteException {
		System.out.println("GameStart");

	}

	@Override
	public void synchronizeMaze() throws RemoteException {
		System.out.println("Sync");

	}

	public boolean isTerminated() {
		return m_isTerminated;
	}
	
	private boolean m_isTerminated = false;
}