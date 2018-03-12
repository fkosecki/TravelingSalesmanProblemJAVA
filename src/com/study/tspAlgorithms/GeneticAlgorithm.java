package com.study.tspAlgorithms;

import com.study.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class GeneticAlgorithm extends Algorithm{

    public final static String NAME = "Genetic Algorithm";
    public final static String DESCRIPTION = "Genetic Algorithm\nNear optimal solutions. Relatively fast for bigger input.";
    private Population population;
    private List<Subject> history = new ArrayList<>();
    private double[][] locations;
    private double[][] distanceMatrix;
    private Adaptor adaptor;
    public DataLoader dataLoader = new DataLoader();
    private long executionTime;
    private Mutator mutator;

    private TspSettings settings;
    private ResultsManager resultsManager;

    public void executeAlgorithm(TspSettings settings){
        this.settings = settings;
        this.resultsManager = new ResultsManager(settings.getLocationsDataset());
        SimpleOption populationSize = (SimpleOption) settings.getMatchingOption("Population Size");
        SimpleOption iterations = (SimpleOption) settings.getMatchingOption("Number of Iterations");
        SimpleOption eliteCount = (SimpleOption) settings.getMatchingOption("Elite Count");
        SimpleOption crossingPropability = (SimpleOption) settings.getMatchingOption("Crossing Propability");
        SimpleOption mutationPropability = (SimpleOption) settings.getMatchingOption("Mutation Propability");
        this.prepareAlgorithm(settings.getLocationsDataset().locationsToArray(),
                ((Double)populationSize.getValue()).intValue(),((Double)eliteCount.getValue()).intValue());
        this.runGeneticAlgorithm(this.population.size(),((Double)iterations.getValue()).intValue(),
                ((Double)eliteCount.getValue()).intValue(),(Double)crossingPropability.getValue(),
                (Double)mutationPropability.getValue());
    }

    public void executeAlgorithm(TspSettings settings,ResultsManager resultsManager){
        this.settings = settings;
        this.resultsManager = resultsManager;
        SimpleOption populationSize = (SimpleOption) settings.getMatchingOption("Population Size");
        SimpleOption iterations = (SimpleOption) settings.getMatchingOption("Number of Iterations");
        SimpleOption eliteCount = (SimpleOption) settings.getMatchingOption("Elite Count");
        SimpleOption crossingPropability = (SimpleOption) settings.getMatchingOption("Crossing Propability");
        SimpleOption mutationPropability = (SimpleOption) settings.getMatchingOption("Mutation Propability");
        this.prepareAlgorithm(settings.getLocationsDataset().locationsToArray(),
                ((Double)populationSize.getValue()).intValue(),((Double)eliteCount.getValue()).intValue());
        this.runGeneticAlgorithm(this.population.size(),((Double)iterations.getValue()).intValue(),
                ((Double)eliteCount.getValue()).intValue(),(Double)crossingPropability.getValue(),
                (Double)mutationPropability.getValue());
    }

    public  TspSettings getSettings(){
        TspSettings settings = new TspSettings(GeneticAlgorithm.NAME);
        settings.addSimpleOption(new SimpleOption("Population Size"));
        settings.addSimpleOption(new SimpleOption("Number of Iterations"));
        settings.addSimpleOption(new SimpleOption("Elite Count"));
        settings.addSimpleOption(new SimpleOption("Crossing Propability"));
        settings.addSimpleOption(new SimpleOption("Mutation Propability"));
        settings.addMultiOption((MultiOption)Mutator.getOptions());
        settings.addMultiOption((MultiOption)Crosser.getOptions());
        settings.setDescription(this.DESCRIPTION);
        return settings;
    }

    public TspSettings getTspSettings(){
        TspSettings settings = new TspSettings(GeneticAlgorithm.NAME);
        settings.addSimpleOption(new SimpleOption("Population Size"));
        settings.addSimpleOption(new SimpleOption("Number of Iterations"));
        settings.addSimpleOption(new SimpleOption("Elite Count"));
        settings.addSimpleOption(new SimpleOption("Crossing Propability"));
        settings.addSimpleOption(new SimpleOption("Mutation Propability"));
        settings.addMultiOption((MultiOption)Mutator.getOptions());
        settings.addMultiOption((MultiOption)Crosser.getOptions());
        return settings;
    }

    public List<Subject> getOptimalRouteHistory(){
        return this.history;
    }

    //GENETIC ALGORITHM RELATED METHODS FROM HERE ONWARDS
    private void setDistanceMatrix(double[][] locations){
        this.distanceMatrix = new double[locations.length][locations.length];
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                this.distanceMatrix[i][j] = Math.sqrt(Math.pow(locations[i][0] - locations[j][0],2)
                                                    + Math.pow(locations[i][1] - locations[j][1],2));
            }
        }
    }

    private void setPopulation(Population newPopulation){
        this.population = newPopulation;
    }

    public void setAdaptor(double[][] distanceMatrix){
        this.adaptor = new Adaptor(distanceMatrix);
    }

    public double[][] getLocations(){
        return this.locations;
    }

    public void setLocations(List<Double> dataInput){

        double[][] tempLocations = new double[dataInput.size()/2][2];
        int j = 0;

        for(int i = 0; i < dataInput.size(); i = i+2){
            tempLocations[j][0] = dataInput.get(i);
            tempLocations[j][1] = dataInput.get(i+1);
            j++;
        }
        this.locations = tempLocations;
    }

    private void prepareAlgorithm(List<Double> locations, int populationSize){
        this.setLocations(locations);
        this.setDistanceMatrix(this.getLocations());
        this.setAdaptor(this.distanceMatrix);
        this.setPopulation(new Population(populationSize,this.locations.length));
        this.population.setAdaptation(this.adaptor);
    }

    private void singleIteration(int eliteCount, double crossingPropability, double mutationPropability,
                                 String[] crossingMethods, String[] mutationMethods){
        this.population.setAdaptation(this.adaptor);
        this.history.add(this.population.getBestAdaptedSubject());
        this.population.shuffle();
        Subject bestAdaptedSubject;
    }

    public Map<Method,Integer> getSelectedCrossingMethods(TspSettings settings){
        Map<Method,Integer> methodOddsMap = new HashMap<>();
        MultiOption multiOption = (MultiOption)settings.getMatchingOption("Crosser");
        String className = this.getClass().getPackage().getName()+"."+multiOption.getOptionName();
        for (SubOption subOption : multiOption.getSubOptions()) {
            if(subOption.isEnabled()) {
                try {
                    Class cl = Class.forName(className);
                    Method method = cl.getMethod(subOption.getOptionName(), Subject.class, Subject.class);
                    methodOddsMap.put(method,((Double)subOption.getValue()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return methodOddsMap;
    }

    private int[] getMethodsOdds(Map<Method,Integer> methodOdds){
        int[] odds = new int[methodOdds.values().size()];
        int sum = 0;
        for(int i = 0; i < odds.length; i++){
            sum += (int)methodOdds.values().toArray()[i];
            odds[i] = sum;
        }
        return odds;
    }

    private Method[] getMethods(Map<Method,Integer> methodOdds){
        Method[] methods = methodOdds.keySet().toArray(new Method[methodOdds.size()]);
        return methods;
    }

    public Subject[] invokeCrossing(Method method, Subject subject1, Subject subject2){
        Subject[] crossed = new Subject[2];
        try{
            crossed = (Subject[])method.invoke(null, subject1, subject2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return crossed;
    }

    public Subject[] randomCrossing(Subject subject1, Subject subject2, Method[] crossingMethods, int[] methodOdds){
        int select = RandomTSP.randomInt(0,methodOdds[methodOdds.length-1]);
        int index = 0;
        for(; index < methodOdds.length; index++){
            if(select <= methodOdds[index]){
                break;
            }
        }
        return invokeCrossing(crossingMethods[index], subject1, subject2);
    }

    private String[] getSelectedSubOptions(MultiOption multiOption){
        ArrayList<String> output = multiOption.getSubOptions().stream().filter(subOpt -> subOpt.isEnabled()).map(SubOption::getOptionName).collect(Collectors.toCollection(ArrayList::new));
        return output.toArray(new String[output.size()]);
    }

    public Map<Method,Integer> getSelectedMutationMethods(TspSettings settings){
        Map<Method,Integer> methodOddsMap = new HashMap<>();
        MultiOption multiOption = (MultiOption)settings.getMatchingOption("Mutator");
        String className = this.getClass().getPackage().getName()+"."+multiOption.getOptionName();
        for (SubOption subOption : multiOption.getSubOptions()) {
            if(subOption.isEnabled()) {
                try {
                    Class cl = Class.forName(className);
                    Method method = cl.getMethod(subOption.getOptionName(), Subject.class);
                    methodOddsMap.put(method,((Double)subOption.getValue()).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return methodOddsMap;
    }

    private Subject invokeMutation(Method method, Subject target){
        Subject mutated = new Subject();
        try{
            mutated = (Subject)method.invoke(null,target);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mutated;
    }

    public Subject randomMutation(Subject subject, Method[] mutationMethods, int[] methodOdds){
        int select = RandomTSP.randomInt(0,methodOdds[methodOdds.length-1]);
        int index = 0;
        for(; index < methodOdds.length; index++){
            if(select <= methodOdds[index]){
                break;
            }
        }
        return invokeMutation(mutationMethods[index],subject);
    }

    private void prepareAlgorithm(List<Double> locations, int populationSize, int eliteCount){
        this.setLocations(locations);
        this.setDistanceMatrix(this.getLocations());
        this.setAdaptor(this.distanceMatrix);

        while((populationSize%eliteCount) != 0){
            populationSize++;
            if(eliteCount >= populationSize){
                break;
            }
        }

        this.setPopulation(new Population(populationSize,this.locations.length));
        this.population.setAdaptation(this.adaptor);
    }

    private void singleIteration(int eliteCount, double crossingPropability, double mutationPropability,
                       String crossingMethod, String mutationMethod){

        this.population.setAdaptation(this.adaptor);
        this.history.add(this.population.getBestAdaptedSubject());
        this.population.shuffle();
        Subject bestAdaptedSubject;
        int[] mutationOdds = this.getMethodsOdds(this.getSelectedMutationMethods(this.settings));
        int[] crossOdds = this.getMethodsOdds(this.getSelectedCrossingMethods(this.settings));
        Method[] mutationMethods = this.getMethods(this.getSelectedMutationMethods(this.settings));
        Method[] crossMethods = this.getMethods(this.getSelectedCrossingMethods(this.settings));

        for(int s = 0; s < population.size(); s = s + eliteCount){
            bestAdaptedSubject = population.getBestAdaptedSubject(population.subList(s, s + eliteCount));
            int worseIndex = RandomTSP.randomInt(s, s + eliteCount, population.indexOf(bestAdaptedSubject));

            if(crossingPropability >= Math.random() * 100) {
                Subject temp = new Subject();
                if(crossOdds.length > 0) {
                    Subject[] temps = this.randomCrossing(bestAdaptedSubject, population.get(worseIndex), crossMethods, crossOdds);
                    if(temps[0].getAdaptation() >= temps[1].getAdaptation()){
                        temp = temps[1];
                    }else{
                        temp = temps[0];
                    }
                }
                //temp = Crosser.crossGetBest(crossingMethod, bestAdaptedSubject, population.get(worseIndex), this.adaptor);
                if(bestAdaptedSubject.getAdaptation() > temp.getAdaptation()){
                    bestAdaptedSubject = temp;
                }
            }

            this.population.set(s,bestAdaptedSubject);

            if(mutationPropability >= Math.random() * 100){
                if(mutationOdds.length > 0) {
                    for (int i = s + 1; i < s + eliteCount; i++) {
                        try {
                            bestAdaptedSubject = randomMutation(bestAdaptedSubject, mutationMethods, mutationOdds);
                            this.population.set(i, bestAdaptedSubject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void singleIteration(int eliteCount, double crossingPropability, double mutationPropability){

        this.population.setAdaptation(this.adaptor);
        this.history.add(this.population.getBestAdaptedSubject());
        this.population.shuffle();
        Subject bestAdaptedSubject;
        int[] mutationOdds = this.getMethodsOdds(this.getSelectedMutationMethods(this.settings));
        int[] crossOdds = this.getMethodsOdds(this.getSelectedCrossingMethods(this.settings));
        Method[] mutationMethods = this.getMethods(this.getSelectedMutationMethods(this.settings));
        Method[] crossMethods = this.getMethods(this.getSelectedCrossingMethods(this.settings));

        for(int s = 0; s < population.size(); s = s + eliteCount){
            bestAdaptedSubject = population.getBestAdaptedSubject(population.subList(s, s + eliteCount));
            int worseIndex = RandomTSP.randomInt(s, s + eliteCount, population.indexOf(bestAdaptedSubject));
            if(crossOdds.length > 0) {
                if (crossingPropability >= Math.random() * 100) {
                    Subject temp = new Subject();
                    Subject[] temps = this.randomCrossing(bestAdaptedSubject, population.get(worseIndex), crossMethods, crossOdds);
                    for(Subject sub : temps){
                        sub.setAdaptation(this.adaptor);
                    }
                    if (temps[0].getAdaptation() >= temps[1].getAdaptation()) {
                        temp = temps[1];
                    } else {
                        temp = temps[0];
                    }
                    if (bestAdaptedSubject.getAdaptation() > temp.getAdaptation()) {
                        bestAdaptedSubject = temp;
                    }
                }
            }

            this.population.set(s,bestAdaptedSubject);

            if(mutationPropability >= Math.random() * 100){
                if(mutationOdds.length > 0) {
                    for (int i = s + 1; i < s + eliteCount; i++) {
                        try {
                            bestAdaptedSubject = randomMutation(bestAdaptedSubject, mutationMethods, mutationOdds);
                            this.population.set(i, bestAdaptedSubject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void runGeneticAlgorithm(String txtDataPath, int populationSize, int iterations, int eliteCount, double crossingPropability
                                    ,double mutationPropability, String crossingMethod, String mutationMethod){
        this.prepareAlgorithm(this.dataLoader.getData(txtDataPath), populationSize);
        long timeStart = System.currentTimeMillis();
        for (;iterations > 0; iterations--){
            singleIteration(eliteCount, crossingPropability, mutationPropability, crossingMethod, mutationMethod);
        }
        this.executionTime = System.currentTimeMillis() - timeStart;
    }

    public void runGeneticAlgorithm(TspSettings settings, String txtDataPath, int populationSize, int iterations, int eliteCount, double crossingPropability
            ,double mutationPropability, String crossingMethod, String mutationMethod){
        this.settings = settings;
        this.prepareAlgorithm(this.dataLoader.getData(txtDataPath), populationSize);
        long timeStart = System.currentTimeMillis();
        for (;iterations > 0; iterations--){
            singleIteration(eliteCount, crossingPropability, mutationPropability, crossingMethod, mutationMethod);
        }
        this.executionTime = System.currentTimeMillis() - timeStart;
        System.out.println(this.history.get(history.size()-1).getAdaptation());
    }

    public void runGeneticAlgorithm(int populationSize, int iterations, int eliteCount, double crossingPropability
            ,double mutationPropability){
        this.prepareAlgorithm(this.settings.getLocationsDataset().locationsToArray(), populationSize);
        long timeStart = System.currentTimeMillis();
        for (;iterations > 0; iterations--){
            this.settings.currentIteration++;
            singleIteration(eliteCount, crossingPropability, mutationPropability);
        }
        this.executionTime = System.currentTimeMillis() - timeStart;
        this.resultsManager.setRunTime(this.executionTime);
        this.resultsManager.setOptimalRouteLength(this.history.get(history.size()-1).getAdaptation());
        this.resultsManager.setOptimalRoute(this.history.get(history.size()-1).getPath());
        double[] temp = new double[this.history.size()];
        int ind = 0;
        for(Subject x : this.history){
            this.resultsManager.addToHistory(x.getPath(),x.getAdaptation());
            temp[ind] = x.getAdaptation();
            ind++;
        }
        this.resultsManager.openResultsWindow();
    }


    public void showHistory(){
        for(Subject x: history){
            System.out.println(x.getAdaptation());
        }
        System.out.println("Execution time: " + this.executionTime);
    }

}

class Population extends ArrayList<Subject>{

    public Population(int populationSize, int subjectSize){
        this.ensureCapacity(populationSize);
        for(int i = 0; i < populationSize; i++){
            this.add(new Subject(subjectSize));
        }
    }

    public void setAdaptation(Adaptor adaptor){
        for (Subject subject: this) {
            subject.setAdaptation(adaptor);
        }
    }

    public Subject getBestAdaptedSubject(){
        double bestAdaptation = Double.MAX_VALUE;
        try {
            Subject bestSubject = new Subject();
            for (Subject subject : this) {
                if (subject.getAdaptation() < bestAdaptation) {
                    bestSubject = subject;
                    bestAdaptation = subject.getAdaptation();
                }
            }
            return bestSubject;
        }
        catch (Exception e){
            return new Subject();
        }
    }

    public Subject getBestAdaptedSubject(List<Subject> subList){
        double bestAdaptation = Double.MAX_VALUE;
        Subject bestSubject = new Subject();
        for (Subject subject: subList) {
            if(subject.getAdaptation() < bestAdaptation){
                bestSubject = subject;
                bestAdaptation = bestSubject.getAdaptation();
            }
        }
        return bestSubject;
    }

    public void shuffle(){
//        for(Subject subject : this){
//            int gen1 = RandomTSP.randomInt(0,this.size() - 1);
//            int gen2 = RandomTSP.randomInt(0,this.size() - 1);
//            Subject tempSub = (Subject)this.get(gen1).clone();
//            this.set(gen2,this.get(gen1));
//            this.set(gen1,tempSub);
//        }
        Collections.shuffle(this);
    }

    private double distance(double[][] distanceMatrix, int pathP1, int pathP2){
        return distanceMatrix[pathP1][pathP2];
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2));
    }

    private double distance(Point a, Point b){
        return Math.sqrt(Math.pow((a.getX() - b.getX()),2) + Math.pow((a.getY() - b.getY()),2));
    }

}

class Subject implements Cloneable{
    private int[] path; // Path should be given as int[], enumerate[]?
    private int pathLength;
    private double adaptation;

    public Object clone(){
        return new Subject(this.path);
    }

    public Subject(){
    }

    public Subject(int[] path){
        this.path = path;
        this.pathLength = path.length;
    }

    public Subject(int pathSize){
        int[] tempPath = new int[pathSize];
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < pathSize; i++) {
            list.add(i);
        }
        java.util.Collections.shuffle(list);
        for(int i = 0; i < pathSize; i++) {
            tempPath[i] = list.get(i);
        }
        this.path = tempPath;
        this.pathLength = pathSize;
    }

    public Subject(int[] path, Adaptor adaptor){
        this.path = path;
        this.pathLength = path.length;
        adaptor.setAdaptation(this);
    }

    public Subject(int pathSize, Adaptor adaptor){
        int[] tempPath = new int[pathSize];
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < pathSize; i++) {
            list.add(i);
        }
         java.util.Collections.shuffle(list);
        for(int i = 0; i < pathSize; i++) {
            tempPath[i] = list.get(i);
        }
        this.path = tempPath;
        this.pathLength = pathSize;
        adaptor.setAdaptation(this);
    }


    public int[] getPath(){
        return this.path;
    }

    public void setPath(int[] newPath){
        this.path = newPath;
    }

    public int getPathLength(){
        return this.pathLength;
    }

    public void setAdaptation(Double adaptation){
        this.adaptation = adaptation;
    }

    public void setAdaptation(Adaptor adaptor){
        adaptor.setAdaptation(this);
    }

    public double getAdaptation(){
        return this.adaptation;
    }
}

class Adaptor{
    private double[][] distanceMatrix;
    private List<Double> locations;

    public Adaptor(double[][] distanceMatrix){
        this.distanceMatrix = distanceMatrix;
    }

    public Adaptor(List<Double> locations){
        this.locations = locations;
        double[][] tempLocations = new double[locations.size()/2][2];
        int j = 0;

        for(int i = 0; i < locations.size(); i = i+2){
            tempLocations[j][0] = locations.get(i);
            tempLocations[j][1] = locations.get(i+1);
            j++;
        }
        this.setDistanceMatrix(tempLocations);
    }

    private void setDistanceMatrix(double[][] locations){
        this.distanceMatrix = new double[locations.length][locations.length];
        for(int i = 0; i < locations.length; i++){
            for(int j = 0; j < locations.length; j++){
                this.distanceMatrix[i][j] = Math.sqrt(Math.pow(locations[i][0] - locations[j][0],2)
                        + Math.pow(locations[i][1] - locations[j][1],2));
            }
        }
    }

    public void setAdaptation(Subject subject){
        double tempAdaptation = 0;
        for(int i = 0; i < subject.getPathLength() - 1; i++){
            tempAdaptation = tempAdaptation + distanceMatrix[subject.getPath()[i]][subject.getPath()[i+1]];
        }
        tempAdaptation = tempAdaptation + distanceMatrix[subject.getPathLength() - 1][subject.getPath()[0]];
        subject.setAdaptation(tempAdaptation);
    }

    public static double adaptation(Subject subject, double[][] distanceMatrix){
        double tempAdaptation = 0;
        for(int i = 0; i < subject.getPathLength() - 1; i++){
            tempAdaptation = tempAdaptation + distanceMatrix[subject.getPath()[i]][subject.getPath()[i+1]];
        }
        tempAdaptation = tempAdaptation + distanceMatrix[subject.getPathLength() - 1][subject.getPath()[0]];
        return tempAdaptation;
    }

    public static double adaptation(int[] path, double[][] distanceMatrix){
        double tempAdaptation = 0;
        for(int i = 0; i < path.length - 1; i++){
            tempAdaptation = tempAdaptation + distanceMatrix[path[i]][path[i+1]];
        }
        tempAdaptation = tempAdaptation + distanceMatrix[path.length - 1][path[0]];
        return tempAdaptation;
    }

    private double distance(double[][] distanceMatrix, int pathP1, int pathP2){
        return distanceMatrix[pathP1][pathP2];
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2));
    }

    private double distance(Point a, Point b){
        return Math.sqrt(Math.pow((a.getX() - b.getX()),2) + Math.pow((a.getY() - b.getY()),2));
    }
}

class Crosser{

    public static TspOption getOptions(){
        MultiOption option = new MultiOption("Crosser");
        try {
            Class cl = Crosser.class;
            Method[] methods = cl.getDeclaredMethods();
            for(Method m : methods){
                if(m.getParameterCount() != 0){
                    if(m.getReturnType() == Subject[].class && m.getParameters()[0].getType() == Subject.class
                            && m.getParameters()[1].getType() == Subject.class){
                        option.addSubOption(new SubOption(m.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return option;
    }

    public static Subject crossGetBest(String crossingMethod, Subject s1, Subject s2, Adaptor adaptor){
        Subject[] tempOx = ox(s1,s2);

        for(Subject s: tempOx){
            s.setAdaptation(adaptor);
        }

        if(tempOx[0].getAdaptation() < tempOx[1].getAdaptation()){
            return tempOx[0];
        }
        else{
            return tempOx[1];
        }


    }

    public static Subject[] ox(Subject s1, Subject s2){

        int[] crossedPath1 = s1.getPath().clone();
        int[] crossedPath2 = s2.getPath().clone();
        int N = crossedPath1.length;
        int crossP2 = new Random().nextInt(N-1) + 1;
        int crossP1 = new Random().nextInt(crossP2);
        int L = crossP2 - crossP1;
        int[] nCrossedPath1 = new int[N];
        int[] nCrossedPath2 = new int[N];
        for(int i = 0; i < N; i++){
            nCrossedPath1[i] = -2;
            nCrossedPath2[i] = -2;
        }
        System.arraycopy(crossedPath1,crossP1,nCrossedPath1,crossP1,(L));
        System.arraycopy(crossedPath2,crossP1,nCrossedPath2,crossP1,(L));

        for(int i = crossP1; i < crossP2; i++){
            int c1 = 0;
            int c2 = 0;
            while(crossedPath2[c1] != nCrossedPath1[i]){
                c1++;
            }

            while(crossedPath1[c2] != nCrossedPath2[i]){
                c2++;
            }
            crossedPath2[c1] = -1;
            crossedPath1[c2] = -1;
        }
        int temp1 = 0;
        int temp2 = 0;
        for(int i = 0; i < crossP1; i++){
            while(crossedPath2[temp1] == -1){
                temp1++;
            }
            nCrossedPath1[i] = crossedPath2[temp1];
            temp1++;
            while(crossedPath1[temp2] == -1){
                temp2++;
            }
            nCrossedPath2[i] = crossedPath1[temp2];
            temp2++;
        }
        for(int i = crossP2; i < N; i++){
            while(crossedPath2[temp1] == -1){
                temp1++;
            }
            nCrossedPath1[i] = crossedPath2[temp1];
            temp1++;
            while(crossedPath1[temp2] == -1){
                temp2++;
            }
            nCrossedPath2[i] = crossedPath1[temp2];
            temp2++;
        }
        return new Subject[]{new Subject(nCrossedPath1), new Subject(nCrossedPath2)};
    }

    public static Subject[] cx(Subject s1, Subject s2) {
        int[] crossedPath1 = s1.getPath().clone();
        int[] crossedPath2 = s2.getPath().clone();
        int N = crossedPath1.length;
        int[] nCrossedPath1 = new int[N];
        int[] nCrossedPath2 = new int[N];

        cxCrossing(crossedPath1,crossedPath2,nCrossedPath1,nCrossedPath2);
        cxCrossing(crossedPath2,crossedPath1,nCrossedPath2,nCrossedPath1);

        return new Subject[]{new Subject(nCrossedPath1), new Subject(nCrossedPath2)};
    }

    private static void cxCrossing(int[] parent1, int[] parent2, int[] targetChild1, int[] targetChild2){
        int start = parent1[0];
        targetChild1[0] = start;
        int sought = parent2[0];
        targetChild2[0] = parent2[0];
        int N = parent1.length;
        int[] positions = new int[N];
        int index = 0;
        while(sought != start) {
            int pos = Crosser.seekPointIndex(sought, parent1);
            positions[pos] = 1;
            targetChild1[pos] = parent1[pos];
            sought = parent2[pos];
        }
        for(int i = 1; i < N; i++){
            if(positions[i] != 1){
                targetChild1[i] = parent2[i];
                targetChild2[i] = parent1[i];
            }else{
                targetChild2[i] = parent2[i];
            }
        }
    }

    private static int seekPointIndex(int point, int[] table) {
        for(int i = 0; i < table.length; i++) {
            if (point == table[i]) {
                return i;
            }
        }
        return 0;
    }


}

class Mutator{

    private double mutationPropability;

    public Mutator(){
    }

    public static TspOption getOptions(){
        MultiOption option = new MultiOption("Mutator");
        try {
            Class cl = Mutator.class;
            Method[] methods = cl.getDeclaredMethods();
            for(Method m : methods){
                if(m.getParameterCount() != 0){
                    if(m.getReturnType() == Subject.class && m.getParameters()[0].getType() == Subject.class){
                        option.addSubOption(new SubOption(m.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return option;
    }

//    public Subject chooseMutation(Subject subject, String... x){
//        return new Subject();
//    }

    public static Subject mutate(String mutationMethod, Subject subject, int mutationCount){
        return subject;
    }

//    public static Subject randomMutation(Subject subject){
//        int choice = RandomTSP.randomInt(0,3);
//
//        switch (choice){
//            case 0:
//                return swap(subject);
//            case 1:
//                return push(subject);
//            case 2:
//                return invert(subject);
//            default:
//                return subject;
//        }
//    }

    public static Subject swap(Subject subject){

        int[] mutatedPath = new int[subject.getPathLength()];
        mutatedPath = subject.getPath().clone();
        int mutP1 = new Random().nextInt(mutatedPath.length);
        int mutP2 = new Random().nextInt(mutatedPath.length);
        int temp = mutatedPath[mutP1];
        mutatedPath[mutP1] = mutatedPath[mutP2];
        mutatedPath[mutP2] = temp;
        return new Subject(mutatedPath);
    }

    public static Subject push(Subject subject){
        int[] mutatedPath = subject.getPath().clone();
        int mutP2 = new Random().nextInt(mutatedPath.length - 1) + 1;
        int mutP1 = new Random().nextInt(mutP2);
        int temp = mutatedPath[mutP1];
        for(int i = mutP1; i < mutP2; i++){
            mutatedPath[i] = mutatedPath[i+1];
        }
        mutatedPath[mutP2] = temp;
        return new Subject(mutatedPath);
    }

    public static Subject invert(Subject subject){

        int[] mutatedPath = subject.getPath().clone();
        int mutP2 = new Random().nextInt(mutatedPath.length - 1) + 1;
        int mutP1 = new Random().nextInt(mutP2);
        int temp;
        for(int i = 0; mutP2 - i > mutP1 + i; i++){
            temp = mutatedPath[mutP1 + i];
            mutatedPath[mutP1 + i] = mutatedPath[mutP2 - i];
            mutatedPath[mutP2 - i] = temp;
        }
        return new Subject(mutatedPath);
    }

    public static <T> T chooseMethod(String methodName, Object subject1){

        T result;
        try {
            Method tempMethod = Mutator.class.getDeclaredMethod(methodName);
            result = (T) tempMethod.invoke(subject1);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

}

class DataLoader{

    private int dataCount;

    private void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getDataCount() {
        return dataCount;
    }

    List<Double> getData(String inputFilePath) {

        List<Double> data = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(inputFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("1")){
                    do {
                        String[] dataString = line.split("\\s+");
                        for (String singleData: dataString) {
                            if(singleData.contains(".")){
                                data.add(Double.parseDouble(singleData));
                            }
                        }
                    }while ((line = br.readLine()) != null);
                }
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        this.setDataCount(data.size());
        return data;
    }
}



