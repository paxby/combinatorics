package paxby.combinatorics.metaheuristics;

import java.util.List;

import paxby.combinatorics.tsp.Node;
import paxby.combinatorics.tsp.TSP;

/**
 * Implements the MAX-MIN Ant System (as an extension of the Ant System)
 * 
 * @author Petter Axby
 * 
 */
public class MMAS extends AS {

	private static final double P_BEST = 0.05;
	private final double pMinFactor;
	private double pMin; // maximum tau (tau_max)
	private double pMax; // minimum tau (tau_min)

	public MMAS(TSP tsp, ASConfig config) {
		super(tsp, config);

		if (localSearch) {
			pMinFactor = 1.0 / 2 / tsp.getSize();
		} else {
			double pDec = Math.pow(P_BEST, 1.0 / tsp.getSize());
			double avg = 0;

			for (List<Node> list: constructMap.values()) {
				avg += list.size();
			}
			avg /= tsp.getSize();
			pMinFactor = (1 - pDec) / (avg / 2.0 - 1) / pDec;
		}
	}

	@Override
	protected void evaporate() {
		for (int i = 0; i < tsp.getSize() - 1; i++) {
			for (int j = i + 1; j < tsp.getSize(); j++) {
				double p = tau[i][j] * (1 - rho);
				if (p > pMin) {
					updatePheromone(i, j, tau[i][j] * (1 - rho));
				} else {
					updatePheromone(i, j, pMin);
				}
			}
		}
	}

	@Override
	protected void nextIterationPostUpdate() {
		pMax = 1.0 / rho / bestTour.getLength();
		pMin = pMax * pMinFactor;
		evaporate();
		addPheromone();
	}

	@Override
	protected void addPheromone() {

		// Gradually increase the frequency of global best update
		int mod;
		if (iteration < 25) {
			mod = 25;
		} else if (iteration < 75) {
			mod = 5;
		} else if (iteration < 125) {
			mod = 3;
		} else if (iteration < 250) {
			mod = 2;
		} else {
			mod = 1;
		}

		for (int i = 0; i < tsp.getSize(); i++) {

			int j;
			double p;

			if (iteration % mod == 0) {
				// global best update
				j = bestTour.getNext(i);
				p = tau[i][j] + 1.0 / bestTour.getLength();
			} else {
				// local best update
				j = iterationBestTour.getNext(i);
				p = tau[i][j] + 1.0 / iterationBestTour.getLength();
			}

			if (p < pMax) {
				updatePheromone(i, j, p);
			} else {
				updatePheromone(i, j, pMax);
			}
		}
	}
}
