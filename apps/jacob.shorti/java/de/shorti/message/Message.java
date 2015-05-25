package de.shorti.message;
/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author David Sommer & Andreas Herz
 * @version 1.0
 */
import java.io.ByteArrayOutputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import de.shorti.baseknowledge.objects.Account;
import de.shorti.baseknowledge.objects.BillingInformation;
import de.shorti.baseknowledge.objects.CommunicationChannel;
import de.shorti.baseknowledge.objects.ContextRegistry;
import de.shorti.baseknowledge.objects.Country;
import de.shorti.baseknowledge.objects.Language;
import de.shorti.baseknowledge.objects.PartnerCompany;
import de.shorti.baseknowledge.objects.Request;
import de.shorti.baseknowledge.objects.RequestLog;
import de.shorti.baseknowledge.objects.ShortiUser;
import de.shorti.baseknowledge.objects.SystemConfiguration;
import de.shorti.util.basic.TraceDispatcher;
import de.shorti.util.basic.TraceFactory;


public class Message
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    private long       inquireTimeout   = 1000*60*5; // 5 Minutes
    private String     subject          = null;
    private String     response;
    private String     question;
	private double     longitude;
	private double     latitude;
    private boolean    hasPosition      = false;
    private boolean    needPosition     = false;
    private boolean    mustReportError  = false;
    private CommunicationChannel   channel;
    private Request    request;
    private ShortiUser user= null;
	private String     formatType  = "DEFAULT";
    private boolean    sendResponse = true; // per default send response
    private int        userAmount  = 0;

    /**
     *
     */
    private Message()
    {
    }


    /**
     *
     */
    public Message(String _formatType, CommunicationChannel _channel, String _question)
    {
        Account    account = null;
        boolean    newUser = false;

		formatType = _formatType;
        response   = "";
        question   = _question;
        channel    = _channel;
        // try to find the corresponding user account......if no account
        // in the database creat a default for this new user
        //
        user = channel.getShortiUser();
        if(user != null)
        {
            account = user.getAccount();
        }
        else
        {
            // no user in the database....create a default for this
            // mobile phone number
            int max_warning    = Integer.parseInt(SystemConfiguration.findByName("INI_CREDIT_WARNING").getValue());
            int service_factor = Integer.parseInt(SystemConfiguration.findByName("INI_SERVICE_FACTOR").getValue());
            int given_credit   = Integer.parseInt(SystemConfiguration.findByName("INI_GIVEN_CREDIT").getValue());

            account = Account.createInstance(0,0,now(),service_factor,max_warning,given_credit);

            // try to detect the home country
            //
            Country country = Country.getHomeCountry(channel.getConnectionString());

            // get the default language for the user
            //
            Language lang = Language.findByShortName("DE");

            // create the user in the database
            //
            user    = ShortiUser.createInstance(channel.getConnectionString(),lang,country,account);

            channel.setShortiUser(user,true);
            newUser = true;
        }

        // create a new request object in the database.....this object is
        // required for the transaktion logging
        //
        request    = Request.createInstance(channel, question);

        // make a log entry if this the first call for the user
        //
        if(newUser==true)
            log("new user/account created for this request userId:"+user.getId()+" accountId:"+account.getId());
    }

    /**
     *
     */
    public String getResponse()
    {
        return response;
    }

    /**
     *
     */
    public Request getRequest()
    {
        return request;
    }

    /**
     *
     */
    public void setResponse(String _response)
    {
        response = _response;
        request.setAnswer(response,true);
    }


    public void disableSendResponse()
    {
        sendResponse= false;
    }

    public boolean getSendResponse()
    {
        return sendResponse;
    }

   /**
     *
     */
    public void setQuestion(String _question)
    {
        question = _question;
    }

    /**
     *
     */
    public void setResponseContext(ContextRegistry context)
    {
        request.setContextRegistry(context,true);
    }

    /**
     *
     */
    public ContextRegistry getResponseContext()
    {
        return request.getContextRegistry();
    }

    /**
     *
     */
    public void setStatusProcessing()
    {
        log("start with processing the message");
        request.setStatus(Request.PROCESSING,true);
    }

    /**
     *
     */
    public void setStatusSuccess()
    {
        trace.debug1(this,"set success status");
        request.setStatus(Request.SUCCESS,true);
    }


    public long getTimeForProcess()
    {
        return request.getTimeForProcess();
    }


    public long getCurrentTimeForProcess()
    {
        return request.getCurrentTimeForProcess();
    }

    /**
     *
     */
    public void setStatusWarning()
    {
        mustReportError = true;
        trace.warning(this,"set warning status");
        request.setStatus(Request.WARNING,true);
    }

    public void setStatusError()
    {
        mustReportError = true;
        trace.error(this,"set error status");
        request.setStatus(Request.ERROR,true);
    }

    public void setLongitude(double value)
    {
        longitude = value;
    }

    public void setLatitude(double value)
    {
        latitude = value;
    }

    public void setHasPosition(boolean value)
    {
        hasPosition = value;
    }

    public void setNeedPosition() // only a toggle flag
    {
        needPosition = true;
    }

    public void setSubject (String _subject)
    {
        subject = _subject;
    }

    public String getSubject()
    {
        return subject;
    }

    /**
     *
     */
    public void setStatusFatal()
    {
        mustReportError = true;
        trace.fatal(this,"set fatal status");
        request.setStatus(Request.FATAL,true);
    }

    /**
     *
     */
    public void setFinishTime()
    {
        request.setFinishDate(now().getTime(),true);
    }

    /**
     *
     */
    public void appendBilling(PartnerCompany partnerCompany, int cent, String reason)
    {

        if(BillingInformation.createInstance(partnerCompany,getCodeLine(), reason, cent, request)!=null)
        {
            userAmount +=cent;
        }
    }

    /**
     * append an additonal text to the message text
     *
     */
//    public boolean appendResponse(String appendText)
//    {
//        boolean result = true;
//
//        // check for the different message types
//        // SMS can only store 160 characters .....for example
//        //
//        if(format    case FORMAT_SMS:
//                if((response.length()+appendText.length()) > 159)
//                {
//                    result = false;
//                }
//                else
//                {
//                    response = response + appendText;
//                }
//                break;
//            default:
//                response = response + appendText;
//                break;
//        }
//
//        if(result)
//            request.setAnswer(response,true);
//
//        return result;
//    }

    public void sendErrorMessage()
    {
        StringBuffer sb = new StringBuffer();

        sb.append("Error during try to resolve question\n");
        sb.append("====================================\n\n\n");
        ArrayList result = request.getRequestLogs();
        Iterator iter = result.iterator();
        long startDate = ((RequestLog)result.get(result.size()-1)).getInsertDate();
        while(iter.hasNext())
        {
            RequestLog rlog = (RequestLog)iter.next();
            sb.append(((rlog.getInsertDate() - startDate)/1000f)+" \t");
            sb.append(rlog.getMessage()+"\n");
        }
        sb.append("\n");

        iter = CommunicationChannel.findByIsAdmin(1).iterator();
        while(iter.hasNext())
        {
            CommunicationChannel channel = (CommunicationChannel)iter.next();
            Message m = new Message("TXT",channel,"Error Report");
            m.setResponse(sb.toString());
            MessageManager.send(m);
        }
    }

    /**
     * return the answer type for this message. This information
     * can be importand for the different context types. The context can
     * pre-formate the answer with HTML or RTF tags corresponding to the
     * answer type/channel
     *
     */
    public String getFormatType()
    {
        return formatType;
    }

    /**
     *
     */
    public int getUserAmount()
    {
        return userAmount;
    }

    /**
     *
     */
    public boolean getHasPosition()
    {
        return hasPosition;
    }

   /**
     *
     */
    public boolean getNeedPosition()
    {
        return needPosition;
    }

   /**
     *
     */
    public boolean getMustReportError()
    {
        return mustReportError;
    }

    /**
     *
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     *
     */
    public double getLatitude()
    {
        return latitude;
    }
    /**
     *
     */
    public CommunicationChannel  getChannel()
    {
        return channel;
    }

    public ShortiUser getShortiUser()
    {
        return user;
    }

    public String getQuestion()
    {
        return question;
    }

    public Language getPreferedLanguage()
    {
        return user.getPreferedLanguage();
    }

    public void log(String message)
    {
        RequestLog.createInstance(message,request);
    }

    java.sql.Date now()
    {
        return new java.sql.Date(new java.util.Date().getTime());
    }
    private String getCodeLine()
    {
        String codeLine = null;
        try
        {
            // Retrieve call stack
            Exception exception = new Exception();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            PrintWriter outWriter = new PrintWriter( outStream );
            exception.printStackTrace( outWriter );
            outWriter.flush();

            // Retrieve class name from call stack. Sample:
            // java.lang.Exception
            //	at com.sapmarkets.waf.instrument.Trace.retrieveclassName(Trace.java:498)
            //	at com.sapmarkets.waf.instrument.Trace.retrieveclassName(Trace.java:373)
            //	at com.sapmarkets.waf.instrument.Trace.exiting(Trace.java:155)
            //	at com.sapmarkets.waf.instrument.test.PackageTest.testTrace(PackageTest.java:164)
            LineNumberReader lineNumberReader = new LineNumberReader( new StringReader( outStream.toString() ) );
            lineNumberReader.readLine();// Skip exception header
            lineNumberReader.readLine();// Skip at least one call stack entry
            lineNumberReader.readLine();// Skip at least one call stack entry
            codeLine = lineNumberReader.readLine().trim();
        }
        catch( Exception exception )
        {
        }
        return codeLine.trim();
    }
}