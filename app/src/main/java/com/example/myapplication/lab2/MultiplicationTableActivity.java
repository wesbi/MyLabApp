package com.example.myapplication.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class MultiplicationTableActivity extends AppCompatActivity {

    private Button allNumbersButton, selectiveButton;
    private TextInputEditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_table);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Инициализация элементов интерфейса
        initViews();

        // Настройка обработчиков событий
        setupClickListeners();
    }

    private void initViews() {
        allNumbersButton = findViewById(R.id.allNumbersButton);
        selectiveButton = findViewById(R.id.selectiveButton);
        editTextNumber = findViewById(R.id.editTextNumber);
    }

    private void setupClickListeners() {
        allNumbersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAllNumbersExercise();
            }
        });

        selectiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectiveExercise();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void startAllNumbersExercise() {
        Intent intent = new Intent(this, AllNumbersExerciseActivity.class);
        startActivity(intent);
    }

    private void startSelectiveExercise() {
        String numberText = editTextNumber.getText().toString().trim();
        
        if (numberText.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int number = Integer.parseInt(numberText);
            if (number < 2 || number > 9) {
                Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, SelectiveExerciseActivity.class);
            intent.putExtra("selected_number", number);
            startActivity(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show();
        }
    }
}
