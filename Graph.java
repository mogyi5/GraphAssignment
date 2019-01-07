import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class Graph extends JComponent {

	private static final long serialVersionUID = 1L;
	private int g_squareX;						// The starting point of the x-coordinates.
	private int g_squareY;						// The starting point of the y-coordinates.
	private int g_squareW;						// The width of the graph.
	private int g_squareH;						// The height of the graph. 
	
	private int g_heightShift = 20;				// The distance between the bottom of the white area and the bottom of the panel. (to leave space for the axis marks and numbers)
	private int g_widthLeftShift = 40;			// The distance between the white area and left edge of the panel.
	private int g_widthRightShift = 45;			// The distance between the white area and right edge of the panel. (for design purposes?)
	
	private int	g_pointRadius = 6;			// The radius of a point.
	
	protected int[][] g_axisLabels = { { 0, 0 }, { 1, 2 }, { 2, 4 }, { 3, 6 }, { 4, 8 } };				// Set initial labels for the axes before loading a file
																										// otherwise the program would crash.
	protected double[][] g_trades = { { 1, 3 }, { 1, 2.5 }, { 1, 2 }, { 1, 1.5 }, { 1, 1 }, { 1.5, 2 }, { 2, 3 }, 
			{ 2, 2.5 }, { 2, 2 }, { 2, 1.5 }, { 2, 1 }, { 3, 3 }, { 3, 2.5 }, { 3, 2 }, { 3, 1.5 }, { 3, 1 } }; 	// Set initial points for decoration and plus marks :).

	protected int g_xValue = 0; 	// Set the index of the header that the x-axis will represent.
	protected int g_yValue = 1;		// Set the index of the header that the y-axis will represent. 

	protected List<Shape> g_pointList = new ArrayList<>();		// A list of all the points in the graph.
	protected Shape g_selectedPoint = null;						// The point which is currently clicked on. 

	public Graph(int squareX, int squareY, int squareW, int squareH) {  // Constructor.

		this.g_squareX = squareX;		// The starting point for the width: 0.
		this.g_squareY = squareY;		// The starting point for the height: 0.
		this.g_squareH = squareH;		// The total height: 500.
		this.g_squareW = squareW;		// The total width: 550.

	}
	
	// Getters for the variables.
	
	public int[][] getLabels() {			// Return the axis labels.
		return g_axisLabels;
	}
	
	public double[][] getTrades() {			// Return the trades.
		return g_trades;
	}
	
	public int getXValue() {			// Return the selected header index for the x axis.
		return g_xValue;
	}
	
	public int getYValue() {			// Return the selected header index for the y axis.
		return g_yValue;
	}
	
	public List<Shape> getPointList() {			// Return the list of shapes.
		return g_pointList;
	}
	
	public Shape getSelectedShape() {			// Return the selected shape.
		return g_selectedPoint;
	}
	
	public Dimension getPreferredSize() {        // Return the size of the graph
		return new Dimension(g_squareW, g_squareH);
	}
	
	// Setters for the variables. 
	
	public void setSelectedShape(Shape point) {			// Set the selected shape.
		g_selectedPoint = point; 
	}
	
	public void setLabels(int[][] g_axisLabels) {			// Set the axis labels.
		this.g_axisLabels = g_axisLabels;
	}
	
	public void setTrades(double[][] g_trades) {			// Set the trades.
		this.g_trades = g_trades;
	}
	
	public void setXValue(int g_xValue) {			// Set the selected header index for the x axis.
		this.g_xValue = g_xValue;
	}
	
	public void setYValue(int g_yValue) {			// Set the selected header index for the y axis.
		this.g_yValue = g_yValue;
	}
	
	//-----------------------------

	protected void paintComponent(Graphics g) {		// Paint the graph.
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.WHITE);					// The background of the graph is white.
		g.fillRect(g_squareX + g_widthLeftShift, g_squareY, g_squareW - g_widthRightShift, g_squareH - g_heightShift); 	// Don't cover everything in white, 
																									// leave space on the left (widthleftshift) and bottom (heightshift) 
																									// for the axis labels, as well as on the right (widthrightshift) for aesthetic purposes.
		g.setColor(Color.BLACK);			// Draw the two axis lines. 
		g.drawLine(g_squareX + g_widthLeftShift, g_squareY, g_squareX + g_widthLeftShift, g_squareH - g_heightShift); 	// Draw the y axis only at the bottom of the white (adjusting with the shifts).
		g.drawLine(g_squareX + g_widthLeftShift, g_squareH - g_heightShift, g_squareW - g_pointRadius, g_squareH - g_heightShift); // Same with the x axis. 

		drawYAxisTicks(g, g2);		// Functionality is in the name.
		drawXAxisTicks(g);

		makeDots(g);	//	Collect the points into g_pointlist.

		for (Shape point : g_pointList) {			// Draw all the points in the list of points. 
			g2.draw(point);
		}

		if (g_selectedPoint != null) {						// If a point is clicked on, fill it with red.
			Graphics2D otherG2 = (Graphics2D) g2.create();
			otherG2.setColor(Color.RED);
			otherG2.fill(g_selectedPoint);
			otherG2.dispose();
		}
	}

	private void drawYAxisTicks(Graphics g, Graphics g2) {			// Draw the tick marks on the Y-axis as well as the labels. 
		FontMetrics fontMetrics = g2.getFontMetrics();				// Get the font metrics for the labels.
		int y = 0;
		for (int i = g_squareH - g_heightShift; i > 0; i = i - 110) {	// Draw tick marks and labels incrementally, every 110 (why not) pixels.
			g.drawLine(g_squareW-g_pointRadius, i, 37, i);				// Shift each tick mark by the radius of the points, to center it.
			String str = String.valueOf(g_axisLabels[y][g_yValue]);
			g2.drawString(str, 35 - fontMetrics.stringWidth(str), i + 5);		// Draw the labels, but right aligned (shift them to the right by 35-their length).
			y++;
		}
	}

	private void drawXAxisTicks(Graphics g) {			// Draw the tick marks on the X-axis as well as the labels. 
		int x = 0;
		for (int i = g_squareW - 55; i > 0; i = i - 110) {	//Draw tick marks and labels incrementally, every 110 (why not) pixels.	
			g.drawLine(i - 15, g_squareY, i - 15, g_squareH - 15);
			g.drawString(String.valueOf(g_axisLabels[g_axisLabels.length - 1 - x][g_xValue]), i - 20, g_squareH);  //Draw the labels backwards, from the highest to the lowest,
			x++;																								// because we are counting backwards. 
		}
	}

	protected void makeDots(Graphics g) {		// Create the dots for the points. 
		g_pointList.clear();					// Each time something happens (new file, new axis), get rid of the points and make the new ones.
		int[] coordinates;
		for (int i = 0; i < g_trades.length; i++) {		// For the number of lines there is:
			coordinates = transformToScale(g_trades[i][g_xValue], g_trades[i][g_yValue]);	// transform the raw numbers into the pixel shift on screen
			g.setColor(Color.BLUE);				
			Shape shape = new Ellipse2D.Double(coordinates[0], coordinates[1], g_pointRadius*2, g_pointRadius*2);	// make the circle point
			g_pointList.add(shape);		// and add it to the list of points.
		}
	}

	private int[] transformToScale(double xRaw, double yRaw) {		// Scale the points so that they are represented accurately on the graph.
		int[] transformed = new int[2];								// Array that holds the coordinates of the transformed points.
		transformed[0] = (int) (((xRaw - g_axisLabels[0][g_xValue]) / (g_axisLabels[4][g_xValue] - g_axisLabels[0][g_xValue]))
				* ((g_squareW - 70) - (g_squareX + g_widthLeftShift)) + g_widthLeftShift - g_pointRadius);		// The x shift equation is basically [(xvalue-column_minimum)/(column_maximum-column_minimum)] * width of white area + shift - point radius
		transformed[1] = (int) ((g_squareH - 20)				// The y shift equation is similar but it must be inverted (height - coordinate)					
				- (((yRaw - g_axisLabels[0][g_yValue]) / (g_axisLabels[4][g_yValue] - g_axisLabels[0][g_yValue])) * (g_squareH - g_heightShift - 40)) - g_pointRadius);
		return transformed;
	}

}