package com.study;

import java.util.List;


public abstract class Algorithm {

    private List<TspOption> options;

    private List<TspOption> getOptions(){
        return this.options;
    }

    public void setOptions(List<TspOption> options){
        this.options = options;
    }

    public long time() {
        return System.currentTimeMillis();
    }

    public void executeAlgorithm(TspSettings settings){

    }
}


