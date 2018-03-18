/*
 * Copyright (c) 2018.
 * Nico Feld
 * 1169233
 */
package Algorithms;

import Levels.Level;
import java.util.Arrays;

public class MinPlus extends PFAlgorithm {


    private class Matrix
    {
        private double[][] matrix;

        Matrix(int width, int height)
        {
            matrix = new double[width*height][width*height];
        }

        double[][] getMatrix() {
            return matrix;
        }

        Matrix calcMinPlus(Matrix m)
        {
            Matrix tmp = new Matrix(20,20);
            for (int i = 0; i < tmp.getMatrix().length; i++)
            {
                for (int j = 0; j < tmp.getMatrix()[0].length; j++)
                {
                    double min = Double.MAX_VALUE;
                    for (int l = 0; l < tmp.getMatrix().length; l++)
                    {
                        if (m.getMatrix()[i][l] + this.getMatrix()[l][j] < min)
                        {
                            min = m.getMatrix()[i][l] + this.getMatrix()[l][j];
                        }
                    }
                    tmp.getMatrix()[i][j] = min;
                }
            }
            return tmp;
        }
    }

    private Matrix costMatrix;

    public MinPlus() {
        super("MinPlus");
    }

    @Override
    public void init(Level level) {
        super.init(level);

        costMatrix = new Matrix(level.getSize(),level.getSize());

        for (int i = 0; i<level.getMap().length; i++)
        {
            for (int j = 0; j<level.getMap()[0].length; j++)
            {

                Level.Field a = level.getMap()[i][j];

                for (int k = 0; k<level.getMap().length; k++)
                {
                    for (int l = 0; l<level.getMap()[0].length; l++) {

                        Level.Field b = level.getMap()[k][l];

                        if (a == b) {
                            costMatrix.getMatrix()[a.x+20*a.y][b.x+20*b.y] = 0;
                            continue;
                        }

                        if (b.status == -1) {
                            costMatrix.getMatrix()[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                            continue;
                        }

                        if (Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2)) <= Math.sqrt(2)) {
                            costMatrix.getMatrix()[a.x+20*a.y][b.x+20*b.y] =  Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2));
                            continue;
                        }

                        costMatrix.getMatrix()[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                    }
                }
            }
        }

        Matrix prev = costMatrix;


        do{

            Matrix tmp = prev.calcMinPlus(costMatrix);

            prev = costMatrix;
            costMatrix = tmp;

        } while (!Arrays.deepEquals(prev.getMatrix(),costMatrix.getMatrix()));

    }

    @Override
    public void compute() {


        Level.Field tmp = level.getEnd();
        while (tmp != level.getStart())
        {
            Level.Field min = tmp;
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    try {
                        if (costMatrix.getMatrix()[level.getStart().x + 20*level.getStart().y][(tmp.x+i)+20*(tmp.y+j)] < costMatrix.getMatrix()[level.getStart().x + 20*level.getStart().y][(min.x)+20*(min.y)])
                            min = level.getMap()[tmp.x+i][tmp.y+j];
                    }catch (ArrayIndexOutOfBoundsException e)
                    {
                        continue;
                    }
                }
            }
            tmp = min;

            if (tmp != level.getStart())
                path.add(tmp);
        }

    }

}
