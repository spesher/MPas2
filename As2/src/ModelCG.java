import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilog.concert.IloException;
import ilog.concert.IloModel;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

/**
 * The cplex model for the Cutting Stock model
 * @author Peter de Voogd and Reinier van Uden
 *
 */

public class ModelCG{
	private final List<Pattern> patterns;
	private final List<Piece> pieces;
	private IloCplex cplex;
	private final int ROD_LENGTH;
	// variables
	private Map<Pattern,IloNumVar> x;
	// map for the constraints
	private Map<Piece, IloRange> constraints;
	// objective expression
	private IloObjective objective;
	
	public ModelCG(List<Pattern> patterns, List<Piece> pieces) throws IloException
	{
		this.patterns = patterns;
		this.pieces = pieces;
		System.out.println(patterns.size() + ", " + pieces.size());
		cplex = new IloCplex();
		x = new HashMap<Pattern,IloNumVar>();
		constraints = new HashMap<Piece, IloRange>();
		
		ROD_LENGTH = 400;
		
		addVariables();
		addObjective();
		addDoPiecesConstraints();
		cplex.exportModel("modelCG.lp");
		cplex.setOut(null);
	}
	
	/**
	 * Solve the model
	 * @throws IloException
	 */
	public void solve() throws IloException
	{
		cplex.solve();
	}
	
	public Map<Piece, Double> getDuals() throws UnknownObjectException, IloException {
		Map<Piece, Double> duals = new HashMap<Piece,Double>();
		// retrieve the value for each constraint
		for (Piece p : pieces) {
			double val = cplex.getDual(constraints.get(p));
			duals.put(p, val);
		}
		return duals;
	}
	
	/**
	 * Solve the LP relaxation with column generation, for a given number of iterations.
	 * @throws IloException
	 */
	public void solveLPColGen(int iterations) throws IloException
	{
		// initialize the patterns 
		List<Pattern> curPatterns = new ArrayList<Pattern>(patterns);
		Map<Piece,Double> duals = new HashMap<Piece,Double>();
		// run the algorithm for each iteration
		for (int i=0; i<iterations; i++) {
			// solve the restricted master problem
			this.solve();
			System.out.println("Iteration " + i + ": " + this.getObjective());
			// obtain the dual variables
			duals = this.getDuals();
			// build model for the pricing problem
//			KnapsackModel pricing = new KnapsackModel(ROD_LENGTH, duals, pieces);
			// TODO: toevoegen dat je uit de loop stapt als reduced cost niet negatief is.
			// adjust the model such that the new pattern is included
			Pattern newPattern = null;		// TODO: wordt de pattern uit het knapsackmodel
			addVariable(newPattern);
			changeDoPiecesConstraints(newPattern.getPieces());		// change the constraints for the pieces in the new pattern
			regenerateObjective();
		}
		
	}
	
	/**
	 * Add the vars to the model
	 * @throws IloException
	 */
	private void addVariables() throws IloException
	{
		int i = 1;
		for (Pattern r: patterns)
		{
			// add the pattern variable y
			IloNumVar var = cplex.numVar(0,1,"x"+i);
			x.put(r, var);			
			i++;
		}
	}
	
	/**
	 * Add the vars for the given patterns
	 * @throws IloException
	 */
	private void addVariable(Pattern p) throws IloException
	{
		int i = x.keySet().size() + 1;		// start counting from the last index
		IloNumVar var = cplex.numVar(0,1,"x"+i);
		x.put(p, var);			
	}
	
	/**
	 * Add the objective to the model
	 * @throws IloException
	 */
	private void addObjective() throws IloException
	{
		IloNumExpr obj = cplex.constant(0);
		// add the vars corresponding to the patterns (y_k)
		for (Pattern r : patterns) { 
			{
				 obj = cplex.sum(obj, x.get(r));	
			}
		}
		objective = cplex.addMinimize(obj);
	}
	
	/**
	 * Regenerate the objective expression for the model. Necessary if new vars added.
	 * @throws IloException
	 */
	private void regenerateObjective() throws IloException {
		cplex.remove(objective);
		IloNumExpr obj = cplex.constant(0);
		// add the vars corresponding to the patterns (y_k): any new patterns are included now
		for (Pattern r : patterns) { 
			{
				 obj = cplex.sum(obj, x.get(r));	
			}
		}
		objective = cplex.addMinimize(obj);
	}
	
	/**
	 * Adds the constraints that make sure each Piece is cut. Save the constraints in a map
	 * @throws IloException
	 */
	private void addDoPiecesConstraints() throws IloException
	{
		// add a constraint for each piece
		for (Piece p : pieces) {
			IloNumExpr lhs = cplex.constant(0);
			// sum over the patterns
			for (Pattern r : patterns) {
				if (r.getPieces().contains(p))
				{
					lhs = cplex.sum(lhs, x.get(r));					
				}
			}
			// add the constraint: rhs=1 because pieces of the same length are uniquely defined
			IloRange currentConstr = cplex.addGe(lhs, 1,"cover"+p.getIndex());
			// add to the map
			constraints.put(p, currentConstr);
		}
	}
	
	private void changeDoPiecesConstraints(List<Piece> changes) throws IloException {
		for (Piece p : changes) {
			IloRange current = constraints.get(p);
			cplex.remove(current);
			// build the new constraint
			IloNumExpr lhs = cplex.constant(0);
			// sum over the patterns: the new pattern is included now
			for (Pattern r : patterns) {
				if (r.getPieces().contains(p))
				{
					lhs = cplex.sum(lhs, x.get(r));					
				}
			}
			// add the constraint: rhs=1 because pieces of the same length are uniquely defined
			IloRange currentConstr = cplex.addGe(lhs, 1,"cover"+p.getIndex());		
			// add to the map
			constraints.put(p, currentConstr);
		}
	}


	// methods to retrieve information about the solution
	/**
	 * Return the objective value, as a double
	 * @return
	 * @throws IloException
	 */
	public double getObjective() throws IloException {
		return cplex.getObjValue();
	}
	
	/**
	 * Returns a map from the used patterns to the pieces which are cut from this pattern.
	 * @return
	 * @throws IloException 
	 * @throws UnknownObjectException 
	 */
	public List<Pattern> getPatterns() throws UnknownObjectException, IloException {
		List<Pattern> result = new ArrayList<Pattern>();
		for (Pattern r : patterns) {
			double val = cplex.getValue(x.get(r));
			if (val > 0.01) {
				// put the used patterns in a list				
				result.add(r);
			}
		}
		return result;
	}
}
