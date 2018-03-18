/*
 * Copyright (c) 2018.
 * Nico Feld
 * 1169233
 */
package Levels;

public abstract class Level
{
    public class Field
    {
        public int x,y;
        public int status;
    }

    private Field start;
    private Field end;
    private int size;
    private Field[][] map;

    public Level(int size) {
        this.size = size;
        this.map = new Field[size][size];
        for (int i = 0;i<map.length;i++)
            for (int j = 0;j<map[0].length;j++) {
                map[i][j] = new Field();
                map[i][j].x = i;
                map[i][j].y = j;
            }
    }

    public int getSize() {
        return size;
    }

    public Field[][] getMap() {
        return map;
    }

    public Field getStart() {
        return start;
    }

    public void setStart(Field start) {
        this.start = start;
    }

    public Field getEnd() {
        return end;
    }

    public void setEnd(Field end) {
        this.end = end;
    }
}