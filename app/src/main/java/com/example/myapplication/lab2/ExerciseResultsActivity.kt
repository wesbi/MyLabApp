package com.example.myapplication.lab2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class ExerciseResultsActivity : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var percentageText: TextView
    private lateinit var messageText: TextView
    private lateinit var scoreProgressBar: ProgressBar
    private lateinit var restartButton: Button

    private var correctAnswers: Int = 0
    private var totalQuestions: Int = 20
    private var exerciseType: String? = null
    private var selectedNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_results)

        // Получаем данные из Intent
        correctAnswers = intent.getIntExtra("correct_answers", 0)
        totalQuestions = intent.getIntExtra("total_questions", 20)
        exerciseType = intent.getStringExtra("exercise_type")
        selectedNumber = intent.getIntExtra("selected_number", 0)

        // Добавляем стрелочку назад в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initViews()
        setupClickListeners()
        displayResults()
    }

    private fun initViews() {
        scoreText = findViewById(R.id.scoreText)
        percentageText = findViewById(R.id.percentageText)
        messageText = findViewById(R.id.messageText)
        scoreProgressBar = findViewById(R.id.scoreProgressBar)
        restartButton = findViewById(R.id.restartButton)
    }

    private fun setupClickListeners() {
        restartButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                restartExercise()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    @SuppressLint("StringFormatMatches")
    private fun displayResults() {
        val percentage = correctAnswers.toDouble() / totalQuestions * 100

        scoreText.text = String.format(getString(R.string.correct_answers_count), correctAnswers, totalQuestions)
        percentageText.text = String.format(getString(R.string.percentage), percentage)

        scoreProgressBar.progress = percentage.toInt()

        val message = when {
            percentage >= 90 -> "Отличная работа!"
            percentage >= 70 -> "Хорошая работа!"
            percentage >= 50 -> "Неплохо, но можно лучше!"
            else -> "Попробуйте еще раз!"
        }

        messageText.text = message
    }

    private fun restartExercise() {
        val intent = if ("all_numbers" == exerciseType) {
            Intent(this, AllNumbersExerciseActivity::class.java)
        } else {
            Intent(this, SelectiveExerciseActivity::class.java).apply {
                putExtra("selected_number", selectedNumber)
            }
        }
        startActivity(intent)
        finish()
    }
}


