This repository provides some libraries for parsing TSPLIB files and modelling TSP instances. It also provides implementations of some metaheuristics and a framework in Java for implementing and testing metaheuristics for the symmetric travelling salesman problem (TSP). The intention is to provide an environment where ideas can be developed, compared and evaluated easily.

I do not take credit for any of the algorithms implemented here, as they have all been described by others in the literature.

paxby.combinatorics.tsp provides:
* Parsing of EUC_2D TSPLIB files (http://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp)
* Functions for generating candidate lists (sets) using two strategies:
  * Nearest neighbour (NN)
  * Quadrant nearest neighbour (QNN)
* Local search using 2-opt
* Hopkins statistic for measuring spatial clustering

paxby.combinatorics.metaheuristics provides the following metaheuristics:
* Ant System (AS)
* MAX-MIN Ant System (MMAS)

paxby.combinatorics.metaheuristics.exp provides two implementations:
* RunWithGUI - a GUI for visual (Concorde-like).
* RunComparison - for comparing the performance of metaheuristics and their parameters

