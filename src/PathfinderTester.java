import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class PathfinderTester {

    public static class Field
    {
        public int x,y;
        public int status;
    }

    public static class Level
    {
        public Field[][] map;
        public Field startPoint;
        public Field endPoint;
    }


    static Level currentLevel;
    static JPanel[][] jPanels;

    public static void main(String[] args)
    {
        JFrame jFrame = new JFrame("PathfinderTester");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(20*30,20*30);
        jFrame.setLayout(new GridLayout(20,20));

        currentLevel = new Level();

        currentLevel.map = new Field[20][20];
        jPanels = new JPanel[20][20];

        for (int i = 0;i<currentLevel.map.length;i++)
            for (int j = 0;j<currentLevel.map[0].length;j++) {
                currentLevel.map[i][j] = new Field();
                currentLevel.map[i][j].x = i;
                currentLevel.map[i][j].y = j;
            }

        currentLevel.startPoint = currentLevel.map[0][0];
        currentLevel.endPoint = currentLevel.map[19][19];

        for (int i = 0;i<20;i++)
        {
            currentLevel.map[10][i].status = -1;
        }

        currentLevel.map[10][3].status = 0;

        for (int i = 0;i<=15;i++)
        {
            currentLevel.map[8][i].status = -1;
        }
        for (int i = 0;i<=5;i++)
        {
            currentLevel.map[8-i][13].status = -1;
        }
        for (int i = 0;i<=6;i++)
        {
            currentLevel.map[6-i][5].status = -1;
        }

        currentLevel.startPoint.status = -2;
        currentLevel.endPoint.status = -3;



        for (int i = 0;i<currentLevel.map.length;i++) {
            for (int j = 0; j < currentLevel.map[0].length; j++) {
                jPanels[i][j] = new JPanel();
                jPanels[i][j].setSize(10, 10);
                switch (currentLevel.map[i][j].status) {
                    case -1: {
                        jPanels[i][j].setBackground(Color.BLACK);
                        break;
                    }
                    case -2: {
                        jPanels[i][j].setBackground(Color.BLUE);
                        break;
                    }
                    case -3: {
                        jPanels[i][j].setBackground(Color.RED);
                        break;
                    }
                    default: {
                        jPanels[i][j].setBackground(Color.GRAY);
                        break;
                    }
                }
                jFrame.add(jPanels[i][j]);
            }
        }
        testAStar();
        testMinPlus();
        jFrame.setVisible(true);
    }

    public static void testAStar()
    {

        HashMap<Field,Field> precessors = new HashMap<>();
        HashMap<Field,Double> g = new HashMap<>();
        ArrayList<Field> openList = new ArrayList<>();
        ArrayList<Field> closedList = new ArrayList<>();

        openList.add(currentLevel.startPoint);
        g.put(currentLevel.startPoint,0d);

        while (!openList.isEmpty()) {
            Field currentField = null;
            for (Field field : openList) {
                if (currentField == null || g.get(field) < g.get(currentField))
                    currentField = field;
            }
            g.put(currentField,0d);
            openList.remove(currentField);

            if (currentField == currentLevel.endPoint){
                /*
                Field prev = precessors.get(currentLevel.endPoint);
                while (prev != currentLevel.startPoint)
                {
                    jPanels[prev.x][prev.y].setBackground(Color.GREEN);
                    prev = precessors.get(prev);
                }

                */
                break;
            }

            closedList.add(currentField);

            for (int i = -1;i<= 1;i++)
            {
                for (int j = -1;j<= 1;j++)
                {
                    Field successor = null;
                    try {
                        successor = currentLevel.map[currentField.x + i][currentField.y + j];
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                        continue;
                    }

                    if (successor.status == -1)
                        closedList.add(successor);

                    if ((i == 0 && j == 0) || closedList.contains(successor))
                        continue;

                    double temp_g = Double.MAX_VALUE -1d;
                    if (g.containsKey(currentField))
                        temp_g = g.get(currentField) + 1;

                    double temp_node_g = Double.MAX_VALUE -1d;
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


    public static void testMinPlus()
    {

        double[][] costMatrix = new double[20*20][20*20];

        for (int i = 0; i<currentLevel.map.length; i++)
        {
            for (int j = 0; j<currentLevel.map[0].length; j++)
            {

                Field a = currentLevel.map[i][j];

                for (int k = 0; k<currentLevel.map.length; k++)
                {
                    for (int l = 0; l<currentLevel.map[0].length; l++) {

                        Field b = currentLevel.map[k][l];

                        if (a == b) {
                            costMatrix[a.x+20*a.y][b.x+20*b.y] = 0;
                            continue;
                        }

                        if (b.status == -1) {
                            costMatrix[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                            continue;
                        }

                        if (Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2)) <= Math.sqrt(2)) {
                            costMatrix[a.x+20*a.y][b.x+20*b.y] =  Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2));
                            continue;
                        }

                        costMatrix[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                    }
                }
            }
        }

        double[][] prev = costMatrix;


        do{

            double[][] tmp = new double[20*20][20*20];
            for (int i = 0; i < 20*20; i++)
            {
                for (int j = 0; j < 20*20; j++)
                {
                    double min = Double.MAX_VALUE;
                    for (int l = 0; l < 20*20; l++)
                    {
                        if (prev[i][l] + costMatrix[l][j] < min)
                        {
                            min = prev[i][l] + costMatrix[l][j];
                        }
                    }
                    tmp[i][j] = min;
                }
            }

            prev = costMatrix;
            costMatrix = tmp;

        } while (!Arrays.deepEquals(prev,costMatrix));

        System.out.println("No Loop!");

        Field tmp = currentLevel.endPoint;
        while (tmp != currentLevel.startPoint)
        {
            Field min = tmp;
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    try {
                        if (costMatrix[currentLevel.startPoint.x + 20*currentLevel.startPoint.y][(tmp.x+i)+20*(tmp.y+j)] < costMatrix[currentLevel.startPoint.x + 20*currentLevel.startPoint.y][(min.x)+20*(min.y)])
                            min = currentLevel.map[tmp.x+i][tmp.y+j];
                    }catch (ArrayIndexOutOfBoundsException e)
                    {
                        continue;
                    }
                }
            }
            tmp = min;

            if (tmp != currentLevel.startPoint)
                jPanels[min.x][min.y].setBackground(Color.GREEN);

        }

    }

    public static class Matrix
    {
        public double[][] m;

        public Matrix(int width, int height)
        {
            m = new double[width*height][width*height];
        }
    }

}
