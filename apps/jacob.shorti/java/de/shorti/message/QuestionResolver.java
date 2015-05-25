package de.shorti.message;
/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.io.*;
import java.util.*;
import de.shorti.context.basis.GenericContext;
import de.shorti.util.basic.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.message.filter.*;

public class QuestionResolver
{

    static TraceDispatcher  trace         = TraceFactory.getTraceDispatcher();
    static HashMap          user2contexte = new HashMap();
    static ArrayList        contexte;
    static ArrayList        messageFilter;

   /**
    * iterate over all loaded contexte and try to find a
    * response to the incomming question/message
    *
    */
    public synchronized static void process( Message m)
    {
        m.setStatusProcessing();
        trace.info(m,"try to resolve question ["+m.getQuestion()+"]");

        try
        {
            // check if there a context cache available
            // ....if no cache available...create them
            //
            ArrayList c = (ArrayList)user2contexte.get(m.getShortiUser().getId());
            if(c ==null)
            {
                // this is the first call of the user since program
                // startup
                //
                trace.debug2(m, "create context cache for user");
                c = (ArrayList)contexte.clone();
                user2contexte.put(m.getShortiUser().getId(),c);
            }


            // FIRST: call all 'filters' for the incomming message
            //
            Iterator filterIter = messageFilter.iterator();
            while(filterIter.hasNext())
            {
                IMessageFilter filter = (IMessageFilter)filterIter.next();
                trace.debug1(m,"checking onBeginProcess-Filter '"+filter.getClass().getName()+"'");
                if(!filter.onBeginProcess(m))
                    return;
            }

            // SECOND: check each contex if the question conserns him
            //
            Iterator contextIter = c.iterator();
            while(contextIter.hasNext())
            {
                GenericContext context = (GenericContext)contextIter.next();
                if(context.process(m))
                {
                    m.setResponseContext(context.getContextRegistry());
                    m.setStatusSuccess();
                    contextIter.remove();
                    c.add(0,context);
                    break;
                }
            }

            // THIRD: call all filters for the outgoing message....last modifications at message possible....
            //
            filterIter = messageFilter.iterator();
            while(filterIter.hasNext())
            {
                IMessageFilter filter = (IMessageFilter)filterIter.next();
                trace.debug1(m,"checking onEndProcess-Filter '"+filter.getClass().getName()+"'");
                if(!filter.onEndProcess(m))
                    return;
            }
        }
        catch(Exception exc  )
        {
            exc.printStackTrace();
            m.setStatusFatal();
            trace.error(m,exc);
            m.disableSendResponse();
        }
        finally
        {
            m.setFinishTime();
        }
    }


    /**
    * load all classes which have implemente the 'de.shorti.context.IContext'
    * interface.
    */
    static
    {
        try
        {
            // load all filters stored in the property file
            //
            messageFilter = new ArrayList();
            for(int i=0; i<100;i++)
            {
                String filter = Config.getConfig("shorti").getProperty("message.filter.class"+i);

                if(filter!=null && filter.length()>0)
                {
                    try
                    {
                        trace.info("loading message filter :"+filter);
                        IMessageFilter obj = (IMessageFilter)Class.forName(filter).newInstance();
                        messageFilter.add(obj);
                    }
                    catch(java.lang.IllegalAccessException ex)
                    {
                        trace.fatal("class '"+filter+"' doesn't have a public standard constructor");
                    }
                    catch (Exception ex)
                    {
                        trace.fatal("class '"+filter+"' doesn't implement the interface: 'de.shorti.message.filter.IMessageFilter'");
                    }
                }
            }


            // Load all Context in the internal ContextStore
            //
            contexte  = new ArrayList();
            String path   = Config.getConfig("shorti").getProperty ("contextPath");
            trace.debug1("try to load the context classes from ["+path+"]");
            File directory= new File(path);
            File [] files = directory.listFiles();
            int i;
            for( i=0 ; i< files.length; i++)
            {
            String className;
            StringTokenizer token = new StringTokenizer(files[i].getName(),".");
            className = "de.shorti.context."+token.nextToken();
            if(!className.equals( "de.shorti.context.basis"))
            {
                try
                {
                    Object obj = Class.forName(className).newInstance();
                    if(obj instanceof GenericContext)
                    {
                        trace.debug1("loading context '"+className+"'");
                        contexte.add(obj);
                    }
                }
                catch (Exception ex)
                {
//                    trace.warning("not possible to load class "+className+" as context");
                }
            }
        }
      }
      catch (Exception ex)
      {
         trace.error(ex);
         ex.printStackTrace();
      }
   }
}