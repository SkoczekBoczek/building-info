package pl.put.poznan.buildinginfo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.put.poznan.buildinginfo.logic.Building;
import pl.put.poznan.buildinginfo.logic.BuildingRepository;
import pl.put.poznan.buildinginfo.logic.Level;
import pl.put.poznan.buildinginfo.logic.Room;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/buildings")
public class BuildingInfoController {

    private static final Logger logger = LoggerFactory.getLogger(BuildingInfoController.class);
    private final BuildingRepository buildingRepository;

    public BuildingInfoController(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @GetMapping(value = "/{buildingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Building getBuilding(@PathVariable int buildingId,
                                @RequestParam(value = "params", defaultValue = "area,cube") String[] params) {
        logger.debug("Building id: {}", buildingId);
        logger.debug("Requested params: {}", Arrays.toString(params));
        return buildingRepository.findById(buildingId)
                .orElseThrow(() -> notFound("Building", buildingId));
    }

    @GetMapping(value = "/{buildingId}/levels/{levelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Level getLevel(@PathVariable int buildingId, @PathVariable int levelId) {
        return findBuilding(buildingId).getLevels().stream()
                .filter(level -> level.getId() == levelId)
                .findFirst()
                .orElseThrow(() -> notFound("Level", levelId));
    }

    @GetMapping(value = "/{buildingId}/levels/{levelId}/rooms/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Room getRoom(@PathVariable int buildingId, @PathVariable int levelId, @PathVariable int roomId) {
        return getLevel(buildingId, levelId).getRooms().stream()
                .filter(room -> room.getId() == roomId)
                .findFirst()
                .orElseThrow(() -> notFound("Room", roomId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Building post(@RequestBody Building building,
                         @RequestParam(value = "params", defaultValue = "area,cube") String[] params) {
        logger.debug("Received building id: {}", building != null ? building.getId() : null);
        logger.debug("Requested params: {}", Arrays.toString(params));
        if (building == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Building data must be provided");
        }
        return buildingRepository.save(building);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", exception.getStatus().value());
        body.put("error", exception.getReason());
        return ResponseEntity.status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    private Building findBuilding(int buildingId) {
        return buildingRepository.findById(buildingId)
                .orElseThrow(() -> notFound("Building", buildingId));
    }

    private ResponseStatusException notFound(String type, int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, type + " " + id + " not found");
    }
}
