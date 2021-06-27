package com.etti.models;

import java.util.Random;

public class Card
{
    private String cardNumber;
    private String expDate;
    private String cvv;
    private String cardholder;
    private Float balance;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", expDate='" + expDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", cardholder='" + cardholder + '\'' +
                ", balance=" + balance +
                '}';
    }

    public void generateNewCard()
    {
        generateCardNumber();
        generateCardExpDate();
        generateCvv();
        balance = 0.f;
    }

    private void generateCardNumber()
    {
        /*
            5555 XXXX XXXX XXXX
         */
        cardNumber = "5555";
        for(int i=0; i<3; i++)
        {
            cardNumber+=" ";
            for(int j=0; j<4; j++)
            {
                cardNumber+=generateRandomDigit();
            }
        }
    }

    private void generateCardExpDate()
    {
        /*
            any_month/ 23-26
         */
        int month = generateRandomDigit(1, 12);
        String monthStr = "";
        if(month<10)
        {
            monthStr+="0";
        }
        monthStr += Integer.toString(month);

        int year = 23 + generateRandomDigit(1,3);
        String yearStr = Integer.toString(year);
        expDate = monthStr+"/"+yearStr;
    }

    private void generateCvv()
    {
        cvv = "";
        for(int i=0; i<3; i++)
        {
            cvv += generateRandomDigit();
        }
    }

    private String generateRandomDigit()
    {
        Random random = new Random();
        int max = 10;
        return Integer.toString(random.nextInt(max));

    }
    private int generateRandomDigit(int min, int max)
    {
        Random random = new Random();
        int result = min + random.nextInt(max);
        return result;
    }
}
