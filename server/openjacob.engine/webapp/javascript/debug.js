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
window.onerror = myOnError;

function myOnError(msg, url, lno) {
	var br=new Array(4);
	var os=new Array(2);
	var flash=new Array(2);
	br=getBrowser();
	os=getOS();
	flash=hasFlashPlugin();
	var errorString = '';
	errorString = errorString + '\n===============================================================\n';
	errorString = errorString + '==== Javascript Error on client side ==========================\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + '==== Client information =======================================\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + "Browser identifier: "+br[0]+"\n";
	errorString = errorString + "Browser version: "+br[1]+"\n";
	errorString = errorString + "Browser major version: "+getMajorVersion(br[1])+"\n";
	errorString = errorString + "Browser minor version: "+getMinorVersion(br[1])+"\n";
	errorString = errorString + "Browser engine: "+br[2]+"\n";
	errorString = errorString + "Browser engine version: "+br[3]+"\n";
	errorString = errorString + "Full user agent string: "+getFullUAString()+"\n";
	errorString = errorString + "Operating system identifier: "+os[0]+"\n";
	errorString = errorString + "Operating system version: "+os[1]+"\n";
	errorString = errorString + "Is Flash installed? "+ (flash[0]==2 ? "Yes" : (flash[0] == 1 ? "No" : "unknown"))+"\n";
	errorString = errorString + "Flash version: "+flash[1]+"\n";
	errorString = errorString + '===============================================================\n';
	errorString = errorString + '==== Error message ============================================\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + 'File: ' + url + '\n';
	errorString = errorString + 'Line: ' + lno + '\n';
	errorString = errorString + 'Message: ' + msg + '\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + '==== RCS Informations =========================================\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + 'RCS_ID Common:       ' + RCS_ID_COMMON + '\n';
	errorString = errorString + '===============================================================\n';
	errorString = errorString + _errorStack_.join(" ");
	
	serverTrace(errorString);
}

/**
 * Send a message to the server and log them with
 *  category:  de.tif.jacob.javascript
 *  severity:  warning
 **/
function serverTrace(msg) 
{
	if (window.XMLHttpRequest) req = new XMLHttpRequest();
	else if (window.ActiveXObject) req = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req.open("POST", "trace.jsp");
	req.setRequestHeader('content-type', 'text/plain');
	req.send(msg);
}

/**************************************************************************************************/
/*                                                                                                */
/* Functions to handle debug message in javascript and service side calls for the                 */
/* jACOB application server                                                                       */
/*                                                                                                */
/* usage: trace('this is a message');                                                             */
/*                                                                                                */
/**************************************************************************************************/
var debugWindow =null;
var sqlWindow=null;


// Try to find the debug window in the navigation frame
//
if(parent!=null && parent.frames['menu'] && parent.frames['menu']!=self)
{
    debugWindow = parent.frames['menu'].debugWindow;
}

// Try to find the SQL window in the navigation frame
//
if(parent!=null && parent.frames['menu'] && parent.frames['menu']!=self)
{
    sqlWindow = parent.frames['menu'].sqlWindow;
}


/**
 * Show a trace message in a seperate Explorer Window
 *
**/
function trace( message) 
{
    if(!debugWindow || !debugWindow.open || debugWindow.closed)
    {
        debugWindow = window.open("about:blank","debug");
        // to avoid permission denied exception if another window has open the window before
        //
        debugWindow.close();
        debugWindow = window.open("about:blank","debug");
        if(parent!=null && parent.frames['menu'] && debugWindow!=null)
        {
            parent.frames['menu'].debugWindow = debugWindow;
        }
    }
    debugWindow.document.writeln("<pre>"+message+"</pre>")
}

function dumpObject(obj)
{
    trace("----------------------------------------------------------------------------");
    trace("- Object dump");
    trace("----------------------------------------------------------------------------");
    for (var i in obj)
    {
        try
        {
            if(typeof obj[i] != "function")
                trace(i + " --&gt; " + obj[i]);
         }
         catch(e)
         {
         }
    }
    for (var i in obj)
    {
        try
        {
            if(typeof obj[i] == "function")
                trace(i + " --&gt; " + obj[i]);
        }
        catch(e)
        {
        }
    }
    trace("----------------------------------------------------------------------------");
}


function showSQL(timestamp, message) 
{
    if(!sqlWindow || !sqlWindow.open || sqlWindow.closed)
    {
        sqlWindow = window.open("about:blank","sql");
        // to avoid permission denied exception if another window has open the window before
        //
        sqlWindow.close();
        sqlWindow = window.open("about:blank","sql");
        if(parent!=null && parent.frames['menu'] && sqlWindow!=null)
        {
            parent.frames['menu'].sqlWindow = sqlWindow;
        }
    }
    sqlWindow.document.writeln("<table><tr><td valign=top style='white-space:nowrap;' >"+timestamp+"</td><td>"+message+"</td></tr></table>")
}


var _errorStack_=[]
function pushErrorStack(/*:Exception*/ e, /*:String*/ functionName)
{
  _errorStack_.push(functionName+"\n");
  /*re*/throw e;
};


