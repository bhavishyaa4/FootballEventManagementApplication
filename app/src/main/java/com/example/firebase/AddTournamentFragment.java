package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.firebase.adminAddTournament.AddTournamentActivity;
import com.example.firebase.adminLogReg.AdminHomeActivity;


public class AddTournamentFragment extends Fragment {
    Button btn;
    ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_tournament, container, false);

        btn = view.findViewById(R.id.addTournament);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTournamentActivity.class);
                startActivity(intent);

            }
        });

        image = view.findViewById(R.id.goBack);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminHomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}