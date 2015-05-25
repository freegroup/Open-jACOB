//
// 
//  
// Source File Name:   GridOptionsDialog.java

package de.shorti.dragdrop.examples.demo1;





import de.shorti.dragdrop.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class GridOptionsDialog extends JDialog
{

    public GridOptionsDialog(Frame frame, String s, boolean flag, FREEGridView gridview)
    {
        super(frame, s, flag);
        panel1 = new JPanel();
        OKButton = new JButton();
        CancelButton = new JButton();
        label4 = new JLabel();
        widthField = new JTextField();
        label1 = new JLabel();
        heightField = new JTextField();
        gridStyleGroup = new ButtonGroup();
        gridInvisibleRadio = new JRadioButton();
        gridDotsRadio = new JRadioButton();
        gridCrossesRadio = new JRadioButton();
        gridLinesRadio = new JRadioButton();
        label2 = new JLabel();
        label3 = new JLabel();
        paperColorButton = new JButton();
        moveSnapGroup = new ButtonGroup();
        moveNoSnapRadio = new JRadioButton();
        moveJumpRadio = new JRadioButton();
        moveAfterRadio = new JRadioButton();
        jLabel2 = new JLabel();
        resizeSnapGroup = new ButtonGroup();
        resizeAfterRadio = new JRadioButton();
        resizeJumpRadio = new JRadioButton();
        resizeNoSnapRadio = new JRadioButton();
        jLabel3 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        fontName = new JTextField();
        fontSize = new JTextField();
        m_view = null;
        fComponentsAdjusted = false;
        try
        {
            jbInit();
            pack();
            m_view = gridview;
            UpdateDialog();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public GridOptionsDialog()
    {
        this(null, "", false, null);
    }

    void jbInit()
        throws Exception
    {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(586, 185));
        panel1.setPreferredSize(new Dimension(586, 185));
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                OKButton_actionPerformed(actionevent);
            }

        });
        CancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                CancelButton_actionPerformed(actionevent);
            }

        });
        OKButton.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                OKButton_keyPressed(keyevent);
            }

        });
        CancelButton.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent)
            {
                CancelButton_keyPressed(keyevent);
            }

        });
        paperColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                paperColorButton_actionPerformed(actionevent);
            }

        });
        moveNoSnapRadio.setText("No snap");
        moveNoSnapRadio.setBounds(new Rectangle(255, 36, 86, 24));
        moveJumpRadio.setText("Jump");
        moveJumpRadio.setBounds(new Rectangle(255, 60, 86, 24));
        moveAfterRadio.setText("Afterwards");
        moveAfterRadio.setBounds(new Rectangle(255, 84, 86, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        jLabel2.setBounds(new Rectangle(339, 13, 98, 24));
        jLabel2.setText("Snap On Move:");
        resizeAfterRadio.setText("Afterwards");
        resizeAfterRadio.setBounds(new Rectangle(354, 82, 86, 24));
        resizeJumpRadio.setText("Jump");
        resizeJumpRadio.setBounds(new Rectangle(354, 58, 86, 24));
        resizeNoSnapRadio.setText("No snap");
        resizeNoSnapRadio.setBounds(new Rectangle(354, 34, 86, 24));
        jLabel3.setText("Snap on Resize:");
        jLabel3.setBounds(new Rectangle(340, 12, 98, 24));
        getContentPane().add(panel1);
        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(201, 148, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(309, 148, 79, 22));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(30, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(90, 36, 36, 24));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        panel1.add(label1);
        label1.setBounds(new Rectangle(30, 60, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(90, 60, 36, 24));
        gridInvisibleRadio.setText("Invisible");
        panel1.add(gridInvisibleRadio);
        gridStyleGroup.add(gridInvisibleRadio);
        gridInvisibleRadio.setBounds(new Rectangle(161, 36, 86, 24));
        gridDotsRadio.setText("Dots");
        gridStyleGroup.add(gridDotsRadio);
        panel1.add(gridDotsRadio);
        gridDotsRadio.setBounds(new Rectangle(161, 60, 86, 24));
        gridCrossesRadio.setText("Crosses");
        panel1.add(gridCrossesRadio);
        gridStyleGroup.add(gridCrossesRadio);
        gridCrossesRadio.setBounds(new Rectangle(161, 84, 86, 24));
        gridLinesRadio.setText("Lines");
        panel1.add(gridLinesRadio);
        gridStyleGroup.add(gridLinesRadio);
        gridLinesRadio.setBounds(new Rectangle(161, 108, 86, 24));
        label2.setText("Grid Style:");
        panel1.add(label2);
        label2.setBounds(new Rectangle(149, 12, 98, 24));
        label3.setText("Grid Size:");
        panel1.add(label3);
        label3.setBounds(new Rectangle(29, 12, 96, 24));
        paperColorButton.setText("Paper Color...");
        panel1.add(paperColorButton);
        moveSnapGroup.add(moveNoSnapRadio);
        panel1.add(moveNoSnapRadio, null);
        moveSnapGroup.add(moveJumpRadio);
        panel1.add(moveJumpRadio, null);
        moveSnapGroup.add(moveAfterRadio);
        panel1.add(moveAfterRadio, null);
        panel1.add(jLabel2, null);
        resizeSnapGroup.add(resizeNoSnapRadio);
        panel1.add(resizeNoSnapRadio, null);
        resizeSnapGroup.add(resizeJumpRadio);
        panel1.add(resizeJumpRadio, null);
        resizeSnapGroup.add(resizeAfterRadio);
        panel1.add(resizeAfterRadio, null);
        panel1.add(jLabel3, null);
        panel1.add(CancelButton);
        panel1.add(OKButton);
        paperColorButton.setBackground(Color.lightGray);
        paperColorButton.setBounds(new Rectangle(24, 108, 116, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        label5.setText("Default Text Properties:");
        panel1.add(label5);
        label5.setBounds(new Rectangle(445, 12, 130, 24));
        label6.setText("Default Font Name:");
        panel1.add(label6);
        label6.setBounds(new Rectangle(445, 34, 130, 24));
        panel1.add(fontName);
        fontName.setBounds(new Rectangle(445, 58, 130, 24));
        label7.setText("Default Font Size:");
        panel1.add(label7);
        label7.setBounds(new Rectangle(445, 82, 130, 24));
        panel1.add(fontSize);
        fontSize.setBounds(new Rectangle(445, 106, 130, 24));
    }

    void UpdateDialog()
    {
        if(m_view == null)
            return;
        widthField.setText(String.valueOf(m_view.getGridWidth()));
        heightField.setText(String.valueOf(m_view.getGridHeight()));
        int i = m_view.getGridStyle();
        if(i == 0)
            gridInvisibleRadio.setSelected(true);
        else
        if(i == 1)
            gridDotsRadio.setSelected(true);
        else
        if(i == 2)
            gridCrossesRadio.setSelected(true);
        else
        if(i == 3)
            gridLinesRadio.setSelected(true);
        int j = m_view.getSnapMove();
        if(j == 0)
            moveNoSnapRadio.setSelected(true);
        else
        if(j == 1)
            moveJumpRadio.setSelected(true);
        else
        if(j == 2)
            moveAfterRadio.setSelected(true);
        int k = m_view.getSnapResize();
        if(k == 0)
            resizeNoSnapRadio.setSelected(true);
        else
        if(k == 1)
            resizeJumpRadio.setSelected(true);
        else
        if(k == 2)
            resizeAfterRadio.setSelected(true);
        m_paperColor = m_view.getDocument().getPaperColor();
        fontSize.setText(String.valueOf(FREEText.getDefaultFontSize()));
        fontName.setText(FREEText.getDefaultFontFaceName());
    }

    void UpdateControl()
    {
        if(m_view == null)
            return;
        m_view.setGridWidth(Integer.parseInt(widthField.getText()));
        m_view.setGridHeight(Integer.parseInt(heightField.getText()));
        byte byte0 = 0;
        if(gridInvisibleRadio.isSelected())
            byte0 = 0;
        else
        if(gridDotsRadio.isSelected())
            byte0 = 1;
        else
        if(gridCrossesRadio.isSelected())
            byte0 = 2;
        else
        if(gridLinesRadio.isSelected())
            byte0 = 3;
        m_view.setGridStyle(byte0);
        byte byte1 = 0;
        if(moveNoSnapRadio.isSelected())
            byte1 = 0;
        else
        if(moveJumpRadio.isSelected())
            byte1 = 1;
        else
        if(moveAfterRadio.isSelected())
            byte1 = 2;
        m_view.setSnapMove(byte1);
        byte byte2 = 0;
        if(resizeNoSnapRadio.isSelected())
            byte2 = 0;
        else
        if(resizeJumpRadio.isSelected())
            byte2 = 1;
        else
        if(resizeAfterRadio.isSelected())
            byte2 = 2;
        m_view.setSnapResize(byte2);
        m_view.getDocument().setPaperColor(m_paperColor);
        FREEText.setDefaultFontFaceName(fontName.getText());
        FREEText.setDefaultFontSize(Integer.parseInt(fontSize.getText()));
    }

    public void addNotify()
    {
        Dimension dimension = getSize();
        super.addNotify();
        if(fComponentsAdjusted)
            return;
        Insets insets = getInsets();
        setSize(insets.left + insets.right + dimension.width, insets.top + insets.bottom + dimension.height);
        Component acomponent[] = getComponents();
        for(int i = 0; i < acomponent.length; i++)
        {
            Point point = acomponent[i].getLocation();
            point.translate(insets.left, insets.top);
            acomponent[i].setLocation(point);
        }

        fComponentsAdjusted = true;
    }

    public void setVisible(boolean flag)
    {
        if(flag)
        {
            Rectangle rectangle = getParent().getBounds();
            Rectangle rectangle1 = getBounds();
            setLocation(rectangle.x + (rectangle.width - rectangle1.width) / 2, rectangle.y + (rectangle.height - rectangle1.height) / 2);
        }
        super.setVisible(flag);
    }

    void OKButton_actionPerformed(ActionEvent actionevent)
    {
        OnOK();
    }

    void OnOK()
    {
        try
        {
            UpdateControl();
            dispose();
        }
        catch(Exception exception) { }
    }

    void CancelButton_actionPerformed(ActionEvent actionevent)
    {
        OnCancel();
    }

    void OnCancel()
    {
        try
        {
            dispose();
        }
        catch(Exception exception) { }
    }

    void OKButton_keyPressed(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == 10)
            OnOK();
        else
        if(keyevent.getKeyCode() == 27)
            OnCancel();
    }

    void CancelButton_keyPressed(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == 10)
            OnCancel();
        else
        if(keyevent.getKeyCode() == 27)
            OnCancel();
    }

    void paperColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Paper Color", m_paperColor);
        if(color != null)
            m_paperColor = color;
    }

    JPanel panel1;
    JButton OKButton;
    JButton CancelButton;
    JLabel label4;
    JTextField widthField;
    JLabel label1;
    JTextField heightField;
    ButtonGroup gridStyleGroup;
    JRadioButton gridInvisibleRadio;
    JRadioButton gridDotsRadio;
    JRadioButton gridCrossesRadio;
    JRadioButton gridLinesRadio;
    JLabel label2;
    JLabel label3;
    JButton paperColorButton;
    ButtonGroup moveSnapGroup;
    JRadioButton moveNoSnapRadio;
    JRadioButton moveJumpRadio;
    JRadioButton moveAfterRadio;
    JLabel jLabel2;
    ButtonGroup resizeSnapGroup;
    JRadioButton resizeAfterRadio;
    JRadioButton resizeJumpRadio;
    JRadioButton resizeNoSnapRadio;
    JLabel jLabel3;
    JLabel label5;
    JLabel label6;
    JLabel label7;
    JTextField fontName;
    JTextField fontSize;
    Color m_paperColor;
    public FREEGridView m_view;
    boolean fComponentsAdjusted;
}
