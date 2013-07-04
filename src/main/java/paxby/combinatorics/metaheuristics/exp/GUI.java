package paxby.combinatorics.metaheuristics.exp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

import paxby.combinatorics.metaheuristics.AS;
import paxby.combinatorics.metaheuristics.MetaHeuristic;
import paxby.combinatorics.metaheuristics.MetaHeuristicEventListener;
import paxby.combinatorics.tsp.Node;
import paxby.combinatorics.tsp.TSP;
import paxby.combinatorics.tsp.Tour;

/**
 * A graphical representation of the TSP and current best tour
 * 
 * @author Petter Axby
 * 
 */
public class GUI extends JPanel implements MetaHeuristicEventListener {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = new Font("Sans", Font.PLAIN, 12);
	private static final int PANEL_SIZE_X = 800;
	private static final int PANEL_SIZE_Y = 600;
	private static final Color COLOR_NODE = Color.BLUE;
	private static final Color COLOR_EDGE = Color.RED;
	private static final int BORDER = 10;
	private static final boolean showLabels = false; // show numeric node labels

	private final StatusBar statusBar = new StatusBar();

	private final MetaHeuristic metaHeuristic;
	private final TSP tsp;
	private Tour tour;
	private int iteration;

	private class StatusBar extends JPanel {

		private static final long serialVersionUID = 1L;

		public StatusBar() {
			setBackground(Color.WHITE);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.setFont(FONT);

			StringBuilder string = new StringBuilder("Iteration " + iteration);

			if (tour != null) {
				string.append(", " + "tour length: " + Integer.toString(tour.getLength()));
			}

			if (metaHeuristic instanceof AS) {
				string.append(", branching factor = "
						+ String.format("%.5f", ((AS) metaHeuristic).getBranchingFactor()));
			}
			g.drawString(string.toString(), 10, getHeight() - 7);
		}
	}

	public GUI(MetaHeuristic metaHeuristic) {

		this.metaHeuristic = metaHeuristic;
		tsp = metaHeuristic.getTSP();

		setBackground(Color.WHITE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(PANEL_SIZE_X, PANEL_SIZE_Y));
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		panel.add(this, c);

		c.weighty = 0;
		c.gridy = 1;
		c.ipady = 20;
		panel.add(statusBar, c);

		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setTitle(tsp.getName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(1.5f));

		int width = getWidth();
		int height = getHeight();

		g.setColor(COLOR_NODE);

		for (Node node : tsp.getNodes()) {
			int x = toX(node.getX(), width);
			int y = toY(node.getY(), height);
			g2d.fillOval(x - 4, y - 4, 8, 8);
			if (showLabels) {
				g.drawString(Integer.toString(node.getIndex()), x + 6, y + 6);
			}
		}

		Tour bestTour = metaHeuristic.getBestTour();

		if (bestTour != null) {
			g.setColor(COLOR_EDGE);

			for (Node n1: tsp.getNodes()) {
				Node n2 = bestTour.getNextNode(n1);
				g2d.drawLine(toX(n1.getX(), width), toY(n1.getY(), height), toX(n2.getX(), width), toY(n2.getY(), height));
			}
		}
	}

	/**
	 * Converts given X and Y coordinates to corresponding screen coordinate
	 */
	private int toX(double x, int width) {
		double scaleX = (width - 2 * BORDER) / (tsp.getMaxX() - tsp.getMinX());
		return (int) ((x - tsp.getMinX()) * scaleX + BORDER);
	}

	/**
	 * Converts given X and Y coordinates to corresponding screen coordinate
	 */
	private int toY(double y, int height) {
		double scaleY = (height - 2 * BORDER) / (tsp.getMaxY() - tsp.getMinY());
		return (int) ((tsp.getMaxY() - y) * scaleY + BORDER);
	}

	/**
	 * Called on new iteration
	 */
	@Override
	public void newIteration(int iteration) {
		this.iteration = iteration;
		statusBar.repaint();
	}

	/**
	 * Called on new best Tour
	 */
	@Override
	public void newTour(Tour tour) {
		this.tour = tour;
		repaint();
	}
}
