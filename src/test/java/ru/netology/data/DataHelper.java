package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));

    private DataHelper() {
    }

    public static AuthInfo getAuthUser() {return new AuthInfo("vasya", "qwerty123");
    }


    public static AuthInfo getRandomUser() {return new AuthInfo(faker.name().username(), faker.internet().password());
    }

    public static VerificationCode getRandomCode() {return new VerificationCode(faker.numerify("######"));
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }


    @Value
    public static class VerificationCode {
        String code;
    }
}