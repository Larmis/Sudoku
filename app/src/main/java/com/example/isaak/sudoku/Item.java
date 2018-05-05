package com.example.isaak.sudoku;

/**
 * Created by isaak on 25/04/2018.
 */

public class Item {
    private String name;
    private int level;
    private int percent;

    public Item(String name, int level, int percent) {
        this.name = name;
        this.level = level;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getPercent() {
        return percent;
    }
}
