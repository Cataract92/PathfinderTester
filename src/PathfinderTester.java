import Algorithms.AStar;
import Algorithms.MinMax;
import Algorithms.PFAlgorithm;
import Algorithms.VisibilityGraphs;
import Levels.Level;
import Levels.Level1;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PathfinderTester extends JFrame {


    private ArrayList<PFAlgorithm> algorithms = new ArrayList<>();
    private Level level;
    private JPanel[][] jPanels;
    private HashMap<JPanel,Level.Field> fieldHashMap = new HashMap<>();

    public PathfinderTester(String title) throws HeadlessException {
        super(title);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(20*30,20*30);


        JPanel text = new JPanel();
        text.add(new JLabel("LMB: StartPoint; RMB EndPoint; MMB Barrier"));

        add(text,BorderLayout.NORTH);
    }

    public void clear()
    {
        for (int i = 0;i<level.getMap().length;i++) {
            for (int j = 0; j < level.getMap()[0].length; j++) {
                switch (level.getMap()[i][j].status) {
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
    }

    public void init(Level level)
    {
        this.level = level;
        this.jPanels = new JPanel[level.getMap().length][level.getMap()[0].length];

        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(20,20));
        add(grid,BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        for (PFAlgorithm algorithm : algorithms)
        {
            JButton button = new JButton(algorithm.getName());
            button.addActionListener(e ->
            {
                testAlgorithm(algorithm);
                setVisible(true);
            });
            buttons.add(button);
        }
        add(buttons,BorderLayout.SOUTH);

        level.getStart().status = -2;
        level.getEnd().status = -3;

        for (int i = 0;i<level.getMap().length;i++) {
            for (int j = 0; j < level.getMap()[0].length; j++) {

                JPanel jPanel = new JPanel();
                jPanel.setSize(10, 10);
                jPanel.addMouseListener(new CustomMouseListener(level,fieldHashMap,jPanels));
                fieldHashMap.put(jPanel,level.getMap()[i][j]);
                jPanels[i][j] = jPanel;

                grid.add(jPanels[i][j]);
            }
        }
        clear();
        setVisible(true);
    }

    public void addAlgorithm(PFAlgorithm algorithm)
    {
        algorithms.add(algorithm);
    }

    public void testAllAlgorithms()
    {
        for (PFAlgorithm algorithm : algorithms)
        {
            clear();
            algorithm.init(level);
            algorithm.compute();
            algorithm.paintPath(jPanels);
        }
    }

    public void testAlgorithm(PFAlgorithm algorithm)
    {
        if (!algorithms.contains(algorithm))
            return;

        clear();
        algorithm.init(level);
        algorithm.compute();
        algorithm.paintPath(jPanels);
    }

    public static void main(String[] args)
    {

        PathfinderTester tester = new PathfinderTester("PathfinderTester");

        tester.addAlgorithm(new AStar());
        tester.addAlgorithm(new MinMax());
        tester.addAlgorithm(new VisibilityGraphs());


        Level level = new Level1(20);

        level.setStart(level.getMap()[0][0]);
        level.setEnd(level.getMap()[19][19]);

        tester.init(level);

        tester.testAllAlgorithms();
        tester.testAllAlgorithms();
        //tester.testAllAlgorithms();

        System.exit(0);
    }

}
