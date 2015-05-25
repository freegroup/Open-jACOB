
/**This notice must be untouched at all times.
This is the COMPRESSED version of the Draw2D Library
WebSite: http://www.draw2d.org
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
Event.prototype.initEvent=function(sType,_3a29){
this.type=sType;
this.cancelable=_3a29;
this.timeStamp=(new Date()).getTime();
};
Event.prototype.preventDefault=function(){
if(this.cancelable){
this.returnValue=false;
}
};
Event.fireDOMEvent=function(_3a2a,_3a2b){
if(document.createEvent){
var evt=document.createEvent("Events");
evt.initEvent(_3a2a,true,true);
_3a2b.dispatchEvent(evt);
}else{
if(document.createEventObject){
var evt=document.createEventObject();
_3a2b.fireEvent("on"+_3a2a,evt);
}
}
};
EventTarget=function(){
this.eventhandlers=new Object();
};
EventTarget.prototype.addEventListener=function(sType,_3a2e){
if(typeof this.eventhandlers[sType]=="undefined"){
this.eventhandlers[sType]=new Array;
}
this.eventhandlers[sType][this.eventhandlers[sType].length]=_3a2e;
};
EventTarget.prototype.dispatchEvent=function(_3a2f){
_3a2f.target=this;
if(typeof this.eventhandlers[_3a2f.type]!="undefined"){
for(var i=0;i<this.eventhandlers[_3a2f.type].length;i++){
this.eventhandlers[_3a2f.type][i](_3a2f);
}
}
return _3a2f.returnValue;
};
EventTarget.prototype.removeEventListener=function(sType,_3a32){
if(typeof this.eventhandlers[sType]!="undefined"){
var _3a33=new Array;
for(var i=0;i<this.eventhandlers[sType].length;i++){
if(this.eventhandlers[sType][i]!=_3a32){
_3a33[_3a33.length]=this.eventhandlers[sType][i];
}
}
this.eventhandlers[sType]=_3a33;
}
};
ArrayList=function(){
this.increment=10;
this.size=0;
this.data=new Array(this.increment);
};
ArrayList.EMPTY_LIST=new ArrayList();
ArrayList.prototype.reverse=function(){
var _3de4=new Array(this.size);
for(var i=0;i<this.size;i++){
_3de4[i]=this.data[this.size-i-1];
}
this.data=_3de4;
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
ArrayList.prototype.addAll=function(obj){
for(var i=0;i<obj.getSize();i++){
this.add(obj.get(i));
}
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
var _3df0=this.data[index];
for(var i=index;i<(this.getSize()-1);i++){
this.data[i]=this.data[i+1];
}
this.data[this.getSize()-1]=null;
this.size--;
return _3df0;
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
var _3dfc;
var _3dfd;
var _3dfe;
var _3dff;
for(i=1;i<this.getSize();i++){
_3dfd=this.data[i];
_3dfc=_3dfd[f];
j=i-1;
_3dfe=this.data[j];
_3dff=_3dfe[f];
while(j>=0&&_3dff>_3dfc){
this.data[j+1]=this.data[j];
j--;
if(j>=0){
_3dfe=this.data[j];
_3dff=_3dfe[f];
}
}
this.data[j+1]=_3dfd;
}
};
ArrayList.prototype.clone=function(){
var _3e00=new ArrayList(this.size);
for(var i=0;i<this.size;i++){
_3e00.add(this.data[i]);
}
return _3e00;
};
ArrayList.prototype.overwriteElementAt=function(obj,index){
this.data[index]=obj;
};
function trace(_3ddb){
var _3ddc=openwindow("about:blank",700,400);
_3ddc.document.writeln("<pre>"+_3ddb+"</pre>");
}
function openwindow(url,width,_3ddf){
var left=(screen.width-width)/2;
var top=(screen.height-_3ddf)/2;
property="left="+left+", top="+top+", toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width="+width+",height="+_3ddf;
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
Drag.setCurrent=function(_3287){
this.current=_3287;
this.dragging=true;
};
Drag.getCurrent=function(){
return this.current;
};
Drag.clearCurrent=function(){
this.current=null;
this.dragging=false;
this.currentTarget=null;
};
Draggable=function(_3288,_3289){
EventTarget.call(this);
this.construct(_3288,_3289);
this.diffX=0;
this.diffY=0;
this.targets=new ArrayList();
};
Draggable.prototype=new EventTarget;
Draggable.prototype.construct=function(_328a,_328b){
this.element=_328a;
this.constraints=_328b;
var oThis=this;
var _328d=function(){
var _328e=new DragDropEvent();
_328e.initDragDropEvent("dblclick",true);
oThis.dispatchEvent(_328e);
var _328f=arguments[0]||window.event;
_328f.cancelBubble=true;
_328f.returnValue=false;
};
var _3290=function(){
var _3291=arguments[0]||window.event;
var _3292=new DragDropEvent();
var _3293=oThis.node.workflow.getAbsoluteX();
var _3294=oThis.node.workflow.getAbsoluteY();
var _3295=oThis.node.workflow.getScrollLeft();
var _3296=oThis.node.workflow.getScrollTop();
_3292.x=_3291.clientX-oThis.element.offsetLeft+_3295-_3293;
_3292.y=_3291.clientY-oThis.element.offsetTop+_3296-_3294;
if(_3291.button==2){
_3292.initDragDropEvent("contextmenu",true);
oThis.dispatchEvent(_3292);
}else{
_3292.initDragDropEvent("dragstart",true);
if(oThis.dispatchEvent(_3292)){
oThis.diffX=_3291.clientX-oThis.element.offsetLeft;
oThis.diffY=_3291.clientY-oThis.element.offsetTop;
Drag.setCurrent(oThis);
if(oThis.isAttached==true){
oThis.detachEventHandlers();
}
oThis.attachEventHandlers();
}
}
_3291.cancelBubble=true;
_3291.returnValue=false;
};
var _3297=function(){
if(Drag.getCurrent()==null){
var _3298=arguments[0]||window.event;
if(Drag.currentHover!=null&&oThis!=Drag.currentHover){
var _3299=new DragDropEvent();
_3299.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_3299);
}
if(oThis!=null&&oThis!=Drag.currentHover){
var _3299=new DragDropEvent();
_3299.initDragDropEvent("mouseenter",false,oThis);
oThis.dispatchEvent(_3299);
}
Drag.currentHover=oThis;
}else{
}
};
if(this.element.addEventListener){
this.element.addEventListener("mousemove",_3297,false);
this.element.addEventListener("mousedown",_3290,false);
this.element.addEventListener("dblclick",_328d,false);
}else{
if(this.element.attachEvent){
this.element.attachEvent("onmousemove",_3297);
this.element.attachEvent("onmousedown",_3290);
this.element.attachEvent("ondblclick",_328d);
}else{
throw new Error("Drag not supported in this browser.");
}
}
};
Draggable.prototype.attachEventHandlers=function(){
var oThis=this;
oThis.isAttached=true;
this.tempMouseMove=function(){
var _329b=arguments[0]||window.event;
var _329c=new Point(_329b.clientX-oThis.diffX,_329b.clientY-oThis.diffY);
if(oThis.node.getCanSnapToHelper()){
_329c=oThis.node.getWorkflow().snapToHelper(oThis.node,_329c);
}
oThis.element.style.left=_329c.x+"px";
oThis.element.style.top=_329c.y+"px";
var _329d=oThis.node.workflow.getScrollLeft();
var _329e=oThis.node.workflow.getScrollTop();
var _329f=oThis.node.workflow.getAbsoluteX();
var _32a0=oThis.node.workflow.getAbsoluteY();
var _32a1=oThis.getDropTarget(_329b.clientX+_329d-_329f,_329b.clientY+_329e-_32a0);
var _32a2=oThis.getCompartment(_329b.clientX+_329d-_329f,_329b.clientY+_329e-_32a0);
if(Drag.currentTarget!=null&&_32a1!=Drag.currentTarget){
var _32a3=new DragDropEvent();
_32a3.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_32a3);
}
if(_32a1!=null&&_32a1!=Drag.currentTarget){
var _32a3=new DragDropEvent();
_32a3.initDragDropEvent("dragenter",false,oThis);
_32a1.dispatchEvent(_32a3);
}
Drag.currentTarget=_32a1;
if(Drag.currentCompartment!=null&&_32a2!=Drag.currentCompartment){
var _32a3=new DragDropEvent();
_32a3.initDragDropEvent("figureleave",false,oThis);
Drag.currentCompartment.dispatchEvent(_32a3);
}
if(_32a2!=null&&_32a2.node!=oThis.node&&_32a2!=Drag.currentCompartment){
var _32a3=new DragDropEvent();
_32a3.initDragDropEvent("figureenter",false,oThis);
_32a2.dispatchEvent(_32a3);
}
Drag.currentCompartment=_32a2;
var _32a4=new DragDropEvent();
_32a4.initDragDropEvent("drag",false);
oThis.dispatchEvent(_32a4);
};
oThis.tempMouseUp=function(){
oThis.detachEventHandlers();
var _32a5=arguments[0]||window.event;
var _32a6=new DragDropEvent();
_32a6.initDragDropEvent("dragend",false);
oThis.dispatchEvent(_32a6);
var _32a7=oThis.node.workflow.getScrollLeft();
var _32a8=oThis.node.workflow.getScrollTop();
var _32a9=oThis.node.workflow.getAbsoluteX();
var _32aa=oThis.node.workflow.getAbsoluteY();
var _32ab=oThis.getDropTarget(_32a5.clientX+_32a7-_32a9,_32a5.clientY+_32a8-_32aa);
var _32ac=oThis.getCompartment(_32a5.clientX+_32a7-_32a9,_32a5.clientY+_32a8-_32aa);
if(_32ab!=null){
var _32ad=new DragDropEvent();
_32ad.initDragDropEvent("drop",false,oThis);
_32ab.dispatchEvent(_32ad);
}
if(_32ac!=null&&_32ac.node!=oThis.node){
var _32ad=new DragDropEvent();
_32ad.initDragDropEvent("figuredrop",false,oThis);
_32ac.dispatchEvent(_32ad);
}
if(Drag.currentTarget!=null){
var _32ad=new DragDropEvent();
_32ad.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_32ad);
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
var _32b1=this.targets.get(i);
if(_32b1.node.isOver(x,y)&&_32b1.node!=this.node){
return _32b1;
}
}
return null;
};
Draggable.prototype.getCompartment=function(x,y){
var _32b4=null;
for(var i=0;i<this.node.workflow.compartments.getSize();i++){
var _32b6=this.node.workflow.compartments.get(i);
if(_32b6.isOver(x,y)&&_32b6!=this.node){
if(_32b4==null){
_32b4=_32b6;
}else{
if(_32b4.getZOrder()<_32b6.getZOrder()){
_32b4=_32b6;
}
}
}
}
return _32b4==null?null:_32b4.dropable;
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
DragDropEvent.prototype.initDragDropEvent=function(sType,_32b8,_32b9){
this.initEvent(sType,_32b8);
this.relatedTarget=_32b9;
};
DropTarget=function(_32ba){
EventTarget.call(this);
this.construct(_32ba);
};
DropTarget.prototype=new EventTarget;
DropTarget.prototype.construct=function(_32bb){
this.element=_32bb;
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
var _3f25=105;
var _3f26=(this.red*0.299)+(this.green*0.587)+(this.blue*0.114);
return (255-_3f26<_3f25)?new Color(0,0,0):new Color(255,255,255);
};
Color.prototype.hex2rgb=function(_3f27){
_3f27=_3f27.replace("#","");
return ({0:parseInt(_3f27.substr(0,2),16),1:parseInt(_3f27.substr(2,2),16),2:parseInt(_3f27.substr(4,2),16)});
};
Color.prototype.hex=function(){
return (this.int2hex(this.red)+this.int2hex(this.green)+this.int2hex(this.blue));
};
Color.prototype.int2hex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
Color.prototype.darker=function(_3f29){
var red=parseInt(Math.round(this.getRed()*(1-_3f29)));
var green=parseInt(Math.round(this.getGreen()*(1-_3f29)));
var blue=parseInt(Math.round(this.getBlue()*(1-_3f29)));
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
return new Color(red,green,blue);
};
Color.prototype.lighter=function(_3f2d){
var red=parseInt(Math.round(this.getRed()*(1+_3f2d)));
var green=parseInt(Math.round(this.getGreen()*(1+_3f2d)));
var blue=parseInt(Math.round(this.getBlue()*(1+_3f2d)));
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
return new Color(red,green,blue);
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
Dimension.prototype.translate=function(dx,dy){
this.x+=dx;
this.y+=dy;
return this;
};
Dimension.prototype.resize=function(dw,dh){
this.w+=dw;
this.h+=dh;
return this;
};
Dimension.prototype.setBounds=function(rect){
this.x=rect.x;
this.y=rect.y;
this.w=rect.w;
this.h=rect.h;
return this;
};
Dimension.prototype.isEmpty=function(){
return this.w<=0||this.h<=0;
};
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
SnapToHelper=function(_3f3d){
this.workflow=_3f3d;
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
SnapToHelper.prototype.snapPoint=function(_3f3e,_3f3f,_3f40){
return _3f3f;
};
SnapToHelper.prototype.snapRectangle=function(_3f41,_3f42){
return _3f41;
};
SnapToHelper.prototype.onSetDocumentDirty=function(){
};
SnapToGrid=function(_39f7){
SnapToHelper.call(this,_39f7);
};
SnapToGrid.prototype=new SnapToHelper;
SnapToGrid.prototype.snapPoint=function(_39f8,_39f9,_39fa){
_39fa.x=this.workflow.gridWidthX*Math.floor(((_39f9.x+this.workflow.gridWidthX/2)/this.workflow.gridWidthX));
_39fa.y=this.workflow.gridWidthY*Math.floor(((_39f9.y+this.workflow.gridWidthY/2)/this.workflow.gridWidthY));
return 0;
};
SnapToGrid.prototype.snapRectangle=function(_39fb,_39fc){
_39fc.x=_39fb.x;
_39fc.y=_39fb.y;
_39fc.w=_39fb.w;
_39fc.h=_39fb.h;
return 0;
};
SnapToGeometryEntry=function(type,_39ea){
this.type=type;
this.location=_39ea;
};
SnapToGeometryEntry.prototype.getLocation=function(){
return this.location;
};
SnapToGeometryEntry.prototype.getType=function(){
return this.type;
};
SnapToGeometry=function(_40fe){
SnapToHelper.call(this,_40fe);
};
SnapToGeometry.prototype=new SnapToHelper;
SnapToGeometry.THRESHOLD=5;
SnapToGeometry.prototype.snapPoint=function(_40ff,_4100,_4101){
if(this.rows==null||this.cols==null){
this.populateRowsAndCols();
}
if((_40ff&SnapToHelper.EAST)!=0){
var _4102=this.getCorrectionFor(this.cols,_4100.getX()-1,1);
if(_4102!=SnapToGeometry.THRESHOLD){
_40ff&=~SnapToHelper.EAST;
_4101.x+=_4102;
}
}
if((_40ff&SnapToHelper.WEST)!=0){
var _4103=this.getCorrectionFor(this.cols,_4100.getX(),-1);
if(_4103!=SnapToGeometry.THRESHOLD){
_40ff&=~SnapToHelper.WEST;
_4101.x+=_4103;
}
}
if((_40ff&SnapToHelper.SOUTH)!=0){
var _4104=this.getCorrectionFor(this.rows,_4100.getY()-1,1);
if(_4104!=SnapToGeometry.THRESHOLD){
_40ff&=~SnapToHelper.SOUTH;
_4101.y+=_4104;
}
}
if((_40ff&SnapToHelper.NORTH)!=0){
var _4105=this.getCorrectionFor(this.rows,_4100.getY(),-1);
if(_4105!=SnapToGeometry.THRESHOLD){
_40ff&=~SnapToHelper.NORTH;
_4101.y+=_4105;
}
}
return _40ff;
};
SnapToGeometry.prototype.snapRectangle=function(_4106,_4107){
var _4108=_4106.getTopLeft();
var _4109=_4106.getBottomRight();
var _410a=this.snapPoint(SnapToHelper.NORTH_WEST,_4106.getTopLeft(),_4108);
_4107.x=_4108.x;
_4107.y=_4108.y;
var _410b=this.snapPoint(SnapToHelper.SOUTH_EAST,_4106.getBottomRight(),_4109);
if(_410a&SnapToHelper.WEST){
_4107.x=_4109.x-_4106.getWidth();
}
if(_410a&SnapToHelper.NORTH){
_4107.y=_4109.y-_4106.getHeight();
}
return _410a|_410b;
};
SnapToGeometry.prototype.populateRowsAndCols=function(){
this.rows=new Array();
this.cols=new Array();
var _410c=this.workflow.getDocument().getFigures();
var index=0;
for(var i=0;i<_410c.getSize();i++){
var _410f=_410c.get(i);
if(_410f!=this.workflow.getCurrentSelection()){
var _4110=_410f.getBounds();
this.cols[index*3]=new SnapToGeometryEntry(-1,_4110.getX());
this.rows[index*3]=new SnapToGeometryEntry(-1,_4110.getY());
this.cols[index*3+1]=new SnapToGeometryEntry(0,_4110.x+(_4110.getWidth()-1)/2);
this.rows[index*3+1]=new SnapToGeometryEntry(0,_4110.y+(_4110.getHeight()-1)/2);
this.cols[index*3+2]=new SnapToGeometryEntry(1,_4110.getRight()-1);
this.rows[index*3+2]=new SnapToGeometryEntry(1,_4110.getBottom()-1);
index++;
}
}
};
SnapToGeometry.prototype.getCorrectionFor=function(_4111,value,side){
var _4114=SnapToGeometry.THRESHOLD;
var _4115=SnapToGeometry.THRESHOLD;
for(var i=0;i<_4111.length;i++){
var entry=_4111[i];
var _4118;
if(entry.type==-1&&side!=0){
_4118=Math.abs(value-entry.location);
if(_4118<_4114){
_4114=_4118;
_4115=entry.location-value;
}
}else{
if(entry.type==0&&side==0){
_4118=Math.abs(value-entry.location);
if(_4118<_4114){
_4114=_4118;
_4115=entry.location-value;
}
}else{
if(entry.type==1&&side!=0){
_4118=Math.abs(value-entry.location);
if(_4118<_4114){
_4114=_4118;
_4115=entry.location-value;
}
}
}
}
}
return _4115;
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
this.moveListener=new ArrayList();
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
this.draggable.target.removeAllElements();
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
Figure.prototype.setCanvas=function(_3e08){
this.canvas=_3e08;
};
Figure.prototype.getWorkflow=function(){
return this.workflow;
};
Figure.prototype.setWorkflow=function(_3e09){
if(this.draggable==null){
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
this.draggable=new Draggable(this.html,Draggable.DRAG_X|Draggable.DRAG_Y);
this.draggable.node=this;
this.tmpContextMenu=function(_3e0c){
oThis.onContextMenu(oThis.x+_3e0c.x,_3e0c.y+oThis.y);
};
this.tmpMouseEnter=function(_3e0d){
oThis.onMouseEnter();
};
this.tmpMouseLeave=function(_3e0e){
oThis.onMouseLeave();
};
this.tmpDragend=function(_3e0f){
oThis.onDragend();
};
this.tmpDragstart=function(_3e10){
var w=oThis.workflow;
w.showMenu(null);
if(oThis.workflow.toolPalette&&oThis.workflow.toolPalette.activeTool){
_3e10.returnValue=false;
oThis.workflow.onMouseDown(oThis.x+_3e10.x,_3e10.y+oThis.y);
oThis.workflow.onMouseUp(oThis.x+_3e10.x,_3e10.y+oThis.y);
return;
}
_3e10.returnValue=oThis.onDragstart(_3e10.x,_3e10.y);
};
this.tmpDrag=function(_3e12){
oThis.onDrag();
};
this.tmpDoubleClick=function(_3e13){
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
this.workflow=_3e09;
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
Figure.prototype.setParent=function(_3e15){
this.parent=_3e15;
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
Figure.prototype.setBorder=function(_3e17){
if(this.border!=null){
this.border.figure=null;
}
this.border=_3e17;
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
var _3e1b=this;
var _3e1c=function(){
if(_3e1b.alpha<1){
_3e1b.setAlpha(Math.min(1,_3e1b.alpha+0.05));
}else{
window.clearInterval(_3e1b.timer);
_3e1b.timer=-1;
}
};
if(_3e1b.timer>0){
window.clearInterval(_3e1b.timer);
}
_3e1b.timer=window.setInterval(_3e1c,20);
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
Figure.prototype.setAlpha=function(_3e20){
if(this.alpha==_3e20){
return;
}
try{
this.html.style.MozOpacity=_3e20;
}
catch(exc){
}
try{
this.html.style.opacity=_3e20;
}
catch(exc){
}
try{
var _3e21=Math.round(_3e20*100);
if(_3e21>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_3e21+")";
}
}
catch(exc){
}
this.alpha=_3e20;
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
Figure.prototype.onKeyDown=function(_3e2b,ctrl){
if(_3e2b==46&&this.isDeleteable()==true){
this.workflow.getCommandStack().execute(new CommandDelete(this));
}
if(ctrl){
this.workflow.onKeyDown(_3e2b,ctrl);
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
Figure.prototype.attachMoveListener=function(_3e33){
if(_3e33==null||this.moveListener==null){
return;
}
this.moveListener.add(_3e33);
};
Figure.prototype.detachMoveListener=function(_3e34){
if(_3e34==null||this.moveListener==null){
return;
}
this.moveListener.remove(_3e34);
};
Figure.prototype.fireMoveEvent=function(){
this.setDocumentDirty();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
Figure.prototype.onOtherFigureMoved=function(_3e37){
};
Figure.prototype.setDocumentDirty=function(){
if(this.workflow!=null){
this.workflow.setDocumentDirty();
}
};
Figure.prototype.generateUId=function(){
var chars="0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
var _3e39=10;
var _3e3a=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_3e39;i++){
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
return this.ports;
};
Node.prototype.getPort=function(_32c4){
if(this.ports==null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_32c4){
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
Node.prototype.setWorkflow=function(_32cb){
var _32cc=this.workflow;
Figure.prototype.setWorkflow.call(this,_32cb);
if(_32cc!=null){
for(var i=0;i<this.ports.getSize();i++){
_32cc.unregisterPort(this.ports.get(i));
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
Node.prototype.getBackgroundColor=function(){
return this.bgColor;
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
VectorFigure.prototype.setWorkflow=function(_3e8f){
Node.prototype.setWorkflow.call(this,_3e8f);
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
this.getHTMLElement().appendChild(this.ports.get(i).getHTMLElement());
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
SVGFigure=function(width,_30cf){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.stroke=1;
this.context=null;
Node.call(this);
if(width&&_30cf){
this.setDimension(width,_30cf);
}
};
SVGFigure.prototype=new Node;
SVGFigure.prototype.type="SVGFigure";
SVGFigure.prototype.createHTMLElement=function(){
var item=new MooCanvas(this.id,{width:100,height:100});
item.style.position="absolute";
item.style.left=this.x+"px";
item.style.top=this.y+"px";
item.style.zIndex=""+Figure.ZOrderBaseIndex;
this.context=item.getContext("2d");
return item;
};
SVGFigure.prototype.paint=function(){
this.context.clearRect(0,0,this.getWidth(),this.getHeight());
this.context.fillStyle="rgba(200,0,0,0.3)";
this.context.fillRect(0,0,this.getWidth(),this.getHeight());
};
SVGFigure.prototype.setDimension=function(w,h){
Node.prototype.setDimension.call(this,w,h);
this.html.width=w;
this.html.height=h;
this.html.style.width=w+"px";
this.html.style.height=h+"px";
if(this.context!=null){
if(this.context.element){
this.context.element.style.width=w+"px";
this.context.element.style.height=h+"px";
}
this.paint();
}
};
SVGFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!=null){
this.paint();
}
};
SVGFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
SVGFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.context!=null){
this.paint();
}
};
SVGFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.context!=null){
this.paint();
}
};
SVGFigure.prototype.getColor=function(){
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
if(window.getComputedStyle){
return parseInt(getComputedStyle(this.html,"").getPropertyValue("width"));
}
return parseInt(this.html.clientWidth);
};
Label.prototype.getHeight=function(){
if(window.getComputedStyle){
return parseInt(getComputedStyle(this.html,"").getPropertyValue("height"));
}
return parseInt(this.html.clientHeight);
};
Label.prototype.getText=function(){
this.msg=text;
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
Circle=function(_3f81){
Oval.call(this);
if(_3f81){
this.setDimension(_3f81,_3f81);
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
Rectangle=function(width,_3e52){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.lineStroke=1;
Figure.call(this);
if(width&&_3e52){
this.setDimension(width,_3e52);
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
Port=function(_41e9,_41ea){
Corona=function(){
};
Corona.prototype=new Circle;
Corona.prototype.setAlpha=function(_41eb){
Circle.prototype.setAlpha.call(this,Math.min(0.3,_41eb));
};
if(_41e9==null){
this.currentUIRepresentation=new Circle();
}else{
this.currentUIRepresentation=_41e9;
}
if(_41ea==null){
this.connectedUIRepresentation=new Circle();
this.connectedUIRepresentation.setColor(null);
}else{
this.connectedUIRepresentation=_41ea;
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
this.dropable.addEventListener("dragenter",function(_41ec){
_41ec.target.node.onDragEnter(_41ec.relatedTarget.node);
});
this.dropable.addEventListener("dragleave",function(_41ed){
_41ed.target.node.onDragLeave(_41ed.relatedTarget.node);
});
this.dropable.addEventListener("drop",function(_41ee){
_41ee.relatedTarget.node.onDrop(_41ee.target.node);
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
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
var _41f3=this.moveListener.get(i);
this.parentNode.workflow.removeFigure(_41f3);
_41f3.dispose();
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
Port.prototype.setUiRepresentation=function(_41f5){
if(_41f5==null){
_41f5=new Figure();
}
if(this.uiRepresentationAdded){
this.html.removeChild(this.currentUIRepresentation.getHTMLElement());
}
this.html.appendChild(_41f5.getHTMLElement());
_41f5.paint();
this.currentUIRepresentation=_41f5;
};
Port.prototype.onMouseEnter=function(){
this.setLineWidth(2);
};
Port.prototype.onMouseLeave=function(){
this.setLineWidth(0);
};
Port.prototype.setDimension=function(width,_41f7){
Rectangle.prototype.setDimension.call(this,width,_41f7);
this.connectedUIRepresentation.setDimension(width,_41f7);
this.disconnectedUIRepresentation.setDimension(width,_41f7);
this.setPosition(this.x,this.y);
};
Port.prototype.setBackgroundColor=function(color){
this.currentUIRepresentation.setBackgroundColor(color);
};
Port.prototype.getBackgroundColor=function(){
return this.currentUIRepresentation.getBackgroundColor();
};
Port.prototype.getConnections=function(){
var _41f9=new ArrayList();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
var _41fc=this.moveListener.get(i);
if(_41fc instanceof Connection){
_41f9.add(_41fc);
}
}
return _41f9;
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
Port.prototype.setParent=function(_4201){
if(this.parentNode!=null){
this.parentNode.detachMoveListener(this);
}
this.parentNode=_4201;
if(this.parentNode!=null){
this.parentNode.attachMoveListener(this);
}
};
Port.prototype.attachMoveListener=function(_4202){
Rectangle.prototype.attachMoveListener.call(this,_4202);
if(this.hideIfConnected==true){
this.setUiRepresentation(this.connectedUIRepresentation);
}
};
Port.prototype.detachMoveListener=function(_4203){
Rectangle.prototype.detachMoveListener.call(this,_4203);
if(this.getConnections().getSize()==0){
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
var _420a=new CommandConnect(this.parentNode.workflow,port,this);
this.parentNode.workflow.getCommandStack().execute(_420a);
}
};
Port.prototype.getAbsolutePosition=function(){
return new Point(this.getAbsoluteX(),this.getAbsoluteY());
};
Port.prototype.getAbsoluteBounds=function(){
return new Dimension(this.getAbsoluteX(),this.getAbsoluteY(),this.getWidth(),this.getHeight());
};
Port.prototype.getAbsoluteY=function(){
return this.originY+this.parentNode.getY();
};
Port.prototype.getAbsoluteX=function(){
return this.originX+this.parentNode.getX();
};
Port.prototype.onOtherFigureMoved=function(_420b){
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
Port.prototype.showCorona=function(flag,_4214){
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
InputPort=function(_4089){
Port.call(this,_4089);
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
var _408b=new CommandConnect(this.parentNode.workflow,port,this);
this.parentNode.workflow.getCommandStack().execute(_408b);
}
}
};
InputPort.prototype.onDragEnter=function(port){
if(port instanceof OutputPort){
Port.prototype.onDragEnter.call(this,port);
}else{
if(port instanceof LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getSource() instanceof InputPort){
Port.prototype.onDragEnter.call(this,line.getSource());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof InputPort){
Port.prototype.onDragEnter.call(this,line.getTarget());
}
}
}
}
};
InputPort.prototype.onDragLeave=function(port){
if(port instanceof OutputPort){
Port.prototype.onDragLeave.call(this,port);
}else{
if(port instanceof LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getSource() instanceof InputPort){
Port.prototype.onDragLeave.call(this,line.getSource());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof InputPort){
Port.prototype.onDragLeave.call(this,line.getTarget());
}
}
}
}
};
OutputPort=function(_359c){
Port.call(this,_359c);
this.maxFanOut=100;
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
var _359e=new CommandConnect(this.parentNode.workflow,this,port);
this.parentNode.workflow.getCommandStack().execute(_359e);
}
}
};
OutputPort.prototype.onDragEnter=function(port){
if(this.getMaxFanOut()<=this.getFanOut()){
return;
}
if(port instanceof InputPort){
Port.prototype.onDragEnter.call(this,port);
}else{
if(port instanceof LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getSource() instanceof OutputPort){
Port.prototype.onDragEnter.call(this,line.getSource());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof OutputPort){
Port.prototype.onDragEnter.call(this,line.getTarget());
}
}
}
}
};
OutputPort.prototype.onDragLeave=function(port){
if(port instanceof InputPort){
Port.prototype.onDragLeave.call(this,port);
}else{
if(port instanceof LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getSource() instanceof OutputPort){
Port.prototype.onDragLeave.call(this,line.getSource());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof OutputPort){
Port.prototype.onDragLeave.call(this,line.getTarget());
}
}
}
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
var size=lines.getSize();
for(var i=0;i<size;i++){
var line=lines.get(i);
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
this.isMoving=false;
this.zOrder=Line.ZOrderBaseIndex;
this.moveListener=new ArrayList();
this.setSelectable(true);
this.setDeleteable(true);
};
Line.ZOrderBaseIndex=200;
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
Line.prototype.setCanvas=function(_3ee9){
this.canvas=_3ee9;
if(this.graphics!=null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.setWorkflow=function(_3eea){
this.workflow=_3eea;
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
Line.prototype.attachMoveListener=function(_3eeb){
this.moveListener.add(_3eeb);
};
Line.prototype.detachMoveListener=function(_3eec){
this.moveListener.remove(_3eec);
};
Line.prototype.fireMoveEvent=function(){
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
Line.prototype.onOtherFigureMoved=function(_3eef){
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
Line.prototype.setAlpha=function(_3ef2){
if(_3ef2==this.alpha){
return;
}
try{
this.html.style.MozOpacity=_3ef2;
}
catch(exc){
}
try{
this.html.style.opacity=_3ef2;
}
catch(exc){
}
try{
var _3ef3=Math.round(_3ef2*100);
if(_3ef3>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_3ef3+")";
}
}
catch(exc){
}
this.alpha=_3ef2;
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
var _3efa=this.getLength();
var angle=-(180/Math.PI)*Math.asin((this.startY-this.endY)/_3efa);
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
var _3f00=10;
var _3f01=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_3f00;i++){
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
var _3f0d=5;
X2-=X1;
Y2-=Y1;
px-=X1;
py-=Y1;
var _3f0e=px*X2+py*Y2;
var _3f0f;
if(_3f0e<=0){
_3f0f=0;
}else{
px=X2-px;
py=Y2-py;
_3f0e=px*X2+py*Y2;
if(_3f0e<=0){
_3f0f=0;
}else{
_3f0f=_3f0e*_3f0e/(X2*X2+Y2*Y2);
}
}
var lenSq=px*px+py*py-_3f0f;
if(lenSq<0){
lenSq=0;
}
return Math.sqrt(lenSq)<_3f0d;
};
ConnectionRouter=function(){
};
ConnectionRouter.prototype.type="ConnectionRouter";
ConnectionRouter.prototype.getDirection=function(r,p){
var _3faa=Math.abs(r.x-p.x);
var _3fab=3;
var i=Math.abs(r.y-p.y);
if(i<=_3faa){
_3faa=i;
_3fab=0;
}
i=Math.abs(r.getBottom()-p.y);
if(i<=_3faa){
_3faa=i;
_3fab=2;
}
i=Math.abs(r.getRight()-p.x);
if(i<_3faa){
_3faa=i;
_3fab=1;
}
return _3fab;
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
ConnectionRouter.prototype.route=function(_3fb3){
};
NullConnectionRouter=function(){
};
NullConnectionRouter.prototype=new ConnectionRouter;
NullConnectionRouter.prototype.type="NullConnectionRouter";
NullConnectionRouter.prototype.invalidate=function(){
};
NullConnectionRouter.prototype.route=function(_3f8c){
_3f8c.addPoint(_3f8c.getStartPoint());
_3f8c.addPoint(_3f8c.getEndPoint());
};
ManhattanConnectionRouter=function(){
this.MINDIST=20;
};
ManhattanConnectionRouter.prototype=new ConnectionRouter;
ManhattanConnectionRouter.prototype.type="ManhattanConnectionRouter";
ManhattanConnectionRouter.prototype.route=function(conn){
var _3bc5=conn.getStartPoint();
var _3bc6=this.getStartDirection(conn);
var toPt=conn.getEndPoint();
var toDir=this.getEndDirection(conn);
this._route(conn,toPt,toDir,_3bc5,_3bc6);
};
ManhattanConnectionRouter.prototype._route=function(conn,_3bca,_3bcb,toPt,toDir){
var TOL=0.1;
var _3bcf=0.01;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_3bca.x-toPt.x;
var yDiff=_3bca.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_3bcf))&&((yDiff*yDiff)<(_3bcf))){
conn.addPoint(new Point(toPt.x,toPt.y));
return;
}
if(_3bcb==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir==RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_3bca.x-this.MINDIST,_3bca.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_3bca.y);
}else{
if(_3bcb==toDir){
var pos=Math.min(_3bca.x,toPt.x)-this.MINDIST;
point=new Point(pos,_3bca.y);
}else{
point=new Point(_3bca.x-(xDiff/2),_3bca.y);
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
if(_3bcb==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_3bca.x+this.MINDIST,_3bca.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_3bca.y);
}else{
if(_3bcb==toDir){
var pos=Math.max(_3bca.x,toPt.x)+this.MINDIST;
point=new Point(pos,_3bca.y);
}else{
point=new Point(_3bca.x-(xDiff/2),_3bca.y);
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
if(_3bcb==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_3bca.x,_3bca.y+this.MINDIST);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_3bca.x,toPt.y);
}else{
if(_3bcb==toDir){
var pos=Math.max(_3bca.y,toPt.y)+this.MINDIST;
point=new Point(_3bca.x,pos);
}else{
point=new Point(_3bca.x,_3bca.y-(yDiff/2));
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
if(_3bcb==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir==DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_3bca.x,_3bca.y-this.MINDIST);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_3bca.x,toPt.y);
}else{
if(_3bcb==toDir){
var pos=Math.min(_3bca.y,toPt.y)-this.MINDIST;
point=new Point(_3bca.x,pos);
}else{
point=new Point(_3bca.x,_3bca.y-(yDiff/2));
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
conn.addPoint(_3bca);
};
BezierConnectionRouter=function(_3567){
if(!_3567){
this.cheapRouter=new ManhattanConnectionRouter();
}else{
this.cheapRouter=null;
}
this.iteration=5;
};
BezierConnectionRouter.prototype=new ConnectionRouter;
BezierConnectionRouter.prototype.type="BezierConnectionRouter";
BezierConnectionRouter.prototype.drawBezier=function(_3568,_3569,t,iter){
var n=_3568.length-1;
var q=new Array();
var _356e=n+1;
for(var i=0;i<_356e;i++){
q[i]=new Array();
q[i][0]=_3568[i];
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
this.drawBezier(c1,_3569,t,--iter);
this.drawBezier(c2,_3569,t,--iter);
}else{
for(var i=0;i<n;i++){
_3569.push(q[i][n-i]);
}
}
};
BezierConnectionRouter.prototype.route=function(conn){
if(this.cheapRouter!=null&&(conn.getSource().getParent().isMoving==true||conn.getTarget().getParent().isMoving==true)){
this.cheapRouter.route(conn);
return;
}
var _3574=new Array();
var _3575=conn.getStartPoint();
var toPt=conn.getEndPoint();
this._route(_3574,conn,toPt,this.getEndDirection(conn),_3575,this.getStartDirection(conn));
var _3577=new Array();
this.drawBezier(_3574,_3577,0.5,this.iteration);
for(var i=0;i<_3577.length;i++){
conn.addPoint(_3577[i]);
}
conn.addPoint(toPt);
};
BezierConnectionRouter.prototype._route=function(_3579,conn,_357b,_357c,toPt,toDir){
var TOL=0.1;
var _3580=0.01;
var _3581=90;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_357b.x-toPt.x;
var yDiff=_357b.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_3580))&&((yDiff*yDiff)<(_3580))){
_3579.push(new Point(toPt.x,toPt.y));
return;
}
if(_357c==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir==RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_357b.x-_3581,_357b.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_357b.y);
}else{
if(_357c==toDir){
var pos=Math.min(_357b.x,toPt.x)-_3581;
point=new Point(pos,_357b.y);
}else{
point=new Point(_357b.x-(xDiff/2),_357b.y);
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
if(_357c==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_357b.x+_3581,_357b.y);
}else{
if(((yDiff>0)&&(toDir==DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_357b.y);
}else{
if(_357c==toDir){
var pos=Math.max(_357b.x,toPt.x)+_3581;
point=new Point(pos,_357b.y);
}else{
point=new Point(_357b.x-(xDiff/2),_357b.y);
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
if(_357c==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_357b.x,_357b.y+_3581);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_357b.x,toPt.y);
}else{
if(_357c==toDir){
var pos=Math.max(_357b.y,toPt.y)+_3581;
point=new Point(_357b.x,pos);
}else{
point=new Point(_357b.x,_357b.y-(yDiff/2));
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
if(_357c==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir==DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_357b.x,_357b.y-_3581);
}else{
if(((xDiff>0)&&(toDir==RIGHT))||((xDiff<0)&&(toDir==LEFT))){
point=new Point(_357b.x,toPt.y);
}else{
if(_357c==toDir){
var pos=Math.min(_357b.y,toPt.y)-_3581;
point=new Point(_357b.x,pos);
}else{
point=new Point(_357b.x,_357b.y-(yDiff/2));
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
this._route(_3579,conn,point,dir,toPt,toDir);
_3579.push(_357b);
};
FanConnectionRouter=function(){
};
FanConnectionRouter.prototype=new NullConnectionRouter;
FanConnectionRouter.prototype.type="FanConnectionRouter";
FanConnectionRouter.prototype.route=function(conn){
var _40ea=conn.getStartPoint();
var toPt=conn.getEndPoint();
var lines=conn.getSource().getConnections();
var _40ed=new ArrayList();
var index=0;
for(var i=0;i<lines.getSize();i++){
var _40f0=lines.get(i);
if(_40f0.getTarget()==conn.getTarget()||_40f0.getSource()==conn.getTarget()){
_40ed.add(_40f0);
if(conn==_40f0){
index=_40ed.getSize();
}
}
}
if(_40ed.getSize()>1){
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
var _40f6=10;
var _40f7=new Point((end.x+start.x)/2,(end.y+start.y)/2);
var _40f8=end.getPosition(start);
var ray;
if(_40f8==PositionConstants.SOUTH||_40f8==PositionConstants.EAST){
ray=new Point(end.x-start.x,end.y-start.y);
}else{
ray=new Point(start.x-end.x,start.y-end.y);
}
var _40fa=Math.sqrt(ray.x*ray.x+ray.y*ray.y);
var _40fb=_40f6*ray.x/_40fa;
var _40fc=_40f6*ray.y/_40fa;
var _40fd;
if(index%2==0){
_40fd=new Point(_40f7.x+(index/2)*(-1*_40fc),_40f7.y+(index/2)*_40fb);
}else{
_40fd=new Point(_40f7.x+(index/2)*_40fc,_40f7.y+(index/2)*(-1*_40fb));
}
conn.addPoint(_40fd);
conn.addPoint(end);
};
Graphics=function(_3a7e,_3a7f,_3a80){
this.jsGraphics=_3a7e;
this.xt=_3a80.x;
this.yt=_3a80.y;
this.radian=_3a7f*Math.PI/180;
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
Graphics.prototype.fillPolygon=function(_3a96,_3a97){
var rotX=new Array();
var rotY=new Array();
for(var i=0;i<_3a96.length;i++){
rotX[i]=this.xt+_3a96[i]*this.cosRadian-_3a97[i]*this.sinRadian;
rotY[i]=this.yt+_3a96[i]*this.sinRadian+_3a97[i]*this.cosRadian;
}
this.jsGraphics.fillPolygon(rotX,rotY);
};
Graphics.prototype.setColor=function(color){
this.jsGraphics.setColor(color.getHTMLStyle());
};
Graphics.prototype.drawPolygon=function(_3a9c,_3a9d){
var rotX=new Array();
var rotY=new Array();
for(var i=0;i<_3a9c.length;i++){
rotX[i]=this.xt+_3a9c[i]*this.cosRadian-_3a9d[i]*this.sinRadian;
rotY[i]=this.yt+_3a9c[i]*this.sinRadian+_3a9d[i]*this.cosRadian;
}
this.jsGraphics.drawPolygon(rotX,rotY);
};
Connection=function(){
Line.call(this);
this.sourcePort=null;
this.targetPort=null;
this.sourceDecorator=null;
this.targetDecorator=null;
this.sourceAnchor=new ConnectionAnchor();
this.targetAnchor=new ConnectionAnchor();
this.router=Connection.defaultRouter;
this.lineSegments=new ArrayList();
this.children=new ArrayList();
this.setColor(new Color(0,0,115));
this.setLineWidth(1);
};
Connection.prototype=new Line;
Connection.defaultRouter=new ManhattanConnectionRouter();
Connection.setDefaultRouter=function(_2e41){
Connection.defaultRouter=_2e41;
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
return true;
};
Connection.prototype.addFigure=function(_2e42,_2e43){
var entry=new Object();
entry.figure=_2e42;
entry.locator=_2e43;
this.children.add(entry);
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setSourceDecorator=function(_2e45){
this.sourceDecorator=_2e45;
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setTargetDecorator=function(_2e46){
this.targetDecorator=_2e46;
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setSourceAnchor=function(_2e47){
this.sourceAnchor=_2e47;
this.sourceAnchor.setOwner(this.sourcePort);
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setTargetAnchor=function(_2e48){
this.targetAnchor=_2e48;
this.targetAnchor.setOwner(this.targetPort);
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.setRouter=function(_2e49){
if(_2e49!=null){
this.router=_2e49;
}else{
this.router=new NullConnectionRouter();
}
if(this.graphics!=null){
this.paint();
}
};
Connection.prototype.getRouter=function(){
return this.router;
};
Connection.prototype.paint=function(){
for(var i=0;i<this.children.getSize();i++){
var entry=this.children.get(i);
if(entry.isAppended==true){
this.html.removeChild(entry.figure.getHTMLElement());
}
entry.isAppended=false;
}
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
for(var i=0;i<this.children.getSize();i++){
var entry=this.children.get(i);
this.html.appendChild(entry.figure.getHTMLElement());
entry.isAppended=true;
entry.locator.relocate(entry.figure);
}
};
Connection.prototype.getStartPoint=function(){
if(this.isMoving==false){
return this.sourceAnchor.getLocation(this.targetAnchor.getReferencePoint());
}else{
return Line.prototype.getStartPoint.call(this);
}
};
Connection.prototype.getEndPoint=function(){
if(this.isMoving==false){
return this.targetAnchor.getLocation(this.sourceAnchor.getReferencePoint());
}else{
return Line.prototype.getEndPoint.call(this);
}
};
Connection.prototype.startStroke=function(){
this.oldPoint=null;
this.lineSegments=new ArrayList();
};
Connection.prototype.finishStroke=function(){
this.graphics.paint();
this.oldPoint=null;
};
Connection.prototype.getPoints=function(){
var _2e4c=new ArrayList();
var line;
for(var i=0;i<this.lineSegments.getSize();i++){
line=this.lineSegments.get(i);
_2e4c.add(line.start);
}
_2e4c.add(line.end);
return _2e4c;
};
Connection.prototype.addPoint=function(p){
p=new Point(parseInt(p.x),parseInt(p.y));
if(this.oldPoint!=null){
this.graphics.drawLine(this.oldPoint.x,this.oldPoint.y,p.x,p.y);
var line=new Object();
line.start=this.oldPoint;
line.end=p;
this.lineSegments.add(line);
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
this.sourceAnchor.setOwner(this.sourcePort);
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
this.targetAnchor.setOwner(this.targetPort);
this.fireTargetPortRouteEvent();
this.targetPort.attachMoveListener(this);
this.setEndPoint(port.getAbsoluteX(),port.getAbsoluteY());
};
Connection.prototype.getTarget=function(){
return this.targetPort;
};
Connection.prototype.onOtherFigureMoved=function(_2e53){
if(_2e53==this.sourcePort){
this.setStartPoint(this.sourcePort.getAbsoluteX(),this.sourcePort.getAbsoluteY());
}else{
this.setEndPoint(this.targetPort.getAbsoluteX(),this.targetPort.getAbsoluteY());
}
};
Connection.prototype.containsPoint=function(px,py){
for(var i=0;i<this.lineSegments.getSize();i++){
var line=this.lineSegments.get(i);
if(Line.hit(line.start.x,line.start.y,line.end.x,line.end.y,px,py)){
return true;
}
}
return false;
};
Connection.prototype.getStartAngle=function(){
var p1=this.lineSegments.get(0).start;
var p2=this.lineSegments.get(0).end;
if(this.router instanceof BezierConnectionRouter){
p2=this.lineSegments.get(5).end;
}
var _2e5a=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_2e5a);
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
var p1=this.lineSegments.get(this.lineSegments.getSize()-1).end;
var p2=this.lineSegments.get(this.lineSegments.getSize()-1).start;
if(this.router instanceof BezierConnectionRouter){
p2=this.lineSegments.get(this.lineSegments.getSize()-5).end;
}
var _2e5e=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_2e5e);
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
var _2e60=this.sourcePort.getConnections();
for(var i=0;i<_2e60.getSize();i++){
_2e60.get(i).paint();
}
};
Connection.prototype.fireTargetPortRouteEvent=function(){
var _2e62=this.targetPort.getConnections();
for(var i=0;i<_2e62.getSize();i++){
_2e62.get(i).paint();
}
};
ConnectionAnchor=function(owner){
this.owner=owner;
};
ConnectionAnchor.prototype.type="ConnectionAnchor";
ConnectionAnchor.prototype.getLocation=function(_40e5){
return this.getReferencePoint();
};
ConnectionAnchor.prototype.getOwner=function(){
return this.owner;
};
ConnectionAnchor.prototype.setOwner=function(owner){
this.owner=owner;
};
ConnectionAnchor.prototype.getBox=function(){
return this.getOwner().getAbsoluteBounds();
};
ConnectionAnchor.prototype.getReferencePoint=function(){
if(this.getOwner()==null){
return null;
}else{
return this.getOwner().getAbsolutePosition();
}
};
ChopboxConnectionAnchor=function(owner){
ConnectionAnchor.call(this,owner);
};
ChopboxConnectionAnchor.prototype=new ConnectionAnchor;
ChopboxConnectionAnchor.prototype.type="ChopboxConnectionAnchor";
ChopboxConnectionAnchor.prototype.getLocation=function(_3871){
var r=new Dimension();
r.setBounds(this.getBox());
r.translate(-1,-1);
r.resize(1,1);
var _3873=r.x+r.w/2;
var _3874=r.y+r.h/2;
if(r.isEmpty()||(_3871.x==_3873&&_3871.y==_3874)){
return new Point(_3873,_3874);
}
var dx=_3871.x-_3873;
var dy=_3871.y-_3874;
var scale=0.5/Math.max(Math.abs(dx)/r.w,Math.abs(dy)/r.h);
dx*=scale;
dy*=scale;
_3873+=dx;
_3874+=dy;
return new Point(Math.round(_3873),Math.round(_3874));
};
ChopboxConnectionAnchor.prototype.getBox=function(){
return this.getOwner().getParent().getBounds();
};
ChopboxConnectionAnchor.prototype.getReferencePoint=function(){
return this.getBox().getCenter();
};
ConnectionDecorator=function(){
this.color=new Color(0,0,0);
this.backgroundColor=new Color(250,250,250);
};
ConnectionDecorator.prototype.type="ConnectionDecorator";
ConnectionDecorator.prototype.paint=function(g){
};
ConnectionDecorator.prototype.setColor=function(c){
this.color=c;
};
ConnectionDecorator.prototype.setBackgroundColor=function(c){
this.backgroundColor=c;
};
ArrowConnectionDecorator=function(){
};
ArrowConnectionDecorator.prototype=new ConnectionDecorator;
ArrowConnectionDecorator.prototype.type="ArrowConnectionDecorator";
ArrowConnectionDecorator.prototype.paint=function(g){
if(this.backgroundColor!=null){
g.setColor(this.backgroundColor);
g.fillPolygon([3,20,20,3],[0,5,-5,0]);
}
g.setColor(this.color);
g.setStroke(1);
g.drawPolygon([3,20,20,3],[0,5,-5,0]);
};
CompartmentFigure=function(){
Node.call(this);
this.children=new ArrayList();
this.setBorder(new LineBorder(1));
this.dropable=new DropTarget(this.html);
this.dropable.node=this;
this.dropable.addEventListener("figureenter",function(_4095){
_4095.target.node.onFigureEnter(_4095.relatedTarget.node);
});
this.dropable.addEventListener("figureleave",function(_4096){
_4096.target.node.onFigureLeave(_4096.relatedTarget.node);
});
this.dropable.addEventListener("figuredrop",function(_4097){
_4097.target.node.onFigureDrop(_4097.relatedTarget.node);
});
};
CompartmentFigure.prototype=new Node;
CompartmentFigure.prototype.type="CompartmentFigure";
CompartmentFigure.prototype.onFigureEnter=function(_4098){
};
CompartmentFigure.prototype.onFigureLeave=function(_4099){
};
CompartmentFigure.prototype.onFigureDrop=function(_409a){
};
CompartmentFigure.prototype.getChildren=function(){
return this.children;
};
CompartmentFigure.prototype.addChild=function(_409b){
_409b.setZOrder(this.getZOrder()+1);
_409b.setParent(this);
this.children.add(_409b);
};
CompartmentFigure.prototype.removeChild=function(_409c){
_409c.setParent(null);
this.children.remove(_409c);
};
CompartmentFigure.prototype.setZOrder=function(index){
Node.prototype.setZOrder.call(this,index);
for(var i=0;i<this.children.getSize();i++){
this.children.get(i).setZOrder(index+1);
}
};
CompartmentFigure.prototype.setPosition=function(xPos,yPos){
var oldX=this.getX();
var oldY=this.getY();
Node.prototype.setPosition.call(this,xPos,yPos);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
CompartmentFigure.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Node.prototype.onDrag.call(this);
for(var i=0;i<this.children.getSize();i++){
var child=this.children.get(i);
child.setPosition(child.getX()+this.getX()-oldX,child.getY()+this.getY()-oldY);
}
};
Document=function(_3a58){
this.canvas=_3a58;
};
Document.prototype.getFigures=function(){
var _3a59=new ArrayList();
var _3a5a=this.canvas.figures;
var _3a5b=this.canvas.dialogs;
for(var i=0;i<_3a5a.getSize();i++){
var _3a5d=_3a5a.get(i);
if(_3a5b.indexOf(_3a5d)==-1&&_3a5d.getParent()==null&&!(_3a5d instanceof Window)){
_3a59.add(_3a5d);
}
}
return _3a59;
};
Document.prototype.getFigure=function(id){
return this.canvas.getFigure(id);
};
Document.prototype.getLines=function(){
return this.canvas.getLines();
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
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
this.disableTextSelection(item);
return item;
};
Annotation.prototype.onDoubleClick=function(){
var _40c1=new AnnotationDialog(this);
this.workflow.showDialog(_40c1);
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
ResizeHandle=function(_2d4c,type){
Rectangle.call(this,5,5);
this.type=type;
var _2d4e=this.getWidth();
var _2d4f=_2d4e/2;
switch(this.type){
case 1:
this.setSnapToGridAnchor(new Point(_2d4e,_2d4e));
break;
case 2:
this.setSnapToGridAnchor(new Point(_2d4f,_2d4e));
break;
case 3:
this.setSnapToGridAnchor(new Point(0,_2d4e));
break;
case 4:
this.setSnapToGridAnchor(new Point(0,_2d4f));
break;
case 5:
this.setSnapToGridAnchor(new Point(0,0));
break;
case 6:
this.setSnapToGridAnchor(new Point(_2d4f,0));
break;
case 7:
this.setSnapToGridAnchor(new Point(_2d4e,0));
break;
case 8:
this.setSnapToGridAnchor(new Point(_2d4e,_2d4f));
break;
}
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_2d4c);
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
var _2d50=this.workflow.currentSelection;
this.commandMove.setPosition(_2d50.getX(),_2d50.getY());
this.commandResize.setDimension(_2d50.getWidth(),_2d50.getHeight());
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
var _2d55=this.workflow.currentSelection;
this.commandMove=new CommandMove(_2d55,_2d55.getX(),_2d55.getY());
this.commandResize=new CommandResize(_2d55,_2d55.getWidth(),_2d55.getHeight());
return true;
};
ResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _2d5a=this.workflow.currentSelection.getX();
var _2d5b=this.workflow.currentSelection.getY();
var _2d5c=this.workflow.currentSelection.getWidth();
var _2d5d=this.workflow.currentSelection.getHeight();
switch(this.type){
case 1:
this.workflow.currentSelection.setPosition(_2d5a-diffX,_2d5b-diffY);
this.workflow.currentSelection.setDimension(_2d5c+diffX,_2d5d+diffY);
break;
case 2:
this.workflow.currentSelection.setPosition(_2d5a,_2d5b-diffY);
this.workflow.currentSelection.setDimension(_2d5c,_2d5d+diffY);
break;
case 3:
this.workflow.currentSelection.setPosition(_2d5a,_2d5b-diffY);
this.workflow.currentSelection.setDimension(_2d5c-diffX,_2d5d+diffY);
break;
case 4:
this.workflow.currentSelection.setPosition(_2d5a,_2d5b);
this.workflow.currentSelection.setDimension(_2d5c-diffX,_2d5d);
break;
case 5:
this.workflow.currentSelection.setPosition(_2d5a,_2d5b);
this.workflow.currentSelection.setDimension(_2d5c-diffX,_2d5d-diffY);
break;
case 6:
this.workflow.currentSelection.setPosition(_2d5a,_2d5b);
this.workflow.currentSelection.setDimension(_2d5c,_2d5d-diffY);
break;
case 7:
this.workflow.currentSelection.setPosition(_2d5a-diffX,_2d5b);
this.workflow.currentSelection.setDimension(_2d5c+diffX,_2d5d-diffY);
break;
case 8:
this.workflow.currentSelection.setPosition(_2d5a-diffX,_2d5b);
this.workflow.currentSelection.setDimension(_2d5c+diffX,_2d5d);
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
}
};
ResizeHandle.prototype.onKeyDown=function(_2d5f,ctrl){
this.workflow.onKeyDown(_2d5f,ctrl);
};
ResizeHandle.prototype.fireMoveEvent=function(){
};
LineStartResizeHandle=function(_411c){
Rectangle.call(this);
this.setDimension(10,10);
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_411c);
this.setZOrder(10000);
};
LineStartResizeHandle.prototype=new Rectangle;
LineStartResizeHandle.prototype.type="LineStartResizeHandle";
LineStartResizeHandle.prototype.onDragend=function(){
var line=this.workflow.currentSelection;
if(line instanceof Connection){
var start=line.sourceAnchor.getLocation(line.targetAnchor.getReferencePoint());
line.setStartPoint(start.x,start.y);
this.getWorkflow().showLineResizeHandles(line);
line.setRouter(line.oldRouter);
}else{
if(this.command==null){
return;
}
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command.setEndPoints(x1,y1,x2,y2);
this.getWorkflow().getCommandStack().execute(this.command);
this.command=null;
}
};
LineStartResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var line=this.workflow.currentSelection;
if(line instanceof Connection){
this.command=new CommandReconnect(line);
line.oldRouter=line.getRouter();
line.setRouter(new NullConnectionRouter());
}else{
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command=new CommandMoveLine(line,x1,y1,x2,y2);
}
return true;
};
LineStartResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _412e=this.workflow.currentSelection.getStartPoint();
var line=this.workflow.currentSelection;
line.setStartPoint(_412e.x-diffX,_412e.y-diffY);
line.isMoving=true;
};
LineStartResizeHandle.prototype.onDrop=function(_4130){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof Connection){
this.command.setNewPorts(_4130,line.getTarget());
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
LineStartResizeHandle.prototype.onKeyDown=function(_4132,ctrl){
if(this.workflow!=null){
this.workflow.onKeyDown(_4132,ctrl);
}
};
LineStartResizeHandle.prototype.fireMoveEvent=function(){
};
LineEndResizeHandle=function(_3f4c){
Rectangle.call(this);
this.setDimension(10,10);
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_3f4c);
this.setZOrder(10000);
};
LineEndResizeHandle.prototype=new Rectangle;
LineEndResizeHandle.prototype.type="LineEndResizeHandle";
LineEndResizeHandle.prototype.onDragend=function(){
var line=this.workflow.currentSelection;
if(line instanceof Connection){
var end=line.targetAnchor.getLocation(line.sourceAnchor.getReferencePoint());
line.setEndPoint(end.x,end.y);
this.getWorkflow().showLineResizeHandles(line);
line.setRouter(line.oldRouter);
}else{
if(this.command==null){
return;
}
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command.setEndPoints(x1,y1,x2,y2);
this.workflow.getCommandStack().execute(this.command);
this.command=null;
}
};
LineEndResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var line=this.workflow.currentSelection;
if(line instanceof Connection){
this.command=new CommandReconnect(line);
line.oldRouter=line.getRouter();
line.setRouter(new NullConnectionRouter());
}else{
var x1=line.getStartX();
var y1=line.getStartY();
var x2=line.getEndX();
var y2=line.getEndY();
this.command=new CommandMoveLine(line,x1,y1,x2,y2);
}
return true;
};
LineEndResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _3f5e=this.workflow.currentSelection.getEndPoint();
var line=this.workflow.currentSelection;
line.setEndPoint(_3f5e.x-diffX,_3f5e.y-diffY);
line.isMoving=true;
};
LineEndResizeHandle.prototype.onDrop=function(_3f60){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof Connection){
this.command.setNewPorts(line.getSource(),_3f60);
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
LineEndResizeHandle.prototype.onKeyDown=function(_3f62){
if(this.workflow!=null){
this.workflow.onKeyDown(_3f62);
}
};
LineEndResizeHandle.prototype.fireMoveEvent=function(){
};
Canvas=function(_3e77){
if(_3e77){
this.construct(_3e77);
}
this.enableSmoothFigureHandling=false;
this.canvasLines=new ArrayList();
};
Canvas.prototype.type="Canvas";
Canvas.prototype.construct=function(_3e78){
this.canvasId=_3e78;
this.html=document.getElementById(this.canvasId);
this.scrollArea=document.body.parentNode;
};
Canvas.prototype.setViewPort=function(divId){
this.scrollArea=document.getElementById(divId);
};
Canvas.prototype.addFigure=function(_3e7a,xPos,yPos,_3e7d){
if(this.enableSmoothFigureHandling==true){
if(_3e7a.timer<=0){
_3e7a.setAlpha(0.001);
}
var _3e7e=_3e7a;
var _3e7f=function(){
if(_3e7e.alpha<1){
_3e7e.setAlpha(Math.min(1,_3e7e.alpha+0.05));
}else{
window.clearInterval(_3e7e.timer);
_3e7e.timer=-1;
}
};
if(_3e7e.timer>0){
window.clearInterval(_3e7e.timer);
}
_3e7e.timer=window.setInterval(_3e7f,30);
}
_3e7a.setCanvas(this);
if(xPos&&yPos){
_3e7a.setPosition(xPos,yPos);
}
if(_3e7a instanceof Line){
this.canvasLines.add(_3e7a);
this.html.appendChild(_3e7a.getHTMLElement());
}else{
var obj=this.canvasLines.getFirstElement();
if(obj==null){
this.html.appendChild(_3e7a.getHTMLElement());
}else{
this.html.insertBefore(_3e7a.getHTMLElement(),obj.getHTMLElement());
}
}
if(!_3e7d){
_3e7a.paint();
}
};
Canvas.prototype.removeFigure=function(_3e81){
if(this.enableSmoothFigureHandling==true){
var oThis=this;
var _3e83=_3e81;
var _3e84=function(){
if(_3e83.alpha>0){
_3e83.setAlpha(Math.max(0,_3e83.alpha-0.05));
}else{
window.clearInterval(_3e83.timer);
_3e83.timer=-1;
oThis.html.removeChild(_3e83.html);
_3e83.setCanvas(null);
}
};
if(_3e83.timer>0){
window.clearInterval(_3e83.timer);
}
_3e83.timer=window.setInterval(_3e84,20);
}else{
this.html.removeChild(_3e81.html);
_3e81.setCanvas(null);
}
if(_3e81 instanceof Line){
this.canvasLines.remove(_3e81);
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
Canvas.prototype.setBackgroundImage=function(_3e86,_3e87){
if(_3e86!=null){
if(_3e87){
this.html.style.background="transparent url("+_3e86+") ";
}else{
this.html.style.background="transparent url("+_3e86+") no-repeat";
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
this.tooltip=null;
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
var _4139=function(){
var _413a=arguments[0]||window.event;
var diffX=_413a.clientX;
var diffY=_413a.clientY;
var _413d=oThis.getScrollLeft();
var _413e=oThis.getScrollTop();
var _413f=oThis.getAbsoluteX();
var _4140=oThis.getAbsoluteY();
if(oThis.getBestFigure(diffX+_413d-_413f,diffY+_413e-_4140)!=null){
return;
}
var line=oThis.getBestLine(diffX+_413d-_413f,diffY+_413e-_4140,null);
if(line!=null){
line.onContextMenu(diffX+_413d-_413f,diffY+_413e-_4140);
}else{
oThis.onContextMenu(diffX+_413d-_413f,diffY+_413e-_4140);
}
};
this.html.oncontextmenu=function(){
return false;
};
var oThis=this;
var _4143=function(event){
var ctrl=event.ctrlKey;
oThis.onKeyDown(event.keyCode,ctrl);
};
var _4146=function(){
var _4147=arguments[0]||window.event;
var diffX=_4147.clientX;
var diffY=_4147.clientY;
var _414a=oThis.getScrollLeft();
var _414b=oThis.getScrollTop();
var _414c=oThis.getAbsoluteX();
var _414d=oThis.getAbsoluteY();
oThis.onMouseDown(diffX+_414a-_414c,diffY+_414b-_414d);
};
var _414e=function(){
var _414f=arguments[0]||window.event;
if(oThis.currentMenu!=null){
oThis.removeFigure(oThis.currentMenu);
oThis.currentMenu=null;
}
if(_414f.button==2){
return;
}
var diffX=_414f.clientX;
var diffY=_414f.clientY;
var _4152=oThis.getScrollLeft();
var _4153=oThis.getScrollTop();
var _4154=oThis.getAbsoluteX();
var _4155=oThis.getAbsoluteY();
oThis.onMouseUp(diffX+_4152-_4154,diffY+_4153-_4155);
};
var _4156=function(){
var _4157=arguments[0]||window.event;
var diffX=_4157.clientX;
var diffY=_4157.clientY;
var _415a=oThis.getScrollLeft();
var _415b=oThis.getScrollTop();
var _415c=oThis.getAbsoluteX();
var _415d=oThis.getAbsoluteY();
oThis.currentMouseX=diffX+_415a-_415c;
oThis.currentMouseY=diffY+_415b-_415d;
var obj=oThis.getBestFigure(oThis.currentMouseX,oThis.currentMouseY);
if(Drag.currentHover!=null&&obj==null){
var _415f=new DragDropEvent();
_415f.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_415f);
}else{
var diffX=_4157.clientX;
var diffY=_4157.clientY;
var _415a=oThis.getScrollLeft();
var _415b=oThis.getScrollTop();
var _415c=oThis.getAbsoluteX();
var _415d=oThis.getAbsoluteY();
oThis.onMouseMove(diffX+_415a-_415c,diffY+_415b-_415d);
}
if(obj==null){
Drag.currentHover=null;
}
if(oThis.tooltip!=null){
if(Math.abs(oThis.currentTooltipX-oThis.currentMouseX)>10||Math.abs(oThis.currentTooltipY-oThis.currentMouseY)>10){
oThis.showTooltip(null);
}
}
};
var _4160=function(_4161){
var _4161=arguments[0]||window.event;
var diffX=_4161.clientX;
var diffY=_4161.clientY;
var _4164=oThis.getScrollLeft();
var _4165=oThis.getScrollTop();
var _4166=oThis.getAbsoluteX();
var _4167=oThis.getAbsoluteY();
var line=oThis.getBestLine(diffX+_4164-_4166,diffY+_4165-_4167,null);
if(line!=null){
line.onDoubleClick();
}
};
if(this.html.addEventListener){
this.html.addEventListener("contextmenu",_4139,false);
this.html.addEventListener("mousemove",_4156,false);
this.html.addEventListener("mouseup",_414e,false);
this.html.addEventListener("mousedown",_4146,false);
this.html.addEventListener("keydown",_4143,false);
this.html.addEventListener("dblclick",_4160,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("oncontextmenu",_4139);
this.html.attachEvent("onmousemove",_4156);
this.html.attachEvent("onmousedown",_4146);
this.html.attachEvent("onmouseup",_414e);
this.html.attachEvent("onkeydown",_4143);
this.html.attachEvent("ondblclick",_4160);
}else{
throw new Error("Open-jACOB Draw2D not supported in this browser.");
}
}
}
};
Workflow.prototype=new Canvas;
Workflow.prototype.type="Workflow";
Workflow.COLOR_GREEN=new Color(0,255,0);
Workflow.prototype.clear=function(){
this.scrollTo(0,0,true);
this.gridWidthX=10;
this.gridWidthY=10;
this.snapToGridHelper=null;
this.verticalSnapToHelperLine=null;
this.horizontalSnapToHelperLine=null;
var _4169=this.getDocument();
var _416a=_4169.getLines().clone();
for(var i=0;i<_416a.getSize();i++){
new CommandDelete(_416a.get(i)).execute();
}
var _416c=_4169.getFigures().clone();
for(var i=0;i<_416c.getSize();i++){
new CommandDelete(_416c.get(i)).execute();
}
this.commonPorts.removeAllElements();
this.dropTargets.removeAllElements();
this.compartments.removeAllElements();
this.selectionListeners.removeAllElements();
this.dialogs.removeAllElements();
this.commandStack=new CommandStack();
this.currentSelection=null;
this.currentMenu=null;
Drag.clearCurrent();
};
Workflow.prototype.onScroll=function(){
var _416d=this.getScrollLeft();
var _416e=this.getScrollTop();
var _416f=_416d-this.oldScrollPosLeft;
var _4170=_416e-this.oldScrollPosTop;
for(var i=0;i<this.figures.getSize();i++){
var _4172=this.figures.get(i);
if(_4172.hasFixedPosition&&_4172.hasFixedPosition()==true){
_4172.setPosition(_4172.getX()+_416f,_4172.getY()+_4170);
}
}
this.oldScrollPosLeft=_416d;
this.oldScrollPosTop=_416e;
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
Workflow.prototype.showTooltip=function(_417d,_417e){
if(this.tooltip!=null){
this.removeFigure(this.tooltip);
this.tooltip=null;
if(this.tooltipTimer>=0){
window.clearTimeout(this.tooltipTimer);
this.tooltipTimer=-1;
}
}
this.tooltip=_417d;
if(this.tooltip!=null){
this.currentTooltipX=this.currentMouseX;
this.currentTooltipY=this.currentMouseY;
this.addFigure(this.tooltip,this.currentTooltipX+10,this.currentTooltipY+10);
var oThis=this;
var _4180=function(){
oThis.tooltipTimer=-1;
oThis.showTooltip(null);
};
if(_417e==true){
this.tooltipTimer=window.setTimeout(_4180,5000);
}
}
};
Workflow.prototype.showDialog=function(_4181,xPos,yPos){
if(xPos){
this.addFigure(_4181,xPos,yPos);
}else{
this.addFigure(_4181,200,100);
}
this.dialogs.add(_4181);
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
Workflow.prototype.setToolWindow=function(_418a,x,y){
this.toolPalette=_418a;
if(y){
this.addFigure(_418a,x,y);
}else{
this.addFigure(_418a,20,20);
}
this.dialogs.add(_418a);
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
Workflow.prototype.addFigure=function(_4191,xPos,yPos){
Canvas.prototype.addFigure.call(this,_4191,xPos,yPos,true);
_4191.setWorkflow(this);
var _4194=this;
if(_4191 instanceof CompartmentFigure){
this.compartments.add(_4191);
}
if(_4191 instanceof Line){
this.lines.add(_4191);
}else{
this.figures.add(_4191);
_4191.draggable.addEventListener("dragend",function(_4195){
});
_4191.draggable.addEventListener("dragstart",function(_4196){
var _4197=_4194.getFigure(_4196.target.element.id);
if(_4197==null){
return;
}
if(_4197.isSelectable()==false){
return;
}
_4194.showResizeHandles(_4197);
_4194.setCurrentSelection(_4197);
});
_4191.draggable.addEventListener("drag",function(_4198){
var _4199=_4194.getFigure(_4198.target.element.id);
if(_4199==null){
return;
}
if(_4199.isSelectable()==false){
return;
}
_4194.moveResizeHandles(_4199);
});
}
_4191.paint();
this.setDocumentDirty();
};
Workflow.prototype.removeFigure=function(_419a){
Canvas.prototype.removeFigure.call(this,_419a);
this.figures.remove(_419a);
this.lines.remove(_419a);
this.dialogs.remove(_419a);
_419a.setWorkflow(null);
if(_419a instanceof CompartmentFigure){
this.compartments.remove(_419a);
}
if(_419a instanceof Connection){
_419a.disconnect();
}
if(this.currentSelection==_419a){
this.setCurrentSelection(null);
}
this.setDocumentDirty();
};
Workflow.prototype.moveFront=function(_419b){
this.html.removeChild(_419b.getHTMLElement());
this.html.appendChild(_419b.getHTMLElement());
};
Workflow.prototype.moveBack=function(_419c){
this.html.removeChild(_419c.getHTMLElement());
this.html.insertBefore(_419c.getHTMLElement(),this.html.firstChild);
};
Workflow.prototype.getBestCompartmentFigure=function(x,y,_419f){
var _41a0=null;
for(var i=0;i<this.figures.getSize();i++){
var _41a2=this.figures.get(i);
if((_41a2 instanceof CompartmentFigure)&&_41a2.isOver(x,y)==true&&_41a2!=_419f){
if(_41a0==null){
_41a0=_41a2;
}else{
if(_41a0.getZOrder()<_41a2.getZOrder()){
_41a0=_41a2;
}
}
}
}
return _41a0;
};
Workflow.prototype.getBestFigure=function(x,y,_41a5){
var _41a6=null;
for(var i=0;i<this.figures.getSize();i++){
var _41a8=this.figures.get(i);
if(_41a8.isOver(x,y)==true&&_41a8!=_41a5){
if(_41a6==null){
_41a6=_41a8;
}else{
if(_41a6.getZOrder()<_41a8.getZOrder()){
_41a6=_41a8;
}
}
}
}
return _41a6;
};
Workflow.prototype.getBestLine=function(x,y,_41ab){
var _41ac=null;
for(var i=0;i<this.lines.getSize();i++){
var line=this.lines.get(i);
if(line.containsPoint(x,y)==true&&line!=_41ab){
if(_41ac==null){
_41ac=line;
}else{
if(_41ac.getZOrder()<line.getZOrder()){
_41ac=line;
}
}
}
}
return _41ac;
};
Workflow.prototype.getFigure=function(id){
for(var i=0;i<this.figures.getSize();i++){
var _41b1=this.figures.get(i);
if(_41b1.id==id){
return _41b1;
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
Workflow.prototype.setCurrentSelection=function(_41b4){
if(_41b4==null){
this.hideResizeHandles();
this.hideLineResizeHandles();
}
this.currentSelection=_41b4;
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
port.draggable.targets=this.dropTargets;
this.commonPorts.add(port);
this.dropTargets.add(port.dropable);
};
Workflow.prototype.unregisterPort=function(port){
port.draggable.targets=null;
this.commonPorts.remove(port);
this.dropTargets.remove(port.dropable);
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
Workflow.prototype.showLineResizeHandles=function(_41bd){
var _41be=this.resizeHandleStart.getWidth()/2;
var _41bf=this.resizeHandleStart.getHeight()/2;
var _41c0=_41bd.getStartPoint();
var _41c1=_41bd.getEndPoint();
Canvas.prototype.addFigure.call(this,this.resizeHandleStart,_41c0.x-_41be,_41c0.y-_41be);
Canvas.prototype.addFigure.call(this,this.resizeHandleEnd,_41c1.x-_41be,_41c1.y-_41be);
this.resizeHandleStart.setCanDrag(_41bd.isResizeable());
this.resizeHandleEnd.setCanDrag(_41bd.isResizeable());
if(_41bd.isResizeable()){
this.resizeHandleStart.setBackgroundColor(Workflow.COLOR_GREEN);
this.resizeHandleEnd.setBackgroundColor(Workflow.COLOR_GREEN);
this.resizeHandleStart.draggable.targets=this.dropTargets;
this.resizeHandleEnd.draggable.targets=this.dropTargets;
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
Workflow.prototype.showResizeHandles=function(_41c2){
this.hideLineResizeHandles();
this.hideResizeHandles();
if(this.getEnableSmoothFigureHandling()==true&&this.getCurrentSelection()!=_41c2){
this.resizeHandle1.setAlpha(0.01);
this.resizeHandle2.setAlpha(0.01);
this.resizeHandle3.setAlpha(0.01);
this.resizeHandle4.setAlpha(0.01);
this.resizeHandle5.setAlpha(0.01);
this.resizeHandle6.setAlpha(0.01);
this.resizeHandle7.setAlpha(0.01);
this.resizeHandle8.setAlpha(0.01);
}
var _41c3=this.resizeHandle1.getWidth();
var _41c4=this.resizeHandle1.getHeight();
var _41c5=_41c2.getHeight();
var _41c6=_41c2.getWidth();
var xPos=_41c2.getX();
var yPos=_41c2.getY();
Canvas.prototype.addFigure.call(this,this.resizeHandle1,xPos-_41c3,yPos-_41c4);
Canvas.prototype.addFigure.call(this,this.resizeHandle3,xPos+_41c6,yPos-_41c4);
Canvas.prototype.addFigure.call(this,this.resizeHandle5,xPos+_41c6,yPos+_41c5);
Canvas.prototype.addFigure.call(this,this.resizeHandle7,xPos-_41c3,yPos+_41c5);
this.moveFront(this.resizeHandle1);
this.moveFront(this.resizeHandle3);
this.moveFront(this.resizeHandle5);
this.moveFront(this.resizeHandle7);
this.resizeHandle1.setCanDrag(_41c2.isResizeable());
this.resizeHandle3.setCanDrag(_41c2.isResizeable());
this.resizeHandle5.setCanDrag(_41c2.isResizeable());
this.resizeHandle7.setCanDrag(_41c2.isResizeable());
if(_41c2.isResizeable()){
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
if(_41c2.isStrechable()&&_41c2.isResizeable()){
this.resizeHandle2.setCanDrag(_41c2.isResizeable());
this.resizeHandle4.setCanDrag(_41c2.isResizeable());
this.resizeHandle6.setCanDrag(_41c2.isResizeable());
this.resizeHandle8.setCanDrag(_41c2.isResizeable());
Canvas.prototype.addFigure.call(this,this.resizeHandle2,xPos+(_41c6/2)-this.resizeHandleHalfWidth,yPos-_41c4);
Canvas.prototype.addFigure.call(this,this.resizeHandle4,xPos+_41c6,yPos+(_41c5/2)-(_41c4/2));
Canvas.prototype.addFigure.call(this,this.resizeHandle6,xPos+(_41c6/2)-this.resizeHandleHalfWidth,yPos+_41c5);
Canvas.prototype.addFigure.call(this,this.resizeHandle8,xPos-_41c3,yPos+(_41c5/2)-(_41c4/2));
this.moveFront(this.resizeHandle2);
this.moveFront(this.resizeHandle4);
this.moveFront(this.resizeHandle6);
this.moveFront(this.resizeHandle8);
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
Workflow.prototype.moveResizeHandles=function(_41ca){
var _41cb=this.resizeHandle1.getWidth();
var _41cc=this.resizeHandle1.getHeight();
var _41cd=_41ca.getHeight();
var _41ce=_41ca.getWidth();
var xPos=_41ca.getX();
var yPos=_41ca.getY();
this.resizeHandle1.setPosition(xPos-_41cb,yPos-_41cc);
this.resizeHandle3.setPosition(xPos+_41ce,yPos-_41cc);
this.resizeHandle5.setPosition(xPos+_41ce,yPos+_41cd);
this.resizeHandle7.setPosition(xPos-_41cb,yPos+_41cd);
if(_41ca.isStrechable()){
this.resizeHandle2.setPosition(xPos+(_41ce/2)-this.resizeHandleHalfWidth,yPos-_41cc);
this.resizeHandle4.setPosition(xPos+_41ce,yPos+(_41cd/2)-(_41cc/2));
this.resizeHandle6.setPosition(xPos+(_41ce/2)-this.resizeHandleHalfWidth,yPos+_41cd);
this.resizeHandle8.setPosition(xPos-_41cb,yPos+(_41cd/2)-(_41cc/2));
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
var size=this.getLines().getSize();
for(var i=0;i<size;i++){
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
Workflow.prototype.onKeyDown=function(_41dc,ctrl){
if(_41dc==46&&this.currentSelection!=null&&this.currentSelection.isDeleteable()){
this.commandStack.execute(new CommandDelete(this.currentSelection));
}else{
if(_41dc==90&&ctrl){
this.commandStack.undo();
}else{
if(_41dc==89&&ctrl){
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
Workflow.prototype.snapToHelper=function(_41e0,pos){
if(this.snapToGeometryHelper!=null){
if(_41e0 instanceof ResizeHandle){
var _41e2=_41e0.getSnapToGridAnchor();
pos.x+=_41e2.x;
pos.y+=_41e2.y;
var _41e3=new Point(pos.x,pos.y);
var _41e4=_41e0.getSnapToDirection();
var _41e5=this.snapToGeometryHelper.snapPoint(_41e4,pos,_41e3);
if((_41e4&SnapToHelper.EAST_WEST)&&!(_41e5&SnapToHelper.EAST_WEST)){
this.showSnapToHelperLineVertical(_41e3.x);
}else{
this.hideSnapToHelperLineVertical();
}
if((_41e4&SnapToHelper.NORTH_SOUTH)&&!(_41e5&SnapToHelper.NORTH_SOUTH)){
this.showSnapToHelperLineHorizontal(_41e3.y);
}else{
this.hideSnapToHelperLineHorizontal();
}
_41e3.x-=_41e2.x;
_41e3.y-=_41e2.y;
return _41e3;
}else{
var _41e6=new Dimension(pos.x,pos.y,_41e0.getWidth(),_41e0.getHeight());
var _41e3=new Dimension(pos.x,pos.y,_41e0.getWidth(),_41e0.getHeight());
var _41e4=SnapToHelper.NSEW;
var _41e5=this.snapToGeometryHelper.snapRectangle(_41e6,_41e3);
if((_41e4&SnapToHelper.WEST)&&!(_41e5&SnapToHelper.WEST)){
this.showSnapToHelperLineVertical(_41e3.x);
}else{
if((_41e4&SnapToHelper.EAST)&&!(_41e5&SnapToHelper.EAST)){
this.showSnapToHelperLineVertical(_41e3.getX()+_41e3.getWidth());
}else{
this.hideSnapToHelperLineVertical();
}
}
if((_41e4&SnapToHelper.NORTH)&&!(_41e5&SnapToHelper.NORTH)){
this.showSnapToHelperLineHorizontal(_41e3.y);
}else{
if((_41e4&SnapToHelper.SOUTH)&&!(_41e5&SnapToHelper.SOUTH)){
this.showSnapToHelperLineHorizontal(_41e3.getY()+_41e3.getHeight());
}else{
this.hideSnapToHelperLineHorizontal();
}
}
return _41e3.getTopLeft();
}
}else{
if(this.snapToGridHelper!=null){
var _41e2=_41e0.getSnapToGridAnchor();
pos.x=pos.x+_41e2.x;
pos.y=pos.y+_41e2.y;
var _41e3=new Point(pos.x,pos.y);
this.snapToGridHelper.snapPoint(0,pos,_41e3);
_41e3.x=_41e3.x-_41e2.x;
_41e3.y=_41e3.y-_41e2.y;
return _41e3;
}
}
return pos;
};
Workflow.prototype.showSnapToHelperLineHorizontal=function(_41e7){
if(this.horizontalSnapToHelperLine==null){
this.horizontalSnapToHelperLine=new Line();
this.horizontalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.horizontalSnapToHelperLine);
}
this.horizontalSnapToHelperLine.setStartPoint(0,_41e7);
this.horizontalSnapToHelperLine.setEndPoint(this.getWidth(),_41e7);
};
Workflow.prototype.showSnapToHelperLineVertical=function(_41e8){
if(this.verticalSnapToHelperLine==null){
this.verticalSnapToHelperLine=new Line();
this.verticalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.verticalSnapToHelperLine);
}
this.verticalSnapToHelperLine.setStartPoint(_41e8,0);
this.verticalSnapToHelperLine.setEndPoint(_41e8,this.getHeight());
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
Window.ZOrderIndex=50000;
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
item.appendChild(this.titlebar);
}
return item;
};
Window.prototype.setDocumentDirty=function(_3bb6){
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
Window.prototype.setWorkflow=function(_3bba){
var _3bbb=this.workflow;
Figure.prototype.setWorkflow.call(this,_3bba);
if(_3bbb!=null){
_3bbb.removeSelectionListener(this);
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
Window.prototype.setAlpha=function(_3bbf){
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
Window.prototype.onSelectionChanged=function(_3bc3){
};
Button=function(_40d0,width,_40d2){
this.x=0;
this.y=0;
this.id=this.generateUId();
this.enabled=true;
this.active=false;
this.palette=_40d0;
if(width&&_40d2){
this.setDimension(width,_40d2);
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
Button.prototype.setTooltip=function(_40d7){
this.tooltip=_40d7;
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
var _40df=10;
var _40e0=10;
nbTry=0;
while(nbTry<1000){
var id="";
for(var i=0;i<_40df;i++){
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
ToggleButton=function(_3e96){
Button.call(this,_3e96);
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
ToolGeneric=function(_3a09){
this.x=0;
this.y=0;
this.enabled=true;
this.tooltip=null;
this.palette=_3a09;
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
ToolGeneric.prototype.setTooltip=function(_3a0f){
this.tooltip=_3a0f;
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
Dialog.prototype.setWorkflow=function(_31de){
Window.prototype.setWorkflow.call(this,_31de);
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
PropertyDialog=function(_3f72,_3f73,label){
this.figure=_3f72;
this.propertyName=_3f73;
this.label=label;
Dialog.call(this);
this.setDimension(400,120);
};
PropertyDialog.prototype=new Dialog;
PropertyDialog.prototype.type="PropertyDialog";
PropertyDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _3f76=document.createElement("form");
_3f76.style.position="absolute";
_3f76.style.left="10px";
_3f76.style.top="30px";
_3f76.style.width="375px";
_3f76.style.font="normal 10px verdana";
item.appendChild(_3f76);
this.labelDiv=document.createElement("div");
this.labelDiv.innerHTML=this.label;
this.disableTextSelection(this.labelDiv);
_3f76.appendChild(this.labelDiv);
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
_3f76.appendChild(this.input);
this.input.focus();
return item;
};
PropertyDialog.prototype.onOk=function(){
Dialog.prototype.onOk.call(this);
this.figure.setProperty(this.propertyName,this.input.value);
};
AnnotationDialog=function(_2e7b){
this.figure=_2e7b;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog;
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _2e7d=document.createElement("form");
_2e7d.style.position="absolute";
_2e7d.style.left="10px";
_2e7d.style.top="30px";
_2e7d.style.width="375px";
_2e7d.style.font="normal 10px verdana";
item.appendChild(_2e7d);
this.label=document.createTextNode("Text");
_2e7d.appendChild(this.label);
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
_2e7d.appendChild(this.input);
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
PropertyWindow.prototype.onSelectionChanged=function(_31ec){
Window.prototype.onSelectionChanged.call(this,_31ec);
if(this.currentSelection!=null){
this.currentSelection.detachMoveListener(this);
}
this.currentSelection=_31ec;
if(_31ec!=null&&_31ec!=this){
this.labelType.innerHTML=_31ec.type;
if(_31ec.getX){
this.labelX.innerHTML=_31ec.getX();
this.labelY.innerHTML=_31ec.getY();
this.labelWidth.innerHTML=_31ec.getWidth();
this.labelHeight.innerHTML=_31ec.getHeight();
this.currentSelection=_31ec;
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
PropertyWindow.prototype.onOtherFigureMoved=function(_31ed){
if(_31ed==this.currentSelection){
this.onSelectionChanged(_31ed);
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
LineColorDialog=function(_2e74){
ColorDialog.call(this);
this.figure=_2e74;
var color=_2e74.getColor();
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
};
LineColorDialog.prototype=new ColorDialog;
LineColorDialog.prototype.type="LineColorDialog";
LineColorDialog.prototype.onOk=function(){
var _2e76=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setColor=="function"){
_2e76.getCommandStack().execute(new CommandSetColor(this.figure,this.getSelectedColor()));
if(_2e76.getCurrentSelection()==this.figure){
_2e76.setCurrentSelection(this.figure);
}
}
};
BackgroundColorDialog=function(_40c9){
ColorDialog.call(this);
this.figure=_40c9;
var color=_40c9.getBackgroundColor();
if(color!=null){
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
}
};
BackgroundColorDialog.prototype=new ColorDialog;
BackgroundColorDialog.prototype.type="BackgroundColorDialog";
BackgroundColorDialog.prototype.onOk=function(){
var _40cb=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setBackgroundColor=="function"){
_40cb.getCommandStack().execute(new CommandSetBackgroundColor(this.figure,this.getSelectedColor()));
if(_40cb.getCurrentSelection()==this.figure){
_40cb.setCurrentSelection(this.figure);
}
}
};
AnnotationDialog=function(_2e7b){
this.figure=_2e7b;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog;
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _2e7d=document.createElement("form");
_2e7d.style.position="absolute";
_2e7d.style.left="10px";
_2e7d.style.top="30px";
_2e7d.style.width="375px";
_2e7d.style.font="normal 10px verdana";
item.appendChild(_2e7d);
this.label=document.createTextNode("Text");
_2e7d.appendChild(this.label);
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
_2e7d.appendChild(this.input);
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
this.eventListeners=new ArrayList();
};
CommandStack.PRE_EXECUTE=1;
CommandStack.PRE_REDO=2;
CommandStack.PRE_UNDO=4;
CommandStack.POST_EXECUTE=8;
CommandStack.POST_REDO=16;
CommandStack.POST_UNDO=32;
CommandStack.POST_MASK=CommandStack.POST_EXECUTE|CommandStack.POST_UNDO|CommandStack.POST_REDO;
CommandStack.PRE_MASK=CommandStack.PRE_EXECUTE|CommandStack.PRE_UNDO|CommandStack.PRE_REDO;
CommandStack.prototype.type="CommandStack";
CommandStack.prototype.setUndoLimit=function(count){
this.maxundo=count;
};
CommandStack.prototype.markSaveLocation=function(){
this.undostack=new Array();
this.redostack=new Array();
};
CommandStack.prototype.execute=function(_3f8e){
if(_3f8e.canExecute()==false){
return;
}
this.notifyListeners(_3f8e,CommandStack.PRE_EXECUTE);
this.undostack.push(_3f8e);
_3f8e.execute();
this.redostack=new Array();
if(this.undostack.length>this.maxundo){
this.undostack=this.undostack.slice(this.undostack.length-this.maxundo);
}
this.notifyListeners(_3f8e,CommandStack.POST_EXECUTE);
};
CommandStack.prototype.undo=function(){
var _3f8f=this.undostack.pop();
if(_3f8f){
this.notifyListeners(_3f8f,CommandStack.PRE_UNDO);
this.redostack.push(_3f8f);
_3f8f.undo();
this.notifyListeners(_3f8f,CommandStack.POST_UNDO);
}
};
CommandStack.prototype.redo=function(){
var _3f90=this.redostack.pop();
if(_3f90){
this.notifyListeners(_3f90,CommandStack.PRE_REDO);
this.undostack.push(_3f90);
_3f90.redo();
this.notifyListeners(_3f90,CommandStack.POST_REDO);
}
};
CommandStack.prototype.canRedo=function(){
return this.redostack.length>0;
};
CommandStack.prototype.canUndo=function(){
return this.undostack.length>0;
};
CommandStack.prototype.addCommandStackEventListener=function(_3f91){
this.eventListeners.add(_3f91);
};
CommandStack.prototype.removeCommandStackEventListener=function(_3f92){
this.eventListeners.remove(_3f92);
};
CommandStack.prototype.notifyListeners=function(_3f93,state){
var event=new CommandStackEvent(_3f93,state);
var size=this.eventListeners.getSize();
for(var i=0;i<size;i++){
this.eventListeners.get(i).stackChanged(event);
}
};
CommandStackEvent=function(_3e65,_3e66){
this.command=_3e65;
this.details=_3e66;
};
CommandStackEvent.prototype.type="CommandStackEvent";
CommandStackEvent.prototype.getCommand=function(){
return this.command;
};
CommandStackEvent.prototype.getDetails=function(){
return this.details;
};
CommandStackEvent.prototype.isPostChangeEvent=function(){
return 0!=(this.getDetails()&CommandStack.POST_MASK);
};
CommandStackEvent.prototype.isPreChangeEvent=function(){
return 0!=(this.getDetails()&CommandStack.PRE_MASK);
};
CommandStackEventListener=function(){
};
CommandStackEventListener.prototype.type="CommandStackEventListener";
CommandStackEventListener.prototype.stackChanged=function(event){
};
CommandAdd=function(_40ab,_40ac,x,y,_40af){
Command.call(this,"add figure");
this.parent=_40af;
this.figure=_40ac;
this.x=x;
this.y=y;
this.workflow=_40ab;
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
CommandDelete=function(_3f31){
Command.call(this,"delete figure");
this.parent=_3f31.parent;
this.figure=_3f31;
this.workflow=_3f31.workflow;
this.connections=null;
this.compartmentDeleteCommands=null;
};
CommandDelete.prototype=new Command;
CommandDelete.prototype.type="CommandDelete";
CommandDelete.prototype.execute=function(){
this.redo();
};
CommandDelete.prototype.undo=function(){
if(this.figure instanceof CompartmentFigure){
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
var _3f33=this.compartmentDeleteCommands.get(i);
this.figure.addChild(_3f33.figure);
this.workflow.getCommandStack().undo();
}
}
this.workflow.addFigure(this.figure);
if(this.figure instanceof Connection){
this.figure.reconnect();
}
this.workflow.setCurrentSelection(this.figure);
if(this.parent!=null){
this.parent.addChild(this.figure);
}
for(var i=0;i<this.connections.getSize();++i){
this.workflow.addFigure(this.connections.get(i));
this.connections.get(i).reconnect();
}
};
CommandDelete.prototype.redo=function(){
if(this.figure instanceof CompartmentFigure){
if(this.compartmentDeleteCommands==null){
this.compartmentDeleteCommands=new ArrayList();
var _3f34=this.figure.getChildren().clone();
for(var i=0;i<_3f34.getSize();i++){
var child=_3f34.get(i);
this.figure.removeChild(child);
var _3f37=new CommandDelete(child);
this.compartmentDeleteCommands.add(_3f37);
this.workflow.getCommandStack().execute(_3f37);
}
}else{
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
this.workflow.redo();
}
}
}
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.figure.getPorts&&this.connections==null){
this.connections=new ArrayList();
var ports=this.figure.getPorts();
for(var i=0;i<ports.getSize();i++){
if(ports.get(i).getConnections){
this.connections.addAll(ports.get(i).getConnections());
}
}
}
if(this.connections==null){
this.connections=new ArrayList();
}
if(this.parent!=null){
this.parent.removeChild(this.figure);
}
for(var i=0;i<this.connections.getSize();++i){
this.workflow.removeFigure(this.connections.get(i));
}
};
CommandMove=function(_3f43,x,y){
Command.call(this,"move figure");
this.figure=_3f43;
this.oldX=x;
this.oldY=y;
this.oldCompartment=_3f43.getParent();
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
CommandResize=function(_3ea0,width,_3ea2){
Command.call(this,"resize figure");
this.figure=_3ea0;
this.oldWidth=width;
this.oldHeight=_3ea2;
};
CommandResize.prototype=new Command;
CommandResize.prototype.type="CommandResize";
CommandResize.prototype.setDimension=function(width,_3ea4){
this.newWidth=width;
this.newHeight=_3ea4;
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
CommandSetText=function(_4215,text){
Command.call(this,"set text");
this.figure=_4215;
this.newText=text;
this.oldText=_4215.getText();
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
CommandSetColor=function(_3a5f,color){
Command.call(this,"set color");
this.figure=_3a5f;
this.newColor=color;
this.oldColor=_3a5f.getColor();
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
CommandSetBackgroundColor=function(_2d34,color){
Command.call(this,"set background color");
this.figure=_2d34;
this.newColor=color;
this.oldColor=_2d34.getBackgroundColor();
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
CommandConnect=function(_4085,_4086,_4087){
Command.call(this,"create connection");
this.workflow=_4085;
this.source=_4086;
this.target=_4087;
this.connection=null;
};
CommandConnect.prototype=new Command;
CommandConnect.prototype.type="CommandConnect";
CommandConnect.prototype.setConnection=function(_4088){
this.connection=_4088;
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
CommandReconnect=function(con){
Command.call(this,"reconnect connection");
this.con=con;
this.oldSourcePort=con.getSource();
this.oldTargetPort=con.getTarget();
this.oldRouter=con.getRouter();
};
CommandReconnect.prototype=new Command;
CommandReconnect.prototype.type="CommandReconnect";
CommandReconnect.prototype.canExecute=function(){
return true;
};
CommandReconnect.prototype.setNewPorts=function(_3565,_3566){
this.newSourcePort=_3565;
this.newTargetPort=_3566;
};
CommandReconnect.prototype.execute=function(){
this.redo();
};
CommandReconnect.prototype.undo=function(){
this.con.setSource(this.oldSourcePort);
this.con.setTarget(this.oldTargetPort);
this.con.setRouter(this.oldRouter);
if(this.con.getWorkflow().getCurrentSelection()==this.con){
this.con.getWorkflow().showLineResizeHandles(this.con);
}
};
CommandReconnect.prototype.redo=function(){
this.con.setSource(this.newSourcePort);
this.con.setTarget(this.newTargetPort);
this.con.setRouter(this.oldRouter);
if(this.con.getWorkflow().getCurrentSelection()==this.con){
this.con.getWorkflow().showLineResizeHandles(this.con);
}
};
CommandMoveLine=function(line,_35b9,_35ba,endX,endY){
Command.call(this,"move line");
this.line=line;
this.startX1=_35b9;
this.startY1=_35ba;
this.endX1=endX;
this.endY1=endY;
};
CommandMoveLine.prototype=new Command;
CommandMoveLine.prototype.type="CommandMoveLine";
CommandMoveLine.prototype.canExecute=function(){
return this.startX1!=this.startX2||this.startY1!=this.startY2||this.endX1!=this.endX2||this.endY1!=this.endY2;
};
CommandMoveLine.prototype.setEndPoints=function(_35bd,_35be,endX,endY){
this.startX2=_35bd;
this.startY2=_35be;
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
this.menuItems=new ArrayList();
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
Menu.prototype.setWorkflow=function(_421b){
this.workflow=_421b;
};
Menu.prototype.appendMenuItem=function(item){
this.menuItems.add(item);
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
this.html.appendChild(li);
li.menuItem=item;
if(li.addEventListener){
li.addEventListener("click",function(event){
var _4223=arguments[0]||window.event;
_4223.cancelBubble=true;
_4223.returnValue=false;
var diffX=_4223.clientX;
var diffY=_4223.clientY;
var _4226=document.body.parentNode.scrollLeft;
var _4227=document.body.parentNode.scrollTop;
this.menuItem.execute(diffX+_4226,diffY+_4227);
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
var _422d=arguments[0]||window.event;
_422d.cancelBubble=true;
_422d.returnValue=false;
var diffX=_422d.clientX;
var diffY=_422d.clientY;
var _4230=document.body.parentNode.scrollLeft;
var _4231=document.body.parentNode.scrollTop;
event.srcElement.menuItem.execute(diffX+_4230,diffY+_4231);
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
MenuItem=function(label,_3f7d,_3f7e){
this.label=label;
this.iconUrl=_3f7d;
this.parentMenu=null;
this.action=_3f7e;
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
Locator=function(){
};
Locator.prototype.type="Locator";
Locator.prototype.relocate=function(_3aaa){
};
ConnectionLocator=function(_3f8b){
Locator.call(this);
this.connection=_3f8b;
};
ConnectionLocator.prototype=new Locator;
ConnectionLocator.prototype.type="ConnectionLocator";
ConnectionLocator.prototype.getConnection=function(){
return this.connection;
};
ManhattenMidpointLocator=function(_3bab){
ConnectionLocator.call(this,_3bab);
};
ManhattenMidpointLocator.prototype=new ConnectionLocator;
ManhattenMidpointLocator.prototype.type="ManhattenMidpointLocator";
ManhattenMidpointLocator.prototype.relocate=function(_3bac){
var conn=this.getConnection();
var p=new Point();
var _3baf=conn.getPoints();
var index=Math.floor((_3baf.getSize()-2)/2);
var p1=_3baf.get(index);
var p2=_3baf.get(index+1);
p.x=(p2.x-p1.x)/2+p1.x+5;
p.y=(p2.y-p1.y)/2+p1.y+5;
_3bac.setPosition(p.x,p.y);
};
