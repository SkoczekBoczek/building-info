package pl.put.poznan.buildinginfo.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.buildinginfo.logic.Building;
import pl.put.poznan.buildinginfo.logic.BuildingInfo;

import java.util.Arrays;

@RestController
@RequestMapping("/buildings")
public class BuildingInfoController {

    private static final Logger logger = LoggerFactory.getLogger(BuildingInfoController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public String get(@PathVariable int id,
                      @RequestParam(value = "params", defaultValue = "area,cube") String[] params) {

        // log the parameters
        logger.debug("Location id: {}", id);
        logger.debug("Requested params: {}", Arrays.toString(params));

        // perform the calculation, you should run your logic here, below is just an example
        BuildingInfo buildingInfo = new BuildingInfo(params);
        return buildingInfo.getInfo(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public String post(@RequestBody Building building,
                       @RequestParam(value = "params", defaultValue = "area,cube") String[] params) {

        // log the parameters
        logger.debug("Received building id: {}", building != null ? building.getId() : null);
        logger.debug("Requested params: {}", Arrays.toString(params));

        // perform the calculation, you should run your logic here, below is just an example
        BuildingInfo buildingInfo = new BuildingInfo(params);
        return buildingInfo.getInfo(building);
    }
}
