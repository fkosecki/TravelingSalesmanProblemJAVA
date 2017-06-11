package com.study;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private int dataCount;
    private static LocationsDataset locationsDataset;

    public static LocationsDataset getLocationsDataset() {
        return locationsDataset;
    }

    public static void setLocationsDataset(LocationsDataset ld) {
        if(ld != null){
            locationsDataset = ld;
        }
    }

    private void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getDataCount() {
        return this.dataCount;
    }

    public List<Double> getDataFromTXT(String inputFilePath) {

        List<Double> data = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(inputFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("locations")){
                    String[] newLine = line.split("\\s+");
                        System.out.println(newLine[newLine.length-1]);
                }
                if(line.contains("DIMENSION")){
                    String[] newLine = line.split("\\s+");
                    System.out.println(newLine[newLine.length-1]);
                }
                if(line.startsWith("1")){
                    do {
                        String[] dataString = line.split("\\s+");
                        for (String singleData: dataString) {
                            if(singleData.contains(".")){
                                data.add(Double.parseDouble(singleData));
                            }
                        }
                    }while ((line = br.readLine()) != null);
                }
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        this.setDataCount(data.size()/2);
        return data;
    }

    public void fillDatabaseWithDirTxtFiles(String dirPath, String databaseConnectionURL){
        TXTfilter txtFilter = new TXTfilter(dirPath);
        DatabaseController dbcon = new DatabaseController(databaseConnectionURL);
        int datasetID = 1;

        for(File file : txtFilter.getDirTxtFiles()){
            addDatasetFromTxtFileToDatabase(file,dbcon, datasetID);
            datasetID++;
        }
        dbcon.closeConnection();
    }

    public void addDatasetFromTxtFileToDatabase(File txtFile, DatabaseController dbcon, int datasetID){
        String[] basicInfo = TXTfilter.getBasicInfo(txtFile);

        dbcon.insertDatasetRow(basicInfo[0],Integer.parseInt(basicInfo[1]));

        List<Double> locationsData = getDataFromTXT(txtFile.getAbsolutePath());
        for (int i = 0; i < locationsData.size(); i = i+2) {
            dbcon.insertLocationsRow(datasetID,i/2 + 1,locationsData.get(i),locationsData.get(i+1));
        }
    }

    public static void saveDatasetToTxtFile(String datasetName, int dimension, ArrayList<Double[]> locationsList){
        String firstLine = "DatasetName: "+datasetName;
        String secondLine = "Dimension: "+dimension;
        String thirdLine = "Locations:";
        try {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(firstLine);
            lines.add(secondLine);
            lines.add(thirdLine);
            for(Double[] data : locationsList){
                lines.add(data[0]+" "+data[1]);
            }
            Path path = Paths.get("datasets//"+datasetName+".txt");
            Files.write(path, lines, Charset.forName("UTF-8"));
            JOptionPane.showMessageDialog(new Frame(),"Dataset successfully saved!");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(),"Error! Couldn't save file to txt.");
        }
    }

    public static void saveDatasetToTxtFile(String filePath, String datasetName, int dimension, ArrayList<Double[]> locationsList){
        String firstLine = "DatasetName: "+datasetName;
        String secondLine = "Dimension: "+dimension;
        String thirdLine = "Locations:";
        try {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(firstLine);
            lines.add(secondLine);
            lines.add(thirdLine);
            for(Double[] data : locationsList){
                lines.add(data[0]+" "+data[1]);
            }
            Path path = Paths.get(filePath+".txt");
            Files.write(path, lines, Charset.forName("UTF-8"));
            JOptionPane.showMessageDialog(new Frame(),"Dataset successfully saved!");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(),"Error! Couldn't save file to txt.");
        }
    }

    public static void saveDatasetToFile(LocationsDataset locationsDataset){
        String firstLine = "DatasetName: "+locationsDataset.getDatasetName();
        String secondLine = "Dimension: "+locationsDataset.getDatasetCount();
        String thirdLine = "Locations:";
        try {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(firstLine);
            lines.add(secondLine);
            lines.add(thirdLine);
            for(Location data : locationsDataset.getLocationsData()){
                lines.add(data.getX()+" "+data.getY());
            }
            Path path = Paths.get("datasets//"+locationsDataset.getDatasetName()+".txt");
            Files.write(path, lines, Charset.forName("UTF-8"));
            JOptionPane.showMessageDialog(new Frame(),"Dataset successfully saved!");
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(),"Error! Couldn't save file to txt.");
        }
    }

    public static LocationsDataset loadDatasetFromTxtFile(){
        LocationsDataset locationsDataset = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(".txt","txt");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(".xml","xml");
        fileChooser.addChoosableFileFilter(txtFilter);
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File("datasets"));
        int result = fileChooser.showOpenDialog(new Frame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
                if(selectedFile.getName().endsWith(".txt")) {
                    locationsDataset = new LocationsDataset(selectedFile);
                    locationsDataset.printUrself();
                }
                else if(selectedFile.getName().endsWith(".xml")) {
                    locationsDataset = XMLFileHandler.loadDatasetFromXML(selectedFile.getPath());
                    locationsDataset.printUrself();
                }
            }catch (Exception e){
            }
        }
        return locationsDataset;
    }

    public static void saveDatasetToFile(String datasetName, int dimension, ArrayList<Double[]> locationsList){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter(".txt","txt");
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(".xml","xml");
        fileChooser.addChoosableFileFilter(txtFilter);
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File("datasets"));
        fileChooser.setSelectedFile(new File(datasetName));
        int result = fileChooser.showSaveDialog(new Frame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
                if(fileChooser.getFileFilter() == txtFilter) {
                    DataManager.saveDatasetToTxtFile(selectedFile.getAbsolutePath(),datasetName, dimension, locationsList);
                }
                else if(fileChooser.getFileFilter() == xmlFilter) {
                    XMLFileHandler.saveDatsetToXMLFile(selectedFile.getAbsolutePath(),datasetName, dimension, locationsList);
                }
            }catch (Exception e){
            }
        }
}

    public static void main(String[] args) {
//        String filePath = "C:\\Users\\Filipek\\Downloads\\java tsp\\qatar194.txt";
//        DataManager mng = new DataManager();
//        List<Double> tempList = mng.getDataFromTXT(filePath);
//        int i = 1;
//        for(Double x : tempList){
//            System.out.println(i+". "+x+" "+mng.getDataCount());
//            i++;
//        }
//        TXTfilter filter = new TXTfilter("src\\datasets");
//        for(String[] x : filter.getTxtFilesInfo()){
//            for(String s : x){
//                System.out.print(s+" ");
//            }
//            System.out.println();
//        }
//        for(File x : filter.getDirTxtFiles()){
//            String[] binfo = filter.getBasicInfo(x);
//            List<Double> tempList2 = mng.getDataFromTXT(x.getAbsolutePath());
//            int j = 1;
//            for(Double loc : tempList2){
//                System.out.println(j+". "+loc+" "+mng.getDataCount());
//                j++;
//            }
//        }
//    }
        String dirPath = "C:\\Users\\Filipek\\Documents\\IntelliJ\\TSP\\src\\datasets";
        String databaseConnectionURL = "jdbc:sqlserver://localhost;database=TSPLocations;integratedSecurity=true;";
        DataManager dm = new DataManager();
        //dm.fillDatabaseWithDirTxtFiles(dirPath,databaseConnectionURL);
    }

    public static LocationsDataset loadDatasetFromDatabase() {
        DatabaseController dbcon = new DatabaseController(DatabaseController.getDatabaseConnectionURL());
        LocationsDataset ld = new LocationsDataset();
        try {
            //clear table
            ArrayList<Object> selectDataset = new ArrayList<>();
            for (String[] x : dbcon.getDatasetTableContent()) {
                String tempString = x[0] + ". " + x[1] + " " + x[2] + " locations";
                selectDataset.add(tempString);
            }
            Object[] possibilities = selectDataset.toArray();
            String datasetName = (String) JOptionPane.showInputDialog(
                    new JFrame(),
                    "Select dataset to load: ",
                    "Load dataset",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "Select dataset");
            if (datasetName != null) {
                // fill table
                String[] datasetSplit = datasetName.split("\\s+");
                String temp = "";
                for (int i = 1; i < datasetSplit.length - 2; i++) {
                    temp += datasetSplit[i] + " ";
                }
                datasetName = temp.trim();
                ld.setDatasetName(datasetName);
                int index = 1;
                for (String[] x : dbcon.getLocationsTableContent(datasetName)) {
                    ld.addLocation(new Location(index,Double.parseDouble(x[1]),Double.parseDouble(x[2]),x[3]));
                }
                ld.setDatasetCount(ld.getLocationsData().size());
                return ld;
            }else{
                return null;
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Data or dataset name has error");
            return null;
        }finally {
            dbcon.closeConnection();
        }
    }
}

class TXTfilter {
    private File[] dirTxtFiles;
    private String[][] txtFilesInfo;

    public TXTfilter(String dirName){
        this.dirTxtFiles =  this.finder(dirName);
        this.txtFilesInfo = new String[this.dirTxtFiles.length][2];
        String[] tempInfo;

        for(int i = 0;  i < this.dirTxtFiles.length; i++){
            tempInfo = this.getBasicInfo(this.dirTxtFiles[i]);
            this.txtFilesInfo[i][0] = tempInfo[0];
            this.txtFilesInfo[i][1] = tempInfo[1];
        }
    }

    public File[] getDirTxtFiles() {
        return dirTxtFiles;
    }

    public String[][] getTxtFilesInfo() {
        return txtFilesInfo;
    }

    private File[] finder(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".txt"); }
        } );

    }

    public static String[] getBasicInfo(File txtFile){
        String[] info = new String[2];
        try {
            InputStream inputStream = new FileInputStream(txtFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("locations")){
                    String[] newLine = line.split("\\s+");
                    info[0] = newLine[newLine.length - 1];
                }
                if(line.contains("DIMENSION")){
                    String[] newLine = line.split("\\s+");
                    info[1] = newLine[newLine.length - 1];
                    break;
                }
            }
            br.close();

        } catch (Exception e) {
        }
        return info;
    }
}

