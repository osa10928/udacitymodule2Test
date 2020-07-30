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
public class NoteCrudTest {
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

    String addNoteId = "addNotesTab";
    String editNoteId = "note-edit-btn-1";
    String noteTitle1 = "Example Title";
    String noteDescription1 = "Example Description of a note";
    String noteTitle2 = "Example Title 2";
    String noteDescription2 = "Example Description of edited note";

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
        fillOutNote(addNoteId, noteTitle1, noteDescription1);
    }

    @Test
    public void creatNote() {
        // First note is created in the beforeeach method
        WebElement firstNote = driver.findElement(By.id("note-1"));
        Assertions.assertNotNull(firstNote);
    }

    @Test
    public void editNote() {
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Get first note and save its title and description
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("note-title-1"), noteTitle1));
        WebElement firstNote = driver.findElement(By.id("note-1"));
        String title = driver.findElement(By.id("note-title-1")).getText();
        String description = driver.findElement(By.id("note-description-1")).getText();

        // Assert the title and description match orignal note title and description
        Assertions.assertEquals(noteTitle1, title);
        Assertions.assertEquals(noteDescription1, description);

        // Edit First Note
        fillOutNote(editNoteId, noteTitle2, noteDescription2);

        // First note should now have a different title and description
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("note-title-1"), noteTitle2));
        title = driver.findElement(By.id("note-title-1")).getText();
        description = driver.findElement(By.id("note-description-1")).getText();

        Assertions.assertNotNull(firstNote);
        Assertions.assertEquals(noteTitle2, title);
        Assertions.assertEquals(noteDescription2, description);
    }

    @Test()
    public void deleteNote() {
        // Note is created in the beforeeach method
        WebDriverWait wait = new WebDriverWait(driver, 20);

        WebElement firstNote = driver.findElement(By.id("note-1"));
        Assertions.assertNotNull(firstNote);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-delete-btn-1"))).click();
        openNoteTab(wait);
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.id("note-1"));
        });

    }

    private void loginUser() {
        driver.get(BASEURL + "/login");
        loginPage.login(username, password);
    }

    private void signupUser() {
        driver.get(BASEURL + "/signup");
        signupPage.signUp(firstname, lastname, username, password);
    }

    private void fillOutNote(String addOrEditNoteId, String noteTitle, String noteDescription) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        openNoteTab(wait);
        fillNote(wait, addOrEditNoteId, noteTitle, noteDescription);
        submitNote(wait);
    }

    private void openNoteTab(WebDriverWait wait) {
        driver.get(BASEURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
    }

    private void fillNote(WebDriverWait wait, String editOrAddId, String noteTitle, String noteDescription) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id(editOrAddId))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        homePage.fillNote(noteTitle, noteDescription);
    }

    private void submitNote(WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmitter"))).click();
        driver.get(BASEURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
    }
}
