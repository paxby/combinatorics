package paxby.combinatorics.metaheuristics;

import paxby.combinatorics.tsp.NearestNeighbourStrategy;
import paxby.combinatorics.tsp.NeighbourStrategy;

public class ASConfig {

	private int alpha = 1;
	private int beta = 2;
	private int m = 25;
	private double rho = 0.2;
	private NeighbourStrategy constructStrategy = new NearestNeighbourStrategy();
	private NeighbourStrategy localStrategy = new NearestNeighbourStrategy();
	private int constructNeighbours = 20;
	private int localNeighbours = 25;

	public int getAlpha() {
		return alpha;
	}
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	public int getBeta() {
		return beta;
	}
	public void setBeta(int beta) {
		this.beta = beta;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	public double getRho() {
		return rho;
	}
	public void setRho(double rho) {
		this.rho = rho;
	}
	public NeighbourStrategy getConstructStrategy() {
		return constructStrategy;
	}
	public void setConstructStrategy(NeighbourStrategy constructStrategy) {
		this.constructStrategy = constructStrategy;
	}
	public NeighbourStrategy getLocalStrategy() {
		return localStrategy;
	}
	public void setLocalStrategy(NeighbourStrategy localStrategy) {
		this.localStrategy = localStrategy;
	}
	public int getConstructNeighbours() {
		return constructNeighbours;
	}
	public void setConstructNeighbours(int constructNeighbours) {
		this.constructNeighbours = constructNeighbours;
	}
	public int getLocalNeighbours() {
		return localNeighbours;
	}
	public void setLocalNeighbours(int localNeighbours) {
		this.localNeighbours = localNeighbours;
	}
}
	