package com.study.gui;

import com.study.ResultsManager;

import javax.swing.*;
import java.awt.*;


public class ResultsWindow {
    private JPanel optimalRouteJPanel;
    private JPanel optimalRoutesHistory;
    private JLabel optimalRouteLengthLabel;
    private JLabel runTimeLabel;
    private JPanel mainWindow;

    private ResultsManager resultsManager;

    public ResultsWindow(ResultsManager resultsManager){
        this.resultsManager = resultsManager;
    }

    public void setUrself(){
        this.optimalRouteJPanel.add(resultsManager.optimalRouteChart().getContentPane());
        this.optimalRoutesHistory.add(resultsManager.optimalRouteHistoryChart().getContentPane());
        this.optimalRouteLengthLabel.setText(this.optimalRouteLengthLabel.getText()+" "+resultsManager.getOptimalRouteLength());
        this.runTimeLabel.setText(this.runTimeLabel.getText()+" "+resultsManager.getRunTime()+"ms");
    }

    public Container getMainWindow(){
        return this.mainWindow;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.optimalRouteJPanel = new JPanel();
        this.optimalRoutesHistory = new JPanel();
    }
}


