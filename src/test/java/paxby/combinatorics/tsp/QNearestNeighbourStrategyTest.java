package paxby.combinatorics.tsp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class QNearestNeighbourStrategyTest {

	List<Node> nodes = new ArrayList<Node>();

	@Before
	public void setUp() {
		nodes.add(new Node(0, 10,10));
		nodes.add(new Node(1, 1,1));
		nodes.add(new Node(2, 2,2));
		nodes.add(new Node(3, 3,3));
		nodes.add(new Node(4, 101,101));
		nodes.add(new Node(5, 102,102));
		nodes.add(new Node(6, 103,103));
		nodes.add(new Node(7, 101,3));
		nodes.add(new Node(8, 102,2));
		nodes.add(new Node(9, 103,1));
		nodes.add(new Node(10, 1,103));
		nodes.add(new Node(11, 2,102));
		nodes.add(new Node(12, 3,101));
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_NearestNeighbourStrategy() {

		Map<Node, List<Node>> neighbours = new QNearestNeighbourStrategy().getMap(nodes,4);
		assertNotNull(neighbours);

		Set<Node> expected = new HashSet<Node>();
		expected.add(nodes.get(3));
		expected.add(nodes.get(7));
		expected.add(nodes.get(12));
		expected.add(nodes.get(4));
		assertEquals(expected, new HashSet<Node>(neighbours.get(nodes.get(0))));

		neighbours = new QNearestNeighbourStrategy().getMap(nodes,5);
		expected.add(nodes.get(2));
		assertEquals(expected, new HashSet<Node>(neighbours.get(nodes.get(0))));

		neighbours = new QNearestNeighbourStrategy().getMap(nodes,6);
		expected.add(nodes.get(11));
		assertEquals(expected, new HashSet<Node>(neighbours.get(nodes.get(0))));

		neighbours = new QNearestNeighbourStrategy().getMap(nodes,7);
		expected.add(nodes.get(8));
		assertEquals(expected, new HashSet<Node>(neighbours.get(nodes.get(0))));

		neighbours = new QNearestNeighbourStrategy().getMap(nodes,8);
		expected.add(nodes.get(5));
		assertEquals(expected, new HashSet<Node>(neighbours.get(nodes.get(0))));
	}
}
