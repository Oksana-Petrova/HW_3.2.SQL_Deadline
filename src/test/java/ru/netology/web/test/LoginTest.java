package ru.netology.web.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.SQLHelper.cleanDatabase;

public class LoginTest {

    @AfterAll
    static void teardown() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Should login with valid login and password")
    void shouldLoginSuccessful() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error message, if user is not exist in base")
    void shouldGetErrorMessageIFLoginWithRandomUserWithoutAddingInBase() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.errorNotificationShouldBeVisible();
    }

    @Test
    @DisplayName("Should get error message, if user with exist in base, but verification code is random")
    void shouldGetErrorMessageIFUserInBaseAndRandomVerificationCode() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPage();
        var verificationCode = DataHelper.generateRandomeVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification();
    }

    @Test
    @DisplayName("Should get error message about system blocking, when the password was entered incorrectly three times")
    void shouldBlockAfterThreeEnterWithInvalidPassword() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.generateUserWithValidLoginAndInvalidPassword();
        loginPage.getErrorIfEnterInvalidPasswordThreeTimes();
    }
}