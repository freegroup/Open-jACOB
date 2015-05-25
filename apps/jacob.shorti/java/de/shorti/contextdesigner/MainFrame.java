/**
 * Title:        FREEObject
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      FreeGroup
 * @author Andreas Herz
 * @version 1.0
 */
package de.shorti.contextdesigner;

import java.io.*;
import java.util.*;
import de.shorti.*;
import de.shorti.dragdrop.*;
import de.shorti.dragdrop.examples.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import de.shorti.contextdesigner.objects.*;

public class MainFrame extends JFrame implements TableModelListener
{
    static Transition currentTransition = null;
    JPanel contentPane;
    JMenuBar  menuBar       = new JMenuBar();

    JMenu     menuFile      = new JMenu();
    JMenuItem menuFileExit  = new JMenuItem();

    JMenu     menuHelp      = new JMenu();
    JMenuItem menuHelpAbout = new JMenuItem();

    JToolBar  toolBar       = new JToolBar();

    JTextField    statusBar     = new JTextField();
    BorderLayout  mainLayout    = new BorderLayout();
    JSplitPane    mainSplitPane = new JSplitPane();
    JSplitPane    horiSplitPane = new JSplitPane();
    HashMap       contextViews  = new HashMap();
    JList         contextToolBar= new JList();
    JScrollPane   contextToolBarScrollPane = null;
    JTable        patternTable  = null;

    JButton fileOpenButton  = new JButton();
    JButton fileSaveButton  = new JButton();
    ContextView currentView = null;
    Perl5PatternRenderer render = new Perl5PatternRenderer();
    static DefaultTableModel   dm=null;
    static Vector dummyHeader = new Vector();
    static boolean duringUpdate=false;

    public MainFrame()
    {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentTransition(Transition trans)
    {
        try
        {
            duringUpdate=true;
            currentTransition = trans;
            dm.setDataVector(new Vector(),dummyHeader);

            Iterator iter =trans.getActions().iterator();
            while(iter.hasNext())
            {
                Transition.Action a = (Transition.Action)iter.next();
                Object[] entry = {a.pattern ,"", a.defaulAnswer};
                dm.addRow(entry);
            }
            // add additional 10 empty rows
            for(int i=0;i<10;i++)
            {
                Object[] empty={"", "", ""};
                dm.addRow(empty);
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            duringUpdate=false;
        }
    }

    /**Component initialization*/
    private void jbInit() throws Exception
    {
        contextToolBar = new JList(getContextsAsStrings());
        contextToolBarScrollPane = new JScrollPane(contextToolBar);

        setSize(new Dimension(529, 448));
        setTitle("ContextDesigner");

        statusBar.setText("Status bar");
        statusBar.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                statusBar_keyReleased(e);
            }
        });

        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener()  {

            public void actionPerformed(ActionEvent e) {
                onMenuFileExit(e);
            }
        });


        toolBar.setDoubleBuffered(true);
        contextToolBar.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                contextToolBar_valueChanged(e);
            }
        });
        fileOpenButton.setText("Load");
        fileSaveButton.setText("Save");
        fileSaveButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fileSaveButton_actionPerformed(e);
            }
        });
        toolBar.add(fileOpenButton);
        toolBar.add(fileSaveButton);

        menuFile.add(menuFileExit);

        setJMenuBar(menuBar);

        mainSplitPane.add(new JList(),   JSplitPane.RIGHT);
        mainSplitPane.add(contextToolBarScrollPane, JSplitPane.LEFT);
        mainSplitPane.setDividerLocation(150);

        horiSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        horiSplitPane.add(mainSplitPane, JSplitPane.TOP);

        /**
         * TESTEST
         */
         Vector data  = new Vector();
         Vector data1 = new Vector();
         data.add(data1);
        dummyHeader.addElement("Pattern");
        dummyHeader.addElement("extracted data");
        dummyHeader.addElement("DefaultMessage");
        dm = new DefaultTableModel();
        dm.setColumnIdentifiers(dummyHeader);
        dm.addTableModelListener(this);
        patternTable = new JTable( dm );

        patternTable.setFont(new Font("Arial",Font.PLAIN,12));
        patternTable.setShowGrid(true);
        patternTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patternTable.setRowHeight(patternTable.getRowHeight()+8);
        patternTable.setDefaultRenderer(Object.class, render);
        patternTable.setEditingColumn(0);
        JScrollPane scrollTable = new JScrollPane( patternTable );

        horiSplitPane.add(scrollTable, JSplitPane.BOTTOM);
        horiSplitPane.setDividerLocation(150);

        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(mainLayout);
        contentPane.add(toolBar      , BorderLayout.NORTH);
        toolBar.add(fileOpenButton, null);
        contentPane.add(statusBar    , BorderLayout.SOUTH);
        contentPane.add(horiSplitPane, BorderLayout.CENTER);
    }


    /**Overridden so we can exit when window is closed*/
    protected void processWindowEvent(WindowEvent e)
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            onMenuFileExit(null);
        }
    }

    /**File | Exit action performed*/
    void onMenuFileExit(ActionEvent e)
    {
        System.exit(0);
    }

    /**
     *
     */
    private String[] getContextsAsStrings()
    {
        ArrayList files = de.shorti.util.file.Dir.getAll(new File("/home/andherz/workspace/jacob.shorti/context"),false);
        String[] result = new String[files.size()];
        for(int i=0; i<files.size() ; i++)
        {
            File file = (File)files.get(i);
            result[i] = file.getName();
            ContextView contextView = new ContextView(result[i], file);
            contextViews.put(result[i],contextView);
        }

        return result;
    }

    /**
     *
     */
    void contextToolBar_valueChanged(ListSelectionEvent e)
    {
        JList list = (JList)e.getSource();
        currentView = (ContextView)contextViews.get(list.getSelectedValue());
        mainSplitPane.add(currentView, JSplitPane.RIGHT );
    }

    /**
     *
     */
    void fileSaveButton_actionPerformed(ActionEvent e)
    {
        Iterator iter = contextViews.keySet().iterator();
        while(iter.hasNext())
        {
            ContextView view = (ContextView)contextViews.get(iter.next());
            view.save();
        }
    }

    public void tableChanged(TableModelEvent e)
    {
        if(duringUpdate)
            return;

        int row = e.getFirstRow();
        int column = e.getColumn();
        if(currentTransition!=null)
        {
            currentTransition.resetActions();
            DefaultTableModel model =(DefaultTableModel) e.getSource();
            int rows = model.getRowCount();
            for(int i=0;i<rows;i++)
            {
                String pattern        =(String) model.getValueAt(i, 0);
                String exctraced      =(String) model.getValueAt(i, 1);
                String defaultMessage =(String) model.getValueAt(i, 2);
                if(pattern!=null && !pattern.equals(""))
                {
                    currentTransition.addPattern(pattern,defaultMessage);
                }
            }
        }
        patternTable.invalidate();
        patternTable.repaint();
    }

    void statusBar_keyReleased(KeyEvent e)
    {
        String question = statusBar.getText();
        // check the pattern
        //
        render.setQuestion(question.toLowerCase());
        patternTable.invalidate();
        patternTable.repaint();
    }
}