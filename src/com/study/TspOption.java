package com.study;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

//public class Option {
//    private String optionName;
//    private List<String> optionList = new ArrayList<>();
//
//    public String getOptionName(){
//        return this.optionName;
//    }
//
//    public void setOptionName(String optionName){
//        this.optionName = optionName;
//    }
//
//    public List<String> getOptionList(){
//        return this.optionList;
//    }
//
//    public void setOptionList(List<String> optionList){
//        this.optionList = optionList;
//    }
//}

abstract public class TspOption{
    private String optionName;

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return this.optionName;
    }
}

