public class Main {
	public static void main(String[] args) {		// Create the MVC objects for our graph.
		Model model = new Model();					// In this case model doesn't care about controller because nothing is being altered.
		View view = new View(model);
		Controller controller = new Controller(model, view);

		view.setVisible(true);
	}
}
