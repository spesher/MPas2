import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main 
{
	
	public static void main(String[] args)
	{
		//main method here
		final int NR_PIECES = readFile(new File("small.txt"));
		System.out.println(NR_PIECES);
	}

	private static int readFile(File file) {
		int nrPieces = 0;
		try
		{
			Scanner s = new Scanner(file);
			while (s.hasNextLine())
			{
				int index = s.nextInt();
				int length = s.nextInt();
				Piece p = new Piece(index, length);
				nrPieces++;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return nrPieces;
	}
	
}
