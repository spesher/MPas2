import java.util.List;

/**
 * Pattern object for the Cutting Stock problem. Contains a list which pieces are in this pattern.
 * @author Peter de Voogd and Reinier van Uden
 *
 */
public class Pattern {
	
	private List<Piece> pieces;
	private int index;
	
	// constructor
	public Pattern(int index, List<Piece> pieces) {
		this.index = index;
		this.pieces = pieces;
	}
	
	// getters
	public List<Piece> getPieces() {
		return this.pieces;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int totalLength() {
		int result = 0;
		for (Piece p : pieces) {
			result = result + p.getLength();
		}
		return result;
	}
	
	// print method
	public String toString() {
		String result = index + "[";
		for (Piece p : pieces) {
			result = result + p.getLength() + ",";
		}
		result = result + "]";
		return result;
	}
}
