
flow.Application=function()
{
  Position.includeScrollOffsets=true;

  this.id = flow.UUID.create();
  this.commandStack = new flow.CommandStack();
  this.parts = [];
  this.canvas = new flow.Canvas("paintarea","scrollarea");
  this.relatedFields = [];
  this.predefinedVariables=[];
  
  $$(".palette_part").each(function(element,index)
  {
    this.parts.push(new flow.PalettePart(this, element));
  }.bind(this));
};


flow.Application.prototype.renameVariable=function(/*:String*/ oldVar, /*:String*/ newVar)
{
  // it is not possible to rename predefined Variables. Predefined Variables are set by the app-programmer
  // via script.
  if(this.predefinedVariables.indexOf(oldVar)!==-1)
     return; // silently
     
  this.canvas.blocks.each(function(element,index)
  {
    element.renameVariable(oldVar, newVar);
  });
};


flow.Application.prototype.getPredefinedVariables=function()
{
  return this.predefinedVariables;
};


flow.Application.prototype.getAllVariables=function()
{
  var result = [];
  this.canvas.blocks.each(function(element,index)
  {
    element.getAllVariables(result);
  });
  
  return result.concat(this.predefinedVariables).uniq().sort();
};


flow.Application.prototype.getRelatedFields=function()
{
  return this.relatedFields;
};


/**
 * Set the new id of the cloude node element.
 *
 * @param {String} if The new id of the model element
 **/
flow.Application.prototype.setId=function(/*:String*/ id )
{
};

flow.Application.prototype.getCanvas=function()
{
  return this.canvas;
};


flow.Application.prototype.getCommandStack=function()
{
  return this.commandStack;
};

flow.Application.prototype.execute=function(/*:flow.Command*/ command)
{
    if(command instanceof flow.Command)
      this.commandStack.execute(command);
    else
      throw "Command doesn't implement required interface [flow.Command]";
};


flow.Application.prototype.saveDocument=function(/*:String*/ docId, /*:function*/ onSuccess)
{
   var xml = flow.ModelXMLSerializer.toXML(app.getCanvas().blocks);
   xml = ["<flow>",xml,"</flow>"].join("\n");
   var req = new Ajax.Request(flow.Configuration.SAVE_XML,
   {
    method: 'post',
    parameters:
    {
      guid: documentId,
      browser: browserId,
      content : xml
    },
    onFailure: function (transport)
    { 
       alert("error");
    },
    onSuccess:onSuccess
  });
};


flow.Application.prototype.deleteDocument=function(/*:String*/ docId, /*:function*/ onSuccess)
{
   var req = new Ajax.Request(flow.Configuration.SAVE_XML,
   {
    method: 'post',
    parameters:
    {
      guid: documentId,
      browser: browserId,
      content : ""
    },
    onFailure: function (transport)
    { 
       alert("error");
    },
    onSuccess:onSuccess
  });
};


flow.Application.prototype.loadDocument=function(/*:String*/ docId)
{
  // Wenn das Backend geladen wurde kann die Importer definition angezeigt werden.
  // Vorher macht dies keinen Sinn, da man keine Tablen, Columns, UniqueKey Definitionen hat.
  // API Documentation: http://www.prototypejs.org/api/ajax/request
  //
  var req= new Ajax.Request(flow.Configuration.GET_XML,
  {
    method: 'get',
    parameters:
    {
      guid: docId,
      browser: browserId
    },
    onFailure: function (transport)
    { 
      alert("error");
    }.bind(this),
    onSuccess: function (transport)
    { 
      try
      {
        flow.ModelXMLDeserializer.fromXML(this, transport.responseXML.firstChild);
      }
      catch(e)
      {
        alert("Edit Document\n"+e);
      }
    }.bind(this)
  });
  
  this.loadPredefinedVariables(docId);
  this.loadRelatedFields(docId);
};

flow.Application.prototype.loadPredefinedVariables=function(/*:String*/ docId)
{
  // Wenn das Backend geladen wurde kann die Importer definition angezeigt werden.
  // Vorher macht dies keinen Sinn, da man keine Tablen, Columns, UniqueKey Definitionen hat.
  // API Documentation: http://www.prototypejs.org/api/ajax/request
  //
  var req= new Ajax.Request(flow.Configuration.GET_PREDEFINED_VARIABLES,
  {
    method: 'get',
    parameters:
    { 
      guid: docId,
      browser: browserId
    },
    onFailure: function (transport)
    { 
      alert("error during loading fields");
    }.bind(this),
    onSuccess: function (transport)
    { 
      this.predefinedVariables = transport.responseJSON.uniq().sort();
    }.bind(this)
  });
};



flow.Application.prototype.loadRelatedFields=function(/*:String*/ docId)
{
  // Wenn das Backend geladen wurde kann die Importer definition angezeigt werden.
  // Vorher macht dies keinen Sinn, da man keine Tablen, Columns, UniqueKey Definitionen hat.
  // API Documentation: http://www.prototypejs.org/api/ajax/request
  //
  var req= new Ajax.Request(flow.Configuration.GET_RELATED_FIELDS,
  {
    method: 'get',
    parameters:
    { 
      guid: docId,
      browser: browserId
    },
    onFailure: function (transport)
    { 
      alert("error during loading fields");
    }.bind(this),
    onSuccess: function (transport)
    { 
      this.relatedFields = transport.responseJSON.uniq().sort();
    }.bind(this)
  });
};


