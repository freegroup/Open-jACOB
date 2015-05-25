package de.shorti.baseknowledge.objects;

/**
 * Title:        short-i
 * Description:  Information retrival System with human language interface
 * Copyright:    Copyright (c) 2001
 * Company:      short-i
 * @author Andreas Herz
 * @version 1.0
 */

public class BillingInformation extends _dbBillingInformation
{
	static public BillingInformation createInstance(PartnerCompany _partnerCompany, String  _creatorClass, String  _reason, int  _cost, _dbRequest _request )
    {
        return _dbBillingInformation.createInstance(_partnerCompany, null,_creatorClass, _reason ,_cost, _request);
    }
}
