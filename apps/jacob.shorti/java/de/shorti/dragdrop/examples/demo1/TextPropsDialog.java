//
// 
//  
// Source File Name:   TextPropsDialog.java

package de.shorti.dragdrop.examples.demo1;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.shorti.dragdrop.FREEText;

public class TextPropsDialog extends JDialog
{

    public TextPropsDialog(Frame frame, String s, boolean flag, FREEText text)
    {
        super(frame, s, flag);
        panel1 = new JPanel();
        OKButton = new JButton();
        CancelButton = new JButton();
        label1 = new JLabel();
        heightField = new JTextField();
        xField = new JTextField();
        label2 = new JLabel();
        yField = new JTextField();
        label3 = new JLabel();
        visibleBox = new JCheckBox();
        selectableBox = new JCheckBox();
        resizableBox = new JCheckBox();
        draggableBox = new JCheckBox();
        label4 = new JLabel();
        editableBox = new JCheckBox();
        boldBox = new JCheckBox();
        italicBox = new JCheckBox();
        underlineBox = new JCheckBox();
        strikeBox = new JCheckBox();
        textField = new JTextField();
        label5 = new JLabel();
        faceNameField = new JTextField();
        alignGroup = new ButtonGroup();
        alignLeftRadio = new JRadioButton();
        alignCenterRadio = new JRadioButton();
        alignRightRadio = new JRadioButton();
        multilineBox = new JCheckBox();
        label6 = new JLabel();
        fontSizeField = new JTextField();
        textColorButton = new JButton();
        backgroundColorButton = new JButton();
        transparentBox = new JCheckBox();
        textArea = new JTextArea();
        textAreaScroll = new JScrollPane(textArea);
        classNameLabel = new JLabel();
        editSingle = new JCheckBox();
        selectBack = new JCheckBox();
        twoDScale = new JCheckBox();
        clipping = new JCheckBox();
        autoResize = new JCheckBox();
        borderLayout1 = new BorderLayout();
        fComponentsAdjusted = false;
        try
        {
            jbInit();
            m_object = text;
            UpdateDialog();
            pack();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public TextPropsDialog()
    {
        this(null, "", false, null);
    }

    void jbInit()
        throws Exception
    {
        panel1.setLayout(null);
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
        textColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                textColorButton_actionPerformed(actionevent);
            }

        });
        backgroundColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                backgroundColorButton_actionPerformed(actionevent);
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
        panel1.setMinimumSize(new Dimension(545, 310));
        panel1.setPreferredSize(new Dimension(545, 310));
        getContentPane().add(panel1);
        setTitle("Text Properties");
        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(168, 272, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(276, 272, 79, 22));
        classNameLabel.setText("class name");
        classNameLabel.setBounds(new Rectangle(8, 4, 389, 24));
        panel1.add(classNameLabel);
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        label1.setBounds(new Rectangle(24, 36, 48, 24));
        panel1.add(label1);
        heightField.setEditable(false);
        heightField.setBounds(new Rectangle(84, 36, 36, 24));
        panel1.add(heightField);
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        label2.setBounds(new Rectangle(24, 60, 48, 24));
        panel1.add(label2);
        xField.setBounds(new Rectangle(84, 60, 36, 24));
        panel1.add(xField);
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        label3.setBounds(new Rectangle(24, 84, 48, 24));
        panel1.add(label3);
        yField.setBounds(new Rectangle(84, 84, 36, 24));
        panel1.add(yField);
        label6.setText("Font Size:");
        label6.setHorizontalAlignment(4);
        label6.setBounds(new Rectangle(12, 108, 64, 24));
        panel1.add(label6);
        fontSizeField.setBounds(new Rectangle(84, 108, 36, 24));
        panel1.add(fontSizeField);
        label4.setText("Text:");
        label4.setHorizontalAlignment(4);
        label4.setBounds(new Rectangle(132, 36, 40, 24));
        panel1.add(label4);
        textField.setBounds(new Rectangle(180, 36, 324, 24));
        panel1.add(textField);
        textAreaScroll.setBounds(new Rectangle(180, 36, 209, 67));
        panel1.add(textAreaScroll);
        label5.setText("Face:");
        label5.setHorizontalAlignment(4);
        label5.setBounds(new Rectangle(136, 108, 36, 24));
        panel1.add(label5);
        faceNameField.setBounds(new Rectangle(180, 108, 324, 24));
        panel1.add(faceNameField);
        visibleBox.setText("Visible");
        visibleBox.setBounds(new Rectangle(24, 144, 96, 24));
        panel1.add(visibleBox);
        selectableBox.setText("Selectable");
        selectableBox.setBounds(new Rectangle(24, 168, 96, 24));
        panel1.add(selectableBox);
        resizableBox.setText("Resizable");
        resizableBox.setBounds(new Rectangle(24, 192, 96, 24));
        panel1.add(resizableBox);
        draggableBox.setText("Draggable");
        draggableBox.setBounds(new Rectangle(24, 216, 96, 24));
        panel1.add(draggableBox);
        twoDScale.setText("2D Scale");
        twoDScale.setBounds(new Rectangle(24, 240, 90, 24));
        panel1.add(twoDScale);
        autoResize.setText("AutoResize");
        autoResize.setBounds(new Rectangle(132, 144, 90, 24));
        panel1.add(autoResize);
        multilineBox.setText("Multiline");
        multilineBox.setBounds(new Rectangle(132, 168, 84, 24));
        panel1.add(multilineBox);
        clipping.setText("Clipping");
        clipping.setBounds(new Rectangle(132, 192, 90, 24));
        panel1.add(clipping);
        editableBox.setText("Editable");
        editableBox.setBounds(new Rectangle(132, 216, 84, 24));
        panel1.add(editableBox);
        editSingle.setText("Edit on Single Click");
        panel1.add(editSingle);
        editSingle.setBounds(new Rectangle(132, 240, 130, 24));
        boldBox.setText("Bold");
        boldBox.setBounds(new Rectangle(228, 144, 84, 24));
        panel1.add(boldBox);
        italicBox.setText("Italic");
        italicBox.setBounds(new Rectangle(228, 168, 84, 24));
        panel1.add(italicBox);
        underlineBox.setText("Underline");
        underlineBox.setBounds(new Rectangle(228, 192, 84, 24));
        panel1.add(underlineBox);
        strikeBox.setText("Strike");
        strikeBox.setBounds(new Rectangle(228, 216, 84, 24));
        panel1.add(strikeBox);
        alignLeftRadio.setText("Align Left");
        alignGroup.add(alignLeftRadio);
        alignLeftRadio.setBounds(new Rectangle(324, 144, 84, 24));
        panel1.add(alignLeftRadio);
        alignCenterRadio.setText("Center");
        alignGroup.add(alignCenterRadio);
        alignCenterRadio.setBounds(new Rectangle(324, 168, 84, 24));
        panel1.add(alignCenterRadio);
        alignRightRadio.setText("Align Right");
        alignGroup.add(alignRightRadio);
        alignRightRadio.setBounds(new Rectangle(324, 192, 84, 24));
        panel1.add(alignRightRadio);
        textColorButton.setText("Text Color...");
        textColorButton.setBackground(Color.lightGray);
        textColorButton.setBounds(new Rectangle(420, 136, 117, 24));
        panel1.add(textColorButton);
        backgroundColorButton.setText("Background...");
        backgroundColorButton.setBackground(Color.lightGray);
        backgroundColorButton.setBounds(new Rectangle(420, 166, 117, 24));
        panel1.add(backgroundColorButton);
        transparentBox.setText("Transparent");
        transparentBox.setBounds(new Rectangle(420, 192, 96, 24));
        panel1.add(transparentBox);
        selectBack.setText("Select Background");
        panel1.add(selectBack);
        selectBack.setBounds(new Rectangle(420, 216, 130, 24));
    }

    void UpdateDialog()
    {
        if(m_object == null)
            return;
        classNameLabel.setText(m_object.getClass().getName());
        Rectangle rectangle = m_object.getBoundingRect();
        heightField.setText(String.valueOf(rectangle.height));
        Point point = m_object.getLocation();
        xField.setText(String.valueOf(point.x));
        yField.setText(String.valueOf(point.y));
        fontSizeField.setText(String.valueOf(m_object.getFontSize()));
        visibleBox.setSelected(m_object.isVisible());
        selectableBox.setSelected(m_object.isSelectable());
        resizableBox.setSelected(m_object.isResizable());
        draggableBox.setSelected(m_object.isDraggable());
        editableBox.setSelected(m_object.isEditable());
        boldBox.setSelected(m_object.isBold());
        italicBox.setSelected(m_object.isItalic());
        underlineBox.setSelected(m_object.isUnderline());
        strikeBox.setSelected(m_object.isStrikeThrough());
        textField.setText(m_object.getText());
        textArea.setText(m_object.getText());
        if(m_object.isMultiline())
        {
            textField.setVisible(false);
            textAreaScroll.setVisible(true);
        } else
        {
            textField.setVisible(true);
            textAreaScroll.setVisible(false);
        }
        multilineBox.setSelected(m_object.isMultiline());
        faceNameField.setText(m_object.getFaceName());
        int i = m_object.getAlignment();
        if(i == 0)
            alignLeftRadio.setSelected(true);
        else
        if(i == 2)
            alignRightRadio.setSelected(true);
        else
            alignCenterRadio.setSelected(true);
        m_textColor = m_object.getTextColor();
        m_bkColor = m_object.getBkColor();
        transparentBox.setSelected(m_object.isTransparent());
        editSingle.setSelected(m_object.isEditOnSingleClick());
        twoDScale.setSelected(m_object.is2DScale());
        clipping.setSelected(m_object.isClipping());
        autoResize.setSelected(m_object.isAutoResize());
        selectBack.setSelected(m_object.isSelectBackground());
    }

    void UpdateControl()
    {
        if(m_object == null)
            return;
        Point point = new Point(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
        m_object.setLocation(point);
        m_object.setFontSize(Integer.parseInt(fontSizeField.getText()));
        m_object.setVisible(visibleBox.isSelected());
        m_object.setSelectable(selectableBox.isSelected());
        m_object.setResizable(resizableBox.isSelected());
        m_object.setDraggable(draggableBox.isSelected());
        m_object.setEditable(editableBox.isSelected());
        m_object.setBold(boldBox.isSelected());
        m_object.setItalic(italicBox.isSelected());
        m_object.setUnderline(underlineBox.isSelected());
        m_object.setStrikeThrough(strikeBox.isSelected());
        if(m_object.isMultiline())
            m_object.setText(textArea.getText());
        else
            m_object.setText(textField.getText());
        m_object.setMultiline(multilineBox.isSelected());
        m_object.setFaceName(faceNameField.getText());
        byte byte0;
        if(alignLeftRadio.isSelected())
            byte0 = 0;
        else
        if(alignRightRadio.isSelected())
            byte0 = 2;
        else
            byte0 = 1;
        m_object.setAlignment(byte0);
        m_object.setTextColor(m_textColor);
        m_object.setBkColor(m_bkColor);
        m_object.setTransparent(transparentBox.isSelected());
        m_object.setEditOnSingleClick(editSingle.isSelected());
        m_object.set2DScale(twoDScale.isSelected());
        m_object.setClipping(clipping.isSelected());
        m_object.setAutoResize(autoResize.isSelected());
        m_object.setSelectBackground(selectBack.isSelected());
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

    void textColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Foreground Color", m_textColor);
        if(color != null)
            m_textColor = color;
    }

    void backgroundColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Foreground Color", m_bkColor);
        if(color != null)
            m_bkColor = color;
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

    JPanel panel1;
    JButton OKButton;
    JButton CancelButton;
    JLabel label1;
    JTextField heightField;
    JTextField xField;
    JLabel label2;
    JTextField yField;
    JLabel label3;
    JCheckBox visibleBox;
    JCheckBox selectableBox;
    JCheckBox resizableBox;
    JCheckBox draggableBox;
    JLabel label4;
    JCheckBox editableBox;
    JCheckBox boldBox;
    JCheckBox italicBox;
    JCheckBox underlineBox;
    JCheckBox strikeBox;
    JTextField textField;
    JLabel label5;
    JTextField faceNameField;
    ButtonGroup alignGroup;
    JRadioButton alignLeftRadio;
    JRadioButton alignCenterRadio;
    JRadioButton alignRightRadio;
    JCheckBox multilineBox;
    JLabel label6;
    JTextField fontSizeField;
    JButton textColorButton;
    JButton backgroundColorButton;
    JCheckBox transparentBox;
    JTextArea textArea;
    JScrollPane textAreaScroll;
    JLabel classNameLabel;
    JCheckBox editSingle;
    JCheckBox selectBack;
    JCheckBox twoDScale;
    JCheckBox clipping;
    JCheckBox autoResize;
    BorderLayout borderLayout1;
    Color m_textColor;
    Color m_bkColor;
    public FREEText m_object;
    boolean fComponentsAdjusted;
}
