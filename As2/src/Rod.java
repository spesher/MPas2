/**
 * Rod class. Stores the length of the rod.
 * @author Reinier van Uden
 *
 */
public class Rod {
	
	private final int LENGTH;
	
	/**
	 * Constructor
	 * @param length of the rod
	 */
	public Rod(int length) {
		this.LENGTH = length;
	}
	
	// getter
	public int getLength() {
		return this.LENGTH;
	}
	
	// toString
	public String toString() {
		return " " + this.LENGTH;
	}

}
