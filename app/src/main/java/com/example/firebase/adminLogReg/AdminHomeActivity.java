package com.example.firebase.adminLogReg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.AdminUpdateActivity;
import com.example.firebase.HomeActivity;
import com.example.firebase.NewsActivity;
import com.example.firebase.R;
import com.example.firebase.UserDisplayTheirProfile.Update;
import com.example.firebase.adminTournamentList.AdminTournamentLIstAcitivty;
import com.example.firebase.adminUserDisplay.AdminUserDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private List<String> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        Spinner mySpinner = findViewById(R.id.admin_go_downn);
        allItems = new ArrayList<>();
        allItems.add("⋮"); // Add the triple-dot icon as the first item
        allItems.addAll(Arrays.asList(getResources().getStringArray(R.array.admin_spinner_items)));
        adapter = new ArrayAdapter<>(this, R.layout.admin_spinner_item, allItems);
        adapter.setDropDownViewResource(R.layout.admin_spinner_item); // Use the same layout for dropdown

        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(AdminHomeActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();

                    if (selectedItem.equals("Tournament")) {
                        Intent intent = new Intent(AdminHomeActivity.this, AdminTournamentActivity.class);
                        startActivity(intent);
                    }  else if (selectedItem.equals("Profile")) {
                        Intent intent = new Intent(AdminHomeActivity.this, AdminUpdateActivity.class);
                        startActivity(intent);
                    }else if (selectedItem.equals("News")) {
                        Intent intent = new Intent(AdminHomeActivity.this, NewsActivity.class);
                        startActivity(intent);
                    }
                    else if (selectedItem.equals("User")){
                        Intent intent = new Intent(AdminHomeActivity.this, AdminUserDetailsActivity.class);
                        startActivity(intent);
                    }else if (selectedItem.equals("Logout")){
                        logoutUser();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mySpinner.setOnTouchListener((v, event) -> {
            if (allItems.contains("⋮")) {
                allItems.remove(0);
                adapter.notifyDataSetChanged();
            }
            return false;
        });
        findViewById(android.R.id.content).setOnTouchListener((v, event) -> {
            if (!allItems.contains("⋮")) {
                allItems.add(0, "⋮");
                adapter.notifyDataSetChanged();
                mySpinner.setSelection(0);
            }
            return false;
        });
    }
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Logout user
        Intent intent = new Intent(AdminHomeActivity.this, AdminLogin.class); // Navigate to HomeActivity
        startActivity(intent);
        finish(); // Finish the current activity
        Toast.makeText(AdminHomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}