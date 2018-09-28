import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
		
		// create all possible patterns
		List<Pattern> patterns = createFeasiblePatterns(allPieces, ROD_LENGTH);
		System.out.println(patterns.size()); // zelfde als Pim
	}
	
	/**
	 * Creates a list of all feasible patterns, for the given list of pieces.
	 * @return
	 */
	private static List<Pattern> createFeasiblePatterns(List<Piece> pieces, int MAX_LENGTH) {
		List<Pattern> result = new ArrayList<Pattern>();
		Queue<Pattern> q1 = new LinkedList<Pattern>();
		Queue<Pattern> q2 = new LinkedList<Pattern>();
		// initialize q1 with an empty pattern and a pattern with the first piece
		Pattern p1 = new Pattern(1,new ArrayList<Piece>());
		List<Piece> listP2 = new ArrayList<Piece>();
		listP2.add(pieces.get(0));
		Pattern p2 = new Pattern(2,listP2);
		q1.add(p1);
		q1.add(p2);
		int counter = 2;			// count the number of patterns: needed for the index
		// Now generate all possible patterns
		for (int i=1; i<pieces.size(); i++) {
			// look which queue is empty
			Queue<Pattern> empty = null;
			Queue<Pattern> full = null;
			if (q1.isEmpty()) {
				empty = q1;
				full = q2;
			} else {
				empty = q2;
				full = q1;
			}
			// retrieve the patterns from the full queue. Generate new ones and add the old ones
			while (!full.isEmpty()) {
				counter++;
				Pattern current = full.poll();
				List<Piece> currentPieces = current.getPieces();
				empty.add(current);
				// generate the new pattern
				List<Piece> newPatternList = new ArrayList<Piece>();
				newPatternList.addAll(currentPieces);
				newPatternList.add(pieces.get(i));
				Pattern newPattern = new Pattern(counter, newPatternList);
				// check if this pattern is feasible
				if (newPattern.totalLength() <= MAX_LENGTH) {
					empty.add(newPattern);
				} else {
					counter--; // adjust the counter
				}
			}
		}
		// determine which queue is full now and store in result
		if (!q1.isEmpty()) {result.addAll(q1); }
		else {result.addAll(q2); }
		return result;
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
