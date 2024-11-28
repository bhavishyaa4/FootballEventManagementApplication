package com.example.firebase.UserDisplayTheirProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.HomeActivity;
import com.example.firebase.Login;
import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText nameEditText, addressEditText, phoneEditText, newPasswordEditText, currentPasswordEditText;
    private RadioGroup genderGroup;
    private Button updateButton, backToLoginButton;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // References to UI elements
        imageview = findViewById(R.id.updateLogo);
        nameEditText = findViewById(R.id.name);
        addressEditText = findViewById(R.id.address);
        phoneEditText = findViewById(R.id.phone);
        newPasswordEditText = findViewById(R.id.new_password);
        currentPasswordEditText = findViewById(R.id.current_password);
        genderGroup = findViewById(R.id.genderGroup);
        updateButton = findViewById(R.id.update_button);
        backToLoginButton = findViewById(R.id.back_to_login_button);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Update.this, HomeActivity.class);
                startActivity(in);
            }
        });

        // Display user details
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserDetails(user.getUid());
        }

        updateButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String currentPassword = currentPasswordEditText.getText().toString().trim();

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            if (selectedGenderId == -1) {
                Toast.makeText(Update.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String gender = selectedGenderButton.getText().toString();

            updateUserDetails(mAuth.getCurrentUser().getUid(), name, address, phone, gender, newPassword, currentPassword);
        });

        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(Update.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserDetails(String userId) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String address = documentSnapshot.getString("address");
                        String phone = documentSnapshot.getString("phone");
                        String gender = documentSnapshot.getString("gender");

                        // Set the user details into the UI
                        nameEditText.setText(name);
                        addressEditText.setText(address);
                        phoneEditText.setText(phone);

                        if (gender != null) {
                            if (gender.equals("Female")) {
                                genderGroup.check(R.id.radioFemale);
                            } else if (gender.equals("Male")) {
                                genderGroup.check(R.id.radioMale);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Update.this, "Error loading user details", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserDetails(String userId, String name, String address, String phone, String gender, String newPassword, String currentPassword) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("address", address);
        updates.put("phone", phone);
        updates.put("gender", gender);

        db.collection("users").document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Update.this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Update.this, "Error updating user details", Toast.LENGTH_SHORT).show();
                });

        // Change password if a new one is provided
        if (!newPassword.isEmpty()) {
            changeUserPassword(newPassword, currentPassword);
        }
    }

    private void changeUserPassword(String newPassword, String currentPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(passwordTask -> {
                        if (passwordTask.isSuccessful()) {
                            Toast.makeText(Update.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Update.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Update.this, "Re-authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(Update.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
