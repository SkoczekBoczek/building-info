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
    void calculatesHeatingPerCubeForRoomLevelAndBuilding() {
        Room firstRoom = new Room(101, "Sala 101", 10.0, 30.0, 60.0f, 100.0);
        Room secondRoom = new Room(102, "Sala 102", 30.0, 90.0, 360.0f, 600.0);
        Level groundFloor = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom));
        Level upperFloor = new Level(11, "Piętro 1",
                Collections.singletonList(new Room(201, "Sala 201", 10.0, 30.0, 30.0f, 50.0)));
        Building building = new Building(1, "Budynek A", Arrays.asList(groundFloor, upperFloor));

        assertEquals(2.0, firstRoom.getHeatingPerCube(), 1e-9);
        assertEquals(4.0, secondRoom.getHeatingPerCube(), 1e-9);
        assertEquals(3.5, groundFloor.getHeatingPerCube(), 1e-9);
        assertEquals(1.0, upperFloor.getHeatingPerCube(), 1e-9);
        assertEquals(3.0, building.getHeatingPerCube(), 1e-9);
    }

    @Test
    void returnsZeroHeatingPerCubeForEmptyOrNonPositiveCubeLocations() {
        Room zeroCubeRoom = new Room(101, "Schowek", 5.0, 0.0, 50.0f, 100.0);
        Room negativeCubeRoom = new Room(102, "Błędny", 10.0, -30.0, 60.0f, 50.0);
        Level emptyLevel = new Level(10, "Pusty poziom", Collections.emptyList());
        Level zeroCubeLevel = new Level(11, "Zerowy poziom", Collections.singletonList(zeroCubeRoom));
        Building emptyBuilding = new Building(1, "Pusty budynek", Collections.emptyList());
        Building zeroCubeBuilding = new Building(2, "Zerowy budynek", Collections.singletonList(zeroCubeLevel));

        assertEquals(0.0, zeroCubeRoom.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, negativeCubeRoom.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, emptyLevel.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, zeroCubeLevel.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, emptyBuilding.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, zeroCubeBuilding.getHeatingPerCube(), 1e-9);
    }

    @Test
    void ignoresNullRoomsAndLevelsWhenCalculatingHeatingPerCube() {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 150.0f, 300.0);
        Level level = new Level(10, "Parter", Arrays.asList(null, room));
        Building building = new Building(1, "Budynek A", Arrays.asList(null, level));

        assertEquals(2.0, level.getHeatingPerCube(), 1e-9);
        assertEquals(2.0, building.getHeatingPerCube(), 1e-9);
    }

    @Test
    void ignoresRoomWithNegativeHeatingWhenCalculatingHeatingPerCube() {
        Room firstRoom = new Room(101, "Sala 101", 10.0, 30.0, 60.0f, 100.0);
        Room secondRoom = new Room(102, "Sala 102", 30.0, 90.0, 360.0f, 600.0);
        Room negativeHeatingRoom = new Room(103, "Błędny pokój", 20.0, 60.0, -80.0f, 100.0);
        Level level = new Level(10, "Parter", Arrays.asList(firstRoom, secondRoom, negativeHeatingRoom));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        assertEquals(0.0, negativeHeatingRoom.getHeatingPerCube(), 1e-9);
        assertEquals(120.0, level.getCube(), 1e-9);
        assertEquals(420.0f, level.getHeating(), 1e-6f);
        assertEquals(3.5, level.getHeatingPerCube(), 1e-9);
        assertEquals(3.5, building.getHeatingPerCube(), 1e-9);
    }

    @Test
    void includesUnheatedRoomsInHeatingPerCubeAverage() {
        Room heatedRoom = new Room(101, "Biuro", 10.0, 30.0, 60.0f, 100.0);
        Room unheatedRoom = new Room(102, "Magazyn", 10.0, 30.0, 0.0f, 50.0);
        Level level = new Level(10, "Parter", Arrays.asList(heatedRoom, unheatedRoom));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        assertEquals(0.0, unheatedRoom.getHeatingPerCube(), 1e-9);
        assertEquals(60.0, level.getCube(), 1e-9);
        assertEquals(60.0f, level.getHeating(), 1e-6f);
        assertEquals(1.0, level.getHeatingPerCube(), 1e-9);
        assertEquals(1.0, building.getHeatingPerCube(), 1e-9);
    }

    @Test
    void ignoresEmptyLevelsAndNonFiniteValuesWhenCalculatingHeatingPerCube() {
        Room validRoom = new Room(101, "Sala 101", 20.0, 50.0, 100.0f, 200.0);
        Room nanHeatingRoom = new Room(102, "NaN heating", 10.0, 30.0, Float.NaN, 100.0);
        Room infiniteHeatingRoom = new Room(103, "Inf heating", 10.0, 30.0, Float.POSITIVE_INFINITY, 100.0);
        Room nanCubeRoom = new Room(104, "NaN cube", 10.0, Double.NaN, 50.0f, 100.0);
        Level emptyLevel = new Level(10, "Pusty", Collections.emptyList());
        Level populatedLevel = new Level(11, "Piętro 1",
                Arrays.asList(validRoom, nanHeatingRoom, infiniteHeatingRoom, nanCubeRoom));
        Building building = new Building(1, "Budynek A", Arrays.asList(emptyLevel, populatedLevel));

        assertEquals(0.0, nanHeatingRoom.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, infiniteHeatingRoom.getHeatingPerCube(), 1e-9);
        assertEquals(0.0, nanCubeRoom.getHeatingPerCube(), 1e-9);
        assertEquals(50.0, populatedLevel.getCube(), 1e-9);
        assertEquals(100.0f, populatedLevel.getHeating(), 1e-6f);
        assertEquals(2.0, populatedLevel.getHeatingPerCube(), 1e-9);
        assertEquals(2.0, building.getHeatingPerCube(), 1e-9);
    }

    @Test
    void deserializesSerializedBuildingWithReadOnlyCalculatedFields() throws Exception {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 150.0f, 300.0);
        Level level = new Level(10, "Parter", Collections.singletonList(room));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(building);
        Building deserialized = mapper.readValue(json, Building.class);

        assertEquals(75.0, deserialized.getCube(), 1e-9);
        assertEquals(150.0f, deserialized.getHeating(), 1e-6f);
        assertEquals(2.0, deserialized.getHeatingPerCube(), 1e-9);
    }

    @Test
    void serializesBuildingHierarchyAsJson() throws Exception {
        Room room = new Room(101, "Sala 101", 25.0, 75.0, 120.5f, 300.0);
        Level level = new Level(10, "Parter", Collections.singletonList(room));
        Building building = new Building(1, "Budynek A", Collections.singletonList(level));

        String json = new BuildingInfo(new String[0]).getInfoJSON(building);
        JsonNode result = new ObjectMapper().readTree(json);

        double expectedHeatingPerCube = 120.5 / 75.0;
        assertEquals(1, result.get("id").asInt());
        assertEquals("Budynek A", result.get("name").asText());
        assertEquals(75.0, result.get("cube").asDouble(), 1e-9);
        assertEquals(120.5, result.get("heating").asDouble(), 1e-6);
        assertEquals(12.0, result.get("lightPerArea").asDouble(), 1e-9);
        assertEquals(expectedHeatingPerCube, result.get("heatingPerCube").asDouble(), 1e-9);
        assertEquals(10, result.get("levels").get(0).get("id").asInt());
        assertEquals(75.0, result.get("levels").get(0).get("cube").asDouble(), 1e-9);
        assertEquals(120.5, result.get("levels").get(0).get("heating").asDouble(), 1e-6);
        assertEquals(12.0, result.get("levels").get(0).get("lightPerArea").asDouble(), 1e-9);
        assertEquals(expectedHeatingPerCube, result.get("levels").get(0).get("heatingPerCube").asDouble(), 1e-9);
        assertEquals(25.0, result.get("levels").get(0).get("rooms").get(0).get("area").asDouble());
        assertEquals(75.0, result.get("levels").get(0).get("rooms").get(0).get("cube").asDouble());
        assertEquals(120.5, result.get("levels").get(0).get("rooms").get(0).get("heating").asDouble());
        assertEquals(300.0, result.get("levels").get(0).get("rooms").get(0).get("light").asDouble());
        assertEquals(12.0, result.get("levels").get(0).get("rooms").get(0).get("lightPerArea").asDouble(), 1e-9);
        assertEquals(expectedHeatingPerCube, result.get("levels").get(0).get("rooms").get(0).get("heatingPerCube").asDouble(), 1e-9);
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
