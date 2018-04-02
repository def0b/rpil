package com.sberpr.rpil;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.core.Is.is;

import com.sberpr.rpil.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class RpilApplicationTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void createNewAccount() {
        UUID uuid = post("/api/account").getBody().as(UUID.class);
        log.info(uuid.toString());

        Assert.assertThat("creation of account gives wrong info",
                          accountRepository.existsById(uuid),
                          is(true));
    }


    @Test
    public void deleteAccount() {
        UUID uuid = post("/api/account").getBody().as(UUID.class);
        Assume.assumeThat("ensure that account is created",
                          accountRepository.existsById(uuid),
                          is(true));

        int statusCode =
            given()
                .param("id", uuid)
                .when()
                .delete("/api/account")
                .getStatusCode();
        Assert.assertThat("statusCode is not normal",
                          statusCode,
                          is(200));
        Assert.assertThat("expected absence of account",
                          accountRepository.existsById(uuid),
                          is(false));
    }
}
