package paxby.combinatorics.metaheuristics;

import java.util.ArrayList;
import java.util.Random;
import paxby.combinatorics.tsp.TSP;
import paxby.combinatorics.tsp.Tour;

/**
 * Abstract class providing functionality common to metaheuristics.
 * 
 * @author Petter Axby
 * 
 */
abstract public class MetaHeuristic {

	protected int iteration;
	protected Tour iterationBestTour;
	protected Tour bestTour;
	private final ArrayList<MetaHeuristicEventListener> eventListeners = new ArrayList<MetaHeuristicEventListener>();
	protected Random ran = new Random();
	protected TSP tsp;

	protected MetaHeuristic(TSP tsp) {
		this.tsp = tsp;
	}

	/**
	 * Solve, and never return
	 */
	public final void solve() {
		while (true) {
			nextIterationProcedure();
		}
	}

	/**
	 * Solve for maxIterations, and return the best solution found
	 * 
	 * @return best Tour found
	 */
	public final Tour solve(int maxIterations) {
		while (iteration < maxIterations) {
			nextIterationProcedure();
		}
		return bestTour;
	}

	/**
	 * Solve, while the improvement over a given number of iterations (period) falls below a given threshold
	 * 
	 * @param period
	 *            window in which to require improvement to keep going
	 * @param threshold
	 *            Minimum improvement, 0 <= threshold <= 1 (e.g. 0.05 = 5% improvement)
	 * @return best Tour found
	 */
	public final Tour solveWithStopCriterion(int period, double threshold) {

		int[] lastBest = new int[period];

		while (true) {

			nextIterationProcedure();

			int index = (iteration - 1) % period;

			if (iteration > period) {
				double diff = 1.0 * (lastBest[index] - bestTour.getLength()) / lastBest[index];
				if (diff < threshold) {
					return bestTour;
				}
			}
			lastBest[index] = bestTour.getLength();
		}
	}

	/**
	 * Steps during each iteration. To be called from each solve method.
	 */
	private void nextIterationProcedure() {

		iteration++;

		iterationBestTour = nextIteration();

		if (iteration == 1 || iterationBestTour.getLength() < bestTour.getLength()) {
			bestTour = iterationBestTour;

			for (MetaHeuristicEventListener listener : eventListeners) {
				listener.newTour(bestTour);
			}
		}

		// update listeners
		for (MetaHeuristicEventListener listener : eventListeners) {
			listener.newIteration(iteration);
		}

		nextIterationPostUpdate();
	}

	/**
	 * Set seed for random number generation
	 * 
	 * @param seed
	 *            Seed
	 */
	public void setSeed(int seed) {
		this.ran = new Random(seed);
	}

	/**
	 * Add listener - to be called on each iteration and when better solution is found
	 * 
	 * @param listener
	 *            ASEventListener object
	 */
	public void addListener(MetaHeuristicEventListener listener) {
		eventListeners.add(listener);
	}

	/**
	 * Code to be executed at each iteration and must return the best Tour at that iteration. Any code that uses
	 * bestTour (best Tour found so far) should go in the nextIterationPostUpdate method.
	 * 
	 * @return best Tour at iteration
	 */
	abstract protected Tour nextIteration();

	/**
	 * Code to be executed at each iteration after bestTour (best Tour found so far) has been updated.
	 */
	abstract protected void nextIterationPostUpdate();

	public int getIteration() {
		return iteration;
	}

	public Tour getBestTour() {
		return bestTour;
	}

	public TSP getTSP() {
		return tsp;
	}

	public ArrayList<MetaHeuristicEventListener> getEventListeners() {
		return eventListeners;
	}
}