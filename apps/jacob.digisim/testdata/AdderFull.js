function AdderFull()
{
  SimulationObjectImage.call(this);
  this.setDimension(31,47);
  this.setResizeable(false);
}

AdderFull.prototype = new SimulationObjectImage;
AdderFull.prototype.type="AdderFull";



AdderFull.prototype.setWorkflow=function(workflow /*:Workflow*/)
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

    this.inputPort3 = new InputPort(new ImageFigure("port.png"));
    this.inputPort3.setWorkflow(workflow);
    this.inputPort3.setName("port3");

    this.outputPort1 = new OutputPort(new ImageFigure("port.png"));
    this.outputPort1.setWorkflow(workflow);
    this.outputPort1.setMaxFanOut(100);
    this.outputPort1.setName("port4");

    this.outputPort2 = new OutputPort(new ImageFigure("port.png"));
    this.outputPort2.setWorkflow(workflow);
    this.outputPort2.setMaxFanOut(100);
    this.outputPort2.setName("port5");

    // Add the port to this object at the left/middle position
    this.addPort(this.inputPort1,-5,10);
    this.addPort(this.inputPort2,-5,this.height/2);
    this.addPort(this.inputPort3,-5,this.height-10);
    this.addPort(this.outputPort1,this.width+5,10);
    this.addPort(this.outputPort2,this.width+5,this.height-10);
  }
}


AdderFull.prototype.calculate=function()
{
   var input0 = this.inputPort1.getProperty("value");
   var input1 = this.inputPort2.getProperty("value");
   var input2 = this.inputPort3.getProperty("value");

   // no carry
   if(input2==false)
   {
      // 0+0
      if(input0==false && input1==false)
      {
        this.outputPort1.setProperty("value",false);
        this.outputPort2.setProperty("value",false);
      }
      // 1+1
      else if(input0==true && input1==true)
      {
        this.outputPort1.setProperty("value",false);
        this.outputPort2.setProperty("value",true);
      }
      // 1+0
      else
      {
        this.outputPort1.setProperty("value",true);
        this.outputPort2.setProperty("value",false);
      }
   }
   else
   {
      // 1+0+0
      if(input0==false && input1==false)
      {
        this.outputPort1.setProperty("value",true);
        this.outputPort2.setProperty("value",false);
      }
      // 1+1+1
      else if(input0==true && input1==true)
      {
        this.outputPort1.setProperty("value",true);
        this.outputPort2.setProperty("value",true);
      }
      // 1+1+0
      else
      {
        this.outputPort1.setProperty("value",false);
        this.outputPort2.setProperty("value",true);
      }
   }
}
