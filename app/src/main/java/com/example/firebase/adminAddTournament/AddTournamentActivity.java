package com.example.firebase.adminAddTournament;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase.R;
import com.example.firebase.adminLogReg.AdminHomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddTournamentActivity extends AppCompatActivity {

    private ImageView tournamentImageView;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private ImageView imageView;

    private EditText nameInput, addressInput, contactInput, emailInput, priceInput, endDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);

        tournamentImageView = findViewById(R.id.tournamentImageView);
        Button chooseImageButton = findViewById(R.id.choose_image_button);
        Button addTournamentButton = findViewById(R.id.add_tournament_button);

        nameInput = findViewById(R.id.name);
        addressInput = findViewById(R.id.address);
        contactInput = findViewById(R.id.contact);
        emailInput = findViewById(R.id.email);
        priceInput = findViewById(R.id.price);
        endDateInput = findViewById(R.id.endDate);
        imageView = findViewById(R.id.logo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTournamentActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();

        chooseImageButton.setOnClickListener(v -> openImagePicker());
        addTournamentButton.setOnClickListener(v -> uploadTournamentDetails());
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Tournament Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            tournamentImageView.setImageURI(imageUri);
        }
    }

    private void uploadTournamentDetails() {
        if (imageUri != null) {
            StorageReference ref = storageReference.child("tournament_images/" + System.currentTimeMillis() + ".jpg");
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveTournamentToFirestore(imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        Log.e("UploadError", "Image upload failed: " + e.getMessage());
                        Toast.makeText(AddTournamentActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            saveTournamentToFirestore(null);
        }
    }

    private void saveTournamentToFirestore(String imageUrl) {
        String name = nameInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String contact = contactInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String endDate = endDateInput.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || priceStr.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> tournament = new HashMap<>();
        tournament.put("name", name);
        tournament.put("address", address);
        tournament.put("contact", contact);
        tournament.put("email",email);
        tournament.put("price", price);
        tournament.put("endDate", endDate);
        tournament.put("image", imageUrl);

        firestore.collection("tournaments")
                .add(tournament)
                .addOnSuccessListener(documentReference -> Toast.makeText(AddTournamentActivity.this, "Tournament added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddTournamentActivity.this, "Error adding tournament", Toast.LENGTH_SHORT).show());
    }
}
