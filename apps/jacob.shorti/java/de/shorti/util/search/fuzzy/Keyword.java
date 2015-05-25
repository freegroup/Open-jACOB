package de.shorti.util.search.fuzzy;

import de.shorti.util.basic.*;

public class Keyword implements Comparable
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public String m_keyword;
    public int m_percentage;

    public Keyword(String keyword, double percentage)
    {
        m_keyword    = keyword;
        m_percentage = (int)percentage;
    }

    public int compareTo(Object o)
    {

        if((((Keyword)o).m_percentage - m_percentage)==0)
        {
            if(((Keyword)o).m_keyword.length()< m_keyword.length())
                return 1;
            else
                return -1;
        }
        return (((Keyword)o).m_percentage - m_percentage);
    }

    public String toString()
    {
        return m_keyword + "("+m_percentage+"%)";
    }
}