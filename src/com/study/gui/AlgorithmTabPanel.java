package com.study.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AlgorithmTabPanel extends JTabbedPane {
    private ArrayList<AlgorithmTab> algTabs;

    public AlgorithmTabPanel(){
        super();
        this.algTabs = new ArrayList<>();
    }

    @Override
    public Component add(Component component){
        super.add(component);
        if(component.getClass() == AlgorithmTab.class){
            algTabs.add((AlgorithmTab)component);
        }
        return component;
    }

    public ArrayList<AlgorithmTab> getAlgTabs() {
        return algTabs;
    }

    public void setAlgTabs(ArrayList<AlgorithmTab> algTabs) {
        this.algTabs = algTabs;
    }
}
