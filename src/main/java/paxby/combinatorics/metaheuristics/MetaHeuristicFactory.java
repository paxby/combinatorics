package paxby.combinatorics.metaheuristics;

import paxby.combinatorics.tsp.TSP;

/**
 * Factory for returning an instance of a Metaheuristic and its description
 * 
 * @author Petter Axby
 * 
 */
public interface MetaHeuristicFactory {

	MetaHeuristic getMetaHeuristic(TSP tsp);

	String getDescription();

}
