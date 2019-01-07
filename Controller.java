import java.awt.Shape;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controller {

	private Model c_model;			// Objects for the model, view and graph.
	private View c_view;
	private Graph c_graph;

	Controller(Model model, View view) {    // Constructor.
		this.c_model = model;
		this.c_view = view;
		c_graph = view.getGraph();

		view.addQuitListener(new QuitListener());			// Add all listeners to view and graph. 
		view.addBrowserListener(new BrowserListener());
		view.addComboListener(new ComboListener());
		c_graph.addMouseListener(new ClickListener());
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public class QuitListener implements ActionListener {	// On click, quit the program. 
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	public class BrowserListener implements ActionListener {	// File chooser opens on click.
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));  // Start the file search in current dir.

			fileChooser.setAcceptAllFileFilterUsed(false);		
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files (*csv)", "csv");			// Only accept csv files.
			fileChooser.setFileFilter(filter);

			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {			// If a file has been chosen, 
				c_model.readHeader(fileChooser.getSelectedFile());	// read the headers
				c_model.readTrades(fileChooser.getSelectedFile());	// read the trades
				c_view.updateComboPanel(c_model.m_headers);			// update the comboboxes accordingly,
				c_model.calculateAxisDetails();						// calculate the details of the axes,
				c_graph.setLabels(c_model.getAxisLabels());			// update the graph labels to match the model labels,		
				c_graph.setTrades(c_model.getTrades());				// update graph trades to match model trades,	
				c_view.getYComboBox().setSelectedIndex(1);					// set second combo box to show second item,
				c_view.getTopTextField().setText(fileChooser.getSelectedFile().getName().toString());		// and set the textfield to show the filename.
			}
		}
	}

	public class ComboListener implements ActionListener {			// If something is selected in the combobox,
		public void actionPerformed(ActionEvent e) {

			if (c_view.getXComboBox().getSelectedIndex() != -1) {			// if it is the first combobox, 
				c_graph.setXValue(c_view.getXComboBox().getSelectedIndex());	//	set the xValue (selected column) in graph to be equal to the combobox selection.
				c_graph.setSelectedShape(null);						// reset the selected shape.
			}
			if (c_view.getYComboBox().getSelectedIndex() != -1) {		// Similar with the second combobox.
				c_graph.setYValue(c_view.getYComboBox().getSelectedIndex());
				c_graph.setSelectedShape(null);
			}
			c_view.changeDescription(1);		//	Set the description back to default.
			c_graph.repaint();

		}
	}

	public class ClickListener extends MouseAdapter {			// If a point has been clicked,
		public void mousePressed(MouseEvent e) {
			for (int i = c_graph.getPointList().size() - 1; i >= 0; i--) {			// search all points in the list,
				if (c_graph.getPointList().get(i).contains(e.getPoint())) {			// if the point is found,
					c_graph.setSelectedShape(c_graph.getPointList().get(i));		// set it to be the selected shape,
					c_view.changeDescription(i);			// rewrite the description in the textarea to reflect the particular line in the trades,
					c_graph.repaint();		// and repaint the graph.
					return;
				}
			}
		}
	}

}
