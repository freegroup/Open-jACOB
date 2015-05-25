//
// 
//  
// Source File Name:   ObjectPropsDialog.java

package de.shorti.dragdrop.examples.demo1;





import de.shorti.dragdrop.FREEArea;
import de.shorti.dragdrop.FREEObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class ObjectPropsDialog extends JDialog
{

    public ObjectPropsDialog(Frame frame, String s, boolean flag, FREEObject object)
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
        classNameLabel = new JLabel();
        areaLabel = new JLabel();
        grabChildSelectionBox = new JCheckBox();
        fComponentsAdjusted = false;
        try
        {
            jbInit();
            pack();
            m_object = object;
            UpdateDialog();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public ObjectPropsDialog()
    {
        this(null, "", false, null);
    }

    void jbInit()
        throws Exception
    {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(294, 241));
        panel1.setPreferredSize(new Dimension(294, 241));
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
        getContentPane().add(panel1);
        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(60, 204, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(168, 204, 79, 22));
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
        visibleBox.setBounds(new Rectangle(24, 96, 96, 24));
        selectableBox.setText("Selectable");
        panel1.add(selectableBox);
        selectableBox.setBounds(new Rectangle(24, 120, 96, 24));
        resizableBox.setText("Resizable");
        panel1.add(resizableBox);
        resizableBox.setBounds(new Rectangle(24, 144, 96, 24));
        draggableBox.setText("Draggable");
        panel1.add(draggableBox);
        draggableBox.setBounds(new Rectangle(24, 168, 96, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(132, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(192, 36, 36, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(23, 5, 264, 24));
        areaLabel.setText("a FREEArea:");
        panel1.add(areaLabel);
        areaLabel.setBounds(new Rectangle(156, 108, 100, 24));
        grabChildSelectionBox.setText("Grab Child Selection");
        panel1.add(grabChildSelectionBox);
        grabChildSelectionBox.setBounds(new Rectangle(156, 132, 144, 24));
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
        if(m_object instanceof FREEArea)
        {
            FREEArea area = (FREEArea)m_object;
            areaLabel.setVisible(true);
            grabChildSelectionBox.setVisible(true);
            grabChildSelectionBox.setSelected(area.isGrabChildSelection());
        } else
        {
            areaLabel.setVisible(false);
            grabChildSelectionBox.setVisible(false);
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
        if(m_object instanceof FREEArea)
        {
            FREEArea area = (FREEArea)m_object;
            area.setGrabChildSelection(grabChildSelectionBox.isSelected());
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
    JLabel classNameLabel;
    JLabel areaLabel;
    JCheckBox grabChildSelectionBox;
    public FREEObject m_object;
    boolean fComponentsAdjusted;
}
