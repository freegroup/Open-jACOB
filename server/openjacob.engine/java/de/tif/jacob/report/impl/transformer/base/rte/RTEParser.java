
//----------------------------------------------------
// The following code was generated by CUP v0.10k
// Tue Apr 28 11:21:04 CEST 2015
//----------------------------------------------------

package de.tif.jacob.report.impl.transformer.base.rte;

import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.tif.jacob.core.exception.InvalidExpressionException;

/** CUP v0.10k generated parser.
  * @version Tue Apr 28 11:21:04 CEST 2015
  */
public class RTEParser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public RTEParser() {super();}

  /** Constructor which sets the default scanner. */
  public RTEParser(java_cup.runtime.Scanner s) {super(s);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\037\000\002\002\004\000\002\003\003\000\002\004" +
    "\002\000\002\004\003\000\002\005\003\000\002\005\004" +
    "\000\002\006\003\000\002\006\003\000\002\007\004\000" +
    "\002\007\003\000\002\007\004\000\002\007\004\000\002" +
    "\007\003\000\002\007\005\000\002\007\004\000\002\007" +
    "\004\000\002\007\004\000\002\007\004\000\002\007\004" +
    "\000\002\007\005\000\002\007\005\000\002\007\004\000" +
    "\002\007\005\000\002\007\005\000\002\007\005\000\002" +
    "\007\005\000\002\007\004\000\002\007\004\000\002\007" +
    "\004\000\002\007\004\000\002\007\004" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\047\000\016\002\uffff\004\010\007\014\016\005\027" +
    "\011\030\007\001\002\000\004\002\051\001\002\000\004" +
    "\031\050\001\002\000\016\002\ufffd\004\ufffd\007\ufffd\016" +
    "\ufffd\027\ufffd\030\ufffd\001\002\000\016\002\ufffb\004\ufffb" +
    "\007\ufffb\016\ufffb\027\ufffb\030\ufffb\001\002\000\016\002" +
    "\ufff8\004\ufff8\007\ufff8\016\ufff8\027\ufff8\030\ufff8\001\002" +
    "\000\046\004\026\005\035\006\025\007\020\010\032\011" +
    "\021\012\033\013\024\014\037\015\023\017\031\020\040" +
    "\021\030\022\017\023\022\024\034\025\036\027\027\001" +
    "\002\000\004\002\000\001\002\000\016\002\ufffa\004\ufffa" +
    "\007\ufffa\016\ufffa\027\ufffa\030\ufffa\001\002\000\016\002" +
    "\ufff5\004\ufff5\007\ufff5\016\ufff5\027\ufff5\030\ufff5\001\002" +
    "\000\016\002\ufffe\004\010\007\014\016\005\027\011\030" +
    "\007\001\002\000\016\002\ufffc\004\ufffc\007\ufffc\016\ufffc" +
    "\027\ufffc\030\ufffc\001\002\000\004\031\047\001\002\000" +
    "\020\002\ufff6\004\ufff6\007\ufff6\016\ufff6\027\ufff6\030\ufff6" +
    "\031\046\001\002\000\016\002\ufff3\004\ufff3\007\ufff3\016" +
    "\ufff3\027\ufff3\030\ufff3\001\002\000\004\031\045\001\002" +
    "\000\004\031\044\001\002\000\016\002\ufff0\004\ufff0\007" +
    "\ufff0\016\ufff0\027\ufff0\030\ufff0\001\002\000\016\002\uffe3" +
    "\004\uffe3\007\uffe3\016\uffe3\027\uffe3\030\uffe3\001\002\000" +
    "\016\002\ufff9\004\ufff9\007\ufff9\016\ufff9\027\ufff9\030\ufff9" +
    "\001\002\000\016\002\uffe4\004\uffe4\007\uffe4\016\uffe4\027" +
    "\uffe4\030\uffe4\001\002\000\016\002\uffe5\004\uffe5\007\uffe5" +
    "\016\uffe5\027\uffe5\030\uffe5\001\002\000\016\002\uffe7\004" +
    "\uffe7\007\uffe7\016\uffe7\027\uffe7\030\uffe7\001\002\000\016" +
    "\002\ufff1\004\ufff1\007\ufff1\016\ufff1\027\ufff1\030\ufff1\001" +
    "\002\000\016\002\ufff2\004\ufff2\007\ufff2\016\ufff2\027\ufff2" +
    "\030\ufff2\001\002\000\004\031\043\001\002\000\016\002" +
    "\ufff7\004\ufff7\007\ufff7\016\ufff7\027\ufff7\030\ufff7\001\002" +
    "\000\004\031\042\001\002\000\020\002\uffef\004\uffef\007" +
    "\uffef\016\uffef\027\uffef\030\uffef\031\041\001\002\000\016" +
    "\002\uffe6\004\uffe6\007\uffe6\016\uffe6\027\uffe6\030\uffe6\001" +
    "\002\000\016\002\uffee\004\uffee\007\uffee\016\uffee\027\uffee" +
    "\030\uffee\001\002\000\016\002\uffe8\004\uffe8\007\uffe8\016" +
    "\uffe8\027\uffe8\030\uffe8\001\002\000\016\002\uffe9\004\uffe9" +
    "\007\uffe9\016\uffe9\027\uffe9\030\uffe9\001\002\000\016\002" +
    "\uffed\004\uffed\007\uffed\016\uffed\027\uffed\030\uffed\001\002" +
    "\000\016\002\uffea\004\uffea\007\uffea\016\uffea\027\uffea\030" +
    "\uffea\001\002\000\016\002\ufff4\004\ufff4\007\ufff4\016\ufff4" +
    "\027\ufff4\030\ufff4\001\002\000\016\002\uffeb\004\uffeb\007" +
    "\uffeb\016\uffeb\027\uffeb\030\uffeb\001\002\000\016\002\uffec" +
    "\004\uffec\007\uffec\016\uffec\027\uffec\030\uffec\001\002\000" +
    "\004\002\001\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\047\000\014\003\003\004\011\005\014\006\005\007" +
    "\012\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\006\006\015\007\012\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$RTEParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$RTEParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$RTEParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


 
	static private final transient Log logger = LogFactory.getLog(RTEParser.class);

	static public final transient String RCS_ID = "$Id: rte.cup,v 1.6 2009/12/22 03:36:32 ibissw Exp $";
	static public final transient String RCS_REV = "$Revision: 1.6 $";
	
	protected IRTEPrinter printer;
	protected IRTEFunctionResolver resolver;
	private String text;

	public static void printTextExpression(IRTEPrinter printer, IRTEFunctionResolver resolver, String text) throws Exception
	{
		Reader reader = new StringReader(text);
		try
		{
			RTEScanner scanner = new RTEScanner(reader);
			scanner.setOriginalText(text);
			RTEParser parser = new RTEParser(scanner);
			parser.printer = printer;
			parser.resolver = resolver;
			parser.text = text;
			parser.parse();
		}
		catch (Exception ex)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Can not parse report text expression \"" + text + "\": " + ex.getMessage());
			}
			throw new InvalidExpressionException(text, ex);
		}
		finally
		{
			reader.close();
		}
	}
  
	private String errorMessage(String message, Object info)
	{
		StringBuffer m = new StringBuffer("Error ");

		if (info instanceof java_cup.runtime.Symbol)
			m.append("(").append(info.toString()).append(")");

		m.append(": ").append(message);

		return m.toString();
	}

	public void report_error(String message, Object info)
	{
		logger.error(errorMessage(message, info));
	}

	public void report_fatal_error(String message, Object info)
	{
		logger.fatal(errorMessage(message, info));
		throw new RuntimeException("Syntax error in \"" + text + "\"");
	}

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$RTEParser$actions {

 

  private final RTEParser parser;

  /** Constructor */
  CUP$RTEParser$actions(RTEParser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$RTEParser$do_action(
    int                        CUP$RTEParser$act_num,
    java_cup.runtime.lr_parser CUP$RTEParser$parser,
    java.util.Stack            CUP$RTEParser$stack,
    int                        CUP$RTEParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$RTEParser$result;

      /* select the action based on the action number */
      switch (CUP$RTEParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 30: // rte_function ::= TILDE IGNORE 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.ignore());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 29: // rte_function ::= TILDE TILDE 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.tilde());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 28: // rte_function ::= TILDE RIGHT 
            {
              Object RESULT = null;
		
	parser.printer.markRightAligned();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 27: // rte_function ::= TILDE CENTER 
            {
              Object RESULT = null;
		
	parser.printer.markCentered();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 26: // rte_function ::= TILDE LEFT 
            {
              Object RESULT = null;
		
	parser.printer.markLeftAligned();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 25: // rte_function ::= TILDE MAX ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.max(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 24: // rte_function ::= TILDE MIN ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.min(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 23: // rte_function ::= TILDE SUM ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.sum(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 22: // rte_function ::= TILDE AVERAGE ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.average(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 21: // rte_function ::= DB_FIELD ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.value(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 20: // rte_function ::= TILDE VALUE ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.value(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 19: // rte_function ::= TILDE COUNT ARG 
            {
              Object RESULT = null;
		int fieldleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int fieldright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String field = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.count(field));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 18: // rte_function ::= TILDE COUNT 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.count());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 17: // rte_function ::= TILDE NAME 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.name());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 16: // rte_function ::= TILDE PAGE 
            {
              Object RESULT = null;
		
	parser.printer.printPageNumber();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 15: // rte_function ::= TILDE TIME 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.time());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 14: // rte_function ::= TILDE DATE 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.date());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // rte_function ::= TILDE TAB ARG 
            {
              Object RESULT = null;
		int numleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int numright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String num = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(parser.resolver.tab(num));

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // rte_function ::= TAB 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.tab());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // rte_function ::= TILDE TAB 
            {
              Object RESULT = null;
		
	parser.printer.print(parser.resolver.tab());

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // rte_function ::= TILDE PAGEBREAK 
            {
              Object RESULT = null;
		
	parser.printer.printPagebreak();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // rte_function ::= LF 
            {
              Object RESULT = null;
		
	parser.printer.printLinefeed();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // rte_function ::= TILDE LF 
            {
              Object RESULT = null;
		
	parser.printer.printLinefeed();

              CUP$RTEParser$result = new java_cup.runtime.Symbol(5/*rte_function*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // rte_part ::= rte_function 
            {
              Object RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(4/*rte_part*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // rte_part ::= TEXT_DATA 
            {
              Object RESULT = null;
		int textleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left;
		int textright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right;
		java.lang.String text = (java.lang.String)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).value;
		
	parser.printer.print(text);

              CUP$RTEParser$result = new java_cup.runtime.Symbol(4/*rte_part*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // rte_parts ::= rte_parts rte_part 
            {
              Object RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(3/*rte_parts*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // rte_parts ::= rte_part 
            {
              Object RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(3/*rte_parts*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // rte_parts_opt ::= rte_parts 
            {
              Object RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(2/*rte_parts_opt*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // rte_parts_opt ::= 
            {
              Object RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(2/*rte_parts_opt*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // rte ::= rte_parts_opt 
            {
              RTE RESULT = null;
		
	RESULT = null;

              CUP$RTEParser$result = new java_cup.runtime.Symbol(1/*rte*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          return CUP$RTEParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= rte EOF 
            {
              Object RESULT = null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).right;
		RTE start_val = (RTE)((java_cup.runtime.Symbol) CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).value;
		RESULT = start_val;
              CUP$RTEParser$result = new java_cup.runtime.Symbol(0/*$START*/, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$RTEParser$stack.elementAt(CUP$RTEParser$top-0)).right, RESULT);
            }
          /* ACCEPT */
          CUP$RTEParser$parser.done_parsing();
          return CUP$RTEParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

