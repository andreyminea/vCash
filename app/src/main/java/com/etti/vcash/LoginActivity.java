package com.etti.vcash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.etti.customViews.CreditCardView;
import com.etti.managers.SharedPrefsSingleton;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton signinBtn;
    private MaterialButton signupBtn;
    private CreditCardView creditCardView;
    private final String TAG = "DEBUGG";
    private final String PAUSE_SHOW = "INTENT_PAUSE_SHOW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signinBtn = (MaterialButton) findViewById(R.id.btnSignIn);
        signupBtn = (MaterialButton) findViewById(R.id.btnCreateAccount);
        creditCardView = (CreditCardView) findViewById(R.id.creditCard);

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPrefs = SharedPrefsSingleton.getPrefs(getApplicationContext());
        String cardNumber = sharedPrefs.getString(SharedPrefsSingleton.SP_CARD_NUMBER, "0");

        if(!cardNumber.equals("0")) {
            creditCardView.setCardNumber(cardNumber, true);
        }

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinUser();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signinUser()
    {
        boolean isValid = creditCardView.isCardNumberValid() && creditCardView.isCvvValid();
        SharedPreferences sp = SharedPrefsSingleton.getPrefs(getApplicationContext());
        String cardCvv = sp.getString(SharedPrefsSingleton.SP_CARD_CVV, "0");
        String cardNumber = sp.getString(SharedPrefsSingleton.SP_CARD_NUMBER, "0");

        if(!cardCvv.equals("0") && !cardNumber.equals("0"))
        {
            if(cardNumber.equals(creditCardView.getCardNumber()))
            {
                if(cardCvv.equals(creditCardView.getCvv()))
                {
                    LogToMain();
                }
                else
                {
                    Toast.makeText(this, "Card CVV is incorrect", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "CVV: " + creditCardView.getCvv());
                }
            }
            else
            {
                Toast.makeText(this, "Card number is incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void LogToMain()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(PAUSE_SHOW, false);
        startActivity(intent);
    }

}