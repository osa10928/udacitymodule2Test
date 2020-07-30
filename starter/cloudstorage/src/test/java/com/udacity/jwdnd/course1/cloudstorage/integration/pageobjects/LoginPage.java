package com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id="inputUsername")
    private WebElement usernameInput;

    @FindBy(id="inputPassword")
    private WebElement passwordInput;

    @FindBy(id="login-btn")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        loginBtn.click();
    }

    private void fillUsername(String value) { fillInput(usernameInput, value); }

    private void fillPassword(String value) { fillInput(passwordInput, value); }

    private void fillInput(WebElement input, String value) {
        input.clear();
        input.sendKeys(String.valueOf(value));
    }
}
