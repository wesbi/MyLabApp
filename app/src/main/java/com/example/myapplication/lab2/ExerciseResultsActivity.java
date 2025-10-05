package com.example.myapplication.lab2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class ExerciseResultsActivity extends AppCompatActivity {

    private TextView scoreText, percentageText, messageText;
    private ProgressBar scoreProgressBar;
    private Button restartButton;

    private int correctAnswers;
    private int totalQuestions;
    private String exerciseType;
    private int selectedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_results);

        // Получаем данные из Intent
        correctAnswers = getIntent().getIntExtra("correct_answers", 0);
        totalQuestions = getIntent().getIntExtra("total_questions", 20);
        exerciseType = getIntent().getStringExtra("exercise_type");
        selectedNumber = getIntent().getIntExtra("selected_number", 0);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initViews();
        setupClickListeners();
        displayResults();
    }

    private void initViews() {
        scoreText = findViewById(R.id.scoreText);
        percentageText = findViewById(R.id.percentageText);
        messageText = findViewById(R.id.messageText);
        scoreProgressBar = findViewById(R.id.scoreProgressBar);
        restartButton = findViewById(R.id.restartButton);
    }

    private void setupClickListeners() {
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartExercise();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint("StringFormatMatches")
    private void displayResults() {
        double percentage = (double) correctAnswers / totalQuestions * 100;
        
        scoreText.setText(String.format(getString(R.string.correct_answers_count), correctAnswers, totalQuestions));
        percentageText.setText(String.format(getString(R.string.percentage), percentage));
        
        scoreProgressBar.setProgress((int) percentage);
        
        // Определяем сообщение в зависимости от результата
        String message;
        if (percentage >= 90) {
            message = "Отличная работа!";
        } else if (percentage >= 70) {
            message = "Хорошая работа!";
        } else if (percentage >= 50) {
            message = "Неплохо, но можно лучше!";
        } else {
            message = "Попробуйте еще раз!";
        }
        
        messageText.setText(message);
    }

    private void restartExercise() {
        Intent intent;
        if ("all_numbers".equals(exerciseType)) {
            intent = new Intent(this, AllNumbersExerciseActivity.class);
        } else {
            intent = new Intent(this, SelectiveExerciseActivity.class);
            intent.putExtra("selected_number", selectedNumber);
        }
        startActivity(intent);
        finish();
    }
}
