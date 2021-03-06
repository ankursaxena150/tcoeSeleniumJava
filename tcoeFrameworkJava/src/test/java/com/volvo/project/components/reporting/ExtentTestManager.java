package com.volvo.project.components.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.volvo.project.components.reporting.ExtentManager;

public class ExtentTestManager {
    private static ThreadLocal<ExtentTest> extentTest;
    private static ExtentReports extent = ExtentManager.getInstance();

    public synchronized static ExtentTest getTest() {
        return extentTest.get();
    }

    public synchronized static ExtentTest createTest(String name, String description, String category) {
        ExtentTest test = extent.createTest(name, description);

        if (category != null && !category.isEmpty())
            test.assignCategory(category);

        extentTest.set(test);

        return getTest();
    }

    public synchronized static ExtentTest createTest(String name, String description) {
        return createTest(name, description, null);
    }

    public synchronized static ExtentTest createTest(String name) {
        return createTest(name, null);
    }

    public synchronized static void log(String message) {
        getTest().info(message);
    }

}