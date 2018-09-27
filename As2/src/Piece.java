/**
 * Models the piece in the Cutting Stock problem
 * @author Peter de Voogd and Reinier van Uden
 *
 */
public class Piece {
	
	private final int LENGTH;
	private final int INDEX;
	
	/**
	 * Constructor
	 * @param index
	 * @param length
	 */
	public Piece(int index, int length) {
		this.LENGTH = length;
		this.INDEX = index;
	}
	
	// getters
	public int getIndex() {
		return this.INDEX;
	}
	
	public int getLength() {
		return this.LENGTH;
	}
	
	// toString
	public String toString() {
		return "(" + this.INDEX + "," + this.LENGTH +  ")";
	}

}
