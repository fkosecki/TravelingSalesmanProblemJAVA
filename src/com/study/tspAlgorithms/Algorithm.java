package com.study.tspAlgorithms;

import com.study.TspOption;
import com.study.TspSettings;

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

    public abstract TspSettings getSettings();

    public abstract void executeAlgorithm(TspSettings settings);
}


