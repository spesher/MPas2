import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
	
	public static void main(String[] args)
	{
		List<Piece> allPieces = readFile(new File("small.txt"));
		System.out.println(allPieces);
	}

	private static List<Piece> readFile(File file) {
		List<Piece> allPieces = new ArrayList<Piece>();
		try
		{
			Scanner s = new Scanner(file);
			while (s.hasNextLine())
			{
				int index = s.nextInt();
				int length = s.nextInt();
				Piece p = new Piece(index, length);
				allPieces.add(p);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return allPieces;
	}
	
}
