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
import de.shorti.baseknowledge.*;
import de.shorti.util.search.fuzzy.*;
import de.shorti.baseknowledge.objects.*;
import de.shorti.util.basic.*;
import de.shorti.message.Message;
import de.shorti.util.basic.*;
import org.apache.oro.text.regex.*;
import java.util.*;
import java.util.Locale;
import de.shorti.context.basis.*;

public class PlayList  extends GenericContext
{
    static TraceDispatcher  trace = TraceFactory.getTraceDispatcher();

    public PlayList()
    {
        super("./context/PlayList.xml");
    }
//
//	public int onStart2Station(HashMap userDataMap, String[] matchedGroups, Message m)
//	{
//		trace.debug2("called 'onStart2Station'");
//        fillTimeStamp(userDataMap, matchedGroups[0], matchedGroups[1]);
//		return REPLY;
//	}
//
//	public int onStart2Name(HashMap userDataMap, String[] matchedGroups, Message m)
//	{
//		trace.debug2("called 'onStart2Name'");
//        fillTimeStamp(userDataMap, matchedGroups[0], matchedGroups[1]);
//        fillStationByName(userDataMap, matchedGroups[2]);
//        responseByStation(userDataMap, matchedGroups, m);
//		return REPLY;
//	}
//
//	public int onStart2Frequency(HashMap userDataMap, String[] matchedGroups, Message m)
//	{
//        String frequency = null;
//
//		trace.debug2("called 'onStart2Frequency'");
//        fillTimeStamp(userDataMap, matchedGroups[1], matchedGroups[2]);
//        if(matchedGroups[0]!=null && matchedGroups[0].compareTo("")!=0)
//        {
//            frequency = matchedGroups[0];
//        }
//        else
//        {
//            frequency = matchedGroups[3];
//        }
//        fillStationByFrequency(userDataMap, frequency);
//        responseByStation(userDataMap, matchedGroups, m);
//		return REPLY;
//	}
//
//	public int onStation2Frequency(HashMap userDataMap, String[] matchedGroups, Message m)
//	{
//		trace.debug2("called 'onStation2Frequency'");
//        fillStationByFrequency(userDataMap, matchedGroups[0]);
//        responseByStation(userDataMap, matchedGroups, m);
//		return REPLY;
//	}
//
//	public int onStation2Name(HashMap userDataMap, String[] matchedGroups, Message m)
//	{
//		trace.debug2("called 'onStation2Name'");
//        fillStationByName(userDataMap, matchedGroups[0]);
//        responseByStation(userDataMap, matchedGroups, m);
//		return REPLY;
//	}
//
//    public void fillStationByFrequency(HashMap userDataMap, String frequency)
//    {
//        RadioRelayStation relayStation = (RadioRelayStation) RadioRelayStation.findByFrequenz(frequency).get(0);
//        RadioStation station = relayStation.getRadioStation();
//        userDataMap.put("STATION", station);
//    }
//
//    public void fillStationByName(HashMap userDataMap, String name)
//    {
//        RadioStation station = RadioStation.findByName(name);
//        userDataMap.put("STATION", station);
//    }
//
//    public void responseByStation(HashMap userDataMap, String[] matchedGroups, Message message)
//    {
//        StringBuffer sb = new StringBuffer();
//
//        RadioStation station = (RadioStation) userDataMap.get("STATION");
//        long time = ((Long) userDataMap.get("TIMESTAMP")).longValue();
//        ArrayList result = PlayListEntry.findByRadioStationAndTime(station, time, 5*60*1000);
//        Iterator it = result.iterator();
//        Date date = null;
//
//        message.setResponse("");
//        if(it.hasNext())
//        {
//            switch(message.getFormatType())
//            {
//                case Message.FORMAT_SMS:
//                    sb.append("Playlist " + station.getName() + ":");
//                    sb.append((char)10);
//                    sb.append((char)10);
//                    break;
//
//                case Message.FORMAT_GUI:
//                case Message.FORMAT_TXT:
//                    sb.append("Playlist " + station.getName() + ":\n\n");
//                    break;
//
//                default:
//                    message.log("Unsupported Channel-Type found!");
//                    message.setResponse("Konnte leider keinen Musiktitel finden :-(");
//                    message.setStatusWarning();
//                    return;
//            }
//        }
//        else
//        {
//            message.log("No PlayListEntries found!");
//            message.setResponse("Konnte leider keinen Musiktitel finden :-(");
//            message.setStatusWarning();
//            return;
//        }
//
//        while(it.hasNext())
//        {
//            PlayListEntry entry = (PlayListEntry) it.next();
//            date = new Date(entry.getStartTime());
//
//            switch(message.getFormatType())
//            {
//                case Message.FORMAT_SMS:
//                    sb.append(date.toString() + ":");
//                    sb.append(entry.getInterpret() + " - " );
//                    sb.append(entry.getTitle());
//                    sb.append((char)10);
//                    break;
//
//                case Message.FORMAT_GUI:
//                case Message.FORMAT_TXT:
//                    sb.append(date.toString() + ":");
//                    sb.append(entry.getInterpret() + " - " );
//                    sb.append(entry.getTitle());
//                    sb.append("\n");
//                    break;
//            }
//
//            if(!message.appendResponse(sb.toString()))
//            {
//                message.setStatusSuccess();
//                return;
//            }
//
//            sb = new StringBuffer();
//        }
//
//        message.setStatusSuccess();
//    }
//
//    public void fillTimeStamp(HashMap userDataMap, String date, String time)
//    {
//        long result = 0;
//
//        if(time != null && !time.trim().equalsIgnoreCase(""))
//        {
//            if(date != null && !date.trim().equalsIgnoreCase(""))
//            {
//                result = DateTime.CreateDate(date, time).getTime();
//            }
//            else
//            {
//                result = DateTime.CreateDate(time).getTime();
//            }
//        }
//        else
//        {
//            result = (new Date()).getTime();
//        }
//
//        userDataMap.put("TIMESTAMP", new Long(result));
//    }
}
