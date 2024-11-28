package com.example.firebase.adminTournamentList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.AdminUpdateActivity;
import com.example.firebase.adminLogReg.AdminHomeActivity;
import com.example.firebase.R;
import com.example.firebase.tournamentListProcess.Tournament;
import com.example.firebase.tournamentListProcess.TournamentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminTournamentLIstAcitivty extends AppCompatActivity {
    private RecyclerView tournamentRecycle;
    private TournamentAdapter tournamentAdapter;
    private List<Tournament> tournamentList;
    private ArrayAdapter<String> adapter;
    private List<String> allItems;
    private FirebaseFirestore db;
    private EditText edits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_tournament_list_acitivty);

        tournamentRecycle = findViewById(R.id.tournamentRecyclerView);
        tournamentRecycle.setLayoutManager(new GridLayoutManager(this, 2));

        // Set logo click listener
        ImageView image = findViewById(R.id.adminLogo);
        image.setOnClickListener(v -> {
            Intent intent = new Intent(AdminTournamentLIstAcitivty.this, AdminHomeActivity.class);
            startActivity(intent);
        });

        TextView textView = findViewById(R.id.textView6);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTournamentLIstAcitivty.this,AdminTournamentLIstAcitivty.class);
                startActivity(intent);
            }
        });

        // Initialize search EditText
        edits = findViewById(R.id.handles);

        // Initialize Spinner and populate items
        Spinner mySpinner = findViewById(R.id.admin_go);
        allItems = new ArrayList<>();
        allItems.add("⋮");
        allItems.addAll(Arrays.asList(getResources().getStringArray(R.array.tournament_spinner_items)));
        adapter = new ArrayAdapter<>(this, R.layout.tournament_spinner_item, allItems);
        adapter.setDropDownViewResource(R.layout.tournament_spinner_item);
        mySpinner.setAdapter(adapter);

        // Spinner Item selection listener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    Toast.makeText(AdminTournamentLIstAcitivty.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();

                    if (selectedItem.equals("Logout")) {
                        logoutUser();
                    } else if(selectedItem.equals("Profile")){
                        Intent intent = new Intent(AdminTournamentLIstAcitivty.this, AdminUpdateActivity.class);
                        startActivity(intent);
                    } else if(selectedItem.equals("Tournament")){
                        Intent intent = new Intent(AdminTournamentLIstAcitivty.this, AdminTournamentLIstAcitivty.class);
                        startActivity(intent);
                    }
                    else if (selectedItem.equals("Sort-By-Name")) {
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


        // Initialize tournament list and adapter
        tournamentList = new ArrayList<>();
        tournamentAdapter = new TournamentAdapter(tournamentList);
        tournamentRecycle.setAdapter(tournamentAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load tournaments from Firestore
        loadTournamentsFromFirestore();

        edits.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {
                String query = editable.toString();
                if (query.isEmpty()) {
                    // If search query is empty, show all tournaments
                    tournamentAdapter.updateList(tournamentList);
                } else {
                    // Filter tournaments by name and address
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
                                } else {
                                    Log.e("TournamentError", "Price is null for document: " + document.getId());
                                }
                            } else {
                                Log.e("TournamentError", "Missing fields in Firestore document: " + document.getId());
                            }
                        }
                        tournamentAdapter.notifyDataSetChanged();
                    } else {
                    }
                });
    }
//    private void filterTournaments(String query) {
//        List<Tournament> filteredList = new ArrayList<>();
//        for (Tournament tournament : tournamentList) {
//            if (tournament.getName().toLowerCase().contains(query.toLowerCase()) ||
//                    tournament.getAddress().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(tournament);
//            }
//        }
//    }
private void filterTournaments(String query) {
    List<Tournament> filteredList = new ArrayList<>();

    // Convert query to lowercase for case-insensitive matching
    String lowerQuery = query.toLowerCase();

    // Perform binary search to find the closest match by name
    int index = binarySearch(tournamentList, lowerQuery);

    // Expand left and right from the found index to find all matches by name or address
    if (index != -1) {
        // Search left (for name or address match)
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

        // Search right (for name or address match)
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

    // If no matches found in binary search, manually filter the list
    if (filteredList.isEmpty()) {
        for (Tournament tournament : tournamentList) {
            if (tournament.getName().toLowerCase().contains(lowerQuery) || tournament.getAddress().toLowerCase().contains(lowerQuery)) {
                filteredList.add(tournament);
            }
        }
    }

    // Update the adapter with the filtered list
    tournamentAdapter.updateList(filteredList);
}

    // Binary search algorithm on sorted tournament list by name
    private int binarySearch(List<Tournament> list, String query) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Tournament mids = list.get(mid);
            String bname = mids.getName().toLowerCase();

            // Compare the query with the tournament name lexicographically
            if (bname.contains(query)) {
                return mid; // Found the closest match
            } else if (bname.compareTo(query) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1; // Return -1 if no close match is found
    }

    // Sort tournaments by Name (Alphabetically)
    private void sortTournamentsByName() {
        List<Tournament> sortedList = new ArrayList<>(tournamentList);
        Collections.sort(sortedList, new Comparator<Tournament>() {
            @Override
            public int compare(Tournament t1, Tournament t2) {
                return t1.getName().compareTo(t2.getName());  // Sorting alphabetically
            }
        });
        tournamentAdapter.updateList(sortedList);
    }

    // Sort tournaments by Price (Ascending)
    private void sortTournamentsByPrice(boolean ascending) {
        List<Tournament> sortedList = new ArrayList<>(tournamentList);
        Collections.sort(sortedList, new Comparator<Tournament>() {
            @Override
            public int compare(Tournament t1, Tournament t2) {
                if (ascending) {
                    return Integer.compare(t1.getPrice(), t2.getPrice()); // Ascending order
                }
                return 0; // Default to no sorting if not ascending (since no descending option)
            }
        });
        tournamentAdapter.updateList(sortedList);
    }


    private void logoutUser() {
        FirebaseAuth.getInstance().signOut(); // Logout user
        Intent intent = new Intent(AdminTournamentLIstAcitivty.this, AdminHomeActivity.class); // Navigate to HomeActivity
        startActivity(intent);
        finish(); // Finish the current activity
        Toast.makeText(AdminTournamentLIstAcitivty.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

}