package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement login = $("[data-test-id=login] input");
    private SelenideElement password = $("[data-test-id=password] input");
    private SelenideElement button = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");

    public void verifyErrorNotification(String expectedText) {
        errorNotification.shouldHave(text(expectedText)).shouldBe(visible, Duration.ofMillis(5000));
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        login.setValue(info.getLogin());
        password.setValue(info.getPassword());
        button.click();
        return new VerificationPage();
    }

    public void loginWithCredentials(String login, String password) {
        this.login.setValue(login);
        this.password.setValue(password);
        button.click();
    }
}
