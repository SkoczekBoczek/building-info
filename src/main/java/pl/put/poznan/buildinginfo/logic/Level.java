package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                .filter(Objects::nonNull)
                .mapToDouble(Room::getArea)
                .sum();
    }
}
