package com.volvo.project.enums;

public enum SauceBrowsers {

    CHROME_WINDOWS("chrome", "61.0", "Windows 10"),
    CHROME_LINUX("chrome", "61.0", "Linux");

    private final String browserName;
    private final String browserVersion;
    private final String os;

    private SauceBrowsers(String browserName, String browserVersion, String os) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.os = os;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getOs() {
        return os;
    }
}
