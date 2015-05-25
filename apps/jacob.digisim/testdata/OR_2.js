function OR_2()
{
  SimulationObjectRectangle.call(this);
  this.setDimension(30,50);
  this.setResizeable(false);
  this.setBackgroundColor(new Color(255,255,255));
}

OR_2.prototype = new SimulationObjectRectangle;
OR_2.prototype.type="OR_2";


/**
 * @private
 **/
OR_2.prototype.createHTMLElement=function()
{
    var item = SimulationObjectRectangle.prototype.createHTMLElement.call(this);
    var label = this.createLabel("&gt;1",3,12);
    label.style.fontSize="12pt";
    item.appendChild(label);
    return item;
}


OR_2.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  SimulationObjectRectangle.prototype.setWorkflow.call(this,workflow);

  if(workflow!=null && this.inputPort==null)
  {
    this.inputPort1 = new InputPort(new ImageFigure("port.png"));
    this.inputPort1.setWorkflow(workflow);
    this.inputPort1.setName("port1");

    this.inputPort2 = new InputPort(new ImageFigure("port.png"));
    this.inputPort2.setWorkflow(workflow);
    this.inputPort2.setName("port2");

    this.outputPort = new OutputPort(new ImageFigure("port.png"));
    this.outputPort.setWorkflow(workflow);
    this.outputPort.setMaxFanOut(100);
    this.outputPort.setName("port3");

    // Add the port to this object at the left/middle position
    this.addPort(this.inputPort1,0,10);
    this.addPort(this.inputPort2,0,this.height-10);
    this.addPort(this.outputPort,this.width,this.height/2);
  }
}


OR_2.prototype.calculate=function()
{
  var value1 = this.inputPort1.getProperty("value");
  var value2 = this.inputPort2.getProperty("value");

  this.outputPort.setProperty("value",value1||value2);
}

