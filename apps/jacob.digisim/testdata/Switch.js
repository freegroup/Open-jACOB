function Switch()
{
  SimulationObjectImage.call(this);
  this.setDimension(30,30);
  this.hasPower=true;
}

Switch.prototype = new SimulationObjectImage;
Switch.prototype.type="Switch";

Switch.prototype.calculate=function()
{
  this.outputPort.setProperty("value",this.hasPower);
}


/**
 * Will be called if the object are move via drag and drop.
 * @param {int} x x position of the mouse in the window
 * @param {int} y y position of the mouse in the window
 **/
Switch.prototype.onDragstart = function(x /*:int*/, y/*:int*/)
{
  /*:NAMESPACE*/Figure.prototype.onDragstart.call(this,x,y);

  // Return only true if the user klicks into the tilebar.
  // (Titlebar is the DragDrop handle for a window)
  //
  if(!this.isSimulationRunning())
    return true;

  // add additional checks for the bootm/right resize handle
  // TODO
  this.hasPower = !this.hasPower
  if(this.hasPower==true)
    this.html.style.backgroundPosition="0px 0px";
  else
    this.html.style.backgroundPosition="0px 30px";

  return false;
}

Switch.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  SimulationObjectImage.prototype.setWorkflow.call(this,workflow);

  if(workflow!=null && this.outputPort==null)
  {
    this.outputPort = new OutputPort(new ImageFigure("port.png"));
    this.outputPort.setMaxFanOut(100);
    this.outputPort.setWorkflow(workflow);
    this.outputPort.setName("port1");

    this.addPort(this.outputPort,this.width,this.height/2);
  }
}