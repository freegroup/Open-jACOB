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
    var header;
    var COLOR_HIGH = new Color(255,0,0);
    var COLOR_LOW  = new Color(255,0,0);

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
                west: {
                   split:true,
                   initialSize: 240,
                   minSize: 220,
                   maxSize: 400,
                   titlebar: true,
                   collapsible: true,
                   autoScroll:true,
                   animate: true
                },
                north: {
                   initialSize: 50,
                   split:false,
                   titlebar: true,
                   collapsible: false,
                   animate: false
                },
                south: {
                   initialSize: 200,
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

          var palette = setupPalette();
          var toolbar = setupToolbar();
          var menu    = setupMenubar();

         layout.beginUpdate();
         header = layout.add('north',  new Ext.ContentPanel('menubar',  {height:33}));
         var canvas_panel = new Ext.ContentPanel('canvas-panel',
         {
           toolbar: toolbar,
           resizeEl: 'canvas-content',
           autoScroll:true,
           fitToFrame:true
         });
         layout.add('center', canvas_panel);
         layout.endUpdate();

         Ext.QuickTips.init();
//         HelpManager.showHint();
         feedbackButton = Ext.get('feedback-button');

         logger.debug("Application ready.");
         Application.setTitle(fileName);
         workflow.scrollArea = document.getElementById("canvas-content");
         var droptarget=new Ext.dd.DropTarget("paintarea",{ddGroup:'TreeDD'});
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

     addPanel: function(/*:String*/ region, /*:String*/ id, /*:String*/ title)
     {
         layout.beginUpdate();
         var view = layout.add(region,  new Ext.ContentPanel(id, {title: title}));
	 layout.endUpdate();
         return view;
     },

     getLayout: function()
     {
         return layout;
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
     },

     fileSave:function()
     {
        if(pkey!=null)
        {
		Ext.Ajax.request({
			url : 'save.jsp' , 
			params : { pkey : pkey, browser : browserId },
			method: 'POST',
			xmlData:new CircuitSerializer().toXML(workflow),
			success: function ( result, request ) { 
				Application.msg('Information', 'Circuit successfull saved.');
			},
			failure: function ( result, request) { 
				Ext.MessageBox.alert('Error', result.responseText); 
			} 
		});
        }
        else
        {
        Ext.MessageBox.prompt('Save','Enter circuit name:',function(btn, text)
               {
                  if(btn=="ok")
                  {
		Ext.Ajax.request({
			url : 'save.jsp' , 
			params : { name : text, browser : browserId },
			method: 'POST',
			xmlData:new CircuitSerializer().toXML(workflow),
			success: function ( result, request ) { 
				Application.msg('Information', 'Circuit successfull saved.');
                                eval(result.responseText);
                                Application.setTitle(text);
			},
			failure: function ( result, request) { 
				Ext.MessageBox.alert('Error', result.responseText); 
			} 
		});
   		  }
               });
           }
        
     },

     fileOpen : function()
     {
            var openButton;
            if(!dialog){ // lazy initialize the dialog and only create it once
                dialog = new Ext.LayoutDialog("hello-dlg", { 
                        modal:true,
                        width:600,
                        height:400,
                        shadow:true,
                        minWidth:300,
                        minHeight:300,
                        proxyDrag: true,
                        center: {
                           autoScroll:true,
                           tabPosition: 'top',
                           closeOnTab: true,
                           alwaysShowTabs: false
                        },
                        load:function()
                        {
                           var pkey = fileGrid.getSelectionModel().getSelected().data.pkey;
                           window.location.href="edit.jsp?pkey="+pkey+"&browser="+browserId;
                        }
                });
                dialog.addKeyListener(27, dialog.hide, dialog);
                openButton = dialog.addButton('Open', dialog.load, dialog);
                dialog.addButton('Cancel', dialog.hide, dialog);
                openButton.setDisabled(true);

                var layout = dialog.getLayout();
                layout.beginUpdate();
	        layout.add('center', new Ext.ContentPanel('file-grid'));
                // generate some other tabs
                layout.endUpdate();
            }
            dialog.show();
            // create the Data Store
            var ds = new Ext.data.Store({
               // load using HTTP
               proxy: new Ext.data.HttpProxy({url: fileUrl}),
               // the return will be XML, so lets set up a reader
               reader: new Ext.data.XmlReader({record: 'circuit'}, ['pkey','title'])});

           var cm = new Ext.grid.ColumnModel([
                  {header: "Id", width: 50, dataIndex: 'pkey'},
                  {header: "Title", width: 400, dataIndex: 'title'}
               ]);

           // create the grid
           fileGrid = new Ext.grid.Grid('file-grid', {
               ds: ds,
               sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
               cm: cm,
               enableLoad:function()
               {
                  if(fileGrid.getSelectionModel().getSelected())
                    openButton.setDisabled(false);
               }
            });
            fileGrid.render();
            fileGrid.on("click", fileGrid.enableLoad, this);
            ds.load();
            var headerBar = fileGrid.getView().getHeaderPanel(true);
            var toolbar = new Ext.Toolbar(headerBar);

            toolbar.addButton({ text: 'Delete', 
                       icon:"icons/cancel.png",
                       cls: 'x-btn-text-icon',
                       handler: function(o, e) 
	               {
	Application.gridFileDelete();
	}});

        },
     fileNew : function()
     {
        window.location.href="edit.jsp?browser="+browserId;
     },
     gridFileDelete:function()
     {
	var m = fileGrid.getSelections();
	if(m.length > 0)
	{
		Ext.MessageBox.confirm('Message', 'Do you really want to delete it?' , Application.gridFileDelete2);	
	}
	else
	{
		Ext.MessageBox.alert('Message', 'Select at least one item to delete');
	}
     },
 
     gridFileDelete2: function(btn)
     {
       if(btn == 'yes')
       {	
          var m = fileGrid.getSelections();
          for(var i = 0, len = m.length; i < len; i++)
          {
             var pkey = m[i].get("pkey");
             Ext.Ajax.request({
                url : 'delete.jsp' , 
                params : { pkey : pkey, browser : browserId },
                method: 'POST',
                success: function ( result, request ) 
                { 
                   fileGrid.dataSource.reload();
                },
                failure: function ( result, request) 
                { 
                   Ext.MessageBox.alert('Error', result.responseText); 
                } 
             });
          }	
	}
      },

      setTitle:function(title)
      {
         header.setTitle("DigitalSimulator: "+title);
      },

     sendKeepAlive: function(btn)
     {
	Ext.Ajax.request({
	   url : 'serverPush.jsp' , 
	   params : { browser : browserId },
	   method: 'POST'
	});
      },

      quit:function()
      {
           window.location.href="login.jsp";
      }

  };
}();
