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
}