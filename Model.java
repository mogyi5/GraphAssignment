import java.io.*;

public class Model {

	// Declare variables. 
	
	private int m_lineCount;				// Count the number of lines of trade values (discounting the headers).
	private BufferedReader m_br;			// Buffered reader to read the csv in.
	protected String[] m_headers;			// A string array for storing the headers.	
	protected double[][] m_trades;			// A 2d array to store the trade numbers, which may be double.
	private double[] m_minVal, m_maxVal;	// Two arrays to store the minimum and maximum values for every column.
	protected int[][] m_axisLabels;			// An array to store the values of the tick marks on the axes.

	Model() {  			// An empty constructor.
	}
	
	// Getters for the variables.
	
	public String[] getHeaders() {			// Return the headers.
		return m_headers;
	}
	
	public double[][] getTrades() { 		// Return the trades array.
		return m_trades;
	}
		
	public int[][] getAxisLabels() {		// Return axis labels.
		return m_axisLabels;
	}
	
	// Other methods. 
	
	protected void readHeader(File theCSV) { 	// A method to read the headers only.
		String line = "";					// Create place for the buffered reader to read the line into.
		m_lineCount = -1;					// Start line count at -1 because after header is read it will be 0, and so			
		try {								// it will give us an accurate read of the number of lines of trade values.
			m_br = new BufferedReader(new FileReader(theCSV));
			while ((line = m_br.readLine()) != null) {
				if (m_lineCount == -1) {			// Only read the first line this way: if lineCount != -1, we just count the lines.
					m_headers = line.split(","); 	// Read in the csv's line to the variable "line".
				}
				m_lineCount++; 				// Keep adding to the lineCount to find out the actual number of lines.
			}
		} catch (IOException e) { 			// Catching IOExceptions.
			System.err.println("IOException caught");
		} finally {
			if (m_br != null) { 			// Try closing the file if there is 
				try {						// nothing else to read, else show an error.
					m_br.close();
				} catch (IOException e) {
					System.err.println("Cannot close file");
				}
			}
		}
	}

	protected void readTrades(File theCSV) {	// A method to read in the trade values only. This is done separately as we need to  
		String line = "";					// know the number of lines before creating the trades array (this is obtained while reading the headers).
		m_trades = new double[m_lineCount][m_headers.length];	//Create array to put in the trade values.

		try {
			int iteration = 0;				// This iteration is actually the line number+1, as we re-read the header (but it is not used in anything)
			m_br = new BufferedReader(new FileReader(theCSV));		// (I am sure there is an easier way, but this works).
			while ((line = m_br.readLine()) != null) {				// While there is more to read, read it.
				String[] splitLine = line.split(",");				// Split the line along the commas.
				double[] doubleSplitLine = new double[splitLine.length];	// Array to store double values.

				if (iteration != 0) {								// If it is not the first iteration, (header), 
					for (int i = 0; i < splitLine.length; i++) {	// for each column
						doubleSplitLine[i] = Double.parseDouble(splitLine[i]);	// convert the string to double values
					}															// and assign the values to doubleSplitLine.
					m_trades[iteration - 1] = doubleSplitLine;		// Set the trades array equal to the doubles array.
				}
				iteration++;
			}
		} catch (IOException e) {				// Catching IOExceptions.
			System.err.println("IOException caught");
		} finally {
			if (m_br != null) {					// Try closing the file if there is 
				try {							// nothing else to read, else show an error.
					m_br.close();
				} catch (IOException e) {
					System.err.println("Cannot close file");
				}
			}
		}
	}

	protected void calculateAxisDetails() { 	 // Method to create the axis limits and the other labels too
		calculateAxisLimits();
		calculateAxisLabels();
	}

	private void calculateAxisLimits() {		//	Calculate the maximum and minimum values for the axes on the graph
		m_minVal = new double[m_headers.length];	// Store the minimum values for all columns
		m_maxVal = new double[m_headers.length];	// Store the maximum values for all columns

		for (int k = 0; k < m_minVal.length; k++) {		// Set all minimums to 1000 and all maximums to -1000,
			m_minVal[k] = 1000.0;						// this way the values will change accurately when they updated 
			m_maxVal[k] = -1000.0;						// to reflect the true minimums and maximums.
		}

		for (int i = 0; i < m_trades.length; i++) {		// For each trade lines
			for (int j = 0; j < m_trades[i].length; j++) {	// for each trade column
				if (m_trades[i][j] > m_maxVal[j]) {			// if the value is bigger then the maximum for that column
					m_maxVal[j] = m_trades[i][j];			// update it to be the new value. 
				}
				if (m_trades[i][j] < m_minVal[j]) {			// Similar to before, but minimums. 
					m_minVal[j] = m_trades[i][j];
				}
			}
		}
	}

	private void calculateAxisLabels() {				// Calculate the axis labels
		m_axisLabels = new int[5][m_headers.length];	// for 5 tick marks on each axis. 

		for (int i = 0; i < m_headers.length; i++) {	// For every column,
			m_axisLabels[4][i] = (int) Math.ceil(m_maxVal[i]);	// make the ceiling of the biggest value tick mark 5
			m_axisLabels[0][i] = (int) Math.floor(m_minVal[i]); // the floor of the smallest value tick mark 1
			m_axisLabels[2][i] = (int) ((m_axisLabels[4][i] + m_axisLabels[0][i]) / 2);	// for the third one split the difference
			m_axisLabels[1][i] = (int) ((m_axisLabels[2][i] + m_axisLabels[0][i]) / 2); // the second one halfway between the first and third
			m_axisLabels[3][i] = (int) ((m_axisLabels[2][i] + m_axisLabels[4][i]) / 2); // the fourth one halfway between the third and fifth
		} // You could just get the difference between max and min and divide by 5 but that gave me weird values and I got paranoid 
	} // so I ended up doing it this way. Please don't judge!

}
