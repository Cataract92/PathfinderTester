package Algorithms;

import Levels.Level;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class PFAlgorithm {

    String name;
    Level level = null;

    ArrayList<Level.Field> path = new ArrayList<>();

    public PFAlgorithm(String name) {
        this.name = name;
    }

    public void init(Level level){
        this.level = level;
    }

    abstract public void compute();

    public void paintPath(JPanel[][] jPanels) {
        for (Level.Field field : path)
        {
            jPanels[field.x][field.y].setBackground(Color.GREEN);
        }
    }

    public String getName() {
        return name;
    }
}
