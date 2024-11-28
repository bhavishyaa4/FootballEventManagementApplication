package com.example.firebase.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.R;
import com.example.firebase.tournamentListProcess.Tournament;

import java.util.List;

public class UserTournamentAdapter extends RecyclerView.Adapter<UserTournamentAdapter.UserTournamentViewHolder> {

    private List<Tournament> tournamentList;

    public UserTournamentAdapter(List<Tournament> tournamentList) {
        this.tournamentList = tournamentList;
    }

    @NonNull
    @Override
    public UserTournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tournament, parent, false);
        return new UserTournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTournamentViewHolder holder, int position) {
        Tournament tournament = tournamentList.get(position);
        holder.tournamentNameTextView.setText(tournament.getName());
    }

    @Override
    public int getItemCount() {
        return tournamentList.size();
    }

    public static class UserTournamentViewHolder extends RecyclerView.ViewHolder {
        TextView tournamentNameTextView;

        public UserTournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            tournamentNameTextView = itemView.findViewById(R.id.tournamentNameTextView);
        }
    }
}
