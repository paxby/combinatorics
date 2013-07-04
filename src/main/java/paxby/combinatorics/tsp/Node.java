package paxby.combinatorics.tsp;

/**
 * Represents a node
 * 
 * @author Petter Axby
 * 
 */
public class Node {

	private int index;
	private double x, y;

	public Node(int index, double x, double y) {
		this.x = x;
		this.y = y;
		this.index = index;
	}

	/**
	 * Returns the distance to another node
	 * 
	 * @param n
	 *            Other node
	 * @return distance to another node
	 */
	public double distTo(Node n) {
		double dx = n.x - x;
		double dy = n.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Returns the distance to another node, rounded to the nearest integer
	 * 
	 * @param n
	 *            Other node
	 * @return The distance
	 */
	public int distToNint(Node n) {
		return (int) (distTo(n) + 0.5);
	}

	@Override
	public String toString() {
		return Integer.toString(index);
	}

	public int getIndex() {
		return index;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
