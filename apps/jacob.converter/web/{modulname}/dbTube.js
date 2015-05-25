/* This notice must be untouched at all times.

wz_jsgraphics.js    v. 3.05
The latest version is available at
http://www.walterzorn.com
or http://www.devira.com
or http://www.walterzorn.de

Copyright (c) 2002-2009 Walter Zorn. All rights reserved.
Created 3. 11. 2002 by Walter Zorn (Web: http://www.walterzorn.com )
Last modified: 2. 2. 2009

Performance optimizations for Internet Explorer
by Thomas Frank and John Holdsworth.
fillPolygon method implemented by Matthieu Haller.

High Performance JavaScript Graphics Library.
Provides methods
- to draw lines, rectangles, ellipses, polygons
	with specifiable line thickness,
- to fill rectangles, polygons, ellipses and arcs
- to draw text.
NOTE: Operations, functions and branching have rather been optimized
to efficiency and speed than to shortness of source code.

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


var jg_ok, jg_ie, jg_fast, jg_dom, jg_moz;


function _chkDHTM(wnd, x, i)
// Under XUL, owner of 'document' must be specified explicitly
{
	x = wnd.document.body || null;
	jg_ie = x && typeof x.insertAdjacentHTML != "undefined" && wnd.document.createElement;
	jg_dom = (x && !jg_ie &&
		typeof x.appendChild != "undefined" &&
		typeof wnd.document.createRange != "undefined" &&
		typeof (i = wnd.document.createRange()).setStartBefore != "undefined" &&
		typeof i.createContextualFragment != "undefined");
	jg_fast = jg_ie && wnd.document.all && !wnd.opera;
	jg_moz = jg_dom && typeof x.style.MozOpacity != "undefined";
	jg_ok = !!(jg_ie || jg_dom);
}

function _pntCnvDom()
{
	var x = this.wnd.document.createRange();
	x.setStartBefore(this.cnv);
	x = x.createContextualFragment(jg_fast? this._htmRpc() : this.htm);
	if(this.cnv) this.cnv.appendChild(x);
	this.htm = "";
}

function _pntCnvIe()
{
	if(this.cnv) this.cnv.insertAdjacentHTML("BeforeEnd", jg_fast? this._htmRpc() : this.htm);
	this.htm = "";
}

function _pntDoc()
{
	this.wnd.document.write(jg_fast? this._htmRpc() : this.htm);
	this.htm = '';
}

function _pntN()
{
	;
}

function _mkDiv(x, y, w, h)
{
	this.htm += '<div style="position:absolute;'+
		'left:' + x + 'px;'+
		'top:' + y + 'px;'+
		'width:' + w + 'px;'+
		'height:' + h + 'px;'+
		'clip:rect(0,'+w+'px,'+h+'px,0);'+
		'background-color:' + this.color +
		(!jg_moz? ';overflow:hidden' : '')+
		';"><\/div>';
}

function _mkDivIe(x, y, w, h)
{
	this.htm += '%%'+this.color+';'+x+';'+y+';'+w+';'+h+';';
}

function _mkDivPrt(x, y, w, h)
{
	this.htm += '<div style="position:absolute;'+
		'border-left:' + w + 'px solid ' + this.color + ';'+
		'left:' + x + 'px;'+
		'top:' + y + 'px;'+
		'width:0px;'+
		'height:' + h + 'px;'+
		'clip:rect(0,'+w+'px,'+h+'px,0);'+
		'background-color:' + this.color +
		(!jg_moz? ';overflow:hidden' : '')+
		';"><\/div>';
}

var _regex =  /%%([^;]+);([^;]+);([^;]+);([^;]+);([^;]+);/g;
function _htmRpc()
{
	return this.htm.replace(
		_regex,
		'<div style="overflow:hidden;position:absolute;background-color:'+
		'$1;left:$2px;top:$3px;width:$4px;height:$5px"></div>\n');
}

function _htmPrtRpc()
{
	return this.htm.replace(
		_regex,
		'<div style="overflow:hidden;position:absolute;background-color:'+
		'$1;left:$2px;top:$3px;width:$4px;height:$5px;border-left:$4px solid $1"></div>\n');
}

function _mkLin(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1;

	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx,
		ox = x;
		while(dx > 0)
		{--dx;
			++x;
			if(p > 0)
			{
				this._mkDiv(ox, y, x-ox, 1);
				y += yIncr;
				p += pru;
				ox = x;
			}
			else p += pr;
		}
		this._mkDiv(ox, y, x2-ox+1, 1);
	}

	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy,
		oy = y;
		if(y2 <= y1)
		{
			while(dy > 0)
			{--dy;
				if(p > 0)
				{
					this._mkDiv(x++, y, 1, oy-y+1);
					y += yIncr;
					p += pru;
					oy = y;
				}
				else
				{
					y += yIncr;
					p += pr;
				}
			}
			this._mkDiv(x2, y2, 1, oy-y2+1);
		}
		else
		{
			while(dy > 0)
			{--dy;
				y += yIncr;
				if(p > 0)
				{
					this._mkDiv(x++, oy, 1, y-oy);
					p += pru;
					oy = y;
				}
				else p += pr;
			}
			this._mkDiv(x2, oy, 1, y2-oy+1);
		}
	}
}

function _mkLin2D(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1;

	var s = this.stroke;
	if(dx >= dy)
	{
		if(dx > 0 && s-3 > 0)
		{
			var _s = (s*dx*Math.sqrt(1+dy*dy/(dx*dx))-dx-(s>>1)*dy) / dx;
			_s = (!(s-4)? Math.ceil(_s) : Math.round(_s)) + 1;
		}
		else var _s = s;
		var ad = Math.ceil(s/2);

		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx,
		ox = x;
		while(dx > 0)
		{--dx;
			++x;
			if(p > 0)
			{
				this._mkDiv(ox, y, x-ox+ad, _s);
				y += yIncr;
				p += pru;
				ox = x;
			}
			else p += pr;
		}
		this._mkDiv(ox, y, x2-ox+ad+1, _s);
	}

	else
	{
		if(s-3 > 0)
		{
			var _s = (s*dy*Math.sqrt(1+dx*dx/(dy*dy))-(s>>1)*dx-dy) / dy;
			_s = (!(s-4)? Math.ceil(_s) : Math.round(_s)) + 1;
		}
		else var _s = s;
		var ad = Math.round(s/2);

		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy,
		oy = y;
		if(y2 <= y1)
		{
			++ad;
			while(dy > 0)
			{--dy;
				if(p > 0)
				{
					this._mkDiv(x++, y, _s, oy-y+ad);
					y += yIncr;
					p += pru;
					oy = y;
				}
				else
				{
					y += yIncr;
					p += pr;
				}
			}
			this._mkDiv(x2, y2, _s, oy-y2+ad);
		}
		else
		{
			while(dy > 0)
			{--dy;
				y += yIncr;
				if(p > 0)
				{
					this._mkDiv(x++, oy, _s, y-oy+ad);
					p += pru;
					oy = y;
				}
				else p += pr;
			}
			this._mkDiv(x2, oy, _s, y2-oy+ad+1);
		}
	}
}

function _mkLinDott(x1, y1, x2, y2)
{
	if(x1 > x2)
	{
		var _x2 = x2;
		var _y2 = y2;
		x2 = x1;
		y2 = y1;
		x1 = _x2;
		y1 = _y2;
	}
	var dx = x2-x1, dy = Math.abs(y2-y1),
	x = x1, y = y1,
	yIncr = (y1 > y2)? -1 : 1,
	drw = true;
	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1),
		p = pr-dx;
		while(dx > 0)
		{--dx;
			if(drw) this._mkDiv(x, y, 1, 1);
			drw = !drw;
			if(p > 0)
			{
				y += yIncr;
				p += pru;
			}
			else p += pr;
			++x;
		}
	}
	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1),
		p = pr-dy;
		while(dy > 0)
		{--dy;
			if(drw) this._mkDiv(x, y, 1, 1);
			drw = !drw;
			y += yIncr;
			if(p > 0)
			{
				++x;
				p += pru;
			}
			else p += pr;
		}
	}
	if(drw) this._mkDiv(x, y, 1, 1);
}

function _mkOv(left, top, width, height)
{
	var a = (++width)>>1, b = (++height)>>1,
	wod = width&1, hod = height&1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	ox = 0, oy = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1),
	w, h;
	while(y > 0)
	{
		if(st < 0)
		{
			st += bb2*((x<<1)+3);
			tt += bb4*(++x);
		}
		else if(tt < 0)
		{
			st += bb2*((x<<1)+3) - aa4*(y-1);
			tt += bb4*(++x) - aa2*(((y--)<<1)-3);
			w = x-ox;
			h = oy-y;
			if((w&2) && (h&2))
			{
				this._mkOvQds(cx, cy, x-2, y+2, 1, 1, wod, hod);
				this._mkOvQds(cx, cy, x-1, y+1, 1, 1, wod, hod);
			}
			else this._mkOvQds(cx, cy, x-1, oy, w, h, wod, hod);
			ox = x;
			oy = y;
		}
		else
		{
			tt -= aa2*((y<<1)-3);
			st -= aa4*(--y);
		}
	}
	w = a-ox+1;
	h = (oy<<1)+hod;
	y = cy-oy;
	this._mkDiv(cx-a, y, w, h);
	this._mkDiv(cx+ox+wod-1, y, w, h);
}

function _mkOv2D(left, top, width, height)
{
	var s = this.stroke;
	width += s+1;
	height += s+1;
	var a = width>>1, b = height>>1,
	wod = width&1, hod = height&1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1);

	if(s-4 < 0 && (!(s-2) || width-51 > 0 && height-51 > 0))
	{
		var ox = 0, oy = b,
		w, h,
		pxw;
		while(y > 0)
		{
			if(st < 0)
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				w = x-ox;
				h = oy-y;

				if(w-1)
				{
					pxw = w+1+(s&1);
					h = s;
				}
				else if(h-1)
				{
					pxw = s;
					h += 1+(s&1);
				}
				else pxw = h = s;
				this._mkOvQds(cx, cy, x-1, oy, pxw, h, wod, hod);
				ox = x;
				oy = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}
		}
		this._mkDiv(cx-a, cy-oy, s, (oy<<1)+hod);
		this._mkDiv(cx+a+wod-s, cy-oy, s, (oy<<1)+hod);
	}

	else
	{
		var _a = (width-(s<<1))>>1,
		_b = (height-(s<<1))>>1,
		_x = 0, _y = _b,
		_aa2 = (_a*_a)<<1, _aa4 = _aa2<<1, _bb2 = (_b*_b)<<1, _bb4 = _bb2<<1,
		_st = (_aa2>>1)*(1-(_b<<1)) + _bb2,
		_tt = (_bb2>>1) - _aa2*((_b<<1)-1),

		pxl = new Array(),
		pxt = new Array(),
		_pxb = new Array();
		pxl[0] = 0;
		pxt[0] = b;
		_pxb[0] = _b-1;
		while(y > 0)
		{
			if(st < 0)
			{
				pxl[pxl.length] = x;
				pxt[pxt.length] = y;
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				pxl[pxl.length] = x;
				st += bb2*((x<<1)+3) - aa4*(y-1);
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				pxt[pxt.length] = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}

			if(_y > 0)
			{
				if(_st < 0)
				{
					_st += _bb2*((_x<<1)+3);
					_tt += _bb4*(++_x);
					_pxb[_pxb.length] = _y-1;
				}
				else if(_tt < 0)
				{
					_st += _bb2*((_x<<1)+3) - _aa4*(_y-1);
					_tt += _bb4*(++_x) - _aa2*(((_y--)<<1)-3);
					_pxb[_pxb.length] = _y-1;
				}
				else
				{
					_tt -= _aa2*((_y<<1)-3);
					_st -= _aa4*(--_y);
					_pxb[_pxb.length-1]--;
				}
			}
		}

		var ox = -wod, oy = b,
		_oy = _pxb[0],
		l = pxl.length,
		w, h;
		for(var i = 0; i < l; i++)
		{
			if(typeof _pxb[i] != "undefined")
			{
				if(_pxb[i] < _oy || pxt[i] < oy)
				{
					x = pxl[i];
					this._mkOvQds(cx, cy, x, oy, x-ox, oy-_oy, wod, hod);
					ox = x;
					oy = pxt[i];
					_oy = _pxb[i];
				}
			}
			else
			{
				x = pxl[i];
				this._mkDiv(cx-x, cy-oy, 1, (oy<<1)+hod);
				this._mkDiv(cx+ox+wod, cy-oy, 1, (oy<<1)+hod);
				ox = x;
				oy = pxt[i];
			}
		}
		this._mkDiv(cx-a, cy-oy, 1, (oy<<1)+hod);
		this._mkDiv(cx+ox+wod, cy-oy, 1, (oy<<1)+hod);
	}
}

function _mkOvDott(left, top, width, height)
{
	var a = (++width)>>1, b = (++height)>>1,
	wod = width&1, hod = height&1, hodu = hod^1,
	cx = left+a, cy = top+b,
	x = 0, y = b,
	aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
	st = (aa2>>1)*(1-(b<<1)) + bb2,
	tt = (bb2>>1) - aa2*((b<<1)-1),
	drw = true;
	while(y > 0)
	{
		if(st < 0)
		{
			st += bb2*((x<<1)+3);
			tt += bb4*(++x);
		}
		else if(tt < 0)
		{
			st += bb2*((x<<1)+3) - aa4*(y-1);
			tt += bb4*(++x) - aa2*(((y--)<<1)-3);
		}
		else
		{
			tt -= aa2*((y<<1)-3);
			st -= aa4*(--y);
		}
		if(drw && y >= hodu) this._mkOvQds(cx, cy, x, y, 1, 1, wod, hod);
		drw = !drw;
	}
}

function _mkRect(x, y, w, h)
{
	var s = this.stroke;
	this._mkDiv(x, y, w, s);
	this._mkDiv(x+w, y, s, h);
	this._mkDiv(x, y+h, w+s, s);
	this._mkDiv(x, y+s, s, h-s);
}

function _mkRectDott(x, y, w, h)
{
	this.drawLine(x, y, x+w, y);
	this.drawLine(x+w, y, x+w, y+h);
	this.drawLine(x, y+h, x+w, y+h);
	this.drawLine(x, y, x, y+h);
}

function jsgFont()
{
	this.PLAIN = 'font-weight:normal;';
	this.BOLD = 'font-weight:bold;';
	this.ITALIC = 'font-style:italic;';
	this.ITALIC_BOLD = this.ITALIC + this.BOLD;
	this.BOLD_ITALIC = this.ITALIC_BOLD;
}
var Font = new jsgFont();

function jsgStroke()
{
	this.DOTTED = -1;
}
var Stroke = new jsgStroke();

function jsGraphics(cnv, wnd)
{
	this.setColor = function(x)
	{
		this.color = x.toLowerCase();
	};

	this.setStroke = function(x)
	{
		this.stroke = x;
		if(!(x+1))
		{
			this.drawLine = _mkLinDott;
			this._mkOv = _mkOvDott;
			this.drawRect = _mkRectDott;
		}
		else if(x-1 > 0)
		{
			this.drawLine = _mkLin2D;
			this._mkOv = _mkOv2D;
			this.drawRect = _mkRect;
		}
		else
		{
			this.drawLine = _mkLin;
			this._mkOv = _mkOv;
			this.drawRect = _mkRect;
		}
	};

	this.setPrintable = function(arg)
	{
		this.printable = arg;
		if(jg_fast)
		{
			this._mkDiv = _mkDivIe;
			this._htmRpc = arg? _htmPrtRpc : _htmRpc;
		}
		else this._mkDiv = arg? _mkDivPrt : _mkDiv;
	};

	this.setFont = function(fam, sz, sty)
	{
		this.ftFam = fam;
		this.ftSz = sz;
		this.ftSty = sty || Font.PLAIN;
	};

	this.drawPolyline = this.drawPolyLine = function(x, y)
	{
		for (var i=x.length - 1; i;)
		{--i;
			this.drawLine(x[i], y[i], x[i+1], y[i+1]);
		}
	};

	this.fillRect = function(x, y, w, h)
	{
		this._mkDiv(x, y, w, h);
	};

	this.drawPolygon = function(x, y)
	{
		this.drawPolyline(x, y);
		this.drawLine(x[x.length-1], y[x.length-1], x[0], y[0]);
	};

	this.drawEllipse = this.drawOval = function(x, y, w, h)
	{
		this._mkOv(x, y, w, h);
	};

	this.fillEllipse = this.fillOval = function(left, top, w, h)
	{
		var a = w>>1, b = h>>1,
		wod = w&1, hod = h&1,
		cx = left+a, cy = top+b,
		x = 0, y = b, oy = b,
		aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
		st = (aa2>>1)*(1-(b<<1)) + bb2,
		tt = (bb2>>1) - aa2*((b<<1)-1),
		xl, dw, dh;
		if(w) while(y > 0)
		{
			if(st < 0)
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0)
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				xl = cx-x;
				dw = (x<<1)+wod;
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				dh = oy-y;
				this._mkDiv(xl, cy-oy, dw, dh);
				this._mkDiv(xl, cy+y+hod, dw, dh);
				oy = y;
			}
			else
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
			}
		}
		this._mkDiv(cx-a, cy-oy, w, (oy<<1)+hod);
	};

	this.fillArc = function(iL, iT, iW, iH, fAngA, fAngZ)
	{
		var a = iW>>1, b = iH>>1,
		iOdds = (iW&1) | ((iH&1) << 16),
		cx = iL+a, cy = iT+b,
		x = 0, y = b, ox = x, oy = y,
		aa2 = (a*a)<<1, aa4 = aa2<<1, bb2 = (b*b)<<1, bb4 = bb2<<1,
		st = (aa2>>1)*(1-(b<<1)) + bb2,
		tt = (bb2>>1) - aa2*((b<<1)-1),
		// Vars for radial boundary lines
		xEndA, yEndA, xEndZ, yEndZ,
		iSects = (1 << (Math.floor((fAngA %= 360.0)/180.0) << 3))
				| (2 << (Math.floor((fAngZ %= 360.0)/180.0) << 3))
				| ((fAngA >= fAngZ) << 16),
		aBndA = new Array(b+1), aBndZ = new Array(b+1);
		
		// Set up radial boundary lines
		fAngA *= Math.PI/180.0;
		fAngZ *= Math.PI/180.0;
		xEndA = cx+Math.round(a*Math.cos(fAngA));
		yEndA = cy+Math.round(-b*Math.sin(fAngA));
		_mkLinVirt(aBndA, cx, cy, xEndA, yEndA);
		xEndZ = cx+Math.round(a*Math.cos(fAngZ));
		yEndZ = cy+Math.round(-b*Math.sin(fAngZ));
		_mkLinVirt(aBndZ, cx, cy, xEndZ, yEndZ);

		while(y > 0)
		{
			if(st < 0) // Advance x
			{
				st += bb2*((x<<1)+3);
				tt += bb4*(++x);
			}
			else if(tt < 0) // Advance x and y
			{
				st += bb2*((x<<1)+3) - aa4*(y-1);
				ox = x;
				tt += bb4*(++x) - aa2*(((y--)<<1)-3);
				this._mkArcDiv(ox, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
				oy = y;
			}
			else // Advance y
			{
				tt -= aa2*((y<<1)-3);
				st -= aa4*(--y);
				if(y && (aBndA[y] != aBndA[y-1] || aBndZ[y] != aBndZ[y-1]))
				{
					this._mkArcDiv(x, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
					ox = x;
					oy = y;
				}
			}
		}
		this._mkArcDiv(x, 0, oy, cx, cy, iOdds, aBndA, aBndZ, iSects);
		if(iOdds >> 16) // Odd height
		{
			if(iSects >> 16) // Start-angle > end-angle
			{
				var xl = (yEndA <= cy || yEndZ > cy)? (cx - x) : cx;
				this._mkDiv(xl, cy, x + cx - xl + (iOdds & 0xffff), 1);
			}
			else if((iSects & 0x01) && yEndZ > cy)
				this._mkDiv(cx - x, cy, x, 1);
		}
	};

/* fillPolygon method, implemented by Matthieu Haller.
This javascript function is an adaptation of the gdImageFilledPolygon for Walter Zorn lib.
C source of GD 1.8.4 found at http://www.boutell.com/gd/

THANKS to Kirsten Schulz for the polygon fixes!

The intersection finding technique of this code could be improved
by remembering the previous intertersection, and by using the slope.
That could help to adjust intersections to produce a nice
interior_extrema. */
	this.fillPolygon = function(array_x, array_y)
	{
		var i;
		var y;
		var miny, maxy;
		var x1, y1;
		var x2, y2;
		var ind1, ind2;
		var ints;

		var n = array_x.length;
		if(!n) return;

		miny = array_y[0];
		maxy = array_y[0];
		for(i = 1; i < n; i++)
		{
			if(array_y[i] < miny)
				miny = array_y[i];

			if(array_y[i] > maxy)
				maxy = array_y[i];
		}
		for(y = miny; y <= maxy; y++)
		{
			var polyInts = new Array();
			ints = 0;
			for(i = 0; i < n; i++)
			{
				if(!i)
				{
					ind1 = n-1;
					ind2 = 0;
				}
				else
				{
					ind1 = i-1;
					ind2 = i;
				}
				y1 = array_y[ind1];
				y2 = array_y[ind2];
				if(y1 < y2)
				{
					x1 = array_x[ind1];
					x2 = array_x[ind2];
				}
				else if(y1 > y2)
				{
					y2 = array_y[ind1];
					y1 = array_y[ind2];
					x2 = array_x[ind1];
					x1 = array_x[ind2];
				}
				else continue;

				 //  Modified 11. 2. 2004 Walter Zorn
				if((y >= y1) && (y < y2))
					polyInts[ints++] = Math.round((y-y1) * (x2-x1) / (y2-y1) + x1);

				else if((y == maxy) && (y > y1) && (y <= y2))
					polyInts[ints++] = Math.round((y-y1) * (x2-x1) / (y2-y1) + x1);
			}
			polyInts.sort(_CompInt);
			for(i = 0; i < ints; i+=2)
				this._mkDiv(polyInts[i], y, polyInts[i+1]-polyInts[i]+1, 1);
		}
	};

	this.drawString = function(txt, x, y)
	{
		this.htm += '<div style="position:absolute;white-space:nowrap;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			'font-family:' +  this.ftFam + ';'+
			'font-size:' + this.ftSz + ';'+
			'color:' + this.color + ';' + this.ftSty + '">'+
			txt +
			'<\/div>';
	};

/* drawStringRect() added by Rick Blommers.
Allows to specify the size of the text rectangle and to align the
text both horizontally (e.g. right) and vertically within that rectangle */
	this.drawStringRect = function(txt, x, y, width, halign)
	{
		this.htm += '<div style="position:absolute;overflow:hidden;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			'width:'+width +'px;'+
			'text-align:'+halign+';'+
			'font-family:' +  this.ftFam + ';'+
			'font-size:' + this.ftSz + ';'+
			'color:' + this.color + ';' + this.ftSty + '">'+
			txt +
			'<\/div>';
	};

	this.drawImage = function(imgSrc, x, y, w, h, a)
	{
		this.htm += '<div style="position:absolute;'+
			'left:' + x + 'px;'+
			'top:' + y + 'px;'+
			// w (width) and h (height) arguments are now optional.
			// Added by Mahmut Keygubatli, 14.1.2008
			(w? ('width:' +  w + 'px;') : '') +
			(h? ('height:' + h + 'px;'):'')+'">'+
			'<img src="' + imgSrc +'"'+ (w ? (' width="' + w + '"'):'')+ (h ? (' height="' + h + '"'):'') + (a? (' '+a) : '') + '>'+
			'<\/div>';
	};

	this.clear = function()
	{
		this.htm = "";
		if(this.cnv) this.cnv.innerHTML = "";
	};

	this._mkOvQds = function(cx, cy, x, y, w, h, wod, hod)
	{
		var xl = cx - x, xr = cx + x + wod - w, yt = cy - y, yb = cy + y + hod - h;
		if(xr > xl+w)
		{
			this._mkDiv(xr, yt, w, h);
			this._mkDiv(xr, yb, w, h);
		}
		else
			w = xr - xl + w;
		this._mkDiv(xl, yt, w, h);
		this._mkDiv(xl, yb, w, h);
	};
	
	this._mkArcDiv = function(x, y, oy, cx, cy, iOdds, aBndA, aBndZ, iSects)
	{
		var xrDef = cx + x + (iOdds & 0xffff), y2, h = oy - y, xl, xr, w;

		if(!h) h = 1;
		x = cx - x;

		if(iSects & 0xff0000) // Start-angle > end-angle
		{
			y2 = cy - y - h;
			if(iSects & 0x00ff)
			{
				if(iSects & 0x02)
				{
					xl = Math.max(x, aBndZ[y]);
					w = xrDef - xl;
					if(w > 0) this._mkDiv(xl, y2, w, h);
				}
				if(iSects & 0x01)
				{
					xr = Math.min(xrDef, aBndA[y]);
					w = xr - x;
					if(w > 0) this._mkDiv(x, y2, w, h);
				}
			}
			else
				this._mkDiv(x, y2, xrDef - x, h);
			y2 = cy + y + (iOdds >> 16);
			if(iSects & 0xff00)
			{
				if(iSects & 0x0100)
				{
					xl = Math.max(x, aBndA[y]);
					w = xrDef - xl;
					if(w > 0) this._mkDiv(xl, y2, w, h);
				}
				if(iSects & 0x0200)
				{
					xr = Math.min(xrDef, aBndZ[y]);
					w = xr - x;
					if(w > 0) this._mkDiv(x, y2, w, h);
				}
			}
			else
				this._mkDiv(x, y2, xrDef - x, h);
		}
		else
		{
			if(iSects & 0x00ff)
			{
				if(iSects & 0x02)
					xl = Math.max(x, aBndZ[y]);
				else
					xl = x;
				if(iSects & 0x01)
					xr = Math.min(xrDef, aBndA[y]);
				else
					xr = xrDef;
				y2 = cy - y - h;
				w = xr - xl;
				if(w > 0) this._mkDiv(xl, y2, w, h);
			}
			if(iSects & 0xff00)
			{
				if(iSects & 0x0100)
					xl = Math.max(x, aBndA[y]);
				else
					xl = x;
				if(iSects & 0x0200)
					xr = Math.min(xrDef, aBndZ[y]);
				else
					xr = xrDef;
				y2 = cy + y + (iOdds >> 16);
				w = xr - xl;
				if(w > 0) this._mkDiv(xl, y2, w, h);
			}
		}
	};

	this.setStroke(1);
	this.setFont("verdana,geneva,helvetica,sans-serif", "12px", Font.PLAIN);
	this.color = "#000000";
	this.htm = "";
	this.wnd = wnd || window;

	if(!jg_ok) _chkDHTM(this.wnd);
	if(jg_ok)
	{
		if(cnv)
		{
			if(typeof(cnv) == "string")
				this.cont = document.all? (this.wnd.document.all[cnv] || null)
					: document.getElementById? (this.wnd.document.getElementById(cnv) || null)
					: null;
			else if(cnv == window.document)
				this.cont = document.getElementsByTagName("body")[0];
			// If cnv is a direct reference to a canvas DOM node
			// (option suggested by Andreas Luleich)
			else this.cont = cnv;
			// Create new canvas inside container DIV. Thus the drawing and clearing
			// methods won't interfere with the container's inner html.
			// Solution suggested by Vladimir.
			this.cnv = this.wnd.document.createElement("div");
			this.cnv.style.fontSize=0;
			this.cont.appendChild(this.cnv);
			this.paint = jg_dom? _pntCnvDom : _pntCnvIe;
		}
		else
			this.paint = _pntDoc;
	}
	else
		this.paint = _pntN;

	this.setPrintable(false);
}

function _mkLinVirt(aLin, x1, y1, x2, y2)
{
	var dx = Math.abs(x2-x1), dy = Math.abs(y2-y1),
	x = x1, y = y1,
	xIncr = (x1 > x2)? -1 : 1,
	yIncr = (y1 > y2)? -1 : 1,
	p,
	i = 0;
	if(dx >= dy)
	{
		var pr = dy<<1,
		pru = pr - (dx<<1);
		p = pr-dx;
		while(dx > 0)
		{--dx;
			if(p > 0)    //  Increment y
			{
				aLin[i++] = x;
				y += yIncr;
				p += pru;
			}
			else p += pr;
			x += xIncr;
		}
	}
	else
	{
		var pr = dx<<1,
		pru = pr - (dy<<1);
		p = pr-dy;
		while(dy > 0)
		{--dy;
			y += yIncr;
			aLin[i++] = x;
			if(p > 0)    //  Increment x
			{
				x += xIncr;
				p += pru;
			}
			else p += pr;
		}
	}
	for(var len = aLin.length, i = len-i; i;)
		aLin[len-(i--)] = x;
};

function _CompInt(x, y)
{
	return(x - y);
}


/**This notice must be untouched at all times.
This is the COMPRESSED version of the Draw2D Library
WebSite: http://www.draw2d.org
Copyright: 2006 Andreas Herz. All rights reserved.
Created: 5.11.2006 by Andreas Herz (Web: http://www.freegroup.de )
LICENSE: LGPL
**/
var draw2d=new Object();
var _errorStack_=[];
function pushErrorStack(e,_500b){
_errorStack_.push(_500b+"\n");
throw e;
}
draw2d.AbstractEvent=function(){
this.type=null;
this.target=null;
this.relatedTarget=null;
this.cancelable=false;
this.timeStamp=null;
this.returnValue=true;
};
draw2d.AbstractEvent.prototype.initEvent=function(sType,_500d){
this.type=sType;
this.cancelable=_500d;
this.timeStamp=(new Date()).getTime();
};
draw2d.AbstractEvent.prototype.preventDefault=function(){
if(this.cancelable){
this.returnValue=false;
}
};
draw2d.AbstractEvent.fireDOMEvent=function(_500e,_500f){
if(document.createEvent){
var evt=document.createEvent("Events");
evt.initEvent(_500e,true,true);
_500f.dispatchEvent(evt);
}else{
if(document.createEventObject){
var evt=document.createEventObject();
_500f.fireEvent("on"+_500e,evt);
}
}
};
draw2d.EventTarget=function(){
this.eventhandlers={};
};
draw2d.EventTarget.prototype.addEventListener=function(sType,_5012){
if(typeof this.eventhandlers[sType]=="undefined"){
this.eventhandlers[sType]=[];
}
this.eventhandlers[sType][this.eventhandlers[sType].length]=_5012;
};
draw2d.EventTarget.prototype.dispatchEvent=function(_5013){
_5013.target=this;
if(typeof this.eventhandlers[_5013.type]!="undefined"){
for(var i=0;i<this.eventhandlers[_5013.type].length;i++){
this.eventhandlers[_5013.type][i](_5013);
}
}
return _5013.returnValue;
};
draw2d.EventTarget.prototype.removeEventListener=function(sType,_5016){
if(typeof this.eventhandlers[sType]!="undefined"){
var _5017=[];
for(var i=0;i<this.eventhandlers[sType].length;i++){
if(this.eventhandlers[sType][i]!=_5016){
_5017[_5017.length]=this.eventhandlers[sType][i];
}
}
this.eventhandlers[sType]=_5017;
}
};
String.prototype.trim=function(){
return (this.replace(new RegExp("^([\\s]+)|([\\s]+)$","gm"),""));
};
String.prototype.lefttrim=function(){
return (this.replace(new RegExp("^[\\s]+","gm"),""));
};
String.prototype.righttrim=function(){
return (this.replace(new RegExp("[\\s]+$","gm"),""));
};
String.prototype.between=function(left,right,_5228){
if(!_5228){
_5228=0;
}
var li=this.indexOf(left,_5228);
if(li==-1){
return null;
}
var ri=this.indexOf(right,li);
if(ri==-1){
return null;
}
return this.substring(li+left.length,ri);
};
draw2d.UUID=function(){
};
draw2d.UUID.prototype.type="draw2d.UUID";
draw2d.UUID.create=function(){
var _5897=function(){
return (((1+Math.random())*65536)|0).toString(16).substring(1);
};
return (_5897()+_5897()+"-"+_5897()+"-"+_5897()+"-"+_5897()+"-"+_5897()+_5897()+_5897());
};
draw2d.ArrayList=function(){
this.increment=10;
this.size=0;
this.data=new Array(this.increment);
};
draw2d.ArrayList.EMPTY_LIST=new draw2d.ArrayList();
draw2d.ArrayList.prototype.type="draw2d.ArrayList";
draw2d.ArrayList.prototype.reverse=function(){
var _4bc1=new Array(this.size);
for(var i=0;i<this.size;i++){
_4bc1[i]=this.data[this.size-i-1];
}
this.data=_4bc1;
};
draw2d.ArrayList.prototype.getCapacity=function(){
return this.data.length;
};
draw2d.ArrayList.prototype.getSize=function(){
return this.size;
};
draw2d.ArrayList.prototype.isEmpty=function(){
return this.getSize()===0;
};
draw2d.ArrayList.prototype.getLastElement=function(){
if(this.data[this.getSize()-1]!==null){
return this.data[this.getSize()-1];
}
};
draw2d.ArrayList.prototype.getFirstElement=function(){
if(this.data[0]!==null&&this.data[0]!==undefined){
return this.data[0];
}
return null;
};
draw2d.ArrayList.prototype.get=function(i){
return this.data[i];
};
draw2d.ArrayList.prototype.add=function(obj){
if(this.getSize()==this.data.length){
this.resize();
}
this.data[this.size++]=obj;
};
draw2d.ArrayList.prototype.addAll=function(obj){
for(var i=0;i<obj.getSize();i++){
this.add(obj.get(i));
}
};
draw2d.ArrayList.prototype.remove=function(obj){
var index=this.indexOf(obj);
if(index>=0){
return this.removeElementAt(index);
}
return null;
};
draw2d.ArrayList.prototype.insertElementAt=function(obj,index){
if(this.size==this.capacity){
this.resize();
}
for(var i=this.getSize();i>index;i--){
this.data[i]=this.data[i-1];
}
this.data[index]=obj;
this.size++;
};
draw2d.ArrayList.prototype.removeElementAt=function(index){
var _4bcd=this.data[index];
for(var i=index;i<(this.getSize()-1);i++){
this.data[i]=this.data[i+1];
}
this.data[this.getSize()-1]=null;
this.size--;
return _4bcd;
};
draw2d.ArrayList.prototype.removeAllElements=function(){
this.size=0;
for(var i=0;i<this.data.length;i++){
this.data[i]=null;
}
};
draw2d.ArrayList.prototype.indexOf=function(obj){
for(var i=0;i<this.getSize();i++){
if(this.data[i]==obj){
return i;
}
}
return -1;
};
draw2d.ArrayList.prototype.contains=function(obj){
for(var i=0;i<this.getSize();i++){
if(this.data[i]==obj){
return true;
}
}
return false;
};
draw2d.ArrayList.prototype.resize=function(){
newData=new Array(this.data.length+this.increment);
for(var i=0;i<this.data.length;i++){
newData[i]=this.data[i];
}
this.data=newData;
};
draw2d.ArrayList.prototype.trimToSize=function(){
if(this.data.length==this.size){
return;
}
var temp=new Array(this.getSize());
for(var i=0;i<this.getSize();i++){
temp[i]=this.data[i];
}
this.size=temp.length;
this.data=temp;
};
draw2d.ArrayList.prototype.sort=function(f){
var i,j;
var _4bd9;
var _4bda;
var _4bdb;
var _4bdc;
for(i=1;i<this.getSize();i++){
_4bda=this.data[i];
_4bd9=_4bda[f];
j=i-1;
_4bdb=this.data[j];
_4bdc=_4bdb[f];
while(j>=0&&_4bdc>_4bd9){
this.data[j+1]=this.data[j];
j--;
if(j>=0){
_4bdb=this.data[j];
_4bdc=_4bdb[f];
}
}
this.data[j+1]=_4bda;
}
};
draw2d.ArrayList.prototype.clone=function(){
var _4bdd=new draw2d.ArrayList(this.size);
for(var i=0;i<this.size;i++){
_4bdd.add(this.data[i]);
}
return _4bdd;
};
draw2d.ArrayList.prototype.overwriteElementAt=function(obj,index){
this.data[index]=obj;
};
draw2d.ArrayList.prototype.getPersistentAttributes=function(){
return {data:this.data,increment:this.increment,size:this.getSize()};
};
function trace(_4af2){
var _4af3=openwindow("about:blank",700,400);
_4af3.document.writeln("<pre>"+_4af2+"</pre>");
}
function openwindow(url,width,_4af6){
var left=(screen.width-width)/2;
var top=(screen.height-_4af6)/2;
property="left="+left+", top="+top+", toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width="+width+",height="+_4af6;
return window.open(url,"_blank",property);
}
function dumpObject(obj){
trace("----------------------------------------------------------------------------");
trace("- Object dump");
trace("----------------------------------------------------------------------------");
for(var i in obj){
try{
if(typeof obj[i]!="function"){
trace(i+" --&gt; "+obj[i]);
}
}
catch(e){
}
}
for(var i in obj){
try{
if(typeof obj[i]=="function"){
trace(i+" --&gt; "+obj[i]);
}
}
catch(e){
}
}
trace("----------------------------------------------------------------------------");
}
draw2d.Drag=function(){
};
draw2d.Drag.current=null;
draw2d.Drag.currentTarget=null;
draw2d.Drag.currentHover=null;
draw2d.Drag.currentCompartment=null;
draw2d.Drag.dragging=false;
draw2d.Drag.isDragging=function(){
return this.dragging;
};
draw2d.Drag.setCurrent=function(_5946){
this.current=_5946;
this.dragging=true;
};
draw2d.Drag.getCurrent=function(){
return this.current;
};
draw2d.Drag.clearCurrent=function(){
this.current=null;
this.dragging=false;
this.currentTarget=null;
};
draw2d.Draggable=function(_5947,_5948){
this.id=draw2d.UUID.create();
this.node=null;
draw2d.EventTarget.call(this);
this.construct(_5947,_5948);
this.diffX=0;
this.diffY=0;
this.targets=new draw2d.ArrayList();
};
draw2d.Draggable.prototype=new draw2d.EventTarget();
draw2d.Draggable.prototype.construct=function(_5949){
if(_5949===null||_5949===undefined){
return;
}
this.element=_5949;
var oThis=this;
var _594b=function(){
var _594c=new draw2d.DragDropEvent();
_594c.initDragDropEvent("dblclick",true);
oThis.dispatchEvent(_594c);
var _594d=arguments[0]||window.event;
_594d.cancelBubble=true;
_594d.returnValue=false;
};
var _594e=function(){
var _594f=arguments[0]||window.event;
var _5950=new draw2d.DragDropEvent();
if(oThis.node!==null){
var _5951=oThis.node.getWorkflow().getAbsoluteX();
var _5952=oThis.node.getWorkflow().getAbsoluteY();
var _5953=oThis.node.getWorkflow().getScrollLeft();
var _5954=oThis.node.getWorkflow().getScrollTop();
_5950.x=_594f.clientX-oThis.element.offsetLeft+_5953-_5951;
_5950.y=_594f.clientY-oThis.element.offsetTop+_5954-_5952;
}
if(_594f.button===2){
_5950.initDragDropEvent("contextmenu",true);
oThis.dispatchEvent(_5950);
}else{
_5950.initDragDropEvent("dragstart",true);
if(oThis.dispatchEvent(_5950)){
oThis.diffX=_594f.clientX-oThis.element.offsetLeft;
oThis.diffY=_594f.clientY-oThis.element.offsetTop;
draw2d.Drag.setCurrent(oThis);
if(oThis.isAttached==true){
oThis.detachEventHandlers();
}
oThis.attachEventHandlers();
}
}
_594f.cancelBubble=true;
_594f.returnValue=false;
};
var _5955=function(){
if(draw2d.Drag.getCurrent()===null){
var _5956=arguments[0]||window.event;
if(draw2d.Drag.currentHover!==null&&oThis!==draw2d.Drag.currentHover){
var _5957=new draw2d.DragDropEvent();
_5957.initDragDropEvent("mouseleave",false,oThis);
draw2d.Drag.currentHover.dispatchEvent(_5957);
}
if(oThis!==null&&oThis!==draw2d.Drag.currentHover){
var _5957=new draw2d.DragDropEvent();
_5957.initDragDropEvent("mouseenter",false,oThis);
oThis.dispatchEvent(_5957);
}
draw2d.Drag.currentHover=oThis;
}else{
}
};
if(this.element.addEventListener){
this.element.addEventListener("mousemove",_5955,false);
this.element.addEventListener("mousedown",_594e,false);
this.element.addEventListener("dblclick",_594b,false);
}else{
if(this.element.attachEvent){
this.element.attachEvent("onmousemove",_5955);
this.element.attachEvent("onmousedown",_594e);
this.element.attachEvent("ondblclick",_594b);
}else{
throw "Drag not supported in this browser.";
}
}
};
draw2d.Draggable.prototype.onDrop=function(_5958,_5959){
};
draw2d.Draggable.prototype.attachEventHandlers=function(){
var oThis=this;
oThis.isAttached=true;
this.tempMouseMove=function(){
var _595b=arguments[0]||window.event;
var _595c=new draw2d.Point(_595b.clientX-oThis.diffX,_595b.clientY-oThis.diffY);
if(oThis.node!==null&&oThis.node.getCanSnapToHelper()){
_595c=oThis.node.getWorkflow().snapToHelper(oThis.node,_595c);
}
oThis.element.style.left=_595c.x+"px";
oThis.element.style.top=_595c.y+"px";
if(oThis.node!==null){
var _595d=oThis.node.getWorkflow().getScrollLeft();
var _595e=oThis.node.getWorkflow().getScrollTop();
var _595f=oThis.node.getWorkflow().getAbsoluteX();
var _5960=oThis.node.getWorkflow().getAbsoluteY();
var _5961=oThis.getDropTarget(_595b.clientX+_595d-_595f,_595b.clientY+_595e-_5960);
var _5962=oThis.getCompartment(_595b.clientX+_595d-_595f,_595b.clientY+_595e-_5960);
if(draw2d.Drag.currentTarget!==null&&_5961!=draw2d.Drag.currentTarget){
var _5963=new draw2d.DragDropEvent();
_5963.initDragDropEvent("dragleave",false,oThis);
draw2d.Drag.currentTarget.dispatchEvent(_5963);
}
if(_5961!==null&&_5961!==draw2d.Drag.currentTarget){
var _5963=new draw2d.DragDropEvent();
_5963.initDragDropEvent("dragenter",false,oThis);
_5961.dispatchEvent(_5963);
}
draw2d.Drag.currentTarget=_5961;
if(draw2d.Drag.currentCompartment!==null&&_5962!==draw2d.Drag.currentCompartment){
var _5963=new draw2d.DragDropEvent();
_5963.initDragDropEvent("figureleave",false,oThis);
draw2d.Drag.currentCompartment.dispatchEvent(_5963);
}
if(_5962!==null&&_5962.node!=oThis.node&&_5962!==draw2d.Drag.currentCompartment){
var _5963=new draw2d.DragDropEvent();
_5963.initDragDropEvent("figureenter",false,oThis);
_5962.dispatchEvent(_5963);
}
draw2d.Drag.currentCompartment=_5962;
}
var _5964=new draw2d.DragDropEvent();
_5964.initDragDropEvent("drag",false);
oThis.dispatchEvent(_5964);
};
oThis.tempMouseUp=function(){
oThis.detachEventHandlers();
var _5965=arguments[0]||window.event;
if(oThis.node!==null){
var _5966=oThis.node.getWorkflow().getScrollLeft();
var _5967=oThis.node.getWorkflow().getScrollTop();
var _5968=oThis.node.getWorkflow().getAbsoluteX();
var _5969=oThis.node.getWorkflow().getAbsoluteY();
var _596a=oThis.getDropTarget(_5965.clientX+_5966-_5968,_5965.clientY+_5967-_5969);
var _596b=oThis.getCompartment(_5965.clientX+_5966-_5968,_5965.clientY+_5967-_5969);
if(_596a!==null){
var _596c=new draw2d.DragDropEvent();
_596c.initDragDropEvent("drop",false,oThis);
_596a.dispatchEvent(_596c);
}
if(_596b!==null&&_596b.node!==oThis.node){
var _596c=new draw2d.DragDropEvent();
_596c.initDragDropEvent("figuredrop",false,oThis);
_596b.dispatchEvent(_596c);
}
if(draw2d.Drag.currentTarget!==null){
var _596c=new draw2d.DragDropEvent();
_596c.initDragDropEvent("dragleave",false,oThis);
draw2d.Drag.currentTarget.dispatchEvent(_596c);
draw2d.Drag.currentTarget=null;
}
}
var _596d=new draw2d.DragDropEvent();
_596d.initDragDropEvent("dragend",false);
oThis.dispatchEvent(_596d);
oThis.onDrop(_5965.clientX,_5965.clientY);
draw2d.Drag.currentCompartment=null;
draw2d.Drag.clearCurrent();
};
if(document.body.addEventListener){
document.body.addEventListener("mousemove",this.tempMouseMove,false);
document.body.addEventListener("mouseup",this.tempMouseUp,false);
}else{
if(document.body.attachEvent){
document.body.attachEvent("onmousemove",this.tempMouseMove);
document.body.attachEvent("onmouseup",this.tempMouseUp);
}else{
throw new Error("Drag doesn't support this browser.");
}
}
};
draw2d.Draggable.prototype.detachEventHandlers=function(){
this.isAttached=false;
if(document.body.removeEventListener){
document.body.removeEventListener("mousemove",this.tempMouseMove,false);
document.body.removeEventListener("mouseup",this.tempMouseUp,false);
}else{
if(document.body.detachEvent){
document.body.detachEvent("onmousemove",this.tempMouseMove);
document.body.detachEvent("onmouseup",this.tempMouseUp);
}else{
throw new Error("Drag doesn't support this browser.");
}
}
};
draw2d.Draggable.prototype.getDropTarget=function(x,y){
for(var i=0;i<this.targets.getSize();i++){
var _5971=this.targets.get(i);
if(_5971.node.isOver(x,y)&&_5971.node!==this.node){
return _5971;
}
}
return null;
};
draw2d.Draggable.prototype.getCompartment=function(x,y){
var _5974=null;
for(var i=0;i<this.node.getWorkflow().compartments.getSize();i++){
var _5976=this.node.getWorkflow().compartments.get(i);
if(_5976.isOver(x,y)&&_5976!==this.node){
if(_5974===null){
_5974=_5976;
}else{
if(_5974.getZOrder()<_5976.getZOrder()){
_5974=_5976;
}
}
}
}
return _5974===null?null:_5974.dropable;
};
draw2d.Draggable.prototype.getLeft=function(){
return this.element.offsetLeft;
};
draw2d.Draggable.prototype.getTop=function(){
return this.element.offsetTop;
};
draw2d.DragDropEvent=function(){
draw2d.AbstractEvent.call(this);
};
draw2d.DragDropEvent.prototype=new draw2d.AbstractEvent();
draw2d.DragDropEvent.prototype.initDragDropEvent=function(sType,_5978,_5979){
this.initEvent(sType,_5978);
this.relatedTarget=_5979;
};
draw2d.DropTarget=function(_597a){
draw2d.EventTarget.call(this);
this.construct(_597a);
};
draw2d.DropTarget.prototype=new draw2d.EventTarget();
draw2d.DropTarget.prototype.construct=function(_597b){
this.element=_597b;
};
draw2d.DropTarget.prototype.getLeft=function(){
var el=this.element;
var ol=el.offsetLeft;
while((el=el.offsetParent)!==null){
ol+=el.offsetLeft;
}
return ol;
};
draw2d.DropTarget.prototype.getTop=function(){
var el=this.element;
var ot=el.offsetTop;
while((el=el.offsetParent)!==null){
ot+=el.offsetTop;
}
return ot;
};
draw2d.DropTarget.prototype.getHeight=function(){
return this.element.offsetHeight;
};
draw2d.DropTarget.prototype.getWidth=function(){
return this.element.offsetWidth;
};
draw2d.PositionConstants=function(){
};
draw2d.PositionConstants.NORTH=1;
draw2d.PositionConstants.SOUTH=4;
draw2d.PositionConstants.WEST=8;
draw2d.PositionConstants.EAST=16;
draw2d.Color=function(red,green,blue){
if(typeof green=="undefined"){
var rgb=this.hex2rgb(red);
this.red=rgb[0];
this.green=rgb[1];
this.blue=rgb[2];
}else{
this.red=red;
this.green=green;
this.blue=blue;
}
};
draw2d.Color.prototype.type="draw2d.Color";
draw2d.Color.prototype.getHTMLStyle=function(){
return "rgb("+this.red+","+this.green+","+this.blue+")";
};
draw2d.Color.prototype.getRed=function(){
return this.red;
};
draw2d.Color.prototype.getGreen=function(){
return this.green;
};
draw2d.Color.prototype.getBlue=function(){
return this.blue;
};
draw2d.Color.prototype.getIdealTextColor=function(){
var _5dae=105;
var _5daf=(this.red*0.299)+(this.green*0.587)+(this.blue*0.114);
return (255-_5daf<_5dae)?new draw2d.Color(0,0,0):new draw2d.Color(255,255,255);
};
draw2d.Color.prototype.hex2rgb=function(_5db0){
_5db0=_5db0.replace("#","");
return ({0:parseInt(_5db0.substr(0,2),16),1:parseInt(_5db0.substr(2,2),16),2:parseInt(_5db0.substr(4,2),16)});
};
draw2d.Color.prototype.hex=function(){
return (this.int2hex(this.red)+this.int2hex(this.green)+this.int2hex(this.blue));
};
draw2d.Color.prototype.int2hex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
draw2d.Color.prototype.darker=function(_5db2){
var red=parseInt(Math.round(this.getRed()*(1-_5db2)));
var green=parseInt(Math.round(this.getGreen()*(1-_5db2)));
var blue=parseInt(Math.round(this.getBlue()*(1-_5db2)));
if(red<0){
red=0;
}else{
if(red>255){
red=255;
}
}
if(green<0){
green=0;
}else{
if(green>255){
green=255;
}
}
if(blue<0){
blue=0;
}else{
if(blue>255){
blue=255;
}
}
return new draw2d.Color(red,green,blue);
};
draw2d.Color.prototype.lighter=function(_5db6){
var red=parseInt(Math.round(this.getRed()*(1+_5db6)));
var green=parseInt(Math.round(this.getGreen()*(1+_5db6)));
var blue=parseInt(Math.round(this.getBlue()*(1+_5db6)));
if(red<0){
red=0;
}else{
if(red>255){
red=255;
}
}
if(green<0){
green=0;
}else{
if(green>255){
green=255;
}
}
if(blue<0){
blue=0;
}else{
if(blue>255){
blue=255;
}
}
return new draw2d.Color(red,green,blue);
};
draw2d.Point=function(x,y){
this.x=x;
this.y=y;
};
draw2d.Point.prototype.type="draw2d.Point";
draw2d.Point.prototype.getX=function(){
return this.x;
};
draw2d.Point.prototype.getY=function(){
return this.y;
};
draw2d.Point.prototype.getPosition=function(p){
var dx=p.x-this.x;
var dy=p.y-this.y;
if(Math.abs(dx)>Math.abs(dy)){
if(dx<0){
return draw2d.PositionConstants.WEST;
}
return draw2d.PositionConstants.EAST;
}
if(dy<0){
return draw2d.PositionConstants.NORTH;
}
return draw2d.PositionConstants.SOUTH;
};
draw2d.Point.prototype.equals=function(o){
return this.x==o.x&&this.y==o.y;
};
draw2d.Point.prototype.getDistance=function(other){
return Math.sqrt((this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y));
};
draw2d.Point.prototype.getTranslated=function(other){
return new draw2d.Point(this.x+other.x,this.y+other.y);
};
draw2d.Point.prototype.getPersistentAttributes=function(){
return {x:this.x,y:this.y};
};
draw2d.Dimension=function(x,y,w,h){
draw2d.Point.call(this,x,y);
this.w=w;
this.h=h;
};
draw2d.Dimension.prototype=new draw2d.Point();
draw2d.Dimension.prototype.type="draw2d.Dimension";
draw2d.Dimension.prototype.translate=function(dx,dy){
this.x+=dx;
this.y+=dy;
return this;
};
draw2d.Dimension.prototype.resize=function(dw,dh){
this.w+=dw;
this.h+=dh;
return this;
};
draw2d.Dimension.prototype.setBounds=function(rect){
this.x=rect.x;
this.y=rect.y;
this.w=rect.w;
this.h=rect.h;
return this;
};
draw2d.Dimension.prototype.isEmpty=function(){
return this.w<=0||this.h<=0;
};
draw2d.Dimension.prototype.getWidth=function(){
return this.w;
};
draw2d.Dimension.prototype.getHeight=function(){
return this.h;
};
draw2d.Dimension.prototype.getRight=function(){
return this.x+this.w;
};
draw2d.Dimension.prototype.getBottom=function(){
return this.y+this.h;
};
draw2d.Dimension.prototype.getTopLeft=function(){
return new draw2d.Point(this.x,this.y);
};
draw2d.Dimension.prototype.getCenter=function(){
return new draw2d.Point(this.x+this.w/2,this.y+this.h/2);
};
draw2d.Dimension.prototype.getBottomRight=function(){
return new draw2d.Point(this.x+this.w,this.y+this.h);
};
draw2d.Dimension.prototype.equals=function(o){
return this.x==o.x&&this.y==o.y&&this.w==o.w&&this.h==o.h;
};
draw2d.SnapToHelper=function(_549c){
this.workflow=_549c;
};
draw2d.SnapToHelper.NORTH=1;
draw2d.SnapToHelper.SOUTH=4;
draw2d.SnapToHelper.WEST=8;
draw2d.SnapToHelper.EAST=16;
draw2d.SnapToHelper.CENTER=32;
draw2d.SnapToHelper.NORTH_EAST=draw2d.SnapToHelper.NORTH|draw2d.SnapToHelper.EAST;
draw2d.SnapToHelper.NORTH_WEST=draw2d.SnapToHelper.NORTH|draw2d.SnapToHelper.WEST;
draw2d.SnapToHelper.SOUTH_EAST=draw2d.SnapToHelper.SOUTH|draw2d.SnapToHelper.EAST;
draw2d.SnapToHelper.SOUTH_WEST=draw2d.SnapToHelper.SOUTH|draw2d.SnapToHelper.WEST;
draw2d.SnapToHelper.NORTH_SOUTH=draw2d.SnapToHelper.NORTH|draw2d.SnapToHelper.SOUTH;
draw2d.SnapToHelper.EAST_WEST=draw2d.SnapToHelper.EAST|draw2d.SnapToHelper.WEST;
draw2d.SnapToHelper.NSEW=draw2d.SnapToHelper.NORTH_SOUTH|draw2d.SnapToHelper.EAST_WEST;
draw2d.SnapToHelper.prototype.snapPoint=function(_549d,_549e,_549f){
return _549e;
};
draw2d.SnapToHelper.prototype.snapRectangle=function(_54a0,_54a1){
return _54a0;
};
draw2d.SnapToHelper.prototype.onSetDocumentDirty=function(){
};
draw2d.SnapToGrid=function(_5b23){
draw2d.SnapToHelper.call(this,_5b23);
};
draw2d.SnapToGrid.prototype=new draw2d.SnapToHelper();
draw2d.SnapToGrid.prototype.type="draw2d.SnapToGrid";
draw2d.SnapToGrid.prototype.snapPoint=function(_5b24,_5b25,_5b26){
_5b26.x=this.workflow.gridWidthX*Math.floor(((_5b25.x+this.workflow.gridWidthX/2)/this.workflow.gridWidthX));
_5b26.y=this.workflow.gridWidthY*Math.floor(((_5b25.y+this.workflow.gridWidthY/2)/this.workflow.gridWidthY));
return 0;
};
draw2d.SnapToGrid.prototype.snapRectangle=function(_5b27,_5b28){
_5b28.x=_5b27.x;
_5b28.y=_5b27.y;
_5b28.w=_5b27.w;
_5b28.h=_5b27.h;
return 0;
};
draw2d.SnapToGeometryEntry=function(type,_6251){
this.type=type;
this.location=_6251;
};
draw2d.SnapToGeometryEntry.prototype.getLocation=function(){
return this.location;
};
draw2d.SnapToGeometryEntry.prototype.getType=function(){
return this.type;
};
draw2d.SnapToGeometry=function(_4a02){
draw2d.SnapToHelper.call(this,_4a02);
};
draw2d.SnapToGeometry.prototype=new draw2d.SnapToHelper();
draw2d.SnapToGeometry.THRESHOLD=5;
draw2d.SnapToGeometry.prototype.snapPoint=function(_4a03,_4a04,_4a05){
if(this.rows===null||this.cols===null){
this.populateRowsAndCols();
}
if((_4a03&draw2d.SnapToHelper.EAST)!==0){
var _4a06=this.getCorrectionFor(this.cols,_4a04.getX()-1,1);
if(_4a06!==draw2d.SnapToGeometry.THRESHOLD){
_4a03&=~draw2d.SnapToHelper.EAST;
_4a05.x+=_4a06;
}
}
if((_4a03&draw2d.SnapToHelper.WEST)!==0){
var _4a07=this.getCorrectionFor(this.cols,_4a04.getX(),-1);
if(_4a07!==draw2d.SnapToGeometry.THRESHOLD){
_4a03&=~draw2d.SnapToHelper.WEST;
_4a05.x+=_4a07;
}
}
if((_4a03&draw2d.SnapToHelper.SOUTH)!==0){
var _4a08=this.getCorrectionFor(this.rows,_4a04.getY()-1,1);
if(_4a08!==draw2d.SnapToGeometry.THRESHOLD){
_4a03&=~draw2d.SnapToHelper.SOUTH;
_4a05.y+=_4a08;
}
}
if((_4a03&draw2d.SnapToHelper.NORTH)!==0){
var _4a09=this.getCorrectionFor(this.rows,_4a04.getY(),-1);
if(_4a09!==draw2d.SnapToGeometry.THRESHOLD){
_4a03&=~draw2d.SnapToHelper.NORTH;
_4a05.y+=_4a09;
}
}
return _4a03;
};
draw2d.SnapToGeometry.prototype.snapRectangle=function(_4a0a,_4a0b){
var _4a0c=_4a0a.getTopLeft();
var _4a0d=_4a0a.getBottomRight();
var _4a0e=this.snapPoint(draw2d.SnapToHelper.NORTH_WEST,_4a0a.getTopLeft(),_4a0c);
_4a0b.x=_4a0c.x;
_4a0b.y=_4a0c.y;
var _4a0f=this.snapPoint(draw2d.SnapToHelper.SOUTH_EAST,_4a0a.getBottomRight(),_4a0d);
if(_4a0e&draw2d.SnapToHelper.WEST){
_4a0b.x=_4a0d.x-_4a0a.getWidth();
}
if(_4a0e&draw2d.SnapToHelper.NORTH){
_4a0b.y=_4a0d.y-_4a0a.getHeight();
}
return _4a0e|_4a0f;
};
draw2d.SnapToGeometry.prototype.populateRowsAndCols=function(){
this.rows=[];
this.cols=[];
var _4a10=this.workflow.getDocument().getFigures();
var index=0;
for(var i=0;i<_4a10.getSize();i++){
var _4a13=_4a10.get(i);
if(_4a13!=this.workflow.getCurrentSelection()){
var _4a14=_4a13.getBounds();
this.cols[index*3]=new draw2d.SnapToGeometryEntry(-1,_4a14.getX());
this.rows[index*3]=new draw2d.SnapToGeometryEntry(-1,_4a14.getY());
this.cols[index*3+1]=new draw2d.SnapToGeometryEntry(0,_4a14.x+(_4a14.getWidth()-1)/2);
this.rows[index*3+1]=new draw2d.SnapToGeometryEntry(0,_4a14.y+(_4a14.getHeight()-1)/2);
this.cols[index*3+2]=new draw2d.SnapToGeometryEntry(1,_4a14.getRight()-1);
this.rows[index*3+2]=new draw2d.SnapToGeometryEntry(1,_4a14.getBottom()-1);
index++;
}
}
};
draw2d.SnapToGeometry.prototype.getCorrectionFor=function(_4a15,value,side){
var _4a18=draw2d.SnapToGeometry.THRESHOLD;
var _4a19=draw2d.SnapToGeometry.THRESHOLD;
for(var i=0;i<_4a15.length;i++){
var entry=_4a15[i];
var _4a1c;
if(entry.type===-1&&side!==0){
_4a1c=Math.abs(value-entry.location);
if(_4a1c<_4a18){
_4a18=_4a1c;
_4a19=entry.location-value;
}
}else{
if(entry.type===0&&side===0){
_4a1c=Math.abs(value-entry.location);
if(_4a1c<_4a18){
_4a18=_4a1c;
_4a19=entry.location-value;
}
}else{
if(entry.type===1&&side!==0){
_4a1c=Math.abs(value-entry.location);
if(_4a1c<_4a18){
_4a18=_4a1c;
_4a19=entry.location-value;
}
}
}
}
}
return _4a19;
};
draw2d.SnapToGeometry.prototype.onSetDocumentDirty=function(){
this.rows=null;
this.cols=null;
};
draw2d.Border=function(){
this.color=null;
};
draw2d.Border.prototype.type="draw2d.Border";
draw2d.Border.prototype.dispose=function(){
this.color=null;
};
draw2d.Border.prototype.getHTMLStyle=function(){
return "";
};
draw2d.Border.prototype.setColor=function(c){
this.color=c;
};
draw2d.Border.prototype.getColor=function(){
return this.color;
};
draw2d.Border.prototype.refresh=function(){
};
draw2d.LineBorder=function(width){
draw2d.Border.call(this);
this.width=1;
if(width){
this.width=width;
}
this.figure=null;
};
draw2d.LineBorder.prototype=new draw2d.Border();
draw2d.LineBorder.prototype.type="draw2d.LineBorder";
draw2d.LineBorder.prototype.dispose=function(){
draw2d.Border.prototype.dispose.call(this);
this.figure=null;
};
draw2d.LineBorder.prototype.setLineWidth=function(w){
this.width=w;
if(this.figure!==null){
this.figure.html.style.border=this.getHTMLStyle();
}
};
draw2d.LineBorder.prototype.getHTMLStyle=function(){
if(this.getColor()!==null){
return this.width+"px solid "+this.getColor().getHTMLStyle();
}
return this.width+"px solid black";
};
draw2d.LineBorder.prototype.refresh=function(){
this.setLineWidth(this.width);
};
draw2d.Figure=function(){
this.construct();
};
draw2d.Figure.prototype.type="draw2d.Figure";
draw2d.Figure.ZOrderBaseIndex=100;
draw2d.Figure.setZOrderBaseIndex=function(index){
draw2d.Figure.ZOrderBaseIndex=index;
};
draw2d.Figure.prototype.construct=function(){
this.lastDragStartTime=0;
this.x=0;
this.y=0;
this.width=10;
this.height=10;
this.border=null;
this.id=draw2d.UUID.create();
this.html=this.createHTMLElement();
this.canvas=null;
this.workflow=null;
this.draggable=null;
this.parent=null;
this.isMoving=false;
this.canSnapToHelper=true;
this.snapToGridAnchor=new draw2d.Point(0,0);
this.timer=-1;
this.model=null;
this.properties={};
this.moveListener=new draw2d.ArrayList();
this.setDimension(10,10);
this.setDeleteable(true);
this.setCanDrag(true);
this.setResizeable(true);
this.setSelectable(true);
};
draw2d.Figure.prototype.dispose=function(){
this.canvas=null;
this.workflow=null;
this.moveListener=null;
if(this.draggable!==null){
this.draggable.removeEventListener("mouseenter",this.tmpMouseEnter);
this.draggable.removeEventListener("mouseleave",this.tmpMouseLeave);
this.draggable.removeEventListener("dragend",this.tmpDragend);
this.draggable.removeEventListener("dragstart",this.tmpDragstart);
this.draggable.removeEventListener("drag",this.tmpDrag);
this.draggable.removeEventListener("dblclick",this.tmpDoubleClick);
this.draggable.node=null;
this.draggable.target.removeAllElements();
}
this.draggable=null;
if(this.border!==null){
this.border.dispose();
}
this.border=null;
if(this.parent!==null){
this.parent.removeChild(this);
}
};
draw2d.Figure.prototype.getProperties=function(){
return this.properties;
};
draw2d.Figure.prototype.getProperty=function(key){
return this.properties[key];
};
draw2d.Figure.prototype.setProperty=function(key,value){
this.properties[key]=value;
this.setDocumentDirty();
};
draw2d.Figure.prototype.getId=function(){
return this.id;
};
draw2d.Figure.prototype.setId=function(id){
this.id=id;
if(this.html!==null){
this.html.id=id;
}
};
draw2d.Figure.prototype.setCanvas=function(_58a0){
this.canvas=_58a0;
};
draw2d.Figure.prototype.getWorkflow=function(){
return this.workflow;
};
draw2d.Figure.prototype.setWorkflow=function(_58a1){
if(this.draggable===null){
this.html.tabIndex="0";
var oThis=this;
this.keyDown=function(event){
event.cancelBubble=true;
event.returnValue=true;
oThis.onKeyDown(event.keyCode,event.ctrlKey);
};
if(this.html.addEventListener){
this.html.addEventListener("keydown",this.keyDown,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("onkeydown",this.keyDown);
}
}
this.draggable=new draw2d.Draggable(this.html,draw2d.Draggable.DRAG_X|draw2d.Draggable.DRAG_Y);
this.draggable.node=this;
this.tmpContextMenu=function(_58a4){
oThis.onContextMenu(oThis.x+_58a4.x,_58a4.y+oThis.y);
};
this.tmpMouseEnter=function(_58a5){
oThis.onMouseEnter();
};
this.tmpMouseLeave=function(_58a6){
oThis.onMouseLeave();
};
this.tmpDragend=function(_58a7){
oThis.onDragend();
};
this.tmpDragstart=function(_58a8){
var w=oThis.workflow;
w.showMenu(null);
if(w.toolPalette&&w.toolPalette.activeTool){
_58a8.returnValue=false;
w.onMouseDown(oThis.x+_58a8.x,_58a8.y+oThis.y);
w.onMouseUp(oThis.x+_58a8.x,_58a8.y+oThis.y);
return;
}
if(!(oThis instanceof draw2d.ResizeHandle)&&!(oThis instanceof draw2d.Port)){
var line=w.getBestLine(oThis.x+_58a8.x,_58a8.y+oThis.y);
if(line!==null){
_58a8.returnValue=false;
w.setCurrentSelection(line);
w.showLineResizeHandles(line);
w.onMouseDown(oThis.x+_58a8.x,_58a8.y+oThis.y);
return;
}else{
if(oThis.isSelectable()){
w.showResizeHandles(oThis);
w.setCurrentSelection(oThis);
}
}
}
_58a8.returnValue=oThis.onDragstart(_58a8.x,_58a8.y);
};
this.tmpDrag=function(_58ab){
oThis.onDrag();
};
this.tmpDoubleClick=function(_58ac){
oThis.onDoubleClick();
};
this.draggable.addEventListener("contextmenu",this.tmpContextMenu);
this.draggable.addEventListener("mouseenter",this.tmpMouseEnter);
this.draggable.addEventListener("mouseleave",this.tmpMouseLeave);
this.draggable.addEventListener("dragend",this.tmpDragend);
this.draggable.addEventListener("dragstart",this.tmpDragstart);
this.draggable.addEventListener("drag",this.tmpDrag);
this.draggable.addEventListener("dblclick",this.tmpDoubleClick);
}
this.workflow=_58a1;
};
draw2d.Figure.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height=this.width+"px";
item.style.width=this.height+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.outline="none";
item.style.zIndex=""+draw2d.Figure.ZOrderBaseIndex;
return item;
};
draw2d.Figure.prototype.setParent=function(_58ae){
this.parent=_58ae;
};
draw2d.Figure.prototype.getParent=function(){
return this.parent;
};
draw2d.Figure.prototype.getZOrder=function(){
return this.html.style.zIndex;
};
draw2d.Figure.prototype.setZOrder=function(index){
this.html.style.zIndex=index;
};
draw2d.Figure.prototype.hasFixedPosition=function(){
return false;
};
draw2d.Figure.prototype.getMinWidth=function(){
return 5;
};
draw2d.Figure.prototype.getMinHeight=function(){
return 5;
};
draw2d.Figure.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
draw2d.Figure.prototype.paint=function(){
};
draw2d.Figure.prototype.setBorder=function(_58b0){
if(this.border!==null){
this.border.figure=null;
}
this.border=_58b0;
this.border.figure=this;
this.border.refresh();
this.setDocumentDirty();
};
draw2d.Figure.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
this.workflow.showMenu(menu,x,y);
}
};
draw2d.Figure.prototype.getContextMenu=function(){
return null;
};
draw2d.Figure.prototype.onDoubleClick=function(){
};
draw2d.Figure.prototype.onMouseEnter=function(){
};
draw2d.Figure.prototype.onMouseLeave=function(){
};
draw2d.Figure.prototype.onDrag=function(){
this.x=this.draggable.getLeft();
this.y=this.draggable.getTop();
if(this.isMoving==false){
this.isMoving=true;
this.setAlpha(0.5);
}
this.fireMoveEvent();
};
draw2d.Figure.prototype.onDragend=function(){
if(this.getWorkflow().getEnableSmoothFigureHandling()==true){
var _58b4=this;
var _58b5=function(){
if(_58b4.alpha<1){
_58b4.setAlpha(Math.min(1,_58b4.alpha+0.05));
}else{
window.clearInterval(_58b4.timer);
_58b4.timer=-1;
}
};
if(_58b4.timer>0){
window.clearInterval(_58b4.timer);
}
_58b4.timer=window.setInterval(_58b5,20);
}else{
this.setAlpha(1);
}
this.command.setPosition(this.x,this.y);
this.workflow.commandStack.execute(this.command);
this.command=null;
this.isMoving=false;
this.workflow.hideSnapToHelperLines();
this.fireMoveEvent();
};
draw2d.Figure.prototype.onDragstart=function(x,y){
this.command=this.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.MOVE));
return this.command!==null;
};
draw2d.Figure.prototype.setCanDrag=function(flag){
this.canDrag=flag;
if(flag){
this.html.style.cursor="move";
}else{
this.html.style.cursor="";
}
};
draw2d.Figure.prototype.getCanDrag=function(){
return this.canDrag;
};
draw2d.Figure.prototype.setAlpha=function(_58b9){
if(this.alpha==_58b9){
return;
}
try{
this.html.style.MozOpacity=_58b9;
}
catch(exc){
}
try{
this.html.style.opacity=_58b9;
}
catch(exc){
}
try{
var _58ba=Math.round(_58b9*100);
if(_58ba>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_58ba+")";
}
}
catch(exc){
}
this.alpha=_58b9;
};
draw2d.Figure.prototype.setDimension=function(w,h){
this.width=Math.max(this.getMinWidth(),w);
this.height=Math.max(this.getMinHeight(),h);
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
this.fireMoveEvent();
if(this.workflow!==null&&this.workflow.getCurrentSelection()==this){
this.workflow.showResizeHandles(this);
}
};
draw2d.Figure.prototype.setPosition=function(xPos,yPos){
this.x=xPos;
this.y=yPos;
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
this.fireMoveEvent();
if(this.workflow!==null&&this.workflow.getCurrentSelection()==this){
this.workflow.showResizeHandles(this);
}
};
draw2d.Figure.prototype.isResizeable=function(){
return this.resizeable;
};
draw2d.Figure.prototype.setResizeable=function(flag){
this.resizeable=flag;
};
draw2d.Figure.prototype.isSelectable=function(){
return this.selectable;
};
draw2d.Figure.prototype.setSelectable=function(flag){
this.selectable=flag;
};
draw2d.Figure.prototype.isStrechable=function(){
return true;
};
draw2d.Figure.prototype.isDeleteable=function(){
return this.deleteable;
};
draw2d.Figure.prototype.setDeleteable=function(flag){
this.deleteable=flag;
};
draw2d.Figure.prototype.setCanSnapToHelper=function(flag){
this.canSnapToHelper=flag;
};
draw2d.Figure.prototype.getCanSnapToHelper=function(){
return this.canSnapToHelper;
};
draw2d.Figure.prototype.getSnapToGridAnchor=function(){
return this.snapToGridAnchor;
};
draw2d.Figure.prototype.setSnapToGridAnchor=function(point){
this.snapToGridAnchor=point;
};
draw2d.Figure.prototype.getBounds=function(){
return new draw2d.Dimension(this.getX(),this.getY(),this.getWidth(),this.getHeight());
};
draw2d.Figure.prototype.getWidth=function(){
return this.width;
};
draw2d.Figure.prototype.getHeight=function(){
return this.height;
};
draw2d.Figure.prototype.getY=function(){
return this.y;
};
draw2d.Figure.prototype.getX=function(){
return this.x;
};
draw2d.Figure.prototype.getAbsoluteY=function(){
return this.y;
};
draw2d.Figure.prototype.getAbsoluteX=function(){
return this.x;
};
draw2d.Figure.prototype.onKeyDown=function(_58c4,ctrl){
if(_58c4==46){
this.workflow.getCommandStack().execute(this.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.DELETE)));
}
if(ctrl){
this.workflow.onKeyDown(_58c4,ctrl);
}
};
draw2d.Figure.prototype.getPosition=function(){
return new draw2d.Point(this.x,this.y);
};
draw2d.Figure.prototype.isOver=function(iX,iY){
var x=this.getAbsoluteX();
var y=this.getAbsoluteY();
var iX2=x+this.width;
var iY2=y+this.height;
return (iX>=x&&iX<=iX2&&iY>=y&&iY<=iY2);
};
draw2d.Figure.prototype.attachMoveListener=function(_58cc){
if(_58cc===null||this.moveListener===null){
return;
}
this.moveListener.add(_58cc);
};
draw2d.Figure.prototype.detachMoveListener=function(_58cd){
if(_58cd===null||this.moveListener===null){
return;
}
this.moveListener.remove(_58cd);
};
draw2d.Figure.prototype.fireMoveEvent=function(){
this.setDocumentDirty();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
draw2d.Figure.prototype.setModel=function(model){
if(this.model!==null){
this.model.removePropertyChangeListener(this);
}
this.model=model;
if(this.model!==null){
this.model.addPropertyChangeListener(this);
}
};
draw2d.Figure.prototype.getModel=function(){
return this.model;
};
draw2d.Figure.prototype.onOtherFigureMoved=function(_58d1){
};
draw2d.Figure.prototype.setDocumentDirty=function(){
if(this.workflow!==null){
this.workflow.setDocumentDirty();
}
};
draw2d.Figure.prototype.disableTextSelection=function(_58d2){
_58d2.onselectstart=function(){
return false;
};
_58d2.unselectable="on";
_58d2.style.MozUserSelect="none";
};
draw2d.Figure.prototype.createCommand=function(_58d3){
if(_58d3.getPolicy()==draw2d.EditPolicy.MOVE){
if(!this.canDrag){
return null;
}
return new draw2d.CommandMove(this);
}
if(_58d3.getPolicy()==draw2d.EditPolicy.DELETE){
if(!this.isDeleteable()){
return null;
}
return new draw2d.CommandDelete(this);
}
if(_58d3.getPolicy()==draw2d.EditPolicy.RESIZE){
if(!this.isResizeable()){
return null;
}
return new draw2d.CommandResize(this);
}
return null;
};
draw2d.Node=function(){
this.bgColor=null;
this.lineColor=new draw2d.Color(128,128,255);
this.lineStroke=1;
this.ports=new draw2d.ArrayList();
draw2d.Figure.call(this);
};
draw2d.Node.prototype=new draw2d.Figure();
draw2d.Node.prototype.type="draw2d.Node";
draw2d.Node.prototype.dispose=function(){
for(var i=0;i<this.ports.getSize();i++){
this.ports.get(i).dispose();
}
this.ports=null;
draw2d.Figure.prototype.dispose.call(this);
};
draw2d.Node.prototype.createHTMLElement=function(){
var item=draw2d.Figure.prototype.createHTMLElement.call(this);
item.style.width="auto";
item.style.height="auto";
item.style.margin="0px";
item.style.padding="0px";
if(this.lineColor!==null){
item.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}
item.style.fontSize="1px";
if(this.bgColor!==null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
draw2d.Node.prototype.paint=function(){
draw2d.Figure.prototype.paint.call(this);
for(var i=0;i<this.ports.getSize();i++){
this.ports.get(i).paint();
}
};
draw2d.Node.prototype.getPorts=function(){
return this.ports;
};
draw2d.Node.prototype.getInputPorts=function(){
var _5d76=new draw2d.ArrayList();
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port instanceof draw2d.InputPort){
_5d76.add(port);
}
}
return _5d76;
};
draw2d.Node.prototype.getOutputPorts=function(){
var _5d79=new draw2d.ArrayList();
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port instanceof draw2d.OutputPort){
_5d79.add(port);
}
}
return _5d79;
};
draw2d.Node.prototype.getPort=function(_5d7c){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_5d7c){
return port;
}
}
};
draw2d.Node.prototype.getInputPort=function(_5d7f){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_5d7f&&port instanceof draw2d.InputPort){
return port;
}
}
};
draw2d.Node.prototype.getOutputPort=function(_5d82){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_5d82&&port instanceof draw2d.OutputPort){
return port;
}
}
};
draw2d.Node.prototype.addPort=function(port,x,y){
this.ports.add(port);
port.setOrigin(x,y);
port.setPosition(x,y);
port.setParent(this);
port.setDeleteable(false);
this.html.appendChild(port.getHTMLElement());
if(this.workflow!==null){
this.workflow.registerPort(port);
}
};
draw2d.Node.prototype.removePort=function(port){
if(this.ports!==null){
this.ports.remove(port);
}
try{
this.html.removeChild(port.getHTMLElement());
}
catch(exc){
}
if(this.workflow!==null){
this.workflow.unregisterPort(port);
}
var _5d89=port.getConnections();
for(var i=0;i<_5d89.getSize();++i){
this.workflow.removeFigure(_5d89.get(i));
}
};
draw2d.Node.prototype.setWorkflow=function(_5d8b){
var _5d8c=this.workflow;
draw2d.Figure.prototype.setWorkflow.call(this,_5d8b);
if(_5d8c!==null){
for(var i=0;i<this.ports.getSize();i++){
_5d8c.unregisterPort(this.ports.get(i));
}
}
if(this.workflow!==null){
for(var i=0;i<this.ports.getSize();i++){
this.workflow.registerPort(this.ports.get(i));
}
}
};
draw2d.Node.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
draw2d.Node.prototype.getBackgroundColor=function(){
return this.bgColor;
};
draw2d.Node.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
draw2d.Node.prototype.setLineWidth=function(w){
this.lineStroke=w;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
draw2d.Node.prototype.getModelSourceConnections=function(){
throw "You must override the method [Node.prototype.getModelSourceConnections]";
};
draw2d.Node.prototype.refreshConnections=function(){
if(this.workflow!==null){
this.workflow.refreshConnections(this);
}
};
draw2d.VectorFigure=function(){
this.bgColor=null;
this.lineColor=new draw2d.Color(0,0,0);
this.stroke=1;
this.graphics=null;
draw2d.Node.call(this);
};
draw2d.VectorFigure.prototype=new draw2d.Node;
draw2d.VectorFigure.prototype.type="draw2d.VectorFigure";
draw2d.VectorFigure.prototype.dispose=function(){
draw2d.Node.prototype.dispose.call(this);
this.bgColor=null;
this.lineColor=null;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
draw2d.VectorFigure.prototype.createHTMLElement=function(){
var item=draw2d.Node.prototype.createHTMLElement.call(this);
item.style.border="0px";
item.style.backgroundColor="transparent";
return item;
};
draw2d.VectorFigure.prototype.setWorkflow=function(_4fcc){
draw2d.Node.prototype.setWorkflow.call(this,_4fcc);
if(this.workflow===null){
this.graphics.clear();
this.graphics=null;
}
};
draw2d.VectorFigure.prototype.paint=function(){
if(this.html===null){
return;
}
try{
if(this.graphics===null){
this.graphics=new jsGraphics(this.html);
}else{
this.graphics.clear();
}
draw2d.Node.prototype.paint.call(this);
for(var i=0;i<this.ports.getSize();i++){
this.getHTMLElement().appendChild(this.ports.get(i).getHTMLElement());
}
}
catch(e){
pushErrorStack(e,"draw2d.VectorFigure.prototype.paint=function()["+area+"]");
}
};
draw2d.VectorFigure.prototype.setDimension=function(w,h){
draw2d.Node.prototype.setDimension.call(this,w,h);
if(this.graphics!==null){
this.paint();
}
};
draw2d.VectorFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!==null){
this.paint();
}
};
draw2d.VectorFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
draw2d.VectorFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!==null){
this.paint();
}
};
draw2d.VectorFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!==null){
this.paint();
}
};
draw2d.VectorFigure.prototype.getColor=function(){
return this.lineColor;
};
draw2d.SVGFigure=function(width,_58d8){
this.bgColor=null;
this.lineColor=new draw2d.Color(0,0,0);
this.stroke=1;
this.context=null;
draw2d.Node.call(this);
if(width&&_58d8){
this.setDimension(width,_58d8);
}
};
draw2d.SVGFigure.prototype=new draw2d.Node();
draw2d.SVGFigure.prototype.type="draw2d.SVGFigure";
draw2d.SVGFigure.prototype.createHTMLElement=function(){
var item=new MooCanvas(this.id,{width:100,height:100});
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.zIndex=""+draw2d.Figure.ZOrderBaseIndex;
this.context=item.getContext("2d");
return item;
};
draw2d.SVGFigure.prototype.paint=function(){
this.context.clearRect(0,0,this.getWidth(),this.getHeight());
this.context.fillStyle="rgba(200,0,0,0.3)";
this.context.fillRect(0,0,this.getWidth(),this.getHeight());
};
draw2d.SVGFigure.prototype.setDimension=function(w,h){
draw2d.Node.prototype.setDimension.call(this,w,h);
this.html.width=w;
this.html.height=h;
this.html.style.width=w+"px";
this.html.style.height=h+"px";
if(this.context!==null){
if(this.context.element){
this.context.element.style.width=w+"px";
this.context.element.style.height=h+"px";
}
this.paint();
}
};
draw2d.SVGFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!==null){
this.paint();
}
};
draw2d.SVGFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
draw2d.SVGFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.context!==null){
this.paint();
}
};
draw2d.SVGFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.context!==null){
this.paint();
}
};
draw2d.SVGFigure.prototype.getColor=function(){
return this.lineColor;
};
draw2d.Label=function(msg){
this.msg=msg;
this.bgColor=null;
this.color=new draw2d.Color(0,0,0);
this.fontSize=10;
this.textNode=null;
this.align="center";
draw2d.Figure.call(this);
};
draw2d.Label.prototype=new draw2d.Figure();
draw2d.Label.prototype.type="draw2d.Label";
draw2d.Label.prototype.createHTMLElement=function(){
var item=draw2d.Figure.prototype.createHTMLElement.call(this);
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
item.style.color=this.color.getHTMLStyle();
item.style.fontSize=this.fontSize+"pt";
item.style.width="auto";
item.style.height="auto";
item.style.paddingLeft="3px";
item.style.paddingRight="3px";
item.style.textAlign=this.align;
item.style.MozUserSelect="none";
this.disableTextSelection(item);
if(this.bgColor!==null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
draw2d.Label.prototype.isResizeable=function(){
return false;
};
draw2d.Label.prototype.setWordwrap=function(flag){
this.html.style.whiteSpace=flag?"wrap":"nowrap";
};
draw2d.Label.prototype.setAlign=function(align){
this.align=align;
this.html.style.textAlign=align;
};
draw2d.Label.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
draw2d.Label.prototype.setColor=function(color){
this.color=color;
this.html.style.color=this.color.getHTMLStyle();
};
draw2d.Label.prototype.setFontSize=function(size){
this.fontSize=size;
this.html.style.fontSize=this.fontSize+"pt";
};
draw2d.Label.prototype.getWidth=function(){
if(window.getComputedStyle){
return parseInt(getComputedStyle(this.html,"").getPropertyValue("width"));
}
return parseInt(this.html.clientWidth);
};
draw2d.Label.prototype.getHeight=function(){
if(window.getComputedStyle){
return parseInt(getComputedStyle(this.html,"").getPropertyValue("height"));
}
return parseInt(this.html.clientHeight);
};
draw2d.Label.prototype.getText=function(){
return this.msg;
};
draw2d.Label.prototype.setText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createTextNode(this.msg);
this.html.appendChild(this.textNode);
};
draw2d.Label.prototype.setStyledText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createElement("div");
this.textNode.style.whiteSpace="nowrap";
this.textNode.innerHTML=text;
this.html.appendChild(this.textNode);
};
draw2d.Oval=function(){
draw2d.VectorFigure.call(this);
};
draw2d.Oval.prototype=new draw2d.VectorFigure();
draw2d.Oval.prototype.type="draw2d.Oval";
draw2d.Oval.prototype.paint=function(){
if(this.html===null){
return;
}
try{
draw2d.VectorFigure.prototype.paint.call(this);
this.graphics.setStroke(this.stroke);
if(this.bgColor!==null){
this.graphics.setColor(this.bgColor.getHTMLStyle());
this.graphics.fillOval(0,0,this.getWidth()-1,this.getHeight()-1);
}
if(this.lineColor!==null){
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.graphics.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);
}
this.graphics.paint();
}
catch(e){
pushErrorStack(e,"draw2d.Oval.prototype.paint=function()");
}
};
draw2d.Circle=function(_58e7){
draw2d.Oval.call(this);
if(_58e7){
this.setDimension(_58e7,_58e7);
}
};
draw2d.Circle.prototype=new draw2d.Oval();
draw2d.Circle.prototype.type="draw2d.Circle";
draw2d.Circle.prototype.setDimension=function(w,h){
if(w>h){
draw2d.Oval.prototype.setDimension.call(this,w,w);
}else{
draw2d.Oval.prototype.setDimension.call(this,h,h);
}
};
draw2d.Circle.prototype.isStrechable=function(){
return false;
};
draw2d.Rectangle=function(width,_4cdc){
this.bgColor=null;
this.lineColor=new draw2d.Color(0,0,0);
this.lineStroke=1;
draw2d.Figure.call(this);
if(width&&_4cdc){
this.setDimension(width,_4cdc);
}
};
draw2d.Rectangle.prototype=new draw2d.Figure();
draw2d.Rectangle.prototype.type="draw2d.Rectangle";
draw2d.Rectangle.prototype.dispose=function(){
draw2d.Figure.prototype.dispose.call(this);
this.bgColor=null;
this.lineColor=null;
};
draw2d.Rectangle.prototype.createHTMLElement=function(){
var item=draw2d.Figure.prototype.createHTMLElement.call(this);
item.style.width="auto";
item.style.height="auto";
item.style.margin="0px";
item.style.padding="0px";
item.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
item.style.fontSize="1px";
item.style.lineHeight="1px";
item.innerHTML="&nbsp";
if(this.bgColor!==null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
draw2d.Rectangle.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
draw2d.Rectangle.prototype.getBackgroundColor=function(){
return this.bgColor;
};
draw2d.Rectangle.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border=this.lineStroke+"0px";
}
};
draw2d.Rectangle.prototype.getColor=function(){
return this.lineColor;
};
draw2d.Rectangle.prototype.getWidth=function(){
return draw2d.Figure.prototype.getWidth.call(this)+2*this.lineStroke;
};
draw2d.Rectangle.prototype.getHeight=function(){
return draw2d.Figure.prototype.getHeight.call(this)+2*this.lineStroke;
};
draw2d.Rectangle.prototype.setDimension=function(w,h){
return draw2d.Figure.prototype.setDimension.call(this,w-2*this.lineStroke,h-2*this.lineStroke);
};
draw2d.Rectangle.prototype.setLineWidth=function(w){
var diff=w-this.lineStroke;
this.setDimension(this.getWidth()-2*diff,this.getHeight()-2*diff);
this.lineStroke=w;
var c="transparent";
if(this.lineColor!==null){
c=this.lineColor.getHTMLStyle();
}
this.html.style.border=this.lineStroke+"px solid "+c;
};
draw2d.Rectangle.prototype.getLineWidth=function(){
return this.lineStroke;
};
draw2d.ImageFigure=function(url){
if(url===undefined){
url=null;
}
this.url=url;
draw2d.Node.call(this);
this.setDimension(40,40);
};
draw2d.ImageFigure.prototype=new draw2d.Node;
draw2d.ImageFigure.prototype.type="draw2d.Image";
draw2d.ImageFigure.prototype.createHTMLElement=function(){
var item=draw2d.Node.prototype.createHTMLElement.call(this);
item.style.width=this.width+"px";
item.style.height=this.height+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.border="0px";
if(this.url!==null){
item.style.backgroundImage="url("+this.url+")";
}else{
item.style.backgroundImage="";
}
return item;
};
draw2d.ImageFigure.prototype.setColor=function(color){
};
draw2d.ImageFigure.prototype.isResizeable=function(){
return false;
};
draw2d.ImageFigure.prototype.setImage=function(url){
if(url===undefined){
url=null;
}
this.url=url;
if(this.url!==null){
this.html.style.backgroundImage="url("+this.url+")";
}else{
this.html.style.backgroundImage="";
}
};
draw2d.Port=function(_4b91,_4b92){
Corona=function(){
};
Corona.prototype=new draw2d.Circle();
Corona.prototype.setAlpha=function(_4b93){
draw2d.Circle.prototype.setAlpha.call(this,Math.min(0.3,_4b93));
this.setDeleteable(false);
this.setCanDrag(false);
this.setResizeable(false);
this.setSelectable(false);
};
if(_4b91===null||_4b91===undefined){
this.currentUIRepresentation=new draw2d.Circle();
}else{
this.currentUIRepresentation=_4b91;
}
if(_4b92===null||_4b92===undefined){
this.connectedUIRepresentation=new draw2d.Circle();
this.connectedUIRepresentation.setColor(null);
}else{
this.connectedUIRepresentation=_4b92;
}
this.disconnectedUIRepresentation=this.currentUIRepresentation;
this.hideIfConnected=false;
this.uiRepresentationAdded=true;
this.parentNode=null;
this.originX=0;
this.originY=0;
this.coronaWidth=10;
this.corona=null;
draw2d.Rectangle.call(this);
this.setDimension(8,8);
this.setBackgroundColor(new draw2d.Color(100,180,100));
this.setColor(new draw2d.Color(90,150,90));
draw2d.Rectangle.prototype.setColor.call(this,null);
this.dropable=new draw2d.DropTarget(this.html);
this.dropable.node=this;
this.dropable.addEventListener("dragenter",function(_4b94){
_4b94.target.node.onDragEnter(_4b94.relatedTarget.node);
});
this.dropable.addEventListener("dragleave",function(_4b95){
_4b95.target.node.onDragLeave(_4b95.relatedTarget.node);
});
this.dropable.addEventListener("drop",function(_4b96){
_4b96.relatedTarget.node.onDrop(_4b96.target.node);
});
};
draw2d.Port.prototype=new draw2d.Rectangle();
draw2d.Port.prototype.type="draw2d.Port";
draw2d.Port.ZOrderBaseIndex=5000;
draw2d.Port.setZOrderBaseIndex=function(index){
draw2d.Port.ZOrderBaseIndex=index;
};
draw2d.Port.prototype.setHideIfConnected=function(flag){
this.hideIfConnected=flag;
};
draw2d.Port.prototype.dispose=function(){
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
var _4b9b=this.moveListener.get(i);
this.parentNode.workflow.removeFigure(_4b9b);
_4b9b.dispose();
}
draw2d.Rectangle.prototype.dispose.call(this);
this.parentNode=null;
this.dropable.node=null;
this.dropable=null;
this.disconnectedUIRepresentation.dispose();
this.connectedUIRepresentation.dispose();
};
draw2d.Port.prototype.createHTMLElement=function(){
var item=draw2d.Rectangle.prototype.createHTMLElement.call(this);
item.style.zIndex=draw2d.Port.ZOrderBaseIndex;
this.currentUIRepresentation.html.zIndex=draw2d.Port.ZOrderBaseIndex;
item.appendChild(this.currentUIRepresentation.html);
this.uiRepresentationAdded=true;
return item;
};
draw2d.Port.prototype.setUiRepresentation=function(_4b9d){
if(_4b9d===null){
_4b9d=new draw2d.Figure();
}
if(this.uiRepresentationAdded){
this.html.removeChild(this.currentUIRepresentation.getHTMLElement());
}
this.html.appendChild(_4b9d.getHTMLElement());
_4b9d.paint();
this.currentUIRepresentation=_4b9d;
};
draw2d.Port.prototype.onMouseEnter=function(){
this.setLineWidth(2);
};
draw2d.Port.prototype.onMouseLeave=function(){
this.setLineWidth(0);
};
draw2d.Port.prototype.setDimension=function(width,_4b9f){
draw2d.Rectangle.prototype.setDimension.call(this,width,_4b9f);
this.connectedUIRepresentation.setDimension(width,_4b9f);
this.disconnectedUIRepresentation.setDimension(width,_4b9f);
this.setPosition(this.x,this.y);
};
draw2d.Port.prototype.setBackgroundColor=function(color){
this.currentUIRepresentation.setBackgroundColor(color);
};
draw2d.Port.prototype.getBackgroundColor=function(){
return this.currentUIRepresentation.getBackgroundColor();
};
draw2d.Port.prototype.getConnections=function(){
var _4ba1=new draw2d.ArrayList();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
var _4ba4=this.moveListener.get(i);
if(_4ba4 instanceof draw2d.Connection){
_4ba1.add(_4ba4);
}
}
return _4ba1;
};
draw2d.Port.prototype.setColor=function(color){
this.currentUIRepresentation.setColor(color);
};
draw2d.Port.prototype.getColor=function(){
return this.currentUIRepresentation.getColor();
};
draw2d.Port.prototype.setLineWidth=function(width){
this.currentUIRepresentation.setLineWidth(width);
};
draw2d.Port.prototype.getLineWidth=function(){
return this.currentUIRepresentation.getLineWidth();
};
draw2d.Port.prototype.paint=function(){
try{
this.currentUIRepresentation.paint();
}
catch(e){
pushErrorStack(e,"draw2d.Port.prototype.paint=function()");
}
};
draw2d.Port.prototype.setPosition=function(xPos,yPos){
this.originX=xPos;
this.originY=yPos;
draw2d.Rectangle.prototype.setPosition.call(this,xPos,yPos);
if(this.html===null){
return;
}
this.html.style.left=(this.x-this.getWidth()/2)+"px";
this.html.style.top=(this.y-this.getHeight()/2)+"px";
};
draw2d.Port.prototype.setParent=function(_4ba9){
if(this.parentNode!==null){
this.parentNode.detachMoveListener(this);
}
this.parentNode=_4ba9;
if(this.parentNode!==null){
this.parentNode.attachMoveListener(this);
}
};
draw2d.Port.prototype.attachMoveListener=function(_4baa){
draw2d.Rectangle.prototype.attachMoveListener.call(this,_4baa);
if(this.hideIfConnected==true){
this.setUiRepresentation(this.connectedUIRepresentation);
}
};
draw2d.Port.prototype.detachMoveListener=function(_4bab){
draw2d.Rectangle.prototype.detachMoveListener.call(this,_4bab);
if(this.getConnections().getSize()==0){
this.setUiRepresentation(this.disconnectedUIRepresentation);
}
};
draw2d.Port.prototype.getParent=function(){
return this.parentNode;
};
draw2d.Port.prototype.onDrag=function(){
draw2d.Rectangle.prototype.onDrag.call(this);
this.parentNode.workflow.showConnectionLine(this.parentNode.x+this.x,this.parentNode.y+this.y,this.parentNode.x+this.originX,this.parentNode.y+this.originY);
};
draw2d.Port.prototype.getCoronaWidth=function(){
return this.coronaWidth;
};
draw2d.Port.prototype.setCoronaWidth=function(width){
this.coronaWidth=width;
};
draw2d.Port.prototype.setOrigin=function(x,y){
this.originX=x;
this.originY=y;
};
draw2d.Port.prototype.onDragend=function(){
this.setAlpha(1);
this.setPosition(this.originX,this.originY);
this.parentNode.workflow.hideConnectionLine();
};
draw2d.Port.prototype.onDragEnter=function(port){
var _4bb0=new draw2d.EditPolicy(draw2d.EditPolicy.CONNECT);
_4bb0.canvas=this.parentNode.workflow;
_4bb0.source=port;
_4bb0.target=this;
var _4bb1=this.createCommand(_4bb0);
if(_4bb1===null){
return;
}
this.parentNode.workflow.connectionLine.setColor(new draw2d.Color(0,150,0));
this.parentNode.workflow.connectionLine.setLineWidth(3);
this.showCorona(true);
};
draw2d.Port.prototype.onDragLeave=function(port){
this.parentNode.workflow.connectionLine.setColor(new draw2d.Color(0,0,0));
this.parentNode.workflow.connectionLine.setLineWidth(1);
this.showCorona(false);
};
draw2d.Port.prototype.onDrop=function(port){
var _4bb4=new draw2d.EditPolicy(draw2d.EditPolicy.CONNECT);
_4bb4.canvas=this.parentNode.workflow;
_4bb4.source=port;
_4bb4.target=this;
var _4bb5=this.createCommand(_4bb4);
if(_4bb5!==null){
this.parentNode.workflow.getCommandStack().execute(_4bb5);
}
};
draw2d.Port.prototype.getAbsolutePosition=function(){
return new draw2d.Point(this.getAbsoluteX(),this.getAbsoluteY());
};
draw2d.Port.prototype.getAbsoluteBounds=function(){
return new draw2d.Dimension(this.getAbsoluteX(),this.getAbsoluteY(),this.getWidth(),this.getHeight());
};
draw2d.Port.prototype.getAbsoluteY=function(){
return this.originY+this.parentNode.getY();
};
draw2d.Port.prototype.getAbsoluteX=function(){
return this.originX+this.parentNode.getX();
};
draw2d.Port.prototype.onOtherFigureMoved=function(_4bb6){
this.fireMoveEvent();
};
draw2d.Port.prototype.getName=function(){
return this.name;
};
draw2d.Port.prototype.setName=function(name){
this.name=name;
};
draw2d.Port.prototype.isOver=function(iX,iY){
var x=this.getAbsoluteX()-this.coronaWidth-this.getWidth()/2;
var y=this.getAbsoluteY()-this.coronaWidth-this.getHeight()/2;
var iX2=x+this.width+(this.coronaWidth*2)+this.getWidth()/2;
var iY2=y+this.height+(this.coronaWidth*2)+this.getHeight()/2;
return (iX>=x&&iX<=iX2&&iY>=y&&iY<=iY2);
};
draw2d.Port.prototype.showCorona=function(flag,_4bbf){
if(flag===true){
this.corona=new Corona();
this.corona.setAlpha(0.3);
this.corona.setBackgroundColor(new draw2d.Color(0,125,125));
this.corona.setColor(null);
this.corona.setDimension(this.getWidth()+(this.getCoronaWidth()*2),this.getWidth()+(this.getCoronaWidth()*2));
this.parentNode.getWorkflow().addFigure(this.corona,this.getAbsoluteX()-this.getCoronaWidth()-this.getWidth()/2,this.getAbsoluteY()-this.getCoronaWidth()-this.getHeight()/2);
}else{
if(flag===false&&this.corona!==null){
this.parentNode.getWorkflow().removeFigure(this.corona);
this.corona=null;
}
}
};
draw2d.Port.prototype.createCommand=function(_4bc0){
if(_4bc0.getPolicy()===draw2d.EditPolicy.MOVE){
if(!this.canDrag){
return null;
}
return new draw2d.CommandMovePort(this);
}
if(_4bc0.getPolicy()===draw2d.EditPolicy.CONNECT){
if(_4bc0.source.parentNode.id===_4bc0.target.parentNode.id){
return null;
}else{
return new draw2d.CommandConnect(_4bc0.canvas,_4bc0.source,_4bc0.target);
}
}
return null;
};
draw2d.InputPort=function(_4fc2){
draw2d.Port.call(this,_4fc2);
};
draw2d.InputPort.prototype=new draw2d.Port();
draw2d.InputPort.prototype.type="draw2d.InputPort";
draw2d.InputPort.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
return true;
};
draw2d.InputPort.prototype.onDragEnter=function(port){
if(port instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragEnter.call(this,port);
}else{
if(port instanceof draw2d.LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getSource() instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragEnter.call(this,line.getTarget());
}
}else{
if(port instanceof draw2d.LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getTarget() instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragEnter.call(this,line.getSource());
}
}
}
}
};
draw2d.InputPort.prototype.onDragLeave=function(port){
if(port instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragLeave.call(this,port);
}else{
if(port instanceof draw2d.LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getSource() instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragLeave.call(this,line.getTarget());
}
}else{
if(port instanceof draw2d.LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getTarget() instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragLeave.call(this,line.getSource());
}
}
}
}
};
draw2d.InputPort.prototype.createCommand=function(_4fc9){
if(_4fc9.getPolicy()==draw2d.EditPolicy.CONNECT){
if(_4fc9.source.parentNode.id==_4fc9.target.parentNode.id){
return null;
}
if(_4fc9.source instanceof draw2d.OutputPort){
return new draw2d.CommandConnect(_4fc9.canvas,_4fc9.source,_4fc9.target);
}
return null;
}
return draw2d.Port.prototype.createCommand.call(this,_4fc9);
};
draw2d.OutputPort=function(_5d91){
draw2d.Port.call(this,_5d91);
this.maxFanOut=100;
};
draw2d.OutputPort.prototype=new draw2d.Port();
draw2d.OutputPort.prototype.type="draw2d.OutputPort";
draw2d.OutputPort.prototype.onDragEnter=function(port){
if(this.getMaxFanOut()<=this.getFanOut()){
return;
}
if(port instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragEnter.call(this,port);
}else{
if(port instanceof draw2d.LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getSource() instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragEnter.call(this,line.getTarget());
}
}else{
if(port instanceof draw2d.LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getTarget() instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragEnter.call(this,line.getSource());
}
}
}
}
};
draw2d.OutputPort.prototype.onDragLeave=function(port){
if(port instanceof draw2d.InputPort){
draw2d.Port.prototype.onDragLeave.call(this,port);
}else{
if(port instanceof draw2d.LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getSource() instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragLeave.call(this,line.getTarget());
}
}else{
if(port instanceof draw2d.LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof draw2d.Connection&&line.getTarget() instanceof draw2d.OutputPort){
draw2d.Port.prototype.onDragLeave.call(this,line.getSource());
}
}
}
}
};
draw2d.OutputPort.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
if(this.maxFanOut===-1){
return true;
}
if(this.getMaxFanOut()<=this.getFanOut()){
return false;
}
return true;
};
draw2d.OutputPort.prototype.setMaxFanOut=function(count){
this.maxFanOut=count;
};
draw2d.OutputPort.prototype.getMaxFanOut=function(){
return this.maxFanOut;
};
draw2d.OutputPort.prototype.getFanOut=function(){
if(this.getParent().workflow===null){
return 0;
}
var count=0;
var lines=this.getParent().workflow.getLines();
var size=lines.getSize();
for(var i=0;i<size;i++){
var line=lines.get(i);
if(line instanceof draw2d.Connection){
if(line.getSource()==this){
count++;
}else{
if(line.getTarget()==this){
count++;
}
}
}
}
return count;
};
draw2d.OutputPort.prototype.createCommand=function(_5d9e){
if(_5d9e.getPolicy()===draw2d.EditPolicy.CONNECT){
if(_5d9e.source.parentNode.id===_5d9e.target.parentNode.id){
return null;
}
if(_5d9e.source instanceof draw2d.InputPort){
return new draw2d.CommandConnect(_5d9e.canvas,_5d9e.target,_5d9e.source);
}
return null;
}
return draw2d.Port.prototype.createCommand.call(this,_5d9e);
};
draw2d.Line=function(){
this.lineColor=new draw2d.Color(0,0,0);
this.stroke=1;
this.canvas=null;
this.workflow=null;
this.html=null;
this.graphics=null;
this.id=draw2d.UUID.create();
this.startX=30;
this.startY=30;
this.endX=100;
this.endY=100;
this.alpha=1;
this.isMoving=false;
this.model=null;
this.zOrder=draw2d.Line.ZOrderBaseIndex;
this.corona=draw2d.Line.CoronaWidth;
this.properties={};
this.moveListener=new draw2d.ArrayList();
this.setSelectable(true);
this.setDeleteable(true);
};
draw2d.Line.prototype.type="draw2d.Line";
draw2d.Line.ZOrderBaseIndex=200;
draw2d.Line.ZOrderBaseIndex=200;
draw2d.Line.CoronaWidth=5;
draw2d.Line.setZOrderBaseIndex=function(index){
draw2d.Line.ZOrderBaseIndex=index;
};
draw2d.Line.setDefaultCoronaWidth=function(width){
draw2d.Line.CoronaWidth=width;
};
draw2d.Line.prototype.dispose=function(){
this.canvas=null;
this.workflow=null;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
draw2d.Line.prototype.getZOrder=function(){
return this.zOrder;
};
draw2d.Line.prototype.setZOrder=function(index){
if(this.html!==null){
this.html.style.zIndex=index;
}
this.zOrder=index;
};
draw2d.Line.prototype.setCoronaWidth=function(width){
this.corona=width;
};
draw2d.Line.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left="0px";
item.style.top="0px";
item.style.height="0px";
item.style.width="0px";
item.style.zIndex=this.zOrder;
return item;
};
draw2d.Line.prototype.setId=function(id){
this.id=id;
if(this.html!==null){
this.html.id=id;
}
};
draw2d.Line.prototype.getId=function(){
return this.id;
};
draw2d.Line.prototype.getProperties=function(){
return this.properties;
};
draw2d.Line.prototype.getProperty=function(key){
return this.properties[key];
};
draw2d.Line.prototype.setProperty=function(key,value){
this.properties[key]=value;
this.setDocumentDirty();
};
draw2d.Line.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
draw2d.Line.prototype.getWorkflow=function(){
return this.workflow;
};
draw2d.Line.prototype.isResizeable=function(){
return true;
};
draw2d.Line.prototype.setCanvas=function(_54da){
this.canvas=_54da;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
draw2d.Line.prototype.setWorkflow=function(_54db){
this.workflow=_54db;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
draw2d.Line.prototype.paint=function(){
if(this.html===null){
return;
}
try{
if(this.graphics===null){
this.graphics=new jsGraphics(this.html);
}else{
this.graphics.clear();
}
this.graphics.setStroke(this.stroke);
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.graphics.drawLine(this.startX,this.startY,this.endX,this.endY);
this.graphics.paint();
}
catch(e){
pushErrorStack(e,"draw2d.Line.prototype.paint=function()");
}
};
draw2d.Line.prototype.attachMoveListener=function(_54dc){
this.moveListener.add(_54dc);
};
draw2d.Line.prototype.detachMoveListener=function(_54dd){
this.moveListener.remove(_54dd);
};
draw2d.Line.prototype.fireMoveEvent=function(){
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
draw2d.Line.prototype.onOtherFigureMoved=function(_54e0){
};
draw2d.Line.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
draw2d.Line.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
draw2d.Line.prototype.getColor=function(){
return this.lineColor;
};
draw2d.Line.prototype.setAlpha=function(_54e3){
if(_54e3==this.alpha){
return;
}
try{
this.html.style.MozOpacity=_54e3;
}
catch(exc1){
}
try{
this.html.style.opacity=_54e3;
}
catch(exc2){
}
try{
var _54e4=Math.round(_54e3*100);
if(_54e4>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_54e4+")";
}
}
catch(exc3){
}
this.alpha=_54e3;
};
draw2d.Line.prototype.setStartPoint=function(x,y){
this.startX=x;
this.startY=y;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
draw2d.Line.prototype.setEndPoint=function(x,y){
this.endX=x;
this.endY=y;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
draw2d.Line.prototype.getStartX=function(){
return this.startX;
};
draw2d.Line.prototype.getStartY=function(){
return this.startY;
};
draw2d.Line.prototype.getStartPoint=function(){
return new draw2d.Point(this.startX,this.startY);
};
draw2d.Line.prototype.getEndX=function(){
return this.endX;
};
draw2d.Line.prototype.getEndY=function(){
return this.endY;
};
draw2d.Line.prototype.getEndPoint=function(){
return new draw2d.Point(this.endX,this.endY);
};
draw2d.Line.prototype.isSelectable=function(){
return this.selectable;
};
draw2d.Line.prototype.setSelectable=function(flag){
this.selectable=flag;
};
draw2d.Line.prototype.isDeleteable=function(){
return this.deleteable;
};
draw2d.Line.prototype.setDeleteable=function(flag){
this.deleteable=flag;
};
draw2d.Line.prototype.getLength=function(){
return Math.sqrt((this.startX-this.endX)*(this.startX-this.endX)+(this.startY-this.endY)*(this.startY-this.endY));
};
draw2d.Line.prototype.getAngle=function(){
var _54eb=this.getLength();
var angle=-(180/Math.PI)*Math.asin((this.startY-this.endY)/_54eb);
if(angle<0){
if(this.endX<this.startX){
angle=Math.abs(angle)+180;
}else{
angle=360-Math.abs(angle);
}
}else{
if(this.endX<this.startX){
angle=180-angle;
}
}
return angle;
};
draw2d.Line.prototype.createCommand=function(_54ed){
if(_54ed.getPolicy()==draw2d.EditPolicy.MOVE){
var x1=this.getStartX();
var y1=this.getStartY();
var x2=this.getEndX();
var y2=this.getEndY();
return new draw2d.CommandMoveLine(this,x1,y1,x2,y2);
}
if(_54ed.getPolicy()==draw2d.EditPolicy.DELETE){
if(this.isDeleteable()==false){
return null;
}
return new draw2d.CommandDelete(this);
}
return null;
};
draw2d.Line.prototype.setModel=function(model){
if(this.model!==null){
this.model.removePropertyChangeListener(this);
}
this.model=model;
if(this.model!==null){
this.model.addPropertyChangeListener(this);
}
};
draw2d.Line.prototype.getModel=function(){
return this.model;
};
draw2d.Line.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
this.workflow.showMenu(menu,x,y);
}
};
draw2d.Line.prototype.getContextMenu=function(){
return null;
};
draw2d.Line.prototype.onDoubleClick=function(){
};
draw2d.Line.prototype.setDocumentDirty=function(){
if(this.workflow!==null){
this.workflow.setDocumentDirty();
}
};
draw2d.Line.prototype.containsPoint=function(px,py){
return draw2d.Line.hit(this.corona,this.startX,this.startY,this.endX,this.endY,px,py);
};
draw2d.Line.hit=function(_54f8,X1,Y1,X2,Y2,px,py){
X2-=X1;
Y2-=Y1;
px-=X1;
py-=Y1;
var _54ff=px*X2+py*Y2;
var _5500;
if(_54ff<=0){
_5500=0;
}else{
px=X2-px;
py=Y2-py;
_54ff=px*X2+py*Y2;
if(_54ff<=0){
_5500=0;
}else{
_5500=_54ff*_54ff/(X2*X2+Y2*Y2);
}
}
var lenSq=px*px+py*py-_5500;
if(lenSq<0){
lenSq=0;
}
return Math.sqrt(lenSq)<_54f8;
};
draw2d.ConnectionRouter=function(){
};
draw2d.ConnectionRouter.prototype.type="draw2d.ConnectionRouter";
draw2d.ConnectionRouter.prototype.getDirection=function(r,p){
var _4ae8=Math.abs(r.x-p.x);
var _4ae9=3;
var i=Math.abs(r.y-p.y);
if(i<=_4ae8){
_4ae8=i;
_4ae9=0;
}
i=Math.abs(r.getBottom()-p.y);
if(i<=_4ae8){
_4ae8=i;
_4ae9=2;
}
i=Math.abs(r.getRight()-p.x);
if(i<_4ae8){
_4ae8=i;
_4ae9=1;
}
return _4ae9;
};
draw2d.ConnectionRouter.prototype.getEndDirection=function(conn){
var p=conn.getEndPoint();
var rect=conn.getTarget().getParent().getBounds();
return this.getDirection(rect,p);
};
draw2d.ConnectionRouter.prototype.getStartDirection=function(conn){
var p=conn.getStartPoint();
var rect=conn.getSource().getParent().getBounds();
return this.getDirection(rect,p);
};
draw2d.ConnectionRouter.prototype.route=function(_4af1){
};
draw2d.NullConnectionRouter=function(){
};
draw2d.NullConnectionRouter.prototype=new draw2d.ConnectionRouter();
draw2d.NullConnectionRouter.prototype.type="draw2d.NullConnectionRouter";
draw2d.NullConnectionRouter.prototype.invalidate=function(){
};
draw2d.NullConnectionRouter.prototype.route=function(_5d72){
_5d72.addPoint(_5d72.getStartPoint());
_5d72.addPoint(_5d72.getEndPoint());
};
draw2d.ManhattanConnectionRouter=function(){
this.MINDIST=20;
};
draw2d.ManhattanConnectionRouter.prototype=new draw2d.ConnectionRouter();
draw2d.ManhattanConnectionRouter.prototype.type="draw2d.ManhattanConnectionRouter";
draw2d.ManhattanConnectionRouter.prototype.route=function(conn){
var _5526=conn.getStartPoint();
var _5527=this.getStartDirection(conn);
var toPt=conn.getEndPoint();
var toDir=this.getEndDirection(conn);
this._route(conn,toPt,toDir,_5526,_5527);
};
draw2d.ManhattanConnectionRouter.prototype._route=function(conn,_552b,_552c,toPt,toDir){
var TOL=0.1;
var _5530=0.01;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_552b.x-toPt.x;
var yDiff=_552b.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_5530))&&((yDiff*yDiff)<(_5530))){
conn.addPoint(new draw2d.Point(toPt.x,toPt.y));
return;
}
if(_552c==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir===RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new draw2d.Point(_552b.x-this.MINDIST,_552b.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir==UP))){
point=new draw2d.Point(toPt.x,_552b.y);
}else{
if(_552c==toDir){
var pos=Math.min(_552b.x,toPt.x)-this.MINDIST;
point=new draw2d.Point(pos,_552b.y);
}else{
point=new draw2d.Point(_552b.x-(xDiff/2),_552b.y);
}
}
}
if(yDiff>0){
dir=UP;
}else{
dir=DOWN;
}
}
}else{
if(_552c==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir===LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new draw2d.Point(_552b.x+this.MINDIST,_552b.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new draw2d.Point(toPt.x,_552b.y);
}else{
if(_552c==toDir){
var pos=Math.max(_552b.x,toPt.x)+this.MINDIST;
point=new draw2d.Point(pos,_552b.y);
}else{
point=new draw2d.Point(_552b.x-(xDiff/2),_552b.y);
}
}
}
if(yDiff>0){
dir=UP;
}else{
dir=DOWN;
}
}
}else{
if(_552c==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new draw2d.Point(_552b.x,_552b.y+this.MINDIST);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new draw2d.Point(_552b.x,toPt.y);
}else{
if(_552c===toDir){
var pos=Math.max(_552b.y,toPt.y)+this.MINDIST;
point=new draw2d.Point(_552b.x,pos);
}else{
point=new draw2d.Point(_552b.x,_552b.y-(yDiff/2));
}
}
}
if(xDiff>0){
dir=LEFT;
}else{
dir=RIGHT;
}
}
}else{
if(_552c==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir===DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new draw2d.Point(_552b.x,_552b.y-this.MINDIST);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new draw2d.Point(_552b.x,toPt.y);
}else{
if(_552c===toDir){
var pos=Math.min(_552b.y,toPt.y)-this.MINDIST;
point=new draw2d.Point(_552b.x,pos);
}else{
point=new draw2d.Point(_552b.x,_552b.y-(yDiff/2));
}
}
}
if(xDiff>0){
dir=LEFT;
}else{
dir=RIGHT;
}
}
}
}
}
}
this._route(conn,point,dir,toPt,toDir);
conn.addPoint(_552b);
};
draw2d.BezierConnectionRouter=function(_58f2){
if(!_58f2){
this.cheapRouter=new draw2d.ManhattanConnectionRouter();
}else{
this.cheapRouter=null;
}
this.iteration=5;
};
draw2d.BezierConnectionRouter.prototype=new draw2d.ConnectionRouter();
draw2d.BezierConnectionRouter.prototype.type="draw2d.BezierConnectionRouter";
draw2d.BezierConnectionRouter.prototype.drawBezier=function(_58f3,_58f4,t,iter){
var n=_58f3.length-1;
var q=[];
var _58f9=n+1;
for(var i=0;i<_58f9;i++){
q[i]=[];
q[i][0]=_58f3[i];
}
for(var j=1;j<=n;j++){
for(var i=0;i<=(n-j);i++){
q[i][j]=new draw2d.Point((1-t)*q[i][j-1].x+t*q[i+1][j-1].x,(1-t)*q[i][j-1].y+t*q[i+1][j-1].y);
}
}
var c1=[];
var c2=[];
for(var i=0;i<n+1;i++){
c1[i]=q[0][i];
c2[i]=q[i][n-i];
}
if(iter>=0){
this.drawBezier(c1,_58f4,t,--iter);
this.drawBezier(c2,_58f4,t,--iter);
}else{
for(var i=0;i<n;i++){
_58f4.push(q[i][n-i]);
}
}
};
draw2d.BezierConnectionRouter.prototype.route=function(conn){
if(this.cheapRouter!==null&&(conn.getSource().getParent().isMoving===true||conn.getTarget().getParent().isMoving===true)){
this.cheapRouter.route(conn);
return;
}
var _58ff=[];
var _5900=conn.getStartPoint();
var toPt=conn.getEndPoint();
this._route(_58ff,conn,toPt,this.getEndDirection(conn),_5900,this.getStartDirection(conn));
var _5902=[];
this.drawBezier(_58ff,_5902,0.5,this.iteration);
for(var i=0;i<_5902.length;i++){
conn.addPoint(_5902[i]);
}
conn.addPoint(toPt);
};
draw2d.BezierConnectionRouter.prototype._route=function(_5904,conn,_5906,_5907,toPt,toDir){
var TOL=0.1;
var _590b=0.01;
var _590c=90;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_5906.x-toPt.x;
var yDiff=_5906.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_590b))&&((yDiff*yDiff)<(_590b))){
_5904.push(new draw2d.Point(toPt.x,toPt.y));
return;
}
if(_5907===LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir===RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new draw2d.Point(_5906.x-_590c,_5906.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new draw2d.Point(toPt.x,_5906.y);
}else{
if(_5907===toDir){
var pos=Math.min(_5906.x,toPt.x)-_590c;
point=new draw2d.Point(pos,_5906.y);
}else{
point=new draw2d.Point(_5906.x-(xDiff/2),_5906.y);
}
}
}
if(yDiff>0){
dir=UP;
}else{
dir=DOWN;
}
}
}else{
if(_5907===RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new draw2d.Point(_5906.x+_590c,_5906.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new draw2d.Point(toPt.x,_5906.y);
}else{
if(_5907===toDir){
var pos=Math.max(_5906.x,toPt.x)+_590c;
point=new draw2d.Point(pos,_5906.y);
}else{
point=new draw2d.Point(_5906.x-(xDiff/2),_5906.y);
}
}
}
if(yDiff>0){
dir=UP;
}else{
dir=DOWN;
}
}
}else{
if(_5907===DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir===UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new draw2d.Point(_5906.x,_5906.y+_590c);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new draw2d.Point(_5906.x,toPt.y);
}else{
if(_5907===toDir){
var pos=Math.max(_5906.y,toPt.y)+_590c;
point=new draw2d.Point(_5906.x,pos);
}else{
point=new draw2d.Point(_5906.x,_5906.y-(yDiff/2));
}
}
}
if(xDiff>0){
dir=LEFT;
}else{
dir=RIGHT;
}
}
}else{
if(_5907===UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir===DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new draw2d.Point(_5906.x,_5906.y-_590c);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new draw2d.Point(_5906.x,toPt.y);
}else{
if(_5907===toDir){
var pos=Math.min(_5906.y,toPt.y)-_590c;
point=new draw2d.Point(_5906.x,pos);
}else{
point=new draw2d.Point(_5906.x,_5906.y-(yDiff/2));
}
}
}
if(xDiff>0){
dir=LEFT;
}else{
dir=RIGHT;
}
}
}
}
}
}
this._route(_5904,conn,point,dir,toPt,toDir);
_5904.push(_5906);
};
draw2d.FanConnectionRouter=function(){
};
draw2d.FanConnectionRouter.prototype=new draw2d.NullConnectionRouter();
draw2d.FanConnectionRouter.prototype.type="draw2d.FanConnectionRouter";
draw2d.FanConnectionRouter.prototype.route=function(conn){
var _54b2=conn.getStartPoint();
var toPt=conn.getEndPoint();
var lines=conn.getSource().getConnections();
var _54b5=new draw2d.ArrayList();
var index=0;
for(var i=0;i<lines.getSize();i++){
var _54b8=lines.get(i);
if(_54b8.getTarget()==conn.getTarget()||_54b8.getSource()==conn.getTarget()){
_54b5.add(_54b8);
if(conn==_54b8){
index=_54b5.getSize();
}
}
}
if(_54b5.getSize()>1){
this.routeCollision(conn,index);
}else{
draw2d.NullConnectionRouter.prototype.route.call(this,conn);
}
};
draw2d.FanConnectionRouter.prototype.routeNormal=function(conn){
conn.addPoint(conn.getStartPoint());
conn.addPoint(conn.getEndPoint());
};
draw2d.FanConnectionRouter.prototype.routeCollision=function(conn,index){
var start=conn.getStartPoint();
var end=conn.getEndPoint();
conn.addPoint(start);
var _54be=10;
var _54bf=new draw2d.Point((end.x+start.x)/2,(end.y+start.y)/2);
var _54c0=end.getPosition(start);
var ray;
if(_54c0==draw2d.PositionConstants.SOUTH||_54c0==draw2d.PositionConstants.EAST){
ray=new draw2d.Point(end.x-start.x,end.y-start.y);
}else{
ray=new draw2d.Point(start.x-end.x,start.y-end.y);
}
var _54c2=Math.sqrt(ray.x*ray.x+ray.y*ray.y);
var _54c3=_54be*ray.x/_54c2;
var _54c4=_54be*ray.y/_54c2;
var _54c5;
if(index%2===0){
_54c5=new draw2d.Point(_54bf.x+(index/2)*(-1*_54c4),_54bf.y+(index/2)*_54c3);
}else{
_54c5=new draw2d.Point(_54bf.x+(index/2)*_54c4,_54bf.y+(index/2)*(-1*_54c3));
}
conn.addPoint(_54c5);
conn.addPoint(end);
};
draw2d.Graphics=function(_5502,_5503,_5504){
this.jsGraphics=_5502;
this.xt=_5504.x;
this.yt=_5504.y;
this.radian=_5503*Math.PI/180;
this.sinRadian=Math.sin(this.radian);
this.cosRadian=Math.cos(this.radian);
};
draw2d.Graphics.prototype.setStroke=function(x){
this.jsGraphics.setStroke(x);
};
draw2d.Graphics.prototype.drawLine=function(x1,y1,x2,y2){
var _x1=this.xt+x1*this.cosRadian-y1*this.sinRadian;
var _y1=this.yt+x1*this.sinRadian+y1*this.cosRadian;
var _x2=this.xt+x2*this.cosRadian-y2*this.sinRadian;
var _y2=this.yt+x2*this.sinRadian+y2*this.cosRadian;
this.jsGraphics.drawLine(_x1,_y1,_x2,_y2);
};
draw2d.Graphics.prototype.fillRect=function(x,y,w,h){
var x1=this.xt+x*this.cosRadian-y*this.sinRadian;
var y1=this.yt+x*this.sinRadian+y*this.cosRadian;
var x2=this.xt+(x+w)*this.cosRadian-y*this.sinRadian;
var y2=this.yt+(x+w)*this.sinRadian+y*this.cosRadian;
var x3=this.xt+(x+w)*this.cosRadian-(y+h)*this.sinRadian;
var y3=this.yt+(x+w)*this.sinRadian+(y+h)*this.cosRadian;
var x4=this.xt+x*this.cosRadian-(y+h)*this.sinRadian;
var y4=this.yt+x*this.sinRadian+(y+h)*this.cosRadian;
this.jsGraphics.fillPolygon([x1,x2,x3,x4],[y1,y2,y3,y4]);
};
draw2d.Graphics.prototype.fillPolygon=function(_551a,_551b){
var rotX=[];
var rotY=[];
for(var i=0;i<_551a.length;i++){
rotX[i]=this.xt+_551a[i]*this.cosRadian-_551b[i]*this.sinRadian;
rotY[i]=this.yt+_551a[i]*this.sinRadian+_551b[i]*this.cosRadian;
}
this.jsGraphics.fillPolygon(rotX,rotY);
};
draw2d.Graphics.prototype.setColor=function(color){
this.jsGraphics.setColor(color.getHTMLStyle());
};
draw2d.Graphics.prototype.drawPolygon=function(_5520,_5521){
var rotX=[];
var rotY=[];
for(var i=0;i<_5520.length;i++){
rotX[i]=this.xt+_5520[i]*this.cosRadian-_5521[i]*this.sinRadian;
rotY[i]=this.yt+_5520[i]*this.sinRadian+_5521[i]*this.cosRadian;
}
this.jsGraphics.drawPolygon(rotX,rotY);
};
draw2d.Connection=function(){
draw2d.Line.call(this);
this.sourcePort=null;
this.targetPort=null;
this.canDrag=true;
this.sourceDecorator=null;
this.targetDecorator=null;
this.sourceAnchor=new draw2d.ConnectionAnchor();
this.targetAnchor=new draw2d.ConnectionAnchor();
this.router=draw2d.Connection.defaultRouter;
this.lineSegments=new draw2d.ArrayList();
this.children=new draw2d.ArrayList();
this.setColor(new draw2d.Color(0,0,115));
this.setLineWidth(1);
};
draw2d.Connection.prototype=new draw2d.Line();
draw2d.Connection.prototype.type="draw2d.Connection";
draw2d.Connection.defaultRouter=new draw2d.ManhattanConnectionRouter();
draw2d.Connection.setDefaultRouter=function(_5543){
draw2d.Connection.defaultRouter=_5543;
};
draw2d.Connection.prototype.disconnect=function(){
if(this.sourcePort!==null){
this.sourcePort.detachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!==null){
this.targetPort.detachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
draw2d.Connection.prototype.reconnect=function(){
if(this.sourcePort!==null){
this.sourcePort.attachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!==null){
this.targetPort.attachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
draw2d.Connection.prototype.isResizeable=function(){
return this.getCanDrag();
};
draw2d.Connection.prototype.setCanDrag=function(flag){
this.canDrag=flag;
};
draw2d.Connection.prototype.getCanDrag=function(){
return this.canDrag;
};
draw2d.Connection.prototype.addFigure=function(_5545,_5546){
var entry={};
entry.figure=_5545;
entry.locator=_5546;
this.children.add(entry);
if(this.graphics!==null){
this.paint();
}
var oThis=this;
var _5549=function(){
var _554a=arguments[0]||window.event;
_554a.returnValue=false;
oThis.getWorkflow().setCurrentSelection(oThis);
oThis.getWorkflow().showLineResizeHandles(oThis);
};
if(_5545.getHTMLElement().addEventListener){
_5545.getHTMLElement().addEventListener("mousedown",_5549,false);
}else{
if(_5545.getHTMLElement().attachEvent){
_5545.getHTMLElement().attachEvent("onmousedown",_5549);
}
}
};
draw2d.Connection.prototype.setSourceDecorator=function(_554b){
this.sourceDecorator=_554b;
if(this.graphics!==null){
this.paint();
}
};
draw2d.Connection.prototype.getSourceDecorator=function(){
return this.sourceDecorator;
};
draw2d.Connection.prototype.setTargetDecorator=function(_554c){
this.targetDecorator=_554c;
if(this.graphics!==null){
this.paint();
}
};
draw2d.Connection.prototype.getTargetDecorator=function(){
return this.targetDecorator;
};
draw2d.Connection.prototype.setSourceAnchor=function(_554d){
this.sourceAnchor=_554d;
this.sourceAnchor.setOwner(this.sourcePort);
if(this.graphics!==null){
this.paint();
}
};
draw2d.Connection.prototype.setTargetAnchor=function(_554e){
this.targetAnchor=_554e;
this.targetAnchor.setOwner(this.targetPort);
if(this.graphics!==null){
this.paint();
}
};
draw2d.Connection.prototype.setRouter=function(_554f){
if(_554f!==null){
this.router=_554f;
}else{
this.router=new draw2d.NullConnectionRouter();
}
if(this.graphics!==null){
this.paint();
}
};
draw2d.Connection.prototype.getRouter=function(){
return this.router;
};
draw2d.Connection.prototype.setWorkflow=function(_5550){
draw2d.Line.prototype.setWorkflow.call(this,_5550);
for(var i=0;i<this.children.getSize();i++){
this.children.get(i).isAppended=false;
}
};
draw2d.Connection.prototype.paint=function(){
if(this.html===null){
return;
}
try{
for(var i=0;i<this.children.getSize();i++){
var entry=this.children.get(i);
if(entry.isAppended==true){
this.html.removeChild(entry.figure.getHTMLElement());
}
entry.isAppended=false;
}
if(this.graphics===null){
this.graphics=new jsGraphics(this.html);
}else{
this.graphics.clear();
}
this.graphics.setStroke(this.stroke);
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.startStroke();
this.router.route(this);
if(this.getSource().getParent().isMoving==false&&this.getTarget().getParent().isMoving==false){
if(this.targetDecorator!==null){
this.targetDecorator.paint(new draw2d.Graphics(this.graphics,this.getEndAngle(),this.getEndPoint()));
}
if(this.sourceDecorator!==null){
this.sourceDecorator.paint(new draw2d.Graphics(this.graphics,this.getStartAngle(),this.getStartPoint()));
}
}
this.finishStroke();
for(var i=0;i<this.children.getSize();i++){
var entry=this.children.get(i);
this.html.appendChild(entry.figure.getHTMLElement());
entry.isAppended=true;
entry.locator.relocate(entry.figure);
}
}
catch(e){
pushErrorStack(e,"draw2d.Connection.prototype.paint=function()");
}
};
draw2d.Connection.prototype.getStartPoint=function(){
if(this.isMoving==false){
return this.sourceAnchor.getLocation(this.targetAnchor.getReferencePoint());
}else{
return draw2d.Line.prototype.getStartPoint.call(this);
}
};
draw2d.Connection.prototype.getEndPoint=function(){
if(this.isMoving==false){
return this.targetAnchor.getLocation(this.sourceAnchor.getReferencePoint());
}else{
return draw2d.Line.prototype.getEndPoint.call(this);
}
};
draw2d.Connection.prototype.startStroke=function(){
this.oldPoint=null;
this.lineSegments=new draw2d.ArrayList();
};
draw2d.Connection.prototype.finishStroke=function(){
this.graphics.paint();
this.oldPoint=null;
};
draw2d.Connection.prototype.getPoints=function(){
var _5554=new draw2d.ArrayList();
var line=null;
for(var i=0;i<this.lineSegments.getSize();i++){
line=this.lineSegments.get(i);
_5554.add(line.start);
}
if(line!==null){
_5554.add(line.end);
}
return _5554;
};
draw2d.Connection.prototype.addPoint=function(p){
p=new draw2d.Point(parseInt(p.x),parseInt(p.y));
if(this.oldPoint!==null){
this.graphics.drawLine(this.oldPoint.x,this.oldPoint.y,p.x,p.y);
var line={};
line.start=this.oldPoint;
line.end=p;
this.lineSegments.add(line);
}
this.oldPoint={};
this.oldPoint.x=p.x;
this.oldPoint.y=p.y;
};
draw2d.Connection.prototype.refreshSourcePort=function(){
var model=this.getModel().getSourceModel();
var _555a=this.getModel().getSourcePortName();
var _555b=this.getWorkflow().getDocument().getFigures();
var count=_555b.getSize();
for(var i=0;i<count;i++){
var _555e=_555b.get(i);
if(_555e.getModel()==model){
var port=_555e.getOutputPort(_555a);
this.setSource(port);
}
}
this.setRouter(this.getRouter());
};
draw2d.Connection.prototype.refreshTargetPort=function(){
var model=this.getModel().getTargetModel();
var _5561=this.getModel().getTargetPortName();
var _5562=this.getWorkflow().getDocument().getFigures();
var count=_5562.getSize();
for(var i=0;i<count;i++){
var _5565=_5562.get(i);
if(_5565.getModel()==model){
var port=_5565.getInputPort(_5561);
this.setTarget(port);
}
}
this.setRouter(this.getRouter());
};
draw2d.Connection.prototype.setSource=function(port){
if(this.sourcePort!==null){
this.sourcePort.detachMoveListener(this);
}
this.sourcePort=port;
if(this.sourcePort===null){
return;
}
this.sourceAnchor.setOwner(this.sourcePort);
this.fireSourcePortRouteEvent();
this.sourcePort.attachMoveListener(this);
this.setStartPoint(port.getAbsoluteX(),port.getAbsoluteY());
};
draw2d.Connection.prototype.getSource=function(){
return this.sourcePort;
};
draw2d.Connection.prototype.setTarget=function(port){
if(this.targetPort!==null){
this.targetPort.detachMoveListener(this);
}
this.targetPort=port;
if(this.targetPort===null){
return;
}
this.targetAnchor.setOwner(this.targetPort);
this.fireTargetPortRouteEvent();
this.targetPort.attachMoveListener(this);
this.setEndPoint(port.getAbsoluteX(),port.getAbsoluteY());
};
draw2d.Connection.prototype.getTarget=function(){
return this.targetPort;
};
draw2d.Connection.prototype.onOtherFigureMoved=function(_5569){
if(_5569==this.sourcePort){
this.setStartPoint(this.sourcePort.getAbsoluteX(),this.sourcePort.getAbsoluteY());
}else{
this.setEndPoint(this.targetPort.getAbsoluteX(),this.targetPort.getAbsoluteY());
}
};
draw2d.Connection.prototype.containsPoint=function(px,py){
for(var i=0;i<this.lineSegments.getSize();i++){
var line=this.lineSegments.get(i);
if(draw2d.Line.hit(this.corona,line.start.x,line.start.y,line.end.x,line.end.y,px,py)){
return true;
}
}
return false;
};
draw2d.Connection.prototype.getStartAngle=function(){
var p1=this.lineSegments.get(0).start;
var p2=this.lineSegments.get(0).end;
if(this.router instanceof draw2d.BezierConnectionRouter){
p2=this.lineSegments.get(5).end;
}
var _5570=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_5570);
if(angle<0){
if(p2.x<p1.x){
angle=Math.abs(angle)+180;
}else{
angle=360-Math.abs(angle);
}
}else{
if(p2.x<p1.x){
angle=180-angle;
}
}
return angle;
};
draw2d.Connection.prototype.getEndAngle=function(){
if(this.lineSegments.getSize()===0){
return 90;
}
var p1=this.lineSegments.get(this.lineSegments.getSize()-1).end;
var p2=this.lineSegments.get(this.lineSegments.getSize()-1).start;
if(this.router instanceof draw2d.BezierConnectionRouter){
p2=this.lineSegments.get(this.lineSegments.getSize()-5).end;
}
var _5574=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_5574);
if(angle<0){
if(p2.x<p1.x){
angle=Math.abs(angle)+180;
}else{
angle=360-Math.abs(angle);
}
}else{
if(p2.x<p1.x){
angle=180-angle;
}
}
return angle;
};
draw2d.Connection.prototype.fireSourcePortRouteEvent=function(){
var _5576=this.sourcePort.getConnections();
for(var i=0;i<_5576.getSize();i++){
_5576.get(i).paint();
}
};
draw2d.Connection.prototype.fireTargetPortRouteEvent=function(){
var _5578=this.targetPort.getConnections();
for(var i=0;i<_5578.getSize();i++){
_5578.get(i).paint();
}
};
draw2d.Connection.prototype.createCommand=function(_557a){
if(_557a.getPolicy()==draw2d.EditPolicy.MOVE){
return new draw2d.CommandReconnect(this);
}
if(_557a.getPolicy()==draw2d.EditPolicy.DELETE){
if(this.isDeleteable()==true){
return new draw2d.CommandDelete(this);
}
return null;
}
return null;
};
draw2d.ConnectionAnchor=function(owner){
this.owner=owner;
};
draw2d.ConnectionAnchor.prototype.type="draw2d.ConnectionAnchor";
draw2d.ConnectionAnchor.prototype.getLocation=function(_54cf){
return this.getReferencePoint();
};
draw2d.ConnectionAnchor.prototype.getOwner=function(){
return this.owner;
};
draw2d.ConnectionAnchor.prototype.setOwner=function(owner){
this.owner=owner;
};
draw2d.ConnectionAnchor.prototype.getBox=function(){
return this.getOwner().getAbsoluteBounds();
};
draw2d.ConnectionAnchor.prototype.getReferencePoint=function(){
if(this.getOwner()===null){
return null;
}else{
return this.getOwner().getAbsolutePosition();
}
};
draw2d.ChopboxConnectionAnchor=function(owner){
draw2d.ConnectionAnchor.call(this,owner);
};
draw2d.ChopboxConnectionAnchor.prototype=new draw2d.ConnectionAnchor();
draw2d.ChopboxConnectionAnchor.prototype.type="draw2d.ChopboxConnectionAnchor";
draw2d.ChopboxConnectionAnchor.prototype.getLocation=function(_4a3b){
var r=new draw2d.Dimension();
r.setBounds(this.getBox());
r.translate(-1,-1);
r.resize(1,1);
var _4a3d=r.x+r.w/2;
var _4a3e=r.y+r.h/2;
if(r.isEmpty()||(_4a3b.x==_4a3d&&_4a3b.y==_4a3e)){
return new Point(_4a3d,_4a3e);
}
var dx=_4a3b.x-_4a3d;
var dy=_4a3b.y-_4a3e;
var scale=0.5/Math.max(Math.abs(dx)/r.w,Math.abs(dy)/r.h);
dx*=scale;
dy*=scale;
_4a3d+=dx;
_4a3e+=dy;
return new draw2d.Point(Math.round(_4a3d),Math.round(_4a3e));
};
draw2d.ChopboxConnectionAnchor.prototype.getBox=function(){
return this.getOwner().getParent().getBounds();
};
draw2d.ChopboxConnectionAnchor.prototype.getReferencePoint=function(){
return this.getBox().getCenter();
};
draw2d.ConnectionDecorator=function(){
this.color=new draw2d.Color(0,0,0);
this.backgroundColor=new draw2d.Color(250,250,250);
};
draw2d.ConnectionDecorator.prototype.type="draw2d.ConnectionDecorator";
draw2d.ConnectionDecorator.prototype.paint=function(g){
};
draw2d.ConnectionDecorator.prototype.setColor=function(c){
this.color=c;
};
draw2d.ConnectionDecorator.prototype.setBackgroundColor=function(c){
this.backgroundColor=c;
};
draw2d.ArrowConnectionDecorator=function(){
};
draw2d.ArrowConnectionDecorator.prototype=new draw2d.ConnectionDecorator();
draw2d.ArrowConnectionDecorator.prototype.type="draw2d.ArrowConnectionDecorator";
draw2d.ArrowConnectionDecorator.prototype.paint=function(g){
if(this.backgroundColor!==null){
g.setColor(this.backgroundColor);
g.fillPolygon([3,20,20,3],[0,5,-5,0]);
}
g.setColor(this.color);
g.setStroke(1);
g.drawPolygon([3,20,20,3],[0,5,-5,0]);
};
draw2d.CompartmentFigure=function(){
draw2d.Node.call(this);
this.children=new draw2d.ArrayList();
this.setBorder(new draw2d.LineBorder(1));
this.dropable=new draw2d.DropTarget(this.html);
this.dropable.node=this;
this.dropable.addEventListener("figureenter",function(_4ace){
_4ace.target.node.onFigureEnter(_4ace.relatedTarget.node);
});
this.dropable.addEventListener("figureleave",function(_4acf){
_4acf.target.node.onFigureLeave(_4acf.relatedTarget.node);
});
this.dropable.addEventListener("figuredrop",function(_4ad0){
_4ad0.target.node.onFigureDrop(_4ad0.relatedTarget.node);
});
};
draw2d.CompartmentFigure.prototype=new draw2d.Node();
draw2d.CompartmentFigure.prototype.type="draw2d.CompartmentFigure";
draw2d.CompartmentFigure.prototype.onFigureEnter=function(_4ad1){
};
draw2d.CompartmentFigure.prototype.onFigureLeave=function(_4ad2){
};
draw2d.CompartmentFigure.prototype.onFigureDrop=function(_4ad3){
};
draw2d.CompartmentFigure.prototype.getChildren=function(){
return this.children;
};
draw2d.CompartmentFigure.prototype.addChild=function(_4ad4){
_4ad4.setZOrder(this.getZOrder()+1);
_4ad4.setParent(this);
this.children.add(_4ad4);
};
draw2d.CompartmentFigure.prototype.removeChild=function(_4ad5){
_4ad5.setParent(null);
this.children.remove(_4ad5);
};
draw2d.CompartmentFigure.prototype.setZOrder=function(index){
draw2d.Node.prototype.setZOrder.call(this,index);
for(var i=0;i<this.children.getSize();i++){
this.children.get(i).setZOrder(index+1);
}
};
draw2d.CompartmentFigure.prototype.setPosition=function(xPos,yPos){
var oldX=this.getX();
var oldY=this.getY();
draw2d.Node.prototype.setPosition.call(this,xPos,yPos);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
draw2d.CompartmentFigure.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
draw2d.Node.prototype.onDrag.call(this);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
draw2d.CanvasDocument=function(_5922){
this.canvas=_5922;
};
draw2d.CanvasDocument.prototype.type="draw2d.CanvasDocument";
draw2d.CanvasDocument.prototype.getFigures=function(){
var _5923=new draw2d.ArrayList();
var _5924=this.canvas.figures;
var _5925=this.canvas.dialogs;
for(var i=0;i<_5924.getSize();i++){
var _5927=_5924.get(i);
if(_5925.indexOf(_5927)==-1&&_5927.getParent()===null&&!(_5927 instanceof draw2d.WindowFigure)){
_5923.add(_5927);
}
}
return _5923;
};
draw2d.CanvasDocument.prototype.getFigure=function(id){
return this.canvas.getFigure(id);
};
draw2d.CanvasDocument.prototype.getLines=function(){
return this.canvas.getLines();
};
draw2d.CanvasDocument.prototype.getLine=function(id){
return this.canvas.getLine(id);
};
draw2d.Annotation=function(msg){
this.msg=msg;
this.color=new draw2d.Color(0,0,0);
this.bgColor=new draw2d.Color(241,241,121);
this.fontSize=10;
this.textNode=null;
draw2d.Figure.call(this);
};
draw2d.Annotation.prototype=new draw2d.Figure();
draw2d.Annotation.prototype.type="draw2d.Annotation";
draw2d.Annotation.prototype.createHTMLElement=function(){
var item=draw2d.Figure.prototype.createHTMLElement.call(this);
item.style.color=this.color.getHTMLStyle();
item.style.backgroundColor=this.bgColor.getHTMLStyle();
item.style.fontSize=this.fontSize+"pt";
item.style.width="auto";
item.style.height="auto";
item.style.margin="0px";
item.style.padding="0px";
item.onselectstart=function(){
return false;
};
item.unselectable="on";
item.style.cursor="default";
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
this.disableTextSelection(item);
return item;
};
draw2d.Annotation.prototype.onDoubleClick=function(){
var _4fe7=new draw2d.AnnotationDialog(this);
this.workflow.showDialog(_4fe7);
};
draw2d.Annotation.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
draw2d.Annotation.prototype.getBackgroundColor=function(){
return this.bgColor;
};
draw2d.Annotation.prototype.setFontSize=function(size){
this.fontSize=size;
this.html.style.fontSize=this.fontSize+"pt";
};
draw2d.Annotation.prototype.getText=function(){
return this.msg;
};
draw2d.Annotation.prototype.setText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createTextNode(this.msg);
this.html.appendChild(this.textNode);
};
draw2d.Annotation.prototype.setStyledText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createElement("div");
this.textNode.innerHTML=text;
this.html.appendChild(this.textNode);
};
draw2d.ResizeHandle=function(_5b0b,type){
draw2d.Rectangle.call(this,5,5);
this.type=type;
var _5b0d=this.getWidth();
var _5b0e=_5b0d/2;
switch(this.type){
case 1:
this.setSnapToGridAnchor(new draw2d.Point(_5b0d,_5b0d));
break;
case 2:
this.setSnapToGridAnchor(new draw2d.Point(_5b0e,_5b0d));
break;
case 3:
this.setSnapToGridAnchor(new draw2d.Point(0,_5b0d));
break;
case 4:
this.setSnapToGridAnchor(new draw2d.Point(0,_5b0e));
break;
case 5:
this.setSnapToGridAnchor(new draw2d.Point(0,0));
break;
case 6:
this.setSnapToGridAnchor(new draw2d.Point(_5b0e,0));
break;
case 7:
this.setSnapToGridAnchor(new draw2d.Point(_5b0d,0));
break;
case 8:
this.setSnapToGridAnchor(new draw2d.Point(_5b0d,_5b0e));
case 9:
this.setSnapToGridAnchor(new draw2d.Point(_5b0e,_5b0e));
break;
}
this.setBackgroundColor(new draw2d.Color(0,255,0));
this.setWorkflow(_5b0b);
this.setZOrder(10000);
};
draw2d.ResizeHandle.prototype=new draw2d.Rectangle();
draw2d.ResizeHandle.prototype.type="draw2d.ResizeHandle";
draw2d.ResizeHandle.prototype.getSnapToDirection=function(){
switch(this.type){
case 1:
return draw2d.SnapToHelper.NORTH_WEST;
case 2:
return draw2d.SnapToHelper.NORTH;
case 3:
return draw2d.SnapToHelper.NORTH_EAST;
case 4:
return draw2d.SnapToHelper.EAST;
case 5:
return draw2d.SnapToHelper.SOUTH_EAST;
case 6:
return draw2d.SnapToHelper.SOUTH;
case 7:
return draw2d.SnapToHelper.SOUTH_WEST;
case 8:
return draw2d.SnapToHelper.WEST;
case 9:
return draw2d.SnapToHelper.CENTER;
}
};
draw2d.ResizeHandle.prototype.onDragend=function(){
var _5b0f=this.workflow.currentSelection;
if(this.commandMove!==null){
this.commandMove.setPosition(_5b0f.getX(),_5b0f.getY());
this.workflow.getCommandStack().execute(this.commandMove);
this.commandMove=null;
}
if(this.commandResize!==null){
this.commandResize.setDimension(_5b0f.getWidth(),_5b0f.getHeight());
this.workflow.getCommandStack().execute(this.commandResize);
this.commandResize=null;
}
this.workflow.hideSnapToHelperLines();
};
draw2d.ResizeHandle.prototype.setPosition=function(xPos,yPos){
this.x=xPos;
this.y=yPos;
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
draw2d.ResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var _5b14=this.workflow.currentSelection;
this.commandMove=_5b14.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.MOVE));
this.commandResize=_5b14.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.RESIZE));
return true;
};
draw2d.ResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
draw2d.Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _5b19=this.workflow.currentSelection.getX();
var _5b1a=this.workflow.currentSelection.getY();
var _5b1b=this.workflow.currentSelection.getWidth();
var _5b1c=this.workflow.currentSelection.getHeight();
switch(this.type){
case 1:
this.workflow.currentSelection.setPosition(_5b19-diffX,_5b1a-diffY);
this.workflow.currentSelection.setDimension(_5b1b+diffX,_5b1c+diffY);
break;
case 2:
this.workflow.currentSelection.setPosition(_5b19,_5b1a-diffY);
this.workflow.currentSelection.setDimension(_5b1b,_5b1c+diffY);
break;
case 3:
this.workflow.currentSelection.setPosition(_5b19,_5b1a-diffY);
this.workflow.currentSelection.setDimension(_5b1b-diffX,_5b1c+diffY);
break;
case 4:
this.workflow.currentSelection.setPosition(_5b19,_5b1a);
this.workflow.currentSelection.setDimension(_5b1b-diffX,_5b1c);
break;
case 5:
this.workflow.currentSelection.setPosition(_5b19,_5b1a);
this.workflow.currentSelection.setDimension(_5b1b-diffX,_5b1c-diffY);
break;
case 6:
this.workflow.currentSelection.setPosition(_5b19,_5b1a);
this.workflow.currentSelection.setDimension(_5b1b,_5b1c-diffY);
break;
case 7:
this.workflow.currentSelection.setPosition(_5b19-diffX,_5b1a);
this.workflow.currentSelection.setDimension(_5b1b+diffX,_5b1c-diffY);
break;
case 8:
this.workflow.currentSelection.setPosition(_5b19-diffX,_5b1a);
this.workflow.currentSelection.setDimension(_5b1b+diffX,_5b1c);
break;
}
this.workflow.moveResizeHandles(this.workflow.getCurrentSelection());
};
draw2d.ResizeHandle.prototype.setCanDrag=function(flag){
draw2d.Rectangle.prototype.setCanDrag.call(this,flag);
if(this.html===null){
return;
}
if(!flag){
this.html.style.cursor="";
return;
}
switch(this.type){
case 1:
this.html.style.cursor="nw-resize";
break;
case 2:
this.html.style.cursor="s-resize";
break;
case 3:
this.html.style.cursor="ne-resize";
break;
case 4:
this.html.style.cursor="w-resize";
break;
case 5:
this.html.style.cursor="se-resize";
break;
case 6:
this.html.style.cursor="n-resize";
break;
case 7:
this.html.style.cursor="sw-resize";
break;
case 8:
this.html.style.cursor="e-resize";
break;
case 9:
this.html.style.cursor="resize";
break;
}
};
draw2d.ResizeHandle.prototype.onKeyDown=function(_5b1e,ctrl){
this.workflow.onKeyDown(_5b1e,ctrl);
};
draw2d.ResizeHandle.prototype.fireMoveEvent=function(){
};
draw2d.LineStartResizeHandle=function(_4bf0){
draw2d.ResizeHandle.call(this,_4bf0,9);
this.setDimension(10,10);
this.setBackgroundColor(new draw2d.Color(100,255,0));
this.setZOrder(10000);
};
draw2d.LineStartResizeHandle.prototype=new draw2d.ResizeHandle();
draw2d.LineStartResizeHandle.prototype.type="draw2d.LineStartResizeHandle";
draw2d.LineStartResizeHandle.prototype.onDragend=function(){
if(this.workflow.currentSelection instanceof draw2d.Connection){
if(this.command!==null){
this.command.cancel();
}
}else{
if(this.command!==null){
this.getWorkflow().getCommandStack().execute(this.command);
}
}
this.command=null;
};
draw2d.LineStartResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
this.command=this.workflow.currentSelection.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.MOVE));
return this.command!==null;
};
draw2d.LineStartResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
draw2d.Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _4bf7=this.workflow.currentSelection.getStartPoint();
var line=this.workflow.currentSelection;
line.setStartPoint(_4bf7.x-diffX,_4bf7.y-diffY);
line.isMoving=true;
};
draw2d.LineStartResizeHandle.prototype.onDrop=function(_4bf9){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof draw2d.Connection){
this.command.setNewPorts(_4bf9,line.getTarget());
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
draw2d.LineEndResizeHandle=function(_4c33){
draw2d.ResizeHandle.call(this,_4c33,9);
this.setDimension(10,10);
this.setBackgroundColor(new draw2d.Color(0,255,0));
this.setZOrder(10000);
};
draw2d.LineEndResizeHandle.prototype=new draw2d.ResizeHandle();
draw2d.LineEndResizeHandle.prototype.type="draw2d.LineEndResizeHandle";
draw2d.LineEndResizeHandle.prototype.onDragend=function(){
if(this.workflow.currentSelection instanceof draw2d.Connection){
if(this.command!==null){
this.command.cancel();
}
}else{
if(this.command!==null){
this.workflow.getCommandStack().execute(this.command);
}
}
this.command=null;
};
draw2d.LineEndResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
this.command=this.workflow.currentSelection.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.MOVE));
return this.command!==null;
};
draw2d.LineEndResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
draw2d.Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _4c3a=this.workflow.currentSelection.getEndPoint();
var line=this.workflow.currentSelection;
line.setEndPoint(_4c3a.x-diffX,_4c3a.y-diffY);
line.isMoving=true;
};
draw2d.LineEndResizeHandle.prototype.onDrop=function(_4c3c){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof draw2d.Connection){
this.command.setNewPorts(line.getSource(),_4c3c);
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
draw2d.Canvas=function(_558e){
try{
if(_558e){
this.construct(_558e);
}
this.enableSmoothFigureHandling=false;
this.canvasLines=new draw2d.ArrayList();
}
catch(e){
pushErrorStack(e,"draw2d.Canvas=function(/*:String*/id)");
}
};
draw2d.Canvas.IMAGE_BASE_URL="";
draw2d.Canvas.prototype.type="draw2d.Canvas";
draw2d.Canvas.prototype.construct=function(_558f){
this.canvasId=_558f;
this.html=document.getElementById(this.canvasId);
this.scrollArea=document.body.parentNode;
};
draw2d.Canvas.prototype.setViewPort=function(divId){
this.scrollArea=document.getElementById(divId);
};
draw2d.Canvas.prototype.addFigure=function(_5591,xPos,yPos,_5594){
try{
if(this.enableSmoothFigureHandling===true){
if(_5591.timer<=0){
_5591.setAlpha(0.001);
}
var _5595=_5591;
var _5596=function(){
if(_5595.alpha<1){
_5595.setAlpha(Math.min(1,_5595.alpha+0.05));
}else{
window.clearInterval(_5595.timer);
_5595.timer=-1;
}
};
if(_5595.timer>0){
window.clearInterval(_5595.timer);
}
_5595.timer=window.setInterval(_5596,30);
}
_5591.setCanvas(this);
if(xPos&&yPos){
_5591.setPosition(xPos,yPos);
}
if(_5591 instanceof draw2d.Line){
this.canvasLines.add(_5591);
this.html.appendChild(_5591.getHTMLElement());
}else{
var obj=this.canvasLines.getFirstElement();
if(obj===null){
this.html.appendChild(_5591.getHTMLElement());
}else{
this.html.insertBefore(_5591.getHTMLElement(),obj.getHTMLElement());
}
}
if(!_5594){
_5591.paint();
}
}
catch(e){
pushErrorStack(e,"draw2d.Canvas.prototype.addFigure= function( /*:draw2d.Figure*/figure,/*:int*/ xPos,/*:int*/ yPos, /*:boolean*/ avoidPaint)");
}
};
draw2d.Canvas.prototype.removeFigure=function(_5598){
if(this.enableSmoothFigureHandling===true){
var oThis=this;
var _559a=_5598;
var _559b=function(){
if(_559a.alpha>0){
_559a.setAlpha(Math.max(0,_559a.alpha-0.05));
}else{
window.clearInterval(_559a.timer);
_559a.timer=-1;
oThis.html.removeChild(_559a.html);
_559a.setCanvas(null);
}
};
if(_559a.timer>0){
window.clearInterval(_559a.timer);
}
_559a.timer=window.setInterval(_559b,20);
}else{
this.html.removeChild(_5598.html);
_5598.setCanvas(null);
}
if(_5598 instanceof draw2d.Line){
this.canvasLines.remove(_5598);
}
};
draw2d.Canvas.prototype.getEnableSmoothFigureHandling=function(){
return this.enableSmoothFigureHandling;
};
draw2d.Canvas.prototype.setEnableSmoothFigureHandling=function(flag){
this.enableSmoothFigureHandling=flag;
};
draw2d.Canvas.prototype.getWidth=function(){
return parseInt(this.html.style.width);
};
draw2d.Canvas.prototype.setWidth=function(width){
if(this.scrollArea!==null){
this.scrollArea.style.width=width+"px";
}else{
this.html.style.width=width+"px";
}
};
draw2d.Canvas.prototype.getHeight=function(){
return parseInt(this.html.style.height);
};
draw2d.Canvas.prototype.setHeight=function(_559e){
if(this.scrollArea!==null){
this.scrollArea.style.height=_559e+"px";
}else{
this.html.style.height=_559e+"px";
}
};
draw2d.Canvas.prototype.setBackgroundImage=function(_559f,_55a0){
if(_559f!==null){
if(_55a0){
this.html.style.background="transparent url("+_559f+") ";
}else{
this.html.style.background="transparent url("+_559f+") no-repeat";
}
}else{
this.html.style.background="transparent";
}
};
draw2d.Canvas.prototype.getY=function(){
return this.y;
};
draw2d.Canvas.prototype.getX=function(){
return this.x;
};
draw2d.Canvas.prototype.getAbsoluteY=function(){
var el=this.html;
var ot=el.offsetTop;
while((el=el.offsetParent)!==null){
ot+=el.offsetTop;
}
return ot;
};
draw2d.Canvas.prototype.getAbsoluteX=function(){
var el=this.html;
var ol=el.offsetLeft;
while((el=el.offsetParent)!==null){
ol+=el.offsetLeft;
}
return ol;
};
draw2d.Canvas.prototype.getScrollLeft=function(){
return this.scrollArea.scrollLeft;
};
draw2d.Canvas.prototype.getScrollTop=function(){
return this.scrollArea.scrollTop;
};
draw2d.Workflow=function(id){
try{
if(!id){
return;
}
this.menu=null;
this.gridWidthX=10;
this.gridWidthY=10;
this.snapToGridHelper=null;
this.verticalSnapToHelperLine=null;
this.horizontalSnapToHelperLine=null;
this.snapToGeometryHelper=null;
this.figures=new draw2d.ArrayList();
this.lines=new draw2d.ArrayList();
this.commonPorts=new draw2d.ArrayList();
this.dropTargets=new draw2d.ArrayList();
this.compartments=new draw2d.ArrayList();
this.selectionListeners=new draw2d.ArrayList();
this.dialogs=new draw2d.ArrayList();
this.toolPalette=null;
this.dragging=false;
this.tooltip=null;
this.draggingLine=null;
this.draggingLineCommand=null;
this.commandStack=new draw2d.CommandStack();
this.oldScrollPosLeft=0;
this.oldScrollPosTop=0;
this.currentSelection=null;
this.currentMenu=null;
this.connectionLine=new draw2d.Line();
this.resizeHandleStart=new draw2d.LineStartResizeHandle(this);
this.resizeHandleEnd=new draw2d.LineEndResizeHandle(this);
this.resizeHandle1=new draw2d.ResizeHandle(this,1);
this.resizeHandle2=new draw2d.ResizeHandle(this,2);
this.resizeHandle3=new draw2d.ResizeHandle(this,3);
this.resizeHandle4=new draw2d.ResizeHandle(this,4);
this.resizeHandle5=new draw2d.ResizeHandle(this,5);
this.resizeHandle6=new draw2d.ResizeHandle(this,6);
this.resizeHandle7=new draw2d.ResizeHandle(this,7);
this.resizeHandle8=new draw2d.ResizeHandle(this,8);
this.resizeHandleHalfWidth=parseInt(this.resizeHandle2.getWidth()/2);
draw2d.Canvas.call(this,id);
this.setPanning(false);
if(this.html!==null){
this.html.style.backgroundImage="url(grid_10.png)";
this.html.className="Workflow";
oThis=this;
this.html.tabIndex="0";
var _5a5b=function(){
var _5a5c=arguments[0]||window.event;
var diffX=_5a5c.clientX;
var diffY=_5a5c.clientY;
var _5a5f=oThis.getScrollLeft();
var _5a60=oThis.getScrollTop();
var _5a61=oThis.getAbsoluteX();
var _5a62=oThis.getAbsoluteY();
if(oThis.getBestFigure(diffX+_5a5f-_5a61,diffY+_5a60-_5a62)!==null){
return;
}
var line=oThis.getBestLine(diffX+_5a5f-_5a61,diffY+_5a60-_5a62,null);
if(line!==null){
line.onContextMenu(diffX+_5a5f-_5a61,diffY+_5a60-_5a62);
}else{
oThis.onContextMenu(diffX+_5a5f-_5a61,diffY+_5a60-_5a62);
}
};
this.html.oncontextmenu=function(){
return false;
};
var oThis=this;
var _5a65=function(event){
var ctrl=event.ctrlKey;
oThis.onKeyDown(event.keyCode,ctrl);
};
var _5a68=function(){
var _5a69=arguments[0]||window.event;
if(_5a69.returnValue==false){
return;
}
var diffX=_5a69.clientX;
var diffY=_5a69.clientY;
var _5a6c=oThis.getScrollLeft();
var _5a6d=oThis.getScrollTop();
var _5a6e=oThis.getAbsoluteX();
var _5a6f=oThis.getAbsoluteY();
oThis.onMouseDown(diffX+_5a6c-_5a6e,diffY+_5a6d-_5a6f);
};
var _5a70=function(){
var _5a71=arguments[0]||window.event;
if(oThis.currentMenu!==null){
oThis.removeFigure(oThis.currentMenu);
oThis.currentMenu=null;
}
if(_5a71.button==2){
return;
}
var diffX=_5a71.clientX;
var diffY=_5a71.clientY;
var _5a74=oThis.getScrollLeft();
var _5a75=oThis.getScrollTop();
var _5a76=oThis.getAbsoluteX();
var _5a77=oThis.getAbsoluteY();
oThis.onMouseUp(diffX+_5a74-_5a76,diffY+_5a75-_5a77);
};
var _5a78=function(){
var _5a79=arguments[0]||window.event;
var diffX=_5a79.clientX;
var diffY=_5a79.clientY;
var _5a7c=oThis.getScrollLeft();
var _5a7d=oThis.getScrollTop();
var _5a7e=oThis.getAbsoluteX();
var _5a7f=oThis.getAbsoluteY();
oThis.currentMouseX=diffX+_5a7c-_5a7e;
oThis.currentMouseY=diffY+_5a7d-_5a7f;
var obj=oThis.getBestFigure(oThis.currentMouseX,oThis.currentMouseY);
if(draw2d.Drag.currentHover!==null&&obj===null){
var _5a81=new draw2d.DragDropEvent();
_5a81.initDragDropEvent("mouseleave",false,oThis);
draw2d.Drag.currentHover.dispatchEvent(_5a81);
}else{
var diffX=_5a79.clientX;
var diffY=_5a79.clientY;
var _5a7c=oThis.getScrollLeft();
var _5a7d=oThis.getScrollTop();
var _5a7e=oThis.getAbsoluteX();
var _5a7f=oThis.getAbsoluteY();
oThis.onMouseMove(diffX+_5a7c-_5a7e,diffY+_5a7d-_5a7f);
}
if(obj===null){
draw2d.Drag.currentHover=null;
}
if(oThis.tooltip!==null){
if(Math.abs(oThis.currentTooltipX-oThis.currentMouseX)>10||Math.abs(oThis.currentTooltipY-oThis.currentMouseY)>10){
oThis.showTooltip(null);
}
}
};
var _5a82=function(_5a83){
var _5a83=arguments[0]||window.event;
var diffX=_5a83.clientX;
var diffY=_5a83.clientY;
var _5a86=oThis.getScrollLeft();
var _5a87=oThis.getScrollTop();
var _5a88=oThis.getAbsoluteX();
var _5a89=oThis.getAbsoluteY();
var line=oThis.getBestLine(diffX+_5a86-_5a88,diffY+_5a87-_5a89,null);
if(line!==null){
line.onDoubleClick();
}
};
if(this.html.addEventListener){
this.html.addEventListener("contextmenu",_5a5b,false);
this.html.addEventListener("mousemove",_5a78,false);
this.html.addEventListener("mouseup",_5a70,false);
this.html.addEventListener("mousedown",_5a68,false);
this.html.addEventListener("keydown",_5a65,false);
this.html.addEventListener("dblclick",_5a82,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("oncontextmenu",_5a5b);
this.html.attachEvent("onmousemove",_5a78);
this.html.attachEvent("onmousedown",_5a68);
this.html.attachEvent("onmouseup",_5a70);
this.html.attachEvent("onkeydown",_5a65);
this.html.attachEvent("ondblclick",_5a82);
}else{
throw "Open-jACOB Draw2D not supported in this browser.";
}
}
}
}
catch(e){
pushErrorStack(e,"draw2d.Workflow=function(/*:String*/id)");
}
};
draw2d.Workflow.prototype=new draw2d.Canvas();
draw2d.Workflow.prototype.type="draw2d.Workflow";
draw2d.Workflow.COLOR_GREEN=new draw2d.Color(0,255,0);
draw2d.Workflow.prototype.clear=function(){
this.scrollTo(0,0,true);
this.gridWidthX=10;
this.gridWidthY=10;
this.snapToGridHelper=null;
this.verticalSnapToHelperLine=null;
this.horizontalSnapToHelperLine=null;
var _5a8b=this.getDocument();
var _5a8c=_5a8b.getLines().clone();
for(var i=0;i<_5a8c.getSize();i++){
(new draw2d.CommandDelete(_5a8c.get(i))).execute();
}
var _5a8e=_5a8b.getFigures().clone();
for(var i=0;i<_5a8e.getSize();i++){
(new draw2d.CommandDelete(_5a8e.get(i))).execute();
}
this.commonPorts.removeAllElements();
this.dropTargets.removeAllElements();
this.compartments.removeAllElements();
this.selectionListeners.removeAllElements();
this.dialogs.removeAllElements();
this.commandStack=new draw2d.CommandStack();
this.currentSelection=null;
this.currentMenu=null;
draw2d.Drag.clearCurrent();
};
draw2d.Workflow.prototype.onScroll=function(){
var _5a8f=this.getScrollLeft();
var _5a90=this.getScrollTop();
var _5a91=_5a8f-this.oldScrollPosLeft;
var _5a92=_5a90-this.oldScrollPosTop;
for(var i=0;i<this.figures.getSize();i++){
var _5a94=this.figures.get(i);
if(_5a94.hasFixedPosition&&_5a94.hasFixedPosition()==true){
_5a94.setPosition(_5a94.getX()+_5a91,_5a94.getY()+_5a92);
}
}
this.oldScrollPosLeft=_5a8f;
this.oldScrollPosTop=_5a90;
};
draw2d.Workflow.prototype.setPanning=function(flag){
this.panning=flag;
if(flag){
this.html.style.cursor="move";
}else{
this.html.style.cursor="default";
}
};
draw2d.Workflow.prototype.scrollTo=function(x,y,fast){
if(fast){
this.scrollArea.scrollLeft=x;
this.scrollArea.scrollTop=y;
}else{
var steps=40;
var xStep=(x-this.getScrollLeft())/steps;
var yStep=(y-this.getScrollTop())/steps;
var oldX=this.getScrollLeft();
var oldY=this.getScrollTop();
for(var i=0;i<steps;i++){
this.scrollArea.scrollLeft=oldX+(xStep*i);
this.scrollArea.scrollTop=oldY+(yStep*i);
}
}
};
draw2d.Workflow.prototype.showTooltip=function(_5a9f,_5aa0){
if(this.tooltip!==null){
this.removeFigure(this.tooltip);
this.tooltip=null;
if(this.tooltipTimer>=0){
window.clearTimeout(this.tooltipTimer);
this.tooltipTimer=-1;
}
}
this.tooltip=_5a9f;
if(this.tooltip!==null){
this.currentTooltipX=this.currentMouseX;
this.currentTooltipY=this.currentMouseY;
this.addFigure(this.tooltip,this.currentTooltipX+10,this.currentTooltipY+10);
var oThis=this;
var _5aa2=function(){
oThis.tooltipTimer=-1;
oThis.showTooltip(null);
};
if(_5aa0==true){
this.tooltipTimer=window.setTimeout(_5aa2,5000);
}
}
};
draw2d.Workflow.prototype.showDialog=function(_5aa3,xPos,yPos){
if(xPos){
this.addFigure(_5aa3,xPos,yPos);
}else{
this.addFigure(_5aa3,200,100);
}
this.dialogs.add(_5aa3);
};
draw2d.Workflow.prototype.showMenu=function(menu,xPos,yPos){
if(this.menu!==null){
this.html.removeChild(this.menu.getHTMLElement());
this.menu.setWorkflow();
}
this.menu=menu;
if(this.menu!==null){
this.menu.setWorkflow(this);
this.menu.setPosition(xPos,yPos);
this.html.appendChild(this.menu.getHTMLElement());
this.menu.paint();
}
};
draw2d.Workflow.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
this.showMenu(menu,x,y);
}
};
draw2d.Workflow.prototype.getContextMenu=function(){
return null;
};
draw2d.Workflow.prototype.setToolWindow=function(_5aac,x,y){
this.toolPalette=_5aac;
if(y){
this.addFigure(_5aac,x,y);
}else{
this.addFigure(_5aac,20,20);
}
this.dialogs.add(_5aac);
};
draw2d.Workflow.prototype.setSnapToGrid=function(flag){
if(flag){
this.snapToGridHelper=new draw2d.SnapToGrid(this);
}else{
this.snapToGridHelper=null;
}
};
draw2d.Workflow.prototype.setSnapToGeometry=function(flag){
if(flag){
this.snapToGeometryHelper=new draw2d.SnapToGeometry(this);
}else{
this.snapToGeometryHelper=null;
}
};
draw2d.Workflow.prototype.setGridWidth=function(dx,dy){
this.gridWidthX=dx;
this.gridWidthY=dy;
};
draw2d.Workflow.prototype.addFigure=function(_5ab3,xPos,yPos){
try{
draw2d.Canvas.prototype.addFigure.call(this,_5ab3,xPos,yPos,true);
_5ab3.setWorkflow(this);
var _5ab6=this;
if(_5ab3 instanceof draw2d.CompartmentFigure){
this.compartments.add(_5ab3);
}
if(_5ab3 instanceof draw2d.Line){
this.lines.add(_5ab3);
}else{
this.figures.add(_5ab3);
_5ab3.draggable.addEventListener("drag",function(_5ab7){
var _5ab8=_5ab6.getFigure(_5ab7.target.element.id);
if(_5ab8===null){
return;
}
if(_5ab8.isSelectable()==false){
return;
}
_5ab6.moveResizeHandles(_5ab8);
});
}
_5ab3.paint();
this.setDocumentDirty();
}
catch(e){
pushErrorStack(e,"draw2d.Workflow.prototype.addFigure=function(/*:draw2d.Figure*/ figure ,/*:int*/ xPos, /*:int*/ yPos)");
}
};
draw2d.Workflow.prototype.removeFigure=function(_5ab9){
draw2d.Canvas.prototype.removeFigure.call(this,_5ab9);
this.figures.remove(_5ab9);
this.lines.remove(_5ab9);
this.dialogs.remove(_5ab9);
_5ab9.setWorkflow(null);
if(_5ab9 instanceof draw2d.CompartmentFigure){
this.compartments.remove(_5ab9);
}
if(_5ab9 instanceof draw2d.Connection){
_5ab9.disconnect();
}
if(this.currentSelection==_5ab9){
this.setCurrentSelection(null);
}
this.setDocumentDirty();
};
draw2d.Workflow.prototype.moveFront=function(_5aba){
this.html.removeChild(_5aba.getHTMLElement());
this.html.appendChild(_5aba.getHTMLElement());
};
draw2d.Workflow.prototype.moveBack=function(_5abb){
this.html.removeChild(_5abb.getHTMLElement());
this.html.insertBefore(_5abb.getHTMLElement(),this.html.firstChild);
};
draw2d.Workflow.prototype.getBestCompartmentFigure=function(x,y,_5abe){
var _5abf=null;
for(var i=0;i<this.figures.getSize();i++){
var _5ac1=this.figures.get(i);
if((_5ac1 instanceof draw2d.CompartmentFigure)&&_5ac1.isOver(x,y)==true&&_5ac1!=_5abe){
if(_5abf===null){
_5abf=_5ac1;
}else{
if(_5abf.getZOrder()<_5ac1.getZOrder()){
_5abf=_5ac1;
}
}
}
}
return _5abf;
};
draw2d.Workflow.prototype.getBestFigure=function(x,y,_5ac4){
var _5ac5=null;
for(var i=0;i<this.figures.getSize();i++){
var _5ac7=this.figures.get(i);
if(_5ac7.isOver(x,y)==true&&_5ac7!=_5ac4){
if(_5ac5===null){
_5ac5=_5ac7;
}else{
if(_5ac5.getZOrder()<_5ac7.getZOrder()){
_5ac5=_5ac7;
}
}
}
}
return _5ac5;
};
draw2d.Workflow.prototype.getBestLine=function(x,y,_5aca){
var _5acb=null;
var count=this.lines.getSize();
for(var i=0;i<count;i++){
var line=this.lines.get(i);
if(line.containsPoint(x,y)==true&&line!=_5aca){
if(_5acb===null){
_5acb=line;
}else{
if(_5acb.getZOrder()<line.getZOrder()){
_5acb=line;
}
}
}
}
return _5acb;
};
draw2d.Workflow.prototype.getFigure=function(id){
for(var i=0;i<this.figures.getSize();i++){
var _5ad1=this.figures.get(i);
if(_5ad1.id==id){
return _5ad1;
}
}
return null;
};
draw2d.Workflow.prototype.getFigures=function(){
return this.figures;
};
draw2d.Workflow.prototype.getDocument=function(){
return new draw2d.CanvasDocument(this);
};
draw2d.Workflow.prototype.addSelectionListener=function(w){
if(w!==null){
if(w.onSelectionChanged){
this.selectionListeners.add(w);
}else{
throw "Object doesn't implement required callback method [onSelectionChanged]";
}
}
};
draw2d.Workflow.prototype.removeSelectionListener=function(w){
this.selectionListeners.remove(w);
};
draw2d.Workflow.prototype.setCurrentSelection=function(_5ad4){
if(_5ad4===null||this.currentSelection!=_5ad4){
this.hideResizeHandles();
this.hideLineResizeHandles();
}
this.currentSelection=_5ad4;
for(var i=0;i<this.selectionListeners.getSize();i++){
var w=this.selectionListeners.get(i);
if(w.onSelectionChanged){
w.onSelectionChanged(this.currentSelection,this.currentSelection?this.currentSelection.getModel():null);
}
}
if(_5ad4 instanceof draw2d.Line){
this.showLineResizeHandles(_5ad4);
if(!(_5ad4 instanceof draw2d.Connection)){
this.draggingLineCommand=line.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.MOVE));
if(this.draggingLineCommand!==null){
this.draggingLine=_5ad4;
}
}
}
};
draw2d.Workflow.prototype.getCurrentSelection=function(){
return this.currentSelection;
};
draw2d.Workflow.prototype.getLine=function(id){
var count=this.lines.getSize();
for(var i=0;i<count;i++){
var line=this.lines.get(i);
if(line.getId()==id){
return line;
}
}
return null;
};
draw2d.Workflow.prototype.getLines=function(){
return this.lines;
};
draw2d.Workflow.prototype.registerPort=function(port){
port.draggable.targets=this.dropTargets;
this.commonPorts.add(port);
this.dropTargets.add(port.dropable);
};
draw2d.Workflow.prototype.unregisterPort=function(port){
port.draggable.targets=null;
this.commonPorts.remove(port);
this.dropTargets.remove(port.dropable);
};
draw2d.Workflow.prototype.getCommandStack=function(){
return this.commandStack;
};
draw2d.Workflow.prototype.showConnectionLine=function(x1,y1,x2,y2){
this.connectionLine.setStartPoint(x1,y1);
this.connectionLine.setEndPoint(x2,y2);
if(this.connectionLine.canvas===null){
draw2d.Canvas.prototype.addFigure.call(this,this.connectionLine);
}
};
draw2d.Workflow.prototype.hideConnectionLine=function(){
if(this.connectionLine.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.connectionLine);
}
};
draw2d.Workflow.prototype.showLineResizeHandles=function(_5ae1){
var _5ae2=this.resizeHandleStart.getWidth()/2;
var _5ae3=this.resizeHandleStart.getHeight()/2;
var _5ae4=_5ae1.getStartPoint();
var _5ae5=_5ae1.getEndPoint();
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandleStart,_5ae4.x-_5ae2,_5ae4.y-_5ae2);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandleEnd,_5ae5.x-_5ae2,_5ae5.y-_5ae2);
this.resizeHandleStart.setCanDrag(_5ae1.isResizeable());
this.resizeHandleEnd.setCanDrag(_5ae1.isResizeable());
if(_5ae1.isResizeable()){
this.resizeHandleStart.setBackgroundColor(draw2d.Workflow.COLOR_GREEN);
this.resizeHandleEnd.setBackgroundColor(draw2d.Workflow.COLOR_GREEN);
this.resizeHandleStart.draggable.targets=this.dropTargets;
this.resizeHandleEnd.draggable.targets=this.dropTargets;
}else{
this.resizeHandleStart.setBackgroundColor(null);
this.resizeHandleEnd.setBackgroundColor(null);
}
};
draw2d.Workflow.prototype.hideLineResizeHandles=function(){
if(this.resizeHandleStart.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandleStart);
}
if(this.resizeHandleEnd.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandleEnd);
}
};
draw2d.Workflow.prototype.showResizeHandles=function(_5ae6){
this.hideLineResizeHandles();
this.hideResizeHandles();
if(this.getEnableSmoothFigureHandling()==true&&this.getCurrentSelection()!=_5ae6){
this.resizeHandle1.setAlpha(0.01);
this.resizeHandle2.setAlpha(0.01);
this.resizeHandle3.setAlpha(0.01);
this.resizeHandle4.setAlpha(0.01);
this.resizeHandle5.setAlpha(0.01);
this.resizeHandle6.setAlpha(0.01);
this.resizeHandle7.setAlpha(0.01);
this.resizeHandle8.setAlpha(0.01);
}
var _5ae7=this.resizeHandle1.getWidth();
var _5ae8=this.resizeHandle1.getHeight();
var _5ae9=_5ae6.getHeight();
var _5aea=_5ae6.getWidth();
var xPos=_5ae6.getX();
var yPos=_5ae6.getY();
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle1,xPos-_5ae7,yPos-_5ae8);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle3,xPos+_5aea,yPos-_5ae8);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle5,xPos+_5aea,yPos+_5ae9);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle7,xPos-_5ae7,yPos+_5ae9);
this.moveFront(this.resizeHandle1);
this.moveFront(this.resizeHandle3);
this.moveFront(this.resizeHandle5);
this.moveFront(this.resizeHandle7);
this.resizeHandle1.setCanDrag(_5ae6.isResizeable());
this.resizeHandle3.setCanDrag(_5ae6.isResizeable());
this.resizeHandle5.setCanDrag(_5ae6.isResizeable());
this.resizeHandle7.setCanDrag(_5ae6.isResizeable());
if(_5ae6.isResizeable()){
var green=new draw2d.Color(0,255,0);
this.resizeHandle1.setBackgroundColor(green);
this.resizeHandle3.setBackgroundColor(green);
this.resizeHandle5.setBackgroundColor(green);
this.resizeHandle7.setBackgroundColor(green);
}else{
this.resizeHandle1.setBackgroundColor(null);
this.resizeHandle3.setBackgroundColor(null);
this.resizeHandle5.setBackgroundColor(null);
this.resizeHandle7.setBackgroundColor(null);
}
if(_5ae6.isStrechable()&&_5ae6.isResizeable()){
this.resizeHandle2.setCanDrag(_5ae6.isResizeable());
this.resizeHandle4.setCanDrag(_5ae6.isResizeable());
this.resizeHandle6.setCanDrag(_5ae6.isResizeable());
this.resizeHandle8.setCanDrag(_5ae6.isResizeable());
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle2,xPos+(_5aea/2)-this.resizeHandleHalfWidth,yPos-_5ae8);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle4,xPos+_5aea,yPos+(_5ae9/2)-(_5ae8/2));
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle6,xPos+(_5aea/2)-this.resizeHandleHalfWidth,yPos+_5ae9);
draw2d.Canvas.prototype.addFigure.call(this,this.resizeHandle8,xPos-_5ae7,yPos+(_5ae9/2)-(_5ae8/2));
this.moveFront(this.resizeHandle2);
this.moveFront(this.resizeHandle4);
this.moveFront(this.resizeHandle6);
this.moveFront(this.resizeHandle8);
}
};
draw2d.Workflow.prototype.hideResizeHandles=function(){
if(this.resizeHandle1.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle1);
}
if(this.resizeHandle2.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle2);
}
if(this.resizeHandle3.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle3);
}
if(this.resizeHandle4.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle4);
}
if(this.resizeHandle5.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle5);
}
if(this.resizeHandle6.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle6);
}
if(this.resizeHandle7.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle7);
}
if(this.resizeHandle8.canvas!==null){
draw2d.Canvas.prototype.removeFigure.call(this,this.resizeHandle8);
}
};
draw2d.Workflow.prototype.moveResizeHandles=function(_5aee){
var _5aef=this.resizeHandle1.getWidth();
var _5af0=this.resizeHandle1.getHeight();
var _5af1=_5aee.getHeight();
var _5af2=_5aee.getWidth();
var xPos=_5aee.getX();
var yPos=_5aee.getY();
this.resizeHandle1.setPosition(xPos-_5aef,yPos-_5af0);
this.resizeHandle3.setPosition(xPos+_5af2,yPos-_5af0);
this.resizeHandle5.setPosition(xPos+_5af2,yPos+_5af1);
this.resizeHandle7.setPosition(xPos-_5aef,yPos+_5af1);
if(_5aee.isStrechable()){
this.resizeHandle2.setPosition(xPos+(_5af2/2)-this.resizeHandleHalfWidth,yPos-_5af0);
this.resizeHandle4.setPosition(xPos+_5af2,yPos+(_5af1/2)-(_5af0/2));
this.resizeHandle6.setPosition(xPos+(_5af2/2)-this.resizeHandleHalfWidth,yPos+_5af1);
this.resizeHandle8.setPosition(xPos-_5aef,yPos+(_5af1/2)-(_5af0/2));
}
};
draw2d.Workflow.prototype.onMouseDown=function(x,y){
this.dragging=true;
this.mouseDownPosX=x;
this.mouseDownPosY=y;
if(this.toolPalette!==null&&this.toolPalette.getActiveTool()!==null){
this.toolPalette.getActiveTool().execute(x,y);
}
this.showMenu(null);
var line=this.getBestLine(x,y);
if(line!==null&&line.isSelectable()){
this.setCurrentSelection(line);
}else{
this.setCurrentSelection(null);
}
};
draw2d.Workflow.prototype.onMouseUp=function(x,y){
this.dragging=false;
if(this.draggingLineCommand!==null){
this.getCommandStack().execute(this.draggingLineCommand);
this.draggingLine=null;
this.draggingLineCommand=null;
}
};
draw2d.Workflow.prototype.onMouseMove=function(x,y){
if(this.dragging===true&&this.draggingLine!==null){
var diffX=x-this.mouseDownPosX;
var diffY=y-this.mouseDownPosY;
this.draggingLine.startX=this.draggingLine.getStartX()+diffX;
this.draggingLine.startY=this.draggingLine.getStartY()+diffY;
this.draggingLine.setEndPoint(this.draggingLine.getEndX()+diffX,this.draggingLine.getEndY()+diffY);
this.mouseDownPosX=x;
this.mouseDownPosY=y;
this.showLineResizeHandles(this.currentSelection);
}else{
if(this.dragging===true&&this.panning===true){
var diffX=x-this.mouseDownPosX;
var diffY=y-this.mouseDownPosY;
this.scrollTo(this.getScrollLeft()-diffX,this.getScrollTop()-diffY,true);
this.onScroll();
}
}
};
draw2d.Workflow.prototype.onKeyDown=function(_5afe,ctrl){
if(_5afe==46&&this.currentSelection!==null){
this.commandStack.execute(this.currentSelection.createCommand(new draw2d.EditPolicy(draw2d.EditPolicy.DELETE)));
}else{
if(_5afe==90&&ctrl){
this.commandStack.undo();
}else{
if(_5afe==89&&ctrl){
this.commandStack.redo();
}
}
}
};
draw2d.Workflow.prototype.setDocumentDirty=function(){
try{
for(var i=0;i<this.dialogs.getSize();i++){
var d=this.dialogs.get(i);
if(d!==null&&d.onSetDocumentDirty){
d.onSetDocumentDirty();
}
}
if(this.snapToGeometryHelper!==null){
this.snapToGeometryHelper.onSetDocumentDirty();
}
if(this.snapToGridHelper!==null){
this.snapToGridHelper.onSetDocumentDirty();
}
}
catch(e){
pushErrorStack(e,"draw2d.Workflow.prototype.setDocumentDirty=function()");
}
};
draw2d.Workflow.prototype.snapToHelper=function(_5b02,pos){
if(this.snapToGeometryHelper!==null){
if(_5b02 instanceof draw2d.ResizeHandle){
var _5b04=_5b02.getSnapToGridAnchor();
pos.x+=_5b04.x;
pos.y+=_5b04.y;
var _5b05=new draw2d.Point(pos.x,pos.y);
var _5b06=_5b02.getSnapToDirection();
var _5b07=this.snapToGeometryHelper.snapPoint(_5b06,pos,_5b05);
if((_5b06&draw2d.SnapToHelper.EAST_WEST)&&!(_5b07&draw2d.SnapToHelper.EAST_WEST)){
this.showSnapToHelperLineVertical(_5b05.x);
}else{
this.hideSnapToHelperLineVertical();
}
if((_5b06&draw2d.SnapToHelper.NORTH_SOUTH)&&!(_5b07&draw2d.SnapToHelper.NORTH_SOUTH)){
this.showSnapToHelperLineHorizontal(_5b05.y);
}else{
this.hideSnapToHelperLineHorizontal();
}
_5b05.x-=_5b04.x;
_5b05.y-=_5b04.y;
return _5b05;
}else{
var _5b08=new draw2d.Dimension(pos.x,pos.y,_5b02.getWidth(),_5b02.getHeight());
var _5b05=new draw2d.Dimension(pos.x,pos.y,_5b02.getWidth(),_5b02.getHeight());
var _5b06=draw2d.SnapToHelper.NSEW;
var _5b07=this.snapToGeometryHelper.snapRectangle(_5b08,_5b05);
if((_5b06&draw2d.SnapToHelper.WEST)&&!(_5b07&draw2d.SnapToHelper.WEST)){
this.showSnapToHelperLineVertical(_5b05.x);
}else{
if((_5b06&draw2d.SnapToHelper.EAST)&&!(_5b07&draw2d.SnapToHelper.EAST)){
this.showSnapToHelperLineVertical(_5b05.getX()+_5b05.getWidth());
}else{
this.hideSnapToHelperLineVertical();
}
}
if((_5b06&draw2d.SnapToHelper.NORTH)&&!(_5b07&draw2d.SnapToHelper.NORTH)){
this.showSnapToHelperLineHorizontal(_5b05.y);
}else{
if((_5b06&draw2d.SnapToHelper.SOUTH)&&!(_5b07&draw2d.SnapToHelper.SOUTH)){
this.showSnapToHelperLineHorizontal(_5b05.getY()+_5b05.getHeight());
}else{
this.hideSnapToHelperLineHorizontal();
}
}
return _5b05.getTopLeft();
}
}else{
if(this.snapToGridHelper!==null){
var _5b04=_5b02.getSnapToGridAnchor();
pos.x=pos.x+_5b04.x;
pos.y=pos.y+_5b04.y;
var _5b05=new draw2d.Point(pos.x,pos.y);
this.snapToGridHelper.snapPoint(0,pos,_5b05);
_5b05.x=_5b05.x-_5b04.x;
_5b05.y=_5b05.y-_5b04.y;
return _5b05;
}
}
return pos;
};
draw2d.Workflow.prototype.showSnapToHelperLineHorizontal=function(_5b09){
if(this.horizontalSnapToHelperLine===null){
this.horizontalSnapToHelperLine=new draw2d.Line();
this.horizontalSnapToHelperLine.setColor(new draw2d.Color(175,175,255));
this.addFigure(this.horizontalSnapToHelperLine);
}
this.horizontalSnapToHelperLine.setStartPoint(0,_5b09);
this.horizontalSnapToHelperLine.setEndPoint(this.getWidth(),_5b09);
};
draw2d.Workflow.prototype.showSnapToHelperLineVertical=function(_5b0a){
if(this.verticalSnapToHelperLine===null){
this.verticalSnapToHelperLine=new draw2d.Line();
this.verticalSnapToHelperLine.setColor(new draw2d.Color(175,175,255));
this.addFigure(this.verticalSnapToHelperLine);
}
this.verticalSnapToHelperLine.setStartPoint(_5b0a,0);
this.verticalSnapToHelperLine.setEndPoint(_5b0a,this.getHeight());
};
draw2d.Workflow.prototype.hideSnapToHelperLines=function(){
this.hideSnapToHelperLineHorizontal();
this.hideSnapToHelperLineVertical();
};
draw2d.Workflow.prototype.hideSnapToHelperLineHorizontal=function(){
if(this.horizontalSnapToHelperLine!==null){
this.removeFigure(this.horizontalSnapToHelperLine);
this.horizontalSnapToHelperLine=null;
}
};
draw2d.Workflow.prototype.hideSnapToHelperLineVertical=function(){
if(this.verticalSnapToHelperLine!==null){
this.removeFigure(this.verticalSnapToHelperLine);
this.verticalSnapToHelperLine=null;
}
};
draw2d.WindowFigure=function(title){
this.title=title;
this.titlebar=null;
draw2d.Figure.call(this);
this.setDeleteable(false);
this.setCanSnapToHelper(false);
this.setZOrder(draw2d.WindowFigure.ZOrderIndex);
};
draw2d.WindowFigure.prototype=new draw2d.Figure();
draw2d.WindowFigure.prototype.type=":draw2d.WindowFigure";
draw2d.WindowFigure.ZOrderIndex=50000;
draw2d.WindowFigure.setZOrderBaseIndex=function(index){
draw2d.WindowFigure.ZOrderBaseIndex=index;
};
draw2d.WindowFigure.prototype.hasFixedPosition=function(){
return true;
};
draw2d.WindowFigure.prototype.hasTitleBar=function(){
return true;
};
draw2d.WindowFigure.prototype.createHTMLElement=function(){
var item=draw2d.Figure.prototype.createHTMLElement.call(this);
item.style.margin="0px";
item.style.padding="0px";
item.style.border="1px solid black";
item.style.backgroundImage="url(window_bg.png)";
item.style.zIndex=draw2d.WindowFigure.ZOrderIndex;
item.style.cursor=null;
item.className="WindowFigure";
if(this.hasTitleBar()){
this.titlebar=document.createElement("div");
this.titlebar.style.position="absolute";
this.titlebar.style.left="0px";
this.titlebar.style.top="0px";
this.titlebar.style.width=this.getWidth()+"px";
this.titlebar.style.height="15px";
this.titlebar.style.margin="0px";
this.titlebar.style.padding="0px";
this.titlebar.style.font="normal 10px verdana";
this.titlebar.style.backgroundColor="blue";
this.titlebar.style.borderBottom="2px solid gray";
this.titlebar.style.whiteSpace="nowrap";
this.titlebar.style.textAlign="center";
this.titlebar.style.backgroundImage="url(window_toolbar.png)";
this.titlebar.className="WindowFigure_titlebar";
this.textNode=document.createTextNode(this.title);
this.titlebar.appendChild(this.textNode);
item.appendChild(this.titlebar);
}
return item;
};
draw2d.WindowFigure.prototype.setDocumentDirty=function(_6262){
};
draw2d.WindowFigure.prototype.onDragend=function(){
};
draw2d.WindowFigure.prototype.onDragstart=function(x,y){
if(this.titlebar===null){
return false;
}
if(this.canDrag===true&&x<parseInt(this.titlebar.style.width)&&y<parseInt(this.titlebar.style.height)){
return true;
}
return false;
};
draw2d.WindowFigure.prototype.isSelectable=function(){
return false;
};
draw2d.WindowFigure.prototype.setCanDrag=function(flag){
draw2d.Figure.prototype.setCanDrag.call(this,flag);
this.html.style.cursor="";
if(this.titlebar===null){
return;
}
if(flag){
this.titlebar.style.cursor="move";
}else{
this.titlebar.style.cursor="";
}
};
draw2d.WindowFigure.prototype.setWorkflow=function(_6266){
var _6267=this.workflow;
draw2d.Figure.prototype.setWorkflow.call(this,_6266);
if(_6267!==null){
_6267.removeSelectionListener(this);
}
if(this.workflow!==null){
this.workflow.addSelectionListener(this);
}
};
draw2d.WindowFigure.prototype.setDimension=function(w,h){
draw2d.Figure.prototype.setDimension.call(this,w,h);
if(this.titlebar!==null){
this.titlebar.style.width=this.getWidth()+"px";
}
};
draw2d.WindowFigure.prototype.setTitle=function(title){
this.title=title;
};
draw2d.WindowFigure.prototype.getMinWidth=function(){
return 50;
};
draw2d.WindowFigure.prototype.getMinHeight=function(){
return 50;
};
draw2d.WindowFigure.prototype.isResizeable=function(){
return false;
};
draw2d.WindowFigure.prototype.setAlpha=function(_626b){
};
draw2d.WindowFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
this.html.style.backgroundImage="";
}
};
draw2d.WindowFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
draw2d.WindowFigure.prototype.setLineWidth=function(w){
this.lineStroke=w;
this.html.style.border=this.lineStroke+"px solid black";
};
draw2d.WindowFigure.prototype.onSelectionChanged=function(_626f,model){
};
draw2d.Button=function(_4eb7,width,_4eb9){
this.x=0;
this.y=0;
this.width=24;
this.height=24;
this.id=draw2d.UUID.create();
this.enabled=true;
this.active=false;
this.palette=_4eb7;
this.html=this.createHTMLElement();
if(width!==undefined&&_4eb9!==undefined){
this.setDimension(width,_4eb9);
}else{
this.setDimension(24,24);
}
};
draw2d.Button.prototype.type="draw2d.Button";
draw2d.Button.prototype.dispose=function(){
};
draw2d.Button.prototype.getImageUrl=function(){
if(this.enabled){
return this.type+".png";
}else{
return this.type+"_disabled.png";
}
};
draw2d.Button.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height=this.width+"px";
item.style.width=this.height+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.outline="none";
if(this.getImageUrl()!==null){
item.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
item.style.backgroundImage="";
}
var oThis=this;
this.omousedown=function(event){
if(oThis.enabled){
oThis.setActive(true);
}
event.cancelBubble=true;
event.returnValue=false;
};
this.omouseup=function(event){
if(oThis.enabled){
oThis.setActive(false);
oThis.execute();
}
event.cancelBubble=true;
event.returnValue=false;
};
if(item.addEventListener){
item.addEventListener("mousedown",this.omousedown,false);
item.addEventListener("mouseup",this.omouseup,false);
}else{
if(item.attachEvent){
item.attachEvent("onmousedown",this.omousedown);
item.attachEvent("onmouseup",this.omouseup);
}
}
return item;
};
draw2d.Button.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
draw2d.Button.prototype.execute=function(){
};
draw2d.Button.prototype.setTooltip=function(_4ebe){
this.tooltip=_4ebe;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
draw2d.Button.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
this.active=flag;
if(flag===true){
this.html.style.border="2px inset";
}else{
this.html.style.border="0px";
}
};
draw2d.Button.prototype.isActive=function(){
return this.active;
};
draw2d.Button.prototype.setEnabled=function(flag){
this.enabled=flag;
if(this.getImageUrl()!==null){
this.html.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
this.html.style.backgroundImage="";
}
};
draw2d.Button.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
draw2d.Button.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
draw2d.Button.prototype.getWidth=function(){
return this.width;
};
draw2d.Button.prototype.getHeight=function(){
return this.height;
};
draw2d.Button.prototype.getY=function(){
return this.y;
};
draw2d.Button.prototype.getX=function(){
return this.x;
};
draw2d.Button.prototype.getPosition=function(){
return new draw2d.Point(this.x,this.y);
};
draw2d.Button.prototype.getToolPalette=function(){
return this.palette;
};
draw2d.ToggleButton=function(_4eac){
draw2d.Button.call(this,_4eac);
this.isDownFlag=false;
};
draw2d.ToggleButton.prototype=new draw2d.Button();
draw2d.ToggleButton.prototype.type="draw2d.ToggleButton";
draw2d.ToggleButton.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height="24px";
item.style.width="24px";
item.style.margin="0px";
item.style.padding="0px";
if(this.getImageUrl()!==null){
item.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
item.style.backgroundImage="";
}
var oThis=this;
this.omousedown=function(event){
if(oThis.enabled){
if(!oThis.isDown()){
draw2d.Button.prototype.setActive.call(oThis,true);
}
}
event.cancelBubble=true;
event.returnValue=false;
};
this.omouseup=function(event){
if(oThis.enabled){
if(oThis.isDown()){
draw2d.Button.prototype.setActive.call(oThis,false);
}
oThis.isDownFlag=!oThis.isDownFlag;
oThis.execute();
}
event.cancelBubble=true;
event.returnValue=false;
};
if(item.addEventListener){
item.addEventListener("mousedown",this.omousedown,false);
item.addEventListener("mouseup",this.omouseup,false);
}else{
if(item.attachEvent){
item.attachEvent("onmousedown",this.omousedown);
item.attachEvent("onmouseup",this.omouseup);
}
}
return item;
};
draw2d.ToggleButton.prototype.isDown=function(){
return this.isDownFlag;
};
draw2d.ToggleButton.prototype.setActive=function(flag){
draw2d.Button.prototype.setActive.call(this,flag);
this.isDownFlag=flag;
};
draw2d.ToggleButton.prototype.execute=function(){
};
draw2d.ToolGeneric=function(_628a){
this.x=0;
this.y=0;
this.enabled=true;
this.tooltip=null;
this.palette=_628a;
this.html=this.createHTMLElement();
this.setDimension(10,10);
};
draw2d.ToolGeneric.prototype.type="draw2d.ToolGeneric";
draw2d.ToolGeneric.prototype.dispose=function(){
};
draw2d.ToolGeneric.prototype.getImageUrl=function(){
if(this.enabled){
return this.type+".png";
}else{
return this.type+"_disabled.png";
}
};
draw2d.ToolGeneric.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height="24px";
item.style.width="24px";
item.style.margin="0px";
item.style.padding="0px";
if(this.getImageUrl()!==null){
item.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
item.style.backgroundImage="";
}
var oThis=this;
this.click=function(event){
if(oThis.enabled){
oThis.palette.setActiveTool(oThis);
}
event.cancelBubble=true;
event.returnValue=false;
};
if(item.addEventListener){
item.addEventListener("click",this.click,false);
}else{
if(item.attachEvent){
item.attachEvent("onclick",this.click);
}
}
return item;
};
draw2d.ToolGeneric.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
draw2d.ToolGeneric.prototype.execute=function(x,y){
if(this.enabled){
this.palette.setActiveTool(null);
}
};
draw2d.ToolGeneric.prototype.setTooltip=function(_6290){
this.tooltip=_6290;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
draw2d.ToolGeneric.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
if(flag===true){
this.html.style.border="2px inset";
}else{
this.html.style.border="0px";
}
};
draw2d.ToolGeneric.prototype.setEnabled=function(flag){
this.enabled=flag;
if(this.getImageUrl()!==null){
this.html.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
this.html.style.backgroundImage="";
}
};
draw2d.ToolGeneric.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
draw2d.ToolGeneric.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
draw2d.ToolGeneric.prototype.getWidth=function(){
return this.width;
};
draw2d.ToolGeneric.prototype.getHeight=function(){
return this.height;
};
draw2d.ToolGeneric.prototype.getY=function(){
return this.y;
};
draw2d.ToolGeneric.prototype.getX=function(){
return this.x;
};
draw2d.ToolGeneric.prototype.getPosition=function(){
return new draw2d.Point(this.x,this.y);
};
draw2d.ToolPalette=function(title){
draw2d.WindowFigure.call(this,title);
this.setDimension(75,400);
this.activeTool=null;
this.children={};
};
draw2d.ToolPalette.prototype=new draw2d.WindowFigure();
draw2d.ToolPalette.prototype.type="draw2d.ToolPalette";
draw2d.ToolPalette.prototype.dispose=function(){
draw2d.WindowFigure.prototype.dispose.call(this);
};
draw2d.ToolPalette.prototype.createHTMLElement=function(){
var item=draw2d.WindowFigure.prototype.createHTMLElement.call(this);
this.scrollarea=document.createElement("div");
this.scrollarea.style.position="absolute";
this.scrollarea.style.left="0px";
if(this.hasTitleBar()){
this.scrollarea.style.top="15px";
}else{
this.scrollarea.style.top="0px";
}
this.scrollarea.style.width=this.getWidth()+"px";
this.scrollarea.style.height="15px";
this.scrollarea.style.margin="0px";
this.scrollarea.style.padding="0px";
this.scrollarea.style.font="normal 10px verdana";
this.scrollarea.style.borderBottom="2px solid gray";
this.scrollarea.style.whiteSpace="nowrap";
this.scrollarea.style.textAlign="center";
this.scrollarea.style.overflowX="auto";
this.scrollarea.style.overflowY="auto";
this.scrollarea.style.overflow="auto";
item.appendChild(this.scrollarea);
return item;
};
draw2d.ToolPalette.prototype.setDimension=function(w,h){
draw2d.WindowFigure.prototype.setDimension.call(this,w,h);
if(this.scrollarea!==null){
this.scrollarea.style.width=this.getWidth()+"px";
if(this.hasTitleBar()){
this.scrollarea.style.height=(this.getHeight()-15)+"px";
}else{
this.scrollarea.style.height=this.getHeight()+"px";
}
}
};
draw2d.ToolPalette.prototype.addChild=function(item){
this.children[item.id]=item;
this.scrollarea.appendChild(item.getHTMLElement());
};
draw2d.ToolPalette.prototype.getChild=function(id){
return this.children[id];
};
draw2d.ToolPalette.prototype.getActiveTool=function(){
return this.activeTool;
};
draw2d.ToolPalette.prototype.setActiveTool=function(tool){
if(this.activeTool!=tool&&this.activeTool!==null){
this.activeTool.setActive(false);
}
if(tool!==null){
tool.setActive(true);
}
this.activeTool=tool;
};
draw2d.Dialog=function(title){
this.buttonbar=null;
if(title){
draw2d.WindowFigure.call(this,title);
}else{
draw2d.WindowFigure.call(this,"Dialog");
}
this.setDimension(400,300);
};
draw2d.Dialog.prototype=new draw2d.WindowFigure();
draw2d.Dialog.prototype.type="draw2d.Dialog";
draw2d.Dialog.prototype.createHTMLElement=function(){
var item=draw2d.WindowFigure.prototype.createHTMLElement.call(this);
var oThis=this;
this.buttonbar=document.createElement("div");
this.buttonbar.style.position="absolute";
this.buttonbar.style.left="0px";
this.buttonbar.style.bottom="0px";
this.buttonbar.style.width=this.getWidth()+"px";
this.buttonbar.style.height="30px";
this.buttonbar.style.margin="0px";
this.buttonbar.style.padding="0px";
this.buttonbar.style.font="normal 10px verdana";
this.buttonbar.style.backgroundColor="#c0c0c0";
this.buttonbar.style.borderBottom="2px solid gray";
this.buttonbar.style.whiteSpace="nowrap";
this.buttonbar.style.textAlign="center";
this.buttonbar.className="Dialog_buttonbar";
this.okbutton=document.createElement("button");
this.okbutton.style.border="1px solid gray";
this.okbutton.style.font="normal 10px verdana";
this.okbutton.style.width="80px";
this.okbutton.style.margin="5px";
this.okbutton.className="Dialog_okbutton";
this.okbutton.innerHTML="Ok";
this.okbutton.onclick=function(){
var error=null;
try{
oThis.onOk();
}
catch(e){
error=e;
}
oThis.workflow.removeFigure(oThis);
if(error!==null){
throw error;
}
};
this.buttonbar.appendChild(this.okbutton);
this.cancelbutton=document.createElement("button");
this.cancelbutton.innerHTML="Cancel";
this.cancelbutton.style.font="normal 10px verdana";
this.cancelbutton.style.border="1px solid gray";
this.cancelbutton.style.width="80px";
this.cancelbutton.style.margin="5px";
this.cancelbutton.className="Dialog_cancelbutton";
this.cancelbutton.onclick=function(){
var error=null;
try{
oThis.onCancel();
}
catch(e){
error=e;
}
oThis.workflow.removeFigure(oThis);
if(error!==null){
throw error;
}
};
this.buttonbar.appendChild(this.cancelbutton);
item.appendChild(this.buttonbar);
return item;
};
draw2d.Dialog.prototype.onOk=function(){
};
draw2d.Dialog.prototype.onCancel=function(){
};
draw2d.Dialog.prototype.setDimension=function(w,h){
draw2d.WindowFigure.prototype.setDimension.call(this,w,h);
if(this.buttonbar!==null){
this.buttonbar.style.width=this.getWidth()+"px";
}
};
draw2d.Dialog.prototype.setWorkflow=function(_4c2e){
draw2d.WindowFigure.prototype.setWorkflow.call(this,_4c2e);
this.setFocus();
};
draw2d.Dialog.prototype.setFocus=function(){
};
draw2d.Dialog.prototype.onSetDocumentDirty=function(){
};
draw2d.InputDialog=function(){
draw2d.Dialog.call(this);
this.setDimension(400,100);
};
draw2d.InputDialog.prototype=new draw2d.Dialog();
draw2d.InputDialog.prototype.type="draw2d.InputDialog";
draw2d.InputDialog.prototype.createHTMLElement=function(){
var item=draw2d.Dialog.prototype.createHTMLElement.call(this);
return item;
};
draw2d.InputDialog.prototype.onOk=function(){
this.workflow.removeFigure(this);
};
draw2d.InputDialog.prototype.onCancel=function(){
this.workflow.removeFigure(this);
};
draw2d.PropertyDialog=function(_627c,_627d,label){
this.figure=_627c;
this.propertyName=_627d;
this.label=label;
draw2d.Dialog.call(this);
this.setDimension(400,120);
};
draw2d.PropertyDialog.prototype=new draw2d.Dialog();
draw2d.PropertyDialog.prototype.type="draw2d.PropertyDialog";
draw2d.PropertyDialog.prototype.createHTMLElement=function(){
var item=draw2d.Dialog.prototype.createHTMLElement.call(this);
var _6280=document.createElement("form");
_6280.style.position="absolute";
_6280.style.left="10px";
_6280.style.top="30px";
_6280.style.width="375px";
_6280.style.font="normal 10px verdana";
item.appendChild(_6280);
this.labelDiv=document.createElement("div");
this.labelDiv.innerHTML=this.label;
this.disableTextSelection(this.labelDiv);
_6280.appendChild(this.labelDiv);
this.input=document.createElement("input");
this.input.style.border="1px solid gray";
this.input.style.font="normal 10px verdana";
this.input.type="text";
var value=this.figure.getProperty(this.propertyName);
if(value){
this.input.value=value;
}else{
this.input.value="";
}
this.input.style.width="100%";
_6280.appendChild(this.input);
this.input.focus();
return item;
};
draw2d.PropertyDialog.prototype.onOk=function(){
draw2d.Dialog.prototype.onOk.call(this);
this.figure.setProperty(this.propertyName,this.input.value);
};
draw2d.AnnotationDialog=function(_4ae2){
this.figure=_4ae2;
draw2d.Dialog.call(this);
this.setDimension(400,100);
};
draw2d.AnnotationDialog.prototype=new draw2d.Dialog();
draw2d.AnnotationDialog.prototype.type="draw2d.AnnotationDialog";
draw2d.AnnotationDialog.prototype.createHTMLElement=function(){
var item=draw2d.Dialog.prototype.createHTMLElement.call(this);
var _4ae4=document.createElement("form");
_4ae4.style.position="absolute";
_4ae4.style.left="10px";
_4ae4.style.top="30px";
_4ae4.style.width="375px";
_4ae4.style.font="normal 10px verdana";
item.appendChild(_4ae4);
this.label=document.createTextNode("Text");
_4ae4.appendChild(this.label);
this.input=document.createElement("input");
this.input.style.border="1px solid gray";
this.input.style.font="normal 10px verdana";
this.input.type="text";
var value=this.figure.getText();
if(value){
this.input.value=value;
}else{
this.input.value="";
}
this.input.style.width="100%";
_4ae4.appendChild(this.input);
this.input.focus();
return item;
};
draw2d.AnnotationDialog.prototype.onOk=function(){
this.workflow.getCommandStack().execute(new draw2d.CommandSetText(this.figure,this.input.value));
this.workflow.removeFigure(this);
};
draw2d.PropertyWindow=function(){
this.currentSelection=null;
draw2d.WindowFigure.call(this,"Property Window");
this.setDimension(200,100);
};
draw2d.PropertyWindow.prototype=new draw2d.WindowFigure();
draw2d.PropertyWindow.prototype.type="draw2d.PropertyWindow";
draw2d.PropertyWindow.prototype.dispose=function(){
draw2d.WindowFigure.prototype.dispose.call(this);
};
draw2d.PropertyWindow.prototype.createHTMLElement=function(){
var item=draw2d.WindowFigure.prototype.createHTMLElement.call(this);
item.appendChild(this.createLabel("Type:",15,25));
item.appendChild(this.createLabel("X :",15,50));
item.appendChild(this.createLabel("Y :",15,70));
item.appendChild(this.createLabel("Width :",85,50));
item.appendChild(this.createLabel("Height :",85,70));
this.labelType=this.createLabel("",50,25);
this.labelX=this.createLabel("",40,50);
this.labelY=this.createLabel("",40,70);
this.labelWidth=this.createLabel("",135,50);
this.labelHeight=this.createLabel("",135,70);
this.labelType.style.fontWeight="normal";
this.labelX.style.fontWeight="normal";
this.labelY.style.fontWeight="normal";
this.labelWidth.style.fontWeight="normal";
this.labelHeight.style.fontWeight="normal";
item.appendChild(this.labelType);
item.appendChild(this.labelX);
item.appendChild(this.labelY);
item.appendChild(this.labelWidth);
item.appendChild(this.labelHeight);
return item;
};
draw2d.PropertyWindow.prototype.onSelectionChanged=function(_522c){
draw2d.WindowFigure.prototype.onSelectionChanged.call(this,_522c);
if(this.currentSelection!==null){
this.currentSelection.detachMoveListener(this);
}
this.currentSelection=_522c;
if(_522c!==null&&_522c!=this){
this.labelType.innerHTML=_522c.type;
if(_522c.getX){
this.labelX.innerHTML=_522c.getX();
this.labelY.innerHTML=_522c.getY();
this.labelWidth.innerHTML=_522c.getWidth();
this.labelHeight.innerHTML=_522c.getHeight();
this.currentSelection=_522c;
this.currentSelection.attachMoveListener(this);
}else{
this.labelX.innerHTML="";
this.labelY.innerHTML="";
this.labelWidth.innerHTML="";
this.labelHeight.innerHTML="";
}
}else{
this.labelType.innerHTML="&lt;none&gt;";
this.labelX.innerHTML="";
this.labelY.innerHTML="";
this.labelWidth.innerHTML="";
this.labelHeight.innerHTML="";
}
};
draw2d.PropertyWindow.prototype.getCurrentSelection=function(){
return this.currentSelection;
};
draw2d.PropertyWindow.prototype.onOtherFigureMoved=function(_522d){
if(_522d==this.currentSelection){
this.onSelectionChanged(_522d);
}
};
draw2d.PropertyWindow.prototype.createLabel=function(text,x,y){
var l=document.createElement("div");
l.style.position="absolute";
l.style.left=x+"px";
l.style.top=y+"px";
l.style.font="normal 10px verdana";
l.style.whiteSpace="nowrap";
l.style.fontWeight="bold";
l.innerHTML=text;
return l;
};
draw2d.ColorDialog=function(){
this.maxValue={"h":"359","s":"100","v":"100"};
this.HSV={0:359,1:100,2:100};
this.slideHSV={0:359,1:100,2:100};
this.SVHeight=165;
this.wSV=162;
this.wH=162;
draw2d.Dialog.call(this,"Color Chooser");
this.loadSV();
this.setColor(new draw2d.Color(255,0,0));
this.setDimension(219,244);
};
draw2d.ColorDialog.prototype=new draw2d.Dialog();
draw2d.ColorDialog.prototype.type="draw2d.ColorDialog";
draw2d.ColorDialog.prototype.createHTMLElement=function(){
var oThis=this;
var item=draw2d.Dialog.prototype.createHTMLElement.call(this);
this.outerDiv=document.createElement("div");
this.outerDiv.id="plugin";
this.outerDiv.style.top="15px";
this.outerDiv.style.left="0px";
this.outerDiv.style.width="201px";
this.outerDiv.style.position="absolute";
this.outerDiv.style.padding="9px";
this.outerDiv.display="block";
this.outerDiv.style.background="#0d0d0d";
this.plugHEX=document.createElement("div");
this.plugHEX.id="plugHEX";
this.plugHEX.innerHTML="F1FFCC";
this.plugHEX.style.color="white";
this.plugHEX.style.font="normal 10px verdana";
this.outerDiv.appendChild(this.plugHEX);
this.SV=document.createElement("div");
this.SV.onmousedown=function(event){
oThis.mouseDownSV(oThis.SVslide,event);
};
this.SV.id="SV";
this.SV.style.cursor="crosshair";
this.SV.style.background="#FF0000 url(SatVal.png)";
this.SV.style.position="absolute";
this.SV.style.height="166px";
this.SV.style.width="167px";
this.SV.style.marginRight="10px";
this.SV.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='SatVal.png', sizingMethod='scale')";
this.SV.style["float"]="left";
this.outerDiv.appendChild(this.SV);
this.SVslide=document.createElement("div");
this.SVslide.onmousedown=function(event){
oThis.mouseDownSV(event);
};
this.SVslide.style.top="40px";
this.SVslide.style.left="40px";
this.SVslide.style.position="absolute";
this.SVslide.style.cursor="crosshair";
this.SVslide.style.background="url(slide.gif)";
this.SVslide.style.height="9px";
this.SVslide.style.width="9px";
this.SVslide.style.lineHeight="1px";
this.outerDiv.appendChild(this.SVslide);
this.H=document.createElement("form");
this.H.id="H";
this.H.onmousedown=function(event){
oThis.mouseDownH(event);
};
this.H.style.border="1px solid #000000";
this.H.style.cursor="crosshair";
this.H.style.position="absolute";
this.H.style.width="19px";
this.H.style.top="28px";
this.H.style.left="191px";
this.outerDiv.appendChild(this.H);
this.Hslide=document.createElement("div");
this.Hslide.style.top="-7px";
this.Hslide.style.left="-8px";
this.Hslide.style.background="url(slideHue.gif)";
this.Hslide.style.height="5px";
this.Hslide.style.width="33px";
this.Hslide.style.position="absolute";
this.Hslide.style.lineHeight="1px";
this.H.appendChild(this.Hslide);
this.Hmodel=document.createElement("div");
this.Hmodel.style.height="1px";
this.Hmodel.style.width="19px";
this.Hmodel.style.lineHeight="1px";
this.Hmodel.style.margin="0px";
this.Hmodel.style.padding="0px";
this.Hmodel.style.fontSize="1px";
this.H.appendChild(this.Hmodel);
item.appendChild(this.outerDiv);
return item;
};
draw2d.ColorDialog.prototype.onOk=function(){
draw2d.Dialog.prototype.onOk.call(this);
};
draw2d.browser=function(v){
return (Math.max(navigator.userAgent.toLowerCase().indexOf(v),0));
};
draw2d.ColorDialog.prototype.showColor=function(c){
this.plugHEX.style.background="#"+c;
this.plugHEX.innerHTML=c;
};
draw2d.ColorDialog.prototype.getSelectedColor=function(){
var rgb=this.hex2rgb(this.plugHEX.innerHTML);
return new draw2d.Color(rgb[0],rgb[1],rgb[2]);
};
draw2d.ColorDialog.prototype.setColor=function(color){
if(color===null){
color=new draw2d.Color(100,100,100);
}
var hex=this.rgb2hex(Array(color.getRed(),color.getGreen(),color.getBlue()));
this.updateH(hex);
};
draw2d.ColorDialog.prototype.XY=function(e,v){
var z=draw2d.browser("msie")?Array(event.clientX+document.body.scrollLeft,event.clientY+document.body.scrollTop):Array(e.pageX,e.pageY);
return z[v];
};
draw2d.ColorDialog.prototype.mkHSV=function(a,b,c){
return (Math.min(a,Math.max(0,Math.ceil((parseInt(c)/b)*a))));
};
draw2d.ColorDialog.prototype.ckHSV=function(a,b){
if(a>=0&&a<=b){
return (a);
}else{
if(a>b){
return (b);
}else{
if(a<0){
return ("-"+oo);
}
}
}
};
draw2d.ColorDialog.prototype.mouseDownH=function(e){
this.slideHSV[0]=this.HSV[0];
var oThis=this;
this.H.onmousemove=function(e){
oThis.dragH(e);
};
this.H.onmouseup=function(e){
oThis.H.onmousemove="";
oThis.H.onmouseup="";
};
this.dragH(e);
};
draw2d.ColorDialog.prototype.dragH=function(e){
var y=this.XY(e,1)-this.getY()-40;
this.Hslide.style.top=(this.ckHSV(y,this.wH)-5)+"px";
this.slideHSV[0]=this.mkHSV(359,this.wH,this.Hslide.style.top);
this.updateSV();
this.showColor(this.commit());
this.SV.style.backgroundColor="#"+this.hsv2hex(Array(this.HSV[0],100,100));
};
draw2d.ColorDialog.prototype.mouseDownSV=function(o,e){
this.slideHSV[0]=this.HSV[0];
var oThis=this;
function reset(){
oThis.SV.onmousemove="";
oThis.SV.onmouseup="";
oThis.SVslide.onmousemove="";
oThis.SVslide.onmouseup="";
}
this.SV.onmousemove=function(e){
oThis.dragSV(e);
};
this.SV.onmouseup=reset;
this.SVslide.onmousemove=function(e){
oThis.dragSV(e);
};
this.SVslide.onmouseup=reset;
this.dragSV(e);
};
draw2d.ColorDialog.prototype.dragSV=function(e){
var x=this.XY(e,0)-this.getX()-1;
var y=this.XY(e,1)-this.getY()-20;
this.SVslide.style.left=this.ckHSV(x,this.wSV)+"px";
this.SVslide.style.top=this.ckHSV(y,this.wSV)+"px";
this.slideHSV[1]=this.mkHSV(100,this.wSV,this.SVslide.style.left);
this.slideHSV[2]=100-this.mkHSV(100,this.wSV,this.SVslide.style.top);
this.updateSV();
};
draw2d.ColorDialog.prototype.commit=function(){
var r="hsv";
var z={};
var j="";
for(var i=0;i<=r.length-1;i++){
j=r.substr(i,1);
z[i]=(j=="h")?this.maxValue[j]-this.mkHSV(this.maxValue[j],this.wH,this.Hslide.style.top):this.HSV[i];
}
return (this.updateSV(this.hsv2hex(z)));
};
draw2d.ColorDialog.prototype.updateSV=function(v){
this.HSV=v?this.hex2hsv(v):Array(this.slideHSV[0],this.slideHSV[1],this.slideHSV[2]);
if(!v){
v=this.hsv2hex(Array(this.slideHSV[0],this.slideHSV[1],this.slideHSV[2]));
}
this.showColor(v);
return v;
};
draw2d.ColorDialog.prototype.loadSV=function(){
var z="";
for(var i=this.SVHeight;i>=0;i--){
z+="<div style=\"background:#"+this.hsv2hex(Array(Math.round((359/this.SVHeight)*i),100,100))+";\"><br/></div>";
}
this.Hmodel.innerHTML=z;
};
draw2d.ColorDialog.prototype.updateH=function(v){
this.plugHEX.innerHTML=v;
this.HSV=this.hex2hsv(v);
this.SV.style.backgroundColor="#"+this.hsv2hex(Array(this.HSV[0],100,100));
this.SVslide.style.top=(parseInt(this.wSV-this.wSV*(this.HSV[1]/100))+20)+"px";
this.SVslide.style.left=(parseInt(this.wSV*(this.HSV[1]/100))+5)+"px";
this.Hslide.style.top=(parseInt(this.wH*((this.maxValue["h"]-this.HSV[0])/this.maxValue["h"]))-7)+"px";
};
draw2d.ColorDialog.prototype.toHex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
draw2d.ColorDialog.prototype.hex2rgb=function(r){
return ({0:parseInt(r.substr(0,2),16),1:parseInt(r.substr(2,2),16),2:parseInt(r.substr(4,2),16)});
};
draw2d.ColorDialog.prototype.rgb2hex=function(r){
return (this.toHex(r[0])+this.toHex(r[1])+this.toHex(r[2]));
};
draw2d.ColorDialog.prototype.hsv2hex=function(h){
return (this.rgb2hex(this.hsv2rgb(h)));
};
draw2d.ColorDialog.prototype.hex2hsv=function(v){
return (this.rgb2hsv(this.hex2rgb(v)));
};
draw2d.ColorDialog.prototype.rgb2hsv=function(r){
var max=Math.max(r[0],r[1],r[2]);
var delta=max-Math.min(r[0],r[1],r[2]);
var H;
var S;
var V;
if(max!=0){
S=Math.round(delta/max*100);
if(r[0]==max){
H=(r[1]-r[2])/delta;
}else{
if(r[1]==max){
H=2+(r[2]-r[0])/delta;
}else{
if(r[2]==max){
H=4+(r[0]-r[1])/delta;
}
}
}
var H=Math.min(Math.round(H*60),360);
if(H<0){
H+=360;
}
}
return ({0:H?H:0,1:S?S:0,2:Math.round((max/255)*100)});
};
draw2d.ColorDialog.prototype.hsv2rgb=function(r){
var R;
var B;
var G;
var S=r[1]/100;
var V=r[2]/100;
var H=r[0]/360;
if(S>0){
if(H>=1){
H=0;
}
H=6*H;
F=H-Math.floor(H);
A=Math.round(255*V*(1-S));
B=Math.round(255*V*(1-(S*F)));
C=Math.round(255*V*(1-(S*(1-F))));
V=Math.round(255*V);
switch(Math.floor(H)){
case 0:
R=V;
G=C;
B=A;
break;
case 1:
R=B;
G=V;
B=A;
break;
case 2:
R=A;
G=V;
B=C;
break;
case 3:
R=A;
G=B;
B=V;
break;
case 4:
R=C;
G=A;
B=V;
break;
case 5:
R=V;
G=A;
B=B;
break;
}
return ({0:R?R:0,1:G?G:0,2:B?B:0});
}else{
return ({0:(V=Math.round(V*255)),1:V,2:V});
}
};
draw2d.LineColorDialog=function(_592a){
draw2d.ColorDialog.call(this);
this.figure=_592a;
var color=_592a.getColor();
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
};
draw2d.LineColorDialog.prototype=new draw2d.ColorDialog();
draw2d.LineColorDialog.prototype.type="draw2d.LineColorDialog";
draw2d.LineColorDialog.prototype.onOk=function(){
var _592c=this.workflow;
draw2d.ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setColor=="function"){
_592c.getCommandStack().execute(new draw2d.CommandSetColor(this.figure,this.getSelectedColor()));
if(_592c.getCurrentSelection()==this.figure){
_592c.setCurrentSelection(this.figure);
}
}
};
draw2d.BackgroundColorDialog=function(_553a){
draw2d.ColorDialog.call(this);
this.figure=_553a;
var color=_553a.getBackgroundColor();
if(color!==null){
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
}
};
draw2d.BackgroundColorDialog.prototype=new draw2d.ColorDialog();
draw2d.BackgroundColorDialog.prototype.type="draw2d.BackgroundColorDialog";
draw2d.BackgroundColorDialog.prototype.onOk=function(){
var _553c=this.workflow;
draw2d.ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setBackgroundColor=="function"){
_553c.getCommandStack().execute(new draw2d.CommandSetBackgroundColor(this.figure,this.getSelectedColor()));
if(_553c.getCurrentSelection()==this.figure){
_553c.setCurrentSelection(this.figure);
}
}
};
draw2d.AnnotationDialog=function(_4ae2){
this.figure=_4ae2;
draw2d.Dialog.call(this);
this.setDimension(400,100);
};
draw2d.AnnotationDialog.prototype=new draw2d.Dialog();
draw2d.AnnotationDialog.prototype.type="draw2d.AnnotationDialog";
draw2d.AnnotationDialog.prototype.createHTMLElement=function(){
var item=draw2d.Dialog.prototype.createHTMLElement.call(this);
var _4ae4=document.createElement("form");
_4ae4.style.position="absolute";
_4ae4.style.left="10px";
_4ae4.style.top="30px";
_4ae4.style.width="375px";
_4ae4.style.font="normal 10px verdana";
item.appendChild(_4ae4);
this.label=document.createTextNode("Text");
_4ae4.appendChild(this.label);
this.input=document.createElement("input");
this.input.style.border="1px solid gray";
this.input.style.font="normal 10px verdana";
this.input.type="text";
var value=this.figure.getText();
if(value){
this.input.value=value;
}else{
this.input.value="";
}
this.input.style.width="100%";
_4ae4.appendChild(this.input);
this.input.focus();
return item;
};
draw2d.AnnotationDialog.prototype.onOk=function(){
this.workflow.getCommandStack().execute(new draw2d.CommandSetText(this.figure,this.input.value));
this.workflow.removeFigure(this);
};
draw2d.Command=function(label){
this.label=label;
};
draw2d.Command.prototype.type="draw2d.Command";
draw2d.Command.prototype.getLabel=function(){
return this.label;
};
draw2d.Command.prototype.canExecute=function(){
return true;
};
draw2d.Command.prototype.execute=function(){
};
draw2d.Command.prototype.cancel=function(){
};
draw2d.Command.prototype.undo=function(){
};
draw2d.Command.prototype.redo=function(){
};
draw2d.CommandStack=function(){
this.undostack=[];
this.redostack=[];
this.maxundo=50;
this.eventListeners=new draw2d.ArrayList();
};
draw2d.CommandStack.PRE_EXECUTE=1;
draw2d.CommandStack.PRE_REDO=2;
draw2d.CommandStack.PRE_UNDO=4;
draw2d.CommandStack.POST_EXECUTE=8;
draw2d.CommandStack.POST_REDO=16;
draw2d.CommandStack.POST_UNDO=32;
draw2d.CommandStack.POST_MASK=draw2d.CommandStack.POST_EXECUTE|draw2d.CommandStack.POST_UNDO|draw2d.CommandStack.POST_REDO;
draw2d.CommandStack.PRE_MASK=draw2d.CommandStack.PRE_EXECUTE|draw2d.CommandStack.PRE_UNDO|draw2d.CommandStack.PRE_REDO;
draw2d.CommandStack.prototype.type="draw2d.CommandStack";
draw2d.CommandStack.prototype.setUndoLimit=function(count){
this.maxundo=count;
};
draw2d.CommandStack.prototype.markSaveLocation=function(){
this.undostack=[];
this.redostack=[];
};
draw2d.CommandStack.prototype.execute=function(_4c3f){
if(_4c3f===null){
return;
}
if(_4c3f.canExecute()==false){
return;
}
this.notifyListeners(_4c3f,draw2d.CommandStack.PRE_EXECUTE);
this.undostack.push(_4c3f);
_4c3f.execute();
this.redostack=[];
if(this.undostack.length>this.maxundo){
this.undostack=this.undostack.slice(this.undostack.length-this.maxundo);
}
this.notifyListeners(_4c3f,draw2d.CommandStack.POST_EXECUTE);
};
draw2d.CommandStack.prototype.undo=function(){
var _4c40=this.undostack.pop();
if(_4c40){
this.notifyListeners(_4c40,draw2d.CommandStack.PRE_UNDO);
this.redostack.push(_4c40);
_4c40.undo();
this.notifyListeners(_4c40,draw2d.CommandStack.POST_UNDO);
}
};
draw2d.CommandStack.prototype.redo=function(){
var _4c41=this.redostack.pop();
if(_4c41){
this.notifyListeners(_4c41,draw2d.CommandStack.PRE_REDO);
this.undostack.push(_4c41);
_4c41.redo();
this.notifyListeners(_4c41,draw2d.CommandStack.POST_REDO);
}
};
draw2d.CommandStack.prototype.canRedo=function(){
return this.redostack.length>0;
};
draw2d.CommandStack.prototype.canUndo=function(){
return this.undostack.length>0;
};
draw2d.CommandStack.prototype.addCommandStackEventListener=function(_4c42){
this.eventListeners.add(_4c42);
};
draw2d.CommandStack.prototype.removeCommandStackEventListener=function(_4c43){
this.eventListeners.remove(_4c43);
};
draw2d.CommandStack.prototype.notifyListeners=function(_4c44,state){
var event=new draw2d.CommandStackEvent(_4c44,state);
var size=this.eventListeners.getSize();
for(var i=0;i<size;i++){
this.eventListeners.get(i).stackChanged(event);
}
};
draw2d.CommandStackEvent=function(_557b,_557c){
this.command=_557b;
this.details=_557c;
};
draw2d.CommandStackEvent.prototype.type="draw2d.CommandStackEvent";
draw2d.CommandStackEvent.prototype.getCommand=function(){
return this.command;
};
draw2d.CommandStackEvent.prototype.getDetails=function(){
return this.details;
};
draw2d.CommandStackEvent.prototype.isPostChangeEvent=function(){
return 0!=(this.getDetails()&draw2d.CommandStack.POST_MASK);
};
draw2d.CommandStackEvent.prototype.isPreChangeEvent=function(){
return 0!=(this.getDetails()&draw2d.CommandStack.PRE_MASK);
};
draw2d.CommandStackEventListener=function(){
};
draw2d.CommandStackEventListener.prototype.type="draw2d.CommandStackEventListener";
draw2d.CommandStackEventListener.prototype.stackChanged=function(event){
};
draw2d.CommandAdd=function(_4c85,_4c86,x,y,_4c89){
draw2d.Command.call(this,"add figure");
if(_4c89===undefined){
_4c89=null;
}
this.parent=_4c89;
this.figure=_4c86;
this.x=x;
this.y=y;
this.workflow=_4c85;
};
draw2d.CommandAdd.prototype=new draw2d.Command();
draw2d.CommandAdd.prototype.type="draw2d.CommandAdd";
draw2d.CommandAdd.prototype.execute=function(){
this.redo();
};
draw2d.CommandAdd.prototype.redo=function(){
if(this.x&&this.y){
this.workflow.addFigure(this.figure,this.x,this.y);
}else{
this.workflow.addFigure(this.figure);
}
this.workflow.setCurrentSelection(this.figure);
if(this.parent!==null){
this.parent.addChild(this.figure);
}
};
draw2d.CommandAdd.prototype.undo=function(){
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.parent!==null){
this.parent.removeChild(this.figure);
}
};
draw2d.CommandDelete=function(_5933){
draw2d.Command.call(this,"delete figure");
this.parent=_5933.parent;
this.figure=_5933;
this.workflow=_5933.workflow;
this.connections=null;
this.compartmentDeleteCommands=null;
};
draw2d.CommandDelete.prototype=new draw2d.Command();
draw2d.CommandDelete.prototype.type="draw2d.CommandDelete";
draw2d.CommandDelete.prototype.execute=function(){
this.redo();
};
draw2d.CommandDelete.prototype.undo=function(){
if(this.figure instanceof draw2d.CompartmentFigure){
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
var _5935=this.compartmentDeleteCommands.get(i);
this.figure.addChild(_5935.figure);
this.workflow.getCommandStack().undo();
}
}
this.workflow.addFigure(this.figure);
if(this.figure instanceof draw2d.Connection){
this.figure.reconnect();
}
this.workflow.setCurrentSelection(this.figure);
if(this.parent!==null){
this.parent.addChild(this.figure);
}
for(var i=0;i<this.connections.getSize();++i){
this.workflow.addFigure(this.connections.get(i));
this.connections.get(i).reconnect();
}
};
draw2d.CommandDelete.prototype.redo=function(){
if(this.figure instanceof draw2d.CompartmentFigure){
if(this.compartmentDeleteCommands===null){
this.compartmentDeleteCommands=new draw2d.ArrayList();
var _5936=this.figure.getChildren().clone();
for(var i=0;i<_5936.getSize();i++){
var child=_5936.get(i);
this.figure.removeChild(child);
var _5939=new draw2d.CommandDelete(child);
this.compartmentDeleteCommands.add(_5939);
this.workflow.getCommandStack().execute(_5939);
}
}else{
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
this.workflow.redo();
}
}
}
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.figure instanceof draw2d.Node&&this.connections===null){
this.connections=new draw2d.ArrayList();
var ports=this.figure.getPorts();
for(var i=0;i<ports.getSize();i++){
var port=ports.get(i);
for(var c=0,c_size=port.getConnections().getSize();c<c_size;c++){
if(!this.connections.contains(port.getConnections().get(c))){
this.connections.add(port.getConnections().get(c));
}
}
}
}
if(this.connections===null){
this.connections=new draw2d.ArrayList();
}
if(this.parent!==null){
this.parent.removeChild(this.figure);
}
for(var i=0;i<this.connections.getSize();++i){
this.workflow.removeFigure(this.connections.get(i));
}
};
draw2d.CommandMove=function(_55c7,x,y){
draw2d.Command.call(this,"move figure");
this.figure=_55c7;
if(x==undefined){
this.oldX=_55c7.getX();
this.oldY=_55c7.getY();
}else{
this.oldX=x;
this.oldY=y;
}
this.oldCompartment=_55c7.getParent();
};
draw2d.CommandMove.prototype=new draw2d.Command();
draw2d.CommandMove.prototype.type="draw2d.CommandMove";
draw2d.CommandMove.prototype.setStartPosition=function(x,y){
this.oldX=x;
this.oldY=y;
};
draw2d.CommandMove.prototype.setPosition=function(x,y){
this.newX=x;
this.newY=y;
this.newCompartment=this.figure.workflow.getBestCompartmentFigure(x,y,this.figure);
};
draw2d.CommandMove.prototype.canExecute=function(){
return this.newX!=this.oldX||this.newY!=this.oldY;
};
draw2d.CommandMove.prototype.execute=function(){
this.redo();
};
draw2d.CommandMove.prototype.undo=function(){
this.figure.setPosition(this.oldX,this.oldY);
if(this.newCompartment!==null){
this.newCompartment.removeChild(this.figure);
}
if(this.oldCompartment!==null){
this.oldCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
draw2d.CommandMove.prototype.redo=function(){
this.figure.setPosition(this.newX,this.newY);
if(this.oldCompartment!==null){
this.oldCompartment.removeChild(this.figure);
}
if(this.newCompartment!==null){
this.newCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
draw2d.CommandResize=function(_5252,width,_5254){
draw2d.Command.call(this,"resize figure");
this.figure=_5252;
if(width===undefined){
this.oldWidth=_5252.getWidth();
this.oldHeight=_5252.getHeight();
}else{
this.oldWidth=width;
this.oldHeight=_5254;
}
};
draw2d.CommandResize.prototype=new draw2d.Command();
draw2d.CommandResize.prototype.type="draw2d.CommandResize";
draw2d.CommandResize.prototype.setDimension=function(width,_5256){
this.newWidth=width;
this.newHeight=_5256;
};
draw2d.CommandResize.prototype.canExecute=function(){
return this.newWidth!=this.oldWidth||this.newHeight!=this.oldHeight;
};
draw2d.CommandResize.prototype.execute=function(){
this.redo();
};
draw2d.CommandResize.prototype.undo=function(){
this.figure.setDimension(this.oldWidth,this.oldHeight);
this.figure.workflow.moveResizeHandles(this.figure);
};
draw2d.CommandResize.prototype.redo=function(){
this.figure.setDimension(this.newWidth,this.newHeight);
this.figure.workflow.moveResizeHandles(this.figure);
};
draw2d.CommandSetText=function(_6297,text){
draw2d.Command.call(this,"set text");
this.figure=_6297;
this.newText=text;
this.oldText=_6297.getText();
};
draw2d.CommandSetText.prototype=new draw2d.Command();
draw2d.CommandSetText.prototype.type="draw2d.CommandSetText";
draw2d.CommandSetText.prototype.execute=function(){
this.redo();
};
draw2d.CommandSetText.prototype.redo=function(){
this.figure.setText(this.newText);
};
draw2d.CommandSetText.prototype.undo=function(){
this.figure.setText(this.oldText);
};
draw2d.CommandSetColor=function(_55c3,color){
draw2d.Command.call(this,"set color");
this.figure=_55c3;
this.newColor=color;
this.oldColor=_55c3.getColor();
};
draw2d.CommandSetColor.prototype=new draw2d.Command();
draw2d.CommandSetColor.prototype.type="draw2d.CommandSetColor";
draw2d.CommandSetColor.prototype.execute=function(){
this.redo();
};
draw2d.CommandSetColor.prototype.undo=function(){
this.figure.setColor(this.oldColor);
};
draw2d.CommandSetColor.prototype.redo=function(){
this.figure.setColor(this.newColor);
};
draw2d.CommandSetBackgroundColor=function(_5916,color){
draw2d.Command.call(this,"set background color");
this.figure=_5916;
this.newColor=color;
this.oldColor=_5916.getBackgroundColor();
};
draw2d.CommandSetBackgroundColor.prototype=new draw2d.Command();
draw2d.CommandSetBackgroundColor.prototype.type="draw2d.CommandSetBackgroundColor";
draw2d.CommandSetBackgroundColor.prototype.execute=function(){
this.redo();
};
draw2d.CommandSetBackgroundColor.prototype.undo=function(){
this.figure.setBackgroundColor(this.oldColor);
};
draw2d.CommandSetBackgroundColor.prototype.redo=function(){
this.figure.setBackgroundColor(this.newColor);
};
draw2d.CommandConnect=function(_5495,_5496,_5497){
draw2d.Command.call(this,"create connection");
this.workflow=_5495;
this.source=_5496;
this.target=_5497;
this.connection=null;
};
draw2d.CommandConnect.prototype=new draw2d.Command();
draw2d.CommandConnect.prototype.type="draw2d.CommandConnect";
draw2d.CommandConnect.prototype.setConnection=function(_5498){
this.connection=_5498;
};
draw2d.CommandConnect.prototype.execute=function(){
if(this.connection===null){
this.connection=new draw2d.Connection();
}
this.connection.setSource(this.source);
this.connection.setTarget(this.target);
this.workflow.addFigure(this.connection);
};
draw2d.CommandConnect.prototype.redo=function(){
this.workflow.addFigure(this.connection);
this.connection.reconnect();
};
draw2d.CommandConnect.prototype.undo=function(){
this.workflow.removeFigure(this.connection);
};
draw2d.CommandReconnect=function(con){
draw2d.Command.call(this,"reconnect connection");
this.con=con;
this.oldSourcePort=con.getSource();
this.oldTargetPort=con.getTarget();
this.oldRouter=con.getRouter();
this.con.setRouter(new draw2d.NullConnectionRouter());
};
draw2d.CommandReconnect.prototype=new draw2d.Command();
draw2d.CommandReconnect.prototype.type="draw2d.CommandReconnect";
draw2d.CommandReconnect.prototype.canExecute=function(){
return true;
};
draw2d.CommandReconnect.prototype.setNewPorts=function(_58e2,_58e3){
this.newSourcePort=_58e2;
this.newTargetPort=_58e3;
};
draw2d.CommandReconnect.prototype.execute=function(){
this.redo();
};
draw2d.CommandReconnect.prototype.cancel=function(){
var start=this.con.sourceAnchor.getLocation(this.con.targetAnchor.getReferencePoint());
var end=this.con.targetAnchor.getLocation(this.con.sourceAnchor.getReferencePoint());
this.con.setStartPoint(start.x,start.y);
this.con.setEndPoint(end.x,end.y);
this.con.getWorkflow().showLineResizeHandles(this.con);
this.con.setRouter(this.oldRouter);
};
draw2d.CommandReconnect.prototype.undo=function(){
this.con.setSource(this.oldSourcePort);
this.con.setTarget(this.oldTargetPort);
this.con.setRouter(this.oldRouter);
if(this.con.getWorkflow().getCurrentSelection()==this.con){
this.con.getWorkflow().showLineResizeHandles(this.con);
}
};
draw2d.CommandReconnect.prototype.redo=function(){
this.con.setSource(this.newSourcePort);
this.con.setTarget(this.newTargetPort);
this.con.setRouter(this.oldRouter);
if(this.con.getWorkflow().getCurrentSelection()==this.con){
this.con.getWorkflow().showLineResizeHandles(this.con);
}
};
draw2d.CommandMoveLine=function(line,_4fa4,_4fa5,endX,endY){
draw2d.Command.call(this,"move line");
this.line=line;
this.startX1=_4fa4;
this.startY1=_4fa5;
this.endX1=endX;
this.endY1=endY;
};
draw2d.CommandMoveLine.prototype=new draw2d.Command();
draw2d.CommandMoveLine.prototype.type="draw2d.CommandMoveLine";
draw2d.CommandMoveLine.prototype.canExecute=function(){
return this.startX1!=this.startX2||this.startY1!=this.startY2||this.endX1!=this.endX2||this.endY1!=this.endY2;
};
draw2d.CommandMoveLine.prototype.execute=function(){
this.startX2=this.line.getStartX();
this.startY2=this.line.getStartY();
this.endX2=this.line.getEndX();
this.endY2=this.line.getEndY();
this.redo();
};
draw2d.CommandMoveLine.prototype.undo=function(){
this.line.setStartPoint(this.startX1,this.startY1);
this.line.setEndPoint(this.endX1,this.endY1);
if(this.line.workflow.getCurrentSelection()==this.line){
this.line.workflow.showLineResizeHandles(this.line);
}
};
draw2d.CommandMoveLine.prototype.redo=function(){
this.line.setStartPoint(this.startX2,this.startY2);
this.line.setEndPoint(this.endX2,this.endY2);
if(this.line.workflow.getCurrentSelection()==this.line){
this.line.workflow.showLineResizeHandles(this.line);
}
};
draw2d.CommandMovePort=function(port){
draw2d.Command.call(this,"move port");
this.port=port;
};
draw2d.CommandMovePort.prototype=new draw2d.Command();
draw2d.CommandMovePort.prototype.type="draw2d.CommandMovePort";
draw2d.CommandMovePort.prototype.execute=function(){
this.port.setAlpha(1);
this.port.setPosition(this.port.originX,this.port.originY);
this.port.parentNode.workflow.hideConnectionLine();
};
draw2d.CommandMovePort.prototype.undo=function(){
};
draw2d.CommandMovePort.prototype.redo=function(){
};
draw2d.CommandMovePort.prototype.setPosition=function(x,y){
};
draw2d.Menu=function(){
this.menuItems=new draw2d.ArrayList();
draw2d.Figure.call(this);
this.setSelectable(false);
this.setDeleteable(false);
this.setCanDrag(false);
this.setResizeable(false);
this.setSelectable(false);
this.setZOrder(10000);
this.dirty=false;
};
draw2d.Menu.prototype=new draw2d.Figure();
draw2d.Menu.prototype.type="draw2d.Menu";
draw2d.Menu.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.zIndex=""+draw2d.Figure.ZOrderBaseIndex;
item.style.border="1px solid gray";
item.style.background="lavender";
item.style.cursor="pointer";
item.className="Menu";
return item;
};
draw2d.Menu.prototype.setWorkflow=function(_586d){
this.workflow=_586d;
};
draw2d.Menu.prototype.appendMenuItem=function(item){
this.menuItems.add(item);
item.parentMenu=this;
this.dirty=true;
};
draw2d.Menu.prototype.getHTMLElement=function(){
var html=draw2d.Figure.prototype.getHTMLElement.call(this);
if(this.dirty){
this.createList();
}
return html;
};
draw2d.Menu.prototype.createList=function(){
this.dirty=false;
this.html.innerHTML="";
var oThis=this;
for(var i=0;i<this.menuItems.getSize();i++){
var item=this.menuItems.get(i);
var li=document.createElement("a");
li.innerHTML=item.getLabel();
li.style.display="block";
li.style.fontFamily="Verdana, Arial, Helvetica, sans-serif";
li.style.fontSize="9pt";
li.style.color="dimgray";
li.style.borderBottom="1px solid silver";
li.style.paddingLeft="5px";
li.style.paddingRight="5px";
li.style.cursor="pointer";
li.className="MenuItem";
this.html.appendChild(li);
li.menuItem=item;
if(li.addEventListener){
li.addEventListener("click",function(event){
var _5875=arguments[0]||window.event;
_5875.cancelBubble=true;
_5875.returnValue=false;
var diffX=_5875.clientX;
var diffY=_5875.clientY;
var _5878=document.body.parentNode.scrollLeft;
var _5879=document.body.parentNode.scrollTop;
this.menuItem.execute(diffX+_5878,diffY+_5879);
},false);
li.addEventListener("mouseup",function(event){
event.cancelBubble=true;
event.returnValue=false;
},false);
li.addEventListener("mousedown",function(event){
event.cancelBubble=true;
event.returnValue=false;
},false);
li.addEventListener("mouseover",function(event){
this.style.backgroundColor="silver";
},false);
li.addEventListener("mouseout",function(event){
this.style.backgroundColor="transparent";
},false);
}else{
if(li.attachEvent){
li.attachEvent("onclick",function(event){
var _587f=arguments[0]||window.event;
_587f.cancelBubble=true;
_587f.returnValue=false;
var diffX=_587f.clientX;
var diffY=_587f.clientY;
var _5882=document.body.parentNode.scrollLeft;
var _5883=document.body.parentNode.scrollTop;
event.srcElement.menuItem.execute(diffX+_5882,diffY+_5883);
});
li.attachEvent("onmousedown",function(event){
event.cancelBubble=true;
event.returnValue=false;
});
li.attachEvent("onmouseup",function(event){
event.cancelBubble=true;
event.returnValue=false;
});
li.attachEvent("onmouseover",function(event){
event.srcElement.style.backgroundColor="silver";
});
li.attachEvent("onmouseout",function(event){
event.srcElement.style.backgroundColor="transparent";
});
}
}
}
};
draw2d.MenuItem=function(label,_4ec6,_4ec7){
this.label=label;
this.iconUrl=_4ec6;
this.parentMenu=null;
this.action=_4ec7;
};
draw2d.MenuItem.prototype.type="draw2d.MenuItem";
draw2d.MenuItem.prototype.isEnabled=function(){
return true;
};
draw2d.MenuItem.prototype.getLabel=function(){
return this.label;
};
draw2d.MenuItem.prototype.execute=function(x,y){
this.parentMenu.workflow.showMenu(null);
this.action(x,y);
};
draw2d.Locator=function(){
};
draw2d.Locator.prototype.type="draw2d.Locator";
draw2d.Locator.prototype.relocate=function(_6259){
};
draw2d.ConnectionLocator=function(_4eb6){
draw2d.Locator.call(this);
this.connection=_4eb6;
};
draw2d.ConnectionLocator.prototype=new draw2d.Locator;
draw2d.ConnectionLocator.prototype.type="draw2d.ConnectionLocator";
draw2d.ConnectionLocator.prototype.getConnection=function(){
return this.connection;
};
draw2d.ManhattanMidpointLocator=function(_6282){
draw2d.ConnectionLocator.call(this,_6282);
};
draw2d.ManhattanMidpointLocator.prototype=new draw2d.ConnectionLocator;
draw2d.ManhattanMidpointLocator.prototype.type="draw2d.ManhattanMidpointLocator";
draw2d.ManhattanMidpointLocator.prototype.relocate=function(_6283){
var conn=this.getConnection();
var p=new draw2d.Point();
var _6286=conn.getPoints();
var index=Math.floor((_6286.getSize()-2)/2);
if(_6286.getSize()<=index+1){
return;
}
var p1=_6286.get(index);
var p2=_6286.get(index+1);
p.x=(p2.x-p1.x)/2+p1.x+5;
p.y=(p2.y-p1.y)/2+p1.y+5;
_6283.setPosition(p.x,p.y);
};
draw2d.EditPartFactory=function(){
};
draw2d.EditPartFactory.prototype.type="draw2d.EditPartFactory";
draw2d.EditPartFactory.prototype.createEditPart=function(model){
};
draw2d.AbstractObjectModel=function(){
this.listeners=new draw2d.ArrayList();
this.id=draw2d.UUID.create();
};
draw2d.AbstractObjectModel.EVENT_ELEMENT_ADDED="element added";
draw2d.AbstractObjectModel.EVENT_ELEMENT_REMOVED="element removed";
draw2d.AbstractObjectModel.EVENT_CONNECTION_ADDED="connection addedx";
draw2d.AbstractObjectModel.EVENT_CONNECTION_REMOVED="connection removed";
draw2d.AbstractObjectModel.prototype.type="draw2d.AbstractObjectModel";
draw2d.AbstractObjectModel.prototype.getModelChildren=function(){
return new draw2d.ArrayList();
};
draw2d.AbstractObjectModel.prototype.getModelParent=function(){
return this.modelParent;
};
draw2d.AbstractObjectModel.prototype.setModelParent=function(_4b04){
this.modelParent=_4b04;
};
draw2d.AbstractObjectModel.prototype.getId=function(){
return this.id;
};
draw2d.AbstractObjectModel.prototype.firePropertyChange=function(_4b05,_4b06,_4b07){
var count=this.listeners.getSize();
if(count===0){
return;
}
var event=new draw2d.PropertyChangeEvent(this,_4b05,_4b06,_4b07);
for(var i=0;i<count;i++){
try{
this.listeners.get(i).propertyChange(event);
}
catch(e){
alert("Method: draw2d.AbstractObjectModel.prototype.firePropertyChange\n"+e+"\nProperty: "+_4b05+"\nListener Class:"+this.listeners.get(i).type);
}
}
};
draw2d.AbstractObjectModel.prototype.addPropertyChangeListener=function(_4b0b){
if(_4b0b!==null){
this.listeners.add(_4b0b);
}
};
draw2d.AbstractObjectModel.prototype.removePropertyChangeListener=function(_4b0c){
if(_4b0c!==null){
this.listeners.remove(_4b0c);
}
};
draw2d.AbstractObjectModel.prototype.getPersistentAttributes=function(){
return {id:this.id};
};
draw2d.AbstractConnectionModel=function(){
draw2d.AbstractObjectModel.call(this);
};
draw2d.AbstractConnectionModel.prototype=new draw2d.AbstractObjectModel();
draw2d.AbstractConnectionModel.prototype.type="draw2d.AbstractConnectionModel";
draw2d.AbstractConnectionModel.prototype.getSourceModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourceModel]";
};
draw2d.AbstractConnectionModel.prototype.getTargetModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetModel]";
};
draw2d.AbstractConnectionModel.prototype.getSourcePortName=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourcePortName]";
};
draw2d.AbstractConnectionModel.prototype.getTargetPortName=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetPortName]";
};
draw2d.AbstractConnectionModel.prototype.getSourcePortModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourcePortModel]";
};
draw2d.AbstractConnectionModel.prototype.getTargetPortModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetPortModel]";
};
draw2d.PropertyChangeEvent=function(model,_4cd8,_4cd9,_4cda){
this.model=model;
this.property=_4cd8;
this.oldValue=_4cd9;
this.newValue=_4cda;
};
draw2d.PropertyChangeEvent.prototype.type="draw2d.PropertyChangeEvent";
draw2d.GraphicalViewer=function(id){
try{
draw2d.Workflow.call(this,id);
this.factory=null;
this.model=null;
this.initDone=false;
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalViewer=function(/*:String*/ id)");
}
};
draw2d.GraphicalViewer.prototype=new draw2d.Workflow();
draw2d.GraphicalViewer.prototype.type="draw2d.GraphicalViewer";
draw2d.GraphicalViewer.prototype.setEditPartFactory=function(_55a6){
this.factory=_55a6;
this.checkInit();
};
draw2d.GraphicalViewer.prototype.setModel=function(model){
try{
if(model instanceof draw2d.AbstractObjectModel){
this.model=model;
this.checkInit();
this.model.addPropertyChangeListener(this);
}else{
alert("Invalid model class type:"+model.type);
}
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalViewer.prototype.setModel=function(/*:draw2d.AbstractObjectModel*/ model )");
}
};
draw2d.GraphicalViewer.prototype.propertyChange=function(event){
switch(event.property){
case draw2d.AbstractObjectModel.EVENT_ELEMENT_REMOVED:
var _55a9=this.getFigure(event.oldValue.getId());
this.removeFigure(_55a9);
break;
case draw2d.AbstractObjectModel.EVENT_ELEMENT_ADDED:
var _55a9=this.factory.createEditPart(event.newValue);
_55a9.setId(event.newValue.getId());
this.addFigure(_55a9);
this.setCurrentSelection(_55a9);
break;
}
};
draw2d.GraphicalViewer.prototype.checkInit=function(){
if(this.factory!==null&&this.model!==null&&this.initDone==false){
try{
var _55aa=this.model.getModelChildren();
var count=_55aa.getSize();
for(var i=0;i<count;i++){
var child=_55aa.get(i);
var _55ae=this.factory.createEditPart(child);
_55ae.setId(child.getId());
this.addFigure(_55ae);
}
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalViewer.prototype.checkInit=function()[addFigures]");
}
try{
var _55af=this.getDocument().getFigures();
var count=_55af.getSize();
for(var i=0;i<count;i++){
var _55ae=_55af.get(i);
if(_55ae instanceof draw2d.Node){
this.refreshConnections(_55ae);
}
}
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalViewer.prototype.checkInit=function()[refreshConnections]");
}
}
};
draw2d.GraphicalViewer.prototype.refreshConnections=function(node){
try{
var _55b1=new draw2d.ArrayList();
var _55b2=node.getModelSourceConnections();
var count=_55b2.getSize();
for(var i=0;i<count;i++){
var _55b5=_55b2.get(i);
_55b1.add(_55b5.getId());
var _55b6=this.getLine(_55b5.getId());
if(_55b6===null){
_55b6=this.factory.createEditPart(_55b5);
var _55b7=_55b5.getSourceModel();
var _55b8=_55b5.getTargetModel();
var _55b9=this.getFigure(_55b7.getId());
var _55ba=this.getFigure(_55b8.getId());
var _55bb=_55b9.getOutputPort(_55b5.getSourcePortName());
var _55bc=_55ba.getInputPort(_55b5.getTargetPortName());
_55b6.setTarget(_55bc);
_55b6.setSource(_55bb);
_55b6.setId(_55b5.getId());
this.addFigure(_55b6);
this.setCurrentSelection(_55b6);
}
}
var ports=node.getOutputPorts();
count=ports.getSize();
for(var i=0;i<count;i++){
var _55be=ports.get(i).getConnections();
var _55bf=_55be.getSize();
for(var ii=0;ii<_55bf;ii++){
var _55c1=_55be.get(ii);
if(!_55b1.contains(_55c1.getId())){
this.removeFigure(_55c1);
_55b1.add(_55c1.getId());
}
}
}
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalViewer.prototype.refreshConnections=function(/*:draw2d.Node*/ node )");
}
};
draw2d.GraphicalEditor=function(id){
try{
this.view=new draw2d.GraphicalViewer(id);
this.initializeGraphicalViewer();
}
catch(e){
pushErrorStack(e,"draw2d.GraphicalEditor=function(/*:String*/ id)");
}
};
draw2d.GraphicalEditor.prototype.type="draw2d.GraphicalEditor";
draw2d.GraphicalEditor.prototype.initializeGraphicalViewer=function(){
};
draw2d.GraphicalEditor.prototype.getGraphicalViewer=function(){
return this.view;
};
var whitespace="\n\r\t ";
XMLP=function(_4a6a){
_4a6a=SAXStrings.replace(_4a6a,null,null,"\r\n","\n");
_4a6a=SAXStrings.replace(_4a6a,null,null,"\r","\n");
this.m_xml=_4a6a;
this.m_iP=0;
this.m_iState=XMLP._STATE_PROLOG;
this.m_stack=new Stack();
this._clearAttributes();
};
XMLP._NONE=0;
XMLP._ELM_B=1;
XMLP._ELM_E=2;
XMLP._ELM_EMP=3;
XMLP._ATT=4;
XMLP._TEXT=5;
XMLP._ENTITY=6;
XMLP._PI=7;
XMLP._CDATA=8;
XMLP._COMMENT=9;
XMLP._DTD=10;
XMLP._ERROR=11;
XMLP._CONT_XML=0;
XMLP._CONT_ALT=1;
XMLP._ATT_NAME=0;
XMLP._ATT_VAL=1;
XMLP._STATE_PROLOG=1;
XMLP._STATE_DOCUMENT=2;
XMLP._STATE_MISC=3;
XMLP._errs=[];
XMLP._errs[XMLP.ERR_CLOSE_PI=0]="PI: missing closing sequence";
XMLP._errs[XMLP.ERR_CLOSE_DTD=1]="DTD: missing closing sequence";
XMLP._errs[XMLP.ERR_CLOSE_COMMENT=2]="Comment: missing closing sequence";
XMLP._errs[XMLP.ERR_CLOSE_CDATA=3]="CDATA: missing closing sequence";
XMLP._errs[XMLP.ERR_CLOSE_ELM=4]="Element: missing closing sequence";
XMLP._errs[XMLP.ERR_CLOSE_ENTITY=5]="Entity: missing closing sequence";
XMLP._errs[XMLP.ERR_PI_TARGET=6]="PI: target is required";
XMLP._errs[XMLP.ERR_ELM_EMPTY=7]="Element: cannot be both empty and closing";
XMLP._errs[XMLP.ERR_ELM_NAME=8]="Element: name must immediatly follow \"<\"";
XMLP._errs[XMLP.ERR_ELM_LT_NAME=9]="Element: \"<\" not allowed in element names";
XMLP._errs[XMLP.ERR_ATT_VALUES=10]="Attribute: values are required and must be in quotes";
XMLP._errs[XMLP.ERR_ATT_LT_NAME=11]="Element: \"<\" not allowed in attribute names";
XMLP._errs[XMLP.ERR_ATT_LT_VALUE=12]="Attribute: \"<\" not allowed in attribute values";
XMLP._errs[XMLP.ERR_ATT_DUP=13]="Attribute: duplicate attributes not allowed";
XMLP._errs[XMLP.ERR_ENTITY_UNKNOWN=14]="Entity: unknown entity";
XMLP._errs[XMLP.ERR_INFINITELOOP=15]="Infininte loop";
XMLP._errs[XMLP.ERR_DOC_STRUCTURE=16]="Document: only comments, processing instructions, or whitespace allowed outside of document element";
XMLP._errs[XMLP.ERR_ELM_NESTING=17]="Element: must be nested correctly";
XMLP.prototype._addAttribute=function(name,value){
this.m_atts[this.m_atts.length]=new Array(name,value);
};
XMLP.prototype._checkStructure=function(_4a6d){
if(XMLP._STATE_PROLOG==this.m_iState){
if((XMLP._TEXT==_4a6d)||(XMLP._ENTITY==_4a6d)){
if(SAXStrings.indexOfNonWhitespace(this.getContent(),this.getContentBegin(),this.getContentEnd())!=-1){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
}
if((XMLP._ELM_B==_4a6d)||(XMLP._ELM_EMP==_4a6d)){
this.m_iState=XMLP._STATE_DOCUMENT;
}
}
if(XMLP._STATE_DOCUMENT==this.m_iState){
if((XMLP._ELM_B==_4a6d)||(XMLP._ELM_EMP==_4a6d)){
this.m_stack.push(this.getName());
}
if((XMLP._ELM_E==_4a6d)||(XMLP._ELM_EMP==_4a6d)){
var _4a6e=this.m_stack.pop();
if((_4a6e===null)||(_4a6e!=this.getName())){
return this._setErr(XMLP.ERR_ELM_NESTING);
}
}
if(this.m_stack.count()===0){
this.m_iState=XMLP._STATE_MISC;
return _4a6d;
}
}
if(XMLP._STATE_MISC==this.m_iState){
if((XMLP._ELM_B==_4a6d)||(XMLP._ELM_E==_4a6d)||(XMLP._ELM_EMP==_4a6d)||(XMLP.EVT_DTD==_4a6d)){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
if((XMLP._TEXT==_4a6d)||(XMLP._ENTITY==_4a6d)){
if(SAXStrings.indexOfNonWhitespace(this.getContent(),this.getContentBegin(),this.getContentEnd())!=-1){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
}
}
return _4a6d;
};
XMLP.prototype._clearAttributes=function(){
this.m_atts=[];
};
XMLP.prototype._findAttributeIndex=function(name){
for(var i=0;i<this.m_atts.length;i++){
if(this.m_atts[i][XMLP._ATT_NAME]==name){
return i;
}
}
return -1;
};
XMLP.prototype.getAttributeCount=function(){
return this.m_atts?this.m_atts.length:0;
};
XMLP.prototype.getAttributeName=function(index){
return ((index<0)||(index>=this.m_atts.length))?null:this.m_atts[index][XMLP._ATT_NAME];
};
XMLP.prototype.getAttributeValue=function(index){
return ((index<0)||(index>=this.m_atts.length))?null:__unescapeString(this.m_atts[index][XMLP._ATT_VAL]);
};
XMLP.prototype.getAttributeValueByName=function(name){
return this.getAttributeValue(this._findAttributeIndex(name));
};
XMLP.prototype.getColumnNumber=function(){
return SAXStrings.getColumnNumber(this.m_xml,this.m_iP);
};
XMLP.prototype.getContent=function(){
return (this.m_cSrc==XMLP._CONT_XML)?this.m_xml:this.m_cAlt;
};
XMLP.prototype.getContentBegin=function(){
return this.m_cB;
};
XMLP.prototype.getContentEnd=function(){
return this.m_cE;
};
XMLP.prototype.getLineNumber=function(){
return SAXStrings.getLineNumber(this.m_xml,this.m_iP);
};
XMLP.prototype.getName=function(){
return this.m_name;
};
XMLP.prototype.next=function(){
return this._checkStructure(this._parse());
};
XMLP.prototype._parse=function(){
if(this.m_iP==this.m_xml.length){
return XMLP._NONE;
}
if(this.m_iP==this.m_xml.indexOf("<?",this.m_iP)){
return this._parsePI(this.m_iP+2);
}else{
if(this.m_iP==this.m_xml.indexOf("<!DOCTYPE",this.m_iP)){
return this._parseDTD(this.m_iP+9);
}else{
if(this.m_iP==this.m_xml.indexOf("<!--",this.m_iP)){
return this._parseComment(this.m_iP+4);
}else{
if(this.m_iP==this.m_xml.indexOf("<![CDATA[",this.m_iP)){
return this._parseCDATA(this.m_iP+9);
}else{
if(this.m_iP==this.m_xml.indexOf("<",this.m_iP)){
return this._parseElement(this.m_iP+1);
}else{
if(this.m_iP==this.m_xml.indexOf("&",this.m_iP)){
return this._parseEntity(this.m_iP+1);
}else{
return this._parseText(this.m_iP);
}
}
}
}
}
}
};
XMLP.prototype._parseAttribute=function(iB,iE){
var iNB,iNE,iEq,iVB,iVE;
var _4a77,strN,strV;
this.m_cAlt="";
iNB=SAXStrings.indexOfNonWhitespace(this.m_xml,iB,iE);
if((iNB==-1)||(iNB>=iE)){
return iNB;
}
iEq=this.m_xml.indexOf("=",iNB);
if((iEq==-1)||(iEq>iE)){
return this._setErr(XMLP.ERR_ATT_VALUES);
}
iNE=SAXStrings.lastIndexOfNonWhitespace(this.m_xml,iNB,iEq);
iVB=SAXStrings.indexOfNonWhitespace(this.m_xml,iEq+1,iE);
if((iVB==-1)||(iVB>iE)){
return this._setErr(XMLP.ERR_ATT_VALUES);
}
_4a77=this.m_xml.charAt(iVB);
if(SAXStrings.QUOTES.indexOf(_4a77)==-1){
return this._setErr(XMLP.ERR_ATT_VALUES);
}
iVE=this.m_xml.indexOf(_4a77,iVB+1);
if((iVE==-1)||(iVE>iE)){
return this._setErr(XMLP.ERR_ATT_VALUES);
}
strN=this.m_xml.substring(iNB,iNE+1);
strV=this.m_xml.substring(iVB+1,iVE);
if(strN.indexOf("<")!=-1){
return this._setErr(XMLP.ERR_ATT_LT_NAME);
}
if(strV.indexOf("<")!=-1){
return this._setErr(XMLP.ERR_ATT_LT_VALUE);
}
strV=SAXStrings.replace(strV,null,null,"\n"," ");
strV=SAXStrings.replace(strV,null,null,"\t"," ");
iRet=this._replaceEntities(strV);
if(iRet==XMLP._ERROR){
return iRet;
}
strV=this.m_cAlt;
if(this._findAttributeIndex(strN)==-1){
this._addAttribute(strN,strV);
}else{
return this._setErr(XMLP.ERR_ATT_DUP);
}
this.m_iP=iVE+2;
return XMLP._ATT;
};
XMLP.prototype._parseCDATA=function(iB){
var iE=this.m_xml.indexOf("]]>",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_CDATA);
}
this._setContent(XMLP._CONT_XML,iB,iE);
this.m_iP=iE+3;
return XMLP._CDATA;
};
XMLP.prototype._parseComment=function(iB){
var iE=this.m_xml.indexOf("-"+"->",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_COMMENT);
}
this._setContent(XMLP._CONT_XML,iB,iE);
this.m_iP=iE+3;
return XMLP._COMMENT;
};
XMLP.prototype._parseDTD=function(iB){
var iE,strClose,iInt,iLast;
iE=this.m_xml.indexOf(">",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_DTD);
}
iInt=this.m_xml.indexOf("[",iB);
strClose=((iInt!=-1)&&(iInt<iE))?"]>":">";
while(true){
if(iE==iLast){
return this._setErr(XMLP.ERR_INFINITELOOP);
}
iLast=iE;
iE=this.m_xml.indexOf(strClose,iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_DTD);
}
if(this.m_xml.substring(iE-1,iE+2)!="]]>"){
break;
}
}
this.m_iP=iE+strClose.length;
return XMLP._DTD;
};
XMLP.prototype._parseElement=function(iB){
var iE,iDE,iNE,iRet;
var iType,strN,iLast;
iDE=iE=this.m_xml.indexOf(">",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_ELM);
}
if(this.m_xml.charAt(iB)=="/"){
iType=XMLP._ELM_E;
iB++;
}else{
iType=XMLP._ELM_B;
}
if(this.m_xml.charAt(iE-1)=="/"){
if(iType==XMLP._ELM_E){
return this._setErr(XMLP.ERR_ELM_EMPTY);
}
iType=XMLP._ELM_EMP;
iDE--;
}
iDE=SAXStrings.lastIndexOfNonWhitespace(this.m_xml,iB,iDE);
if(iE-iB!=1){
if(SAXStrings.indexOfNonWhitespace(this.m_xml,iB,iDE)!=iB){
return this._setErr(XMLP.ERR_ELM_NAME);
}
}
this._clearAttributes();
iNE=SAXStrings.indexOfWhitespace(this.m_xml,iB,iDE);
if(iNE==-1){
iNE=iDE+1;
}else{
this.m_iP=iNE;
while(this.m_iP<iDE){
if(this.m_iP==iLast){
return this._setErr(XMLP.ERR_INFINITELOOP);
}
iLast=this.m_iP;
iRet=this._parseAttribute(this.m_iP,iDE);
if(iRet==XMLP._ERROR){
return iRet;
}
}
}
strN=this.m_xml.substring(iB,iNE);
if(strN.indexOf("<")!=-1){
return this._setErr(XMLP.ERR_ELM_LT_NAME);
}
this.m_name=strN;
this.m_iP=iE+1;
return iType;
};
XMLP.prototype._parseEntity=function(iB){
var iE=this.m_xml.indexOf(";",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_ENTITY);
}
this.m_iP=iE+1;
return this._replaceEntity(this.m_xml,iB,iE);
};
XMLP.prototype._parsePI=function(iB){
var iE,iTB,iTE,iCB,iCE;
iE=this.m_xml.indexOf("?>",iB);
if(iE==-1){
return this._setErr(XMLP.ERR_CLOSE_PI);
}
iTB=SAXStrings.indexOfNonWhitespace(this.m_xml,iB,iE);
if(iTB==-1){
return this._setErr(XMLP.ERR_PI_TARGET);
}
iTE=SAXStrings.indexOfWhitespace(this.m_xml,iTB,iE);
if(iTE==-1){
iTE=iE;
}
iCB=SAXStrings.indexOfNonWhitespace(this.m_xml,iTE,iE);
if(iCB==-1){
iCB=iE;
}
iCE=SAXStrings.lastIndexOfNonWhitespace(this.m_xml,iCB,iE);
if(iCE==-1){
iCE=iE-1;
}
this.m_name=this.m_xml.substring(iTB,iTE);
this._setContent(XMLP._CONT_XML,iCB,iCE+1);
this.m_iP=iE+2;
return XMLP._PI;
};
XMLP.prototype._parseText=function(iB){
var iE,iEE;
iE=this.m_xml.indexOf("<",iB);
if(iE==-1){
iE=this.m_xml.length;
}
iEE=this.m_xml.indexOf("&",iB);
if((iEE!=-1)&&(iEE<=iE)){
iE=iEE;
}
this._setContent(XMLP._CONT_XML,iB,iE);
this.m_iP=iE;
return XMLP._TEXT;
};
XMLP.prototype._replaceEntities=function(strD,iB,iE){
if(SAXStrings.isEmpty(strD)){
return "";
}
iB=iB||0;
iE=iE||strD.length;
var iEB,iEE,strRet="";
iEB=strD.indexOf("&",iB);
iEE=iB;
while((iEB>0)&&(iEB<iE)){
strRet+=strD.substring(iEE,iEB);
iEE=strD.indexOf(";",iEB)+1;
if((iEE===0)||(iEE>iE)){
return this._setErr(XMLP.ERR_CLOSE_ENTITY);
}
iRet=this._replaceEntity(strD,iEB+1,iEE-1);
if(iRet==XMLP._ERROR){
return iRet;
}
strRet+=this.m_cAlt;
iEB=strD.indexOf("&",iEE);
}
if(iEE!=iE){
strRet+=strD.substring(iEE,iE);
}
this._setContent(XMLP._CONT_ALT,strRet);
return XMLP._ENTITY;
};
XMLP.prototype._replaceEntity=function(strD,iB,iE){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
switch(strD.substring(iB,iE)){
case "amp":
strEnt="&";
break;
case "lt":
strEnt="<";
break;
case "gt":
strEnt=">";
break;
case "apos":
strEnt="'";
break;
case "quot":
strEnt="\"";
break;
default:
if(strD.charAt(iB)=="#"){
strEnt=String.fromCharCode(parseInt(strD.substring(iB+1,iE)));
}else{
return this._setErr(XMLP.ERR_ENTITY_UNKNOWN);
}
break;
}
this._setContent(XMLP._CONT_ALT,strEnt);
return XMLP._ENTITY;
};
XMLP.prototype._setContent=function(iSrc){
var args=arguments;
if(XMLP._CONT_XML==iSrc){
this.m_cAlt=null;
this.m_cB=args[1];
this.m_cE=args[2];
}else{
this.m_cAlt=args[1];
this.m_cB=0;
this.m_cE=args[1].length;
}
this.m_cSrc=iSrc;
};
XMLP.prototype._setErr=function(iErr){
var _4a91=XMLP._errs[iErr];
this.m_cAlt=_4a91;
this.m_cB=0;
this.m_cE=_4a91.length;
this.m_cSrc=XMLP._CONT_ALT;
return XMLP._ERROR;
};
SAXDriver=function(){
this.m_hndDoc=null;
this.m_hndErr=null;
this.m_hndLex=null;
};
SAXDriver.DOC_B=1;
SAXDriver.DOC_E=2;
SAXDriver.ELM_B=3;
SAXDriver.ELM_E=4;
SAXDriver.CHARS=5;
SAXDriver.PI=6;
SAXDriver.CD_B=7;
SAXDriver.CD_E=8;
SAXDriver.CMNT=9;
SAXDriver.DTD_B=10;
SAXDriver.DTD_E=11;
SAXDriver.prototype.parse=function(strD){
var _4a93=new XMLP(strD);
if(this.m_hndDoc&&this.m_hndDoc.setDocumentLocator){
this.m_hndDoc.setDocumentLocator(this);
}
this.m_parser=_4a93;
this.m_bErr=false;
if(!this.m_bErr){
this._fireEvent(SAXDriver.DOC_B);
}
this._parseLoop();
if(!this.m_bErr){
this._fireEvent(SAXDriver.DOC_E);
}
this.m_xml=null;
this.m_iP=0;
};
SAXDriver.prototype.setDocumentHandler=function(hnd){
this.m_hndDoc=hnd;
};
SAXDriver.prototype.setErrorHandler=function(hnd){
this.m_hndErr=hnd;
};
SAXDriver.prototype.setLexicalHandler=function(hnd){
this.m_hndLex=hnd;
};
SAXDriver.prototype.getColumnNumber=function(){
return this.m_parser.getColumnNumber();
};
SAXDriver.prototype.getLineNumber=function(){
return this.m_parser.getLineNumber();
};
SAXDriver.prototype.getMessage=function(){
return this.m_strErrMsg;
};
SAXDriver.prototype.getPublicId=function(){
return null;
};
SAXDriver.prototype.getSystemId=function(){
return null;
};
SAXDriver.prototype.getLength=function(){
return this.m_parser.getAttributeCount();
};
SAXDriver.prototype.getName=function(index){
return this.m_parser.getAttributeName(index);
};
SAXDriver.prototype.getValue=function(index){
return this.m_parser.getAttributeValue(index);
};
SAXDriver.prototype.getValueByName=function(name){
return this.m_parser.getAttributeValueByName(name);
};
SAXDriver.prototype._fireError=function(_4a9a){
this.m_strErrMsg=_4a9a;
this.m_bErr=true;
if(this.m_hndErr&&this.m_hndErr.fatalError){
this.m_hndErr.fatalError(this);
}
};
SAXDriver.prototype._fireEvent=function(iEvt){
var hnd,func,args=arguments,iLen=args.length-1;
if(this.m_bErr){
return;
}
if(SAXDriver.DOC_B==iEvt){
func="startDocument";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.DOC_E==iEvt){
func="endDocument";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.ELM_B==iEvt){
func="startElement";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.ELM_E==iEvt){
func="endElement";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.CHARS==iEvt){
func="characters";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.PI==iEvt){
func="processingInstruction";
hnd=this.m_hndDoc;
}else{
if(SAXDriver.CD_B==iEvt){
func="startCDATA";
hnd=this.m_hndLex;
}else{
if(SAXDriver.CD_E==iEvt){
func="endCDATA";
hnd=this.m_hndLex;
}else{
if(SAXDriver.CMNT==iEvt){
func="comment";
hnd=this.m_hndLex;
}
}
}
}
}
}
}
}
}
if(hnd&&hnd[func]){
if(0==iLen){
hnd[func]();
}else{
if(1==iLen){
hnd[func](args[1]);
}else{
if(2==iLen){
hnd[func](args[1],args[2]);
}else{
if(3==iLen){
hnd[func](args[1],args[2],args[3]);
}
}
}
}
}
};
SAXDriver.prototype._parseLoop=function(_4a9d){
var _4a9e,_4a9d;
_4a9d=this.m_parser;
while(!this.m_bErr){
_4a9e=_4a9d.next();
if(_4a9e==XMLP._ELM_B){
this._fireEvent(SAXDriver.ELM_B,_4a9d.getName(),this);
}else{
if(_4a9e==XMLP._ELM_E){
this._fireEvent(SAXDriver.ELM_E,_4a9d.getName());
}else{
if(_4a9e==XMLP._ELM_EMP){
this._fireEvent(SAXDriver.ELM_B,_4a9d.getName(),this);
this._fireEvent(SAXDriver.ELM_E,_4a9d.getName());
}else{
if(_4a9e==XMLP._TEXT){
this._fireEvent(SAXDriver.CHARS,_4a9d.getContent(),_4a9d.getContentBegin(),_4a9d.getContentEnd()-_4a9d.getContentBegin());
}else{
if(_4a9e==XMLP._ENTITY){
this._fireEvent(SAXDriver.CHARS,_4a9d.getContent(),_4a9d.getContentBegin(),_4a9d.getContentEnd()-_4a9d.getContentBegin());
}else{
if(_4a9e==XMLP._PI){
this._fireEvent(SAXDriver.PI,_4a9d.getName(),_4a9d.getContent().substring(_4a9d.getContentBegin(),_4a9d.getContentEnd()));
}else{
if(_4a9e==XMLP._CDATA){
this._fireEvent(SAXDriver.CD_B);
this._fireEvent(SAXDriver.CHARS,_4a9d.getContent(),_4a9d.getContentBegin(),_4a9d.getContentEnd()-_4a9d.getContentBegin());
this._fireEvent(SAXDriver.CD_E);
}else{
if(_4a9e==XMLP._COMMENT){
this._fireEvent(SAXDriver.CMNT,_4a9d.getContent(),_4a9d.getContentBegin(),_4a9d.getContentEnd()-_4a9d.getContentBegin());
}else{
if(_4a9e==XMLP._DTD){
}else{
if(_4a9e==XMLP._ERROR){
this._fireError(_4a9d.getContent());
}else{
if(_4a9e==XMLP._NONE){
return;
}
}
}
}
}
}
}
}
}
}
}
}
};
SAXStrings=function(){
};
SAXStrings.WHITESPACE=" \t\n\r";
SAXStrings.QUOTES="\"'";
SAXStrings.getColumnNumber=function(strD,iP){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iP=iP||strD.length;
var arrD=strD.substring(0,iP).split("\n");
var _4aa2=arrD[arrD.length-1];
arrD.length--;
var _4aa3=arrD.join("\n").length;
return iP-_4aa3;
};
SAXStrings.getLineNumber=function(strD,iP){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iP=iP||strD.length;
return strD.substring(0,iP).split("\n").length;
};
SAXStrings.indexOfNonWhitespace=function(strD,iB,iE){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iB;i<iE;i++){
if(SAXStrings.WHITESPACE.indexOf(strD.charAt(i))==-1){
return i;
}
}
return -1;
};
SAXStrings.indexOfWhitespace=function(strD,iB,iE){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iB;i<iE;i++){
if(SAXStrings.WHITESPACE.indexOf(strD.charAt(i))!=-1){
return i;
}
}
return -1;
};
SAXStrings.isEmpty=function(strD){
return (strD===null)||(strD.length===0);
};
SAXStrings.lastIndexOfNonWhitespace=function(strD,iB,iE){
if(SAXStrings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iE-1;i>=iB;i--){
if(SAXStrings.WHITESPACE.indexOf(strD.charAt(i))==-1){
return i;
}
}
return -1;
};
SAXStrings.replace=function(strD,iB,iE,strF,strR){
if(SAXStrings.isEmpty(strD)){
return "";
}
iB=iB||0;
iE=iE||strD.length;
return strD.substring(iB,iE).split(strF).join(strR);
};
Stack=function(){
this.m_arr=[];
};
Stack.prototype.clear=function(){
this.m_arr=[];
};
Stack.prototype.count=function(){
return this.m_arr.length;
};
Stack.prototype.destroy=function(){
this.m_arr=null;
};
Stack.prototype.peek=function(){
if(this.m_arr.length===0){
return null;
}
return this.m_arr[this.m_arr.length-1];
};
Stack.prototype.pop=function(){
if(this.m_arr.length===0){
return null;
}
var o=this.m_arr[this.m_arr.length-1];
this.m_arr.length--;
return o;
};
Stack.prototype.push=function(o){
this.m_arr[this.m_arr.length]=o;
};
function isEmpty(str){
return (str===null)||(str.length==0);
}
function trim(_4abb,_4abc,_4abd){
if(isEmpty(_4abb)){
return "";
}
if(_4abc===null){
_4abc=true;
}
if(_4abd===null){
_4abd=true;
}
var left=0;
var right=0;
var i=0;
var k=0;
if(_4abc==true){
while((i<_4abb.length)&&(whitespace.indexOf(_4abb.charAt(i++))!=-1)){
left++;
}
}
if(_4abd==true){
k=_4abb.length-1;
while((k>=left)&&(whitespace.indexOf(_4abb.charAt(k--))!=-1)){
right++;
}
}
return _4abb.substring(left,_4abb.length-right);
}
function __escapeString(str){
var _4ac3=/&/g;
var _4ac4=/</g;
var _4ac5=/>/g;
var _4ac6=/"/g;
var _4ac7=/'/g;
str=str.replace(_4ac3,"&amp;");
str=str.replace(_4ac4,"&lt;");
str=str.replace(_4ac5,"&gt;");
str=str.replace(_4ac6,"&quot;");
str=str.replace(_4ac7,"&apos;");
return str;
}
function __unescapeString(str){
var _4ac9=/&amp;/g;
var _4aca=/&lt;/g;
var _4acb=/&gt;/g;
var _4acc=/&quot;/g;
var _4acd=/&apos;/g;
str=str.replace(_4ac9,"&");
str=str.replace(_4aca,"<");
str=str.replace(_4acb,">");
str=str.replace(_4acc,"\"");
str=str.replace(_4acd,"'");
return str;
}
function addClass(_5dea,_5deb){
if(_5dea){
if(_5dea.indexOf("|"+_5deb+"|")<0){
_5dea+=_5deb+"|";
}
}else{
_5dea="|"+_5deb+"|";
}
return _5dea;
}
DOMException=function(code){
this._class=addClass(this._class,"DOMException");
this.code=code;
};
DOMException.INDEX_SIZE_ERR=1;
DOMException.DOMSTRING_SIZE_ERR=2;
DOMException.HIERARCHY_REQUEST_ERR=3;
DOMException.WRONG_DOCUMENT_ERR=4;
DOMException.INVALID_CHARACTER_ERR=5;
DOMException.NO_DATA_ALLOWED_ERR=6;
DOMException.NO_MODIFICATION_ALLOWED_ERR=7;
DOMException.NOT_FOUND_ERR=8;
DOMException.NOT_SUPPORTED_ERR=9;
DOMException.INUSE_ATTRIBUTE_ERR=10;
DOMException.INVALID_STATE_ERR=11;
DOMException.SYNTAX_ERR=12;
DOMException.INVALID_MODIFICATION_ERR=13;
DOMException.NAMESPACE_ERR=14;
DOMException.INVALID_ACCESS_ERR=15;
DOMImplementation=function(){
this._class=addClass(this._class,"DOMImplementation");
this._p=null;
this.preserveWhiteSpace=false;
this.namespaceAware=true;
this.errorChecking=true;
};
DOMImplementation.prototype.escapeString=function DOMNode__escapeString(str){
return __escapeString(str);
};
DOMImplementation.prototype.unescapeString=function DOMNode__unescapeString(str){
return __unescapeString(str);
};
DOMImplementation.prototype.hasFeature=function DOMImplementation_hasFeature(_5def,_5df0){
var ret=false;
if(_5def.toLowerCase()=="xml"){
ret=(!_5df0||(_5df0=="1.0")||(_5df0=="2.0"));
}else{
if(_5def.toLowerCase()=="core"){
ret=(!_5df0||(_5df0=="2.0"));
}
}
return ret;
};
DOMImplementation.prototype.loadXML=function DOMImplementation_loadXML(_5df2){
var _5df3;
try{
_5df3=new XMLP(_5df2);
}
catch(e){
alert("Error Creating the SAX Parser. Did you include xmlsax.js or tinyxmlsax.js in your web page?\nThe SAX parser is needed to populate XML for <SCRIPT>'s W3C DOM Parser with data.");
}
var doc=new DOMDocument(this);
this._parseLoop(doc,_5df3);
doc._parseComplete=true;
return doc;
};
DOMImplementation.prototype.translateErrCode=function DOMImplementation_translateErrCode(code){
var msg="";
switch(code){
case DOMException.INDEX_SIZE_ERR:
msg="INDEX_SIZE_ERR: Index out of bounds";
break;
case DOMException.DOMSTRING_SIZE_ERR:
msg="DOMSTRING_SIZE_ERR: The resulting string is too long to fit in a DOMString";
break;
case DOMException.HIERARCHY_REQUEST_ERR:
msg="HIERARCHY_REQUEST_ERR: The Node can not be inserted at this location";
break;
case DOMException.WRONG_DOCUMENT_ERR:
msg="WRONG_DOCUMENT_ERR: The source and the destination Documents are not the same";
break;
case DOMException.INVALID_CHARACTER_ERR:
msg="INVALID_CHARACTER_ERR: The string contains an invalid character";
break;
case DOMException.NO_DATA_ALLOWED_ERR:
msg="NO_DATA_ALLOWED_ERR: This Node / NodeList does not support data";
break;
case DOMException.NO_MODIFICATION_ALLOWED_ERR:
msg="NO_MODIFICATION_ALLOWED_ERR: This object cannot be modified";
break;
case DOMException.NOT_FOUND_ERR:
msg="NOT_FOUND_ERR: The item cannot be found";
break;
case DOMException.NOT_SUPPORTED_ERR:
msg="NOT_SUPPORTED_ERR: This implementation does not support function";
break;
case DOMException.INUSE_ATTRIBUTE_ERR:
msg="INUSE_ATTRIBUTE_ERR: The Attribute has already been assigned to another Element";
break;
case DOMException.INVALID_STATE_ERR:
msg="INVALID_STATE_ERR: The object is no longer usable";
break;
case DOMException.SYNTAX_ERR:
msg="SYNTAX_ERR: Syntax error";
break;
case DOMException.INVALID_MODIFICATION_ERR:
msg="INVALID_MODIFICATION_ERR: Cannot change the type of the object";
break;
case DOMException.NAMESPACE_ERR:
msg="NAMESPACE_ERR: The namespace declaration is incorrect";
break;
case DOMException.INVALID_ACCESS_ERR:
msg="INVALID_ACCESS_ERR: The object does not support this function";
break;
default:
msg="UNKNOWN: Unknown Exception Code ("+code+")";
}
return msg;
};
DOMImplementation.prototype._parseLoop=function DOMImplementation__parseLoop(doc,p){
var iEvt,iNode,iAttr,strName;
iNodeParent=doc;
var _5dfa=0;
var _5dfb=[];
var _5dfc=[];
if(this.namespaceAware){
var iNS=doc.createNamespace("");
iNS.setValue("http://www.w3.org/2000/xmlns/");
doc._namespaces.setNamedItem(iNS);
}
while(true){
iEvt=p.next();
if(iEvt==XMLP._ELM_B){
var pName=p.getName();
pName=trim(pName,true,true);
if(!this.namespaceAware){
iNode=doc.createElement(p.getName());
for(var i=0;i<p.getAttributeCount();i++){
strName=p.getAttributeName(i);
iAttr=iNode.getAttributeNode(strName);
if(!iAttr){
iAttr=doc.createAttribute(strName);
}
iAttr.setValue(p.getAttributeValue(i));
iNode.setAttributeNode(iAttr);
}
}else{
iNode=doc.createElementNS("",p.getName());
iNode._namespaces=iNodeParent._namespaces._cloneNodes(iNode);
for(var i=0;i<p.getAttributeCount();i++){
strName=p.getAttributeName(i);
if(this._isNamespaceDeclaration(strName)){
var _5e00=this._parseNSName(strName);
if(strName!="xmlns"){
iNS=doc.createNamespace(strName);
}else{
iNS=doc.createNamespace("");
}
iNS.setValue(p.getAttributeValue(i));
iNode._namespaces.setNamedItem(iNS);
}else{
iAttr=iNode.getAttributeNode(strName);
if(!iAttr){
iAttr=doc.createAttributeNS("",strName);
}
iAttr.setValue(p.getAttributeValue(i));
iNode.setAttributeNodeNS(iAttr);
if(this._isIdDeclaration(strName)){
iNode.id=p.getAttributeValue(i);
}
}
}
if(iNode._namespaces.getNamedItem(iNode.prefix)){
iNode.namespaceURI=iNode._namespaces.getNamedItem(iNode.prefix).value;
}
for(var i=0;i<iNode.attributes.length;i++){
if(iNode.attributes.item(i).prefix!=""){
if(iNode._namespaces.getNamedItem(iNode.attributes.item(i).prefix)){
iNode.attributes.item(i).namespaceURI=iNode._namespaces.getNamedItem(iNode.attributes.item(i).prefix).value;
}
}
}
}
if(iNodeParent.nodeType==DOMNode.DOCUMENT_NODE){
iNodeParent.documentElement=iNode;
}
iNodeParent.appendChild(iNode);
iNodeParent=iNode;
}else{
if(iEvt==XMLP._ELM_E){
iNodeParent=iNodeParent.parentNode;
}else{
if(iEvt==XMLP._ELM_EMP){
pName=p.getName();
pName=trim(pName,true,true);
if(!this.namespaceAware){
iNode=doc.createElement(pName);
for(var i=0;i<p.getAttributeCount();i++){
strName=p.getAttributeName(i);
iAttr=iNode.getAttributeNode(strName);
if(!iAttr){
iAttr=doc.createAttribute(strName);
}
iAttr.setValue(p.getAttributeValue(i));
iNode.setAttributeNode(iAttr);
}
}else{
iNode=doc.createElementNS("",p.getName());
iNode._namespaces=iNodeParent._namespaces._cloneNodes(iNode);
for(var i=0;i<p.getAttributeCount();i++){
strName=p.getAttributeName(i);
if(this._isNamespaceDeclaration(strName)){
var _5e00=this._parseNSName(strName);
if(strName!="xmlns"){
iNS=doc.createNamespace(strName);
}else{
iNS=doc.createNamespace("");
}
iNS.setValue(p.getAttributeValue(i));
iNode._namespaces.setNamedItem(iNS);
}else{
iAttr=iNode.getAttributeNode(strName);
if(!iAttr){
iAttr=doc.createAttributeNS("",strName);
}
iAttr.setValue(p.getAttributeValue(i));
iNode.setAttributeNodeNS(iAttr);
if(this._isIdDeclaration(strName)){
iNode.id=p.getAttributeValue(i);
}
}
}
if(iNode._namespaces.getNamedItem(iNode.prefix)){
iNode.namespaceURI=iNode._namespaces.getNamedItem(iNode.prefix).value;
}
for(var i=0;i<iNode.attributes.length;i++){
if(iNode.attributes.item(i).prefix!=""){
if(iNode._namespaces.getNamedItem(iNode.attributes.item(i).prefix)){
iNode.attributes.item(i).namespaceURI=iNode._namespaces.getNamedItem(iNode.attributes.item(i).prefix).value;
}
}
}
}
if(iNodeParent.nodeType==DOMNode.DOCUMENT_NODE){
iNodeParent.documentElement=iNode;
}
iNodeParent.appendChild(iNode);
}else{
if(iEvt==XMLP._TEXT||iEvt==XMLP._ENTITY){
var _5e01=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
if(trim(_5e01,true,true)==""){
_5e01="";
}
}
if(_5e01.length>0){
var _5e02=doc.createTextNode(_5e01);
iNodeParent.appendChild(_5e02);
if(iEvt==XMLP._ENTITY){
_5dfb[_5dfb.length]=_5e02;
}else{
_5dfc[_5dfc.length]=_5e02;
}
}
}else{
if(iEvt==XMLP._PI){
iNodeParent.appendChild(doc.createProcessingInstruction(p.getName(),p.getContent().substring(p.getContentBegin(),p.getContentEnd())));
}else{
if(iEvt==XMLP._CDATA){
_5e01=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
_5e01=trim(_5e01,true,true);
_5e01.replace(/ +/g," ");
}
if(_5e01.length>0){
iNodeParent.appendChild(doc.createCDATASection(_5e01));
}
}else{
if(iEvt==XMLP._COMMENT){
var _5e01=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
_5e01=trim(_5e01,true,true);
_5e01.replace(/ +/g," ");
}
if(_5e01.length>0){
iNodeParent.appendChild(doc.createComment(_5e01));
}
}else{
if(iEvt==XMLP._DTD){
}else{
if(iEvt==XMLP._ERROR){
throw (new DOMException(DOMException.SYNTAX_ERR));
}else{
if(iEvt==XMLP._NONE){
if(iNodeParent==doc){
break;
}else{
throw (new DOMException(DOMException.SYNTAX_ERR));
}
}
}
}
}
}
}
}
}
}
}
}
var _5e03=_5dfb.length;
for(intLoop=0;intLoop<_5e03;intLoop++){
var _5e04=_5dfb[intLoop];
var _5e05=_5e04.getParentNode();
if(_5e05){
_5e05.normalize();
if(!this.preserveWhiteSpace){
var _5e06=_5e05.getChildNodes();
var _5e07=_5e06.getLength();
for(intLoop2=0;intLoop2<_5e07;intLoop2++){
var child=_5e06.item(intLoop2);
if(child.getNodeType()==DOMNode.TEXT_NODE){
var _5e09=child.getData();
_5e09=trim(_5e09,true,true);
_5e09.replace(/ +/g," ");
child.setData(_5e09);
}
}
}
}
}
if(!this.preserveWhiteSpace){
var _5e03=_5dfc.length;
for(intLoop=0;intLoop<_5e03;intLoop++){
var node=_5dfc[intLoop];
if(node.getParentNode()!==null){
var _5e0b=node.getData();
_5e0b=trim(_5e0b,true,true);
_5e0b.replace(/ +/g," ");
node.setData(_5e0b);
}
}
}
};
DOMImplementation.prototype._isNamespaceDeclaration=function DOMImplementation__isNamespaceDeclaration(_5e0c){
return (_5e0c.indexOf("xmlns")>-1);
};
DOMImplementation.prototype._isIdDeclaration=function DOMImplementation__isIdDeclaration(_5e0d){
return (_5e0d.toLowerCase()=="id");
};
DOMImplementation.prototype._isValidName=function DOMImplementation__isValidName(name){
return name.match(re_validName);
};
re_validName=/^[a-zA-Z_:][a-zA-Z0-9\.\-_:]*$/;
DOMImplementation.prototype._isValidString=function DOMImplementation__isValidString(name){
return (name.search(re_invalidStringChars)<0);
};
re_invalidStringChars=/\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F/;
DOMImplementation.prototype._parseNSName=function DOMImplementation__parseNSName(_5e10){
var _5e11={};
_5e11.prefix=_5e10;
_5e11.namespaceName="";
delimPos=_5e10.indexOf(":");
if(delimPos>-1){
_5e11.prefix=_5e10.substring(0,delimPos);
_5e11.namespaceName=_5e10.substring(delimPos+1,_5e10.length);
}
return _5e11;
};
DOMImplementation.prototype._parseQName=function DOMImplementation__parseQName(_5e12){
var _5e13={};
_5e13.localName=_5e12;
_5e13.prefix="";
delimPos=_5e12.indexOf(":");
if(delimPos>-1){
_5e13.prefix=_5e12.substring(0,delimPos);
_5e13.localName=_5e12.substring(delimPos+1,_5e12.length);
}
return _5e13;
};
DOMNodeList=function(_5e14,_5e15){
this._class=addClass(this._class,"DOMNodeList");
this._nodes=[];
this.length=0;
this.parentNode=_5e15;
this.ownerDocument=_5e14;
this._readonly=false;
};
DOMNodeList.prototype.getLength=function DOMNodeList_getLength(){
return this.length;
};
DOMNodeList.prototype.item=function DOMNodeList_item(index){
var ret=null;
if((index>=0)&&(index<this._nodes.length)){
ret=this._nodes[index];
}
return ret;
};
DOMNodeList.prototype._findItemIndex=function DOMNodeList__findItemIndex(id){
var ret=-1;
if(id>-1){
for(var i=0;i<this._nodes.length;i++){
if(this._nodes[i]._id==id){
ret=i;
break;
}
}
}
return ret;
};
DOMNodeList.prototype._insertBefore=function DOMNodeList__insertBefore(_5e1b,_5e1c){
if((_5e1c>=0)&&(_5e1c<this._nodes.length)){
var _5e1d=[];
_5e1d=this._nodes.slice(0,_5e1c);
if(_5e1b.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
_5e1d=_5e1d.concat(_5e1b.childNodes._nodes);
}else{
_5e1d[_5e1d.length]=_5e1b;
}
this._nodes=_5e1d.concat(this._nodes.slice(_5e1c));
this.length=this._nodes.length;
}
};
DOMNodeList.prototype._replaceChild=function DOMNodeList__replaceChild(_5e1e,_5e1f){
var ret=null;
if((_5e1f>=0)&&(_5e1f<this._nodes.length)){
ret=this._nodes[_5e1f];
if(_5e1e.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
var _5e21=[];
_5e21=this._nodes.slice(0,_5e1f);
_5e21=_5e21.concat(_5e1e.childNodes._nodes);
this._nodes=_5e21.concat(this._nodes.slice(_5e1f+1));
}else{
this._nodes[_5e1f]=_5e1e;
}
}
return ret;
};
DOMNodeList.prototype._removeChild=function DOMNodeList__removeChild(_5e22){
var ret=null;
if(_5e22>-1){
ret=this._nodes[_5e22];
var _5e24=[];
_5e24=this._nodes.slice(0,_5e22);
this._nodes=_5e24.concat(this._nodes.slice(_5e22+1));
this.length=this._nodes.length;
}
return ret;
};
DOMNodeList.prototype._appendChild=function DOMNodeList__appendChild(_5e25){
if(_5e25.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
this._nodes=this._nodes.concat(_5e25.childNodes._nodes);
}else{
this._nodes[this._nodes.length]=_5e25;
}
this.length=this._nodes.length;
};
DOMNodeList.prototype._cloneNodes=function DOMNodeList__cloneNodes(deep,_5e27){
var _5e28=new DOMNodeList(this.ownerDocument,_5e27);
for(var i=0;i<this._nodes.length;i++){
_5e28._appendChild(this._nodes[i].cloneNode(deep));
}
return _5e28;
};
DOMNodeList.prototype.toString=function DOMNodeList_toString(){
var ret="";
for(var i=0;i<this.length;i++){
ret+=this._nodes[i].toString();
}
return ret;
};
DOMNamedNodeMap=function(_5e2c,_5e2d){
this._class=addClass(this._class,"DOMNamedNodeMap");
this.DOMNodeList=DOMNodeList;
this.DOMNodeList(_5e2c,_5e2d);
};
DOMNamedNodeMap.prototype=new DOMNodeList;
DOMNamedNodeMap.prototype.getNamedItem=function DOMNamedNodeMap_getNamedItem(name){
var ret=null;
var _5e30=this._findNamedItemIndex(name);
if(_5e30>-1){
ret=this._nodes[_5e30];
}
return ret;
};
DOMNamedNodeMap.prototype.setNamedItem=function DOMNamedNodeMap_setNamedItem(arg){
if(this.ownerDocument.implementation.errorChecking){
if(this.ownerDocument!=arg.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._readonly||(this.parentNode&&this.parentNode._readonly)){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(arg.ownerElement&&(arg.ownerElement!=this.parentNode)){
throw (new DOMException(DOMException.INUSE_ATTRIBUTE_ERR));
}
}
var _5e32=this._findNamedItemIndex(arg.name);
var ret=null;
if(_5e32>-1){
ret=this._nodes[_5e32];
if(this.ownerDocument.implementation.errorChecking&&ret._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}else{
this._nodes[_5e32]=arg;
}
}else{
this._nodes[this.length]=arg;
}
this.length=this._nodes.length;
arg.ownerElement=this.parentNode;
return ret;
};
DOMNamedNodeMap.prototype.removeNamedItem=function DOMNamedNodeMap_removeNamedItem(name){
var ret=null;
if(this.ownerDocument.implementation.errorChecking&&(this._readonly||(this.parentNode&&this.parentNode._readonly))){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _5e36=this._findNamedItemIndex(name);
if(this.ownerDocument.implementation.errorChecking&&(_5e36<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _5e37=this._nodes[_5e36];
if(this.ownerDocument.implementation.errorChecking&&_5e37._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
return this._removeChild(_5e36);
};
DOMNamedNodeMap.prototype.getNamedItemNS=function DOMNamedNodeMap_getNamedItemNS(_5e38,_5e39){
var ret=null;
var _5e3b=this._findNamedItemNSIndex(_5e38,_5e39);
if(_5e3b>-1){
ret=this._nodes[_5e3b];
}
return ret;
};
DOMNamedNodeMap.prototype.setNamedItemNS=function DOMNamedNodeMap_setNamedItemNS(arg){
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly||(this.parentNode&&this.parentNode._readonly)){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=arg.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(arg.ownerElement&&(arg.ownerElement!=this.parentNode)){
throw (new DOMException(DOMException.INUSE_ATTRIBUTE_ERR));
}
}
var _5e3d=this._findNamedItemNSIndex(arg.namespaceURI,arg.localName);
var ret=null;
if(_5e3d>-1){
ret=this._nodes[_5e3d];
if(this.ownerDocument.implementation.errorChecking&&ret._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}else{
this._nodes[_5e3d]=arg;
}
}else{
this._nodes[this.length]=arg;
}
this.length=this._nodes.length;
arg.ownerElement=this.parentNode;
return ret;
};
DOMNamedNodeMap.prototype.removeNamedItemNS=function DOMNamedNodeMap_removeNamedItemNS(_5e3f,_5e40){
var ret=null;
if(this.ownerDocument.implementation.errorChecking&&(this._readonly||(this.parentNode&&this.parentNode._readonly))){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _5e42=this._findNamedItemNSIndex(_5e3f,_5e40);
if(this.ownerDocument.implementation.errorChecking&&(_5e42<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _5e43=this._nodes[_5e42];
if(this.ownerDocument.implementation.errorChecking&&_5e43._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
return this._removeChild(_5e42);
};
DOMNamedNodeMap.prototype._findNamedItemIndex=function DOMNamedNodeMap__findNamedItemIndex(name){
var ret=-1;
for(var i=0;i<this._nodes.length;i++){
if(this._nodes[i].name==name){
ret=i;
break;
}
}
return ret;
};
DOMNamedNodeMap.prototype._findNamedItemNSIndex=function DOMNamedNodeMap__findNamedItemNSIndex(_5e47,_5e48){
var ret=-1;
if(_5e48){
for(var i=0;i<this._nodes.length;i++){
if((this._nodes[i].namespaceURI==_5e47)&&(this._nodes[i].localName==_5e48)){
ret=i;
break;
}
}
}
return ret;
};
DOMNamedNodeMap.prototype._hasAttribute=function DOMNamedNodeMap__hasAttribute(name){
var ret=false;
var _5e4d=this._findNamedItemIndex(name);
if(_5e4d>-1){
ret=true;
}
return ret;
};
DOMNamedNodeMap.prototype._hasAttributeNS=function DOMNamedNodeMap__hasAttributeNS(_5e4e,_5e4f){
var ret=false;
var _5e51=this._findNamedItemNSIndex(_5e4e,_5e4f);
if(_5e51>-1){
ret=true;
}
return ret;
};
DOMNamedNodeMap.prototype._cloneNodes=function DOMNamedNodeMap__cloneNodes(_5e52){
var _5e53=new DOMNamedNodeMap(this.ownerDocument,_5e52);
for(var i=0;i<this._nodes.length;i++){
_5e53._appendChild(this._nodes[i].cloneNode(false));
}
return _5e53;
};
DOMNamedNodeMap.prototype.toString=function DOMNamedNodeMap_toString(){
var ret="";
for(var i=0;i<this.length-1;i++){
ret+=this._nodes[i].toString()+" ";
}
if(this.length>0){
ret+=this._nodes[this.length-1].toString();
}
return ret;
};
DOMNamespaceNodeMap=function(_5e57,_5e58){
this._class=addClass(this._class,"DOMNamespaceNodeMap");
this.DOMNamedNodeMap=DOMNamedNodeMap;
this.DOMNamedNodeMap(_5e57,_5e58);
};
DOMNamespaceNodeMap.prototype=new DOMNamedNodeMap;
DOMNamespaceNodeMap.prototype._findNamedItemIndex=function DOMNamespaceNodeMap__findNamedItemIndex(_5e59){
var ret=-1;
for(var i=0;i<this._nodes.length;i++){
if(this._nodes[i].localName==_5e59){
ret=i;
break;
}
}
return ret;
};
DOMNamespaceNodeMap.prototype._cloneNodes=function DOMNamespaceNodeMap__cloneNodes(_5e5c){
var _5e5d=new DOMNamespaceNodeMap(this.ownerDocument,_5e5c);
for(var i=0;i<this._nodes.length;i++){
_5e5d._appendChild(this._nodes[i].cloneNode(false));
}
return _5e5d;
};
DOMNamespaceNodeMap.prototype.toString=function DOMNamespaceNodeMap_toString(){
var ret="";
for(var ind=0;ind<this._nodes.length;ind++){
var ns=null;
try{
var ns=this.parentNode.parentNode._namespaces.getNamedItem(this._nodes[ind].localName);
}
catch(e){
break;
}
if(!(ns&&(""+ns.nodeValue==""+this._nodes[ind].nodeValue))){
ret+=this._nodes[ind].toString()+" ";
}
}
return ret;
};
DOMNode=function(_5e62){
this._class=addClass(this._class,"DOMNode");
if(_5e62){
this._id=_5e62._genId();
}
this.namespaceURI="";
this.prefix="";
this.localName="";
this.nodeName="";
this.nodeValue="";
this.nodeType=0;
this.parentNode=null;
this.childNodes=new DOMNodeList(_5e62,this);
this.firstChild=null;
this.lastChild=null;
this.previousSibling=null;
this.nextSibling=null;
this.attributes=new DOMNamedNodeMap(_5e62,this);
this.ownerDocument=_5e62;
this._namespaces=new DOMNamespaceNodeMap(_5e62,this);
this._readonly=false;
};
DOMNode.ELEMENT_NODE=1;
DOMNode.ATTRIBUTE_NODE=2;
DOMNode.TEXT_NODE=3;
DOMNode.CDATA_SECTION_NODE=4;
DOMNode.ENTITY_REFERENCE_NODE=5;
DOMNode.ENTITY_NODE=6;
DOMNode.PROCESSING_INSTRUCTION_NODE=7;
DOMNode.COMMENT_NODE=8;
DOMNode.DOCUMENT_NODE=9;
DOMNode.DOCUMENT_TYPE_NODE=10;
DOMNode.DOCUMENT_FRAGMENT_NODE=11;
DOMNode.NOTATION_NODE=12;
DOMNode.NAMESPACE_NODE=13;
DOMNode.prototype.hasAttributes=function DOMNode_hasAttributes(){
if(this.attributes.length===0){
return false;
}else{
return true;
}
};
DOMNode.prototype.getNodeName=function DOMNode_getNodeName(){
return this.nodeName;
};
DOMNode.prototype.getNodeValue=function DOMNode_getNodeValue(){
return this.nodeValue;
};
DOMNode.prototype.setNodeValue=function DOMNode_setNodeValue(_5e63){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.nodeValue=_5e63;
};
DOMNode.prototype.getNodeType=function DOMNode_getNodeType(){
return this.nodeType;
};
DOMNode.prototype.getParentNode=function DOMNode_getParentNode(){
return this.parentNode;
};
DOMNode.prototype.getChildNodes=function DOMNode_getChildNodes(){
return this.childNodes;
};
DOMNode.prototype.getFirstChild=function DOMNode_getFirstChild(){
return this.firstChild;
};
DOMNode.prototype.getLastChild=function DOMNode_getLastChild(){
return this.lastChild;
};
DOMNode.prototype.getPreviousSibling=function DOMNode_getPreviousSibling(){
return this.previousSibling;
};
DOMNode.prototype.getNextSibling=function DOMNode_getNextSibling(){
return this.nextSibling;
};
DOMNode.prototype.getAttributes=function DOMNode_getAttributes(){
return this.attributes;
};
DOMNode.prototype.getOwnerDocument=function DOMNode_getOwnerDocument(){
return this.ownerDocument;
};
DOMNode.prototype.getNamespaceURI=function DOMNode_getNamespaceURI(){
return this.namespaceURI;
};
DOMNode.prototype.getPrefix=function DOMNode_getPrefix(){
return this.prefix;
};
DOMNode.prototype.setPrefix=function DOMNode_setPrefix(_5e64){
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_5e64)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
if(!this.ownerDocument._isValidNamespace(this.namespaceURI,_5e64+":"+this.localName)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if((_5e64=="xmlns")&&(this.namespaceURI!="http://www.w3.org/2000/xmlns/")){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if((_5e64=="")&&(this.localName=="xmlns")){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
}
this.prefix=_5e64;
if(this.prefix!=""){
this.nodeName=this.prefix+":"+this.localName;
}else{
this.nodeName=this.localName;
}
};
DOMNode.prototype.getLocalName=function DOMNode_getLocalName(){
return this.localName;
};
DOMNode.prototype.insertBefore=function DOMNode_insertBefore(_5e65,_5e66){
var _5e67;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_5e65.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_5e65)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
if(_5e66){
var _5e68=this.childNodes._findItemIndex(_5e66._id);
if(this.ownerDocument.implementation.errorChecking&&(_5e68<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _5e69=_5e65.parentNode;
if(_5e69){
_5e69.removeChild(_5e65);
}
this.childNodes._insertBefore(_5e65,this.childNodes._findItemIndex(_5e66._id));
_5e67=_5e66.previousSibling;
if(_5e65.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_5e65.childNodes._nodes.length>0){
for(var ind=0;ind<_5e65.childNodes._nodes.length;ind++){
_5e65.childNodes._nodes[ind].parentNode=this;
}
_5e66.previousSibling=_5e65.childNodes._nodes[_5e65.childNodes._nodes.length-1];
}
}else{
_5e65.parentNode=this;
_5e66.previousSibling=_5e65;
}
}else{
_5e67=this.lastChild;
this.appendChild(_5e65);
}
if(_5e65.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_5e65.childNodes._nodes.length>0){
if(_5e67){
_5e67.nextSibling=_5e65.childNodes._nodes[0];
}else{
this.firstChild=_5e65.childNodes._nodes[0];
}
_5e65.childNodes._nodes[0].previousSibling=_5e67;
_5e65.childNodes._nodes[_5e65.childNodes._nodes.length-1].nextSibling=_5e66;
}
}else{
if(_5e67){
_5e67.nextSibling=_5e65;
}else{
this.firstChild=_5e65;
}
_5e65.previousSibling=_5e67;
_5e65.nextSibling=_5e66;
}
return _5e65;
};
DOMNode.prototype.replaceChild=function DOMNode_replaceChild(_5e6b,_5e6c){
var ret=null;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_5e6b.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_5e6b)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
var index=this.childNodes._findItemIndex(_5e6c._id);
if(this.ownerDocument.implementation.errorChecking&&(index<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _5e6f=_5e6b.parentNode;
if(_5e6f){
_5e6f.removeChild(_5e6b);
}
ret=this.childNodes._replaceChild(_5e6b,index);
if(_5e6b.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_5e6b.childNodes._nodes.length>0){
for(var ind=0;ind<_5e6b.childNodes._nodes.length;ind++){
_5e6b.childNodes._nodes[ind].parentNode=this;
}
if(_5e6c.previousSibling){
_5e6c.previousSibling.nextSibling=_5e6b.childNodes._nodes[0];
}else{
this.firstChild=_5e6b.childNodes._nodes[0];
}
if(_5e6c.nextSibling){
_5e6c.nextSibling.previousSibling=_5e6b;
}else{
this.lastChild=_5e6b.childNodes._nodes[_5e6b.childNodes._nodes.length-1];
}
_5e6b.childNodes._nodes[0].previousSibling=_5e6c.previousSibling;
_5e6b.childNodes._nodes[_5e6b.childNodes._nodes.length-1].nextSibling=_5e6c.nextSibling;
}
}else{
_5e6b.parentNode=this;
if(_5e6c.previousSibling){
_5e6c.previousSibling.nextSibling=_5e6b;
}else{
this.firstChild=_5e6b;
}
if(_5e6c.nextSibling){
_5e6c.nextSibling.previousSibling=_5e6b;
}else{
this.lastChild=_5e6b;
}
_5e6b.previousSibling=_5e6c.previousSibling;
_5e6b.nextSibling=_5e6c.nextSibling;
}
return ret;
};
DOMNode.prototype.removeChild=function DOMNode_removeChild(_5e71){
if(this.ownerDocument.implementation.errorChecking&&(this._readonly||_5e71._readonly)){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _5e72=this.childNodes._findItemIndex(_5e71._id);
if(this.ownerDocument.implementation.errorChecking&&(_5e72<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
this.childNodes._removeChild(_5e72);
_5e71.parentNode=null;
if(_5e71.previousSibling){
_5e71.previousSibling.nextSibling=_5e71.nextSibling;
}else{
this.firstChild=_5e71.nextSibling;
}
if(_5e71.nextSibling){
_5e71.nextSibling.previousSibling=_5e71.previousSibling;
}else{
this.lastChild=_5e71.previousSibling;
}
_5e71.previousSibling=null;
_5e71.nextSibling=null;
return _5e71;
};
DOMNode.prototype.appendChild=function DOMNode_appendChild(_5e73){
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_5e73.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_5e73)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
var _5e74=_5e73.parentNode;
if(_5e74){
_5e74.removeChild(_5e73);
}
this.childNodes._appendChild(_5e73);
if(_5e73.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_5e73.childNodes._nodes.length>0){
for(var ind=0;ind<_5e73.childNodes._nodes.length;ind++){
_5e73.childNodes._nodes[ind].parentNode=this;
}
if(this.lastChild){
this.lastChild.nextSibling=_5e73.childNodes._nodes[0];
_5e73.childNodes._nodes[0].previousSibling=this.lastChild;
this.lastChild=_5e73.childNodes._nodes[_5e73.childNodes._nodes.length-1];
}else{
this.lastChild=_5e73.childNodes._nodes[_5e73.childNodes._nodes.length-1];
this.firstChild=_5e73.childNodes._nodes[0];
}
}
}else{
_5e73.parentNode=this;
if(this.lastChild){
this.lastChild.nextSibling=_5e73;
_5e73.previousSibling=this.lastChild;
this.lastChild=_5e73;
}else{
this.lastChild=_5e73;
this.firstChild=_5e73;
}
}
return _5e73;
};
DOMNode.prototype.hasChildNodes=function DOMNode_hasChildNodes(){
return (this.childNodes.length>0);
};
DOMNode.prototype.cloneNode=function DOMNode_cloneNode(deep){
try{
return this.ownerDocument.importNode(this,deep);
}
catch(e){
return null;
}
};
DOMNode.prototype.normalize=function DOMNode_normalize(){
var inode;
var _5e78=new DOMNodeList();
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
var _5e79=null;
for(var i=0;i<this.childNodes.length;i++){
inode=this.childNodes.item(i);
if(inode.nodeType==DOMNode.TEXT_NODE){
if(inode.length<1){
_5e78._appendChild(inode);
}else{
if(_5e79){
_5e79.appendData(inode.data);
_5e78._appendChild(inode);
}else{
_5e79=inode;
}
}
}else{
_5e79=null;
inode.normalize();
}
}
for(var i=0;i<_5e78.length;i++){
inode=_5e78.item(i);
inode.parentNode.removeChild(inode);
}
}
};
DOMNode.prototype.isSupported=function DOMNode_isSupported(_5e7b,_5e7c){
return this.ownerDocument.implementation.hasFeature(_5e7b,_5e7c);
};
DOMNode.prototype.getElementsByTagName=function DOMNode_getElementsByTagName(_5e7d){
return this._getElementsByTagNameRecursive(_5e7d,new DOMNodeList(this.ownerDocument));
};
DOMNode.prototype._getElementsByTagNameRecursive=function DOMNode__getElementsByTagNameRecursive(_5e7e,_5e7f){
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
if((this.nodeName==_5e7e)||(_5e7e=="*")){
_5e7f._appendChild(this);
}
for(var i=0;i<this.childNodes.length;i++){
_5e7f=this.childNodes.item(i)._getElementsByTagNameRecursive(_5e7e,_5e7f);
}
}
return _5e7f;
};
DOMNode.prototype.getXML=function DOMNode_getXML(){
return this.toString();
};
DOMNode.prototype.getElementsByTagNameNS=function DOMNode_getElementsByTagNameNS(_5e81,_5e82){
return this._getElementsByTagNameNSRecursive(_5e81,_5e82,new DOMNodeList(this.ownerDocument));
};
DOMNode.prototype._getElementsByTagNameNSRecursive=function DOMNode__getElementsByTagNameNSRecursive(_5e83,_5e84,_5e85){
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
if(((this.namespaceURI==_5e83)||(_5e83=="*"))&&((this.localName==_5e84)||(_5e84=="*"))){
_5e85._appendChild(this);
}
for(var i=0;i<this.childNodes.length;i++){
_5e85=this.childNodes.item(i)._getElementsByTagNameNSRecursive(_5e83,_5e84,_5e85);
}
}
return _5e85;
};
DOMNode.prototype._isAncestor=function DOMNode__isAncestor(node){
return ((this==node)||((this.parentNode)&&(this.parentNode._isAncestor(node))));
};
DOMNode.prototype.importNode=function DOMNode_importNode(_5e88,deep){
var _5e8a;
this.getOwnerDocument()._performingImportNodeOperation=true;
try{
if(_5e88.nodeType==DOMNode.ELEMENT_NODE){
if(!this.ownerDocument.implementation.namespaceAware){
_5e8a=this.ownerDocument.createElement(_5e88.tagName);
for(var i=0;i<_5e88.attributes.length;i++){
_5e8a.setAttribute(_5e88.attributes.item(i).name,_5e88.attributes.item(i).value);
}
}else{
_5e8a=this.ownerDocument.createElementNS(_5e88.namespaceURI,_5e88.nodeName);
for(var i=0;i<_5e88.attributes.length;i++){
_5e8a.setAttributeNS(_5e88.attributes.item(i).namespaceURI,_5e88.attributes.item(i).name,_5e88.attributes.item(i).value);
}
for(var i=0;i<_5e88._namespaces.length;i++){
_5e8a._namespaces._nodes[i]=this.ownerDocument.createNamespace(_5e88._namespaces.item(i).localName);
_5e8a._namespaces._nodes[i].setValue(_5e88._namespaces.item(i).value);
}
}
}else{
if(_5e88.nodeType==DOMNode.ATTRIBUTE_NODE){
if(!this.ownerDocument.implementation.namespaceAware){
_5e8a=this.ownerDocument.createAttribute(_5e88.name);
}else{
_5e8a=this.ownerDocument.createAttributeNS(_5e88.namespaceURI,_5e88.nodeName);
for(var i=0;i<_5e88._namespaces.length;i++){
_5e8a._namespaces._nodes[i]=this.ownerDocument.createNamespace(_5e88._namespaces.item(i).localName);
_5e8a._namespaces._nodes[i].setValue(_5e88._namespaces.item(i).value);
}
}
_5e8a.setValue(_5e88.value);
}else{
if(_5e88.nodeType==DOMNode.DOCUMENT_FRAGMENT){
_5e8a=this.ownerDocument.createDocumentFragment();
}else{
if(_5e88.nodeType==DOMNode.NAMESPACE_NODE){
_5e8a=this.ownerDocument.createNamespace(_5e88.nodeName);
_5e8a.setValue(_5e88.value);
}else{
if(_5e88.nodeType==DOMNode.TEXT_NODE){
_5e8a=this.ownerDocument.createTextNode(_5e88.data);
}else{
if(_5e88.nodeType==DOMNode.CDATA_SECTION_NODE){
_5e8a=this.ownerDocument.createCDATASection(_5e88.data);
}else{
if(_5e88.nodeType==DOMNode.PROCESSING_INSTRUCTION_NODE){
_5e8a=this.ownerDocument.createProcessingInstruction(_5e88.target,_5e88.data);
}else{
if(_5e88.nodeType==DOMNode.COMMENT_NODE){
_5e8a=this.ownerDocument.createComment(_5e88.data);
}else{
throw (new DOMException(DOMException.NOT_SUPPORTED_ERR));
}
}
}
}
}
}
}
}
if(deep){
for(var i=0;i<_5e88.childNodes.length;i++){
_5e8a.appendChild(this.ownerDocument.importNode(_5e88.childNodes.item(i),true));
}
}
this.getOwnerDocument()._performingImportNodeOperation=false;
return _5e8a;
}
catch(eAny){
this.getOwnerDocument()._performingImportNodeOperation=false;
throw eAny;
}
};
DOMNode.prototype.__escapeString=function DOMNode__escapeString(str){
return __escapeString(str);
};
DOMNode.prototype.__unescapeString=function DOMNode__unescapeString(str){
return __unescapeString(str);
};
DOMDocument=function(_5e8e){
this._class=addClass(this._class,"DOMDocument");
this.DOMNode=DOMNode;
this.DOMNode(this);
this.doctype=null;
this.implementation=_5e8e;
this.documentElement=null;
this.all=[];
this.nodeName="#document";
this.nodeType=DOMNode.DOCUMENT_NODE;
this._id=0;
this._lastId=0;
this._parseComplete=false;
this.ownerDocument=this;
this._performingImportNodeOperation=false;
};
DOMDocument.prototype=new DOMNode;
DOMDocument.prototype.getDoctype=function DOMDocument_getDoctype(){
return this.doctype;
};
DOMDocument.prototype.getImplementation=function DOMDocument_implementation(){
return this.implementation;
};
DOMDocument.prototype.getDocumentElement=function DOMDocument_getDocumentElement(){
return this.documentElement;
};
DOMDocument.prototype.createElement=function DOMDocument_createElement(_5e8f){
if(this.ownerDocument.implementation.errorChecking&&(!this.ownerDocument.implementation._isValidName(_5e8f))){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
var node=new DOMElement(this);
node.tagName=_5e8f;
node.nodeName=_5e8f;
this.all[this.all.length]=node;
return node;
};
DOMDocument.prototype.createDocumentFragment=function DOMDocument_createDocumentFragment(){
var node=new DOMDocumentFragment(this);
return node;
};
DOMDocument.prototype.createTextNode=function DOMDocument_createTextNode(data){
var node=new DOMText(this);
node.data=data;
node.nodeValue=data;
node.length=data.length;
return node;
};
DOMDocument.prototype.createComment=function DOMDocument_createComment(data){
var node=new DOMComment(this);
node.data=data;
node.nodeValue=data;
node.length=data.length;
return node;
};
DOMDocument.prototype.createCDATASection=function DOMDocument_createCDATASection(data){
var node=new DOMCDATASection(this);
node.data=data;
node.nodeValue=data;
node.length=data.length;
return node;
};
DOMDocument.prototype.createProcessingInstruction=function DOMDocument_createProcessingInstruction(_5e98,data){
if(this.ownerDocument.implementation.errorChecking&&(!this.implementation._isValidName(_5e98))){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
var node=new DOMProcessingInstruction(this);
node.target=_5e98;
node.nodeName=_5e98;
node.data=data;
node.nodeValue=data;
node.length=data.length;
return node;
};
DOMDocument.prototype.createAttribute=function DOMDocument_createAttribute(name){
if(this.ownerDocument.implementation.errorChecking&&(!this.ownerDocument.implementation._isValidName(name))){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
var node=new DOMAttr(this);
node.name=name;
node.nodeName=name;
return node;
};
DOMDocument.prototype.createElementNS=function DOMDocument_createElementNS(_5e9d,_5e9e){
if(this.ownerDocument.implementation.errorChecking){
if(!this.ownerDocument._isValidNamespace(_5e9d,_5e9e)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_5e9e)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
var node=new DOMElement(this);
var qname=this.implementation._parseQName(_5e9e);
node.nodeName=_5e9e;
node.namespaceURI=_5e9d;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.tagName=_5e9e;
this.all[this.all.length]=node;
return node;
};
DOMDocument.prototype.createAttributeNS=function DOMDocument_createAttributeNS(_5ea1,_5ea2){
if(this.ownerDocument.implementation.errorChecking){
if(!this.ownerDocument._isValidNamespace(_5ea1,_5ea2,true)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_5ea2)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
var node=new DOMAttr(this);
var qname=this.implementation._parseQName(_5ea2);
node.nodeName=_5ea2;
node.namespaceURI=_5ea1;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.name=_5ea2;
node.nodeValue="";
return node;
};
DOMDocument.prototype.createNamespace=function DOMDocument_createNamespace(_5ea5){
var node=new DOMNamespace(this);
var qname=this.implementation._parseQName(_5ea5);
node.nodeName=_5ea5;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.name=_5ea5;
node.nodeValue="";
return node;
};
DOMDocument.prototype.getElementById=function DOMDocument_getElementById(_5ea8){
retNode=null;
for(var i=0;i<this.all.length;i++){
var node=this.all[i];
if((node.id==_5ea8)&&(node._isAncestor(node.ownerDocument.documentElement))){
retNode=node;
break;
}
}
return retNode;
};
DOMDocument.prototype._genId=function DOMDocument__genId(){
this._lastId+=1;
return this._lastId;
};
DOMDocument.prototype._isValidNamespace=function DOMDocument__isValidNamespace(_5eab,_5eac,_5ead){
if(this._performingImportNodeOperation==true){
return true;
}
var valid=true;
var qName=this.implementation._parseQName(_5eac);
if(this._parseComplete==true){
if(qName.localName.indexOf(":")>-1){
valid=false;
}
if((valid)&&(!_5ead)){
if(!_5eab){
valid=false;
}
}
if((valid)&&(qName.prefix=="")){
valid=false;
}
}
if((valid)&&(qName.prefix=="xml")&&(_5eab!="http://www.w3.org/XML/1998/namespace")){
valid=false;
}
return valid;
};
DOMDocument.prototype.toString=function DOMDocument_toString(){
return ""+this.childNodes;
};
DOMElement=function(_5eb0){
this._class=addClass(this._class,"DOMElement");
this.DOMNode=DOMNode;
this.DOMNode(_5eb0);
this.tagName="";
this.id="";
this.nodeType=DOMNode.ELEMENT_NODE;
};
DOMElement.prototype=new DOMNode;
DOMElement.prototype.getTagName=function DOMElement_getTagName(){
return this.tagName;
};
DOMElement.prototype.getAttribute=function DOMElement_getAttribute(name){
var ret="";
var attr=this.attributes.getNamedItem(name);
if(attr){
ret=attr.value;
}
return ret;
};
DOMElement.prototype.setAttribute=function DOMElement_setAttribute(name,value){
var attr=this.attributes.getNamedItem(name);
if(!attr){
attr=this.ownerDocument.createAttribute(name);
}
var value=new String(value);
if(this.ownerDocument.implementation.errorChecking){
if(attr._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(!this.ownerDocument.implementation._isValidString(value)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
if(this.ownerDocument.implementation._isIdDeclaration(name)){
this.id=value;
}
attr.value=value;
attr.nodeValue=value;
if(value.length>0){
attr.specified=true;
}else{
attr.specified=false;
}
this.attributes.setNamedItem(attr);
};
DOMElement.prototype.removeAttribute=function DOMElement_removeAttribute(name){
return this.attributes.removeNamedItem(name);
};
DOMElement.prototype.getAttributeNode=function DOMElement_getAttributeNode(name){
return this.attributes.getNamedItem(name);
};
DOMElement.prototype.setAttributeNode=function DOMElement_setAttributeNode(_5eb9){
if(this.ownerDocument.implementation._isIdDeclaration(_5eb9.name)){
this.id=_5eb9.value;
}
return this.attributes.setNamedItem(_5eb9);
};
DOMElement.prototype.removeAttributeNode=function DOMElement_removeAttributeNode(_5eba){
if(this.ownerDocument.implementation.errorChecking&&_5eba._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _5ebb=this.attributes._findItemIndex(_5eba._id);
if(this.ownerDocument.implementation.errorChecking&&(_5ebb<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
return this.attributes._removeChild(_5ebb);
};
DOMElement.prototype.getAttributeNS=function DOMElement_getAttributeNS(_5ebc,_5ebd){
var ret="";
var attr=this.attributes.getNamedItemNS(_5ebc,_5ebd);
if(attr){
ret=attr.value;
}
return ret;
};
DOMElement.prototype.setAttributeNS=function DOMElement_setAttributeNS(_5ec0,_5ec1,value){
var attr=this.attributes.getNamedItem(_5ec0,_5ec1);
if(!attr){
attr=this.ownerDocument.createAttributeNS(_5ec0,_5ec1);
}
var value=new String(value);
if(this.ownerDocument.implementation.errorChecking){
if(attr._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(!this.ownerDocument._isValidNamespace(_5ec0,_5ec1)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if(!this.ownerDocument.implementation._isValidString(value)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
if(this.ownerDocument.implementation._isIdDeclaration(name)){
this.id=value;
}
attr.value=value;
attr.nodeValue=value;
if(value.length>0){
attr.specified=true;
}else{
attr.specified=false;
}
this.attributes.setNamedItemNS(attr);
};
DOMElement.prototype.removeAttributeNS=function DOMElement_removeAttributeNS(_5ec4,_5ec5){
return this.attributes.removeNamedItemNS(_5ec4,_5ec5);
};
DOMElement.prototype.getAttributeNodeNS=function DOMElement_getAttributeNodeNS(_5ec6,_5ec7){
return this.attributes.getNamedItemNS(_5ec6,_5ec7);
};
DOMElement.prototype.setAttributeNodeNS=function DOMElement_setAttributeNodeNS(_5ec8){
if((_5ec8.prefix=="")&&this.ownerDocument.implementation._isIdDeclaration(_5ec8.name)){
this.id=_5ec8.value;
}
return this.attributes.setNamedItemNS(_5ec8);
};
DOMElement.prototype.hasAttribute=function DOMElement_hasAttribute(name){
return this.attributes._hasAttribute(name);
};
DOMElement.prototype.hasAttributeNS=function DOMElement_hasAttributeNS(_5eca,_5ecb){
return this.attributes._hasAttributeNS(_5eca,_5ecb);
};
DOMElement.prototype.toString=function DOMElement_toString(){
var ret="";
var ns=this._namespaces.toString();
if(ns.length>0){
ns=" "+ns;
}
var attrs=this.attributes.toString();
if(attrs.length>0){
attrs=" "+attrs;
}
ret+="<"+this.nodeName+ns+attrs+">";
ret+=this.childNodes.toString();
ret+="</"+this.nodeName+">";
return ret;
};
DOMAttr=function(_5ecf){
this._class=addClass(this._class,"DOMAttr");
this.DOMNode=DOMNode;
this.DOMNode(_5ecf);
this.name="";
this.specified=false;
this.value="";
this.nodeType=DOMNode.ATTRIBUTE_NODE;
this.ownerElement=null;
this.childNodes=null;
this.attributes=null;
};
DOMAttr.prototype=new DOMNode;
DOMAttr.prototype.getName=function DOMAttr_getName(){
return this.nodeName;
};
DOMAttr.prototype.getSpecified=function DOMAttr_getSpecified(){
return this.specified;
};
DOMAttr.prototype.getValue=function DOMAttr_getValue(){
return this.nodeValue;
};
DOMAttr.prototype.setValue=function DOMAttr_setValue(value){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.setNodeValue(value);
};
DOMAttr.prototype.setNodeValue=function DOMAttr_setNodeValue(value){
this.nodeValue=new String(value);
this.value=this.nodeValue;
this.specified=(this.value.length>0);
};
DOMAttr.prototype.toString=function DOMAttr_toString(){
var ret="";
ret+=this.nodeName+"=\""+this.__escapeString(this.nodeValue)+"\"";
return ret;
};
DOMAttr.prototype.getOwnerElement=function(){
return this.ownerElement;
};
DOMNamespace=function(_5ed3){
this._class=addClass(this._class,"DOMNamespace");
this.DOMNode=DOMNode;
this.DOMNode(_5ed3);
this.name="";
this.specified=false;
this.value="";
this.nodeType=DOMNode.NAMESPACE_NODE;
};
DOMNamespace.prototype=new DOMNode;
DOMNamespace.prototype.getValue=function DOMNamespace_getValue(){
return this.nodeValue;
};
DOMNamespace.prototype.setValue=function DOMNamespace_setValue(value){
this.nodeValue=new String(value);
this.value=this.nodeValue;
};
DOMNamespace.prototype.toString=function DOMNamespace_toString(){
var ret="";
if(this.nodeName!=""){
ret+=this.nodeName+"=\""+this.__escapeString(this.nodeValue)+"\"";
}else{
ret+="xmlns=\""+this.__escapeString(this.nodeValue)+"\"";
}
return ret;
};
DOMCharacterData=function(_5ed6){
this._class=addClass(this._class,"DOMCharacterData");
this.DOMNode=DOMNode;
this.DOMNode(_5ed6);
this.data="";
this.length=0;
};
DOMCharacterData.prototype=new DOMNode;
DOMCharacterData.prototype.getData=function DOMCharacterData_getData(){
return this.nodeValue;
};
DOMCharacterData.prototype.setData=function DOMCharacterData_setData(data){
this.setNodeValue(data);
};
DOMCharacterData.prototype.setNodeValue=function DOMCharacterData_setNodeValue(data){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.nodeValue=new String(data);
this.data=this.nodeValue;
this.length=this.nodeValue.length;
};
DOMCharacterData.prototype.getLength=function DOMCharacterData_getLength(){
return this.nodeValue.length;
};
DOMCharacterData.prototype.substringData=function DOMCharacterData_substringData(_5ed9,count){
var ret=null;
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_5ed9<0)||(_5ed9>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
if(!count){
ret=this.data.substring(_5ed9);
}else{
ret=this.data.substring(_5ed9,_5ed9+count);
}
}
return ret;
};
DOMCharacterData.prototype.appendData=function DOMCharacterData_appendData(arg){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.setData(""+this.data+arg);
};
DOMCharacterData.prototype.insertData=function DOMCharacterData_insertData(_5edd,arg){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_5edd<0)||(_5edd>this.data.length))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(this.data.substring(0,_5edd).concat(arg,this.data.substring(_5edd)));
}else{
if(this.ownerDocument.implementation.errorChecking&&(_5edd!=0)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(arg);
}
};
DOMCharacterData.prototype.deleteData=function DOMCharacterData_deleteData(_5edf,count){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_5edf<0)||(_5edf>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
if(!count||(_5edf+count)>this.data.length){
this.setData(this.data.substring(0,_5edf));
}else{
this.setData(this.data.substring(0,_5edf).concat(this.data.substring(_5edf+count)));
}
}
};
DOMCharacterData.prototype.replaceData=function DOMCharacterData_replaceData(_5ee1,count,arg){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_5ee1<0)||(_5ee1>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(this.data.substring(0,_5ee1).concat(arg,this.data.substring(_5ee1+count)));
}else{
this.setData(arg);
}
};
DOMText=function(_5ee4){
this._class=addClass(this._class,"DOMText");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_5ee4);
this.nodeName="#text";
this.nodeType=DOMNode.TEXT_NODE;
};
DOMText.prototype=new DOMCharacterData;
DOMText.prototype.splitText=function DOMText_splitText(_5ee5){
var data,inode;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if((_5ee5<0)||(_5ee5>this.data.length)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
}
if(this.parentNode){
data=this.substringData(_5ee5);
inode=this.ownerDocument.createTextNode(data);
if(this.nextSibling){
this.parentNode.insertBefore(inode,this.nextSibling);
}else{
this.parentNode.appendChild(inode);
}
this.deleteData(_5ee5);
}
return inode;
};
DOMText.prototype.toString=function DOMText_toString(){
return this.__escapeString(""+this.nodeValue);
};
DOMCDATASection=function(_5ee7){
this._class=addClass(this._class,"DOMCDATASection");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_5ee7);
this.nodeName="#cdata-section";
this.nodeType=DOMNode.CDATA_SECTION_NODE;
};
DOMCDATASection.prototype=new DOMCharacterData;
DOMCDATASection.prototype.splitText=function DOMCDATASection_splitText(_5ee8){
var data,inode;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if((_5ee8<0)||(_5ee8>this.data.length)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
}
if(this.parentNode){
data=this.substringData(_5ee8);
inode=this.ownerDocument.createCDATASection(data);
if(this.nextSibling){
this.parentNode.insertBefore(inode,this.nextSibling);
}else{
this.parentNode.appendChild(inode);
}
this.deleteData(_5ee8);
}
return inode;
};
DOMCDATASection.prototype.toString=function DOMCDATASection_toString(){
var ret="";
ret+="<![CDATA["+this.nodeValue+"]]>";
return ret;
};
DOMComment=function(_5eeb){
this._class=addClass(this._class,"DOMComment");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_5eeb);
this.nodeName="#comment";
this.nodeType=DOMNode.COMMENT_NODE;
};
DOMComment.prototype=new DOMCharacterData;
DOMComment.prototype.toString=function DOMComment_toString(){
var ret="";
ret+="<!--"+this.nodeValue+"-->";
return ret;
};
DOMProcessingInstruction=function(_5eed){
this._class=addClass(this._class,"DOMProcessingInstruction");
this.DOMNode=DOMNode;
this.DOMNode(_5eed);
this.target="";
this.data="";
this.nodeType=DOMNode.PROCESSING_INSTRUCTION_NODE;
};
DOMProcessingInstruction.prototype=new DOMNode;
DOMProcessingInstruction.prototype.getTarget=function DOMProcessingInstruction_getTarget(){
return this.nodeName;
};
DOMProcessingInstruction.prototype.getData=function DOMProcessingInstruction_getData(){
return this.nodeValue;
};
DOMProcessingInstruction.prototype.setData=function DOMProcessingInstruction_setData(data){
this.setNodeValue(data);
};
DOMProcessingInstruction.prototype.setNodeValue=function DOMProcessingInstruction_setNodeValue(data){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.nodeValue=new String(data);
this.data=this.nodeValue;
};
DOMProcessingInstruction.prototype.toString=function DOMProcessingInstruction_toString(){
var ret="";
ret+="<?"+this.nodeName+" "+this.nodeValue+" ?>";
return ret;
};
DOMDocumentFragment=function(_5ef1){
this._class=addClass(this._class,"DOMDocumentFragment");
this.DOMNode=DOMNode;
this.DOMNode(_5ef1);
this.nodeName="#document-fragment";
this.nodeType=DOMNode.DOCUMENT_FRAGMENT_NODE;
};
DOMDocumentFragment.prototype=new DOMNode;
DOMDocumentFragment.prototype.toString=function DOMDocumentFragment_toString(){
var xml="";
var _5ef3=this.getChildNodes().getLength();
for(intLoop=0;intLoop<_5ef3;intLoop++){
xml+=this.getChildNodes().item(intLoop).toString();
}
return xml;
};
DOMDocumentType=function(){
alert("DOMDocumentType.constructor(): Not Implemented");
};
DOMEntity=function(){
alert("DOMEntity.constructor(): Not Implemented");
};
DOMEntityReference=function(){
alert("DOMEntityReference.constructor(): Not Implemented");
};
DOMNotation=function(){
alert("DOMNotation.constructor(): Not Implemented");
};
Strings=new Object();
Strings.WHITESPACE=" \t\n\r";
Strings.QUOTES="\"'";
Strings.isEmpty=function Strings_isEmpty(strD){
return (strD===null)||(strD.length===0);
};
Strings.indexOfNonWhitespace=function Strings_indexOfNonWhitespace(strD,iB,iE){
if(Strings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iB;i<iE;i++){
if(Strings.WHITESPACE.indexOf(strD.charAt(i))==-1){
return i;
}
}
return -1;
};
Strings.lastIndexOfNonWhitespace=function Strings_lastIndexOfNonWhitespace(strD,iB,iE){
if(Strings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iE-1;i>=iB;i--){
if(Strings.WHITESPACE.indexOf(strD.charAt(i))==-1){
return i;
}
}
return -1;
};
Strings.indexOfWhitespace=function Strings_indexOfWhitespace(strD,iB,iE){
if(Strings.isEmpty(strD)){
return -1;
}
iB=iB||0;
iE=iE||strD.length;
for(var i=iB;i<iE;i++){
if(Strings.WHITESPACE.indexOf(strD.charAt(i))!=-1){
return i;
}
}
return -1;
};
Strings.replace=function Strings_replace(strD,iB,iE,strF,strR){
if(Strings.isEmpty(strD)){
return "";
}
iB=iB||0;
iE=iE||strD.length;
return strD.substring(iB,iE).split(strF).join(strR);
};
Strings.getLineNumber=function Strings_getLineNumber(strD,iP){
if(Strings.isEmpty(strD)){
return -1;
}
iP=iP||strD.length;
return strD.substring(0,iP).split("\n").length;
};
Strings.getColumnNumber=function Strings_getColumnNumber(strD,iP){
if(Strings.isEmpty(strD)){
return -1;
}
iP=iP||strD.length;
var arrD=strD.substring(0,iP).split("\n");
var _5f0b=arrD[arrD.length-1];
arrD.length--;
var _5f0c=arrD.join("\n").length;
return iP-_5f0c;
};
StringBuffer=function(){
this._a=[];
};
StringBuffer.prototype.append=function StringBuffer_append(d){
this._a[this._a.length]=d;
};
StringBuffer.prototype.toString=function StringBuffer_toString(){
return this._a.join("");
};
draw2d.XMLSerializer=function(){
alert("do not init this class. Use the static methods instead");
};
draw2d.XMLSerializer.toXML=function(obj,_5dd3,_5dd4){
if(_5dd3==undefined){
_5dd3="model";
}
_5dd4=_5dd4?_5dd4:"";
var t=draw2d.XMLSerializer.getTypeName(obj);
var s=_5dd4+"<"+_5dd3+" type=\""+t+"\">";
switch(t){
case "int":
case "number":
case "boolean":
s+=obj;
break;
case "string":
s+=draw2d.XMLSerializer.xmlEncode(obj);
break;
case "date":
s+=obj.toLocaleString();
break;
case "Array":
case "array":
s+="\n";
var _5dd7=_5dd4+"   ";
for(var i=0;i<obj.length;i++){
s+=draw2d.XMLSerializer.toXML(obj[i],("element"),_5dd7);
}
s+=_5dd4;
break;
default:
if(obj!==null){
s+="\n";
if(obj instanceof draw2d.ArrayList){
obj.trimToSize();
}
var _5dd9=obj.getPersistentAttributes();
var _5dd7=_5dd4+"   ";
for(var name in _5dd9){
s+=draw2d.XMLSerializer.toXML(_5dd9[name],name,_5dd7);
}
s+=_5dd4;
}
break;
}
s+="</"+_5dd3+">\n";
return s;
};
draw2d.XMLSerializer.isSimpleVar=function(t){
switch(t){
case "int":
case "string":
case "String":
case "Number":
case "number":
case "Boolean":
case "boolean":
case "bool":
case "dateTime":
case "Date":
case "date":
case "float":
return true;
}
return false;
};
draw2d.XMLSerializer.getTypeName=function(obj){
if(obj===null){
return "undefined";
}
if(obj instanceof Array){
return "Array";
}
if(obj instanceof Date){
return "Date";
}
var t=typeof (obj);
if(t=="number"){
return (parseInt(obj).toString()==obj)?"int":"number";
}
if(draw2d.XMLSerializer.isSimpleVar(t)){
return t;
}
return obj.type.replace("@NAMESPACE"+"@","");
};
draw2d.XMLSerializer.xmlEncode=function(_5dde){
var _5ddf=_5dde;
var amp=/&/gi;
var gt=/>/gi;
var lt=/</gi;
var quot=/"/gi;
var apos=/'/gi;
var _5de5="&#62;";
var _5de6="&#38;#60;";
var _5de7="&#38;#38;";
var _5de8="&#34;";
var _5de9="&#39;";
_5ddf=_5ddf.replace(amp,_5de7);
_5ddf=_5ddf.replace(quot,_5de8);
_5ddf=_5ddf.replace(lt,_5de6);
_5ddf=_5ddf.replace(gt,_5de5);
_5ddf=_5ddf.replace(apos,_5de9);
return _5ddf;
};
draw2d.XMLDeserializer=function(){
alert("do not init this class. Use the static methods instead");
};
draw2d.XMLDeserializer.fromXML=function(node,_4b11){
var _4b12=""+node.getAttributes().getNamedItem("type").getNodeValue();
var value=node.getNodeValue();
switch(_4b12){
case "int":
try{
return parseInt(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
case "string":
case "String":
try{
if(node.getChildNodes().getLength()>0){
return ""+node.getChildNodes().item(0).getNodeValue();
}
return "";
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
case "Number":
case "number":
try{
return parseFloat(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
case "Boolean":
case "boolean":
case "bool":
try{
return "true"==(""+node.getChildNodes().item(0).getNodeValue()).toLowerCase();
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
case "dateTime":
case "Date":
case "date":
try{
return new Date(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
case "float":
try{
return parseFloat(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_4b12+"\nXML Node:"+node);
}
break;
}
_4b12=_4b12.replace("@NAMESPACE"+"@","");
var obj=eval("new "+_4b12+"()");
if(_4b11!=undefined&&obj.setModelParent!=undefined){
obj.setModelParent(_4b11);
}
var _4b15=node.getChildNodes();
for(var i=0;i<_4b15.length;i++){
var child=_4b15.item(i);
var _4b18=child.getNodeName();
if(obj instanceof Array){
_4b18=i;
}
obj[_4b18]=draw2d.XMLDeserializer.fromXML(child,obj instanceof draw2d.AbstractObjectModel?obj:_4b11);
}
return obj;
};
draw2d.EditPolicy=function(_58e6){
this.policy=_58e6;
};
draw2d.EditPolicy.DELETE="DELETE";
draw2d.EditPolicy.MOVE="MOVE";
draw2d.EditPolicy.CONNECT="CONNECT";
draw2d.EditPolicy.RESIZE="RESIZE";
draw2d.EditPolicy.prototype.type="draw2d.EditPolicy";
draw2d.EditPolicy.prototype.getPolicy=function(){
return this.policy;
};
draw2d.AbstractPalettePart=function(){
this.x=0;
this.y=0;
this.html=null;
};
draw2d.AbstractPalettePart.prototype.type="draw2d.AbstractPalettePart";
draw2d.AbstractPalettePart.prototype=new draw2d.Draggable();
draw2d.AbstractPalettePart.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.height="24px";
item.style.width="24px";
return item;
};
draw2d.AbstractPalettePart.prototype.setEnviroment=function(_4ff7,_4ff8){
this.palette=_4ff8;
this.workflow=_4ff7;
};
draw2d.AbstractPalettePart.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
draw2d.Draggable.call(this,this.html);
}
return this.html;
};
draw2d.AbstractPalettePart.prototype.onDrop=function(_4ff9,_4ffa){
var _4ffb=this.workflow.getScrollLeft();
var _4ffc=this.workflow.getScrollTop();
var _4ffd=this.workflow.getAbsoluteX();
var _4ffe=this.workflow.getAbsoluteY();
this.setPosition(this.x,this.y);
this.execute(_4ff9+_4ffb-_4ffd,_4ffa+_4ffc-_4ffe);
};
draw2d.AbstractPalettePart.prototype.execute=function(x,y){
alert("inerited class should override the method 'draw2d.AbstractPalettePart.prototype.execute'");
};
draw2d.AbstractPalettePart.prototype.setTooltip=function(_5001){
this.tooltip=_5001;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
draw2d.AbstractPalettePart.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
draw2d.AbstractPalettePart.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
this.html.style.cursor="move";
};
draw2d.AbstractPalettePart.prototype.getWidth=function(){
return this.width;
};
draw2d.AbstractPalettePart.prototype.getHeight=function(){
return this.height;
};
draw2d.AbstractPalettePart.prototype.getY=function(){
return this.y;
};
draw2d.AbstractPalettePart.prototype.getX=function(){
return this.x;
};
draw2d.AbstractPalettePart.prototype.getPosition=function(){
return new draw2d.Point(this.x,this.y);
};
draw2d.AbstractPalettePart.prototype.disableTextSelection=function(e){
if(typeof e.onselectstart!="undefined"){
e.onselectstart=function(){
return false;
};
}else{
if(typeof e.style.MozUserSelect!="undefined"){
e.style.MozUserSelect="none";
}
}
};
draw2d.ExternalPalette=function(_5007,divId){
this.html=document.getElementById(divId);
this.workflow=_5007;
this.parts=new draw2d.ArrayList();
};
draw2d.ExternalPalette.prototype.type="draw2d.ExternalPalette";
draw2d.ExternalPalette.prototype.getHTMLElement=function(){
return this.html;
};
draw2d.ExternalPalette.prototype.addPalettePart=function(part){
if(!(part instanceof draw2d.AbstractPalettePart)){
throw "parameter is not instanceof [draw2d.AbstractPalettePart]";
}
this.parts.add(part);
this.html.appendChild(part.getHTMLElement());
part.setEnviroment(this.workflow,this);
};
draw2d.Configuration=function()
{
}

// Modify this file or override the settings in your JS file
// before other draw2d files are loaded
draw2d.Configuration.APP_PATH="";
draw2d.Configuration.IMAGEPATH="";
draw2d.Configuration.APP_PATH="./application/@APPLICATION@/@VERSION@/{modulename}/";
draw2d.Configuration.IMAGEPATH=draw2d.Configuration.APP_PATH+"images/";
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

/**
 * @class The model for a DB table.

 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.AbstractRecordSourceModel=function(/*:String*/ name)
{
   draw2d.AbstractObjectModel.call(this);

   if(name)
     this.name = name;
   else
     this.name = "default";
   this.providerType = draw2d.AbstractRecordSourceModel.TYPE_PROVIDER;
   this.pos = new draw2d.Point(42,42);
   this.connections = new draw2d.ArrayList();
   this.columns = new draw2d.ArrayList();
}

draw2d.AbstractRecordSourceModel.EVENT_COLUMN_ADDED      = "column added";
draw2d.AbstractRecordSourceModel.EVENT_COLUMN_REMOVED    = "column removed";
draw2d.AbstractRecordSourceModel.EVENT_COLUMN_UP         = "column up";
draw2d.AbstractRecordSourceModel.EVENT_COLUMN_DOWN       = "column down";
draw2d.AbstractRecordSourceModel.EVENT_NAME_CHANGED      = "name changed";
draw2d.AbstractRecordSourceModel.EVENT_POSITION_CHANGED  = "position changed";
draw2d.AbstractRecordSourceModel.EVENT_TYPE_CHANGED      = "type changed";
draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED   = "default changed";
draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_CHANGED = "unique key changed";
draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_ADDED   = "unique key added";
draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_REMOVED = "unique key removed";

draw2d.AbstractRecordSourceModel.TYPE_SOURCE     = "source";
draw2d.AbstractRecordSourceModel.TYPE_PROVIDER   = "provider";
draw2d.AbstractRecordSourceModel.TYPE_TARGET     = "target";

draw2d.AbstractRecordSourceModel.prototype = new draw2d.AbstractObjectModel;
/** @private **/
draw2d.AbstractRecordSourceModel.prototype.type="draw2d.AbstractRecordSourceModel";


draw2d.AbstractRecordSourceModel.prototype.setName=function(/*:String*/ name )
{
  var save = this.name;
  this.name = name;

  this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_NAME_CHANGED,save, this.name);
}

/**
 * Return the name of the Excelsheet
 * 
 * @type String
 **/
draw2d.AbstractRecordSourceModel.prototype.getName=function()
{
   return this.name;
}


/**
 * Set the position attribute of the model. This will enforce a redraw of the corresponding
 * figure (if existing).
 *
 * @param {int} xPos The x coordinate for the model
 * @param {int} yPos The y coordinate for the model
 **/
draw2d.AbstractRecordSourceModel.prototype.setPosition=function(/*:int*/ xPos , /*:int*/ yPos )
{
  var save = this.pos;
  if(save.x==xPos && save.y==yPos)
      return;

  this.pos = new draw2d.Point(xPos,yPos);

  this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_POSITION_CHANGED,save, this.pos);
}



/**
 **/
draw2d.AbstractRecordSourceModel.prototype.setProviderType=function(/*:String*/ type )
{
  switch(type)
  {
    case draw2d.AbstractRecordSourceModel.TYPE_SOURCE:
    case draw2d.AbstractRecordSourceModel.TYPE_PROVIDER:
    case draw2d.AbstractRecordSourceModel.TYPE_TARGET:
       break;
    default:
       throw "Invalid parameter ["+type+"] in [AbstractRecordSourceModel.prototype.setProviderType]";
  }
  var save = this.providerType;
  if(save === type)
      return;

  this.providerType = type;
  this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_TYPE_CHANGED,save, this.providerType);
}

/**
 * Return the provider type of this node.
 *
 * @type String
 **/
draw2d.AbstractRecordSourceModel.prototype.getProviderType=function()
{
   return this.providerType;
}

/**
 * Return the x/y position of the table in the database graphical layout.
 *
 * @type draw2d.Point
 **/
draw2d.AbstractRecordSourceModel.prototype.getPosition=function()
{
   return this.pos;
}


/**
 * Return all fields or columns of the database table.
 *
 * @type draw2d.ArrayList
 **/
draw2d.AbstractRecordSourceModel.prototype.getColumnModels=function()
{
  return this.columns;
}

/**
 * Add a field or column to the database table.
 *
 * @param {draw2d.ColumnModel} column the column to add
 **/
draw2d.AbstractRecordSourceModel.prototype.addColumnModel=function(/*:draw2d.ColumnModel*/ column)
{
  if(!(column instanceof draw2d.ColumnModel))
    throw "Invalid parameter type in [AbstractRecordSourceModel.prototype.addColumnModel]";

  if(this.columns.indexOf(column)===-1)
  {
    this.columns.add(column);
    column.setModelParent(this);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_COLUMN_ADDED,null, column);
  }
}


/**
 * move a column one position up
 *
 * @param {int} the index to move once up
 **/
draw2d.AbstractRecordSourceModel.prototype.columnModelUp=function(/*:int*/ index)
{
  if(index <=1)
    return;

  var column = this.columns.removeElementAt(index);
  this.columns.insertElementAt(column,index-1);
  this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_COLUMN_UP,index, index-1);
}


/**
 * move a column one position up
 *
 * @param {int} the index to move once up
 **/
draw2d.AbstractRecordSourceModel.prototype.columnModelDown=function(/*:int*/ index)
{
  if(index >= (this.columns.getSize()-1))
    throw "Invalid parameter. [index] must be < "+(this.columns.getSize()-1);

  var column = this.columns.removeElementAt(index);
  this.columns.insertElementAt(column, index+1);
  this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_COLUMN_DOWN,index, index+1);
}



/**
 * return the column model with the hands over columnName.
 *
 * @return The column model with the given name or null
 * @param {String} columnName the name of the column
 * @type draw2d.ColumnModel
 **/
draw2d.AbstractRecordSourceModel.prototype.getColumnModel=function(/*:String*/ columnName)
{
  var count = this.columns.getSize();
  for(var i=0;i<count;i++)
  {
    var column = this.columns.get(i);
    if(column.getName()===columnName)
    {
       return column;
    }
  }
  return null;
}

/**
 * Add a field or column to the database table.
 *
 * @param {draw2d.ColumnModel} column the column to add
 **/
draw2d.AbstractRecordSourceModel.prototype.removeColumnModel=function(/*:draw2d.ColumnModel*/ column)
{
  if(this.columns.contains(column))
  {
    // Alle �berg�nge welche von dieser Column ausgehen l�schen
    // 
    var connections = this.getConverterModel().getConnectionModels().clone();
    var count = connections.getSize();
    for(var ii=0; ii<count;ii++)
    {
        var con = connections.get(ii);
        if(con.getSourcePortModel()==column)
          con.getModelParent().removeConnectionModel(con);
        else if(con.getTargetPortModel()==column)
          con.getModelParent().removeConnectionModel(con);
    }

    this.columns.remove(column);
    column.setModelParent(null);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_COLUMN_REMOVED,column,null);
  }
}


/**
 * Return all connections from the record source model.
 *
 * @type draw2d.ArrayList
 **/
draw2d.AbstractRecordSourceModel.prototype.getConnectionModels=function()
{
  return this.connections;
}


/**
 * Add a connection between two columns of two different tables
 *
 * @param {draw2d.ColumnConnectionModel}
 **/
draw2d.AbstractRecordSourceModel.prototype.addConnectionModel=function(/*:draw2d.ColumnConnectionModel*/ connection)
{
  if(!(connection instanceof draw2d.ColumnConnectionModel))
    throw "Invalid parameter type in [AbstractRecordSourceModel.prototype.addConnectionModel]";

  if(this.connections.indexOf(connection)==-1)
  {
    this.connections.add(connection);
    connection.setModelParent(this);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractObjectModel.EVENT_CONNECTION_ADDED,null,connection);
  }
}

/**
 * Remove a connection between two columns of two different tables
 *
 * @param {draw2d.ColumnConnectionModel}
 **/
draw2d.AbstractRecordSourceModel.prototype.removeConnectionModel=function(/*:draw2d.ColumnConnectionModel*/ connection)
{
  if(!(connection instanceof draw2d.ColumnConnectionModel))
    throw "Invalid parameter type in [AbstractRecordSourceModel.prototype.addConnectionModel]";

  if(this.connections.remove(connection)!=null)
  {
    connection.setModelParent(null);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractObjectModel.EVENT_CONNECTION_REMOVED,connection,null);
  }
}


/**
 * Returns the related database model.
 *
 * @type draw2d.ConverterModel
 **/
draw2d.AbstractRecordSourceModel.prototype.getConverterModel=function()
{
   return this.getModelParent().getConverterModel();
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.15
 **/
draw2d.AbstractRecordSourceModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.name= this.name;
   att.pos= this.pos;
   att.connections = this.connections;
   att.columns = this.columns;
   att.providerType = this.providerType;

   return att;
}

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
draw2d.ColumnTypeModel=function(/*:String*/ name)
{
  draw2d.AbstractObjectModel.call(this);
  this.name = name;
  this.parent = null;
}

draw2d.ColumnTypeModel.prototype = new draw2d.AbstractObjectModel;
/** @private */
draw2d.ColumnTypeModel.prototype.type="draw2d.ColumnTypeModel";

draw2d.ColumnTypeModel.prototype.getName=function()
{
   return this.name;
}

/**
 * @type draw2d.ConverterModel
 **/
draw2d.ColumnTypeModel.prototype.getConverterModel=function()
{
   return this.getModelParent().getConverterModel();
}

/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.name= this.name;

   return att;
}


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
draw2d.ColumnTypeModelBoolean=function( /*:boolean*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_BOOLEAN);

  if(defaultValue)
     this.defaultValue = defaultValue;
   else
     this.defaultValue = true;
}

draw2d.ColumnTypeModelBoolean.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelBoolean.prototype.type="draw2d.ColumnTypeModelBoolean";



draw2d.ColumnTypeModelBoolean.prototype.getDefault=function()
{
   return this.defaultValue;
}


draw2d.ColumnTypeModelBoolean.prototype.setDefault=function(/*:boolean*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelBoolean.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;

   return att;
}
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
draw2d.ColumnTypeModelInteger=function( /*:boolean*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_INTEGER);
  if(defaultValue!==undefined)
    this.defaultValue = defaultValue;
  else
    this.defaultValue = 0;
}

draw2d.ColumnTypeModelInteger.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelInteger.prototype.type="draw2d.ColumnTypeModelInteger";


/**
 *
 * @type int
 **/
draw2d.ColumnTypeModelInteger.prototype.getDefault=function()
{
   return this.defaultValue;
}

/**
 * Set the default value of the column
 
 **/
draw2d.ColumnTypeModelInteger.prototype.setDefault=function(/*:int*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelInteger.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;

   return att;
}
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

draw2d.ColumnTypeModelLongText=function( /*:String*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_LONGTEXT);

  if(defaultValue)
     this.defaultValue = defaultValue;
   else
     this.defaultValue = "";
  this.length = 40;
  this.fixedLength = true;
}

draw2d.ColumnTypeModelLongText.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelLongText.prototype.type="draw2d.ColumnTypeModelLongText";


/**
 *
 * @type String
 **/
draw2d.ColumnTypeModelLongText.prototype.getDefault=function()
{
   return this.defaultValue;
}


/**
 * Set the default value of the column
 
 **/
draw2d.ColumnTypeModelLongText.prototype.setDefault=function(/*:String*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelLongText.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;

   return att;
}
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
draw2d.ColumnTypeModelDocument=function()
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_DOCUMENT);
}

draw2d.ColumnTypeModelDocument.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelDocument.prototype.type="draw2d.ColumnTypeModelDocument";


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelDocument.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties

   return att;
}
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
draw2d.ColumnTypeModelLong=function( /*:long*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_LONG);
  if(defaultValue)
    this.defaultValue = defaultValue;
  else
    this.defaultValue = 0;
}

draw2d.ColumnTypeModelLong.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelLong.prototype.type="draw2d.ColumnTypeModelLong";


/**
 *
 * @type long
 **/
draw2d.ColumnTypeModelLong.prototype.getDefault=function()
{
   return this.defaultValue;
}

/**
 * Set the default value of the column
 **/
draw2d.ColumnTypeModelLong.prototype.setDefault=function(/*:long*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelLong.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;

   return att;
}
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
draw2d.ColumnTypeModelUniqueKeyPart=function( /*:boolean*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_KEY_PART);
}

draw2d.ColumnTypeModelUniqueKeyPart.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelUniqueKeyPart.prototype.type="draw2d.ColumnTypeModelUniqueKeyPart";



/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelUniqueKeyPart.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties

   return att;
}
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

draw2d.ColumnTypeModelText=function( /*:String*/ defaultValue, /*:int*/ length)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_TEXT);

  if(defaultValue)
     this.defaultValue = defaultValue;
  else
     this.defaultValue = "";
  
  if(length)
	  this.length = length;
  else
	  this.length = 40;
  this.fixedLength = true;
}

draw2d.ColumnTypeModelText.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelText.prototype.type="draw2d.ColumnTypeModelText";


/**
 *
 * @type String
 **/
draw2d.ColumnTypeModelText.prototype.getDefault=function()
{
   return this.defaultValue;
}

/**
 *
 * @type int
 **/
draw2d.ColumnTypeModelText.prototype.getLength=function()
{
   return this.length;
}

/**
 *
 * @type int
 **/
draw2d.ColumnTypeModelText.prototype.isFixedLength=function()
{
   return this.fixedLength;
}

/**
 * Set the default value of the column
 
 **/
draw2d.ColumnTypeModelText.prototype.setDefault=function(/*:String*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelText.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;
   att.length= this.length;
   att.fixedLength = this.fixedLength;

   return att;
}
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
draw2d.ColumnTypeModelDecimal=function( /*:boolean*/ defaultValue)
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_DECIMAL);
  if(defaultValue!==undefined)
    this.defaultValue = defaultValue;
  else
    this.defaultValue = 0.0;
};

draw2d.ColumnTypeModelDecimal.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelDecimal.prototype.type="draw2d.ColumnTypeModelDecimal";


/**
 *
 * @type int
 **/
draw2d.ColumnTypeModelDecimal.prototype.getDefault=function()
{
   return this.defaultValue;
};

/**
 * Set the default value of the column
 
 **/
draw2d.ColumnTypeModelDecimal.prototype.setDefault=function(/*:int*/ value)
{
    var save = this.getDefault();
    if(save==value)
      return;

    this.defaultValue = value;
    this.parent.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_DEFAULT_CHANGED, save, value);
};


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelDecimal.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.defaultValue= this.defaultValue;

   return att;
};
draw2d.ColumnTypeModelTimestamp=function()
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_TIMESTAMP);
}

draw2d.ColumnTypeModelTimestamp.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelTimestamp.prototype.type="draw2d.ColumnTypeModelTimestamp";



/**
 * Returns all attributes which are relevant for serialization.
 * 
 * @return The list of persistent attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelTimestamp.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
//   att.defaultValue= this.defaultValue;

   return att;
}
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
draw2d.ColumnTypeModelRelevanceIndicator=function()
{
  draw2d.ColumnTypeModel.call(this,draw2d.ColumnModel.TYPE_INDICATOR);
}

draw2d.ColumnTypeModelRelevanceIndicator.prototype = new  draw2d.ColumnTypeModel;
draw2d.ColumnTypeModelRelevanceIndicator.prototype.type="draw2d.ColumnTypeModelRelevanceIndicator";




/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.ColumnTypeModelRelevanceIndicator.prototype.getPersistentAttributes=function()
{
   var att = draw2d.ColumnTypeModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties

   return att;
}
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
draw2d.ColumnModel=function(/*:String*/ name, /*:draw2d.FieldTypeModel*/ typeModel)
{
   draw2d.AbstractObjectModel.call(this);

   /** @private */
   this.name  = name;

   if(typeModel)
      this.typeModel = typeModel;
   else
      this.typeModel = new draw2d.ColumnTypeModelBoolean(false);
}

draw2d.ColumnModel.prototype = new draw2d.AbstractObjectModel;
/** @private */
draw2d.ColumnModel.prototype.type="draw2d.ColumnModel";

draw2d.ColumnModel.TYPE_KEY_PART  = "KEY_PART";
draw2d.ColumnModel.TYPE_INDICATOR = "INDICATOR";
draw2d.ColumnModel.TYPE_TEXT      = "TEXT";
draw2d.ColumnModel.TYPE_DOCUMENT  = "DOCUMENT";
draw2d.ColumnModel.TYPE_INTEGER   = "INTEGER";
draw2d.ColumnModel.TYPE_LONG      = "LONG";
draw2d.ColumnModel.TYPE_FLOAT     = "FLOAT";
draw2d.ColumnModel.TYPE_DOUBLE    = "DOUBLE";
draw2d.ColumnModel.TYPE_DECIMAL   = "DECIMAL";
draw2d.ColumnModel.TYPE_DATE      = "DATE";
draw2d.ColumnModel.TYPE_TIME      = "TIME";
draw2d.ColumnModel.TYPE_TIMESTAMP = "TIMESTAMP";
draw2d.ColumnModel.TYPE_LONGTEXT  = "LONGTEXT";
draw2d.ColumnModel.TYPE_BINARY    = "BINARY";
draw2d.ColumnModel.TYPE_ENUM      = "ENUM";
draw2d.ColumnModel.TYPE_BOOLEAN   = "BOOLEAN";



/**
 * Return the name of the column.
 *
 * @type String
 **/
draw2d.ColumnModel.prototype.getName=function()
{
  return this.name;
}

/**
 * Return <b>true</b> if the column the relevance indicator for the import.<br>
 *
 * @type boolean
 **/
draw2d.ColumnModel.prototype.isRelevanceIndicator=function()
{
  return (this.typeModel instanceof draw2d.ColumnTypeModelRelevanceIndicator);
}

/**
 * Returns an extended "human readable" label of the column.
 *
 * @type String
 **/
draw2d.ColumnModel.prototype.getExtendedDescriptionLabel=function()
{
   if(this.getTypeName()== draw2d.ColumnModel.TYPE_TEXT)
     return this.getName()+" "+this.getTypeName()+"<"+this.getLengthAsString()+">";
   if(this.isRelevanceIndicator())
     return "[RELEVANCE INDICATOR]";
   return this.getName()+" "+this.getTypeName();
}


/**
 * Returns the name of the column type
 *
 * @type String
 **/
draw2d.ColumnModel.prototype.getTypeName=function()
{
  return this.typeModel.getName();
}


draw2d.ColumnModel.prototype.setTypeModel=function(/*:draw2d.FieldTypeModel*/ typeModel)
{
  if(!(typeModel instanceof draw2d.ColumnTypeModel))
    throw "Invalid parameter type in [ColumnModel.prototype.setTypeModel]";

  this.typeModel = typeModel;
  this.typeModel.setModelParent(this);
}

draw2d.ColumnModel.prototype.getTypeModel=function()
{
  return this.typeModel;
}

draw2d.ColumnModel.prototype.getLengthAsString=function()
{
  var length = "";
  if (draw2d.ColumnModel.TYPE_TEXT == this.getTypeName())
  {
    length = parseInt(this.getTypeModel().getLength());
    if(this.getTypeModel().isFixedLength())
      length = "["+length+"]";
  }
  return length;
}

/**
 * @type draw2d.ConverterModel
 **/
draw2d.ColumnModel.prototype.getConverterModel=function()
{
   return this.getModelParent().getConverterModel();
}


/**
 * Returns all attributes which are relevant for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 * @type Object
 **/
draw2d.ColumnModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.name= this.name;
   att.typeModel= this.typeModel;

   return att;
}
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

/**
 * @class The model for a DB table.

 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.ExcelSheetModel=function()
{
   draw2d.AbstractRecordSourceModel.call(this);
   this.providerType = draw2d.AbstractRecordSourceModel.TYPE_SOURCE;
   this.firstDataRow = 1;
   this.duringAdjust = false;
}

draw2d.ExcelSheetModel.prototype = new draw2d.AbstractRecordSourceModel;
/** @private **/
draw2d.ExcelSheetModel.prototype.type="draw2d.ExcelSheetModel";


/**
*
**/
draw2d.ExcelSheetModel.prototype.setFirstDataRow=function(/*:int*/ index)
{
   if(index ==this.firstDataRow)
      return;

  var save = this.firstDataRow;
  this.firstDataRow = index;
}

/**
* @type int
**/
draw2d.ExcelSheetModel.prototype.getFirstDataRow=function()
{
  return this.firstDataRow ;
}


/**
 *  
 */
draw2d.ExcelSheetModel.prototype.setColumnCount=function(/*:int*/ newCount)
{
   if(this.duringAdjust==true || newCount==this.columns.getSize())
	   return;
   this.duringAdjust = true;
   try
   {
	   if(newCount<this.columns.getSize())
	   {
		   while(newCount != this.columns.getSize())
		   {
			   this.removeColumnModel(this.columns.get(this.columns.getSize()-1));
			   this.columnCount =this.columns.getSize();
		   }
	   }
	   else if(newCount>this.columns.getSize())
	   {
		   while(newCount != this.columns.getSize())
		   {
			   this.addColumnModel(new draw2d.ColumnModel(""+this.columns.getSize(), new draw2d.ColumnTypeModelText("",4000)));
			   this.columnCount =this.columns.getSize();
		   }
	   }
   }
   catch(e)
   {
      this.duringAdjust =false;
      pushErrorStack(e,"draw2d.ExcelSheetModel.prototype.adjustColumns=function(/*:int*/ newCount)");
   }
   this.duringAdjust =false;
}

/**
 * Returns all attributes which are relevant for serialization.
 * 
 * @return The list of persistent attribute.
 * @since 0.9.15
 **/
draw2d.ExcelSheetModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractRecordSourceModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.firstDataRow = this.firstDataRow;
   att.columnCount = this.columnCount;
   
   return att;
}
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

/**
 * @class The model for a DB table.

 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.TableModel=function(/*:String*/ name)
{
   draw2d.AbstractRecordSourceModel.call(this, name);

   /** @private */
   this.uniqueKey = null;
   this.uniqueKeyColumns = new draw2d.ArrayList();
   this.createIfNotExists=false;
   this.addColumnModel(new draw2d.ColumnModel("indicator",new draw2d.ColumnTypeModelRelevanceIndicator()));
}


draw2d.TableModel.prototype = new draw2d.AbstractRecordSourceModel;
/** @private **/
draw2d.TableModel.prototype.type="draw2d.TableModel";



/**
 * Add a field or column to the database table.
 *
 * @param {draw2d.ColumnModel} column the column to add
 **/
draw2d.TableModel.prototype.setUniqueKeyModel=function(/*:draw2d.UniqueKeyModel*/ key)
{
  if(key !=null && !(key instanceof draw2d.UniqueKeyModel))
    throw "Invalid parameter type in [AbstractRecordSourceModel.prototype.addUniqueKeyModel]";

  if(this.uniqueKey!=null)
  {
    var names = this.uniqueKey.getColumnNames();
    var connections = this.getConverterModel().getConnectionModels().clone();
    var count = connections.getSize();
    for(var ii=0; ii<count;ii++)
    {
        var con = connections.get(ii);
        for(iii=0;iii<this.uniqueKeyColumns.getSize();iii++)
        {
          var column = this.uniqueKeyColumns.get(iii);
          if(con.getSourcePortModel()==column)
            con.getModelParent().removeConnectionModel(con);
          else if(con.getTargetPortModel()==column)
            con.getModelParent().removeConnectionModel(con);
        }
    }

    this.uniqueKeyColumns = new draw2d.ArrayList();
    this.uniqueKey.setModelParent(null);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_REMOVED,this.uniqueKey,null);
  }

  if(key !=null)
  {
    this.uniqueKey=key;
    var names = key.getColumnNames();
    for(var i=0;i<names.getSize();i++)
    {
      var name = names.get(i);
      var column = new draw2d.ColumnModel(key.getName()+"."+name);
      column.setModelParent(this);
      this.uniqueKeyColumns.add(column);
    }
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_ADDED,null, key);
  }
}

/**
 * Return all unique keys of this table.
 *
 * @type draw2d.UniqueKeyModel
 **/
draw2d.TableModel.prototype.getUniqueKeyModel=function()
{
  return this.uniqueKey;
}

 /**
  * Return [true] i the create should be created if it not possible to retrieve them 
  * from the database.
  *
  * @type boolean
  **/
 draw2d.TableModel.prototype.getCreateIfNotExists=function()
 {
   return this.createIfNotExists;
 }

 /**
 * Return [true] i the create should be created if it not possible to retrieve them 
 * from the database.
 *
 * @param {boolean}
 **/
draw2d.TableModel.prototype.setCreateIfNotExists=function(/*:boolean*/ forceCreate)
{
  this.createIfNotExists = forceCreate;
}


/**
 * return the column model with the hands over columnName.
 *
 * @return The column model with the given name or null
 * @param {String} columnName the name of the column
 * @type draw2d.ColumnModel
 **/
draw2d.TableModel.prototype.getColumnModel=function(/*:String*/ columnName)
{
  var column = draw2d.AbstractRecordSourceModel.prototype.getColumnModel.call(this, columnName);
  if(column!=null)
    return column;

  // versuchen ob es eventuell ein Teil eines uniqueKey ist.
  //
  var count = this.uniqueKeyColumns.getSize();
  for(var i=0;i<count;i++)
  {
    var column = this.uniqueKeyColumns.get(i);
    if(column.getName()==columnName)
    {
       return column;
    }
  }
  return null;
}


/**
 * Returns all attributes which are relevant for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 **/
draw2d.TableModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractRecordSourceModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.uniqueKey = this.uniqueKey;
   att.uniqueKeyColumns = this.uniqueKeyColumns;
   att.createIfNotExists = this.createIfNotExists;
   
   return att;
}
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

/**
 * @class OneToMany relation between 2 database tables
 * fromTabel <--- toTable
 **/
draw2d.ColumnConnectionModel=function( /*:String*/ sourceTableId, /*:String*/ sourceField, /*:String*/ targetTableId, /*:String*/ targetField)
{
  draw2d.AbstractConnectionModel.call(this);

  /** @private */
  this.sourceTableId = sourceTableId;
  /** @private */
  this.sourceField = sourceField;
  /** @private */
  this.targetTableId = targetTableId;
  /** @private */
  this.targetField = targetField;
  /** @private **/
  this.transferMode = draw2d.ColumnConnectionModel.TRANSFER_MODE_ALWAYS;
}

draw2d.ColumnConnectionModel.TRANSFER_MODE_ALWAYS          = "always";
draw2d.ColumnConnectionModel.TRANSFER_MODE_TARGET_NULL     = "ifTargetNull";
draw2d.ColumnConnectionModel.TRANSFER_MODE_SOURCE_NOT_NULL = "ifSourceNotNull";

draw2d.ColumnConnectionModel.EVENT_SOURCE_CHANGED  = "source changed";
draw2d.ColumnConnectionModel.EVENT_TARGET_CHANGED  = "target changed";

draw2d.ColumnConnectionModel.prototype = new draw2d.AbstractConnectionModel;
/** @private */
draw2d.ColumnConnectionModel.prototype.type="draw2d.ColumnConnectionModel";


/**
 *
 **/
draw2d.ColumnConnectionModel.prototype.setSourceModel=function(/*:draw2d.ColumnModel*/ model)
{
   var save1 = this.sourceTableId;
   var save2 = this.sourceField;

   this.sourceTableId = model.getModelParent().getId();
   this.sourceField = model.getName();

   if(save1 ==this.sourceTableId && save2==this.sourceField)
      return;

   // inform all listener, mainly the visual representation, about the changes.
   this.firePropertyChange(draw2d.ColumnConnectionModel.EVENT_SOURCE_CHANGED,null, model);
}

/**
 *
 * @type draw2d.AbstractRecordSourceModel
 **/
draw2d.ColumnConnectionModel.prototype.getSourceModel=function()
{
   return this.getConverterModel().getRecordSourceModel(this.sourceTableId);
}


/**
 *
 **/
draw2d.ColumnConnectionModel.prototype.setTargetModel=function(/*:draw2d.ColumnModel*/ model)
{
   var save1 = this.targetTableId;
   var save2 = this.targetField;

   this.targetTableId = model.getModelParent().getId();
   this.targetField = model.getName();

   if(save1 ==this.targetTableId && save2==this.targetField)
      return;

   // inform all listener, mainly the visual representation, about the changes.
   this.firePropertyChange(draw2d.ColumnConnectionModel.EVENT_TARGET_CHANGED,null, model);
}


/**
 *
 * @type draw2d.AbstractRecordSourceModel
 **/
draw2d.ColumnConnectionModel.prototype.getTargetModel=function()
{
   return this.getConverterModel().getRecordSourceModel(this.targetTableId);
}


/**
 *
 * @type String
 **/
draw2d.ColumnConnectionModel.prototype.getSourcePortName=function()
{
   return this.sourceField;
}

/**
 *
 * @type String
 **/
draw2d.ColumnConnectionModel.prototype.getTargetPortName=function()
{
   return this.targetField;
}

/**
 *
 * @type String
 **/
draw2d.ColumnConnectionModel.prototype.getTransferMode=function()
{
   return this.transferMode;
}


/**
 *
 **/
draw2d.ColumnConnectionModel.prototype.setTransferMode=function(/*:String*/ transferMode)
{
   this.transferMode= transferMode;
   // inform all listener, mainly the visual representation, about the changes.
   this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_COLUMN_ADDED,null, column);
}

/**
 *
 **/
draw2d.ColumnConnectionModel.prototype.getSourcePortModel=function()
{
   return this.getSourceModel().getColumnModel(this.getSourcePortName());
}

/**
 *
 **/
draw2d.ColumnConnectionModel.prototype.getTargetPortModel=function()
{
   return this.getTargetModel().getColumnModel(this.getTargetPortName());
}


/**
 * @type draw2d.ConverterModel
 **/
draw2d.ColumnConnectionModel.prototype.getConverterModel=function()
{
   return this.getModelParent().getConverterModel();
}


/**
 * Returns all attributes which are relevatn for serialization.
 * 
 * @return The list of persistend attribute.
 **/
draw2d.ColumnConnectionModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.sourceTableId = this.sourceTableId;
   att.sourceField   = this.sourceField;
   att.targetTableId = this.targetTableId;
   att.targetField   = this.targetField;
   att.transferMode  = this.transferMode;

   return att;
}
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

/**
 * Main data model for converting/importing ExcelSheet into a database
 *
 **/
draw2d.ConverterModel=function()
{
  this.recordSourceModels = new draw2d.ArrayList();
  this.name = "default";
}


draw2d.ConverterModel.prototype = new draw2d.AbstractObjectModel;
draw2d.ConverterModel.prototype.type="draw2d.ConverterModel";


/**
 *
 **/
draw2d.ConverterModel.prototype.getModelChildren=function()
{
   return this.recordSourceModels;
}

/**
 * Return all connections from the model.
 *
 * @type draw2d.ArrayList
 **/
draw2d.ConverterModel.prototype.getConnectionModels=function()
{
  var result = new draw2d.ArrayList();
  var count = this.recordSourceModels.getSize();

  for(var i=0; i<count;i++)
  {
     var model = this.recordSourceModels.get(i);
     result.addAll(model.getConnectionModels());
  }

  return result;
}


/**
 * Returns the table models which are involved in the import process.
 *
 * @type draw2d.ArrayList
 **/
draw2d.ConverterModel.prototype.getRecordSourceModels=function()
{
   return this.recordSourceModels;
}

/**
 * Add a new RecordSourceModel to the Converter model
 *
 **/
draw2d.ConverterModel.prototype.addRecordSourceModel=function(/*:draw2d.AbstractRecordSourceModel*/ model)
{
   this.recordSourceModels.add(model);
   model.setModelParent(this);
   // inform all listener, mainly the visual representation, about the changes.
   this.firePropertyChange(draw2d.AbstractObjectModel.EVENT_ELEMENT_ADDED,null, model);
}

/**
 **/
draw2d.ConverterModel.prototype.removeRecordSourceModel=function(/*:draw2d.AbstractRecordSourceModell*/ model)
{
   if(this.recordSourceModels.remove(model)!=null)
   {
     model.setModelParent(null);
     // inform all listener, mainly the visual representation, about the changes.
     this.firePropertyChange(draw2d.AbstractObjectModel.EVENT_ELEMENT_REMOVED,model,null);
   }
}

/**
 * Return the table model with the hands over db name.
 *
 * @type draw2d.TableModel
 **/
draw2d.ConverterModel.prototype.getRecordSourceModel=function(/*:String*/ sourceId)
{
   var count=this.recordSourceModels.getSize();
   for(var i=0;i<count;i++)
   {
      var source = this.recordSourceModels.get(i);
      if(source.getId()==sourceId)
         return source;
   }
   return null;
}

/**
 * Return the name of the converter model
 *
 * @type String
 **/
draw2d.ConverterModel.prototype.getName=function()
{
   return this.name;
}
 
/**
* Return the name of the converter model
*
* @param {String}
**/
draw2d.ConverterModel.prototype.setName=function(/*:String*/ name)
{
  this.name = name;
}

/**
 * Return the root  element of the model.
 *
 * @type draw2d.ConverterModel
 **/
draw2d.ConverterModel.prototype.getConverterModel=function()
{
   return this;
}

/**
 * Return all attribute wich are relevant for the XML serialisation
 *
 **/
draw2d.ConverterModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.recordSourceModels= this.recordSourceModels;
   att.name= this.name;

   return att;
}
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
draw2d.UniqueKeyModel=function(/*:String*/ name)
{
   draw2d.AbstractObjectModel.call(this);
   
   if(!name)
     return;

   /** @private */
   this.name  = name;

   /** @private */
   this.columnModelNames = new draw2d.ArrayList();

}

draw2d.UniqueKeyModel.prototype = new draw2d.AbstractObjectModel;
/** @private */
draw2d.UniqueKeyModel.prototype.type="draw2d.UniqueKeyModel";



/**
 **/
draw2d.UniqueKeyModel.prototype.addColumnModel=function(/*:String*/ columnName)
{
  if(this.columnModelNames.indexOf(columnName)==-1)
  {
    this.columnModelNames.add(columnName);
    // inform all listener, mainly the visual representation, about the changes.
    this.firePropertyChange(draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_CHANGED,null, this);
  }
}

/**
 **/
draw2d.UniqueKeyModel.prototype.hasColumnModel=function(/*:String*/ columnName)
{
  return (this.columnModelNames.indexOf(columnName)!=-1);
}

/**
 *
 **/
draw2d.UniqueKeyModel.prototype.getColumnNames=function()
{
   return this.columnModelNames;
}


/**
 * Return the name of the column.
 *
 * @type String
 **/
draw2d.UniqueKeyModel.prototype.getName=function()
{
  return this.name;
}



/**
 * Returns all attributes which are relevant for serialization.
 * 
 * @return The list of persistend attribute.
 * @since 0.9.18
 * @type Object
 **/
draw2d.UniqueKeyModel.prototype.getPersistentAttributes=function()
{
   var att = draw2d.AbstractObjectModel.prototype.getPersistentAttributes.call(this);

   // enrich the base attributes with the class/instance specific properties
   att.name= this.name;
   att.columnModelNames = this.columnModelNames;

   return att;
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 * @since 0.9.15
 */
draw2d.InputColumnFigure=function()
{
  draw2d.InputPort.call(this, new draw2d.ImageFigure(""+draw2d.Configuration.IMAGEPATH+"inputport.png"));
  this.setDimension(10,10);
  this.setColor(null);
  this.setBackgroundColor(null);
}

draw2d.InputColumnFigure.prototype = new draw2d.InputPort;
draw2d.InputColumnFigure.prototype.type="draw2d.InputColumnFigure";


/**
 * Returns the Command to perform the specified Request or null.<br>
 * Inherited figures can override this method to return the own implementation
 * of the request.<br>
 *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a draw2d.Command
 * @type draw2d.Command 
 **/
draw2d.InputColumnFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
   // Connect request between two ports
   //
   if(request.getPolicy() ==draw2d.EditPolicy.CONNECT)
   {
     if(request.source.parentNode.id == request.target.parentNode.id)
        return null;
     if(request.target.getConnections().getSize()>0)
        return null;

     if(request.source instanceof draw2d.OutputPort)
     {
        // This is the different to the OutputPort implementation of createCommand
        var sourceModel = request.source.getParent().getModel().getColumnModel(request.source.getName());
        var targetModel = request.target.getParent().getModel().getColumnModel(request.target.getName());
        return new draw2d.CommandConnectColumn(sourceModel,targetModel);
     }
   }

   // ...else call the base class
   return draw2d.InputPort.prototype.createCommand.call(this,request);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 * @since 0.9.18
 */
draw2d.InputKeyFigure=function()
{
   draw2d.InputPort.call(this, new draw2d.ImageFigure(""+draw2d.Configuration.IMAGEPATH+"keyport.png"));
   this.setDimension(10,10);
   this.setColor(null);
   this.setBackgroundColor(null);
}

draw2d.InputKeyFigure.prototype = new draw2d.InputPort;
draw2d.InputKeyFigure.prototype.type="draw2d.InputKeyFigure";


/**
 * Returns the Command to perform the specified Request or null.<br>
 * Inherited figures can override this method to return the own implementation
 * of the request.<br>
 *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a draw2d.Command
 * @type draw2d.Command 
 **/
draw2d.InputKeyFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
   // Connect request between two ports
   //
   if(request.getPolicy() ==draw2d.EditPolicy.CONNECT)
   {
     if(request.source.parentNode.id == request.target.parentNode.id)
        return null;
     if(request.target.getConnections().getSize()>0)
        return null;
     if(request.source instanceof draw2d.OutputPort)
     {
        // This is the different to the OutputPort implementation of createCommand
        var sourceModel = request.source.getParent().getModel().getColumnModel(request.source.getName());
        var targetModel = request.target.getParent().getModel().getColumnModel(request.target.getName());
        return new draw2d.CommandConnectColumn(sourceModel,targetModel);
     }
   }

   // ...else call the base class
   return draw2d.InputPort.prototype.createCommand.call(this,request);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 * @since 0.9.15
 */
draw2d.OutputColumnFigure=function()
{
  draw2d.OutputPort.call(this, new draw2d.ImageFigure(""+draw2d.Configuration.IMAGEPATH+"outputport.png"));
  this.setDimension(10,10);
  this.setColor(null);
  this.setBackgroundColor(null);
}

draw2d.OutputColumnFigure.prototype = new draw2d.OutputPort;
draw2d.OutputColumnFigure.prototype.type="draw2d.OutputColumnFigure";

/**
 * Returns the Command to perform the specified Request or null.<br>
 * Inherited figures can override this method to return the own implementation
 * of the request.<br>
 *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a draw2d.Command
 * @type draw2d.Command 
 * @since 0.9.18
 **/
draw2d.OutputColumnFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
   // Connect request between two ports
   //
   if(request.getPolicy() ==draw2d.EditPolicy.CONNECT)
   {
     if(request.source.parentNode.id == request.target.parentNode.id)
        return null;
     if(request.target.getConnections().getSize()>0)
        return null;

     if(request.source instanceof draw2d.InputPort)
     {
        // This is the different to the InputPort implementation of createCommand.
        var sourceModel = request.source.getParent().getModel().getColumnModel(request.source.getName());
        var targetModel = request.target.getParent().getModel().getColumnModel(request.target.getName());
        return new draw2d.CommandConnectColumn(targetModel,sourceModel);
     }
     return null;
   }

   // ...else call the base class
   return draw2d.Port.prototype.createCommand.call(this,request);
}
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

/**
 *
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 * @since 0.9.18
 */
draw2d.ColumnConnectionFigure=function()
{
  draw2d.Connection.call(this);
  this.setTargetDecorator(new draw2d.ArrowConnectionDecorator());
  this.getTargetDecorator().setColor(draw2d.ColumnConnectionFigure.DEFAULT_COLOR);
  this.setRouter(new draw2d.BezierConnectionRouter());
  this.setColor(draw2d.ColumnConnectionFigure.DEFAULT_COLOR);
}

draw2d.ColumnConnectionFigure.prototype = new draw2d.Connection;
draw2d.ColumnConnectionFigure.prototype.type="draw2d.ColumnConnectionFigure";
draw2d.ColumnConnectionFigure.DEFAULT_COLOR = new draw2d.Color(95,95,95);

draw2d.ColumnConnectionFigure.prototype.propertyChange=function( /*:draw2d.PropertyChangeEvent*/ event)
{
  switch(event.property)
  {
    case draw2d.ColumnConnectionModel.EVENT_SOURCE_CHANGED:
        this.refreshSourcePort();
        break;
    case draw2d.ColumnConnectionModel.EVENT_TARGET_CHANGED:
        this.refreshTargetPort();
        break;
   }
}


/**
 * Returns the Command to perform the specified Request or null.
  *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a Command
 * @type draw2d.Command 
 **/
draw2d.ColumnConnectionFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
  if(request.getPolicy() == draw2d.EditPolicy.MOVE)
  {
    return new draw2d.CommandReconnectColumn(this.model);
  }

  if(request.getPolicy() == draw2d.EditPolicy.DELETE)
  {
    if(this.isDeleteable()==true)
      return new draw2d.CommandDisconnectColumn(this.getModel());
    return null;
  }

  return  null;
}
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
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA,
or see http://www.gnu.org/copyleft/lesser.html
*/
draw2d.AbstractTableFigure=function()
{
  /** private */
  this.table   = null;
  /** private */
  this.header  = null;
  /** private */
  this.columns = [];

  draw2d.Node.call(this);

  this.setResizeable(false);
}

draw2d.AbstractTableFigure.prototype = new draw2d.Node;
draw2d.AbstractTableFigure.prototype.type="draw2d.AbstractTableFigure";


draw2d.AbstractTableFigure.prototype.paint=function()
{
	try
	{
	   var model = this.getModel();
	   this.setPosition(model.getPosition().x, model.getPosition().y);
	
	   this.header.innerHTML = model.getName();
	   this.setDimension(this.getWidth(),this.getHeight());
	
	   var fields = this.model.getColumnModels();
	   for(var i=0; i<fields.getSize(); i++)
	   {
	     var field =fields.get(i);
	     this.addColumn(field.getName(),field.getExtendedDescriptionLabel(), field.isRelevanceIndicator());
	   }
	}
	catch(e)
	{
	   pushErrorStack(e,"draw2d.AbstractTableFigure.prototype.paint=function()");
	}
}


draw2d.AbstractTableFigure.prototype.propertyChange=function( /*:draw2d.PropertyChangeEvent*/ event)
{
  switch(event.property)
  {
    case draw2d.AbstractRecordSourceModel.EVENT_POSITION_CHANGED:
        this.setPosition(event.newValue.x,event.newValue.y);
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_COLUMN_ADDED:
        this.addColumn(event.newValue.getName(),event.newValue.getExtendedDescriptionLabel(), event.newValue.isRelevanceIndicator());
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_ADDED:
        this.addUniqueKey(event.newValue.getName(),event.newValue.getColumnNames());
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_CHANGED:
        alert("unique key changed not implemented");
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_COLUMN_REMOVED:
        this.removeColumn(event.oldValue.getName());
        break;
    case draw2d.AbstractObjectModel.EVENT_CONNECTION_ADDED:
        this.refreshConnections();
        break;
    case draw2d.AbstractObjectModel.EVENT_CONNECTION_REMOVED:
        this.refreshConnections();
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_NAME_CHANGED:
        this.header.innerHTML = this.getModel().getName();
        break;
   }
}


/**
 * Returns the calculated width of the figure.
 *
 **/
draw2d.AbstractTableFigure.prototype.getWidth=function()
{
  if(this.table==null)
    return 10;
  if(window.getComputedStyle)
    return parseInt(getComputedStyle(this.table,'').getPropertyValue("width"));
  return (this.table.clientWidth);
}

/**
 * Returns the calculated height of the figure.
 *
 **/
draw2d.AbstractTableFigure.prototype.getHeight=function()
{
  if(this.table==null)
    return 10;
  if(window.getComputedStyle)
    return parseInt(getComputedStyle(this.table,'').getPropertyValue("height"));
  return (this.table.clientHeight);
}


/**
 * @private
 **/
draw2d.AbstractTableFigure.prototype.addColumn=function(/*:String*/ name, /*:String*/ label )
{
   throw "Inherit class must override the method [AbstractTableFigure.prototype.addColumn]";
}


/**
 * @private
 **/
draw2d.AbstractTableFigure.prototype.removeColumn=function(/*:String*/ name)
{
   throw "Inherit class must override the method [AbstractTableFigure.prototype.removeColumn]";
}


/**
 * Returns the List of the connection model objects for which this Figure's model is the source. 
 * Callers must not modify the returned List. 
 * Only called if you use the MVC pattern of Draw2D
 *
 * @type draw2d.ArrayList
 * @return the List of model source connections
 * @since 0.9.18
 */
draw2d.AbstractTableFigure.prototype.getModelSourceConnections=function()
{
   return this.getModel().getConnectionModels();
}

/**
 * Returns the Command to perform the specified Request or null.
  *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a Command
 * @type draw2d.Command 
 **/
draw2d.AbstractTableFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
  if(request.getPolicy() == draw2d.EditPolicy.DELETE)
  {
    if(!this.canDrag)
      return null;
    return new draw2d.CommandRemoveRecordSource(this.getModel());
  }

  return  null;
}


draw2d.AbstractTableFigure.prototype.getOffset=function(/*:HTMLElement*/ element)
{
    var top =0;
    while(element != this.html)
    {
      if(element.tagName.toUpperCase()!="TD")
        top =  top + element.offsetTop;
      element= element.parentNode;
    }
    return top;
}/* This notice must be untouched at all times.

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
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA,
or see http://www.gnu.org/copyleft/lesser.html
*/
draw2d.ExcelSheetFigure=function()
{
  draw2d.AbstractTableFigure.call(this);
}

draw2d.ExcelSheetFigure.prototype = new draw2d.AbstractTableFigure;
draw2d.ExcelSheetFigure.prototype.type="draw2d.ExcelSheetFigure";


/**
 * Returns the Command to perform the specified Request or null.
  *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a Command
 * @type draw2d.Command 
 **/
draw2d.ExcelSheetFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
  if(request.getPolicy() == draw2d.EditPolicy.MOVE)
  {
    if(!this.canDrag)
      return null;
    return new draw2d.CommandMoveExcelSheet(this.model);
  }

  return null;
}


draw2d.ExcelSheetFigure.prototype.createHTMLElement=function()
{
 var item = draw2d.AbstractTableFigure.prototype.createHTMLElement.call(this);

 item.style.width="100px";
 item.style.height="100px";
 item.style.margin="0px";
 item.style.padding="0px";

 this.table = document.createElement("table");
 this.table.style.fontSize="8pt";
 this.table.style.margin="0px";
 this.table.style.padding="0px";
 this.table.cellPadding ="0";
 this.table.cellSpacing ="0";

 var row=this.table.insertRow(0);
 this.header=row.insertCell(0);
 this.disableTextSelection(this.header);
 this.header.innerHTML = "";
 this.header.colSpan="2";
 this.header.style.background ="transparent url("+draw2d.Configuration.IMAGEPATH+"header_excel.png) repeat-x";
 this.header.style.height ="25px";
 this.header.style.paddingLeft ="30px";
 this.header.style.paddingRight ="5px";
 this.disableTextSelection(this.header);
 item.appendChild(this.table);

 return item;
}



/**
 * @private
 **/
draw2d.ExcelSheetFigure.prototype.addColumn=function(/*:String*/ name, /*:String*/ label )
{
   try
   {
      var x=this.table.insertRow(this.table.rows.length);
      this.columns[name] = x;
      var y=x.insertCell(0);
      this.disableTextSelection(y);
      y.innerHTML=toExcelColumn(parseInt(name));
      if(this.table.rows.length%2==0)
         y.style.backgroundColor="#FFFFFF";
      else
         y.style.backgroundColor="#F5F5F5";
      y.style.borderBottom="1px solid #CCCCFF";
      y.style.whiteSpace="nowrap";
      y.style.padding="2px";
      this.disableTextSelection(y);
      this.setDimension(this.getWidth(),this.getHeight());
   
      var port = new draw2d.OutputColumnFigure();
      port.setCanDrag(this.getCanDrag());
      port.setWorkflow(this.workflow);
      port.setName(name);
      port.parentHTML = x;
      this.addPort(port,y.offsetWidth+5, y.offsetTop+y.clientHeight/2);
      port.paint();
   }
   catch(e)
   {
      pushErrorStack(e,"draw2d.ExcelSheetFigure.prototype.addColumn=function(/*:String*/ name, /*:String*/ label )");
   }
}



/**
 * @private
 **/
draw2d.ExcelSheetFigure.prototype.removeColumn=function(/*:String*/ name)
{
   var x = this.columns[name];
   if(x)
   {
     x.parentNode.removeChild(x);
     this.columns[name] = null;
     this.removePort(this.getOutputPort(name));
   }
   this.setDimension(this.getWidth(),this.getHeight());
}


  /**
* Set the new width and height of the figure. 
*
* @see #getMinWidth
* @see #getMinHeight
* @param {int} w The new width of the figure
* @param {int} h The new height of the figure
**/
draw2d.ExcelSheetFigure.prototype.setDimension=function(/*:int*/ w, /*:int*/ h)
{
    draw2d.AbstractTableFigure.prototype.setDimension.call(this,w,h);
   
    // rearange the ports
    var ports = this.getPorts();
    var count = ports.getSize();
    for(var i=0;i<count;i++)
    {
       var port = ports.get(i);
       if(port instanceof draw2d.OutputPort)
       {
          port.setPosition(w+5,this.getOffset(port.parentHTML)+port.parentHTML.clientHeight/2);
       }
    }
}
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
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA,
or see http://www.gnu.org/copyleft/lesser.html
*/
draw2d.TableFigure=function()
{
  draw2d.AbstractTableFigure.call(this);

  /** private */
  this.keys = [];
}

draw2d.TableFigure.prototype = new draw2d.AbstractTableFigure;
draw2d.TableFigure.prototype.type="draw2d.TableFigure";


draw2d.TableFigure.prototype.propertyChange=function( /*:draw2d.PropertyChangeEvent*/ event)
{
  switch(event.property)
  {
    case draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_ADDED:
        this.addUniqueKey(event.newValue.getName(),event.newValue.getColumnNames());
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_REMOVED:
        this.removeUniqueKey(event.oldValue.getName(),event.oldValue.getColumnNames());
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_UNIQUEKEY_CHANGED:
        alert("unique key changed not implemented");
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_COLUMN_UP:
        var rowToMove= this.table.tBodies[0].rows[event.oldValue+1];
        var rowBefore= this.table.tBodies[0].rows[event.newValue+1];
        this.table.tBodies[0].insertBefore(rowToMove, rowBefore);
        var tmp = rowToMove.style.backgroundColor;
        rowToMove.style.backgroundColor=rowBefore.style.backgroundColor;
        rowBefore.style.backgroundColor = tmp;
        // trigger resize and port position calculation
        this.setDimension(this.getWidth(),this.getHeight());
        break;
    case draw2d.AbstractRecordSourceModel.EVENT_COLUMN_DOWN:
        var rowToMove= this.table.tBodies[0].rows[event.oldValue+1];
        var rowBelow= this.table.tBodies[0].rows[event.newValue+1];
        this.table.tBodies[0].insertBefore(rowBelow, rowToMove);
        var tmp = rowToMove.style.backgroundColor;
        rowToMove.style.backgroundColor=rowBelow.style.backgroundColor;
        rowBelow.style.backgroundColor = tmp;
        // trigger resize and port position calculation
        this.setDimension(this.getWidth(),this.getHeight());
        break;
    default:
        draw2d.AbstractTableFigure.prototype.propertyChange.call(this,event);
   }
}

draw2d.TableFigure.prototype.createHTMLElement=function()
{
 var item = draw2d.AbstractTableFigure.prototype.createHTMLElement.call(this);

 item.style.width="100px";
 item.style.height="100px";
 item.style.margin="0px";
 item.style.padding="0px";

 this.table = document.createElement("table");
 this.table.style.fontSize="7pt";
 this.table.style.margin="0px";
 this.table.style.padding="0px";
 this.table.style.width="100%";
 this.table.cellPadding ="0";
 this.table.cellSpacing ="0";

 var row=this.table.insertRow(0);
 this.header=row.insertCell(0);
 this.disableTextSelection(this.header);
 this.header.innerHTML = "";
 this.header.colSpan="3";
 this.header.style.background ="transparent url("+draw2d.Configuration.IMAGEPATH+"header_table.png) repeat-x";
 this.header.style.height ="25px";
 this.header.style.paddingLeft ="30px";
 this.header.style.paddingRight ="5px";
 this.disableTextSelection(this.header);
 item.appendChild(this.table);

 this.uniqueTable = document.createElement("table");
 this.uniqueTable.style.fontSize="7pt";
 this.uniqueTable.style.margin="0px";
 this.uniqueTable.style.padding="0px";
 this.uniqueTable.style.width="100%";
 this.uniqueTable.cellPadding ="0";
 this.uniqueTable.cellSpacing ="0";
 var row=this.table.insertRow(1);
 var cell=row.insertCell(0);
 cell.colSpan="3";
 cell.appendChild(this.uniqueTable);

 return item;
}


draw2d.TableFigure.prototype.paint=function()
{
  draw2d.AbstractTableFigure.prototype.paint.call(this);

  var model = this.getModel();
  var key = model.getUniqueKeyModel();
  if(key!=null)
     this.addUniqueKey(key.getName(),key.getColumnNames());
}


/**
 * Returns the Command to perform the specified Request or null.
  *
 * @param {draw2d.EditPolicy} request describes the Command being requested
 * @return null or a Command
 * @type draw2d.Command 
 **/
draw2d.TableFigure.prototype.createCommand=function(/*:draw2d.EditPolicy*/ request)
{
  if(request.getPolicy() == draw2d.EditPolicy.MOVE)
  {
    if(!this.canDrag)
      return null;
    return new draw2d.CommandMoveTable(this.model);
  }

  return  draw2d.AbstractTableFigure.prototype.createCommand.call(this, request);
}

/**
 * @private
 **/
draw2d.TableFigure.prototype.removeUniqueKey=function(/*:String*/ name, /*:String[]*/ fieldNames)
{
   var x = this.keys[name];
   if(x)
   {
     x.parentNode.removeChild(x);
     this.keys[name] = null;
     for(var i=0;i<fieldNames.getSize();i++)
     {
        var fieldName =fieldNames.get(i);
        this.removePort(this.getInputPort(name+"."+fieldName));
     }
   }
   // trigger resize of the element
   this.setDimension(this.getWidth(),this.getHeight());
}

/**
 * @private
 **/
draw2d.TableFigure.prototype.addUniqueKey=function(/*:String*/ keyName, /*:String[]*/ fieldNames)
{
  var table = document.createElement("table");
  table.style.fontSize="7pt";
  table.style.margin="0px";
  table.style.padding="0px";
  table.style.border="1px solid black";
  table.style.width="100%";
  table.cellPadding ="0";
  table.cellSpacing ="0";

  var row = this.uniqueTable.insertRow(this.uniqueTable.rows.length);
  row.insertCell(0).appendChild(table);

  this.keys[keyName] = row;
  var x=table.insertRow(table.rows.length);
  var y=x.insertCell(0);
  this.disableTextSelection(y);
  y.innerHTML=keyName;
  y.style.backgroundColor="#d0ffd0";
  y.style.whiteSpace="nowrap";
  y.style.padding="2px";
  y.style.textAlign="right";
  y.style.fontSize="6pt";
  y.style.textDecoration="underline";
  y.style.cursor="pointer";
  y.style.color="gray";
  var oThis = this;
  y.onclick=function()
  {
    oThis.workflow.getCommandStack().execute(new draw2d.CommandSelectUniqueKey(oThis.getModel()));
  }
  this.disableTextSelection(y);
  for(var i=0;i<fieldNames.getSize();i++)
  {
      var fieldName =fieldNames.get(i); 
      var row=table.insertRow(table.rows.length);
      var cell=row.insertCell(0);
      this.disableTextSelection(cell);
      cell.innerHTML=fieldName;
      cell.style.backgroundColor="#d0ffd0";
      cell.style.whiteSpace="nowrap";
      cell.style.padding="2px";
      this.disableTextSelection(cell);
      var port = new draw2d.InputKeyFigure();
      port.setCanDrag(this.getCanDrag());
      port.setWorkflow(this.workflow);
      port.setName(keyName+"."+fieldName);
      port.parentHTML = row;
      this.addPort(port,-5, row.offsetTop+row.clientHeight/2);
      port.paint();
   }
   this.setDimension(this.getWidth(),this.getHeight());
}




/**
 * @private
 **/
draw2d.TableFigure.prototype.addColumn=function(/*:String*/ name, /*:String*/ label, /*:boolean*/ indicator)
{
   try
   {
      var oThis = this;
      var element;
      if(indicator)
      {
        element = this.header;
        var port = new draw2d.InputColumnFigure();
        port.setWorkflow(this.workflow);
        port.setName(name);
        port.setCanDrag(this.getCanDrag());
        port.parentHTML = element;
        this.addPort(port,-5, element.offsetTop+element.clientHeight/2);
        port.paint();

        this.setDimension(this.getWidth(),this.getHeight());
      }
      else
      {
        element=this.table.insertRow(this.table.rows.length);
        element.onmouseover = function()
        {
           $(this.cells[1]).select("img").each(function(img)
           {
              img.style.display="";
           });
        };
        element.onmouseout  = function()
        {
           $(this.cells[1]).select("img").each(function(img)
           {
              img.style.display="none";
           });
        };

        this.columns[name] = element;
        var y=element.insertCell(0);
        this.disableTextSelection(y);
        y.innerHTML=label;

        var up_down=element.insertCell(1);
        up_down.style.width="20px";
        up_down.style.whiteSpace="nowrap";
        // UP und DOWN Icons in die Zelle einf�gen
        //
        var upImage = document.createElement("img");
        upImage.src= draw2d.Configuration.IMAGEPATH+"up.png";
        upImage.style.float="left";
        upImage.style.cursor="pointer";
        upImage.title="Column Up";
        up_down.appendChild(upImage);
        upImage.onclick = function()
        {
           var index = this.parentNode.parentNode.sectionRowIndex;
           index = index-2; // Es gibt 2 Zeilen in der Tabelle welche keinen Bezug zu einer Column haben.
           index = index+1  // im Model ist an erster Stelle eine k�nstliche Column "INDICATOR". Dier muss ber�cksichtigt werden
           oThis.getModel().columnModelUp(index);
        }

        var downImage = document.createElement("img");
        downImage.src= draw2d.Configuration.IMAGEPATH+"down.png";
        downImage.style.clear="both";
        downImage.style.cursor="pointer";
        downImage.title="Column Down";
        up_down.appendChild(downImage);
        downImage.onclick = function()
        {
           var index = this.parentNode.parentNode.sectionRowIndex;
           index = index-2; // Es gibt 2 Zeilen in der Tabelle welche keinen Bezug zu einer Column haben.
           index = index+1  // im Model ist an erster Stelle eine k�nstliche Column "INDICATOR". Dier muss ber�cksichtigt werden
           oThis.getModel().columnModelDown(index);
        }

        if(this.table.rows.length%2==0)
        {
          element.style.backgroundColor="#FFFFFF";
        }
        else
        {
          element.style.backgroundColor="#F5F5F5";
        }
        y.style.borderBottom="1px solid #CCCCFF";
        up_down.style.borderBottom="1px solid #CCCCFF";
        y.style.whiteSpace="nowrap";
        y.style.padding="2px";

        port = new draw2d.OutputColumnFigure();
        port.setWorkflow(this.workflow);
        port.setName(name);
        port.setCanDrag(this.getCanDrag());
        this.addPort(port,element.offsetWidth+5, element.offsetTop+element.clientHeight/2);
        port.paint();
        port.parentHTML = element;

        var port = new draw2d.InputColumnFigure();
        port.setWorkflow(this.workflow);
        port.setName(name);
        port.setCanDrag(this.getCanDrag());
        port.parentHTML = element;
        this.addPort(port,-5, element.offsetTop+element.clientHeight/2);
        port.paint();
  
        // erst die Weite berechnen und dann das Bildchen verstecken
        this.setDimension(this.getWidth(),this.getHeight());
        upImage.style.display="none";
        downImage.style.display="none";
      }
   }
   catch(e)
   {
      pushErrorStack(e,"draw2d.TableFigure.prototype.addColumn=function(/*:String*/ name, /*:String*/ label, /*:boolean*/ indicator)");
   }
}


/**
 * @private
 **/
draw2d.TableFigure.prototype.removeColumn=function(/*:String*/ name)
{
   var x = this.columns[name];
   if(x)
   {
     x.parentNode.removeChild(x);
     this.columns[name] = null;
     this.removePort(this.getInputPort(name));
     this.removePort(this.getOutputPort(name));
   }
   // trigger resize of the element
   this.setDimension(this.getWidth(),this.getHeight());
}

 /**
 * @private
 **/
draw2d.TableFigure.prototype.removeAllColumns=function()
{
   for(var i=0;i<this.columns.lenght;i++)
   {
      var x = this.columns[i];
      if(x)
      {
        x.parentNode.removeChild(x);
        this.columns[i] = null;
        this.removePort(this.getOutputPort(name));
      }
   }
   this.columns=[];
   this.setDimension(this.getWidth(),this.getHeight());
}

/**
 * Set the new width and height of the figure. 
 *
 * @see #getMinWidth
 * @see #getMinHeight
 * @param {int} w The new width of the figure
 * @param {int} h The new height of the figure
 **/
draw2d.TableFigure.prototype.setDimension=function(/*:int*/ w, /*:int*/ h)
{
  draw2d.AbstractTableFigure.prototype.setDimension.call(this,w,h);

  // rearange the ports
  var ports = this.getPorts();
  var count = ports.getSize();
  for(var i=0;i<count;i++)
  {
     var port = ports.get(i);
     if(port instanceof draw2d.OutputPort)
     {
        port.setPosition(w+5,this.getOffset(port.parentHTML)+port.parentHTML.clientHeight/2);
     }
     else if(port instanceof draw2d.InputPort)
     {
        port.setPosition(-5,this.getOffset(port.parentHTML)+port.parentHTML.clientHeight/2);
     }
  }
}
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

/**
 * Base class for elements which can be inserted into an external
 * tool palette.<br>
 * Objects of this class can be drag&drop around the hole web page. An event will
 * be fired if the element has been dropped into the canvas.<br>
 * Inherited classes should override the drop event method to implement
 * special behaviour.
 *
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 * @since 0.9.18
 */
draw2d.TablePalettePart=function(/*:draw2d.ConverterModel*/ converterModel)
{
  draw2d.AbstractPalettePart.call(this);
  this.converterModel = converterModel;
}

/** @private **/
draw2d.TablePalettePart.prototype.type="draw2d.TablePalettePart";
draw2d.TablePalettePart.prototype = new draw2d.AbstractPalettePart;


/**
 * @private
 **/
draw2d.TablePalettePart.prototype.createHTMLElement=function()
{
    var item = draw2d.AbstractPalettePart.prototype.createHTMLElement.call(this);
    this.table = document.createElement("table");
    this.table.style.fontSize="8pt";
    this.table.style.margin="0px";
    this.table.style.padding="0px";
    this.table.cellPadding ="0";
    this.table.cellSpacing ="0";
    
    var row=this.table.insertRow(0);
    this.header=row.insertCell(0);
    this.header.innerHTML = "&lt;DB&nbsp;TABLE&gt;";
    this.header.colSpan="2";
    this.header.style.background ="transparent url("+draw2d.Configuration.IMAGEPATH+"header_table.png) repeat-x";
    this.header.style.height ="25px";
    this.header.style.paddingLeft ="30px";
    this.header.style.paddingRight ="5px";
    this.disableTextSelection(this.header);
    item.appendChild(this.table);
    return item;
}


/**
 *
 **/
draw2d.TablePalettePart.prototype.execute=function(/*:int*/ x, /*:int*/ y)
{
    editor.getGraphicalViewer().getCommandStack().execute(new draw2d.CommandAddTable(this.converterModel, x-10,y-10));
}
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

draw2d.GenericButton=function(/*:draw2d.PaletteWindow*/ palette)
{
  draw2d.Button.call(this,palette,16,16);
}

draw2d.GenericButton.prototype = new draw2d.Button;
/** @private */
draw2d.GenericButton.prototype.type="draw2d.GenericButton";


/**
 * @final
 */
draw2d.GenericButton.prototype.getImageUrl=function()
{
  if(this.enabled)
    return draw2d.Configuration.IMAGEPATH+this.type+".png";
  else
    return draw2d.Configuration.IMAGEPATH+this.type+"_disabled.png";
}
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

draw2d.ButtonDelete=function(/*:draw2d.PaletteWindow*/ palette)
{
  draw2d.GenericButton.call(this,palette,16,16);
}

draw2d.ButtonDelete.prototype = new draw2d.GenericButton;
/** @private */
draw2d.ButtonDelete.prototype.type="draw2d.ButtonDelete";


draw2d.ButtonDelete.prototype.execute=function()
{
  this.palette.workflow.getCommandStack().execute(new draw2d.CommandRemoveRecordSource(this.palette.workflow.getCurrentSelection().getModel()));
  draw2d.ToolGeneric.prototype.execute.call(this);
}
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

draw2d.ButtonMoveBack=function(/*:draw2d.PaletteWindow*/ palette)
{
  draw2d.GenericButton.call(this,palette,16,16);
}

draw2d.ButtonMoveBack.prototype = new draw2d.GenericButton;
/** @private */
draw2d.ButtonMoveBack.prototype.type="draw2d.ButtonMoveBack";


draw2d.ButtonMoveBack.prototype.execute=function()
{
  this.palette.workflow.moveBack(this.palette.workflow.getCurrentSelection());
  draw2d.ToolGeneric.prototype.execute.call(this);
}
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

draw2d.ButtonMoveFront=function(/*:draw2d.PaletteWindow*/ palette)
{
  draw2d.GenericButton.call(this,palette,16,16);
}

draw2d.ButtonMoveFront.prototype = new draw2d.GenericButton;
/** @private */
draw2d.ButtonMoveFront.prototype.type="draw2d.ButtonMoveFront";


draw2d.ButtonMoveFront.prototype.execute=function()
{
  this.palette.workflow.moveFront(this.palette.workflow.getCurrentSelection());
  draw2d.ToolGeneric.prototype.execute.call(this);
}
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
draw2d.FlowMenu=function(/*:draw2d.Workflow*/ workflow)
{
  this.actionDelete = new draw2d.ButtonDelete(this);
  this.actionFront = new draw2d.ButtonMoveFront(this);
  this.actionBack = new draw2d.ButtonMoveBack(this);

  draw2d.ToolPalette.call(this);

  this.setDimension(20,60);
  this.setBackgroundColor(new  draw2d.Color(220,255,255));
  this.currentFigure = null;
  this.myworkflow = workflow;
  this.added = false;
  this.setDeleteable(false);
  this.setCanDrag(false);
  this.setResizeable(false);
  this.setSelectable(false);
  this.setBackgroundColor(null);
  this.setColor(null);
  this.scrollarea.style.borderBottom="0px";

  this.actionDelete.setPosition(0,0);
  this.actionFront.setPosition(0,18);
  this.actionBack.setPosition(0,36);

  this.addChild(this.actionDelete);
//  this.addChild(this.actionFront);
//  this.addChild(this.actionBack);
}

draw2d.FlowMenu.prototype = new draw2d.ToolPalette;


/**
 * Reenable the setAlpha method. This has been disabled in the Window class.
 *
 **/
draw2d.FlowMenu.prototype.setAlpha=function(/*:float 0-1*/ percent)
{
   draw2d.Figure.prototype.setAlpha.call(this,percent);
}

/**
 * The FlowMenu has no title bar => return false.
 *
 * @returns Returns [true] if the window has a title bar
 * @type boolean
 **/
draw2d.FlowMenu.prototype.hasTitleBar=function()
{
  return false;
}

/**
 * Call back method of the framework if the selected object has been changed.
 *
 * @param {draw2d.Figure} figure the object which has been selected.
 **/
draw2d.FlowMenu.prototype.onSelectionChanged=function(/*:draw2d.Figure*/ figure)
{
  if(figure==this.currentFigure)
     return;

  if(figure instanceof draw2d.Line)
     return;

  if(this.added==true)
  {
     this.myworkflow.removeFigure(this);
     this.added=false;
  }

  if(figure!=null && this.added==false)
  {
     // The figure has been changed. Hide the FlowMenu. The addFigure(..) will increase the alpha 
     // with an internal timer. But only if the the smooth handling is enabled.
     //
     if(this.myworkflow.getEnableSmoothFigureHandling()==true)
         this.setAlpha(0.01);

     this.myworkflow.addFigure(this,100,100);
     this.added=true;
  }

  // deregister the moveListener from the old figure
  //
  if(this.currentFigure!=null)
  {
     this.currentFigure.detachMoveListener(this);
  }

  this.currentFigure = figure;
  // deregister the moveListener from the old figure
  //
  if(this.currentFigure!=null)
  {
     this.currentFigure.attachMoveListener(this);
     this.onOtherFigureMoved(this.currentFigure);
  }
}


draw2d.FlowMenu.prototype.setWorkflow= function( /*:draw2d.Workflow*/ workflow)
{
  // Call the Figure.setWorkflow(...) and NOT the ToolPalette!
  // Reson: the ToolPalette deregister the selectionListener from the workflow. But we need 
  // the selection listener event.
  draw2d.Figure.prototype.setWorkflow.call(this,workflow);
}


/**
 * Move the FlowMenu in synch with the corresponding figure.
 *
 * @param {draw2d.Figure} figure The figure which has changed its position
 * @private
 */
draw2d.FlowMenu.prototype.onOtherFigureMoved=function(/*:draw2d.Figure*/ figure)
{
   if(figure instanceof draw2d.Line)
      return;
   var pos = figure.getPosition();
   this.setPosition(pos.x+figure.getWidth()+7,pos.y-16);
}

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

draw2d.SelectUniqueKeyDialog=function(/*:String*/ name, /*:draw2d.TabelModel*/ model)
{
  draw2d.Dialog.call(this,name);

  this.model = model;
  this.setDimension(280,250);
  this.currentSelection=null;
}

draw2d.SelectUniqueKeyDialog.prototype = new draw2d.Dialog;
/** @private **/
draw2d.SelectUniqueKeyDialog.prototype.type="draw2d.OverviewWindow";


draw2d.SelectUniqueKeyDialog.prototype.createHTMLElement=function()
{
  var item = draw2d.Dialog.prototype.createHTMLElement.call(this);
  this.inputDiv = document.createElement("div");
  this.inputDiv.style.position="absolute";
  this.inputDiv.style.left = "10px";
  this.inputDiv.style.top = "20px";
  this.inputDiv.style.overflow="auto";
  this.inputDiv.style.border="1px solid black";
  this.inputDiv.style.font="normal 10px verdana";
  item.appendChild(this.inputDiv);

  item.style.backgroundColor="white";
  return item;
}

/**
 * Resize the scrolling server list if the parent window resizes.
 *
 **/ 
draw2d.SelectUniqueKeyDialog.prototype.setDimension=function(/*:int*/ w,/*:int*/ h)
{
  draw2d.Dialog.prototype.setDimension.call(this,w,h);
  if(this.inputDiv!=null)
  {
    this.inputDiv.style.height=(h-65)+"px";
    this.inputDiv.style.width=(w-20)+"px";
  }
}


/**
 * Create the figure list and add to all entries an onClick event.
 *
 **/
draw2d.SelectUniqueKeyDialog.prototype.setFocus=function()
{
  this.inputDiv.innerHTML="";
  var table = document.createElement("table");
  table.style.width="100%";
  var oThis = this;
  var uniqueKeys = dbBackend.getUniqueKeys(this.model.getName());
  for(var i=0; i<uniqueKeys.getSize();i++)
  {
    var row = table.insertRow(table.rows.length);
    var cell = row.insertCell(0);

    var key = uniqueKeys.get(i);
    var keyDiv  = document.createElement("div");
    keyDiv.id = key.getName();
    keyDiv.onclick = function()
    {
       if(oThis.currentSelection!=null)
         oThis.currentSelection.style.backgroundColor="transparent";
       this.style.backgroundColor="#90ff90";
       oThis.currentSelection = this;
    }
    keyDiv.innerHTML = key.getName();
    keyDiv.style.color = "green";
    keyDiv.style.width="100%";
    keyDiv.style.fontWeight="bold";
    var list = document.createElement("ul");
    var names = key.getColumnNames();
    for(ii=0;ii<names.getSize();ii++)
    {
      var name = names.get(ii);
      var li = document.createElement("li");
      li.innerHTML = name;
      li.style.fontWeight="normal";
      list.appendChild(li);
    }
    keyDiv.appendChild(list);
    cell.appendChild(keyDiv);
  }
  this.inputDiv.appendChild(table);
}


/**
 * This method will be called if the user pressed the OK button in buttonbar of the dialog.<br>
 * Subclasses can override this method to implement there own stuff.<br><br>
 **/
draw2d.SelectUniqueKeyDialog.prototype.onOk=function()
{
  if(this.currentSelection!=null)
  {
    var selection =this.currentSelection.id;
    var uniqueKeys = dbBackend.getUniqueKeys(this.model.getName());
    for(var i=0; i<uniqueKeys.getSize();i++)
    {
      var key = uniqueKeys.get(i);
      if(key.getName()==selection)
      {
        this.model.setUniqueKeyModel(key)
        return;
      }
    }
  }
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandAddTable=function(/*:draw2d.ConverterModel*/ converter, /*:int*/ x, /*:int*/ y)
{
   draw2d.Command.call(this,"add table model");
   this.model = new draw2d.TableModel("name");

   this.model.setName(dbBackend.getTables().get(0));

   // add the new one
   //
   var newColumns = dbBackend.getColumns(this.model.getName());
   var newCount = newColumns.getSize();
   for(var i=0;i<newCount;i++)
   {
     var column= newColumns.get(i);
     this.model.addColumnModel(column);
   }

   // add the first uniqueKey to the data model
   var newKeys = dbBackend.getUniqueKeys(this.model.getName());
   var newCount = newKeys.getSize();
   if(newCount>0)
   {
     var key= newKeys.get(0);
     this.model.setUniqueKeyModel(key);
   }

   this.converter = converter;
   this.x = x;
   this.y = y;
}

draw2d.CommandAddTable.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandAddTable.prototype.type="draw2d.CommandAddTable";



/**
 * Returns [true] if the command can be execute and the execution of the
 * command modify the model. A CommandMove with [startX,startX] == [endX,endY] should
 * return false. <br>
 * the execution of the Command doesn't modify the model.
 *
 * @type boolean
 **/
draw2d.CommandAddTable.prototype.canExecute=function()
{
  return true;
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandAddTable.prototype.execute=function()
{
   this.redo();
}

/**
 * Undo the command
 *
 **/
draw2d.CommandAddTable.prototype.undo=function()
{
	 this.converter.removeRecordSourceModel(this.model);
}

/** Redo the command after the user has undo this command
 *
 **/
draw2d.CommandAddTable.prototype.redo=function()
{
    this.converter.addRecordSourceModel(this.model);
    this.model.setPosition(this.x,this.y);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandMoveTable=function(/*:draw2d.TableModel*/ model)
{
   draw2d.Command.call(this,"move model");
   this.model = model;
   this.oldX  = model.getPosition().getX();
   this.oldY  = model.getPosition().getY();
}

draw2d.CommandMoveTable.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandMoveTable.prototype.type="draw2d.CommandMoveTable";


/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandMoveTable.prototype.setPosition=function(/*:int*/ x, /*:int*/ y)
{
   this.newX = x;
   this.newY = y;
}

/**
 * Returns [true] if the command can be execute and the execution of the
 * command modify the model. A CommandMove with [startX,startX] == [endX,endY] should
 * return false. <br>
 * the execution of the Command doesn't modify the model.
 *
 * @type boolean
 **/
draw2d.CommandMoveTable.prototype.canExecute=function()
{
  // return false if we doesn't modify the model => NOP Command
  return this.newX!=this.oldX || this.newY!=this.oldY;
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandMoveTable.prototype.execute=function()
{
   this.redo();
}

/**
 * Undo the command
 *
 **/
draw2d.CommandMoveTable.prototype.undo=function()
{
   this.model.setPosition(this.oldX, this.oldY);
}

/** Redo the command after the user has undo this command
 *
 **/
draw2d.CommandMoveTable.prototype.redo=function()
{
   this.model.setPosition(this.newX, this.newY);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandMoveExcelSheet=function(/*:draw2d.ExcelSheetModel*/ model)
{
   draw2d.Command.call(this,"move model");
   this.model = model;
   this.oldX  = model.getPosition().getX();
   this.oldY  = model.getPosition().getY();
}

draw2d.CommandMoveExcelSheet.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandMoveExcelSheet.prototype.type="draw2d.CommandMoveExcelSheet";


/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandMoveExcelSheet.prototype.setPosition=function(/*:int*/ x, /*:int*/ y)
{
   this.newX = x;
   this.newY = y;
}

/**
 * Returns [true] if the command can be execute and the execution of the
 * command modify the model. A CommandMove with [startX,startX] == [endX,endY] should
 * return false. <br>
 * the execution of the Command doesn't modify the model.
 *
 * @type boolean
 **/
draw2d.CommandMoveExcelSheet.prototype.canExecute=function()
{
  // return false if we doesn't modify the model => NOP Command
  return this.newX!=this.oldX || this.newY!=this.oldY;
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandMoveExcelSheet.prototype.execute=function()
{
   this.redo();
}

/**
 * Undo the command
 *
 **/
draw2d.CommandMoveExcelSheet.prototype.undo=function()
{
   this.model.setPosition(this.oldX, this.oldY);
}

/** Redo the command after the user has undo this command
 *
 **/
draw2d.CommandMoveExcelSheet.prototype.redo=function()
{
   this.model.setPosition(this.newX, this.newY);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandRemoveRecordSource=function(/*:draw2d.AbstractRecordSourceModel*/ recordSource)
{
   draw2d.Command.call(this,"remove record source");
   this.recordSource = recordSource;
}

draw2d.CommandRemoveRecordSource.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandRemoveRecordSource.prototype.type="draw2d.CommandRemoveRecordSource";


/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandRemoveRecordSource.prototype.execute=function()
{
   // remove all connections related to this element
   //
    var oldColumns = this.recordSource.getColumnModels().clone();
    var oldCount = oldColumns.getSize();
    for(var i=0;i<oldCount;i++)
    {
      var column= oldColumns.get(i);
      // Alle �berg�nge welche von dieser Column ausgehen l�schen
      // 
      var connections = this.recordSource.getModelParent().getConnectionModels().clone();
      var count = connections.getSize();
      for(var ii=0; ii<count;ii++)
      {
          var con = connections.get(ii);
          if(con.getSourcePortModel()==column)
            con.getModelParent().removeConnectionModel(con);
          else if(con.getTargetPortModel()==column)
            con.getModelParent().removeConnectionModel(con);
      }
    }
    this.recordSource.getModelParent().removeRecordSourceModel(this.recordSource);
}

/**
 * Redo the command after the user has undo this command.
 *
 **/
draw2d.CommandRemoveRecordSource.prototype.redo=function()
{
}

/** 
 * Undo the command.
 *
 **/
draw2d.CommandRemoveRecordSource.prototype.undo=function()
{
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandDisconnectColumn=function(/*:draw2d.ColumnConnectionModel*/ connection)
{
   draw2d.Command.call(this,"delete connection");
   this.connection = connection;
}

draw2d.CommandDisconnectColumn.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandDisconnectColumn.prototype.type="draw2d.CommandDisconnectColumn";


/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandDisconnectColumn.prototype.execute=function()
{
  var sourceTable = this.connection.getModelParent();
  sourceTable.removeConnectionModel(this.connection);
}

/**
 * Redo the command after the user has undo this command.
 *
 **/
draw2d.CommandDisconnectColumn.prototype.redo=function()
{
}

/** 
 * Undo the command.
 *
 **/
draw2d.CommandDisconnectColumn.prototype.undo=function()
{
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandReconnectColumn=function(/*:draw2d.ColumnConnectionModel*/ con)
{
   draw2d.Command.call(this,"reconnect connection");
   this.con = con;
   this.oldSourceModel = con.getSourceModel().getColumnModel(con.getSourcePortName());
   this.oldTargetModel = con.getTargetModel().getColumnModel(con.getTargetPortName());
}

draw2d.CommandReconnectColumn.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandReconnectColumn.prototype.type="draw2d.CommandReconnectColumn";

/**
 * Returns [true] if the command can be execute and the execution of the
 * command modify the model. A CommandMove with [startX,startX] == [endX,endY] should
 * return false. <br>
 * the execution of the Command doesn't modify the model.
 *
 * @type boolean
 **/
draw2d.CommandReconnectColumn.prototype.canExecute=function()
{
  // return false if we doesn't modify the model => NOP Command
  return true;
}

/**
 * called by the framework
 **/
draw2d.CommandReconnectColumn.prototype.setNewPorts=function(/*:draw2d.Port*/ source, /*:draw2d.Port*/ target)
{
  this.newSourceModel = source.getParent().getModel().getColumnModel(source.getName());
  this.newTargetModel = target.getParent().getModel().getColumnModel(target.getName());
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandReconnectColumn.prototype.execute=function()
{
   this.redo();
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandReconnectColumn.prototype.cancel=function()
{
  this.con.setSourceModel(this.oldSourceModel);
  this.con.setTargetModel(this.oldTargetModel);
}

/**
 * Undo the command
 *
 **/
draw2d.CommandReconnectColumn.prototype.undo=function()
{
  this.con.setSourceModel(this.oldSourceModel);
  this.con.setTargetModel(this.oldTargetModel);
}

/** 
 * Redo the command after the user has undo this command
 *
 **/
draw2d.CommandReconnectColumn.prototype.redo=function()
{
  this.con.setSourceModel(this.newSourceModel);
  this.con.setTargetModel(this.newTargetModel);
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandConnectColumn=function(/*:draw2d.ColumnModel*/ source, /*:draw2d.ColumnModel*/target)
{
   draw2d.Command.call(this,"create connection");
   this.source   = source;
   this.target   = target;
}

draw2d.CommandConnectColumn.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandConnectColumn.prototype.type="draw2d.CommandConnectColumn";

/**
 * Init the Command with my own implementation of a connection
 *
 **/

draw2d.CommandConnectColumn.prototype.setConnection=function(/*:draw2d.Connection*/ connection)
{
   this.connection=connection;
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandConnectColumn.prototype.execute=function()
{
  var sourceTable = this.source.getModelParent();
  var targetTable = this.target.getModelParent();
  sourceTable.addConnectionModel(new draw2d.ColumnConnectionModel(sourceTable.getId(), this.source.getName(), targetTable.getId(), this.target.getName()));
}

/**
 * Redo the command after the user has undo this command.
 *
 **/
draw2d.CommandConnectColumn.prototype.redo=function()
{
}

/** 
 * Undo the command.
 *
 **/
draw2d.CommandConnectColumn.prototype.undo=function()
{
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.CommandSelectUniqueKey=function(/*:draw2d.TableModel*/ model)
{
   draw2d.Command.call(this,"move model");
   this.model = model;
   this.currentUniqueKey = this.model.getUniqueKeyModel();
}

draw2d.CommandSelectUniqueKey.prototype = new draw2d.Command;
/** @private **/
draw2d.CommandSelectUniqueKey.prototype.type="draw2d.CommandSelectUniqueKey";



/**
 * Returns [true] if the command can be execute and the execution of the
 * command modify the model. A CommandMove with [startX,startX] == [endX,endY] should
 * return false. <br>
 * the execution of the Command doesn't modify the model.
 *
 * @type boolean
 **/
draw2d.CommandSelectUniqueKey.prototype.canExecute=function()
{
  // return false if we doesn't modify the model => NOP Command
  return true;
}

/**
 * Execute the command the first time
 * 
 **/
draw2d.CommandSelectUniqueKey.prototype.execute=function()
{
   var dialog = new draw2d.SelectUniqueKeyDialog("Slect unique Key", this.model);
   editor.getGraphicalViewer().showDialog(dialog);
   this.redo();
}

/**
 * Undo the command
 *
 **/
draw2d.CommandSelectUniqueKey.prototype.undo=function()
{
}

/** Redo the command after the user has undo this command
 *
 **/
draw2d.CommandSelectUniqueKey.prototype.redo=function()
{
}
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
draw2d.HTTPXMLDeserializer=function()
{
   alert("do not init this class. Use the static methods instead");
}



draw2d.HTTPXMLDeserializer.fromXML=function(/*:DOMNode*/ node, /*:Object*/ modelParent)
{
   var className = ""+node.attributes.getNamedItem("type").nodeValue;
   var value = node.nodeValue;
   if(value==null)
	   value=node.textContent;
   switch(className)
   {
      case "int":
        try 
        {
           return parseInt(""+node.childNodes.item(0).nodeValue);
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
      case "string":
      case "String":
        try 
        {
           if(node.childNodes.length>0)
            return ""+node.childNodes.item(0).nodeValue;
           return "";
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
      case "Number":
      case "number":
        try 
        {
           return parseFloat(""+node.childNodes.item(0).nodeValue);
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
      case "Boolean":
      case "boolean":
      case "bool":
        try 
        {
           return "true" == (""+node.childNodes.item(0).nodeValue).toLowerCase();
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
      case "dateTime":
      case "Date":
      case "date":
        try 
        {
           return new Date(""+node.childNodes.item(0).nodeValue);
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
      case "float":
        try 
        {
           return parseFloat(""+node.childNodes.item(0).nodeValue);
        }catch(e)
        {
          alert("Error:"+e+"\nDataType:"+className+"\nXML Node:"+node);
        }
        break;
   }
   var obj = eval("new "+className+"()");
   if(modelParent != undefined && obj.setModelParent!=undefined)
      obj.setModelParent(modelParent);
   var children = node.childNodes;
   var counter=0;
   for(var i=0;i<children.length;i++)
   {
      var child = children.item(i);
      var attName = child.nodeName;
      if(attName == "#text")
    	  continue;
      if(obj instanceof Array)
        attName =counter;/* parseInt(attName.replace("index",""));*/
      obj[attName] = draw2d.HTTPXMLDeserializer.fromXML(child,obj instanceof draw2d.AbstractObjectModel?obj:modelParent);
      counter++;
   }
   return obj;
}


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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 *
 **/
draw2d.MyGraphicalEditorFactory=function(/*:boolean*/ readonly)
{
   this.readonly = readonly;
   draw2d.EditPartFactory.call(this);
}

draw2d.MyGraphicalEditorFactory.prototype = new draw2d.EditPartFactory;
/** @private **/
draw2d.MyGraphicalEditorFactory.prototype.type="draw2d.MyGraphicalEditorFactory";


/**
 * Creates a new Figure given the specified model.
 * @param {draw2d.AbstractObjectModel} mode - the model of the figure being created
 *
 * @type draw2d.Figure
 **/
draw2d.MyGraphicalEditorFactory.prototype.createEditPart=function(/*:draw2d.AbstractObjectModel*/ model)
{
	try
	{
	   var figure;
	
	   if(model instanceof draw2d.TableModel)
	      figure = new draw2d.TableFigure();
	
	   else if(model instanceof draw2d.ExcelSheetModel)
	      figure = new draw2d.ExcelSheetFigure();
	
	   else if(model instanceof draw2d.ColumnConnectionModel)
	      figure = new draw2d.ColumnConnectionFigure();
	
	   if(figure==null)
	     throw "factory called with unknown model class:"+model.type;
	
	   figure.setModel(model);
	   if(this.readonly)
	   {
	      figure.setDeleteable(false);
	      figure.setCanDrag(false);
	   }
	   return figure;
	}
	catch(e)
	{
	   pushErrorStack(e,"draw2d.MyGraphicalEditorFactory.prototype.createEditPart=function(/*:draw2d.AbstractObjectModel*/ model)");
	}
}

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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.DatabaseBackend=function(/*:String*/ backendPath)
{
   if(!backendPath)
      return;
   
   this.tables = new draw2d.ArrayList();
   this.columns = [];
   this.keys = [];
   oThis = this;
   var myAjax = new Ajax.Request(backendPath,
           {
             method: 'post',
             onSuccess: function (transport)
             { 
                var stack="1";
                
                try
                {
                  var values = transport.responseText.evalJSON().tables;
                  stack="2";
                  for(var i=0;i<values.length;i++)
                  {
                    var table = values[i];
                    stack="3";
                    var columns = table.columns;
                    stack="4";
                    var keys = table.unique;
                    stack="5";
                    oThis.tables.add(table.name);
                    stack="6";
                    var c = new draw2d.ArrayList();
                    stack="7";
                    oThis.columns[table.name] = c;
                    stack="8";
                       for(var ii=0;ii<columns.length;ii++)
                       {
                          var column = columns[ii];
                          stack="9";
                          switch(column.type)
                          {
                          case "TextFieldType":
                           var length = column.length;
                              c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelText("",length)));
                           break;
                          case "LongFieldType":
                              c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelLong()));
                           break;
                          case "LongTextFieldType":
                              c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelLongText()));
                           break;
                          case "DocumentFieldType":
                              c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelDocument()));
                           break;
                          default:
                              c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelText()));
                           break;
                          }
                          stack="10";
                       }
                    var k = new draw2d.ArrayList();
                    stack="11";
                    oThis.keys[table.name] = k;
                    stack="12";
                       for(var iii=0;iii<keys.length;iii++)
                       {
                          var key = keys[iii];
                          stack="13";
                          var unique = new draw2d.UniqueKeyModel(key.name);
                          stack="14";
                         oThis.keys[table.name] = k;
                         stack="15";
                         for(var iiii=0;iiii<key.columns.length;iiii++)
                         {
                            unique.addColumnModel( key.columns[iiii].name);
                            stack="16";
                         }
                         stack="17";
                         k.add(unique);
                       }
                  }
                }
                catch(e)
                {
                   alert("draw2d.DatabaseBackend=function(/*:String*/ backendPath)["+stack+"]\n"+e);
                }
             }
           });
}

/** @private **/
draw2d.DatabaseBackend.prototype.type="draw2d.DatabaseBackend";


/**
 * @type draw2d.ArrayList
 **/
draw2d.DatabaseBackend.prototype.getTables=function()
{
  return this.tables;
}


/**
 * @abstract
 * @type draw2d.ArrayList[draw2d.ColumnModel]
 **/
draw2d.DatabaseBackend.prototype.getColumns=function(/*:String*/ tableName)
{
  return this.columns[tableName];
}


/**
 * @abstract
 * @type draw2d.ArrayList[draw2d.UniqueKeyModel]
 **/
draw2d.DatabaseBackend.prototype.getUniqueKeys=function(/*:String*/ tableName)
{
  return this.keys[tableName];
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 *
 **/
draw2d.PropertyPage=function()
{
}

/** @private **/
draw2d.PropertyPage.prototype.type="draw2d.PropertyPage";


/**
 * @param {draw2d.AbstractObjectModel} mode - the current selected model
 * @abstract
 **/
draw2d.PropertyPage.prototype.init=function(/*:draw2d.AbstractObjectModel*/ model)
{
  throw "Inherit classes must override the abstract function [PropertyPage.prototype.init]";
}

/**
 * Creates a new Figure given the specified model.
 * @final
 **/
draw2d.PropertyPage.prototype.deinit=function()
{
  throw "Inherit classes must override the abstract function [PropertyPage.prototype.deinit]";
}

/**
 * @abstract
 * @type HTMLElement
 * @private
 **/
draw2d.PropertyPage.prototype.getHTMLElement=function()
{
  throw "Inherit classes must override the abstract function [PropertyPage.prototype.getHTMLElement]";
}



/**
 * @type HTMLElement
 * @private
 **/
draw2d.PropertyPage.prototype.createInputElement=function(/*:int*/x, /*:int*/ y)
{
  var element = document.createElement("input");
  element.type="text";
  element.style.width="260px";
  element.style.left = x+"px";
  element.style.top  = y+"px";
  element.style.font="normal 11px verdana";
  element.style.paddingLeft="5px";
  element.style.position ="absolute";

  return element;
}

/**
 * @type HTMLElement
 * @private
 **/
draw2d.PropertyPage.prototype.createLabelElement=function(/*:String*/ text,/*:int*/ x,/*:int*/ y)
{
  var element = document.createElement("div");
  element.style.left = x+"px";
  element.style.top  = y+"px";
  element.style.font="normal 11px verdana";
  element.style.position ="absolute";
  element.innerHTML=text;
  return element;
}

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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.DefaultPropertyPage=function()
{
   draw2d.PropertyPage.call(this);
   this.html = document.createElement("div");
   this.html.style.backgroundColor="white";
   this.html.style.width="100%";
   this.html.style.height="100%";
   this.html.style.background="#FAFAFF url("+draw2d.Configuration.IMAGEPATH+"header_model.png) repeat-x";

   this.header = this.createLabelElement("Common Properties",25,5);
   this.html.appendChild(this.header);

   this.modelLabel = this.createLabelElement("Model Name",10,45);
   this.modelLabel.style.color="gray";
   this.html.appendChild(this.modelLabel);
   
   this.nameText = document.createElement("input");
   this.nameText.type = "text";
   var oThis = this;
   if(editor.isReadonly())
   {
      this.nameText.disabled ="true";
   }
   else
   {
      Event.observe(this.nameText,"keyup",function(e)
             {
               oThis.currentModel.setName(oThis.nameText.value);
             });
   }
   this.nameText.style.position="absolute";
   this.nameText.style.width="110px";
   this.nameText.style.top="65px";
   this.nameText.style.left="10px";
 
   this.html.appendChild(this.nameText);
}

draw2d.DefaultPropertyPage.prototype = new draw2d.PropertyPage;
/** @private **/
draw2d.DefaultPropertyPage.prototype.type="draw2d.DefaultPropertyPage";


/**
 *
 **/
draw2d.DefaultPropertyPage.prototype.init=function(/*:draw2d.AbstractObjectModel*/ model)
{
   this.currentModel = model;
   this.nameText.value = model.getName();
}

/**
 *
 **/
draw2d.DefaultPropertyPage.prototype.deinit=function()
{
}

/**
 * @abstract
 * @type HTMLElement
 * @private
 **/
draw2d.DefaultPropertyPage.prototype.getHTMLElement=function()
{
  return this.html;
}



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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.ExcelPropertyPage=function()
{
   draw2d.PropertyPage.call(this);
   
   this.html = document.createElement("div");
   this.html.style.width="100%";
   this.html.style.height="100%";
   this.html.style.background="#FAFAFF url("+draw2d.Configuration.IMAGEPATH+"header_excel.png) repeat-x";
  
   this.header = this.createLabelElement("Excel Properties",25,5);
   this.html.appendChild(this.header);
   
   // DATA ROW 
   //
   this.datarowLabel = this.createLabelElement("First Data Row",10,45);
   this.datarowLabel.style.color="gray";
   this.html.appendChild(this.datarowLabel);

   this.listboxDatarow = document.createElement("select");
   this.listboxDatarow.style.position="absolute";
   this.listboxDatarow.style.overflow="auto";
   this.listboxDatarow.style.width="110px";
   this.listboxDatarow.style.top="65px";
   this.listboxDatarow.style.left="10px";
   this.listboxDatarow.size=1;
   var oThis = this;
   this.listboxDatarow['onchange']=function()
   {
      oThis.currentModel.setFirstDataRow(oThis.listboxDatarow.selectedIndex);
   }
   for(var i=1;i<10;i++)
   {
      var node = document.createElement("option");
      node.value = ""+i;
      node.appendChild(document.createTextNode(""+i));
      this.listboxDatarow.appendChild(node);
   }
   this.html.appendChild(this.listboxDatarow);
   
   // COLUMN COUNT
   //
   this.columncountLabel = this.createLabelElement("Column Count",10,95);
   this.columncountLabel.style.color="gray";
   this.html.appendChild(this.columncountLabel);

   this.listboxColumncount = document.createElement("select");
   this.listboxColumncount.style.position="absolute";
   this.listboxColumncount.style.overflow="auto";
   this.listboxColumncount.style.width="110px";
   this.listboxColumncount.style.top="115px";
   this.listboxColumncount.style.left="10px";
   this.listboxColumncount.size=1;
   this.listboxColumncount['onchange']=function()
   {
      oThis.currentModel.setColumnCount(oThis.listboxColumncount.selectedIndex+1);
   }
   for(var i=1;i<40;i++)
   {
      var node = document.createElement("option");
      node.value = ""+i;
      node.appendChild(document.createTextNode(""+i+"   ("+toExcelColumn(i-1)+")"));
      this.listboxColumncount.appendChild(node);
   }
   this.html.appendChild(this.listboxColumncount);
}

draw2d.ExcelPropertyPage.prototype = new draw2d.PropertyPage;
/** @private **/
draw2d.ExcelPropertyPage.prototype.type="draw2d.ExcelPropertyPage";


/**
 *
 **/
draw2d.ExcelPropertyPage.prototype.init=function(/*:draw2d.AbstractObjectModel*/ model)
{
   if(editor.isReadonly())
   {
      this.listboxDatarow.disabled="true";
      this.listboxColumncount.disabled="true";
   }
   this.listboxDatarow.selectedIndex = model.getFirstDataRow();
   this.listboxColumncount.selectedIndex = model.getColumnModels().getSize()-1;
   
   this.currentModel = model;
}

/**
 *
 **/
draw2d.ExcelPropertyPage.prototype.deinit=function()
{
}

/**
 * @abstract
 * @type HTMLElement
 * @private
 **/
draw2d.ExcelPropertyPage.prototype.getHTMLElement=function()
{
  return this.html;
}/* This notice must be untouched at all times.

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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.ConnectionPropertyPage=function()
{
   var stack = "0";
   try
   {
      this.listbox = null;
      stack="1";
      draw2d.PropertyPage.call(this);
      stack="2";
      this.html = document.createElement("div");
      stack="3";
      this.html.style.width="100%";
      stack="4";
      this.html.style.height="100%";
      stack="5";
      this.html.style.background="#FAFAFF url("+draw2d.Configuration.IMAGEPATH+"header_connection.png) repeat-x";
      stack="6";
      this.html.style.whiteSpace="nowrap";
      stack="7";
   
      this.header = this.createLabelElement("Connection Properties",25,5);
      stack="8";
      this.html.appendChild(this.header);
      stack="9";
      
      this.comboLabel = this.createLabelElement("Transfer value",10,45);
      stack="10";
      this.comboLabel.style.color="gray";
      stack="11";
      this.html.appendChild(this.comboLabel);
      stack="12";
   
      this.currentModel = null;
      stack="13";
   }
   catch(e)
   {
      pushErrorStack(e,"draw2d.ConnectionPropertyPage=function()["+stack+"]");
   }
}

draw2d.ConnectionPropertyPage.prototype = new draw2d.PropertyPage;
/** @private **/
draw2d.ConnectionPropertyPage.prototype.type="draw2d.ConnectionPropertyPage";


/**
 *
 **/
draw2d.ConnectionPropertyPage.prototype.init=function(/*:draw2d.AbstractObjectModel*/ model)
{
   this.currentModel = model;

   // adding a comboxbox for the table selection
   //
   this.listbox = document.createElement("select");
   this.listbox.style.position="absolute";
   this.listbox.style.overflow="auto";
   this.listbox.style.top="65px";
   this.listbox.style.left="10px";
   this.listbox.size=1;
   var oThis = this;
   this.listbox['onchange']=function(){oThis._onChange()};
   
   // TRANSFER_MODE_ALWAYS
   var node = document.createElement("option");
   node.value = draw2d.ColumnConnectionModel.TRANSFER_MODE_ALWAYS;
   node.appendChild(document.createTextNode(node.value));
   this.listbox.appendChild(node);
   
   // TRANSFER_MODE_TARGET_NULL
   var node = document.createElement("option");
   node.value = draw2d.ColumnConnectionModel.TRANSFER_MODE_TARGET_NULL;
   node.appendChild(document.createTextNode(node.value));
   this.listbox.appendChild(node);

   // TRANSFER_MODE_SOURCE_NOT_NULL
   var node = document.createElement("option");
   node.value = draw2d.ColumnConnectionModel.TRANSFER_MODE_SOURCE_NOT_NULL;
   node.appendChild(document.createTextNode(node.value));
   this.listbox.appendChild(node);
   switch(this.currentModel.getTransferMode())
   {
      case draw2d.ColumnConnectionModel.TRANSFER_MODE_TARGET_NULL:
         this.listbox.selectedIndex = 1;
         break;
      case draw2d.ColumnConnectionModel.TRANSFER_MODE_SOURCE_NOT_NULL:
         this.listbox.selectedIndex = 2;
         break;
      default:
         this.listbox.selectedIndex = 0;
   }
   
   this.html.appendChild(this.listbox);

   if(editor.isReadonly())
   {
      this.listbox.disabled="true";
   }
}

/**
 *
 **/
draw2d.ConnectionPropertyPage.prototype.deinit=function()
{
   this.currentModel = null;
   if(this.listbox !=null)
	   this.html.removeChild(this.listbox);
   this.listbox = null;
   if(this.createListbox !=null)
      this.html.removeChild(this.createListbox);
   this.createListbox=null;
}

/**
 * @abstract
 * @type HTMLElement
 * @private
 **/
draw2d.ConnectionPropertyPage.prototype.getHTMLElement=function()
{
  return this.html;
}

/**
 *
 **/
draw2d.ConnectionPropertyPage.prototype._onChange=function()
{
  switch(this.listbox.selectedIndex)
  {
     case 1:
        this.currentModel.setTransferMode(draw2d.ColumnConnectionModel.TRANSFER_MODE_TARGET_NULL);
        break;
     case 2:
        this.currentModel.setTransferMode(draw2d.ColumnConnectionModel.TRANSFER_MODE_SOURCE_NOT_NULL);
        break;
     default:
        this.currentModel.setTransferMode(draw2d.ColumnConnectionModel.TRANSFER_MODE_ALWAYS);
  }
}
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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.TablePropertyPage=function()
{
   this.listbox = null;
   draw2d.PropertyPage.call(this);
   this.html = document.createElement("div");
   this.html.style.width="100%";
   this.html.style.height="100%";
   this.html.style.background="#FAFAFF url("+draw2d.Configuration.IMAGEPATH+"header_table.png) repeat-x";

   this.header = this.createLabelElement("Table Properties",25,5);
   this.html.appendChild(this.header);
   
   this.comboLabel = this.createLabelElement("Table Name",10,45);
   this.comboLabel.style.color="gray";
   this.html.appendChild(this.comboLabel);

   this.createLabel = this.createLabelElement("Create if not found",10,105);
   this.createLabel.style.color="gray";
   this.html.appendChild(this.createLabel);


   this.currentModel = null;
}

draw2d.TablePropertyPage.prototype = new draw2d.PropertyPage;
/** @private **/
draw2d.TablePropertyPage.prototype.type="draw2d.TablePropertyPage";


/**
 *
 **/
draw2d.TablePropertyPage.prototype.init=function(/*:draw2d.AbstractObjectModel*/ model)
{
   // adding a comboxbox for the table selection
   //
   this.listbox = document.createElement("select");
   this.listbox.style.position="absolute";
   this.listbox.style.overflow="auto";
   this.listbox.style.top="65px";
   this.listbox.style.left="10px";
   this.listbox.size=1;
   var oThis = this;
   this.listbox['onchange']=function(){oThis._onChange()};
   var values = dbBackend.getTables();
   for(var i=0;i<values.getSize();i++)
   {
      var value = values.get(i);
      var node = document.createElement("option");
      node.value = value;
      node.appendChild(document.createTextNode(value));
      this.listbox.appendChild(node);
   }
   this.html.appendChild(this.listbox);

   this.createListbox = document.createElement("select");
   this.createListbox.style.position="absolute";
   this.createListbox.style.overflow="auto";
   this.createListbox.style.top="125px";
   this.createListbox.style.left="10px";
   this.createListbox.size=1;
   var oThis = this;
   this.createListbox['onchange']=function()
   {
      if(oThis.createListbox.selectedIndex==0)
         oThis.currentModel.setCreateIfNotExists(true);
      else
         oThis.currentModel.setCreateIfNotExists(false);
   };
      var node = document.createElement("option");
      node.value = "yes";
      node.appendChild(document.createTextNode("yes"));
      this.createListbox.appendChild(node);
      
      var node = document.createElement("option");
      node.value = "no";
      node.appendChild(document.createTextNode("no"));
      this.createListbox.appendChild(node);
   this.html.appendChild(this.createListbox);
   if(model.getCreateIfNotExists())
      this.createListbox.selectedIndex = 0;
   else
      this.createListbox.selectedIndex = 1;
   
   var name = model.getName();
   var index = dbBackend.getTables().indexOf(name);
   this.listbox.selectedIndex = index;
   this.currentModel = model;
   
   if(editor.isReadonly())
   {
      this.listbox.disabled="true";
      this.createListbox.disabled="true";
   }
}

/**
 *
 **/
draw2d.TablePropertyPage.prototype.deinit=function()
{
   this.currentModel = null;
   if(this.listbox !=null)
	   this.html.removeChild(this.listbox);
   this.listbox = null;
   if(this.createListbox !=null)
      this.html.removeChild(this.createListbox);
   this.createListbox=null;
}

/**
 * @abstract
 * @type HTMLElement
 * @private
 **/
draw2d.TablePropertyPage.prototype.getHTMLElement=function()
{
  return this.html;
}

/**
 *
 **/
draw2d.TablePropertyPage.prototype._onChange=function()
{
  if(this.currentModel!=null)
  {
    // remove the old uniqueKeys
    //
    this.currentModel.setUniqueKeyModel(null);

    // remove the old columns
    //
    var oldColumns = this.currentModel.getColumnModels().clone();
    var oldCount = oldColumns.getSize();
    for(var i=0;i<oldCount;i++)
    {
      var column= oldColumns.get(i);
      if(!column.isRelevanceIndicator())
      {
        this.currentModel.removeColumnModel(column);
      }
    }

    this.currentModel.setName(this.listbox.value);

    // add the new one
    //
    var newColumns = dbBackend.getColumns(this.listbox.value);
    var newCount = newColumns.getSize();
    for(var i=0;i<newCount;i++)
    {
      var column= newColumns.get(i);
      this.currentModel.addColumnModel(column);
    }

    // add the first uniqueKey to the data model
    var newKeys = dbBackend.getUniqueKeys(this.listbox.value);
    var newCount = newKeys.getSize();
    if(newCount>0)
    {
      var key= newKeys.get(0);
      this.currentModel.setUniqueKeyModel(key);
    }
  }
}
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

/**
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 *
 **/
draw2d.PropertyPanel=function(/*:String*/ contentDivId)
{
   var stack="0";
   try
   {
     if(!contentDivId)
       return;
     stack="1";
     this.html = $(contentDivId);
     stack="2";
     this.pages = [];
     stack="3";
     this.currentPage=null;
     stack="4";
     this.pages[draw2d.TableModel.prototype.type]= new draw2d.TablePropertyPage();
     stack="5";
     this.pages[draw2d.ExcelSheetModel.prototype.type]= new draw2d.ExcelPropertyPage();
     stack="6";
     this.pages[draw2d.ColumnConnectionModel.prototype.type]= new draw2d.ConnectionPropertyPage();
     stack="7";
     this.defaultPage = new draw2d.DefaultPropertyPage();
     stack="8";
     this.onSelectionChanged(null,null);
  }
  catch(e)
  {
     pushErrorStack(e,"draw2d.PropertyPanel=function(/*:String*/ contentDivId)["+stack+"]");
  }
}

/** @private **/
draw2d.PropertyPanel.prototype.type="draw2d.PropertyPanel";


/**
 *
 **/
draw2d.PropertyPanel.prototype.onSelectionChanged=function(/*:draw2d.Figure*/ figure, /*:draw2d.AbstractObjectModel*/ model)
{
   try
   {
	   if(this.currentPage!=null)
	   {
	      this.currentPage.deinit();
	      this.html.removeChild(this.currentPage.getHTMLElement());
	      this.currentPage = null;
	   }
	
	   this.html.innerHTML="";
	   if(model!=null)
	   {
	      var page = this.pages[model.type];
	      if(page)
	      {
	        this.html.appendChild(page.getHTMLElement());
	        page.init(model);
	        this.currentPage = page;
	      }
	   }
	   else
	   {
	      this.html.appendChild(this.defaultPage.getHTMLElement());
	      this.defaultPage.init(editor.getModel());
	      this.currentPage = this.defaultPage;
	   }
   }
   catch(e)
   {
	   alert(e);
   }
}
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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.MyGraphicalEditor=function(/*:String*/ id, /*:boolean*/ readonly)
{
   draw2d.GraphicalEditor.call(this,id);
   this.readonly = readonly;
}

draw2d.MyGraphicalEditor.prototype = new draw2d.GraphicalEditor;
/** @private **/
draw2d.MyGraphicalEditor.prototype.type="draw2d.MyGraphicalEditor";


/**
 * Called to configure the graphical viewer before it receives its contents. 
 * Subclasses should extend or override this method as needed.
 **/
draw2d.MyGraphicalEditor.prototype.setModel=function(/*:draw2d.AbstractObjectModel*/ model)
{
	try
	{
	   this.model = model;
	   // assign the model to the view
	   this.getGraphicalViewer().setModel(this.model);
	   // ...and the factory for the editparts/figures
	   this.getGraphicalViewer().setEditPartFactory(new draw2d.MyGraphicalEditorFactory(this.readonly));
	}
	catch(e)
	{
	   pushErrorStack(e,"draw2d.MyGraphicalEditor.prototype.setModel=function()");
	}
}

/**
 *  Return true if the editor is in readonly mode.
 *  @type boolean
 */
draw2d.MyGraphicalEditor.prototype.isReadonly=function()
{
   return this.readonly;
}


draw2d.MyGraphicalEditor.prototype.getModel=function()
{
   return this.model;
}
/**
* Converts column number to Excel column characters name, e.g.: 43 => AQ
*/
function toExcelColumn( /*:int*/ columnNumber)
{
   columnNumber++;
   var sChars = ["0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
   var sCol = "";
   while (columnNumber > 26)
   {
       var nChar = columnNumber % 26;
       if (nChar == 0)
           nChar = 26;
       columnNumber = (columnNumber - nChar) / 26;
       sCol = sChars[nChar] + sCol;
   }
   if (columnNumber != 0)
       sCol = sChars[columnNumber] + sCol;
   return sCol;
}

var stack="";
function showXML(/*:AbstractObjectModel*/ model)
{
   stack="1";
   editor  = new draw2d.MyGraphicalEditor("paintarea",true);
   stack="2";
   editor.setModel(model);
   stack="3";
   editor.getGraphicalViewer().setViewPort("scrollarea");
   stack="4";
   editor.getGraphicalViewer().setBackgroundImage(draw2d.Configuration.IMAGEPATH+"grid_10.png",true);
   stack="5";
   editor.getGraphicalViewer().setCurrentSelection(null);
   stack="6";
   var panel = new draw2d.PropertyPanel("property_panel");
   stack="7";
   // register the editor as listener to the root model.
   editor.getGraphicalViewer().addSelectionListener(panel);
   stack="8";
}

function editXML(/*:AbstractObjectModel*/ model)
{
   editor  = new draw2d.MyGraphicalEditor("paintarea",false);
   editor.setModel(model);
   editor.getGraphicalViewer().setViewPort("scrollarea");
   editor.getGraphicalViewer().setBackgroundImage(draw2d.Configuration.IMAGEPATH+"grid_10.png",true);
   editor.getGraphicalViewer().setCurrentSelection(null);
   var palette = new draw2d.ExternalPalette(editor.getGraphicalViewer(),"object_panel");
   var part1 = new draw2d.TablePalettePart(model);
   
   editor.getGraphicalViewer().addSelectionListener(new draw2d.FlowMenu(editor.getGraphicalViewer()));
   editor.getGraphicalViewer().addSelectionListener(new draw2d.PropertyPanel("property_panel"));
//   editor.getGraphicalViewer().setEnableSmoothFigureHandling(true);

   palette.addPalettePart(part1);
   part1.setPosition(10,10);

   // Wrap the SAVE-Button with a button which post the content to the server
   var saveButton = $$(".draw2d_toolbar p a")[0];
   var event = saveButton.href.replace("javascript:","");
   saveButton.href="#";
   saveButton.observe("click",function(){
      var myAjax = new Ajax.Request(draw2d.Configuration.APP_PATH+"backend/jacob/updateActiveDocument.jsp",
            {
              method: 'post',
              parameters:
              {
                browser: browserId,
                xml : draw2d.XMLSerializer.toXML(editor.getModel())
              },
              onSuccess: function (transport)
              {
                 eval(event);
              }
            });
   });
}



FakeWindow=function(/*:String*/ mode)
{
   this.mode = mode;
}

/**
 *
 **/
FakeWindow.prototype.show=function()
{
  var oThis = this;
  new Ajax.Request(draw2d.Configuration.APP_PATH+"backend/jacob/getActiveDocument.jsp",
  {
    method: 'post',
    parameters:
    {
      browser: browserId
    },
    onComplete: function()
    {
    },
    onSuccess: function (transport)
    { 
	  try
	  {
		  var model = draw2d.HTTPXMLDeserializer.fromXML(transport.responseXML.firstChild);
		  if(oThis.mode=="show")
		     showXML(model);
		  else
		     editXML(model);
	  }
	  catch(e)
	  {
		  alert(oThis.mode+" document\n"+e+"\n"+_errorStack_+"\n"+stack);
	  }
    }
  });
}/* This notice must be untouched at all times.

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

/**
 * A factory for creating new EditParts. EditPartViewers can be configured with an EditPartFactory.
 * Whenever an EditPart in that viewer needs to create another EditPart, it can use the Viewer's factory.
 * The factory is also used by the viewer whenever EditPartViewer.setContents(Object)  is called.
 * 
 * @version 0.9.18
 * @author Andreas Herz
 * @constructor
 */
draw2d.JacobDatabaseBackend=function()
{
   draw2d.DatabaseBackend.call(this);
   this.tables = new draw2d.ArrayList();
   this.columns = [];
   this.keys = [];
   oThis = this;
   var myAjax = new Ajax.Request("application/@APPLICATION@/@VERSION@/{modulename}/backend/jacob/getTables.jsp",
			  {
			    method: 'post',
			    parameters:
			    {
			      browser: browserId
			    },
			    onSuccess: function (transport)
			    { 
			      var values = transport.responseText.evalJSON().tables;
			      for(var i=0;i<values.length;i++)
			      {
			        var table = values[i];
			        var columns = table.columns;
			        var keys = table.unique;
			        oThis.tables.add(table.name);
			        var c = new draw2d.ArrayList();
			        oThis.columns[table.name] = c;
                    for(var ii=0;ii<columns.length;ii++)
                    {
                       var column = columns[ii];
                       switch(column.type)
                       {
                       case "TextFieldType":
                    	   var length = column.length;
                           c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelText("",length)));
                    	   break;
                       case "LongFieldType":
                           c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelLong()));
                    	   break;
                       case "LongTextFieldType":
                           c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelLongText()));
                    	   break;
                       case "DocumentFieldType":
                           c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelDocument()));
                    	   break;
                       case "TimestampFieldType":
                          c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelTimestamp()));
                       break;
                       case "DecimalFieldType":
                          c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelDecimal()));
                       break;
                    	 default:
                           c.add(new draw2d.ColumnModel(column.name, new draw2d.ColumnTypeModelText("",40)));
                    	   break;
                       }
                    }
			        var k = new draw2d.ArrayList();
			        oThis.keys[table.name] = k;
                    for(var iii=0;iii<keys.length;iii++)
                    {
                       var key = keys[iii];
                       var unique = new draw2d.UniqueKeyModel(key.name);
	   			       oThis.keys[table.name] = k;
	                   for(var iiii=0;iiii<key.columns.length;iiii++)
	                   {
	                      unique.addColumnModel( key.columns[iiii].name);
	                   }
                       k.add(unique);
                    }
                    
			      }
			    }
			  });
}

draw2d.JacobDatabaseBackend.prototype = new draw2d.DatabaseBackend;
/** @private **/
draw2d.JacobDatabaseBackend.prototype.type="draw2d.JacobDatabaseBackend";


/**
 * @type draw2d.ArrayList
 **/
draw2d.JacobDatabaseBackend.prototype.getTables=function()
{
  return this.tables;
}


/**
 * @abstract
 * @type draw2d.ArrayList[draw2d.ColumnModel]
 **/
draw2d.JacobDatabaseBackend.prototype.getColumns=function(/*:String*/ tableName)
{
  return this.columns[tableName];
}


/**
 * @abstract
 * @type draw2d.ArrayList[draw2d.UniqueKeyModel]
 **/
draw2d.JacobDatabaseBackend.prototype.getUniqueKeys=function(/*:String*/ tableName)
{
  return this.keys[tableName];
}


