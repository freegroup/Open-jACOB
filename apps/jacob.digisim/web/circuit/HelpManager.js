/* This notice must be untouched at all times.

Open-jACOB Draw2D
The latest version is available at
http://www.openjacob.org

Copyright (c) 2006 Andreas Herz. All rights reserved.
Created 5. 11. 2006 by Andreas Herz (Web: http://www.freegroup.de )

LICENSE: LGPL

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License (LGPL) as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA,
or see http://www.gnu.org/copyleft/lesser.html
*/

function HelpManager()
{
   
}

HelpManager.helpView = null;
HelpManager.currentType = null;

HelpManager.show=function(/*:String*/ objType)
{
  if(HelpManager.helpView==null)
  {
     var toolbar = new Ext.Toolbar("help-toolbar");

     toolbar.addButton({ text: 'Edit', 
                       id:'help-print-button',
                       icon:"icons/fileEdit.png", 
                       tooltip: {text:'Edit the Dokumentation', title:'Edit', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
     {
         HelpManager.edit();
     }});

     toolbar.addButton({ text: 'Print', 
                       id:'help-print-button',
                       icon:"icons/printer.png", 
                       tooltip: {text:'Print the Dokumentation', title:'Print', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
     {
         HelpManager.print();
     }});
     toolbar.addFill();
     toolbar.addButton({ text: 'Info', 
                       id:'help-print-button',
                       icon:"icons/help.png", 
                       tooltip: {text:'Information About the Documentation Tool', title:'Tool Information', autoHide:true},
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
     {
            var dialog = new Ext.LayoutDialog("help-information", { 
                        modal:true,
                        width:600,
                        height:400,
                        shadow:true,
                        minWidth:300,
                        minHeight:300,
                        proxyDrag: true,
                        center: {
	                        autoScroll:true,
	                        alwaysShowTabs: false
	                    }
                     });
               var layout = dialog.getLayout();
               layout.beginUpdate();
               layout.add('center', new Ext.ContentPanel('help-information-content', {title: 'asdf'}));
               layout.endUpdate();
               dialog.addKeyListener(27, dialog.hide, dialog);
               dialog.addButton('Close', dialog.hide, dialog);
               dialog.show();
     }});

     var layout = Application.getLayout();
     layout.beginUpdate();
     var panel = new Ext.ContentPanel('help-panel',
         {
           toolbar: toolbar,
           resizeEl: 'help-content',
           autoScroll:true, 
           fitToFrame:true 
         });
     HelpManager.helpView = layout.add("south",  panel);
     layout.endUpdate();
  }
  HelpManager.currentType = objType;
  HelpManager.helpView.setTitle("Help: "+objType);
  Ext.Ajax.request({
	url : 'part_documentation.jsp' ,
	params : { name : objType, browser : browserId },
	method: 'POST',
	success: function ( result, request ) { 
                Ext.get('help-content').dom.innerHTML =result.responseText;
	},
	failure: function ( result, request) { 
		Ext.MessageBox.alert('Error', result.responseText); 
	}
   });
}


HelpManager.showHint=function()
{
	var dialog = new Ext.LayoutDialog("start-information", { 
		modal:true,
		width:500,
		height:300,
		shadow:true,
		minWidth:300,
		minHeight:300,
		proxyDrag: true,
		center: {
			autoScroll:true,
			alwaysShowTabs: false
			}
		});
	var layout = dialog.getLayout();
	layout.beginUpdate();
	layout.add('center', new Ext.ContentPanel('start-information-content', {title: 'asdf'}));
	layout.endUpdate();
	dialog.addKeyListener(27, dialog.hide, dialog);
	dialog.addButton('Close', dialog.hide, dialog);
	dialog.show();
}

HelpManager.edit=function()
{
   window.open("part_documentation_edit.jsp?browser="+browserId+"&name="+HelpManager.currentType);
}



HelpManager.print=function()
{
   window.open("part_documentation_print.jsp?browser="+browserId+"&name="+HelpManager.currentType);
}
