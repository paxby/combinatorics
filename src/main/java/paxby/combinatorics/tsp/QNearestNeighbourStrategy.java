package paxby.combinatorics.tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class QNearestNeighbourStrategy implements NeighbourStrategy {

	public QNearestNeighbourStrategy() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns a map of sorted quadrant-nearest-neighbour (QNN) lists of given size.
	 * 
	 * @return neighbour map
	 * @param clSize size of neighbour list
	 */
	@Override
	public Map<Node, List<Node>> getMap(List<Node> nodes, int size) {

		Map<Node, List<Node>> NNMap = new NearestNeighbourStrategy().getMap(nodes);
		Map<Node, List<Node>> QNNMap = new HashMap<Node, List<Node>>();
				
		for (Node node: NNMap.keySet()) {
				
			List<ArrayList<Node>> qLists = new ArrayList<ArrayList<Node>>();

			// create 4 collections to store nodes in the four quadrants
			for (int k = 0; k < 4; k++) {
				qLists.add(new ArrayList<Node>());
			}

			for (Node neighbour: NNMap.get(node)) {

				if (neighbour.getX() < node.getX()) {
					if (neighbour.getY() < node.getY()) {
						qLists.get(0).add(neighbour);
					} else {
						qLists.get(1).add(neighbour);
					}
				} else {
					if (neighbour.getY() < node.getY()) {
						qLists.get(2).add(neighbour);
					} else {
						qLists.get(3).add(neighbour);
					}
				}
			}

			List<Iterator<Node>> iterators = new ArrayList<Iterator<Node>>();

			for (ArrayList<Node> k : qLists) {
				iterators.add(k.iterator());
			}

			List<Node> list = new ArrayList<Node>();

			while (list.size() < size) {
				for (Iterator<Node> iterator: iterators) {
					if (iterator.hasNext()) {
						list.add(iterator.next());
					}
					if (list.size() == size) {
						break;
					}
				}
			}
			
			QNNMap.put(node,  list);
		}

		return QNNMap;
	}

	/**
	 * Returns a map of sorted quadrant-nearest-neighbour (QNN) lists containing all other nodes
	 * 
	 * @return map of sorted quadrant-nearest-neighbour (QNN) lists
	 */
	@Override
	public Map<Node, List<Node>> getMap(List<Node> nodes) {
		return getMap(nodes, nodes.size() - 1);
	}
}
