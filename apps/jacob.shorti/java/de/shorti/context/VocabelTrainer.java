package de.shorti.context;
/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.shorti.baseknowledge.objects.Action;
import de.shorti.baseknowledge.objects.CommunicationChannel;
import de.shorti.baseknowledge.objects.Language;
import de.shorti.baseknowledge.objects.ShortiUser;
import de.shorti.baseknowledge.objects.SubscribedAction;
import de.shorti.baseknowledge.objects.Translation;
import de.shorti.context.basis.GenericContext;
import de.shorti.message.Message;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;

public class VocabelTrainer  extends GenericContext
{
    class ResponseItem
    {
        public String original;
        public String translation;
    }

    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    static final String QUESTION = "vokabeltrainer"; // this is a question which sends 10 vocables to the user

    public VocabelTrainer()
    {
        super("./context/VocabelTrainer.xml");
    }

    /**
     * einfacher Vocabeltrainer welcher 10 beliebige Vocabeln versendet
     * (im Moment nur von deutsch nach englisch)
     *
     */
	public int onStart2SendRandom(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        Language from = Language.findByShortName("de");
        Language to   = Language.findByShortName("en");

        ArrayList result = Translation.getRandom(from,to,10);
        trace.debug2(result.size()+" vocables created for sending.");
        Iterator iter = result.iterator();
        while(iter.hasNext())
        {
            Translation                     trans = (Translation)iter.next();
            ResponseItem item  = new ResponseItem();
            item.original    = trans.getOriginal();
            item.translation = trans.getTrans();
            addResponseItem(m,item);
        }
		return RENDER_OUTPUT;
	}

    /**
     * create the SubscribetAction for the vocabeltrainer
     *
     */
	public int onStart2StartTrainer(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        ShortiUser user = m.getShortiUser();
        CommunicationChannel channel = m.getChannel();
        Action               action  = Action.findByQuestion(QUESTION);

        // check if the user has an usefull channel for subscriptions
        //
        de.shorti.iochannel.IChannel obj=null;
        try
        {
            obj = (de.shorti.iochannel.IChannel)Class.forName(channel.getClassName()).newInstance();
        }
        catch (Exception ex)
        {
            trace.error(m,"unable to create class for checking 'canSendAsynchron'");
            m.setStatusError();
            m.setResponse("zur Zeit ist es leider nicht möglich den Vokabeltrainer zu starten. Versuchen sie es später noch einmal");
        }
        if(obj.canSendAsynchron())
        {
            // check if there already a subscribed action for this channel installed
            // ....at the moment only one subscribtion possible)
            //
            if(SubscribedAction.findByChannelAndAction(channel,action).size()>0)
            {
                m.setResponse("Der Vokabeltrainer wurde bereits aktiviert.");
                return REPLY;
            }

            // create a new action if no useful available
            //
            if(action ==null)
            {
                action = Action.createInstance("simple Vokabeltrainer",0,"vokabeltrainer",0,0,"einfacher Vokabeltrainer welcher eine gewisse anzahl Vokabeln versendet");
            }


            // create a new subscribed action for the user
            //
            SubscribedAction.createInstance(60*24,                          // message interval
                                            -1l ,                           // last day
                                            new java.util.Date().getTime(), // start day
                                            0 ,                             // lock status
                                            m.getFormatType() ,             // Message format
                                            0 ,                             // send count
                                            -1 ,                            // end day (-1 means infinite)
                                            channel ,                       // communication channel
                                            action                          // the action
                                             );
        }
        else
        {
            m.setResponse("Auf diesem Kanal kann man keinen Vokabeltrainer starten. Bitte sende mir doch einfache eine SMS oder eMail zu.");
        }

		return REPLY;
	}

    /**
     * destroy the subsribed action for the user
     *
     */
	public int onStart2StopTrainer(HashMap userDataMap, String[] matchedGroups, Message m)
	{
        ShortiUser user = m.getShortiUser();
        CommunicationChannel channel = m.getChannel();
        Action               action  = Action.findByQuestion(QUESTION);

        // check if the user has an usefull channel for subscriptions
        //
        de.shorti.iochannel.IChannel obj=null;
        try
        {
            obj = (de.shorti.iochannel.IChannel)Class.forName(channel.getClassName()).newInstance();
        }
        catch (Exception ex)
        {
            trace.error(m,"unable to create class for checking 'canSendAsynchron'");
            m.setStatusError();
            m.setResponse("zur Zeit ist es leider nicht möglich den Vokabeltrainer zu starten. Versuchen sie es später noch einmal");
        }
        if(obj.canSendAsynchron())
        {
            ArrayList result = SubscribedAction.findByChannelAndAction(channel, action);
            if(result.size()<=0)
            {
                m.setResponse("Der Vokabeltrainer wurde noch nicht gestartet und kann somit auch nicht gestoppt werden");
                return REPLY;
            }

            Iterator iter = result.iterator();
            while(iter.hasNext())
            {
                SubscribedAction sa = (SubscribedAction)iter.next();
                sa.destroy();
            }
        }
        else
        {
            m.setResponse("Der Vokabeltrainer kann nicht über diesen Kanal administriert werden. Schicke mir bitte eine SMS oder eMail.");
        }
		return REPLY;
	}
}
