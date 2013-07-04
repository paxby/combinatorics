package paxby.combinatorics.tsp;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NearestNeighbourStrategyTest {

	Node n0, n1, n2;
	List<Node> nodes;

	@Before
	public void setUp() {
		n0 = new Node(0, 1,3);
		n1 = new Node(1, 1,4);
		n2 = new Node(2, 4,9);
		nodes = Arrays.asList(new Node[] { n0, n1, n2 });
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test_NearestNeighbourStrategy() {

		Map<Node, List<Node>> neighbours = new NearestNeighbourStrategy().getMap(nodes);
		assertEquals(Arrays.asList(new Node[] { n1, n2 }), neighbours.get(n0));
		assertEquals(Arrays.asList(new Node[] { n0, n2 }), neighbours.get(n1));
		assertEquals(Arrays.asList(new Node[] { n1, n0 }), neighbours.get(n2));

		neighbours = new NearestNeighbourStrategy().getMap(nodes, 1);
		assertEquals(Arrays.asList(new Node[] { n1 }), neighbours.get(n0));
		assertEquals(Arrays.asList(new Node[] { n0 }), neighbours.get(n1));
		assertEquals(Arrays.asList(new Node[] { n1 }), neighbours.get(n2));

	}
}
