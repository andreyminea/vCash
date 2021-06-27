package com.carla.vcash.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.carla.customViews.CreditCardView;
import com.carla.managers.FirebaseSingleton;
import com.carla.managers.SharedPrefsSingleton;
import com.carla.models.Card;
import com.carla.models.User;
import com.carla.vcash.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeFragment extends Fragment {

    private static String TAG = "DEBUGG";
    private Handler mHandler;
    private User user;
    private ImageView profileImage;
    private TextView userName;
    private TextView balance;
    private MaterialButton showAddMoneyWidgetBtn;
    private MaterialButton addMoneyBtn;
    private CardView addMoneyWidget;
    private TextInputEditText amountInput;
    private CreditCardView virtualCard;
    private Boolean isAddMoneyWidgetOpen;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPrefsSingleton.getUser(getContext());
        mHandler = new Handler();
        isAddMoneyWidgetOpen = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        userName = (TextView) view.findViewById(R.id.userName);
        balance = (TextView) view.findViewById(R.id.accountBalance);
        showAddMoneyWidgetBtn = (MaterialButton) view.findViewById(R.id.accountAddMoneyBtn);
        addMoneyWidget = (CardView) view.findViewById(R.id.addMoneyCard);
        addMoneyBtn = (MaterialButton) view.findViewById(R.id.addMoneyBtn);
        amountInput = (TextInputEditText) view.findViewById(R.id.amountInput);
        virtualCard = (CreditCardView) view.findViewById(R.id.virtualCard);

        Glide.with(getContext())
                .load(user.getImageLink())
                .circleCrop()
                .into(profileImage);
        String userFullName = user.getFirstname() + " " + user.getLastname();
        addVirtualCardDetails();
        listenForBalanceChanges();
        userName.setText(userFullName);

        showAddMoneyWidgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAddMoneyWidgetOpen) {
                    virtualCard.setVisibility(View.INVISIBLE);
                    addMoneyWidget.setVisibility(View.VISIBLE);
                    showAddMoneyWidgetBtn.setIconResource(R.drawable.ic_round_close_24);
                    isAddMoneyWidgetOpen = true;
                }
                else
                {
                    addMoneyWidget.setVisibility(View.INVISIBLE);
                    virtualCard.setVisibility(View.VISIBLE);
                    showAddMoneyWidgetBtn.setIconResource(R.drawable.ic_round_add_24);
                    isAddMoneyWidgetOpen = false;
                }
            }
        });

        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                String[] result = str.split("\\.");
                int left = result[0].length();
                if(result.length > 1)
                {
                    int right = result[1].length();
                    if(right>2)
                    {
                        s.delete(left+1+2, left+1+right);
                    }
                }
            }
        });


        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testAmount = amountInput.getText().toString();
                Toast.makeText(getContext(), testAmount, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopListening();
    }

    private void listenForBalanceChanges()
    {
        mBalanceChecker.run();
    }

    private void stopListening()
    {
        mHandler.removeCallbacks(mBalanceChecker);
    }

    Runnable mBalanceChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateBalance();
            }finally {
                mHandler.postDelayed(mBalanceChecker, 1000);
            }
        }
    };


    private void updateBalance()
    {
        String userID = SharedPrefsSingleton.getUserDocID(getContext());
        String cardID = SharedPrefsSingleton.getUserCardDocID(getContext());
        FirebaseSingleton
                .getUserCardDocumentReference(userID, cardID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value,
                                        @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error)
                    {
                        if(error != null)
                        {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(value != null && value.exists())
                        {
                            Card tempCard = value.toObject(Card.class);
                            balance.setText(Float.toString(tempCard.getBalance()));
                        }
                    }
                });
    }

    private void addVirtualCardDetails()
    {
        Card userCard = SharedPrefsSingleton.getCard(getContext());
        virtualCard.setCardholder(userCard.getCardholder());
        virtualCard.setCardExpDate(userCard.getExpDate());
        virtualCard.setCardNumber(userCard.getCardNumber(), false);
        virtualCard.setCvv(userCard.getCvv(), false);
    }
}