package com.carla.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.carla.vcash.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class CreditCardView extends MaterialCardView
{
    // FOR CARD NUMBER
    private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
    private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
    private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
    private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
    private static final char DIVIDER = ' ';
    //

    private final String TAG = "DEBUGG cv";

    private String mCardholderName;
    private String mCardNumber;
    private String mExpDate;
    private String mCvv;
    private boolean mShowName;
    private boolean mShowExprDate;

    private RelativeLayout bgLayout;
    private TextInputEditText cardNumber;
    private TextInputEditText cardExpDate;
    private TextInputEditText cardholder;
    private TextInputEditText cardCvv;
    private TextView cardExprDateText;

    public CreditCardView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        this.setRadius(50);

        TypedArray typedArray = context.obtainStyledAttributes(
                                            attrs, R.styleable.CreditCardView);
        mCardholderName = typedArray.getString(
                                R.styleable.CreditCardView_card_holder);
        mCardNumber = typedArray.getString(
                                R.styleable.CreditCardView_card_number);
        mExpDate = typedArray.getString(
                                R.styleable.CreditCardView_expiration_date);
        mCvv = typedArray.getString(R.styleable.CreditCardView_cvv);
        mShowExprDate = typedArray.getBoolean(
                                R.styleable.CreditCardView_show_exprDate, true);
        mShowName = typedArray.getBoolean(
                R.styleable.CreditCardView_show_name, true);
        typedArray.recycle();

        init(context);
    }

    @SuppressLint("ResourceType")
    private void init(Context context)
    {
        inflate(context, R.layout.layout_credit_card, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bgLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        cardNumber = (TextInputEditText) findViewById(R.id.card_number);
        cardExpDate = (TextInputEditText) findViewById(R.id.cardExpDate);
        cardExprDateText = (TextView) findViewById(R.id.card_exprDate_text);
        cardholder = (TextInputEditText) findViewById(R.id.cardholder_name);
        cardCvv = (TextInputEditText) findViewById(R.id.cvv);

        setCardholder();
        setCardNumber(true);
        setCardExpDate();
        setCvv(true);

        if(!mShowName) {
            cardholder.setVisibility(INVISIBLE);
        }
        if(!mShowExprDate) {
            cardExpDate.setVisibility(INVISIBLE);
            cardExprDateText.setVisibility(INVISIBLE);
        }

        cardCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCvv = s.toString();
            }
        });

        cardExpDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strS = s.toString();
                if(strS.length()>2 && !strS.contains("/"))
                {
                    s.insert(2, "/");
                }
            }
        });

        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(
                            getDigitArray(s, TOTAL_DIGITS),
                            DIVIDER_POSITION,
                            DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }

        });
    }

    public boolean isCardNumberValid()
    {
        String cardNr = cardNumber.getText().toString();
        if(cardNr.length() == 19)
            return true;
        return false;
    }

    public boolean isCvvValid()
    {
        if(cardCvv.getText().toString().length() == 3)
            return true;
        return false;
    }



    // Getters and setters

    public String getCardholder() {
        if(mCardholderName != null)
            return mCardholderName;
        return "";
    }
    public void setCardholder(String cardholderName)
    {
        mCardholderName = cardholderName.toUpperCase();
        setCardholder();
    }
    private void setCardholder() {
        if(mCardholderName != null)
        {
            cardholder.setText(mCardholderName.toUpperCase());
            cardholder.setEnabled(false);
        }
    }

    public String getCardNumber() {
        if(mCardNumber != null)
            return mCardNumber;
        return "";
    }

    public void setCardNumber(String cardNumber, boolean editable)
    {
        mCardNumber = cardNumber;
        setCardNumber(editable);
    }

    private void setCardNumber(boolean editable) {
        if(mCardNumber != null)
        {
            cardNumber.setText(mCardNumber);
            cardNumber.setEnabled(editable);
        }
    }

    public String getExpDate() {
        if(mExpDate != null)
            return mExpDate;
        return "";
    }

    public void setCardExpDate(String expDate)
    {
        mExpDate = expDate;
        setCardExpDate();
    }

    private void setCardExpDate() {
        if(mExpDate != null)
        {
            cardExpDate.setText(mExpDate);
            cardExpDate.setEnabled(false);
        }
    }

    public String getCvv() {
        return mCvv;
    }

    // Value must have 3 digits
    public void setCvv(String Cvv)
    {
        mCvv = Cvv;
    }

    public void setCvv(String cvv, Boolean enabled)
    {
        mCvv = cvv;
        setCvv(enabled);
    }

    private void setCvv(Boolean enabled)
    {
        cardCvv.setText(mCvv);
        cardCvv.setEnabled(enabled);
    }

}
