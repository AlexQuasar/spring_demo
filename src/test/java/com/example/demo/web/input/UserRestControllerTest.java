package com.example.demo.web.input;

import com.example.demo.UserDataGenerator;
import com.example.demo.dto.userInteraction.UserAveragePresence;
import com.example.demo.dto.xmlStructure.input.Log;
import com.example.demo.entity.User;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.UserVisitRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
// TODO: 2/4/20 это позволит использовать тестовые настройки. И ты сможешь протестировать поведение пользователя.
//  Что он пришел получил токен и смог зайти или если пришел с просроченным то не смог и тд. https://mkyong.com/spring-boot/spring-boot-profile-based-properties-and-yaml-example/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    UserVisitRepository userVisitRepository;

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
    @Transactional
    public void addVisitsTest() throws Exception {
        String addVisits = userController + "/addVisits";

        List<UserVisit> visits = new ArrayList<>();
        visits.add(generateUserVisit());

        int expectedVisitsSize = visits.size() + userVisitRepository.findAll().size();

        mockMvc.perform(post(addVisits)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(visits)))
        .andExpect(status().isOk());

        assertEquals(expectedVisitsSize, userVisitRepository.findAll().size());
    }

    @Test
    @Transactional
    public void addVisitTest() throws Exception {
        String addVisit = userController + "/addVisit";

        UserVisit userVisit = generateUserVisit();

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

    @Test
    @Transactional
    public void addLogTest() throws Exception {
        String addLog = userController + "/addLog";
        String filePath = "./src/test/java/com/example/demo/testLogXML/log.xml";
        UserDataGenerator userDataGenerator = new UserDataGenerator();
        Log log = userDataGenerator.getLogFromFile(filePath);
        int expectedVisitsSize = userVisitRepository.findAll().size() + 30;

        mockMvc.perform(post(addLog)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Arrays.asList(log))))
        .andExpect(status().isOk());

        assertEquals(expectedVisitsSize, userVisitRepository.findAll().size());
    }

    private UserVisit generateUserVisit() {
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

        return userVisit;
    }
}