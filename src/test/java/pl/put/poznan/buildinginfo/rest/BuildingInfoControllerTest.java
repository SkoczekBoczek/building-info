package pl.put.poznan.buildinginfo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.put.poznan.buildinginfo.app.BuildingInfoApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BuildingInfoApplication.class)
@AutoConfigureMockMvc
class BuildingInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void storesBuildingAndReturnsJsonForBuildingLevelAndRoom() throws Exception {
        String building = "{\"id\":2,\"name\":\"Budynek B\",\"levels\":["
                + "{\"id\":3,\"name\":\"Piętro 1\",\"rooms\":["
                + "{\"id\":4,\"name\":\"Biuro\",\"area\":12.5,\"cube\":30.0,"
                + "\"heating\":10.0,\"light\":40.0}]}]}";

        mockMvc.perform(post("/buildings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(building))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.area").value(12.5));

        mockMvc.perform(get("/buildings/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.levels[0].area").value(12.5));

        mockMvc.perform(get("/buildings/2/levels/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rooms[0].area").value(12.5));

        mockMvc.perform(get("/buildings/2/levels/3/rooms/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Biuro"))
                .andExpect(jsonPath("$.area").value(12.5));
    }

    @Test
    void missingBuildingReturnsJsonNotFound() throws Exception {
        mockMvc.perform(get("/buildings/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Building 999999 not found"));
    }
}
