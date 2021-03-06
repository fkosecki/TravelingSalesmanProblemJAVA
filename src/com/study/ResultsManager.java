package com.study;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Date;

import com.study.gui.Gui;
import com.study.gui.ResultsWindow;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;

public class ResultsManager{
    private double[][] locations;
    //private GeneticAlgorithm genetico; //WSZYSTKO Z TYM ZWIAZANE WYJEBAC!
    //Algorithm algorithm;
    private double optimalRouteCost;
    private int[] optimalRoute;
    private ArrayList<int[]> optimalRoutesHistory = new ArrayList<>();
    private ArrayList<Double> optimalRoutesCostHistory = new ArrayList<>();
    private long runTime;

    public void addToHistory(int[] path, double cost){
        this.optimalRoutesHistory.add(path);
        this.optimalRoutesCostHistory.add(cost);
    }

    public void setOptimalRoutesHistory(ArrayList<int[]> pathHistory){
        this.optimalRoutesHistory = pathHistory;
    }

    public ArrayList<Double> getOptimalRoutesHistoryCost(){
        return this.optimalRoutesCostHistory;
    }

    public void setOptimalRoutesHistoryCost(ArrayList<Double> pathHistoryCost){
        this.optimalRoutesCostHistory = pathHistoryCost;
    }

    public ArrayList<int[]> getOptimalRoutesHistory(){
        return this.optimalRoutesHistory;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public double getOptimalRouteCost() {
        return optimalRouteCost;
    }

    public void setOptimalRouteLength(double optimalRouteCost) {
        this.optimalRouteCost = optimalRouteCost;
    }

    public int[] getOptimalRoute() {
        return optimalRoute;
    }

    public void setOptimalRoute(int[] optimalRoute) {
        this.optimalRoute = optimalRoute;
    }

    public ResultsManager(LocationsDataset locationsDataset){
        double[][] temp = new double[locationsDataset.getLocationsData().size()][2];
        int index = 0;
        for(Location loc : locationsDataset.getLocationsData()){
            temp[index][0] = loc.getX();
            temp[index][1] = loc.getY();
            index++;
        }
        this.locations = temp;

    }

    public ResultsManager(){
    }

    public ApplicationFrame optimalRouteHistoryChart() {
        ApplicationFrame applicationFrame = new ApplicationFrame("Optimal Route History Chart");
        ////////JFreeChart xylineChart = getOptimalRouteHistoryChart(genetico);
        JFreeChart xylineChart = getOptimalRouteHistoryChart();
        //JFreeChart xylineChart = getLocationsChart(locations);
        //JFreeChart xylineChart = getOptimalRouteChart(genetico);
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        applicationFrame.setContentPane( chartPanel );
        return applicationFrame;
    }

    public ApplicationFrame locationsChart() {
        ApplicationFrame applicationFrame = new ApplicationFrame("Locations Chart");
        //JFreeChart xylineChart = getOptimalRouteHistoryChart(genetico);
        JFreeChart xylineChart = getLocationsChart(locations);
        //JFreeChart xylineChart = getOptimalRouteChart(genetico);
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        applicationFrame.setContentPane( chartPanel );
        return applicationFrame;
    }

    public ApplicationFrame optimalRouteChart() {
        ApplicationFrame applicationFrame = new ApplicationFrame("Optimal Route Chart");
        //JFreeChart xylineChart = getOptimalRouteHistoryChart(genetico);
        //JFreeChart xylineChart = getLocationsChart(locations);
        //////////JFreeChart xylineChart = getOptimalRouteChart(genetico);
        JFreeChart xylineChart = getOptimalRouteChart();
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        applicationFrame.setContentPane( chartPanel );
        return applicationFrame;
    }

    private JFreeChart getLocationsChart(double[][] locations){

        JFreeChart xyPointChart = ChartFactory.createScatterPlot("Location XY cordinates","X cordinate","Y cordinate",createLocationsDataset(locations));
        XYPlot plot = xyPointChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false,true);
        renderer.setBaseToolTipGenerator(new RMXYLabelGenerator());
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesShape( 0 , ShapeUtilities.createRegularCross(2,1));
        plot.setRenderer( renderer );
        return xyPointChart;
    }

    private XYDataset createLocationsDataset(double[][] locations){
        XYSeries locationsSeries = new XYSeries("Locations");
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(double[] xy : locations){
            locationsSeries.add(xy[0],xy[1]);
        }
        dataset.addSeries(locationsSeries);

        return dataset;
    }

    public static ApplicationFrame locationsChart(LocationsDataset ld) {
        ApplicationFrame applicationFrame = new ApplicationFrame("Locations Chart");
        JFreeChart xylineChart = getLocationsChart(ld);
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        applicationFrame.setContentPane( chartPanel );
        return applicationFrame;
    }

    private static JFreeChart getLocationsChart(LocationsDataset ld){
        JFreeChart xyPointChart = ChartFactory.createScatterPlot("Location XY cordinates","X cordinate","Y cordinate",createLocationsDataset(ld));
        XYPlot plot = xyPointChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false,true);
        renderer.setBaseToolTipGenerator(new RMXYLabelGenerator());
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesShape( 0 , ShapeUtilities.createRegularCross(2,1));
        plot.setRenderer( renderer );
        return xyPointChart;
    }

    private static XYDataset createLocationsDataset(LocationsDataset ld){
        XYSeries locationsSeries = new XYSeries("Locations");
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(Location loc : ld.getLocationsData()){
            locationsSeries.add(loc.getX(),loc.getY());
        }
        dataset.addSeries(locationsSeries);

        return dataset;
    }

    public void openResultsWindow(){
        JFrame frame = new JFrame();
        ResultsWindow resultsWindow = new ResultsWindow(this);
        resultsWindow.setUrself();
        frame.setContentPane(resultsWindow.getMainWindow());
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart getOptimalRouteHistoryChart(){
        JFreeChart xylineChart = ChartFactory.createScatterPlot("Optimal Route History","Iterations","Distance",createHistoryDataset());
        XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,false);
        renderer.setSeriesPaint( 0 , Color.BLUE );
        renderer.setSeriesStroke( 0 , new BasicStroke(1f) );
        plot.setRenderer( renderer );
        return xylineChart;
    }

    private JFreeChart getOptimalRouteChart(){

        JFreeChart xylineChart = ChartFactory.createScatterPlot("Optimal Route","Cordinate X","Cordinate Y",createOptimalRouteDataset());
        XYPlot plot = xylineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,true);
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesStroke( 0 , new BasicStroke(0.5f) );
        plot.setRenderer( renderer );
        return xylineChart;
    }

    private XYDataset createHistoryDataset(){
        XYSeries optimalRoutesHistorySeries = new XYSeries("");
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(int i = 0; i < this.getOptimalRoutesHistoryCost().size(); i++){
            optimalRoutesHistorySeries.add(i,this.getOptimalRoutesHistoryCost().get(i));
        }
        dataset.addSeries(optimalRoutesHistorySeries);
        return dataset;
    }

    private XYDataset createOptimalRouteDataset(){
        XYSeries dataseries = new XYSeries("Locations",false);
        XYSeriesCollection dataset = new XYSeriesCollection();
        for(int x : this.getOptimalRoute()){
            dataseries.add(this.locations[x][0],this.locations[x][1]);
        }
        dataset.addSeries(dataseries);
        return dataset;

    }

    public static void main( String[ ] args ) {
//        GeneticAlgorithm genetico = new GeneticAlgorithm();
//        genetico.runGeneticAlgorithm("qatar194.txt", 200, 20000, 4, 0, 100,"","");
//        ResultsManager chart = new ResultsManager(genetico.getLocations(),genetico);
////        ResultsManager chart = new ResultsManager("Browser Usage Statistics",
////                "Which Browser are you using?",genetico.getLocations(),genetico);
//        chart.locationsChart().pack( );
//        RefineryUtilities.centerFrameOnScreen( chart.locationsChart() );
//        chart.locationsChart().setVisible( true );
    }
}

class RMXYLabelGenerator implements XYToolTipGenerator {

    private String smth = " lolololo";

    @Override
    public String generateToolTip(XYDataset dataset, int series, int category) {
        String cordinateX = null;
        String cordinateY = null;

        //Number value = dataset.getValue(series, category);
        Number value = (int)dataset.getXValue(series, category);
        Number value1 = (int)dataset.getYValue(series, category);

        if (value != null && value1!=null) {
            cordinateX = value.toString();
            cordinateY = value1.toString();
        }
        return "X: "+cordinateX+"  Y: "+cordinateY+" "+smth;
    }
}
