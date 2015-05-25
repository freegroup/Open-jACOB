var de;
var w;
var h;
var xm=0;
var ym=0;
var xm_old=0;
var ym_old=0;
var ay=0;
var PI_180 = Math.PI/180;
var sin=Math.sin(ay*PI_180);
var cos=Math.cos(ay*PI_180); 
var angle = 180;
var k=0;
var elem = [];
var camDist = 900;
var scale = .4;
var vpsi = 0.05714;
var psi =  0.05714;
var zoom=1;
var tx=100;
var ty=0;
var coord_tx=-100;
var coord_ty=0;

function rotate(e) 
{
  e.angle = (angle+ e.theta);

  //psi from -.99 to .99
  var dd=e.angle*PI_180;
  var cc=e.psie*PI_180;
  var ee =Math.cos(cc);
  var X = e.r*Math.cos(dd)*ee;
  var Y = e.r*Math.sin(dd)*ee*Math.sin(psi) + e.z*Math.cos(psi);


  e.dist = parseInt( e.r*Math.cos((vpsi + e.psie)*PI_180) +
                  e.rr*Math.sin(dd)*Math.cos(vpsi*PI_180) + camDist);

  //scale factor for sizing
  size = (e.dist)/500;
  try
  {
    e.style.top = -(Y*zoom-ty) + "px";
    e.style.left = (X*zoom+tx) + "px";
//    e.style.fontSize = (size*zoom) + "em";
//    e.style.opacity  = (size)/6;
//    e.style.zIndex  = e.dist;
  }
  catch(e)
  {
  }
}
  
var items;
function run()
{
    items.each(rotate);
    setTimeout("run()", 160);
}

(function() 
{

  Event.observe(window, "resize", arrangeUI);
  Event.observe(window, "load", function() 
  {
    Event.observe(document, "mousewheel", onZoom, false);
    Event.observe(document, "DOMMouseScroll", onZoom, false); // Firefox
 

    items = $$("#links a");
    items.each(cartesianToSphereical);
    run();
//    items.each(rotate);
 
    Event.observe(document, 'mousedown', startDrag);

    var img = document.createElement("img");
    img.src = "spin.png";
    img.id = "spin";
    img.height="50";
    document.body.appendChild(img);

    arrangeUI();

    new Control.Slider('handle2' , 'track2',
    {
      range: $R(0.3,4),
      sliderValue: zoom,
      onSlide: function(v) 
      {
          zoom=v;
//          items.each(rotate);
      }
    });

    new Control.Slider('handle1' , 'track1',
    {
      range: $R(-4,4),
      sliderValue: psi,
      axis:'vertical',
      onSlide: function(v) 
      {
          psi=v;
//          items.each(rotate);
//    items.each(rotate);
      }
    });
  });

  function arrangeUI()
  {
    var de = document.documentElement;
    var w = (window.innerWidth || self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth);
    var h = (window.innerHeight || self.innerHeight || (de&&de.clientHeight) || document.body.clientHeight);
    $("spin").style.top=(h/2-25)+"px";
    $("spin").style.left="0px";
    $("track1").style.top=(h/2-75)+"px";
    $("track1").style.left="30px";
  }

  function onZoom(event) 
  {
     zoom +=  (Event.wheel(event)/5.0);
     zoom = Math.max(0.3,zoom);
     zoom = Math.min(4.0,zoom);
//     items.each(rotate);
  }

  function startDrag(event) 
  {
    cursorStartX = Event.pointerX(event);
    cursorStartY = Event.pointerY(event);
    elementStartX =tx;
    elementStartY =ty;
    Event.observe(document, 'mousemove', dragGo);
    Event.observe(document, 'mouseup', dragStop);
  }

  function dragGo(event) 
  {
    tx = ( Event.pointerX(event) - cursorStartX + elementStartX);
    ty = ( Event.pointerY(event) - cursorStartY + elementStartY);
//    items.each(rotate);
    Event.stop(event);
  }

  function dragStop(event) 
  { 
    Event.stopObserving(document, 'mousemove', dragGo);
    Event.stopObserving(document, 'mouseup', dragStop);
    Event.stop(event);
  }

  function cartesianToSphereical(e) 
  {
    var curLink = e.href;
    var queryString = curLink.replace(/^[^\?]+\??/,'');
    params = parseQuery( queryString );

    var x = parseInt(params['x'])+coord_tx;
    var y = parseInt(params['y']);
    var z = parseInt(params['z']);
    var r = parseInt(Math.abs(Math.sqrt(x*x+y*y+z*z)));
    var rr = parseInt(Math.abs(Math.sqrt(x*x+y*y)));
    

    //angle from 0 between -pi/pi  , need to add 180 degrees for anything in 2,3 quadrant
    if(x<0)
        theta= (Math.atan(y/x)*180/Math.PI)+180;
    else
        theta= (Math.atan(y/x)*180/Math.PI);

    psiElement= (Math.atan(z/rr)*180/Math.PI);

    //sphereical is r, theta, and psi (psie here because psi is the system, psie is individual)
    e.theta = theta;           //rotational angle
    e.r = r;                   //total radius
    e.psie = psiElement;        //vertical angle
    e.z = z;
    e.rr = rr;
  }

  function parseQuery ( query ) 
  {
     var Params = new Object ();
     if ( ! query ) return Params; // return empty object
     var Pairs = query.split(/[;&]/);
     for ( var i = 0; i < Pairs.length; i++ ) 
     {
        var KeyVal = Pairs[i].split('=');
        if ( ! KeyVal || KeyVal.length != 2 ) continue;
        var key = unescape( KeyVal[0] );
        var val = unescape( KeyVal[1] );
        val = val.replace(/\+/g, ' ');
        Params[key] = val;
     }
     return Params;
  }

})();