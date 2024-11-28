package com.example.firebase.adminEdit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.firebase.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditTournamentsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName, editAddress, editContact, editEmail, editPrice, editEndDate;
    private ImageView tournamentImageView;
    private Button changeImageButton, saveButton;
    private FirebaseFirestore db;
    private String tournamentId;
    private String imageUrl;  // To store the new image URL if updated

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tournaments);

        db = FirebaseFirestore.getInstance();

        editName = findViewById(R.id.editTournamentName);
        editAddress = findViewById(R.id.editTournamentAddress);
        editContact = findViewById(R.id.editTournamentContact);
        editEmail = findViewById(R.id.editTournamentEmail);
        editPrice = findViewById(R.id.editTournamentPrice);
        editEndDate = findViewById(R.id.editTournamentEndDate);
        tournamentImageView = findViewById(R.id.editTournamentImageView);
        changeImageButton = findViewById(R.id.changeImageButton);
        saveButton = findViewById(R.id.saveEditButton);

        Intent intent = getIntent();
        tournamentId = intent.getStringExtra("TOURNAMENT_ID");
        loadTournamentDetails(tournamentId);

        changeImageButton.setOnClickListener(v -> selectImage());
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void loadTournamentDetails(String id) {
        db.collection("tournaments").document(id).get().addOnSuccessListener(document -> {
            if (document.exists()) {
                editName.setText(document.getString("name"));
                editAddress.setText(document.getString("address"));
                editContact.setText(document.getString("contact"));
                editEmail.setText(document.getString("email"));
                editPrice.setText(String.valueOf(document.getLong("price")));
                editEndDate.setText(document.getString("endDate"));
                imageUrl = document.getString("image");

                // Load image using Glide
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(this).load(imageUrl).into(tournamentImageView);
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("update_tournament_images/" + tournamentId + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();  // Store the new image URL
                    Glide.with(this).load(imageUrl).into(tournamentImageView);  // Update ImageView
                    Toast.makeText(this, "Image updated successfully", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

    private void saveChanges() {
        String name = editName.getText().toString();
        String address = editAddress.getText().toString();
        String contact = editContact.getText().toString();
        String email = editEmail.getText().toString();
        int price = Integer.parseInt(editPrice.getText().toString());
        String endDate = editEndDate.getText().toString();

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", name);
        updateData.put("address", address);
        updateData.put("contact", contact);
        updateData.put("email", email);
        updateData.put("price", price);
        updateData.put("endDate", endDate);

        if (imageUrl != null) {
            updateData.put("image", imageUrl);  // Add image URL if updated
        }

        db.collection("tournaments").document(tournamentId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Tournament updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating tournament", Toast.LENGTH_SHORT).show());
    }
}
