package jacob.exception;

/**
 * Diese Exception wird geworfen, wenn bei der Ausführung eines der
 * SAP-Schnittselle etwas schief geht.<p>
 * Diese Exception wird speziell von {@
 * 
 * @author Andreas Sonntag
 */
public class SapProcessingException extends Exception
{
  static public final transient String RCS_ID = "$Id: SapProcessingException.java,v 1.1 2007/08/08 11:38:30 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

  public SapProcessingException(String message)
  {
    super(message);
  }

  public SapProcessingException(Exception cause)
  {
    super(cause.toString(), cause);
  }
}
