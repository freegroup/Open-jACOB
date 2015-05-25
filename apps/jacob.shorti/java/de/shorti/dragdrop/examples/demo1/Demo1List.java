//
// 
//  
// Source File Name:   Demo1List.java

package de.shorti.dragdrop.examples.demo1;





import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

// Referenced classes of package de.shorti.dragdrop.examples.demo1:
//            AppAction

public class Demo1List extends JList
    implements DragSourceListener, DragGestureListener
{

    public Demo1List()
    {
        m_dragSource = new DragSource();
        m_recognizer = m_dragSource.createDefaultDragGestureRecognizer(this, 3, this);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseevent)
            {
                if(mouseevent.getClickCount() == 2)
                {
                    int i = locationToIndex(mouseevent.getPoint());
                    AppAction appaction = (AppAction)getModel().getElementAt(i);
                    appaction.actionPerformed(null);
                }
            }

        });
    }

    public void dragGestureRecognized(DragGestureEvent draggestureevent)
    {
        AppAction appaction = (AppAction)getSelectedValue();
        if(appaction != null)
        {
            StringSelection stringselection = new StringSelection(appaction.toString());
            draggestureevent.startDrag(DragSource.DefaultCopyDrop, stringselection, this);
        }
    }

    public void dragDropEnd(DragSourceDropEvent dragsourcedropevent)
    {
    }

    public void dragEnter(DragSourceDragEvent dragsourcedragevent)
    {
    }

    public void dragExit(DragSourceEvent dragsourceevent)
    {
    }

    public void dragOver(DragSourceDragEvent dragsourcedragevent)
    {
    }

    public void dropActionChanged(DragSourceDragEvent dragsourcedragevent)
    {
    }

    private DragSource m_dragSource;
    private DragGestureRecognizer m_recognizer;
}
