package com.example.myapplication.lab4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lab4Activity extends AppCompatActivity implements PlayerAdapter.OnPresenceChangedListener {

    private SharedPreferences preferences;
    private final ArrayList<Player> players = new ArrayList<>();
    private PlayerAdapter adapter;

    private EditText editTextPlayer;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        editTextPlayer = findViewById(R.id.editTextPlayer);
        textResult = findViewById(R.id.textResult);
        RecyclerView recyclerView = findViewById(R.id.recyclerPlayers);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonDraw = findViewById(R.id.buttonDraw);
        Button buttonDeleteSelected = findViewById(R.id.buttonDeleteSelected);

        adapter = new PlayerAdapter(players, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            addPlayer();
            savePlayers();
        });
        buttonDraw.setOnClickListener(v -> drawTeams());
        buttonDeleteSelected.setOnClickListener(v -> deleteSelected());

        // Автозагрузка при старте
        loadPlayers();
    }

    private void addPlayer() {
        String name = editTextPlayer.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast("Введите имя игрока");
            return;
        }
        players.add(new Player(name));
        adapter.notifyItemInserted(players.size() - 1);
        editTextPlayer.setText("");
        savePlayers();
        showToast("Игрок добавлен");
    }

    private void savePlayers() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("N", players.size());
        for (int i = 0; i < players.size(); i++) {
            editor.putString("Person " + i, players.get(i).getName());
            editor.putBoolean("Present " + i, players.get(i).isPresent());
            editor.putString("Team " + i, players.get(i).getTeam());
        }
        editor.apply();
    }

    private void loadPlayers() {
        players.clear();
        int n = preferences.getInt("N", 0);
        for (int i = 0; i < n; i++) {
            String name = preferences.getString("Person " + i, "");
            boolean present = preferences.getBoolean("Present " + i, false);
            String team = preferences.getString("Team " + i, "");
            Player p = new Player(name);
            p.setPresent(present);
            p.setTeam(team);
            players.add(p);
        }
        adapter.notifyDataSetChanged();
        editTextPlayer.setText("");
    }

    private void drawTeams() {
        List<Player> presentPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.isPresent()) presentPlayers.add(p);
        }
        if (presentPlayers.isEmpty()) {
            showToast("Отметьте присутствующих игроков");
            return;
        }
        Collections.shuffle(presentPlayers);
        StringBuilder red = new StringBuilder();
        StringBuilder green = new StringBuilder();
        for (int i = 0; i < presentPlayers.size(); i++) {
            String name = presentPlayers.get(i).getName();
            if (i % 2 == 0) {
                if (red.length() > 0) red.append(", ");
                red.append(name);
                presentPlayers.get(i).setTeam("Красные");
            } else {
                if (green.length() > 0) green.append(", ");
                green.append(name);
                presentPlayers.get(i).setTeam("Зелёные");
            }
        }
        adapter.notifyDataSetChanged();
        textResult.setText("");
        savePlayers();
        showToast("Команды сформированы");
    }

    private void deleteSelected() {
        boolean changed = false;
        for (int i = players.size() - 1; i >= 0; i--) {
            if (players.get(i).isPresent()) {
                players.remove(i);
                changed = true;
            }
        }
        if (changed) {
            adapter.notifyDataSetChanged();
            savePlayers();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onPresenceChanged(int position, boolean present) {
        if (position >= 0 && position < players.size()) {
            players.get(position).setPresent(present);
            savePlayers();
        }
    }

    @Override
    public void onEditRequested(int position) {
        if (position < 0 || position >= players.size()) return;
        Player p = players.get(position);
        // простое переименование через поле ввода сверху: подставим имя и перезапишем при повторном добавлении
        editTextPlayer.setText(p.getName());
        editTextPlayer.setSelection(editTextPlayer.getText().length());
        // заменим действие кнопки Добавить на сохранение текущей строки
        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setText("Сохранить имя");
        buttonAdd.setOnClickListener(v -> {
            String newName = editTextPlayer.getText().toString().trim();
            if (!TextUtils.isEmpty(newName)) {
                p.setName(newName);
                adapter.notifyItemChanged(position);
                editTextPlayer.setText("");
                savePlayers();
                showToast("Имя обновлено");
            }
            // вернуть исходное поведение
            buttonAdd.setText("Добавить");
            buttonAdd.setOnClickListener(v2 -> addPlayer());
        });
    }

    @Override
    public void onDeleteRequested(int position) {
        if (position < 0 || position >= players.size()) return;
        players.remove(position);
        adapter.notifyItemRemoved(position);
        savePlayers();
        showToast("Игрок удалён");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
