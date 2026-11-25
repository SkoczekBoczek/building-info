package pl.put.poznan.buildinginfo.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is just an example to show that the logic should be outside the REST service.
 */
public class BuildingInfo {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String[] params;

    public BuildingInfo(String[] params) {
        this.params = params;
    }

    public String getInfo(Building building) {
        // of course, normally it would calculate parameters based on the building structure
        if (building == null) {
            return "No building data provided";
        }
        String name = building.getName() != null ? building.getName() : "unnamed";
        return "Building " + building.getId() + " (" + name + "), levels: " + building.getLevels().size();
    }

    public String getInfoJSON(Building building) {
        try {
            return OBJECT_MAPPER.writeValueAsString(building);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Unable to serialize building information as JSON", e);
        }
    }

    public void printInfoJSON(Building building) {
        System.out.println(getInfoJSON(building));
    }

    public String getInfo(int locationId) {
        // placeholder example for GET requests by location id
        return "Location " + locationId + " info";
    }
}
