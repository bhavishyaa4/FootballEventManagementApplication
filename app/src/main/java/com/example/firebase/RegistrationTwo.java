package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.ModelClass.PlayerDetails;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationTwo extends AppCompatActivity {

    private EditText keeper1, keeper2, defence1, defence2, defence3, defence4, defence5, midfield1, midfield2, midfield3,midfield4, midfield5, striker1, striker2, striker3;
    private FirebaseFirestore db;
    private String teamId;
    private ImageView imageView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_two);

        btn = findViewById(R.id.submitForPayment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationTwo.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

        imageView = findViewById(R.id.register2Logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationTwo.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();

        keeper1 = findViewById(R.id.keeper1);
        keeper2 = findViewById(R.id.keeper2);
        defence1 = findViewById(R.id.d1);
        defence2 = findViewById(R.id.d2);
        defence3 = findViewById(R.id.d3);
        defence4 = findViewById(R.id.d4);
        defence5 = findViewById(R.id.d5);
        midfield1 = findViewById(R.id.md1);
        midfield2 = findViewById(R.id.md2);
        midfield3 = findViewById(R.id.md3);
        midfield4 = findViewById(R.id.md4);
        midfield5 = findViewById(R.id.md5);
        striker1 = findViewById(R.id.sk1);
        striker2 = findViewById(R.id.sk2);
        striker3 = findViewById(R.id.sk3);

        teamId = getIntent().getStringExtra("teamId");
        if (teamId == null || teamId.isEmpty()) {
            Toast.makeText(this, "Error: Team ID not provided.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        findViewById(R.id.submitForPayment).setOnClickListener(v -> savePlayerDetails());
    }

    private void savePlayerDetails() {
        // Retrieve and trim input values
        String keeper1Name = keeper1.getText().toString().trim();
        String keeper2Name = keeper2.getText().toString().trim();
        String defence1Name = defence1.getText().toString().trim();
        String defence2Name = defence2.getText().toString().trim();
        String defence3Name = defence3.getText().toString().trim();
        String defence4Name = defence4.getText().toString().trim();
        String defence5Name = defence5.getText().toString().trim();
        String midfield1Name = midfield1.getText().toString().trim();
        String midfield2Name = midfield2.getText().toString().trim();
        String midfield3Name = midfield3.getText().toString().trim();
        String midfield4Name = midfield4.getText().toString().trim();
        String midfield5Name = midfield5.getText().toString().trim();
        String striker1Name = striker1.getText().toString().trim();
        String striker2Name = striker2.getText().toString().trim();
        String striker3Name = striker3.getText().toString().trim();

        if (keeper1Name.isEmpty() || keeper2Name.isEmpty() || defence1Name.isEmpty() || defence2Name.isEmpty() ||
                defence3Name.isEmpty() || defence4Name.isEmpty() || defence5Name.isEmpty() || midfield1Name.isEmpty() ||
                midfield2Name.isEmpty() || midfield3Name.isEmpty() || midfield4Name.isEmpty() || midfield5Name.isEmpty() ||
                striker1Name.isEmpty() || striker2Name.isEmpty() || striker3Name.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        PlayerDetails playerDetails = new PlayerDetails(keeper1Name, keeper2Name, defence1Name, defence2Name, defence3Name, defence4Name, defence5Name,
                midfield1Name, midfield2Name, midfield3Name, midfield4Name, midfield5Name, striker1Name, striker2Name, striker3Name, teamId);


        db.collection("registered_user").document(teamId).collection("players")
                .document("PlayerDetails").set(playerDetails)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Player details saved successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PaymentActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
