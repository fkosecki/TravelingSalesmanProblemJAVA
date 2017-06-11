package com.study.gui;

import com.study.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class DataGenerateWindow extends Frame implements ActionListener{
    private JButton saveDatasetToFileButton;
    private JButton saveDatasetToDatabaseButton;
    private JButton newDataButton;
    private JTextField newDatasetTextField;
    private JLabel datasetNameLabel;
    private JTable dataTable;
    private JPanel mainWindow;
    private JTextField xField;
    private JTextField tagnameField;
    private JTextField yField;
    private JButton deleteSelectedButton;
    private JPanel generatedDataPanel;
    private JButton addRandomDataButton;
    private JButton loadDatasetFromFileButton;
    private JButton loadDatasetFromDatabaseButton;
    private JButton removeDatasetFromDatabaseButton;
    private JButton changeDatabaseButton;

    private ArrayList<Double[]> locationsList;
    private DefaultTableModel model;
    private String databaseConnectionURL = "jdbc:sqlserver://localhost;database=TSPLocations;integratedSecurity=true;";

    public static void main(String[] args){
        JFrame frame = new JFrame();
        DataGenerateWindow dataGenerateWindow = new DataGenerateWindow();
        frame.setContentPane(dataGenerateWindow.mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public DataGenerateWindow(){
        this.locationsList = new ArrayList<>();
        this.newDataButton.addActionListener(this);
        this.deleteSelectedButton.addActionListener(this);
        this.addRandomDataButton.addActionListener(this);
        this.saveDatasetToDatabaseButton.addActionListener(this);
        this.loadDatasetFromDatabaseButton.addActionListener(this);
        this.removeDatasetFromDatabaseButton.addActionListener(this);
        this.changeDatabaseButton.addActionListener(this);
        this.saveDatasetToFileButton.addActionListener(this);
        this.loadDatasetFromFileButton.addActionListener(this);
    }

    public Container getMainWindow(){
        return this.mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        //Add Data button action
        if(e.getSource() == newDataButton) {
            addData(this.model, this.xField, this.yField, this.tagnameField);
            //this.updateLocationsList();
            this.refreshDataChart();
        }
        //Delete selected button action
        if(e.getSource() == deleteSelectedButton){
            removeSelectedRow();
            this.updateLocationsList();
            refreshDataChart();
        }
        if(e.getSource() == addRandomDataButton){
            addRandomData(this.model, this.xField, this.yField, this.tagnameField);
            this.updateLocationsList();
            this.refreshDataChart();
        }
        if(e.getSource() == saveDatasetToDatabaseButton){
            this.saveDataToDatabase(databaseConnectionURL);
        }
        if(e.getSource() == loadDatasetFromDatabaseButton){
            this.loadDataFromDatabase(databaseConnectionURL);
        }
        if(e.getSource() == removeDatasetFromDatabaseButton){
            this.removeDatasetFromDatabase(databaseConnectionURL);
        }
        if(e.getSource() == changeDatabaseButton){
            this.changeDatabase();
        }
        if(e.getSource() == saveDatasetToFileButton){
            DataManager.saveDatasetToFile(this.newDatasetTextField.getText(),this.locationsList.size(),this.locationsList);
        }
        if(e.getSource() == loadDatasetFromFileButton){
            LocationsDataset ld = DataManager.loadDatasetFromTxtFile();
            this.setDataTable(ld);
        }
    }

    private void refreshDataChart(){
        this.generatedDataPanel.removeAll();
        this.generatedDataPanel.revalidate();
        DataChart dataChart = new DataChart(getLocations(locationsList));
        this.generatedDataPanel.add(dataChart.getContentPane());
        this.generatedDataPanel.repaint();
    }

    private void setDataTable(LocationsDataset locationsDataset){
        this.newDatasetTextField.setText(locationsDataset.getDatasetName());
        this.clearTable(this.model);
        for(Location data : locationsDataset.getLocationsData()){
            addData(this.model,String.valueOf(data.getX()),String.valueOf(data.getY()),data.getTagName());
        }
        this.updateLocationsList();
        this.refreshDataChart();
    }

    private void clearTable(DefaultTableModel tableModel){
        for(int i = dataTable.getRowCount() - 1; i >= 0; i--){
            tableModel.removeRow(i);
        }
        this.updateLocationsList();
    }

    private void addData(DefaultTableModel targetTableModel, JTextField xField, JTextField yField, JTextField tagnameField){
        try {
            double v1 = Double.parseDouble(xField.getText());
            double v2 = Double.parseDouble(yField.getText());
            locationsList.add(new Double[]{v1,v2});
            targetTableModel.insertRow(targetTableModel.getRowCount()
                    , new Object[]{targetTableModel.getRowCount() + 1, xField.getText(), yField.getText(), tagnameField.getText()});
        }catch(Exception e){
            xField.setText("");
            yField.setText("");
            System.out.println("Wrong data format");
            JOptionPane.showMessageDialog(this, "X and Y values must be in double format!");
        }
    }

    private void addData(DefaultTableModel targetTableModel, String x, String y, String tagnameField){
        try {
            double v1 = Double.parseDouble(x);
            double v2 = Double.parseDouble(y);
            targetTableModel.insertRow(targetTableModel.getRowCount()
                    , new Object[]{targetTableModel.getRowCount() + 1, x, y, tagnameField});
        }catch(Exception e){
            xField.setText("");
            yField.setText("");
            System.out.println("Wrong data format");
            JOptionPane.showMessageDialog(this, "X and Y values must be in double format!");
        }
    }

    private void addRandomData(DefaultTableModel targetTableModel, JTextField xField, JTextField yField, JTextField tagnameField){
        try {
            Double r1 = RandomTSP.randomDoubleBetweenValues(Double.parseDouble(xField.getText()), Double.parseDouble(yField.getText()));
            Double r2 = RandomTSP.randomDoubleBetweenValues(Double.parseDouble(xField.getText()), Double.parseDouble(yField.getText()));
            locationsList.add(new Double[]{r1,r2});
            targetTableModel.insertRow(targetTableModel.getRowCount(),
                    new Object[]{targetTableModel.getRowCount() + 1, r1.toString(), r2.toString(), tagnameField.getText()});
        }catch(Exception e){
            xField.setText("");
            yField.setText("");
            System.out.println("Wrong data format");
            JOptionPane.showMessageDialog(this, "X and Y values must be double in format!");
        }
    }

    private void removeSelectedRow(){
        if(dataTable.getSelectedRow() > -1) {
            for (int x : dataTable.getSelectedRows()) {
                this.model.removeRow(this.dataTable.getSelectedRow());
            }
            updateIndexes();
        }
    }

    private void updateIndexes(){
        for(int i = 0; i < model.getRowCount(); i++){
            dataTable.setValueAt(i+1,i,0);
        }
    }

    private void updateLocationsList(){
        locationsList.clear();
        for (int i = 0; i < this.dataTable.getRowCount(); i++) {
            Double[] tempData = new Double[2];
            tempData[0] = Double.parseDouble((String)model.getValueAt(i,1));
            tempData[1] = Double.parseDouble((String)model.getValueAt(i,2));
            this.locationsList.add(tempData);
        }
    }

    private double[][] getLocations(ArrayList<Double[]> locationsList){
        if(!locationsList.isEmpty()){
            double[][] output = new double[locationsList.size()][2];
            for (int i = 0; i < locationsList.size(); i++) {
                output[i][0] = locationsList.get(i)[0];
                output[i][1] = locationsList.get(i)[1];
            }
            return output;
        }else{
            double[][] td = new double[1][2];td[0][1] = 0.0;td[0][0] = 0.0;
            return td;
        }
    }

    private void saveDataToDatabase(String databaseConnectionURL){
        DatabaseController dbcon = new DatabaseController(databaseConnectionURL);
        this.updateLocationsList();
        try {
            String datasetName = this.newDatasetTextField.getText();
            int dimension = this.locationsList.size();
            if(dbcon.getDatasetID(datasetName) == -1) {
                dbcon.insertDatasetRow(datasetName, dimension);
            }else{
                dbcon.clearLocationsTable(datasetName);
            }
            int datasetID = dbcon.getDatasetID(datasetName);
            int index = 0;
            for(Double[] row : this.locationsList){
                dbcon.insertLocationsRow(datasetID,index,row[0],row[1]);
                index++;
            }
        JOptionPane.showMessageDialog(new JFrame(),"Dataset successfully stored in database!");
        }catch(Exception e){
            System.out.println("Data or dataset name has error");
        }finally {
            dbcon.updateDatasetTableLocationsCount();
            dbcon.closeConnection();
        }
    }

    private void loadDataFromDatabase(String databaseConnectionURL){
        DatabaseController dbcon = new DatabaseController(databaseConnectionURL);
        try {
            //clear table
            ArrayList<Object> selectDataset = new ArrayList<>();
            for(String[] x : dbcon.getDatasetTableContent()){
                String tempString = x[0]+". "+x[1] + " "+x[2]+" locations";
                selectDataset.add(tempString);
            }
            Object[] possibilities = selectDataset.toArray();
            String datasetName = (String)JOptionPane.showInputDialog(
                    this,
                    "Select dataset to load: ",
                    "Load dataset",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Select dataset");
            if(datasetName != null) {
                for (int i = this.model.getRowCount(); i > 0; i--) {
                    this.model.removeRow(i - 1);
                }
                // fill table
                String[] datasetSplit = datasetName.split("\\s+");
                String temp = "";
                for(int i = 1; i < datasetSplit.length - 2; i++){
                     temp += datasetSplit[i]+ " ";
                }
                datasetName = temp.trim();
                this.newDatasetTextField.setText(datasetName);
                for (String[] x : dbcon.getLocationsTableContent(datasetName)) {
                    addData(this.model, x[1], x[2], x[3]);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Data or dataset name has error");
        }finally {
            this.updateLocationsList();
            this.refreshDataChart();
            dbcon.closeConnection();
        }
    }

    private void loadDataFromDatabase(String databaseConnectionURL, String datasetName){
        DatabaseController dbcon = new DatabaseController(databaseConnectionURL);
        try {
            //clear table
            for(int i = this.model.getRowCount(); i > 0; i--) {
                this.model.removeRow(i-1);
            }
            // fill table
            for(String[] x : dbcon.getLocationsTableContent(datasetName)){
                addData(this.model,x[1],x[2],x[3]);
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Data or dataset name has error");
        }finally {
            this.updateLocationsList();
            this.refreshDataChart();
            dbcon.closeConnection();
        }
    }

    private void removeDatasetFromDatabase(String databaseConnectionURL){
        DatabaseController dbcon = new DatabaseController(databaseConnectionURL);
        try {
            //clear table
            ArrayList<Object> selectDataset = new ArrayList<>();
            for(String[] x : dbcon.getDatasetTableContent()){
                String tempString = x[0]+". "+x[1] + " "+x[2]+" locations";
                selectDataset.add(tempString);
            }
            Object[] possibilities = selectDataset.toArray();
            String datasetName = (String)JOptionPane.showInputDialog(
                    this,
                    "Select dataset to remove: ",
                    "Remove dataset",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Select dataset");
            System.out.println("HERE: "+datasetName);
            // fill table
            if(datasetName != null) {
                datasetName = datasetName.split("\\s+")[1];
                dbcon.removeDataset(datasetName);
                for(int i = this.model.getRowCount(); i > 0; i--) {
                    this.model.removeRow(i-1);
                }
            }


        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Data or dataset name has error");
        }finally {
            this.updateLocationsList();
            this.refreshDataChart();
            dbcon.closeConnection();
        }
    }

    private void changeDatabase(){
        String message = "Enter new database connection URL. Current URL: \n\n"+
                    this.databaseConnectionURL+"\n\n";
        String newDatabaseConnectionURL = (String)JOptionPane.showInputDialog(
                    this,
                    message,
                    "Change database",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
        System.out.println("HERE: "+newDatabaseConnectionURL);
        try{
            DatabaseController newDbcon = new DatabaseController(newDatabaseConnectionURL);

            if(newDbcon.getConnection() != null) {
                newDbcon.closeConnection();
                DatabaseController.setDatabaseConnectionURL(newDatabaseConnectionURL);
            }else{
                JOptionPane.showMessageDialog(this,"Error! New connection doesn't work. Check if:\n" +
                        "- new connection URL has any errors\n" +
                        "- database you're trying to connect to, has correct DataManager schema\n" +
                        "- database you're trying to connect to, supports T-SQL query\n" +
                        "- if that doesn't help, you're on your own. Take care!");
            }
        }catch(Exception e){
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        model = new DefaultTableModel();
        this.dataTable = new JTable(model){
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return column == 0 ? false : true;
            }
        };
        model.addColumn("Index");
        model.addColumn("X");
        model.addColumn("Y");
        model.addColumn("Tagname(optional)");

        this.locationsList = new ArrayList<>();
        DataChart dataChart = new DataChart(getLocations(locationsList));
        this.generatedDataPanel = new JPanel();
        this.generatedDataPanel.add(dataChart.getContentPane());
    }
}

class DataChart extends ApplicationFrame {
    public DataChart(double[][] locations){
        super("Generated data chart");
        JFreeChart xylineChart = getDataChart(locations);
        ChartPanel chartPanel = new ChartPanel( xylineChart );
        //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 400 ) );
        this.setContentPane( chartPanel );
    }
    private JFreeChart getDataChart(double[][] locations){

        JFreeChart xyPointChart = ChartFactory.createScatterPlot("Location XY cordinates","X cordinate","Y cordinate",createLocationsDataset(locations));
        XYPlot plot = xyPointChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false,true);
        renderer.setBaseToolTipGenerator(new DGWXYLabelGenerator());
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
}

class DGWXYLabelGenerator implements XYToolTipGenerator {

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
        return "X: "+cordinateX+"  Y: "+cordinateY;
    }
}


