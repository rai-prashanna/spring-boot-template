package com.tc.execution.api;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Controller
@Path("/v1/echo")
public class EchoController {
    @GET
    @Path("/{text}")
    public Response echo(@PathParam("text") String text) {
        return Response.ok(text).build();
    }
}
