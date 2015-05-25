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
function openFFComboBox(/*:String*/ jacobId)
{
   var url = "dialogs/ffComboBoxPicker.jsp";
   var myAjax = new Ajax.Request(url,
   {
      parameters: 
      {
        guid: jacobId,
        browser: browserId
      },
      onSuccess: function(transport)
      {
        var obj = getObj("j_"+jacobId);
        var menu = document.createElement('div'); // caption container
        menu.style.position="absolute";
        menu.style.left=obj.style.left;
        menu.style.width=obj.style.width;
        menu.style.top=(parseInt(obj.style.top)+parseInt(obj.style.height))+"px";
        menu.style.opacity="0.001";
        menu.style.filter= "alpha(opacity=1)";
        menu.id = "columnPicker";
        menu.innerHTML=transport.responseText;
        obj.parentNode.appendChild(menu);

        // Versuchen die ausgeklappte Combobox auf dem Bildschirm besser anzuordnen
        //
        // 1.) Hat die Box überhaupt Platz wenn diese nach unten ausklappt. Sprich - sind
        //     die Einträge überhaupt sichtbar. Scrollbalken anzeigen wenn zu viele Eintrage vorhanden sind.
        //
        var columnPicker = new LayerObject("columnPicker");
        if(columnPicker.height>200)
        {
           columnPicker.css.height="200px";
           columnPicker.css.overflow="auto";
        }

        // Falls das Div nicht vollständig sichtbar ist, wird dies dann nach oben aufgeklappt.
        //
        var testDiv = new LayerObject(menu);
        if(!testDiv.isInVisibleArea())
        {
          testDiv.moveTo(obj.style.left,(parseInt(obj.style.top))-testDiv.getHeight());
        }
        new fx.Opacity( menu,{duration:400} ).custom(0.001,1.0);
      }
   });
}
