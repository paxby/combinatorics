package paxby.combinatorics.metaheuristics;

import paxby.combinatorics.tsp.Tour;

/**
 * To be called on each iteration and when better solution is found - e.g. GUIs
 * 
 * @author Petter Axby
 * 
 */
public interface MetaHeuristicEventListener {

	void newIteration(int i);

	void newTour(Tour tour);
}
