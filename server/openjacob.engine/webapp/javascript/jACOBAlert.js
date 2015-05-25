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
function jACOBAlert(/*:String*/ msg, /*:String*/ extendedMessage, /*:String*/ hashCode)
{
    windowsToShow.push(this);
    this.msg = msg;
    this.extendedMessage=extendedMessage;
    
    if(hashCode ===undefined)
      hashCode = null;
      
    this.hashCode = hashCode;
}

/**
 *

<div class="jacob_window" id="2" style="position:absolute;top:40%;left:30%;width:400px;">
	<div class="jacob_window_top">
		<img src="images/top_left.gif" align="left">
		<img src="images/top_center.gif" class="jacob_window_topCenterImage">
		<div class="jacob_window_top_buttons">
			<img src="images/close.gif">
			<img src="images/top_right.gif">
		</div>
	</div>
	<div class="jacob_window_middle">
		<div class="jacob_window_content" style="height:200px">
		This select box is here just to show you that this window script manage to cover selectboxes in IE. 
		</div>
	</div>
	<div class="jacob_window_bottom"></div>
</div>

 *
 **/
jACOBAlert.prototype.show=function()
{
  fireEventIsInProcess=true;
  var oThis = this;
  var content = getObj("body");

  var jacob_window = document.createElement("div");
  jacob_window.style.position ="absolute";
  jacob_window.style.left     ="40%";
  jacob_window.style.top      ="20%";
  jacob_window.style.width    ="450px";
  jacob_window.style.backgroundColor ="white";
  jacob_window.className      ="jacob_window";
  jacob_window.id             ="message";
  jacob_window.style.opacity="0.001";
  jacob_window.style.filter = "alpha(opacity=1)";

     var jacob_window_top = document.createElement("div");
     jacob_window_top.className ="jacob_window_top";
     jacob_window.appendChild(jacob_window_top);

          var img_top_left = document.createElement("img");
          img_top_left.src="themes/window_top_left.gif";
          img_top_left.align="left";
          jacob_window_top.appendChild(img_top_left);

          var img_top_center = document.createElement("img");
          img_top_center.src="themes/window_top_center.gif";
          img_top_center.className="jacob_window_topCenterImage";
          jacob_window_top.appendChild(img_top_center);

          var jacob_window_top_buttons = document.createElement("div");
          jacob_window_top_buttons.className ="jacob_window_top_buttons";
          jacob_window_top.appendChild(jacob_window_top_buttons);

             var close_img = document.createElement("img");
             close_img.src="themes/window_close.png";
             close_img.onclick=function(){oThis.onClose();};
             jacob_window_top_buttons.appendChild(close_img);

             var top_right = document.createElement("img");
             top_right.src="themes/window_top_right.gif";
             jacob_window_top_buttons.appendChild(top_right);

     var jacob_window_middle = document.createElement("div");
     jacob_window_middle.className ="jacob_window_middle";
     jacob_window.appendChild(jacob_window_middle);

        var jacob_window_content = document.createElement("div");
        jacob_window_content.className ="jacob_window_content";
//        jacob_window_content.style.height="90px";
        jacob_window_content.style.height="auto";
        jacob_window_content.style.margin="0px";
        jacob_window_content.style.padding="0px";
        jacob_window_middle.appendChild(jacob_window_content);

        var table = document.createElement("table");
        var tbody = document.createElement("tbody");
        table.cellspacing="0";
        table.cellpadding="0";
        table.style.height="100%";
        table.style.width="98%";
        table.appendChild(tbody);
        // the normal (short) message
        //
        var tr1 = document.createElement("tr");
        var td1 = document.createElement("td");
        td1.style.textAlign="center";
        td1.valign="top";
        td1.height="100%";
        td1.style.paddingTop="30px";
        td1.style.paddingBottom="30px";
        td1.className="caption_normal_update";
        td1.innerHTML =this.msg;
        tr1.appendChild(td1);
        tbody.appendChild(tr1);

        // expandable message
        //
        if(this.extendedMessage)
        {
           var tr2 = document.createElement("tr");
           var td2 = document.createElement("td");
           td2.style.textAlign="center";
           td2.valign="top";
           td2.style.paddingBottom="30px";
           var expandedArea = document.createElement("textarea");
           expandedArea.id = "expandArea";
           expandedArea.value = this.extendedMessage.decodeUTF8();
           expandedArea.style.width="100%";
           expandedArea.style.height="1px";
           expandedArea.style.overflow="hidden";
           expandedArea.readonly="";
           expandedArea.className = "longtext_disabled";
           td2.appendChild(expandedArea);
           tr2.appendChild(td2);
           tbody.appendChild(tr2);
        }

        var tr3 = document.createElement("tr");
        var td3 = document.createElement("td");
        td3.style.textAlign="center";
        td3.valign="bottom";
           var close_button = document.createElement("button");
           close_button.onclick=function(){oThis.onClose();};
           close_button.innerHTML=BUTTON_COMMON_CLOSE;
           close_button.className="button_normal";
           td3.appendChild(close_button);

           if(this.extendedMessage)
           {
             var detail_button = document.createElement("button");
             detail_button.onclick = function()
             {
              if(parseInt( $('expandArea').style.height)<=1)
              {
                 var effect = new fx.Height( 'expandArea',{duration:300, onComplete:function()
                 {
                    $("detailsButton").innerHTML=BUTTON_COMMON_HIDE_DETAILS;
                 }});
                 $("expandArea").style.overflow="scroll";
                 effect.custom(1,200);
              }
              else
              {
                var effect = new fx.Height( 'expandArea',{duration:300, onComplete:function()
                {
                   $("detailsButton").innerHTML=BUTTON_COMMON_SHOW_DETAILS;
                }} );
                effect.custom(200,1);
              }
              };
              detail_button.innerHTML=BUTTON_COMMON_SHOW_DETAILS;
              detail_button.className="button_normal";
              detail_button.id = "detailsButton";
              td3.appendChild(detail_button);
           }
        tr3.appendChild(td3);

        tbody.appendChild(tr3);

        jacob_window_content.appendChild(table);


     var jacob_window_bottom = document.createElement("div");
     jacob_window_bottom.className ="jacob_window_bottom";
     jacob_window.appendChild(jacob_window_bottom);

  var antiClick = document.createElement("div");
  antiClick.style.position ="absolute";
  antiClick.style.left     ="0";
  antiClick.style.top      ="0";
  antiClick.style.width    ="100%";
  antiClick.style.height   ="100%";
  content.appendChild(antiClick);

  content.appendChild(jacob_window);

  new fx.Opacity( 'message',{duration:500} ).custom(0.001,1.0);

  this.windowDiv = jacob_window;
  this.antiClick  = antiClick;

  // Element merken welches zuvor den Focus hatte. Dieses Element bekommt nach dem "onClose" wieder den
  // den Focus.
  close_button.focus();
}

/**
 *
 **/
jACOBAlert.prototype.onClose=function()
{
  var content = getObj("body");

  content.removeChild(this.antiClick);
  content.removeChild(this.windowDiv);
  fireEventIsInProcess=false;
  if(this.hashCode!=null)
  {
    FireEvent(this.hashCode,'close');
  }
  else if(lastFocusElement!=null)
  {
      try
      {
        lastFocusElement.focus();
      }
      catch(exc)
      {
        // ignore. Kann passieren wenn das Feld disabled oder auf unsichtbar geschaltet worden ist.
      }
  }
}
