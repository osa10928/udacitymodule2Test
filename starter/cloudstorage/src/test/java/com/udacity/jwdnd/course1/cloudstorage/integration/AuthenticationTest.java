package com.udacity.jwdnd.course1.cloudstorage.integration;

import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.SignupPage;
import com.udacity.jwdnd.course1.cloudstorage.integration.pageobjects.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTest {
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
        BASEURL = "http://localhost:";
    }

    @Test
    public void restrictsToLoginAndSignIn() {
        // Without logging in, navigating to home path sends you to login path
        BASEURL += port;
        driver.get(BASEURL + "/home");
        Assertions.assertEquals(driver.getCurrentUrl(), BASEURL + "/login");

        // Allow access to signup page without login
        driver.get(BASEURL + "/signup");
        Assertions.assertEquals(driver.getCurrentUrl(), BASEURL + "/signup");
    }

    @Test
    public void signupAndLogin() {
        BASEURL += port;
        signupUser();
        Assertions.assertNotNull(signupPage.getSignupSuccessMsg());

        loginUser();
        Assertions.assertEquals(driver.getCurrentUrl(), BASEURL + "/home");

        homePage.logout();
        WebDriverWait wait = new WebDriverWait(driver, 10000);
        WebElement marker = wait.until(webDriver -> webDriver.findElement(By.id("login-btn")));
        Assertions.assertEquals(driver.getCurrentUrl(), BASEURL + "/login?logout");

        driver.get(BASEURL + "/home");
        Assertions.assertEquals(driver.getCurrentUrl(), BASEURL + "/login");
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
