package Algorithms;

import Levels.Level;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

public class VisibilityGraphs extends PFAlgorithm {



    private class Tuple
    {
        Level.Field field;
        double distance;

        Tuple(Level.Field field, double distance) {
            this.field = field;
            this.distance = distance;
        }
    }

    private HashMap<Level.Field,HashMap<Level.Field,Double>> visibilityMap = new HashMap<>();
    private HashMap<Level.Field,HashMap<Level.Field,Tuple>> routeMap = new HashMap<>();
    private ArrayList<Level.Field> navPoints = new ArrayList<>();
    private ArrayList<Rectangle> barriers = new ArrayList<>();

    public VisibilityGraphs() {
        super("VisibilityGraphs");
    }

    @Override
    public void init(Level level) {
        super.init(level);

        visibilityMap.clear();
        routeMap.clear();
        navPoints.clear();
        barriers.clear();

        for (int i = 0; i < level.getMap().length; i++)
        {
            for (int j = 0; j < level.getMap()[0].length; j++)
            {
                if (level.getMap()[i][j].status != -1)
                    continue;

                barriers.add(new Rectangle(i,j,1,1));
                for (int k = -1; k <= 1; k++)
                {
                    for (int l = -1; l <= 1; l++)
                    {
                        try {
                            if (!navPoints.contains(level.getMap()[i+k][j+l]) && level.getMap()[i+k][j+l].status != -1)
                                navPoints.add(level.getMap()[i+k][j+l]);

                        } catch (ArrayIndexOutOfBoundsException e)
                        {
                            continue;
                        }
                    }
                }
            }
        }

        for (Level.Field f1 : navPoints)
        {
            visibilityMap.put(f1,new HashMap<>());
            routeMap.put(f1,new HashMap<>());
            for (Level.Field f2 : navPoints)
            {
                boolean isVisible = true;
                Line2D line = new Line2D.Double(f1.x, f1.y,f2.x, f2.y);
                for (Rectangle rect : barriers)
                {
                    if (line.intersects(rect)){
                        isVisible = false;
                        break;
                    }
                }
                if (isVisible)
                    visibilityMap.get(f1).put(f2,Math.sqrt(Math.pow(f1.x - f2.x,2)+Math.pow(f1.y - f2.y,2)));
            }
        }

        for (Level.Field vis : visibilityMap.keySet())
        {
            path.addAll(visibilityMap.get(vis).keySet());
        }

        for (Level.Field field : navPoints)
        {
            routeMap.get(field).put(field, new Tuple(field, 0d));
            for (Level.Field nav : visibilityMap.get(field).keySet()) {
                Tuple t;
                if (!routeMap.get(nav).containsKey(field)) {
                    t = new Tuple(field, visibilityMap.get(field).get(nav));
                    routeMap.get(nav).put(field,t);
                }
                t = routeMap.get(nav).get(field);

                if (t.distance > visibilityMap.get(field).get(nav))
                {
                    t.distance = visibilityMap.get(field).get(nav);
                    t.field = field;
                }

                route(field,nav);
            }
        }
    }

    @Override
    public void compute() {

    }


    private void route(Level.Field begin, Level.Field current)
    {
        for (Level.Field nav : visibilityMap.get(current).keySet())
        {
            if (nav == begin)
                continue;

            Tuple t;

            if (!routeMap.get(nav).containsKey(begin)) {
                t = new Tuple(current, visibilityMap.get(current).get(nav) + routeMap.get(current).get(begin).distance);
                routeMap.get(nav).put(begin,t);
                route(begin,nav);
                continue;
            }

            t = routeMap.get(nav).get(begin);

            if (t.distance > visibilityMap.get(current).get(nav) + routeMap.get(current).get(begin).distance)
            {
                t.distance = visibilityMap.get(current).get(nav) + routeMap.get(current).get(begin).distance;
                t.field = current;
                route(begin,nav);
            }
        }
    }
}
