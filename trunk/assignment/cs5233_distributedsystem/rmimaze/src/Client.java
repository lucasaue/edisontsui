/**
 * Maze Game client main class
 * i)  locate RMIregistry host
 * ii) Text Input (TextInput.java) /output (ClientListener.java)
 * 
 * Usage: java Client hostname
 * 
 * @author Edison (edisontsui@gmail.com)
 */


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
	
	// main
	public static void main(String[] args) {
		Client client 	= new Client(args);
		TextInput input = new TextInput(client);
		input.run();
		
		System.exit(0);
	}
	// ctor
	public Client(String[] args) {
		String host = (args.length < 1)? null : args[0];
        try {
        	Registry registry = LocateRegistry.getRegistry(host);
            Remote remoteObject = registry.lookup("edisontsui_rmimazegame");

			if (remoteObject instanceof MazeGameInterface) {
				m_gameServer = (MazeGameInterface)remoteObject ;
				m_listener = new ClientListener();
			} else {
				System.err.println("Maze Game Server NOT found");
				System.exit(0);
			}
        }
        catch(RemoteException e) {
            System.err.println("Remote:RMI Lookup Exception (Cannot locate RMIRegistry Server)");
            System.exit(0);        	
        }
        catch(Exception e){
            System.err.println("Fail to connect to server: "+e.getMessage());
            System.exit(0);
        };    
	}
	
	public ClientListener getListener() {
		return m_listener;
	}
	public MazeGameInterface getGameServer() {
		return m_gameServer;
	}
	public void setPlayerId(int playerId) {
		m_playerId = playerId;
		if(m_listener != null)
			m_listener.setPlayerId(playerId);
	}
	public int getPlayerId() {
		return m_playerId;
	}
	
	// fields
	private ClientListener m_listener 		= null;
	private MazeGameInterface m_gameServer	= null;
	private int m_playerId 					= -1;
}
