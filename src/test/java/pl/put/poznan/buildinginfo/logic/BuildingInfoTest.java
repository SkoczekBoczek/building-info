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
    void ignoresNullRoomsAndLevelsWhenCalculatingArea() {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Level level = new Level(10, "Parter", Arrays.asList(null, room));
        Building building = new Building(1, "Budynek A", Arrays.asList(null, level));

        assertEquals(25.0, level.getArea());
        assertEquals(25.0, building.getArea());
    }

    @Test
    void calculatesCubeForRoomLevelAndBuilding() {
        Room firstRoom = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Room secondRoom = new Room(102, "Sala 102", 15.5, 46.5, 80.0f, 200.0);
        Level level = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom));
        Level upperLevel = new Level(11, "Piętro 1",
                Collections.singletonList(new Room(201, "Sala 201", 8.5, 25.5, 70.0f, 150.0)));
        Building building = new Building(1, "Budynek A", Arrays.asList(level, upperLevel));

        assertEquals(75.0, firstRoom.getCube(), 1e-9);
        assertEquals(121.5, level.getCube(), 1e-9);
        assertEquals(25.5, upperLevel.getCube(), 1e-9);
        assertEquals(147.0, building.getCube(), 1e-9);
    }

    @Test
    void emptyLevelsAndBuildingsHaveZeroCube() {
        assertEquals(0.0, new Level(10, "Parter", Collections.emptyList()).getCube(), 1e-9);
        assertEquals(0.0, new Building(1, "Budynek A", Collections.emptyList()).getCube(), 1e-9);
    }

    @Test
    void ignoresNullAndNonPositiveCubeRoomsAndLevelsWhenCalculatingCube() {
        Room validRoom = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Room zeroCubeRoom = new Room(102, "Schowek", 5.0, 0.0, 10.0f, 20.0);
        Room negativeCubeRoom = new Room(103, "Błędny", 10.0, -30.0, 40.0f, 50.0);
        Room nanCubeRoom = new Room(104, "NaN", 10.0, Double.NaN, 40.0f, 50.0);
        Room infiniteCubeRoom = new Room(105, "Inf", 10.0, Double.POSITIVE_INFINITY, 40.0f, 50.0);
        Level level = new Level(10, "Parter",
                Arrays.asList(null, validRoom, zeroCubeRoom, negativeCubeRoom, nanCubeRoom, infiniteCubeRoom));
        Building building = new Building(1, "Budynek A", Arrays.asList(null, level));

        assertEquals(75.0, level.getCube(), 1e-9);
        assertEquals(75.0, building.getCube(), 1e-9);
    }

    @Test
    void calculatesLightPerAreaForRoomLevelAndBuilding() {
        Room firstRoom = new Room(101, "Sala 101", 10.0, 30.0, 50.0f, 100.0);
        Room secondRoom = new Room(102, "Sala 102", 30.0, 90.0, 120.0f, 600.0);
        Level groundFloor = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom));
        Level upperFloor = new Level(11, "Piętro 1",
                Collections.singletonList(new Room(201, "Sala 201", 10.0, 30.0, 40.0f, 50.0)));
        Building building = new Building(1, "Budynek A", Arrays.asList(groundFloor, upperFloor));

        assertEquals(10.0, firstRoom.getLightPerArea(), 1e-9);
        assertEquals(20.0, secondRoom.getLightPerArea(), 1e-9);
        assertEquals(17.5, groundFloor.getLightPerArea(), 1e-9);
        assertEquals(5.0, upperFloor.getLightPerArea(), 1e-9);
        assertEquals(15.0, building.getLightPerArea(), 1e-9);
    }

    @Test
    void returnsZeroLightPerAreaForEmptyOrNonPositiveAreaLocations() {
        Room zeroAreaRoom = new Room(101, "Schowek", 0.0, 0.0, 0.0f, 100.0);
        Room negativeAreaRoom = new Room(102, "Błędny", -5.0, 0.0, 0.0f, 50.0);
        Level emptyLevel = new Level(10, "Pusty poziom", Collections.emptyList());
        Level zeroAreaLevel = new Level(11, "Zerowy poziom", Collections.singletonList(zeroAreaRoom));
        Building emptyBuilding = new Building(1, "Pusty budynek", Collections.emptyList());
        Building zeroAreaBuilding = new Building(2, "Zerowy budynek", Collections.singletonList(zeroAreaLevel));

        assertEquals(0.0, zeroAreaRoom.getLightPerArea(), 1e-9);
        assertEquals(0.0, negativeAreaRoom.getLightPerArea(), 1e-9);
        assertEquals(0.0, emptyLevel.getLightPerArea(), 1e-9);
        assertEquals(0.0, zeroAreaLevel.getLightPerArea(), 1e-9);
        assertEquals(0.0, emptyBuilding.getLightPerArea(), 1e-9);
        assertEquals(0.0, zeroAreaBuilding.getLightPerArea(), 1e-9);
    }

    @Test
    void ignoresNullRoomsAndLevelsWhenCalculatingLightPerArea() {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Level level = new Level(10, "Parter", Arrays.asList(null, room));
        Building building = new Building(1, "Budynek A", Arrays.asList(null, level));

        assertEquals(12.0, level.getLightPerArea(), 1e-9);
        assertEquals(12.0, building.getLightPerArea(), 1e-9);
    }

    @Test
    void ignoresRoomWithNegativeLightWhenCalculatingLightPerArea() {
        Room firstRoom = new Room(101, "Sala 101", 10.0, 30.0, 50.0f, 100.0);
        Room secondRoom = new Room(102, "Sala 102", 30.0, 90.0, 120.0f, 600.0);
        Room negativeLightRoom = new Room(103, "Błędny pokój", 20.0, 60.0, 80.0f, -100.0);
        Level level = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom, negativeLightRoom));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        assertEquals(0.0, negativeLightRoom.getLightPerArea(), 1e-9);
        assertEquals(40.0, level.getArea(), 1e-9);
        assertEquals(700.0, level.getLight(), 1e-9);
        assertEquals(17.5, level.getLightPerArea(), 1e-9);
        assertEquals(17.5, building.getLightPerArea(), 1e-9);
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
        assertEquals(75.0, result.get("cube").asDouble(), 1e-9);
        assertEquals(12.0, result.get("lightPerArea").asDouble(), 1e-9);
        assertEquals(10, result.get("levels").get(0).get("id").asInt());
        assertEquals(75.0, result.get("levels").get(0).get("cube").asDouble(), 1e-9);
        assertEquals(12.0, result.get("levels").get(0).get("lightPerArea").asDouble(), 1e-9);
        assertEquals(25.0, result.get("levels").get(0).get("rooms").get(0).get("area").asDouble());
        assertEquals(75.0, result.get("levels").get(0).get("rooms").get(0).get("cube").asDouble());
        assertEquals(120.5, result.get("levels").get(0).get("rooms").get(0).get("heating").asDouble());
        assertEquals(300.0, result.get("levels").get(0).get("rooms").get(0).get("light").asDouble());
        assertEquals(12.0, result.get("levels").get(0).get("rooms").get(0).get("lightPerArea").asDouble(), 1e-9);
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
