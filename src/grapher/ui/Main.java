package grapher.ui;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.ListModel;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;

import grapher.fc.Function;
import grapher.fc.FunctionFactory;

public class Main extends JFrame {

	private Grapher pGrapher;
	private SidePanel pSidePanel;
	private DefaultListModel<Function> pFunctions;

	Main(String title, String[] expressions) {
		super(title);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		/* Create a ListModel that will contains all the Functions */
		this.pFunctions = new DefaultListModel<Function>();

		/* Create the add and del actions here */
		Action wAddAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				String wFunction = JOptionPane.showInputDialog(Main.this, "Nouvelle expression :");
				Main.this.pFunctions.addElement(FunctionFactory.createFunction(wFunction));
				Main.this.repaint();
			}
		};
		Action wDelAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				int[] wIndices = Main.this.pSidePanel.getSelectedIndices();
				/* Parse the array in reverse */
				for(int wI = wIndices.length-1; wI >= 0; wI--){
					Main.this.pFunctions.remove(wIndices[wI]);
				}
				Main.this.repaint();
			}
		};

		/* Create the Grapher and SidePanel */
		this.pGrapher = new Grapher(this.pFunctions);
		this.pSidePanel = new SidePanel(this.pFunctions, wAddAction, wDelAction);

		/* Add all functions given to the program as paramters to the model */
		for(String expression : expressions) {
			this.pFunctions.addElement(FunctionFactory.createFunction(expression));
		}

		/* add the grapher and Sidepanel to a JSplitPane in the JFrame */
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.pSidePanel, this.pGrapher));
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
