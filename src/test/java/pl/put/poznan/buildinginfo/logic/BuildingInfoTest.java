package pl.put.poznan.buildinginfo.logic;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingInfoTest {

    @Test
    void returnsSummaryForBuilding() {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Level level = new Level(10, "Parter", Collections.singletonList(room));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        BuildingInfo info = new BuildingInfo(new String[]{"area", "cube"});
        assertEquals("Building 1 (Budynek A), levels: 1", info.getInfo(building));
    }
}
