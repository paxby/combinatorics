package paxby.combinatorics.tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearestNeighbourStrategy implements NeighbourStrategy {

	/**
	 * Returns a map of sorted nearest-neighbour (NN) lists of given size.
	 * 
	 * @return neighbour map
	 * @param clSize size of neighbour list
	 */
	@Override
	public Map<Node, List<Node>> getMap(List<Node> nodes, int size) {

		Map<Node, List<Node>> map = new HashMap<Node, List<Node>>();

		for (Node n1 : nodes) {
			ArrayList<Node> list = new ArrayList<Node>(nodes);
			list.remove(n1);
			Collections.sort(list, new NNComparator(n1));

			map.put(n1, size < list.size() ? list.subList(0, size) : list);
		}
		return map;
	}

	/**
	 * Returns a map of sorted nearest-neighbour (NN) lists containing all other nodes
	 * 
	 * @return map of sorted nearest-neighbour (NN) lists
	 */
	@Override
	public Map<Node, List<Node>> getMap(List<Node> nodes) {
		return getMap(nodes, nodes.size() - 1);
	}
}
