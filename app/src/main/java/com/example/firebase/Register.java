package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText nameEditText = findViewById(R.id.name);
        EditText addressEditText = findViewById(R.id.address);
        EditText phoneEditText = findViewById(R.id.phone);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password);
        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        Button registerButton = findViewById(R.id.register_button);
        TextView loginaccount = findViewById(R.id.login_account);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String address = addressEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            int selectedGenderId = genderGroup.getCheckedRadioButtonId();
            if (selectedGenderId == -1) {
                Toast.makeText(Register.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedGenderButton = findViewById(selectedGenderId);
            String gender = selectedGenderButton.getText().toString();

            // Validation checks
            if (name.isEmpty()) {
                Toast.makeText(Register.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (address.isEmpty()) {
                Toast.makeText(Register.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.isEmpty()) {
                Toast.makeText(Register.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(Register.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty() || password.length() < 6) {
                Toast.makeText(Register.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call to register user
            registerUser(name, address, phone, gender, email, password);
        });

        loginaccount.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser(String name, String address, String phone, String gender, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            storeUserData(user.getUid(), name, address, phone, gender, email, password);
                        } else {
                            Log.e("RegisterActivity", "FirebaseUser is null after registration");
                            Toast.makeText(Register.this, "User registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void storeUserData(String userId, String name, String address, String phone, String gender, String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("address", address);
        user.put("phone", phone);
        user.put("gender", gender);
        user.put("email", email);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("RegisterActivity", "User data successfully written!");
                    Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    // After registration, redirect to Login page
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w("RegisterActivity", "Error writing user data", e);
                    Toast.makeText(Register.this, "Failed to store user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
