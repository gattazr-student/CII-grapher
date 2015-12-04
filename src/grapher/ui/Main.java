package grapher.ui;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.SwingUtilities;


public class Main extends JFrame {
	Main(String title, String[] expressions) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Grapher wGrapher = new Grapher();
		SidePanel wSidePanel = new SidePanel();

		for(String expression : expressions) {
			wSidePanel.addExpression(expression);
			wGrapher.add(expression);
		}

		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wSidePanel, wGrapher));
		pack();
	}

	public static void main(String[] argv) {
		final String[] expressions = argv;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main("grapher", expressions).setVisible(true);
			}
		});
	}
}
