package com.tc.execution.api;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

@Configuration
@Primary
public class JerseyConfig extends ResourceConfig {

    @PostConstruct
    public void init() {
        registerEndpoints();
        configureSwagger();
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
    private void registerEndpoints() {
        register(EchoController.class);
        register(NumberController.class);
        register(SeriesController.class);
        register(SipTestParametersController.class);
        register(CustomerController.class);
        register(SubscriptionController.class);
        register(NotificationController.class);
        register(OperatorConsentController.class);
    }

    public void configureSwagger() {
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setConfigId("execution-api");
        config.setTitle("execution-api");
        config.setVersion("1.0");
        config.setBasePath("/execution");
        config.setResourcePackage("com.tc.execution");
        config.setScan(true);
    }
}
