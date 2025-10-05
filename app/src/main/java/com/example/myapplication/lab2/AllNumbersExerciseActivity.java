package com.example.myapplication.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class AllNumbersExerciseActivity extends AppCompatActivity {

    private TextView progressText, questionText, resultText;
    private TextInputEditText editTextAnswer;
    private Button checkButton, nextButton;
    private ProgressBar progressBar;
    private CardView resultCard;

    private Random random;
    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private int firstNumber, secondNumber, correctAnswer;
    private boolean isAnswerChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_numbers_exercise);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        random = new Random();
        initViews();
        setupClickListeners();
        generateNewQuestion();
    }

    private void initViews() {
        progressText = findViewById(R.id.progressText);
        questionText = findViewById(R.id.questionText);
        resultText = findViewById(R.id.resultText);
        editTextAnswer = findViewById(R.id.editTextAnswer);
        checkButton = findViewById(R.id.checkButton);
        nextButton = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressBar);
        resultCard = findViewById(R.id.resultCard);
    }

    private void setupClickListeners() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion < 20) {
                    generateNewQuestion();
                } else {
                    finishExercise();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void generateNewQuestion() {
        currentQuestion++;
        firstNumber = random.nextInt(8) + 2; // 2-9
        secondNumber = random.nextInt(8) + 2; // 2-9
        correctAnswer = firstNumber * secondNumber;

        questionText.setText(String.format(getString(R.string.question_format), firstNumber, secondNumber));
        editTextAnswer.setText("");
        editTextAnswer.setEnabled(true);
        checkButton.setEnabled(true);
        resultCard.setVisibility(View.GONE);
        isAnswerChecked = false; // Сбрасываем флаг проверки ответа

        updateProgress();
    }

    private void checkAnswer() {
        if (isAnswerChecked) return;

        String answerText = editTextAnswer.getText().toString().trim();
        if (answerText.isEmpty()) {
            editTextAnswer.setError("Введите ответ");
            return;
        }

        try {
            int userAnswer = Integer.parseInt(answerText);
            isAnswerChecked = true;

            if (userAnswer == correctAnswer) {
                correctAnswers++;
                resultText.setText(getString(R.string.correct_answer));
                resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                resultText.setText(getString(R.string.incorrect_answer) + 
                    "\nПравильный ответ: " + correctAnswer);
                resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            editTextAnswer.setEnabled(false);
            checkButton.setEnabled(false);
            resultCard.setVisibility(View.VISIBLE);

            if (currentQuestion == 20) {
                nextButton.setText(getString(R.string.finish_test));
            }

        } catch (NumberFormatException e) {
            editTextAnswer.setError("Введите корректное число");
        }
    }

    private void updateProgress() {
        progressText.setText("Вопрос " + currentQuestion + " из 20");
        progressBar.setProgress(currentQuestion);
    }

    private void finishExercise() {
        Intent intent = new Intent(this, ExerciseResultsActivity.class);
        intent.putExtra("correct_answers", correctAnswers);
        intent.putExtra("total_questions", 20);
        intent.putExtra("exercise_type", "all_numbers");
        startActivity(intent);
        finish();
    }
}
