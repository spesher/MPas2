import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ilog.concert.IloException;
import ilog.cplex.IloCplex.UnknownObjectException;

/**
 * Runs the small instance
 * @author Peter de Voogd and Reinier van Uden
 *
 */

public class Main 
{
	
	public static void main(String[] args) throws IloException
	{
		final int ROD_LENGTH = 150;
		List<Piece> allPieces = readFile(new File("small.txt"));
		List<Rod> rods = new ArrayList<Rod>();
		// create a rod for each piece. Then there will be enough for sure
		for (int i = 0; i < allPieces.size(); i++) {
			rods.add(new Rod(ROD_LENGTH));
		}
		// build the model
		Model model = new Model(rods, allPieces);
		// solve
		model.solve();
		// print solution info
		System.out.println("Objective: " + model.getObjective());
		printSolutionInfo(model);
	}
	
	/**
	 * Prints solution information from the model that has been solved.
	 * @throws IloException 
	 * @throws UnknownObjectException 
	 */
	private static void printSolutionInfo(Model model) throws UnknownObjectException, IloException {
		Map<Rod,List<Piece>> result = model.getRods();
		// print the pieces per rod, and the total length used per rod
		int counter = 0;
		for (Rod r : result.keySet()) {
			counter++;
			System.out.print("Rod " + counter + ": ");
			int length = 0;
			for (Piece p : result.get(r)) {
				length = length + p.getLength();
				System.out.print(p + " ");
			}
			System.out.println(" with total length: " + length);
		}
	}
	
	/**
	 * Read the information about the pieces from the given file. Returns a list of Piece objects.
	 * @param file
	 * @return
	 */
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
