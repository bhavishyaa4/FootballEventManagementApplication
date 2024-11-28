package com.example.firebase.Adapter;

import android.content.DialogInterface;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.R;
import com.example.firebase.ModelClass.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Users> userList;
    private FirebaseFirestore db;
    private Context context;

    public UserAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.db = FirebaseFirestore.getInstance();  // Initialize Firestore
        this.context = context;  // Save context for dialog creation
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);

        // Display user details
        holder.nameTextView.setText("Name: " + user.getName());
        holder.addressTextView.setText("Address: " + user.getAddress());
        holder.emailTextView.setText("Email: " + user.getEmail());
        holder.phoneTextView.setText("Phone: " + user.getPhone());
        holder.genderTextView.setText("Gender: " + user.getGender());

        // Display registered tournaments
        List<String> tournaments = user.getRegisteredTournaments();
        if (tournaments != null && !tournaments.isEmpty()) {
            StringBuilder tournamentList = new StringBuilder("Registered Tournaments:\n");
            for (String tournament : tournaments) {
                tournamentList.append(tournament).append("\n");
            }
            holder.tournamentsTextView.setText(tournamentList.toString());
        } else {
            holder.tournamentsTextView.setText("No tournaments registered.");
        }

        // Handle the delete button click
        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(user.getEmail(), position);  // Show confirmation dialog
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Show confirmation dialog before deletion
    private void showDeleteConfirmationDialog(String userEmail, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteUserFromFirestore(userEmail, position);  // Delete the user if confirmed
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();  // Dismiss the dialog if cancelled
                })
                .show();
    }

    // Delete user from Firestore and update the list
    // Delete user from Firestore and update the list
    private void deleteUserFromFirestore(String userEmail, int position) {
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Get the document ID of the user to be deleted
                        String documentId = task.getResult().getDocuments().get(0).getId();
                        // Delete the user document from Firestore
                        db.collection("users").document(documentId)
                                .delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        // Remove from the local list and notify RecyclerView
                                        userList.remove(position);
                                        notifyItemRemoved(position);

                                        // Show a Toast message for successful deletion
                                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();  // Add this line
                                    } else {
                                        // Show a Toast message in case of failure
                                        Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressTextView, emailTextView, phoneTextView, genderTextView, tournamentsTextView;
        Button deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            tournamentsTextView = itemView.findViewById(R.id.tournamentsTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
