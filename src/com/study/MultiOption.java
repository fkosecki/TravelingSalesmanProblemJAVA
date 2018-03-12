package com.study;

import java.util.ArrayList;
import java.util.List;

public class MultiOption extends TspOption{
    private List<SubOption> subOptions = new ArrayList<>();

    public MultiOption(String optionName){
        this.setOptionName(optionName);
    }

    public MultiOption(String optionName, List<SubOption> subOptions){
        this.setOptionName(optionName);
        this.subOptions = subOptions;
    }

    public List<SubOption> getSubOptions(){
        return this.subOptions;
    }

    public void setSubOptions(List<SubOption> subOptions) {
        this.subOptions = subOptions;
    }

    public void addSubOption(SubOption subOption){
        this.subOptions.add(subOption);
    }

    public void enableSubOption(String subOptionName){
        for(SubOption x : this.subOptions){
            if(x.getOptionName() == subOptionName){
                x.enable();
            }
        }
    }

    public void enableSubOption(int index){
        this.subOptions.get(index).enable();
    }

    public void disableSubOption(String subOptionName){
        for(SubOption x : this.subOptions){
            if(x.getOptionName() == subOptionName){
                x.disable();
            }
        }
    }

    public void disableSubOption(int index){
        this.subOptions.get(index).disable();
    }

    public List<SubOption> getEnabledOptions(){
        List<SubOption> tempOptions = new ArrayList<>();
        for(SubOption x : this.subOptions){
            if(x.isEnabled()){
                tempOptions.add(x);
            }
        }
        return tempOptions;
    }
}
