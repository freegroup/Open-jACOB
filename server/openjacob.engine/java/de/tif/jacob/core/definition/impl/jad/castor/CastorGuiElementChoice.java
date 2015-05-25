/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package de.tif.jacob.core.definition.impl.jad.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CastorGuiElementChoice.
 * 
 * @version $Revision$ $Date$
 */
public class CastorGuiElementChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _localInputField
     */
    private de.tif.jacob.core.definition.impl.jad.castor.LocalInputField _localInputField;

    /**
     * Field _foreignInputField
     */
    private de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField _foreignInputField;

    /**
     * Field _inFormBrowser
     */
    private de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser _inFormBrowser;

    /**
     * Field _tableListBox
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TableListBox _tableListBox;

    /**
     * Field _flowLayoutContainer
     */
    private de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer _flowLayoutContainer;

    /**
     * Field _button
     */
    private de.tif.jacob.core.definition.impl.jad.castor.CastorButton _button;

    /**
     * Field _label
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Label _label;

    /**
     * Field _pluginComponent
     */
    private de.tif.jacob.core.definition.impl.jad.castor.PluginComponent _pluginComponent;

    /**
     * Field _ownDrawElement
     */
    private de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement _ownDrawElement;

    /**
     * Field _barChart
     */
    private de.tif.jacob.core.definition.impl.jad.castor.BarChart _barChart;

    /**
     * Field _lineChart
     */
    private de.tif.jacob.core.definition.impl.jad.castor.LineChart _lineChart;

    /**
     * Field _timeLine
     */
    private de.tif.jacob.core.definition.impl.jad.castor.TimeLine _timeLine;

    /**
     * Field _calendar
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Calendar _calendar;

    /**
     * Field _styledText
     */
    private de.tif.jacob.core.definition.impl.jad.castor.StyledText _styledText;

    /**
     * Field _breadCrumb
     */
    private de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb _breadCrumb;

    /**
     * Field _staticImage
     */
    private de.tif.jacob.core.definition.impl.jad.castor.StaticImage _staticImage;

    /**
     * Field _dynamicImage
     */
    private de.tif.jacob.core.definition.impl.jad.castor.DynamicImage _dynamicImage;

    /**
     * Field _container
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Container _container;

    /**
     * Field _heading
     */
    private de.tif.jacob.core.definition.impl.jad.castor.Heading _heading;


      //----------------/
     //- Constructors -/
    //----------------/

    public CastorGuiElementChoice() {
        super();
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'barChart'.
     * 
     * @return the value of field 'barChart'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.BarChart getBarChart()
    {
        return this._barChart;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BarChart getBarChart() 

    /**
     * Returns the value of field 'breadCrumb'.
     * 
     * @return the value of field 'breadCrumb'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb getBreadCrumb()
    {
        return this._breadCrumb;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb getBreadCrumb() 

    /**
     * Returns the value of field 'button'.
     * 
     * @return the value of field 'button'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.CastorButton getButton()
    {
        return this._button;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.CastorButton getButton() 

    /**
     * Returns the value of field 'calendar'.
     * 
     * @return the value of field 'calendar'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Calendar getCalendar()
    {
        return this._calendar;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Calendar getCalendar() 

    /**
     * Returns the value of field 'container'.
     * 
     * @return the value of field 'container'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Container getContainer()
    {
        return this._container;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Container getContainer() 

    /**
     * Returns the value of field 'dynamicImage'.
     * 
     * @return the value of field 'dynamicImage'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.DynamicImage getDynamicImage()
    {
        return this._dynamicImage;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.DynamicImage getDynamicImage() 

    /**
     * Returns the value of field 'flowLayoutContainer'.
     * 
     * @return the value of field 'flowLayoutContainer'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer getFlowLayoutContainer()
    {
        return this._flowLayoutContainer;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer getFlowLayoutContainer() 

    /**
     * Returns the value of field 'foreignInputField'.
     * 
     * @return the value of field 'foreignInputField'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField getForeignInputField()
    {
        return this._foreignInputField;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField getForeignInputField() 

    /**
     * Returns the value of field 'heading'.
     * 
     * @return the value of field 'heading'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Heading getHeading()
    {
        return this._heading;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Heading getHeading() 

    /**
     * Returns the value of field 'inFormBrowser'.
     * 
     * @return the value of field 'inFormBrowser'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser getInFormBrowser()
    {
        return this._inFormBrowser;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser getInFormBrowser() 

    /**
     * Returns the value of field 'label'.
     * 
     * @return the value of field 'label'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.Label getLabel()
    {
        return this._label;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.Label getLabel() 

    /**
     * Returns the value of field 'lineChart'.
     * 
     * @return the value of field 'lineChart'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.LineChart getLineChart()
    {
        return this._lineChart;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LineChart getLineChart() 

    /**
     * Returns the value of field 'localInputField'.
     * 
     * @return the value of field 'localInputField'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.LocalInputField getLocalInputField()
    {
        return this._localInputField;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.LocalInputField getLocalInputField() 

    /**
     * Returns the value of field 'ownDrawElement'.
     * 
     * @return the value of field 'ownDrawElement'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement getOwnDrawElement()
    {
        return this._ownDrawElement;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement getOwnDrawElement() 

    /**
     * Returns the value of field 'pluginComponent'.
     * 
     * @return the value of field 'pluginComponent'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.PluginComponent getPluginComponent()
    {
        return this._pluginComponent;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.PluginComponent getPluginComponent() 

    /**
     * Returns the value of field 'staticImage'.
     * 
     * @return the value of field 'staticImage'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.StaticImage getStaticImage()
    {
        return this._staticImage;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.StaticImage getStaticImage() 

    /**
     * Returns the value of field 'styledText'.
     * 
     * @return the value of field 'styledText'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.StyledText getStyledText()
    {
        return this._styledText;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.StyledText getStyledText() 

    /**
     * Returns the value of field 'tableListBox'.
     * 
     * @return the value of field 'tableListBox'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TableListBox getTableListBox()
    {
        return this._tableListBox;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TableListBox getTableListBox() 

    /**
     * Returns the value of field 'timeLine'.
     * 
     * @return the value of field 'timeLine'.
     */
    public de.tif.jacob.core.definition.impl.jad.castor.TimeLine getTimeLine()
    {
        return this._timeLine;
    } //-- de.tif.jacob.core.definition.impl.jad.castor.TimeLine getTimeLine() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'barChart'.
     * 
     * @param barChart the value of field 'barChart'.
     */
    public void setBarChart(de.tif.jacob.core.definition.impl.jad.castor.BarChart barChart)
    {
        this._barChart = barChart;
    } //-- void setBarChart(de.tif.jacob.core.definition.impl.jad.castor.BarChart) 

    /**
     * Sets the value of field 'breadCrumb'.
     * 
     * @param breadCrumb the value of field 'breadCrumb'.
     */
    public void setBreadCrumb(de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb breadCrumb)
    {
        this._breadCrumb = breadCrumb;
    } //-- void setBreadCrumb(de.tif.jacob.core.definition.impl.jad.castor.BreadCrumb) 

    /**
     * Sets the value of field 'button'.
     * 
     * @param button the value of field 'button'.
     */
    public void setButton(de.tif.jacob.core.definition.impl.jad.castor.CastorButton button)
    {
        this._button = button;
    } //-- void setButton(de.tif.jacob.core.definition.impl.jad.castor.CastorButton) 

    /**
     * Sets the value of field 'calendar'.
     * 
     * @param calendar the value of field 'calendar'.
     */
    public void setCalendar(de.tif.jacob.core.definition.impl.jad.castor.Calendar calendar)
    {
        this._calendar = calendar;
    } //-- void setCalendar(de.tif.jacob.core.definition.impl.jad.castor.Calendar) 

    /**
     * Sets the value of field 'container'.
     * 
     * @param container the value of field 'container'.
     */
    public void setContainer(de.tif.jacob.core.definition.impl.jad.castor.Container container)
    {
        this._container = container;
    } //-- void setContainer(de.tif.jacob.core.definition.impl.jad.castor.Container) 

    /**
     * Sets the value of field 'dynamicImage'.
     * 
     * @param dynamicImage the value of field 'dynamicImage'.
     */
    public void setDynamicImage(de.tif.jacob.core.definition.impl.jad.castor.DynamicImage dynamicImage)
    {
        this._dynamicImage = dynamicImage;
    } //-- void setDynamicImage(de.tif.jacob.core.definition.impl.jad.castor.DynamicImage) 

    /**
     * Sets the value of field 'flowLayoutContainer'.
     * 
     * @param flowLayoutContainer the value of field
     * 'flowLayoutContainer'.
     */
    public void setFlowLayoutContainer(de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer flowLayoutContainer)
    {
        this._flowLayoutContainer = flowLayoutContainer;
    } //-- void setFlowLayoutContainer(de.tif.jacob.core.definition.impl.jad.castor.FlowLayoutContainer) 

    /**
     * Sets the value of field 'foreignInputField'.
     * 
     * @param foreignInputField the value of field
     * 'foreignInputField'.
     */
    public void setForeignInputField(de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField foreignInputField)
    {
        this._foreignInputField = foreignInputField;
    } //-- void setForeignInputField(de.tif.jacob.core.definition.impl.jad.castor.ForeignInputField) 

    /**
     * Sets the value of field 'heading'.
     * 
     * @param heading the value of field 'heading'.
     */
    public void setHeading(de.tif.jacob.core.definition.impl.jad.castor.Heading heading)
    {
        this._heading = heading;
    } //-- void setHeading(de.tif.jacob.core.definition.impl.jad.castor.Heading) 

    /**
     * Sets the value of field 'inFormBrowser'.
     * 
     * @param inFormBrowser the value of field 'inFormBrowser'.
     */
    public void setInFormBrowser(de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser inFormBrowser)
    {
        this._inFormBrowser = inFormBrowser;
    } //-- void setInFormBrowser(de.tif.jacob.core.definition.impl.jad.castor.InFormBrowser) 

    /**
     * Sets the value of field 'label'.
     * 
     * @param label the value of field 'label'.
     */
    public void setLabel(de.tif.jacob.core.definition.impl.jad.castor.Label label)
    {
        this._label = label;
    } //-- void setLabel(de.tif.jacob.core.definition.impl.jad.castor.Label) 

    /**
     * Sets the value of field 'lineChart'.
     * 
     * @param lineChart the value of field 'lineChart'.
     */
    public void setLineChart(de.tif.jacob.core.definition.impl.jad.castor.LineChart lineChart)
    {
        this._lineChart = lineChart;
    } //-- void setLineChart(de.tif.jacob.core.definition.impl.jad.castor.LineChart) 

    /**
     * Sets the value of field 'localInputField'.
     * 
     * @param localInputField the value of field 'localInputField'.
     */
    public void setLocalInputField(de.tif.jacob.core.definition.impl.jad.castor.LocalInputField localInputField)
    {
        this._localInputField = localInputField;
    } //-- void setLocalInputField(de.tif.jacob.core.definition.impl.jad.castor.LocalInputField) 

    /**
     * Sets the value of field 'ownDrawElement'.
     * 
     * @param ownDrawElement the value of field 'ownDrawElement'.
     */
    public void setOwnDrawElement(de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement ownDrawElement)
    {
        this._ownDrawElement = ownDrawElement;
    } //-- void setOwnDrawElement(de.tif.jacob.core.definition.impl.jad.castor.OwnDrawElement) 

    /**
     * Sets the value of field 'pluginComponent'.
     * 
     * @param pluginComponent the value of field 'pluginComponent'.
     */
    public void setPluginComponent(de.tif.jacob.core.definition.impl.jad.castor.PluginComponent pluginComponent)
    {
        this._pluginComponent = pluginComponent;
    } //-- void setPluginComponent(de.tif.jacob.core.definition.impl.jad.castor.PluginComponent) 

    /**
     * Sets the value of field 'staticImage'.
     * 
     * @param staticImage the value of field 'staticImage'.
     */
    public void setStaticImage(de.tif.jacob.core.definition.impl.jad.castor.StaticImage staticImage)
    {
        this._staticImage = staticImage;
    } //-- void setStaticImage(de.tif.jacob.core.definition.impl.jad.castor.StaticImage) 

    /**
     * Sets the value of field 'styledText'.
     * 
     * @param styledText the value of field 'styledText'.
     */
    public void setStyledText(de.tif.jacob.core.definition.impl.jad.castor.StyledText styledText)
    {
        this._styledText = styledText;
    } //-- void setStyledText(de.tif.jacob.core.definition.impl.jad.castor.StyledText) 

    /**
     * Sets the value of field 'tableListBox'.
     * 
     * @param tableListBox the value of field 'tableListBox'.
     */
    public void setTableListBox(de.tif.jacob.core.definition.impl.jad.castor.TableListBox tableListBox)
    {
        this._tableListBox = tableListBox;
    } //-- void setTableListBox(de.tif.jacob.core.definition.impl.jad.castor.TableListBox) 

    /**
     * Sets the value of field 'timeLine'.
     * 
     * @param timeLine the value of field 'timeLine'.
     */
    public void setTimeLine(de.tif.jacob.core.definition.impl.jad.castor.TimeLine timeLine)
    {
        this._timeLine = timeLine;
    } //-- void setTimeLine(de.tif.jacob.core.definition.impl.jad.castor.TimeLine) 

    /**
     * Method unmarshalCastorGuiElementChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCastorGuiElementChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice) Unmarshaller.unmarshal(de.tif.jacob.core.definition.impl.jad.castor.CastorGuiElementChoice.class, reader);
    } //-- java.lang.Object unmarshalCastorGuiElementChoice(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
