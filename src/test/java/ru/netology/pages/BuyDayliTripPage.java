package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyDayliTripPage {

    private final SelenideElement buyHeader = $(byText("Оплата по карте"));
    private final SelenideElement creditHeader = $(byText("Кредит по данным карты"));

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement continueButton = $("#root > div > form > fieldset > div:nth-child(4) > button > span");

    private final SelenideElement cardNumberInput = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthInput = $("input[placeholder='08'");
    private final SelenideElement yearInput = $("input[placeholder='22'");
    private final SelenideElement ownerInput = $("#root > div > form > fieldset > div:nth-child(3) > span > span:nth-child(1) > span > span > span.input__box > input");
    private final SelenideElement cvcInput = $("input[placeholder='999'");

    private final SelenideElement cardNumberError = $x(".//*[@id='root']/div/form/fieldset/div[1]/span/span/span[3]");
    private final SelenideElement monthError = $x("//*[@id=\"root\"]/div/form/fieldset/div[2]/span/span[1]/span/span/span[3]");
    private final SelenideElement yearError = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private final SelenideElement ownerError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private final SelenideElement cvcError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");
    private final SelenideElement notificationError = $x("//*[@id=\"root\"]/div/div[3]/div[1]/span");

    private final SelenideElement notificationSuccessfully = $(".notification_status_ok");

    public void choicePaymentCard() {
        buyButton.click();
        buyHeader.shouldBe(Condition.visible);
        continueButton.shouldBe(Condition.visible);
    }

    public void choiceCreditPayment() {
        creditButton.click();
        creditHeader.shouldBe(Condition.visible);
        continueButton.shouldBe(Condition.visible);
    }

    public void sendDataInFoarm(String number, String month, String year, String owner, String cvc) {
        cardNumberInput.setValue(number);
        monthInput.setValue(month);
        yearInput.setValue(year);
        ownerInput.setValue(owner);
        cvcInput.setValue(cvc);
        continueButton.click();
    }

    public void comparisonSendAndCurrentData(String number, String month, String year, String owner, String cvc) {
        assertEquals(number, cardNumberInput.getValue());
        assertEquals(month, monthInput.getValue());
        assertEquals(year, yearInput.getValue());
        assertEquals(owner, ownerInput.getValue());
        assertEquals(cvc, cvcInput.getValue());
    }

    public void approved() {
        notificationSuccessfully.shouldBe(Condition.visible);
    }

    public void declined() {
        notificationError.shouldBe(Condition.visible);
    }

    public void monthError() {
        cardNumberError.shouldBe(Condition.hidden);
        monthError.shouldBe(Condition.visible);
        yearError.shouldBe(Condition.hidden);
        ownerError.shouldBe(Condition.hidden);
        cvcError.shouldBe(Condition.hidden);
    }
    public void cardNumberError() {
        cardNumberError.shouldBe(Condition.visible);
        monthError.shouldBe(Condition.hidden);
        yearError.shouldBe(Condition.hidden);
        ownerError.shouldBe(Condition.hidden);
        cvcError.shouldBe(Condition.hidden);
    }

    public void yearError() {
        cardNumberError.shouldBe(Condition.hidden);
        monthError.shouldBe(Condition.hidden);
        yearError.shouldBe(Condition.visible);
        ownerError.shouldBe(Condition.hidden);
        cvcError.shouldBe(Condition.hidden);
    }

    public void ownerError() {
        cardNumberError.shouldBe(Condition.hidden);
        monthError.shouldBe(Condition.hidden);
        yearError.shouldBe(Condition.hidden);
        ownerError.shouldBe(Condition.visible);
        cvcError.shouldBe(Condition.hidden);
    }

    public void cvcError() {
        cardNumberError.shouldBe(Condition.hidden);
        monthError.shouldBe(Condition.hidden);
        yearError.shouldBe(Condition.hidden);
        ownerError.shouldBe(Condition.hidden);
        cvcError.shouldBe(Condition.visible);
    }
}
