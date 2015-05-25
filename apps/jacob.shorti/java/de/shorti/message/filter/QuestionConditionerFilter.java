package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import de.shorti.baseknowledge.TranslationOriginalCatalog;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;
import de.shorti.util.search.fuzzy.Keyword;

public class QuestionConditionerFilter implements IMessageFilter
{
    static TraceDispatcher  trace  = TraceFactory.getTraceDispatcher();


    public boolean onBeginProcess(Message m)
    {
        boolean questionModified = false;

        StringTokenizer tokenizer = new StringTokenizer(m.getQuestion());
        String newQuestion ="";

        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken();
            if(token.length()>3)
            {
                // find all possible words from the original word catalog
                // and try to find a translation
                //
                ArrayList   trans = TranslationOriginalCatalog.find(token);

                String orig;
                Iterator iter  = trans.iterator();
                if(iter.hasNext())
                {
                    orig =((Keyword)iter.next()).m_keyword;
                    if(Math.abs(orig.length()-token.length())<=1)
                    {
                        newQuestion = newQuestion+" "+orig;
                        questionModified = true;
                    }
                    else
                        newQuestion = newQuestion+" "+token;
                }
            }
            else
            {
                newQuestion = newQuestion+" "+token;
            }
        }

        if(questionModified)
        {
            trace.debug1(m,"fitting question["+m.getQuestion()+"] to ["+newQuestion.trim()+"]");
            m.setQuestion(newQuestion.trim());
        }
        return true;
    }


    public boolean onEndProcess(Message m)
    {
        return true;
    }

}