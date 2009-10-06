import java.io.Console;
import java.util.Arrays;
import java.io.IOException;

public class MyConsole {
	public static void main(String[] args)throws IOException 
	{
		Console c = System.console();
		if( c == null)
		{
			System.err.println("no console.");
			System.exit(1);
		}
		
		String login = c.readLine("Enter your Login: ");
		char [] oldPassword = c.readPassword("Enter your old password: ");
		
		if(verify(login, oldPassword))
		{
			boolean noMatch;
			do {
				char [] newPassword1 = c.readPassword("Enter your new password: ");
				char [] newPassword2 = c.readPassword("Enter your new password: ");
				noMatch = ! Arrays.equals(newPassword1, newPassword2);
				if(noMatch) {
					c.format("Passwords not match. Try again. %n");
				} else {
					change(login, newPassword1);
					c.format("Password for %s changed. %n", login);
				}
				Arrays.fill(newPassword1, ' ');
				Arrays.fill(newPassword2, ' ');
			} while(noMatch);
		}
		
		Arrays.fill(oldPassword, ' ');
	}

	private static void change(String login, char[] newPassword1) {
		// TODO Auto-generated method stub
		return;
	}

	private static boolean verify(String login, char[] oldPassword) {
		// TODO Auto-generated method stub
		return true;
	}

}
