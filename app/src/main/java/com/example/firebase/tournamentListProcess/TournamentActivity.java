package com.example.firebase.tournamentListProcess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.HomeActivity;
import com.example.firebase.NewsActivity;
import com.example.firebase.R;
import com.example.firebase.UserDisplayTheirProfile.Update;
import com.example.firebase.adminLogReg.AdminHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TournamentActivity extends AppCompatActivity {

    private RecyclerView tournamentRecycle;
    private TournamentAdapter tournamentAdapter;
    private List<Tournament> tournamentList;
    private ArrayAdapter<String> adapter;
    private List<String> allItems;
    private FirebaseFirestore db;
    private EditText edit;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        textView = findViewById(R.id.textView6);
        textView.setOnClickListener(v -> {
            Intent in = new Intent(TournamentActivity.this, TournamentActivity.class);
            startActivity(in);
        });

        tournamentRecycle = findViewById(R.id.tournamentRecyclerView);
        tournamentRecycle.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView image = findViewById(R.id.logoshow);
        image.setOnClickListener(v -> {
            Intent intent = new Intent(TournamentActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        edit = findViewById(R.id.searchHandle);
        Spinner mySpinner = findViewById(R.id.go_down);
        allItems = new ArrayList<>();
        allItems.add("⋮");
        allItems.addAll(Arrays.asList(getResources().getStringArray(R.array.tournament_spinner_items)));
        adapter = new ArrayAdapter<>(this, R.layout.tournament_spinner_item, allItems);
        adapter.setDropDownViewResource(R.layout.tournament_spinner_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(TournamentActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();

                    if (selectedItem.equals("Logout")) {
                        logoutUser();
                    } else if (selectedItem.equals("Profile")) {
                        Intent intent = new Intent(TournamentActivity.this, Update.class);
                        startActivity(intent);
                    } else if (selectedItem.equals("News")) {
                        Intent intent = new Intent(TournamentActivity.this, NewsActivity.class);
                        startActivity(intent);
                    } else if (selectedItem.equals("Tournament")) {
                        Intent intent = new Intent(TournamentActivity.this, TournamentActivity.class);
                        startActivity(intent);
                    } else if (selectedItem.equals("Sort-By-Name")) {
                        sortTournamentsByName(); // Call sort by name
                    } else if (selectedItem.equals("Sort-By-Price")) {
                        sortTournamentsByPrice(true); // Call sort by price ascending
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        tournamentList = new ArrayList<>();
        tournamentAdapter = new TournamentAdapter(tournamentList);
        tournamentRecycle.setAdapter(tournamentAdapter);

        db = FirebaseFirestore.getInstance();

        loadTournamentsFromFirestore();

        edit.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                String query = editable.toString();
                if (query.isEmpty()) {
                    tournamentAdapter.updateList(tournamentList);
                } else {
                    filterTournaments(query);
                }
            }
        });
    }

    private void loadTournamentsFromFirestore() {
        db.collection("tournaments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains("name") && document.contains("address") &&
                                    document.contains("contact") && document.contains("email") &&
                                    document.contains("price") && document.contains("endDate") &&
                                    document.contains("image")) {

                                String name = document.getString("name");
                                String address = document.getString("address");
                                String contact = document.getString("contact");
                                String email = document.getString("email");
                                Long priceLong = document.getLong("price");
                                if (priceLong != null) {
                                    int price = priceLong.intValue();
                                    String endDate = document.getString("endDate");
                                    String imageUrl = document.getString("image");

                                    Tournament tournament = new Tournament(name, address, contact, email, price, endDate, imageUrl);
                                    tournamentList.add(tournament);
                                }
                            }
                        }
                        Collections.sort(tournamentList, (t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
                        tournamentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TournamentActivity.this, "Error getting tournaments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void filterTournaments(String query) {
        List<Tournament> filteredList = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        int index = binarySearch(tournamentList, lowerQuery);
        if (index != -1) {
            int left = index;
            while (left >= 0) {
                Tournament tournament = tournamentList.get(left);
                if (tournament.getName().toLowerCase().contains(lowerQuery) || tournament.getAddress().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(tournament);
                } else {
                    break;
                }
                left--;
            }
            int right = index + 1;
            while (right < tournamentList.size()) {
                Tournament tournament = tournamentList.get(right);
                if (tournament.getName().toLowerCase().contains(lowerQuery) || tournament.getAddress().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(tournament);
                } else {
                    break;
                }
                right++;
            }
        }
        if (filteredList.isEmpty()) {
            for (Tournament tournament : tournamentList) {
                if (tournament.getName().toLowerCase().contains(lowerQuery) || tournament.getAddress().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(tournament);
                }
            }
        }
        tournamentAdapter.updateList(filteredList);
    }

    private int binarySearch(List<Tournament> list, String query) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Tournament mids = list.get(mid);
            String bname = mids.getName().toLowerCase();
            if (bname.contains(query)) {
                return mid;
            } else if (bname.compareTo(query) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
    private void sortTournamentsByName() {
        List<Tournament> sortedList = new ArrayList<>(tournamentList);
        int n = sortedList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedList.get(j).getName().compareTo(sortedList.get(j + 1).getName()) > 0) {
                    Tournament temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                }
            }
        }

        tournamentAdapter.updateList(sortedList);
    }

    private void sortTournamentsByPrice(boolean ascending) {
        List<Tournament> sortedList = new ArrayList<>(tournamentList);
        int n = sortedList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                boolean condition = ascending`
                        ? sortedList.get(j).getPrice() > sortedList.get(j + 1).getPrice()
                        : sortedList.get(j).getPrice() < sortedList.get(j + 1).getPrice();

                if (condition) {
                    Tournament temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                }
            }
        }

        tournamentAdapter.updateList(sortedList);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Logout user
        Intent intent = new Intent(TournamentActivity.this, HomeActivity.class); // Navigate to HomeActivity
        startActivity(intent);
        finish();
    }
}
