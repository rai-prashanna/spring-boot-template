package com.tc.execution.api;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test-case to test the liveliness probe of execution-api controller by
 * means of the http request-response.
 */
@Tag("controller")
public class EchoControllerTest {
    final EchoController echoController = new EchoController();

    @Test
    public void echoTest() {
        final String expectedContent = "test";
        final int expectedStatus = 200;
        final Response response = echoController.echo("test");
        assertEquals(response.getStatus(),expectedStatus);
        assertThat(response.getEntity().toString()).contains(expectedContent);
    }
}
