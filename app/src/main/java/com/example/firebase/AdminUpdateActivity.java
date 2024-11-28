package com.example.firebase;

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

import com.example.firebase.adminLogReg.AdminHomeActivity;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminUpdateActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText adminNameEditText, adminPhoneEditText, adminRoleEditText, newPasswordEditText, currentPasswordEditText;
    private RadioGroup adminGenderGroup;
    private Button updateButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        adminNameEditText = findViewById(R.id.adminname);
        adminPhoneEditText = findViewById(R.id.adminphone);
        adminRoleEditText = findViewById(R.id.role);
        newPasswordEditText = findViewById(R.id.adminpassword);
        currentPasswordEditText = findViewById(R.id.adminconfirm_password);
        adminGenderGroup = findViewById(R.id.admingenderGroup);
        updateButton = findViewById(R.id.updatebtn);
        backButton = findViewById(R.id.backbtn);

        ImageView imageView = findViewById(R.id.imageView5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUpdateActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });

        // Display admin details if the user is logged in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadAdminDetails(user.getUid());
        }

        // Handle update button click
        updateButton.setOnClickListener(v -> {
            String name = adminNameEditText.getText().toString().trim();
            String phone = adminPhoneEditText.getText().toString().trim();
            String role = adminRoleEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String currentPassword = currentPasswordEditText.getText().toString().trim();

            int selectedGenderId = adminGenderGroup.getCheckedRadioButtonId();
            if (selectedGenderId == -1) {
                Toast.makeText(AdminUpdateActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String gender = selectedGenderButton.getText().toString();

            // Update admin details
            updateAdminDetails(user.getUid(), name, phone, role, gender, newPassword, currentPassword);
        });

        // Handle back button click
        backButton.setOnClickListener(v -> finish());
    }

    private void loadAdminDetails(String adminId) {
        // Fetch the admin details from Firestore
        db.collection("admins").document(adminId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve data from the document
                        String name = documentSnapshot.getString("Name");
                        String phone = documentSnapshot.getString("Phone");
                        String role = documentSnapshot.getString("Role");
                        String gender = documentSnapshot.getString("Gender");

                        // Populate fields with existing admin details
                        adminNameEditText.setText(name != null ? name : "");
                        adminPhoneEditText.setText(phone != null ? phone : "");
                        adminRoleEditText.setText(role != null ? role : "");

                        if (gender != null) {
                            if (gender.equalsIgnoreCase("Female")) {
                                adminGenderGroup.check(R.id.radioFemale);
                            } else if (gender.equalsIgnoreCase("Male")) {
                                adminGenderGroup.check(R.id.radioMale);
                            }
                        }
                    } else {
                        Toast.makeText(AdminUpdateActivity.this, "Admin details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminUpdateActivity.this, "Error loading admin details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateAdminDetails(String adminId, String name, String phone, String role, String gender, String newPassword, String currentPassword) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("Name", name);
        updates.put("Phone", phone);
        updates.put("Role", role);
        updates.put("Gender", gender);

        // Update the admin details in Firestore
        db.collection("admins").document(adminId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminUpdateActivity.this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminUpdateActivity.this, "Error updating admin details", Toast.LENGTH_SHORT).show();
                });

        // If new password is provided, attempt to change it
        if (!newPassword.isEmpty()) {
            changeAdminPassword(newPassword, currentPassword);
        }
    }

    private void changeAdminPassword(String newPassword, String currentPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Re-authenticate the admin
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update the password
                    user.updatePassword(newPassword).addOnCompleteListener(passwordTask -> {
                        if (passwordTask.isSuccessful()) {
                            Toast.makeText(AdminUpdateActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminUpdateActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AdminUpdateActivity.this, "Re-authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AdminUpdateActivity.this, "No admin logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
