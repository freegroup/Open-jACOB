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
function jACOBTransparentMessage(/*:String*/ msg, /*:boolean*/ fadeOut)
{
    windowsToShow.push(this);
    this.msg = msg;
    if(fadeOut ==undefined)
     this.fadeOut =true;
    else
     this.fadeOut =fadeOut;
}

/**
 *
 *
 **/
jACOBTransparentMessage.prototype.show=function()
{
      windowsToShow = windowsToShow.without(this);
      var oThis = this;

      var content = getObj("body");

      this.center = document.createElement("center");
      this.center.id ="transparentMessage";
//      center.className ="rounded";
      this.center.style.padding="20px";
      this.center.style.position ="absolute";
      this.center.style.top ="0px";
      this.center.style.width ="100%";
      this.center.style.opacity="0.001";
      this.center.style.filter = "alpha(opacity=1)";
      this.center.style.zIndex="1000";
      
      var jacob_window = document.createElement("div");
      this.center.appendChild(jacob_window);
      jacob_window.innerHTML=this.msg;
      jacob_window.className ="rounded";
      jacob_window.style.backgroundColor="#8CC73F";
      jacob_window.style.color="white";
      jacob_window.style.width="300px";
      jacob_window.style.fontWeight="bold";
      jacob_window.style.fontSize="15pt";
      jacob_window.style.position="relative";
      content.appendChild(this.center);
      new fx.Opacity( 'transparentMessage',{duration:300} ).custom(0.001,0.8);
      // automatische ausblenden der Nachricht
      //
      if(this.fadeOut==true)
      {
	new fx.Interval( function() { 
		new fx.Top( 'transparentMessage',{duration:1500} ).custom(0,-100) ; 
		new fx.Opacity( 'transparentMessage',{duration:1500} ).custom(0.8,0) }
	,1);
      }
      // ...oder einen Close-Button
      //
      else
      {
        var close_img = document.createElement("img");
        close_img.src="themes/"+userTheme+"/images/close.png";
        close_img.onclick=function()
          {
      	     new fx.Opacity( oThis.center, {onComplete:function(){oThis.center.parentNode.removeChild(oThis.center)}} ).custom(0.8,0);
          };
        close_img.style.right="-5px";
        close_img.style.top="-5px";
        close_img.style.cursor="pointer";
        close_img.style.position="absolute";
        jacob_window.appendChild(close_img);
      }

      this.addRounded(jacob_window, "#FFFFFF", "#8CC73F", 10,10, true);
      this.addRounded(jacob_window, "#FFFFFF", "#8CC73F", 10,10, false);
}

jACOBTransparentMessage.prototype.addRounded=function(el, bk, color, sizex, sizey, top) 
{
  if (!sizex && !sizey)
	return;
  var i, j;
  var d = document.createElement("div");
  d.style.backgroundColor = bk;
  var lastarc = 0;
  for (i = 1; i <= sizey; i++) 
  {
    var coverage, arc2, arc3;
    // Find intersection of arc with bottom of pixel row
    arc = Math.sqrt(1.0 - Math.sqr(1.0 - i / sizey)) * sizex;
    // Calculate how many pixels are bg, fg and blended.
    var n_bg = sizex - Math.ceil(arc);
    var n_fg = Math.floor(lastarc);
    var n_aa = sizex - n_bg - n_fg;
    // Create pixel row wrapper
    var x = document.createElement("div");
    var y = d;
    x.style.margin = "0px " + n_bg + "px";
    x.style.height='1px';
    x.style.overflow='hidden';
    // Make a wrapper per anti-aliased pixel (at least one)
    for (j = 1; j <= n_aa; j++) 
    {
      // Calculate coverage per pixel
      // (approximates circle by a line within the pixel)
      if (j == 1) {
        if (j == n_aa) {
          // Single pixel
          coverage = ((arc + lastarc) * .5) - n_fg;
        }
        else {
          // First in a run
          arc2 = Math.sqrt(1.0 - Math.sqr((sizex - n_bg - j + 1) / sizex)) * sizey;
          coverage = (arc2 - (sizey - i)) * (arc - n_fg - n_aa + 1) * 0.5;
          // Coverage is incorrect. Why?
          coverage = 0;
        }
      }
      else if (j == n_aa) {
        // Last in a run
        arc2 = Math.sqrt(1.0 - Math.sqr((sizex - n_bg - j + 1) / sizex)) * sizey;
        coverage = 1.0 - (1.0 - (arc2 - (sizey - i))) * (1.0 - (lastarc - n_fg)) * 0.5;
      }
      else {
        // Middle of a run
        arc3 = Math.sqrt(1.0 - Math.sqr((sizex - n_bg - j) / sizex)) * sizey;
        arc2 = Math.sqrt(1.0 - Math.sqr((sizex - n_bg - j + 1) / sizex)) * sizey;
        coverage = ((arc2 + arc3) * .5) - (sizey - i);
      }
      
      x.style.backgroundColor = Blend(bk, color, coverage);
	  if (top)
	      y.appendChild(x);
      else
	      y.insertBefore(x, y.firstChild);
      y = x;
      var x = document.createElement("div");
      x.style.height='1px';
      x.style.overflow='hidden';
      x.style.margin = "0px 1px";
    }
    x.style.backgroundColor = color;
    if (top)
         y.appendChild(x);
    else
         y.insertBefore(x, y.firstChild);
    lastarc = arc;
  }
  if (top)
       el.insertBefore(d, el.firstChild);
  else
        el.appendChild(d);
}

Math.sqr = function (x) {
  return x*x;
};

function Blend(a, b, alpha) {

  var ca = new Array(
    parseInt('0x' + a.substring(1, 3)),
    parseInt('0x' + a.substring(3, 5)),
    parseInt('0x' + a.substring(5, 7))
  );
  var cb = new Array(
    parseInt('0x' + b.substring(1, 3)),
    parseInt('0x' + b.substring(3, 5)),
    parseInt('0x' + b.substring(5, 7))
  );
  return '#' + ('0'+Math.round(ca[0] + (cb[0] - ca[0])*alpha).toString(16)).slice(-2).toString(16)
             + ('0'+Math.round(ca[1] + (cb[1] - ca[1])*alpha).toString(16)).slice(-2).toString(16)
             + ('0'+Math.round(ca[2] + (cb[2] - ca[2])*alpha).toString(16)).slice(-2).toString(16);

  return '#' + ('0'+Math.round(ca[0] + (cb[0] - ca[0])*alpha).toString(16)).slice(-2).toString(16)
             + ('0'+Math.round(ca[1] + (cb[1] - ca[1])*alpha).toString(16)).slice(-2).toString(16)
             + ('0'+Math.round(ca[2] + (cb[2] - ca[2])*alpha).toString(16)).slice(-2).toString(16);
}


