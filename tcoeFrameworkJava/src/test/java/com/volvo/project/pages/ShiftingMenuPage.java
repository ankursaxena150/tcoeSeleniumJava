package com.volvo.project.pages;

import org.openqa.selenium.WebDriver;

import com.volvo.project.components.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ShiftingMenuPage extends PageObject {

    @FindBy(css = "")
    private WebElement homeTab;
    @FindBy(id = "dsada")
    private WebElement aboutTab;
    @FindBy(css = "sdsds")
    public WebElement portfolioTab;
    @FindBy(css = "")
    private WebElement hidePortfolioButton;

    public ShiftingMenuPage(WebDriver driver) {
        super(driver);
    }

    public void hideThePortolio() {
        hidePortfolioButton.click();
    }

    public AboutPage goToAboutPage() {
        aboutTab.click();
        return PageFactory.initElements(getDriver(), AboutPage.class);
    }

    public HomePage gotoHomePage() {
        homeTab.click();
        return PageFactory.initElements(getDriver(), HomePage.class);
    }
}
