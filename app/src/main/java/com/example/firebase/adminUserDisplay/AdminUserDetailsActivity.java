package com.example.firebase.adminUserDisplay;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.R;
import com.example.firebase.Adapter.UserAdapter;
import com.example.firebase.ModelClass.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUserDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> userList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList,AdminUserDetailsActivity.this);
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();

        fetchUsers(); // Fetch user data from Firestore
    }

    private void fetchUsers() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                String name = document.getString("name") != null ? document.getString("name") : "N/A";
                                String address = document.getString("address") != null ? document.getString("address") : "N/A";
                                String email = document.getString("email") != null ? document.getString("email") : "N/A";
                                String phone = document.getString("phone") != null ? document.getString("phone") : "N/A";
                                String gender = document.getString("gender") != null ? document.getString("gender") : "N/A";

                                // Safely retrieve registeredTournaments
                                List<?> rawTournaments = (List<?>) document.get("registeredTournaments");

                                // Convert rawTournaments to a List<String>, if necessary
                                List<String> registeredTournaments = new ArrayList<>();
                                if (rawTournaments != null) {
                                    for (Object tournament : rawTournaments) {
                                        if (tournament instanceof String) {
                                            registeredTournaments.add((String) tournament);
                                        }
                                    }
                                }

                                // Create user object with parsed data
                                Users user = new Users(name, address, email, phone, gender, registeredTournaments);
                                userList.add(user);

                            } catch (Exception e) {
                                Log.e("AdminUserDetails", "Error parsing user document: " + document.getId(), e);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("AdminUserDetails", "Error getting users: ", task.getException());
                        Toast.makeText(AdminUserDetailsActivity.this, "Failed to fetch users.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
