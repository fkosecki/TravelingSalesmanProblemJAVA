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
}
