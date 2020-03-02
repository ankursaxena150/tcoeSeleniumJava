/*
 * Author: Pawel Piesniewski
 * ID: A049473
 */
package com.volvo.project.enums;


public enum SauceLabsConnectionMode {

    ON("ON"),
    OFF("OFF");

    private final String value;

    private SauceLabsConnectionMode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
