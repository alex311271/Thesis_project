package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {


    private static final Faker faker = new Faker(new Locale("en"));
    private static final Faker fakerWithCyrillic = new Faker(new Locale("ru", "RU"));

    private static final String approvedCard = "4444 4444 4444 4441";
    private static final String declinedCard = "4444 4444 4444 4442";


    private DataGenerator() {}


    @Value
    public static class CardData {
        private final String number;
        private final String month;
        private final String year;
        private final String owner;
        private final String cvc;
    }

    public static CardData getValidApprovedCard() {
        return new CardData(approvedCard, getMonth(3), getYear(2), getOwner(), getCVC());
    }

    public static CardData getValidDeclinedCard() {
        return new CardData(declinedCard, getMonth(2), getYear(1), getOwner(), getCVC());
    }

//    public static int shiftData() {
//        if (getMonth(- 1) == "12") {
//            return (- 1);
//        } return (0);
//    }

    public static String getCardNumberWith11Digits() {
        return faker.numerify("4444 44## ###");
    }
    public static String getCardNumberWith12Digits() {
        return faker.numerify("4444 444# #### ####");
    }
    public static String getCardNumberWith19Digits() {
        return faker.numerify("4444 444# #### #### ###");
    }
    public static String getCardNumberWith20Digits() {
        return faker.numerify("4444 444# #### #### ####");
    }
    public static String getInvalidCardNumber() {
        return faker.numerify("???? ???? ???? ????");
    }

    public static String getMonth(int shiftMonth) {
        return LocalDate.now().plusMonths(shiftMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getOneDigit() {
        return faker.numerify("#");
    }
    public static String getInvalidMonth() {
        return faker.regexify("[a-z!@#$%^&*()_+-=}{|?><]{2}");
    }


    public static String getYear(int shiftYear) {
        return  LocalDate.now().plusYears(shiftYear).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getOwner() {
        return faker.name().fullName().toUpperCase();
    }
    public static String getOwnerDoubleName() {
        return faker.name().firstName().toUpperCase() + " " + faker.name().lastName().toUpperCase() + "-"
                + faker.name().lastName().toUpperCase();
    }
    public static String getOwnerWithCyrillic() {
        return fakerWithCyrillic.name().firstName().toUpperCase() + " " + fakerWithCyrillic.name().lastName().toUpperCase();
    }
    public static String getOwnerWithRandomSymbols() {
        return faker.regexify("[!@#$%^&*()_+-={}|?><]{6} [!@#$%^&*()_+-={}|?><]{5}");
    }

    public static String getCVC() {
        return faker.numerify("###");
    }
    public static String getCVC2Digits() {
        return faker.numerify("##");
    }

    public static String getInvalidCVC() {
        return faker.regexify("[!@#$%^&*()_+-={}|?><]{3}");
    }
    public static String getLiteralCVC() {
        return faker.bothify("???");
    }

}
