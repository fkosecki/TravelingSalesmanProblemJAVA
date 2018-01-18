package com.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TspSettings{
    private List<SimpleOption> simpleOptions;
    private List<MultiOption> multiOptions;
    private String algorithmName = "";
    private LocationsDataset locationsDataset;
    private String description = "";
    public Integer currentIteration;

    public TspSettings(String algorithmName){
        this.algorithmName = algorithmName;
        this.simpleOptions = new ArrayList<>();
        this.multiOptions = new ArrayList<>();
        this.currentIteration = new Integer(0);
    }

    public TspOption getMatchingOption(String optionName){
        for(SimpleOption simpleOption : this.simpleOptions){
            if(simpleOption.getOptionName().matches(optionName)){
                return simpleOption;
            }
        }
        for(MultiOption multiOption : this.multiOptions){
            if(multiOption.getOptionName().matches(optionName)){
                return multiOption;
            }
        }
        return null;
    }

    public TspOption getMatchingOption(String multiOptionName, String subOptionName){
        for(MultiOption multiOption : this.multiOptions){
            if(multiOption.getOptionName().matches(multiOptionName)){
                for(SubOption subOption : multiOption.getSubOptions()){
                    if(subOption.getOptionName().matches(subOptionName)){
                        return subOption;
                    }
                }
            }
        }
        return null;
    }

    public Integer getCurrentIteration(){
        return this.currentIteration;
    }

    public void setCurrentIteration(Integer iteration){
        this.currentIteration = iteration;
    }

    public String getAlgorithmName(){
        return this.algorithmName;
    }

    public List<SimpleOption> getSimpleOptions() {
        return this.simpleOptions;
    }

    public List<MultiOption> getMultiOptions() {
        return this.multiOptions;
    }

    public LocationsDataset getLocationsDataset(){
        return this.locationsDataset;
    }

    public void setLocationsDataset(LocationsDataset locationsDataset){
        this.locationsDataset = locationsDataset;
    }

    public void addSimpleOption(SimpleOption newSimpleOption){
        this.simpleOptions.add(newSimpleOption);
    }

    public void addMultiOption(MultiOption newMultiOption){
        this.multiOptions.add(newMultiOption);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String text = description.replaceAll("\n","<br/>");
        this.description = text;
    }
}
