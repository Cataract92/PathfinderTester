/*
 * Copyright (c) 2018.
 * Nico Feld
 * 1169233
 */
package Algorithms;

import Levels.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class AStar extends PFAlgorithm {

    private HashMap<Level.Field,Level.Field> precessors = new HashMap<>();
    private HashMap<Level.Field,Double> g = new HashMap<>();
    private ArrayList<Level.Field> openList = new ArrayList<>();
    private ArrayList<Level.Field> closedList = new ArrayList<>();

    public AStar() {
        super("AStar");
    }

    @Override
    public void init(Level level) {
        super.init(level);

        precessors.clear();
        g.clear();
        openList.clear();
        closedList.clear();
    }

    @Override
    public void compute() {

        openList.add(level.getStart());
        g.put(level.getStart(),0d);

        while (!openList.isEmpty()) {
            Level.Field currentField = null;
            for (Level.Field field : openList) {
                if (currentField == null || g.get(field) < g.get(currentField))
                    currentField = field;
            }
            g.put(currentField,0d);
            openList.remove(currentField);

            if (currentField == level.getEnd()){

                Level.Field prev = precessors.get(level.getEnd());
                while (prev != level.getStart())
                {
                    path.add(prev);
                    prev = precessors.get(prev);
                }
                break;
            }

            closedList.add(currentField);

            for (int i = -1;i<= 1;i++)
            {
                for (int j = -1;j<= 1;j++)
                {
                    Level.Field successor = null;
                    try {
                        successor = level.getMap()[currentField.x + i][currentField.y + j];
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                        continue;
                    }

                    if (successor.status == -1)
                        closedList.add(successor);

                    if ((i == 0 && j == 0) || closedList.contains(successor))
                        continue;

                    double temp_g = Double.MAX_VALUE;
                    if (g.containsKey(currentField))
                        temp_g = g.get(currentField) + Math.sqrt(Math.pow(successor.x-currentField.x,2) + Math.pow(successor.y-currentField.y,2));

                    double temp_node_g = Double.MAX_VALUE;
                    if (g.containsKey(successor))
                        temp_node_g = g.get(successor);

                    if (openList.contains(successor) && temp_g >= temp_node_g)
                        continue;

                    precessors.put(successor,currentField);
                    g.put(successor,temp_g);
                    if (!openList.contains(successor))
                        openList.add(successor);

                }
            }
        }
    }
}
