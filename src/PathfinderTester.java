import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static class FieldMatrix
    {
        public double[][] m;

        public FieldMatrix(int width, int height)
        {
            m = new double[width*height][width*height];
        }
    }

    public static class Tuple
    {
        public Field field;
        public double distance;

        public Tuple(Field field, double distance) {
            this.field = field;
            this.distance = distance;
        }
    }

    static Level currentLevel;
    static JPanel[][] jPanels;

    public static void main(String[] args)
    {
        JFrame jFrame = new JFrame("PathfinderTester");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(20*30,20*30);

        JPanel text = new JPanel();
        text.add(new JLabel("LMB: StartPoint; RMB EndPoint; MMB Barrier"));

        jFrame.add(text,BorderLayout.NORTH);

        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(20,20));
        jFrame.add(grid,BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton startAStar = new JButton("A*");
        startAStar.addActionListener(e -> {
            for (int i = 0;i<currentLevel.map.length;i++) {
                for (int j = 0; j < currentLevel.map[0].length; j++) {
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
                        }
                    }
                }
            }
            testAStar();
            jFrame.setVisible(true);
        });
        buttons.add(startAStar);

        JButton startMinMax = new JButton("MinPlus");
        startMinMax.addActionListener(e -> {
            for (int i = 0;i<currentLevel.map.length;i++) {
                for (int j = 0; j < currentLevel.map[0].length; j++) {
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
                        }
                    }
                }
            }
            testMinPlus();
            jFrame.setVisible(true);
        });
        buttons.add(startMinMax);

        JButton startVis = new JButton("Vis");
        startVis.addActionListener(e -> {
            for (int i = 0;i<currentLevel.map.length;i++) {
                for (int j = 0; j < currentLevel.map[0].length; j++) {
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
                        }
                    }
                }
            }
            testVisibilityGraphs();
            jFrame.setVisible(true);
        });
        buttons.add(startVis);

        jFrame.add(buttons,BorderLayout.SOUTH);


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
                jPanels[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1)
                        {
                            JPanel tmp = (JPanel) e.getSource();
                            int tmpi = 0;
                            int tmpj = 0;
                            for (int i = 0; i< currentLevel.map.length; i++)
                            {
                                for (int j = 0; j < currentLevel.map[0].length; j++)
                                {
                                    if (jPanels[i][j] == tmp)
                                    {
                                        tmpi = i;
                                        tmpj = j;
                                        break;
                                    }
                                }
                            }

                            if (currentLevel.map[tmpi][tmpj].status != 0)
                                return;

                            currentLevel.startPoint.status = 0;
                            jPanels[currentLevel.startPoint.x][currentLevel.startPoint.y].setBackground(Color.GRAY);

                            currentLevel.startPoint = currentLevel.map[tmpi][tmpj];
                            tmp.setBackground(Color.BLUE);
                        } else if (e.getButton() == MouseEvent.BUTTON2)
                        {
                            JPanel tmp = (JPanel) e.getSource();
                            int tmpi = 0;
                            int tmpj = 0;
                            for (int i = 0; i< currentLevel.map.length; i++)
                            {
                                for (int j = 0; j < currentLevel.map[0].length; j++)
                                {
                                    if (jPanels[i][j] == tmp)
                                    {
                                        tmpi = i;
                                        tmpj = j;
                                        break;
                                    }
                                }
                            }

                            if (currentLevel.map[tmpi][tmpj].status == 0)
                            {
                                currentLevel.map[tmpi][tmpj].status = -1;
                                tmp.setBackground(Color.BLACK);
                            } else if (currentLevel.map[tmpi][tmpj].status == -1)
                            {
                                currentLevel.map[tmpi][tmpj].status = 0;
                                tmp.setBackground(Color.GRAY);
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3)
                        {
                            JPanel tmp = (JPanel) e.getSource();
                            int tmpi = 0;
                            int tmpj = 0;
                            for (int i = 0; i< currentLevel.map.length; i++)
                            {
                                for (int j = 0; j < currentLevel.map[0].length; j++)
                                {
                                    if (jPanels[i][j] == tmp)
                                    {
                                        tmpi = i;
                                        tmpj = j;
                                        break;
                                    }
                                }
                            }

                            if (currentLevel.map[tmpi][tmpj].status != 0)
                                return;

                            currentLevel.endPoint.status = 0;
                            jPanels[currentLevel.endPoint.x][currentLevel.endPoint.y].setBackground(Color.GRAY);

                            currentLevel.endPoint = currentLevel.map[tmpi][tmpj];
                            tmp.setBackground(Color.RED);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
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
                grid.add(jPanels[i][j]);
            }
        }

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

                Field prev = precessors.get(currentLevel.endPoint);
                while (prev != currentLevel.startPoint)
                {
                    jPanels[prev.x][prev.y].setBackground(Color.GREEN);
                    prev = precessors.get(prev);
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


    public static void testMinPlus()
    {
        FieldMatrix costMatrix = new FieldMatrix(20,20);
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
                            costMatrix.m[a.x+20*a.y][b.x+20*b.y] = 0;
                            continue;
                        }

                        if (b.status == -1) {
                            costMatrix.m[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                            continue;
                        }

                        if (Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2)) <= Math.sqrt(2)) {
                            costMatrix.m[a.x+20*a.y][b.x+20*b.y] =  Math.sqrt(Math.pow(a.x-b.x,2) + Math.pow(a.y-b.y,2));
                            continue;
                        }

                        costMatrix.m[a.x+20*a.y][b.x+20*b.y] = Double.MAX_VALUE;
                    }
                }
            }
        }

        FieldMatrix prev = costMatrix;

        do{

            FieldMatrix tmp = MatrixMinPlus(prev,costMatrix);

            prev = costMatrix;
            costMatrix = tmp;

        } while (!Arrays.deepEquals(prev.m,costMatrix.m));

        Field tmp = currentLevel.endPoint;
        while (tmp != currentLevel.startPoint)
        {
            Field min = tmp;
            for (int i = -1; i <= 1; i++)
            {
                for (int j = -1; j <= 1; j++)
                {
                    try {
                        if (costMatrix.m[currentLevel.startPoint.x + 20*currentLevel.startPoint.y][(tmp.x+i)+20*(tmp.y+j)] < costMatrix.m[currentLevel.startPoint.x + 20*currentLevel.startPoint.y][(min.x)+20*(min.y)])
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

    public static FieldMatrix MatrixMinPlus(FieldMatrix m1, FieldMatrix m2)
    {
        FieldMatrix tmp = new FieldMatrix(20,20);
        for (int i = 0; i < tmp.m.length; i++)
        {
            for (int j = 0; j < tmp.m[0].length; j++)
            {
                double min = Double.MAX_VALUE;
                for (int l = 0; l < tmp.m.length; l++)
                {
                    if (m2.m[i][l] + m1.m[l][j] < min)
                    {
                        min = m2.m[i][l] + m1.m[l][j];
                    }
                }
                tmp.m[i][j] = min;
            }
        }
        return tmp;
    }


    static HashMap<Field,HashMap<Field,Double>> visibilityMap = new HashMap<>();
    static HashMap<Field,HashMap<Field,Tuple>> routeMap = new HashMap<>();

    public static void testVisibilityGraphs()
    {

        ArrayList<Field> navPoints = new ArrayList<>();
        ArrayList<Rectangle> barriers = new ArrayList<>();


        for (int i = 0; i < currentLevel.map.length; i++)
        {
            for (int j = 0; j < currentLevel.map[0].length; j++)
            {
                if (currentLevel.map[i][j].status != -1)
                    continue;

                barriers.add(new Rectangle(i,j,1,1));
                for (int k = -1; k <= 1; k++)
                {
                    for (int l = -1; l <= 1; l++)
                    {
                        try {
                            if (!navPoints.contains(currentLevel.map[i+k][j+l]) && currentLevel.map[i+k][j+l].status != -1)
                                navPoints.add(currentLevel.map[i+k][j+l]);

                        } catch (ArrayIndexOutOfBoundsException e)
                        {
                            continue;
                        }
                    }
                }
            }
        }


        for (Field start : navPoints)
        {
            visibilityMap.put(start,new HashMap<>());
            routeMap.put(start,new HashMap<>());
            for (Field end : navPoints)
            {
                boolean isVisible = true;
                Line2D line = new Line2D.Double(start.x, start.y,end.x, end.y);
                for (Rectangle rect : barriers)
                {
                    if (line.intersects(rect)){
                        isVisible = false;
                        break;
                    }
                }
                if (isVisible)
                    visibilityMap.get(start).put(end,Math.sqrt(Math.pow(start.x - end.x,2)+Math.pow(start.y - end.y,2)));
            }
        }


        for (Field vis : visibilityMap.keySet())
        {
            for (Field vis2 : visibilityMap.get(vis).keySet())
            {
                jPanels[vis2.x][vis2.y].setBackground(Color.CYAN);
            }
        }

        for (Field start : navPoints)
        {
            routeMap.get(start).put(start, new Tuple(start, 0d));
            for (Field nav : visibilityMap.get(start).keySet()) {
                Tuple t;
                if (!routeMap.get(nav).containsKey(start)) {
                    t = new Tuple(start, visibilityMap.get(start).get(nav));
                    routeMap.get(nav).put(start,t);
                }
                t = routeMap.get(nav).get(start);

                if (t.distance > visibilityMap.get(start).get(nav))
                {
                    t.distance = visibilityMap.get(start).get(nav);
                    t.field = start;
                }

                route(start,nav);
            }
        }



    }

    public static void route(Field start, Field current)
    {
        for (Field nav : visibilityMap.get(current).keySet())
        {
            if (nav == start)
                continue;

            Tuple t;

            if (!routeMap.get(nav).containsKey(start)) {
                t = new Tuple(current, visibilityMap.get(current).get(nav) + routeMap.get(current).get(start).distance);
                routeMap.get(nav).put(start,t);
                route(start,nav);
                continue;
            }

            t = routeMap.get(nav).get(start);

            if (t.distance > visibilityMap.get(current).get(nav) + routeMap.get(current).get(start).distance)
            {
                t.distance = visibilityMap.get(current).get(nav) + routeMap.get(current).get(start).distance;
                t.field = current;
                route(start,nav);
                continue;
            }
        }
    }

}
