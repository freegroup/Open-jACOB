
/**This notice must be untouched at all times.
This is the COMPRESSED version of the Draw2D Library
WebSite: http://www.draw2d.org
Copyright: 2006 Andreas Herz. All rights reserved.
Created: 5.11.2006 by Andreas Herz (Web: http://www.freegroup.de )
LICENSE: LGPL
**/
var _errorStack_=[];
function pushErrorStack(e,_439a){
_errorStack_.push(_439a+"\n");
throw e;
}
AbstractEvent=function(){
this.type=null;
this.target=null;
this.relatedTarget=null;
this.cancelable=false;
this.timeStamp=null;
this.returnValue=true;
};
AbstractEvent.prototype.initEvent=function(sType,_439c){
this.type=sType;
this.cancelable=_439c;
this.timeStamp=(new Date()).getTime();
};
AbstractEvent.prototype.preventDefault=function(){
if(this.cancelable){
this.returnValue=false;
}
};
AbstractEvent.fireDOMEvent=function(_439d,_439e){
if(document.createEvent){
var evt=document.createEvent("Events");
evt.initEvent(_439d,true,true);
_439e.dispatchEvent(evt);
}else{
if(document.createEventObject){
var evt=document.createEventObject();
_439e.fireEvent("on"+_439d,evt);
}
}
};
EventTarget=function(){
this.eventhandlers={};
};
EventTarget.prototype.addEventListener=function(sType,_43a1){
if(typeof this.eventhandlers[sType]=="undefined"){
this.eventhandlers[sType]=[];
}
this.eventhandlers[sType][this.eventhandlers[sType].length]=_43a1;
};
EventTarget.prototype.dispatchEvent=function(_43a2){
_43a2.target=this;
if(typeof this.eventhandlers[_43a2.type]!="undefined"){
for(var i=0;i<this.eventhandlers[_43a2.type].length;i++){
this.eventhandlers[_43a2.type][i](_43a2);
}
}
return _43a2.returnValue;
};
EventTarget.prototype.removeEventListener=function(sType,_43a5){
if(typeof this.eventhandlers[sType]!="undefined"){
var _43a6=[];
for(var i=0;i<this.eventhandlers[sType].length;i++){
if(this.eventhandlers[sType][i]!=_43a5){
_43a6[_43a6.length]=this.eventhandlers[sType][i];
}
}
this.eventhandlers[sType]=_43a6;
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
String.prototype.between=function(left,right,_43e4){
if(!_43e4){
_43e4=0;
}
var li=this.indexOf(left,_43e4);
if(li==-1){
return null;
}
var ri=this.indexOf(right,li);
if(ri==-1){
return null;
}
return this.substring(li+left.length,ri);
};
UUID=function(){
};
UUID.prototype.type="UUID";
UUID.create=function(){
var _42ce=function(){
return (((1+Math.random())*65536)|0).toString(16).substring(1);
};
return (_42ce()+_42ce()+"-"+_42ce()+"-"+_42ce()+"-"+_42ce()+"-"+_42ce()+_42ce()+_42ce());
};
ArrayList=function(){
this.increment=10;
this.size=0;
this.data=new Array(this.increment);
};
ArrayList.EMPTY_LIST=new ArrayList();
ArrayList.prototype.type="ArrayList";
ArrayList.prototype.reverse=function(){
var _49d4=new Array(this.size);
for(var i=0;i<this.size;i++){
_49d4[i]=this.data[this.size-i-1];
}
this.data=_49d4;
};
ArrayList.prototype.getCapacity=function(){
return this.data.length;
};
ArrayList.prototype.getSize=function(){
return this.size;
};
ArrayList.prototype.isEmpty=function(){
return this.getSize()===0;
};
ArrayList.prototype.getLastElement=function(){
if(this.data[this.getSize()-1]!==null){
return this.data[this.getSize()-1];
}
};
ArrayList.prototype.getFirstElement=function(){
if(this.data[0]!==null&&this.data[0]!==undefined){
return this.data[0];
}
return null;
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
var _49e0=this.data[index];
for(var i=index;i<(this.getSize()-1);i++){
this.data[i]=this.data[i+1];
}
this.data[this.getSize()-1]=null;
this.size--;
return _49e0;
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
ArrayList.prototype.sort=function(f){
var i,j;
var _49ec;
var _49ed;
var _49ee;
var _49ef;
for(i=1;i<this.getSize();i++){
_49ed=this.data[i];
_49ec=_49ed[f];
j=i-1;
_49ee=this.data[j];
_49ef=_49ee[f];
while(j>=0&&_49ef>_49ec){
this.data[j+1]=this.data[j];
j--;
if(j>=0){
_49ee=this.data[j];
_49ef=_49ee[f];
}
}
this.data[j+1]=_49ed;
}
};
ArrayList.prototype.clone=function(){
var _49f0=new ArrayList(this.size);
for(var i=0;i<this.size;i++){
_49f0.add(this.data[i]);
}
return _49f0;
};
ArrayList.prototype.overwriteElementAt=function(obj,index){
this.data[index]=obj;
};
ArrayList.prototype.getPersistentAttributes=function(){
return {data:this.data,increment:this.increment,size:this.getSize()};
};
function trace(_3854){
var _3855=openwindow("about:blank",700,400);
_3855.document.writeln("<pre>"+_3854+"</pre>");
}
function openwindow(url,width,_3858){
var left=(screen.width-width)/2;
var top=(screen.height-_3858)/2;
property="left="+left+", top="+top+", toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width="+width+",height="+_3858;
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
Drag.currentHover=null;
Drag.currentCompartment=null;
Drag.dragging=false;
Drag.isDragging=function(){
return this.dragging;
};
Drag.setCurrent=function(_3d98){
this.current=_3d98;
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
Draggable=function(_3d99,_3d9a){
this.id=UUID.create();
this.node=null;
EventTarget.call(this);
this.construct(_3d99,_3d9a);
this.diffX=0;
this.diffY=0;
this.targets=new ArrayList();
};
Draggable.prototype=new EventTarget();
Draggable.prototype.construct=function(_3d9b){
if(_3d9b===null||_3d9b===undefined){
return;
}
this.element=_3d9b;
var oThis=this;
var _3d9d=function(){
var _3d9e=new DragDropEvent();
_3d9e.initDragDropEvent("dblclick",true);
oThis.dispatchEvent(_3d9e);
var _3d9f=arguments[0]||window.event;
_3d9f.cancelBubble=true;
_3d9f.returnValue=false;
};
var _3da0=function(){
var _3da1=arguments[0]||window.event;
var _3da2=new DragDropEvent();
if(oThis.node!==null){
var _3da3=oThis.node.getWorkflow().getAbsoluteX();
var _3da4=oThis.node.getWorkflow().getAbsoluteY();
var _3da5=oThis.node.getWorkflow().getScrollLeft();
var _3da6=oThis.node.getWorkflow().getScrollTop();
_3da2.x=_3da1.clientX-oThis.element.offsetLeft+_3da5-_3da3;
_3da2.y=_3da1.clientY-oThis.element.offsetTop+_3da6-_3da4;
}
if(_3da1.button===2){
_3da2.initDragDropEvent("contextmenu",true);
oThis.dispatchEvent(_3da2);
}else{
_3da2.initDragDropEvent("dragstart",true);
if(oThis.dispatchEvent(_3da2)){
oThis.diffX=_3da1.clientX-oThis.element.offsetLeft;
oThis.diffY=_3da1.clientY-oThis.element.offsetTop;
Drag.setCurrent(oThis);
if(oThis.isAttached==true){
oThis.detachEventHandlers();
}
oThis.attachEventHandlers();
}
}
_3da1.cancelBubble=true;
_3da1.returnValue=false;
};
var _3da7=function(){
if(Drag.getCurrent()===null){
var _3da8=arguments[0]||window.event;
if(Drag.currentHover!==null&&oThis!==Drag.currentHover){
var _3da9=new DragDropEvent();
_3da9.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_3da9);
}
if(oThis!==null&&oThis!==Drag.currentHover){
var _3da9=new DragDropEvent();
_3da9.initDragDropEvent("mouseenter",false,oThis);
oThis.dispatchEvent(_3da9);
}
Drag.currentHover=oThis;
}else{
}
};
if(this.element.addEventListener){
this.element.addEventListener("mousemove",_3da7,false);
this.element.addEventListener("mousedown",_3da0,false);
this.element.addEventListener("dblclick",_3d9d,false);
}else{
if(this.element.attachEvent){
this.element.attachEvent("onmousemove",_3da7);
this.element.attachEvent("onmousedown",_3da0);
this.element.attachEvent("ondblclick",_3d9d);
}else{
throw "Drag not supported in this browser.";
}
}
};
Draggable.prototype.onDrop=function(_3daa,_3dab){
};
Draggable.prototype.attachEventHandlers=function(){
var oThis=this;
oThis.isAttached=true;
this.tempMouseMove=function(){
var _3dad=arguments[0]||window.event;
var _3dae=new Point(_3dad.clientX-oThis.diffX,_3dad.clientY-oThis.diffY);
if(oThis.node!==null&&oThis.node.getCanSnapToHelper()){
_3dae=oThis.node.getWorkflow().snapToHelper(oThis.node,_3dae);
}
oThis.element.style.left=_3dae.x+"px";
oThis.element.style.top=_3dae.y+"px";
if(oThis.node!==null){
var _3daf=oThis.node.getWorkflow().getScrollLeft();
var _3db0=oThis.node.getWorkflow().getScrollTop();
var _3db1=oThis.node.getWorkflow().getAbsoluteX();
var _3db2=oThis.node.getWorkflow().getAbsoluteY();
var _3db3=oThis.getDropTarget(_3dad.clientX+_3daf-_3db1,_3dad.clientY+_3db0-_3db2);
var _3db4=oThis.getCompartment(_3dad.clientX+_3daf-_3db1,_3dad.clientY+_3db0-_3db2);
if(Drag.currentTarget!==null&&_3db3!=Drag.currentTarget){
var _3db5=new DragDropEvent();
_3db5.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_3db5);
}
if(_3db3!==null&&_3db3!==Drag.currentTarget){
var _3db5=new DragDropEvent();
_3db5.initDragDropEvent("dragenter",false,oThis);
_3db3.dispatchEvent(_3db5);
}
Drag.currentTarget=_3db3;
if(Drag.currentCompartment!==null&&_3db4!==Drag.currentCompartment){
var _3db5=new DragDropEvent();
_3db5.initDragDropEvent("figureleave",false,oThis);
Drag.currentCompartment.dispatchEvent(_3db5);
}
if(_3db4!==null&&_3db4.node!=oThis.node&&_3db4!==Drag.currentCompartment){
var _3db5=new DragDropEvent();
_3db5.initDragDropEvent("figureenter",false,oThis);
_3db4.dispatchEvent(_3db5);
}
Drag.currentCompartment=_3db4;
}
var _3db6=new DragDropEvent();
_3db6.initDragDropEvent("drag",false);
oThis.dispatchEvent(_3db6);
};
oThis.tempMouseUp=function(){
oThis.detachEventHandlers();
var _3db7=arguments[0]||window.event;
if(oThis.node!==null){
var _3db8=oThis.node.getWorkflow().getScrollLeft();
var _3db9=oThis.node.getWorkflow().getScrollTop();
var _3dba=oThis.node.getWorkflow().getAbsoluteX();
var _3dbb=oThis.node.getWorkflow().getAbsoluteY();
var _3dbc=oThis.getDropTarget(_3db7.clientX+_3db8-_3dba,_3db7.clientY+_3db9-_3dbb);
var _3dbd=oThis.getCompartment(_3db7.clientX+_3db8-_3dba,_3db7.clientY+_3db9-_3dbb);
if(_3dbc!==null){
var _3dbe=new DragDropEvent();
_3dbe.initDragDropEvent("drop",false,oThis);
_3dbc.dispatchEvent(_3dbe);
}
if(_3dbd!==null&&_3dbd.node!==oThis.node){
var _3dbe=new DragDropEvent();
_3dbe.initDragDropEvent("figuredrop",false,oThis);
_3dbd.dispatchEvent(_3dbe);
}
if(Drag.currentTarget!==null){
var _3dbe=new DragDropEvent();
_3dbe.initDragDropEvent("dragleave",false,oThis);
Drag.currentTarget.dispatchEvent(_3dbe);
Drag.currentTarget=null;
}
}
var _3dbf=new DragDropEvent();
_3dbf.initDragDropEvent("dragend",false);
oThis.dispatchEvent(_3dbf);
oThis.onDrop(_3db7.clientX,_3db7.clientY);
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
var _3dc3=this.targets.get(i);
if(_3dc3.node.isOver(x,y)&&_3dc3.node!==this.node){
return _3dc3;
}
}
return null;
};
Draggable.prototype.getCompartment=function(x,y){
var _3dc6=null;
for(var i=0;i<this.node.getWorkflow().compartments.getSize();i++){
var _3dc8=this.node.getWorkflow().compartments.get(i);
if(_3dc8.isOver(x,y)&&_3dc8!==this.node){
if(_3dc6===null){
_3dc6=_3dc8;
}else{
if(_3dc6.getZOrder()<_3dc8.getZOrder()){
_3dc6=_3dc8;
}
}
}
}
return _3dc6===null?null:_3dc6.dropable;
};
Draggable.prototype.getLeft=function(){
return this.element.offsetLeft;
};
Draggable.prototype.getTop=function(){
return this.element.offsetTop;
};
DragDropEvent=function(){
AbstractEvent.call(this);
};
DragDropEvent.prototype=new AbstractEvent();
DragDropEvent.prototype.initDragDropEvent=function(sType,_3dca,_3dcb){
this.initEvent(sType,_3dca);
this.relatedTarget=_3dcb;
};
DropTarget=function(_3dcc){
EventTarget.call(this);
this.construct(_3dcc);
};
DropTarget.prototype=new EventTarget();
DropTarget.prototype.construct=function(_3dcd){
this.element=_3dcd;
};
DropTarget.prototype.getLeft=function(){
var el=this.element;
var ol=el.offsetLeft;
while((el=el.offsetParent)!==null){
ol+=el.offsetLeft;
}
return ol;
};
DropTarget.prototype.getTop=function(){
var el=this.element;
var ot=el.offsetTop;
while((el=el.offsetParent)!==null){
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
var _39c1=105;
var _39c2=(this.red*0.299)+(this.green*0.587)+(this.blue*0.114);
return (255-_39c2<_39c1)?new Color(0,0,0):new Color(255,255,255);
};
Color.prototype.hex2rgb=function(_39c3){
_39c3=_39c3.replace("#","");
return ({0:parseInt(_39c3.substr(0,2),16),1:parseInt(_39c3.substr(2,2),16),2:parseInt(_39c3.substr(4,2),16)});
};
Color.prototype.hex=function(){
return (this.int2hex(this.red)+this.int2hex(this.green)+this.int2hex(this.blue));
};
Color.prototype.int2hex=function(v){
v=Math.round(Math.min(Math.max(0,v),255));
return ("0123456789ABCDEF".charAt((v-v%16)/16)+"0123456789ABCDEF".charAt(v%16));
};
Color.prototype.darker=function(_39c5){
var red=parseInt(Math.round(this.getRed()*(1-_39c5)));
var green=parseInt(Math.round(this.getGreen()*(1-_39c5)));
var blue=parseInt(Math.round(this.getBlue()*(1-_39c5)));
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
Color.prototype.lighter=function(_39c9){
var red=parseInt(Math.round(this.getRed()*(1+_39c9)));
var green=parseInt(Math.round(this.getGreen()*(1+_39c9)));
var blue=parseInt(Math.round(this.getBlue()*(1+_39c9)));
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
Point.prototype.getPersistentAttributes=function(){
return {x:this.x,y:this.y};
};
Dimension=function(x,y,w,h){
Point.call(this,x,y);
this.w=w;
this.h=h;
};
Dimension.prototype=new Point();
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
SnapToHelper=function(_45fa){
this.workflow=_45fa;
};
SnapToHelper.NORTH=1;
SnapToHelper.SOUTH=4;
SnapToHelper.WEST=8;
SnapToHelper.EAST=16;
SnapToHelper.CENTER=32;
SnapToHelper.NORTH_EAST=SnapToHelper.NORTH|SnapToHelper.EAST;
SnapToHelper.NORTH_WEST=SnapToHelper.NORTH|SnapToHelper.WEST;
SnapToHelper.SOUTH_EAST=SnapToHelper.SOUTH|SnapToHelper.EAST;
SnapToHelper.SOUTH_WEST=SnapToHelper.SOUTH|SnapToHelper.WEST;
SnapToHelper.NORTH_SOUTH=SnapToHelper.NORTH|SnapToHelper.SOUTH;
SnapToHelper.EAST_WEST=SnapToHelper.EAST|SnapToHelper.WEST;
SnapToHelper.NSEW=SnapToHelper.NORTH_SOUTH|SnapToHelper.EAST_WEST;
SnapToHelper.prototype.snapPoint=function(_45fb,_45fc,_45fd){
return _45fc;
};
SnapToHelper.prototype.snapRectangle=function(_45fe,_45ff){
return _45fe;
};
SnapToHelper.prototype.onSetDocumentDirty=function(){
};
SnapToGrid=function(_3e33){
SnapToHelper.call(this,_3e33);
};
SnapToGrid.prototype=new SnapToHelper();
SnapToGrid.prototype.type="SnapToGrid";
SnapToGrid.prototype.snapPoint=function(_3e34,_3e35,_3e36){
_3e36.x=this.workflow.gridWidthX*Math.floor(((_3e35.x+this.workflow.gridWidthX/2)/this.workflow.gridWidthX));
_3e36.y=this.workflow.gridWidthY*Math.floor(((_3e35.y+this.workflow.gridWidthY/2)/this.workflow.gridWidthY));
return 0;
};
SnapToGrid.prototype.snapRectangle=function(_3e37,_3e38){
_3e38.x=_3e37.x;
_3e38.y=_3e37.y;
_3e38.w=_3e37.w;
_3e38.h=_3e37.h;
return 0;
};
SnapToGeometryEntry=function(type,_3c02){
this.type=type;
this.location=_3c02;
};
SnapToGeometryEntry.prototype.getLocation=function(){
return this.location;
};
SnapToGeometryEntry.prototype.getType=function(){
return this.type;
};
SnapToGeometry=function(_3638){
SnapToHelper.call(this,_3638);
};
SnapToGeometry.prototype=new SnapToHelper();
SnapToGeometry.THRESHOLD=5;
SnapToGeometry.prototype.snapPoint=function(_3639,_363a,_363b){
if(this.rows===null||this.cols===null){
this.populateRowsAndCols();
}
if((_3639&SnapToHelper.EAST)!==0){
var _363c=this.getCorrectionFor(this.cols,_363a.getX()-1,1);
if(_363c!==SnapToGeometry.THRESHOLD){
_3639&=~SnapToHelper.EAST;
_363b.x+=_363c;
}
}
if((_3639&SnapToHelper.WEST)!==0){
var _363d=this.getCorrectionFor(this.cols,_363a.getX(),-1);
if(_363d!==SnapToGeometry.THRESHOLD){
_3639&=~SnapToHelper.WEST;
_363b.x+=_363d;
}
}
if((_3639&SnapToHelper.SOUTH)!==0){
var _363e=this.getCorrectionFor(this.rows,_363a.getY()-1,1);
if(_363e!==SnapToGeometry.THRESHOLD){
_3639&=~SnapToHelper.SOUTH;
_363b.y+=_363e;
}
}
if((_3639&SnapToHelper.NORTH)!==0){
var _363f=this.getCorrectionFor(this.rows,_363a.getY(),-1);
if(_363f!==SnapToGeometry.THRESHOLD){
_3639&=~SnapToHelper.NORTH;
_363b.y+=_363f;
}
}
return _3639;
};
SnapToGeometry.prototype.snapRectangle=function(_3640,_3641){
var _3642=_3640.getTopLeft();
var _3643=_3640.getBottomRight();
var _3644=this.snapPoint(SnapToHelper.NORTH_WEST,_3640.getTopLeft(),_3642);
_3641.x=_3642.x;
_3641.y=_3642.y;
var _3645=this.snapPoint(SnapToHelper.SOUTH_EAST,_3640.getBottomRight(),_3643);
if(_3644&SnapToHelper.WEST){
_3641.x=_3643.x-_3640.getWidth();
}
if(_3644&SnapToHelper.NORTH){
_3641.y=_3643.y-_3640.getHeight();
}
return _3644|_3645;
};
SnapToGeometry.prototype.populateRowsAndCols=function(){
this.rows=[];
this.cols=[];
var _3646=this.workflow.getDocument().getFigures();
var index=0;
for(var i=0;i<_3646.getSize();i++){
var _3649=_3646.get(i);
if(_3649!=this.workflow.getCurrentSelection()){
var _364a=_3649.getBounds();
this.cols[index*3]=new SnapToGeometryEntry(-1,_364a.getX());
this.rows[index*3]=new SnapToGeometryEntry(-1,_364a.getY());
this.cols[index*3+1]=new SnapToGeometryEntry(0,_364a.x+(_364a.getWidth()-1)/2);
this.rows[index*3+1]=new SnapToGeometryEntry(0,_364a.y+(_364a.getHeight()-1)/2);
this.cols[index*3+2]=new SnapToGeometryEntry(1,_364a.getRight()-1);
this.rows[index*3+2]=new SnapToGeometryEntry(1,_364a.getBottom()-1);
index++;
}
}
};
SnapToGeometry.prototype.getCorrectionFor=function(_364b,value,side){
var _364e=SnapToGeometry.THRESHOLD;
var _364f=SnapToGeometry.THRESHOLD;
for(var i=0;i<_364b.length;i++){
var entry=_364b[i];
var _3652;
if(entry.type===-1&&side!==0){
_3652=Math.abs(value-entry.location);
if(_3652<_364e){
_364e=_3652;
_364f=entry.location-value;
}
}else{
if(entry.type===0&&side===0){
_3652=Math.abs(value-entry.location);
if(_3652<_364e){
_364e=_3652;
_364f=entry.location-value;
}
}else{
if(entry.type===1&&side!==0){
_3652=Math.abs(value-entry.location);
if(_3652<_364e){
_364e=_3652;
_364f=entry.location-value;
}
}
}
}
}
return _364f;
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
LineBorder.prototype=new Border();
LineBorder.prototype.type="LineBorder";
LineBorder.prototype.dispose=function(){
Border.prototype.dispose.call(this);
this.figure=null;
};
LineBorder.prototype.setLineWidth=function(w){
this.width=w;
if(this.figure!==null){
this.figure.html.style.border=this.getHTMLStyle();
}
};
LineBorder.prototype.getHTMLStyle=function(){
if(this.getColor()!==null){
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
this.width=10;
this.height=10;
this.border=null;
this.id=UUID.create();
this.html=this.createHTMLElement();
this.canvas=null;
this.workflow=null;
this.draggable=null;
this.parent=null;
this.isMoving=false;
this.canSnapToHelper=true;
this.snapToGridAnchor=new Point(0,0);
this.timer=-1;
this.model=null;
this.properties={};
this.moveListener=new ArrayList();
this.setDimension(this.width,this.height);
this.setDeleteable(true);
this.setCanDrag(true);
this.setResizeable(true);
this.setSelectable(true);
};
Figure.prototype.dispose=function(){
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
Figure.prototype.setId=function(id){
this.id=id;
if(this.html!==null){
this.html.id=id;
}
};
Figure.prototype.setCanvas=function(_3c36){
this.canvas=_3c36;
};
Figure.prototype.getWorkflow=function(){
return this.workflow;
};
Figure.prototype.setWorkflow=function(_3c37){
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
this.draggable=new Draggable(this.html,Draggable.DRAG_X|Draggable.DRAG_Y);
this.draggable.node=this;
this.tmpContextMenu=function(_3c3a){
oThis.onContextMenu(oThis.x+_3c3a.x,_3c3a.y+oThis.y);
};
this.tmpMouseEnter=function(_3c3b){
oThis.onMouseEnter();
};
this.tmpMouseLeave=function(_3c3c){
oThis.onMouseLeave();
};
this.tmpDragend=function(_3c3d){
oThis.onDragend();
};
this.tmpDragstart=function(_3c3e){
var w=oThis.workflow;
w.showMenu(null);
if(w.toolPalette&&w.toolPalette.activeTool){
_3c3e.returnValue=false;
w.onMouseDown(oThis.x+_3c3e.x,_3c3e.y+oThis.y);
w.onMouseUp(oThis.x+_3c3e.x,_3c3e.y+oThis.y);
return;
}
if(!(oThis instanceof ResizeHandle)&&!(oThis instanceof Port)){
var line=w.getBestLine(oThis.x+_3c3e.x,_3c3e.y+oThis.y);
if(line!==null){
_3c3e.returnValue=false;
w.setCurrentSelection(line);
w.showLineResizeHandles(line);
w.onMouseDown(oThis.x+_3c3e.x,_3c3e.y+oThis.y);
return;
}else{
if(oThis.isSelectable()){
w.showResizeHandles(oThis);
w.setCurrentSelection(oThis);
}
}
}
_3c3e.returnValue=oThis.onDragstart(_3c3e.x,_3c3e.y);
};
this.tmpDrag=function(_3c41){
oThis.onDrag();
};
this.tmpDoubleClick=function(_3c42){
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
this.workflow=_3c37;
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
Figure.prototype.setParent=function(_3c44){
this.parent=_3c44;
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
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
Figure.prototype.paint=function(){
};
Figure.prototype.setBorder=function(_3c46){
if(this.border!==null){
this.border.figure=null;
}
this.border=_3c46;
this.border.figure=this;
this.border.refresh();
this.setDocumentDirty();
};
Figure.prototype.onRemove=function(_3c47){
};
Figure.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
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
var _3c4b=this;
var _3c4c=function(){
if(_3c4b.alpha<1){
_3c4b.setAlpha(Math.min(1,_3c4b.alpha+0.05));
}else{
window.clearInterval(_3c4b.timer);
_3c4b.timer=-1;
}
};
if(_3c4b.timer>0){
window.clearInterval(_3c4b.timer);
}
_3c4b.timer=window.setInterval(_3c4c,20);
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
this.command=this.createCommand(new EditPolicy(EditPolicy.MOVE));
return this.command!==null;
};
Figure.prototype.setCanDrag=function(flag){
this.canDrag=flag;
if(flag){
this.html.style.cursor="move";
}else{
this.html.style.cursor="";
}
};
Figure.prototype.getCanDrag=function(){
return this.canDrag;
};
Figure.prototype.setAlpha=function(_3c50){
if(this.alpha==_3c50){
return;
}
try{
this.html.style.MozOpacity=_3c50;
}
catch(exc){
}
try{
this.html.style.opacity=_3c50;
}
catch(exc){
}
try{
var _3c51=Math.round(_3c50*100);
if(_3c51>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_3c51+")";
}
}
catch(exc){
}
this.alpha=_3c50;
};
Figure.prototype.setDimension=function(w,h){
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
Figure.prototype.setPosition=function(xPos,yPos){
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
Figure.prototype.onKeyDown=function(_3c5b,ctrl){
if(_3c5b==46){
this.workflow.getCommandStack().execute(this.createCommand(new EditPolicy(EditPolicy.DELETE)));
}
if(ctrl){
this.workflow.onKeyDown(_3c5b,ctrl);
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
Figure.prototype.attachMoveListener=function(_3c63){
if(_3c63===null||this.moveListener===null){
return;
}
this.moveListener.add(_3c63);
};
Figure.prototype.detachMoveListener=function(_3c64){
if(_3c64===null||this.moveListener===null){
return;
}
this.moveListener.remove(_3c64);
};
Figure.prototype.fireMoveEvent=function(){
this.setDocumentDirty();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
Figure.prototype.setModel=function(model){
if(this.model!==null){
this.model.removePropertyChangeListener(this);
}
this.model=model;
if(this.model!==null){
this.model.addPropertyChangeListener(this);
}
};
Figure.prototype.getModel=function(){
return this.model;
};
Figure.prototype.onOtherFigureMoved=function(_3c68){
};
Figure.prototype.setDocumentDirty=function(){
if(this.workflow!==null){
this.workflow.setDocumentDirty();
}
};
Figure.prototype.disableTextSelection=function(_3c69){
_3c69.onselectstart=function(){
return false;
};
_3c69.unselectable="on";
_3c69.style.MozUserSelect="none";
_3c69.onmousedown=function(){
return false;
};
};
Figure.prototype.createCommand=function(_3c6a){
if(_3c6a.getPolicy()==EditPolicy.MOVE){
if(!this.canDrag){
return null;
}
return new CommandMove(this);
}
if(_3c6a.getPolicy()==EditPolicy.DELETE){
if(!this.isDeleteable()){
return null;
}
return new CommandDelete(this);
}
if(_3c6a.getPolicy()==EditPolicy.RESIZE){
if(!this.isResizeable()){
return null;
}
return new CommandResize(this);
}
return null;
};
Node=function(){
this.bgColor=null;
this.lineColor=new Color(128,128,255);
this.lineStroke=1;
this.ports=new ArrayList();
Figure.call(this);
};
Node.prototype=new Figure();
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
if(this.lineColor!==null){
item.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}
item.style.fontSize="1px";
if(this.bgColor!==null){
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
Node.prototype.getInputPorts=function(){
var _47b7=new ArrayList();
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port instanceof InputPort){
_47b7.add(port);
}
}
return _47b7;
};
Node.prototype.getOutputPorts=function(){
var _47ba=new ArrayList();
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port instanceof OutputPort){
_47ba.add(port);
}
}
return _47ba;
};
Node.prototype.getPort=function(_47bd){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_47bd){
return port;
}
}
};
Node.prototype.getInputPort=function(_47c0){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_47c0&&port instanceof InputPort){
return port;
}
}
};
Node.prototype.getOutputPort=function(_47c3){
if(this.ports===null){
return null;
}
for(var i=0;i<this.ports.getSize();i++){
var port=this.ports.get(i);
if(port.getName()==_47c3&&port instanceof OutputPort){
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
if(this.workflow!==null){
this.workflow.registerPort(port);
}
};
Node.prototype.removePort=function(port){
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
var _47ca=port.getConnections();
for(var i=0;i<_47ca.getSize();++i){
this.workflow.removeFigure(_47ca.get(i));
}
};
Node.prototype.setWorkflow=function(_47cc){
var _47cd=this.workflow;
Figure.prototype.setWorkflow.call(this,_47cc);
if(_47cd!==null){
for(var i=0;i<this.ports.getSize();i++){
_47cd.unregisterPort(this.ports.get(i));
}
}
if(this.workflow!==null){
for(var i=0;i<this.ports.getSize();i++){
this.workflow.registerPort(this.ports.get(i));
}
}
};
Node.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
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
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
Node.prototype.setLineWidth=function(w){
this.lineStroke=w;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
Node.prototype.getModelSourceConnections=function(){
throw "You must override the method [Node.prototype.getModelSourceConnections]";
};
Node.prototype.refreshConnections=function(){
if(this.workflow!==null){
this.workflow.refreshConnections(this);
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
if(this.graphics!==null){
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
VectorFigure.prototype.setWorkflow=function(_4668){
Node.prototype.setWorkflow.call(this,_4668);
if(this.workflow===null){
this.graphics.clear();
this.graphics=null;
}
};
VectorFigure.prototype.paint=function(){
if(this.html===null){
return;
}
try{
if(this.graphics===null){
this.graphics=new jsGraphics(this.html);
}else{
this.graphics.clear();
}
Node.prototype.paint.call(this);
for(var i=0;i<this.ports.getSize();i++){
this.getHTMLElement().appendChild(this.ports.get(i).getHTMLElement());
}
}
catch(e){
pushErrorStack(e,"VectorFigure.prototype.paint=function()["+area+"]");
}
};
VectorFigure.prototype.setDimension=function(w,h){
Node.prototype.setDimension.call(this,w,h);
if(this.graphics!==null){
this.paint();
}
};
VectorFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!==null){
this.paint();
}
};
VectorFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
VectorFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!==null){
this.paint();
}
};
VectorFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!==null){
this.paint();
}
};
VectorFigure.prototype.getColor=function(){
return this.lineColor;
};
SVGFigure=function(width,_49fe){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.stroke=1;
this.context=null;
Node.call(this);
if(width&&_49fe){
this.setDimension(width,_49fe);
}
};
SVGFigure.prototype=new Node();
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
if(this.context!==null){
if(this.context.element){
this.context.element.style.width=w+"px";
this.context.element.style.height=h+"px";
}
this.paint();
}
};
SVGFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.graphics!==null){
this.paint();
}
};
SVGFigure.prototype.getBackgroundColor=function(){
return this.bgColor;
};
SVGFigure.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.context!==null){
this.paint();
}
};
SVGFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.context!==null){
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
Label.prototype=new Figure();
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
item.style.MozUserSelect="none";
this.disableTextSelection(item);
if(this.bgColor!==null){
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
if(this.bgColor!==null){
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
Label.prototype.setDimension=function(w,h){
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
return this.msg;
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
Oval.prototype=new VectorFigure();
Oval.prototype.type="Oval";
Oval.prototype.paint=function(){
if(this.html===null){
return;
}
try{
VectorFigure.prototype.paint.call(this);
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
pushErrorStack(e,"Oval.prototype.paint=function()");
}
};
Circle=function(_3e39){
Oval.call(this);
if(_3e39){
this.setDimension(_3e39,_3e39);
}
};
Circle.prototype=new Oval();
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
Rectangle=function(width,_42af){
this.bgColor=null;
this.lineColor=new Color(0,0,0);
this.lineStroke=1;
Figure.call(this);
if(width&&_42af){
this.setDimension(width,_42af);
}
};
Rectangle.prototype=new Figure();
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
if(this.bgColor!==null){
item.style.backgroundColor=this.bgColor.getHTMLStyle();
}
return item;
};
Rectangle.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
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
if(this.lineColor!==null){
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
if(this.lineColor!==null){
c=this.lineColor.getHTMLStyle();
}
this.html.style.border=this.lineStroke+"px solid "+c;
};
Rectangle.prototype.getLineWidth=function(){
return this.lineStroke;
};
ImageFigure=function(url){
if(url===undefined){
url=null;
}
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
if(this.url!==null){
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
Port=function(_3e66,_3e67){
Corona=function(){
};
Corona.prototype=new Circle();
Corona.prototype.setAlpha=function(_3e68){
Circle.prototype.setAlpha.call(this,Math.min(0.3,_3e68));
this.setDeleteable(false);
this.setCanDrag(false);
this.setResizeable(false);
this.setSelectable(false);
};
if(_3e66===null||_3e66===undefined){
this.currentUIRepresentation=new Circle();
}else{
this.currentUIRepresentation=_3e66;
}
if(_3e67===null||_3e67===undefined){
this.connectedUIRepresentation=new Circle();
this.connectedUIRepresentation.setColor(null);
}else{
this.connectedUIRepresentation=_3e67;
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
this.dropable.addEventListener("dragenter",function(_3e69){
_3e69.target.node.onDragEnter(_3e69.relatedTarget.node);
});
this.dropable.addEventListener("dragleave",function(_3e6a){
_3e6a.target.node.onDragLeave(_3e6a.relatedTarget.node);
});
this.dropable.addEventListener("drop",function(_3e6b){
_3e6b.relatedTarget.node.onDrop(_3e6b.target.node);
});
};
Port.prototype=new Rectangle();
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
var _3e70=this.moveListener.get(i);
this.parentNode.workflow.removeFigure(_3e70);
_3e70.dispose();
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
Port.prototype.setUiRepresentation=function(_3e72){
if(_3e72===null){
_3e72=new Figure();
}
if(this.uiRepresentationAdded){
this.html.removeChild(this.currentUIRepresentation.getHTMLElement());
}
this.html.appendChild(_3e72.getHTMLElement());
_3e72.paint();
this.currentUIRepresentation=_3e72;
};
Port.prototype.onMouseEnter=function(){
this.setLineWidth(2);
};
Port.prototype.onMouseLeave=function(){
this.setLineWidth(0);
};
Port.prototype.setDimension=function(width,_3e74){
Rectangle.prototype.setDimension.call(this,width,_3e74);
this.connectedUIRepresentation.setDimension(width,_3e74);
this.disconnectedUIRepresentation.setDimension(width,_3e74);
this.setPosition(this.x,this.y);
};
Port.prototype.setBackgroundColor=function(color){
this.currentUIRepresentation.setBackgroundColor(color);
};
Port.prototype.getBackgroundColor=function(){
return this.currentUIRepresentation.getBackgroundColor();
};
Port.prototype.getConnections=function(){
var _3e76=new ArrayList();
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
var _3e79=this.moveListener.get(i);
if(_3e79 instanceof Connection){
_3e76.add(_3e79);
}
}
return _3e76;
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
try{
this.currentUIRepresentation.paint();
}
catch(e){
pushErrorStack(e,"Port.prototype.paint=function()");
}
};
Port.prototype.setPosition=function(xPos,yPos){
this.originX=xPos;
this.originY=yPos;
Rectangle.prototype.setPosition.call(this,xPos,yPos);
if(this.html===null){
return;
}
this.html.style.left=(this.x-this.getWidth()/2)+"px";
this.html.style.top=(this.y-this.getHeight()/2)+"px";
};
Port.prototype.setParent=function(_3e7e){
if(this.parentNode!==null){
this.parentNode.detachMoveListener(this);
}
this.parentNode=_3e7e;
if(this.parentNode!==null){
this.parentNode.attachMoveListener(this);
}
};
Port.prototype.attachMoveListener=function(_3e7f){
Rectangle.prototype.attachMoveListener.call(this,_3e7f);
if(this.hideIfConnected==true){
this.setUiRepresentation(this.connectedUIRepresentation);
}
};
Port.prototype.detachMoveListener=function(_3e80){
Rectangle.prototype.detachMoveListener.call(this,_3e80);
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
Port.prototype.setOrigin=function(x,y){
this.originX=x;
this.originY=y;
};
Port.prototype.onDragend=function(){
this.setAlpha(1);
this.setPosition(this.originX,this.originY);
this.parentNode.workflow.hideConnectionLine();
document.body.focus();
};
Port.prototype.onDragEnter=function(port){
var _3e85=new EditPolicy(EditPolicy.CONNECT);
_3e85.canvas=this.parentNode.workflow;
_3e85.source=port;
_3e85.target=this;
var _3e86=this.createCommand(_3e85);
if(_3e86===null){
return;
}
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
var _3e89=new EditPolicy(EditPolicy.CONNECT);
_3e89.canvas=this.parentNode.workflow;
_3e89.source=port;
_3e89.target=this;
var _3e8a=this.createCommand(_3e89);
if(_3e8a!==null){
this.parentNode.workflow.getCommandStack().execute(_3e8a);
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
Port.prototype.onOtherFigureMoved=function(_3e8b){
this.fireMoveEvent();
};
Port.prototype.getName=function(){
return this.name;
};
Port.prototype.setName=function(name){
this.name=name;
};
Port.prototype.isOver=function(iX,iY){
var x=this.getAbsoluteX()-this.coronaWidth-this.getWidth()/2;
var y=this.getAbsoluteY()-this.coronaWidth-this.getHeight()/2;
var iX2=x+this.width+(this.coronaWidth*2)+this.getWidth()/2;
var iY2=y+this.height+(this.coronaWidth*2)+this.getHeight()/2;
return (iX>=x&&iX<=iX2&&iY>=y&&iY<=iY2);
};
Port.prototype.showCorona=function(flag,_3e94){
if(flag===true){
this.corona=new Corona();
this.corona.setAlpha(0.3);
this.corona.setBackgroundColor(new Color(0,125,125));
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
Port.prototype.createCommand=function(_3e95){
if(_3e95.getPolicy()===EditPolicy.MOVE){
if(!this.canDrag){
return null;
}
return new CommandMovePort(this);
}
if(_3e95.getPolicy()===EditPolicy.CONNECT){
if(_3e95.source.parentNode.id===_3e95.target.parentNode.id){
return null;
}else{
return new CommandConnect(_3e95.canvas,_3e95.source,_3e95.target);
}
}
return null;
};
InputPort=function(_385f){
Port.call(this,_385f);
};
InputPort.prototype=new Port();
InputPort.prototype.type="InputPort";
InputPort.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
return true;
};
InputPort.prototype.onDragEnter=function(port){
if(port instanceof OutputPort){
Port.prototype.onDragEnter.call(this,port);
}else{
if(port instanceof LineStartResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getSource() instanceof InputPort){
Port.prototype.onDragEnter.call(this,line.getTarget());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof InputPort){
Port.prototype.onDragEnter.call(this,line.getSource());
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
Port.prototype.onDragLeave.call(this,line.getTarget());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof InputPort){
Port.prototype.onDragLeave.call(this,line.getSource());
}
}
}
}
};
InputPort.prototype.createCommand=function(_3866){
if(_3866.getPolicy()==EditPolicy.CONNECT){
if(_3866.source.parentNode.id==_3866.target.parentNode.id){
return null;
}
if(_3866.source instanceof OutputPort){
return new CommandConnect(_3866.canvas,_3866.source,_3866.target);
}
return null;
}
return Port.prototype.createCommand.call(this,_3866);
};
OutputPort=function(_43d4){
Port.call(this,_43d4);
this.maxFanOut=100;
};
OutputPort.prototype=new Port();
OutputPort.prototype.type="OutputPort";
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
Port.prototype.onDragEnter.call(this,line.getTarget());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof OutputPort){
Port.prototype.onDragEnter.call(this,line.getSource());
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
Port.prototype.onDragLeave.call(this,line.getTarget());
}
}else{
if(port instanceof LineEndResizeHandle){
var line=this.workflow.currentSelection;
if(line instanceof Connection&&line.getTarget() instanceof OutputPort){
Port.prototype.onDragLeave.call(this,line.getSource());
}
}
}
}
};
OutputPort.prototype.onDragstart=function(x,y){
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
OutputPort.prototype.setMaxFanOut=function(count){
this.maxFanOut=count;
};
OutputPort.prototype.getMaxFanOut=function(){
return this.maxFanOut;
};
OutputPort.prototype.getFanOut=function(){
if(this.getParent().workflow===null){
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
OutputPort.prototype.createCommand=function(_43e1){
if(_43e1.getPolicy()===EditPolicy.CONNECT){
if(_43e1.source.parentNode.id===_43e1.target.parentNode.id){
return null;
}
if(_43e1.source instanceof InputPort){
return new CommandConnect(_43e1.canvas,_43e1.target,_43e1.source);
}
return null;
}
return Port.prototype.createCommand.call(this,_43e1);
};
Line=function(){
this.lineColor=new Color(0,0,0);
this.stroke=1;
this.canvas=null;
this.workflow=null;
this.html=null;
this.graphics=null;
this.id=UUID.create();
this.startX=30;
this.startY=30;
this.endX=100;
this.endY=100;
this.alpha=1;
this.isMoving=false;
this.model=null;
this.zOrder=Line.ZOrderBaseIndex;
this.corona=Line.CoronaWidth;
this.properties={};
this.moveListener=new ArrayList();
this.setSelectable(true);
this.setDeleteable(true);
};
Line.prototype.type="Line";
Line.ZOrderBaseIndex=200;
Line.ZOrderBaseIndex=200;
Line.CoronaWidth=5;
Line.setZOrderBaseIndex=function(index){
Line.ZOrderBaseIndex=index;
};
Line.setDefaultCoronaWidth=function(width){
Line.CoronaWidth=width;
};
Line.prototype.dispose=function(){
this.canvas=null;
this.workflow=null;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.getZOrder=function(){
return this.zOrder;
};
Line.prototype.setZOrder=function(index){
if(this.html!==null){
this.html.style.zIndex=index;
}
this.zOrder=index;
};
Line.prototype.setCoronaWidth=function(width){
this.corona=width;
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
Line.prototype.setId=function(id){
this.id=id;
if(this.html!==null){
this.html.id=id;
}
};
Line.prototype.getId=function(){
return this.id;
};
Line.prototype.getProperties=function(){
return this.properties;
};
Line.prototype.getProperty=function(key){
return this.properties[key];
};
Line.prototype.setProperty=function(key,value){
this.properties[key]=value;
this.setDocumentDirty();
};
Line.prototype.getHTMLElement=function(){
if(this.html===null){
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
Line.prototype.setCanvas=function(_3df3){
this.canvas=_3df3;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.setWorkflow=function(_3df4){
this.workflow=_3df4;
if(this.graphics!==null){
this.graphics.clear();
}
this.graphics=null;
};
Line.prototype.paint=function(){
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
pushErrorStack(e,"Line.prototype.paint=function()");
}
};
Line.prototype.attachMoveListener=function(_3df5){
this.moveListener.add(_3df5);
};
Line.prototype.detachMoveListener=function(_3df6){
this.moveListener.remove(_3df6);
};
Line.prototype.fireMoveEvent=function(){
var size=this.moveListener.getSize();
for(var i=0;i<size;i++){
this.moveListener.get(i).onOtherFigureMoved(this);
}
};
Line.prototype.onOtherFigureMoved=function(_3df9){
};
Line.prototype.setLineWidth=function(w){
this.stroke=w;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.setColor=function(color){
this.lineColor=color;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.getColor=function(){
return this.lineColor;
};
Line.prototype.setAlpha=function(_3dfc){
if(_3dfc==this.alpha){
return;
}
try{
this.html.style.MozOpacity=_3dfc;
}
catch(exc1){
}
try{
this.html.style.opacity=_3dfc;
}
catch(exc2){
}
try{
var _3dfd=Math.round(_3dfc*100);
if(_3dfd>=99){
this.html.style.filter="";
}else{
this.html.style.filter="alpha(opacity="+_3dfd+")";
}
}
catch(exc3){
}
this.alpha=_3dfc;
};
Line.prototype.setStartPoint=function(x,y){
this.startX=x;
this.startY=y;
if(this.graphics!==null){
this.paint();
}
this.setDocumentDirty();
};
Line.prototype.setEndPoint=function(x,y){
this.endX=x;
this.endY=y;
if(this.graphics!==null){
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
var _3e04=this.getLength();
var angle=-(180/Math.PI)*Math.asin((this.startY-this.endY)/_3e04);
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
Line.prototype.createCommand=function(_3e06){
if(_3e06.getPolicy()==EditPolicy.MOVE){
var x1=this.getStartX();
var y1=this.getStartY();
var x2=this.getEndX();
var y2=this.getEndY();
return new CommandMoveLine(this,x1,y1,x2,y2);
}
if(_3e06.getPolicy()==EditPolicy.DELETE){
if(this.isDeleteable()==false){
return null;
}
return new CommandDelete(this);
}
return null;
};
Line.prototype.setModel=function(model){
if(this.model!==null){
this.model.removePropertyChangeListener(this);
}
this.model=model;
if(this.model!==null){
this.model.addPropertyChangeListener(this);
}
};
Line.prototype.getModel=function(){
return this.model;
};
Line.prototype.onRemove=function(_3e0c){
};
Line.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
this.workflow.showMenu(menu,x,y);
}
};
Line.prototype.getContextMenu=function(){
return null;
};
Line.prototype.onDoubleClick=function(){
};
Line.prototype.setDocumentDirty=function(){
if(this.workflow!==null){
this.workflow.setDocumentDirty();
}
};
Line.prototype.containsPoint=function(px,py){
return Line.hit(this.corona,this.startX,this.startY,this.endX,this.endY,px,py);
};
Line.hit=function(_3e12,X1,Y1,X2,Y2,px,py){
X2-=X1;
Y2-=Y1;
px-=X1;
py-=Y1;
var _3e19=px*X2+py*Y2;
var _3e1a;
if(_3e19<=0){
_3e1a=0;
}else{
px=X2-px;
py=Y2-py;
_3e19=px*X2+py*Y2;
if(_3e19<=0){
_3e1a=0;
}else{
_3e1a=_3e19*_3e19/(X2*X2+Y2*Y2);
}
}
var lenSq=px*px+py*py-_3e1a;
if(lenSq<0){
lenSq=0;
}
return Math.sqrt(lenSq)<_3e12;
};
ConnectionRouter=function(){
};
ConnectionRouter.prototype.type="ConnectionRouter";
ConnectionRouter.prototype.getDirection=function(r,p){
var _43bb=Math.abs(r.x-p.x);
var _43bc=3;
var i=Math.abs(r.y-p.y);
if(i<=_43bb){
_43bb=i;
_43bc=0;
}
i=Math.abs(r.getBottom()-p.y);
if(i<=_43bb){
_43bb=i;
_43bc=2;
}
i=Math.abs(r.getRight()-p.x);
if(i<_43bb){
_43bb=i;
_43bc=1;
}
return _43bc;
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
ConnectionRouter.prototype.route=function(_43c4){
};
NullConnectionRouter=function(){
};
NullConnectionRouter.prototype=new ConnectionRouter();
NullConnectionRouter.prototype.type="NullConnectionRouter";
NullConnectionRouter.prototype.invalidate=function(){
};
NullConnectionRouter.prototype.route=function(_4640){
_4640.addPoint(_4640.getStartPoint());
_4640.addPoint(_4640.getEndPoint());
};
ManhattanConnectionRouter=function(){
this.MINDIST=20;
};
ManhattanConnectionRouter.prototype=new ConnectionRouter();
ManhattanConnectionRouter.prototype.type="ManhattanConnectionRouter";
ManhattanConnectionRouter.prototype.route=function(conn){
var _3992=conn.getStartPoint();
var _3993=this.getStartDirection(conn);
var toPt=conn.getEndPoint();
var toDir=this.getEndDirection(conn);
this._route(conn,toPt,toDir,_3992,_3993);
};
ManhattanConnectionRouter.prototype._route=function(conn,_3997,_3998,toPt,toDir){
var TOL=0.1;
var _399c=0.01;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_3997.x-toPt.x;
var yDiff=_3997.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_399c))&&((yDiff*yDiff)<(_399c))){
conn.addPoint(new Point(toPt.x,toPt.y));
return;
}
if(_3998==LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir===RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_3997.x-this.MINDIST,_3997.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir==UP))){
point=new Point(toPt.x,_3997.y);
}else{
if(_3998==toDir){
var pos=Math.min(_3997.x,toPt.x)-this.MINDIST;
point=new Point(pos,_3997.y);
}else{
point=new Point(_3997.x-(xDiff/2),_3997.y);
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
if(_3998==RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir===LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_3997.x+this.MINDIST,_3997.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new Point(toPt.x,_3997.y);
}else{
if(_3998==toDir){
var pos=Math.max(_3997.x,toPt.x)+this.MINDIST;
point=new Point(pos,_3997.y);
}else{
point=new Point(_3997.x-(xDiff/2),_3997.y);
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
if(_3998==DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir==UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_3997.x,_3997.y+this.MINDIST);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new Point(_3997.x,toPt.y);
}else{
if(_3998===toDir){
var pos=Math.max(_3997.y,toPt.y)+this.MINDIST;
point=new Point(_3997.x,pos);
}else{
point=new Point(_3997.x,_3997.y-(yDiff/2));
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
if(_3998==UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir===DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_3997.x,_3997.y-this.MINDIST);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new Point(_3997.x,toPt.y);
}else{
if(_3998===toDir){
var pos=Math.min(_3997.y,toPt.y)-this.MINDIST;
point=new Point(_3997.x,pos);
}else{
point=new Point(_3997.x,_3997.y-(yDiff/2));
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
conn.addPoint(_3997);
};
BezierConnectionRouter=function(_39fd){
if(!_39fd){
this.cheapRouter=new ManhattanConnectionRouter();
}else{
this.cheapRouter=null;
}
this.iteration=5;
};
BezierConnectionRouter.prototype=new ConnectionRouter();
BezierConnectionRouter.prototype.type="BezierConnectionRouter";
BezierConnectionRouter.prototype.drawBezier=function(_39fe,_39ff,t,iter){
var n=_39fe.length-1;
var q=[];
var _3a04=n+1;
for(var i=0;i<_3a04;i++){
q[i]=[];
q[i][0]=_39fe[i];
}
for(var j=1;j<=n;j++){
for(var i=0;i<=(n-j);i++){
q[i][j]=new Point((1-t)*q[i][j-1].x+t*q[i+1][j-1].x,(1-t)*q[i][j-1].y+t*q[i+1][j-1].y);
}
}
var c1=[];
var c2=[];
for(var i=0;i<n+1;i++){
c1[i]=q[0][i];
c2[i]=q[i][n-i];
}
if(iter>=0){
this.drawBezier(c1,_39ff,t,--iter);
this.drawBezier(c2,_39ff,t,--iter);
}else{
for(var i=0;i<n;i++){
_39ff.push(q[i][n-i]);
}
}
};
BezierConnectionRouter.prototype.route=function(conn){
if(this.cheapRouter!==null&&(conn.getSource().getParent().isMoving===true||conn.getTarget().getParent().isMoving===true)){
this.cheapRouter.route(conn);
return;
}
var _3a0a=[];
var _3a0b=conn.getStartPoint();
var toPt=conn.getEndPoint();
this._route(_3a0a,conn,toPt,this.getEndDirection(conn),_3a0b,this.getStartDirection(conn));
var _3a0d=[];
this.drawBezier(_3a0a,_3a0d,0.5,this.iteration);
for(var i=0;i<_3a0d.length;i++){
conn.addPoint(_3a0d[i]);
}
conn.addPoint(toPt);
};
BezierConnectionRouter.prototype._route=function(_3a0f,conn,_3a11,_3a12,toPt,toDir){
var TOL=0.1;
var _3a16=0.01;
var _3a17=90;
var UP=0;
var RIGHT=1;
var DOWN=2;
var LEFT=3;
var xDiff=_3a11.x-toPt.x;
var yDiff=_3a11.y-toPt.y;
var point;
var dir;
if(((xDiff*xDiff)<(_3a16))&&((yDiff*yDiff)<(_3a16))){
_3a0f.push(new Point(toPt.x,toPt.y));
return;
}
if(_3a12===LEFT){
if((xDiff>0)&&((yDiff*yDiff)<TOL)&&(toDir===RIGHT)){
point=toPt;
dir=toDir;
}else{
if(xDiff<0){
point=new Point(_3a11.x-_3a17,_3a11.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new Point(toPt.x,_3a11.y);
}else{
if(_3a12===toDir){
var pos=Math.min(_3a11.x,toPt.x)-_3a17;
point=new Point(pos,_3a11.y);
}else{
point=new Point(_3a11.x-(xDiff/2),_3a11.y);
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
if(_3a12===RIGHT){
if((xDiff<0)&&((yDiff*yDiff)<TOL)&&(toDir==LEFT)){
point=toPt;
dir=toDir;
}else{
if(xDiff>0){
point=new Point(_3a11.x+_3a17,_3a11.y);
}else{
if(((yDiff>0)&&(toDir===DOWN))||((yDiff<0)&&(toDir===UP))){
point=new Point(toPt.x,_3a11.y);
}else{
if(_3a12===toDir){
var pos=Math.max(_3a11.x,toPt.x)+_3a17;
point=new Point(pos,_3a11.y);
}else{
point=new Point(_3a11.x-(xDiff/2),_3a11.y);
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
if(_3a12===DOWN){
if(((xDiff*xDiff)<TOL)&&(yDiff<0)&&(toDir===UP)){
point=toPt;
dir=toDir;
}else{
if(yDiff>0){
point=new Point(_3a11.x,_3a11.y+_3a17);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new Point(_3a11.x,toPt.y);
}else{
if(_3a12===toDir){
var pos=Math.max(_3a11.y,toPt.y)+_3a17;
point=new Point(_3a11.x,pos);
}else{
point=new Point(_3a11.x,_3a11.y-(yDiff/2));
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
if(_3a12===UP){
if(((xDiff*xDiff)<TOL)&&(yDiff>0)&&(toDir===DOWN)){
point=toPt;
dir=toDir;
}else{
if(yDiff<0){
point=new Point(_3a11.x,_3a11.y-_3a17);
}else{
if(((xDiff>0)&&(toDir===RIGHT))||((xDiff<0)&&(toDir===LEFT))){
point=new Point(_3a11.x,toPt.y);
}else{
if(_3a12===toDir){
var pos=Math.min(_3a11.y,toPt.y)-_3a17;
point=new Point(_3a11.x,pos);
}else{
point=new Point(_3a11.x,_3a11.y-(yDiff/2));
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
this._route(_3a0f,conn,point,dir,toPt,toDir);
_3a0f.push(_3a11);
};
FanConnectionRouter=function(){
};
FanConnectionRouter.prototype=new NullConnectionRouter();
FanConnectionRouter.prototype.type="FanConnectionRouter";
FanConnectionRouter.prototype.route=function(conn){
var _3840=conn.getStartPoint();
var toPt=conn.getEndPoint();
var lines=conn.getSource().getConnections();
var _3843=new ArrayList();
var index=0;
for(var i=0;i<lines.getSize();i++){
var _3846=lines.get(i);
if(_3846.getTarget()==conn.getTarget()||_3846.getSource()==conn.getTarget()){
_3843.add(_3846);
if(conn==_3846){
index=_3843.getSize();
}
}
}
if(_3843.getSize()>1){
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
var _384c=10;
var _384d=new Point((end.x+start.x)/2,(end.y+start.y)/2);
var _384e=end.getPosition(start);
var ray;
if(_384e==PositionConstants.SOUTH||_384e==PositionConstants.EAST){
ray=new Point(end.x-start.x,end.y-start.y);
}else{
ray=new Point(start.x-end.x,start.y-end.y);
}
var _3850=Math.sqrt(ray.x*ray.x+ray.y*ray.y);
var _3851=_384c*ray.x/_3850;
var _3852=_384c*ray.y/_3850;
var _3853;
if(index%2===0){
_3853=new Point(_384d.x+(index/2)*(-1*_3852),_384d.y+(index/2)*_3851);
}else{
_3853=new Point(_384d.x+(index/2)*_3852,_384d.y+(index/2)*(-1*_3851));
}
conn.addPoint(_3853);
conn.addPoint(end);
};
Graphics=function(_35fb,_35fc,_35fd){
this.jsGraphics=_35fb;
this.xt=_35fd.x;
this.yt=_35fd.y;
this.radian=_35fc*Math.PI/180;
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
Graphics.prototype.fillPolygon=function(_3613,_3614){
var rotX=[];
var rotY=[];
for(var i=0;i<_3613.length;i++){
rotX[i]=this.xt+_3613[i]*this.cosRadian-_3614[i]*this.sinRadian;
rotY[i]=this.yt+_3613[i]*this.sinRadian+_3614[i]*this.cosRadian;
}
this.jsGraphics.fillPolygon(rotX,rotY);
};
Graphics.prototype.setColor=function(color){
this.jsGraphics.setColor(color.getHTMLStyle());
};
Graphics.prototype.drawPolygon=function(_3619,_361a){
var rotX=[];
var rotY=[];
for(var i=0;i<_3619.length;i++){
rotX[i]=this.xt+_3619[i]*this.cosRadian-_361a[i]*this.sinRadian;
rotY[i]=this.yt+_3619[i]*this.sinRadian+_361a[i]*this.cosRadian;
}
this.jsGraphics.drawPolygon(rotX,rotY);
};
Connection=function(){
Line.call(this);
this.sourcePort=null;
this.targetPort=null;
this.canDrag=true;
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
Connection.prototype=new Line();
Connection.prototype.type="Connection";
Connection.defaultRouter=new ManhattanConnectionRouter();
Connection.setDefaultRouter=function(_4271){
Connection.defaultRouter=_4271;
};
Connection.prototype.disconnect=function(){
if(this.sourcePort!==null){
this.sourcePort.detachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!==null){
this.targetPort.detachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
Connection.prototype.reconnect=function(){
if(this.sourcePort!==null){
this.sourcePort.attachMoveListener(this);
this.fireSourcePortRouteEvent();
}
if(this.targetPort!==null){
this.targetPort.attachMoveListener(this);
this.fireTargetPortRouteEvent();
}
};
Connection.prototype.isResizeable=function(){
return this.getCanDrag();
};
Connection.prototype.setCanDrag=function(flag){
this.canDrag=flag;
};
Connection.prototype.getCanDrag=function(){
return this.canDrag;
};
Connection.prototype.addFigure=function(_4273,_4274){
var entry={};
entry.figure=_4273;
entry.locator=_4274;
this.children.add(entry);
if(this.graphics!==null){
this.paint();
}
var oThis=this;
var _4277=function(){
var _4278=arguments[0]||window.event;
_4278.returnValue=false;
oThis.getWorkflow().setCurrentSelection(oThis);
oThis.getWorkflow().showLineResizeHandles(oThis);
};
if(_4273.getHTMLElement().addEventListener){
_4273.getHTMLElement().addEventListener("mousedown",_4277,false);
}else{
if(_4273.getHTMLElement().attachEvent){
_4273.getHTMLElement().attachEvent("onmousedown",_4277);
}
}
};
Connection.prototype.setSourceDecorator=function(_4279){
this.sourceDecorator=_4279;
if(this.graphics!==null){
this.paint();
}
};
Connection.prototype.getSourceDecorator=function(){
return this.sourceDecorator;
};
Connection.prototype.setTargetDecorator=function(_427a){
this.targetDecorator=_427a;
if(this.graphics!==null){
this.paint();
}
};
Connection.prototype.getTargetDecorator=function(){
return this.targetDecorator;
};
Connection.prototype.setSourceAnchor=function(_427b){
this.sourceAnchor=_427b;
this.sourceAnchor.setOwner(this.sourcePort);
if(this.graphics!==null){
this.paint();
}
};
Connection.prototype.setTargetAnchor=function(_427c){
this.targetAnchor=_427c;
this.targetAnchor.setOwner(this.targetPort);
if(this.graphics!==null){
this.paint();
}
};
Connection.prototype.setRouter=function(_427d){
if(_427d!==null){
this.router=_427d;
}else{
this.router=new NullConnectionRouter();
}
if(this.graphics!==null){
this.paint();
}
};
Connection.prototype.getRouter=function(){
return this.router;
};
Connection.prototype.setWorkflow=function(_427e){
Line.prototype.setWorkflow.call(this,_427e);
for(var i=0;i<this.children.getSize();i++){
this.children.get(i).isAppended=false;
}
};
Connection.prototype.paint=function(){
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
this.targetDecorator.paint(new Graphics(this.graphics,this.getEndAngle(),this.getEndPoint()));
}
if(this.sourceDecorator!==null){
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
}
catch(e){
pushErrorStack(e,"Connection.prototype.paint=function()");
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
var _4282=new ArrayList();
var line=null;
for(var i=0;i<this.lineSegments.getSize();i++){
line=this.lineSegments.get(i);
_4282.add(line.start);
}
if(line!==null){
_4282.add(line.end);
}
return _4282;
};
Connection.prototype.addPoint=function(p){
p=new Point(parseInt(p.x),parseInt(p.y));
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
Connection.prototype.refreshSourcePort=function(){
var model=this.getModel().getSourceModel();
var _4288=this.getModel().getSourcePortName();
var _4289=this.getWorkflow().getDocument().getFigures();
var count=_4289.getSize();
for(var i=0;i<count;i++){
var _428c=_4289.get(i);
if(_428c.getModel()==model){
var port=_428c.getOutputPort(_4288);
this.setSource(port);
}
}
this.setRouter(this.getRouter());
};
Connection.prototype.refreshTargetPort=function(){
var model=this.getModel().getTargetModel();
var _428f=this.getModel().getTargetPortName();
var _4290=this.getWorkflow().getDocument().getFigures();
var count=_4290.getSize();
for(var i=0;i<count;i++){
var _4293=_4290.get(i);
if(_4293.getModel()==model){
var port=_4293.getInputPort(_428f);
this.setTarget(port);
}
}
this.setRouter(this.getRouter());
};
Connection.prototype.setSource=function(port){
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
Connection.prototype.getSource=function(){
return this.sourcePort;
};
Connection.prototype.setTarget=function(port){
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
Connection.prototype.getTarget=function(){
return this.targetPort;
};
Connection.prototype.onOtherFigureMoved=function(_4297){
if(_4297==this.sourcePort){
this.setStartPoint(this.sourcePort.getAbsoluteX(),this.sourcePort.getAbsoluteY());
}else{
this.setEndPoint(this.targetPort.getAbsoluteX(),this.targetPort.getAbsoluteY());
}
};
Connection.prototype.containsPoint=function(px,py){
for(var i=0;i<this.lineSegments.getSize();i++){
var line=this.lineSegments.get(i);
if(Line.hit(this.corona,line.start.x,line.start.y,line.end.x,line.end.y,px,py)){
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
var _429e=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_429e);
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
if(this.lineSegments.getSize()===0){
return 90;
}
var p1=this.lineSegments.get(this.lineSegments.getSize()-1).end;
var p2=this.lineSegments.get(this.lineSegments.getSize()-1).start;
if(this.router instanceof BezierConnectionRouter){
p2=this.lineSegments.get(this.lineSegments.getSize()-5).end;
}
var _42a2=Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
var angle=-(180/Math.PI)*Math.asin((p1.y-p2.y)/_42a2);
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
var _42a4=this.sourcePort.getConnections();
for(var i=0;i<_42a4.getSize();i++){
_42a4.get(i).paint();
}
};
Connection.prototype.fireTargetPortRouteEvent=function(){
var _42a6=this.targetPort.getConnections();
for(var i=0;i<_42a6.getSize();i++){
_42a6.get(i).paint();
}
};
Connection.prototype.createCommand=function(_42a8){
if(_42a8.getPolicy()==EditPolicy.MOVE){
return new CommandReconnect(this);
}
if(_42a8.getPolicy()==EditPolicy.DELETE){
if(this.isDeleteable()==true){
return new CommandDelete(this);
}
return null;
}
return null;
};
ConnectionAnchor=function(owner){
this.owner=owner;
};
ConnectionAnchor.prototype.type="ConnectionAnchor";
ConnectionAnchor.prototype.getLocation=function(_371d){
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
if(this.getOwner()===null){
return null;
}else{
return this.getOwner().getAbsolutePosition();
}
};
ChopboxConnectionAnchor=function(owner){
ConnectionAnchor.call(this,owner);
};
ChopboxConnectionAnchor.prototype=new ConnectionAnchor();
ChopboxConnectionAnchor.prototype.type="ChopboxConnectionAnchor";
ChopboxConnectionAnchor.prototype.getLocation=function(_382a){
var r=new Dimension();
r.setBounds(this.getBox());
r.translate(-1,-1);
r.resize(1,1);
var _382c=r.x+r.w/2;
var _382d=r.y+r.h/2;
if(r.isEmpty()||(_382a.x==_382c&&_382a.y==_382d)){
return new Point(_382c,_382d);
}
var dx=_382a.x-_382c;
var dy=_382a.y-_382d;
var scale=0.5/Math.max(Math.abs(dx)/r.w,Math.abs(dy)/r.h);
dx*=scale;
dy*=scale;
_382c+=dx;
_382d+=dy;
return new Point(Math.round(_382c),Math.round(_382d));
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
ArrowConnectionDecorator.prototype=new ConnectionDecorator();
ArrowConnectionDecorator.prototype.type="ArrowConnectionDecorator";
ArrowConnectionDecorator.prototype.paint=function(g){
if(this.backgroundColor!==null){
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
this.dropable.addEventListener("figureenter",function(_3c15){
_3c15.target.node.onFigureEnter(_3c15.relatedTarget.node);
});
this.dropable.addEventListener("figureleave",function(_3c16){
_3c16.target.node.onFigureLeave(_3c16.relatedTarget.node);
});
this.dropable.addEventListener("figuredrop",function(_3c17){
_3c17.target.node.onFigureDrop(_3c17.relatedTarget.node);
});
};
CompartmentFigure.prototype=new Node();
CompartmentFigure.prototype.type="CompartmentFigure";
CompartmentFigure.prototype.onFigureEnter=function(_3c18){
};
CompartmentFigure.prototype.onFigureLeave=function(_3c19){
};
CompartmentFigure.prototype.onFigureDrop=function(_3c1a){
};
CompartmentFigure.prototype.getChildren=function(){
return this.children;
};
CompartmentFigure.prototype.addChild=function(_3c1b){
_3c1b.setZOrder(this.getZOrder()+1);
_3c1b.setParent(this);
this.children.add(_3c1b);
};
CompartmentFigure.prototype.removeChild=function(_3c1c){
_3c1c.setParent(null);
this.children.remove(_3c1c);
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
CanvasDocument=function(_366b){
this.canvas=_366b;
};
CanvasDocument.prototype.type="CanvasDocument";
CanvasDocument.prototype.getFigures=function(){
var _366c=new ArrayList();
var _366d=this.canvas.figures;
var _366e=this.canvas.dialogs;
for(var i=0;i<_366d.getSize();i++){
var _3670=_366d.get(i);
if(_366e.indexOf(_3670)==-1&&_3670.getParent()===null&&!(_3670 instanceof WindowFigure)){
_366c.add(_3670);
}
}
return _366c;
};
CanvasDocument.prototype.getFigure=function(id){
return this.canvas.getFigure(id);
};
CanvasDocument.prototype.getLines=function(){
return this.canvas.getLines();
};
CanvasDocument.prototype.getLine=function(id){
return this.canvas.getLine(id);
};
Annotation=function(msg){
this.msg=msg;
this.color=new Color(0,0,0);
this.bgColor=new Color(241,241,121);
this.fontSize=10;
this.textNode=null;
Figure.call(this);
};
Annotation.prototype=new Figure();
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
item.style.cursor="default";
this.textNode=document.createTextNode(this.msg);
item.appendChild(this.textNode);
this.disableTextSelection(item);
return item;
};
Annotation.prototype.onDoubleClick=function(){
var _43e9=new AnnotationDialog(this);
this.workflow.showDialog(_43e9);
};
Annotation.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
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
ResizeHandle=function(_3d71,type){
Rectangle.call(this,5,5);
this.type=type;
var _3d73=this.getWidth();
var _3d74=_3d73/2;
switch(this.type){
case 1:
this.setSnapToGridAnchor(new Point(_3d73,_3d73));
break;
case 2:
this.setSnapToGridAnchor(new Point(_3d74,_3d73));
break;
case 3:
this.setSnapToGridAnchor(new Point(0,_3d73));
break;
case 4:
this.setSnapToGridAnchor(new Point(0,_3d74));
break;
case 5:
this.setSnapToGridAnchor(new Point(0,0));
break;
case 6:
this.setSnapToGridAnchor(new Point(_3d74,0));
break;
case 7:
this.setSnapToGridAnchor(new Point(_3d73,0));
break;
case 8:
this.setSnapToGridAnchor(new Point(_3d73,_3d74));
case 9:
this.setSnapToGridAnchor(new Point(_3d74,_3d74));
break;
}
this.setBackgroundColor(new Color(0,255,0));
this.setWorkflow(_3d71);
this.setZOrder(10000);
};
ResizeHandle.prototype=new Rectangle();
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
case 9:
return SnapToHelper.CENTER;
}
};
ResizeHandle.prototype.onDragend=function(){
var _3d75=this.workflow.currentSelection;
if(this.commandMove!==null){
this.commandMove.setPosition(_3d75.getX(),_3d75.getY());
this.workflow.getCommandStack().execute(this.commandMove);
this.commandMove=null;
}
if(this.commandResize!==null){
this.commandResize.setDimension(_3d75.getWidth(),_3d75.getHeight());
this.workflow.getCommandStack().execute(this.commandResize);
this.commandResize=null;
}
this.workflow.hideSnapToHelperLines();
};
ResizeHandle.prototype.setPosition=function(xPos,yPos){
this.x=xPos;
this.y=yPos;
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
};
ResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
var _3d7a=this.workflow.currentSelection;
this.commandMove=_3d7a.createCommand(new EditPolicy(EditPolicy.MOVE));
this.commandResize=_3d7a.createCommand(new EditPolicy(EditPolicy.RESIZE));
return true;
};
ResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _3d7f=this.workflow.currentSelection.getX();
var _3d80=this.workflow.currentSelection.getY();
var _3d81=this.workflow.currentSelection.getWidth();
var _3d82=this.workflow.currentSelection.getHeight();
switch(this.type){
case 1:
this.workflow.currentSelection.setPosition(_3d7f-diffX,_3d80-diffY);
this.workflow.currentSelection.setDimension(_3d81+diffX,_3d82+diffY);
break;
case 2:
this.workflow.currentSelection.setPosition(_3d7f,_3d80-diffY);
this.workflow.currentSelection.setDimension(_3d81,_3d82+diffY);
break;
case 3:
this.workflow.currentSelection.setPosition(_3d7f,_3d80-diffY);
this.workflow.currentSelection.setDimension(_3d81-diffX,_3d82+diffY);
break;
case 4:
this.workflow.currentSelection.setPosition(_3d7f,_3d80);
this.workflow.currentSelection.setDimension(_3d81-diffX,_3d82);
break;
case 5:
this.workflow.currentSelection.setPosition(_3d7f,_3d80);
this.workflow.currentSelection.setDimension(_3d81-diffX,_3d82-diffY);
break;
case 6:
this.workflow.currentSelection.setPosition(_3d7f,_3d80);
this.workflow.currentSelection.setDimension(_3d81,_3d82-diffY);
break;
case 7:
this.workflow.currentSelection.setPosition(_3d7f-diffX,_3d80);
this.workflow.currentSelection.setDimension(_3d81+diffX,_3d82-diffY);
break;
case 8:
this.workflow.currentSelection.setPosition(_3d7f-diffX,_3d80);
this.workflow.currentSelection.setDimension(_3d81+diffX,_3d82);
break;
}
this.workflow.moveResizeHandles(this.workflow.getCurrentSelection());
};
ResizeHandle.prototype.setCanDrag=function(flag){
Rectangle.prototype.setCanDrag.call(this,flag);
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
ResizeHandle.prototype.onKeyDown=function(_3d84,ctrl){
this.workflow.onKeyDown(_3d84,ctrl);
};
ResizeHandle.prototype.fireMoveEvent=function(){
};
LineStartResizeHandle=function(_3653){
ResizeHandle.call(this,_3653,9);
this.setDimension(10,10);
this.setBackgroundColor(new Color(100,255,0));
this.setZOrder(10000);
};
LineStartResizeHandle.prototype=new ResizeHandle();
LineStartResizeHandle.prototype.type="LineStartResizeHandle";
LineStartResizeHandle.prototype.onDragend=function(){
if(this.workflow.currentSelection instanceof Connection){
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
LineStartResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
this.command=this.workflow.currentSelection.createCommand(new EditPolicy(EditPolicy.MOVE));
return this.command!==null;
};
LineStartResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _365a=this.workflow.currentSelection.getStartPoint();
var line=this.workflow.currentSelection;
line.setStartPoint(_365a.x-diffX,_365a.y-diffY);
line.isMoving=true;
};
LineStartResizeHandle.prototype.onDrop=function(_365c){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof Connection){
this.command.setNewPorts(_365c,line.getTarget());
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
LineEndResizeHandle=function(_43fe){
ResizeHandle.call(this,_43fe,9);
this.setDimension(10,10);
this.setBackgroundColor(new Color(0,255,0));
this.setZOrder(10000);
};
LineEndResizeHandle.prototype=new ResizeHandle();
LineEndResizeHandle.prototype.type="LineEndResizeHandle";
LineEndResizeHandle.prototype.onDragend=function(){
if(this.workflow.currentSelection instanceof Connection){
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
LineEndResizeHandle.prototype.onDragstart=function(x,y){
if(!this.canDrag){
return false;
}
this.command=this.workflow.currentSelection.createCommand(new EditPolicy(EditPolicy.MOVE));
return this.command!==null;
};
LineEndResizeHandle.prototype.onDrag=function(){
var oldX=this.getX();
var oldY=this.getY();
Rectangle.prototype.onDrag.call(this);
var diffX=oldX-this.getX();
var diffY=oldY-this.getY();
var _4405=this.workflow.currentSelection.getEndPoint();
var line=this.workflow.currentSelection;
line.setEndPoint(_4405.x-diffX,_4405.y-diffY);
line.isMoving=true;
};
LineEndResizeHandle.prototype.onDrop=function(_4407){
var line=this.workflow.currentSelection;
line.isMoving=false;
if(line instanceof Connection){
this.command.setNewPorts(line.getSource(),_4407);
this.getWorkflow().getCommandStack().execute(this.command);
}
this.command=null;
};
Canvas=function(_4259){
try{
if(_4259){
this.construct(_4259);
}
this.enableSmoothFigureHandling=false;
this.canvasLines=new ArrayList();
}
catch(e){
pushErrorStack(e,"Canvas=function(/*:String*/id)");
}
};
Canvas.IMAGE_BASE_URL="";
Canvas.prototype.type="Canvas";
Canvas.prototype.construct=function(_425a){
this.canvasId=_425a;
this.html=document.getElementById(this.canvasId);
this.scrollArea=document.body.parentNode;
};
Canvas.prototype.setViewPort=function(divId){
this.scrollArea=document.getElementById(divId);
};
Canvas.prototype.addFigure=function(_425c,xPos,yPos,_425f){
try{
if(this.enableSmoothFigureHandling===true){
if(_425c.timer<=0){
_425c.setAlpha(0.001);
}
var _4260=_425c;
var _4261=function(){
if(_4260.alpha<1){
_4260.setAlpha(Math.min(1,_4260.alpha+0.05));
}else{
window.clearInterval(_4260.timer);
_4260.timer=-1;
}
};
if(_4260.timer>0){
window.clearInterval(_4260.timer);
}
_4260.timer=window.setInterval(_4261,30);
}
_425c.setCanvas(this);
if(xPos&&yPos){
_425c.setPosition(xPos,yPos);
}
if(_425c instanceof Line){
this.canvasLines.add(_425c);
this.html.appendChild(_425c.getHTMLElement());
}else{
var obj=this.canvasLines.getFirstElement();
if(obj===null){
this.html.appendChild(_425c.getHTMLElement());
}else{
this.html.insertBefore(_425c.getHTMLElement(),obj.getHTMLElement());
}
}
if(!_425f){
_425c.paint();
}
}
catch(e){
pushErrorStack(e,"Canvas.prototype.addFigure= function( /*:Figure*/figure,/*:int*/ xPos,/*:int*/ yPos, /*:boolean*/ avoidPaint)");
}
};
Canvas.prototype.removeFigure=function(_4263){
if(this.enableSmoothFigureHandling===true){
var oThis=this;
var _4265=_4263;
var _4266=function(){
if(_4265.alpha>0){
_4265.setAlpha(Math.max(0,_4265.alpha-0.05));
}else{
window.clearInterval(_4265.timer);
_4265.timer=-1;
oThis.html.removeChild(_4265.html);
_4265.setCanvas(null);
}
};
if(_4265.timer>0){
window.clearInterval(_4265.timer);
}
_4265.timer=window.setInterval(_4266,20);
}else{
this.html.removeChild(_4263.html);
_4263.setCanvas(null);
}
if(_4263 instanceof Line){
this.canvasLines.remove(_4263);
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
Canvas.prototype.setWidth=function(width){
if(this.scrollArea!==null){
this.scrollArea.style.width=width+"px";
}else{
this.html.style.width=width+"px";
}
};
Canvas.prototype.getHeight=function(){
return parseInt(this.html.style.height);
};
Canvas.prototype.setHeight=function(_4269){
if(this.scrollArea!==null){
this.scrollArea.style.height=_4269+"px";
}else{
this.html.style.height=_4269+"px";
}
};
Canvas.prototype.setBackgroundImage=function(_426a,_426b){
if(_426a!==null){
if(_426b){
this.html.style.background="transparent url("+_426a+") ";
}else{
this.html.style.background="transparent url("+_426a+") no-repeat";
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
while((el=el.offsetParent)!==null){
ot+=el.offsetTop;
}
return ot;
};
Canvas.prototype.getAbsoluteX=function(){
var el=this.html;
var ol=el.offsetLeft;
while((el=el.offsetParent)!==null){
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
this.draggingLineCommand=null;
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
if(this.html!==null){
this.html.style.backgroundImage="url(grid_10.png)";
this.html.className="Workflow";
oThis=this;
this.html.tabIndex="0";
var _42e9=function(){
var _42ea=arguments[0]||window.event;
_42ea.cancelBubble=true;
_42ea.returnValue=false;
_42ea.stopped=true;
var diffX=_42ea.clientX;
var diffY=_42ea.clientY;
var _42ed=oThis.getScrollLeft();
var _42ee=oThis.getScrollTop();
var _42ef=oThis.getAbsoluteX();
var _42f0=oThis.getAbsoluteY();
if(oThis.getBestFigure(diffX+_42ed-_42ef,diffY+_42ee-_42f0)!==null){
return;
}
var line=oThis.getBestLine(diffX+_42ed-_42ef,diffY+_42ee-_42f0,null);
if(line!==null){
line.onContextMenu(diffX+_42ed-_42ef,diffY+_42ee-_42f0);
}else{
oThis.onContextMenu(diffX+_42ed-_42ef,diffY+_42ee-_42f0);
}
};
this.html.oncontextmenu=function(){
return false;
};
var oThis=this;
var _42f3=function(event){
var ctrl=event.ctrlKey;
oThis.onKeyDown(event.keyCode,ctrl);
};
var _42f6=function(){
var _42f7=arguments[0]||window.event;
if(_42f7.returnValue==false){
return;
}
var diffX=_42f7.clientX;
var diffY=_42f7.clientY;
var _42fa=oThis.getScrollLeft();
var _42fb=oThis.getScrollTop();
var _42fc=oThis.getAbsoluteX();
var _42fd=oThis.getAbsoluteY();
oThis.onMouseDown(diffX+_42fa-_42fc,diffY+_42fb-_42fd);
};
var _42fe=function(){
var _42ff=arguments[0]||window.event;
if(oThis.currentMenu!==null){
oThis.removeFigure(oThis.currentMenu);
oThis.currentMenu=null;
}
if(_42ff.button==2){
return;
}
var diffX=_42ff.clientX;
var diffY=_42ff.clientY;
var _4302=oThis.getScrollLeft();
var _4303=oThis.getScrollTop();
var _4304=oThis.getAbsoluteX();
var _4305=oThis.getAbsoluteY();
oThis.onMouseUp(diffX+_4302-_4304,diffY+_4303-_4305);
};
var _4306=function(){
var _4307=arguments[0]||window.event;
var diffX=_4307.clientX;
var diffY=_4307.clientY;
var _430a=oThis.getScrollLeft();
var _430b=oThis.getScrollTop();
var _430c=oThis.getAbsoluteX();
var _430d=oThis.getAbsoluteY();
oThis.currentMouseX=diffX+_430a-_430c;
oThis.currentMouseY=diffY+_430b-_430d;
var obj=oThis.getBestFigure(oThis.currentMouseX,oThis.currentMouseY);
if(Drag.currentHover!==null&&obj===null){
var _430f=new DragDropEvent();
_430f.initDragDropEvent("mouseleave",false,oThis);
Drag.currentHover.dispatchEvent(_430f);
}else{
var diffX=_4307.clientX;
var diffY=_4307.clientY;
var _430a=oThis.getScrollLeft();
var _430b=oThis.getScrollTop();
var _430c=oThis.getAbsoluteX();
var _430d=oThis.getAbsoluteY();
oThis.onMouseMove(diffX+_430a-_430c,diffY+_430b-_430d);
}
if(obj===null){
Drag.currentHover=null;
}
if(oThis.tooltip!==null){
if(Math.abs(oThis.currentTooltipX-oThis.currentMouseX)>10||Math.abs(oThis.currentTooltipY-oThis.currentMouseY)>10){
oThis.showTooltip(null);
}
}
};
var _4310=function(_4311){
var _4311=arguments[0]||window.event;
var diffX=_4311.clientX;
var diffY=_4311.clientY;
var _4314=oThis.getScrollLeft();
var _4315=oThis.getScrollTop();
var _4316=oThis.getAbsoluteX();
var _4317=oThis.getAbsoluteY();
var line=oThis.getBestLine(diffX+_4314-_4316,diffY+_4315-_4317,null);
if(line!==null){
line.onDoubleClick();
}
};
if(this.html.addEventListener){
this.html.addEventListener("contextmenu",_42e9,false);
this.html.addEventListener("mousemove",_4306,false);
this.html.addEventListener("mouseup",_42fe,false);
this.html.addEventListener("mousedown",_42f6,false);
this.html.addEventListener("keydown",_42f3,false);
this.html.addEventListener("dblclick",_4310,false);
}else{
if(this.html.attachEvent){
this.html.attachEvent("oncontextmenu",_42e9);
this.html.attachEvent("onmousemove",_4306);
this.html.attachEvent("onmousedown",_42f6);
this.html.attachEvent("onmouseup",_42fe);
this.html.attachEvent("onkeydown",_42f3);
this.html.attachEvent("ondblclick",_4310);
}else{
throw "Open-jACOB Draw2D not supported in this browser.";
}
}
}
}
catch(e){
pushErrorStack(e,"Workflow=function(/*:String*/id)");
}
};
Workflow.prototype=new Canvas();
Workflow.prototype.type="Workflow";
Workflow.COLOR_GREEN=new Color(0,255,0);
Workflow.prototype.clear=function(){
this.scrollTo(0,0,true);
this.gridWidthX=10;
this.gridWidthY=10;
this.snapToGridHelper=null;
this.verticalSnapToHelperLine=null;
this.horizontalSnapToHelperLine=null;
var _4319=this.getDocument();
var _431a=_4319.getLines().clone();
for(var i=0;i<_431a.getSize();i++){
(new CommandDelete(_431a.get(i))).execute();
}
var _431c=_4319.getFigures().clone();
for(var i=0;i<_431c.getSize();i++){
(new CommandDelete(_431c.get(i))).execute();
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
var _431d=this.getScrollLeft();
var _431e=this.getScrollTop();
var _431f=_431d-this.oldScrollPosLeft;
var _4320=_431e-this.oldScrollPosTop;
for(var i=0;i<this.figures.getSize();i++){
var _4322=this.figures.get(i);
if(_4322.hasFixedPosition&&_4322.hasFixedPosition()==true){
_4322.setPosition(_4322.getX()+_431f,_4322.getY()+_4320);
}
}
this.oldScrollPosLeft=_431d;
this.oldScrollPosTop=_431e;
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
Workflow.prototype.showTooltip=function(_432d,_432e){
if(this.tooltip!==null){
this.removeFigure(this.tooltip);
this.tooltip=null;
if(this.tooltipTimer>=0){
window.clearTimeout(this.tooltipTimer);
this.tooltipTimer=-1;
}
}
this.tooltip=_432d;
if(this.tooltip!==null){
this.currentTooltipX=this.currentMouseX;
this.currentTooltipY=this.currentMouseY;
this.addFigure(this.tooltip,this.currentTooltipX+10,this.currentTooltipY+10);
var oThis=this;
var _4330=function(){
oThis.tooltipTimer=-1;
oThis.showTooltip(null);
};
if(_432e==true){
this.tooltipTimer=window.setTimeout(_4330,5000);
}
}
};
Workflow.prototype.showDialog=function(_4331,xPos,yPos){
if(xPos){
this.addFigure(_4331,xPos,yPos);
}else{
this.addFigure(_4331,200,100);
}
this.dialogs.add(_4331);
};
Workflow.prototype.showMenu=function(menu,xPos,yPos){
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
Workflow.prototype.onContextMenu=function(x,y){
var menu=this.getContextMenu();
if(menu!==null){
this.showMenu(menu,x,y);
}
};
Workflow.prototype.getContextMenu=function(){
return null;
};
Workflow.prototype.setToolWindow=function(_433a,x,y){
this.toolPalette=_433a;
if(y){
this.addFigure(_433a,x,y);
}else{
this.addFigure(_433a,20,20);
}
this.dialogs.add(_433a);
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
Workflow.prototype.addFigure=function(_4341,xPos,yPos){
try{
Canvas.prototype.addFigure.call(this,_4341,xPos,yPos,true);
_4341.setWorkflow(this);
var _4344=this;
if(_4341 instanceof CompartmentFigure){
this.compartments.add(_4341);
}
if(_4341 instanceof Line){
this.lines.add(_4341);
}else{
this.figures.add(_4341);
_4341.draggable.addEventListener("drag",function(_4345){
var _4346=_4344.getFigure(_4345.target.element.id);
if(_4346===null){
return;
}
if(_4346.isSelectable()==false){
return;
}
_4344.moveResizeHandles(_4346);
});
}
_4341.paint();
this.setDocumentDirty();
}
catch(e){
pushErrorStack(e,"Workflow.prototype.addFigure=function(/*:Figure*/ figure ,/*:int*/ xPos, /*:int*/ yPos)");
}
};
Workflow.prototype.removeFigure=function(_4347){
Canvas.prototype.removeFigure.call(this,_4347);
this.figures.remove(_4347);
this.lines.remove(_4347);
this.dialogs.remove(_4347);
_4347.setWorkflow(null);
if(_4347 instanceof CompartmentFigure){
this.compartments.remove(_4347);
}
if(_4347 instanceof Connection){
_4347.disconnect();
}
if(this.currentSelection==_4347){
this.setCurrentSelection(null);
}
this.setDocumentDirty();
_4347.onRemove(this);
};
Workflow.prototype.moveFront=function(_4348){
this.html.removeChild(_4348.getHTMLElement());
this.html.appendChild(_4348.getHTMLElement());
};
Workflow.prototype.moveBack=function(_4349){
this.html.removeChild(_4349.getHTMLElement());
this.html.insertBefore(_4349.getHTMLElement(),this.html.firstChild);
};
Workflow.prototype.getBestCompartmentFigure=function(x,y,_434c){
var _434d=null;
for(var i=0;i<this.figures.getSize();i++){
var _434f=this.figures.get(i);
if((_434f instanceof CompartmentFigure)&&_434f.isOver(x,y)==true&&_434f!=_434c){
if(_434d===null){
_434d=_434f;
}else{
if(_434d.getZOrder()<_434f.getZOrder()){
_434d=_434f;
}
}
}
}
return _434d;
};
Workflow.prototype.getBestFigure=function(x,y,_4352){
var _4353=null;
for(var i=0;i<this.figures.getSize();i++){
var _4355=this.figures.get(i);
if(_4355.isOver(x,y)==true&&_4355!=_4352){
if(_4353===null){
_4353=_4355;
}else{
if(_4353.getZOrder()<_4355.getZOrder()){
_4353=_4355;
}
}
}
}
return _4353;
};
Workflow.prototype.getBestLine=function(x,y,_4358){
var _4359=null;
var count=this.lines.getSize();
for(var i=0;i<count;i++){
var line=this.lines.get(i);
if(line.containsPoint(x,y)==true&&line!=_4358){
if(_4359===null){
_4359=line;
}else{
if(_4359.getZOrder()<line.getZOrder()){
_4359=line;
}
}
}
}
return _4359;
};
Workflow.prototype.getFigure=function(id){
for(var i=0;i<this.figures.getSize();i++){
var _435f=this.figures.get(i);
if(_435f.id==id){
return _435f;
}
}
return null;
};
Workflow.prototype.getFigures=function(){
return this.figures;
};
Workflow.prototype.getDocument=function(){
return new CanvasDocument(this);
};
Workflow.prototype.addSelectionListener=function(w){
if(w!==null){
if(w.onSelectionChanged){
this.selectionListeners.add(w);
}else{
throw "Object doesn't implement required callback method [onSelectionChanged]";
}
}
};
Workflow.prototype.removeSelectionListener=function(w){
this.selectionListeners.remove(w);
};
Workflow.prototype.setCurrentSelection=function(_4362){
if(_4362===null||this.currentSelection!=_4362){
this.hideResizeHandles();
this.hideLineResizeHandles();
}
this.currentSelection=_4362;
for(var i=0;i<this.selectionListeners.getSize();i++){
var w=this.selectionListeners.get(i);
if(w.onSelectionChanged){
w.onSelectionChanged(this.currentSelection,this.currentSelection?this.currentSelection.getModel():null);
}
}
if(_4362 instanceof Line){
this.showLineResizeHandles(_4362);
if(!(_4362 instanceof Connection)){
this.draggingLineCommand=line.createCommand(new EditPolicy(EditPolicy.MOVE));
if(this.draggingLineCommand!==null){
this.draggingLine=_4362;
}
}
}
};
Workflow.prototype.getCurrentSelection=function(){
return this.currentSelection;
};
Workflow.prototype.getLine=function(id){
var count=this.lines.getSize();
for(var i=0;i<count;i++){
var line=this.lines.get(i);
if(line.getId()==id){
return line;
}
}
return null;
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
if(this.connectionLine.canvas===null){
Canvas.prototype.addFigure.call(this,this.connectionLine);
}
};
Workflow.prototype.hideConnectionLine=function(){
if(this.connectionLine.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.connectionLine);
}
};
Workflow.prototype.showLineResizeHandles=function(_436f){
var _4370=this.resizeHandleStart.getWidth()/2;
var _4371=this.resizeHandleStart.getHeight()/2;
var _4372=_436f.getStartPoint();
var _4373=_436f.getEndPoint();
Canvas.prototype.addFigure.call(this,this.resizeHandleStart,_4372.x-_4370,_4372.y-_4370);
Canvas.prototype.addFigure.call(this,this.resizeHandleEnd,_4373.x-_4370,_4373.y-_4370);
this.resizeHandleStart.setCanDrag(_436f.isResizeable());
this.resizeHandleEnd.setCanDrag(_436f.isResizeable());
if(_436f.isResizeable()){
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
if(this.resizeHandleStart.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandleStart);
}
if(this.resizeHandleEnd.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandleEnd);
}
};
Workflow.prototype.showResizeHandles=function(_4374){
this.hideLineResizeHandles();
this.hideResizeHandles();
if(this.getEnableSmoothFigureHandling()==true&&this.getCurrentSelection()!=_4374){
this.resizeHandle1.setAlpha(0.01);
this.resizeHandle2.setAlpha(0.01);
this.resizeHandle3.setAlpha(0.01);
this.resizeHandle4.setAlpha(0.01);
this.resizeHandle5.setAlpha(0.01);
this.resizeHandle6.setAlpha(0.01);
this.resizeHandle7.setAlpha(0.01);
this.resizeHandle8.setAlpha(0.01);
}
var _4375=this.resizeHandle1.getWidth();
var _4376=this.resizeHandle1.getHeight();
var _4377=_4374.getHeight();
var _4378=_4374.getWidth();
var xPos=_4374.getX();
var yPos=_4374.getY();
Canvas.prototype.addFigure.call(this,this.resizeHandle1,xPos-_4375,yPos-_4376);
Canvas.prototype.addFigure.call(this,this.resizeHandle3,xPos+_4378,yPos-_4376);
Canvas.prototype.addFigure.call(this,this.resizeHandle5,xPos+_4378,yPos+_4377);
Canvas.prototype.addFigure.call(this,this.resizeHandle7,xPos-_4375,yPos+_4377);
this.moveFront(this.resizeHandle1);
this.moveFront(this.resizeHandle3);
this.moveFront(this.resizeHandle5);
this.moveFront(this.resizeHandle7);
this.resizeHandle1.setCanDrag(_4374.isResizeable());
this.resizeHandle3.setCanDrag(_4374.isResizeable());
this.resizeHandle5.setCanDrag(_4374.isResizeable());
this.resizeHandle7.setCanDrag(_4374.isResizeable());
if(_4374.isResizeable()){
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
if(_4374.isStrechable()&&_4374.isResizeable()){
this.resizeHandle2.setCanDrag(_4374.isResizeable());
this.resizeHandle4.setCanDrag(_4374.isResizeable());
this.resizeHandle6.setCanDrag(_4374.isResizeable());
this.resizeHandle8.setCanDrag(_4374.isResizeable());
Canvas.prototype.addFigure.call(this,this.resizeHandle2,xPos+(_4378/2)-this.resizeHandleHalfWidth,yPos-_4376);
Canvas.prototype.addFigure.call(this,this.resizeHandle4,xPos+_4378,yPos+(_4377/2)-(_4376/2));
Canvas.prototype.addFigure.call(this,this.resizeHandle6,xPos+(_4378/2)-this.resizeHandleHalfWidth,yPos+_4377);
Canvas.prototype.addFigure.call(this,this.resizeHandle8,xPos-_4375,yPos+(_4377/2)-(_4376/2));
this.moveFront(this.resizeHandle2);
this.moveFront(this.resizeHandle4);
this.moveFront(this.resizeHandle6);
this.moveFront(this.resizeHandle8);
}
};
Workflow.prototype.hideResizeHandles=function(){
if(this.resizeHandle1.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle1);
}
if(this.resizeHandle2.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle2);
}
if(this.resizeHandle3.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle3);
}
if(this.resizeHandle4.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle4);
}
if(this.resizeHandle5.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle5);
}
if(this.resizeHandle6.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle6);
}
if(this.resizeHandle7.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle7);
}
if(this.resizeHandle8.canvas!==null){
Canvas.prototype.removeFigure.call(this,this.resizeHandle8);
}
};
Workflow.prototype.moveResizeHandles=function(_437c){
var _437d=this.resizeHandle1.getWidth();
var _437e=this.resizeHandle1.getHeight();
var _437f=_437c.getHeight();
var _4380=_437c.getWidth();
var xPos=_437c.getX();
var yPos=_437c.getY();
this.resizeHandle1.setPosition(xPos-_437d,yPos-_437e);
this.resizeHandle3.setPosition(xPos+_4380,yPos-_437e);
this.resizeHandle5.setPosition(xPos+_4380,yPos+_437f);
this.resizeHandle7.setPosition(xPos-_437d,yPos+_437f);
if(_437c.isStrechable()){
this.resizeHandle2.setPosition(xPos+(_4380/2)-this.resizeHandleHalfWidth,yPos-_437e);
this.resizeHandle4.setPosition(xPos+_4380,yPos+(_437f/2)-(_437e/2));
this.resizeHandle6.setPosition(xPos+(_4380/2)-this.resizeHandleHalfWidth,yPos+_437f);
this.resizeHandle8.setPosition(xPos-_437d,yPos+(_437f/2)-(_437e/2));
}
};
Workflow.prototype.onMouseDown=function(x,y){
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
Workflow.prototype.onMouseUp=function(x,y){
this.dragging=false;
if(this.draggingLineCommand!==null){
this.getCommandStack().execute(this.draggingLineCommand);
this.draggingLine=null;
this.draggingLineCommand=null;
}
};
Workflow.prototype.onMouseMove=function(x,y){
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
Workflow.prototype.onKeyDown=function(_438c,ctrl){
if(_438c==46&&this.currentSelection!==null){
this.commandStack.execute(this.currentSelection.createCommand(new EditPolicy(EditPolicy.DELETE)));
}else{
if(_438c==90&&ctrl){
this.commandStack.undo();
}else{
if(_438c==89&&ctrl){
this.commandStack.redo();
}
}
}
};
Workflow.prototype.setDocumentDirty=function(){
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
pushErrorStack(e,"Workflow.prototype.setDocumentDirty=function()");
}
};
Workflow.prototype.snapToHelper=function(_4390,pos){
if(this.snapToGeometryHelper!==null){
if(_4390 instanceof ResizeHandle){
var _4392=_4390.getSnapToGridAnchor();
pos.x+=_4392.x;
pos.y+=_4392.y;
var _4393=new Point(pos.x,pos.y);
var _4394=_4390.getSnapToDirection();
var _4395=this.snapToGeometryHelper.snapPoint(_4394,pos,_4393);
if((_4394&SnapToHelper.EAST_WEST)&&!(_4395&SnapToHelper.EAST_WEST)){
this.showSnapToHelperLineVertical(_4393.x);
}else{
this.hideSnapToHelperLineVertical();
}
if((_4394&SnapToHelper.NORTH_SOUTH)&&!(_4395&SnapToHelper.NORTH_SOUTH)){
this.showSnapToHelperLineHorizontal(_4393.y);
}else{
this.hideSnapToHelperLineHorizontal();
}
_4393.x-=_4392.x;
_4393.y-=_4392.y;
return _4393;
}else{
var _4396=new Dimension(pos.x,pos.y,_4390.getWidth(),_4390.getHeight());
var _4393=new Dimension(pos.x,pos.y,_4390.getWidth(),_4390.getHeight());
var _4394=SnapToHelper.NSEW;
var _4395=this.snapToGeometryHelper.snapRectangle(_4396,_4393);
if((_4394&SnapToHelper.WEST)&&!(_4395&SnapToHelper.WEST)){
this.showSnapToHelperLineVertical(_4393.x);
}else{
if((_4394&SnapToHelper.EAST)&&!(_4395&SnapToHelper.EAST)){
this.showSnapToHelperLineVertical(_4393.getX()+_4393.getWidth());
}else{
this.hideSnapToHelperLineVertical();
}
}
if((_4394&SnapToHelper.NORTH)&&!(_4395&SnapToHelper.NORTH)){
this.showSnapToHelperLineHorizontal(_4393.y);
}else{
if((_4394&SnapToHelper.SOUTH)&&!(_4395&SnapToHelper.SOUTH)){
this.showSnapToHelperLineHorizontal(_4393.getY()+_4393.getHeight());
}else{
this.hideSnapToHelperLineHorizontal();
}
}
return _4393.getTopLeft();
}
}else{
if(this.snapToGridHelper!==null){
var _4392=_4390.getSnapToGridAnchor();
pos.x=pos.x+_4392.x;
pos.y=pos.y+_4392.y;
var _4393=new Point(pos.x,pos.y);
this.snapToGridHelper.snapPoint(0,pos,_4393);
_4393.x=_4393.x-_4392.x;
_4393.y=_4393.y-_4392.y;
return _4393;
}
}
return pos;
};
Workflow.prototype.showSnapToHelperLineHorizontal=function(_4397){
if(this.horizontalSnapToHelperLine===null){
this.horizontalSnapToHelperLine=new Line();
this.horizontalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.horizontalSnapToHelperLine);
}
this.horizontalSnapToHelperLine.setStartPoint(0,_4397);
this.horizontalSnapToHelperLine.setEndPoint(this.getWidth(),_4397);
};
Workflow.prototype.showSnapToHelperLineVertical=function(_4398){
if(this.verticalSnapToHelperLine===null){
this.verticalSnapToHelperLine=new Line();
this.verticalSnapToHelperLine.setColor(new Color(175,175,255));
this.addFigure(this.verticalSnapToHelperLine);
}
this.verticalSnapToHelperLine.setStartPoint(_4398,0);
this.verticalSnapToHelperLine.setEndPoint(_4398,this.getHeight());
};
Workflow.prototype.hideSnapToHelperLines=function(){
this.hideSnapToHelperLineHorizontal();
this.hideSnapToHelperLineVertical();
};
Workflow.prototype.hideSnapToHelperLineHorizontal=function(){
if(this.horizontalSnapToHelperLine!==null){
this.removeFigure(this.horizontalSnapToHelperLine);
this.horizontalSnapToHelperLine=null;
}
};
Workflow.prototype.hideSnapToHelperLineVertical=function(){
if(this.verticalSnapToHelperLine!==null){
this.removeFigure(this.verticalSnapToHelperLine);
this.verticalSnapToHelperLine=null;
}
};
WindowFigure=function(title){
this.title=title;
this.titlebar=null;
Figure.call(this);
this.setDeleteable(false);
this.setCanSnapToHelper(false);
this.setZOrder(WindowFigure.ZOrderIndex);
};
WindowFigure.prototype=new Figure();
WindowFigure.prototype.type=":WindowFigure";
WindowFigure.ZOrderIndex=50000;
WindowFigure.setZOrderBaseIndex=function(index){
WindowFigure.ZOrderBaseIndex=index;
};
WindowFigure.prototype.hasFixedPosition=function(){
return true;
};
WindowFigure.prototype.hasTitleBar=function(){
return true;
};
WindowFigure.prototype.createHTMLElement=function(){
var item=Figure.prototype.createHTMLElement.call(this);
item.style.margin="0px";
item.style.padding="0px";
item.style.border="1px solid black";
item.style.backgroundImage="url(window_bg.png)";
item.style.zIndex=WindowFigure.ZOrderIndex;
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
this.disableTextSelection(this.titlebar);
item.appendChild(this.titlebar);
}
return item;
};
WindowFigure.prototype.setDocumentDirty=function(_440c){
};
WindowFigure.prototype.onDragend=function(){
};
WindowFigure.prototype.onDragstart=function(x,y){
if(this.titlebar===null){
return false;
}
if(this.canDrag===true&&x<parseInt(this.titlebar.style.width)&&y<parseInt(this.titlebar.style.height)){
return true;
}
return false;
};
WindowFigure.prototype.isSelectable=function(){
return false;
};
WindowFigure.prototype.setCanDrag=function(flag){
Figure.prototype.setCanDrag.call(this,flag);
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
WindowFigure.prototype.setWorkflow=function(_4410){
var _4411=this.workflow;
Figure.prototype.setWorkflow.call(this,_4410);
if(_4411!==null){
_4411.removeSelectionListener(this);
}
if(this.workflow!==null){
this.workflow.addSelectionListener(this);
}
};
WindowFigure.prototype.setDimension=function(w,h){
Figure.prototype.setDimension.call(this,w,h);
if(this.titlebar!==null){
this.titlebar.style.width=this.getWidth()+"px";
}
};
WindowFigure.prototype.setTitle=function(title){
this.title=title;
};
WindowFigure.prototype.getMinWidth=function(){
return 50;
};
WindowFigure.prototype.getMinHeight=function(){
return 50;
};
WindowFigure.prototype.isResizeable=function(){
return false;
};
WindowFigure.prototype.setAlpha=function(_4415){
};
WindowFigure.prototype.setBackgroundColor=function(color){
this.bgColor=color;
if(this.bgColor!==null){
this.html.style.backgroundColor=this.bgColor.getHTMLStyle();
}else{
this.html.style.backgroundColor="transparent";
this.html.style.backgroundImage="";
}
};
WindowFigure.prototype.setColor=function(color){
this.lineColor=color;
if(this.lineColor!==null){
this.html.style.border=this.lineStroke+"px solid "+this.lineColor.getHTMLStyle();
}else{
this.html.style.border="0px";
}
};
WindowFigure.prototype.setLineWidth=function(w){
this.lineStroke=w;
this.html.style.border=this.lineStroke+"px solid black";
};
WindowFigure.prototype.onSelectionChanged=function(_4419,model){
};
Button=function(_47a3,width,_47a5){
this.x=0;
this.y=0;
this.width=24;
this.height=24;
this.id=UUID.create();
this.enabled=true;
this.active=false;
this.palette=_47a3;
this.html=this.createHTMLElement();
if(width!==undefined&&_47a5!==undefined){
this.setDimension(width,_47a5);
}else{
this.setDimension(24,24);
}
};
Button.prototype.type="Button";
Button.prototype.dispose=function(){
};
Button.prototype.getImageUrl=function(){
return this.type+".png";
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
oThis.palette.setActiveTool(null);
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
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
Button.prototype.execute=function(){
};
Button.prototype.setTooltip=function(_47aa){
this.tooltip=_47aa;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
Button.prototype.getWorkflow=function(){
return this.getToolPalette().getWorkflow();
};
Button.prototype.getToolPalette=function(){
return this.palette;
};
Button.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
this.active=flag;
if(flag===true){
this.html.style.border="1px inset";
}else{
this.html.style.border="0px";
}
};
Button.prototype.isActive=function(){
return this.active;
};
Button.prototype.setEnabled=function(flag){
this.enabled=flag;
if(flag){
this.html.style.filter="alpha(opacity=100)";
this.html.style.opacity="1.0";
}else{
this.html.style.filter="alpha(opacity=30)";
this.html.style.opacity="0.3";
}
};
Button.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
Button.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
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
ToggleButton=function(_3f8b){
Button.call(this,_3f8b);
this.isDownFlag=false;
};
ToggleButton.prototype=new Button();
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
if(this.getImageUrl()!==null){
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
ToolGeneric=function(_3e4e){
this.x=0;
this.y=0;
this.enabled=true;
this.tooltip=null;
this.palette=_3e4e;
this.html=this.createHTMLElement();
this.setDimension(10,10);
};
ToolGeneric.prototype.type="ToolGeneric";
ToolGeneric.prototype.dispose=function(){
};
ToolGeneric.prototype.getImageUrl=function(){
return this.type+".png";
};
ToolGeneric.prototype.getWorkflow=function(){
return this.getToolPalette().getWorkflow();
};
ToolGeneric.prototype.getToolPalette=function(){
return this.palette;
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
if(this.tooltip!==null){
item.title=this.tooltip;
}else{
item.title="";
}
return item;
};
ToolGeneric.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
}
return this.html;
};
ToolGeneric.prototype.execute=function(x,y){
if(this.enabled){
this.palette.setActiveTool(null);
}
};
ToolGeneric.prototype.setTooltip=function(_3e54){
this.tooltip=_3e54;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
ToolGeneric.prototype.setActive=function(flag){
if(!this.enabled){
return;
}
if(flag===true){
this.html.style.border="1px inset";
}else{
this.html.style.border="0px";
}
};
ToolGeneric.prototype.setEnabled=function(flag){
this.enabled=flag;
if(flag){
this.html.style.filter="alpha(opacity=100)";
this.html.style.opacity="1.0";
}else{
this.html.style.filter="alpha(opacity=30)";
this.html.style.opacity="0.3";
}
};
ToolGeneric.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
ToolGeneric.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
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
WindowFigure.call(this,title);
this.setDimension(75,400);
this.activeTool=null;
this.children={};
};
ToolPalette.prototype=new WindowFigure();
ToolPalette.prototype.type="ToolPalette";
ToolPalette.prototype.dispose=function(){
WindowFigure.prototype.dispose.call(this);
};
ToolPalette.prototype.createHTMLElement=function(){
var item=WindowFigure.prototype.createHTMLElement.call(this);
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
WindowFigure.prototype.setDimension.call(this,w,h);
if(this.scrollarea!==null){
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
if(this.activeTool!=tool&&this.activeTool!==null){
this.activeTool.setActive(false);
}
if(tool!==null){
tool.setActive(true);
}
this.activeTool=tool;
};
Dialog=function(title){
this.buttonbar=null;
if(title){
WindowFigure.call(this,title);
}else{
WindowFigure.call(this,"Dialog");
}
this.setDimension(400,300);
};
Dialog.prototype=new WindowFigure();
Dialog.prototype.type="Dialog";
Dialog.prototype.createHTMLElement=function(){
var item=WindowFigure.prototype.createHTMLElement.call(this);
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
Dialog.prototype.onOk=function(){
};
Dialog.prototype.onCancel=function(){
};
Dialog.prototype.setDimension=function(w,h){
WindowFigure.prototype.setDimension.call(this,w,h);
if(this.buttonbar!==null){
this.buttonbar.style.width=this.getWidth()+"px";
}
};
Dialog.prototype.setWorkflow=function(_464e){
WindowFigure.prototype.setWorkflow.call(this,_464e);
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
InputDialog.prototype=new Dialog();
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
PropertyDialog=function(_386c,_386d,label){
this.figure=_386c;
this.propertyName=_386d;
this.label=label;
Dialog.call(this);
this.setDimension(400,120);
};
PropertyDialog.prototype=new Dialog();
PropertyDialog.prototype.type="PropertyDialog";
PropertyDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _3870=document.createElement("form");
_3870.style.position="absolute";
_3870.style.left="10px";
_3870.style.top="30px";
_3870.style.width="375px";
_3870.style.font="normal 10px verdana";
item.appendChild(_3870);
this.labelDiv=document.createElement("div");
this.labelDiv.innerHTML=this.label;
this.disableTextSelection(this.labelDiv);
_3870.appendChild(this.labelDiv);
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
_3870.appendChild(this.input);
this.input.focus();
return item;
};
PropertyDialog.prototype.onOk=function(){
Dialog.prototype.onOk.call(this);
this.figure.setProperty(this.propertyName,this.input.value);
};
AnnotationDialog=function(_371f){
this.figure=_371f;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog();
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _3721=document.createElement("form");
_3721.style.position="absolute";
_3721.style.left="10px";
_3721.style.top="30px";
_3721.style.width="375px";
_3721.style.font="normal 10px verdana";
item.appendChild(_3721);
this.label=document.createTextNode("Text");
_3721.appendChild(this.label);
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
_3721.appendChild(this.input);
this.input.focus();
return item;
};
AnnotationDialog.prototype.onOk=function(){
this.workflow.getCommandStack().execute(new CommandSetText(this.figure,this.input.value));
this.workflow.removeFigure(this);
};
PropertyWindow=function(){
this.currentSelection=null;
WindowFigure.call(this,"Property Window");
this.setDimension(200,100);
};
PropertyWindow.prototype=new WindowFigure();
PropertyWindow.prototype.type="PropertyWindow";
PropertyWindow.prototype.dispose=function(){
WindowFigure.prototype.dispose.call(this);
};
PropertyWindow.prototype.createHTMLElement=function(){
var item=WindowFigure.prototype.createHTMLElement.call(this);
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
PropertyWindow.prototype.onSelectionChanged=function(_362c){
WindowFigure.prototype.onSelectionChanged.call(this,_362c);
if(this.currentSelection!==null){
this.currentSelection.detachMoveListener(this);
}
this.currentSelection=_362c;
if(_362c!==null&&_362c!=this){
this.labelType.innerHTML=_362c.type;
if(_362c.getX){
this.labelX.innerHTML=_362c.getX();
this.labelY.innerHTML=_362c.getY();
this.labelWidth.innerHTML=_362c.getWidth();
this.labelHeight.innerHTML=_362c.getHeight();
this.currentSelection=_362c;
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
PropertyWindow.prototype.onOtherFigureMoved=function(_362d){
if(_362d==this.currentSelection){
this.onSelectionChanged(_362d);
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
ColorDialog.prototype=new Dialog();
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
if(color===null){
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
LineColorDialog=function(_3635){
ColorDialog.call(this);
this.figure=_3635;
var color=_3635.getColor();
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
};
LineColorDialog.prototype=new ColorDialog();
LineColorDialog.prototype.type="LineColorDialog";
LineColorDialog.prototype.onOk=function(){
var _3637=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setColor=="function"){
_3637.getCommandStack().execute(new CommandSetColor(this.figure,this.getSelectedColor()));
if(_3637.getCurrentSelection()==this.figure){
_3637.setCurrentSelection(this.figure);
}
}
};
BackgroundColorDialog=function(_3719){
ColorDialog.call(this);
this.figure=_3719;
var color=_3719.getBackgroundColor();
if(color!==null){
this.updateH(this.rgb2hex(color.getRed(),color.getGreen(),color.getBlue()));
}
};
BackgroundColorDialog.prototype=new ColorDialog();
BackgroundColorDialog.prototype.type="BackgroundColorDialog";
BackgroundColorDialog.prototype.onOk=function(){
var _371b=this.workflow;
ColorDialog.prototype.onOk.call(this);
if(typeof this.figure.setBackgroundColor=="function"){
_371b.getCommandStack().execute(new CommandSetBackgroundColor(this.figure,this.getSelectedColor()));
if(_371b.getCurrentSelection()==this.figure){
_371b.setCurrentSelection(this.figure);
}
}
};
AnnotationDialog=function(_371f){
this.figure=_371f;
Dialog.call(this);
this.setDimension(400,100);
};
AnnotationDialog.prototype=new Dialog();
AnnotationDialog.prototype.type="AnnotationDialog";
AnnotationDialog.prototype.createHTMLElement=function(){
var item=Dialog.prototype.createHTMLElement.call(this);
var _3721=document.createElement("form");
_3721.style.position="absolute";
_3721.style.left="10px";
_3721.style.top="30px";
_3721.style.width="375px";
_3721.style.font="normal 10px verdana";
item.appendChild(_3721);
this.label=document.createTextNode("Text");
_3721.appendChild(this.label);
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
_3721.appendChild(this.input);
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
return this.label;
};
Command.prototype.canExecute=function(){
return true;
};
Command.prototype.execute=function(){
};
Command.prototype.cancel=function(){
};
Command.prototype.undo=function(){
};
Command.prototype.redo=function(){
};
CommandStack=function(){
this.undostack=[];
this.redostack=[];
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
this.undostack=[];
this.redostack=[];
};
CommandStack.prototype.execute=function(_39a7){
if(_39a7===null){
return;
}
if(_39a7.canExecute()==false){
return;
}
this.notifyListeners(_39a7,CommandStack.PRE_EXECUTE);
this.undostack.push(_39a7);
_39a7.execute();
this.redostack=[];
if(this.undostack.length>this.maxundo){
this.undostack=this.undostack.slice(this.undostack.length-this.maxundo);
}
this.notifyListeners(_39a7,CommandStack.POST_EXECUTE);
};
CommandStack.prototype.undo=function(){
var _39a8=this.undostack.pop();
if(_39a8){
this.notifyListeners(_39a8,CommandStack.PRE_UNDO);
this.redostack.push(_39a8);
_39a8.undo();
this.notifyListeners(_39a8,CommandStack.POST_UNDO);
}
};
CommandStack.prototype.redo=function(){
var _39a9=this.redostack.pop();
if(_39a9){
this.notifyListeners(_39a9,CommandStack.PRE_REDO);
this.undostack.push(_39a9);
_39a9.redo();
this.notifyListeners(_39a9,CommandStack.POST_REDO);
}
};
CommandStack.prototype.canRedo=function(){
return this.redostack.length>0;
};
CommandStack.prototype.canUndo=function(){
return this.undostack.length>0;
};
CommandStack.prototype.addCommandStackEventListener=function(_39aa){
this.eventListeners.add(_39aa);
};
CommandStack.prototype.removeCommandStackEventListener=function(_39ab){
this.eventListeners.remove(_39ab);
};
CommandStack.prototype.notifyListeners=function(_39ac,state){
var event=new CommandStackEvent(_39ac,state);
var size=this.eventListeners.getSize();
for(var i=0;i<size;i++){
this.eventListeners.get(i).stackChanged(event);
}
};
CommandStackEvent=function(_3dda,_3ddb){
this.command=_3dda;
this.details=_3ddb;
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
CommandAdd=function(_4424,_4425,x,y,_4428){
Command.call(this,"add figure");
if(_4428===undefined){
_4428=null;
}
this.parent=_4428;
this.figure=_4425;
this.x=x;
this.y=y;
this.workflow=_4424;
};
CommandAdd.prototype=new Command();
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
if(this.parent!==null){
this.parent.addChild(this.figure);
}
};
CommandAdd.prototype.undo=function(){
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.parent!==null){
this.parent.removeChild(this.figure);
}
};
CommandDelete=function(_4655){
Command.call(this,"delete figure");
this.parent=_4655.parent;
this.figure=_4655;
this.workflow=_4655.workflow;
this.connections=null;
this.compartmentDeleteCommands=null;
};
CommandDelete.prototype=new Command();
CommandDelete.prototype.type="CommandDelete";
CommandDelete.prototype.execute=function(){
this.redo();
};
CommandDelete.prototype.undo=function(){
if(this.figure instanceof CompartmentFigure){
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
var _4657=this.compartmentDeleteCommands.get(i);
this.figure.addChild(_4657.figure);
this.workflow.getCommandStack().undo();
}
}
this.workflow.addFigure(this.figure);
if(this.figure instanceof Connection){
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
CommandDelete.prototype.redo=function(){
if(this.figure instanceof CompartmentFigure){
if(this.compartmentDeleteCommands===null){
this.compartmentDeleteCommands=new ArrayList();
var _4658=this.figure.getChildren().clone();
for(var i=0;i<_4658.getSize();i++){
var child=_4658.get(i);
this.figure.removeChild(child);
var _465b=new CommandDelete(child);
this.compartmentDeleteCommands.add(_465b);
this.workflow.getCommandStack().execute(_465b);
}
}else{
for(var i=0;i<this.compartmentDeleteCommands.getSize();i++){
this.workflow.redo();
}
}
}
this.workflow.removeFigure(this.figure);
this.workflow.setCurrentSelection(null);
if(this.figure instanceof Node&&this.connections===null){
this.connections=new ArrayList();
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
this.connections=new ArrayList();
}
if(this.parent!==null){
this.parent.removeChild(this.figure);
}
for(var i=0;i<this.connections.getSize();++i){
this.workflow.removeFigure(this.connections.get(i));
}
};
CommandMove=function(_315b,x,y){
Command.call(this,"move figure");
this.figure=_315b;
if(x==undefined){
this.oldX=_315b.getX();
this.oldY=_315b.getY();
}else{
this.oldX=x;
this.oldY=y;
}
this.oldCompartment=_315b.getParent();
};
CommandMove.prototype=new Command();
CommandMove.prototype.type="CommandMove";
CommandMove.prototype.setStartPosition=function(x,y){
this.oldX=x;
this.oldY=y;
};
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
if(this.newCompartment!==null){
this.newCompartment.removeChild(this.figure);
}
if(this.oldCompartment!==null){
this.oldCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandMove.prototype.redo=function(){
this.figure.setPosition(this.newX,this.newY);
if(this.oldCompartment!==null){
this.oldCompartment.removeChild(this.figure);
}
if(this.newCompartment!==null){
this.newCompartment.addChild(this.figure);
}
this.figure.workflow.moveResizeHandles(this.figure);
};
CommandResize=function(_3fb5,width,_3fb7){
Command.call(this,"resize figure");
this.figure=_3fb5;
if(width===undefined){
this.oldWidth=_3fb5.getWidth();
this.oldHeight=_3fb5.getHeight();
}else{
this.oldWidth=width;
this.oldHeight=_3fb7;
}
};
CommandResize.prototype=new Command();
CommandResize.prototype.type="CommandResize";
CommandResize.prototype.setDimension=function(width,_3fb9){
this.newWidth=width;
this.newHeight=_3fb9;
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
CommandSetText=function(_42cc,text){
Command.call(this,"set text");
this.figure=_42cc;
this.newText=text;
this.oldText=_42cc.getText();
};
CommandSetText.prototype=new Command();
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
CommandSetColor=function(_43f1,color){
Command.call(this,"set color");
this.figure=_43f1;
this.newColor=color;
this.oldColor=_43f1.getColor();
};
CommandSetColor.prototype=new Command();
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
CommandSetBackgroundColor=function(_464f,color){
Command.call(this,"set background color");
this.figure=_464f;
this.newColor=color;
this.oldColor=_464f.getBackgroundColor();
};
CommandSetBackgroundColor.prototype=new Command();
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
CommandConnect=function(_465f,_4660,_4661){
Command.call(this,"create connection");
this.workflow=_465f;
this.source=_4660;
this.target=_4661;
this.connection=null;
};
CommandConnect.prototype=new Command();
CommandConnect.prototype.type="CommandConnect";
CommandConnect.prototype.setConnection=function(_4662){
this.connection=_4662;
};
CommandConnect.prototype.execute=function(){
if(this.connection===null){
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
this.con.setRouter(new NullConnectionRouter());
};
CommandReconnect.prototype=new Command();
CommandReconnect.prototype.type="CommandReconnect";
CommandReconnect.prototype.canExecute=function(){
return true;
};
CommandReconnect.prototype.setNewPorts=function(_3e41,_3e42){
this.newSourcePort=_3e41;
this.newTargetPort=_3e42;
};
CommandReconnect.prototype.execute=function(){
this.redo();
};
CommandReconnect.prototype.cancel=function(){
var start=this.con.sourceAnchor.getLocation(this.con.targetAnchor.getReferencePoint());
var end=this.con.targetAnchor.getLocation(this.con.sourceAnchor.getReferencePoint());
this.con.setStartPoint(start.x,start.y);
this.con.setEndPoint(end.x,end.y);
this.con.getWorkflow().showLineResizeHandles(this.con);
this.con.setRouter(this.oldRouter);
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
CommandMoveLine=function(line,_3c11,_3c12,endX,endY){
Command.call(this,"move line");
this.line=line;
this.startX1=_3c11;
this.startY1=_3c12;
this.endX1=endX;
this.endY1=endY;
};
CommandMoveLine.prototype=new Command();
CommandMoveLine.prototype.type="CommandMoveLine";
CommandMoveLine.prototype.canExecute=function(){
return this.startX1!=this.startX2||this.startY1!=this.startY2||this.endX1!=this.endX2||this.endY1!=this.endY2;
};
CommandMoveLine.prototype.execute=function(){
this.startX2=this.line.getStartX();
this.startY2=this.line.getStartY();
this.endX2=this.line.getEndX();
this.endY2=this.line.getEndY();
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
CommandMovePort=function(port){
Command.call(this,"move port");
this.port=port;
};
CommandMovePort.prototype=new Command();
CommandMovePort.prototype.type="CommandMovePort";
CommandMovePort.prototype.execute=function(){
this.port.setAlpha(1);
this.port.setPosition(this.port.originX,this.port.originY);
this.port.parentNode.workflow.hideConnectionLine();
};
CommandMovePort.prototype.undo=function(){
};
CommandMovePort.prototype.redo=function(){
};
CommandMovePort.prototype.setPosition=function(x,y){
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
Menu.prototype=new Figure();
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
item.style.width="auto";
item.style.height="auto";
item.className="Menu";
return item;
};
Menu.prototype.setWorkflow=function(_4601){
this.workflow=_4601;
};
Menu.prototype.setDimension=function(w,h){
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
li.style.whiteSpace="nowrap";
li.style.cursor="pointer";
li.className="MenuItem";
this.html.appendChild(li);
li.menuItem=item;
if(li.addEventListener){
li.addEventListener("click",function(event){
var _460b=arguments[0]||window.event;
_460b.cancelBubble=true;
_460b.returnValue=false;
var diffX=_460b.clientX;
var diffY=_460b.clientY;
var _460e=document.body.parentNode.scrollLeft;
var _460f=document.body.parentNode.scrollTop;
this.menuItem.execute(diffX+_460e,diffY+_460f);
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
var _4615=arguments[0]||window.event;
_4615.cancelBubble=true;
_4615.returnValue=false;
var diffX=_4615.clientX;
var diffY=_4615.clientY;
var _4618=document.body.parentNode.scrollLeft;
var _4619=document.body.parentNode.scrollTop;
event.srcElement.menuItem.execute(diffX+_4618,diffY+_4619);
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
MenuItem=function(label,_3d6d,_3d6e){
this.label=label;
this.iconUrl=_3d6d;
this.parentMenu=null;
this.action=_3d6e;
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
Locator.prototype.relocate=function(_42cf){
};
ConnectionLocator=function(_4646){
Locator.call(this);
this.connection=_4646;
};
ConnectionLocator.prototype=new Locator;
ConnectionLocator.prototype.type="ConnectionLocator";
ConnectionLocator.prototype.getConnection=function(){
return this.connection;
};
ManhattanMidpointLocator=function(_441c){
ConnectionLocator.call(this,_441c);
};
ManhattanMidpointLocator.prototype=new ConnectionLocator;
ManhattanMidpointLocator.prototype.type="ManhattanMidpointLocator";
ManhattanMidpointLocator.prototype.relocate=function(_441d){
var conn=this.getConnection();
var p=new Point();
var _4420=conn.getPoints();
var index=Math.floor((_4420.getSize()-2)/2);
if(_4420.getSize()<=index+1){
return;
}
var p1=_4420.get(index);
var p2=_4420.get(index+1);
p.x=(p2.x-p1.x)/2+p1.x+5;
p.y=(p2.y-p1.y)/2+p1.y+5;
_441d.setPosition(p.x,p.y);
};
EditPartFactory=function(){
};
EditPartFactory.prototype.type="EditPartFactory";
EditPartFactory.prototype.createEditPart=function(model){
};
AbstractObjectModel=function(){
this.listeners=new ArrayList();
this.id=UUID.create();
};
AbstractObjectModel.EVENT_ELEMENT_ADDED="element added";
AbstractObjectModel.EVENT_ELEMENT_REMOVED="element removed";
AbstractObjectModel.EVENT_CONNECTION_ADDED="connection addedx";
AbstractObjectModel.EVENT_CONNECTION_REMOVED="connection removed";
AbstractObjectModel.prototype.type="AbstractObjectModel";
AbstractObjectModel.prototype.getModelChildren=function(){
return new ArrayList();
};
AbstractObjectModel.prototype.getModelParent=function(){
return this.modelParent;
};
AbstractObjectModel.prototype.setModelParent=function(_3e5d){
this.modelParent=_3e5d;
};
AbstractObjectModel.prototype.getId=function(){
return this.id;
};
AbstractObjectModel.prototype.firePropertyChange=function(_3e5e,_3e5f,_3e60){
var count=this.listeners.getSize();
if(count===0){
return;
}
var event=new PropertyChangeEvent(this,_3e5e,_3e5f,_3e60);
for(var i=0;i<count;i++){
try{
this.listeners.get(i).propertyChange(event);
}
catch(e){
alert("Method: AbstractObjectModel.prototype.firePropertyChange\n"+e+"\nProperty: "+_3e5e+"\nListener Class:"+this.listeners.get(i).type);
}
}
};
AbstractObjectModel.prototype.addPropertyChangeListener=function(_3e64){
if(_3e64!==null){
this.listeners.add(_3e64);
}
};
AbstractObjectModel.prototype.removePropertyChangeListener=function(_3e65){
if(_3e65!==null){
this.listeners.remove(_3e65);
}
};
AbstractObjectModel.prototype.getPersistentAttributes=function(){
return {id:this.id};
};
AbstractConnectionModel=function(){
AbstractObjectModel.call(this);
};
AbstractConnectionModel.prototype=new AbstractObjectModel();
AbstractConnectionModel.prototype.type="AbstractConnectionModel";
AbstractConnectionModel.prototype.getSourceModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourceModel]";
};
AbstractConnectionModel.prototype.getTargetModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetModel]";
};
AbstractConnectionModel.prototype.getSourcePortName=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourcePortName]";
};
AbstractConnectionModel.prototype.getTargetPortName=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetPortName]";
};
AbstractConnectionModel.prototype.getSourcePortModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getSourcePortModel]";
};
AbstractConnectionModel.prototype.getTargetPortModel=function(){
throw "you must override the method [AbstractConnectionModel.prototype.getTargetPortModel]";
};
PropertyChangeEvent=function(model,_3b80,_3b81,_3b82){
this.model=model;
this.property=_3b80;
this.oldValue=_3b81;
this.newValue=_3b82;
};
PropertyChangeEvent.prototype.type="PropertyChangeEvent";
GraphicalViewer=function(id){
try{
Workflow.call(this,id);
this.factory=null;
this.model=null;
this.initDone=false;
}
catch(e){
pushErrorStack(e,"GraphicalViewer=function(/*:String*/ id)");
}
};
GraphicalViewer.prototype=new Workflow();
GraphicalViewer.prototype.type="GraphicalViewer";
GraphicalViewer.prototype.setEditPartFactory=function(_3f92){
this.factory=_3f92;
this.checkInit();
};
GraphicalViewer.prototype.setModel=function(model){
try{
if(model instanceof AbstractObjectModel){
this.model=model;
this.checkInit();
this.model.addPropertyChangeListener(this);
}else{
alert("Invalid model class type:"+model.type);
}
}
catch(e){
pushErrorStack(e,"GraphicalViewer.prototype.setModel=function(/*:AbstractObjectModel*/ model )");
}
};
GraphicalViewer.prototype.propertyChange=function(event){
switch(event.property){
case AbstractObjectModel.EVENT_ELEMENT_REMOVED:
var _3f95=this.getFigure(event.oldValue.getId());
this.removeFigure(_3f95);
break;
case AbstractObjectModel.EVENT_ELEMENT_ADDED:
var _3f95=this.factory.createEditPart(event.newValue);
_3f95.setId(event.newValue.getId());
this.addFigure(_3f95);
this.setCurrentSelection(_3f95);
break;
}
};
GraphicalViewer.prototype.checkInit=function(){
if(this.factory!==null&&this.model!==null&&this.initDone==false){
try{
var _3f96=this.model.getModelChildren();
var count=_3f96.getSize();
for(var i=0;i<count;i++){
var child=_3f96.get(i);
var _3f9a=this.factory.createEditPart(child);
_3f9a.setId(child.getId());
this.addFigure(_3f9a);
}
}
catch(e){
pushErrorStack(e,"GraphicalViewer.prototype.checkInit=function()[addFigures]");
}
try{
var _3f9b=this.getDocument().getFigures();
var count=_3f9b.getSize();
for(var i=0;i<count;i++){
var _3f9a=_3f9b.get(i);
if(_3f9a instanceof Node){
this.refreshConnections(_3f9a);
}
}
}
catch(e){
pushErrorStack(e,"GraphicalViewer.prototype.checkInit=function()[refreshConnections]");
}
}
};
GraphicalViewer.prototype.refreshConnections=function(node){
try{
var _3f9d=new ArrayList();
var _3f9e=node.getModelSourceConnections();
var count=_3f9e.getSize();
for(var i=0;i<count;i++){
var _3fa1=_3f9e.get(i);
_3f9d.add(_3fa1.getId());
var _3fa2=this.getLine(_3fa1.getId());
if(_3fa2===null){
_3fa2=this.factory.createEditPart(_3fa1);
var _3fa3=_3fa1.getSourceModel();
var _3fa4=_3fa1.getTargetModel();
var _3fa5=this.getFigure(_3fa3.getId());
var _3fa6=this.getFigure(_3fa4.getId());
var _3fa7=_3fa5.getOutputPort(_3fa1.getSourcePortName());
var _3fa8=_3fa6.getInputPort(_3fa1.getTargetPortName());
_3fa2.setTarget(_3fa8);
_3fa2.setSource(_3fa7);
_3fa2.setId(_3fa1.getId());
this.addFigure(_3fa2);
this.setCurrentSelection(_3fa2);
}
}
var ports=node.getOutputPorts();
count=ports.getSize();
for(var i=0;i<count;i++){
var _3faa=ports.get(i).getConnections();
var _3fab=_3faa.getSize();
for(var ii=0;ii<_3fab;ii++){
var _3fad=_3faa.get(ii);
if(!_3f9d.contains(_3fad.getId())){
this.removeFigure(_3fad);
_3f9d.add(_3fad.getId());
}
}
}
}
catch(e){
pushErrorStack(e,"GraphicalViewer.prototype.refreshConnections=function(/*:Node*/ node )");
}
};
GraphicalEditor=function(id){
try{
this.view=new GraphicalViewer(id);
this.initializeGraphicalViewer();
}
catch(e){
pushErrorStack(e,"GraphicalEditor=function(/*:String*/ id)");
}
};
GraphicalEditor.prototype.type="GraphicalEditor";
GraphicalEditor.prototype.initializeGraphicalViewer=function(){
};
GraphicalEditor.prototype.getGraphicalViewer=function(){
return this.view;
};
var whitespace="\n\r\t ";
XMLP=function(_3b10){
_3b10=SAXStrings.replace(_3b10,null,null,"\r\n","\n");
_3b10=SAXStrings.replace(_3b10,null,null,"\r","\n");
this.m_xml=_3b10;
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
XMLP.prototype._checkStructure=function(_3b13){
if(XMLP._STATE_PROLOG==this.m_iState){
if((XMLP._TEXT==_3b13)||(XMLP._ENTITY==_3b13)){
if(SAXStrings.indexOfNonWhitespace(this.getContent(),this.getContentBegin(),this.getContentEnd())!=-1){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
}
if((XMLP._ELM_B==_3b13)||(XMLP._ELM_EMP==_3b13)){
this.m_iState=XMLP._STATE_DOCUMENT;
}
}
if(XMLP._STATE_DOCUMENT==this.m_iState){
if((XMLP._ELM_B==_3b13)||(XMLP._ELM_EMP==_3b13)){
this.m_stack.push(this.getName());
}
if((XMLP._ELM_E==_3b13)||(XMLP._ELM_EMP==_3b13)){
var _3b14=this.m_stack.pop();
if((_3b14===null)||(_3b14!=this.getName())){
return this._setErr(XMLP.ERR_ELM_NESTING);
}
}
if(this.m_stack.count()===0){
this.m_iState=XMLP._STATE_MISC;
return _3b13;
}
}
if(XMLP._STATE_MISC==this.m_iState){
if((XMLP._ELM_B==_3b13)||(XMLP._ELM_E==_3b13)||(XMLP._ELM_EMP==_3b13)||(XMLP.EVT_DTD==_3b13)){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
if((XMLP._TEXT==_3b13)||(XMLP._ENTITY==_3b13)){
if(SAXStrings.indexOfNonWhitespace(this.getContent(),this.getContentBegin(),this.getContentEnd())!=-1){
return this._setErr(XMLP.ERR_DOC_STRUCTURE);
}
}
}
return _3b13;
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
var _3b1d,strN,strV;
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
_3b1d=this.m_xml.charAt(iVB);
if(SAXStrings.QUOTES.indexOf(_3b1d)==-1){
return this._setErr(XMLP.ERR_ATT_VALUES);
}
iVE=this.m_xml.indexOf(_3b1d,iVB+1);
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
var _3b37=XMLP._errs[iErr];
this.m_cAlt=_3b37;
this.m_cB=0;
this.m_cE=_3b37.length;
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
var _3b39=new XMLP(strD);
if(this.m_hndDoc&&this.m_hndDoc.setDocumentLocator){
this.m_hndDoc.setDocumentLocator(this);
}
this.m_parser=_3b39;
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
SAXDriver.prototype._fireError=function(_3b40){
this.m_strErrMsg=_3b40;
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
SAXDriver.prototype._parseLoop=function(_3b43){
var _3b44,_3b43;
_3b43=this.m_parser;
while(!this.m_bErr){
_3b44=_3b43.next();
if(_3b44==XMLP._ELM_B){
this._fireEvent(SAXDriver.ELM_B,_3b43.getName(),this);
}else{
if(_3b44==XMLP._ELM_E){
this._fireEvent(SAXDriver.ELM_E,_3b43.getName());
}else{
if(_3b44==XMLP._ELM_EMP){
this._fireEvent(SAXDriver.ELM_B,_3b43.getName(),this);
this._fireEvent(SAXDriver.ELM_E,_3b43.getName());
}else{
if(_3b44==XMLP._TEXT){
this._fireEvent(SAXDriver.CHARS,_3b43.getContent(),_3b43.getContentBegin(),_3b43.getContentEnd()-_3b43.getContentBegin());
}else{
if(_3b44==XMLP._ENTITY){
this._fireEvent(SAXDriver.CHARS,_3b43.getContent(),_3b43.getContentBegin(),_3b43.getContentEnd()-_3b43.getContentBegin());
}else{
if(_3b44==XMLP._PI){
this._fireEvent(SAXDriver.PI,_3b43.getName(),_3b43.getContent().substring(_3b43.getContentBegin(),_3b43.getContentEnd()));
}else{
if(_3b44==XMLP._CDATA){
this._fireEvent(SAXDriver.CD_B);
this._fireEvent(SAXDriver.CHARS,_3b43.getContent(),_3b43.getContentBegin(),_3b43.getContentEnd()-_3b43.getContentBegin());
this._fireEvent(SAXDriver.CD_E);
}else{
if(_3b44==XMLP._COMMENT){
this._fireEvent(SAXDriver.CMNT,_3b43.getContent(),_3b43.getContentBegin(),_3b43.getContentEnd()-_3b43.getContentBegin());
}else{
if(_3b44==XMLP._DTD){
}else{
if(_3b44==XMLP._ERROR){
this._fireError(_3b43.getContent());
}else{
if(_3b44==XMLP._NONE){
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
var _3b48=arrD[arrD.length-1];
arrD.length--;
var _3b49=arrD.join("\n").length;
return iP-_3b49;
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
function trim(_3b61,_3b62,_3b63){
if(isEmpty(_3b61)){
return "";
}
if(_3b62===null){
_3b62=true;
}
if(_3b63===null){
_3b63=true;
}
var left=0;
var right=0;
var i=0;
var k=0;
if(_3b62==true){
while((i<_3b61.length)&&(whitespace.indexOf(_3b61.charAt(i++))!=-1)){
left++;
}
}
if(_3b63==true){
k=_3b61.length-1;
while((k>=left)&&(whitespace.indexOf(_3b61.charAt(k--))!=-1)){
right++;
}
}
return _3b61.substring(left,_3b61.length-right);
}
function __escapeString(str){
var _3b69=/&/g;
var _3b6a=/</g;
var _3b6b=/>/g;
var _3b6c=/"/g;
var _3b6d=/'/g;
str=str.replace(_3b69,"&amp;");
str=str.replace(_3b6a,"&lt;");
str=str.replace(_3b6b,"&gt;");
str=str.replace(_3b6c,"&quot;");
str=str.replace(_3b6d,"&apos;");
return str;
}
function __unescapeString(str){
var _3b6f=/&amp;/g;
var _3b70=/&lt;/g;
var _3b71=/&gt;/g;
var _3b72=/&quot;/g;
var _3b73=/&apos;/g;
str=str.replace(_3b6f,"&");
str=str.replace(_3b70,"<");
str=str.replace(_3b71,">");
str=str.replace(_3b72,"\"");
str=str.replace(_3b73,"'");
return str;
}
function addClass(_467f,_4680){
if(_467f){
if(_467f.indexOf("|"+_4680+"|")<0){
_467f+=_4680+"|";
}
}else{
_467f="|"+_4680+"|";
}
return _467f;
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
DOMImplementation.prototype.hasFeature=function DOMImplementation_hasFeature(_4684,_4685){
var ret=false;
if(_4684.toLowerCase()=="xml"){
ret=(!_4685||(_4685=="1.0")||(_4685=="2.0"));
}else{
if(_4684.toLowerCase()=="core"){
ret=(!_4685||(_4685=="2.0"));
}
}
return ret;
};
DOMImplementation.prototype.loadXML=function DOMImplementation_loadXML(_4687){
var _4688;
try{
_4688=new XMLP(_4687);
}
catch(e){
alert("Error Creating the SAX Parser. Did you include xmlsax.js or tinyxmlsax.js in your web page?\nThe SAX parser is needed to populate XML for <SCRIPT>'s W3C DOM Parser with data.");
}
var doc=new DOMDocument(this);
this._parseLoop(doc,_4688);
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
var _468f=0;
var _4690=[];
var _4691=[];
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
var _4695=this._parseNSName(strName);
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
var _4695=this._parseNSName(strName);
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
var _4696=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
if(trim(_4696,true,true)==""){
_4696="";
}
}
if(_4696.length>0){
var _4697=doc.createTextNode(_4696);
iNodeParent.appendChild(_4697);
if(iEvt==XMLP._ENTITY){
_4690[_4690.length]=_4697;
}else{
_4691[_4691.length]=_4697;
}
}
}else{
if(iEvt==XMLP._PI){
iNodeParent.appendChild(doc.createProcessingInstruction(p.getName(),p.getContent().substring(p.getContentBegin(),p.getContentEnd())));
}else{
if(iEvt==XMLP._CDATA){
_4696=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
_4696=trim(_4696,true,true);
_4696.replace(/ +/g," ");
}
if(_4696.length>0){
iNodeParent.appendChild(doc.createCDATASection(_4696));
}
}else{
if(iEvt==XMLP._COMMENT){
var _4696=p.getContent().substring(p.getContentBegin(),p.getContentEnd());
if(!this.preserveWhiteSpace){
_4696=trim(_4696,true,true);
_4696.replace(/ +/g," ");
}
if(_4696.length>0){
iNodeParent.appendChild(doc.createComment(_4696));
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
var _4698=_4690.length;
for(intLoop=0;intLoop<_4698;intLoop++){
var _4699=_4690[intLoop];
var _469a=_4699.getParentNode();
if(_469a){
_469a.normalize();
if(!this.preserveWhiteSpace){
var _469b=_469a.getChildNodes();
var _469c=_469b.getLength();
for(intLoop2=0;intLoop2<_469c;intLoop2++){
var child=_469b.item(intLoop2);
if(child.getNodeType()==DOMNode.TEXT_NODE){
var _469e=child.getData();
_469e=trim(_469e,true,true);
_469e.replace(/ +/g," ");
child.setData(_469e);
}
}
}
}
}
if(!this.preserveWhiteSpace){
var _4698=_4691.length;
for(intLoop=0;intLoop<_4698;intLoop++){
var node=_4691[intLoop];
if(node.getParentNode()!==null){
var _46a0=node.getData();
_46a0=trim(_46a0,true,true);
_46a0.replace(/ +/g," ");
node.setData(_46a0);
}
}
}
};
DOMImplementation.prototype._isNamespaceDeclaration=function DOMImplementation__isNamespaceDeclaration(_46a1){
return (_46a1.indexOf("xmlns")>-1);
};
DOMImplementation.prototype._isIdDeclaration=function DOMImplementation__isIdDeclaration(_46a2){
return (_46a2.toLowerCase()=="id");
};
DOMImplementation.prototype._isValidName=function DOMImplementation__isValidName(name){
return name.match(re_validName);
};
re_validName=/^[a-zA-Z_:][a-zA-Z0-9\.\-_:]*$/;
DOMImplementation.prototype._isValidString=function DOMImplementation__isValidString(name){
return (name.search(re_invalidStringChars)<0);
};
re_invalidStringChars=/\x01|\x02|\x03|\x04|\x05|\x06|\x07|\x08|\x0B|\x0C|\x0E|\x0F|\x10|\x11|\x12|\x13|\x14|\x15|\x16|\x17|\x18|\x19|\x1A|\x1B|\x1C|\x1D|\x1E|\x1F|\x7F/;
DOMImplementation.prototype._parseNSName=function DOMImplementation__parseNSName(_46a5){
var _46a6={};
_46a6.prefix=_46a5;
_46a6.namespaceName="";
delimPos=_46a5.indexOf(":");
if(delimPos>-1){
_46a6.prefix=_46a5.substring(0,delimPos);
_46a6.namespaceName=_46a5.substring(delimPos+1,_46a5.length);
}
return _46a6;
};
DOMImplementation.prototype._parseQName=function DOMImplementation__parseQName(_46a7){
var _46a8={};
_46a8.localName=_46a7;
_46a8.prefix="";
delimPos=_46a7.indexOf(":");
if(delimPos>-1){
_46a8.prefix=_46a7.substring(0,delimPos);
_46a8.localName=_46a7.substring(delimPos+1,_46a7.length);
}
return _46a8;
};
DOMNodeList=function(_46a9,_46aa){
this._class=addClass(this._class,"DOMNodeList");
this._nodes=[];
this.length=0;
this.parentNode=_46aa;
this.ownerDocument=_46a9;
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
DOMNodeList.prototype._insertBefore=function DOMNodeList__insertBefore(_46b0,_46b1){
if((_46b1>=0)&&(_46b1<this._nodes.length)){
var _46b2=[];
_46b2=this._nodes.slice(0,_46b1);
if(_46b0.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
_46b2=_46b2.concat(_46b0.childNodes._nodes);
}else{
_46b2[_46b2.length]=_46b0;
}
this._nodes=_46b2.concat(this._nodes.slice(_46b1));
this.length=this._nodes.length;
}
};
DOMNodeList.prototype._replaceChild=function DOMNodeList__replaceChild(_46b3,_46b4){
var ret=null;
if((_46b4>=0)&&(_46b4<this._nodes.length)){
ret=this._nodes[_46b4];
if(_46b3.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
var _46b6=[];
_46b6=this._nodes.slice(0,_46b4);
_46b6=_46b6.concat(_46b3.childNodes._nodes);
this._nodes=_46b6.concat(this._nodes.slice(_46b4+1));
}else{
this._nodes[_46b4]=_46b3;
}
}
return ret;
};
DOMNodeList.prototype._removeChild=function DOMNodeList__removeChild(_46b7){
var ret=null;
if(_46b7>-1){
ret=this._nodes[_46b7];
var _46b9=[];
_46b9=this._nodes.slice(0,_46b7);
this._nodes=_46b9.concat(this._nodes.slice(_46b7+1));
this.length=this._nodes.length;
}
return ret;
};
DOMNodeList.prototype._appendChild=function DOMNodeList__appendChild(_46ba){
if(_46ba.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
this._nodes=this._nodes.concat(_46ba.childNodes._nodes);
}else{
this._nodes[this._nodes.length]=_46ba;
}
this.length=this._nodes.length;
};
DOMNodeList.prototype._cloneNodes=function DOMNodeList__cloneNodes(deep,_46bc){
var _46bd=new DOMNodeList(this.ownerDocument,_46bc);
for(var i=0;i<this._nodes.length;i++){
_46bd._appendChild(this._nodes[i].cloneNode(deep));
}
return _46bd;
};
DOMNodeList.prototype.toString=function DOMNodeList_toString(){
var ret="";
for(var i=0;i<this.length;i++){
ret+=this._nodes[i].toString();
}
return ret;
};
DOMNamedNodeMap=function(_46c1,_46c2){
this._class=addClass(this._class,"DOMNamedNodeMap");
this.DOMNodeList=DOMNodeList;
this.DOMNodeList(_46c1,_46c2);
};
DOMNamedNodeMap.prototype=new DOMNodeList;
DOMNamedNodeMap.prototype.getNamedItem=function DOMNamedNodeMap_getNamedItem(name){
var ret=null;
var _46c5=this._findNamedItemIndex(name);
if(_46c5>-1){
ret=this._nodes[_46c5];
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
var _46c7=this._findNamedItemIndex(arg.name);
var ret=null;
if(_46c7>-1){
ret=this._nodes[_46c7];
if(this.ownerDocument.implementation.errorChecking&&ret._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}else{
this._nodes[_46c7]=arg;
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
var _46cb=this._findNamedItemIndex(name);
if(this.ownerDocument.implementation.errorChecking&&(_46cb<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _46cc=this._nodes[_46cb];
if(this.ownerDocument.implementation.errorChecking&&_46cc._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
return this._removeChild(_46cb);
};
DOMNamedNodeMap.prototype.getNamedItemNS=function DOMNamedNodeMap_getNamedItemNS(_46cd,_46ce){
var ret=null;
var _46d0=this._findNamedItemNSIndex(_46cd,_46ce);
if(_46d0>-1){
ret=this._nodes[_46d0];
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
var _46d2=this._findNamedItemNSIndex(arg.namespaceURI,arg.localName);
var ret=null;
if(_46d2>-1){
ret=this._nodes[_46d2];
if(this.ownerDocument.implementation.errorChecking&&ret._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}else{
this._nodes[_46d2]=arg;
}
}else{
this._nodes[this.length]=arg;
}
this.length=this._nodes.length;
arg.ownerElement=this.parentNode;
return ret;
};
DOMNamedNodeMap.prototype.removeNamedItemNS=function DOMNamedNodeMap_removeNamedItemNS(_46d4,_46d5){
var ret=null;
if(this.ownerDocument.implementation.errorChecking&&(this._readonly||(this.parentNode&&this.parentNode._readonly))){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _46d7=this._findNamedItemNSIndex(_46d4,_46d5);
if(this.ownerDocument.implementation.errorChecking&&(_46d7<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _46d8=this._nodes[_46d7];
if(this.ownerDocument.implementation.errorChecking&&_46d8._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
return this._removeChild(_46d7);
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
DOMNamedNodeMap.prototype._findNamedItemNSIndex=function DOMNamedNodeMap__findNamedItemNSIndex(_46dc,_46dd){
var ret=-1;
if(_46dd){
for(var i=0;i<this._nodes.length;i++){
if((this._nodes[i].namespaceURI==_46dc)&&(this._nodes[i].localName==_46dd)){
ret=i;
break;
}
}
}
return ret;
};
DOMNamedNodeMap.prototype._hasAttribute=function DOMNamedNodeMap__hasAttribute(name){
var ret=false;
var _46e2=this._findNamedItemIndex(name);
if(_46e2>-1){
ret=true;
}
return ret;
};
DOMNamedNodeMap.prototype._hasAttributeNS=function DOMNamedNodeMap__hasAttributeNS(_46e3,_46e4){
var ret=false;
var _46e6=this._findNamedItemNSIndex(_46e3,_46e4);
if(_46e6>-1){
ret=true;
}
return ret;
};
DOMNamedNodeMap.prototype._cloneNodes=function DOMNamedNodeMap__cloneNodes(_46e7){
var _46e8=new DOMNamedNodeMap(this.ownerDocument,_46e7);
for(var i=0;i<this._nodes.length;i++){
_46e8._appendChild(this._nodes[i].cloneNode(false));
}
return _46e8;
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
DOMNamespaceNodeMap=function(_46ec,_46ed){
this._class=addClass(this._class,"DOMNamespaceNodeMap");
this.DOMNamedNodeMap=DOMNamedNodeMap;
this.DOMNamedNodeMap(_46ec,_46ed);
};
DOMNamespaceNodeMap.prototype=new DOMNamedNodeMap;
DOMNamespaceNodeMap.prototype._findNamedItemIndex=function DOMNamespaceNodeMap__findNamedItemIndex(_46ee){
var ret=-1;
for(var i=0;i<this._nodes.length;i++){
if(this._nodes[i].localName==_46ee){
ret=i;
break;
}
}
return ret;
};
DOMNamespaceNodeMap.prototype._cloneNodes=function DOMNamespaceNodeMap__cloneNodes(_46f1){
var _46f2=new DOMNamespaceNodeMap(this.ownerDocument,_46f1);
for(var i=0;i<this._nodes.length;i++){
_46f2._appendChild(this._nodes[i].cloneNode(false));
}
return _46f2;
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
DOMNode=function(_46f7){
this._class=addClass(this._class,"DOMNode");
if(_46f7){
this._id=_46f7._genId();
}
this.namespaceURI="";
this.prefix="";
this.localName="";
this.nodeName="";
this.nodeValue="";
this.nodeType=0;
this.parentNode=null;
this.childNodes=new DOMNodeList(_46f7,this);
this.firstChild=null;
this.lastChild=null;
this.previousSibling=null;
this.nextSibling=null;
this.attributes=new DOMNamedNodeMap(_46f7,this);
this.ownerDocument=_46f7;
this._namespaces=new DOMNamespaceNodeMap(_46f7,this);
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
DOMNode.prototype.setNodeValue=function DOMNode_setNodeValue(_46f8){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
this.nodeValue=_46f8;
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
DOMNode.prototype.setPrefix=function DOMNode_setPrefix(_46f9){
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_46f9)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
if(!this.ownerDocument._isValidNamespace(this.namespaceURI,_46f9+":"+this.localName)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if((_46f9=="xmlns")&&(this.namespaceURI!="http://www.w3.org/2000/xmlns/")){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if((_46f9=="")&&(this.localName=="xmlns")){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
}
this.prefix=_46f9;
if(this.prefix!=""){
this.nodeName=this.prefix+":"+this.localName;
}else{
this.nodeName=this.localName;
}
};
DOMNode.prototype.getLocalName=function DOMNode_getLocalName(){
return this.localName;
};
DOMNode.prototype.insertBefore=function DOMNode_insertBefore(_46fa,_46fb){
var _46fc;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_46fa.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_46fa)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
if(_46fb){
var _46fd=this.childNodes._findItemIndex(_46fb._id);
if(this.ownerDocument.implementation.errorChecking&&(_46fd<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _46fe=_46fa.parentNode;
if(_46fe){
_46fe.removeChild(_46fa);
}
this.childNodes._insertBefore(_46fa,this.childNodes._findItemIndex(_46fb._id));
_46fc=_46fb.previousSibling;
if(_46fa.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_46fa.childNodes._nodes.length>0){
for(var ind=0;ind<_46fa.childNodes._nodes.length;ind++){
_46fa.childNodes._nodes[ind].parentNode=this;
}
_46fb.previousSibling=_46fa.childNodes._nodes[_46fa.childNodes._nodes.length-1];
}
}else{
_46fa.parentNode=this;
_46fb.previousSibling=_46fa;
}
}else{
_46fc=this.lastChild;
this.appendChild(_46fa);
}
if(_46fa.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_46fa.childNodes._nodes.length>0){
if(_46fc){
_46fc.nextSibling=_46fa.childNodes._nodes[0];
}else{
this.firstChild=_46fa.childNodes._nodes[0];
}
_46fa.childNodes._nodes[0].previousSibling=_46fc;
_46fa.childNodes._nodes[_46fa.childNodes._nodes.length-1].nextSibling=_46fb;
}
}else{
if(_46fc){
_46fc.nextSibling=_46fa;
}else{
this.firstChild=_46fa;
}
_46fa.previousSibling=_46fc;
_46fa.nextSibling=_46fb;
}
return _46fa;
};
DOMNode.prototype.replaceChild=function DOMNode_replaceChild(_4700,_4701){
var ret=null;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_4700.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_4700)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
var index=this.childNodes._findItemIndex(_4701._id);
if(this.ownerDocument.implementation.errorChecking&&(index<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
var _4704=_4700.parentNode;
if(_4704){
_4704.removeChild(_4700);
}
ret=this.childNodes._replaceChild(_4700,index);
if(_4700.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_4700.childNodes._nodes.length>0){
for(var ind=0;ind<_4700.childNodes._nodes.length;ind++){
_4700.childNodes._nodes[ind].parentNode=this;
}
if(_4701.previousSibling){
_4701.previousSibling.nextSibling=_4700.childNodes._nodes[0];
}else{
this.firstChild=_4700.childNodes._nodes[0];
}
if(_4701.nextSibling){
_4701.nextSibling.previousSibling=_4700;
}else{
this.lastChild=_4700.childNodes._nodes[_4700.childNodes._nodes.length-1];
}
_4700.childNodes._nodes[0].previousSibling=_4701.previousSibling;
_4700.childNodes._nodes[_4700.childNodes._nodes.length-1].nextSibling=_4701.nextSibling;
}
}else{
_4700.parentNode=this;
if(_4701.previousSibling){
_4701.previousSibling.nextSibling=_4700;
}else{
this.firstChild=_4700;
}
if(_4701.nextSibling){
_4701.nextSibling.previousSibling=_4700;
}else{
this.lastChild=_4700;
}
_4700.previousSibling=_4701.previousSibling;
_4700.nextSibling=_4701.nextSibling;
}
return ret;
};
DOMNode.prototype.removeChild=function DOMNode_removeChild(_4706){
if(this.ownerDocument.implementation.errorChecking&&(this._readonly||_4706._readonly)){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _4707=this.childNodes._findItemIndex(_4706._id);
if(this.ownerDocument.implementation.errorChecking&&(_4707<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
this.childNodes._removeChild(_4707);
_4706.parentNode=null;
if(_4706.previousSibling){
_4706.previousSibling.nextSibling=_4706.nextSibling;
}else{
this.firstChild=_4706.nextSibling;
}
if(_4706.nextSibling){
_4706.nextSibling.previousSibling=_4706.previousSibling;
}else{
this.lastChild=_4706.previousSibling;
}
_4706.previousSibling=null;
_4706.nextSibling=null;
return _4706;
};
DOMNode.prototype.appendChild=function DOMNode_appendChild(_4708){
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.ownerDocument!=_4708.ownerDocument){
throw (new DOMException(DOMException.WRONG_DOCUMENT_ERR));
}
if(this._isAncestor(_4708)){
throw (new DOMException(DOMException.HIERARCHY_REQUEST_ERR));
}
}
var _4709=_4708.parentNode;
if(_4709){
_4709.removeChild(_4708);
}
this.childNodes._appendChild(_4708);
if(_4708.nodeType==DOMNode.DOCUMENT_FRAGMENT_NODE){
if(_4708.childNodes._nodes.length>0){
for(var ind=0;ind<_4708.childNodes._nodes.length;ind++){
_4708.childNodes._nodes[ind].parentNode=this;
}
if(this.lastChild){
this.lastChild.nextSibling=_4708.childNodes._nodes[0];
_4708.childNodes._nodes[0].previousSibling=this.lastChild;
this.lastChild=_4708.childNodes._nodes[_4708.childNodes._nodes.length-1];
}else{
this.lastChild=_4708.childNodes._nodes[_4708.childNodes._nodes.length-1];
this.firstChild=_4708.childNodes._nodes[0];
}
}
}else{
_4708.parentNode=this;
if(this.lastChild){
this.lastChild.nextSibling=_4708;
_4708.previousSibling=this.lastChild;
this.lastChild=_4708;
}else{
this.lastChild=_4708;
this.firstChild=_4708;
}
}
return _4708;
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
var _470d=new DOMNodeList();
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
var _470e=null;
for(var i=0;i<this.childNodes.length;i++){
inode=this.childNodes.item(i);
if(inode.nodeType==DOMNode.TEXT_NODE){
if(inode.length<1){
_470d._appendChild(inode);
}else{
if(_470e){
_470e.appendData(inode.data);
_470d._appendChild(inode);
}else{
_470e=inode;
}
}
}else{
_470e=null;
inode.normalize();
}
}
for(var i=0;i<_470d.length;i++){
inode=_470d.item(i);
inode.parentNode.removeChild(inode);
}
}
};
DOMNode.prototype.isSupported=function DOMNode_isSupported(_4710,_4711){
return this.ownerDocument.implementation.hasFeature(_4710,_4711);
};
DOMNode.prototype.getElementsByTagName=function DOMNode_getElementsByTagName(_4712){
return this._getElementsByTagNameRecursive(_4712,new DOMNodeList(this.ownerDocument));
};
DOMNode.prototype._getElementsByTagNameRecursive=function DOMNode__getElementsByTagNameRecursive(_4713,_4714){
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
if((this.nodeName==_4713)||(_4713=="*")){
_4714._appendChild(this);
}
for(var i=0;i<this.childNodes.length;i++){
_4714=this.childNodes.item(i)._getElementsByTagNameRecursive(_4713,_4714);
}
}
return _4714;
};
DOMNode.prototype.getXML=function DOMNode_getXML(){
return this.toString();
};
DOMNode.prototype.getElementsByTagNameNS=function DOMNode_getElementsByTagNameNS(_4716,_4717){
return this._getElementsByTagNameNSRecursive(_4716,_4717,new DOMNodeList(this.ownerDocument));
};
DOMNode.prototype._getElementsByTagNameNSRecursive=function DOMNode__getElementsByTagNameNSRecursive(_4718,_4719,_471a){
if(this.nodeType==DOMNode.ELEMENT_NODE||this.nodeType==DOMNode.DOCUMENT_NODE){
if(((this.namespaceURI==_4718)||(_4718=="*"))&&((this.localName==_4719)||(_4719=="*"))){
_471a._appendChild(this);
}
for(var i=0;i<this.childNodes.length;i++){
_471a=this.childNodes.item(i)._getElementsByTagNameNSRecursive(_4718,_4719,_471a);
}
}
return _471a;
};
DOMNode.prototype._isAncestor=function DOMNode__isAncestor(node){
return ((this==node)||((this.parentNode)&&(this.parentNode._isAncestor(node))));
};
DOMNode.prototype.importNode=function DOMNode_importNode(_471d,deep){
var _471f;
this.getOwnerDocument()._performingImportNodeOperation=true;
try{
if(_471d.nodeType==DOMNode.ELEMENT_NODE){
if(!this.ownerDocument.implementation.namespaceAware){
_471f=this.ownerDocument.createElement(_471d.tagName);
for(var i=0;i<_471d.attributes.length;i++){
_471f.setAttribute(_471d.attributes.item(i).name,_471d.attributes.item(i).value);
}
}else{
_471f=this.ownerDocument.createElementNS(_471d.namespaceURI,_471d.nodeName);
for(var i=0;i<_471d.attributes.length;i++){
_471f.setAttributeNS(_471d.attributes.item(i).namespaceURI,_471d.attributes.item(i).name,_471d.attributes.item(i).value);
}
for(var i=0;i<_471d._namespaces.length;i++){
_471f._namespaces._nodes[i]=this.ownerDocument.createNamespace(_471d._namespaces.item(i).localName);
_471f._namespaces._nodes[i].setValue(_471d._namespaces.item(i).value);
}
}
}else{
if(_471d.nodeType==DOMNode.ATTRIBUTE_NODE){
if(!this.ownerDocument.implementation.namespaceAware){
_471f=this.ownerDocument.createAttribute(_471d.name);
}else{
_471f=this.ownerDocument.createAttributeNS(_471d.namespaceURI,_471d.nodeName);
for(var i=0;i<_471d._namespaces.length;i++){
_471f._namespaces._nodes[i]=this.ownerDocument.createNamespace(_471d._namespaces.item(i).localName);
_471f._namespaces._nodes[i].setValue(_471d._namespaces.item(i).value);
}
}
_471f.setValue(_471d.value);
}else{
if(_471d.nodeType==DOMNode.DOCUMENT_FRAGMENT){
_471f=this.ownerDocument.createDocumentFragment();
}else{
if(_471d.nodeType==DOMNode.NAMESPACE_NODE){
_471f=this.ownerDocument.createNamespace(_471d.nodeName);
_471f.setValue(_471d.value);
}else{
if(_471d.nodeType==DOMNode.TEXT_NODE){
_471f=this.ownerDocument.createTextNode(_471d.data);
}else{
if(_471d.nodeType==DOMNode.CDATA_SECTION_NODE){
_471f=this.ownerDocument.createCDATASection(_471d.data);
}else{
if(_471d.nodeType==DOMNode.PROCESSING_INSTRUCTION_NODE){
_471f=this.ownerDocument.createProcessingInstruction(_471d.target,_471d.data);
}else{
if(_471d.nodeType==DOMNode.COMMENT_NODE){
_471f=this.ownerDocument.createComment(_471d.data);
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
for(var i=0;i<_471d.childNodes.length;i++){
_471f.appendChild(this.ownerDocument.importNode(_471d.childNodes.item(i),true));
}
}
this.getOwnerDocument()._performingImportNodeOperation=false;
return _471f;
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
DOMDocument=function(_4723){
this._class=addClass(this._class,"DOMDocument");
this.DOMNode=DOMNode;
this.DOMNode(this);
this.doctype=null;
this.implementation=_4723;
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
DOMDocument.prototype.createElement=function DOMDocument_createElement(_4724){
if(this.ownerDocument.implementation.errorChecking&&(!this.ownerDocument.implementation._isValidName(_4724))){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
var node=new DOMElement(this);
node.tagName=_4724;
node.nodeName=_4724;
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
DOMDocument.prototype.createProcessingInstruction=function DOMDocument_createProcessingInstruction(_472d,data){
if(this.ownerDocument.implementation.errorChecking&&(!this.implementation._isValidName(_472d))){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
var node=new DOMProcessingInstruction(this);
node.target=_472d;
node.nodeName=_472d;
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
DOMDocument.prototype.createElementNS=function DOMDocument_createElementNS(_4732,_4733){
if(this.ownerDocument.implementation.errorChecking){
if(!this.ownerDocument._isValidNamespace(_4732,_4733)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_4733)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
var node=new DOMElement(this);
var qname=this.implementation._parseQName(_4733);
node.nodeName=_4733;
node.namespaceURI=_4732;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.tagName=_4733;
this.all[this.all.length]=node;
return node;
};
DOMDocument.prototype.createAttributeNS=function DOMDocument_createAttributeNS(_4736,_4737){
if(this.ownerDocument.implementation.errorChecking){
if(!this.ownerDocument._isValidNamespace(_4736,_4737,true)){
throw (new DOMException(DOMException.NAMESPACE_ERR));
}
if(!this.ownerDocument.implementation._isValidName(_4737)){
throw (new DOMException(DOMException.INVALID_CHARACTER_ERR));
}
}
var node=new DOMAttr(this);
var qname=this.implementation._parseQName(_4737);
node.nodeName=_4737;
node.namespaceURI=_4736;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.name=_4737;
node.nodeValue="";
return node;
};
DOMDocument.prototype.createNamespace=function DOMDocument_createNamespace(_473a){
var node=new DOMNamespace(this);
var qname=this.implementation._parseQName(_473a);
node.nodeName=_473a;
node.prefix=qname.prefix;
node.localName=qname.localName;
node.name=_473a;
node.nodeValue="";
return node;
};
DOMDocument.prototype.getElementById=function DOMDocument_getElementById(_473d){
retNode=null;
for(var i=0;i<this.all.length;i++){
var node=this.all[i];
if((node.id==_473d)&&(node._isAncestor(node.ownerDocument.documentElement))){
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
DOMDocument.prototype._isValidNamespace=function DOMDocument__isValidNamespace(_4740,_4741,_4742){
if(this._performingImportNodeOperation==true){
return true;
}
var valid=true;
var qName=this.implementation._parseQName(_4741);
if(this._parseComplete==true){
if(qName.localName.indexOf(":")>-1){
valid=false;
}
if((valid)&&(!_4742)){
if(!_4740){
valid=false;
}
}
if((valid)&&(qName.prefix=="")){
valid=false;
}
}
if((valid)&&(qName.prefix=="xml")&&(_4740!="http://www.w3.org/XML/1998/namespace")){
valid=false;
}
return valid;
};
DOMDocument.prototype.toString=function DOMDocument_toString(){
return ""+this.childNodes;
};
DOMElement=function(_4745){
this._class=addClass(this._class,"DOMElement");
this.DOMNode=DOMNode;
this.DOMNode(_4745);
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
DOMElement.prototype.setAttributeNode=function DOMElement_setAttributeNode(_474e){
if(this.ownerDocument.implementation._isIdDeclaration(_474e.name)){
this.id=_474e.value;
}
return this.attributes.setNamedItem(_474e);
};
DOMElement.prototype.removeAttributeNode=function DOMElement_removeAttributeNode(_474f){
if(this.ownerDocument.implementation.errorChecking&&_474f._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
var _4750=this.attributes._findItemIndex(_474f._id);
if(this.ownerDocument.implementation.errorChecking&&(_4750<0)){
throw (new DOMException(DOMException.NOT_FOUND_ERR));
}
return this.attributes._removeChild(_4750);
};
DOMElement.prototype.getAttributeNS=function DOMElement_getAttributeNS(_4751,_4752){
var ret="";
var attr=this.attributes.getNamedItemNS(_4751,_4752);
if(attr){
ret=attr.value;
}
return ret;
};
DOMElement.prototype.setAttributeNS=function DOMElement_setAttributeNS(_4755,_4756,value){
var attr=this.attributes.getNamedItem(_4755,_4756);
if(!attr){
attr=this.ownerDocument.createAttributeNS(_4755,_4756);
}
var value=new String(value);
if(this.ownerDocument.implementation.errorChecking){
if(attr._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(!this.ownerDocument._isValidNamespace(_4755,_4756)){
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
DOMElement.prototype.removeAttributeNS=function DOMElement_removeAttributeNS(_4759,_475a){
return this.attributes.removeNamedItemNS(_4759,_475a);
};
DOMElement.prototype.getAttributeNodeNS=function DOMElement_getAttributeNodeNS(_475b,_475c){
return this.attributes.getNamedItemNS(_475b,_475c);
};
DOMElement.prototype.setAttributeNodeNS=function DOMElement_setAttributeNodeNS(_475d){
if((_475d.prefix=="")&&this.ownerDocument.implementation._isIdDeclaration(_475d.name)){
this.id=_475d.value;
}
return this.attributes.setNamedItemNS(_475d);
};
DOMElement.prototype.hasAttribute=function DOMElement_hasAttribute(name){
return this.attributes._hasAttribute(name);
};
DOMElement.prototype.hasAttributeNS=function DOMElement_hasAttributeNS(_475f,_4760){
return this.attributes._hasAttributeNS(_475f,_4760);
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
DOMAttr=function(_4764){
this._class=addClass(this._class,"DOMAttr");
this.DOMNode=DOMNode;
this.DOMNode(_4764);
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
DOMNamespace=function(_4768){
this._class=addClass(this._class,"DOMNamespace");
this.DOMNode=DOMNode;
this.DOMNode(_4768);
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
DOMCharacterData=function(_476b){
this._class=addClass(this._class,"DOMCharacterData");
this.DOMNode=DOMNode;
this.DOMNode(_476b);
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
DOMCharacterData.prototype.substringData=function DOMCharacterData_substringData(_476e,count){
var ret=null;
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_476e<0)||(_476e>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
if(!count){
ret=this.data.substring(_476e);
}else{
ret=this.data.substring(_476e,_476e+count);
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
DOMCharacterData.prototype.insertData=function DOMCharacterData_insertData(_4772,arg){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_4772<0)||(_4772>this.data.length))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(this.data.substring(0,_4772).concat(arg,this.data.substring(_4772)));
}else{
if(this.ownerDocument.implementation.errorChecking&&(_4772!=0)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(arg);
}
};
DOMCharacterData.prototype.deleteData=function DOMCharacterData_deleteData(_4774,count){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_4774<0)||(_4774>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
if(!count||(_4774+count)>this.data.length){
this.setData(this.data.substring(0,_4774));
}else{
this.setData(this.data.substring(0,_4774).concat(this.data.substring(_4774+count)));
}
}
};
DOMCharacterData.prototype.replaceData=function DOMCharacterData_replaceData(_4776,count,arg){
if(this.ownerDocument.implementation.errorChecking&&this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if(this.data){
if(this.ownerDocument.implementation.errorChecking&&((_4776<0)||(_4776>this.data.length)||(count<0))){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
this.setData(this.data.substring(0,_4776).concat(arg,this.data.substring(_4776+count)));
}else{
this.setData(arg);
}
};
DOMText=function(_4779){
this._class=addClass(this._class,"DOMText");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_4779);
this.nodeName="#text";
this.nodeType=DOMNode.TEXT_NODE;
};
DOMText.prototype=new DOMCharacterData;
DOMText.prototype.splitText=function DOMText_splitText(_477a){
var data,inode;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if((_477a<0)||(_477a>this.data.length)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
}
if(this.parentNode){
data=this.substringData(_477a);
inode=this.ownerDocument.createTextNode(data);
if(this.nextSibling){
this.parentNode.insertBefore(inode,this.nextSibling);
}else{
this.parentNode.appendChild(inode);
}
this.deleteData(_477a);
}
return inode;
};
DOMText.prototype.toString=function DOMText_toString(){
return this.__escapeString(""+this.nodeValue);
};
DOMCDATASection=function(_477c){
this._class=addClass(this._class,"DOMCDATASection");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_477c);
this.nodeName="#cdata-section";
this.nodeType=DOMNode.CDATA_SECTION_NODE;
};
DOMCDATASection.prototype=new DOMCharacterData;
DOMCDATASection.prototype.splitText=function DOMCDATASection_splitText(_477d){
var data,inode;
if(this.ownerDocument.implementation.errorChecking){
if(this._readonly){
throw (new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR));
}
if((_477d<0)||(_477d>this.data.length)){
throw (new DOMException(DOMException.INDEX_SIZE_ERR));
}
}
if(this.parentNode){
data=this.substringData(_477d);
inode=this.ownerDocument.createCDATASection(data);
if(this.nextSibling){
this.parentNode.insertBefore(inode,this.nextSibling);
}else{
this.parentNode.appendChild(inode);
}
this.deleteData(_477d);
}
return inode;
};
DOMCDATASection.prototype.toString=function DOMCDATASection_toString(){
var ret="";
ret+="<![CDATA["+this.nodeValue+"]]>";
return ret;
};
DOMComment=function(_4780){
this._class=addClass(this._class,"DOMComment");
this.DOMCharacterData=DOMCharacterData;
this.DOMCharacterData(_4780);
this.nodeName="#comment";
this.nodeType=DOMNode.COMMENT_NODE;
};
DOMComment.prototype=new DOMCharacterData;
DOMComment.prototype.toString=function DOMComment_toString(){
var ret="";
ret+="<!--"+this.nodeValue+"-->";
return ret;
};
DOMProcessingInstruction=function(_4782){
this._class=addClass(this._class,"DOMProcessingInstruction");
this.DOMNode=DOMNode;
this.DOMNode(_4782);
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
DOMDocumentFragment=function(_4786){
this._class=addClass(this._class,"DOMDocumentFragment");
this.DOMNode=DOMNode;
this.DOMNode(_4786);
this.nodeName="#document-fragment";
this.nodeType=DOMNode.DOCUMENT_FRAGMENT_NODE;
};
DOMDocumentFragment.prototype=new DOMNode;
DOMDocumentFragment.prototype.toString=function DOMDocumentFragment_toString(){
var xml="";
var _4788=this.getChildNodes().getLength();
for(intLoop=0;intLoop<_4788;intLoop++){
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
var _47a0=arrD[arrD.length-1];
arrD.length--;
var _47a1=arrD.join("\n").length;
return iP-_47a1;
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
XMLSerializer=function(){
alert("do not init this class. Use the static methods instead");
};
XMLSerializer.toXML=function(obj,_39d9,_39da){
if(_39d9==undefined){
_39d9="model";
}
_39da=_39da?_39da:"";
var t=XMLSerializer.getTypeName(obj);
var s=_39da+"<"+_39d9+" type=\""+t+"\">";
switch(t){
case "int":
case "number":
case "boolean":
s+=obj;
break;
case "string":
s+=XMLSerializer.xmlEncode(obj);
break;
case "date":
s+=obj.toLocaleString();
break;
case "Array":
case "array":
s+="\n";
var _39dd=_39da+"   ";
for(var i=0;i<obj.length;i++){
s+=XMLSerializer.toXML(obj[i],("element"),_39dd);
}
s+=_39da;
break;
default:
if(obj!==null){
s+="\n";
if(obj instanceof ArrayList){
obj.trimToSize();
}
var _39df=obj.getPersistentAttributes();
var _39dd=_39da+"   ";
for(var name in _39df){
s+=XMLSerializer.toXML(_39df[name],name,_39dd);
}
s+=_39da;
}
break;
}
s+="</"+_39d9+">\n";
return s;
};
XMLSerializer.isSimpleVar=function(t){
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
XMLSerializer.getTypeName=function(obj){
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
if(XMLSerializer.isSimpleVar(t)){
return t;
}
return obj.type.replace("@NAMESPACE"+"@","");
};
XMLSerializer.xmlEncode=function(_39e4){
var _39e5=_39e4;
var amp=/&/gi;
var gt=/>/gi;
var lt=/</gi;
var quot=/"/gi;
var apos=/'/gi;
var _39eb="&#62;";
var _39ec="&#38;#60;";
var _39ed="&#38;#38;";
var _39ee="&#34;";
var _39ef="&#39;";
_39e5=_39e5.replace(amp,_39ed);
_39e5=_39e5.replace(quot,_39ee);
_39e5=_39e5.replace(lt,_39ec);
_39e5=_39e5.replace(gt,_39eb);
_39e5=_39e5.replace(apos,_39ef);
return _39e5;
};
XMLDeserializer=function(){
alert("do not init this class. Use the static methods instead");
};
XMLDeserializer.fromXML=function(node,_3b8f){
var _3b90=""+node.getAttributes().getNamedItem("type").getNodeValue();
var value=node.getNodeValue();
switch(_3b90){
case "int":
try{
return parseInt(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
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
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
}
case "Number":
case "number":
try{
return parseFloat(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
}
case "Boolean":
case "boolean":
case "bool":
try{
return "true"==(""+node.getChildNodes().item(0).getNodeValue()).toLowerCase();
}
catch(e){
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
}
case "dateTime":
case "Date":
case "date":
try{
return new Date(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
}
case "float":
try{
return parseFloat(""+node.getChildNodes().item(0).getNodeValue());
}
catch(e){
alert("Error:"+e+"\nDataType:"+_3b90+"\nXML Node:"+node);
}
break;
}
_3b90=_3b90.replace("@NAMESPACE"+"@","");
var obj=eval("new "+_3b90+"()");
if(_3b8f!=undefined&&obj.setModelParent!=undefined){
obj.setModelParent(_3b8f);
}
var _3b93=node.getChildNodes();
for(var i=0;i<_3b93.length;i++){
var child=_3b93.item(i);
var _3b96=child.getNodeName();
if(obj instanceof Array){
_3b96=i;
}
obj[_3b96]=XMLDeserializer.fromXML(child,obj instanceof AbstractObjectModel?obj:_3b8f);
}
return obj;
};
EditPolicy=function(_385d){
this.policy=_385d;
};
EditPolicy.DELETE="DELETE";
EditPolicy.MOVE="MOVE";
EditPolicy.CONNECT="CONNECT";
EditPolicy.RESIZE="RESIZE";
EditPolicy.prototype.type="EditPolicy";
EditPolicy.prototype.getPolicy=function(){
return this.policy;
};
AbstractPalettePart=function(){
this.x=0;
this.y=0;
this.html=null;
};
AbstractPalettePart.prototype.type="AbstractPalettePart";
AbstractPalettePart.prototype=new Draggable();
AbstractPalettePart.prototype.createHTMLElement=function(){
var item=document.createElement("div");
item.id=this.id;
item.style.position="absolute";
item.style.height="24px";
item.style.width="24px";
return item;
};
AbstractPalettePart.prototype.setEnviroment=function(_316c,_316d){
this.palette=_316d;
this.workflow=_316c;
};
AbstractPalettePart.prototype.getHTMLElement=function(){
if(this.html===null){
this.html=this.createHTMLElement();
Draggable.call(this,this.html);
}
return this.html;
};
AbstractPalettePart.prototype.onDrop=function(_316e,_316f){
var _3170=this.workflow.getScrollLeft();
var _3171=this.workflow.getScrollTop();
var _3172=this.workflow.getAbsoluteX();
var _3173=this.workflow.getAbsoluteY();
this.setPosition(this.x,this.y);
this.execute(_316e+_3170-_3172,_316f+_3171-_3173);
};
AbstractPalettePart.prototype.execute=function(x,y){
alert("inerited class should override the method 'AbstractPalettePart.prototype.execute'");
};
AbstractPalettePart.prototype.setTooltip=function(_3176){
this.tooltip=_3176;
if(this.tooltip!==null){
this.html.title=this.tooltip;
}else{
this.html.title="";
}
};
AbstractPalettePart.prototype.setDimension=function(w,h){
this.width=w;
this.height=h;
if(this.html===null){
return;
}
this.html.style.width=this.width+"px";
this.html.style.height=this.height+"px";
};
AbstractPalettePart.prototype.setPosition=function(xPos,yPos){
this.x=Math.max(0,xPos);
this.y=Math.max(0,yPos);
if(this.html===null){
return;
}
this.html.style.left=this.x+"px";
this.html.style.top=this.y+"px";
this.html.style.cursor="move";
};
AbstractPalettePart.prototype.getWidth=function(){
return this.width;
};
AbstractPalettePart.prototype.getHeight=function(){
return this.height;
};
AbstractPalettePart.prototype.getY=function(){
return this.y;
};
AbstractPalettePart.prototype.getX=function(){
return this.x;
};
AbstractPalettePart.prototype.getPosition=function(){
return new Point(this.x,this.y);
};
AbstractPalettePart.prototype.disableTextSelection=function(e){
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
ExternalPalette=function(_3d68,divId){
this.html=document.getElementById(divId);
this.workflow=_3d68;
this.parts=new ArrayList();
};
ExternalPalette.prototype.type="ExternalPalette";
ExternalPalette.prototype.getHTMLElement=function(){
return this.html;
};
ExternalPalette.prototype.addPalettePart=function(part){
if(!(part instanceof AbstractPalettePart)){
throw "parameter is not instanceof [AbstractPalettePart]";
}
this.parts.add(part);
this.html.appendChild(part.getHTMLElement());
part.setEnviroment(this.workflow,this);
};
