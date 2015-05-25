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
var firstChance = new Array();
var secondChance = new Array();

firstChance["application/rtf"]=openWord;
firstChance["application/msword"]=openWord;
firstChance["application/vnd.ms-excel"]= openExcel;
firstChance["application/vnd.ms-powerpoint"]= openPowerpoint;
firstChance["application/vnd.sun.xml.writer"]= openOpenOffice;
firstChance["application/vnd.sun.xml.calc"]= openOpenOffice;
firstChance["application/vnd.sun.xml.draw"]= openOpenOffice;
firstChance["application/vnd.sun.xml.impress"]= openOpenOffice;
firstChance["application/vnd.oasis.opendocument.text"]= openOpenOffice;
firstChance["application/vnd.oasis.opendocument.spreadsheet"]= openOpenOffice;
firstChance["application/vnd.oasis.opendocument.presentation"]= openOpenOffice;
firstChance["application/vnd.oasis.opendocument.graphics"]=openOpenOffice;

secondChance["application/rtf"]=openOpenOffice;
secondChance["application/msword"]=openOpenOffice;
secondChance["application/vnd.ms-excel"]= openOpenOffice;
secondChance["application/vnd.ms-powerpoint"]= openOpenOffice;
secondChance["application/vnd.sun.xml.writer"]= openOpenOffice;
secondChance["application/vnd.sun.xml.calc"]= openOpenOffice;
secondChance["application/vnd.sun.xml.draw"]= openOpenOffice;
secondChance["application/vnd.sun.xml.impress"]= openOpenOffice;
secondChance["application/vnd.oasis.opendocument.text"]= openOpenOffice;
secondChance["application/vnd.oasis.opendocument.spreadsheet"]= openOpenOffice;
secondChance["application/vnd.oasis.opendocument.presentation"]= openOpenOffice;
secondChance["application/vnd.oasis.opendocument.graphics"]=openOpenOffice;


function editDocument(docUrl, mimeType)
{
  var callBack = firstChance[mimeType];
  if(callBack)
  {
	  if(!callBack(docUrl))
	  {
    	callBack = secondChance[mimeType];
    	if(callBack)
    		callBack(docUrl);
	  }
	}
}

function openWord(docUrl)
{
  return openMSOffice(createActiveXComponent("Word.Application"),docUrl);
}

function openExcel()
{
  return openMSOffice(createActiveXComponent("Excel.Application"),docUrl);
}

function openPowerpoint()
{
  return openMSOffice(createActiveXComponent("Powerpoint.Application"),docUrl);
}

function openMSOffice(control, docUrl)
{
	if(control)
	{
   control.WindowState = 0;
   control.Application.Visible = true;
   control.Application.Documents.Open(docUrl);
   control.Application.Visible = true;
   control=null;
   return true;
  }
  return false;
}

function openOpenOffice(docUrl)
{
  var serviceManager = createActiveXComponent("com.sun.star.ServiceManager");
  var objDesktop = serviceManager.CreateInstance("com.sun.star.frame.Desktop");
  var objDocument = objDesktop.LoadComponentFromUrl(docUrl, "_blank", 0, new Array());
  return true;
}


function createActiveXComponent(name)
{
  return Try.these(
  		function() {return GetObject("",name);},
      function() {return new ActiveXObject(name);});
}