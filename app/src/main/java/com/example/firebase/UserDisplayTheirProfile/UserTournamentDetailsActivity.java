package com.example.firebase.UserDisplayTheirProfile;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserTournamentDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView teamNameTextView, teamIdTextView, contactNumberTextView, teamAddressTextView, playerDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tournament_details);

        // Initialize Firestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize TextViews for displaying team and player details
        teamNameTextView = findViewById(R.id.teamNameTextView);
        teamIdTextView = findViewById(R.id.teamIdTextView);
        contactNumberTextView = findViewById(R.id.contactNumberTextView);
        teamAddressTextView = findViewById(R.id.teamAddressTextView);
        playerDetailsTextView = findViewById(R.id.playerDetailsTextView);

        // Retrieve the tournamentId passed from the previous activity (intent)
        String tournamentId = getIntent().getStringExtra("tournamentId");

        if (tournamentId != null) {
            fetchTeamDetails(tournamentId);
        } else {
            // Handle the case where the tournamentId is not passed or is null
            Toast.makeText(this, "No Tournament ID provided", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to fetch team details
    private void fetchTeamDetails(String tournamentId) {
        db.collection("registered_user")
                .document(tournamentId) // Use the tournamentId to get the specific team details
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String teamName = document.getString("teamName");
                            String teamId = document.getString("teamId");
                            String contactNumber = document.getString("contactNumber");
                            String teamAddress = document.getString("teamAddress");

                            // Display team details
                            teamNameTextView.setText("Team Name: " + teamName);
                            teamIdTextView.setText("Team ID: " + teamId);
                            contactNumberTextView.setText("Contact Number: " + contactNumber);
                            teamAddressTextView.setText("Team Address: " + teamAddress);

                            fetchPlayerDetails(tournamentId);
                        } else {
                            Log.e("UserTournamentDetails", "No such document!");
                            Toast.makeText(UserTournamentDetailsActivity.this, "No team found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("UserTournamentDetails", "Error getting document: ", task.getException());
                        Toast.makeText(UserTournamentDetailsActivity.this, "Error fetching team details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to fetch player details from the 'player' sub-collection
    private void fetchPlayerDetails(String tournamentId) {
        db.collection("registered_user")
                .document(tournamentId)
                .collection("player")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder playerDetails = new StringBuilder("Players:\n");

                        // Iterate through the documents in the player sub-collection
                        for (DocumentSnapshot document : task.getResult()) {
                            String position = document.getId(); // Player position (e.g., defence, midfield)
                            String playerName = document.getString("name");

                            if (playerName != null) {
                                playerDetails.append(position).append(": ").append(playerName).append("\n");
                            } else {
                                playerDetails.append(position).append(": ").append("Unknown Player").append("\n");
                            }
                        }

                        playerDetailsTextView.setText(playerDetails.toString());
                    } else {
                        Log.e("UserTournamentDetails", "Error getting player details: ", task.getException());
                        Toast.makeText(UserTournamentDetailsActivity.this, "Error fetching player details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
