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
		for (Rod a: rods)
		{
					
		}
	}
	
	/**
	 * Add the objective to the model
	 * @throws IloException
	 */
	private void addObjective() throws IloException
	{
		IloNumExpr obj = cplex.constant(0);
		// loop over the rods to add the connection cost and fixed cost
		for (Rod a : rods) {
			if (x.containsKey(a)) 
			{
				
			}
		}
		cplex.addMinimize(obj);
	}
	
	/**
	 * Adds the constraints that make sure each Piece is performed, either by a small or large bus
	 * @throws IloException
	 */
	private void addDoPiecesConstraints() throws IloException
	{
		
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