/*
 * Created on 09.11.2005
 *
 */
package de.tif.jacob.rule;

import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.SchemeBorder.Scheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public class Constants
{
  public static Font  FONT_SMALL          = new Font(null, "Arial", 6,  SWT.NORMAL);
  public static Font  FONT_NORMAL         = new Font(null, "Arial", 7,  SWT.NORMAL);
  public static Font  FONT_NORMAL_BOLD    = new Font(null, "Arial", 7,  SWT.BOLD);
  public static Font  FONT_BIG            = new Font(null, "Arial", 12, SWT.NORMAL);
  public static Font  FONT_BIG_BOLD       = new Font(null, "Arial", 14, SWT.BOLD);
	public static Font  FONT_BROWSERHEADER  = new Font(null, "Arial", 8,  SWT.NORMAL);
	public static Font  FONT_GROUPHEADER    = new Font(null, "Arial", 8,  SWT.NORMAL);
	
  public static Color implementedColor    = new Color(null, 255, 255, 206);
  public static Color COLOR_PANE          = new Color(null, 238, 238, 238);
  public static Color COLOR_HEADER        = new Color(null, 204, 204, 204);
  public static Color COLOR_BORDER        = new Color(null, 152, 152, 152);
  public static Color COLOR_DISABLEFONT   = new Color(null, 179, 179, 179);
  public static Color COLOR_FONT          = new Color(null, 33,  33,  44);
  
  public static Color COLOR_WORKAREA      = new Color(null, 250,  250,  250);
  public static Color COLOR_BORDER_DARK   = new Color(null, 93,  93,  94);
  public static Color COLOR_BORDER_BRIGHT = new Color(null, 152, 152, 152);
  public static Color COLOR_STICKYNOTE    = new Color(null, 255, 255, 153);
  public static Color COLOR_TABLE         = new Color(null, 245, 255, 193);
  
  public static Color COLOR_BO_START      = new Color(null,90, 181,226);
  public static Color COLOR_BO_END        = new Color(null,102,203,255);
  public static Color COLOR_BUSINESS_IN   = new Color(null,71,162,255);
  public static Color COLOR_BUSINESS_OUT  = new Color(null,71,162,255);

  public static Color COLOR_DECISION       = new Color(null, 247, 243, 148);
  public static Color COLOR_DECISION_FALSE = new Color(null, 193,  93,  94);
  public static Color COLOR_DECISION_TRUE  = new Color(null, 148,  207,  49);
  public static Color COLOR_DECISION_TRUE_BORDER  = new Color(null, 0,  127,  5);
  public static Color COLOR_DECISION_FALSE_BORDER = new Color(null, 232,  164,  75);
  public static Color COLOR_DECISION_IN           = new Color(null, 83,  229,  100);
  public static Color COLOR_DECISION_IN_BORDER    = new Color(null, 0,  127,  5);
  public static Color COLOR_RULE_TRANSISTION      = new Color(null, 21,  69,  151);
  public static Color COLOR_RULE_ANNOTATION       = new Color(null, 220,228, 237);
  public static Color COLOR_RULE_ANNOTATION_BORDER= new Color(null, 169,209, 255);
  
  public static int   RULE_GRID_WIDTH             = 25;
  public static int   RULE_GRID_HEIGHT            = 25;
  public static int   RULE_ELEMENT_HEIGHT         = RULE_GRID_HEIGHT*2;
  
  public static final SchemeBorder BORDER= new SchemeBorder(new Scheme(
      new Color[]{Constants.COLOR_BORDER_DARK},
      new Color[]{Constants.COLOR_BORDER_BRIGHT}));
}
