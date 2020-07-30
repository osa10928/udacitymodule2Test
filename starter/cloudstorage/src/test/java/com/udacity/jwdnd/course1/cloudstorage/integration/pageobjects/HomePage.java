package com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects;

import org.mockito.internal.matchers.Find;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logoutBtn")
    private WebElement logoutBtn;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getCredentialPassword() {
        return credentialPassword;
    }

    public void logout() {
        logoutBtn.click();
    }

    public void fillNote(String noteTitle, String noteDescription) {
        fillNoteTitle(noteTitle);
        fillNoteDescription(noteDescription);
    }

    public void fillCredential(String url, String username, String password) {
        fillCredentialUrl(url);
        fillCredentialUsername(username);
        fillCredentialPassword(password);
    }

    private void fillNoteDescription(String value) {
        fillInput(noteDescription, value);
    }

    private void fillNoteTitle(String value) {
        fillInput(noteTitle, value);
    }

    private void fillCredentialUrl(String value) {
        fillInput(credentialUrl, value);
    }

    private void fillCredentialUsername(String value) {
        fillInput(credentialUsername, value);
    }

    private void fillCredentialPassword(String value) {
        fillInput(credentialPassword, value);
    }

    private void fillInput(WebElement input, String value) {
        input.clear();
        input.sendKeys(String.valueOf(value));
    }
}
