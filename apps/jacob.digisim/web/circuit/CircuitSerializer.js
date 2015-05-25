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
function CircuitSerializer()
{
}

CircuitSerializer.prototype = new Object;
CircuitSerializer.prototype.type="CircuitSerializer";

CircuitSerializer.prototype.toXML=function(workflow /*:Workflow*/)
{
  var xml = '<?xml version="1.0" encoding="ISO-8859-1"?>\n';

  xml = xml+'<circuit xmlns="http://www.example.org/circuit" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="circuit">\n';
  var doc = workflow.getDocument();
  var figures = doc.getFigures();
  for(var i=0;i<figures.length;i++)
  {
    var figure = figures[i];
	xml = xml + '<part id="'+figure.id+'"  x="'+figure.getX()+'"  y="'+figure.getY()+'"  type="'+figure.type+'" />\n'
  }

  var lines = doc.getLines();
  for(var i=0;i<lines.length;i++)
  {
    var line = lines[i];
	  var source = line.getSource();
	  var target = line.getTarget();
	  xml = xml + '<connection sourcePartId="'+source.getParent().id+'"  sourcePortId="'+source.getName()+'" targetPartId="'+target.getParent().id+'" targetPortId="'+target.getName()+'" />\n'
  }

  xml = xml +'</circuit>\n'
  return xml;
}
