package com.tc.execution.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * An integration test of EchoController class by
 * means of the RestTemplate.
 */
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Tag("controller")
public class EchoControllerITest {
    @LocalServerPort
    private int randomServerPort;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private EchoController echoController;

    @Test
    void contextLoads() {
    }

    @Test
    public void should_return_Ok_status() {
        String url = "http://localhost:"+randomServerPort+"/v1/echo/test";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response= restTemplate.getForEntity(url, String.class);
        assertEquals("test",response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
