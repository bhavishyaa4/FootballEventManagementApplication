package com.example.firebase.tournamentListProcess;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.firebase.R;
import com.example.firebase.RegistrationActivity;

import java.util.ArrayList;
import java.util.List;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {

    private List<Tournament> tournamentList;

    public TournamentAdapter(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tournament, parent, false);
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        Tournament tournament = tournamentList.get(position);
        holder.tournamentName.setText(tournament.getName());
        holder.tournamentAddress.setText("Address: " + tournament.getAddress());
        holder.tournamentContact.setText("Contact: " + tournament.getContact());
        holder.tournamentEmail.setText("Email: " + tournament.getEmail());
        holder.tournamentPrice.setText("Price: Rs " + tournament.getPrice() + "/- Only");
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
                    .into(holder.tournamentImage);
        } else {
            Log.e("TournamentAdapter", "Image URL is null or empty for tournament: " + tournament.getName());
        }

        // Set onClickListener for the Register button
        holder.regButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
            intent.putExtra("TOURNAMENT_NAME", tournament.getName());
            intent.putExtra("TOURNAMENT_ADDRESS", tournament.getAddress());
            intent.putExtra("TOURNAMENT_CONTACT", tournament.getContact());
            intent.putExtra("TOURNAMENT_EMAIL", tournament.getEmail());
            intent.putExtra("TOURNAMENT_PRICE", tournament.getPrice());
            intent.putExtra("TOURNAMENT_END_DATE", tournament.getEndDate());
            intent.putExtra("TOURNAMENT_IMAGE_URL", tournament.getImageUrl());
            v.getContext().startActivity(intent);
        });

        // Set image dimensions and scale type
        holder.tournamentImage.getLayoutParams().width = 400;
        holder.tournamentImage.getLayoutParams().height = 400;
        holder.tournamentImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    public static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TextView tournamentName, tournamentAddress, tournamentContact, tournamentEmail, tournamentPrice, tournamentEndDate;
        ImageView tournamentImage;
        Button regButton;

        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            tournamentName = itemView.findViewById(R.id.tournamentName);
            tournamentAddress = itemView.findViewById(R.id.tournamentAddress);
            tournamentContact = itemView.findViewById(R.id.tournamentContact);
            tournamentEmail = itemView.findViewById(R.id.tournamentEmail);
            tournamentPrice = itemView.findViewById(R.id.tournamentPrice);
            tournamentEndDate = itemView.findViewById(R.id.tournamentEndDate);
            tournamentImage = itemView.findViewById(R.id.tournamentImage);
            regButton = itemView.findViewById(R.id.regbtn);
        }
    }
    public void updateList(List<Tournament> newList) {
        tournamentList = new ArrayList<>();
        tournamentList.addAll(newList);
        notifyDataSetChanged();
    }


}
