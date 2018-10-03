import java.util.List;
import java.util.Map;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class KnapsackModel 
{
	private final int ROD_LENGTH;
	private Map<Piece,Double> duals;
	final List<Piece> pieces;
	private Pattern pattern;
	
	private IloCplex cplex;
	private Map<Piece,IloNumVar> a;
	
	public KnapsackModel(int ROD_LENGTH, Map<Piece, Double> duals, List<Piece> pieces) throws IloException {
		this.ROD_LENGTH = ROD_LENGTH;
		this.duals = duals;
		this.pieces = pieces;
		addVariables();
		addObjective();
		addWeightConstraint();
		cplex.exportModel("knapsack.lp");
	}

	private void addVariables() throws IloException {
		for(Piece p: pieces)
		{
			IloNumVar aVar = cplex.boolVar(p.toString());
			a.put(p, aVar);
		}
	}

	private void addObjective() throws IloException {
		IloNumExpr obj = cplex.constant(0);
		for(Piece p: pieces)
		{
			obj = cplex.sum(obj,a.get(p));
		}
		cplex.addMaximize(obj);
	}

	
	
	
	private void addWeightConstraint() throws IloException {
		IloNumExpr lhs = cplex.constant(0);
		for(Piece p: pieces)
		{
			lhs = cplex.sum(lhs,cplex.prod(a.get(p),p.getLength()));
		}
		cplex.addLe(lhs, ROD_LENGTH);
	}


	public void solve() throws IloException
	{
		cplex.solve();
	}
	
	public Pattern getPattern() {
		return pattern;
	}
	
	
}
