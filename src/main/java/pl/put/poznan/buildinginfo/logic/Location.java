package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class representing any location in the building hierarchy (building, level, or room).
 */
public abstract class Location {
    private int id;
    private String name;

    public Location() {
    }

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract double getArea();

    public abstract double getCube();

    public abstract double getLight();

    /**
     * Calculates lighting power per square meter (m²) for this location.
     * Returns 0.0 when total area is non-positive or lighting power is negative.
     *
     * @return lighting power per m²
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getLightPerArea() {
        double area = getArea();
        double light = getLight();
        if (area <= 0.0 || light < 0.0) {
            return 0.0;
        }
        return light / area;
    }
}
