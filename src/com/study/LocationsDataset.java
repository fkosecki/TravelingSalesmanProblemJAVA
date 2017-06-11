package com.study;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LocationsDataset {
    private String datasetName;
    private int datasetCount;
    private ArrayList<Location> locationsData;

    LocationsDataset(){
        this.locationsData = new ArrayList<>();
    }

    LocationsDataset(String datasetName,int datasetCount){
        this.datasetName = datasetName;
        this.datasetCount = datasetCount;
        this.locationsData = new ArrayList<>();
    }

    public LocationsDataset (File txtDatasetFile){
        this.locationsData = new ArrayList<>();
        try {
            List<String> content = Files.readAllLines(txtDatasetFile.toPath());
            for(String line : content){
                if(line.startsWith("DatasetName:")){
                    this.datasetName = line.substring(12).trim();
                }else if(line.startsWith("Dimension:")){
                    this.datasetCount = Integer.parseInt(line.substring(10).trim());
                }else if(line.startsWith("Locations:")){
                    int index = 1;
                    for(int i = content.indexOf(line) + 1; i < content.size(); i++){
                        try{
                            String[] dataLine = content.get(i).split(" ");
                            this.locationsData.add(new Location(index,Double.parseDouble(dataLine[0]),Double.parseDouble(dataLine[1])));
                            index++;
                        }catch(Exception e){
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(new Frame(),"Error in locations line number "+i);
                        }
                    }
                }
            }
            this.datasetCount = this.locationsData.size();
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(new Frame(),"Error! Something is wrong with selected file!");
        }
    }

    public ArrayList<Location> getLocationsData() {
        return locationsData;
    }

    public void printUrself(){
        System.out.println(datasetName);
        System.out.println(datasetCount);
        for(Location l : locationsData) {
            System.out.println(l.getID()+". "+l.getX()+" "+l.getY()+" "+l.getTagName());
        }

    }

    public void addLocation(Location location){
        this.locationsData.add(location);
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getDatasetCount() {
        return datasetCount;
    }

    public void setDatasetCount(int datasetCount) {
        this.datasetCount = datasetCount;
    }

    public void setLocationsData(ArrayList<Location> locationsData) {
        this.locationsData = locationsData;
    }

    public ArrayList<Double> locationsToArray(){
        ArrayList<Double> output = new ArrayList<>();
        for(Location loc : this.locationsData){
            output.add(loc.getX());
            output.add(loc.getY());
        }
        return output;
    }
}
