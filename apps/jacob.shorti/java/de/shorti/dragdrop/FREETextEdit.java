//
//
//
// Source File Name:   com/nwoods//FREETextEdit

package de.shorti.dragdrop;


import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;

// Referenced classes of package de.shorti.dragdrop:
//            FREEControl, FREEText, FREEObject, FREEView,
//            FREECopyEnvironment

public class FREETextEdit extends FREEControl
{
    class TextEditTextArea extends JTextArea
    {

        protected void processKeyEvent(KeyEvent keyevent)
        {
            if(keyevent.getKeyCode() == 27 && m_textEdit != null)
            {
                FREETextEdit textedit = m_textEdit;
                m_textEdit = null;
                textedit.doEndEdit();
            } else
            {
                super.processKeyEvent(keyevent);
            }
        }

        protected void processFocusEvent(FocusEvent focusevent)
        {
            if(focusevent.getID() == 1005 && m_textEdit != null)
            {
                m_textEdit.setText(this.getText());
                m_textEdit.doEndEdit();
                m_textEdit = null;
            }
            super.processFocusEvent(focusevent);
        }

        protected FREETextEdit m_textEdit;
//        private final FREETextEdit this$0;

        TextEditTextArea(String s, FREETextEdit textedit1)
        {
            super(s);
//            this$0 = FREETextEdit.this;
            m_textEdit = textedit1;
            setBorder(new BevelBorder(1));
            enableEvents(12L);
        }
    }

    class TextEditTextField extends JTextField
    {

        protected void processKeyEvent(KeyEvent keyevent)
        {
            if(keyevent.getKeyCode() == 10 && m_textEdit != null)
            {
                m_textEdit.setText(this.getText());
                m_textEdit.doEndEdit();
                m_textEdit = null;
            } else
            if(keyevent.getKeyCode() == 27 && m_textEdit != null)
            {
                FREETextEdit textedit = m_textEdit;
                m_textEdit = null;
                textedit.doEndEdit();
            } else
            {
                super.processKeyEvent(keyevent);
            }
        }

        protected void processFocusEvent(FocusEvent focusevent)
        {
            if(focusevent.getID() == 1005 && m_textEdit != null)
            {
                m_textEdit.setText(this.getText());
                m_textEdit.doEndEdit();
                m_textEdit = null;
            }
            super.processFocusEvent(focusevent);
        }

        protected FREETextEdit m_textEdit;
//        private final FREETextEdit this$0;

        TextEditTextField(String s, FREETextEdit textedit1)
        {
            super(s);
//            this$0 = FREETextEdit.this;
            m_textEdit = textedit1;
            enableEvents(12L);
            switch(textedit1.getTextObject().getAlignment())
            {
            case 0: // '\0'
                setHorizontalAlignment(2);
                break;

            case 1: // '\001'
                setHorizontalAlignment(0);
                break;

            case 2: // '\002'
                setHorizontalAlignment(4);
                break;
            }
        }
    }


    public FREETextEdit()
    {
        m_originalText = "";
        m_nultiline = false;
        m_textObject = null;
    }

    public FREETextEdit(Point point, Dimension dimension, String s, boolean flag, FREEText text)
    {
        super(point, dimension);
        m_originalText = s;
        m_nultiline = flag;
        m_textObject = text;
    }

    public FREETextEdit(Rectangle rectangle, String s, boolean flag, FREEText text)
    {
        super(rectangle);
        m_originalText = s;
        m_nultiline = flag;
        m_textObject = text;
    }

    public FREEObject copyObject(FREECopyEnvironment copyEnv)
    {
        FREETextEdit textedit = (FREETextEdit)super.copyObject(copyEnv);
        if(textedit != null)
        {
            textedit.m_nultiline = m_nultiline;
            textedit.m_originalText = m_originalText;
            textedit.m_textObject = (FREEText)copyEnv.get(m_textObject);
        }
        return textedit;
    }

    public FREEText getTextObject()
    {
        return m_textObject;
    }

    public void setTextObject(FREEText text)
    {
        m_textObject = text;
    }

    public JComponent createComponent(FREEView view)
    {
        Object obj;
        if(m_nultiline)
            obj = new TextEditTextArea(m_originalText, this);
        else
            obj = new TextEditTextField(m_originalText, this);
        return ((JComponent) (obj));
    }

    public void paint(Graphics2D graphics2d, FREEView view)
    {
        JComponent jcomponent = getComponent(view);
        if(jcomponent != null)
        {
            Font font = getTextObject().getFont();
            if(Math.abs(view.getScale() - 1.0D) >= 0.01D)
            {
                String s = font.getFontName();
                int i = font.getSize();
                int j = font.getStyle();
                font = new Font(s, j, (int)((double)i * view.getScale()));
            }
            jcomponent.setFont(font);
            Rectangle rectangle = view.E5();
            rectangle.setBounds(getBoundingRect());
            rectangle.x -= 4;
            rectangle.y--;
            rectangle.width += 12;
            rectangle.height += 4;
            view.convertDocToView(rectangle);
            jcomponent.setBounds(rectangle);
            jcomponent.repaint();
        }
    }

    public String getText()
    {
        return getTextObject().getText();
    }

    public void setText(String s)
    {
        getTextObject().setText(s);
    }

    public void doEndEdit()
    {
        getTextObject().doEndEdit();
    }

    private boolean m_nultiline;
    private String m_originalText;
    private FREEText m_textObject;
}
