package de.shorti.dragdrop.examples;



import de.shorti.dragdrop.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.EventObject;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class BasicApp extends JApplet    implements Runnable
{
    private FREEView m_view;

    public BasicApp()
    {
        Container container = getContentPane();
        m_view = new FREEView();
        container.add(m_view);
    }

    public void init()
    {
        FREEDocument document = m_view.getDocument();
        BasicNode basicnode = new BasicNode();
        basicnode.initialize(new Point(100, 100), "Start");
        basicnode.setBrush(FREEBrush.makeStockBrush(Color.blue));
        document.addObjectAtTail(basicnode);
        BasicNode basicnode1 = new BasicNode();
        basicnode1.initialize(new Point(200, 100), "Location");
        basicnode1.setBrush(FREEBrush.makeStockBrush(Color.magenta));
        document.addObjectAtTail(basicnode1);
    }

    public void start()
    {
        (new Thread(this)).start();
    }

    public void run()
    {
        m_view.initializeDragDropHandling();
    }

    public static void main(String args[])
    {
        try
        {
            final JFrame mainFrame = new JFrame();
            mainFrame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent windowevent)
                {
                    Object obj = windowevent.getSource();
                    if(obj == mainFrame)
                        System.exit(0);
                }

            });
            mainFrame.setTitle("short-i ContextDesigner");
            mainFrame.setSize(450, 450);
            BasicApp basicapp = new BasicApp();
            Container container = mainFrame.getContentPane();
            container.setLayout(new BorderLayout());
            container.add("Center", basicapp);
            container.validate();
            mainFrame.setVisible(true);
            basicapp.init();
            basicapp.start();
        }
        catch(Throwable throwable)
        {
            System.err.println(throwable);
            throwable.printStackTrace();
            System.exit(1);
        }
    }
}
