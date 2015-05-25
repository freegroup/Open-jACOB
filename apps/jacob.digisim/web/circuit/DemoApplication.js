Application = function()
{
    var layout;
    var simDoc;
    var timer;
    var msgCt;
    var dialog;
    var feedbackDialog;
    var showBtn;
    var fileGrid;

 function createBox(t, s){
 return ['<div class="msg">',
 '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
 '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
 '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
 '</div>'].join('');
 }
    return {
       init : function(){
          layout = new Ext.BorderLayout(document.body, {
                north: {
                   initialSize: 30,
                   split:false,
                   titlebar: false,
                   collapsible: false,
                   animate: false
                },
                south: {
                   initialSize: 130,
                   split:true,
                   titlebar: true,
                   collapsible: true,
                   autoScroll:true,
                   animate: true
                },
                center: {
                   titlebar: false,
                   autoScroll:true,
                   fitToFrame:true
          }
          });

         layout.beginUpdate();
         layout.add('north',  new Ext.ContentPanel('toolbar',  {height:33}));
         layout.add('center', new Ext.ContentPanel('canvas', {width:100, height:200}));
         layout.endUpdate();


         Ext.QuickTips.init();
         setupToolbar();
         feedbackButton = Ext.get('feedback-button');

         logger.debug("Application ready.");
         Ext.MessageBox.alert("Info", "Press [Start] to run the simulation.");
         logger.debug("application running with demo user account.");
         workflow.scrollArea = document.getElementById("canvas").parentNode;
         var droptarget=new Ext.dd.DropTarget("canvas",{ddGroup:'TreeDD'});
         droptarget.notifyDrop=function(dd, e, data)
         {
            if(data.name)
            {
               var xOffset    = workflow.getAbsoluteX();
               var yOffset    = workflow.getAbsoluteY();
               var scrollLeft = workflow.getScrollLeft();
               var scrollTop  = workflow.getScrollTop();

               workflow.addFigure(eval("new "+data.name+"()"),e.xy[0]-xOffset+scrollLeft,e.xy[1]-yOffset+scrollTop);
               return true;
            }
         }
       },

     addPanel: function(/*:String*/ region, /*:String*/ id, /*:String*/ title)
     {
         layout.beginUpdate();
         layout.add(region,  new Ext.ContentPanel(id, {title: title}));
	 layout.endUpdate();
     },

      msg : function(title, format)
      {

         if(!msgCt)
         {
            msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
         }
         msgCt.alignTo(document, 't-t');
         var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
         var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
         m.slideIn('t').pause(1).ghost("t", {remove:true});

     },

     sendFeedback:function()
     {

        Ext.MessageBox.show({
           title: 'Feedback',
           msg: 'Enter your feedback or comment:',
           width:300,
           buttons: Ext.MessageBox.OKCANCEL,
           multiline: true,
	   animEl: 'feedback-button',
           fn: function(btn, text)
               {
                  if(btn=="ok")
                  {
		       		Ext.Ajax.request({
						url : '../../../../cmdenter',
						params : { entry : 'CreateFeedback', 
						           app : appName,
						           user :'guest',
						           pwd : '',
						           subject: text,
						           message: text
						          },
						method: 'POST',
						success: function ( result, request ) 
						         { 
								Application.msg('Information', 'Your feedback has been saved.');
	  						 }
					       });
				   }
			   }
			   });
     },

     startSimulation:function()
     {
	    workflow.setCurrentSelection(null);
	    simDoc = workflow.getDocument();
	    var figures = simDoc.getFigures();
	    for(var i=0; i<figures.length;i++)
	    {
	      var figure = figures[i];
	      if(figure.startSimulation)
	        figure.startSimulation();
	    }
	    timer = window.setInterval(Application.calculateStep,100);
     },
     
     stopSimulation:function()
     {
	    window.clearInterval(timer);
	    var figures = simDoc.getFigures();
	    for(var i=0; i<figures.length;i++)
	    {
	      var figure = figures[i];
	      if(figure.stopSimulation)
	        figure.stopSimulation();
	    }
	    simDoc = null;
     },
     
     calculateStep:function()
     {
	  var figures = simDoc.getFigures();
	  for(var i=0; i< figures.length;i++)
	  {
	    var figure = figures[i];
	    if(figure.calculate)
	      figure.calculate();
	  }
	
	  var lines = simDoc.getLines();
	  for(var i=0;i< lines.length;i++)
	  {
		  var line = lines[i];
	      if(line.getSource().type == "OutputPort")
	      {
	        var value = line.getSource().getProperty("value");
	        line.getTarget().setProperty("value",value);
	      }
	      else
	      {
	        var value = line.getTarget().getProperty("value");
	        line.getSource().setProperty("value",value);
	      }
	      if(value==true)
	        var color = new Color(255,0,0);
	      else
	        var color = new Color(0,0,255);
	      line.setColor(color);
	      line.getSource().setBackgroundColor(color);
	      line.getTarget().setBackgroundColor(color);
	    }
     }
  };
}();

