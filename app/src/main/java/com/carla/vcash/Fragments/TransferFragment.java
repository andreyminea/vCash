package com.carla.vcash.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carla.managers.FirebaseSingleton;
import com.carla.models.SimpleUser;
import com.carla.models.User;
import com.carla.vcash.Adapters.ItemClickListener;
import com.carla.vcash.Adapters.UserAdapter;
import com.carla.vcash.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TransferFragment extends Fragment implements ItemClickListener {

    private static String TAG = "DEBUGG";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<SimpleUser> users;
    private int lastSelectedPosition;

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
                        User newUser = doc.toObject(User.class);
                        SimpleUser newSimpleUser = new SimpleUser(newUser);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.userList);
        return view;
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
    }

    @Override
    public void onItemClick(View view, int position) {
        if(lastSelectedPosition != -1)
        {
            UserAdapter.ViewHolder holder = (UserAdapter.ViewHolder) recyclerView
                                            .findViewHolderForAdapterPosition(lastSelectedPosition);
            holder.selectIcon.setVisibility(View.INVISIBLE);
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