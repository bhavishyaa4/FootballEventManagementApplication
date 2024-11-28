package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.firebase.adminLogReg.AdminTournamentActivity;
import com.example.firebase.adminTournamentList.AdminTournamentLIstAcitivty;

public class ListTournamentFragment extends Fragment {

    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tournament, container, false);
        btn = view.findViewById(R.id.ListTournament);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AdminTournamentLIstAcitivty.class);
                startActivity(in);
            }
        });

        return view;
    }
}