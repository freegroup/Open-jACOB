package de.shorti;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

public class GUIMain
{
    boolean packFrame = false;
  
    /**Construct the application*/
    public GUIMain()
    {
        try
        {
            // init all managers
            //3403
            Class.forName("de.shorti.action.ActionManager");
            Class.forName("de.shorti.agent.AgentManager");

            MainFrame frame = new MainFrame();
            //Validate frames that have preset sizes
            //Pack frames that have useful preferred size info, e.g. from their layout
            if (packFrame)
            {
                frame.pack();
            }
            else
            {
                frame.validate();
            }
            //Center the window
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();
            if (frameSize.height > screenSize.height)
            {
                frameSize.height = screenSize.height;
            }
            if (frameSize.width > screenSize.width)
            {
                frameSize.width = screenSize.width;
            }
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            frame.setVisible(true);
        }
        catch(Exception exc)
        {
            System.out.print(exc);
            Shorti.exit();
        }
    }
    /**Main method*/
    public static void main(String[] args)
    {
        try
        {
            System.getProperties().put( "proxySet", "true" );
            System.getProperties().put( "proxyHost", "proxy.wdf.sap-ag.de" );
            System.getProperties().put( "proxyPort", "8080" );

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Shorti.exit();
        }
        new GUIMain();
    }
}