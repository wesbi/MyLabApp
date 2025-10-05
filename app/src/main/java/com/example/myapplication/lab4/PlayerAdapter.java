package com.example.myapplication.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public interface OnPresenceChangedListener {
        void onPresenceChanged(int position, boolean present);
        void onEditRequested(int position);
        void onDeleteRequested(int position);
    }

    private final List<Player> players;
    private final OnPresenceChangedListener listener;

    public PlayerAdapter(List<Player> players, OnPresenceChangedListener listener) {
        this.players = players;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.nameText.setText(player.getName());
        String team = player.getTeam();
        holder.teamText.setText(team == null ? "" : team);
        holder.presentCheck.setOnCheckedChangeListener(null);
        holder.presentCheck.setChecked(player.isPresent());
        holder.presentCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onPresenceChanged(holder.getAdapterPosition(), isChecked);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEditRequested(holder.getAdapterPosition());
        });
        // удаление через кнопки в списке отключено
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView teamText;
        CheckBox presentCheck;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            teamText = itemView.findViewById(R.id.textTeam);
            presentCheck = itemView.findViewById(R.id.checkPresent);
        }
    }
}


