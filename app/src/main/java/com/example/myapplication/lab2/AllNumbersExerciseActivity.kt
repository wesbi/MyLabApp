package com.example.myapplication.lab2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Random

class AllNumbersExerciseActivity : AppCompatActivity() {

    private lateinit var progressText: TextView
    private lateinit var questionText: TextView
    private lateinit var resultText: TextView
    private lateinit var editTextAnswer: TextInputEditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var resultCard: CardView

    private lateinit var random: Random
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0
    private var firstNumber: Int = 0
    private var secondNumber: Int = 0
    private var correctAnswer: Int = 0
    private var isAnswerChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_numbers_exercise)

        // Добавляем стрелочку назад в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        random = Random()
        initViews()
        setupClickListeners()
        generateNewQuestion()
    }

    private fun initViews() {
        progressText = findViewById(R.id.progressText)
        questionText = findViewById(R.id.questionText)
        resultText = findViewById(R.id.resultText)
        editTextAnswer = findViewById(R.id.editTextAnswer)
        checkButton = findViewById(R.id.checkButton)
        nextButton = findViewById(R.id.nextButton)
        progressBar = findViewById(R.id.progressBar)
        resultCard = findViewById(R.id.resultCard)
    }

    private fun setupClickListeners() {
        checkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                checkAnswer()
            }
        })

        nextButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (currentQuestion < 20) {
                    generateNewQuestion()
                } else {
                    finishExercise()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun generateNewQuestion() {
        currentQuestion++
        firstNumber = random.nextInt(8) + 2 // 2-9
        secondNumber = random.nextInt(8) + 2 // 2-9
        correctAnswer = firstNumber * secondNumber

        questionText.text = String.format(getString(R.string.question_format), firstNumber, secondNumber)
        editTextAnswer.setText("")
        editTextAnswer.isEnabled = true
        checkButton.isEnabled = true
        resultCard.visibility = View.GONE
        isAnswerChecked = false

        updateProgress()
    }

    private fun checkAnswer() {
        if (isAnswerChecked) return

        val answerText = editTextAnswer.text.toString().trim()
        if (answerText.isEmpty()) {
            editTextAnswer.error = "Введите ответ"
            return
        }

        try {
            val userAnswer = answerText.toInt()
            isAnswerChecked = true

            if (userAnswer == correctAnswer) {
                correctAnswers++
                resultText.text = getString(R.string.correct_answer)
                resultText.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            } else {
                resultText.text = getString(R.string.incorrect_answer) +
                    "\nПравильный ответ: " + correctAnswer
                resultText.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }

            editTextAnswer.isEnabled = false
            checkButton.isEnabled = false
            resultCard.visibility = View.VISIBLE

            if (currentQuestion == 20) {
                nextButton.text = getString(R.string.finish_test)
            }
        } catch (_: NumberFormatException) {
            editTextAnswer.error = "Введите корректное число"
        }
    }

    private fun updateProgress() {
        progressText.text = "Вопрос $currentQuestion из 20"
        progressBar.progress = currentQuestion
    }

    private fun finishExercise() {
        val intent = Intent(this, ExerciseResultsActivity::class.java)
        intent.putExtra("correct_answers", correctAnswers)
        intent.putExtra("total_questions", 20)
        intent.putExtra("exercise_type", "all_numbers")
        startActivity(intent)
        finish()
    }
}


