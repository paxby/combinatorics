package paxby.combinatorics.metaheuristics.exp;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import paxby.combinatorics.metaheuristics.*;

/**
 * Compares performance of various meta-heuristics
 * 
 * @author Petter Axby
 * 
 */
public class RunComparison {

	private static final int TESTS = 50;
	private static final int ITERATIONS = 100;

	ApplicationContext context = new ClassPathXmlApplicationContext("test1.xml");

	private int[] Test(String bean, int tests, int iterations) {

		int[] lengths = new int[tests];

		for (int i = 0; i < tests; i++) {
			MetaHeuristic meta = (MetaHeuristic) context.getBean(bean);
			lengths[i] = meta.solve(iterations).getLength();
		}

		return lengths;
	}

	private void testAll() {

		System.out.println("Tests:        " + TESTS);
		System.out.println("Iterations:   " + ITERATIONS);
		System.out.println();

		@SuppressWarnings("unchecked")
		List<String> beans = (List<String>) context.getBean("test");

		for (String bean : beans) {
			System.out.print(String.format("%-13s", bean + ":"));
			int[] result = Test(bean, TESTS, ITERATIONS);
			System.out.println(String.format("mean = %.0f, sd = %.0f",
					mean(result), sd(result)));
		}
	}

	public static double mean(int[] arr) {
		double a = 0;
		for (int i = 0; i < arr.length; i++) {
			a += arr[i];
		}
		return a / arr.length;
	}

	public static double var(int[] arr) {
		double mean = mean(arr);
		double a = 0;
		for (int i = 0; i < arr.length; i++) {
			a += (arr[i] - mean) * (arr[i] - mean);
		}
		return (a / (arr.length - 1));
	}

	public static double sd(int[] arr) {
		return Math.sqrt(var(arr));
	}

	public static void main(String args[]) {
		new RunComparison().testAll();
	}
}
