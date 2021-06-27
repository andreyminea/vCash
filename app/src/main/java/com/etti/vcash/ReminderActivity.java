package com.etti.vcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etti.managers.SharedPrefsSingleton;
import com.google.android.material.button.MaterialButton;

public class ReminderActivity extends AppCompatActivity {

    private TextView cardNumber;
    private TextView cardCVV;
    private MaterialButton nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        cardNumber = (TextView) findViewById(R.id.reminderCardNumber);
        cardCVV = (TextView) findViewById(R.id.reminderCardCVV);
        nextBtn = (MaterialButton) findViewById(R.id.reminderNextButton);

        SharedPreferences sharedPrefs = SharedPrefsSingleton.getPrefs(getApplicationContext());
        String SPcardNumber = sharedPrefs.getString(SharedPrefsSingleton.SP_CARD_NUMBER, "0");
        String SPcardCVV = sharedPrefs.getString(SharedPrefsSingleton.SP_CARD_CVV, "0");
        cardNumber.setText(SPcardNumber);
        cardCVV.setText(SPcardCVV);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}