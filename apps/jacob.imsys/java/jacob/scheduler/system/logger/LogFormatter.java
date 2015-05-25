/*
 * Created on 16.11.2004
 * by mike
 *
 */


/**
 * @author mike
 *  
 */


package jacob.scheduler.system.logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formats a message in in a readable style. <br>
 * Writes out a string containing date and time, severity and a shortened text.
 */
public final class LogFormatter extends Formatter
{
	transient private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SSS");

	public final String format(final LogRecord record)
	{
		
		final String s = date.format(new Date(record.getMillis())) + " " + record.getLevel().toString() +"\t"+ record.getSourceMethodName().toString() + ":\t" + this.formatMessage(record) + "\n";
		//       return s.length() > 160 ? s.substring(0, 160) : s;
		return s;
	}
}
