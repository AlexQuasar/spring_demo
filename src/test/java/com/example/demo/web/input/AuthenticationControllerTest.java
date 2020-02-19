package com.example.demo.web.input;

import com.example.demo.dto.mailInteraction.DataMail;
import com.example.demo.dto.tokenInteraction.TokenGenerator;
import com.example.demo.entity.Mail;
import com.example.demo.entity.UserVisit;
import com.example.demo.repository.MailRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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

import java.time.Instant;
import java.util.Date;
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

    @Autowired
    TokenGenerator tokenGenerator;

    Logger log = Logger.getLogger(AuthenticationControllerTest.class.getName());
    String authentication = "/authentication";

    final String testMail = "mail_1@google.com";

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
    }

    @Test
    @Transactional
    public void authorization() throws Exception {
        String authorization = this.authentication + "/authorization";

        Mail mail = this.mailRepository.findByLogin(testMail);
        assertNotNull(mail);

        this.mockMvc.perform(get(authorization)
                .param("login", mail.getLogin())
                .param("password", mail.getPassword()))
        .andExpect(status().isOk());

        String token = tokenGenerator.generateToken(mail.getLogin()); // TODO: 2/15/20 тут генерю новый токен, а надо получить уже существующий. Как это сделать?
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenGenerator.getSecurityKey()).parseClaimsJws(token);
        Claims body = claimsJws.getBody();

        Date expiration = body.getExpiration();
        assertTrue(expiration.after(new Date(Instant.now().toEpochMilli())));
    }

    @Test
    @Transactional
    public void getTodayVisits() throws Exception {
        String getTodayVisits = this.authentication + "/getTodayVisits";

        String token = tokenGenerator.generateToken(testMail);

        getTodayVisits += "/token=" + token;

        // TODO: 2/16/20 тут столкнулся с проблемой, что у меня в тестовом классе (тут) создается объект класса TokenGenerator
        //  и в сервисе AuthenticationService тоже создается свой объект класса TokenGenerator.
        //  Получается тут сыпется ошибка из-за того что у них поле Key securityKey разные, а точнее в сервисе оно даже не определено (null)
        //  как решить эту проблему или я не туда рою? нужна помощь
        //  хотя в спринге по умолчанию синглтон. Тогда не знаю в чем причина)

        // TODO: 2/19/20 просто создай вечный токен и помести в константу строковую и пользуйся им. Тебе не нужно генерировать его каджый раз. Просто поставь expiration через 100 лет.
        MvcResult result = this.mockMvc.perform(get(getTodayVisits)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<UserVisit> visits = mapper.readValue(content, new TypeReference<List<UserVisit>>() {});

        assertEquals(0, visits.size());
    }
}