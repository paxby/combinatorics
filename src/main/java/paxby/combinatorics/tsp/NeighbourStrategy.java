package paxby.combinatorics.tsp;

import java.util.List;
import java.util.Map;

public interface NeighbourStrategy {

	Map<Node,List<Node>> getMap(List<Node> nodes, int size);
	Map<Node,List<Node>> getMap(List<Node> nodes);

}
