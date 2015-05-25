package jacob.event.ui.chartconfig;

import jacob.common.AppLogger;

import java.awt.Color;
import java.util.Calendar;

import org.apache.commons.logging.Log;

import de.tif.jacob.core.data.IDataAccessor;
import de.tif.jacob.core.data.IDataTable;
import de.tif.jacob.core.data.IDataTableRecord;
import de.tif.jacob.i18n.ApplicationMessage;
import de.tif.jacob.screen.IChart;
import de.tif.jacob.screen.IClientContext;
import de.tif.jacob.screen.event.ILineChartEventHandler;



/**
 * @author achim
 * 
 */
public class Statisticchart extends ILineChartEventHandler
{
    static public final transient String RCS_ID = "$Id$";

    static public final transient String RCS_REV = "$Revision$";

    // use this logger to write messages and NOT the System.out.println(..) ;-)
    static private final transient Log logger = AppLogger.getLogger();

    static private final String STATISTIC_CHARTDATA = "STATISTIC_CHARTDATA";

    static private final int ROW_TABLENAME = 0;

    static private final int ROW_LEGEND_NAME = 1;
    
    static private final int ROW_FIELD_REPORTED = 2;
    static private final int ROW_MONTH = 3;

    // private class for collecting chart data
    static private class ChartData
    {

        private String title;

        private String xAxis;

        private String yAxis;

        private String[] xAxisLabels;

        private String[] legendLabels;

        private Color[] color;

        private double[][] dataElements;

        public String[][] lineInfo;

        public Color[] getColor()
        {
            return color;
        }

        public String[][] getLineInfo()
        {
            return lineInfo;
        }

        public double[][] getDataElements()
        {
            return dataElements;
        }

        public String[] getLegendLabels()
        {
            return legendLabels;
        }

        public String getXAxis()
        {
            return xAxis;
        }

        public String[] getXAxisLabels()
        {
            return xAxisLabels;
        }

        public String getYAxis()
        {
            return yAxis;
        }

        public String getTitle()
        {
            return title;
        }

        protected double getCount(IDataAccessor accessor, String[][] lineInfo, int index) throws Exception
        {
            accessor.clear();
            
            IDataTable table = accessor.getTable(lineInfo[ROW_TABLENAME][index]);
            
            // constraint table to display
            table.qbeSetValue(lineInfo[ROW_FIELD_REPORTED][index], "thism"); // this month
            table.setMaxRecords(IDataTable.UNLIMITED_RECORDS);
            return table.search();
        }

        protected String[][] getTableLineInfo(IDataAccessor accessor,IDataTableRecord chartConfigRec) throws Exception
        {

            int lineCount=0;
            // first I need to find ou how many lines shoud be display
            if (chartConfigRec.getintValue("showactivity")==1)
            {
                lineCount=lineCount+1;
            }
            if (chartConfigRec.getintValue("showcalls")==1)
            {
                lineCount=lineCount+1;
            }
            if (chartConfigRec.getintValue("showdefects")==1)
            {
                lineCount=lineCount+1;
            }
            if (chartConfigRec.getintValue("showincident")==1)
            {
                lineCount=lineCount+1;
            }
            if (chartConfigRec.getintValue("showorder")==1)
            {
                lineCount=lineCount+1;
            }
            if (chartConfigRec.getintValue("showquote")==1)
            {
                lineCount=lineCount+1;
            }
            
            // build TableLineInfo
            String[][] data = new String[4][lineCount];
            int currentCol =0;
            if (chartConfigRec.getintValue("showactivity")==1)
            {
                data[ROW_TABLENAME][currentCol]="activity";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Activities");
                data[ROW_FIELD_REPORTED][currentCol]="datecreated";
                currentCol =currentCol+1;
            }
            if (chartConfigRec.getintValue("showcalls")==1)
            {
                data[ROW_TABLENAME][currentCol]="call";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Calls");
                data[ROW_FIELD_REPORTED][currentCol]="datereported";
                currentCol =currentCol+1;
            }
            if (chartConfigRec.getintValue("showdefects")==1)
            {
                data[ROW_TABLENAME][currentCol]="defect";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Defects");
                data[ROW_FIELD_REPORTED][currentCol]="datereported";
                currentCol =currentCol+1;
            }
            if (chartConfigRec.getintValue("showincident")==1)
            {
                data[ROW_TABLENAME][currentCol]="incident";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Incidents");
                data[ROW_FIELD_REPORTED][currentCol]="datecreated";
                currentCol =currentCol+1;
            }
            if (chartConfigRec.getintValue("showorder")==1)
            {
                data[ROW_TABLENAME][currentCol]="orderheader";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Orders");
                data[ROW_FIELD_REPORTED][currentCol]="datecreated";
                currentCol =currentCol+1;
            }
            if (chartConfigRec.getintValue("showquote")==1)
            {
                data[ROW_TABLENAME][currentCol]="quoteheader";
                data[ROW_LEGEND_NAME][currentCol]=ApplicationMessage.getLocalized("Statisticchart.Quotes");
                data[ROW_FIELD_REPORTED][currentCol]="datecreated";
                currentCol =currentCol+1;
            }

            return data;
            
  
        }

        ChartData(IClientContext context) throws Exception
        {
            IDataTableRecord chartConfigRec = context.getSelectedRecord();
//            boolean isMyTaskSearch = "mytasks".equals((String) context.getProperty("charttype"));
            int maxMonth = context.getSelectedRecord().getintValue("linechartmonth");
            IDataAccessor accessor = context.getDataAccessor().newAccessor();

            this.lineInfo = getTableLineInfo(accessor,chartConfigRec);

            this.title = ApplicationMessage.getLocalized("Statisticchart.Statistics");
            this.xAxis = ApplicationMessage.getLocalized("Statisticchart.Month");
            this.yAxis = ApplicationMessage.getLocalized("Statisticchart.Count");
            this.legendLabels = lineInfo[ROW_LEGEND_NAME];
            
            IDataTable chartData = accessor.getTable("chartdata");
            //current month will calculated
            chartData.setMaxRecords(maxMonth-1);
            chartData.clear();
            chartData.search();
            chartData.setMaxRecords(100);
            int recordCount = chartData.recordCount();
            String[] myXAxisLables =new String[recordCount+1]; // I want RrecordCount +1 month display
            double[][] data = new double[lineInfo[ROW_LEGEND_NAME].length][recordCount+1];
            for (int ix = 0; ix < recordCount; ix++)
            {         
                IDataTableRecord record = chartData.getRecord(ix);
                myXAxisLables[ix]= record.getSaveStringValue("month");
                
                // values of all lines
                for (int jy = 0; jy < lineInfo[ROW_TABLENAME].length; jy++)
                {
                    data[jy][ix] = record.getintValue(lineInfo[ROW_TABLENAME][jy]);
                }
            }
            // values of the actual month

            Color[] myColors = new Color[lineInfo[ROW_LEGEND_NAME].length];
                                  

            for (int i = 0; i < lineInfo[ROW_TABLENAME].length; i++)
            {
                myColors[i] =new Color((int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), (int) getRandomNumber(1, 255), 255);
                data[i][recordCount] = getCount(accessor, lineInfo, i);
            }
            this.color = myColors;
            Calendar cal = Calendar.getInstance();
            
            myXAxisLables[recordCount]=Integer.toString(cal.get(Calendar.MONTH)+1);
            this.xAxisLabels =myXAxisLables;
            this.dataElements = data;
        }

        /**
         * little help method for test data
         */
        private static double getRandomNumber(double minValue, double maxValue) throws Exception
        {
            return (minValue + (Math.random() * (maxValue - minValue)));
        }
    }

    // -----------------------------------------------------------------

    private static ChartData getChartData(IClientContext context) throws Exception
    {
        ChartData data = (ChartData) context.getProperty(STATISTIC_CHARTDATA);
        if (data != null)
            return data;

        data = new ChartData(context);
        context.setPropertyForRequest(STATISTIC_CHARTDATA, data);
        return data;
    }

    /**
     * 
     * @param context
     *            the current working context
     * @param chart
     *            the chart GUI element
     * @return The title of the line chart
     */
    public String getTitle(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getTitle();
    }

    /**
     * 
     * @param context
     *            the current working context
     * @param chart
     *            the chart GUI element
     * @return The caption for the x-axis
     */
    public String getXAxisTitle(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getXAxis();
    }

    /**
     * 
     * @param context
     *            the current working context
     * @param chart
     *            the chart GUI element
     * @return The caption for the y-axis
     */
    public String getYAxisTitle(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getYAxis();
    }

    /**
     * Return the labels for the x-axis. The count of the labels <b>must</b> be
     * match with the count of the returnd data elements.
     * 
     * @param context
     *            the current working context
     * @param chart
     *            the chart GUI element
     * @return the labels for the x-axis
     */
    public String[] getXAxisLabels(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getXAxisLabels();
    }

    /**
     * Return the legend label for each data set/line in the chart.
     * 
     * @param context
     * @param chart
     * @returnReturn the legend label for each data set/line in the chart.
     */
    public String[] getLegendLabels(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getLegendLabels();
    }

    /**
     * Return the different colors of the chart lines.
     * 
     * @param context
     * @param chart
     * @return Return the different colors of the chart lines.
     */
    public Color[] getColors(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getColor();
    }

    /**
     * Return a array of data values.<br>
     * 
     * @param context
     *            the current working context
     * @param chart
     *            the chart GUI element
     * @return
     */
    public double[][] getDataElements(IClientContext context, IChart chart) throws Exception
    {
        return getChartData(context).getDataElements();
    }

}
