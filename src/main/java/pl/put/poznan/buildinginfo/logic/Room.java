package pl.put.poznan.buildinginfo.logic;

/**
 * Represents a single room within a building level.
 */
public class Room extends Location {
    private double area;
    private double cube;
    private float heating;
    private double light;

    public Room() {
    }

    public Room(int id, String name, double area, double cube, float heating, double light) {
        super(id, name);
        this.area = area;
        this.cube = cube;
        this.heating = heating;
        this.light = light;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getCube() {
        return cube;
    }

    public void setCube(double cube) {
        this.cube = cube;
    }

    public float getHeating() {
        return heating;
    }

    public void setHeating(float heating) {
        this.heating = heating;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }
}
