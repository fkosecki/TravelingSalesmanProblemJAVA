package com.study;

public class SubOption extends SimpleOption{
    private boolean enabled = false;

    public SubOption(String optionName){
        super(optionName);
    }

    public SubOption(String optionName, double value){
        super(optionName, value);
    }

    public SubOption(String optionName, String value){
        super(optionName, value);
    }

    public void enable(){
        this.enabled = true;
    }

    public void disable(){
        this.enabled = false;
    }

    public boolean isEnabled(){
        if(this.enabled){
            return true;
        }
        else{
            return false;
        }
    }
}
