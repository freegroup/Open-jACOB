package de.shorti.util.search.fuzzy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

/**
 *
 */
class urgs
{
    public urgs()
    {
        count = 0;
        match = 0;
    }
    int count;
    int match;
};
/**
 * Simple FuzzySearch mechnism. The FuzzySearch has a Set of words and
 * has some service functions to search in them via a fuzzy algorithm
 *
 */
public class FuzzySearch
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    HashSet m_catalog = null;

    public FuzzySearch()
    {
        m_catalog = new HashSet();
    }

    /**
     * set the search catalog for the fuzzy search
     *
     */
    public void setCatalog(HashSet catalog)
    {
        m_catalog = catalog;
        trace.debug1("FuzzySearch initialised with '"+m_catalog.size()+"' words");
    }

    /**
     * Grossbuchstaben in Kleinbuchstaben, Satzzeichen und div.
     * Sonderzeichen in Leerzeichen umwandeln, Laenge ermitteln
     */
    private String prepare(String originStr)
    {
        String result=originStr.toLowerCase();
        result = result.replace('ä','a');
        result = result.replace('ö','o');
        result = result.replace('ü','u');
//        result = result.replace(':',' ');
//        result = result.replace(';',' ');
//        result = result.replace('<',' ');
//        result = result.replace('>',' ');
//        result = result.replace('=',' ');
//        result = result.replace('?',' ');
//        result = result.replace('[',' ');
//        result = result.replace(']',' ');
//        result = result.replace('/',' ');
        return result;
    }

    /**
     * sucht n-Gramme im Text und zaehlt die Treffer
     */
    private urgs NGramMatch(String text, String SearchStr, int SearchStrLen, int NGramLen)
    {
        urgs          result = new urgs();
        try
        {
            StringBuffer  NGram = new StringBuffer("        ");
            int           NGramCount;
            int           i, Count;

            NGram.setCharAt(NGramLen,'\0');
            NGramCount = SearchStrLen - NGramLen + 1;

            // Suchstring in n-Gramme zerlegen und diese im Text suchen
            //
            for(i = 0, Count = 0; i < NGramCount; i++)
            {
               NGram =  new StringBuffer(SearchStr.substring(i, i+NGramLen));
               NGram.setLength(8);
                // bei Wortzwischenraum weiterruecken
                if (NGram.charAt(NGramLen - 2) == ' ' && NGram.charAt(0) != ' ')
                    i += NGramLen - 3;
                else
                {
                    result.match  += NGramLen;
                    if(text.indexOf( NGram.toString().trim()) != -1)
                    {
                        Count++;
                    }
                }
            }
            result.count = Count* NGramLen;
        }
        catch( Exception exc)
        {
            System.err.println(exc);
        }
        return result;
    }

    /**
     *  fuehrt die unscharfe Suche durch und liefert alle worte
     *  zurück welche der Prozentzahl an Übereinstimmung erfüllen.
     */
    public ArrayList find( String searchStr, int Threshold)
    {
        ArrayList words = new ArrayList();
        int     TextLen;
        int     NGram1Len, NGram2Len;
        int     MatchCount1, MatchCount2;
        int     MaxMatch1, MaxMatch2;
        double  Similarity, bestSim = 0.0;
        String  bestWord ="";
        int     searchStrLen;
        String  prepLine="";
        String  origLine="";
        try
        {
            // n-Gramm-Laenge in Abhaengigkeit vom Suchstring festlegen
            //
            searchStr    = " " + searchStr.trim() + " ";

            // searchStr    = searchStr.toLowerCase() ;
            searchStr    = prepare(searchStr);

            searchStrLen = searchStr.length();
            NGram1Len = 3;
            NGram2Len = (searchStrLen < 7) ? 2 : 5;
            Iterator iter = m_catalog.iterator();

            while(iter.hasNext())
            {
                origLine = (String)iter.next();

                //prepLine =  origLine.toLowerCase();
                prepLine =  prepare(origLine);

                TextLen = prepLine.length();
                urgs urgs1 = NGramMatch(prepLine, searchStr, searchStrLen, NGram1Len);
                urgs urgs2 = NGramMatch(prepLine, searchStr, searchStrLen, NGram2Len);
                // Trefferguete berechnen und Bestwert festhalten
                //
                Similarity = 100.0 *(urgs1.count + urgs2.count)/(urgs1.match + urgs2.match);
                if (Similarity > bestSim)
                {
                    bestSim  = Similarity;
                    bestWord = origLine;
                }
                // Wenn Guete >= Schwellwert: Treffer ausgeben
                //
                if(Similarity >= Threshold)
                {
                    words.add(new Keyword(origLine.trim(),Similarity));
                }
            }
        }
        catch (Exception exc)
        {
            System.err.println(exc);
        }
        Collections.sort(words);
        return words;
    }


    /**
     *  Only to test this class.<br>
     *  Usage: java FuzzySearch filename searchword
     */
    public static void main(String arg[])
    {
        try
        {
            FuzzySearch fuzzy= new FuzzySearch();
            ArrayList     words= null;
            String line;
            HashSet catalog= new HashSet();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(arg[0]));
            // build a catalog of possible words
            //
            while ((line = bufferedReader.readLine()) !=null)
            {
                catalog.add(" "+line.trim()+" ");
            }
            fuzzy.setCatalog(catalog);

            String     search =  arg[1];

            words = fuzzy.find(search,  40 /* 0-100% */);
            Iterator iter = words.iterator();
            while(iter.hasNext())
            {
                Keyword key= (Keyword)iter.next();
                System.out.println("Matching word: ["+key.m_keyword+ "] "+key.m_percentage+"%");
            }
        }
        catch( Exception exc)
        {
            System.err.println(exc);
        }
     }
}
