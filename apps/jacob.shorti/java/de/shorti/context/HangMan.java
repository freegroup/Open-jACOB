package de.shorti.context;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.util.HashMap;

import de.shorti.baseknowledge.objects.HangManWord;
import de.shorti.baseknowledge.objects.SystemConfiguration;
import de.shorti.context.basis.GenericContext;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class HangMan extends GenericContext
{
	static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();
    static int maxTryCount=-1;

    public HangMan()
    {
        super("./context/HangMan.xml");
    }

	public int onChar2Word(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        checkWord(userDataMap,matchedGroups,m);
		return REPLY;
	}

	public int onChar2Char(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        checkChar(userDataMap,matchedGroups,m);
		return REPLY;
	}

	public int onLost2Char(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        initNewGame(userDataMap);
		return REPLY;
	}

	public int onWin2Char(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        initNewGame(userDataMap);
		return REPLY;
	}

	public int onWord2Word(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        checkWord(userDataMap,matchedGroups,m);
		return REPLY;
	}

	public int onWord2Char(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        checkChar(userDataMap,matchedGroups,m);
		return REPLY;
	}


	public int onStart2Char(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        initNewGame(userDataMap);
		return REPLY;
	}

    /**
     *
     */
	public void checkWord(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        incrementTryCount(userDataMap);
        StringBuffer sb = new StringBuffer();

        String  c       = (String)userDataMap.get("computer");
        String  u       = (String)userDataMap.get("user");
        String  testUser= matchedGroups[0];


        // If the has find the word
        //
        if(c.equalsIgnoreCase(testUser)==true)
        {
            String s_freeSMS       = SystemConfiguration.findByName("HANGMAN_WIN_SMS").getValue();
            String s_defaultAmount = SystemConfiguration.findByName("SMS_DEFAULT_AMOUNT").getValue();
            int    freeSMS         = Integer.parseInt(s_freeSMS);
            int    defaultAmount   = Integer.parseInt(s_defaultAmount);
            int    incAcountCredit = (freeSMS - getTryCount(userDataMap))*defaultAmount;

            if(incAcountCredit > 0)
            {
                // increment the user account with the won credit
                //
                m.getShortiUser().getAccount().incCredit(incAcountCredit);
                // generate the success message and invite the user to a next game
                //
                m.setResponse("Richtig! Sie haben auch noch "+incAcountCredit+" cent gutgeschreiben bekommen. Wollen sie noch ein Spiel?!!?!");
                initNewGame(userDataMap);
//                setAlternative(m, NODE_WIN);
            }
            else
            {
                m.setResponse("Richtig, aber leider nichts gewonnen. Wollen sie noch ein Spiel?!!?!");
                initNewGame(userDataMap);
//                setAlternative(m, NODE_LOST);
                return;
            }
         }
         else
         {
            incrementErrorCount(userDataMap );
            if(getErrorCount(userDataMap )>=maxTryCount )
            {
                sb.append("Ooooh - das Mänchen hängt.");
                sb.append((char)10);
                sb.append("Lösung war:");
                sb.append((char)10);
                sb.append("["+c+"]");
                sb.append((char)10);
                sb.append("Neues Spiel?!");
                sb.append(getStickMan( userDataMap ));
                initNewGame(userDataMap);
//                setAlternative(m, NODE_LOST);
            }
            else
            {
                sb.append("Kein Treffer");
                sb.append((char)10);
                sb.append("["+u+"]");
                sb.append(getStickMan( userDataMap ));
            }
            m.setResponse(sb.toString());
        }
    }


    /**
     *
     */
	public void checkChar(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        incrementTryCount(userDataMap);
        StringBuffer sb = new StringBuffer();

        String  c       = (String)userDataMap.get("computer");
        String  u       = (String)userDataMap.get("user");
        String  newUser = "";
        boolean hit     = false;

        char input = matchedGroups[0].toLowerCase().charAt(0);

        // replace the char in the user-word
        //
        for(int i=0; i<c.length(); i++)
        {
            if(c.toLowerCase().charAt(i) == input)
            {
                newUser = newUser+c.charAt(i);
                hit     = true;
            }
            else
            {
                newUser = newUser+u.charAt(i);
            }
        }

        // generate the response
        //
        if(hit)
        {
            sb.append("Treffer");
            sb.append((char)10);
            sb.append("["+newUser+"]");
        }
        else
        {
            sb.append("Kein Treffer");
            sb.append((char)10);
            sb.append("["+newUser+"]");
            incrementErrorCount(userDataMap );
        }

        // If the has find the word
        //
        if(c.equalsIgnoreCase(newUser)==true)
        {
            String s_freeSMS       = SystemConfiguration.findByName("HANGMAN_WIN_SMS").getValue();
            String s_defaultAmount = SystemConfiguration.findByName("SMS_DEFAULT_AMOUNT").getValue();
            int    freeSMS         = Integer.parseInt(s_freeSMS);
            int    defaultAmount   = Integer.parseInt(s_defaultAmount);
            int    incAcountCredit = (freeSMS - getTryCount(userDataMap))*defaultAmount;

            if(incAcountCredit > 0)
            {
                // increment the user account with the won credit
                //
                m.getShortiUser().getAccount().incCredit(incAcountCredit);
                // generate the success message and invite the user to a next game
                //
                m.setResponse("GEWONNEN!!! Sie haben "+incAcountCredit+" cent gutgeschrieben bekommen. Wollen sie noch ein Spiel?!!?!");
                initNewGame(userDataMap);
//                setAlternative(m, NODE_WIN);
                return;
            }
            else
            {
                m.setResponse("Leider nichts gewonnen. Wollen sie noch ein Spiel?!!?!");
                initNewGame(userDataMap);
//                setAlternative(m, NODE_LOST);
                return;
            }
        }
        else
        {
            if(getErrorCount(userDataMap )>=maxTryCount )
            {
                sb = new StringBuffer();
                sb.append("Ooooh - das Männchen hängt.");
                sb.append((char)10);
                sb.append("Lösung war:");
                sb.append((char)10);
                sb.append("["+c+"]");
                sb.append((char)10);
                sb.append("Neues Spiel?!");
                sb.append(getStickMan( userDataMap ));
                initNewGame(userDataMap);
//                setAlternative(m, NODE_LOST);
            }
            else
            {
                sb.append(getStickMan( userDataMap ));
                userDataMap.put("user",newUser);
            }
        }
        m.setResponse(sb.toString());
	}

    /**
     *
     */
    protected StringBuffer getStickMan(HashMap userDataMap)
    {
        StringBuffer sb = new StringBuffer();
        int currentStick = getErrorCount( userDataMap );
        for(int i=0; i< sms_stickMan[currentStick].length;i++)
        {
            sb.append((char)10);
            sb.append(sms_stickMan[currentStick][i]);
            if(i==2)
                sb.append("  Sie hatten ");
            if(i==3)
                sb.append("   "+getTryCount(userDataMap  ));
            if(i==4)
                sb.append("  Versuche" );
        }
        return sb;
    }

    /**
     * increment the user try
     */
    protected int getTryCount(HashMap userDataMap)
    {
        Integer count = (Integer)userDataMap.get("count");
        if(count==null)
        {
            count = new Integer(0);
            userDataMap.put("count",count);
        }
        return count.intValue();
    }

    /**
     * increment the user try
     */
    protected void incrementTryCount(HashMap userDataMap)
    {
        Integer count = (Integer)userDataMap.get("count");
        if(count==null)
        {
            count = new Integer(1);
            userDataMap.put("count",count);
        }
        else
        {
            userDataMap.put("count",new Integer(count.intValue()+1));
        }
    }

    /**
     * increment the user try
     */
    protected int getErrorCount(HashMap userDataMap)
    {
        Integer count = (Integer)userDataMap.get("error");
        if(count==null)
        {
            count = new Integer(0);
            userDataMap.put("error",count);
        }
        return count.intValue();
    }

    /**
     * increment the user try
     */
    protected void incrementErrorCount(HashMap userDataMap)
    {
        Integer count = (Integer)userDataMap.get("error");
        if(count==null)
        {
            count = new Integer(1);
            userDataMap.put("error",count);
        }
        else
        {
            userDataMap.put("error",new Integer(count.intValue()+1));
        }
    }

    protected void initNewGame(HashMap userDataMap)
    {
        userDataMap.clear();
        String word = HangManWord.getRandom().getWord();

        userDataMap.put("computer",word);
        userDataMap.put("user"    ,new String("******************************").substring(0,word.length()));
        maxTryCount = sms_stickMan.length-1;
    }


    static String[][] sms_stickMan=
{
{
"      ",
"      ",
"      ",
"      ",
"      ",
"      ",
"      "
},
{
"      ",
"      ",
"      ",
"      ",
"      ",
"      ",
"-----+"
},
{
"     ",
"     I",
"     I",
"     I",
"     I",
"     I",
"-----+"
},
{
"_____",
"     I",
"     I",
"     I",
"     I",
"     I",
"-----+"
},
{
"_____",
" O   I",
"     I",
"     I",
"     I",
"     I",
"-----+"
},
{
"_____",
" O   I",
" I   I",
" I   I",
"     I",
"     I",
"-----+"
},
{
"_____",
" O   I",
" I   I",
" I   I",
"/ \\  I",
"     I",
"-----+"
},
{
"_____",
" O   I",
"/I\\  I",
" I   I",
"/ \\  I",
"     I",
"-----+"
},
};
}