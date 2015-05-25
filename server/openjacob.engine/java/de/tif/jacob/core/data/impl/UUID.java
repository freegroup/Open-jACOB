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
package de.tif.jacob.core.data.impl;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ConcurrentModificationException;
import java.util.Random;

/**
 * Implements time-based UUIDs (version 1 UUIDs) according to the IETF draft on
 * UUIDs and GUIDs from February 4, 1998 (draft-leach-uuids-guids-01.txt).
 * 
 * @version 1.0
 */

public class UUID implements Cloneable, Serializable
{
	static public transient final String        RCS_ID = "$Id: UUID.java,v 1.1 2007/01/19 09:50:35 freegroup Exp $";
	static public transient final String        RCS_REV = "$Revision: 1.1 $";
	
	public final static int STRING_LENGTH = 36;
	
	private final static long timeDiff = 0x01B21DD213814000L;
	private final static char[] hexCharacters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private final static boolean nativeLibraryAvailable;

	private final static int randomClockSequence;
	private final static byte[] randomNodeId;

	private static long previousTime = 0;

	private final String uuid;

	static {
		boolean loadSuccessful = false;
		try
		{
			System.loadLibrary("UUID");
			loadSuccessful = true;
		}
		catch (UnsatisfiedLinkError e)
		{
		}
		catch (SecurityException e)
		{
		}
		nativeLibraryAvailable = loadSuccessful;

		Random random = new Random();
		randomClockSequence = random.nextInt(0x4000);

		// The ideal solution is to obtain a 47 bit cryptographic quality
		// random number, and use it as the low 47 bits of the node ID, with
		// the most significant bit of the first octet of the node ID set
		// to 1. This bit is the unicast/multicast bit, which will never be
		// set in IEEE 802 addresses obtained from network cards; hence, there
		// can never be a conflict between UUIDs generated by machines with
		// and without network cards.
		randomNodeId = new byte[6];
		random.nextBytes(randomNodeId);
		randomNodeId[0] |= 0x80;
		
		try
		{
			// since we could not get the MAC address get the IP-adress
			byte[] localHostAddress = InetAddress.getLocalHost().getAddress();
			for (int i=0; i<4; i++)
			{
				randomNodeId[i+2] = localHostAddress[i];
			}
		}
		catch (Exception ex)
		{
			// ignore
		}
	}

	/**
	 * Creates a new UUID object.
	 */
	public UUID()
	{
		uuid = generateUUID();
	}

	/**
	 * Creates a UUID object from a string in UUID format. This constructor has
	 * no effect on the creation of further UUIDs.
	 * 
	 * @throws NumberFormatException
	 *           if the string is not in UUID format.
	 */
	public UUID(String uuid) throws NumberFormatException
	{
		if (uuid.length() != STRING_LENGTH || uuid.charAt(8) != '-' || uuid.charAt(13) != '-' || uuid.charAt(18) != '-' || uuid.charAt(23) != '-')
		{
			throw new NumberFormatException("'" + uuid + "' is not in UUID format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
		}
		Long.parseLong(uuid.substring(0, 8), 16);
		Integer.parseInt(uuid.substring(9, 13), 16);
		Integer.parseInt(uuid.substring(14, 18), 16);
		Integer.parseInt(uuid.substring(19, 23), 16);
		Long.parseLong(uuid.substring(24, 36), 16);

		this.uuid = uuid;
	}

	/**
	 * Compares this UUID to the specified object. The result is true if and only
	 * if the argument is not null and is a UUID object that represents the same
	 * UUID as this object.
	 * 
	 * @return <code>true</code> if the UUIDs are equal; <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(Object anObject)
	{
		if (this == anObject)
		{
			return true;
		}
		if (anObject instanceof UUID)
		{
			UUID another = (UUID) anObject;
			return this.uuid.equals(another.uuid);
		}
		return false;
	}

	/**
	 * Returns a hash code value for the object.
	 * 
	 * @return a hash code value for this object.
	 */
	public int hashCode()
	{
		return uuid.hashCode();
	}

	/**
	 * Generates a time-based UUID (version 1 UUID).
	 */
	public static String generateUUID()
	{
		synchronized (UUID.class)
		{
			String nativeUUID = null;
			if (nativeLibraryAvailable)
			{
				try
				{
					nativeUUID = getNativeUUID();
				}
				catch (ConcurrentModificationException e)
				{
					// nativeUUID is still null so we will create a pure Java UUID
				}
			}

			if (nativeUUID != null)
			{
				return nativeUUID;
			}
			long currentTime = getCurrentTime();

			int timeLow = (int) currentTime;
			short timeMid = (short) (currentTime >> 32);
			short timeHiAndVersion = (short) (((currentTime >> 48) & 0xfff) | 0x1000);

			byte clockSeqHiAndReserved = (byte) (((randomClockSequence >> 8) & 0x3f) | 0x80);
			byte clockSeqLow = (byte) (randomClockSequence & 0xff);

			StringBuffer buf = new StringBuffer(STRING_LENGTH);
			appendHexValue(buf, timeLow, 8).append('-');
			appendHexValue(buf, timeMid, 4).append('-');
			appendHexValue(buf, timeHiAndVersion, 4).append('-');
			appendHexValue(buf, clockSeqHiAndReserved, 2);
			appendHexValue(buf, clockSeqLow, 2).append('-');
			for (int i = 0; i < 6; i++)
			{
				appendHexValue(buf, randomNodeId[i], 2);
			}
			return buf.toString();
		}
	}

	/**
	 * Indicates if a UUID is strong. Strong UUIDs are more likely to be unique
	 * than weak UUIDs. This is a relative qualification, strong UUIDs are still
	 * not guaranteed to be indeed universally unique, however, they have a
	 * higher probability than weak UUIDs.
	 * 
	 * @return <code>true</code> if the UUID is considered to be strong; <code>false</code>
	 *         otherwise.
	 */
	public boolean isStrong()
	{
		try
		{
			// check if it is time based, the IETF variant and a hardware node id
			return (Integer.parseInt(uuid.substring(14, 16), 16) & 0xf0) == 0x10 && (Integer.parseInt(uuid.substring(19, 21), 16) & 0xc0) == 0x80 && (Integer.parseInt(uuid.substring(24, 26), 16) & 0x80) == 0x00;
		}
		catch (NumberFormatException e)
		{
			// should never happen
			return false;
		}
	}

	/**
	 * Gets the current time in 100ns. Makes sure that no two invokations return
	 * the same value.
	 * 
	 * @return the current time
	 */
	protected static synchronized long getCurrentTime()
	{
		for (;;)
		{
			long currentTime = System.currentTimeMillis() * 10000 + timeDiff;
			if (previousTime >= currentTime)
			{
				if (previousTime - currentTime >= 9999)
				{ // stall on overflow
					try
					{
						Thread.sleep(1);
					}
					catch (InterruptedException e)
					{
						// ignore, we will check the time again in the next loop anyway
					}
					continue;
				}
				currentTime = previousTime + 1;
			}
			previousTime = currentTime;
			return currentTime;
		}
	}

	/**
	 * Appends a hex value to a <code>StringBuffer</code>.
	 * 
	 * @param buf
	 *          the string buffer.
	 * @param v
	 *          the value.
	 * @param len
	 *          the number of characters to be added.
	 * @return the string buffer that was passed in as buf.
	 */
	private final static StringBuffer appendHexValue(StringBuffer buf, int v, int len)
	{
		for (int i = (len - 1) * 4; i >= 0; i -= 4)
		{
			buf.append(hexCharacters[(v >> i) & 0xf]);
		}
		return buf;
	}

	/**
	 * Returns the UUID as a string in the format
	 * xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx.
	 * 
	 * @return the string representation of the UUID.
	 */
	public String toString()
	{
		return uuid;
	}

	/**
	 * Returns a UUID created utilizing all means available from JNI. This might
	 * include system wide locking to avoid parallel creation of identical ids,
	 * access to the MAC address of an installed ethernet card, and persisting of
	 * each generated UUID.
	 * 
	 * @return the UUID or <code>null</code> if it could not be created. throws
	 *         ConcurrentModificationException if the system wide lock could not
	 *         be obtained.
	 */
	private static native String getNativeUUID() throws ConcurrentModificationException;

	/**
	 * Test method.
	 */
	public static void main(String[] args)
	
	{
		System.out.println("randomClockSequence: "+randomClockSequence);
		
		UUID uuid1 = new UUID();
		UUID uuid2 = new UUID();
		System.out.println("Generated UUID 1  : " + uuid1.toString() + (uuid1.isStrong() ? " (strong)" : " (weak)"));
		System.out.println("Generated UUID 2  : " + uuid2.toString() + (uuid2.isStrong() ? " (strong)" : " (weak)"));
		UUID uuid2c = new UUID(uuid2.toString());
		System.out.println("Copied UUID 2     : " + uuid2c.toString() + (uuid2c.isStrong() ? " (strong)" : " (weak)"));

		// unique test
		java.util.Set set = new java.util.HashSet();
		long millis = System.currentTimeMillis();
		for (int i=0; i<50000; i++)
		{
			String uuid = generateUUID();
			if (!set.add(uuid))
			{
				System.err.println("Error not unique!!!!");
				return;
			}
		}
		System.out.println("Time: "+(System.currentTimeMillis()-millis)+"ms");

		// speed test
		millis = System.currentTimeMillis();
		for (int i=0; i<500000; i++)
		{
			generateUUID();
		}
		System.out.println("Time: "+(System.currentTimeMillis()-millis)+"ms");
	}
}
