/*******************************************************************************
 *    This file is part of Open-jACOB
 *    Copyright (C) 2005-2006 Tarragon GmbH
 * 
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; version 2 of the License.
 * 
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License     
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
 *    USA
 *******************************************************************************/
package de.tif.jacob.cluster.impl;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.MembershipListener;
import org.jgroups.Message;
import org.jgroups.Transport;
import org.jgroups.View;
import org.jgroups.blocks.PullPushAdapter;
import org.w3c.dom.Document;

import de.tif.jacob.cluster.ClusterManager;

/**
 * Distributed group implementation by means of using adhoc-networking
 * technologie. A distributed group provides the following functionality: <br>
 * <li>maintaining group membership
 * <li>sending of messages to one or to all members
 * <li>providing of local data to all members (e.g. user counts, node name,
 * etc.)
 * <li>determining of exactly one coordinator
 * 
 * @author Andreas Sonntag
 */
public class Group
{
  static public final transient String RCS_ID = "$Id: Group.java,v 1.2 2010-04-26 19:56:53 sonntag Exp $";
  static public final transient String RCS_REV = "$Revision: 1.2 $";
  
  static private final transient Log logger = LogFactory.getLog(Group.class);
  
  private static final String NODENAME_PREFIX = "$nodename:";
  private static final String MEMBER_SINCE_PREFIX = "$since:";
  
  private static final String SET_VALUES_MESSAGE_TYPE = "$SET_VALUES";
  private static final String REQ_VALUES_MESSAGE_TYPE = "$REQ_VALUES";
  private static final String RESP_VALUES_MESSAGE_TYPE = "$RESP_VALUES";
  
  /**
   * Map(String:prefix -> Serializable:value)
   */
  private final Map localEntries;
  
  /**
   * Map(String:memberId -> Map(String:prefix -> Serializable:value))
   */
  private Map remoteEntries;
  
  private final MyPullPushAdapter adapter;
  private final JChannel channel;
  private final String localMemberId;
  private final String name;
  
  private boolean started = false;
  
  /**
   * Creates a distributed group.
   * 
   * @param name
   *          the name of the group
   * @param props
   *          the properties of the group, which are implementation specific
   * @throws Exception
   *           on any problem
   */
  public Group(String name, String props) throws Exception
  {
    if (logger.isInfoEnabled())
      logger.info("### Creating group '" + name + "'");
    
    this.name = name;
    this.localEntries = new HashMap();
    this.remoteEntries = new HashMap();
    
    // register nodename of this group member
    // note: data will be requested by means of MyMembershipListener.viewAccepted()
    //
    this.localEntries.put(NODENAME_PREFIX, ClusterManager.getNodeName());
    this.localEntries.put(MEMBER_SINCE_PREFIX, new Date(System.currentTimeMillis()));
    
    // create channel with the given properties
    // assume JGroups XML format
    //
    Document doc = XMLUtils.newDocument(new ByteArrayInputStream(props.getBytes()));
    this.channel = new JChannel(doc.getDocumentElement());
    this.channel.setOpt(Channel.GET_STATE_EVENTS, Boolean.TRUE);
    this.channel.setOpt(Channel.SUSPECT, Boolean.TRUE); 
    this.channel.connect(name);
    this.localMemberId = this.channel.getLocalAddress().toString();
    this.adapter = new MyPullPushAdapter(this.channel);
    this.adapter.addMembershipListener(new MyMembershipListener());
    
    // register listener for multiple value changes
    //
    this.adapter.registerListener(SET_VALUES_MESSAGE_TYPE, new AbstractMessageListener()
    {
      public void receive(Message msg)
      {
        MapValue[] mapValues = (MapValue[]) msg.getObject();
        Group.this.processRemoteEntries(msg.getSrc(), mapValues);
      }
    });
    
    // register listener for value request
    //
    this.adapter.registerListener(REQ_VALUES_MESSAGE_TYPE, new AbstractMessageListener()
    {
      public void receive(Message msg)
      {
        // source address requests values -> send them to requester
        Address srcAddr = msg.getSrc();
        Group.this.propagateLocalEntries(srcAddr);
      }
    });
    
    // register listener for value response
    //
    this.adapter.registerListener(RESP_VALUES_MESSAGE_TYPE, new AbstractMessageListener()
    {
      public void receive(Message msg)
      {
        MapValue[] mapValues = (MapValue[]) msg.getObject();
        
        if (logger.isInfoEnabled())
          logger.info("### " + Group.this + " received requested values from " + msg.getSrc());
        
        Group.this.processRemoteEntries(msg.getSrc(), mapValues);
      }
    });
    
  }
  
  /**
   * Really starts the group, i.e. 
   */
  public void start() throws Exception
  {
    // Attention: this method must not be synchronized, because otherwise JGroups would report 
    // an error: setState() never got called (due to a synchronization lock of PushPullAdapter
    // thread)
    
    if (logger.isInfoEnabled())
      logger.info("### Starting " + this);
    
    // enforce start after all notifiers have been registered, because we do not want to miss
    // viewChanged-Event!!!
    this.adapter.enforceStart();
    
    // must be before putValue()
    this.started = true;
    
    if (logger.isInfoEnabled())
      logger.info("### " + this + " started: isCoordinator=" + isCoordinator());
  }

  /**
   * Returns the name of this group.
   * 
   * @return group name
   */
  public String getName()
  {
    return name;
  }
  
  /**
   * Prints internal information of this group.
   * 
   * @param writer
   *          the writer to print information to.
   */
  public void printInfo(PrintWriter writer)
  {
    writer.println("Group name:");
    writer.println("\t" + this.name);

    writer.println("Group members:");
    if (isChannelClosed())
    {
      writer.print("\t");
      writer.print(this.localMemberId);
      writer.print(" (closed)");
      writer.println();
    }
    else
    {
      List members = this.channel.getView().getMembers();
      for (int i = 0; i < members.size(); i++)
      {
        Address member = (Address) members.get(i);
        writer.print("\t");
        writer.print(((Address) members.get(i)).toString());
        if (i == 0)
          writer.print(" (coordinator)");
        if (member.equals(this.channel.getLocalAddress()))
          writer.print(" (local)");
        writer.println();
      }

      writer.println("Group content:");
      // get a consistent view and avoid ConcurrentModificationException
      Set tempSet = new TreeSet();
      Map tempMap = new HashMap();
      synchronized (this)
      {
        for (Iterator it = this.remoteEntries.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry entry = (Map.Entry) it.next();
          String memberId = (String) entry.getKey();
          Map remoteValueMap = (Map) entry.getValue();
          for (Iterator it2 = remoteValueMap.entrySet().iterator(); it2.hasNext();)
          {
            Map.Entry entry2 = (Map.Entry) it2.next();
            String prefix = (String) entry2.getKey();
            Serializable value = (Serializable) entry2.getValue();
            String key = prefix + memberId;
            tempSet.add(key);
            tempMap.put(key, value);
          }
        }
        for (Iterator it = this.localEntries.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry entry = (Map.Entry) it.next();
          String prefix = (String) entry.getKey();
          Serializable value = (Serializable) entry.getValue();
          String key = prefix + this.localMemberId;
          tempSet.add(key);
          tempMap.put(key, value);
        }
      }
      for (Iterator it = tempSet.iterator(); it.hasNext();)
      {
        Object key = it.next();
        writer.println("\t" + key + "=" + tempMap.get(key));
      }
    }

    // print protocol stack
    //
    writer.println("Group protocol stack:");
    String protocolSpec = this.channel.printProtocolSpec(true);
    if (protocolSpec != null)
      protocolSpec = StringUtils.replace(protocolSpec, "\n", "\r\n\t");
    writer.print("\t");
    writer.println(protocolSpec);
  }
  
  private void checkStarted()
  {
    if (!this.started)
      throw new IllegalStateException("Group has not been started");
  }
  
  private boolean isChannelClosed()
  {
    return !this.channel.isOpen() || !this.channel.isConnected();
  }
  
  /**
   * Sets the given value for the given prefix for this member (node) and
   * propagates it to all members of this group.
   * 
   * @param prefix
   *          the prefix
   * @param value
   *          the value
   */
  public synchronized void setValue(String prefix, Serializable value)
  {
    checkStarted();

    if (value == null)
      throw new NullPointerException("No value specified for '" + prefix + "'");

    if (!isChannelClosed())
    {
      MapValue[] mapValues = new MapValue[1];
      mapValues[0] = new MapValue(prefix, value);
      try
      {
        sendMessageInternal(null, SET_VALUES_MESSAGE_TYPE, mapValues);
      }
      catch (Exception ex)
      {
        if (logger.isWarnEnabled())
          logger.warn("### " + this + " propagation of value " + mapValues[0] + " failed!", ex);
      }
      this.localEntries.put(prefix, value);
    }
  }
  
  /**
   * Returns whether this member (node) is the coordinator of the group.
   * 
   * @return <code>true</code> if this node is the coordinator, otherwise
   *         <code>false</code>.
   */
  public synchronized boolean isCoordinator()
  {
    if (isChannelClosed())
      return false;

    return isCoordinator(this.channel.getLocalAddress());
  }

  private synchronized boolean isCoordinator(Address localAddress)
  {
    if (isChannelClosed())
      return false;

    return localAddress != null && localAddress.equals(this.channel.getView().getMembers().get(0));
  }
  
  /**
   * Gets the value of the given member for the given prefix.
   * 
   * @param prefix
   *          the prefix to get value for
   * @param member
   *          the member
   * @return the value or <code>null</code>, which means that either the
   *         group is already closed or really no value exists for that member.
   */
  public synchronized Serializable getValue(String prefix, Member member)
  {
    checkStarted();
    
    if (isChannelClosed())
      return null;

    String memberId = member.getId();
    
    if (member.isLocal())
    {
      return (Serializable) this.localEntries.get(prefix);
    }
    else
    {
      Map remoteValueMap = (Map) this.remoteEntries.get(memberId);
      if (remoteValueMap == null)
      {
        return null;
      }
      return (Serializable) remoteValueMap.get(prefix);
    }
  }

  /**
   * Gets the values of all group members for the given prefix.
   * 
   * @param prefix
   *          the prefix to get values for.
   * @return <code>List</code> of all existing values, i.e. values which are
   *         not <code>null</code>, or <code>null</code> if group is already closed.
   */
  public synchronized List getValues(String prefix)
  {
    checkStarted();

    // just to avoid exception in case channel is dead (as occurred at Achim in old version)
    // we are no quite sure whether this would not occur again :-(
    if (isChannelClosed())
      return null;

    List members = this.channel.getView().getMembers();
    List result = new ArrayList(members.size());

    for (int i = 0; i < members.size(); i++)
    {
      Serializable value;
      String memberId = ((Address) members.get(i)).toString();
      Map remoteValueMap = (Map) this.remoteEntries.get(memberId);
      if (remoteValueMap == null)
      {
        if (this.localMemberId.equals(memberId))
        {
          value = (Serializable) this.localEntries.get(prefix);
        }
        else
        {
          value = null;
        }
      }
      else
      {
        value = (Serializable) remoteValueMap.get(prefix);
      }

      if (value == null)
      {
        if (logger.isWarnEnabled())
          logger.warn("### " + this + " contains no value for : " + prefix + ":" + memberId);
      }
      else
        result.add(value);
    }
    return result;
  }
  
  /**
   * Returns all members of this group.
   * 
   * @return List of {@link Member}
   */
  public synchronized List getMembers()
  {
    checkStarted();
    
    if (isChannelClosed())
      return null;

    List members = this.channel.getView().getMembers();
    List result = new ArrayList(members.size());
    for (int i=0; i<members.size();i++)
    {
      result.add(new Member((Address) members.get(i)));
    }
    return result;
  }
  
  /**
   * Sends a message to all member (including this member) of the group.
   * 
   * @param messageType
   *          the message type used to distinguish between the different
   *          listeners
   * @param message
   *          the message itself, which could be in principle an serializable
   *          object or <code>null</code>
   * @throws Exception
   *           on any problem
   * 
   * @see #register(String, MessageListener)
   */
  public synchronized void sendMessage(String messageType, Serializable message) throws Exception
  {
    sendMessage(null, messageType, message);
  }
  
  /**
   * Sends a message to the given member of the group.
   * 
   * @param dest
   *          the member to send the message to or <code>null</code> to send
   *          the message to all members.
   * @param messageType
   *          the message type used to distinguish between the different
   *          listeners
   * @param message
   *          the message itself, which could be in principle an serializable
   *          object or <code>null</code>
   * @throws Exception
   *           on any problem
   * 
   * @see #register(String, MessageListener)
   */
  public synchronized void sendMessage(Member dest, String messageType, Serializable message) throws Exception
  {
    checkStarted();

    if (isChannelClosed())
      return;

    if (messageType == null)
      throw new NullPointerException("messageType is null");

    sendMessageInternal(dest == null ? null : dest.address, messageType, message);
  }
  
  private void sendMessageInternal(Address destAddress, String messageType, Serializable message) throws Exception
  {
    this.adapter.send(messageType, new Message(destAddress, this.channel.getLocalAddress(), message));
  }
  
  private synchronized void propagateLocalEntries(Address destAddress)
  {
    // Propagate all local entries
    //
    MapValue[] mapValues = new MapValue[Group.this.localEntries.size()];
    int i = 0;
    for (Iterator it = Group.this.localEntries.entrySet().iterator(); it.hasNext(); i++)
    {
      Map.Entry entry = (Map.Entry) it.next();
      mapValues[i] = new MapValue((String) entry.getKey(), (Serializable) entry.getValue());
    }
    try
    {
      sendMessageInternal(destAddress, RESP_VALUES_MESSAGE_TYPE, mapValues);
    }
    catch (Exception ex)
    {
      if (logger.isWarnEnabled())
        logger.warn("### " + Group.this + " propagation of values failed!", ex);
    }
  }
  
  private synchronized void processRemoteEntries(Address srcAddress, MapValue[] mapValues)
  {
    String srcid = srcAddress.toString();
    
    // ignore own messages
    if (!srcid.equals(Group.this.localMemberId))
    {
      Map remoteValueMap = (Map) Group.this.remoteEntries.get(srcid);
      if (remoteValueMap == null)
      {
        if (logger.isWarnEnabled())
          logger.warn("### " + this + " discarding message from unknown member: " + srcid);
      }
      else
      {
        for (int i = 0; i < mapValues.length; i++)
        {
          remoteValueMap.put(mapValues[i].prefix, mapValues[i].value);
        }
      }
    }
  }
  
  /**
   * Internal JGroups message listener, which forwards messages to user message
   * listener.
   * 
   * @author Andreas Sonntag
   */
  private class InternalMessageListener implements org.jgroups.MessageListener
  {
    private final IGroupMessageListener listener;
    private final boolean receiveOwn;
    
    private InternalMessageListener(IGroupMessageListener listener, boolean receiveOwn)
    {
      this.listener = listener;
      this.receiveOwn = receiveOwn;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#getState()
     */
    public byte[] getState()
    {
      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#receive(org.jgroups.Message)
     */
    public void receive(Message msg)
    {
      // discard own message if not desired
      //
      if (!this.receiveOwn && Group.this.localMemberId.equals(msg.getSrc().toString()))
      {
        return;
      }
      
      try
      {
        this.listener.receive((Serializable) msg.getObject());
      }
      catch (Exception ex)
      {
        logger.error("Listener error on receiving message", ex);
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#setState(byte[])
     */
    public void setState(byte[] state)
    {
      // nothing to do here
    }
  }
  
  private abstract class AbstractMessageListener implements org.jgroups.MessageListener
  {
    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#getState()
     */
    public byte[] getState()
    {
      return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgroups.MessageListener#setState(byte[])
     */
    public void setState(byte[] state)
    {
      // nothing to do here
    }
  }
  
  /**
   * Registers a message listener for a given message type.
   * 
   * @param messageType
   *          the type of messages to listen for
   * @param listener
   *          the message listener
   * @param receiveOwn
   *          <code>true</code> if own messages should be received, otherwise
   *          <code>false</code>
   */
  public synchronized void register(String messageType, IGroupMessageListener listener, boolean receiveOwn)
  {
    if (isChannelClosed())
      return;
    
    if (messageType == null)
      throw new NullPointerException("messageType is null");

    if (listener == null)
      throw new NullPointerException("listener is null");

    this.adapter.registerListener(messageType, new InternalMessageListener(listener, receiveOwn));
  }

  /**
   * Closes the group and releases all internal resources.
   */
  public synchronized void close()
  {
    if (logger.isInfoEnabled())
      logger.info("### Closing " + this);

    // tear down hashtable, adapter and channel
    this.adapter.stop();
    this.channel.close();

    if (logger.isInfoEnabled())
      logger.info("### " + this + " closed");
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return "Group '" + this.name + "' (member " + this.localMemberId + ")";
  }
  
  /**
   * Prefix value pairs for exchanging map entries
   * 
   * @author Andreas Sonntag
   */
  private static class MapValue implements java.io.Serializable
  {
    /** use serialVersionUID for interoperability */
    private static final long serialVersionUID = -753651235300000001L;
    
    private final String prefix;
    private final Serializable value;
    
    private MapValue(String prefix, Serializable value)
    {
      this.prefix = prefix;
      this.value = value;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return this.prefix + this.value;
    }
  }
  
  /**
   * Implementation of a group member.
   * 
   * @see Group#getMembers()
   * 
   * @author Andreas Sonntag
   */
  public final class Member
  {
    private final Address address;
    private final String memberId;
    
    private Member(Address address)
    {
      this.address = address;
      this.memberId = address.toString();
    }
    
    /**
     * Returns the node name of the member.
     * 
     * @return the node name or <code>null</code> if not distributed so far.
     * 
     * @see ClusterManager#getNodeName()
     */
    public String getNodeName()
    {
      return (String) getValue(NODENAME_PREFIX, this);
    }
    
    /**
     * Returns the timestamp then the member joined the group.
     * 
     * @return the joining timestamp or <code>null</code> if not accessible.
     */
    public Date since()
    {
      return (Date) getValue(MEMBER_SINCE_PREFIX, this);
    }
    
    /**
     * Checks whether this member is the local node.
     * 
     * @return <code>true</code> member is the local node, otherwise <code>false</code> 
     */
    public boolean isLocal()
    {
      return this.memberId.endsWith(Group.this.localMemberId);
    }
    
  	/* (non-Javadoc)
  	 * @see java.lang.Object#hashCode()
  	 */
  	public int hashCode()
    {
      return this.memberId.hashCode();
    }
  	
    /**
     * Checks whether this member is the group coordinator.
     * 
     * @return <code>true</code> member is the group coordinator, otherwise
     *         <code>false</code>
     */
  	public boolean isCoordinator()
  	{
  	  return Group.this.isCoordinator(this.address);
  	}
  	
  	/* (non-Javadoc)
  	 * @see java.lang.Object#equals(java.lang.Object)
  	 */
  	public boolean equals(Object anObject)
  	{
  		if (this == anObject)
  		{
  			return true;
  		}
  		if (anObject instanceof Member)
  		{
  		  Member another = (Member) anObject;
  			return this.memberId.equals(another.memberId);
  		}
  		return false;
  	}
  	
  	/**
     * The internal id of the member.
     * 
     * @return internal id
     */
  	public String getId()
  	{
  	  return this.memberId;
  	}
  	
  	/* (non-Javadoc)
  	 * @see java.lang.Object#toString()
  	 */
  	public String toString()
  	{
  		return this.memberId;
  	}
  }
  
  /**
   * Internal JGroups channel membership listener.
   * 
   * @author Andreas Sonntag
   */
  private class MyMembershipListener implements MembershipListener
  {
    private MyMembershipListener()
    {
    }
    
    /* (non-Javadoc)
     * @see org.jgroups.MembershipListener#block()
     */
    public void block()
    {
      // nothing to do here
    }
    
    /* (non-Javadoc)
     * @see org.jgroups.MembershipListener#suspect(org.jgroups.Address)
     */
    public void suspect(Address suspected_mbr)
    {
      if (logger.isWarnEnabled())
        logger.warn("### " + Group.this + " suspect member: " + suspected_mbr);
    }
    
    /* (non-Javadoc)
     * @see org.jgroups.MembershipListener#viewAccepted(org.jgroups.View)
     */
    public void viewAccepted(View new_view)
    {
      if (logger.isInfoEnabled())
        logger.info("### " + Group.this + " new view: " + new_view.printDetails());
      
      List members = new_view.getMembers();
      synchronized (Group.this)
      {
        // Prepare new remote members
        //
        Map newRemoteEntries = new HashMap();
        for (int i = 0; i < members.size(); i++)
        {
          Address member = (Address) members.get(i);
          String memberId = member.toString();
          
          // ignore local messages?
          if (!Group.this.localMemberId.equals(memberId))
          {
            Map remoteValueMap = (Map) Group.this.remoteEntries.get(memberId);
            if (remoteValueMap == null)
            {
              // request entries from new member
              try
              {
                if (logger.isInfoEnabled())
                  logger.info("### " + Group.this + " requesting values from " + memberId);
                
                sendMessageInternal(member, REQ_VALUES_MESSAGE_TYPE, null);
              }
              catch (Exception ex)
              {
                if (logger.isWarnEnabled())
                  logger.warn("### " + Group.this + " request of values from " + memberId + " failed!", ex);
              }
              remoteValueMap = new HashMap();
            }
            newRemoteEntries.put(memberId, remoteValueMap);
          }
        }
        Group.this.remoteEntries = newRemoteEntries;
      }
    }
  }
  
  /**
   * Extended version of buggy JGroups PullPushAdapter. The original version
   * immediately starts the adapter (within constructors), which leads to the
   * creating of a thread pulling events from the channel. The problem is that
   * no consumers (e.g. distributed hashtable) might have been registered so far
   * leading to lost events (e.g. view change event)!!!
   * 
   * @author Andreas Sonntag
   */
  private static class MyPullPushAdapter extends PullPushAdapter
  {
    private boolean startEnabled = false;
    
    /**
     * @param transport
     */
    protected MyPullPushAdapter(Transport transport)
    {
      super(transport);
    }

    /* (non-Javadoc)
     * @see org.jgroups.blocks.PullPushAdapter#start()
     */
    public void start()
    {
      // only start if not postponed
      if (this.startEnabled)
        super.start();
    }
    
    public void enforceStart()
    {
      // now enable start
      this.startEnabled = true;
      start();
    }
  }
}
