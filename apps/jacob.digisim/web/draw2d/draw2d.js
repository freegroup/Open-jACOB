
/**This notice must be untouched at all times.
This is the COMPRESSED version of Open-jACOB Draw2D
WebSite: http://www.openjacob.org
Copyright: 2006 Andreas Herz. All rights reserved.
Created: 5.11.2006 by Andreas Herz (Web: http://www.freegroup.de )
LICENSE: LGPL
**/
Event=function(){
this.type=null;
this.target=null;
this.relatedTarget=null;
this.cancelable=false;
this.timeStamp=null;
this.returnValue=true;
};
Event.prototype.initEvent=function(sType,_3455){
this.type=sType;
this.cancelable=_3455;
this.timeStamp=(new Date()).getTime();
};
Event.prototype.preventDefault=function(){
if(this.cancelable){
this.returnValue=false;
}
};
Event.fireDOMEvent=function(_3456,_3457){
if(document.createEvent){
var evt=document.createEvent("Events");
evt.initEvent(_3456,true,true);
_3457.dispatchEvent(evt);
}else{
if(document.createEventObject){
var evt=document.createEventObject();
_3457.fireEvent("on"+_3456,evt);
}
}
};
EventTarget=function(){
this.eventhandlers=new Object();
};
EventTarget.prototype.addEventListener=function(sType,_345a){
if(typeof this.eventhandlers[sType]=="undefined"){
this.eventhandlers[sType]=new Array;
}
this.eventhandlers[sType][this.eventhandlers[sType].length]=_345a;
};
EventTarget.prototype.dispatchEvent=function(_345b){
_345b.target=this;
if(typeof this.eventhandlers[_345b.type]!="undefined"){
for(var i=0;i<this.eventhandlers[_345b.type].length;i++){
this.eventhandlers[_345b.type][i](_345b);
}
}
return _345b.returnValue;
};
EventTarget.prototype.removeEventListener=function(sType,_345e){
if(typeof this.eventhandlers[sType]!="undefined"){
var _345f=new Array;
for(var i=0;i<this.eventhandlers[sType].length;i++){
if(this.eventhandlers[sType][i]!=_345e){
_345f[_345f.length]=this.eventhandlers[sType][i];
}
}
this.eventhandlers[sType]=_345f;
}
};
ArrayList=function(){
this.increment=10;
this.size=0;
this.data=new Array(this.increment);
};
ArrayList.EMPTY_LIST=new ArrayList();
ArrayList.prototype.reverse=function(){
var _37f5=new Array(this.size);
for(var i=0;i<this.size;i++){
_37f5[i]=this.data[this.size-i-1];
}
this.data=_37f5;
};
ArrayList.prototype.getCapacity=function(){
return this.data.length;
};
ArrayList.prototype.getSize=function(){
return this.size;
};
ArrayList.prototype.isEmpty=function(){
return this.getSize()==0;
};
ArrayList.prototype.getLastElement=function(){
if(this.data[this.getSize()-1]!=null){
return this.data[this.getSize()-1];
}
};
ArrayList.prototype.getFirstElement=function(){
if(this.data[0]!=null){
return this.data[0];
}
};
ArrayList.prototype.get=function(i){
return this.data[i];
};
ArrayList.prototype.add=function(obj){
if(this.getSize()==this.data.length){
this.resize();
}
this.data[this.size++]=obj;
};
ArrayList.prototype.remove=function(obj){
var index=this.indexOf(obj);
if(index>=0){
return this.removeElementAt(index);
}
return null;
};
ArrayList.prototype.insertElementAt=function(obj,index){
if(this.size==this.capacity){
this.resize();
}
for(var i=this.getSize();i>index;i--){
this.data[i]=this.data[i-1];
}
this.data[index]=obj;
this.size++;
};
ArrayList.prototype.removeElementAt=function(index){
var _37ff=this.data[index];
for(var i=index;i<(this.getSize()-1);i++){
this.data[i]=this.data[i+1];
}
this.data[this.getSize()-1]=null;
this.size--;
return _37ff;
};
ArrayList.prototype.removeAllElements=function(){
this.size=0;
for(var i=0;i<this.data.length;i++){
this.data[i]=null;
}
};
ArrayList.prototype.indexOf=function(obj){
for(var i=0;i<this.getSize();i++){
if(this.data[i]==obj){
return i;
}
}
return -1;
};
ArrayList.prototype.contains=function(obj){
for(var i=0;i<this.getSize();i++){
if(this.data[i]==obj){
return true;
}
}
return false;
};
ArrayList.prototype.resize=function(){
newData=new Array(this.data.length+this.increment);
for(var i=0;i<this.data.length;i++){
newData[i]=this.data[i];
}
this.data=newData;
};
ArrayList.prototype.trimToSize=function(){
var temp=new Array(this.getSize());
for(var i=0;i<this.getSize();i++){
temp[i]=this.data[i];
}
this.size=temp.length-1;
this.data=temp;
};
ArrayList.prototype.sort=function(f){
var i,j;
var _380b;
var _380c;
var _380d;
var _380e;
for(i=1;i<this.getSize();i++){
_380c=this.data[i];
_380b=_380c[f];
j=i-1;
_380d=this.data[j];
_380e=_380d[f];
while(j>=0&&_380e>_380b){
this.data[j+1]=this.data[j];
j--;
if(j>=0){
_380d=this.data[j];
_380e=_380d[f];
}
}
this.data[j+1]=_380c;
}
};
ArrayList.prototype.clone=function(){
var _380f=new ArrayList(this.size);
for(var i=0;i<this.size;i++){
_380f.add(this.data[i]);
}
return _380f;
};
ArrayList.prototype.overwriteElementAt=function(obj,index){
this.data[index]=obj;
};
function trace(_37ec){
var _37ed=openwindow("about:blank",700,400);
_37ed.document.writeln("<pre>"+_37ec+"</pre>");
}
function openwindow(url,width,_37f0){
var left=(screen.width-width)/2;
var top=(screen.height-_37f0)/2;
property="left="+left+", top="+top+", toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width="+width+",height="+_37f0;
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
Drag=function(){
};
Drag.current=null;
Drag.currentTarget=null;
Drag.dragging=false;
Drag.isDragging=function(){
return this.dragging;
};
Drag.setCurrent=function(_2cd6){
this.current=_2cd6;
this.dragging=true;
};
Drag.getCurrent=function(){
return this.current;
};
Drag.clearCurrent=function(){
this.current=null;
this.dragging=false;
};
Draggable=function(_2cd7,_2cd8){
EventTarget.call(this);
this.construct(_2cd7,_2cd8);
this.diffX=0;
this.diffY=0;
this.targets=new ArrayList();
};
Draggable.prototype=new EventTarget;
Draggable.prototype.addDropTarget=function(_2cd9){
this.targets.add(_2cd9);
};
Draggable.prototype.construct=function(_2cda,_2cdb){
this.element=_2cda;
this.constraints=_2cdb;
var oThis=this;
var _2cdd=function(){
var _2cde=new DragDropEvent();
_2cde.initDragDropEvent("dblclick",true);
oThis.dispatchEvent(_2cde);
var _2cdf=arguments[0]||window.event;
_2cdf.cancelBubble=true;
_2cdf.returnValue=false;
};
var _2ce0=function(){
var _2ce1=arguments[0]||window.event;
var _2ce2=new DragDropEvent();
var _2ce3=oThis.node.workflow.getAbsoluteX();
var _2ce4=oThis.node.workflow.getAbsoluteY();
var _2ce5=oThis.node.workflow.getScrollLeft();
var _2ce6=oThis.node.workflow.getScrollTop();
_2ce2.x=_2ce1.clientX-oThis.element.offsetLeft+_2ce5-_2ce3;
_2ce2.y=_2ce1.clientY-oThis.element.offsetTop+_2ce6-_2ce4;
if(_2ce1.button==2){
_2ce2.initDragDropEvent("contextmenu",true);
oThis.dispatchEvent(_2ce2);
}else{
_2ce2.initDragDropEvent("dragstart",true);
if(oThis.dispatchEvent(_2ce2)){
oThis.diffX=_2ce1.clientX-oThis.element.offsetLeft;
oThis.diffY=_2ce1.clientY-oThis.element.offsetTop;
Drag.setCurrent(oThis);
if(oThis.isAttached==true){
oThis.detachEventHandlers();
}
oThis.attachEventHandlers();
}
}
_2ce1.cancelBubble=true;
_2ce1.returnValue=false;
};
var _2ce7=function(){
if(Drag.getCurrent()==null){
var _2ce8=arguments[0]||window.event;
if(Drag.currentHover!=null&&oThis!=Drag.currentHover){
var _2ce9=new DragDropEvent();
_2ce9.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_2ce9);
}
if(oThis!=null&&oThis!=Drag.currentHover){
var _2ce9=new DragDropEvent();
_2ce9.initDragDropEvent("mouseenter",false,oThis);
oThis.dispatchEvent(_2ce9);
}
Drag.currentHover=oThis;
}else{
}
};
if(this.element.addEventListener){
this.element.addEventListener("mousemove",_2ce7,false);
this.element.addEventListener("mousedown",_2ce0,false);
this.element.addEventListener("dblclick",_2cdd,false);
}else{
if(this.element.attachEvent){
this.element.attachEvent("onmousemove",_2ce7);
this.element.attachEvent("onmousedown",_2ce0);
this.element.attachEvent("ondblclick",_2cdd);
}else{
throw new Error("Drag not supported in this browser.");
}
}
};
Draggable.prototype.attachEventHandlers=function(){
var oThis=this;
oThis.isAttached=true;
this.tempMouseMove=function(){
var _2ceb=arguments[0]||window.event;
var _2cec=new Point(_2ceb.clientX-oThis.diffX,_2ceb.clientY-oThis.diffY);
if(oThis.node.getCanSnapToHelper()){
_2cec=oThis.node.getWorkflow().snapToHelper(oThis.node,_2cec);
}
oThis.element.style.left=_2cec.x+"px";
oThis.element.style.top=_2cec.y+"px";
var _2ced=oThis.node.workflow.getScrollLeft();
var _2cee=oThis.node.workflow.getScrollTop();
var _2cef=oThis.node.workflow.getAbsoluteX();
var _2cf0=oThis.node.workflow.getAbsoluteY();
var _2cf1=oThis.getDropTarget(_2ceb.clientX+_2ced-_2cef,_2ceb.clientY+_2cee-_2cf0);
var _2cf2=oThis.getCompartment(_2ceb.clientX+_2ced-_2cef,_2ceb.clientY+_2cee-_2cf0);
if(Drag.currentTarget!=null&&_2cf1!=Drag.currentTarget){
var _2cf3=new DragDropEvent();
_2cf3.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_2cf3);
}
if(_2cf1!=null&&_2cf1!=Drag.currentTarget){
var _2cf3=new DragDropEvent();
_2cf3.initDragDropEvent("dragenter",false,oThis);
_2cf1.dispatchEvent(_2cf3);
}
Drag.currentTarget=_2cf1;
if(Drag.currentCompartment!=null&&_2cf2!=Drag.currentCompartment){
var _2cf3=new DragDropEvent();
_2cf3.initDragDropEvent("figureleave",false,oThis);
Drag.currentCompartment.dispatchEvent(_2cf3);
}
if(_2cf2!=null&&_2cf2.node!=oThis.node&&_2cf2!=Drag.currentCompartment){
var _2cf3=new DragDropEvent();
_2cf3.initDragDropEvent("figureenter",false,oThis);
_2cf2.dispatchEvent(_2cf3);
}
Drag.currentCompartment=_2cf2;
var _2cf4=new DragDropEvent();
_2cf4.initDragDropEvent("drag",false);
oThis.dispatchEvent(_2cf4);
};
oThis.tempMouseUp=function(){
oThis.detachEventHandlers();
var _2cf5=arguments[0]||window.event;
var _2cf6=new DragDropEvent();
_2cf6.initDragDropEvent("dragend",false);
oThis.dispatchEvent(_2cf6);
var _2cf7=oThis.node.workflow.getScrollLeft();
var _2cf8=oThis.node.workflow.getScrollTop();
var _2cf9=oThis.node.workflow.getAbsoluteX();
var _2cfa=oThis.node.workflow.getAbsoluteY();
var _2cfb=oThis.getDropTarget(_2cf5.clientX+_2cf7-_2cf9,_2cf5.clientY+_2cf8-_2cfa);
var _2cfc=oThis.getCompartment(_2cf5.clientX+_2cf7-_2cf9,_2cf5.clientY+_2cf8-_2cfa);
if(_2cfb!=null){
var _2cfd=new DragDropEvent();
_2cfd.initDragDropEvent("drop",false,oThis);
_2cfb.dispatchEvent(_2cfd);
}
if(_2cfc!=null&&_2cfc.node!=oThis.node){
var _2cfd=new DragDropEvent();
_2cfd.initDragDropEvent("figuredrop",false,oThis);
_2cfc.dispatchEvent(_2cfd);
}
if(Drag.currentTarget!=null){
var _2cfd=new DragDropEvent();
_2cfd.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_2cfd);
Drag.currentTarget=null;
}
Drag.currentCompartment=null;
Drag.clearCurrent();
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
Draggable.prototype.detachEventHandlers=function(){
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
Draggable.prototype.getDropTarget=function(x,y){
for(var i=0;i<this.targets.getSize();i++){
var _2d01=this.targets.get(i);
if(_2d01.node.isOver(x,y)&&_2d01.node!=this.node){
return _2d01;
}
}
return null;
};
Draggable.prototype.getCompartment=function(x,y){
var _2d04=null;
for(var i=0;i<this.node.workflow.compartments.getSize();i++){
var _2d06=this.node.workflow.compartments.get(i);
if(_2d06.isOver(x,y)&&_2d06!=this.node){
if(_2d04==null){
_2d04=_2d06;
}else{
if(_2d04.getZOrder()<_2d06.getZOrder()){
_2d04=_2d06;
}
}
}
}
return _2d04==null?null:_2d04.dropable;
};
Draggable.prototype.getLeft=function(){
return this.element.offsetLeft;
};
Draggable.prototype.getTop=function(){
return this.element.offsetTop;
};
DragDropEvent=function(){
Event.call(this);
};
DragDropEvent.prototype=new Event();
DragDropEvent.prototype.initDragDropEvent=function(sType,_2d08,_2d09){
this.initEvent(sType,_2d08);
this.relatedTarget=_2d09;
};
DropTarget=function(_2d0a){
EventTarget.call(this);
this.construct(_2d0a);
};
DropTarget.prototype=new EventTarget;
DropTarget.prototype.construct=function(_2d0b){
this.element=_2d0b;
};
DropTarget.prototype.getLeft=function(){
var el=this.element;
var ol=el.offsetLeft;
while((el=el.offsetParent)!=null){
ol+=el.offsetLeft;
}
return ol;
};
DropTarget.prototype.getTop=function(){
var el=this.element;
var ot=el.offsetTop;
while((el=el.offsetParent)!=null){
ot+=el.offsetTop;
}
return ot;
};
DropTarget.prototype.getHeight=function(){
return this.element.offsetHeight;
};
DropTarget.prototype.getWidth=function(){
return this.element.offsetWidth;
};
PositionConstants=function(){
};
PositionConstants.NORTH=1;
PositionConstants.SOUTH=4;
PositionConstants.WEST=8;
PositionConstants.EAST=16;
Color=function(red,green,blue){
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
Color.prototype.type="Color";
Color.prototype.getHTMLStyle=function(){
return "rgb("+this.red+","+this.green+","+this.blue+")";
};
Color.prototype.getRed=function(){
return this.red;
};
Color.prototype.getGreen=function(){
return this.green;
};
Color.prototype.getBlue=function(){
return this.blue;
};
Color.prototype.getIdealTextColor=function(){
var _390a=105;
var _390b=(this.red*0.299)+(this.green*0.587)+(this.blue*0.114);
return (255-_390b<_390a)?new Color(0,0,0):new Color(255,255,255);
};
Color.prototype.hex2rgb=function(_390c){
_390c=_390c.replace("#","");
return ({0:parseInt(_390c.substr(0,2),16),1:parseInt(_390c.substr(2,2),16),2:parseInt(_390c.substr(4,2),16)});
};
Color.prototype.hex=function(){
return (this.int2hex(this.red)+this.int2hex(this.green)+this.int2hex(this.blue));
};
Color.prototype.int2hex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
Point=function(x,y){
this.x=x;
this.y=y;
};
Point.prototype.type="Point";
Point.prototype.getX=function(){
return this.x;
};
Point.prototype.getY=function(){
return this.y;
};
Point.prototype.getPosition=function(p){
var dx=p.x-this.x;
var dy=p.y-this.y;
if(Math.abs(dx)>Math.abs(dy)){
if(dx<0){
return PositionConstants.WEST;
}
return PositionConstants.EAST;
}
if(dy<0){
return PositionConstants.NORTH;
}
return PositionConstants.SOUTH;
};
Point.prototype.equals=function(o){
return this.x==o.x&&this.y==o.y;
};
Point.prototype.getDistance=function(other){
return Math.sqrt((this.x-other.x)*(this.x-other.x)+(this.y-other.y)*(this.y-other.y));
};
Point.prototype.getTranslated=function(other){
return new Point(this.x+other.x,this.y+other.y);
};
Dimension=function(x,y,w,h){
Point.call(this,x,y);
this.w=w;
this.h=h;
};
Dimension.prototype=new Point;
Dimension.prototype.type="Dimension";
Dimension.prototype.getWidth=function(){
return this.w;
};
Dimension.prototype.getHeight=function(){
return this.h;
};
Dimension.prototype.getRight=function(){
return this.x+this.w;
};
Dimension.prototype.getBottom=function(){
return this.y+this.h;
};
Dimension.prototype.getTopLeft=function(){
return new Point(this.x,this.y);
};
Dimension.prototype.getCenter=function(){
return new Point(this.x+this.w/2,this.y+this.h/2);
};
Dimension.prototype.getBottomRight=function(){
return new Point(this.x+this.w,this.y+this.h);
};
Dimension.prototype.equals=function(o){
return this.x==o.x&&this.y==o.y&&this.w==o.w&&this.h==o.h;
};
SnapToHelper=function(_3912){
this.workflow=_3912;
};
SnapToHelper.NORTH=1;
SnapToHelper.SOUTH=4;
SnapToHelper.WEST=8;
SnapToHelper.EAST=16;
SnapToHelper.NORTH_EAST=SnapToHelper.NORTH|SnapToHelper.EAST;
SnapToHelper.NORTH_WEST=SnapToHelper.NORTH|SnapToHelper.WEST;
SnapToHelper.SOUTH_EAST=SnapToHelper.SOUTH|SnapToHelper.EAST;
SnapToHelper.SOUTH_WEST=SnapToHelper.SOUTH|SnapToHelper.WEST;
SnapToHelper.NORTH_SOUTH=SnapToHelper.NORTH|SnapToHelper.SOUTH;
SnapToHelper.EAST_WEST=SnapToHelper.EAST|SnapToHelper.WEST;
SnapToHelper.NSEW=SnapToHelper.NORTH_SOUTH|SnapToHelper.EAST_WEST;
SnapToHelper.prototype.snapPoint=function(_3913,_3914,_3915){
return _3914;
};
SnapToHelper.prototype.snapRectangle=function(_3916,_3917){
return _3916;
};
SnapToHelper.prototype.onSetDocumentDirty=function(){
};
SnapToGrid=function(_342a){
SnapToHelper.call(this,_342a);
};
SnapToGrid.prototype=new SnapToHelper;
SnapToGrid.prototype.snapPoint=function(_342b,_342c,_342d){
_342d.x=this.workflow.gridWidthX*Math.floor(((_342c.x+this.workflow.gridWidthX/2)/this.workflow.gridWidthX));
_342d.y=this.workflow.gridWidthY*Math.floor(((_342c.y+this.workflow.gridWidthY/2)/this.workflow.gridWidthY));
return 0;
};
SnapToGrid.prototype.snapRectangle=function(_342e,_342f){
_342f.x=_342e.x;
_342f.y=_342e.y;
_342f.w=_342e.w;
_342f.h=_342e.h;
return 0;
};
SnapToGeometryEntry=function(type,_341e){
this.type=type;
this.location=_341e;
};
SnapToGeometryEntry.prototype.getLocation=function(){
return this.location;
};
SnapToGeometryEntry.prototype.getType=function(){
return this.type;
};
SnapToGeometry=function(_3ab0){
SnapToHelper.call(this,_3ab0);
};
SnapToGeometry.prototype=new SnapToHelper;
SnapToGeometry.THRESHOLD=5;
SnapToGeometry.prototype.snapPoint=function(_3ab1,_3ab2,_3ab3){
if(this.rows==null||this.cols==null){
this.populateRowsAndCols();
}
if((_3ab1&SnapToHelper.EAST)!=0){
var _3ab4=this.getCorrectionFor(this.cols,_3ab2.getX()-1,1);
if(_3ab4!=SnapToGeometry.THRESHOLD){
_3ab1&=~SnapToHelper.EAST;
_3ab3.x+=_3ab4;
}
}
if((_3ab1&SnapToHelper.WEST)!=0){
var _3ab5=this.getCorrectionFor(this.cols,_3ab2.getX(),-1);
if(_3ab5!=SnapToGeometry.THRESHOLD){
_3ab1&=~SnapToHelper.WEST;
_3ab3.x+=_3ab5;
}
}
if((_3ab1&SnapToHelper.SOUTH)!=0){
var _3ab6=this.getCorrectionFor(this.rows,_3ab2.getY()-1,1);
if(_3ab6!=SnapToGeometry.THRESHOLD){
_3ab1&=~SnapToHelper.SOUTH;
_3ab3.y+=_3ab6;
}
}
if((_3ab1&SnapToHelper.NORTH)!=0){
var _3ab7=this.getCorrectionFor(this.rows,_3ab2.getY(),-1);
if(_3ab7!=SnapToGeometry.THRESHOLD){
_3ab1&=~SnapToHelper.NORTH;
_3ab3.y+=_3ab7;
}
}
return _3ab1;
};
SnapToGeometry.prototype.snapRectangle=function(_3ab8,_3ab9){
var _3aba=_3ab8.getTopLeft();
var _3abb=_3ab8.getBottomRight();
var _3abc=this.snapPoint(SnapToHelper.NORTH_WEST,_3ab8.getTopLeft(),_3aba);
_3ab9.x=_3aba.x;
_3ab9.y=_3aba.y;
var _3abd=this.snapPoint(SnapToHelper.SOUTH_EAST,_3ab8.getBottomRight(),_3abb);
if(_3abc&SnapToHelper.WEST){
_3ab9.x=_3abb.x-_3ab8.getWidth();
}
if(_3abc&SnapToHelper.NORTH){
_3ab9.y=_3abb.y-_3ab8.getHeight();
}
return _3abc|_3abd;
};
SnapToGeometry.prototype.populateRowsAndCols=function(){
this.rows=new Array();
this.cols=new Array();
var _3abe=this.workflow.getDocument().getFigures();
var index=0;
for(var i=0;i<_3abe.length;i++){
var _3ac1=_3abe[i];
if(_3ac1!=this.workflow.getCurrentSelection()){
var _3ac2=_3ac1.getBounds();
this.cols[index*3]=new SnapToGeometryEntry(-1,_3ac2.getX());
this.rows[index*3]=new SnapToGeometryEntry(-1,_3ac2.getY());
this.cols[index*3+1]=new SnapToGeometryEntry(0,_3ac2.x+(_3ac2.getWidth()-1)/2);
this.rows[index*3+1]=new SnapToGeometryEntry(0,_3ac2.y+(_3ac2.getHeight()-1)/2);
this.cols[index*3+2]=new SnapToGeometryEntry(1,_3ac2.getRight()-1);
this.rows[index*3+2]=new SnapToGeometryEntry(1,_3ac2.getBottom()-1);
index++;
}
}
};
SnapToGeometry.prototype.getCorrectionFor=function(_3ac3,value,side){
var _3ac6=SnapToGeometry.THRESHOLD;
var _3ac7=SnapToGeometry.THRESHOLD;
for(var i=0;i<_3ac3.length;i++){
var entry=_3ac3[i];
var _3aca;
if(entry.type==-1&&side!=0){
_3aca=Math.abs(value-entry.location);
if(_3aca<_3ac6){
_3ac6=_3aca;
_3ac7=entry.location-value;
}
}else{
if(entry.type==0&&side==0){
_3aca=Math.abs(value-entry.location);
if(_3aca<_3ac6){
_3ac6=_3aca;
_3ac7=entry.location-value;
}
}else{
if(entry.type==1&&side!=0){
_3aca=Math.abs(value-entry.location);
if(_3aca<_3ac6){
_3ac6=_3aca;
_3ac7=entry.location-value;
}
}
}
}
}
return _3ac7;
};
SnapToGeometry.prototype.onSetDocumentDirty=function(){
this.rows=null;
this.cols=null;
};
Border=function(){
this.color=null;
};
Border.prototype.type="Border";
Border.prototype.dispose=function(){
this.color=null;
};
Border.prototype.getHTMLStyle=function(){
return "";
};
Border.prototype.setColor=function(c){
this.color=c;
};
Border.prototype.getColor=function(){
return this.color;
};
Border.prototype.refresh=function(){
};
LineBorder=function(width){
Border.call(this);
this.width=1;
if(width){
this.width=width;
}
this.figure=null;
};
LineBorder.prototype=new Border;
LineBorder.prototype.type="LineBorder";
LineBorder.prototype.dispose=function(){
Border.prototype.dispose.call(this);
this.figure=null;
};
LineBorder.prototype.setLineWidth=function(w){
this.width=w;
if(this.figure!=null){
this.figure.html.style.border=this.getHTMLStyle();
}
};
LineBorder.prototype.getHTMLStyle=function(){
if(this.getColor()!=null){
return this.width+"px solid "+this.getColor().getHTMLStyle();
}
return this.width+"px solid black";
};
LineBorder.prototype.refresh=function(){
this.setLineWidth(this.width);
};
Figure=function(){
this.construct();
};
Figure.prototype.type="Figure";
Figure.ZOrderBaseIndex=100;
Figure.setZOrderBaseIndex=function(index){
Figure.ZOrderBaseIndex=index;
};
Figure.prototype.construct=function(){
this.lastDragStartTime=0;
this.x=0;
this.y=0;
this.border=null;
this.setDimension(10,10);
this.id=this.generateUId();
this.html=this.createHTMLElement();
this.canvas=null;
this.workflow=null;
this.draggable=null;
this.parent=null;
this.isMoving=false;
this.canSnapToHelper=true;
this.snapToGridAnchor=new Point(0,0);
this.timer=-1;
this.setDeleteable(true);
this.setCanDrag(true);
this.setResizeable(true);
this.setSelectable(true);
this.properties=new Object();
this.moveListener=new Object();
};
Figure.prototype.dispose=function(){
this.canvas=null;
this.workflow=null;
this.moveListener=null;
if(this.draggable!=null){
this.draggable.removeEventListener("mouseenter",this.tmpMouseEnter);
this.draggable.removeEventListener("mouseleave",this.tmpMouseLeave);
this.draggable.removeEventListener("dragend",this.tmpDragend);
this.draggable.removeEventListener("dragstart",this.tmpDragstart);
this.draggable.removeEventListener("drag",this.tmpDrag);
this.draggable.removeEventListener("dblclick",this.tmpDoubleClick);
this.draggable.node=null;
}
this.draggable=null;
if(this.border!=null){
this.border.dispose();
}
this.border=null;
if(this.parent!=null){
this.parent.removeChild(this);
}
};
Figure.prototype.getProperties=function(){
return this.properties;
};
Figure.prototype.getProperty=function(key){
return this.properties[key];
};
Figure.prototype.setProperty=function(key,value){
this.properties[key]=value;
this.setDocumentDirty();
};
Figure.prototype.getId=function(){
return this.id;
};
Figure.prototype.setCanvas=function(_3817){
this.canvas=_3817;
};
Figure.prototype.getWorkflow=function(){
return this.workflow;
};
Figure.prototype.setWorkflow=function(_3818){
if(this.draggable==null){
this.html.tabIndex="0";
var oThis=this;
this.keyDown=function(event){
event.cancelBubble=true;
event.returnValue=false;
oThis.onKeyDown(event.keyCode,event.ctrlKey);
};
if(this.html.addEventListener){
this.html.addEventListener("keydown",this.keyDown,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("onkeydown",this.keyDown);
}
}
this.draggable=new Draggable(this.html,Draggable.DRAG_X|Draggable.DRAG_Y);
this.draggable.node=this;
this.tmpContextMenu=function(_381b){
oThis.onContextMenu(oThis.x+_381b.x,_381b.y+oThis.y);
};
this.tmpMouseEnter=function(_381c){
oThis.onMouseEnter();
};
this.tmpMouseLeave=function(_381d){
oThis.onMouseLeave();
};
this.tmpDragend=function(_381e){
oThis.onDragend();
};
this.tmpDragstart=function(_381f){
var w=oThis.workflow;
w.showMenu(null);
if(oThis.workflow.toolPalette&&oThis.workflow.toolPalette.activeTool){
_381f.returnValue=false;
oThis.workflow.onMouseDown(oThis.x+_381f.x,_381f.y+oThis.y);
oThis.workflow.onMouseUp(oThis.x+_381f.x,_381f.y+oThis.y);
return;
}
_381f.returnValue=oThis.onDragstart(_381f.x,_381f.y);
};
this.tmpDrag=function(_3821){
oThis.onDrag();
};
this.tmpDoubleClick=function(_3822){
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
this.workflow=_3818;
};
Figure.prototype.createHTMLElement=function(){
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
item.style.zIndex=""+Figure.ZOrderBaseIndex;
return item;
};
Figure.prototype.setParent=function(_3824){
this.parent=_3824;
};
Figure.prototype.getParent=function(){
return this.parent;
};
Figure.prototype.getZOrder=function(){
return this.html.style.zIndex;
};
Figure.prototype.setZOrder=function(index){
this.html.style.zIndex=index;
};
Figure.prototype.hasFixedPosition=function(){
return false;
};
Figure.prototype.getMinWidth=function(){
return 5;
};
Figure.prototype.getMinHeight=function(){
return 5;
};
Figure.prototype.getHTMLElement=function(){
if(this.html==null){
this.html=this.createHTMLElement();
}
return this.html;
};
Figure.prototype.paint=function(){
};
Figure.prototype.setBorder=function(_3826){
if(this.border!=null){
this.border.figure=null;
}
this.border=_3826;
this.border.figure=this;
this.border.refresh();
this.setDocumentDirty();
};
Figure.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!=null){
this.workflow.showMenu(menu,x,y);
}
};
Figure.prototype.getContextMenu=function(){
return null;
};
Figure.prototype.onDoubleClick=function(){
};
Figure.prototype.onMouseEnter=function(){
};
Figure.prototype.onMouseLeave=function(){
};
Figure.prototype.onDrag=function(){
this.x=this.draggable.getLeft();
this.y=this.draggable.getTop();
if(this.isMoving==false){
this.isMoving=true;
this.setAlpha(0.5);
}
this.fireMoveEvent();
};
Figure.prototype.onDragend=function(){
if(this.getWorkflow().getEnableSmoothFigureHandling()==true){
var _382a=this;
var _382b=function(){
if(_382a.alpha<1){
_382a.setAlpha(Math.min(1,_382a.alpha+0.05));
}else{
window.clearInterval(_382a.timer);
_382a.timer=-1;
}
};
if(_382a.timer>0){
window.clearInterval(_382a.timer);
}
_382a.timer=window.setInterval(_382b,20);
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
Figure.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
this.command=new CommandMove(this,this.x,this.y);
return true;
};
Figure.prototype.setCanDrag=function(flag){
this.canDrag=flag;
if(flag){
this.html.style.cursor="move";
}else{
this.html.style.cursor=null;
}
};
Figure.prototype.setAlpha=function(_382f){
if(this.alpha==_382f){
return;
}
try{
this.html.style.MozOpacity=_382f;
}
catch(exc){
}
try{
this.html.style.opacity=_382f;
}
catch(exc){
}
try{
var _3830=Math.round(_382f*100);
if(_3830>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_3830+")";
}
}
catch(exc){
}
this.alpha=_382f;
};
Figure.prototype.setDimension=function(w,h){
this.width=Math.max(this.getMinWidth(),w);
this.height=Math.max(this.getMinHeight(),h);
if(this.html==null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
this.fireMoveEvent();
if(this.workflow!=null&&this.workflow.getCurrentSelection()==this){
this.workflow.showResizeHandles(this);
}
};
Figure.prototype.setPosition=function(xPos,yPos){
this.x=xPos;
this.y=yPos;
if(this.html==null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
this.fireMoveEvent();
if(this.workflow!=null&&this.workflow.getCurrentSelection()==this){
this.workflow.showResizeHandles(this);
}
};
Figure.prototype.isResizeable=function(){
return this.resizeable;
};
Figure.prototype.setResizeable=function(flag){
this.resizeable=flag;
};
Figure.prototype.isSelectable=function(){
return this.selectable;
};
Figure.prototype.setSelectable=function(flag){
this.selectable=flag;
};
Figure.prototype.isStrechable=function(){
return true;
};
Figure.prototype.isDeleteable=function(){
return this.deleteable;
};
Figure.prototype.setDeleteable=function(flag){
this.deleteable=flag;
};
Figure.prototype.setCanSnapToHelper=function(flag){
this.canSnapToHelper=flag;
};
Figure.prototype.getCanSnapToHelper=function(){
return this.canSnapToHelper;
};
Figure.prototype.getSnapToGridAnchor=function(){
return this.snapToGridAnchor;
};
Figure.prototype.setSnapToGridAnchor=function(point){
this.snapToGridAnchor=point;
};
Figure.prototype.getBounds=function(){
return new Dimension(this.getX(),this.getY(),this.getWidth(),this.getHeight());
};
Figure.prototype.getWidth=function(){
return this.width;
};
Figure.prototype.getHeight=function(){
return this.height;
};
Figure.prototype.getY=function(){
return this.y;
};
Figure.prototype.getX=function(){
return this.x;
};
Figure.prototype.getAbsoluteY=function(){
return this.y;
};
Figure.prototype.getAbsoluteX=function(){
return this.x;
};
Figure.prototype.onKeyDown=function(_383a,ctrl){
if(_383a==46&&this.isDeleteable()==true){
this.workflow.commandStack.execute(new CommandDelete(this));
}
if(ctrl){
this.workflow.onKeyDown(_383a,ctrl);
}
};
Figure.prototype.getPosition=function(){
return new Point(this.x,this.y);
};
Figure.prototype.isOver=function(iX,iY){
var x=this.getAbsoluteX();
var y=this.getAbsoluteY();
var iX2=x+this.width;
var iY2=y+this.height;
return (iX>=x&&iX<=iX2&&iY>=y&&iY<=iY2);
};
Figure.prototype.attachMoveListener=function(_3842){
if(_3842==null||this.moveListener==null){
return;
}
this.moveListener[_3842.id]=_3842;
};
Figure.prototype.detachMoveListener=function(_3843){
if(_3843==null||this.moveListener==null){
return;
}
this.moveListener[_3843.id]=null;
};
Figure.prototype.fireMoveEvent=function(){
this.setDocumentDirty();
for(key in this.moveListener){
var _3844=this.moveListener[key];
if(_3844!=null){
_3844.onOtherFigureMoved(this);
}
}
};
Figure.prototype.onOtherFigureMoved=function(_3845){
};
Figure.prototype.setDocumentDirty=function(){
if(this.workflow!=null){
this.workflow.setDocumentDirty();
}
};
Figure.prototype.generateUId=function(){
var chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
var _3847=10;
var _3848=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_3847;i++){
var rnum=Math.floor(Math.random()*chars.length);
id+=chars.substring(rnum,rnum+1);
}
elem=document.getElementById(id);
if(!elem){
return id;
}
nbTry+=1;
}
return null;
};
Figure.prototype.disableTextSelection=function(e){
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
Node=function(){
this.bgColor=null;
this.lineColor=new Color(128,128,255);
this.lineStroke=1;
this.ports=new ArrayList();
Figure.call(this);
};
Node.prototype=new Figure;
Node.prototype.type="Node";
Node.prototype.dispose=function(){
for(var i=0;i<this.ports.getSize();i++){
this.ports.get(i).dispose();
}
this.ports=null;
Figure.prototype.dispose.call(this);
};
Node.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
item.style.width="auto";
item.style.height="auto";
item.style.margin="0px";
item.style.padding="0px";
if(this.lineColor!=null){
item.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}
item.style.fontSize="1px";
if(this.bgColor!=null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
Node.prototype.paint=function(){
Figure.prototype.paint.call(this);
for(var i=0;i<this.ports.getSize();i++){
this.ports.get(i).paint();
}
};
Node.prototype.getPorts=function(){
var _2d14=new Array();
for(var i=0;i<this.ports.getSize();i++){
_2d14.push(this.ports.get(i));
}
return _2d14;
};
Node.prototype.getPort=function(_2d16){
if(this.ports==null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_2d16){
return port;
}
}
};
Node.prototype.addPort=function(port,x,y){
this.ports.add(port);
port.setOrigin(x,y);
port.setPosition(x,y);
port.setParent(this);
port.setDeleteable(false);
this.html.appendChild(port.getHTMLElement());
if(this.workflow!=null){
this.workflow.registerPort(port);
}
};
Node.prototype.removePort=function(port){
if(this.ports!=null){
this.ports.removeElementAt(this.ports.indexOf(port));
}
try{
this.html.removeChild(port.getHTMLElement());
}
catch(exc){
}
if(this.workflow!=null){
this.workflow.unregisterPort(port);
}
};
Node.prototype.setWorkflow=function(_2d1d){
var _2d1e=this.workflow;
Figure.prototype.setWorkflow.call(this,_2d1d);
if(_2d1e!=null){
for(var i=0;i<this.ports.getSize();i++){
_2d1e.unregisterPort(this.ports.get(i));
}
}
if(this.workflow!=null){
for(var i=0;i<this.ports.getSize();i++){
this.workflow.registerPort(this.ports.get(i));
}
}
};
Node.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!=null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
Node.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!=null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
Node.prototype.setLineWidth=function(w){
this.lineStroke=w;
if(this.lineColor!=null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
VectorFigure=function(){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.stroke=1;
this.graphics=null;
Node.call(this);
};
VectorFigure.prototype=new Node;
VectorFigure.prototype.type="VectorFigure";
VectorFigure.prototype.dispose=function(){
Node.prototype.dispose.call(this);
this.bgColor=null;
this.lineColor=null;
if(this.graphics!=null){
this.graphics.clear();
}
this.graphics=null;
};
VectorFigure.prototype.createHTMLElement=function(){
var item=Node.prototype.createHTMLElement.call(this);
item.style.border="0px";
item.style.backgroundColor="transparent";
return item;
};
VectorFigure.prototype.setWorkflow=function(_3882){
Node.prototype.setWorkflow.call(this,_3882);
if(this.workflow==null){
this.graphics.clear();
this.graphics=null;
}
};
VectorFigure.prototype.paint=function(){
if(this.graphics==null){
this.graphics=new jsGraphics(this.id);
}else{
this.graphics.clear();
}
Node.prototype.paint.call(this);
for(var i=0;i<this.ports.getSize();i++){
this.html.appendChild(this.ports.get(i).getHTMLElement());
}
};
VectorFigure.prototype.setDimension=function(w,h){
Node.prototype.setDimension.call(this,w,h);
if(this.graphics!=null){
this.paint();
}
};
VectorFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!=null){
this.paint();
}
};
VectorFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
VectorFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!=null){
this.paint();
}
};
VectorFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!=null){
this.paint();
}
};
VectorFigure.prototype.getColor=function(){
return this.lineColor;
};
Label=function(msg){
this.msg=msg;
this.bgColor=null;
this.color=new Color(0,0,0);
this.fontSize=10;
this.textNode=null;
this.align="center";
Figure.call(this);
};
Label.prototype=new Figure;
Label.prototype.type="Label";
Label.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
item.style.color=this.color.getHTMLStyle();
item.style.fontSize=this.fontSize+"pt";
item.style.width="auto";
item.style.height="auto";
item.style.paddingLeft="3px";
item.style.paddingRight="3px";
item.style.textAlign=this.align;
if(this.bgColor!=null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
Label.prototype.isResizeable=function(){
return false;
};
Label.prototype.setWordwrap=function(flag){
this.html.style.whiteSpace=flag?"wrap":"nowrap";
};
Label.prototype.setAlign=function(align){
this.align=align;
this.html.style.textAlign=align;
};
Label.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!=null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
Label.prototype.setColor=function(color){
this.color=color;
this.html.style.color=this.color.getHTMLStyle();
};
Label.prototype.setFontSize=function(size){
this.fontSize=size;
this.html.style.fontSize=this.fontSize+"pt";
};
Label.prototype.getWidth=function(){
try{
return parseInt(getComputedStyle(this.html,"").getPropertyValue("width"));
}
catch(e){
return (this.html.clientWidth);
}
return 100;
};
Label.prototype.getHeight=function(){
try{
return parseInt(getComputedStyle(this.html,"").getPropertyValue("height"));
}
catch(e){
return (this.html.clientHeight);
}
return 30;
};
Label.prototype.setText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createTextNode(this.msg);
this.html.appendChild(this.textNode);
};
Label.prototype.setStyledText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createElement("div");
this.textNode.style.whiteSpace="nowrap";
this.textNode.innerHTML=text;
this.html.appendChild(this.textNode);
};
Oval=function(){
VectorFigure.call(this);
};
Oval.prototype=new VectorFigure;
Oval.prototype.type="Oval";
Oval.prototype.paint=function(){
VectorFigure.prototype.paint.call(this);
this.graphics.setStroke(this.stroke);
if(this.bgColor!=null){
this.graphics.setColor(this.bgColor.getHTMLStyle());
this.graphics.fillOval(0,0,this.getWidth()-1,this.getHeight()-1);
}
if(this.lineColor!=null){
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.graphics.drawOval(0,0,this.getWidth()-1,this.getHeight()-1);
}
this.graphics.paint();
};
Circle=function(_3945){
Oval.call(this);
if(_3945){
this.setDimension(_3945,_3945);
}
};
Circle.prototype=new Oval;
Circle.prototype.type="Circle";
Circle.prototype.setDimension=function(w,h){
if(w>h){
Oval.prototype.setDimension.call(this,w,w);
}else{
Oval.prototype.setDimension.call(this,h,h);
}
};
Circle.prototype.isStrechable=function(){
return false;
};
Rectangle=function(width,_3851){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.lineStroke=1;
Figure.call(this);
if(width&&_3851){
this.setDimension(width,_3851);
}
};
Rectangle.prototype=new Figure;
Rectangle.prototype.type="Rectangle";
Rectangle.prototype.dispose=function(){
Figure.prototype.dispose.call(this);
this.bgColor=null;
this.lineColor=null;
};
Rectangle.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
item.style.width="auto";
item.style.height="auto";
item.style.margin="0px";
item.style.padding="0px";
item.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
item.style.fontSize="1px";
item.style.lineHeight="1px";
item.innerHTML="&nbsp";
if(this.bgColor!=null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
Rectangle.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!=null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
Rectangle.prototype.getBackgroundColor=function(){
return this.bgColor;
};
Rectangle.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!=null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border=this.lineStroke+"0px";
}
};
Rectangle.prototype.getColor=function(){
return this.lineColor;
};
Rectangle.prototype.getWidth=function(){
return Figure.prototype.getWidth.call(this)+2*this.lineStroke;
};
Rectangle.prototype.getHeight=function(){
return Figure.prototype.getHeight.call(this)+2*this.lineStroke;
};
Rectangle.prototype.setDimension=function(w,h){
return Figure.prototype.setDimension.call(this,w-2*this.lineStroke,h-2*this.lineStroke);
};
Rectangle.prototype.setLineWidth=function(w){
var diff=w-this.lineStroke;
this.setDimension(this.getWidth()-2*diff,this.getHeight()-2*diff);
this.lineStroke=w;
var c="transparent";
if(this.lineColor!=null){
c=this.lineColor.getHTMLStyle();
}
this.html.style.border=this.lineStroke+"px solid "+c;
};
Rectangle.prototype.getLineWidth=function(){
return this.lineStroke;
};
ImageFigure=function(url){
this.url=url;
Node.call(this);
this.setDimension(40,40);
};
ImageFigure.prototype=new Node;
ImageFigure.prototype.type="Image";
ImageFigure.prototype.createHTMLElement=function(){
var item=Node.prototype.createHTMLElement.call(this);
item.style.width=this.width+"px";
item.style.height=this.height+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.border="0px";
if(this.url!=null){
item.style.backgroundImage="url("+this.url+")";
}else{
item.style.backgroundImage="";
}
return item;
};
ImageFigure.prototype.setBackgroundColor=function(color){
};
ImageFigure.prototype.setColor=function(color){
};
ImageFigure.prototype.isResizeable=function(){
return false;
};
ImageFigure.prototype.setImage=function(url){
this.url=url;
if(this.url!=null){
this.html.style.backgroundImage="url("+this.url+")";
}else{
this.html.style.backgroundImage="";
}
};
Port=function(_3b8a,_3b8b){
Corona=function(){
};
Corona.prototype=new Circle;
Corona.prototype.setAlpha=function(_3b8c){
Circle.prototype.setAlpha.call(this,Math.min(0.3,_3b8c));
};
if(_3b8a==null){
this.currentUIRepresentation=new Circle();
}else{
this.currentUIRepresentation=_3b8a;
}
if(_3b8b==null){
this.connectedUIRepresentation=new Circle();
this.connectedUIRepresentation.setColor(null);
}else{
this.connectedUIRepresentation=_3b8b;
}
this.disconnectedUIRepresentation=this.currentUIRepresentation;
this.hideIfConnected=false;
this.uiRepresentationAdded=true;
this.parentNode=null;
this.originX=0;
this.originY=0;
this.coronaWidth=10;
this.corona=null;
Rectangle.call(this);
this.setDimension(8,8);
this.setBackgroundColor(new Color(100,180,100));
this.setColor(new Color(90,150,90));
Rectangle.prototype.setColor.call(this,null);
this.dropable=new DropTarget(this.html);
this.dropable.node=this;
this.dropable.addEventListener("dragenter",function(_3b8d){
_3b8d.target.node.onDragEnter(_3b8d.relatedTarget.node);
});
this.dropable.addEventListener("dragleave",function(_3b8e){
_3b8e.target.node.onDragLeave(_3b8e.relatedTarget.node);
});
this.dropable.addEventListener("drop",function(_3b8f){
_3b8f.relatedTarget.node.onDrop(_3b8f.target.node);
});
};
Port.prototype=new Rectangle;
Port.prototype.type="Port";
Port.ZOrderBaseIndex=5000;
Port.setZOrderBaseIndex=function(index){
Port.ZOrderBaseIndex=index;
};
Port.prototype.setHideIfConnected=function(flag){
this.hideIfConnected=flag;
};
Port.prototype.dispose=function(){
for(key in this.moveListener){
var _3b92=this.moveListener[key];
if(_3b92!=null){
this.parentNode.workflow.removeFigure(_3b92);
_3b92.dispose();
}
}
Rectangle.prototype.dispose.call(this);
this.parentNode=null;
this.dropable.node=null;
this.dropable=null;
this.disconnectedUIRepresentation.dispose();
this.connectedUIRepresentation.dispose();
};
Port.prototype.createHTMLElement=function(){
var item=Rectangle.prototype.createHTMLElement.call(this);
item.style.zIndex=Port.ZOrderBaseIndex;
this.currentUIRepresentation.html.zIndex=Port.ZOrderBaseIndex;
item.appendChild(this.currentUIRepresentation.html);
this.uiRepresentationAdded=true;
return item;
};
Port.prototype.setUiRepresentation=function(_3b94){
if(_3b94==null){
_3b94=new Figure();
}
if(this.uiRepresentationAdded){
this.html.removeChild(this.currentUIRepresentation.getHTMLElement());
}
this.html.appendChild(_3b94.getHTMLElement());
_3b94.paint();
this.currentUIRepresentation=_3b94;
};
Port.prototype.onMouseEnter=function(){
this.setLineWidth(2);
};
Port.prototype.onMouseLeave=function(){
this.setLineWidth(0);
};
Port.prototype.setDimension=function(width,_3b96){
Rectangle.prototype.setDimension.call(this,width,_3b96);
this.connectedUIRepresentation.setDimension(width,_3b96);
this.disconnectedUIRepresentation.setDimension(width,_3b96);
this.setPosition(this.x,this.y);
};
Port.prototype.setBackgroundColor=function(color){
this.currentUIRepresentation.setBackgroundColor(color);
};
Port.prototype.getBackgroundColor=function(){
return this.currentUIRepresentation.getBackgroundColor();
};
Port.prototype.getConnections=function(){
var _3b98=new Array();
for(key in this.moveListener){
var _3b99=this.moveListener[key];
if(_3b99 instanceof Connection){
_3b98.push(_3b99);
}
}
return _3b98;
};
Port.prototype.setColor=function(color){
this.currentUIRepresentation.setColor(color);
};
Port.prototype.getColor=function(){
return this.currentUIRepresentation.getColor();
};
Port.prototype.setLineWidth=function(width){
this.currentUIRepresentation.setLineWidth(width);
};
Port.prototype.getLineWidth=function(){
return this.currentUIRepresentation.getLineWidth();
};
Port.prototype.paint=function(){
this.currentUIRepresentation.paint();
};
Port.prototype.setPosition=function(xPos,yPos){
this.originX=xPos;
this.originY=yPos;
Rectangle.prototype.setPosition.call(this,xPos,yPos);
if(this.html==null){
return;
}
this.html.style.left=(this.x-this.getWidth()/2)+"px";
this.html.style.top=(this.y-this.getHeight()/2)+"px";
};
Port.prototype.setParent=function(_3b9e){
if(this.parentNode!=null){
this.parentNode.detachMoveListener(this);
}
this.parentNode=_3b9e;
if(this.parentNode!=null){
this.parentNode.attachMoveListener(this);
}
};
Port.prototype.attachMoveListener=function(_3b9f){
Rectangle.prototype.attachMoveListener.call(this,_3b9f);
if(this.hideIfConnected==true){
this.setUiRepresentation(this.connectedUIRepresentation);
}
};
Port.prototype.detachMoveListener=function(_3ba0){
Rectangle.prototype.detachMoveListener.call(this,_3ba0);
if(this.getConnections().length==0){
this.setUiRepresentation(this.disconnectedUIRepresentation);
}
};
Port.prototype.getParent=function(){
return this.parentNode;
};
Port.prototype.onDrag=function(){
Rectangle.prototype.onDrag.call(this);
this.parentNode.workflow.showConnectionLine(this.parentNode.x+this.x,this.parentNode.y+this.y,this.parentNode.x+this.originX,this.parentNode.y+this.originY);
};
Port.prototype.getCoronaWidth=function(){
return this.coronaWidth;
};
Port.prototype.setCoronaWidth=function(width){
this.coronaWidth=width;
};
Port.prototype.onDragend=function(){
this.setAlpha(1);
this.setPosition(this.originX,this.originY);
this.parentNode.workflow.hideConnectionLine();
};
Port.prototype.setOrigin=function(x,y){
this.originX=x;
this.originY=y;
};
Port.prototype.onDragEnter=function(port){
this.parentNode.workflow.connectionLine.setColor(new Color(0,150,0));
this.parentNode.workflow.connectionLine.setLineWidth(3);
this.showCorona(true);
};
Port.prototype.onDragLeave=function(port){
this.parentNode.workflow.connectionLine.setColor(new Color(0,0,0));
this.parentNode.workflow.connectionLine.setLineWidth(1);
this.showCorona(false);
};
Port.prototype.onDrop=function(port){
if(this.parentNode.id==port.parentNode.id){
}else{
var _3ba7=new CommandConnect(this.parentNode.workflow,port,this);
this.parentNode.workflow.getCommandStack().execute(_3ba7);
}
};
Port.prototype.getAbsolutePosition=function(){
return new Point(this.getAbsoluteX(),this.getAbsoluteY());
};
Port.prototype.getAbsoluteY=function(){
return this.originY+this.parentNode.getY();
};
Port.prototype.getAbsoluteX=function(){
return this.originX+this.parentNode.getX();
};
Port.prototype.onOtherFigureMoved=function(_3ba8){
this.fireMoveEvent();
};
Port.prototype.getName=function(){
return this.getProperty("name");
};
Port.prototype.setName=function(name){
this.setProperty("name",name);
};
Port.prototype.isOver=function(iX,iY){
var x=this.getAbsoluteX()-this.coronaWidth-this.getWidth()/2;
var y=this.getAbsoluteY()-this.coronaWidth-this.getHeight()/2;
var iX2=x+this.width+(this.coronaWidth*2)+this.getWidth()/2;
var iY2=y+this.height+(this.coronaWidth*2)+this.getHeight()/2;
return (iX>=x&&iX<=iX2&&iY>=y&&iY<=iY2);
};
Port.prototype.showCorona=function(flag,_3bb1){
if(flag==true){
this.corona=new Corona();
this.corona.setAlpha(0.3);
this.corona.setBackgroundColor(new Color(0,125,125));
this.corona.setColor(null);
this.corona.setDimension(this.getWidth()+(this.getCoronaWidth()*2),this.getWidth()+(this.getCoronaWidth()*2));
this.parentNode.getWorkflow().addFigure(this.corona,this.getAbsoluteX()-this.getCoronaWidth()-this.getWidth()/2,this.getAbsoluteY()-this.getCoronaWidth()-this.getHeight()/2);
}else{
if(flag==false&&this.corona!=null){
this.parentNode.getWorkflow().removeFigure(this.corona);
this.corona=null;
}
}
};
InputPort=function(_3a45){
Port.call(this,_3a45);
};
InputPort.prototype=new Port;
InputPort.prototype.type="InputPort";
InputPort.prototype.onDrop=function(port){
if(port.getMaxFanOut&&port.getMaxFanOut()<=port.getFanOut()){
return;
}
if(this.parentNode.id==port.parentNode.id){
}else{
if(port instanceof OutputPort){
var _3a47=new CommandConnect(this.parentNode.workflow,port,this);
this.parentNode.workflow.getCommandStack().execute(_3a47);
}
}
};
InputPort.prototype.onDragEnter=function(port){
if(port instanceof OutputPort){
Port.prototype.onDragEnter.call(this,port);
}
};
InputPort.prototype.onDragLeave=function(port){
if(port instanceof OutputPort){
Port.prototype.onDragLeave.call(this,port);
}
};
OutputPort=function(_2fea){
Port.call(this,_2fea);
this.maxFanOut=-1;
};
OutputPort.prototype=new Port;
OutputPort.prototype.type="OutputPort";
OutputPort.prototype.onDrop=function(port){
if(this.getMaxFanOut()<=this.getFanOut()){
return;
}
if(this.parentNode.id==port.parentNode.id){
}else{
if(port instanceof InputPort){
var _2fec=new CommandConnect(this.parentNode.workflow,this,port);
this.parentNode.workflow.getCommandStack().execute(_2fec);
}
}
};
OutputPort.prototype.onDragEnter=function(port){
if(this.getMaxFanOut()<=this.getFanOut()){
return;
}
if(port instanceof InputPort){
Port.prototype.onDragEnter.call(this,port);
}
};
OutputPort.prototype.onDragLeave=function(port){
if(port instanceof InputPort){
Port.prototype.onDragLeave.call(this,port);
}
};
OutputPort.prototype.onDragstart=function(x,y){
if(this.maxFanOut==-1){
return true;
}
if(this.getMaxFanOut()<=this.getFanOut()){
return false;
}
return true;
};
OutputPort.prototype.setMaxFanOut=function(count){
this.maxFanOut=count;
};
OutputPort.prototype.getMaxFanOut=function(){
return this.maxFanOut;
};
OutputPort.prototype.getFanOut=function(){
if(this.getParent().workflow==null){
return 0;
}
var count=0;
var lines=this.getParent().workflow.getLines();
for(key in lines){
var line=lines[key];
if(line instanceof Connection){
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
Line=function(){
this.lineColor=new Color(0,0,0);
this.stroke=1;
this.canvas=null;
this.workflow=null;
this.html=null;
this.graphics=null;
this.id=this.generateUId();
this.startX=30;
this.startY=30;
this.endX=100;
this.endY=100;
this.alpha=1;
this.zOrder=Line.ZOrderBaseIndex;
this.moveListener=new Object();
this.setSelectable(true);
this.setDeleteable(true);
};
Line.ZOrderBaseIndex=20;
Line.setZOrderBaseIndex=function(index){
Line.ZOrderBaseIndex=index;
};
Line.prototype.dispose=function(){
this.canvas=null;
this.workflow=null;
if(this.graphics!=null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.getZOrder=function(){
return this.zOrder;
};
Line.prototype.setZOrder=function(index){
if(this.html!=null){
this.html.style.zIndex=index;
}
this.zOrder=index;
};
Line.prototype.createHTMLElement=function(){
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
Line.prototype.getHTMLElement=function(){
if(this.html==null){
this.html=this.createHTMLElement();
}
return this.html;
};
Line.prototype.getWorkflow=function(){
return this.workflow;
};
Line.prototype.isResizeable=function(){
return true;
};
Line.prototype.setCanvas=function(_38db){
this.canvas=_38db;
if(this.graphics!=null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.setWorkflow=function(_38dc){
this.workflow=_38dc;
if(this.graphics!=null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.paint=function(){
if(this.graphics==null){
this.graphics=new jsGraphics(this.id);
}else{
this.graphics.clear();
}
this.graphics.setStroke(this.stroke);
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.graphics.drawLine(this.startX,this.startY,this.endX,this.endY);
this.graphics.paint();
};
Line.prototype.attachMoveListener=function(_38dd){
this.moveListener[_38dd.id]=_38dd;
};
Line.prototype.detachMoveListener=function(_38de){
this.moveListener[_38de.id]=null;
};
Line.prototype.fireMoveEvent=function(){
for(key in this.moveListener){
var _38df=this.moveListener[key];
if(_38df!=null){
_38df.onOtherFigureMoved(this);
}
}
};
Line.prototype.onOtherFigureMoved=function(_38e0){
};
Line.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!=null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!=null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.getColor=function(){
return this.lineColor;
};
Line.prototype.setAlpha=function(_38e3){
if(_38e3==this.alpha){
return;
}
try{
this.html.style.MozOpacity=_38e3;
}
catch(exc){
}
try{
this.html.style.opacity=_38e3;
}
catch(exc){
}
try{
var _38e4=Math.round(_38e3*100);
if(_38e4>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_38e4+")";
}
}
catch(exc){
}
this.alpha=_38e3;
};
Line.prototype.setStartPoint=function(x,y){
this.startX=x;
this.startY=y;
if(this.graphics!=null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.setEndPoint=function(x,y){
this.endX=x;
this.endY=y;
if(this.graphics!=null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.getStartX=function(){
return this.startX;
};
Line.prototype.getStartY=function(){
return this.startY;
};
Line.prototype.getStartPoint=function(){
return new Point(this.startX,this.startY);
};
Line.prototype.getEndX=function(){
return this.endX;
};
Line.prototype.getEndY=function(){
return this.endY;
};
Line.prototype.getEndPoint=function(){
return new Point(this.endX,this.endY);
};
Line.prototype.isSelectable=function(){
return this.selectable;
};
Line.prototype.setSelectable=function(flag){
this.selectable=flag;
};
Line.prototype.isDeleteable=function(){
return this.deleteable;
};
Line.prototype.setDeleteable=function(flag){
this.deleteable=flag;
};
Line.prototype.getLength=function(){
return Math.sqrt((this.startX-this.endX)*(this.startX-this.endX)+(this.startY-this.endY)*(this.startY-this.endY));
};
Line.prototype.getAngle=function(){
var _38eb=this.getLength();
var angle=-(180/Math.PI)*Math.asin((this.startY-this.endY)/_38eb);
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
Line.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!=null){
this.workflow.showMenu(menu,x,y);
}
};
Line.prototype.getContextMenu=function(){
return null;
};
Line.prototype.onDoubleClick=function(){
};
Line.prototype.setDocumentDirty=function(){
if(this.workflow!=null){
this.workflow.setDocumentDirty();
}
};
Line.prototype.generateUId=function(){
var chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
var _38f1=10;
var _38f2=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_38f1;i++){
var rnum=Math.floor(Math.random()*chars.length);
id+=chars.substring(rnum,rnum+1);
}
elem=document.getElementById(id);
if(!elem){
return id;
}
nbTry+=1;
}
return null;
};
Line.prototype.containsPoint=function(px,py){
return Line.hit(this.startX,this.startY,this.endX,this.endY,px,py);
};
Line.hit=function(X1,Y1,X2,Y2,px,py){
var _38fe=5;
X2-=X1;
Y2-=Y1;
px-=X1;
py-=Y1;
var _38ff=px*X2+py*Y2;
var _3900;
if(_38ff<=0){
_3900=0;
}else{
px=X2-px;
py=Y2-py;
_38ff=px*X2+py*Y2;
if(_38ff<=0){
_3900=0;
}else{
_3900=_38ff*_38ff/(X2*X2+Y2*Y2);
}
}
var lenSq=px*px+py*py-_3900;
if(lenSq<0){
lenSq=0;
}
return Math.sqrt(lenSq)<_38fe;
};
ConnectionRouter=function(){
};
ConnectionRouter.prototype.type="ConnectionRouter";
ConnectionRouter.prototype.getDirection=function(r,p){
var _3966=Math.abs(r.x-p.x);
var _3967=3;
var i=Math.abs(r.y-p.y);
if(i<=_3966){
_3966=i;
_3967=0;
}
i=Math.abs(r.getBottom()-p.y);
if(i<=_3966){
_3966=i;
_3967=2;
}
i=Math.abs(r.getRight()-p.x);
if(i<_3966){
_3966=i;
_3967=1;
}
return _3967;
};
ConnectionRouter.prototype.getEndDirection=function(conn){
var p=conn.getEndPoint();
var rect=conn.getTarget().getParent().getBounds();
return this.getDirection(rect,p);
};
ConnectionRouter.prototype.getStartDirection=function(conn){
var p=conn.getStartPoint();
var rect=conn.getSource().getParent().getBounds();
return this.getDirection(rect,p);
};
ConnectionRouter.prototype.route=function(_396f){
};
NullConnectionRouter=function(){
};
NullConnectionRouter.prototype=new ConnectionRouter;
NullConnectionRouter.prototype.type="NullConnectionRouter";
NullConnectionRouter.prototype.invalidate=function(){
};
NullConnectionRouter.prototype.route=function(_3953){
_3953.addPoint(_3953.getStartPoint());
_3953.addPoint(_3953.getEndPoint());
};
ManhattanConnectionRouter=function(){
this.MINDIST=20;
};
ManhattanConnectionRouter.prototype=new ConnectionRouter;
ManhattanConnectionRouter.prototype.type="ManhattanConnectionRouter";
ManhattanConnectionRouter.prototype.route=function(conn){
var _35d6=conn.getStartPoint();
var _35d7=this.getStartDirection(conn);
var toPt=conn.getEndPoint();
var toDir=this.getEndDirection(conn);
this._route(conn,toPt,toDir,_35d6,_35d7);
};
ManhattanConnectionRouter.prototype._route=function(conn,_35db,_35dc,toPt,toDir){
var TOL=0.1;
var _35e0=0.01;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_35db.x-toPt.x;
var yDiff=_35db.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_35e0))&&((yDiff*yDiff)<(_35e0))){
conn.addPoint(new Point(toPt.x,toPt.y));
return;
}
if(_35dc==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir==RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_35db.x-this.MINDIST,_35db.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_35db.y);
}else{
if(_35dc==toDir){
var pos=Math.min(_35db.x,toPt.x)-this.MINDIST;
point=new Point(pos,_35db.y);
}else{
point=new Point(_35db.x-(xDiff/2),_35db.y);
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
if(_35dc==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_35db.x+this.MINDIST,_35db.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_35db.y);
}else{
if(_35dc==toDir){
var pos=Math.max(_35db.x,toPt.x)+this.MINDIST;
point=new Point(pos,_35db.y);
}else{
point=new Point(_35db.x-(xDiff/2),_35db.y);
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
if(_35dc==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_35db.x,_35db.y+this.MINDIST);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_35db.x,toPt.y);
}else{
if(_35dc==toDir){
var pos=Math.max(_35db.y,toPt.y)+this.MINDIST;
point=new Point(_35db.x,pos);
}else{
point=new Point(_35db.x,_35db.y-(yDiff/2));
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
if(_35dc==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir==DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_35db.x,_35db.y-this.MINDIST);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_35db.x,toPt.y);
}else{
if(_35dc==toDir){
var pos=Math.min(_35db.y,toPt.y)-this.MINDIST;
point=new Point(_35db.x,pos);
}else{
point=new Point(_35db.x,_35db.y-(yDiff/2));
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
conn.addPoint(_35db);
};
BezierConnectionRouter=function(_2fb6){
if(!_2fb6){
this.cheapRouter=new ManhattanConnectionRouter();
}else{
this.cheapRouter=null;
}
this.iteration=5;
};
BezierConnectionRouter.prototype=new ConnectionRouter;
BezierConnectionRouter.prototype.type="BezierConnectionRouter";
BezierConnectionRouter.prototype.drawBezier=function(_2fb7,_2fb8,t,iter){
var n=_2fb7.length-1;
var q=new Array();
for(var i=0;i<n+1;i++){
q[i]=new Array();
q[i][0]=_2fb7[i];
}
for(var j=1;j<=n;j++){
for(var i=0;i<=(n-j);i++){
q[i][j]=new Point((1-t)*q[i][j-1].x+t*q[i+1][j-1].x,(1-t)*q[i][j-1].y+t*q[i+1][j-1].y);
}
}
var c1=new Array();
var c2=new Array();
for(var i=0;i<n+1;i++){
c1[i]=q[0][i];
c2[i]=q[i][n-i];
}
if(iter>=0){
this.drawBezier(c1,_2fb8,t,--iter);
this.drawBezier(c2,_2fb8,t,--iter);
}else{
for(var i=0;i<n;i++){
_2fb8.push(q[i][n-i]);
}
}
};
BezierConnectionRouter.prototype.route=function(conn){
if(this.cheapRouter!=null&&(conn.getSource().getParent().isMoving==true||conn.getTarget().getParent().isMoving==true)){
this.cheapRouter.route(conn);
return;
}
var _2fc2=new Array();
var _2fc3=conn.getStartPoint();
var toPt=conn.getEndPoint();
this._route(_2fc2,conn,toPt,this.getEndDirection(conn),_2fc3,this.getStartDirection(conn));
var _2fc5=new Array();
this.drawBezier(_2fc2,_2fc5,0.5,this.iteration);
for(var i=0;i<_2fc5.length;i++){
conn.addPoint(_2fc5[i]);
}
conn.addPoint(toPt);
};
BezierConnectionRouter.prototype._route=function(_2fc7,conn,_2fc9,_2fca,toPt,toDir){
var TOL=0.1;
var _2fce=0.01;
var _2fcf=90;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_2fc9.x-toPt.x;
var yDiff=_2fc9.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_2fce))&&((yDiff*yDiff)<(_2fce))){
_2fc7.push(new Point(toPt.x,toPt.y));
return;
}
if(_2fca==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir==RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_2fc9.x-_2fcf,_2fc9.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_2fc9.y);
}else{
if(_2fca==toDir){
var pos=Math.min(_2fc9.x,toPt.x)-_2fcf;
point=new Point(pos,_2fc9.y);
}else{
point=new Point(_2fc9.x-(xDiff/2),_2fc9.y);
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
if(_2fca==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_2fc9.x+_2fcf,_2fc9.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_2fc9.y);
}else{
if(_2fca==toDir){
var pos=Math.max(_2fc9.x,toPt.x)+_2fcf;
point=new Point(pos,_2fc9.y);
}else{
point=new Point(_2fc9.x-(xDiff/2),_2fc9.y);
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
if(_2fca==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_2fc9.x,_2fc9.y+_2fcf);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_2fc9.x,toPt.y);
}else{
if(_2fca==toDir){
var pos=Math.max(_2fc9.y,toPt.y)+_2fcf;
point=new Point(_2fc9.x,pos);
}else{
point=new Point(_2fc9.x,_2fc9.y-(yDiff/2));
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
if(_2fca==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir==DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_2fc9.x,_2fc9.y-_2fcf);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_2fc9.x,toPt.y);
}else{
if(_2fca==toDir){
var pos=Math.min(_2fc9.y,toPt.y)-_2fcf;
point=new Point(_2fc9.x,pos);
}else{
point=new Point(_2fc9.x,_2fc9.y-(yDiff/2));
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
this._route(_2fc7,conn,point,dir,toPt,toDir);
_2fc7.push(_2fc9);
};
FanConnectionRouter=function(){
};
FanConnectionRouter.prototype=new NullConnectionRouter;
FanConnectionRouter.prototype.type="FanConnectionRouter";
FanConnectionRouter.prototype.route=function(conn){
var _3a9c=conn.getStartPoint();
var toPt=conn.getEndPoint();
var lines=conn.getSource().getConnections();
var _3a9f=new ArrayList();
var index=0;
for(var i=0;i<lines.length;i++){
var _3aa2=lines[i];
if(_3aa2.getTarget()==conn.getTarget()||_3aa2.getSource()==conn.getTarget()){
_3a9f.add(_3aa2);
if(conn==_3aa2){
index=_3a9f.getSize();
}
}
}
if(_3a9f.getSize()>1){
this.routeCollision(conn,index);
}else{
NullConnectionRouter.prototype.route.call(this,conn);
}
};
FanConnectionRouter.prototype.routeNormal=function(conn){
conn.addPoint(conn.getStartPoint());
conn.addPoint(conn.getEndPoint());
};
FanConnectionRouter.prototype.routeCollision=function(conn,index){
var start=conn.getStartPoint();
var end=conn.getEndPoint();
conn.addPoint(start);
var _3aa8=10;
var _3aa9=new Point((end.x+start.x)/2,(end.y+start.y)/2);
var _3aaa=end.getPosition(start);
var ray;
if(_3aaa==PositionConstants.SOUTH||_3aaa==PositionConstants.EAST){
ray=new Point(end.x-start.x,end.y-start.y);
}else{
ray=new Point(start.x-end.x,start.y-end.y);
}
var _3aac=Math.sqrt(ray.x*ray.x+ray.y*ray.y);
var _3aad=_3aa8*ray.x/_3aac;
var _3aae=_3aa8*ray.y/_3aac;
var _3aaf;
if(index%2==0){
_3aaf=new Point(_3aa9.x+(index/2)*(-1*_3aae),_3aa9.y+(index/2)*_3aad);
}else{
_3aaf=new Point(_3aa9.x+(index/2)*_3aae,_3aa9.y+(index/2)*(-1*_3aad));
}
conn.addPoint(_3aaf);
conn.addPoint(end);
};
Graphics=function(_348f,_3490,_3491){
this.jsGraphics=_348f;
this.xt=_3491.x;
this.yt=_3491.y;
this.radian=_3490*Math.PI/180;
this.sinRadian=Math.sin(this.radian);
this.cosRadian=Math.cos(this.radian);
};
Graphics.prototype.setStroke=function(x){
this.jsGraphics.setStroke(x);
};
Graphics.prototype.drawLine=function(x1,y1,x2,y2){
var _x1=this.xt+x1*this.cosRadian-y1*this.sinRadian;
var _y1=this.yt+x1*this.sinRadian+y1*this.cosRadian;
var _x2=this.xt+x2*this.cosRadian-y2*this.sinRadian;
var _y2=this.yt+x2*this.sinRadian+y2*this.cosRadian;
this.jsGraphics.drawLine(_x1,_y1,_x2,_y2);
};
Graphics.prototype.fillRect=function(x,y,w,h){
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
Graphics.prototype.fillPolygon=function(_34a7,_34a8){
var rotX=new Array();
var rotY=new Array();
for(var i=0;i<_34a7.length;i++){
rotX[i]=this.xt+_34a7[i]*this.cosRadian-_34a8[i]*this.sinRadian;
rotY[i]=this.yt+_34a7[i]*this.sinRadian+_34a8[i]*this.cosRadian;
}
this.jsGraphics.fillPolygon(rotX,rotY);
};
Graphics.prototype.setColor=function(color){
this.jsGraphics.setColor(color.getHTMLStyle());
};
Graphics.prototype.drawPolygon=function(_34ad,_34ae){
var rotX=new Array();
var rotY=new Array();
for(var i=0;i<_34ad.length;i++){
rotX[i]=this.xt+_34ad[i]*this.cosRadian-_34ae[i]*this.sinRadian;
rotY[i]=this.yt+_34ad[i]*this.sinRadian+_34ae[i]*this.cosRadian;
}
this.jsGraphics.drawPolygon(rotX,rotY);
};
Connection=function(){
Line.call(this);
this.sourcePort=null;
this.targetPort=null;
this.sourceDecorator=null;
this.targetDecorator=null;
this.router=Connection.defaultRouter;
this.lineSegments=new Array();
this.setColor(new Color(0,0,115));
this.setLineWidth(1);
};
Connection.prototype=new Line;
Connection.defaultRouter=new ManhattanConnectionRouter();
Connection.setDefaultRouter=function(_28b0){
Connection.defaultRouter=_28b0;
};
Connection.prototype.disconnect=function(){
if(this.sourcePort!=null){
this.sourcePort.detachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!=null){
this.targetPort.detachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
Connection.prototype.reconnect=function(){
if(this.sourcePort!=null){
this.sourcePort.attachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!=null){
this.targetPort.attachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
Connection.prototype.isResizeable=function(){
return false;
};
Connection.prototype.setSourceDecorator=function(_28b1){
this.sourceDecorator=_28b1;
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setTargetDecorator=function(_28b2){
this.targetDecorator=_28b2;
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setRouter=function(_28b3){
if(_28b3!=null){
this.router=_28b3;
}else{
this.router=new NullConnectionRouter();
}
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.paint=function(){
if(this.graphics==null){
this.graphics=new jsGraphics(this.id);
}else{
this.graphics.clear();
}
this.graphics.setStroke(this.stroke);
this.graphics.setColor(this.lineColor.getHTMLStyle());
this.startStroke();
this.router.route(this);
if(this.getSource().getParent().isMoving==false&&this.getTarget().getParent().isMoving==false){
if(this.targetDecorator!=null){
this.targetDecorator.paint(new Graphics(this.graphics,this.getEndAngle(),this.getEndPoint()));
}
if(this.sourceDecorator!=null){
this.sourceDecorator.paint(new Graphics(this.graphics,this.getStartAngle(),this.getStartPoint()));
}
}
this.finishStroke();
};
Connection.prototype.startStroke=function(){
this.oldPoint=null;
this.lineSegments=new Array();
};
Connection.prototype.finishStroke=function(){
this.graphics.paint();
this.oldPoint=null;
};
Connection.prototype.addPoint=function(p){
p=new Point(parseInt(p.x),parseInt(p.y));
if(this.oldPoint!=null){
this.graphics.drawLine(this.oldPoint.x,this.oldPoint.y,p.x,p.y);
var line=new Object();
line.start=this.oldPoint;
line.end=p;
this.lineSegments.push(line);
}
this.oldPoint=new Object();
this.oldPoint.x=p.x;
this.oldPoint.y=p.y;
};
Connection.prototype.setSource=function(port){
if(this.sourcePort!=null){
this.sourcePort.detachMoveListener(this);
}
this.sourcePort=port;
if(this.sourcePort==null){
return;
}
this.fireSourcePortRouteEvent();
this.sourcePort.attachMoveListener(this);
this.setStartPoint(port.getAbsoluteX(),port.getAbsoluteY());
};
Connection.prototype.getSource=function(){
return this.sourcePort;
};
Connection.prototype.setTarget=function(port){
if(this.targetPort!=null){
this.targetPort.detachMoveListener(this);
}
this.targetPort=port;
if(this.targetPort==null){
return;
}
this.fireTargetPortRouteEvent();
this.targetPort.attachMoveListener(this);
this.setEndPoint(port.getAbsoluteX(),port.getAbsoluteY());
};
Connection.prototype.getTarget=function(){
return this.targetPort;
};
Connection.prototype.onOtherFigureMoved=function(_28b8){
if(_28b8==this.sourcePort){
this.setStartPoint(this.sourcePort.getAbsoluteX(),this.sourcePort.getAbsoluteY());
}else{
this.setEndPoint(this.targetPort.getAbsoluteX(),this.targetPort.getAbsoluteY());
}
};
Connection.prototype.containsPoint=function(px,py){
for(var i=0;i<this.lineSegments.length;i++){
var line=this.lineSegments[i];
if(Line.hit(line.start.x,line.start.y,line.end.x,line.end.y,px,py)){
return true;
}
}
return false;
};
Connection.prototype.getStartAngle=function(){
var p1=this.lineSegments[0].start;
var p2=this.lineSegments[0].end;
if(this.router instanceof BezierConnectionRouter){
p2=this.lineSegments[5].end;
}
var _28bf=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_28bf);
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
Connection.prototype.getEndAngle=function(){
var p1=this.lineSegments[this.lineSegments.length-1].end;
var p2=this.lineSegments[this.lineSegments.length-1].start;
if(this.router instanceof BezierConnectionRouter){
p2=this.lineSegments[this.lineSegments.length-5].end;
}
var _28c3=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_28c3);
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
Connection.prototype.fireSourcePortRouteEvent=function(){
var _28c5=this.sourcePort.getConnections();
for(var i=0;i<_28c5.length;i++){
_28c5[i].paint();
}
};
Connection.prototype.fireTargetPortRouteEvent=function(){
var _28c7=this.targetPort.getConnections();
for(var i=0;i<_28c7.length;i++){
_28c7[i].paint();
}
};
ConnectionDecorator=function(){
};
ConnectionDecorator.prototype.type="ConnectionDecorator";
ConnectionDecorator.prototype.paint=function(g){
};
ArrowConnectionDecorator=function(){
};
ArrowConnectionDecorator.prototype=new ConnectionDecorator;
ArrowConnectionDecorator.prototype.type="ArrowConnectionDecorator";
ArrowConnectionDecorator.prototype.paint=function(g){
g.setColor(new Color(255,128,128));
g.fillPolygon([3,20,20,3],[0,5,-5,0]);
g.setColor(new Color(128,128,255));
g.setStroke(1);
g.drawPolygon([3,20,20,3],[0,5,-5,0]);
};
CompartmentFigure=function(){
Figure.call(this);
this.children=new ArrayList();
this.setBorder(new LineBorder(1));
this.dropable=new DropTarget(this.html);
this.dropable.node=this;
this.dropable.addEventListener("figureenter",function(_3a4e){
_3a4e.target.node.onFigureEnter(_3a4e.relatedTarget.node);
});
this.dropable.addEventListener("figureleave",function(_3a4f){
_3a4f.target.node.onFigureLeave(_3a4f.relatedTarget.node);
});
this.dropable.addEventListener("figuredrop",function(_3a50){
_3a50.target.node.onFigureDrop(_3a50.relatedTarget.node);
});
};
CompartmentFigure.prototype=new Figure;
CompartmentFigure.prototype.type="CompartmentFigure";
CompartmentFigure.prototype.onFigureEnter=function(_3a51){
};
CompartmentFigure.prototype.onFigureLeave=function(_3a52){
};
CompartmentFigure.prototype.onFigureDrop=function(_3a53){
};
CompartmentFigure.prototype.getChildren=function(){
var _3a54=new Array();
for(var i=0;i<this.children.getSize();i++){
_3a54.push(this.children.get(i));
}
return _3a54;
};
CompartmentFigure.prototype.addChild=function(_3a56){
_3a56.setZOrder(this.getZOrder()+1);
_3a56.setParent(this);
if(this.children.indexOf(_3a56)!=-1){
alert("error");
}
this.children.add(_3a56);
};
CompartmentFigure.prototype.removeChild=function(_3a57){
_3a57.setParent(null);
this.children.remove(_3a57);
};
CompartmentFigure.prototype.setZOrder=function(index){
Figure.prototype.setZOrder.call(this,index);
for(var i=0;i<this.children.getSize();i++){
this.children.get(i).setZOrder(index+1);
}
};
CompartmentFigure.prototype.setPosition=function(xPos,yPos){
var oldX=this.getX();
var oldY=this.getY();
Figure.prototype.setPosition.call(this,xPos,yPos);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
CompartmentFigure.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Figure.prototype.onDrag.call(this);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
Document=function(_3474){
this.canvas=_3474;
};
Document.prototype.getFigures=function(){
var _3475=new Array();
var _3476=this.canvas.figures;
var _3477=this.canvas.dialogs;
for(var i=0;i<_3476.getSize();i++){
var _3479=_3476.get(i);
if(_3477.indexOf(_3479)==-1&&_3479.getParent()==null&&!(_3479 instanceof Window)){
_3475.push(_3479);
}
}
return _3475;
};
Document.prototype.getLines=function(){
var _347a=new Array();
var lines=this.canvas.getLines();
for(var i=0;i<lines.getSize();i++){
_347a.push(lines.get(i));
}
return _347a;
};
Annotation=function(msg){
this.msg=msg;
this.color=new Color(0,0,0);
this.bgColor=new Color(241,241,121);
this.fontSize=10;
this.textNode=null;
Figure.call(this);
};
Annotation.prototype=new Figure;
Annotation.prototype.type="Annotation";
Annotation.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
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
item.style.MozUserSelect="none";
item.style.cursor="default";
item.style.zIndex=(Figure.ZOrderIndex-1);
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
this.disableTextSelection(item);
return item;
};
Annotation.prototype.onDoubleClick=function(){
var _3a75=new AnnotationDialog(this);
this.workflow.showDialog(_3a75);
};
Annotation.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!=null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
}
};
Annotation.prototype.getBackgroundColor=function(){
return this.bgColor;
};
Annotation.prototype.setFontSize=function(size){
this.fontSize=size;
this.html.style.fontSize=this.fontSize+"pt";
};
Annotation.prototype.getText=function(){
return this.msg;
};
Annotation.prototype.setText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createTextNode(this.msg);
this.html.appendChild(this.textNode);
};
Annotation.prototype.setStyledText=function(text){
this.msg=text;
this.html.removeChild(this.textNode);
this.textNode=document.createElement("div");
this.textNode.innerHTML=text;
this.html.appendChild(this.textNode);
};
ResizeHandle=function(_289a,type){
Rectangle.call(this,5,5);
this.type=type;
var _289c=this.getWidth();
var _289d=_289c/2;
switch(this.type){
case 1:
this.setSnapToGridAnchor(new Point(_289c,_289c));
break;
case 2:
this.setSnapToGridAnchor(new Point(_289d,_289c));
break;
case 3:
this.setSnapToGridAnchor(new Point(0,_289c));
break;
case 4:
this.setSnapToGridAnchor(new Point(0,_289d));
break;
case 5:
this.setSnapToGridAnchor(new Point(0,0));
break;
case 6:
this.setSnapToGridAnchor(new Point(_289d,0));
break;
case 7:
this.setSnapToGridAnchor(new Point(_289c,0));
break;
case 8:
this.setSnapToGridAnchor(new Point(_289c,_289d));
break;
}
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_289a);
this.setZOrder(10000);
};
ResizeHandle.prototype=new Rectangle;
ResizeHandle.prototype.type="ResizeHandle";
ResizeHandle.prototype.getSnapToDirection=function(){
switch(this.type){
case 1:
return SnapToHelper.NORTH_WEST;
case 2:
return SnapToHelper.NORTH;
case 3:
return SnapToHelper.NORTH_EAST;
case 4:
return SnapToHelper.EAST;
case 5:
return SnapToHelper.SOUTH_EAST;
case 6:
return SnapToHelper.SOUTH;
case 7:
return SnapToHelper.SOUTH_WEST;
case 8:
return SnapToHelper.WEST;
}
};
ResizeHandle.prototype.onDragend=function(){
if(this.commandMove==null){
return;
}
var _289e=this.workflow.currentSelection;
this.commandMove.setPosition(_289e.getX(),_289e.getY());
this.commandResize.setDimension(_289e.getWidth(),_289e.getHeight());
this.workflow.getCommandStack().execute(this.commandResize);
this.workflow.getCommandStack().execute(this.commandMove);
this.commandMove=null;
this.commandResize=null;
this.workflow.hideSnapToHelperLines();
};
ResizeHandle.prototype.setPosition=function(xPos,yPos){
this.x=xPos;
this.y=yPos;
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
ResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var _28a3=this.workflow.currentSelection;
this.commandMove=new CommandMove(_28a3,_28a3.getX(),_28a3.getY());
this.commandResize=new CommandResize(_28a3,_28a3.getWidth(),_28a3.getHeight());
return true;
};
ResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _28a8=this.workflow.currentSelection.getX();
var _28a9=this.workflow.currentSelection.getY();
var _28aa=this.workflow.currentSelection.getWidth();
var _28ab=this.workflow.currentSelection.getHeight();
switch(this.type){
case 1:
this.workflow.currentSelection.setPosition(_28a8-diffX,_28a9-diffY);
this.workflow.currentSelection.setDimension(_28aa+diffX,_28ab+diffY);
break;
case 2:
this.workflow.currentSelection.setPosition(_28a8,_28a9-diffY);
this.workflow.currentSelection.setDimension(_28aa,_28ab+diffY);
break;
case 3:
this.workflow.currentSelection.setPosition(_28a8,_28a9-diffY);
this.workflow.currentSelection.setDimension(_28aa-diffX,_28ab+diffY);
break;
case 4:
this.workflow.currentSelection.setPosition(_28a8,_28a9);
this.workflow.currentSelection.setDimension(_28aa-diffX,_28ab);
break;
case 5:
this.workflow.currentSelection.setPosition(_28a8,_28a9);
this.workflow.currentSelection.setDimension(_28aa-diffX,_28ab-diffY);
break;
case 6:
this.workflow.currentSelection.setPosition(_28a8,_28a9);
this.workflow.currentSelection.setDimension(_28aa,_28ab-diffY);
break;
case 7:
this.workflow.currentSelection.setPosition(_28a8-diffX,_28a9);
this.workflow.currentSelection.setDimension(_28aa+diffX,_28ab-diffY);
break;
case 8:
this.workflow.currentSelection.setPosition(_28a8-diffX,_28a9);
this.workflow.currentSelection.setDimension(_28aa+diffX,_28ab);
break;
}
this.workflow.moveResizeHandles(this.workflow.getCurrentSelection());
};
ResizeHandle.prototype.setCanDrag=function(flag){
Rectangle.prototype.setCanDrag.call(this,flag);
if(!flag){
this.html.style.cursor="";
return;
}
switch(this.type){
case 1:
this.html.style.cursor="nw-resize";
break;
case 2:
this.html.style.cursor="n-resize";
break;
case 3:
this.html.style.cursor="ne-resize";
break;
case 4:
this.html.style.cursor="w-resize";
break;
case 5:
this.html.style.cursor="nw-resize";
break;
case 6:
this.html.style.cursor="n-resize";
break;
case 7:
this.html.style.cursor="ne-resize";
break;
case 8:
this.html.style.cursor="w-resize";
break;
}
};
ResizeHandle.prototype.onKeyDown=function(_28ad,ctrl){
this.workflow.onKeyDown(_28ad,ctrl);
};
ResizeHandle.prototype.fireMoveEvent=function(){
};
LineStartResizeHandle=function(_3acb){
Rectangle.call(this);
this.setDimension(5,5);
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_3acb);
this.setZOrder(10000);
};
LineStartResizeHandle.prototype=new Rectangle;
LineStartResizeHandle.prototype.type="LineStartResizeHandle";
LineStartResizeHandle.prototype.onDragend=function(){
if(this.command==null){
return;
}
var line=this.workflow.currentSelection;
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command.setEndPoints(x1,y1,x2,y2);
this.workflow.getCommandStack().execute(this.command);
this.command=null;
};
LineStartResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var line=this.workflow.currentSelection;
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command=new CommandMoveLine(line,x1,y1,x2,y2);
return true;
};
LineStartResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _3adc=this.workflow.currentSelection.getStartX();
var _3add=this.workflow.currentSelection.getStartY();
this.workflow.currentSelection.setStartPoint(_3adc-diffX,_3add-diffY);
};
LineStartResizeHandle.prototype.onKeyDown=function(_3ade,ctrl){
if(this.workflow!=null){
this.workflow.onKeyDown(_3ade,ctrl);
}
};
LineStartResizeHandle.prototype.fireMoveEvent=function(){
};
LineEndResizeHandle=function(_3921){
Rectangle.call(this);
this.setDimension(5,5);
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_3921);
this.setZOrder(10000);
};
LineEndResizeHandle.prototype=new Rectangle;
LineEndResizeHandle.prototype.type="LineEndResizeHandle";
LineEndResizeHandle.prototype.onDragend=function(){
if(this.command==null){
return;
}
var line=this.workflow.currentSelection;
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command.setEndPoints(x1,y1,x2,y2);
this.workflow.getCommandStack().execute(this.command);
this.command=null;
};
LineEndResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var line=this.workflow.currentSelection;
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command=new CommandMoveLine(line,x1,y1,x2,y2);
return true;
};
LineEndResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _3932=this.workflow.currentSelection.getEndX();
var _3933=this.workflow.currentSelection.getEndY();
this.workflow.currentSelection.setEndPoint(_3932-diffX,_3933-diffY);
};
LineEndResizeHandle.prototype.onKeyDown=function(_3934){
if(this.workflow!=null){
this.workflow.onKeyDown(_3934);
}
};
LineEndResizeHandle.prototype.fireMoveEvent=function(){
};
Canvas=function(_386b){
if(_386b){
this.construct(_386b);
}
this.enableSmoothFigureHandling=false;
};
Canvas.prototype.type="Canvas";
Canvas.prototype.construct=function(_386c){
this.canvasId=_386c;
this.html=document.getElementById(this.canvasId);
this.scrollArea=document.body.parentNode;
};
Canvas.prototype.setViewPort=function(divId){
this.scrollArea=document.getElementById(divId);
};
Canvas.prototype.addFigure=function(_386e,xPos,yPos,_3871){
if(this.enableSmoothFigureHandling==true){
if(_386e.timer<=0){
_386e.setAlpha(0.001);
}
var _3872=_386e;
var _3873=function(){
if(_3872.alpha<1){
_3872.setAlpha(Math.min(1,_3872.alpha+0.05));
}else{
window.clearInterval(_3872.timer);
_3872.timer=-1;
}
};
if(_3872.timer>0){
window.clearInterval(_3872.timer);
}
_3872.timer=window.setInterval(_3873,30);
}
_386e.setCanvas(this);
if(xPos&&yPos){
_386e.setPosition(xPos,yPos);
}
this.html.appendChild(_386e.getHTMLElement());
if(!_3871){
_386e.paint();
}
};
Canvas.prototype.removeFigure=function(_3874){
if(this.enableSmoothFigureHandling==true){
var oThis=this;
var _3876=_3874;
var _3877=function(){
if(_3876.alpha>0){
_3876.setAlpha(Math.max(0,_3876.alpha-0.05));
}else{
window.clearInterval(_3876.timer);
_3876.timer=-1;
oThis.html.removeChild(_3876.html);
_3876.setCanvas(null);
}
};
if(_3876.timer>0){
window.clearInterval(_3876.timer);
}
_3876.timer=window.setInterval(_3877,20);
}else{
this.html.removeChild(_3874.html);
_3874.setCanvas(null);
}
};
Canvas.prototype.getEnableSmoothFigureHandling=function(){
return this.enableSmoothFigureHandling;
};
Canvas.prototype.setEnableSmoothFigureHandling=function(flag){
this.enableSmoothFigureHandling=flag;
};
Canvas.prototype.getWidth=function(){
return parseInt(this.html.style.width);
};
Canvas.prototype.getHeight=function(){
return parseInt(this.html.style.height);
};
Canvas.prototype.setBackgroundImage=function(_3879,_387a){
if(_3879!=null){
if(_387a){
this.html.style.background="transparent url("+_3879+") ";
}else{
this.html.style.background="transparent url("+_3879+") no-repeat";
}
}else{
this.html.style.background="transparent";
}
};
Canvas.prototype.getY=function(){
return this.y;
};
Canvas.prototype.getX=function(){
return this.x;
};
Canvas.prototype.getAbsoluteY=function(){
var el=this.html;
var ot=el.offsetTop;
while((el=el.offsetParent)!=null){
ot+=el.offsetTop;
}
return ot;
};
Canvas.prototype.getAbsoluteX=function(){
var el=this.html;
var ol=el.offsetLeft;
while((el=el.offsetParent)!=null){
ol+=el.offsetLeft;
}
return ol;
};
Canvas.prototype.getScrollLeft=function(){
return this.scrollArea.scrollLeft;
};
Canvas.prototype.getScrollTop=function(){
return this.scrollArea.scrollTop;
};
Workflow=function(id){
if(!id){
return;
}
this.gridWidthX=10;
this.gridWidthY=10;
this.snapToGridHelper=null;
this.verticalSnapToHelperLine=null;
this.horizontalSnapToHelperLine=null;
this.figures=new ArrayList();
this.lines=new ArrayList();
this.commonPorts=new ArrayList();
this.dropTargets=new ArrayList();
this.compartments=new ArrayList();
this.selectionListeners=new ArrayList();
this.dialogs=new ArrayList();
this.toolPalette=null;
this.dragging=false;
this.draggingLine=null;
this.commandStack=new CommandStack();
this.oldScrollPosLeft=0;
this.oldScrollPosTop=0;
this.currentSelection=null;
this.currentMenu=null;
this.connectionLine=new Line();
this.resizeHandleStart=new LineStartResizeHandle(this);
this.resizeHandleEnd=new LineEndResizeHandle(this);
this.resizeHandle1=new ResizeHandle(this,1);
this.resizeHandle2=new ResizeHandle(this,2);
this.resizeHandle3=new ResizeHandle(this,3);
this.resizeHandle4=new ResizeHandle(this,4);
this.resizeHandle5=new ResizeHandle(this,5);
this.resizeHandle6=new ResizeHandle(this,6);
this.resizeHandle7=new ResizeHandle(this,7);
this.resizeHandle8=new ResizeHandle(this,8);
this.resizeHandleHalfWidth=parseInt(this.resizeHandle2.getWidth()/2);
Canvas.call(this,id);
this.setPanning(false);
if(this.html!=null){
this.html.style.backgroundImage="url(grid_10.png)";
oThis=this;
this.html.tabIndex="0";
var _3ae5=function(){
var _3ae6=arguments[0]||window.event;
var diffX=_3ae6.clientX;
var diffY=_3ae6.clientY;
var _3ae9=oThis.getScrollLeft();
var _3aea=oThis.getScrollTop();
var _3aeb=oThis.getAbsoluteX();
var _3aec=oThis.getAbsoluteY();
if(oThis.getBestFigure(diffX+_3ae9-_3aeb,diffY+_3aea-_3aec)!=null){
return;
}
var line=oThis.getBestLine(diffX+_3ae9-_3aeb,diffY+_3aea-_3aec,null);
if(line!=null){
line.onContextMenu(diffX+_3ae9-_3aeb,diffY+_3aea-_3aec);
}else{
oThis.onContextMenu(diffX+_3ae9-_3aeb,diffY+_3aea-_3aec);
}
};
this.html.oncontextmenu=function(){
return false;
};
var oThis=this;
var _3aef=function(event){
var ctrl=event.ctrlKey;
oThis.onKeyDown(event.keyCode,ctrl);
};
var _3af2=function(){
var _3af3=arguments[0]||window.event;
var diffX=_3af3.clientX;
var diffY=_3af3.clientY;
var _3af6=oThis.getScrollLeft();
var _3af7=oThis.getScrollTop();
var _3af8=oThis.getAbsoluteX();
var _3af9=oThis.getAbsoluteY();
oThis.onMouseDown(diffX+_3af6-_3af8,diffY+_3af7-_3af9);
};
var _3afa=function(){
var _3afb=arguments[0]||window.event;
if(oThis.currentMenu!=null){
oThis.removeFigure(oThis.currentMenu);
oThis.currentMenu=null;
}
if(_3afb.button==2){
return;
}
var diffX=_3afb.clientX;
var diffY=_3afb.clientY;
var _3afe=oThis.getScrollLeft();
var _3aff=oThis.getScrollTop();
var _3b00=oThis.getAbsoluteX();
var _3b01=oThis.getAbsoluteY();
oThis.onMouseUp(diffX+_3afe-_3b00,diffY+_3aff-_3b01);
};
var _3b02=function(){
var _3b03=arguments[0]||window.event;
if(Drag.currentHover!=null){
var _3b04=new DragDropEvent();
_3b04.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_3b04);
Drag.currentHover=null;
}else{
var diffX=_3b03.clientX;
var diffY=_3b03.clientY;
var _3b07=oThis.getScrollLeft();
var _3b08=oThis.getScrollTop();
var _3b09=oThis.getAbsoluteX();
var _3b0a=oThis.getAbsoluteY();
oThis.onMouseMove(diffX+_3b07-_3b09,diffY+_3b08-_3b0a);
}
};
var _3b0b=function(_3b0c){
var _3b0c=arguments[0]||window.event;
var diffX=_3b0c.clientX;
var diffY=_3b0c.clientY;
var _3b0f=oThis.getScrollLeft();
var _3b10=oThis.getScrollTop();
var _3b11=oThis.getAbsoluteX();
var _3b12=oThis.getAbsoluteY();
var line=oThis.getBestLine(diffX+_3b0f-_3b11,diffY+_3b10-_3b12,null);
if(line!=null){
line.onDoubleClick();
}
};
if(this.html.addEventListener){
this.html.addEventListener("contextmenu",_3ae5,false);
this.html.addEventListener("mousemove",_3b02,false);
this.html.addEventListener("mouseup",_3afa,false);
this.html.addEventListener("mousedown",_3af2,false);
this.html.addEventListener("keydown",_3aef,false);
this.html.addEventListener("dblclick",_3b0b,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("oncontextmenu",_3ae5);
this.html.attachEvent("onmousemove",_3b02);
this.html.attachEvent("onmousedown",_3af2);
this.html.attachEvent("onmouseup",_3afa);
this.html.attachEvent("onkeydown",_3aef);
this.html.attachEvent("ondblclick",_3b0b);
}else{
throw new Error("Open-jACOB.Graphics not supported in this browser.");
}
}
}
};
Workflow.prototype=new Canvas;
Workflow.prototype.type="Workflow";
Workflow.prototype.onScroll=function(){
var _3b14=this.getScrollLeft();
var _3b15=this.getScrollTop();
var _3b16=_3b14-this.oldScrollPosLeft;
var _3b17=_3b15-this.oldScrollPosTop;
for(var i=0;i<this.figures.getSize();i++){
var _3b19=this.figures.get(i);
if(_3b19.hasFixedPosition&&_3b19.hasFixedPosition()==true){
_3b19.setPosition(_3b19.getX()+_3b16,_3b19.getY()+_3b17);
}
}
this.oldScrollPosLeft=_3b14;
this.oldScrollPosTop=_3b15;
};
Workflow.prototype.setPanning=function(flag){
this.panning=flag;
if(flag){
this.html.style.cursor="move";
}else{
this.html.style.cursor="default";
}
};
Workflow.prototype.scrollTo=function(x,y,fast){
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
Workflow.prototype.showDialog=function(_3b24,xPos,yPos){
if(xPos){
this.addFigure(_3b24,xPos,yPos);
}else{
this.addFigure(_3b24,200,100);
}
this.dialogs.add(_3b24);
};
Workflow.prototype.showMenu=function(menu,xPos,yPos){
if(this.menu!=null){
this.html.removeChild(this.menu.getHTMLElement());
this.menu.setWorkflow();
}
this.menu=menu;
if(this.menu!=null){
this.menu.setWorkflow(this);
this.menu.setPosition(xPos,yPos);
this.html.appendChild(this.menu.getHTMLElement());
this.menu.paint();
}
};
Workflow.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!=null){
this.showMenu(menu,x,y);
}
};
Workflow.prototype.getContextMenu=function(){
return null;
};
Workflow.prototype.setToolWindow=function(_3b2d,x,y){
this.toolPalette=_3b2d;
if(y){
this.addFigure(_3b2d,x,y);
}else{
this.addFigure(_3b2d,20,20);
}
this.dialogs.add(_3b2d);
};
Workflow.prototype.setSnapToGrid=function(flag){
if(flag){
this.snapToGridHelper=new SnapToGrid(this);
}else{
this.snapToGridHelper=null;
}
};
Workflow.prototype.setSnapToGeometry=function(flag){
if(flag){
this.snapToGeometryHelper=new SnapToGeometry(this);
}else{
this.snapToGeometryHelper=null;
}
};
Workflow.prototype.setGridWidth=function(dx,dy){
this.gridWidthX=dx;
this.gridWidthY=dy;
};
Workflow.prototype.addFigure=function(_3b34,xPos,yPos){
_3b34.setWorkflow(this);
Canvas.prototype.addFigure.call(this,_3b34,xPos,yPos,true);
var _3b37=this;
if(_3b34 instanceof CompartmentFigure){
this.compartments.add(_3b34);
}
if(_3b34 instanceof Line){
this.lines.add(_3b34);
}else{
this.figures.add(_3b34);
_3b34.draggable.addEventListener("dragend",function(_3b38){
});
_3b34.draggable.addEventListener("dragstart",function(_3b39){
var _3b3a=_3b37.getFigure(_3b39.target.element.id);
if(_3b3a==null){
return;
}
if(_3b3a.isSelectable()==false){
return;
}
_3b37.showResizeHandles(_3b3a);
_3b37.setCurrentSelection(_3b3a);
});
_3b34.draggable.addEventListener("drag",function(_3b3b){
var _3b3c=_3b37.getFigure(_3b3b.target.element.id);
if(_3b3c==null){
return;
}
if(_3b3c.isSelectable()==false){
return;
}
_3b37.moveResizeHandles(_3b3c);
});
}
_3b34.paint();
this.setDocumentDirty();
};
Workflow.prototype.removeFigure=function(_3b3d){
Canvas.prototype.removeFigure.call(this,_3b3d);
this.figures.remove(_3b3d);
this.lines.remove(_3b3d);
this.dialogs.remove(_3b3d);
_3b3d.setWorkflow(null);
if(_3b3d instanceof CompartmentFigure){
this.compartments.remove(_3b3d);
}
if(_3b3d instanceof Connection){
_3b3d.disconnect();
}
if(this.currentSelection==_3b3d){
this.setCurrentSelection(null);
}
this.setDocumentDirty();
};
Workflow.prototype.moveFront=function(_3b3e){
this.html.removeChild(_3b3e.getHTMLElement());
this.html.appendChild(_3b3e.getHTMLElement());
};
Workflow.prototype.moveBack=function(_3b3f){
this.html.removeChild(_3b3f.getHTMLElement());
this.html.insertBefore(_3b3f.getHTMLElement(),this.html.firstChild);
};
Workflow.prototype.getBestCompartmentFigure=function(x,y,_3b42){
var _3b43=null;
for(var i=0;i<this.figures.getSize();i++){
var _3b45=this.figures.get(i);
if((_3b45 instanceof CompartmentFigure)&&_3b45.isOver(x,y)==true&&_3b45!=_3b42){
if(_3b43==null){
_3b43=_3b45;
}else{
if(_3b43.getZOrder()<_3b45.getZOrder()){
_3b43=_3b45;
}
}
}
}
return _3b43;
};
Workflow.prototype.getBestFigure=function(x,y,_3b48){
var _3b49=null;
for(var i=0;i<this.figures.getSize();i++){
var _3b4b=this.figures.get(i);
if(_3b4b.isOver(x,y)==true&&_3b4b!=_3b48){
if(_3b49==null){
_3b49=_3b4b;
}else{
if(_3b49.getZOrder()<_3b4b.getZOrder()){
_3b49=_3b4b;
}
}
}
}
return _3b49;
};
Workflow.prototype.getBestLine=function(x,y,_3b4e){
var _3b4f=null;
for(var i=0;i<this.lines.getSize();i++){
var line=this.lines.get(i);
if(line.containsPoint(x,y)==true&&line!=_3b4e){
if(_3b4f==null){
_3b4f=line;
}else{
if(_3b4f.getZOrder()<line.getZOrder()){
_3b4f=line;
}
}
}
}
return _3b4f;
};
Workflow.prototype.getFigure=function(id){
for(var i=0;i<this.figures.getSize();i++){
var _3b54=this.figures.get(i);
if(_3b54.id==id){
return _3b54;
}
}
return null;
};
Workflow.prototype.getFigures=function(){
return this.figures;
};
Workflow.prototype.getDocument=function(){
return new Document(this);
};
Workflow.prototype.addSelectionListener=function(w){
this.selectionListeners.add(w);
};
Workflow.prototype.removeSelectionListener=function(w){
this.selectionListeners.remove(w);
};
Workflow.prototype.setCurrentSelection=function(_3b57){
if(_3b57==null){
this.hideResizeHandles();
this.hideLineResizeHandles();
}
this.currentSelection=_3b57;
for(var i=0;i<this.selectionListeners.getSize();i++){
var w=this.selectionListeners.get(i);
if(w!=null&&w.onSelectionChanged){
w.onSelectionChanged(this.currentSelection);
}
}
};
Workflow.prototype.getCurrentSelection=function(){
return this.currentSelection;
};
Workflow.prototype.getLines=function(){
return this.lines;
};
Workflow.prototype.registerPort=function(port){
this.commonPorts.add(port);
port.draggable.targets=this.dropTargets;
this.dropTargets.add(port.dropable);
};
Workflow.prototype.unregisterPort=function(port){
port.targets=null;
this.commonPorts.remove(port);
this.dropTargets.remove(port);
};
Workflow.prototype.getCommandStack=function(){
return this.commandStack;
};
Workflow.prototype.showConnectionLine=function(x1,y1,x2,y2){
this.connectionLine.setStartPoint(x1,y1);
this.connectionLine.setEndPoint(x2,y2);
if(this.connectionLine.canvas==null){
Canvas.prototype.addFigure.call(this,this.connectionLine);
}
};
Workflow.prototype.hideConnectionLine=function(){
if(this.connectionLine.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.connectionLine);
}
};
Workflow.prototype.showLineResizeHandles=function(_3b60){
var _3b61=this.resizeHandleStart.getWidth()/2;
var _3b62=this.resizeHandleStart.getHeight()/2;
Canvas.prototype.addFigure.call(this,this.resizeHandleStart,_3b60.getStartX()-_3b61,_3b60.getStartY()-_3b61);
Canvas.prototype.addFigure.call(this,this.resizeHandleEnd,_3b60.getEndX()-_3b61,_3b60.getEndY()-_3b61);
this.resizeHandleStart.setCanDrag(_3b60.isResizeable());
this.resizeHandleEnd.setCanDrag(_3b60.isResizeable());
if(_3b60.isResizeable()){
var green=new Color(0,255,0);
this.resizeHandleStart.setBackgroundColor(green);
this.resizeHandleEnd.setBackgroundColor(green);
}else{
this.resizeHandleStart.setBackgroundColor(null);
this.resizeHandleEnd.setBackgroundColor(null);
}
};
Workflow.prototype.hideLineResizeHandles=function(){
if(this.resizeHandleStart.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandleStart);
}
if(this.resizeHandleEnd.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandleEnd);
}
};
Workflow.prototype.showResizeHandles=function(_3b64){
this.hideLineResizeHandles();
this.hideResizeHandles();
if(this.getEnableSmoothFigureHandling()==true&&this.getCurrentSelection()!=_3b64){
this.resizeHandle1.setAlpha(0.01);
this.resizeHandle2.setAlpha(0.01);
this.resizeHandle3.setAlpha(0.01);
this.resizeHandle4.setAlpha(0.01);
this.resizeHandle5.setAlpha(0.01);
this.resizeHandle6.setAlpha(0.01);
this.resizeHandle7.setAlpha(0.01);
this.resizeHandle8.setAlpha(0.01);
}
var _3b65=this.resizeHandle1.getWidth();
var _3b66=this.resizeHandle1.getHeight();
var _3b67=_3b64.getHeight();
var _3b68=_3b64.getWidth();
var xPos=_3b64.getX();
var yPos=_3b64.getY();
Canvas.prototype.addFigure.call(this,this.resizeHandle1,xPos-_3b65,yPos-_3b66);
Canvas.prototype.addFigure.call(this,this.resizeHandle3,xPos+_3b68,yPos-_3b66);
Canvas.prototype.addFigure.call(this,this.resizeHandle5,xPos+_3b68,yPos+_3b67);
Canvas.prototype.addFigure.call(this,this.resizeHandle7,xPos-_3b65,yPos+_3b67);
this.resizeHandle1.setCanDrag(_3b64.isResizeable());
this.resizeHandle3.setCanDrag(_3b64.isResizeable());
this.resizeHandle5.setCanDrag(_3b64.isResizeable());
this.resizeHandle7.setCanDrag(_3b64.isResizeable());
if(_3b64.isResizeable()){
var green=new Color(0,255,0);
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
if(_3b64.isStrechable()&&_3b64.isResizeable()){
Canvas.prototype.addFigure.call(this,this.resizeHandle2,xPos+(_3b68/2)-this.resizeHandleHalfWidth,yPos-_3b66);
Canvas.prototype.addFigure.call(this,this.resizeHandle4,xPos+_3b68,yPos+(_3b67/2)-(_3b66/2));
Canvas.prototype.addFigure.call(this,this.resizeHandle6,xPos+(_3b68/2)-this.resizeHandleHalfWidth,yPos+_3b67);
Canvas.prototype.addFigure.call(this,this.resizeHandle8,xPos-_3b65,yPos+(_3b67/2)-(_3b66/2));
}
};
Workflow.prototype.hideResizeHandles=function(){
if(this.resizeHandle1.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle1);
}
if(this.resizeHandle2.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle2);
}
if(this.resizeHandle3.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle3);
}
if(this.resizeHandle4.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle4);
}
if(this.resizeHandle5.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle5);
}
if(this.resizeHandle6.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle6);
}
if(this.resizeHandle7.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle7);
}
if(this.resizeHandle8.canvas!=null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle8);
}
};
Workflow.prototype.moveResizeHandles=function(_3b6c){
var _3b6d=this.resizeHandle1.getWidth();
var _3b6e=this.resizeHandle1.getHeight();
var _3b6f=_3b6c.getHeight();
var _3b70=_3b6c.getWidth();
var xPos=_3b6c.getX();
var yPos=_3b6c.getY();
this.resizeHandle1.setPosition(xPos-_3b6d,yPos-_3b6e);
this.resizeHandle3.setPosition(xPos+_3b70,yPos-_3b6e);
this.resizeHandle5.setPosition(xPos+_3b70,yPos+_3b6f);
this.resizeHandle7.setPosition(xPos-_3b6d,yPos+_3b6f);
if(_3b6c.isStrechable()){
this.resizeHandle2.setPosition(xPos+(_3b70/2)-this.resizeHandleHalfWidth,yPos-_3b6e);
this.resizeHandle4.setPosition(xPos+_3b70,yPos+(_3b6f/2)-(_3b6e/2));
this.resizeHandle6.setPosition(xPos+(_3b70/2)-this.resizeHandleHalfWidth,yPos+_3b6f);
this.resizeHandle8.setPosition(xPos-_3b6d,yPos+(_3b6f/2)-(_3b6e/2));
}
};
Workflow.prototype.onMouseDown=function(x,y){
this.dragging=true;
this.mouseDownPosX=x;
this.mouseDownPosY=y;
if(this.toolPalette!=null&&this.toolPalette.getActiveTool()!=null){
this.toolPalette.getActiveTool().execute(x,y);
}
this.setCurrentSelection(null);
this.showMenu(null);
for(var i=0;i<this.getLines().getSize();i++){
var line=this.lines.get(i);
if(line.containsPoint(x,y)&&line.isSelectable()){
this.hideResizeHandles();
this.setCurrentSelection(line);
this.showLineResizeHandles(this.currentSelection);
if(line instanceof Line&&!(line instanceof Connection)){
this.draggingLine=line;
}
break;
}
}
};
Workflow.prototype.onMouseUp=function(x,y){
this.dragging=false;
this.draggingLine=null;
};
Workflow.prototype.onMouseMove=function(x,y){
if(this.dragging==true&&this.draggingLine!=null){
var diffX=x-this.mouseDownPosX;
var diffY=y-this.mouseDownPosY;
this.draggingLine.startX=this.draggingLine.getStartX()+diffX;
this.draggingLine.startY=this.draggingLine.getStartY()+diffY;
this.draggingLine.setEndPoint(this.draggingLine.getEndX()+diffX,this.draggingLine.getEndY()+diffY);
this.mouseDownPosX=x;
this.mouseDownPosY=y;
this.showLineResizeHandles(this.currentSelection);
}else{
if(this.dragging==true&&this.panning==true){
var diffX=x-this.mouseDownPosX;
var diffY=y-this.mouseDownPosY;
this.scrollTo(this.getScrollLeft()-diffX,this.getScrollTop()-diffY,true);
this.onScroll();
}
}
};
Workflow.prototype.onKeyDown=function(_3b7d,ctrl){
if(_3b7d==46&&this.currentSelection!=null&&this.currentSelection.isDeleteable()){
this.commandStack.execute(new CommandDelete(this.currentSelection));
}else{
if(_3b7d==90&&ctrl){
this.commandStack.undo();
}else{
if(_3b7d==89&&ctrl){
this.commandStack.redo();
}
}
}
};
Workflow.prototype.setDocumentDirty=function(){
for(var i=0;i<this.dialogs.getSize();i++){
var d=this.dialogs.get(i);
if(d!=null&&d.onSetDocumentDirty){
d.onSetDocumentDirty();
}
}
if(this.snapToGeometryHelper!=null){
this.snapToGeometryHelper.onSetDocumentDirty();
}
if(this.snapToGridHelper!=null){
this.snapToGridHelper.onSetDocumentDirty();
}
};
Workflow.prototype.snapToHelper=function(_3b81,pos){
if(this.snapToGeometryHelper!=null){
if(_3b81 instanceof ResizeHandle){
var _3b83=_3b81.getSnapToGridAnchor();
pos.x+=_3b83.x;
pos.y+=_3b83.y;
var _3b84=new Point(pos.x,pos.y);
var _3b85=_3b81.getSnapToDirection();
var _3b86=this.snapToGeometryHelper.snapPoint(_3b85,pos,_3b84);
if((_3b85&SnapToHelper.EAST_WEST)&&!(_3b86&SnapToHelper.EAST_WEST)){
this.showSnapToHelperLineVertical(_3b84.x);
}else{
this.hideSnapToHelperLineVertical();
}
if((_3b85&SnapToHelper.NORTH_SOUTH)&&!(_3b86&SnapToHelper.NORTH_SOUTH)){
this.showSnapToHelperLineHorizontal(_3b84.y);
}else{
this.hideSnapToHelperLineHorizontal();
}
_3b84.x-=_3b83.x;
_3b84.y-=_3b83.y;
return _3b84;
}else{
var _3b87=new Dimension(pos.x,pos.y,_3b81.getWidth(),_3b81.getHeight());
var _3b84=new Dimension(pos.x,pos.y,_3b81.getWidth(),_3b81.getHeight());
var _3b85=SnapToHelper.NSEW;
var _3b86=this.snapToGeometryHelper.snapRectangle(_3b87,_3b84);
if((_3b85&SnapToHelper.WEST)&&!(_3b86&SnapToHelper.WEST)){
this.showSnapToHelperLineVertical(_3b84.x);
}else{
if((_3b85&SnapToHelper.EAST)&&!(_3b86&SnapToHelper.EAST)){
this.showSnapToHelperLineVertical(_3b84.getX()+_3b84.getWidth());
}else{
this.hideSnapToHelperLineVertical();
}
}
if((_3b85&SnapToHelper.NORTH)&&!(_3b86&SnapToHelper.NORTH)){
this.showSnapToHelperLineHorizontal(_3b84.y);
}else{
if((_3b85&SnapToHelper.SOUTH)&&!(_3b86&SnapToHelper.SOUTH)){
this.showSnapToHelperLineHorizontal(_3b84.getY()+_3b84.getHeight());
}else{
this.hideSnapToHelperLineHorizontal();
}
}
return _3b84.getTopLeft();
}
}else{
if(this.snapToGridHelper!=null){
var _3b83=_3b81.getSnapToGridAnchor();
pos.x=pos.x+_3b83.x;
pos.y=pos.y+_3b83.y;
var _3b84=new Point(pos.x,pos.y);
this.snapToGridHelper.snapPoint(0,pos,_3b84);
_3b84.x=_3b84.x-_3b83.x;
_3b84.y=_3b84.y-_3b83.y;
return _3b84;
}
}
return pos;
};
Workflow.prototype.showSnapToHelperLineHorizontal=function(_3b88){
if(this.horizontalSnapToHelperLine==null){
this.horizontalSnapToHelperLine=new Line();
this.horizontalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.horizontalSnapToHelperLine);
}
this.horizontalSnapToHelperLine.setStartPoint(0,_3b88);
this.horizontalSnapToHelperLine.setEndPoint(this.getWidth(),_3b88);
};
Workflow.prototype.showSnapToHelperLineVertical=function(_3b89){
if(this.verticalSnapToHelperLine==null){
this.verticalSnapToHelperLine=new Line();
this.verticalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.verticalSnapToHelperLine);
}
this.verticalSnapToHelperLine.setStartPoint(_3b89,0);
this.verticalSnapToHelperLine.setEndPoint(_3b89,this.getHeight());
};
Workflow.prototype.hideSnapToHelperLines=function(){
this.hideSnapToHelperLineHorizontal();
this.hideSnapToHelperLineVertical();
};
Workflow.prototype.hideSnapToHelperLineHorizontal=function(){
if(this.horizontalSnapToHelperLine!=null){
this.removeFigure(this.horizontalSnapToHelperLine);
this.horizontalSnapToHelperLine=null;
}
};
Workflow.prototype.hideSnapToHelperLineVertical=function(){
if(this.verticalSnapToHelperLine!=null){
this.removeFigure(this.verticalSnapToHelperLine);
this.verticalSnapToHelperLine=null;
}
};
Window=function(title){
this.title=title;
this.titlebar=null;
Figure.call(this);
this.setDeleteable(false);
this.setCanSnapToHelper(false);
this.setZOrder(Window.ZOrderIndex);
};
Window.prototype=new Figure;
Window.prototype.type="Window";
Window.ZOrderIndex=5000;
Window.setZOrderBaseIndex=function(index){
Window.ZOrderBaseIndex=index;
};
Window.prototype.hasFixedPosition=function(){
return true;
};
Window.prototype.hasTitleBar=function(){
return true;
};
Window.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
item.style.margin="0px";
item.style.padding="0px";
item.style.border="1px solid black";
item.style.backgroundImage="url(window_bg.png)";
item.style.zIndex=Window.ZOrderBaseIndex;
item.style.cursor=null;
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
this.textNode=document.createTextNode(this.title);
this.titlebar.appendChild(this.textNode);
this.disableTextSelection(this.titlebar);
item.appendChild(this.titlebar);
}
return item;
};
Window.prototype.setDocumentDirty=function(_35c7){
};
Window.prototype.onDragend=function(){
};
Window.prototype.onDragstart=function(x,y){
if(this.titlebar==null){
return false;
}
if(this.canDrag==true&&x<parseInt(this.titlebar.style.width)&&y<parseInt(this.titlebar.style.height)){
return true;
}
return false;
};
Window.prototype.isSelectable=function(){
return false;
};
Window.prototype.setCanDrag=function(flag){
Figure.prototype.setCanDrag.call(this,flag);
this.html.style.cursor="";
if(this.titlebar==null){
return;
}
if(flag){
this.titlebar.style.cursor="move";
}else{
this.titlebar.style.cursor="";
}
};
Window.prototype.setWorkflow=function(_35cb){
var _35cc=this.workflow;
Figure.prototype.setWorkflow.call(this,_35cb);
if(_35cc!=null){
_35cc.removeSelectionListener(this);
}
if(this.workflow!=null){
this.workflow.addSelectionListener(this);
}
};
Window.prototype.setDimension=function(w,h){
Figure.prototype.setDimension.call(this,w,h);
if(this.titlebar!=null){
this.titlebar.style.width=this.getWidth()+"px";
}
};
Window.prototype.setTitle=function(title){
this.title=title;
};
Window.prototype.getMinWidth=function(){
return 50;
};
Window.prototype.getMinHeight=function(){
return 50;
};
Window.prototype.isResizeable=function(){
return false;
};
Window.prototype.setAlpha=function(_35d0){
};
Window.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!=null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
this.html.style.backgroundImage="";
}
};
Window.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!=null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
Window.prototype.setLineWidth=function(w){
this.lineStroke=w;
this.html.style.border=this.lineStroke+"px solid black";
};
Window.prototype.onSelectionChanged=function(_35d4){
};
Button=function(_3a85,width,_3a87){
this.x=0;
this.y=0;
this.id=this.generateUId();
this.enabled=true;
this.active=false;
this.palette=_3a85;
if(width&&_3a87){
this.setDimension(width,_3a87);
}else{
this.setDimension(24,24);
}
this.html=this.createHTMLElement();
};
Button.prototype.type="Button";
Button.prototype.dispose=function(){
};
Button.prototype.getImageUrl=function(){
if(this.enabled){
return this.type+".png";
}else{
return this.type+"_disabled.png";
}
};
Button.prototype.createHTMLElement=function(){
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
if(this.getImageUrl()!=null){
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
Button.prototype.getHTMLElement=function(){
if(this.html==null){
this.html=this.createHTMLElement();
}
return this.html;
};
Button.prototype.execute=function(){
};
Button.prototype.setTooltip=function(_3a8c){
this.tooltip=_3a8c;
if(this.tooltip!=null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
Button.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
this.active=flag;
if(flag==true){
this.html.style.border="2px inset";
}else{
this.html.style.border="0px";
}
};
Button.prototype.isActive=function(){
return this.active;
};
Button.prototype.setEnabled=function(flag){
this.enabled=flag;
if(this.getImageUrl()!=null){
this.html.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
this.html.style.backgroundImage="";
}
};
Button.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html==null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
Button.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html==null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
Button.prototype.getWidth=function(){
return this.width;
};
Button.prototype.getHeight=function(){
return this.height;
};
Button.prototype.getY=function(){
return this.y;
};
Button.prototype.getX=function(){
return this.x;
};
Button.prototype.getPosition=function(){
return new Point(this.x,this.y);
};
Button.prototype.getToolPalette=function(){
return this.palette;
};
Button.prototype.generateUId=function(){
var chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
var _3a94=10;
var _3a95=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_3a94;i++){
var rnum=Math.floor(Math.random()*chars.length);
id+=chars.substring(rnum,rnum+1);
}
elem=document.getElementById(id);
if(!elem){
return id;
}
nbTry+=1;
}
return null;
};
ToggleButton=function(_3889){
Button.call(this,_3889);
this.isDownFlag=false;
};
ToggleButton.prototype=new Button;
ToggleButton.prototype.type="ToggleButton";
ToggleButton.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height="24px";
item.style.width="24px";
item.style.margin="0px";
item.style.padding="0px";
if(this.getImageUrl()!=null){
item.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
item.style.backgroundImage="";
}
var oThis=this;
this.omousedown=function(event){
if(oThis.enabled){
if(!oThis.isDown()){
Button.prototype.setActive.call(oThis,true);
}
}
event.cancelBubble=true;
event.returnValue=false;
};
this.omouseup=function(event){
if(oThis.enabled){
if(oThis.isDown()){
Button.prototype.setActive.call(oThis,false);
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
ToggleButton.prototype.isDown=function(){
return this.isDownFlag;
};
ToggleButton.prototype.setActive=function(flag){
Button.prototype.setActive.call(this,flag);
this.isDownFlag=flag;
};
ToggleButton.prototype.execute=function(){
};
ToolGeneric=function(_343d){
this.x=0;
this.y=0;
this.enabled=true;
this.tooltip=null;
this.palette=_343d;
this.setDimension(10,10);
this.html=this.createHTMLElement();
};
ToolGeneric.prototype.type="ToolGeneric";
ToolGeneric.prototype.dispose=function(){
};
ToolGeneric.prototype.getImageUrl=function(){
if(this.enabled){
return this.type+".png";
}else{
return this.type+"_disabled.png";
}
};
ToolGeneric.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.height="24px";
item.style.width="24px";
item.style.margin="0px";
item.style.padding="0px";
if(this.getImageUrl()!=null){
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
ToolGeneric.prototype.getHTMLElement=function(){
if(this.html==null){
this.html=this.createHTMLElement();
}
return this.html;
};
ToolGeneric.prototype.execute=function(x,y){
if(this.enabled){
this.palette.setActiveTool(null);
}
};
ToolGeneric.prototype.setTooltip=function(_3443){
this.tooltip=_3443;
if(this.tooltip!=null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
ToolGeneric.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
if(flag==true){
this.html.style.border="2px inset";
}else{
this.html.style.border="0px";
}
};
ToolGeneric.prototype.setEnabled=function(flag){
this.enabled=flag;
if(this.getImageUrl()!=null){
this.html.style.backgroundImage="url("+this.getImageUrl()+")";
}else{
this.html.style.backgroundImage="";
}
};
ToolGeneric.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html==null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
ToolGeneric.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html==null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
ToolGeneric.prototype.getWidth=function(){
return this.width;
};
ToolGeneric.prototype.getHeight=function(){
return this.height;
};
ToolGeneric.prototype.getY=function(){
return this.y;
};
ToolGeneric.prototype.getX=function(){
return this.x;
};
ToolGeneric.prototype.getPosition=function(){
return new Point(this.x,this.y);
};
ToolPalette=function(title){
Window.call(this,title);
this.setDimension(75,400);
this.activeTool=null;
this.children=new Object();
};
ToolPalette.prototype=new Window;
ToolPalette.prototype.type="ToolPalette";
ToolPalette.prototype.dispose=function(){
Window.prototype.dispose.call(this);
};
ToolPalette.prototype.createHTMLElement=function(){
var item=Window.prototype.createHTMLElement.call(this);
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
ToolPalette.prototype.setDimension=function(w,h){
Window.prototype.setDimension.call(this,w,h);
if(this.scrollarea!=null){
this.scrollarea.style.width=this.getWidth()+"px";
if(this.hasTitleBar()){
this.scrollarea.style.height=(this.getHeight()-15)+"px";
}else{
this.scrollarea.style.height=this.getHeight()+"px";
}
}
};
ToolPalette.prototype.addChild=function(item){
this.children[item.id]=item;
this.scrollarea.appendChild(item.getHTMLElement());
};
ToolPalette.prototype.getChild=function(id){
return this.children[id];
};
ToolPalette.prototype.getActiveTool=function(){
return this.activeTool;
};
ToolPalette.prototype.setActiveTool=function(tool){
if(this.activeTool!=tool&&this.activeTool!=null){
this.activeTool.setActive(false);
}
if(tool!=null){
tool.setActive(true);
}
this.activeTool=tool;
};
Dialog=function(title){
this.buttonbar=null;
if(title){
Window.call(this,title);
}else{
Window.call(this,"Dialog");
}
this.setDimension(400,300);
};
Dialog.prototype=new Window;
Dialog.prototype.type="Dialog";
Dialog.prototype.createHTMLElement=function(){
var item=Window.prototype.createHTMLElement.call(this);
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
this.okbutton=document.createElement("button");
this.okbutton.style.border="1px solid gray";
this.okbutton.style.font="normal 10px verdana";
this.okbutton.style.width="80px";
this.okbutton.style.margin="5px";
this.okbutton.innerHTML="Ok";
this.okbutton.onclick=function(){
oThis.onOk();
};
this.buttonbar.appendChild(this.okbutton);
this.cancelbutton=document.createElement("button");
this.cancelbutton.innerHTML="Cancel";
this.cancelbutton.style.font="normal 10px verdana";
this.cancelbutton.style.border="1px solid gray";
this.cancelbutton.style.width="80px";
this.cancelbutton.style.margin="5px";
this.cancelbutton.onclick=function(){
oThis.onCancel();
};
this.buttonbar.appendChild(this.cancelbutton);
item.appendChild(this.buttonbar);
return item;
};
Dialog.prototype.onOk=function(){
this.workflow.removeFigure(this);
};
Dialog.prototype.onCancel=function(){
this.workflow.removeFigure(this);
};
Dialog.prototype.setDimension=function(w,h){
Window.prototype.setDimension.call(this,w,h);
if(this.buttonbar!=null){
this.buttonbar.style.width=this.getWidth()+"px";
}
};
Dialog.prototype.setWorkflow=function(_2c2c){
Window.prototype.setWorkflow.call(this,_2c2c);
this.setFocus();
};
Dialog.prototype.setFocus=function(){
};
Dialog.prototype.onSetDocumentDirty=function(){
};
InputDialog=function(){
Dialog.call(this);
this.setDimension(400,100);
};
InputDialog.prototype=new Dialog;
InputDialog.prototype.type="InputDialog";
InputDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
return item;
};
InputDialog.prototype.onOk=function(){
this.workflow.removeFigure(this);
};
InputDialog.prototype.onCancel=function(){
this.workflow.removeFigure(this);
};
PropertyDialog=function(_3936,_3937,label){
this.figure=_3936;
this.propertyName=_3937;
this.label=label;
Dialog.call(this);
this.setDimension(400,120);
};
PropertyDialog.prototype=new Dialog;
PropertyDialog.prototype.type="PropertyDialog";
PropertyDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _393a=document.createElement("form");
_393a.style.position="absolute";
_393a.style.left="10px";
_393a.style.top="30px";
_393a.style.width="375px";
_393a.style.font="normal 10px verdana";
item.appendChild(_393a);
this.label=document.createTextNode(this.label);
this.disableTextSelection(this.label);
_393a.appendChild(this.label);
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
_393a.appendChild(this.input);
this.input.focus();
return item;
};
PropertyDialog.prototype.onOk=function(){
Dialog.prototype.onOk.call(this);
this.figure.setProperty(this.propertyName,this.input.value);
};
AnnotationDialog=function(_28e0){
this.figure=_28e0;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog;
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _28e2=document.createElement("form");
_28e2.style.position="absolute";
_28e2.style.left="10px";
_28e2.style.top="30px";
_28e2.style.width="375px";
_28e2.style.font="normal 10px verdana";
item.appendChild(_28e2);
this.label=document.createTextNode("Text");
_28e2.appendChild(this.label);
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
_28e2.appendChild(this.input);
this.input.focus();
return item;
};
AnnotationDialog.prototype.onOk=function(){
this.workflow.getCommandStack().execute(new CommandSetText(this.figure,this.input.value));
this.workflow.removeFigure(this);
};
PropertyWindow=function(){
this.currentSelection=null;
Window.call(this,"Property Window");
this.setDimension(200,100);
};
PropertyWindow.prototype=new Window;
PropertyWindow.prototype.type="PropertyWindow";
PropertyWindow.prototype.dispose=function(){
Window.prototype.dispose.call(this);
};
PropertyWindow.prototype.createHTMLElement=function(){
var item=Window.prototype.createHTMLElement.call(this);
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
PropertyWindow.prototype.onSelectionChanged=function(_2c3b){
Window.prototype.onSelectionChanged.call(this,_2c3b);
if(this.currentSelection!=null){
this.currentSelection.detachMoveListener(this);
}
this.currentSelection=_2c3b;
if(_2c3b!=null&&_2c3b!=this){
this.labelType.innerHTML=_2c3b.type;
if(_2c3b.getX){
this.labelX.innerHTML=_2c3b.getX();
this.labelY.innerHTML=_2c3b.getY();
this.labelWidth.innerHTML=_2c3b.getWidth();
this.labelHeight.innerHTML=_2c3b.getHeight();
this.currentSelection=_2c3b;
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
PropertyWindow.prototype.getCurrentSelection=function(){
return this.currentSelection;
};
PropertyWindow.prototype.onOtherFigureMoved=function(_2c3c){
if(_2c3c==this.currentSelection){
this.onSelectionChanged(_2c3c);
}
};
PropertyWindow.prototype.createLabel=function(text,x,y){
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
ColorDialog=function(){
this.maxValue={"h":"359","s":"100","v":"100"};
this.HSV={0:359,1:100,2:100};
this.slideHSV={0:359,1:100,2:100};
this.SVHeight=165;
this.wSV=162;
this.wH=162;
Dialog.call(this,"Color Chooser");
this.loadSV();
this.setColor(new Color(255,0,0));
this.setDimension(219,244);
};
ColorDialog.prototype=new Dialog;
ColorDialog.prototype.type="ColorDialog";
ColorDialog.prototype.createHTMLElement=function(){
var oThis=this;
var item=Dialog.prototype.createHTMLElement.call(this);
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
ColorDialog.prototype.onOk=function(){
Dialog.prototype.onOk.call(this);
};
browser=function(v){
return (Math.max(navigator.userAgent.toLowerCase().indexOf(v),0));
};
ColorDialog.prototype.showColor=function(c){
this.plugHEX.style.background="#"+c;
this.plugHEX.innerHTML=c;
};
ColorDialog.prototype.getSelectedColor=function(){
var rgb=this.hex2rgb(this.plugHEX.innerHTML);
return new Color(rgb[0],rgb[1],rgb[2]);
};
ColorDialog.prototype.setColor=function(color){
if(color==null){
color=new Color(100,100,100);
}
var hex=this.rgb2hex(Array(color.getRed(),color.getGreen(),color.getBlue()));
this.updateH(hex);
};
ColorDialog.prototype.XY=function(e,v){
var z=browser("msie")?Array(event.clientX+document.body.scrollLeft,event.clientY+document.body.scrollTop):Array(e.pageX,e.pageY);
return z[v];
};
ColorDialog.prototype.mkHSV=function(a,b,c){
return (Math.min(a,Math.max(0,Math.ceil((parseInt(c)/b)*a))));
};
ColorDialog.prototype.ckHSV=function(a,b){
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
ColorDialog.prototype.mouseDownH=function(e){
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
ColorDialog.prototype.dragH=function(e){
var y=this.XY(e,1)-this.getY()-40;
this.Hslide.style.top=(this.ckHSV(y,this.wH)-5)+"px";
this.slideHSV[0]=this.mkHSV(359,this.wH,this.Hslide.style.top);
this.updateSV();
this.showColor(this.commit());
this.SV.style.backgroundColor="#"+this.hsv2hex(Array(this.HSV[0],100,100));
};
ColorDialog.prototype.mouseDownSV=function(o,e){
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
ColorDialog.prototype.dragSV=function(e){
var x=this.XY(e,0)-this.getX()-1;
var y=this.XY(e,1)-this.getY()-20;
this.SVslide.style.left=this.ckHSV(x,this.wSV)+"px";
this.SVslide.style.top=this.ckHSV(y,this.wSV)+"px";
this.slideHSV[1]=this.mkHSV(100,this.wSV,this.SVslide.style.left);
this.slideHSV[2]=100-this.mkHSV(100,this.wSV,this.SVslide.style.top);
this.updateSV();
};
ColorDialog.prototype.commit=function(){
var r="hsv";
var z={};
var j="";
for(var i=0;i<=r.length-1;i++){
j=r.substr(i,1);
z[i]=(j=="h")?this.maxValue[j]-this.mkHSV(this.maxValue[j],this.wH,this.Hslide.style.top):this.HSV[i];
}
return (this.updateSV(this.hsv2hex(z)));
};
ColorDialog.prototype.updateSV=function(v){
this.HSV=v?this.hex2hsv(v):Array(this.slideHSV[0],this.slideHSV[1],this.slideHSV[2]);
if(!v){
v=this.hsv2hex(Array(this.slideHSV[0],this.slideHSV[1],this.slideHSV[2]));
}
this.showColor(v);
return v;
};
ColorDialog.prototype.loadSV=function(){
var z="";
for(var i=this.SVHeight;i>=0;i--){
z+="<div style=\"background:#"+this.hsv2hex(Array(Math.round((359/this.SVHeight)*i),100,100))+";\"><br/></div>";
}
this.Hmodel.innerHTML=z;
};
ColorDialog.prototype.updateH=function(v){
this.plugHEX.innerHTML=v;
this.HSV=this.hex2hsv(v);
this.SV.style.backgroundColor="#"+this.hsv2hex(Array(this.HSV[0],100,100));
this.SVslide.style.top=(parseInt(this.wSV-this.wSV*(this.HSV[1]/100))+20)+"px";
this.SVslide.style.left=(parseInt(this.wSV*(this.HSV[1]/100))+5)+"px";
this.Hslide.style.top=(parseInt(this.wH*((this.maxValue["h"]-this.HSV[0])/this.maxValue["h"]))-7)+"px";
};
ColorDialog.prototype.toHex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
ColorDialog.prototype.hex2rgb=function(r){
return ({0:parseInt(r.substr(0,2),16),1:parseInt(r.substr(2,2),16),2:parseInt(r.substr(4,2),16)});
};
ColorDialog.prototype.rgb2hex=function(r){
return (this.toHex(r[0])+this.toHex(r[1])+this.toHex(r[2]));
};
ColorDialog.prototype.hsv2hex=function(h){
return (this.rgb2hex(this.hsv2rgb(h)));
};
ColorDialog.prototype.hex2hsv=function(v){
return (this.rgb2hsv(this.hex2rgb(v)));
};
ColorDialog.prototype.rgb2hsv=function(r){
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
ColorDialog.prototype.hsv2rgb=function(r){
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
LineColorDialog=function(_28d9){
ColorDialog.call(this);
this.figure=_28d9;
var color=_28d9.getColor();
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
};
LineColorDialog.prototype=new ColorDialog;
LineColorDialog.prototype.type="LineColorDialog";
LineColorDialog.prototype.onOk=function(){
var _28db=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setColor=="function"){
_28db.getCommandStack().execute(new CommandSetColor(this.figure,this.getSelectedColor()));
if(_28db.getCurrentSelection()==this.figure){
_28db.setCurrentSelection(this.figure);
}
}
};
BackgroundColorDialog=function(_3a7e){
ColorDialog.call(this);
this.figure=_3a7e;
var color=_3a7e.getBackgroundColor();
if(color!=null){
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
}
};
BackgroundColorDialog.prototype=new ColorDialog;
BackgroundColorDialog.prototype.type="BackgroundColorDialog";
BackgroundColorDialog.prototype.onOk=function(){
var _3a80=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setBackgroundColor=="function"){
_3a80.getCommandStack().execute(new CommandSetBackgroundColor(this.figure,this.getSelectedColor()));
if(_3a80.getCurrentSelection()==this.figure){
_3a80.setCurrentSelection(this.figure);
}
}
};
AnnotationDialog=function(_28e0){
this.figure=_28e0;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog;
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _28e2=document.createElement("form");
_28e2.style.position="absolute";
_28e2.style.left="10px";
_28e2.style.top="30px";
_28e2.style.width="375px";
_28e2.style.font="normal 10px verdana";
item.appendChild(_28e2);
this.label=document.createTextNode("Text");
_28e2.appendChild(this.label);
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
_28e2.appendChild(this.input);
this.input.focus();
return item;
};
AnnotationDialog.prototype.onOk=function(){
this.workflow.getCommandStack().execute(new CommandSetText(this.figure,this.input.value));
this.workflow.removeFigure(this);
};
Command=function(label){
this.label=label;
};
Command.prototype.type="Command";
Command.prototype.getLabel=function(){
};
Command.prototype.canExecute=function(){
return true;
};
Command.prototype.execute=function(){
};
Command.prototype.undo=function(){
};
Command.prototype.redo=function(){
};
CommandStack=function(){
this.undostack=new Array();
this.redostack=new Array();
this.maxundo=50;
};
CommandStack.prototype.type="CommandStack";
CommandStack.prototype.setUndoLimit=function(count){
this.maxundo=count;
};
CommandStack.prototype.markSaveLocation=function(){
this.undostack=new Array();
this.redostack=new Array();
};
CommandStack.prototype.execute=function(_3950){
if(_3950.canExecute()==false){
return;
}
this.undostack.push(_3950);
_3950.execute();
this.redostack=new Array();
if(this.undostack.length>this.maxundo){
this.undostack=this.undostack.slice(this.undostack.length-this.maxundo);
}
};
CommandStack.prototype.undo=function(){
var _3951=this.undostack.pop();
if(_3951){
this.redostack.push(_3951);
_3951.undo();
}
};
CommandStack.prototype.redo=function(){
var _3952=this.redostack.pop();
if(_3952){
this.undostack.push(_3952);
_3952.redo();
}
};
CommandStack.prototype.canRedo=function(){
return this.redostack.length>0;
};
CommandStack.prototype.canUndo=function(){
return this.undostack.length>0;
};
CommandAdd=function(_3a66,_3a67,x,y,_3a6a){
Command.call(this,"add figure");
this.parent=_3a6a;
this.figure=_3a67;
this.x=x;
this.y=y;
this.workflow=_3a66;
};
CommandAdd.prototype=new Command;
CommandAdd.prototype.type="CommandAdd";
CommandAdd.prototype.execute=function(){
this.redo();
};
CommandAdd.prototype.redo=function(){
if(this.x&&this.y){
this.workflow.addFigure(this.figure,this.x,this.y);
}else{
this.workflow.addFigure(this.figure);
}
this.workflow.setCurrentSelection(this.figure);
if(this.parent!=null){
this.parent.addChild(this.figure);
}
};
CommandAdd.prototype.undo=function(){
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.parent!=null){
this.parent.removeChild(this.figure);
}
};
CommandDelete=function(_390e){
Command.call(this,"delete figure");
this.parent=_390e.parent;
this.figure=_390e;
this.workflow=_390e.workflow;
this.connections=null;
};
CommandDelete.prototype=new Command;
CommandDelete.prototype.type="CommandDelete";
CommandDelete.prototype.execute=function(){
this.redo();
};
CommandDelete.prototype.undo=function(){
this.workflow.addFigure(this.figure);
if(this.figure instanceof Connection){
this.figure.reconnect();
}
this.workflow.setCurrentSelection(this.figure);
if(this.parent!=null){
this.parent.addChild(this.figure);
}
for(var i=0;i<this.connections.length;++i){
this.workflow.addFigure(this.connections[i]);
this.connections[i].reconnect();
}
};
CommandDelete.prototype.redo=function(){
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.figure.getPorts&&this.connections==null){
this.connections=new Array();
var ports=this.figure.getPorts();
for(var i=0;i<ports.length;i++){
if(ports[i].getConnections){
this.connections=this.connections.concat(ports[i].getConnections());
}
}
}
if(this.connections==null){
this.connections=new Array();
}
if(this.parent!=null){
this.parent.removeChild(this.figure);
}
for(var i=0;i<this.connections.length;++i){
this.workflow.removeFigure(this.connections[i]);
}
};
CommandMove=function(_3918,x,y){
Command.call(this,"move figure");
this.figure=_3918;
this.oldX=x;
this.oldY=y;
this.oldCompartment=_3918.getParent();
};
CommandMove.prototype=new Command;
CommandMove.prototype.type="CommandMove";
CommandMove.prototype.setPosition=function(x,y){
this.newX=x;
this.newY=y;
this.newCompartment=this.figure.workflow.getBestCompartmentFigure(x,y,this.figure);
};
CommandMove.prototype.canExecute=function(){
return this.newX!=this.oldX||this.newY!=this.oldY;
};
CommandMove.prototype.execute=function(){
this.redo();
};
CommandMove.prototype.undo=function(){
this.figure.setPosition(this.oldX,this.oldY);
if(this.newCompartment!=null){
this.newCompartment.removeChild(this.figure);
}
if(this.oldCompartment!=null){
this.oldCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandMove.prototype.redo=function(){
this.figure.setPosition(this.newX,this.newY);
if(this.oldCompartment!=null){
this.oldCompartment.removeChild(this.figure);
}
if(this.newCompartment!=null){
this.newCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandResize=function(_3893,width,_3895){
Command.call(this,"resize figure");
this.figure=_3893;
this.oldWidth=width;
this.oldHeight=_3895;
};
CommandResize.prototype=new Command;
CommandResize.prototype.type="CommandResize";
CommandResize.prototype.setDimension=function(width,_3897){
this.newWidth=width;
this.newHeight=_3897;
};
CommandResize.prototype.canExecute=function(){
return this.newWidth!=this.oldWidth||this.newHeight!=this.oldHeight;
};
CommandResize.prototype.execute=function(){
this.redo();
};
CommandResize.prototype.undo=function(){
this.figure.setDimension(this.oldWidth,this.oldHeight);
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandResize.prototype.redo=function(){
this.figure.setDimension(this.newWidth,this.newHeight);
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandSetText=function(text){
Command.call(this,"set text");
this.figure=figure;
this.newText=text;
this.oldText=figure.getText();
};
CommandSetText.prototype=new Command;
CommandSetText.prototype.type="CommandSetText";
CommandSetText.prototype.execute=function(){
this.redo();
};
CommandSetText.prototype.redo=function(){
this.figure.setText(this.newText);
};
CommandSetText.prototype.undo=function(){
this.figure.setText(this.oldText);
};
CommandSetColor=function(_347d,color){
Command.call(this,"set color");
this.figure=_347d;
this.newColor=color;
this.oldColor=_347d.getColor();
};
CommandSetColor.prototype=new Command;
CommandSetColor.prototype.type="CommandSetColor";
CommandSetColor.prototype.execute=function(){
this.redo();
};
CommandSetColor.prototype.undo=function(){
this.figure.setColor(this.oldColor);
};
CommandSetColor.prototype.redo=function(){
this.figure.setColor(this.newColor);
};
CommandSetBackgroundColor=function(_2885,color){
Command.call(this,"set background color");
this.figure=_2885;
this.newColor=color;
this.oldColor=_2885.getBackgroundColor();
};
CommandSetBackgroundColor.prototype=new Command;
CommandSetBackgroundColor.prototype.type="CommandSetBackgroundColor";
CommandSetBackgroundColor.prototype.execute=function(){
this.redo();
};
CommandSetBackgroundColor.prototype.undo=function(){
this.figure.setBackgroundColor(this.oldColor);
};
CommandSetBackgroundColor.prototype.redo=function(){
this.figure.setBackgroundColor(this.newColor);
};
CommandConnect=function(_3a41,_3a42,_3a43){
Command.call(this,"create connection");
this.workflow=_3a41;
this.source=_3a42;
this.target=_3a43;
this.connection=null;
};
CommandConnect.prototype=new Command;
CommandConnect.prototype.type="CommandConnect";
CommandConnect.prototype.setConnection=function(_3a44){
this.connection=_3a44;
};
CommandConnect.prototype.execute=function(){
if(this.connection==null){
this.connection=new Connection();
}
this.connection.setSource(this.source);
this.connection.setTarget(this.target);
this.workflow.addFigure(this.connection);
};
CommandConnect.prototype.redo=function(){
this.workflow.addFigure(this.connection);
this.connection.reconnect();
};
CommandConnect.prototype.undo=function(){
this.workflow.removeFigure(this.connection);
};
CommandMoveLine=function(line,_2ff9,_2ffa,endX,endY){
Command.call(this,"move line");
this.line=line;
this.startX1=_2ff9;
this.startY1=_2ffa;
this.endX1=endX;
this.endY1=endY;
};
CommandMoveLine.prototype=new Command;
CommandMoveLine.prototype.type="CommandMoveLine";
CommandMoveLine.prototype.canExecute=function(){
return this.startX1!=this.startX2||this.startY1!=this.startY2||this.endX1!=this.endX2||this.endY1!=this.endY2;
};
CommandMoveLine.prototype.setEndPoints=function(_2ffd,_2ffe,endX,endY){
this.startX2=_2ffd;
this.startY2=_2ffe;
this.endX2=endX;
this.endY2=endY;
};
CommandMoveLine.prototype.execute=function(){
this.redo();
};
CommandMoveLine.prototype.undo=function(){
this.line.setStartPoint(this.startX1,this.startY1);
this.line.setEndPoint(this.endX1,this.endY1);
if(this.line.workflow.getCurrentSelection()==this.line){
this.line.workflow.showLineResizeHandles(this.line);
}
};
CommandMoveLine.prototype.redo=function(){
this.line.setStartPoint(this.startX2,this.startY2);
this.line.setEndPoint(this.endX2,this.endY2);
if(this.line.workflow.getCurrentSelection()==this.line){
this.line.workflow.showLineResizeHandles(this.line);
}
};
Menu=function(){
this.menuItems=new Array();
Figure.call(this);
this.setSelectable(false);
this.setDeleteable(false);
this.setCanDrag(false);
this.setResizeable(false);
this.setSelectable(false);
this.setZOrder(10000);
this.dirty=false;
};
Menu.prototype=new Figure;
Menu.prototype.type="Menu";
Menu.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.margin="0px";
item.style.padding="0px";
item.style.zIndex=""+Figure.ZOrderBaseIndex;
item.style.border="1px solid gray";
item.style.background="lavender";
item.style.cursor="pointer";
return item;
};
Menu.prototype.setWorkflow=function(_3bb5){
this.workflow=_3bb5;
};
Menu.prototype.appendMenuItem=function(item){
this.menuItems.push(item);
item.parentMenu=this;
this.dirty=true;
};
Menu.prototype.getHTMLElement=function(){
var html=Figure.prototype.getHTMLElement.call(this);
if(this.dirty){
this.createList();
}
return html;
};
Menu.prototype.createList=function(){
this.dirty=false;
this.html.innerHTML="";
var oThis=this;
for(var i=0;i<this.menuItems.length;i++){
var item=this.menuItems[i];
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
this.html.appendChild(li);
li.menuItem=item;
if(li.addEventListener){
li.addEventListener("click",function(event){
var _3bbd=arguments[0]||window.event;
_3bbd.cancelBubble=true;
_3bbd.returnValue=false;
var diffX=_3bbd.clientX;
var diffY=_3bbd.clientY;
var _3bc0=document.body.parentNode.scrollLeft;
var _3bc1=document.body.parentNode.scrollTop;
this.menuItem.execute(diffX+_3bc0,diffY+_3bc1);
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
var _3bc7=arguments[0]||window.event;
_3bc7.cancelBubble=true;
_3bc7.returnValue=false;
var diffX=_3bc7.clientX;
var diffY=_3bc7.clientY;
var _3bca=document.body.parentNode.scrollLeft;
var _3bcb=document.body.parentNode.scrollTop;
event.srcElement.menuItem.execute(diffX+_3bca,diffY+_3bcb);
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
MenuItem=function(label,_393e,_393f){
this.label=label;
this.iconUrl=_393e;
this.parentMenu=null;
this.action=_393f;
};
MenuItem.prototype.type="MenuItem";
MenuItem.prototype.isEnabled=function(){
return true;
};
MenuItem.prototype.getLabel=function(){
return this.label;
};
MenuItem.prototype.execute=function(x,y){
this.parentMenu.workflow.showMenu(null);
this.action(x,y);
};
