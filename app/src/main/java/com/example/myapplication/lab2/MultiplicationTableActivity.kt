package com.example.myapplication.lab2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText

class MultiplicationTableActivity : AppCompatActivity() {

    private lateinit var allNumbersButton: Button
    private lateinit var selectiveButton: Button
    private lateinit var editTextNumber: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplication_table)

        // Добавляем стрелочку назад в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        allNumbersButton = findViewById(R.id.allNumbersButton)
        selectiveButton = findViewById(R.id.selectiveButton)
        editTextNumber = findViewById(R.id.editTextNumber)
    }

    private fun setupClickListeners() {
        allNumbersButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startAllNumbersExercise()
            }
        })

        selectiveButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startSelectiveExercise()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun startAllNumbersExercise() {
        val intent = Intent(this, AllNumbersExerciseActivity::class.java)
        startActivity(intent)
    }

    private fun startSelectiveExercise() {
        val numberText = editTextNumber.text.toString().trim()

        if (numberText.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val number = numberText.toInt()
            if (number < 2 || number > 9) {
                Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show()
                return
            }

            val intent = Intent(this, SelectiveExerciseActivity::class.java)
            intent.putExtra("selected_number", number)
            startActivity(intent)
        } catch (_: NumberFormatException) {
            Toast.makeText(this, getString(R.string.invalid_number), Toast.LENGTH_SHORT).show()
        }
    }
}


