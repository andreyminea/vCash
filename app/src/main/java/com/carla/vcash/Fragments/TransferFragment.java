package com.carla.vcash.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carla.managers.FirebaseSingleton;
import com.carla.managers.SharedPrefsSingleton;
import com.carla.models.Card;
import com.carla.models.Record;
import com.carla.models.SimpleUser;
import com.carla.models.User;
import com.carla.vcash.Adapters.ItemClickListener;
import com.carla.vcash.Adapters.UserAdapter;
import com.carla.vcash.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TransferFragment extends Fragment implements ItemClickListener {

    private static String TAG = "DEBUGG";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<SimpleUser> users;
    private MaterialCardView atmCard;
    private ImageView atmSelect;
    private TextInputEditText sendInput;
    private MaterialButton sendBtn;
    private int lastSelectedPosition;
    private boolean isATMSelected;

    public TransferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param1, String param2) {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }

    private void getAllUsers()
    {
        FirebaseSingleton.getUsersCollection().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    users = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                    {
                        String debug = "Name: ";
                        User newUser = doc.toObject(User.class);
                        SimpleUser newSimpleUser = new SimpleUser(newUser);
                        newSimpleUser.setUserID(doc.getId());
                        if(!newSimpleUser.getUserID().equals(
                                                SharedPrefsSingleton.getUserDocID(getContext())))
                            users.add(newSimpleUser);
                    }
                    InitList();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "Error getting all the users");
                    }
                });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastSelectedPosition = -1;
        isATMSelected = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.userList);
        atmCard = (MaterialCardView) view.findViewById(R.id.atm_withdraw);
        atmSelect = (ImageView) view.findViewById(R.id.selectCheck);
        sendBtn = (MaterialButton) view.findViewById(R.id.sendMoneyBtn);
        sendInput = (TextInputEditText) view.findViewById(R.id.sendInput);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSomethingChecked())
                {
                    Toast.makeText(getContext(), "Please selecte a sender", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String moneyText = "";
                    if (sendInput.getText().length() == 0)
                    {
                        moneyText = "0";
                    }
                    else
                    {
                        moneyText = sendInput.getText().toString();
                    }
                    float money = Float.parseFloat(moneyText);
                    if(money > 0)
                    {
                        if(isATMSelected)
                        {
                            atmWithdraw(money);
                        }
                        if(lastSelectedPosition > -1)
                        {
                            Toast.makeText(getContext(), "Sending money to somebody", Toast.LENGTH_SHORT).show();
                            personSend(money, lastSelectedPosition);
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Type the amount you want to send", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        sendInput.addTextChangedListener(new TextWatcher() {
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
        return view;
    }

    private void personSend(float money, int lastSelectedPosition)
    {
        // Create History
        // Subtract money from account
        // Add money to selected account
        SimpleUser currentUser = new SimpleUser(SharedPrefsSingleton.getUser(getContext()));
        currentUser.setUserID(SharedPrefsSingleton.getUserDocID(getContext()));
        SimpleUser otherUser = users.get(lastSelectedPosition);

        FirebaseSingleton.getUserCardCollectionReference(currentUser.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String currentUserCardID = queryDocumentSnapshots.getDocuments().get(0).getId();
                float currentUserBalance = queryDocumentSnapshots.getDocuments().get(0).toObject(Card.class).getBalance();
                FirebaseSingleton.getUserCardCollectionReference(otherUser.getUserID()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String otherUserCardID = queryDocumentSnapshots.getDocuments().get(0).getId();
                        float otherUserBalance = queryDocumentSnapshots.getDocuments().get(0).toObject(Card.class).getBalance();
                        if(currentUserBalance >= money)
                        {
                            FirebaseSingleton
                                    .getUserCardDocumentReference(currentUser.getUserID(), currentUserCardID)
                                    .update("balance", currentUserBalance - money);
                            FirebaseSingleton
                                    .getUserCardDocumentReference(otherUser.getUserID(), otherUserCardID)
                                    .update("balance", otherUserBalance + money);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Insufficient founds", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void atmWithdraw(float money)
    {
        String cardID = SharedPrefsSingleton.getUserCardDocID(getContext());
        String userID = SharedPrefsSingleton.getUserDocID(getContext());

        FirebaseSingleton.getUserCardDocumentReference(userID, cardID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot value) {
                Card tempCard = value.toObject(Card.class);
                if (tempCard.getBalance() >= money) {
                    FirebaseSingleton.getUserCardDocumentReference(userID, cardID).update("balance", tempCard.getBalance() - money).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Operation done!", Toast.LENGTH_SHORT).show();
                            Record transactionRecord = new Record();
                            transactionRecord.setOperationType(Record.OPERATION_TYPE.WITHDRAW);
                            transactionRecord.setAmount(money);
                            FirebaseSingleton.getUserHistoryReference(userID).add(transactionRecord);
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Insufficient founds", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isSomethingChecked()
    {
        if(isATMSelected || lastSelectedPosition > -1)
            return true;
        return false;
    }

    private void InitList()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(users, this, getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllUsers();

        atmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isATMSelected) {
                    atmSelect.setVisibility(View.VISIBLE);
                    if (lastSelectedPosition != -1) {
                        UserAdapter.ViewHolder holder = (UserAdapter.ViewHolder) recyclerView
                                .findViewHolderForAdapterPosition(lastSelectedPosition);
                        holder.selectIcon.setVisibility(View.INVISIBLE);
                        lastSelectedPosition = -1;
                    }
                    isATMSelected = true;
                }
                else
                {
                    atmSelect.setVisibility(View.INVISIBLE);
                    isATMSelected=false;
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if(lastSelectedPosition != -1)
        {
            UserAdapter.ViewHolder holder = (UserAdapter.ViewHolder) recyclerView
                                            .findViewHolderForAdapterPosition(lastSelectedPosition);
            holder.selectIcon.setVisibility(View.INVISIBLE);
        }
        if(isATMSelected)
        {
            atmSelect.setVisibility(View.INVISIBLE);
            isATMSelected=false;
        }
        if(lastSelectedPosition == position)
        {
            lastSelectedPosition = -1;
        }
        else {
            UserAdapter.ViewHolder holder = (UserAdapter.ViewHolder) recyclerView.findContainingViewHolder(view);
            holder.selectIcon.setVisibility(View.VISIBLE);
            lastSelectedPosition = position;
        }
        Log.d(TAG, users.get(position).getFirstname());
    }
}