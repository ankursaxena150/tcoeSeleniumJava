/*
 * Author: Pawel Piesniewski
 * ID: A049473
 */
package com.volvo.project.enums;

public enum ManagerName {

    GimliA1("GIMLI_A1"), 
    EowynB3("EOWYN_B3");
    
    private final String value;

    private ManagerName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}