package paxby.combinatorics.metaheuristics;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import paxby.combinatorics.tsp.InvalidFileException;
import paxby.combinatorics.tsp.QNearestNeighbourStrategy;
import paxby.combinatorics.tsp.TSP;
import paxby.combinatorics.tsp.UnsupportedFileException;

public class MMASTest {

	TSP randomTsp;
	ASConfig asConfigNN;
	ASConfig asConfigQNN;
	
	@Before
	public void Setup() throws FileNotFoundException, UnsupportedFileException, InvalidFileException {
		randomTsp = new TSP("tsplibx/random300.tsp");
		asConfigNN = new ASConfig();
		asConfigQNN = new ASConfig();
		asConfigQNN.setConstructStrategy(new QNearestNeighbourStrategy());
	}

	@Test
	public void test_TourLength_NN() {
		
		MetaHeuristic meta = new MMAS(randomTsp, asConfigNN);
		meta.setSeed(12345);
		assertEquals(13607, meta.solve(3).getLength());
	}

	@Test
	public void test_TourLength_QNN() {
		
		MetaHeuristic meta = new MMAS(randomTsp, asConfigQNN);
		meta.setSeed(12345);
		assertEquals(13399, meta.solve(10).getLength());
	}
	
}
