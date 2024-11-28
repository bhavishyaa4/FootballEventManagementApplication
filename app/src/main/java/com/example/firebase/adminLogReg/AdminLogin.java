package com.example.firebase.adminLogReg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class AdminLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText adminEmail, adminPass;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        adminEmail = findViewById(R.id.admin_login_emaill);
        adminPass = findViewById(R.id.admin_login_passwordd);
        btn = findViewById(R.id.admin_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmail.getText().toString().trim();
                String pass = adminPass.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(AdminLogin.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginAdmin(email, pass);
            }
        });
    }

    private void loginAdmin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("AdminLoginActivity", "signInWithEmail:success");
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (currentUser != null) {
                            // Check if the user exists in the admins collection
                            db.collection("admins").document(currentUser.getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot document = task1.getResult();
                                            if (document != null && document.exists()) {
                                                // User is an admin, proceed to AdminHomeActivity
                                                Toast.makeText(AdminLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AdminLogin.this, AdminHomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // User is not an admin, log out and show error
                                                mAuth.signOut();
                                                Toast.makeText(AdminLogin.this, "You are not authorized as an admin", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.w("AdminLoginActivity", "Error getting admin document", task1.getException());
                                            Toast.makeText(AdminLogin.this, "Error verifying admin", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.w("AdminLoginActivity", "signInWithEmail:failure", task.getException());
                        Toast.makeText(AdminLogin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
