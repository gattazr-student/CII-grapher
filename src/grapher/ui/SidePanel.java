package grapher.ui;

import javax.swing.JList;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.Action;
import grapher.fc.Function;


public class SidePanel extends JPanel {

    private DefaultListModel<Function> pFunctions;
    private JList pList;

    public SidePanel(DefaultListModel<Function> aFunctions, Action aAddAction, Action aDelAction){
        super();

        setLayout(new BorderLayout());

        /* Creation of the toolbar and the buttons */
        JToolBar wToolBar = new JToolBar();
        JButton wAdd = new JButton(aAddAction);
        wAdd.setText("+");
        JButton wDel = new JButton(aDelAction);
        wDel.setText("-");
        wToolBar.add(wAdd);
        wToolBar.addSeparator();
        wToolBar.add(wDel);
        wToolBar.setFloatable(false);

        /* Creation of the JList */
        this.pFunctions = aFunctions;
		this.pList = new JList(this.pFunctions);

        /* add JToolBar at the bottom */
        add(wToolBar, BorderLayout.SOUTH);

        /* add JList at the center */
        add(this.pList, BorderLayout.CENTER);
    }

    public int[] getSelectedIndices(){
        return pList.getSelectedIndices();
    }

}
