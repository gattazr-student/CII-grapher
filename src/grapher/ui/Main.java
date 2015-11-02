package grapher.ui;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;


public class Main extends JFrame {
	Main(String title, String[] expressions) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Grapher grapher = new Grapher();

		DefaultListModel<String> wListModel = new DefaultListModel<String>();
		for(String expression : expressions) {
			wListModel.addElement(expression);
			grapher.add(expression);
		}

		JList wSidePanel = new JList(wListModel);

		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wSidePanel, grapher));
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
