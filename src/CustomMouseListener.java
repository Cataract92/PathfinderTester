/*
 * Copyright (c) 2018.
 * Nico Feld
 * 1169233
 */
import Levels.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class CustomMouseListener implements MouseListener {

    private Level level;
    private HashMap<JPanel,Level.Field> fieldHashMap;
    private JPanel[][] jPanels;

    public CustomMouseListener(Level level, HashMap<JPanel,Level.Field> map, JPanel[][] jPanels) {
        this.level = level;
        this.fieldHashMap = map;
        this.jPanels = jPanels;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JPanel source = (JPanel) e.getSource();
        Level.Field sourceField = fieldHashMap.get(source);

        if (e.getButton() == MouseEvent.BUTTON1) {

            if (sourceField.status != 0)
                return;

            level.getStart().status = 0;
            jPanels[level.getStart().x][level.getStart().y].setBackground(Color.GRAY);

            level.setStart(sourceField);
            sourceField.status = -2;
            source.setBackground(Color.BLUE);

        } else if (e.getButton() == MouseEvent.BUTTON2) {

            if (sourceField.status == 0) {
                sourceField.status = -1;
                source.setBackground(Color.BLACK);
            } else if (sourceField.status == -1) {
                sourceField.status = 0;
                source.setBackground(Color.GRAY);
            }

        } else if (e.getButton() == MouseEvent.BUTTON3) {

            if (sourceField.status != 0)
                return;

            level.getEnd().status = 0;
            jPanels[level.getEnd().x][level.getEnd().y].setBackground(Color.GRAY);

            level.setStart(sourceField);
            sourceField.status = -3;
            source.setBackground(Color.RED);
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
}
