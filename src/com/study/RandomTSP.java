package com.study;


import java.util.Random;

public class RandomTSP extends Random {
    public static int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt(((max-1) - min) + 1) + min;
    }

    public static int randomInt(int min, int max, int except){
        Random rand = new Random();
        int randomNum;
        do{
            randomNum = rand.nextInt((Math.abs(max-1) - (Math.abs(min)) + 1) + (Math.abs(min)));
        }while(randomNum == except && max != min);
        return randomNum;
    }

    public static double randomDoubleBetweenValues(double value1, double value2){
        int min,max;
        if(value1 < value2){
            min = (int)(value1*100);
            max = (int)(value2*100);
        }else{
            max = (int)(value1*100);
            min = (int)(value2*100);
        }
        Random rand = new Random();
        return ((double)rand.nextInt(((max-1) - min) + 1) + min)/100;
    }
}
