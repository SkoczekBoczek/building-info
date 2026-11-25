package pl.put.poznan.buildinginfo.logic;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class BuildingRepository {
    private final ConcurrentMap<Integer, Building> buildings = new ConcurrentHashMap<>();

    public Building save(Building building) {
        buildings.put(building.getId(), building);
        return building;
    }

    public Optional<Building> findById(int id) {
        return Optional.ofNullable(buildings.get(id));
    }
}
