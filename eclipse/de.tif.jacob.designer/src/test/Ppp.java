/*
 * Created on 22.09.2006
 *
 */
package test;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
public class Ppp extends JDialog
{
  private static final long serialVersionUID = 1L;
  private JPanel jContentPane = null;

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
  }

  /**
   * @param owner
   */
  public Ppp(Frame owner)
  {
    super(owner);
    initialize();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize()
  {
    this.setSize(300, 200);
    this.setContentPane(getJContentPane());
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane()
  {
    if (jContentPane == null)
    {
      jContentPane = new JPanel();
      jContentPane.setLayout(new BorderLayout());
    }
    return jContentPane;
  }
}
