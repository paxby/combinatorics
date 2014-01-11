package paxby.combinatorics.metaheuristics.exp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import paxby.combinatorics.metaheuristics.MetaHeuristic;

/**
 * Runs solve() using GUI
 * 
 * @author Petter Axby
 * 
 */
public class RunWithGUI {

	public static void main(String args[]) {

		ApplicationContext context = new FileSystemXmlApplicationContext("config.xml");

		MetaHeuristic meta = (MetaHeuristic) context.getBean(args.length > 0 ? args[0] : "default");
		GUI gui = new GUI(meta);
		meta.addListener(gui);
		meta.solve();
	}
}
