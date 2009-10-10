/**
 * Input 
 * 
 * @author Edison
 */

import java.io.Console;

public class TextInput implements ClientInput {
	public TextInput(Client client) {
		m_console = System.console();
		if( m_console == null ) {
			System.err.println("No console");
			System.exit(1);
		}
		m_client = client;
	}
	
	public void run() {
		try {
			String name = m_console.readLine("Enter your Login name:");
			
			int playerId = m_client.getGameServer().join(m_client.getListener(), name);
			m_client.setPlayerId(playerId);
			
			while(m_client.getListener().isTerminated() == false)
			{
				char c = (char) System.in.read();
				if(m_client.getListener().isTerminated()==true)
					break;

				switch(c) {
				case 72:	// up
				case 'w':
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.NORTH);
					break;
				case 80:	// down
				case 's':
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.SOUTH);
					break;
				case 75:	// left
				case 'a':
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.WEST);
					break;
				case 77:	// right
				case 'd':
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.EAST);
					break;
				default:
				}
			}
		} catch (Exception e) {
			System.err.println("[Input] errMsg:"+e.toString());
		}
	}
	
	private Console m_console	= null;
	private Client 	m_client;
}
