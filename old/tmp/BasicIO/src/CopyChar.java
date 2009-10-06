import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import java.util.Scanner;

public class CopyChar {
	public static void testmain(String[] args) throws IOException
	{
		System.out.format("hello edison, copying: %s, to: %s.\n", args[0], args[1]);
		
		BufferedReader in = null;
		PrintWriter out = null;
		Scanner s = null;
		
		try
		{
			in = new BufferedReader(new FileReader(args[0]));
			out = new PrintWriter(new FileWriter(args[1]));
			s = new Scanner(in);
			s.useDelimiter(",\\s*");
			
			String l;
			/*while ((l=in.readLine()) != null)
			{
				out.println(l);
			}*/
			while (s.hasNext())
			{
				System.out.println(s.next());
			}
		}
		finally
		{
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(s != null)
				s.close();
		}
	}
}
