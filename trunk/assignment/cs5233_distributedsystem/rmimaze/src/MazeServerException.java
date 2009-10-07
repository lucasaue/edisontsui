

public class MazeServerException extends Exception {
	// ctor
	public MazeServerException() {
		super();
		m_cusErrMsg = "Unknown";
	}
	
	public MazeServerException(String errMsg) {
		super();
		m_cusErrMsg = errMsg;
	}
	
	 public String getError() {
		 return m_cusErrMsg;
	 }

	
	// field
	private String m_cusErrMsg;
}
