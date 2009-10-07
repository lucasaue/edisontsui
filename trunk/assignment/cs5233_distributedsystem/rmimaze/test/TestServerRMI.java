import java.rmi.Naming;
import java.rmi.Remote;

import junit.framework.TestCase;


public class TestServerRMI extends TestCase {

	public void testServer() {
		ClientListener m_listener;
		MazeGameInterface m_gameServer;
        try {
            Remote remoteObject = Naming.lookup("rmimazegame");

			if (remoteObject instanceof MazeGameInterface) {
				m_gameServer = (MazeGameInterface)remoteObject ;
				m_listener = new ClientListener();

				int playerId =m_gameServer.join(m_listener, "Edison");
				
				assertEquals(0, playerId);
			} else {
				System.err.println("Server not a Maze Game Server.");
				System.exit(0);
			}
        }
        catch(Exception e){
            System.err.println("RMI Lookup Exception");
            fail("");
            System.exit(0);
            
        }; 
	}

}
