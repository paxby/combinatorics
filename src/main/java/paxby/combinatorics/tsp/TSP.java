package paxby.combinatorics.tsp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Class for representing a TSP. 
 * 
 * @author Petter Axby
 * 
 */
public class TSP {

	private String name;
	private List<Node> nodes;
	private Map<Node, List<Node>> neighbours;
	private Random ran = new Random();
	private Range range;
	private int[][] distanceMatrix;

	/**
	 * Initialises the object by reading TSPLIB file
	 * 
	 * @param fileName
	 *            TSPLIB file
	 * @throws UnsupportedFileException
	 *             Type is not supported (must be TSP + EUC_2D or ATT)
	 * @throws FileNotFoundException
	 *             File not found
	 * @throws InvalidFileException
	 *             Unable to parse file
	 */
	public TSP(String fileName) throws UnsupportedFileException, FileNotFoundException, InvalidFileException {

		Scanner in = new Scanner(new FileReader(fileName));
		int size = 0;

		while (in.hasNextLine()) {

			String line = in.nextLine();

			String[] fields = line.split(":");
			fields[0] = fields[0].trim().toUpperCase();

			if (fields[0].equals("NAME")) {
				name = fields[1].trim();
			} 
			else if (fields[0].equals("TYPE")) {
				String type = fields[1].trim().toUpperCase();

				if (!type.equals("TSP")) {
					in.close();
					throw new UnsupportedFileException("Type " + type + " is not supported");
				}
			}
			else if (fields[0].equals("DIMENSION")) {
				size = Integer.parseInt(fields[1].trim());
			} 
			else if (fields[0].equals("EDGE_WEIGHT_TYPE")) {
				String edgeWeightType = fields[1].trim().toUpperCase();
				if (!edgeWeightType.equals("EUC_2D") && !edgeWeightType.equals("ATT")) {  // Note: ATT is treated as Euclidean TSP
					in.close();
					throw new UnsupportedFileException("Edge weight type " + edgeWeightType + " is not supported");
				}
			} 
			else if (fields[0].equals("FIXED_EDGES_SECTION")) {
				in.close();
				throw new UnsupportedFileException("FIXED_EDGES_SECTION is not supported");
			} 
			else if (fields[0].equals("NODE_COORD_SECTION")) {
				nodes = new ArrayList<Node>();
				for (int i = 0; i < size; i++) {

					try {
						nodes.add(new Node(in.nextInt() - 1, in.nextFloat(), in.nextFloat()));
						in.nextLine();
					} catch (Exception e) {
						in.close();
						throw new InvalidFileException("Unable to parse file " + fileName);
					}

					if (nodes.get(i).getIndex() != i) {
						in.close();
						throw new InvalidFileException("Node indices must be sequential starting from 1");
					}
				}
			} 
			else if (!fields[0].equals("") && !fields[0].equals("EOF") && !fields[0].equals("COMMENT")) {
				in.close();
				throw new InvalidFileException("Unable to parse file " + fileName);
			}
		}
		in.close();
		initialise();
	}
	
	public TSP(List<Node> nodes) {
		
		this.nodes = nodes;
		initialise();
	}

	private void initialise() {
		distanceMatrix = getDistanceMatrix(nodes);
		range = getRange(nodes);
		neighbours = new NearestNeighbourStrategy().getMap(nodes);
	}
	
	/**
	 * Returns the X/Y range as a Range object
	 */
	public static Range getRange(List<Node> nodes) {
		
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (Node node: nodes) {
			if (node.getX() < minX) {
				minX = node.getX();
			}
			if (node.getX() > maxX) {
				maxX = node.getX();
			}
			if (node.getY() < minY) {
				minY = node.getY();
			}
			if (node.getY() > maxY) {
				maxY = node.getY();
			}
		}
	
		return new Range(minX, minY, maxX, maxY);

	}

	/**
	 * Returns a distanceMatrix matrix given a list of nodes
	 */
	public static int[][] getDistanceMatrix(List<Node> nodes) {

		int size = nodes.size();

		int[][] distance = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				distance[i][j] = nodes.get(i).distToNint(nodes.get(j));
				distance[j][i] = distance[i][j];
			}
		}
		return distance;
	}
	
	/**
	 * Returns the nearest neighbour tour starting at the first node
	 * 
	 * @return nearest neighbour tour
	 */
	public Tour getNNTour() {
		
		List<Node> walk = new ArrayList<Node>();
		Set<Node> visited = new HashSet<Node>();
	
		walk.add(nodes.get(0));
		visited.add(nodes.get(0));

		while (walk.size() < nodes.size()) {
			Node current = walk.get(walk.size() - 1);
			for (Node node: neighbours.get(current)) {
				if (!walk.contains(node)) {
					walk.add(node);
					visited.add(node);
				}
			}
		}
		return new Tour(this, walk);
	}

	/**
	 * Returns a List of random nodes.
	 * 
	 * @return List of random nodes
	 * @param sampleSize
	 *            Number of nodes
	 */
	private List<Node> getRandomNodes(int size) {
		List<Node> shuffledList = new ArrayList<Node>(nodes);
		Collections.shuffle(shuffledList);
		return shuffledList.subList(0, size);
	}
	
	/**
	 * Returns a List of points selected randomly from the space bounded by the space of TSP
	 * 
	 * @return List of points
	 * @param sampleSize
	 *            Number of nodes
	 */
	private List<Node> getRandomPoints(int sampleSize) {
		List<Node> u = new ArrayList<Node>();
		for (int i = 0; i < sampleSize; i++) {
			double x = ran.nextDouble() * (range.getMaxX() - range.getMinX()) + range.getMinX();
			double y = ran.nextDouble() * (range.getMaxY() - range.getMinY()) + range.getMinY();
			u.add(new Node(0, x, y));
		}
		return u;
	}

	/**
	 * Returns the Hopkins statistic for the TSP (a measure of spatial clustering)
	 * 
	 * @param numberOfTests
	 *            Number of independent tests
	 * @param sampleSizeFactor
	 *            Proportion to select as test sample (0 < sampleSizeFactor <= 1)
	 * @return Hopkins statistic (mean of independent tests)
	 */
	public double getHopkins(int numberOfTests, double sampleSizeFactor) {

		final int sampleSize = (int) (nodes.size() * sampleSizeFactor);
		double sum = 0;

		for (int z = 0; z < numberOfTests; z++) {

			List<Node> w = getRandomNodes(sampleSize);
			List<Node> u = getRandomPoints(sampleSize);

			double sumdu = 0;
			for (Node nu : u) {
				double d = Double.POSITIVE_INFINITY;
				for (Node n : nodes) {
					if (nu.distTo(n) < d) {
						d = nu.distTo(n);
					}
				}
				sumdu += d;
			}

			double sumdw = 0;
			for (Node nw : w) {
				double d = Double.POSITIVE_INFINITY;
				for (Node n : nodes) {
					if ((nw.distTo(n) < d) && n != nw) {
						d = nw.distTo(n);
					}
				}
				sumdw += d;
			}

			double hopkins = sumdu / (sumdu + sumdw);
			sum += hopkins;
		}

		return sum / numberOfTests;
	}


	/**
	 * Set seed for random number generation
	 * 
	 * @param seed
	 *            Seed
	 */
	public void setSeed(int seed) {
		ran = new Random(seed);
	}

	public double getMinX() {
		return range.getMinX();
	}

	public double getMinY() {
		return range.getMinY();
	}

	public double getMaxX() {
		return range.getMaxX();
	}

	public double getMaxY() {
		return range.getMaxY();
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return nodes.size();
	}

	public Map<Node, List<Node>> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(Map<Node, List<Node>> neighbours) {
		this.neighbours = neighbours;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}
}