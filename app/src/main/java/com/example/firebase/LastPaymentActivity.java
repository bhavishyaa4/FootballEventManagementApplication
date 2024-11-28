//package com.example.firebase;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class LastPaymentActivity extends AppCompatActivity {
//    Button btn1, btn2;
//    ImageView imageView;
//    FirebaseFirestore db;
//    FirebaseAuth mAuth;
//    FirebaseUser currentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_last_payment);
//
//        imageView = findViewById(R.id.payend);
//        btn1 = findViewById(R.id.yesbtn);
//        btn2 = findViewById(R.id.nobtn);
//
//        // Initialize Firebase
//        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(LastPaymentActivity.this, PaymentActivity.class);
//                startActivity(in);
//            }
//        });
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(LastPaymentActivity.this, PaymentActivity.class);
//                startActivity(in);
//            }
//        });
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // When "Yes" button is clicked, update payment status to "Paid"
//                if (currentUser != null) {
//                    updatePaymentStatus("Paid");
//                } else {
//                    Toast.makeText(LastPaymentActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    // Method to update payment status in Firebase Firestore
//    private void updatePaymentStatus(String status) {
//        if (currentUser != null) {
//            // Reference to the current user's document in the Firestore "users" collection
//            db.collection("users").document(currentUser.getUid())
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            // Get the array of registered tournaments
//                            List<String> registeredTournaments = (List<String>) documentSnapshot.get("registeredTournaments");
//
//                            if (registeredTournaments != null && registeredTournaments.size() > 0) {
//                                // Assuming you want to update the payment status for the first tournament
//                                // You can change the index based on which tournament you want to update
//                                String tournament = registeredTournaments.get(0); // Modify the index as needed
//
//                                // Create a map for the tournament details with the paymentStatus
//                                Map<String, Object> tournamentData = new HashMap<>();
//                                tournamentData.put("name", tournament);
//                                tournamentData.put("paymentStatus", status);
//
//                                // Update the registeredTournaments array with the new payment status
//                                db.collection("users").document(currentUser.getUid())
//                                        .update("registeredTournaments", FieldValue.arrayUnion(tournamentData))
//                                        .addOnSuccessListener(aVoid -> {
//                                            Toast.makeText(LastPaymentActivity.this, "Payment Status Updated", Toast.LENGTH_SHORT).show();
//                                            // Redirect to a success page
//                                            Intent intent = new Intent(LastPaymentActivity.this, PaymentSuccessActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        })
//                                        .addOnFailureListener(e -> {
//                                            Toast.makeText(LastPaymentActivity.this, "Error updating payment status", Toast.LENGTH_SHORT).show();
//                                        });
//                            } else {
//                                Toast.makeText(LastPaymentActivity.this, "No registered tournaments found", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(LastPaymentActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(LastPaymentActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            Toast.makeText(LastPaymentActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}
package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LastPaymentActivity extends AppCompatActivity {
    Button btn1, btn2;
    ImageView imageView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_payment);

        imageView = findViewById(R.id.payend);
        btn1 = findViewById(R.id.yesbtn);
        btn2 = findViewById(R.id.nobtn);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LastPaymentActivity.this, PaymentActivity.class);
                startActivity(in);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LastPaymentActivity.this, PaymentActivity.class);
                startActivity(in);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LastPaymentActivity.this, PaymentSuccessActivity.class);
                startActivity(intent);
                finish();  // Optionally finish the current activity to prevent going back to it
            }
        });
    }
}
