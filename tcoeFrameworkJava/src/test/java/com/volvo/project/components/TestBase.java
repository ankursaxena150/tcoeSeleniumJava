/*
 * Author: Pawel Piesniewski
 * ID: A049473
 */
package com.volvo.project.components;

import DDDD.ITSTest;
import com.applitools.eyes.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.jayway.restassured.RestAssured;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.volvo.project.components.database.ConnectionToDB;
import com.volvo.project.components.datatdriventesting.ExcelLibrary;
import com.volvo.project.components.networking.ProxyConfiguration;
import com.volvo.project.components.networking.ProxyUtil;
import com.volvo.project.components.openshift.ConnectionToOpenshift;
import com.volvo.project.components.reporting.TestLog;
import com.volvo.project.components.security.PingFederateAccess;
import com.volvo.project.components.reporting.ExtentManager;
import com.volvo.project.components.testsystemconnections.HpConnection;
import com.volvo.project.enums.*;
import com.volvo.project.pages.InternetLoginPage;
import com.volvo.project.parameters.InternetParameters;
import com.volvo.project.parameters.SauceLabsParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.net.Proxy;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.volvo.project.components.PageObject.INTERNET_HOST;

public class TestBase extends Utils {

    protected Eyes eyes = new Eyes(); //visual testing demo
    private EyesMode eyesMode = EyesMode.OFF; //visual testing demo
    private HpQcConnectionMode hpIntegration = HpQcConnectionMode.OFF;//restriction: stable only when run via java 32bits
    String[][] testCaseIDforHpQc = new String[500][2];
    private String pathFile = "";
    public static TestLog logger = new TestLog();
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> parentTestThreadLocal = new ThreadLocal<>();
    private HashMap<String, ExtentTest> extentMap = new HashMap<>();
    public static String accessToken;
    protected static ThreadLocal<String> categoryGroup = new ThreadLocal<>();
    private String resolutionSize = "";
    protected static ThreadLocal<ExtentTest> child = new ThreadLocal<>();
    protected static ThreadLocal<InternetLoginPage> loginPage = new ThreadLocal<>();
    protected ThreadLocal<String> testNameParameter = new ThreadLocal<>();
    protected static ThreadLocal<String> testName = new ThreadLocal<>();
    private static ConnectionToDB connectionToDb;
    private ConnectionToOpenshift connectionToOpenshift;
    private String rest = "rest";
    protected TimeUnit testDuration;


    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() throws MalformedURLException {
        Utils.beforeConfiguration();
        setupRestAssuredConf();
        connectToOpenshift();
        //connectToDatabase();
        getAccessToken();
        configureProxy();
    }

    private void setupRestAssuredConf() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = "https://" + INTERNET_HOST + InternetParameters.BASE_API_URL;
    }

    private void connectToOpenshift() {
        connectionToOpenshift = new ConnectionToOpenshift(InternetParameters.DEFAULT_OPENSHIFT_BAT_NAME, InternetParameters.DEFAULT_OPENSHIFT_BAT_PATH);
        connectionToOpenshift.connect();
    }

    private void connectToDatabase() {
        try {
            connectionToDb = new ConnectionToDB(DataBaseSelection.DataBaseNumber1);
            connectionToDb.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAccessToken() {
        PingFederateAccess pingFederateAccess = new PingFederateAccess();
        try {
            accessToken = pingFederateAccess.loginToAthos(PageObject.TEAMMEMBER_ROLE_USERNAME, PageObject.TEAMMEMBER_ROLE_PASSWORD, PageObject.ENDCODED_CLIENT_SECRET);

        } catch (JSONException ignored) {
        } catch (UnirestException ignored) {
        }
    }

    private void configureProxy() {
        String PAC_URL = "http://proxyconf.srv.volvo.com/";
        URL url = null;
        try {
            url = new URL(PAC_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Proxy proxy = null;
        try {
            proxy = ProxyUtil.getProxy(url, new URL("http://ondemand.eu-central-1.saucelabs.com"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String fullProxyAddress;
        if (proxy != null) {
            fullProxyAddress = proxy.address().toString();
            proxyAddress = StringUtils.substringBefore(fullProxyAddress, "/");
            logger.info("Resolved proxy address: " + proxyAddress);
            ProxyConfiguration proxyConfiguration = new ProxyConfiguration(proxyAddress, "8080", userID, vcnPass);
            System.getProperties().put("http.proxyHost", proxyConfiguration.getHost());
            System.getProperties().put("http.proxyPort", proxyConfiguration.getPort());
            System.getProperties().put("https.proxyHost", proxyConfiguration.getHost());
            System.getProperties().put("https.proxyPort", proxyConfiguration.getPort());

            final String authUser = proxyConfiguration.getUser();
            final String authPassword = proxyConfiguration.getPassword();
            Authenticator.setDefault(
                    new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    authUser, authPassword.toCharArray());
                        }
                    }
            );

            System.getProperties().put("http.proxyUser", proxyConfiguration.getUser());
            System.getProperties().put("http.proxyPassword", proxyConfiguration.getPassword());

        }
    }


    @BeforeClass(alwaysRun = true)
    public void logStartTestExecutionBeforeClass() {
        logger.logStartTestClassExecution(getClass().getSimpleName());
        extent = ExtentManager.getInstance();
        parentTestThreadLocal.set(extent.createTest(getClass().getSimpleName()));
        extentMap.put(getClass().getSimpleName(), parentTestThreadLocal.get());
        if (hpIntegration == HpQcConnectionMode.ON) {
            setTestCaseIDFromExcel();
        }
        if (eyesMode == EyesMode.ON) {
            eyes.setProxy(new ProxySettings("http://" + proxyAddress + ":8080"));
            // This is your api key, make sure you use it in all your tests.
            eyes.setApiKey("1xnV1010103H7zX5JId51VXt8JiXYYVup1p6wYjaLv0j5mk110");
            eyes.setForceFullPageScreenshot(true);
            eyes.setStitchMode(StitchMode.CSS); //if your page has a ribbon/menu which
            eyes.setHideScrollbars(true); //to hide scrollbar during taking the screenshot
            eyes.setMatchLevel(MatchLevel.LAYOUT2); //for dynamic pages
            eyes.setBaselineEnvName("TCoEVisualTests");
        }
    }


    @BeforeMethod(alwaysRun = true)
    public void logStartTestExecutionBeforeMethod(Method testMethod) {
        logger.logStartTestExecution(testMethod.getName());
        setTestCategory(testMethod);
        testName.set(getClass().getSimpleName() + "." + testMethod.getName());
        if (!categoryGroup.get().equals(rest)) {
            loginPage.set(PageFactory.initElements(chooseAndLaunchBrowser(), InternetLoginPage.class));
            //homePage = loginPage.get().login();
//        if (resolutionSize.equals("")) {
//            resolutionSize = driver.get().manage().window().getSize().toString();
//        }
            if (eyesMode == EyesMode.ON) {
                int width = resolutionSize.indexOf("{");
                int height = resolutionSize.indexOf(",");
                eyes.open(driver.get(), "TCoE example", testMethod.getName(), new RectangleSize(width, height));
            }
        }

    }

    private void setTestCategory(Method testMethod) {
        Test t = testMethod.getAnnotation(Test.class);
        try {
            categoryGroup.set(t.groups()[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            categoryGroup.set("uknownCategory");
        }
    }


    @AfterMethod(alwaysRun = true)
    public void logEndTestExecutionAfterMethod(ITestResult result, Method testMethod) throws IOException {
        child.set(extentMap.get(getClass().getSimpleName()).createNode(testMethod.getName() + dataProviderString()));
        child.get().assignCategory(categoryGroup.get());
        readTestStatusAndLogResults(result, testMethod);

        if (hpIntegration == HpQcConnectionMode.ON) {
            //HP QC connection
            initUpdateTestResultsinHPQC();
            updateTestResultsinHPQC(testMethod, result, pathFile);
        }
        if (eyesMode == EyesMode.ON) {
            try {
                // End visual testing. Validate visual correctness.
                eyes.close();
            } finally {
                // Abort test in case of an unexpected error.
                eyes.abortIfNotClosed();
            }
        }
        if (!categoryGroup.get().equals(rest))
            driver.get().quit();
    }

    private void readTestStatusAndLogResults(ITestResult result, Method testMethod) throws IOException, WebDriverException {
        if (result.getStatus() == ITestResult.FAILURE) {
            new File(File.separator + "target" + File.separator + "test-screenshots" + File.separator).mkdirs();
            if (eyesMode == EyesMode.OFF) {
                if (!categoryGroup.get().equals(rest)) {
                    File screenshot = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
                    String pathDir = new File("").getAbsolutePath();
                    pathFile = pathDir + File.separator + "target" + File.separator + "test-screen"+ File.separator + result.getMethod().getMethodName() + testNameParameter.get() + "-screenshot.png";
                    File targetFile = new File(pathFile);
                    FileUtils.copyFile(screenshot, targetFile);
                    child.get().addScreenCaptureFromPath(pathFile);
                }

            }
            child.get().log(Status.FAIL, "Test || " + testMethod.getName() + dataProviderString() + "|| FAILED with message: " + result.getThrowable().getMessage());
            //child.log(LogStatus.INFO, "Test was performed by admin: " + adminId);
            if (!categoryGroup.get().equals(rest)) {
                child.get().log(Status.INFO, "Test environment: " + readEnv());
                if (System.getProperty("browserInfo") == null) {
                    Capabilities cap = ((RemoteWebDriver) driver.get()).getCapabilities();
                    System.setProperty("browserInfo", cap.getBrowserName() + " ver." + cap.getVersion());
                    System.setProperty("browserPlatform", cap.getPlatform().toString());
                }
                try {
                    child.get().log(Status.INFO, "URL in the End of Test: " + driver.get().getCurrentUrl());
                } catch (UnsupportedCommandException ignored) {
                }
            }
        }
        if (result.getStatus() == ITestResult.SUCCESS) {
            child.get().log(Status.PASS, "Test PASSED: " + testMethod.getName());
        }
        if (result.getStatus() == ITestResult.SKIP) {
            child.get().log(Status.SKIP, "Test SKIPPED");
        }

        logger.logEndTestExecution(testMethod.getName() + dataProviderString(), result.getStatus());
        if (ExecutionModes.SAUCELABS.isEnabled()) {
            logger.info("Starting publishToSauceLabs");
            Boolean passed = result.getStatus() == ITestResult.SUCCESS;
            try {
                // Logs the result to Sauce Labs
                ((JavascriptExecutor) driver.get()).executeScript("sauce:job-result=" + (passed ? "passed" : "failed"));
                ((JavascriptExecutor) driver.get()).executeScript("sauce:job-name=" + getClass().getSimpleName() + "." + testMethod.getName() + dataProviderString());
            } catch (NoClassDefFoundError e) {
            }
//            if (sessionId != null) {
//                Map<String, Object> updates = new HashMap<String, Object>();
//                updates.put("build", SauceLabsParams.buildName + " here is name of the AppVersion");
//                updates.put("tags", result);
//                getSauceREST().updateJobInfo(sessionId.toString(), updates);
//            }
            logger.info("Ending publishToSauceLabs");
        }
    }

    public static SauceREST getSauceREST() {

        return new SauceREST(username, accessKey, DataCenter.EU);

    }


    @AfterClass(alwaysRun = true)
    public void logEndTestExecutionAfterClass() {
        logger.logEndTestClassExecution(getClass().getSimpleName());
    }

    @AfterSuite(alwaysRun = true)
    public void logEndTestExecutionAfterSuite() throws IOException, InterruptedException {
        addInformationToExtentReport();
        extent.flush();
        if (! ExecutionModes.SAUCELABS.isEnabled())
            Utils.killDrivers();
    }

    private void addInformationToExtentReport() throws NullPointerException {
        //EmailReporter emailReporter = new EmailReporter(emailReportFile); if you want to send report by Email
        extent.setSystemInfo("APP Version", System.getProperty("ActualVersion"));
        extent.setSystemInfo("Test Environment: ", System.getProperty("testedEnv"));
        extent.setSystemInfo("Browser: ", System.getProperty("browserInfo"));
        extent.setSystemInfo("Browser Resolution: ", resolutionSize);
        extent.setSystemInfo("Java Version : ", System.getProperty("java.version"));
        if (! ExecutionModes.SAUCELABS.isEnabled()) {
            extent.setSystemInfo("OS : ", System.getProperty("os.name"));
            extent.setSystemInfo("OS Architecture : ", System.getProperty("os.arch"));
            extent.setSystemInfo("User Name : ", System.getProperty("user.name"));
            extent.setSystemInfo("Machine Name : ", System.getProperty("machine.name"));
            extent.setSystemInfo("IP Address : ", System.getProperty("machine.address"));
            //extent.attachReporter(htmlReporter, emailReporter);
            extent.setSystemInfo("Execution Machine: ", getMachineName());
        }
        if (ExecutionModes.SAUCELABS.isEnabled()) {
            extent.setSystemInfo("OS on Sauce: ", System.getProperty("browserPlatform"));
            extent.setSystemInfo("Execution Cloud name: ", "SauceLabs");
        }
    }

    private String getMachineName() {
//    import java.net.InetAddress;
//import java.net.UnknownHostException;
        String hostname = "Unknown Machine";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            logger.info("Hostname can not be resolved");
        }
        return hostname;
    }

    private String dataProviderString() {
        if (testNameParameter.get() != null) {
            return " (" + testNameParameter.get() + ")";
        }
        return "";
    }

    private void initUpdateTestResultsinHPQC() {
        HpConnection hpConnection = new HpConnection();
        hpConnection.connectionToQC();
        hpConnection.addFoldername(readEnv());
    }

    private void updateTestResultsinHPQC(Method testMethod, ITestResult result, String pathFile) {
        String testCaseID;
        //ITestSet testSet = null;
        HpConnection hpConnection = new HpConnection();
        hpConnection.addTestSet(categoryGroup.get());
        String testName = testMethod.getName();
        testCaseID = findTestCaseID(testName);
        String hpTestResult;
        if (result.getStatus() == ITestResult.FAILURE) {
            hpTestResult = "Failed";
        } else if (result.getStatus() == ITestResult.SKIP) {
            hpTestResult = "N/A";
        } else {
            hpTestResult = "Passed";
        }
        if (testCaseID != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String stackMessage = result.getThrowable().getMessage();
                ITSTest testCase = hpConnection.addTestCaseToTestSet(testCaseID, hpTestResult, testDuration);
                hpConnection.attachFailureMessageToTestCase(testCase, stackMessage);
                hpConnection.attachScreenShotToTestCase(testCase, pathFile);
            } else {
                hpConnection.addTestCaseToTestSet(testCaseID, hpTestResult, testDuration);
            }
        }
//        else {
//            ITSTest testCase = hpConnection.addTestCaseToTestSet(testCaseIDforHpQc, hpTestResult, testDuration);
//            hpConnection.addAlertForTest(testCase, Integer.parseInt(testCaseIDforHpQc));
//        }
    }

    private void setTestCaseIDFromExcel() {
        int i = 1;
        String excelHpConnection = "/hpConnection/InternetTestMethodReferingTestCaseID.xlsx";
        ExcelLibrary excelread = new ExcelLibrary();
        while (excelread.readFromExcel(i, 0, excelHpConnection) != null && !excelread.readFromExcel(i, 0, excelHpConnection).equals("")) {
            String a1 = excelread.readFromExcel(i, 0, excelHpConnection);
            String a2 = excelread.readFromExcel(i, 1, excelHpConnection);
            testCaseIDforHpQc[i][0] = a1;
            testCaseIDforHpQc[i][1] = a2;
            i++;

        }
    }

    private String findTestCaseID(String testCaseName) {

        for (int i = 0; i <= testCaseIDforHpQc.length - 1; i++) {
            String a1 = testCaseIDforHpQc[i][0];
            try {
                if (a1.equals(testCaseName)) {
                    return testCaseIDforHpQc[i][1];
                }
            } catch (NullPointerException ignored) {
            }
        }
        return null;
    }
}
