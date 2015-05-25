Ext.tree.TreeNodeUI.prototype.initEvents =
Ext.tree.TreeNodeUI.prototype.initEvents.createSequence(function(){
      if(this.node.attributes.tipCfg){
          var o = this.node.attributes.tipCfg;
          o.target = Ext.id(this.textNode);
          Ext.QuickTips.register(o);
      }
});

var Composer = function(){
    // shorthand
    var Tree = Ext.tree;
    var root;
    var tree;
    var msgCt;
	 function createBox(t, s){
	 return ['<div class="msg">',
	 '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
	 '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', t, '</h3>', s, '</div></div></div>',
	 '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
	 '</div>'].join('');
	 }
	     return {
        init : function(){
            Ext.QuickTips.init();
            Ext.QuickTips.maxWidth = 600;
            var layout = new Ext.BorderLayout(document.body, {
                fitToFrame:true,
                west: {
                    initialSize:150,
                    split:true,
                    titlebar:true
                },
                south: {
                    initialSize:50,
                    split:false,
                    titlebar:false
                },
                east:{
                    split:true,
                    initialSize:200,
                    minSize:150,
                    maxSize:350
                },
                center: {
                    titlebar:true,
                    tabPosition:'top'
                }
            });

            var albums = layout.getEl().createChild({tag:'div', id:'albums'});
            var viewEl = albums.createChild({tag:'div', id:'folders'});

		   var toolbar = new Ext.Toolbar('buttonbar');
		
		   toolbar.addFill();
		   toolbar.addButton({ text: 'Speichern', 
		                       id:'save-button',
		                       cls: 'x-btn-text',
		                       handler: function(o, e) 
		   {
		       Composer.save();
		   }});
		
		   toolbar.addSpacer();
		   toolbar.addButton({ text: 'Schliessen', 
		                       id:'close-button',
		                       cls: 'x-btn-text',
		                       handler: function(o, e) 
		   {
		       window.close();
		   }});
           layout.add('south',new Ext.ContentPanel('buttonbar',  {height:50}));

			var dp = layout.add('east', new Ext.ContentPanel(Ext.id(), {
				autoCreate : true,
				fitToFrame:true
			}));
			this.detailEl = dp.getEl();

            var folders = layout.add('west', new Ext.ContentPanel(albums, {
                title:'Zusammenstellung',
                fitToFrame:true,
                autoScroll:true,
                autoCreate:true,
                resizeEl:viewEl
            }));

            var images = layout.add('center', new Ext.ContentPanel('images', {
                title:'Folienvorrat', 
                fitToFrame:true,
                autoScroll:true,
                autoCreate:true
            }));
            var imgBody = images.getEl();

		    layout.endUpdate();

            var tree = new Tree.TreePanel(viewEl, {
                animate:true, 
                enableDD:true,
                containerScroll: true,
                ddGroup: 'organizerDD',
                rootVisible:false
            });
			tree.on('contextmenu', function (node) 
			{
               var selectedNode = Composer.tree.selModel.selNode;
			   if(selectedNode)
                   Composer.menu.show(node.ui.getAnchor());
   			});
            Composer.tree = tree;
			Composer.menu = new Ext.menu.Menu({ id: 'menuContext',
			                                    items: [ { text: 'Delete', handler: Composer.deleteItem } ]
			});

            Composer.root = new Tree.TreeNode({
                text: 'Compositions', 
                allowDrag:false,
                allowDrop:false
            });
            tree.setRootNode(Composer.root);
            tree.render();
            Composer.root.expand();

			this.detailsTemplate = new Ext.Template(
				'<div class="details"><b class="details-header">Folien Eigenschaften</b><img src="{url}"><div class="details-info">' +
				'<b>Name:</b>' +
				'<span>{name}</span>' +
				'<b>Geändert am:</b>' +
				'<span>{dateString}</span></div></div>'
			);
			this.detailsTemplate.compile();	

            // create the required templates
        	var tpl = new Ext.Template(
        		'<div class="thumb-wrap" id="{pkey}">' +
        		'<div class="thumb"><img src="{url}" class="thumb-img"></div>' +
        		'<span>{shortName}</span></div>'
        	);

        	var qtipTpl = new Ext.Template(
        	    '<div class="image-tip"><img src="{url}" align="left">'+
        		'<b>Name:</b>' +
        		'<span>{shortName}</span>' +
        		'<b>Geändert am:</b>' +
        		'<span>{dateString}</span></div>'
        	);
        	qtipTpl.compile();	

        	// initialize the View		
        	Composer.view = new Ext.JsonView(imgBody, tpl, {
        		multiSelect: true,
        		jsonRoot: 'images'
        	});
            var view = Composer.view;
       
        	Composer.lookup = {};

        	view.prepareData = function(data)
        	{
	            data.shortName = data.name.ellipse(15);
	            data.dateString = new Date(data.lastmod).format("d.m.Y H:i:s");
	            data.qtip = new String(qtipTpl.applyTemplate(data));
	            Composer.lookup[data.pkey] = data;
	            return data;
            };
		    view.on('selectionchange', this.showDetails, this, {buffer:100});
		    view.on('beforeselect', function(view)
		    {
		        return view.getCount() > 0;
		    });

            var dragZone = new ImageDragZone(view, {containerScroll:true, ddGroup: 'organizerDD'});

            view.load({
                url: 'ppts.jsp?browser='+browserId
            });
            initTree();
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
	
        deleteItem : function( menuItem )
        {
           var node = Composer.tree.selModel.selNode;
		   if(node)
             node.parentNode.removeChild(node);
        },
        
        save: function()
        {
            var children = Composer.root.childNodes[0].childNodes;
		    var url = "save.jsp?pkey="+pkey+"&browser="+browserId
            for(var i=0; i < children.length; i++)
		    {
		      url = url +"&ppt_"+i+"="+children[i].attributes.data.pkey;
		    }
    		Ext.Ajax.request({
	 			url : url , 
				method: 'GET',
				success: function ( result, request ) { 
					Composer.msg('Speichern', 'Folie wurde gesichert.');
				},
				failure: function ( result, request) { 
					Ext.MessageBox.alert('Error', result.responseText); 
				} 
			});
   	    },
   	    

		showDetails : function(view, nodes)
		{
		    var selNode = nodes[0];
			if(selNode && this.view.getCount() > 0){
			    var data = Composer.lookup[selNode.id];
	            this.detailEl.hide();
	            this.detailsTemplate.overwrite(this.detailEl, data);
	            this.detailEl.slideIn('l', {stopFx:true,duration:.2});
				
			}else{
			    this.detailEl.update('');
			}
		},
     addComposition: function(name){
          return Composer.root.appendChild(
              new Tree.TreeNode({text:name, cls:'album-node', allowDrag:false})
          );
     }
    };
}();

Ext.EventManager.onDocumentReady(Composer.init, Composer, true);

/**
 * Create a DragZone instance for our JsonView
 */
ImageDragZone = function(view, config){
    this.view = view;
    ImageDragZone.superclass.constructor.call(this, view.getEl(), config);
};
Ext.extend(ImageDragZone, Ext.dd.DragZone, {
    // We don't want to register our image elements, so let's 
    // override the default registry lookup to fetch the image 
    // from the event instead
    getDragData : function(e){
        e = Ext.EventObject.setEvent(e);
        var target = e.getTarget('.thumb-wrap');
        if(target){
            var view = this.view;
            if(!view.isSelected(target)){
                view.select(target, e.ctrlKey);
            }
            var selNodes = view.getSelectedNodes();
            var dragData = {
                nodes: selNodes
            };
            if(selNodes.length == 1){
                dragData.ddel = target.firstChild.firstChild; // the img element
                dragData.single = true;
            }else{
                var div = document.createElement('div'); // create the multi element drag "ghost"
                div.className = 'multi-proxy';
                for(var i = 0, len = selNodes.length; i < len; i++){
                    div.appendChild(selNodes[i].firstChild.firstChild.cloneNode(true));
                    if((i+1) % 3 == 0){
                        div.appendChild(document.createElement('br'));
                    }
                }
                dragData.ddel = div;
                dragData.multi = true;
            }
            return dragData;
        }
        return false;
    },
    
    // this method is called by the TreeDropZone after a node drop 
    // to get the new tree node (there are also other way, but this is easiest)
    getTreeNode : function(){
        var treeNodes = [];
        var nodeData = this.view.getNodeData(this.dragData.nodes);
        for(var i = 0, len = nodeData.length; i < len; i++)
        {
            var data = nodeData[i];
            treeNodes.push(new Ext.tree.TreeNode({
                text: data.name,
                icon: data.url,
                data: data,
                leaf:true,
                cls: 'image-node',
                qtip: data.qtip
            }));
        }
        return treeNodes;
    },
    
    // the default action is to "highlight" after a bad drop
    // but since an image can't be highlighted, let's frame it 
    afterRepair:function(){
        for(var i = 0, len = this.dragData.nodes.length; i < len; i++){
            Ext.fly(this.dragData.nodes[i]).frame('#8db2e3', 1);
        }
        this.dragging = false;    
    },
    
    // override the default repairXY with one offset for the margins and padding
    getRepairXY : function(e){
        if(!this.dragData.multi){
            var xy = Ext.Element.fly(this.dragData.ddel).getXY();
            xy[0]+=3;xy[1]+=3;
            return xy;
        }
        return false;
    }
});


Ext.tree.TreePanel.prototype.toXMLString=function() {
  return "<?xml version='1.0'?><tree>" + this.nodeToXMLString (this.getRootNode()) + "</tree>";
}
/*
    Recursive function
    sweeps the tree in all levels
*/
Ext.tree.TreePanel.prototype.nodeToXMLString = function(node) {
    var result = "<node id='" + node.id + "'>";
    var data = node.attributes["data"];
    var children = node.childNodes;
    for(var i=0; i < children.length; i++) {
        result += this.nodeToXMLString (children[i]);
    }
    return result + "</node>";
};


// Utility functions

String.prototype.ellipse = function(maxLength){
    if(this.length > maxLength){
        return this.substr(0, maxLength-3) + '...';
    }
    return this;
};