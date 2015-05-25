/*
 * jACOB event handler created with the jACOB Application Designer
 * 
 * Created on Wed May 06 14:40:24 CEST 2009
 */
package jacob.event.ui.milestoneEntry;

import jacob.model.IncidentEntry;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.exception.ExceptionHandler;
import de.tif.jacob.screen.IClientContext;


/**
 *
 * @author andherz
 */
public class MilestoneEntryOwnDraw extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
   static public final transient String RCS_ID = "$Id: MilestoneEntryOwnDraw.java,v 1.1 2009-05-06 22:00:27 herz Exp $";
   static public final transient String RCS_REV = "$Revision: 1.1 $";
   
   // Setup the colors
   //
   private final static Color transparent   = Color.blue;
   private final static Color alertColor    = new Color(255,121 ,10);
   private final static Color fillColor     = new Color(204,204,255);
   private final static Color barColor      = fillColor.darker();// new Color(97,96 ,100);
   private final static Color frameBrighter = barColor.brighter();
   private final static Color frameDarker   = barColor.darker();
   private final static Color fontColor     = new Color(97,96,100);;

   /*
    * will be called after a refresh() call
    */
   public void paint(IClientContext context, Graphics graphics, Dimension size)
   {
    try 
    {
          IDataTableRecord record = context.getSelectedRecord();
          fill3DRect(graphics, size.width, size.height);

          if(record==null)
             return;
          
          int qaOk=0;
          IDataTable incidentTable = context.getDataTable(IncidentEntry.NAME);
          System.out.println(incidentTable.recordCount());
          for(int i=0; i<incidentTable.recordCount();i++)
          {
             IDataTableRecord incident = incidentTable.getRecord(i);
             String state = incident.getSaveStringValue(IncidentEntry.state);
             if(state.equals(IncidentEntry.state_ENUM._Rejected) || state.equals(IncidentEntry.state_ENUM._QA) || state.equals(IncidentEntry.state_ENUM._Done))
                qaOk++;
          }
          double percentage = (100.0/Math.max(1,incidentTable.recordCount())*qaOk)/100.0;
          drawBar(graphics, percentage, size.width, size.height);
       } 
    catch (Exception e) 
    {
       ExceptionHandler.handle(context,e);
    }
   }
   
   /**
    * Draw the percentage bar
    * 
    * @param g
    * @param percent
    * @param width
    * @param height
    */
   private void drawBar(Graphics g, double percent,int width, int height) 
   {
     int barEnd = (int)(width*percent);
     g.setColor(barColor);
     g.fillRect(2, 2, barEnd, height-4);
     
     // Find the size of this text so we can center it
     g.setColor(fontColor);
     percent = (int)(percent*100);
     FontMetrics fm   = g.getFontMetrics();  // metrics for this object
     Rectangle2D rect = fm.getStringBounds(""+percent+"%", g); // size of string

     int textHeight = (int)(rect.getHeight());
     int textWidth  = (int)(rect.getWidth());

     // Center text horizontally and vertically
     int x = (width  - textWidth)  / 2;
     int y = (height - textHeight) / 2  + fm.getAscent();

     g.drawString(""+percent+"%", x, y);
   }

   /**
    * Draw the frame of the progress bar.
    * 
    * @param g
    * @param width
    * @param height
    */
   private void fill3DRect(Graphics g,int width, int height) 
   {
     g.setColor(fillColor);
     g.fillRect(1, 1, width-2, height-2);
     g.setColor(frameDarker);
     g.drawLine(0, 0, 0, height);
     g.drawLine(1, 0, width-1, 0);
     g.setColor(frameBrighter);
     g.drawLine(1, height, width, height);
     g.drawLine(width, 0, width, height-1);
   }

   
   /**
    * 
    * @return The transparent color for the image
    */
   public Color getTransparentColor()
   {
     return transparent;
   }
}
