import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Model{
	private final List<Rod> rods;
	private final List<Piece> pieces;
	private IloCplex cplex;
	// variables
	private Map<Rod,Map<Piece,IloNumVar>> x;
	private Map<Rod,IloNumVar> y;
	private final int ROD_LENGTH;

	public Model(List<Rod> rods, List<Piece> pieces) throws IloException
	{
		this.rods = rods;
		this.pieces = pieces;
		
		cplex = new IloCplex();
		x = new HashMap<Rod,Map<Piece,IloNumVar>>();
		y = new HashMap<Rod,IloNumVar>();
		ROD_LENGTH = pieces.get(0).getLength();
		
		addVariables();
		addObjective();
		addDoPiecesConstraints();
		addLengthConstraints();		// TODO
		cplex.exportModel("model.lp");
	}
	
	/**
	 * Solve the model
	 * @throws IloException
	 */
	public void solve() throws IloException
	{
		cplex.solve();
	}
	
	/**
	 * Add the vars to the model
	 * @throws IloException
	 */
	private void addVariables() throws IloException
	{
		for (Rod r: rods)
		{
			// add the rod variable y
			IloNumVar var = cplex.boolVar("Rod");
			y.put(r, var);
			// add a variable for each piece, for this rod (x_ik)
			for (Piece p : pieces) {
				IloNumVar var2 = cplex.intVar(0, pieces.size(), "" + p.getIndex());
				x.get(r).put(p, var2);
			}
		}
	}
	
	/**
	 * Add the objective to the model
	 * @throws IloException
	 */
	private void addObjective() throws IloException
	{
		IloNumExpr obj = cplex.constant(0);
		// add the vars corresponding to the rods (y_k)
		for (Rod r : rods) { 
			{
				 obj = cplex.sum(obj, y.get(r));	
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
			// sum over the rods
			for (Rod r : rods) {
				lhs = cplex.sum(lhs, x.get(r).get(p));
			}
			// add the constraint: rhs=1 because pieces of the same length are uniquely defined
			cplex.addEq(lhs, 1);
		}
	}
	
	/**
	 * Adds the constraints that make sure the lengths of the rods are respected and the x and y vars are connected
	 * @throws IloException
	 */
	private void addLengthConstraints() throws IloException 
	{
		// add a constraint for each rod
		for (Rod r : rods) {
			IloNumExpr lhs = cplex.constant(0);
			// nu loopen over de pieces en het gewicht meetellen
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
	
}
