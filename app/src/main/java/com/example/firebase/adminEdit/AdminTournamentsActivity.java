package com.example.firebase.adminEdit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.adminLogReg.AdminTournamentActivity;
import com.example.firebase.R;
import com.example.firebase.tournamentListProcess.Tournament;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminTournamentsActivity extends AppCompatActivity {
    private RecyclerView tournamentRecycle;
    private AdminTournamentAdapter tournamentAdapter;
    private List<Tournament> tournamentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tournaments);

        tournamentRecycle = findViewById(R.id.admintournamentRecyclerView);
        tournamentRecycle.setLayoutManager(new GridLayoutManager(this, 2));

        // Set logo click listener
        ImageView image = findViewById(R.id.adminHello);
        image.setOnClickListener(v -> {
            Intent intent = new Intent(AdminTournamentsActivity.this, AdminTournamentActivity.class);
            startActivity(intent);
        });

        // Initialize tournament list and adapter
        tournamentList = new ArrayList<>();
        tournamentAdapter = new AdminTournamentAdapter(tournamentList, this);
        tournamentRecycle.setAdapter(tournamentAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load tournaments from Firestore
        loadsTournamentsFromFirestore();
    }

    private void loadsTournamentsFromFirestore() {
        db.collection("tournaments")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("TournamentError", "Listen failed", e);
                        return;
                    }

                    if (snapshots != null && !snapshots.isEmpty()) {
                        tournamentList.clear();  // Clear list to avoid duplicates
                        for (QueryDocumentSnapshot document : snapshots) {
                            if (document.contains("name") && document.contains("address") &&
                                    document.contains("contact") && document.contains("email") &&
                                    document.contains("price") && document.contains("endDate") &&
                                    document.contains("image")) {

                                String name = document.getString("name");
                                String address = document.getString("address");
                                String contact = document.getString("contact");
                                String email = document.getString("email");
                                Long priceLong = document.getLong("price");
                                if (priceLong != null) {
                                    int price = priceLong.intValue();
                                    String endDate = document.getString("endDate");
                                    String imageUrl = document.getString("image");

                                    Tournament tournament = new Tournament(name, address, contact, email, price, endDate, imageUrl);
                                    tournament.setId(document.getId()); // Set ID for editing
                                    tournamentList.add(tournament);
                                } else {
                                    Log.e("TournamentError", "Price is null for document: " + document.getId());
                                }
                            } else {
                                Log.e("TournamentError", "Missing fields in Firestore document: " + document.getId());
                            }
                        }
                        tournamentAdapter.notifyDataSetChanged();  // Update RecyclerView
                    }
                });
    }
}