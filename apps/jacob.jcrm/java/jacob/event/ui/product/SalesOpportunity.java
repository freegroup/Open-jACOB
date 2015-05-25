/*
 * Created on Apr 26, 2004
 *
 */
package jacob.event.ui.product;

import jacob.common.AppProfile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataBrowser;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.core.definition.Filldirection;
import de.tif.jacob.core.definition.IRelationSet;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.IGroup;

/**
 * 
 */
public class SalesOpportunity extends de.tif.jacob.screen.event.IOwnDrawElementEventHandler
{
  /**
   * Ein PipeTextObject enthält den Text und die Koordinaten und Farbe des zu
   * zeichnenden Textes
   * 
   */
  private static class PipeTextObject
  {
    String value;

    int x;

    int y;

    Color color;

    PipeTextObject()
    {
      x = 10;
      y = 10;
      value = null;
      color = Color.black;
    }
  }

  /**
   * Diese Klasse kapselt alle notwendigen Werte für die Salespipeline Dieses
   * Objekt wird am User gespeichert, da das Diagramm-Objekt nicht für jeden
   * User instanziert wird und somit keine Membervariablen verwendet werden
   * können
   * 
   * @author mike
   * 
   */
  private static class UserPipeValuesObject
  {
    long [] pipeAmount = null;
    
    List [] pipeProjectIds = null;

    PipeTextObject[][] pipeText = null; // die Werte und der Footer als
    // Textobject

    PipeTextObject headerText = null;

    PipeTextObject descriptionProjectCount = null;

    PipeTextObject descriptionAmmount = null;
  }

  static public final transient String RCS_ID = "$Id: SalesOpportunity.java,v 1.4 2006/11/14 10:23:38 achim Exp $"; //$NON-NLS-1$

  static public final transient String RCS_REV = "$Revision: 1.4 $"; //$NON-NLS-1$

  static private final int HORIZONTALLY = 1;

  static private final int VERTICALLY = 2;

  static private final int AMOUNT = 0;

  static private final int PROJECTCOUNT = 1;

  static private final int FOOTER = 2;

  static private final int PIPETEXT_SIZE = 3;

  static private final int PIPELINE_SIZE = 4;

  static private Polygon[] pipeline = null;

  static private Color[] pipeColor = null;

  static private final BigDecimal ZERO2 = new BigDecimal(BigInteger.ZERO, 2);

  /**
   * 
   * @param g
   * @param string
   * @param min
   * @param max
   * @param orientation
   * @return zentrale Lage eines String zwischen zwei Punkten
   */
  private static int center(Graphics g, String string, int min, int max, int orientation)
  {
    int value = 0;
    FontMetrics fm = g.getFontMetrics(); // metrics for this object
    Rectangle2D rect = fm.getStringBounds(string, g); // size of string

    int textHeight = (int) (rect.getHeight());
    int textWidth = (int) (rect.getWidth());

    // Center text horizontally and vertically
    if (orientation == HORIZONTALLY)
    {
      value = (max - min - textWidth) / 2;
    }
    else
    {
      value = (max - min - textHeight) / 2 + fm.getAscent();
    }

    return value;
  }

  /**
   * setzt die Werte des Charts in ein Array
   * 
   * @param context
   */
  private static void setPipeValues(IClientContext context, UserPipeValuesObject userPipe) throws Exception
  {
    userPipe.pipeAmount = new long[PIPELINE_SIZE];
    userPipe.pipeProjectIds = new List[PIPELINE_SIZE];
    long[] pipeAmount = userPipe.pipeAmount;
    List[] pipeProjectIds = userPipe.pipeProjectIds;

    IDataBrowser productBrowser = context.getDataBrowser("productBrowser");

    // wenn Produkt selektiert, dann die Werte für das Produkt
    // sonst für alle Produkte im Browser, falls der Browser leer ist,
    // 0- Werte erzwingen
    String productConstraint = null;
    if (context.getGroup().getDataStatus() == IGroup.SELECTED)
    {
      productConstraint = context.getSelectedRecord().getStringValue("pkey");
    }
    else
    {
      if (productBrowser.recordCount() > 0)
      {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < productBrowser.recordCount(); i++)
        {
          if (i != 0)
          {
            buffer.append("|");
          }
          buffer.append(productBrowser.getRecord(i).getTableRecord().getSaveStringValue("pkey"));
        }
        productConstraint = buffer.toString();
      }
      else
      {
        productConstraint = "0"; // Hack um nichts zu finden
      }
    }
    // MIKE: Refreshzyklus ist noch falsch
    IDataAccessor myAccessor = context.getDataAccessor().newAccessor();
    IDataTable salesproject = myAccessor.getTable("salesproject");
    IDataBrowser salesprojectBrowser = myAccessor.getBrowser("salesprojectBrowser");
    salesprojectBrowser.setMaxRecords(IDataBrowser.UNLIMITED_RECORDS);

    // Focus 1
    salesproject.qbeClear();
    if (productConstraint != null)
      salesproject.qbeSetValue("product_key", productConstraint);
    salesproject.qbeSetValue("status", "Open");
    salesproject.qbeSetValue("focus", "1");
    salesproject.qbeSetValue("estclosed", "today..today+" + AppProfile.getValue(context, "so_delay1") + "m");
    salesprojectBrowser.search(IRelationSet.LOCAL_NAME, Filldirection.NONE);
    BigDecimal value = ZERO2;
    StringBuffer ignoreKeys = new StringBuffer();
    ignoreKeys.append("salesproject.pkey not in (0");
    pipeProjectIds[3] = new ArrayList();
    for (int i = 0; i < salesprojectBrowser.recordCount(); i++)
    {
      IDataTableRecord record = salesprojectBrowser.getRecord(i).getTableRecord();
      ignoreKeys.append(",");
      ignoreKeys.append(record.getStringValue("pkey"));
      value = value.add(record.getSaveDecimalValue("prod_value_est")).add(record.getSaveDecimalValue("srv_value_est"));
      pipeProjectIds[3].add(record.getValue("pkey"));
    }
    pipeAmount[3] = value.longValue();

    // Focus 2
    salesproject.qbeClear();
    if (productConstraint != null)
      salesproject.qbeSetValue("product_key", productConstraint);
    salesproject.qbeSetValue("status", "Open");
    salesproject.qbeSetValue("focus", "<3");
    salesproject.qbeSetValue("estclosed", "today..today+" + AppProfile.getValue(context, "so_delay2") + "m");
    salesprojectBrowser.searchWhere(IRelationSet.LOCAL_NAME, Filldirection.NONE, ignoreKeys.toString() + ")");
    value = ZERO2;
    pipeProjectIds[2] = new ArrayList();
    for (int i = 0; i < salesprojectBrowser.recordCount(); i++)
    {
      IDataTableRecord record = salesprojectBrowser.getRecord(i).getTableRecord();
      ignoreKeys.append(",");
      ignoreKeys.append(record.getSaveStringValue("pkey"));
      value = value.add(record.getSaveDecimalValue("prod_value_est")).add(record.getSaveDecimalValue("srv_value_est"));
      pipeProjectIds[2].add(record.getValue("pkey"));
    }
    pipeAmount[2] = value.longValue();

    // Focus 3
    salesproject.qbeClear();
    if (productConstraint != null)
      salesproject.qbeSetValue("product_key", productConstraint);
    salesproject.qbeSetValue("status", "Open");
    salesproject.qbeSetValue("focus", "<4");
    salesproject.qbeSetValue("estclosed", "today..today+" + AppProfile.getValue(context, "so_delay3") + "m");
    salesprojectBrowser.searchWhere(IRelationSet.LOCAL_NAME, Filldirection.NONE, ignoreKeys.toString() + ")");
    value = ZERO2;
    pipeProjectIds[1] = new ArrayList();
    for (int i = 0; i < salesprojectBrowser.recordCount(); i++)
    {
      IDataTableRecord record = salesprojectBrowser.getRecord(i).getTableRecord();
      ignoreKeys.append(",");
      ignoreKeys.append(record.getSaveStringValue("pkey"));
      value = value.add(record.getSaveDecimalValue("prod_value_est")).add(record.getSaveDecimalValue("srv_value_est"));
      pipeProjectIds[1].add(record.getValue("pkey"));
    }
    pipeAmount[1] = value.longValue();

    // Focus 4
    salesproject.qbeClear();
    if (productConstraint != null)
      salesproject.qbeSetValue("product_key", productConstraint);
    salesproject.qbeSetValue("status", "Open");
    salesproject.qbeSetValue("estclosed", ">today");
    salesprojectBrowser.searchWhere(IRelationSet.LOCAL_NAME, Filldirection.NONE, ignoreKeys.toString() + ")");
    value = ZERO2;
    pipeProjectIds[0] = new ArrayList();
    for (int i = 0; i < salesprojectBrowser.recordCount(); i++)
    {
      IDataTableRecord record = salesprojectBrowser.getRecord(i).getTableRecord();
      ignoreKeys.append(",");
      ignoreKeys.append(record.getSaveStringValue("pkey"));
      value = value.add(record.getSaveDecimalValue("prod_value_est")).add(record.getSaveDecimalValue("srv_value_est"));
      pipeProjectIds[0].add(record.getValue("pkey"));
    }
    pipeAmount[0] = value.longValue();
  }

  private static void initializePipeline(IClientContext context, UserPipeValuesObject userPipe, Graphics g, Dimension size) throws Exception
  {
    int mx = size.width;
    int my = size.height;
    if (pipeline == null)
    {
      pipeline = new Polygon[PIPELINE_SIZE];
      pipeColor = new Color[PIPELINE_SIZE];
      for (int i = 0; i < PIPELINE_SIZE; i++)
        pipeline[i] = new Polygon();
      pipeline[0].addPoint(0, (int) (my * 0.1));
      pipeline[0].addPoint((int) (mx * 0.2), (int) (my * 0.15));
      pipeline[0].addPoint((int) (mx * 0.2), (int) (my * 0.85));
      pipeline[0].addPoint(0, (int) (my * 0.9));
      pipeColor[0] = new Color(40, 253, 2, 100);
      pipeline[1].addPoint((int) (mx * 0.2), (int) (my * 0.15));
      pipeline[1].addPoint((int) (mx * 0.4), (int) (my * 0.20));
      pipeline[1].addPoint((int) (mx * 0.4), (int) (my * 0.80));
      pipeline[1].addPoint((int) (mx * 0.2), (int) (my * 0.85));
      pipeColor[1] = new Color(40, 253, 2, 150);
      pipeline[2].addPoint((int) (mx * 0.4), (int) (my * 0.20));
      pipeline[2].addPoint((int) (mx * 0.6), (int) (my * 0.25));
      pipeline[2].addPoint((int) (mx * 0.6), (int) (my * 0.75));
      pipeline[2].addPoint((int) (mx * 0.4), (int) (my * 0.80));
      pipeColor[2] = new Color(40, 253, 2, 200);
      pipeline[3].addPoint((int) (mx * 0.6), (int) (my * 0.25));
      pipeline[3].addPoint((int) (mx * 0.8), (int) (my * 0.30));
      pipeline[3].addPoint((int) (mx * 0.8), (int) (my * 0.70));
      pipeline[3].addPoint((int) (mx * 0.6), (int) (my * 0.75));
      pipeColor[3] = new Color(40, 253, 2, 250);
    }

    // wenn am User noch keine Daten hängen, Daten erzeugen
    if (userPipe.pipeText == null)
    {
      userPipe.pipeText = new PipeTextObject[PIPELINE_SIZE][PIPETEXT_SIZE];
      for (int i = 0; i < PIPELINE_SIZE; i++)
        for (int j = 0; j < PIPETEXT_SIZE; j++)
          userPipe.pipeText[i][j] = new PipeTextObject();
    }

    // berechne die Werte in der Pipe
    setPipeValues(context, userPipe);

    int xmin = 0;
    int xmax = 0;
    int ymin = 0;
    int ymax = 0;

    for (int i = 0; i < PIPELINE_SIZE; i++)
    {
      userPipe.pipeText[i][AMOUNT].value = Long.toString(userPipe.pipeAmount[i]);
      xmin = pipeline[i].getBounds().x;
      xmax = xmin + pipeline[3].getBounds().width;
      userPipe.pipeText[i][AMOUNT].x = xmin + center(g, userPipe.pipeText[i][AMOUNT].value, xmin, xmax, HORIZONTALLY);
      userPipe.pipeText[i][PROJECTCOUNT].value = Integer.toString(userPipe.pipeProjectIds[i].size());
      userPipe.pipeText[i][PROJECTCOUNT].x = xmin + center(g, userPipe.pipeText[i][PROJECTCOUNT].value, xmin, xmax, HORIZONTALLY);
      // nur setzten wenn noch nicht gesetzt
      if (userPipe.pipeText[i][FOOTER].value == null)
      {
        userPipe.pipeText[i][FOOTER].value = ApplicationMessage.getLocalized("SalesOpportunity.Focus") + (PIPELINE_SIZE - i); //$NON-NLS-1$
        userPipe.pipeText[i][FOOTER].x = xmin + center(g, userPipe.pipeText[i][FOOTER].value, xmin, xmax, HORIZONTALLY);
      }
      ymin = pipeline[3].getBounds().y;
      ymax = ymin + pipeline[3].getBounds().height;
      userPipe.pipeText[i][AMOUNT].y = ymin + center(g, userPipe.pipeText[i][AMOUNT].value, ymin, ymax, VERTICALLY);
      ymin = userPipe.pipeText[i][AMOUNT].y;
      ymax = pipeline[3].getBounds().y + pipeline[3].getBounds().height;
      userPipe.pipeText[i][PROJECTCOUNT].y = ymin + center(g, userPipe.pipeText[i][PROJECTCOUNT].value, ymin, ymax, VERTICALLY);
      ymin = pipeline[0].getBounds().y + pipeline[0].getBounds().height;
      ymax = my;
      userPipe.pipeText[i][FOOTER].y = ymin + center(g, userPipe.pipeText[i][FOOTER].value, ymin, ymax, VERTICALLY);
    }

    // nur wenn noch nicht berechnet die Beschriftung setzten
    if (userPipe.headerText == null)
    {
      userPipe.headerText = new PipeTextObject();
      userPipe.headerText.value = ApplicationMessage.getLocalized("SalesOpportunity.SalesPipe"); //$NON-NLS-1$
      userPipe.headerText.x = center(g, userPipe.headerText.value, 0, mx, HORIZONTALLY);
      userPipe.headerText.y = center(g, userPipe.headerText.value, 0, pipeline[0].getBounds().y, VERTICALLY);
      xmin = pipeline[PIPELINE_SIZE - 1].getBounds().x + pipeline[PIPELINE_SIZE - 1].getBounds().width;

      userPipe.descriptionAmmount = new PipeTextObject();
      userPipe.descriptionAmmount.value = ApplicationMessage.getLocalized("SalesOpportunity.Amount"); //$NON-NLS-1$
      userPipe.descriptionAmmount.x = xmin + center(g, userPipe.descriptionAmmount.value, xmin, mx, HORIZONTALLY);
      userPipe.descriptionAmmount.y = userPipe.pipeText[0][AMOUNT].y;

      userPipe.descriptionProjectCount = new PipeTextObject();
      userPipe.descriptionProjectCount.value = ApplicationMessage.getLocalized("SalesOpportunity.Project"); //$NON-NLS-1$
      userPipe.descriptionProjectCount.x = xmin + center(g, userPipe.descriptionProjectCount.value, xmin, mx, HORIZONTALLY);
      userPipe.descriptionProjectCount.y = userPipe.pipeText[0][PROJECTCOUNT].y;
    }
    // aktuelle Werte am User speichern
    context.getUser().setProperty("salesOpportunity", userPipe);

  }
  
  /**
   * @param context the context
   * @param focus between 1 and 4
   * @return list of sales project pkeys
   */
  public static List getPipeProjectIds(IClientContext context, int focus)
  {
    UserPipeValuesObject userPipe=null;
    if (context.getUser().getProperty("salesOpportunity")==null)
    {
      return Collections.EMPTY_LIST;
    }
    else{
      userPipe = (UserPipeValuesObject) context.getUser().getProperty("salesOpportunity");
    }
//    UserPipeValuesObject userPipe = (UserPipeValuesObject) context.getUser().getProperty("salesOpportunity");
//    if (userPipe == null)
//      return Collections.EMPTY_LIST;
    return userPipe.pipeProjectIds[PIPELINE_SIZE-focus];
  }

  /*
   * will be called after a refresh() call
   */
  public void paint(IClientContext context, Graphics graphics, Dimension size) throws Exception
  {
    // Setup the colors
    Color transparent = getTransparentColor();
    Color foreground = Color.black;
    int mx = size.width;
    int my = size.height;

    graphics.setColor(transparent);
    graphics.fillRect(0, 0, mx, my);

    // benutzerdefinierte Datencontainer instanzieren
    UserPipeValuesObject userPipe=null;
    if (context.getUser().getProperty("salesOpportunity")==null)
    {
      userPipe = new UserPipeValuesObject();
    }
    else{
      userPipe = (UserPipeValuesObject) context.getUser().getProperty("salesOpportunity");
    }
//    UserPipeValuesObject userPipe = (UserPipeValuesObject) context.getUser().getProperty("salesOpportunity");
//    if (userPipe == null)
//    {
//      userPipe = new UserPipeValuesObject();
//    }
    initializePipeline(context, userPipe, graphics, size);

    for (int i = 0; i < pipeline.length; i++)
    {
      graphics.setColor(pipeColor[i]);
      graphics.fillPolygon(pipeline[i]);
      for (int j = 0; j < userPipe.pipeText[i].length; j++)
      {
        graphics.setColor(userPipe.pipeText[i][j].color);
        graphics.drawString(userPipe.pipeText[i][j].value, userPipe.pipeText[i][j].x, userPipe.pipeText[i][j].y);
      }
    }
    graphics.setColor(userPipe.headerText.color);
    graphics.drawString(userPipe.headerText.value, userPipe.headerText.x, userPipe.headerText.y);
    graphics.setColor(userPipe.descriptionAmmount.color);
    graphics.drawString(userPipe.descriptionAmmount.value, userPipe.descriptionAmmount.x, userPipe.descriptionAmmount.y);
    graphics.setColor(userPipe.descriptionProjectCount.color);
    graphics.drawString(userPipe.descriptionProjectCount.value, userPipe.descriptionProjectCount.x, userPipe.descriptionProjectCount.y);
  }

  /**
   * 
   * @return The transparent color for the image
   */
  public Color getTransparentColor()
  {
    return Color.white;
  }
}
