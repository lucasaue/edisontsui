/**
 * Simple Unique ID generator
 * 
 * @author Edison
 *
 */

public class IdGenerator {
	static private int m_count = 0;
	
	static public synchronized int getNewId() {
		return m_count++;
	}
}
