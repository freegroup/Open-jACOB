package jacob.common.vCard;

public class EscapeChar
{
  static public final transient String RCS_ID = "$Id: EscapeChar.java,v 1.1 2007/11/25 22:12:39 freegroup Exp $";
  static public final transient String RCS_REV = "$Revision: 1.1 $";

	// specialChar[x][0] contiene il carattere speciale, specialChar[x][1] contiene la traduzione xml
	public static final String[][] specialChar = {
		{   "\\", "\\\\"  }, 
		{
			";", "\\;" }, 
		{
			",", "\\," }

	};
	/**
	* Metodo che sostituisce alcuni caratteri speciali per la corretta visualizzazione dei testi negli XSL
	*
	* @param input la stringa in cui sostituire i caratteri
	* @return la stringa con le opportune sostituzioni
	*/
	public static String encode(String input)
	{
		for (int i = 0; i < specialChar.length; i++)
		{
			input = replaceString(input, specialChar[i][0], specialChar[i][1]);
		}
		return input;
	}
	/**
	* Metodo che sostituisce alcuni caratteri speciali per la corretta visualizzazione dei testi negli XSL
	*
	* @param input la stringa in cui sostituire i caratteri
	* @return la stringa con le opportune sostituzioni
	*/
	public static String decode(String input)
	{
		for (int i = 0; i < specialChar.length; i++)
		{
			input = replaceString(input, specialChar[i][1], specialChar[i][0]);
		}
		return input;
	}
	/**
	* Metodo che sostituisce nella stringa di input la sotto-stringa desiderata con una nuova sottostringa. Sostituisce la vecchia 'replaceVirg'
	* rendendola più generale.
	* Es. replaceString ("bla'bla'bla","'","''") --> "bla''bla''bla"
	*
	* @param la stringa in cui sostituire la 'oldString' con la 'newString'
	* @param la sotto-stringa da sostituire
	* @param la sotto-stringa sostituente
	* @return la stringa con le opportune sostituzioni
	*/
	public static synchronized String replaceString(	String input,String oldString,String newString)
	{
		StringBuffer out = new StringBuffer();
		int last = 0;
		while (input.indexOf(oldString) != -1)
		{
			String tok = input.substring(0, input.indexOf(oldString));
			last = input.indexOf(oldString) + oldString.length();
			input = input.substring(last, input.length());
			out.append(tok + newString);
		}
		out.append(input);
		return out.toString();
	}
}
