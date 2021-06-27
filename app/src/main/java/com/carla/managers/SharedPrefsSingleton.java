package com.carla.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.carla.models.Card;
import com.carla.models.User;
import com.carla.vcash.R;

public class SharedPrefsSingleton
{
    public static final String SP_HASACCOUNT = "hasAccount";
    public static final String SP_FIRSTNAME = "firstname";
    public static final String SP_LASTNAME = "lastname";
    public static final String SP_PROFILE_IMAGE_LINK = "profileImageLink";
    public static final String SP_PHONE_NUMBER = "phoneNumber";
    public static final String SP_CARD_NUMBER = "cardNumber";
    public static final String SP_CARD_EXP_DATE = "cardExpDate";
    public static final String SP_CARD_CARDHOLDER = "cardholder";
    public static final String SP_CARD_CVV = "cardCVV";
    private static final String SP_USER_DOC_ID = "userDocID";
    private static final String SP_USER_CARD_DOC_ID = "userCardDocID";
    private static final String ERROR_DEFAULT = "No User";

    public static SharedPreferences getPrefs(Context context)
    {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                context.getString(R.string.user_data), Context.MODE_PRIVATE);
        return sharedPrefs;
    }

    public static void saveUserID(String ID, Context context)
    {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_USER_DOC_ID, ID);
        editor.apply();
    }

    public static void saveUserCardID(String ID, Context context)
    {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_USER_CARD_DOC_ID, ID);
        editor.apply();
    }

    public static String getUserDocID(Context context)
    {
        SharedPreferences sp = getPrefs(context);
        return sp.getString(SP_USER_DOC_ID, "None");
    }

    public static String getUserCardDocID(Context context)
    {
        SharedPreferences sp = getPrefs(context);
        return sp.getString(SP_USER_CARD_DOC_ID, "None");
    }

    /*
        Brief details about the user:
        Firstname
        Lastname
        Image
        Phone number
     */
    public static User getUser(Context context)
    {
        User user = new User();
        SharedPreferences sp = getPrefs(context);
        user.setFirstname(sp.getString(SP_FIRSTNAME, ERROR_DEFAULT));
        user.setLastname(sp.getString(SP_LASTNAME, ERROR_DEFAULT));
        user.setImageLink(sp.getString(SP_PROFILE_IMAGE_LINK, ERROR_DEFAULT));
        user.setPhoneNumber(sp.getString(SP_PHONE_NUMBER, ERROR_DEFAULT));
        return user;
    }

    public static Card getCard(Context context)
    {
        Card card = new Card();
        SharedPreferences sp = getPrefs(context);
        card.setCardholder(sp.getString(SP_CARD_CARDHOLDER, ERROR_DEFAULT));
        card.setExpDate(sp.getString(SP_CARD_EXP_DATE, ERROR_DEFAULT));
        card.setCardNumber(sp.getString(SP_CARD_NUMBER, ERROR_DEFAULT));
        card.setCvv(sp.getString(SP_CARD_CVV, ERROR_DEFAULT));
        return card;
    }

    public static void saveUserData(User user, Context context)
    {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_FIRSTNAME, user.getFirstname());
        editor.putString(SP_LASTNAME, user.getLastname());
        editor.putString(SP_PHONE_NUMBER, user.getPhoneNumber());
        editor.putString(SP_PROFILE_IMAGE_LINK, user.getImageLink());
        editor.apply();
    }

    public static void saveUserCardData(Card card, Context context)
    {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SP_CARD_NUMBER, card.getCardNumber());
        editor.putString(SP_CARD_EXP_DATE, card.getExpDate());
        editor.putString(SP_CARD_CARDHOLDER, card.getCardholder());
        editor.putString(SP_CARD_CVV, card.getCvv());
        editor.apply();
    }

    public static void setHasAccount(Context context, boolean value)
    {
        SharedPreferences sp = getPrefs(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SP_HASACCOUNT, value);
        editor.apply();
    }
}
