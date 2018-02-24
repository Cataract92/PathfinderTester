import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
                        jPanels[i][j].setBackground(Color.GREEN);
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
        jFrame.setVisible(true);
    }

    public static void testAStar()
    {
        HashMap<Field,Float> openList = new HashMap<>();
        HashMap<Field,Field> preds = new HashMap<>();
        ArrayList<Field> closedList = new ArrayList<>();

        openList.put(currentLevel.startPoint,0.f);

        while (!openList.isEmpty()) {
            Field currentField = null;
            for (Field field : openList.keySet()) {
                if (currentField == null || openList.get(field) < openList.get(currentField))
                    currentField = field;
            }

            if (currentField == currentLevel.endPoint){
                Field prev = preds.get(currentLevel.endPoint);
                while (prev != currentLevel.startPoint)
                {
                    jPanels[prev.x][prev.y].setBackground(Color.GREEN);
                    prev = preds.get(prev);
                }
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

                    float newValue = openList.get(currentField) + 10;

                    if (openList.keySet().contains(successor) && newValue >= openList.get(successor))
                        continue;

                    preds.put(successor,currentField);
                    openList.put(successor,newValue+(float)Math.sqrt(Math.pow(currentLevel.endPoint.x - currentLevel.startPoint.x,2) + Math.pow(currentLevel.endPoint.y - currentLevel.startPoint.y,2)));
                    jPanels[successor.x][successor.y].add(new JLabel(""+(int) (newValue+(float)Math.sqrt(Math.pow(currentLevel.endPoint.x - currentLevel.startPoint.x,2) + Math.pow(currentLevel.endPoint.y - currentLevel.startPoint.y,2)))));
                }
            }
            openList.remove(currentField);
        }
        System.out.println("No Path found");

    }

}
