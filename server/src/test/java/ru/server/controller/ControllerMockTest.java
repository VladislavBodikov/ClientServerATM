package ru.server.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.springframework.test.web.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import static org.springframework.test.web.servlet.MockMvc.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.*;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import ru.server.Application;
import ru.server.controller.HostRestController;
import ru.server.entity.User;

@SpringBootTest(classes = {Application.class,HostRestController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@WebMvcTest(HostRestController.class)
//@AutoConfigureRestDocs
//@ContextConfiguration("ru/server/Application.java")
public class ControllerMockTest {
//    @Autowired
//    private MockMvc mockMvc;

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getAccountsTest() throws Exception {
        final String baseURI = "http://localhost:" + port;
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        ResponseEntity<String> body = restTemplate.getForEntity(baseURI + "/host/test", String.class);
//        assertEquals("completed test", body.getBody());

        assertEquals("completed test",restTemplate.getForEntity(baseURI + "/host/test",String.class).getBody());





        //mockMvc.perform(get("/host/test")).andExpect(status().isOk()).andExpect(content().string("completed test"));
        //User user = getBaseTestUser();
        //mockMvc.perform(post("/host/create/user",user)).andExpect(status().isOk()).andExpect(content().string("USER SAVED: " + user));
    }
    private User getBaseTestUser(){
        User user = new User();
        user.setFirstName("Vlad");
        user.setLastName("Bodik");

        return user;
    }
}
