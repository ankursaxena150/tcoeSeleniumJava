package com.volvo.project.components;

import com.volvo.project.enums.ExecutionModes;
import com.volvo.project.enums.SauceBrowsers;
import com.volvo.project.parameters.SauceLabsParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.volvo.project.components.TestBase.*;


/**
 * Utility class for test automation scripts.
 *
 * @author a083259
 */
public class Utils {

    private static final int SLEEP_INTERVAL = 5000;
    private static final String IE_BROWSER = "ie11";
    private static final String CHROME_BROWSER = "chrome";
    private static final String FIREFOX_BROWSER = "firefox";
    private static final String EDGE_BROWSER = "edge";

    //TEST RUN PARAMETERS
    protected static String userID = System.getenv("PROXY_USERID");
    protected static String vcnPass = System.getenv("PROXY_VCNPASS");
    protected static String username = System.getenv("SAUCE_USER");
    protected static String accessKey = System.getenv("SAUCE_KEY");
    //JENKINS PARAMS READING
//    private static String SAUCE_CONNECTION = System.getProperty("SAUCE_CONNECTION");
    //private static String SAUCE_CONNECTION = SauceLabsConnectionMode.OFF.toString();

//        private static String BROWSER_TYPE = System.getProperty("BROWSER_TYPE");
    private static String BROWSER_TYPE = "chrome";
//        private static String BROWSER_VERSION = System.getProperty("BROWSER_VERSION");
    private static String BROWSER_VERSION = "latest";
//        private static String BROWSER_PLATFORM = System.getProperty("BROWSER_PLATFORM");
    private static String BROWSER_PLATFORM = "Linux";

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    //protected static SauceLabsConnectionMode driverType;
    protected static String startTimestamp = "";
    protected static ThreadLocal<String> sessionId = new ThreadLocal<>();
    protected static String proxyAddress;
    private static String buildDate = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm a").format(Calendar.getInstance().getTime());


    public static String readEnv() {
        String env;
        try {
            if (driver.get().getCurrentUrl().contains("ipmr-qa.")) {
                env = "INTERNET QA";
            } else if (driver.get().getCurrentUrl().contains("ipmr-test")) {
                env = "INTERNET TEST";
            } else if (driver.get().getCurrentUrl().contains("ipmr-dev")) {
                env = "INTERNET DEV";
            } else {
                env = "uknownEnvironment";
            }
        } catch (NullPointerException e) {
            env = "CLOSED!";
        }
        if (System.getProperty("testedEnv") == null || System.getProperty("testedEnv").equals("uknownEnvironment")) {
            System.setProperty("testedEnv", env);
        }
        return env;
    }

    /**
     * @return Timestamp in "yyyy-MM-dd'T'HH:mm:ss.SSS" format
     */
    private static String currentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date());
    }


    /**
     * @return today date in yyyy-MM-dd format as a String
     */
    public static String getTodayDate() {
        String dateFormat = "M/dd/yyyy";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    /**
     * @return current year as a String
     */
    public static String getCurrentYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * @param maxValue
     * @return random integer value from following range <1, @param maxValue>
     */
    public static int getRandomInt(int maxValue) {
        Random generator = new Random();
        return generator.nextInt(maxValue) + 1;
    }

    public static void killDrivers() throws IOException, InterruptedException {
        if(System.getProperty("os.name").contains("Windows")) {
            if (BROWSER_TYPE.equals(IE_BROWSER)) {
                Runtime.getRuntime().exec("taskkill /F /IM IEdriverServer.exe");
                Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
            }
            if (BROWSER_TYPE.equals(CHROME_BROWSER)) {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver_v2.2.exe");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver_win_32.exe");
            }
            if (BROWSER_TYPE.equals(FIREFOX_BROWSER)) {
                Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
            }
            Thread.sleep(SLEEP_INTERVAL);
        }
        else if(System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac")) {
            if (BROWSER_TYPE.equals(CHROME_BROWSER)) {
                Runtime.getRuntime().exec("killall chromedriver");
                Runtime.getRuntime().exec("killall chrome");
            }
            if (BROWSER_TYPE.equals(FIREFOX_BROWSER)) {
                Runtime.getRuntime().exec("killall firefox");
            }
            Thread.sleep(SLEEP_INTERVAL);
        }
    }

    private static void configureFirefoxWebDriver() {
        driver.set(new FirefoxDriver());
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    private static void configureChromeWebDriver() {
        if(ExecutionModes.DOCKER.isEnabled()){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            options.addArguments("--verbose");
            options.addArguments("--log-path=/var/log/chromedriver");
            driver.set(new ChromeDriver(options));
            driver.get().manage().window().maximize();
            driver.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        }
        else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            driver.set(new ChromeDriver(options));
            driver.get().manage().window().maximize();
            driver.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        }
    }

    private static void configureIEWebDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability("nativeEvents", false);
        capabilities.setCapability("unexpectedAlertBehaviour", "accept");
        capabilities.setCapability("ignoreProtectedModeSettings", true);
        capabilities.setCapability("disable-popup-blocking", true);
        capabilities.setCapability("enablePersistentHover", true);
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        driver.set(new InternetExplorerDriver(capabilities));
        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    /**
     * Initiates the Webdriver.get()
     *
     * @return the driver.get() for a specified browser
     */
    public WebDriver chooseAndLaunchBrowser() {

        if (! ExecutionModes.SAUCELABS.isEnabled()) {
            //driverType = SauceLabsConnectionMode.OFF;
            if(BROWSER_TYPE == null) {
                BROWSER_TYPE = "chrome";
            }

            switch(BROWSER_TYPE) {
                case FIREFOX_BROWSER:
                    configureFirefoxWebDriver();
                    break;

                case CHROME_BROWSER:
                    configureChromeWebDriver();
                    break;

                case IE_BROWSER:
                    configureIEWebDriver();
                    break;
            }
        }
        if (ExecutionModes.SAUCELABS.isEnabled()) {
            //driverType = SauceLabsConnectionMode.ON;
            //create sauceLabs webdriver.get() session
            try {
                if (BROWSER_TYPE.equals(null)) {
                    BROWSER_TYPE = SauceBrowsers.CHROME_WINDOWS.getBrowserName();
                }
                if (BROWSER_VERSION.equals(null)) {
                    BROWSER_VERSION = SauceBrowsers.CHROME_WINDOWS.getBrowserVersion();
                }
                if (BROWSER_PLATFORM == null) {
                    BROWSER_PLATFORM = SauceBrowsers.CHROME_WINDOWS.getOs();
                }
            } catch (NullPointerException e) {
            }
            driver.set(createSauceLabsDriver(BROWSER_TYPE, BROWSER_VERSION, BROWSER_PLATFORM, testName.get(), categoryGroup.get()));
        }
        return driver.get();
    }

    public static void beforeConfiguration() {
        startTimestamp = currentTimeStamp();
        String pathDir = new File("").getAbsolutePath();
        /*String com4jDll = pathDir + "\\src\\test\\resources\\hpConnection\\com4j-amd64.dll"
        System.load(com4jDll);*/

        File file = new File(pathDir + File.separator + "target" + File.separator + "test-screen");
        if (!file.exists()) {
            if (file.mkdir()) {
                try {
                    FileUtils.cleanDirectory(new File(pathDir + File.separator + "target" + File.separator + "test-screen" + File.separator));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("test-screen directory is created");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        new File(pathDir + File.separator + "target" + File.separator + "TestLogReport.html").delete();
        if (System.getProperty("webdriver.ie.driver") == null) {
            System.setProperty("webdriver.ie.driver", getWebDriverBinaryPath(IE_BROWSER));
        }
        if (System.getProperty("webdriver.edge.driver") == null) {
            System.setProperty("webdriver.edge.driver", getWebDriverBinaryPath(EDGE_BROWSER));
        }
        if (System.getProperty("webdriver.chrome.driver") == null) {
            System.setProperty("webdriver.chrome.driver", getWebDriverBinaryPath(CHROME_BROWSER));
        }
        if (System.getProperty("webdriver.gecko.driver") == null) {
            System.setProperty("webdriver.gecko.driver", getWebDriverBinaryPath(FIREFOX_BROWSER));
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
        }
    }

    private static String getWebDriverBinaryPath(String browser) {
        String path = "";
        switch (browser) {
            case CHROME_BROWSER:
                path = "src" + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver";
                break;

            case FIREFOX_BROWSER:
                path = "src" + File.separator + "resources" + File.separator + "drivers" + File.separator + "geckodriver";
                break;

            case IE_BROWSER:
                path = "src" + File.separator + "resources" + File.separator + "drivers" + File.separator + "IEdriverServer";
                break;

            case EDGE_BROWSER:
                path = "src" + File.separator + "resources" + File.separator + "drivers" + File.separator + "MicrosoftWebDriver";
                break;
        }

        if(System.getProperty("os.name").contains("Windows")) {
            path = path + ".exe";
        }
        return path;
    }

    private static WebDriver createSauceLabsDriver(String browser, String version, String os, String testMethod, String tag) {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(CapabilityType.BROWSER_NAME, browser);
        caps.setCapability(CapabilityType.VERSION, version);
        caps.setCapability(CapabilityType.PLATFORM, os);
        caps.setCapability("build", SauceLabsParams.buildName + buildDate);
        caps.setCapability("tags", tag);
        caps.setCapability("parentTunnel", "VolvoGroupIT");
        caps.setCapability("tunnelIdentifier", "eu");
        caps.setCapability("name", testMethod);
        caps.setCapability("idleTimeout", 120);
        // Launch remote browser and set it as the current thread
        try {
            driver.set(new RemoteWebDriver(new URL("http://" + username + ":" + accessKey + "@ondemand.eu-central-1.saucelabs.com:80/wd/hub"), caps));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String id = ((RemoteWebDriver) getDriver()).getSessionId().toString();
        sessionId.set(id);
        return driver.get();
    }

    public static synchronized WebDriver getDriver() {
        return driver.get();
    }

}
