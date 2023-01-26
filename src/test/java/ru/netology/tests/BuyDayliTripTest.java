package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;
import ru.netology.pages.BuyDayliTripPage;


import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DbHelper.*;

public class BuyDayliTripTest {

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        databaseCleanUp();
    }

    @Nested
    public class ValidApprovedCard {

        @Test
        @SneakyThrows
        @DisplayName("Purchase with a valid card")
        public void shouldPaymentValidCard() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            buyDayliTripPage.approved();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        }

        @Test
        @SneakyThrows
        @DisplayName("Purchase on credit with a valid card")
        public void shouldCreditValidCard() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choiceCreditPayment();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var creditInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, creditInfo.getStatus());
            assertEquals(creditInfo.getId(), orderInfo.getCredit_id());
            buyDayliTripPage.approved();
        }

    }

    @Nested
    public class ValidDeclinedCard {

        @Test
        @SneakyThrows
        @DisplayName("Payment with a declined card")
        public void shouldPaymentDeclinedCard() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "DECLINED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            buyDayliTripPage.declined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Purchase on credit with a declined card")
        public void shouldCreditDeclinedCard() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choiceCreditPayment();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "DECLINED";
            var paymentInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getBank_id(), orderInfo.getPayment_id());
            buyDayliTripPage.declined();

        }
    }

    @Nested
    public class EmptyFields {

        @Test
        @SneakyThrows
        @DisplayName("The card number field is empty")
        public void shouldCardNumberError() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(null, cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.cardNumberError();
        }

        @Test
        @SneakyThrows
        @DisplayName("The month field is empty")
        public void shouldMonthError() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), null, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.monthError();
        }

        @Test
        @SneakyThrows
        @DisplayName("The year field is empty")
        public void shouldYearError() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), null,
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.yearError();
        }

        @Test
        @SneakyThrows
        @DisplayName("The owner field is empty")
        public void shouldOwnerError() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    null, cardData.getCvc());
            buyDayliTripPage.ownerError();
        }

        @Test
        @SneakyThrows
        @DisplayName("The cvc field is empty")
        public void shouldCVCError() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), null);
            buyDayliTripPage.cvcError();
        }
    }

    @Nested
    public class OtherTests {

        @Test
        @SneakyThrows
        @DisplayName("Switching to card payment")
        public void shouldSwitchingOnCardPayment() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choiceCreditPayment();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
        }
        @Test
        @SneakyThrows
        @DisplayName("Switching to buying on credit")
        public void shouldSwitchingOnCredit() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.choiceCreditPayment();
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("11 digits in the card number field")
        public void elevenDigitsCardNumber() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(getCardNumberWith11Digits(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.cardNumberError();
        }

        @Test
        @SneakyThrows
        @DisplayName("Random 12 digits in the card number field")
        public void random12CardNumber() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(getCardNumberWith12Digits(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            buyDayliTripPage.declined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Random 19 digits in the card number field")
        public void random19CardNumber() {
            var cardData = getValidDeclinedCard();
            var number = DataGenerator.getCardNumberWith19Digits();
            var comparisonNumber = number;
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(number, cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            buyDayliTripPage.comparisonSendAndCurrentData(comparisonNumber, cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.declined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Random 20 digits in the card number field")
        public void random20CardNumber() {
            var cardData = getValidDeclinedCard();
            var number = DataGenerator.getCardNumberWith20Digits();
            var comparisonNumber = number;
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(number, cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            buyDayliTripPage.comparisonSendAndCurrentData(comparisonNumber, cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.declined();
        }

        @Test
        @SneakyThrows
        @DisplayName("Random symbols in the card number field")
        public void randomSymbolsCardNumber() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(getInvalidCardNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.cardNumberError();
        }

        @Test
        @SneakyThrows
        @DisplayName("1 digits in the month field")
        public void oneDigitsInMonth() {
            var cardData = getValidDeclinedCard();
            var month = DataGenerator.getOneDigit();
            var comparisonMonth = "0" + month;
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), comparisonMonth, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("3 digits in the month field")
        public void threeDigitsInMonth() {
            var cardData = getValidDeclinedCard();
            var month = cardData.getMonth() + DataGenerator.getOneDigit();
            var comparisonMonth = cardData.getMonth();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), comparisonMonth, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("Two 0 in the month field")
        public void two0InMonth() {
            var cardData = getValidDeclinedCard();
            var month = "00";
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.monthError();
        }

        @Test
        @SneakyThrows
        @DisplayName("01 in the month field")
        public void theMonth01() {
            var cardData = getValidApprovedCard();
            var month = "01";
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            buyDayliTripPage.approved();
        }

        @Test
        @SneakyThrows
        @DisplayName("12 in the month field")
        public void twelveInMonth() {
            var cardData = getValidApprovedCard();
            var month = "12";
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            buyDayliTripPage.approved();
        }

        @Test
        @SneakyThrows
        @DisplayName("13 in the month field")
        public void thirteenInMonth() {
            var cardData = getValidApprovedCard();
            var month = "13";
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), month, cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.monthError();
        }
        @Test
        @SneakyThrows
        @DisplayName("Random symbols in month field")
        public void randomSymbolsInMonth() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), getInvalidMonth(), cardData.getYear(),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.monthError();
        }

        @Test
        @SneakyThrows
        @DisplayName("The year field contains the date in the past")
        public void oldYear() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), getYear(- 1),
                    cardData.getOwner(), cardData.getCvc());
            buyDayliTripPage.yearError();
        }

        // Данная проверка возможна начиная со второг месяц текущего года
//        @Test
//        @SneakyThrows
//        @DisplayName("The month field contains the last year of the current year.")
//        public void oldMonth() {
//            var cardData = getValidApprovedCard();
//            var buyDayliTripPage = new BuyDayliTripPage();
//            buyDayliTripPage.choicePaymentCard();
//            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), getMonth(-1), getYear(0),
//                    cardData.getOwner(), cardData.getCvc());
//            buyDayliTripPage.monthError();
//
//        }
        @Test
        @SneakyThrows
        @DisplayName("Current month and year")
        public void currentMonthAndYear() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), getMonth(0), getYear(0),
                    cardData.getOwner(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            buyDayliTripPage.approved();
        }

        @Test
        @SneakyThrows
        @DisplayName("Writing a name with a hyphen")
        public void hyphenInName() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    getOwnerDoubleName(), cardData.getCvc());
            TimeUnit.SECONDS.sleep(15);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            assertEquals(expected, paymentInfo.getStatus());
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            buyDayliTripPage.approved();
        }

        @Test
        @SneakyThrows
        @DisplayName("Owner in lower case")
        public void ownerLowerCase() {
            var cardData = getValidDeclinedCard();
            var owner = cardData.getOwner().toLowerCase();
            var comparisonMonth = cardData.getOwner();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    owner, cardData.getCvc());
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    comparisonMonth, cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("Spaces at the beginning and end owner")
        public void spacesInFiledOwner() {
            var cardData = getValidDeclinedCard();
            var owner = " " + cardData.getOwner() + " ";
            var comparisonMonth = cardData.getOwner();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    owner, cardData.getCvc());
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    comparisonMonth, cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("Hyphen at the beginning and end owner")
        public void hyphenInFiledOwner() {
            var cardData = getValidDeclinedCard();
            var owner = "-" + cardData.getOwner() + "-";
            var comparisonMonth = cardData.getOwner();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    owner, cardData.getCvc());
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    comparisonMonth, cardData.getCvc());
        }

        @Test
        @SneakyThrows
        @DisplayName("Cardholder's name in Cyrillic")
        public void nameInCyrillic() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    getOwnerWithCyrillic(), cardData.getCvc());
            buyDayliTripPage.ownerError();
        }

        @Test
        @SneakyThrows
        @DisplayName("Random characters in cardholder name")
        public void nameInRandomSymbols() {
            var cardData = getValidDeclinedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    getOwnerWithRandomSymbols(), cardData.getCvc());
            buyDayliTripPage.ownerError();
        }

        @Test
        @SneakyThrows
        @DisplayName("There are two numbers in the CVC field")
        public void cvcTwoDigits() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), getCVC2Digits());
            buyDayliTripPage.cvcError();
        }

        @Test
        @SneakyThrows
        @DisplayName("Trimming extra characters in a field CVC")
        public void shouldTrimmingLongCVC() {
            var cardData = getValidDeclinedCard();
            var cvc = cardData.getCvc() + DataGenerator.getOneDigit();
            var comparisonCVC = cardData.getCvc();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), cvc);
            buyDayliTripPage.comparisonSendAndCurrentData(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), comparisonCVC);
        }

        @Test
        @SneakyThrows
        @DisplayName("Random symbols in the CVC field")
        public void cvcRandomSymbols() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), getInvalidCVC());
            buyDayliTripPage.cvcError();
        }
        @Test
        @SneakyThrows
        @DisplayName("Random litters in the CVC field")
        public void cvcRandomLitters() {
            var cardData = getValidApprovedCard();
            var buyDayliTripPage = new BuyDayliTripPage();
            buyDayliTripPage.choicePaymentCard();
            buyDayliTripPage.sendDataInFoarm(cardData.getNumber(), cardData.getMonth(), cardData.getYear(),
                    cardData.getOwner(), getLiteralCVC());
            buyDayliTripPage.cvcError();
        }

    }
}