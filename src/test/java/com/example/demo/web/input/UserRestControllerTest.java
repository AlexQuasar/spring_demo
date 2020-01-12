package com.example.demo.web.input;

import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    ObjectMapper mapper;

    Logger log = Logger.getLogger(UserRestControllerTest.class.getName());
    String userController = "/userController";

    @Before
    public void setUp() throws Exception {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation));
        this.mockMvc = builder.build();
    }

    @Test
    public void addVisitTest() throws Exception {
        String addVisit = userController + "/addVisit";

        LocalDateTime day = LocalDateTime.now();
        int timeSpent = 100;

        User user = new User();
        user.setName("Petya");
        UserVisit userVisit = new UserVisit();
        userVisit.setDay(day.toLocalDate());
        userVisit.setUser(user);
        userVisit.setUrl("http://google.com");
        userVisit.setTimeSpent(timeSpent);
        userVisit.setTimeInterval(Duration.between(day, day.plusSeconds(timeSpent)));

        mockMvc.perform(post(addVisit)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userVisit)))
        .andExpect(status().isOk())
        .andDo(document(addVisit));
    }

    @Test
    public void getGroupedUserVisitsTest() throws Exception {
        String getGroupedUserVisits = userController + "/getGroupedUserVisits";

        MvcResult result = mockMvc.perform(get(getGroupedUserVisits)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UserAveragePresence> visits = mapper.readValue(content, new TypeReference<List<UserAveragePresence>>() {});

        assertNotEquals(0, visits.size());
    }
}