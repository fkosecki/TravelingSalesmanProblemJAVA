package com.study.gui;

import com.study.*;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Gui implements ActionListener {
    private JPanel WindowName;
    private JTabbedPane tabbedPane1;
    private JButton editDatasetButton;
    private JPanel dataDisplayPanel;
    private JPanel tempPanel2;
    private JButton saveSettingButton;
    private JButton loadSettingsButton;
    private JButton executeButton;
    private JLabel datasetNameLabel;
    private JLabel datasetCountLabel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton loadDatasetFromFileButton;
    private JButton loadDatasetFromDatabaseButton;
    private AlgorithmTabPanel algorithmTabPanel1;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Gui gui = new Gui();
        frame.setContentPane(gui.getMainWindow());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Gui() {
        this.editDatasetButton.addActionListener(this);
        this.loadDatasetFromFileButton.addActionListener(this);
        this.loadDatasetFromDatabaseButton.addActionListener(this);
        this.executeButton.addActionListener(this);
        this.saveSettingButton.addActionListener(this);
        this.loadSettingsButton.addActionListener(this);
    }

    public Container getMainWindow() {
        return this.WindowName;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == editDatasetButton) {
            this.openDataGeneratorWindow();
        }
        if (e.getSource() == loadDatasetFromFileButton) {
            DataManager.setLocationsDataset(DataManager.loadDatasetFromTxtFile());
            this.refreshDataManagerLabels();
            this.refreshLocationsChart();
        }
        if (e.getSource() == loadDatasetFromDatabaseButton) {
            DataManager.setLocationsDataset(DataManager.loadDatasetFromDatabase());
            this.refreshDataManagerLabels();
            this.refreshLocationsChart();
        }
        if (e.getSource() == executeButton) {
            this.algorithmTabPanel1.getAlgTabs().get(0).getTspSettings().setLocationsDataset(DataManager.getLocationsDataset());
            Thread t = new Thread(this.algorithmTabPanel1.getAlgTabs().get(0));
            t.start();
        }
        if (e.getSource() == saveSettingButton) {
            System.out.println(this.algorithmTabPanel1.getAlgTabs().size());
            this.algorithmTabPanel1.getAlgTabs().forEach(AlgorithmTab::retrieveSettingsFromPanels);

        }
        if (e.getSource() == loadSettingsButton) {
            GeneticAlgorithm kl = new GeneticAlgorithm();
            this.algorithmTabPanel1.getAlgTabs().get(0).getTspSettings().setLocationsDataset(DataManager.getLocationsDataset());
            kl.executeAlgorithm(this.algorithmTabPanel1.getAlgTabs().get(0).getTspSettings());
        }

        if(e.getSource() == this.algorithmTabPanel1){
            System.out.println("AKCJA");
        }
    }

    private void refreshLocationsChart() {
        this.dataDisplayPanel.removeAll();
        this.dataDisplayPanel.revalidate();
        this.dataDisplayPanel.add(ResultsManager.locationsChart(DataManager.getLocationsDataset()).getContentPane());
        this.dataDisplayPanel.repaint();
    }

    private void openDataGeneratorWindow() {
        JFrame frame = new JFrame();
        DataGenerateWindow dataGenerateWindow = new DataGenerateWindow();
        frame.setContentPane(dataGenerateWindow.getMainWindow());
        frame.pack();
        frame.setVisible(true);
    }

    private void refreshDataManagerLabels() {
        if (DataManager.getLocationsDataset() != null) {
            this.datasetNameLabel.setText("Dataset name: " + DataManager.getLocationsDataset().getDatasetName());
            this.datasetCountLabel.setText("Number of locations: " + DataManager.getLocationsDataset().getDatasetCount());
        }
    }

    private void createUIComponents() {
//        // TODO: place custom component creation code here
        this.algorithmTabPanel1 = new AlgorithmTabPanel();
        this.algorithmTabPanel1.add(new AlgorithmTab(GeneticAlgorithm.getSettings(), new JPanel()));
        this.dataDisplayPanel = new JPanel();
    }

}
     class MultiOptionJPanel extends JPanel{
        private JLabel label;
        private JPanel optionsPanel;
        private List<SubOptionPanel> subOptionPanelList;

         public List<SubOptionPanel> getSubOptionPanelList() {
             return subOptionPanelList;
         }

        MultiOptionJPanel(MultiOption multiOption){
            super.setName(multiOption.getOptionName());
            this.setLayout(new BorderLayout());
            this.optionsPanel = new JPanel();
            this.optionsPanel.setLayout(new GridLayout(multiOption.getSubOptions().size(),1));
            this.label = new JLabel(multiOption.getOptionName());
            this.add(this.label, BorderLayout.WEST);
            this.add(this.optionsPanel, BorderLayout.CENTER);
            this.subOptionPanelList = new ArrayList<>();

            for(SubOption option : multiOption.getSubOptions()) {
                SubOptionPanel panel = new SubOptionPanel(option);
                this.subOptionPanelList.add(panel);
                this.optionsPanel.add(panel);
            }
        }
     }

     class SubOptionPanel extends JPanel implements ChangeListener{
        private SubOption subOption;
        private JCheckBox checkBox;
        private JSlider slider;
        private JSpinner spinner;
        private int value = 20;

         SubOptionPanel(SubOption subOption){
             super.setName(subOption.getOptionName());
             this.subOption = subOption;
             this.checkBox = new JCheckBox(subOption.getOptionName());
             this.slider = new JSlider(0,100,this.value);
             this.slider.setMaximumSize(new Dimension(50,30));
             this.spinner = new JSpinner(new SpinnerNumberModel(this.value,0,100,1));
             this.add(this.checkBox);
             this.add(this.slider);
             this.add(this.spinner);

             this.spinner.addChangeListener(this);
             this.slider.addChangeListener(this);
             this.checkBox.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     if(subOption.isEnabled()){
                         subOption.disable();
                     }else{
                         subOption.enable();
                     }
                 }
             });
         }

         public void setSubOption(SubOption subOption) {
             this.subOption = subOption;
         }

         public void setCheckBox(JCheckBox checkBox) {
             this.checkBox = checkBox;
         }

         public void setSlider(JSlider slider) {
             this.slider = slider;
         }

         public void setSpinner(JSpinner spinner) {
             this.spinner = spinner;
         }

         public void setValue(int value) {
             this.value = value;
         }

         public SubOption getSubOption() {
             return subOption;
         }

         public JCheckBox getCheckBox() {
             return checkBox;
         }

         public JSlider getSlider() {
             return slider;
         }

         public JSpinner getSpinner() {
             return spinner;
         }

         public int getValue() {
             return value;
         }

         public void refreshOption(){
             try {
                 this.subOption.setValue(this.slider.getValue());
             }catch (Exception e){
                 JOptionPane.showMessageDialog(new JFrame(),"Error! Check options!");
             }
         }

         @Override
         public void stateChanged(ChangeEvent e) {
             if(e.getSource() == this.spinner){
                 this.slider.setValue((int)this.spinner.getValue());
             }
             if(e.getSource() == this.slider){
                 this.spinner.setValue(this.slider.getValue());
             }
         }
     }

     class SimpleOptionJPanel extends JPanel{
        private JLabel label;
        private JTextField textField;

         public SimpleOption getSimpleOption() {
             return simpleOption;
         }

         public void setSimpleOption(SimpleOption simpleOption) {
             this.simpleOption = simpleOption;
         }

         private SimpleOption simpleOption;

        SimpleOptionJPanel(SimpleOption simpleOption){
            this.simpleOption = simpleOption;
            super.setName(simpleOption.getOptionName());
            this.label = new JLabel(simpleOption.getOptionName());
            this.textField = new JTextField("0.0");
            this.add(label);
            this.add(textField);
        }

         public void refreshOption(){
             try {
                 this.simpleOption.setValue(Double.parseDouble(this.textField.getText()));
             }catch (Exception e){
                 JOptionPane.showMessageDialog(new JFrame(),"Error! Check options!");
             }
         }

    }

     class ManySimpleOptionJPanel extends JPanel{
        private List<SimpleOption> simpleOptionsList;
         private List<SimpleOptionJPanel> simpleOptionJPanelsList;
        private final String name = "SimpleOptionsJPanel";
        private int columnsCount = 2;

        ManySimpleOptionJPanel(){
            super.setName(this.name);
            this.simpleOptionsList = new ArrayList<>();
            this.simpleOptionJPanelsList = new ArrayList<>();
        }

        ManySimpleOptionJPanel(List<SimpleOption> simpleOptionsList){
            super.setName(this.name);
            this.simpleOptionsList = simpleOptionsList;
            this.simpleOptionJPanelsList = new ArrayList<>();
            panelSetup();
        }

        private void panelSetup(){
            int rowsCount = (int)Math.round((double)this.simpleOptionsList.size()/(double)columnsCount);
            this.setLayout(new GridLayout(rowsCount,columnsCount));

            for(SimpleOption x : this.simpleOptionsList){
                SimpleOptionJPanel panel = new SimpleOptionJPanel(x);
                this.simpleOptionJPanelsList.add(panel);
                this.add(panel);
            }
        }

        public void addSimpleOption(SimpleOption simpleOption){
            this.setLayout(new GridLayout((this.simpleOptionsList.size() + 1), columnsCount));
            SimpleOptionJPanel panel = new SimpleOptionJPanel(simpleOption);
            this.simpleOptionJPanelsList.add(panel);
            this.add(panel);
        }

         public List<SimpleOption> getSimpleOptionsList() {
             return simpleOptionsList;
         }

         public void setSimpleOptionsList(List<SimpleOption> simpleOptionsList) {
             this.simpleOptionsList = simpleOptionsList;
         }

         public List<SimpleOptionJPanel> getSimpleOptionJPanelsList() {
             return simpleOptionJPanelsList;
         }

         public void setSimpleOptionJPanelsList(List<SimpleOptionJPanel> simpleOptionJPanelsList) {
             this.simpleOptionJPanelsList = simpleOptionJPanelsList;
         }
     }

     class ManyMultiOptionJPanel extends JPanel{
         private List<MultiOption> multiOptionsList;
         private List<MultiOptionJPanel> multiOptionJPanelList;
         private String name = "MultiOptionsJPanel";
         private int columnsCount = 1;

         ManyMultiOptionJPanel(){
             super.setName(this.name);
             this.multiOptionsList = new ArrayList<>();
             this.multiOptionJPanelList = new ArrayList<>();
         }

         ManyMultiOptionJPanel(List<MultiOption> multiOptionsList){
             super.setName(this.name);
             this.multiOptionsList = multiOptionsList;
             this.multiOptionJPanelList = new ArrayList<>();
             panelSetup();
         }

         private void panelSetup(){
            int rowsCount = this.multiOptionsList.size();
            this.setLayout(new GridLayout(rowsCount,columnsCount));

            for(MultiOption x : this.multiOptionsList){
                MultiOptionJPanel panel = new MultiOptionJPanel(x);
                this.multiOptionJPanelList.add(panel);
                this.add(panel);
            }
         }

         public void addMultiOption(MultiOption multiOption){
            this.setLayout(new GridLayout((this.multiOptionsList.size() + 1), columnsCount));
            this.add(new MultiOptionJPanel(multiOption));
         }

         public List<MultiOptionJPanel> getMultiOptionJPanelList() {
             return multiOptionJPanelList;
         }
     }


class ResultsPanel extends JPanel{

}
