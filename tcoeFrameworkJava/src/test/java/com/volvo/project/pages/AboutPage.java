package com.volvo.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AboutPage extends ShiftingMenuPage{
    // ELEMENTS
    @FindBy(css = "")
    private WebElement onlyOneElementOnThisPage;

    //METHODS
    public String readTheTextFromElement(){
        return onlyOneElementOnThisPage.getText();
    }

    public AboutPage(WebDriver driver) {
        super(driver);
    }
}
