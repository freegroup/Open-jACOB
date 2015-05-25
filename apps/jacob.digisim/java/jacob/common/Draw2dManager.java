/*
 * Created on 14.11.2006
 *
 */
package jacob.common;

import jacob.circuit.castor.Circuit;
import jacob.circuit.castor.Connection;
import jacob.circuit.castor.Part;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Draw2dManager
{
  public static void main(String[] args) throws Exception
  {
    try
    {
//      InputStream stream = Draw2dManager.class.getResourceAsStream("notification.ruleset");
   InputStream stream = Draw2dManager.class.getResourceAsStream("test.circuit");
      System.out.println(toDraw2D(stream));
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static String toDraw2D(InputStream stream) throws Exception
  {
      StringBuffer sb = new StringBuffer();
      Circuit circuit=(Circuit)Circuit.unmarshalCircuit(new InputStreamReader(stream,"ISO-8859-1"));
      sb.append("\n\n/* Create the used objects */\n");
      sb.append("var objectMap = new Object();\n"); 
      sb.append("var object = null;\n"); 
      for (int i = 0; i < circuit.getPartCount(); i++)
      {
        Part part = circuit.getPart(i);
        sb.append("object = new "+part.getType()+"();\n");
        sb.append("object.setProperty('pkey', '"+part.getId()+"');\n");
        sb.append("objectMap['"+part.getId()+"'] = object;\n");
      }
      
      sb.append("\n\n/* Add the objects to the workflow */\n");
      for (int i = 0; i < circuit.getPartCount(); i++)
      {
        Part rule = circuit.getPart(i);
        sb.append("workflow.addFigure(objectMap['"+rule.getId()+"'],"+rule.getX()+","+rule.getY()+");\n");
      }

      sb.append("\n\n/* Create the connections between this objects */\n");
      sb.append("var connection = null;\n");
      for (int i = 0; i < circuit.getConnectionCount(); i++)
      {
        Connection connection = circuit.getConnection(i);
        sb.append("connection = new Wire();\n"); 
        sb.append("connection.setSource(objectMap['"+connection.getSourcePartId()+"'].getPort('"+connection.getSourcePortId()+"'));\n"); 
        sb.append("connection.setTarget(objectMap['"+connection.getTargetPartId()+"'].getPort('"+connection.getTargetPortId()+"'));\n"); 
        sb.append("workflow.addFigure(connection);\n");
      }
      return sb.toString();
    }
}
