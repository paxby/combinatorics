package paxby.combinatorics.metaheuristics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import paxby.combinatorics.tsp.NeighbourStrategy;
import paxby.combinatorics.tsp.Node;
import paxby.combinatorics.tsp.TSP;
import paxby.combinatorics.tsp.Tour;

/**
 * An implementation of the Ant System
 * 
 * @author Petter Axby
 * 
 */
public class AS extends MetaHeuristic {

	public static final double LAMBDA = 0.05; // for branching factor

	protected final int alpha, beta;
	protected final int m;
	protected final double rho;
	protected final boolean localSearch;
	protected final Map<Node,List<Node>> constructMap;
	protected final Map<Node,List<Node>> localMap;
	protected final double[][] tau; // pheromone trail
	protected final double[][] eta; // heuristic value (1 / distance ^ beta)
	protected final double[][] pre; // pre-computed tau ^ alpha * eta
	protected Tour[] tours; // tours found at last iteration

	public AS(TSP tsp, ASConfig config) {
		super(tsp);
		this.alpha = config.getAlpha();
		this.beta = config.getBeta();
		this.rho = config.getRho();
		this.m = config.getM();
		
		NeighbourStrategy constructStrategy = config.getConstructStrategy();
		constructMap = constructStrategy.getMap(tsp.getNodes(), config.getConstructNeighbours());

		NeighbourStrategy localStrategy = config.getLocalStrategy();

		if (localStrategy == null) {
			localSearch = false;
			localMap = null;
		} else {
			localSearch = true;
			localMap = localStrategy.getMap(tsp.getNodes(), config.getLocalNeighbours());
		}

		tau = new double[tsp.getSize()][tsp.getSize()];
		eta = new double[tsp.getSize()][tsp.getSize()];
		pre = new double[tsp.getSize()][tsp.getSize()];
		initialiseArrays();
	}

	private void initialiseArrays() {

		int[][] distance = tsp.getDistanceMatrix();
		
		for (int i = 0; i < tsp.getSize() - 1; i++) {

			for (int j = i + 1; j < tsp.getSize(); j++) {

				if (distance[i][j] != 0) {
					eta[i][j] = pow(1.0 / distance[i][j], beta);
				} else {
					eta[i][j] = Double.POSITIVE_INFINITY;
				}
				eta[j][i] = eta[i][j];
			}
		}

		int estimatedLength = getNNTourLength();
		for (int i = 0; i < tsp.getSize() - 1; i++) {
			for (int j = i + 1; j < tsp.getSize(); j++) {
				updatePheromone(i, j, 1.0 / rho / estimatedLength);
			}
		}
	}

	/**
	 * @return The length of a NN tour + LS, for initialisation of tau trails
	 */
	private int getNNTourLength() {

		Tour nnTour = tsp.getNNTour();
		nnTour.twoOpt(constructMap);
		return nnTour.getLength();
	}

	/**
	 * Construct and return a tour
	 * 
	 * @return the Tour
	 */
	private Tour constructTour() {

		List<Node> nodes = tsp.getNodes();
		List<Node> walk = new ArrayList<Node>();
		Set<Node> visited = new HashSet<Node>();

		walk.add(nodes.get(0));
	
		while (walk.size() < nodes.size()) {
			Node current = walk.get(walk.size() - 1);
			visited.add(current);
			walk.add(findNextNode(current, visited));
		}
		return new Tour(tsp, walk);
	}

	/**
	 * Returns an unvisited node from the candidate list probabilistically, or, if all nodes in CL visited, return node
	 * with highest probability
	 * 
	 * @param currentNode
	 *            The current node
	 * @param visited
	 *            A set of visited nodes
	 * @return The successor node
	 */
	private Node findNextNode(Node currentNode, Set<Node> visited) {

		int n1 = currentNode.getIndex();
		boolean found = false;
		double s = 0;

		for (Node node: constructMap.get(currentNode)) {
			int n2 = node.getIndex();

			if (!visited.contains(node)) {
				found = true;
				if (Double.isInfinite(pre[n1][n2])) {
					return node;
				}
				s += pre[n1][n2];
			}
		}

		if (found) { // an unvisited node was found in the CL
			double p = ran.nextDouble() * s;
			double t = 0;

			for (Node node: constructMap.get(currentNode)) {
				int n2 = node.getIndex();

				if (!visited.contains(node)) {
					double dt = pre[n1][n2];

					if (p >= t && p <= (t + dt)) {
						return node;
					}
					t += dt;
				}
			}
		} else {

			// No unvisited node found in the CL. Return the node with the highest probability

			double bestEta = 0;
			Node bestNode = null;
			
			for (Node node: tsp.getNodes()) {
				int n2 = node.getIndex();

				if (!visited.contains(node)) {
					if (Double.isInfinite(pre[n1][n2])) {
						return node;
					}
					if (pre[n1][n2] > bestEta) {
						bestEta = pre[n1][n2];
						bestNode = node;
					}
				}
			}
			return bestNode;
		}

		// this should never happen
		assert false;
		return null;
	}

	/**
	 * Update the raw and pre-computed tau trails (symmetrically)
	 * 
	 * @param i
	 *            index of first node
	 * @param j
	 *            index of second node
	 * @param value
	 *            new value for tau
	 */
	final void updatePheromone(int i, int j, double value) {
		tau[i][j] = value;
		tau[j][i] = value;
		pre[i][j] = pow(tau[i][j], alpha) * eta[i][j];
		pre[j][i] = pre[i][j];
	}

	/**
	 * Evaporate tau trails
	 */
	protected void evaporate() {
		for (int i = 0; i < tsp.getSize() - 1; i++) {
			for (int j = i + 1; j < tsp.getSize(); j++) {
				updatePheromone(i, j, tau[i][j] * (1 - rho));
			}
		}
	}

	/**
	 * Loop through nodes in all tours and update tau trails
	 */
	protected void addPheromone() {
		for (Tour tour : tours) {
			for (int i = 0; i < tsp.getSize(); i++) {
				int j = tour.getNext(i);
				updatePheromone(i, j, tau[i][j] + 1.0 / tour.getLength());
			}
		}
	}

	/**
	 * Slightly faster than Math.pow()
	 */
	private static double pow(double b, int e) {
		double r = b;
		for (int i = 0; i < e - 1; i++) {
			r *= b;
		}
		return r;
	}

	@Override
	protected Tour nextIteration() {

		tours = new Tour[m];
		for (int i = 0; i < m; i++) {
			tours[i] = constructTour();
			if (localSearch) {
				tours[i].twoOpt(localMap);
			}
		}

		Tour iterationBestTour = null;
		int iterationBestLength = Integer.MAX_VALUE;

		for (Tour tour: tours) {
			if (tour.getLength() < iterationBestLength) {
				iterationBestLength = tour.getLength();
				iterationBestTour = tour;
			}
		}

		return iterationBestTour;
	}

	@Override
	protected void nextIterationPostUpdate() {
		evaporate();
		addPheromone();
	}

	/**
	 * Returns the branching factor
	 */
	public double getBranchingFactor() {
		int cnt = 0;

		for (Node node1: tsp.getNodes()) {

			int i = node1.getIndex();
			double max = Double.MIN_VALUE;
			double min = Double.MAX_VALUE;

			for (Node node2: constructMap.get(node1)) {
				
				int c = node2.getIndex();

				if (tau[i][c] < min) {
					min = tau[i][c];
				}

				if (tau[i][c] > max) {
					max = tau[i][c];
				}
			}

			double limit = LAMBDA * (max - min) + min;

			for (Node node2: constructMap.get(node1)) {
				int c = node2.getIndex();

				if (tau[i][c] > limit) {
					cnt++;
				}
			}
		}
		return 1.0 * cnt / 2 / tsp.getSize();
	}
}
