/**
 * ClientListener
 * i) 	Handle server callback
 * ii)	Display to standard out
 * 
 * Print format is sth like
 * | | | | |
 * |X| |$| |
 * | | | | |
 * where x denote player and $ denote treasure 
 * 
 * @author Edison (edisontsui@gmail.com)
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;

public class ClientListener extends UnicastRemoteObject implements MazeNotifyInterface {
	private static final long serialVersionUID = -7204060028020671154L;

	// ctor
	public ClientListener() throws RemoteException {
	}
	
	// Method inherited from MazeNotifyInterface
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
		int winnerId = -1;
		int winnerScore =-1;
		System.out.println("========================================");
		System.out.println("=======++++++++ GAME END +++++++========");
		try {
			m_mazeData.resetPlayerItr();
			MazeData.Player player = null;
			while( (player=m_mazeData.getNextPlayer()) != null)
			{
				if(player.getId() == m_playerId)
					System.out.print("Me-");
				
				System.out.println("[Player_"+player.getId()+"] treasure:"+player.getEarnTreasure());
				if(player.getEarnTreasure() > winnerScore) {
					winnerId = player.getId();
					winnerScore = player.getEarnTreasure();
				}
			}
		} catch (Exception e) {
		} finally { 
			System.out.println("****************************************");
			System.out.println("WINNER: player_"+winnerId+" Score: "+winnerScore);
			System.out.println("****************************************");
			System.out.println("Press enter to continue~ See you next time");
			m_isTerminated = true;
		}
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

	// Helper function
	// Check if Game is terminated
	public boolean isTerminated() {
		return m_isTerminated;
	}
	
	// Print Maze - take m_maze and print info to standard out
	private void printMaze() {
		if(m_mazeData == null)
			return;
		System.out.println("Treasure Left:"+ m_mazeData.getLeftTreasure());

		try {
			m_mazeData.resetPlayerItr();
			MazeData.Player player = null;
			while ( (player=m_mazeData.getNextPlayer()) != null ) {
				System.out.println("[Player_"+player.getId()+"] treasure:"+player.getEarnTreasure());
			}

			System.out.println("X -player, $ -treasure");

			int size = m_mazeData.getMazeSize();
			for(int y=0;y<size;++y) {
				for(int x=0;x<size;++x) {
					int pos = y*size+x;
					System.out.print("|");
					if(m_mazeData.getMazeElement(pos).isOccupied() == true) {
						System.out.print("X");
					} else if (m_mazeData.getMazeElement(pos).getTreasure() != 0){
						System.out.print("$");
					} else {
						System.out.print(" ");
					}
				}
				System.out.print("|\n");
			}
		} catch (MazeServerException e) {
			System.err.println("[PRINT_MAZE] err:"+e.getError());
		} 
	}
	
	public void setPlayerId(int playerId) {
		m_playerId = playerId;
	}
	
	// field
	private int m_playerId 			= -1;
	private boolean m_isTerminated 	= false;
	private MazeData m_mazeData 	= null;
}
