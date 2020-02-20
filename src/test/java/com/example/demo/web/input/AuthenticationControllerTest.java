package com.example.demo.web.input;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.TokenGenerator;
import com.example.demo.entity.Mail;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.MailRepository;
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

import java.util.List;
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
    ObjectMapper mapper;

    Logger log = Logger.getLogger(AuthenticationControllerTest.class.getName());
    String authentication = "/authentication";

    final String testMail = "mail_1@google.com";
    final long hundredYearsInSeconds = 3155673600L;
    final String tokenHundredYears = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJtYWlsXzFAZ29vZ2xlLmNvbSIsImV4cCI6NDczNzg5MTQwNH0.3eIKjn783JMyoAROR5yjXFLJuWM3fRfVrjVTIr73REalQ1ZSfbzdjyPBN5dnBxE1qeT-vc0FJEWEap4Fvx0VMA";

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

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345", "user_test");

        int expectedMailSize = this.mailRepository.findAll().size() + 1;

        // TODO: 2/7/20 можно в рамках одного тест кейса сделать несколько запросов. На регистрацию и после авторизацию.
        //  Валидные и не валидные кейсы в том числе с просрочкой.
        this.mockMvc.perform(post(registration)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dataMail)))
        .andExpect(status().isOk());

        assertEquals(expectedMailSize, this.mailRepository.findAll().size());
        assertNotNull(this.mailRepository.findByLogin(dataMail.getLogin()));
    }

    @Test
    @Transactional
    public void authorization() throws Exception {
        String authorization = this.authentication + "/authorization";

        Mail mail = this.mailRepository.findByLogin(this.testMail);
        assertNotNull(mail);

        this.mockMvc.perform(get(authorization)
                .param("login", mail.getLogin())
                .param("password", mail.getPassword()))
        .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getTodayVisits() throws Exception {
        String getTodayVisits = this.authentication + "/getTodayVisits";

        TokenGenerator tokenGenerator = new TokenGenerator(this.hundredYearsInSeconds);
        String token = tokenGenerator.generateToken(this.testMail);

        getTodayVisits += "/token=" + tokenHundredYears;

        // TODO: 2/16/20 тут столкнулся с проблемой, что у меня в тестовом классе (тут) создается объект класса TokenGenerator
        //  и в сервисе AuthenticationService тоже создается свой объект класса TokenGenerator.
        //  Получается тут сыпется ошибка из-за того что у них поле Key securityKey разные, а точнее в сервисе оно даже не определено (null)
        //  как решить эту проблему или я не туда рою? нужна помощь
        //  хотя в спринге по умолчанию синглтон. Тогда не знаю в чем причина)

        // TODO: 2/19/20 просто создай вечный токен и помести в константу строковую и пользуйся им. Тебе не нужно генерировать его каджый раз. Просто поставь expiration через 100 лет.

        // TODO: 2/20/20 создаю, но сервис его не принимает почему-то, в гугле понятного мне ответа не нашел
        MvcResult result = this.mockMvc.perform(get(getTodayVisits)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UserVisit> visits = this.mapper.readValue(content, new TypeReference<List<UserVisit>>() {});

        assertEquals(0, visits.size());
    }

    @Test
    @Transactional
    public void allStepsTest() throws Exception {
        String registration = this.authentication + "/registration";
        String authorization = this.authentication + "/authorization";
        String getTodayVisits = this.authentication + "/getTodayVisits";

        TokenGenerator tokenGenerator = new TokenGenerator(300L);
        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345", "user_test");

        this.mockMvc.perform(post(registration)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dataMail)))
        .andExpect(status().isOk());

        assertNotNull(this.mailRepository.findByLogin(dataMail.getLogin()));

        Mail mail = this.mailRepository.findByLogin(dataMail.getLogin());
        assertNotNull(mail);

        this.mockMvc.perform(get(authorization)
                .param("login", mail.getLogin())
                .param("password", mail.getPassword()))
        .andExpect(status().isOk());

        String token = tokenGenerator.getExistToken(mail.getLogin());
        getTodayVisits += "/token=" + token;

        MvcResult result = this.mockMvc.perform(get(getTodayVisits)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UserVisit> visits = this.mapper.readValue(content, new TypeReference<List<UserVisit>>() {});

        assertEquals(0, visits.size());
    }
}