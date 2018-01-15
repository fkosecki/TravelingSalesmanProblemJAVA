package com.study;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/** The Held Karp algorithm:
 *
 * There are 2 possible cases in each iteration:
 *
 * A) A base case where we already know the answer. (Stopping condition)
 * B) Decreasing the number of considered vertices and calling our algorithm again. (Recursion)
 *
 * Explanation of every case:
 *
 * A) If the list of vertices is empty, return the distance between starting point and vertex.
 * B) If the list of vertices is not empty, lets decrease our problem space:
 *
 *      1) Consider each vertex in vertices as a starting point ("initial")
 *      2) As "initial" is the starting point, we have to remove it from the list of vertices
 *      3) Calculate the cost of visiting "initial" (costCurrentNode) + cost of visiting the rest from it ("costChildren")
 *      4) Return the minimum result from step 3
 */

//public class HeldKarpAlgorithm extends Algorithm{
//
//    /* ----------------------------- GLOBAL VARIABLES ------------------------------ */
//    private static final String NAME = "Held-Karp Algorithm";
//    private static final String DESCRIPTION = " Held-Karp Algorithm\n Giving more than 18 cities as input may result in outrageous execution time.";
//    private static int[][] distances;
//    private static int optimalDistance = Integer.MAX_VALUE;
//    private static String optimalPath = "";
//
//    private TspSettings settings;
//    private ResultsManager resultsManager;
//    private static int iteration = 0;
//    private static ArrayList<Integer[]> history = new ArrayList<>();
//    private static ArrayList<Integer> bestPath = new ArrayList<>();
//
//    @Override
//    // PRZEPISZ ALGORYTM ZEBY MOC GO STOSOWAC
//    public void executeAlgorithm(TspSettings settings){
//        this.settings = settings;
//        this.resultsManager = new ResultsManager(settings.getLocationsDataset());
//
//        int size = 5;
//        distances = new int[size][size];
//        for(int j = 0; j < size; j++) {
//            for (int i = 0; i < distances.length; i++) {
//                distances[i][j] = RandomTSP.randomInt(1, 30);
//            }
//            distances[j][j] = 0;
//        }
//
//        /* ------------------------- ALGORITHM INITIALIZATION ----------------------- */
//
//        // Initial variables to start the algorithm
//        String path = "";
//        int[] vertices = new int[size - 1];
//
//        // Filling the initial vertices array with the proper values
//        for (int i = 1; i < size; i++) {
//            vertices[i - 1] = i;
//        }
//        for(int x : vertices){
//            System.out.println("x: "+x);
//        }
//        // FIRST CALL TO THE RECURSIVE FUNCTION
//
//        long timeStart = System.currentTimeMillis();
//        procedure(0, vertices, path, 0);
//        System.out.println("Path: " + optimalPath + ". Distance = " + optimalDistance);
//
//        //execute
//        this.resultsManager.setRunTime(System.currentTimeMillis() - timeStart);
//        this.resultsManager.setOptimalRouteLength(this.optimalDistance);
//        int[] tempBestPath = new int[bestPath.size()];
//        for(int i = 0; i < bestPath.size(); i++){
//            tempBestPath[i] = bestPath.get(i);
//        }
//        this.resultsManager.setOptimalRoute(tempBestPath);
//        this.resultsManager.openResultsWindow();
//    }
//
//    public List<Integer[]> getOptimalRouteHistory(){
//        return this.history;
//    }
//
//    public static void main(String args[]) throws IOException{
//
//
////        /* ----------------------------- IO MANAGEMENT ----------------------------- */
////
////        // The path to the files with the distances is asked
////        Scanner input = new Scanner(System.in);
////        System.out.println("Please, introduce the path where the text file is stored");
////        String file = input.nextLine();
////
////        // The size of the distance matrix is asked
////        System.out.println("Please, introduce the size of the matrix");
////        int size = input.nextInt();
////
////        // Distances array is initiated considering the size of the matrix
////        distances = new int[size][size];
////
////        // The file in that location is opened
////        FileReader f = new FileReader(file);
////        BufferedReader b = new BufferedReader(f);
////
////
////        // Our matrix is filled with the values of the file matrix
////        for (int row = 0 ; row < size ; row++) {
////
////            // Every value of each row is read and stored
////            String line = b.readLine();
////            String[] values = line.trim().split("\\s+");
////
////            for (int col = 0; col < size; col++) {
////                distances[row][col] = Integer.parseInt(values[col]);
////            }
////        }
////
////        // Closing file
////        b.close();
////
//        int size = 3;
//        distances = new int[size][size];
//        for(int j = 0; j < size; j++) {
//            for (int i = 0; i < distances.length; i++) {
//                distances[i][j] = RandomTSP.randomInt(1, 30);
//            }
//            distances[j][j] = 0;
//        }
//        for(int j = 0; j < size; j++) {
//            for (int i = 0; i < distances.length; i++) {
//                System.out.print("["+distances[i][j]+"]");
//            }
//            System.out.println();
//            distances[j][j] = 0;
//        }
//        /* ------------------------- ALGORITHM INITIALIZATION ----------------------- */
//
//        // Initial variables to start the algorithm
//        String path = "";
//        int[] vertices = new int[size - 1];
//
//        // Filling the initial vertices array with the proper values
//        for (int i = 1; i < size; i++) {
//            vertices[i - 1] = i;
//        }
//        for(int x : vertices){
//            System.out.println("x: "+x);
//        }
//        // FIRST CALL TO THE RECURSIVE FUNCTION
//        procedure(0, vertices, path, 0);
//        System.out.println("Path: " + optimalPath + ". Distance = " + optimalDistance);
//        for(Integer x : bestPath){
//            System.out.print("["+x+"]");
//        }
//    }
//
//
//    /* ------------------------------- RECURSIVE FUNCTION ---------------------------- */
//
//    private static int procedure(int initial, int vertices[], String path, int costUntilHere) {
//
//        // We concatenate the current path and the vertex taken as initial
//        path = path + Integer.toString(initial);
//        int length = vertices.length;
//        int newCostUntilHere;
//
//
//        // Exit case, if there are no more options to evaluate (last node)
//        if (length == 0) {
//            newCostUntilHere = costUntilHere + distances[initial][0];
//
//            // If its cost is lower than the stored one
//            if (newCostUntilHere < optimalDistance){
//                optimalDistance = newCostUntilHere;
//                bestPath.clear();
//                for(int i = 0; i < path.length(); i++){
//                    bestPath.add(Integer.parseInt(path.substring(i,i+1)));
//                }
//                optimalPath = path + "0";
//            }
//
//            return (distances[initial][0]);
//        }
//
//
//        // If the current branch has higher cost than the stored one: stop traversing
//        else if (costUntilHere > optimalDistance){
//            return 0;
//        }
//
//
//        // Common case, when there are several nodes in the list
//        else {
//
//            int[][] newVertices = new int[length][(length - 1)];
//            int costCurrentNode, costChild;
//            int bestCost = Integer.MAX_VALUE;
//
//            // For each of the nodes of the list
//            for (int i = 0; i < length; i++) {
//
//                // Each recursion new vertices list is constructed
//                for (int j = 0, k = 0; j < length; j++, k++) {
//
//                    // The current child is not stored in the new vertices array
//                    if (j == i) {
//                        k--;
//                        continue;
//                    }
//                    newVertices[i][k] = vertices[j];
//                }
//
//                // Cost of arriving the current node from its parent
//                costCurrentNode = distances[initial][vertices[i]];
//
//                // Here the cost to be passed to the recursive function is computed
//                newCostUntilHere = costCurrentNode + costUntilHere;
//
//                // RECURSIVE CALLS TO THE FUNCTION IN ORDER TO COMPUTE THE COSTS
//                costChild = procedure(vertices[i], newVertices[i], path, newCostUntilHere);
//
//                // The cost of every child + the current node cost is computed
//                int totalCost = costChild + costCurrentNode;
//
//                // Finally we select from the minimum from all possible children costs
//                if (totalCost < bestCost) {
//                    iteration++;
//                    bestCost = totalCost;
//                }
//            }
//            return (bestCost);
//        }
//    }
//
//    public static TspSettings getSettings(){
//        TspSettings settings = new TspSettings(HeldKarpAlgorithm.NAME);
//        return settings;
//    }
//}

public class HeldKarpAlgorithm extends Algorithm{

    /* ----------------------------- GLOBAL VARIABLES ------------------------------ */
    public static final String NAME = "Held-Karp Algorithm";
    public static final String DESCRIPTION = " Held-Karp Algorithm\n Giving more than 12 cities as input may result in outrageous execution time.";
    private double[][] distances;
    private double optimalDistance = Double.MAX_VALUE;
    private String optimalPath = "";

    private TspSettings settings;
    private ResultsManager resultsManager;
    private int iteration = 0;
    private ArrayList<Integer[]> history = new ArrayList<>();
    private ArrayList<Integer> bestPath = new ArrayList<>();
    private void setDistances(LocationsDataset locationsDataset){
        this.distances = new double[locationsDataset.getLocationsData().size()][locationsDataset.getLocationsData().size()];
        for(int i = 0; i < locationsDataset.getLocationsData().size(); i++) {
            double x1 = locationsDataset.getLocationsData().get(i).getX();
            double y1 = locationsDataset.getLocationsData().get(i).getY();
            for(int j = 0; j < locationsDataset.getLocationsData().size(); j++) {
                double x2 = locationsDataset.getLocationsData().get(j).getX();
                double y2 = locationsDataset.getLocationsData().get(j).getY();
                this.distances[i][j] = Math.sqrt(((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2)));
            }
        }
    }

    private double[][] getDistances(){
        return this.distances;
    }

    @Override
    public void executeAlgorithm(TspSettings settings){

        this.settings = settings;
        this.resultsManager = new ResultsManager(settings.getLocationsDataset());
        this.setDistances(settings.getLocationsDataset());
        int size = this.distances.length;
        String path = "";
        int[] vertices = new int[size - 1];

        for (int i = 1; i < size; i++) {
            vertices[i - 1] = i;
        }
        for(int x : vertices){
            System.out.println("x: "+x);
        }

        long timeStart = System.currentTimeMillis();

        procedure(0, vertices, path, 0);

        bestPath.clear();
        String[] tempString = optimalPath.split("-");
        for(String x : tempString){
            bestPath.add(Integer.parseInt(x));
        }

        System.out.println("Path: " + optimalPath + ". Distance = " + optimalDistance);

        this.resultsManager.setRunTime(System.currentTimeMillis() - timeStart);
        this.resultsManager.setOptimalRouteLength(this.optimalDistance);
        int[] tempBestPath = new int[bestPath.size()];
        for(int i = 0; i < bestPath.size(); i++){
            tempBestPath[i] = bestPath.get(i);
        }
        this.resultsManager.setOptimalRoute(tempBestPath);
        this.resultsManager.openResultsWindow();
    }

    public List<Integer[]> getOptimalRouteHistory(){
        return this.history;
    }

    public static void main(String args[]) throws IOException{

    }

    private double procedure(int initial, int vertices[], String path, double costUntilHere) {

        path = path + Integer.toString(initial)+"-";
        int length = vertices.length;
        double newCostUntilHere;

        if (length == 0) {
            newCostUntilHere = costUntilHere + distances[initial][0];

            int[] tempPath = new int[path.length()];
            String[] tempString = path.split("-");
            for(int p = 0; p < tempString.length; p++){
                tempPath[p] = Integer.parseInt(tempString[p]);
            }
            this.resultsManager.addToHistory(tempPath,newCostUntilHere);
            iteration++;

            if (newCostUntilHere < optimalDistance){
                optimalDistance = newCostUntilHere;
                optimalPath = path + "0";
            }
            return (distances[initial][0]);
        }

        else if (costUntilHere > optimalDistance){
            return 0;
        }

        else {

            int[][] newVertices = new int[length][(length - 1)];
            double costCurrentNode, costChild;
            double bestCost = Double.MAX_VALUE;

            for (int i = 0; i < length; i++) {
                for (int j = 0, k = 0; j < length; j++, k++) {
                    if (j == i) {
                        k--;
                        continue;
                    }
                    newVertices[i][k] = vertices[j];
                }
                costCurrentNode = distances[initial][vertices[i]];
                newCostUntilHere = costCurrentNode + costUntilHere;
                costChild = procedure(vertices[i], newVertices[i], path, newCostUntilHere);
                double totalCost = costChild + costCurrentNode;
                if (totalCost < bestCost) {
                    bestCost = totalCost;
                }
            }
            return (bestCost);
        }
    }

    public static TspSettings getSettings(){
        TspSettings settings = new TspSettings(HeldKarpAlgorithm.NAME);
        return settings;
    }
}
