package paxby.combinatorics.tsp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TSPTest {

	private static final String tspLib = "resources/tsplib/";
	
	Node n0, n1, n2;
	List<Node> nodes;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() {
		n0 = new Node(0, 1,3);
		n1 = new Node(1, 1,4);
		n2 = new Node(2, 4,9);
		nodes = Arrays.asList(new Node[] { n0, n1, n2 });
	}
		
	@Test
	public void test_getDistanceMatrix() {
		int[][] arr = new int[][] { { 0, 1, 7 }, { 1, 0, 6 }, { 7, 6, 0 }};
		assertArrayEquals(arr, TSP.getDistanceMatrix(nodes));
	}

	@Test
	public void testConstructor() throws FileNotFoundException, UnsupportedFileException, InvalidFileException {
		
		File f = new File(tspLib);
		ArrayList<File> files = new ArrayList<File>(Arrays.asList(f.listFiles()));
		
		for (File file: files) {
			String fileName = file.getName();
			int size = Integer.parseInt(fileName.replaceAll("\\D",  ""));
			
			if (size < 100) {
				TSP tsp = new TSP(tspLib + fileName);
				assertEquals("size of instance " + fileName, size, tsp.getSize());
			}
		}
	}
	
	@Test
	public void testGetNNTour() {

		nodes = new ArrayList<Node>();
		nodes.add(new Node(0, 0, 0));
		nodes.add(new Node(1, 0, 2));
		nodes.add(new Node(2, 0, 1));
		nodes.add(new Node(3, 0, 3));
		nodes.add(new Node(4, 0, 4));
		
		TSP tsp = new TSP(nodes);
		assertEquals(8, tsp.getNNTour().getLength());
	}
}
