package ru.yandex.practicum.model;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    @Step("Generate random courier")
    public static CourierData getRandomCourier(String loginParam, String passwordParam,
                                               String firstNameParam){
        String login = loginParam + RandomStringUtils.randomAlphabetic(4);
        String password = passwordParam + RandomStringUtils.randomAlphabetic(4);
        String firstName = firstNameParam + RandomStringUtils.randomAlphabetic(4);

        return new CourierData(login, password, firstName);
    }

    @Step("Generate random courier without login")
    public static CourierData getRandomCourierWithoutLoginArgument(String passwordParam,
                                                                   String firstNameParam){
        String password = passwordParam + RandomStringUtils.randomAlphabetic(4);
        String firstName = firstNameParam + RandomStringUtils.randomAlphabetic(4);

        return new CourierData(null, password, firstName);
    }

    @Step("Generate random courier without password")
    public static CourierData getRandomCourierWithoutPasswordArgument(String loginParam,
                                                                      String firstNameParam){
        String login = loginParam + RandomStringUtils.randomAlphabetic(4);
        String firstName = firstNameParam + RandomStringUtils.randomAlphabetic(4);

        return new CourierData(login, null, firstName);
    }
}
