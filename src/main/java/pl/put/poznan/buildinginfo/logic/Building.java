package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getArea() {
        return levels.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Level::getArea)
                .sum();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getLight() {
        return levels.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Level::getLight)
                .sum();
    }
}
