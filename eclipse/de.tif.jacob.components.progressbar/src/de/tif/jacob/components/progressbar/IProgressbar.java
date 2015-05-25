/*
 * Created on 24.02.2009
 *
 */
package de.tif.jacob.components.progressbar;

public interface IProgressbar
{

  /**
   * Set the percentage of the progressbar.
   *  
   * @param percentage values between 0-100
   * @throws IndexOutOfBoundsException if the value not between [0-100]
   */
  public void setPrecentage(int percentage) throws IndexOutOfBoundsException;
  
  public void setVisible(boolean flag);
}

