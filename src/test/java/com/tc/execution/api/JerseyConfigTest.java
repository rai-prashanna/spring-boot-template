package com.tc.execution.api;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Tag("controller")
public class JerseyConfigTest {
    JerseyConfig jerseyConfig=new JerseyConfig();

    @Test
    @DisplayName("verify_Initialization_of_Jersey_configuration")
    public void verify_Initialization_of_Jersey_configuration(){
        jerseyConfig.init();

    }

}
