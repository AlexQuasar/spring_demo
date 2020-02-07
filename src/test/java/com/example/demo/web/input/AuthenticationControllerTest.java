package com.example.demo.web.input;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.repository.MailRepository;
import com.example.demo.services.AuthenticationService;
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
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    MailRepository mailRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ObjectMapper mapper;

    Logger log = Logger.getLogger(AuthenticationControllerTest.class.getName());
    String authentication = "/authentication";

    @Before
    public void setUp() throws Exception {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation));
        this.mockMvc = builder.build();
    }

    @Test
    @Transactional
    public void registration() throws Exception {
        String registration = this.authentication + "/registration";

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345");

        int expectedMailSize = this.mailRepository.findAll().size() + 1;

        // TODO: 2/7/20 можно в рамках одного тест кейса сделать несколько запросов. На регистрацию и после авторизацию.
        //  Валидные и не валидные кейсы в том числе с просрочкой.
        this.mockMvc.perform(post(registration)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dataMail)))
        .andExpect(status().isOk());

        assertEquals(expectedMailSize, this.mailRepository.findAll().size());
    }

    @Test
    @Transactional
    public void authorization() throws Exception {
        String authorization = this.authentication + "/authorization";

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345");
        assertTrue(this.authenticationService.registration(dataMail));

        authorization += "?login=" + dataMail.getLogin() + "&password=" + dataMail.getPassword();

        this.mockMvc.perform(get(authorization))
        .andExpect(status().isOk());

        assertNotNull(this.mailRepository.findByLogin(dataMail.getLogin()));
    }

    @Test
    @Transactional
    public void getPassword() throws Exception {
        String getPassword = this.authentication + "/getPassword";

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345");
        assertTrue(this.authenticationService.registration(dataMail));
        assertTrue(this.authenticationService.authorization(dataMail.getLogin(), dataMail.getPassword()));

        // TODO: 2/6/20 как тут можно сгенерить токен, чтобы по нему нашлась почта, либо как его достать?
    }
}