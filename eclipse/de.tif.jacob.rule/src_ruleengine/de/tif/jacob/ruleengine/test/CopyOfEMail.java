package de.tif.jacob.ruleengine.test;

import de.tif.jacob.ruleengine.BusinessObject;

public class CopyOfEMail extends BusinessObject 
{
	/**
	  * Dies ist der Kommentar der Funktion und mehr
	  * Dies ist die zweite Zeile.
	  * 
	  @param personAddress
	  @param a
	 */
	public void sendNotifcationMail(String personAddress, String a)
	{
		System.out.println("CopyOfEMail.sendNotifcationMail(String personAddress, String a)");
		System.out.println("\t"+personAddress);
		System.out.println("\t"+a);
	}
}
