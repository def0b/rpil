package com.sberpr.rpil;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;

import com.google.common.collect.Iterables;
import com.sberpr.rpil.entity.AccountEntity;
import com.sberpr.rpil.repository.AccountRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class GuiTests {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();
    @Autowired
    private AccountRepository accountRepository;

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.get("http://localhost:8080/webint.html");
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    private void sleepForSeconds(long t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (InterruptedException e) {
            log.error("InterruptedException while sleeping", e);
        }
    }

    @Test
    public void addAccountTest() {
        List<UUID> oldList = accountRepository.findAll().stream()
            .map(AccountEntity::getId)
            .collect(Collectors.toList());
        driver.findElement(By.id("newAccountBtn")).click();
        sleepForSeconds(5);
        UUID newAccountUuid = UUID.fromString(driver.findElement(By.id("newAccountResult")).getText());

        errorCollector.checkThat("new uuid should not be in old list",
                                 oldList,
                                 not(hasItem(newAccountUuid)));
        errorCollector.checkThat("new uuid should be in db",
                                 accountRepository.existsById(newAccountUuid),
                                 is(true));

        accountRepository.deleteById(newAccountUuid);
    }

    @Test
    @Transactional
    public void deleteExistingAccountTest() {
        AccountEntity entity = new AccountEntity();
        entity.setId(UUID.randomUUID());
        entity = accountRepository.saveAndFlush(entity);

        driver.findElement(By.id("deleteAccUuid")).sendKeys(entity.getId().toString());
        driver.findElement(By.id("deleteAccBtn")).click();
        sleepForSeconds(5);

        errorCollector.checkThat("interface should have confirmation of deleting",
                                 Boolean.parseBoolean(driver.findElement(By.id("deleteResult")).getText()),
                                 is(true));
        errorCollector.checkThat("uuid should not be in db",
                                 accountRepository.existsById(entity.getId()),
                                 is(false));

    }

    @Test
    public void deleteNonexistingAccountTest() {
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (accountRepository.existsById(id));

        driver.findElement(By.id("deleteAccUuid")).sendKeys(id.toString());
        driver.findElement(By.id("deleteAccBtn")).click();
        sleepForSeconds(5);

        errorCollector.checkThat("interface should have warning of deleting",
                                 Boolean.parseBoolean(driver.findElement(By.id("deleteResult")).getText()),
                                 is(false));
        errorCollector.checkThat("uuid should not be in db",
                                 accountRepository.existsById(id),
                                 is(false));
    }

    @Test
    public void showAllAccountsTest() {
        driver.findElement(By.id("getAllAccBtn")).click();
        sleepForSeconds(4);
        List<String> actual = driver.findElement(By.id("allAccounts"))
            .findElements(By.tagName("tr")).stream()
            .map(WebElement::getText)
            .collect(Collectors.toList());
        List<String> expected = accountRepository.findAll().stream()
            .map(x -> x.getId().toString())
            .collect(Collectors.toList());
        errorCollector.checkThat("displayed and expected account lists differs",
                                 actual,
                                 containsInAnyOrder(Iterables.toArray(expected, String.class)));
    }

}
