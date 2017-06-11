package com.study;

import com.study.gui.*;

import java.lang.reflect.Method;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Gui gui = new Gui();
        frame.setContentPane(gui.getMainWindow());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }





    public static void getMethods(String figureName){
        try {
            Class cl = Class.forName(figureName);
            Method[] methods = cl.getDeclaredMethods();

            System.out.println("Dostepne metody: ");

            for(Method m : methods) {
                if (m.getParameterCount() != 0) {
                    System.out.println("NAZWA: " + m.getName() + " ZWRACA: " + m.getReturnType().getSimpleName() + " PARAMTR: " + m.getParameters()[0].getType().getSimpleName());
                    if (m.getReturnType() == Subject[].class && m.getParameters()[0].getType() == Subject.class && m.getParameters()[1].getType() == Subject.class) {
                        System.out.println("KURWAAAAAAAAA");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
