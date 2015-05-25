package de.shorti.message.filter;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */
import java.util.*;
import de.shorti.util.basic.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.*;

public class BillingFilter implements IMessageFilter
{
    static TraceDispatcher  trace   = TraceFactory.getTraceDispatcher();

    public boolean onBeginProcess(Message m)
    {
        Account account = m.getShortiUser().getAccount();
        int amount = m.getUserAmount();
        int credit = account.getCredit();
        int wcount = account.getCreditWarningCount();
        // check the credit if the answer not for free!!
        // Send a warning to the user and dec. the maxCreditWarningCount
        //
        if((amount!=0) && ((credit-amount)<= 0) && (wcount>0))
        {
            account.setCreditWarningCount(wcount-1,false);
            account.setRequestCount(account.getRequestCount()+1,true);
            m.log("user account is overdraft");
            m.setResponse(SystemMessage.get("NO_ACCOUNT_OVERDRAFT",m.getPreferedLanguage()));
            m.setStatusWarning();
            return false;
        }

        // check if the user has receive to maximum of total warnings
        // and check if the message not free.
        // If the user has received the total count of warnings
        // no answer will be send
        //
        if((amount !=0) && ((credit-amount) <= 0) && (wcount<=0))
        {
            m.log("no credit and all credit warnigs sent. no response to user will be send");
            m.setStatusWarning();
            m.disableSendResponse();
            return false;
        }

        // decrement the user credit
        // incemenet the number of send messages to the user
        //
        account.setCredit(account.getCredit()-amount,false);
        account.setRequestCount(account.getRequestCount()+1,true);
        return true;
    }

    public boolean onEndProcess(Message m)
    {
        return true;
    }
}