package test;

import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * An example that shows a JToolbar, as well as a JList, JTable, JSplitPane and JTree
 */
public class MoreSwingComponents extends javax.swing.JFrame {
	private javax.swing.JPanel ivjJFrameContentPane = null;

	private javax.swing.JPanel ivjJPanel = null;

	private JToolBar jToolBar = null;

	private JMenu jMenu = null;

	private JMenuItem jMenuItem = null;

	public MoreSwingComponents() {
		super();
		initialize();
	}

	/**
	 * Return the JFrameContentPane property value.
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			ivjJFrameContentPane = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout_3 = new java.awt.BorderLayout();
			ivjJFrameContentPane.setLayout(layBorderLayout_3);
			ivjJFrameContentPane.add(getIvjJPanel(), java.awt.BorderLayout.CENTER);
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.add(getJToolBar(), java.awt.BorderLayout.NORTH);
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {
		this.setContentPane(getJFrameContentPane());
		this.setName("JFrame1");
		this.setTitle("More Swing Components");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(23, 36, 526, 301);
	}

	/**
	 * This method initializes ivjJPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getIvjJPanel() {
		if (ivjJPanel == null) {
			ivjJPanel = new javax.swing.JPanel();
			java.awt.GridLayout layGridLayout_4 = new java.awt.GridLayout();
			layGridLayout_4.setRows(2);
			layGridLayout_4.setColumns(2);
			ivjJPanel.setLayout(layGridLayout_4);
		}
		return ivjJPanel;
	}

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
			jToolBar.add(getJMenu());
		}
		return jToolBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.add(getJMenuItem());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
		}
		return jMenuItem;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="0,0"
