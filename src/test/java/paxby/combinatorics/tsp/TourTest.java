package paxby.combinatorics.tsp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TourTest {

	TSP tsp;
	List<Node> nodes;
	
	@Before
	public void setUp() {

		nodes = new ArrayList<Node>();
		nodes.add(new Node(0, 0, 0));
		nodes.add(new Node(1, 0, 1));
		nodes.add(new Node(2, 0, 2));
		nodes.add(new Node(3, 0, 3));
		nodes.add(new Node(4, 0, 4));

		tsp = mock(TSP.class);
		when(tsp.getNodes()).thenReturn(nodes);
		when(tsp.getDistanceMatrix()).thenReturn(TSP.getDistanceMatrix(nodes));
	}
	
	@Test
	public void test() {

		Tour tour = new Tour(tsp, Arrays.asList(new Node[] { 
				nodes.get(0),
				nodes.get(2),
				nodes.get(3),
				nodes.get(1),
				nodes.get(4)
				}));
	
		assertEquals(nodes.get(2), tour.getNextNode(0));
		assertEquals(nodes.get(4), tour.getNextNode(1));
		assertEquals(nodes.get(3), tour.getNextNode(2));
		assertEquals(nodes.get(1), tour.getNextNode(3));
		assertEquals(nodes.get(0), tour.getNextNode(4));
		assertEquals(12, tour.getLength());
	
		tour = new Tour(tsp, Arrays.asList(new Node[] { 
				nodes.get(0),
				nodes.get(1),
				nodes.get(2),
				nodes.get(3),
				nodes.get(4)
				}));
		assertEquals(8, tour.getLength());
	
		tour = new Tour(tsp, Arrays.asList(new Node[] { 
				nodes.get(0),
				nodes.get(2),
				nodes.get(1),
				nodes.get(3),
				nodes.get(4)
				}));
		assertEquals(10, tour.getLength());
		
	}

}
