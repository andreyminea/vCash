package com.etti.vcash.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etti.vcash.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferFragment extends Fragment {


    public TransferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TransferFragment newInstance(String param1, String param2) {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }
}