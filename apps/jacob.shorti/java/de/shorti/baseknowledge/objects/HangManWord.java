package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.baseknowledge.objects.*;
import de.shorti.db.*;
import java.sql.*;
import de.shorti.util.basic.*;

public class HangManWord extends _dbHangManWord
{
    static java.util.Random random = new java.util.Random();
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public static HangManWord getRandom()
    {
        SaveStatement  stmt;
        HangManWord result = new HangManWord();
        try
        {
            stmt  = ConnectionManager.getValid().createStatement("HangManWord.getRandom");
            try
            {
                ResultSet s = stmt.executeQuery( " SELECT  count(*) FROM HangManWord ");
                int count = s.getInt(1);
                s.close();
                int begin = java.lang.Math.abs(random.nextInt())%(count);

                s = stmt.executeQuery( " SELECT word, id from HangManWord limit "+begin+",1");

                if(s.next())
                {
                    result.word = s.getString(1);
                    result.id   = s.getString(2);
                }
                s.close();
            }
            catch(SQLException exc)
            {
                trace.error(exc);
            }
            stmt.close();
        }
        catch (Exception ex)
        {
            trace.error(ex);
        }

        return result;
    }
}