function BCD2DEC()
{
  SimulationObjectImage.call(this);

  this.inputPort1 = null;
  this.inputPort2 = null;
  this.inputPort3 = null;
  this.inputPort4 = null;
  this.inputPort5 = null;
  this.inputPort6 = null;
  this.inputPort7 = null;

  this.value1 = false;
  this.value2 = false;
  this.value3 = false;
  this.value4 = false;
  this.value5 = false;
  this.value6 = false;
  this.value7 = false;

  this.setDimension(64,94);
}

BCD2DEC.prototype = new SimulationObjectImage;
BCD2DEC.prototype.type="BCD2DEC";

BCD2DEC.prototype.calculate=function()
{
  var newValue1 = this.inputPort1.getProperty("value");
  var newValue2 = this.inputPort2.getProperty("value");
  var newValue3 = this.inputPort3.getProperty("value");
  var newValue4 = this.inputPort4.getProperty("value");
  var newValue5 = this.inputPort5.getProperty("value");
  var newValue6 = this.inputPort6.getProperty("value");
  var newValue7 = this.inputPort7.getProperty("value");

  if(newValue1 != this.value1)
  {
    this.segment1.style.backgroundPosition="0px "+(newValue1?(-94*1):0)+"px";
    this.value1 = newValue1;
  }
  if(newValue2 != this.value2)
  {
    this.segment2.style.backgroundPosition="0px "+(newValue2?(-94*2):0)+"px";
    this.value2 = newValue2;
  }
  if(newValue3 != this.value3)
  {
    this.segment3.style.backgroundPosition="0px "+(newValue3?(-94*3):0)+"px";
    this.value3 = newValue3;
  }
  if(newValue4 != this.value4)
  {
    this.segment4.style.backgroundPosition="0px "+(newValue4?(-94*4):0)+"px";
    this.value4 = newValue4;
  }
  if(newValue5 != this.value5)
  {
    this.segment5.style.backgroundPosition="0px "+(newValue5?(-94*5):0)+"px";
    this.value5 = newValue5;
  }
  if(newValue6 != this.value6)
  {
    this.segment6.style.backgroundPosition="0px "+(newValue6?(-94*6):0)+"px";
    this.value6 = newValue6;
  }
  if(newValue7 != this.value7)
  {
    this.segment7.style.backgroundPosition="0px "+(newValue7?(-94*7):0)+"px";
    this.value7 = newValue7;
  }
}


BCD2DEC.prototype.setWorkflow=function(workflow /*:Workflow*/)
{
  SimulationObjectImage.prototype.setWorkflow.call(this,workflow);

  if(workflow!=null && this.inputPort1==null)
  {
    this.inputPort1 = new InputPort();
    this.inputPort1.setWorkflow(workflow);
    this.inputPort1.setName("port1");

    this.inputPort2 = new InputPort();
    this.inputPort2.setWorkflow(workflow);
    this.inputPort2.setName("port2");

    this.inputPort3 = new InputPort();
    this.inputPort3.setWorkflow(workflow);
    this.inputPort3.setName("port3");

    this.inputPort4 = new InputPort();
    this.inputPort4.setWorkflow(workflow);
    this.inputPort4.setName("port4");

    this.addPort(this.inputPort1,0,12 );
    this.addPort(this.inputPort2,0,24 );
    this.addPort(this.inputPort3,0,36 );
    this.addPort(this.inputPort4,0,48 );
    this.addPort(this.inputPort5,0,60 );
    this.addPort(this.inputPort6,0,72 );
    this.addPort(this.inputPort7,0,84 );
  }
}
