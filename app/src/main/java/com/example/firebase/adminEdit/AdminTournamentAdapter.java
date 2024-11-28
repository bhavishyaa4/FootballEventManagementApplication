package com.example.firebase.adminEdit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebase.R;
import com.example.firebase.tournamentListProcess.Tournament;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminTournamentAdapter extends RecyclerView.Adapter<AdminTournamentAdapter.TournamentViewHolder> {

    private List<Tournament> tournamentList;
    private Context context;
    private FirebaseFirestore db;

    public AdminTournamentAdapter(List<Tournament> tournamentList, Context context) {
        this.tournamentList = tournamentList;
        this.context = context;
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_tournament, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        Tournament tournament = tournamentList.get(position);

        // Set tournament data in views
        holder.tournamentName.setText(tournament.getName());
        holder.tournamentAddress.setText(tournament.getAddress());
        holder.tournamentContact.setText(tournament.getContact());
        holder.tournamentEmail.setText(tournament.getEmail());
        holder.tournamentPrice.setText("Price: " + tournament.getPrice());
        holder.tournamentEndDate.setText("End Date: " + tournament.getEndDate());

        // Load image using Glide
        String imageUrl = tournament.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.chelsea) // Placeholder image
                    .error(R.drawable.chelsea) // Error image
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (e != null) {
                                Log.e("GlideError", "Image load failed: " + e.getMessage());
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.tournamentImagee);
        } else {
            Log.e("TournamentAdapter", "Image URL is null or empty for tournament: " + tournament.getName());
        }

        // Edit button click event
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTournamentsActivity.class);
            intent.putExtra("TOURNAMENT_ID", tournament.getId());  // Pass tournament ID
            context.startActivity(intent);
        });

        // Delete button click event
        holder.deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog before deletion
            new AlertDialog.Builder(context)
                    .setTitle("Delete Tournament")
                    .setMessage("Are you sure you want to delete this tournament?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        deleteTournament(tournament.getId(), position); // Call delete method
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    // Method to delete tournament from Firebase and update RecyclerView
    private void deleteTournament(String tournamentId, int position) {
        db.collection("tournaments").document(tournamentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove tournament from the list and notify the adapter
                    tournamentList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, tournamentList.size());
                    Toast.makeText(context, "Tournament deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteError", "Error deleting tournament", e);
                    Toast.makeText(context, "Error deleting tournament", Toast.LENGTH_SHORT).show();
                });
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {

        TextView tournamentName, tournamentAddress, tournamentContact, tournamentEmail, tournamentPrice, tournamentEndDate;
        ImageView tournamentImagee;
        Button editButton, deleteButton;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            tournamentName = itemView.findViewById(R.id.tournamentName);
            tournamentAddress = itemView.findViewById(R.id.tournamentAddress);
            tournamentContact = itemView.findViewById(R.id.tournamentContact);
            tournamentEmail = itemView.findViewById(R.id.tournamentEmail);
            tournamentPrice = itemView.findViewById(R.id.tournamentPrice);
            tournamentEndDate = itemView.findViewById(R.id.tournamentEndDate);
            tournamentImagee = itemView.findViewById(R.id.tournamentImaged);
            editButton = itemView.findViewById(R.id.editbtn);
            deleteButton = itemView.findViewById(R.id.delbtn); // Initialize delete button
        }
    }
}
