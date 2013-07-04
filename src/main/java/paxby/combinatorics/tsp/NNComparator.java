package paxby.combinatorics.tsp;

import java.util.Comparator;

/**
 * NNComparator - class used to comparing two nodes with respect to their distance from a reference node, for example
 * for sorting list of nodes.
 * 
 * @author Petter Axby
 * 
 */
public class NNComparator implements Comparator<Node> {

	private final Node n;

	public NNComparator(Node n) {
		this.n = n;
	}

	@Override
	public int compare(Node x, Node y) {
		int d = n.distToNint(x) - n.distToNint(y);
		return d == 0 ? 0 : (d < 0 ? -1 : 1);
	}
}
