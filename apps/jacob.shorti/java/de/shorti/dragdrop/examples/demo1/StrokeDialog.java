//
// 
//  
// Source File Name:   StrokeDialog.java

package de.shorti.dragdrop.examples.demo1;





import de.shorti.dragdrop.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class StrokeDialog extends JDialog
{

    public StrokeDialog(Frame frame, String s, boolean flag, FREEStroke stroke)
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
        startArrow = new JCheckBox();
        endArrow = new JCheckBox();
        JPanel3 = new JPanel();
        label9 = new JLabel();
        label10 = new JLabel();
        arrowLength = new JTextField();
        label11 = new JLabel();
        shaftLength = new JTextField();
        label12 = new JLabel();
        arrowAngle = new JTextField();
        isOrtho = new JCheckBox();
        JPanel4 = new JPanel();
        label13 = new JLabel();
        label14 = new JLabel();
        highlightWidth = new JTextField();
        highlightGroup = new ButtonGroup();
        solidHighlight = new JRadioButton();
        dashedHighlight = new JRadioButton();
        dottedHighlight = new JRadioButton();
        dashdotHighlight = new JRadioButton();
        dashdotdotHighlight = new JRadioButton();
        customHighlight = new JRadioButton();
        noHighlight = new JRadioButton();
        highlightColorButton = new JButton();
        fComponentsAdjusted = false;
        try
        {
            m_object = stroke;
            jbInit();
            pack();
            UpdateDialog();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public StrokeDialog()
    {
        this(null, "", false, null);
    }

    void jbInit()
        throws Exception
    {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(580, 329));
        panel1.setPreferredSize(new Dimension(580, 329));
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
        highlightColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                highlightColorButton_actionPerformed(actionevent);
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
        OKButton.setBounds(new Rectangle(166, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(274, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        panel1.add(label1);
        label1.setBounds(new Rectangle(472, 52, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(532, 52, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(434, 28, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        panel1.add(label2);
        label2.setBounds(new Rectangle(372, 28, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(434, 52, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        panel1.add(label3);
        label3.setBounds(new Rectangle(372, 52, 48, 24));
        visibleBox.setText("Visible");
        panel1.add(visibleBox);
        visibleBox.setBounds(new Rectangle(236, 24, 72, 24));
        selectableBox.setText("Selectable");
        panel1.add(selectableBox);
        selectableBox.setBounds(new Rectangle(236, 44, 84, 24));
        resizableBox.setText("Resizable");
        panel1.add(resizableBox);
        resizableBox.setBounds(new Rectangle(324, 24, 84, 24));
        draggableBox.setText("Draggable");
        panel1.add(draggableBox);
        draggableBox.setBounds(new Rectangle(324, 44, 84, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(472, 28, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(532, 28, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(Color.lightGray);
        brushColorButton.setBounds(new Rectangle(315, 258, 108, 24));
        JPanel2.setLayout(new FlowLayout(1, 5, 5));
        panel1.add(JPanel2);
        JPanel2.add(label8);
        JPanel2.setBackground(Color.lightGray);
        JPanel2.setBounds(new Rectangle(307, 96, 124, 156));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(Color.lightGray);
        JPanel2.add(solidBrushBox);
        solidBrushBox.setBounds(new Rectangle(10, 28, 108, 24));
        JPanel3.setBackground(Color.lightGray);
        JPanel3.setBounds(new Rectangle(440, 96, 133, 220));
        JPanel3.add(label9);
        startArrow.setText("Start Arrow");
        JPanel3.add(startArrow);
        startArrow.setBounds(new Rectangle(10, 28, 84, 24));
        startArrow.setBackground(Color.lightGray);
        endArrow.setText("End Arrow");
        JPanel3.add(endArrow);
        endArrow.setBounds(new Rectangle(10, 49, 84, 24));
        endArrow.setBackground(Color.lightGray);
        highlightColorButton.setText("Highlight Color...");
        highlightColorButton.setBackground(Color.lightGray);
        highlightColorButton.setBounds(new Rectangle(10, 258, 128, 24));
        if(m_object instanceof FREELink)
        {
            isOrtho.setText("Orthagonal Segments");
            panel1.add(isOrtho);
            isOrtho.setBounds(new Rectangle(236, 64, 184, 24));
        }
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(Color.lightGray);
        penColorButton.setBounds(new Rectangle(172, 258, 108, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(26, 4, 264, 24));
        JPanel1.setLayout(null);
        panel1.add(JPanel1);
        JPanel1.setBackground(Color.lightGray);
        JPanel1.setBounds(new Rectangle(157, 96, 140, 156));
        JPanel4.setLayout(null);
        panel1.add(JPanel4);
        JPanel4.setBackground(Color.lightGray);
        JPanel4.setBounds(new Rectangle(7, 96, 140, 156));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(Color.lightGray);
        JPanel1.add(solidPenButton);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(12, 52, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        JPanel1.add(dashedPenButton);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(12, 68, 120, 12));
        dashedPenButton.setBackground(Color.lightGray);
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(Color.lightGray);
        JPanel1.add(dottedPenButton);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(12, 84, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(Color.lightGray);
        JPanel1.add(dashdotPenButton);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(12, 100, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(Color.lightGray);
        JPanel1.add(dashdotdotPenButton);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(12, 116, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(Color.lightGray);
        JPanel1.add(customPenButton);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(12, 132, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(Color.lightGray);
        JPanel1.add(noPenButton);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(12, 36, 120, 12));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", 2, 12));
        label5.setBounds(new Rectangle(27, 1, 95, 23));
        label6.setText("Pen Width:");
        label6.setBounds(new Rectangle(163, 35, 67, 27));
        penWidth.setText("1");
        JPanel1.add(label5);
        penWidth.setBounds(new Rectangle(163, 60, 48, 24));
        panel1.add(CancelButton);
        panel1.add(OKButton);
        panel1.add(brushColorButton);
        panel1.add(penColorButton);
        panel1.add(highlightColorButton);
        panel1.add(label6, null);
        panel1.add(penWidth, null);
        label8.setText("Brush Properties");
        label8.setFont(new Font("Dialog", 2, 12));
        JPanel3.setLayout(null);
        panel1.add(JPanel3);
        label9.setText("Arrow Properties");
        label9.setFont(new Font("Dialog", 2, 12));
        label9.setBounds(new Rectangle(22, 1, 95, 23));
        JPanel3.add(label10);
        label10.setText("Arrow Length");
        label10.setBounds(new Rectangle(10, 72, 95, 23));
        JPanel3.add(arrowLength);
        arrowLength.setBounds(new Rectangle(10, 91, 48, 24));
        JPanel3.add(label11);
        label11.setText("Shaft Length");
        label11.setBounds(new Rectangle(10, 119, 95, 23));
        JPanel3.add(shaftLength);
        shaftLength.setBounds(new Rectangle(10, 138, 48, 24));
        JPanel3.add(label12);
        label12.setText("Arrow Angle (radians)");
        label12.setBounds(new Rectangle(10, 166, 128, 23));
        JPanel3.add(arrowAngle);
        arrowAngle.setBounds(new Rectangle(10, 185, 115, 24));
        JPanel4.add(label13);
        label13.setText("Highlight Properties");
        label13.setFont(new Font("Dialog", 2, 12));
        label13.setBounds(new Rectangle(12, 1, 130, 23));
        panel1.add(label14);
        label14.setText("Highlight Width:");
        label14.setBounds(new Rectangle(17, 35, 100, 27));
        panel1.add(highlightWidth);
        highlightWidth.setBounds(new Rectangle(17, 60, 48, 24));
        solidHighlight.setBackground(Color.lightGray);
        dashedHighlight.setBackground(Color.lightGray);
        dottedHighlight.setBackground(Color.lightGray);
        dashdotHighlight.setBackground(Color.lightGray);
        dashdotdotHighlight.setBackground(Color.lightGray);
        customHighlight.setBackground(Color.lightGray);
        noHighlight.setBackground(Color.lightGray);
        solidHighlight.setText("Solid Highlight");
        dashedHighlight.setText("Dashed Line");
        dottedHighlight.setText("Dotted Line");
        dashdotHighlight.setText("Dash Dot Line");
        dashdotdotHighlight.setText("Dash Dot Dot Line");
        customHighlight.setText("Custom Highlight");
        noHighlight.setText("No Highlight");
        JPanel4.add(solidHighlight);
        solidHighlight.setBounds(new Rectangle(12, 52, 120, 14));
        JPanel4.add(dashedHighlight);
        dashedHighlight.setBounds(new Rectangle(12, 68, 120, 14));
        JPanel4.add(dottedHighlight);
        dottedHighlight.setBounds(new Rectangle(12, 84, 120, 14));
        JPanel4.add(dashdotHighlight);
        dashdotHighlight.setBounds(new Rectangle(12, 100, 120, 14));
        JPanel4.add(dashdotdotHighlight);
        dashdotdotHighlight.setBounds(new Rectangle(12, 116, 120, 14));
        JPanel4.add(customHighlight);
        customHighlight.setBounds(new Rectangle(12, 132, 120, 14));
        JPanel4.add(noHighlight);
        noHighlight.setBounds(new Rectangle(12, 36, 120, 14));
        highlightGroup.add(solidHighlight);
        highlightGroup.add(dashedHighlight);
        highlightGroup.add(dottedHighlight);
        highlightGroup.add(dashdotHighlight);
        highlightGroup.add(dashdotdotHighlight);
        highlightGroup.add(customHighlight);
        highlightGroup.add(noHighlight);
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
        startArrow.setSelected(m_object.hasArrowAtStart());
        endArrow.setSelected(m_object.hasArrowAtEnd());
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
        arrowLength.setText(String.valueOf(m_object.getArrowLength()));
        shaftLength.setText(String.valueOf(m_object.getArrowShaftLength()));
        arrowAngle.setText(String.valueOf(m_object.getArrowAngle()));
        if(m_object instanceof FREELink)
            isOrtho.setSelected(((FREELink)m_object).isOrthogonal());
        FREEPen pen = m_object.getHighlight();
        if(pen == null)
        {
            noHighlight.setSelected(true);
            highlightWidth.setText("0");
            m_highlightColor = Color.black;
        } else
        {
            m_highlightColor = pen.getColor();
            highlightWidth.setText(String.valueOf(pen.getWidth()));
            solidHighlight.setSelected(pen.getStyle() == 65535);
            dashedHighlight.setSelected(pen.getStyle() == 1);
            dottedHighlight.setSelected(pen.getStyle() == 2);
            dashdotHighlight.setSelected(pen.getStyle() == 3);
            dashdotdotHighlight.setSelected(pen.getStyle() == 4);
            customHighlight.setSelected(pen.getStyle() == 65534);
        }
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
        m_object.setArrowHeads(startArrow.isSelected(), endArrow.isSelected());
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
        int k = Integer.parseInt(penWidth.getText());
        Color color = m_penColor;
        m_object.setPen(FREEPen.make(i, k, color));
        if(solidBrushBox.isSelected())
            m_object.setBrush(FREEBrush.make(65535, m_brushColor));
        else
        if(!(m_object.getBrush().getPaint() instanceof GradientPaint))
            m_object.setBrush(null);
        m_object.setArrowLength(Double.parseDouble(arrowLength.getText()));
        m_object.setArrowShaftLength(Double.parseDouble(shaftLength.getText()));
        m_object.setArrowAngle(Double.parseDouble(arrowAngle.getText()));
        if(m_object instanceof FREELink)
            ((FREELink)m_object).setOrthogonal(isOrtho.isSelected());
        if(noHighlight.isSelected() || Integer.parseInt(highlightWidth.getText()) == 0)
        {
            m_object.setHighlight(null);
        } else
        {
            Color color1 = m_highlightColor;
            int j;
            if(solidHighlight.isSelected())
                j = 65535;
            else
            if(dashedHighlight.isSelected())
                j = 1;
            else
            if(dottedHighlight.isSelected())
                j = 2;
            else
            if(dashdotHighlight.isSelected())
                j = 3;
            else
            if(dashdotdotHighlight.isSelected())
                j = 4;
            else
                j = 65534;
            m_object.setHighlight(new FREEPen(j, Integer.parseInt(highlightWidth.getText()), color1));
        }
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

    void highlightColorButton_actionPerformed(ActionEvent actionevent)
    {
        Color color = JColorChooser.showDialog(this, "Highlight Color", m_highlightColor);
        if(color != null)
            m_highlightColor = color;
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
    JCheckBox startArrow;
    JCheckBox endArrow;
    JPanel JPanel3;
    JLabel label9;
    JLabel label10;
    JTextField arrowLength;
    JLabel label11;
    JTextField shaftLength;
    JLabel label12;
    JTextField arrowAngle;
    JCheckBox isOrtho;
    JPanel JPanel4;
    JLabel label13;
    JLabel label14;
    JTextField highlightWidth;
    ButtonGroup highlightGroup;
    JRadioButton solidHighlight;
    JRadioButton dashedHighlight;
    JRadioButton dottedHighlight;
    JRadioButton dashdotHighlight;
    JRadioButton dashdotdotHighlight;
    JRadioButton customHighlight;
    JRadioButton noHighlight;
    JButton highlightColorButton;
    Color m_brushColor;
    Color m_penColor;
    Color m_highlightColor;
    public FREEStroke m_object;
    boolean fComponentsAdjusted;
}
