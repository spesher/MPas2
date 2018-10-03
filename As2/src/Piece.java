/**
 * Models the piece in the Cutting Stock problem
 * @author Peter de Voogd and Reinier van Uden
 *
 */
public class Piece implements Comparable<Piece> {
	
	private final int LENGTH;
	private final int INDEX;
	private double ratio;
	
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
	
	public double getRatio() {
		return this.ratio;
	}
	
	// setter
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	
	// toString
	public String toString() {
		return "(" + this.INDEX + "," + this.LENGTH +  ")";
	}
	
	// compare method
	@Override
	public int compareTo(Piece p) {
		if (this.ratio - p.getRatio() < 0) {
			return 1;
		} else if (this.ratio - p.getRatio() > 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
