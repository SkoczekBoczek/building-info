package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a building level (floor) consisting of rooms.
 */
public class Level extends Location {
    private List<Room> rooms = new ArrayList<>();

    public Level() {
    }

    public Level(int id, String name, List<Room> rooms) {
        super(id, name);
        if (rooms != null) {
            this.rooms = rooms;
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms != null ? rooms : new ArrayList<>();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getArea() {
        return rooms.stream()
                .filter(r -> r != null && r.getArea() > 0 && r.getLight() >= 0)
                .mapToDouble(Room::getArea)
                .sum();
    }

    private static boolean hasValidCubeAndHeating(Room room) {
        return room != null
                && Double.isFinite(room.getCube()) && room.getCube() > 0
                && Float.isFinite(room.getHeating()) && room.getHeating() >= 0;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getCube() {
        return rooms.stream()
                .filter(Level::hasValidCubeAndHeating)
                .mapToDouble(Room::getCube)
                .sum();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public float getHeating() {
        return (float) rooms.stream()
                .filter(Level::hasValidCubeAndHeating)
                .mapToDouble(Room::getHeating)
                .sum();
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public double getLight() {
        return rooms.stream()
                .filter(r -> r != null && r.getArea() > 0 && r.getLight() >= 0)
                .mapToDouble(Room::getLight)
                .sum();
    }
}
