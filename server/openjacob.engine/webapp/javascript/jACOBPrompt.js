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
function jACOBPrompt(/*:String*/ msg,/*:String*/ defaultValue, /*:String*/ hashCode, /*:boolean*/ multiline)
{
    windowsToShow.push(this);
    this.msg = msg;
    this.hashCode = hashCode;
    this.defaultValue = defaultValue;
    this.multiline = multiline;
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
jACOBPrompt.prototype.show=function()
{
  fireEventIsInProcess=true;
  var oThis = this;
  var content = getObj("body");

  var jacob_window = document.createElement("div");
  jacob_window.style.position ="absolute";
  jacob_window.style.left     ="40%";
  jacob_window.style.top      ="20%";
  jacob_window.style.width    ="350px";
  jacob_window.className      ="jacob_window";
  jacob_window.style.opacity="0.001";
  jacob_window.style.filter = "alpha(opacity=1)";
  jacob_window.id ="message";

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
             close_img.onclick=function(){oThis.onCancel();};
             jacob_window_top_buttons.appendChild(close_img);

             var top_right = document.createElement("img");
             top_right.src="themes/window_top_right.gif";
             jacob_window_top_buttons.appendChild(top_right);

     var jacob_window_middle = document.createElement("div");
     jacob_window_middle.className ="jacob_window_middle";
     jacob_window.appendChild(jacob_window_middle);

        var jacob_window_content = document.createElement("div");
        jacob_window_content.className ="jacob_window_content";
        jacob_window_content.style.height="90px";
        jacob_window_middle.appendChild(jacob_window_content);

        var table = document.createElement("table");
        var tbody = document.createElement("tbody");
        table.style.height="100%";
        table.style.width="100%";
        table.appendChild(tbody);
        var tr1 = document.createElement("tr");
        var td1 = document.createElement("td");
        td1.style.textAlign="left";
        td1.align="left";
        td1.valign="top";
        td1.height="100%";

        // create caption
        var caption = document.createElement("div");
        caption.innerHTML=this.msg;
        caption.className="caption_normal_update";
        td1.appendChild(caption);

        // create input box
        if(this.multiline)
        {
           this.input = document.createElement("textarea");
           this.input.className="text_area editable_inputfield";
           this.input.value=this.defaultValue;
           this.input.style.width="100%";
           this.input.style.height="80px";
           jacob_window_content.style.height="170px";
        }
        else
        {
           this.input = document.createElement("input");
           this.input.type="text";
           this.input.className="text_normal editable_inputfield";
           this.input.value=this.defaultValue;
           this.input.style.width="100%";
        }
        td1.appendChild(this.input);

        tr1.appendChild(td1);
        tbody.appendChild(tr1);

        var tr2 = document.createElement("tr");
        var td2 = document.createElement("td");
        td2.style.textAlign="center";
        td2.valign="bottom";
           var ok_button = document.createElement("button");
           ok_button.onclick=function(){oThis.onOk();};
           ok_button.innerHTML=BUTTON_COMMON_APPLY;
           ok_button.className="button_normal";
           td2.appendChild(ok_button);

           var cancel_button = document.createElement("button");
           cancel_button.onclick=function(){oThis.onCancel();};
           cancel_button.innerHTML=BUTTON_COMMON_CANCEL;
           cancel_button.className="button_normal";
           td2.appendChild(cancel_button);
        tr2.appendChild(td2);

        tbody.appendChild(tr2);

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

  this.windowDiv = jacob_window;
  this.antiClick  = antiClick;
  this.input.focus();
  new fx.Opacity( 'message',{duration:500} ).custom(0.001,1.0);
}

jACOBPrompt.prototype.onOk=function()
{
  var content = getObj("body");

  content.removeChild(this.antiClick);
  content.removeChild(this.windowDiv);
  fireEventIsInProcess=false;
  FireEventData(this.hashCode,'ok',this.input.value);
}

/**
 *
 **/
jACOBPrompt.prototype.onCancel=function()
{
  var content = getObj("body");

  content.removeChild(this.antiClick);
  content.removeChild(this.windowDiv);
  fireEventIsInProcess=false;
  FireEvent(this.hashCode,'cancel');
}


