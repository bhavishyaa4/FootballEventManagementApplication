//package com.example.firebase.UserDisplayTheirProfile;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.firebase.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.List;
//
//public class UserDetailsActivity extends AppCompatActivity {
//
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_details);
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance();
//
//        // Fetch and display user details
//        fetchUserDetails();
//    }
//
//    private void fetchUserDetails() {
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user ID
//        if (userId == null || userId.isEmpty()) {
//            Log.e("UserDetailsActivity", "User ID is null or empty");
//            return; // Handle error or show a message
//        }
//
//        // Fetch user details from Firestore
//        db.collection("users").document(userId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            // Fetch user details
//                            String name = document.getString("name");
//                            String address = document.getString("address");
//                            String email = document.getString("email");
//                            String phone = document.getString("phone");
//                            String gender = document.getString("gender");
//
//                            // Fetch registered tournaments (which is an array)
//                            List<String> registeredTournaments = (List<String>) document.get("registeredTournaments");
//
//                            // Display the user details and registered tournaments
//                            displayUserDetails(name, address, email, phone, gender, registeredTournaments);
//
//                        } else {
//                            Log.e("UserDetails", "No such document found!");
//                        }
//                    } else {
//                        Log.e("UserDetails", "Error getting document: ", task.getException());
//                    }
//                });
//    }
//
//    private void displayUserDetails(String name, String address, String email, String phone, String gender, List<String> registeredTournaments) {
//        TextView nameTextView = findViewById(R.id.nameTextView);
//        TextView addressTextView = findViewById(R.id.addressTextView);
//        TextView emailTextView = findViewById(R.id.emailTextView);
//        TextView phoneTextView = findViewById(R.id.phoneTextView);
//        TextView genderTextView = findViewById(R.id.genderTextView);
//        TextView tournamentsTextView = findViewById(R.id.tournamentsTextView);
//
//        // Display basic user details
//        nameTextView.setText("Name: " + name);
//        addressTextView.setText("Address: " + address);
//        emailTextView.setText("Email: " + email);
//        phoneTextView.setText("Phone: " + phone);
//        genderTextView.setText("Gender: " + gender);
//
//        // Display the list of registered tournaments
//        if (registeredTournaments != null && !registeredTournaments.isEmpty()) {
//            StringBuilder tournaments = new StringBuilder("Registered Tournaments:\n");
//            for (String tournament : registeredTournaments) {
//                tournaments.append(tournament).append("\n");
//            }
//            tournamentsTextView.setText(tournaments.toString());
//
//            // Add a button for each tournament to view details
//            addTournamentDetailsButton(registeredTournaments);
//        } else {
//            tournamentsTextView.setText("No tournaments registered.");
//        }
//    }
//
//    private void addTournamentDetailsButton(List<String> registeredTournaments) {
//        for (String tournamentId : registeredTournaments) {
//            Button tournamentButton = new Button(this);
//            tournamentButton.setText("View " + tournamentId + " Details");
//
//            tournamentButton.setOnClickListener(v -> {
//                // When a tournament is clicked, navigate to the details page
//                Intent intent = new Intent(UserDetailsActivity.this, UserTournamentDetailsActivity.class);
//                intent.putExtra("tournamentId", tournamentId); // Pass the tournament ID
//                startActivity(intent);
//            });
//
//            // Add button to a layout (assumed LinearLayout with ID tournamentDetailsLayout)
//            LinearLayout layout = findViewById(R.id.tournamentDetailsLayout);
//            layout.addView(tournamentButton);
//        }
//    }
//}
package com.example.firebase.UserDisplayTheirProfile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch and display user details
        fetchUserDetails();
    }

    private void fetchUserDetails() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user ID
        if (userId == null || userId.isEmpty()) {
            Log.e("UserDetailsActivity", "User ID is null or empty");
            return; // Handle error or show a message
        }

        // Fetch user details from Firestore
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Fetch user details
                            String name = document.getString("name");
                            String address = document.getString("address");
                            String email = document.getString("email");
                            String phone = document.getString("phone");
                            String gender = document.getString("gender");

                            // Fetch registered tournaments (which is an array)
                            List<String> registeredTournaments = (List<String>) document.get("registeredTournaments");

                            // Display the user details and registered tournaments
                            displayUserDetails(name, address, email, phone, gender, registeredTournaments);

                        } else {
                            Log.e("UserDetails", "No such document found!");
                        }
                    } else {
                        Log.e("UserDetails", "Error getting document: ", task.getException());
                    }
                });
    }

    private void displayUserDetails(String name, String address, String email, String phone, String gender, List<String> registeredTournaments) {
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView phoneTextView = findViewById(R.id.phoneTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView tournamentsTextView = findViewById(R.id.tournamentsTextView);

        // Display basic user details
        nameTextView.setText("Name: " + name);
        addressTextView.setText("Address: " + address);
        emailTextView.setText("Email: " + email);
        phoneTextView.setText("Phone: " + phone);
        genderTextView.setText("Gender: " + gender);

        // Display the list of registered tournaments
        if (registeredTournaments != null && !registeredTournaments.isEmpty()) {
            tournamentsTextView.setText("Your Registered Tournaments:");

            // Add a delete button for each tournament
            addTournamentDeleteButton(registeredTournaments);
        } else {
            tournamentsTextView.setText("No tournaments registered.");
        }
    }

    private void addTournamentDeleteButton(List<String> registeredTournaments) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayout layout = findViewById(R.id.tournamentDetailsLayout);

        for (String tournamentId : registeredTournaments) {
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete " + tournamentId);

            deleteButton.setOnClickListener(v -> {
                // Show a confirmation dialog
                new AlertDialog.Builder(this)
                        .setTitle("Delete Tournament")
                        .setMessage("Are you sure you want to delete this tournament?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Remove the tournament from the list
                            registeredTournaments.remove(tournamentId);

                            // Update the Firestore document
                            db.collection("users").document(userId)
                                    .update("registeredTournaments", registeredTournaments)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            layout.removeView(deleteButton); // Remove the button from the layout
                                            Toast.makeText(this, "Tournament deleted successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(this, "Failed to delete tournament", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            // Add button to the layout
            layout.addView(deleteButton);
        }
    }
}

