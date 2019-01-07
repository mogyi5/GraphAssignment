import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class View extends JFrame {

	private static final long serialVersionUID = 1L; // Java shouted at me for not having a serial number.

	// Declare variables. 
	
	protected Graph v_graph;		// Import the graph class.

	JPanel v_topPanel;			// Three panels in the frame: top, middle and bottom. 
	JPanel v_middlePanel;
	JPanel v_bottomPanel;

	private String[] v_labelArray = { "X Axis", "Y Axis"};		// Array showing the contents of the comboboxes.
	private JButton v_openBtn = new JButton("Open");				// Button that says "open".
	protected JTextField v_filenameTF = new JTextField(35);			// Textbox with filename in it. 
	private JButton v_exitBtn = new JButton("Quit");				// Button that says "quit".
	protected JComboBox<String> v_xCB;								// Combobox for x-axis.
	protected JComboBox<String> v_yCB;								// Combobox for y-axis.
	private JTextArea v_descriptionTF = new JTextArea(5, 20);		// Text area for the descriptions.
																	// Because it is only five lines tall, it only supports 5 header variables.

	View(Model model) {			// Constructor.

		v_graph = new Graph(0, 0, 550, 500);		// Make our graph 550x500px large.

		v_topPanel = new JPanel();					// Top panel has the open button,
		v_openBtn.setPreferredSize(new Dimension(80, 30));
		v_topPanel.add(v_openBtn);					//add it to the top,
		
		v_filenameTF.setEditable(false);			// textfield	
		Font tr = new Font("Times", Font.BOLD, 15);	// with a beautiful font,
		v_filenameTF.setFont(tr);
		v_filenameTF.setHorizontalAlignment(JTextField.CENTER);	// center the text,
		v_topPanel.add(v_filenameTF);
		
		v_exitBtn.setPreferredSize(new Dimension(80, 30));	// and an exit button the same size as the open button.
		v_topPanel.add(v_exitBtn);

		v_middlePanel = new JPanel();				// Middle panel only has the graph.
		v_middlePanel.add(v_graph);

		v_bottomPanel = new JPanel();				// Bottom panel has two combo boxes,
		v_xCB = new JComboBox<String>(v_labelArray);	// one for x-axis and one for the y-axis
		v_yCB = new JComboBox<String>(v_labelArray);
		v_yCB.setSelectedIndex(1);					// where the second combobox starts from the second option,
		v_bottomPanel.add(v_xCB);
		v_bottomPanel.add(v_yCB);
		
		v_descriptionTF.setEditable(false);			// and it has a text area for showing trade details.
		v_descriptionTF.setText("<Select a trade to \n view its details>");
		v_bottomPanel.add(v_descriptionTF);

		this.setLocation(450, 150);					// Frame is located in a prime position on the screen.
		this.setLayout(new BorderLayout());			// The layout is a border layout with a top, bottom and middle.
		this.add(v_topPanel, BorderLayout.PAGE_START);
		this.add(v_middlePanel, BorderLayout.CENTER);
		this.add(v_bottomPanel, BorderLayout.PAGE_END);

		this.setTitle("Final Assignment");					
		this.pack();								// Good frame size. 
	}
	
	// Getters for the variables.
	
	public Graph getGraph() {			// Return the axis labels.
		return v_graph;
	}
	
	public JComboBox<String> getXComboBox() {			// Return the first combobox.
		return v_xCB;
	}
	
	public JComboBox<String> getYComboBox() {			// Return the second combobox.
		return v_yCB;
	}
	
	public JTextField getTopTextField() {			// Return the filename textfield.
		return v_filenameTF;
	}
	
	// Actionlisteners

	void addQuitListener(ActionListener al) {			// Add the action listener for closing to the quit button.
		v_exitBtn.addActionListener(al);
	}

	void addBrowserListener(ActionListener al) {		// Add the filebrowser listener to the open button.
		v_openBtn.addActionListener(al);
	}

	void addComboListener(ActionListener al) {			// Add the combobox listener to each combobox. 
		v_xCB.addActionListener(al);
		v_yCB.addActionListener(al);
	}
	
	// Other methods

	void updateComboPanel(String[] items) {					// A method for updating the contents of a combobox.

		DefaultComboBoxModel<String> model1 = (DefaultComboBoxModel<String>) v_xCB.getModel(); 	// Get each combobox's model separately.
		DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) v_yCB.getModel();

		model1.removeAllElements();		// Empty both of the models.
		model2.removeAllElements();

		for (String item : items) {
			model1.addElement(item);	// Add each new item from the String[] to both models.
			model2.addElement(item);
		}

		v_xCB.setModel(model1);			//	Set the models to be the new appended models for both comboboxes.
		v_yCB.setModel(model2);
	}

	void changeDescription(int i) {					// Method for changing the description in the textarea on the bottom right. 
		if (v_graph.g_selectedPoint == null) {		// If nothing is selected, put in generic text.
			v_descriptionTF.setText("<Select a trade to \n view its details>");
		} else {									// If something is selected, erase the previous text and print out the first header and its value,
			v_descriptionTF.setText(v_xCB.getItemAt(0) + ": " + String.format("%7s", String.valueOf(v_graph.g_trades[i][0])) + "\n");
			for (int j = 1; j < v_graph.g_trades[i].length; j++) {		// and add the rest of the header names too and their values in that line respectively.
				v_descriptionTF.append(v_xCB.getItemAt(j) + ": " + String.format("%7s", String.valueOf(v_graph.g_trades[i][j])) + "\n");
			}
		}
	}

}
