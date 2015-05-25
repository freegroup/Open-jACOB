package de.shorti.message;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import de.shorti.baseknowledge.objects.*;
import java.util.ArrayList;
import java.util.Iterator;
import de.shorti.util.basic.*;
import de.shorti.util.container.*;
import java.util.HashMap;
import de.shorti.iochannel.IChannel;

public class MessageManager
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    static BlockingQueue     messageQueue = new BlockingQueue(500);
    static HashMap           ioChannels   = new HashMap();

    /**
     *
     */
    public static void send(Message m)
    {
        try
        {
            // try to find the corresponding IO Channel
            IChannel channel =(IChannel)ioChannels.get(m.getChannel().getClassName());
            // send the message if the required channel available
            //
            if(channel != null)
            {
                trace.debug1(m,"sending message via channel ["+channel.getClass().getName()+"]");
                if(channel.send(m)==true)
                    m.setStatusSuccess();
                else
                    m.setStatusError();
            }
            else
            {
                trace.error(m,"Channel not found: "+m.getChannel().getClassName());
            }
        }
        finally
        {
            m.setFinishTime();
        }
    }

    /**
     *
     */
    public static Message receive()
    {
        Message m =(Message)messageQueue.remove();
        trace.debug2(m,"message removed from internal message queue for processing");
        return m;
    }

    /**
     * check the OutMessage table for outgoing messages and send
     * them via the required CommunicationChannel
     */
//    public static void checkOutMessage()
//    {
//        while(true)
//        {
//            try
//            {
//                ArrayList result = OutMessage.getAll();
//                Iterator iter = result.iterator();
//                while(iter.hasNext())
//                {
//                    OutMessage omessage= (OutMessage)iter.next();
//                    Message m = new Message(null, omessage.getChannel(),"MessageManager");
//                    m.setResponse(omessage.getMessage());
//                    trace.debug2(m, "new message for deliver readed from database table");
//                    OutMessage.destroyInstance(omessage.getId());
//                    send(m);
//                }
//                Thread.sleep(5000);
//            }
//            catch(Exception exc)
//            {
//                trace.fatal(exc);
//            }
//        }
//    }

    /**
     * check the hadns over channel for incomming messages and store them
     * in the common message queue.
     *
     * This function/thread runs for each created communication channel.
     *
     */
    public static void receiveMessage(de.shorti.iochannel.IChannel channel)
    {
        while(true)
        {
            try
            {
                Message m = channel.receive();
                if(m!=null)
                {
                    trace.debug1(m,"adding new message to message-queue ( created from:["+channel.getClass().getName()+"]) ");
                    messageQueue.add(m);
                }
                else
                {
                    Thread.sleep(1000);
                }
            }
            catch(Exception exc)
            {
                trace.fatal(exc);
                // sleep 1 Minutes to avoid system overrun on a returned error
                //
                try{Thread.sleep(1000*60*1);}catch (InterruptedException ex){}
            }
        }
    }

    /**
     *
     */
    static
    {
        // start the thread for the OutMessage-Table
        //
//        try
//        {
//            Thread thread = new Thread ( new Runnable ( ){public void run ( ){checkOutMessage();}});
//            thread.setDaemon(true);
//            thread.start();
//        }
//        catch (Exception ex)
//        {
//        }

        // create the different IO Channels
        //
        for(int i=0; i<100;i++)
        {
            String channel = Config.getConfig("shorti").getProperty("iochannel.class"+i);
            if(channel==null || channel.length()<1)
            {
                // channel not set
                //
            }
            else
            {
                //load the channel
                //
                try
                {
                    trace.info("loading ioChannel :"+channel);
                    final de.shorti.iochannel.IChannel obj = (de.shorti.iochannel.IChannel)Class.forName(channel).newInstance();
                    if(obj.start()==true)
                    {
                        ioChannels.put( channel, obj);
                        Thread thread = new Thread ( new Runnable ( ){public void run ( ){receiveMessage(obj);}});
                        thread.setDaemon(true);
                        thread.start();
                    }
                    else
                    {
                        trace.fatal("unable to start iochannel ["+channel+"]");
                    }
                }
                catch (Exception ex)
                {
                    trace.error("error during loading the iochannel class :"+channel);
                }
            }
        }
    }
}