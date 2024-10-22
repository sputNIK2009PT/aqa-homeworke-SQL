// Запустить Docker Desktop
// В терминале IDEA выполнить команду docker compose up
// Для запуска тестируемого приложения, находясь в корне проекта,
// выполните в терминале команду: java -jar ./artifacts/app-deadline.jar

// Используется selenide

package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class BankLoginTest {
    private LoginPage loginPage;

    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @AfterEach
    void tearDown() {
        SQLHelper.cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        SQLHelper.cleanDataBase();
    }

    @Test
    @DisplayName("Successful login to your personal account")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthUser();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Error message. The user does not exist in the database")
    void shouldShowErrorForNonExistentUser() {
        var authInfo = DataHelper.getRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotification("Неверно указан логин или пароль");
    }

    @Test
    @DisplayName("Error message. The login exists, but the verification code is incorrect.")
    void shouldShowErrorForIncorrectVerificationCode() {
        var authInfo = DataHelper.getAuthUser();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPage();
        var verificationCode = DataHelper.getRandomCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Неверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("Logging in three times with the wrong password")
    void shouldBlockUserAfterThreeFailedLoginAttempts() {
        for (int i = 0; i < 3; i++) {
            loginPage.loginWithCredentials(DataHelper.getAuthUser().getLogin(),
                    DataHelper.getRandomUser().getPassword());
            loginPage.verifyErrorNotification("Неверно указан логин или пароль");
            open("http://localhost:9999", LoginPage.class);
        }

        var authInfo = DataHelper.getAuthUser();
        Assertions.assertEquals("blocked", SQLHelper.getUserStatus(authInfo.getLogin()));

        loginPage.loginWithCredentials(authInfo.getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.verifyErrorNotification("Данный пользователь заблокирован");
    }
}
