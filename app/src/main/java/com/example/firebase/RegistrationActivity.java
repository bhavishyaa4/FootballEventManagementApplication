package com.example.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.firebase.ModelClass.TeamRegistration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private EditText teamName, teamAddress, coachName, contactNumber, email;
    private TextView tournamentName, tournamentAddress, tournamentContact, tournamentPrice, tournamentEndDate;
    private ImageView tournamentImage, imageshow;
    private Button regbtn;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginSession", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("USER_ID", null);

        // Initialize UI components
        teamName = findViewById(R.id.teamName);
        teamAddress = findViewById(R.id.teamAddress);
        coachName = findViewById(R.id.coachName);
        contactNumber = findViewById(R.id.contactNumber);
        email = findViewById(R.id.email);
        tournamentName = findViewById(R.id.tournamentName);
        tournamentAddress = findViewById(R.id.tournamentAddress);
        tournamentContact = findViewById(R.id.tournamentContact);
        tournamentPrice = findViewById(R.id.tournamentPrice);
        tournamentEndDate = findViewById(R.id.tournamentEndDate);
        tournamentImage = findViewById(R.id.tournamentImage);
        regbtn = findViewById(R.id.registerButton);
        imageshow = findViewById(R.id.registerLogo);

        // Get tournament details from intent
        String name = getIntent().getStringExtra("TOURNAMENT_NAME");
        String address = getIntent().getStringExtra("TOURNAMENT_ADDRESS");
        String contact = getIntent().getStringExtra("TOURNAMENT_CONTACT");
        int price = getIntent().getIntExtra("TOURNAMENT_PRICE", 0);
        String endDate = getIntent().getStringExtra("TOURNAMENT_END_DATE");
        String imageUrl = getIntent().getStringExtra("TOURNAMENT_IMAGE_URL");

        // Set tournament details in UI
        tournamentName.setText(name);
        tournamentAddress.setText("Address: " + address);
        tournamentContact.setText("Contact: " + contact);
        tournamentPrice.setText("Price: " + price + "/- Only");
        tournamentEndDate.setText("End Date: " + endDate);

        // Load tournament image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.chelsea)
                    .error(R.drawable.chelsea)
                    .into(tournamentImage);
        }

        // Register button click listener
        regbtn.setOnClickListener(v -> {
            if (currentUserId == null) {
                showLoginRegisterPopup();
                return;
            }

            // Check if the user has already registered for the tournament
            checkIfAlreadyRegistered(name);  // Updated method to handle async properly
        });

        // Home logo click listener
        imageshow.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    private void checkIfAlreadyRegistered(String tournamentName) {
        // Check if the user has already registered for this tournament in the users collection
        db.collection("users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the list of registered tournaments for the current user
                        List<String> registeredTournaments = (List<String>) documentSnapshot.get("registeredTournaments");

                        if (registeredTournaments != null && registeredTournaments.contains(tournamentName)) {
                            // User has already registered for this tournament, show message and do not allow registration
                            Toast.makeText(this, "You have already registered for this tournament.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Proceed with the registration process since user has not registered yet
                            registerTeam(tournamentName);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void registerTeam(String tournamentName) {
        String teamNameText = teamName.getText().toString().trim();
        String teamAddressText = teamAddress.getText().toString().trim();
        String coachNameText = coachName.getText().toString().trim();
        String contactNumberText = contactNumber.getText().toString().trim();
        String emailText = email.getText().toString().trim();

        // Check for empty fields
        if (teamNameText.isEmpty() || teamAddressText.isEmpty() || coachNameText.isEmpty() || contactNumberText.isEmpty() || emailText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!isValidEmail(emailText)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique team ID and prepare data
        String teamId = db.collection("registered_user").document().getId();
        TeamRegistration teamData = new TeamRegistration(teamNameText, teamAddressText, coachNameText, contactNumberText, emailText, teamId);

        db.collection("registered_user").document(teamId).set(teamData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Team Registered Successfully", Toast.LENGTH_SHORT).show();

                    // Update user document to reflect registered tournament
                    db.collection("users").document(currentUserId)
                            .update("registeredTournaments", FieldValue.arrayUnion(tournamentName))
                            .addOnSuccessListener(unused -> {
                                Intent intent = new Intent(this, RegistrationTwo.class);
                                intent.putExtra("teamId", teamId);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }


    // Show popup if user is not logged in
    private void showLoginRegisterPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Required")
                .setMessage("Please register or log in to register for the tournament.")
                .setPositiveButton("Register", (dialog, which) -> {
                    startActivity(new Intent(this, Register.class));
                })
                .setNegativeButton("Log In", (dialog, which) -> {
                    startActivity(new Intent(this, Login.class));
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = currentUser != null ? currentUser.getUid() : null;

        if (currentUserId == null) {
            showLoginRegisterPopup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("LoginSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}


//package com.example.firebase;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import com.bumptech.glide.Glide;
//import com.example.firebase.ModelClass.TeamRegistration;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.List;
//
//public class RegistrationActivity extends AppCompatActivity {
//
//    private EditText teamName, teamAddress, coachName, contactNumber, email;
//    private TextView tournamentName, tournamentAddress, tournamentContact, tournamentPrice, tournamentEndDate;
//    private ImageView tournamentImage, imageshow;
//    private Button regbtn;
//    private FirebaseFirestore db;
//    private FirebaseAuth auth;
//    private String currentUserId;
//    private String tournamentId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
//
//        db = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("LoginSession", MODE_PRIVATE);
//        currentUserId = sharedPreferences.getString("USER_ID", null);
//
//        // Initialize UI components
//        teamName = findViewById(R.id.teamName);
//        teamAddress = findViewById(R.id.teamAddress);
//        coachName = findViewById(R.id.coachName);
//        contactNumber = findViewById(R.id.contactNumber);
//        email = findViewById(R.id.email);
//        tournamentName = findViewById(R.id.tournamentName);
//        tournamentAddress = findViewById(R.id.tournamentAddress);
//        tournamentContact = findViewById(R.id.tournamentContact);
//        tournamentPrice = findViewById(R.id.tournamentPrice);
//        tournamentEndDate = findViewById(R.id.tournamentEndDate);
//        tournamentImage = findViewById(R.id.tournamentImage);
//        regbtn = findViewById(R.id.registerButton);
//        imageshow = findViewById(R.id.registerLogo);
//
//        // Get tournament details from intent
//        String name = getIntent().getStringExtra("TOURNAMENT_NAME");
//        String address = getIntent().getStringExtra("TOURNAMENT_ADDRESS");
//        String contact = getIntent().getStringExtra("TOURNAMENT_CONTACT");
//        int price = getIntent().getIntExtra("TOURNAMENT_PRICE", 0);
//        String endDate = getIntent().getStringExtra("TOURNAMENT_END_DATE");
//        String imageUrl = getIntent().getStringExtra("TOURNAMENT_IMAGE_URL");
//        tournamentId = getIntent().getStringExtra("TOURNAMENT_ID");  // Ensure tournamentId is passed here
//
//        // Set tournament details in UI
//        tournamentName.setText(name);
//        tournamentAddress.setText("Address: " + address);
//        tournamentContact.setText("Contact: " + contact);
//        tournamentPrice.setText("Price: " + price + "/- Only");
//        tournamentEndDate.setText("End Date: " + endDate);
//
//        // Load tournament image
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            Glide.with(this)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.chelsea)
//                    .error(R.drawable.chelsea)
//                    .into(tournamentImage);
//        }
//
//        // Register button click listener
//        regbtn.setOnClickListener(v -> {
//            if (currentUserId == null) {
//                showLoginRegisterPopup();
//                return;
//            }
//
//            // Check if the user has already registered for the tournament
//            checkIfAlreadyRegistered(name);  // Updated method to handle async properly
//        });
//
//        // Home logo click listener
//        imageshow.setOnClickListener(v -> {
//            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    private void checkIfAlreadyRegistered(String tournamentName) {
//        // Check if the user has already registered for this tournament in the users collection
//        db.collection("users")
//                .document(currentUserId)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        // Get the list of registered tournaments for the current user
//                        List<String> registeredTournaments = (List<String>) documentSnapshot.get("registeredTournaments");
//
//                        if (registeredTournaments != null && registeredTournaments.contains(tournamentName)) {
//                            // User has already registered for this tournament, show message and do not allow registration
//                            Toast.makeText(this, "You have already registered for this tournament.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            // Proceed with the registration process since user has not registered yet
//                            registerTeam(tournamentName, tournamentId);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    private void registerTeam(String tournamentName, String tournamentId) {
//        String teamNameText = teamName.getText().toString().trim();
//        String teamAddressText = teamAddress.getText().toString().trim();
//        String coachNameText = coachName.getText().toString().trim();
//        String contactNumberText = contactNumber.getText().toString().trim();
//        String emailText = email.getText().toString().trim();
//
//        // Check for empty fields
//        if (teamNameText.isEmpty() || teamAddressText.isEmpty() || coachNameText.isEmpty() || contactNumberText.isEmpty() || emailText.isEmpty()) {
//            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Generate unique team ID and prepare data
//        String teamId = db.collection("registered_user").document().getId();
//        TeamRegistration teamData = new TeamRegistration(teamNameText, teamAddressText, coachNameText, contactNumberText, emailText, teamId, tournamentId);  // Added tournamentId
//
//        // Save team data to Firestore
//        db.collection("registered_user").document(teamId).set(teamData)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(this, "Team Registered Successfully", Toast.LENGTH_SHORT).show();
//
//                    // Update user document to reflect registered tournament
//                    db.collection("users").document(currentUserId)
//                            .update("registeredTournaments", FieldValue.arrayUnion(tournamentName))
//                            .addOnSuccessListener(unused -> {
//                                Intent intent = new Intent(this, RegistrationTwo.class);
//                                intent.putExtra("teamId", teamId);
//                                intent.putExtra("tournamentId", tournamentId);  // Added tournamentId
//                                startActivity(intent);
//                            })
//                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to update user info: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    // Show popup if user is not logged in
//    private void showLoginRegisterPopup() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Login Required")
//                .setMessage("Please register or log in to register for the tournament.")
//                .setPositiveButton("Register", (dialog, which) -> {
//                    startActivity(new Intent(this, Register.class));
//                })
//                .setNegativeButton("Log In", (dialog, which) -> {
//                    startActivity(new Intent(this, Login.class));
//                })
//                .show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        currentUserId = currentUser != null ? currentUser.getUid() : null;
//
//        if (currentUserId == null) {
//            showLoginRegisterPopup();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences sharedPreferences = getSharedPreferences("LoginSession", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//    }
//}

