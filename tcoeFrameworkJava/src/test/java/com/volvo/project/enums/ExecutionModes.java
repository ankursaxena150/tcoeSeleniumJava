package com.volvo.project.enums;

public enum ExecutionModes {

    SAUCELABS(System.getProperty("SauceExecution", System.getenv("SAUCE_EXECUTION"))),
    DOCKER(System.getProperty("DockerExecution", System.getenv("DOCKER_EXECUTION")));
//    SAUCELABS("true"),
//    DOCKER("true");

    private final String value;

    private ExecutionModes(String value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(value);
    }

}
