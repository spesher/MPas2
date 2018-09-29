import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilog.concert.IloException;
import ilog.concert.IloModel;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

/**
 * The cplex model for the Cutting Stock model
 * @author Peter de Voogd and Reinier van Uden
 *
 */

public class Model2{
	private final List<Pattern> patterns;
	private final List<Piece> pieces;
	private IloCplex cplex;
	// variables
	private Map<Pattern,IloNumVar> x;
	
	public Model2(List<Pattern> patterns, List<Piece> pieces) throws IloException
	{
		this.patterns = patterns;
		this.pieces = pieces;
		
		cplex = new IloCplex();
		x = new HashMap<Pattern,IloNumVar>();
		
		addVariables();
		addObjective();
		addDoPiecesConstraints();
		cplex.exportModel("model2.lp");
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

 	
	public void solveLP() throws IloException
	{
		for (Pattern r: patterns)
		{
			// get the pattern variable from y and add LP relaxation
			cplex.add(cplex.conversion(x.get(r),IloNumVarType.Float));
		}
		cplex.solve();
		
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
			IloNumVar var = cplex.intVar(0,1,"x"+i);
			x.put(r, var);			
			i++;
		}
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
		cplex.addMinimize(obj);
	}
	
	/**
	 * Adds the constraints that make sure each Piece is cut
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
			cplex.addEq(lhs, 1,"cover"+p.getIndex());
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
