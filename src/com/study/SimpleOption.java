package com.study;

public class SimpleOption extends TspOption{
    private Object value;

    SimpleOption(){

    }

    public SimpleOption(String optionName){
        this.setOptionName(optionName);
    }

    public SimpleOption(String optionName, double value) {
        this.setOptionName(optionName);
        this.value = value;
    }

    public SimpleOption(String optionName, String value){
        this.setOptionName(optionName);
        this.value = Double.parseDouble(value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(String value){
        this.value = Double.parseDouble(value);
    }

    public Object getValue() {
        return this.value;
    }

    public boolean hasValue(){
        if(this.value != null){
            return true;
        }
        else
        {
            return false;
        }
    }

}
