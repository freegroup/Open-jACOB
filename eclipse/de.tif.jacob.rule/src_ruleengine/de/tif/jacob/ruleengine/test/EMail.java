package de.tif.jacob.ruleengine.test;

import de.tif.jacob.ruleengine.BusinessObject;

public class EMail extends BusinessObject 
{
	public void sendNotifcationMail(String personAddress)
	{
		System.out.println("EMail.sendNotifcationMail(String personAddress)");
		System.out.println("\t"+personAddress);
	}
}
