package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(LogoutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

        Toast.makeText(LogoutActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
