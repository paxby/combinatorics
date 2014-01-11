package paxby.combinatorics.metaheuristics;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import paxby.combinatorics.tsp.InvalidFileException;
import paxby.combinatorics.tsp.TSP;
import paxby.combinatorics.tsp.UnsupportedFileException;

public class ASTest {

	TSP randomTsp;
	ASConfig asConfig;
	
	@Before
	public void Setup() throws FileNotFoundException, UnsupportedFileException, InvalidFileException {
		randomTsp = new TSP("tsplibx/random300.tsp");
		asConfig = new ASConfig();
	}

	@Test
	public void test_TourLength() {
		
		MetaHeuristic meta = new AS(randomTsp, asConfig);
		meta.setSeed(12345);
		assertEquals(13241, meta.solve(3).getLength());
	}

}
