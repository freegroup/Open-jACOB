function AdderHalf()
{
  SimulationObjectImage.call(this);
  this.setDimension(31,47);
  this.setResizeable(false);
}

AdderHalf.prototype = new SimulationObjectImage;
AdderHalf.prototype.type="AdderHalf";



AdderHalf.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  SimulationObjectImage.prototype.setWorkflow.call(this,workflow);

  if(workflow!=null && this.inputPort1==null)
  {
    this.inputPort1 = new InputPort(new ImageFigure("port.png"));
    this.inputPort1.setWorkflow(workflow);
    this.inputPort1.setName("port1");

    this.inputPort2 = new InputPort(new ImageFigure("port.png"));
    this.inputPort2.setWorkflow(workflow);
    this.inputPort2.setName("port2");

    this.outputPort1 = new OutputPort(new ImageFigure("port.png"));
    this.outputPort1.setWorkflow(workflow);
    this.outputPort1.setMaxFanOut(100);
    this.outputPort1.setName("port3");

    this.outputPort2 = new OutputPort(new ImageFigure("port.png"));
    this.outputPort2.setWorkflow(workflow);
    this.outputPort2.setMaxFanOut(100);
    this.outputPort2.setName("port4");

    // Add the port to this object at the left/middle position
    this.addPort(this.inputPort1,-5,10);
    this.addPort(this.inputPort2,-5,this.height-10);
    this.addPort(this.outputPort1,this.width+5,10);
    this.addPort(this.outputPort2,this.width+5,this.height-10);
  }
}


AdderHalf.prototype.calculate=function()
{
   var value1 = this.inputPort1.getProperty("value");
   var value2 = this.inputPort2.getProperty("value");

    if(value1==false && value2==false)
    {
        this.outputPort1.setProperty("value",false);
        this.outputPort2.setProperty("value",false);
    }
    else if(value1==true && value2==true)
    {
        this.outputPort1.setProperty("value",false);
        this.outputPort2.setProperty("value",true);
    }
    else
    {
        this.outputPort1.setProperty("value",true);
        this.outputPort2.setProperty("value",false);
    }
}
