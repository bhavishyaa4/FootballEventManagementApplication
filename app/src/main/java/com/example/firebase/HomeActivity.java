package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.UserDisplayTheirProfile.UserProfileDetailActivity;
import com.example.firebase.tournamentListProcess.TournamentActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> allItems;
    private List<String> spinnerItems;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        Button tournamentBtn = findViewById(R.id.tournamentbutton);
        Button newsBtn = findViewById(R.id.newsbutton);
        imageView = findViewById(R.id.imageViews);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(in);
            }
        });
        Spinner mySpinner = findViewById(R.id.go_down);
        allItems = new ArrayList<>();
        allItems.add("⋮"); // Add the triple-dot icon as the first item
        allItems.addAll(Arrays.asList(getResources().getStringArray(R.array.spinner_items)));
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, allItems);
        adapter.setDropDownViewResource(R.layout.spinner_item); // Use the same layout for dropdown

        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    Toast.makeText(HomeActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(HomeActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();

                    if (selectedItem.equals("Tournament")) {
                        Intent intent = new Intent(HomeActivity.this, TournamentActivity.class);
                        startActivity(intent);
                    }else if (selectedItem.equals("News")) {
                        Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                        startActivity(intent);
                    }else if (selectedItem.equals("Profile")) {
                        Intent intent = new Intent(HomeActivity.this, UserProfileDetailActivity.class);
                        startActivity(intent);
                    } else if(selectedItem.equals("Logout")){
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

        tournamentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TournamentActivity.class);
                startActivity(intent);
            }
        });

        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Logout user
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class); // Navigate to HomeActivity
        startActivity(intent);
        finish(); // Finish the current activity
        Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
