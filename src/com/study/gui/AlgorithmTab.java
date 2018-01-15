package com.study.gui;

import com.study.*;

import javax.security.auth.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

public class AlgorithmTab extends JScrollPane implements Runnable{
    private String algorithmName;
    private ResultsManager resultsManager;
    private LocationsDataset locationsDataset;
    private TspSettings tspSettings;
    private ManySimpleOptionJPanel manySimpleOptionJPanel;
    private ManyMultiOptionJPanel manyMultiOptionJPanel;
    private JPanel innerPanel;
    private AlgorithmProgressBar algorithmProgressBar;
    private Algorithm algorithm;

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public AlgorithmTab(TspSettings tspSettings, JPanel innerPanel){
        super(innerPanel);
        this.tspSettings = tspSettings;
        this.algorithmName = tspSettings.getAlgorithmName();
        this.innerPanel = new JPanel();
        this.manySimpleOptionJPanel = new ManySimpleOptionJPanel(this.tspSettings.getSimpleOptions());
        this.manyMultiOptionJPanel = new ManyMultiOptionJPanel(this.tspSettings.getMultiOptions());
        this.algorithmProgressBar = new AlgorithmProgressBar(this.tspSettings);
        innerPanel.setLayout(new BorderLayout());
        innerPanel.add(this.manySimpleOptionJPanel,BorderLayout.NORTH);
        innerPanel.add(this.manyMultiOptionJPanel,BorderLayout.CENTER);
        innerPanel.add(this.algorithmProgressBar,BorderLayout.SOUTH);
        this.setName(this.algorithmName);
      }

    public void retrieveSettingsFromPanels(){
      for(SimpleOptionJPanel simpleOptionJPanel : this.manySimpleOptionJPanel.getSimpleOptionJPanelsList()){
          simpleOptionJPanel.refreshOption();
      }
      for(MultiOptionJPanel multiOptionJPanel : this.manyMultiOptionJPanel.getMultiOptionJPanelList()){
          for(SubOptionPanel subOptionPanel : multiOptionJPanel.getSubOptionPanelList()){
              subOptionPanel.refreshOption();
          }
      }
  }

    public void run(){
        runAlgorithm();
    }

    private void runAlgorithm(){
        this.algorithm = new GeneticAlgorithm();
        //this.algorithm = new HeldKarpAlgorithm();
        this.retrieveSettingsFromPanels();
        Thread t = new Thread(this.algorithmProgressBar);
        t.start();
        this.algorithm.executeAlgorithm(this.tspSettings);
    }

    public TspSettings getTspSettings(){
        return this.tspSettings;
    }
}

class AlgorithmProgressBar extends JProgressBar implements Runnable{
    private TspSettings tspSettings;

    public AlgorithmProgressBar(TspSettings settings){
        this.tspSettings = settings;
    }

    public void run(){
        this.setValue(0);
        int maxIterations = ((Double)((SimpleOption)this.tspSettings.getMatchingOption("Number of Iterations")).getValue()).intValue();
        this.setMaximum(maxIterations);
        System.out.println("MAX: "+maxIterations);
        while(maxIterations > this.tspSettings.currentIteration){
            this.setValue(this.tspSettings.currentIteration);
        }
        System.out.println("KONIEC: "+this.getValue());
        this.tspSettings.currentIteration = 0;
        System.out.println("KONIEC");
    }
}