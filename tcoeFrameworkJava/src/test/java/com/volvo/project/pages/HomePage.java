package com.volvo.project.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends ShiftingMenuPage {
    //Elements
    @FindBy(id="")
    private WebElement headerOfThePage;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String readTheHomeHeader(){
        return headerOfThePage.getText();
    }
}
