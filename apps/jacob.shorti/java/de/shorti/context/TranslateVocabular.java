package de.shorti.context;
/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */

import de.shorti.context.basis.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.baseknowledge.*;
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import de.shorti.util.search.fuzzy.*;
import java.util.*;

public class TranslateVocabular  extends GenericContext
{
    public class ResponseItem
    {
        public String original;
        public String translation;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public TranslateVocabular()
    {
        super("./context/TranslateVocabular.xml");
    }

    /**
     *
     */
   	public int onStart2Trans(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Language language;
        String origWord   = matchedGroups[0];

        // the 2. pattern must be the language
        // ...try to find language_id
        //
        trace.debug1(m,"try to retrieve language object");
        ArrayList keywords = LanguageNameCatalog.find(matchedGroups[1]);
        if(keywords.size()>0)
        {
            // hip ....a language identified
            //
            Keyword  key = (Keyword)keywords.get(0);
            language = Language.findByName(key.m_keyword);
        }
        else
        {
            trace.debug1(m,"can't identify '"+matchedGroups[1]+"' as a valid language/country");
            m.setResponse("Übersetzung nicht möglich. Das Land/Sprache ´"+matchedGroups[1]+"´ ist schort-i nicht bekannt");
            m.setStatusWarning();
            return REPLY;
        }

        // find all possible words from the original word catalog
        // and try to find a translation
        //
        trace.debug1(m,"try to find matching words via fuzzy-search");
        ArrayList   trans = TranslationOriginalCatalog.find(origWord);

        String orig;
        String translation;
        m.log(trans.size()+" words selected from the database which are possible answer items");
        for(int i=0; (i<20)&&(i<trans.size()) ; i++)
        {
            orig        =((Keyword)trans.get(i)).m_keyword;
            translation = Translation.translate(orig,language);

            if(translation != null)
            {
                // hip translation found
                //
                ResponseItem item = new ResponseItem();
                item.original    = orig;
                item.translation = translation;
                addResponseItem(m,item);
            }
        }
        return RENDER_OUTPUT;
    }
}
