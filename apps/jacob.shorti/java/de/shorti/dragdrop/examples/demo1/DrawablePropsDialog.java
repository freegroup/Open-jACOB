//
// 
//  
// Source File Name:   DrawablePropsDialog.java

package de.shorti.dragdrop.examples.demo1;





import de.shorti.dragdrop.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class DrawablePropsDialog extends JDialog
{

    public DrawablePropsDialog(Frame frame, String s, boolean flag, FREEDrawable drawable)
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
        widthField = new JTextField();
        brushColorButton = new JButton();
        solidBrushBox = new JCheckBox();
        penColorButton = new JButton();
        classNameLabel = new JLabel();
        penGroup = new ButtonGroup();
        solidPenButton = new JRadioButton();
        dashedPenButton = new JRadioButton();
        dottedPenButton = new JRadioButton();
        dashdotPenButton = new JRadioButton();
        dashdotdotPenButton = new JRadioButton();
        customPenButton = new JRadioButton();
        noPenButton = new JRadioButton();
        JPanel1 = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        penWidth = new JTextField();
        JPanel2 = new JPanel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        shadowWidth = new JTextField();
        flapWidth = new JTextField();
        raised = new JCheckBox();
        fComponentsAdjusted = false;
        try
        {
            m_object = drawable;
            jbInit();
            pack();
            UpdateDialog();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public DrawablePropsDialog()
    {
        this(null, "", false, null);
    }

    void jbInit()
        throws Exception
    {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(416, 329));
        panel1.setPreferredSize(new Dimension(416, 329));
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
        penColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                penColorButton_actionPerformed(actionevent);
            }

        });
        brushColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                brushColorButton_actionPerformed(actionevent);
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
        getContentPane().add(panel1);
        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(106, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(214, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        panel1.add(label1);
        label1.setBounds(new Rectangle(132, 60, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(192, 60, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(84, 36, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        panel1.add(label2);
        label2.setBounds(new Rectangle(24, 36, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(84, 60, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        panel1.add(label3);
        label3.setBounds(new Rectangle(24, 60, 48, 24));
        visibleBox.setText("Visible");
        panel1.add(visibleBox);
        visibleBox.setBounds(new Rectangle(24, 96, 72, 24));
        selectableBox.setText("Selectable");
        panel1.add(selectableBox);
        selectableBox.setBounds(new Rectangle(24, 120, 84, 24));
        resizableBox.setText("Resizable");
        panel1.add(resizableBox);
        resizableBox.setBounds(new Rectangle(24, 144, 84, 24));
        draggableBox.setText("Draggable");
        panel1.add(draggableBox);
        draggableBox.setBounds(new Rectangle(24, 168, 84, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(132, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(192, 36, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(Color.lightGray);
        brushColorButton.setBounds(new Rectangle(283, 258, 108, 24));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(Color.lightGray);
        panel1.add(solidBrushBox);
        solidBrushBox.setBounds(new Rectangle(288, 132, 108, 24));
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(Color.lightGray);
        penColorButton.setBounds(new Rectangle(127, 258, 108, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(26, 4, 364, 24));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(Color.lightGray);
        panel1.add(solidPenButton);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(120, 144, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        dashedPenButton.setBackground(Color.lightGray);
        panel1.add(dashedPenButton);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(120, 156, 120, 12));
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(Color.lightGray);
        panel1.add(dottedPenButton);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(120, 172, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(Color.lightGray);
        panel1.add(dashdotPenButton);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(120, 190, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(Color.lightGray);
        panel1.add(dashdotdotPenButton);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(120, 207, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(Color.lightGray);
        panel1.add(customPenButton);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(120, 224, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(Color.lightGray);
        panel1.add(noPenButton);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(120, 132, 120, 12));
        JPanel1.setLayout(null);
        panel1.add(JPanel1);
        JPanel1.setBackground(Color.lightGray);
        JPanel1.setBounds(new Rectangle(108, 96, 156, 156));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", 2, 12));
        label5.setBounds(new Rectangle(27, 1, 95, 23));
        label6.setText("Pen Width:");
        label6.setBounds(new Rectangle(25, 217, 67, 27));
        penWidth.setText("1");
        JPanel1.add(label5);
        penWidth.setBounds(new Rectangle(28, 240, 48, 24));
        JPanel2.setLayout(new FlowLayout(1, 5, 5));
        panel1.add(JPanel2);
        JPanel2.add(label8);
        panel1.add(CancelButton);
        panel1.add(OKButton);
        panel1.add(brushColorButton);
        panel1.add(penColorButton);
        panel1.add(label6, null);
        panel1.add(penWidth, null);
        JPanel2.setBackground(Color.lightGray);
        JPanel2.setBounds(new Rectangle(276, 96, 132, 156));
        label8.setText("Brush Properties");
        label8.setFont(new Font("Dialog", 2, 12));
        if(m_object instanceof FREE3DNoteRect)
        {
            label9.setText("Shadow width:");
            label10.setText("Flap width:");
            panel1.add(label9);
            panel1.add(label10);
            label9.setBounds(new Rectangle(234, 36, 80, 24));
            label10.setBounds(new Rectangle(254, 60, 60, 24));
            panel1.add(shadowWidth);
            panel1.add(flapWidth);
            shadowWidth.setBounds(new Rectangle(326, 36, 40, 24));
            flapWidth.setBounds(new Rectangle(326, 60, 40, 24));
        }
        if(m_object instanceof FREE3DRect)
        {
            raised.setText("Raised?");
            panel1.add(raised);
            raised.setBounds(new Rectangle(24, 192, 84, 24));
        }
    }

    void UpdateDialog()
    {
        if(m_object == null)
            return;
        classNameLabel.setText(m_object.getClass().getName());
        Rectangle rectangle = m_object.getBoundingRect();
        xField.setText(String.valueOf(rectangle.x));
        yField.setText(String.valueOf(rectangle.y));
        heightField.setText(String.valueOf(rectangle.height));
        widthField.setText(String.valueOf(rectangle.width));
        visibleBox.setSelected(m_object.isVisible());
        selectableBox.setSelected(m_object.isSelectable());
        resizableBox.setSelected(m_object.isResizable());
        draggableBox.setSelected(m_object.isDraggable());
        noPenButton.setSelected(m_object.getPen().getStyle() == 0);
        solidPenButton.setSelected(m_object.getPen().getStyle() == 65535);
        dashedPenButton.setSelected(m_object.getPen().getStyle() == 1);
        dottedPenButton.setSelected(m_object.getPen().getStyle() == 2);
        dashdotPenButton.setSelected(m_object.getPen().getStyle() == 3);
        dashdotdotPenButton.setSelected(m_object.getPen().getStyle() == 4);
        customPenButton.setSelected(m_object.getPen().getStyle() == 65534);
        penWidth.setText(String.valueOf(m_object.getPen().getWidth()));
        m_penColor = m_object.getPen().getColor();
        FREEBrush brush = m_object.getBrush();
        if(brush != null && !(brush.getPaint() instanceof GradientPaint))
        {
            solidBrushBox.setSelected(true);
            if(m_object.getBrush().getPaint() instanceof Color)
                m_brushColor = (Color)m_object.getBrush().getPaint();
            else
                m_brushColor = Color.black;
        } else
        {
            solidBrushBox.setSelected(false);
            m_brushColor = Color.black;
        }
        if(m_object instanceof FREE3DNoteRect)
        {
            shadowWidth.setText(String.valueOf(((FREE3DNoteRect)m_object).getShadowSize()));
            flapWidth.setText(String.valueOf(((FREE3DNoteRect)m_object).getFlapSize()));
        }
        if(m_object instanceof FREE3DRect)
            raised.setSelected(((FREE3DRect)m_object).getState() == 0);
    }

    void UpdateControl()
    {
        if(m_object == null)
            return;
        Rectangle rectangle = new Rectangle(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()), Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
        m_object.setBoundingRect(rectangle);
        m_object.setVisible(visibleBox.isSelected());
        m_object.setSelectable(selectableBox.isSelected());
        m_object.setResizable(resizableBox.isSelected());
        m_object.setDraggable(draggableBox.isSelected());
        int i;
        if(solidPenButton.isSelected())
            i = 65535;
        else
        if(dashedPenButton.isSelected())
            i = 1;
        else
        if(dottedPenButton.isSelected())
            i = 2;
        else
        if(dashdotPenButton.isSelected())
            i = 3;
        else
        if(dashdotdotPenButton.isSelected())
            i = 4;
        else
        if(customPenButton.isSelected())
            i = 65534;
        else
            i = 0;
        int j = Integer.parseInt(penWidth.getText());
        Color color = m_penColor;
        m_object.setPen(FREEPen.make(i, j, color));
        if(solidBrushBox.isSelected())
            m_object.setBrush(FREEBrush.make(65535, m_brushColor));
        else
        if(m_object.getBrush() != null && !(m_object.getBrush().getPaint() instanceof GradientPaint))
            m_object.setBrush(null);
        if(m_object instanceof FREE3DNoteRect)
        {
            ((FREE3DNoteRect)m_object).setShadowSize(Integer.parseInt(shadowWidth.getText()));
            ((FREE3DNoteRect)m_object).setFlapSize(Integer.parseInt(flapWidth.getText()));
        }
        if(m_object instanceof FREE3DRect)
            if(raised.isSelected())
                ((FREE3DRect)m_object).setState(0);
            else
                ((FREE3DRect)m_object).setState(1);
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

    void penColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Pen Color", m_penColor);
        if(color != null)
            m_penColor = color;
    }

    void brushColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Brush Color", m_brushColor);
        if(color != null)
            m_brushColor = color;
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
    JTextField widthField;
    JButton brushColorButton;
    JCheckBox solidBrushBox;
    JButton penColorButton;
    JLabel classNameLabel;
    ButtonGroup penGroup;
    JRadioButton solidPenButton;
    JRadioButton dashedPenButton;
    JRadioButton dottedPenButton;
    JRadioButton dashdotPenButton;
    JRadioButton dashdotdotPenButton;
    JRadioButton customPenButton;
    JRadioButton noPenButton;
    JPanel JPanel1;
    JLabel label5;
    JLabel label6;
    JTextField penWidth;
    JPanel JPanel2;
    JLabel label8;
    JLabel label9;
    JLabel label10;
    JTextField shadowWidth;
    JTextField flapWidth;
    JCheckBox raised;
    Color m_brushColor;
    Color m_penColor;
    public FREEDrawable m_object;
    boolean fComponentsAdjusted;
}