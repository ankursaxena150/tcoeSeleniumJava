package com.volvo.project.tests.e2e;

import com.volvo.project.components.*;
import com.volvo.project.components.datatdriventesting.DataProviderArguments;
import com.volvo.project.components.datatdriventesting.ExcelDataProvider;
import com.volvo.project.components.datatdriventesting.TestDataProvider;
import com.volvo.project.components.testsystemconnections.Xray;
import com.volvo.project.pages.InternetHomePage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternetTestSuiteDataProviderExampleLoginTest extends TestBase {

    private static ThreadLocal<InternetHomePage> homePage = new ThreadLocal<>();

    @DataProviderArguments(file = "InternetLoginValues.xlsx")
    @Xray(test = "TCOETA-115", labels = "Label2")
    @Test(groups = {"smoke","regression"}, dataProvider = "getDataFromFile", dataProviderClass = TestDataProvider.class)
//    @Test(groups = {"login","regression"})
    public void successfulLoginArrayDataProviderTest(String name, String password) {
        //given:
        testNameParameter.set(name+","+password);
        //when:
        homePage.set(loginPage.get().login(name, password));
        //then:
        assertThat(homePage.get().isLoaded(name)).isTrue();
    }

    @Xray(test = "TCOE-116", labels = "Label1")
    @ExcelDataProvider(fileName = "InternetValues.xlsx", tab = "testCase2")
    @Test(groups = {"smoke","regression"}, dataProvider = "getExcelDataFromFile", dataProviderClass = TestDataProvider.class)
    public void unsuccessLogiExcelDataProviderTabTest(String name, String password, String col3, String col4) {
        //given:
        testNameParameter.set(name + ", " + password+ ", " +col3+ ", " +col4);
        //when:
        homePage.set(loginPage.get().login(col3, col4));
        assertThat(homePage.get().isLoaded(name)).isFalse();
    }
}
