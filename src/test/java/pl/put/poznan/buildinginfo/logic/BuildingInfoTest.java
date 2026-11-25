package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

    @Test
    void calculatesAreaForRoomLevelAndBuilding() {
        Room firstRoom = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Room secondRoom = new Room(102, "Sala 102", 15.5, 46.5, 80.0f, 200.0);
        Level level = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom));
        Level upperLevel = new Level(11, "Piętro 1",
                Collections.singletonList(new Room(201, "Sala 201", 8.5, 25.5, 70.0f, 150.0)));
        Building building = new Building(1, "Budynek A", Arrays.asList(level, upperLevel));

        assertEquals(25.0, firstRoom.getArea());
        assertEquals(40.5, level.getArea());
        assertEquals(8.5, upperLevel.getArea());
        assertEquals(49.0, building.getArea());
    }

    @Test
    void emptyLevelsAndBuildingsHaveZeroArea() {
        assertEquals(0.0, new Level(10, "Parter", Collections.emptyList()).getArea());
        assertEquals(0.0, new Building(1, "Budynek A", Collections.emptyList()).getArea());
    }

    @Test
    void serializesBuildingHierarchyAsJson() throws Exception {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Level level = new Level(10, "Parter", Collections.singletonList(room));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        String json = new BuildingInfo(new String[0]).getInfoJSON(building);
        JsonNode result = new ObjectMapper().readTree(json);

        assertEquals(1, result.get("id").asInt());
        assertEquals("Budynek A", result.get("name").asText());
        assertEquals(10, result.get("levels").get(0).get("id").asInt());
        assertEquals(25.0, result.get("levels").get(0).get("rooms").get(0).get("area").asDouble());
        assertEquals(75.0, result.get("levels").get(0).get("rooms").get(0).get("cube").asDouble());
        assertEquals(120.5, result.get("levels").get(0).get("rooms").get(0).get("heating").asDouble());
        assertEquals(300.0, result.get("levels").get(0).get("rooms").get(0).get("light").asDouble());
    }

    @Test
    void printJSON() throws Exception {
        Building building = new Building(1, "Budynek A", Collections.emptyList());
        BuildingInfo info = new BuildingInfo(new String[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8.name()));
            info.printInfoJSON(building);
        } finally {
            System.setOut(originalOut);
        }

        assertEquals(info.getInfoJSON(building) + System.lineSeparator(),
                output.toString(StandardCharsets.UTF_8.name()));
    }
}
