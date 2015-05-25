/**
 * Title:        FREEObject
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      FreeGroup
 * @author Andreas Herz
 * @version 1.0
 */

package de.shorti.contextdesigner;

import javax.swing.UIManager;
import java.awt.*;

public class Application
{
    boolean packFrame = false;

    /**Construct the application*/
    public Application() {
        MainFrame frame = new MainFrame();
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        }
        else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    /**Main method*/
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
       }
        catch(Exception e) {
            e.printStackTrace();
        }
        new Application();
    }
}
