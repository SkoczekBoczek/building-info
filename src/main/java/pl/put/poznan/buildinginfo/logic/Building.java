package pl.put.poznan.buildinginfo.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a building consisting of levels.
 */
public class Building extends Location {
    private List<Level> levels = new ArrayList<>();

    public Building() {
    }

    public Building(int id, String name, List<Level> levels) {
        super(id, name);
        if (levels != null) {
            this.levels = levels;
        }
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels != null ? levels : new ArrayList<>();
    }
}
