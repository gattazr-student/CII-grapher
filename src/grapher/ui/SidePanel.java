package grapher.ui;

import javax.swing.JList;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;


public class SidePanel extends JPanel {

    private DefaultListModel<String> pListModel;

    public SidePanel(){
        super();

        setLayout(new BorderLayout());

        /* Creation of the toolbar and the buttons */
        JToolBar wToolBar = new JToolBar();
        JButton wAdd = new JButton("+");
        JButton wDel = new JButton("-");
        wToolBar.add(wAdd);
        wToolBar.addSeparator();
        wToolBar.add(wDel);
        wToolBar.setFloatable(false);

        /* Creation of the JList */
        this.pListModel = new DefaultListModel<String>();
		JList wList = new JList(this.pListModel);

        /* add JPANEL of buttons at the bottom */
        add(wToolBar, BorderLayout.SOUTH);

        /* add JLIST at the center */
        add(wList , BorderLayout.CENTER);
    }

    public void addExpression(String aExpression){
        pListModel.addElement(aExpression);
    }

}
