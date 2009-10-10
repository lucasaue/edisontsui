
public class RobotInput implements ClientInput {
	public RobotInput(Client client, int respTime) {
		m_client = client;
		m_respTime = respTime;
	}
	
	public void run() {
		try {
			String playerName = "Robot."+Math.floor(Math.random()*100);
			
			int playerId = m_client.getGameServer().join(m_client.getListener(), playerName);
			m_client.setPlayerId(playerId);
			
			while(m_client.getListener().isTerminated() == false)
			{
				int fourDirection = (int) Math.floor(Math.random()*4);

				switch(fourDirection) {
				case 0:	// up
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.NORTH);
					break;
				case 1:	// down
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.SOUTH);
					break;
				case 2:	// left
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.WEST);
					break;
				case 3:	// right
					m_client.getGameServer().move(m_client.getPlayerId(), EnumDirection.EAST);
					break;
				default:
				}
				Thread.sleep(m_respTime); // sleep m_respTime ms
			}
		} catch (Exception e) {
			System.err.println("[ROBOT_Input] errMsg:"+e.toString());
		}
	}
	
	private Client 	m_client;
	private int m_respTime = 0;
}
