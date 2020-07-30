package com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id = "inputFirstName")
    private WebElement firstNameInput;

    @FindBy(id = "inputLastName")
    private WebElement lastNameInput;

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(id="signup-btn")
    private WebElement signupBtn;

    @FindBy(id="signup-success-msg")
    private WebElement signupSuccessMsg;

    @FindBy(id="signup-error-msg")
    private WebElement signupErrorMsg;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public WebElement getSignupSuccessMsg() { return signupSuccessMsg; }
    public WebElement getSignupErrorMsg() { return signupErrorMsg; }

    public void signUp(String firstname, String lastname, String username, String password) {
        fillFirstName(firstname);
        fillLastName(lastname);
        fillUsername(username);
        fillPassword(password);
        signupBtn.click();
    }

    private void fillFirstName(String value) { fillInput(firstNameInput, value); }
    private void fillLastName(String value) { fillInput(lastNameInput, value); }
    private void fillUsername(String value) { fillInput(usernameInput, value); }
    private void fillPassword(String value) { fillInput(passwordInput, value); }

    private void fillInput(WebElement input, String value) {
        input.clear();
        input.sendKeys(String.valueOf(value));
    }
}
