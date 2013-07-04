package paxby.combinatorics.tsp;

import java.util.List;
import java.util.Map;

/**
 * Represents a tour of a given TSP
 * 
 * @author Petter Axby
 * 
 */
public class Tour {

	// speed-up techniques for 2-opt
	private static final boolean FIRST_IMPROVEMENT = false;
	private static final boolean FIXED_RADIUS = true;
	private static final boolean USE_DLB = false; // do-not-look bits

	private final TSP tsp;
	private int length;

	// Incidence array. A node j is followed by node i in a tour if successor[i]=j
	private final int successor[];

	public Tour(TSP tsp, List<Node> nodes) {

		this.tsp = tsp;
		successor = new int[nodes.size()];
	
		for (int i = 0; i < nodes.size() - 1; i++) {
			successor[nodes.get(i).getIndex()] = nodes.get(i + 1).getIndex();
		}
		successor[nodes.get(nodes.size() - 1).getIndex()] = nodes.get(0).getIndex();
		calculateLength();
	}
	

	/**
	 * @return the index of the successor node of the tour
	 * @param i
	 *            index of node
	 */
	public int getNext(int i) {
		return successor[i];
	}

	/**
	 * @return the successor Node of the tour
	 * @param i
	 *            index of node
	 */
	public Node getNextNode(int i) {
		return tsp.getNodes().get(successor[i]);
	}
	
	/**
	 * @return the successor node of the tour
	 * @param i
	 *            index of node
	 */
	public Node getNextNode(Node node) {
		return tsp.getNodes().get(successor[node.getIndex()]);
	}
	

	/**
	 * Calculate and set the length of the tour
	 */
	private void calculateLength() {
		length = 0;
		
		int[][] distance = tsp.getDistanceMatrix();

		for (int i = 0; i < successor.length; i++) {
			length += distance[i][successor[i]];
		}
	}

	/**
	 * Improves the tour by applying a (currently very crude and inefficient) implementation of 2-opt
	 * 
	 * @param cl
	 *            Array of nodes to search
	 */
	@SuppressWarnings("unused")
	public void twoOpt(Map<Node, List<Node>> neighbours) {
		
		int[][] distance = tsp.getDistanceMatrix();
		List<Node> nodes = tsp.getNodes();

		int bestDiff = 0;

		final boolean[] doNotLook;

		if (USE_DLB) {
			doNotLook = new boolean[tsp.getSize()];
		}

		do {
			int swap1 = 0;
			int swap2 = 0;
			bestDiff = 0;

			for (Node node: nodes) {
				
				int i = node.getIndex();

				if (USE_DLB && doNotLook[i]) {
					continue;
				}

				boolean foundBetter = false;

				for (int c = 0; c < neighbours.get(node).size(); c++) {
				
					int j = neighbours.get(nodes.get(i)).get(c).getIndex();

					if (FIXED_RADIUS && (distance[i][successor[i]] < distance[i][j])) {
						break;
					}

					int oldlength = distance[i][successor[i]] + distance[j][successor[j]];
					int newlength = distance[i][j] + distance[successor[i]][successor[j]];
					int diff = oldlength - newlength;

					if (diff > 0) {
						foundBetter = true;

						if (diff > bestDiff) {
							bestDiff = diff;
							swap1 = i;
							swap2 = j;
						}

						if (FIRST_IMPROVEMENT) {
							break;
						}
					}
				}

				if (FIRST_IMPROVEMENT && bestDiff > 0) {
					break;
				}

				if (USE_DLB && !foundBetter) {
					doNotLook[i] = true;
				}
			}

			if (bestDiff > 0) {

				int nexti = successor[swap1];
				int nextj = successor[swap2];

				if (USE_DLB) {
					doNotLook[swap2] = false;
					doNotLook[nexti] = false;
					doNotLook[nextj] = false;
				}

				// reverse
				int tempNode = successor[swap1];
				int tempNodex = successor[tempNode];

				do {
					int newTempNodex = successor[tempNodex];
					successor[tempNodex] = tempNode;
					tempNode = tempNodex;
					tempNodex = newTempNodex;
				} while (tempNode != swap2);

				// swap
				successor[swap1] = swap2;
				successor[nexti] = nextj;

				length -= bestDiff;
			}
		} while (bestDiff > 0);
	}

	@Override
	public String toString() {
		String s = "";
		int n = 0;
		for (int i = 0; i < successor.length; i++) {
			s += n + " ";
			n = successor[n];
		}
		return s + "\n";
	}

	public int getLength() {
		return length;
	}
}
