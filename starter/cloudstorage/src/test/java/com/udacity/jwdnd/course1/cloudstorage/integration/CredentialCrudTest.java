package com.udacity.jwdnd.course1.cloudstorage.integration;

import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialCrudTest {
    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;
    private String BASEURL;
    String firstname = "Stephen";
    String lastname = "Agwu";
    String username = "osa10928";
    String password = "l";

    String addCredentialTabId = "add-credential-btn";
    String editCredentialId = "credential-edit-btn-1";
    String credentialUrl1 = "google.com";
    String credentialUsername1 = "osa10928";
    String credentialPassword1 = "p";
    String credentialUrl2 = "twitter.com";
    String credentialUsername2 = "osa109281111";
    String credentialPassword2 = "z";

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    static void afterAll() { driver.quit(); }

    @BeforeEach
    public void beforeEach() {
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        BASEURL = "http://localhost:" + port;
        signupUser();
        loginUser();
        fillOutCredential(addCredentialTabId, credentialUrl1, credentialUsername1, credentialPassword1);
    }

    @Test
    public void createCredential() {
        // First credential is created in the beforeeach method
        WebElement firstCredential = driver.findElement(By.id("credential-1"));
        Assertions.assertNotNull(firstCredential);

        // Compared encrypted password, entered password, and password displayed during editing
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("credential-url-1"), credentialUrl1));
        String encryptedPassword = driver.findElement(By.id("credential-password-1")).getText();
        wait.until(ExpectedConditions.elementToBeClickable(By.id(editCredentialId))).click();
        String password1 = driver.findElement(By.id("credential-password")).getAttribute("value");

        // Encrypted Password and Entered password are not equal
        Assertions.assertNotEquals(credentialPassword1, encryptedPassword);
        // Entered Password and Password shown during editing are equal
        Assertions.assertEquals(credentialPassword1, password1);
    }

    @Test
    public void editCredentials() {
        // First credential is created in the beforeeach method
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("credential-url-1"), credentialUrl1));
        String url = driver.findElement(By.id("credential-url-1")).getText();
        String username = driver.findElement(By.id("credential-username-1")).getText();
        String password = driver.findElement(By.id("credential-password-1")).getText();

        // Assert url and username match entered url and username
        Assertions.assertEquals(credentialUrl1, url);
        Assertions.assertEquals(credentialUsername1, username);
        // Assert password entered does not match encrypted password
        Assertions.assertNotEquals(credentialPassword1, password);

        fillOutCredential(editCredentialId, credentialUrl2, credentialUsername2, credentialPassword2);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("credential-url-1"), credentialUrl2));
        url = driver.findElement(By.id("credential-url-1")).getText();
        username = driver.findElement(By.id("credential-username-1")).getText();
        password = driver.findElement(By.id("credential-password-1")).getText();

        // Assert that url and username match second username and second url
        Assertions.assertEquals(credentialUrl2, url);
        Assertions.assertEquals(credentialUsername2, username);
        // Assert that encrypted password does not equal second entered password
        Assertions.assertNotEquals(credentialPassword2, password);

        wait.until(ExpectedConditions.elementToBeClickable(By.id(editCredentialId))).click();
        password = driver.findElement(By.id("credential-password")).getAttribute("value");

        // Assert that password shown when editing equals second entered password
        Assertions.assertEquals(credentialPassword2, password);
    }

    @Test
    public void deleteCredential() {
        // Note is created in the beforeeach method
        WebDriverWait wait = new WebDriverWait(driver, 20);

        WebElement firstCredential = driver.findElement(By.id("credential-1"));
        Assertions.assertNotNull(firstCredential);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-delete-btn-1"))).click();
        openCredentialTab(wait);
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.id("credential-1"));
        });
    }

    private void fillOutCredential(String editOrAddId, String url, String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        openCredentialTab(wait);
        fillCredential(url, username, password, editOrAddId, wait);
        submitCredential(wait);
    }

    private void openCredentialTab(WebDriverWait wait) {
        driver.get(BASEURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credential-tab"))).click();
    }

    private void fillCredential(String url, String username, String password, String editOrAddId, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id(editOrAddId))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
        homePage.fillCredential(url, username, password);
    }

    private void submitCredential(WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmitter"))).click();
        driver.get(BASEURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credential-tab"))).click();
    }

    private void loginUser() {
        driver.get(BASEURL + "/login");
        loginPage.login(username, password);
    }

    private void signupUser() {
        driver.get(BASEURL + "/signup");
        signupPage.signUp(firstname, lastname, username, password);
    }
}
