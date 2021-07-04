package com.carla.vcash.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carla.managers.FirebaseSingleton;
import com.carla.managers.SharedPrefsSingleton;
import com.carla.models.Record;
import com.carla.vcash.Adapters.RecordAdapter;
import com.carla.vcash.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentsFragment extends Fragment {

    private static String TAG = "DEBUGG";
    private ArrayList<Record> records;
    private RecordAdapter adapter;
    private RecyclerView logsRecycler;

    public PaymentsFragment() {

    }

    public static PaymentsFragment newInstance() {
        PaymentsFragment fragment = new PaymentsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        records = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments, container, false);
        logsRecycler = (RecyclerView) view.findViewById(R.id.historyLogs);
        return view;
    }

    private void InitList()
    {
        String userID = SharedPrefsSingleton.getUserDocID(getContext());
        FirebaseSingleton.getUserHistoryReference(userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value,
                                @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty())
                {
                    records.clear();
                    for(DocumentSnapshot doc : value)
                    {
                        Record tempRecord = doc.toObject(Record.class);
                        records.add(tempRecord);
                    }
                    records.sort(new Comparator<Record>() {
                        @Override
                        public int compare(Record o1, Record o2) {
                            return o2.getRecordDate().compareTo(o1.getRecordDate());
                        }
                    });
                    logsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new RecordAdapter(records, getContext());
                    logsRecycler.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        InitList();
    }
}