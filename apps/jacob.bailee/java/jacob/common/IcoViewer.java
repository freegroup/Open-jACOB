package jacob.common;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.IconView;

public class IcoViewer extends JFrame
{
  final static int SP_PREF_WIDTH = 300;
  final static int SP_PREF_HEIGHT = 300;
  static Ico ico;

  public IcoViewer()
  {
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    GPanel gpanel = new GPanel();
    final JLabel lblImage = new JLabel(new ImageIcon(ico.getImage(0)));
    JScrollPane sp = new JScrollPane(lblImage);
    sp.setOpaque(false);
    sp.setPreferredSize(new Dimension(SP_PREF_WIDTH, SP_PREF_HEIGHT));
    sp.getViewport().setOpaque(false);
    gpanel.add(sp, BorderLayout.NORTH);
    String[] resolutions = new String[ico.getNumImages()];
    for (int i = 0; i < resolutions.length; i++)
      resolutions[i] = ico.getImage(i).getWidth() + "x" + ico.getImage(i).getHeight() + ": " + (ico.getNumColors(i) == 0 ? "32-bit color" : ico.getNumColors(i) + " colors");
    final JComboBox cbResolutions = new JComboBox(resolutions);
    cbResolutions.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    cbResolutions.setOpaque(false);
    ItemListener il;
    il = new ItemListener()
    {
      public void itemStateChanged(ItemEvent ie)
      {
        if (ie.getStateChange() == ItemEvent.SELECTED)
        {
          JComboBox cb = (JComboBox) ie.getSource();
          int index = cb.getSelectedIndex();
          lblImage.setIcon(new ImageIcon(ico.getImage(index)));
        }
      }
    };
    cbResolutions.addItemListener(il);
    gpanel.add(new JPanel()
    {
      {
        add(cbResolutions);
        setOpaque(false);
      }
    }, BorderLayout.SOUTH);
    setContentPane(gpanel);
    pack();
    setResizable(false);
    setVisible(true);
  }

  public static void main(final String[] args) throws Exception
  {
    InputStream stream = IcoViewer.class.getResourceAsStream("test.ico");
    System.out.println(stream);
    ico = new Ico(stream);
    Runnable r= new Runnable()
    {
      public void run()
      {
        // Swing GUIs must be created on the event-dispatching thread.
        // See Sun's Java Tutorial for more information.
        new IcoViewer();
      }
    };
    EventQueue.invokeLater(r);
  }
}

class GPanel extends JPanel
{
  private GradientPaint gp;

  GPanel()
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  public void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;
    if (gp == null)
      gp = new GradientPaint(0, 0, Color.white, 0, getHeight(), Color.blue);
    // Paint a nice gradient background with white at the top and blue at
    // the bottom.
    g2d.setPaint(gp);
    g.fillRect(0, 0, getWidth(), getHeight());
  }
}
