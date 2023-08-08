package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorMessage = $("[data-test-id=error-notification]");
    private SelenideElement closeErrorMessageButton = $("[data-test-id=error-notification] button");

    public void errorNotificationShouldBeVisible() {
        errorMessage.shouldBe(visible);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void clearPasswordField() {
        passwordField.doubleClick().sendKeys(Keys.BACK_SPACE);
    }

    public void getErrorIfEnterInvalidPasswordThreeTimes() {
        $(byText("Превышен лимит ввода пароля")).shouldBe(visible);
    }

    public void enterInvalidPasswordThreeTime(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        errorNotificationShouldBeVisible();
        closeErrorMessageButton.click();
        clearPasswordField();
        passwordField.setValue(info.getPassword());
        loginButton.click();
        closeErrorMessageButton.click();
        clearPasswordField();
        passwordField.setValue(info.getPassword());
        loginButton.click();
        getErrorIfEnterInvalidPasswordThreeTimes();
    }
}