package com.example.myapplication.lab2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class QuadraticEquationActivity extends AppCompatActivity {

    private TextInputEditText editTextA, editTextB, editTextC;
    private TextView resultText;
    private Button solveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadratic_equation);

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
        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextC = findViewById(R.id.editTextC);
        resultText = findViewById(R.id.resultText);
        solveButton = findViewById(R.id.solveButton);
    }

    private void setupClickListeners() {
        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveQuadraticEquation();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void solveQuadraticEquation() {
        try {
            // Получаем коэффициенты
            double a = Double.parseDouble(editTextA.getText().toString());
            double b = Double.parseDouble(editTextB.getText().toString());
            double c = Double.parseDouble(editTextC.getText().toString());

            // Проверяем, что a не равно 0
            if (a == 0) {
                resultText.setText("Коэффициент 'a' не может быть равен 0!\nЭто не квадратное уравнение.");
                return;
            }

            // Вычисляем дискриминант
            double discriminant = b * b - 4 * a * c;

            if (discriminant > 0) {
                // Два различных действительных корня
                double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
                double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);
                
                String result = String.format("Два решения:\n" +
                        "x₁ = %.4f\n" +
                        "x₂ = %.4f\n\n" +
                        "Дискриминант: %.4f", x1, x2, discriminant);
                resultText.setText(result);
            } else if (discriminant == 0) {
                // Один корень
                double x = -b / (2 * a);
                String result = String.format("Одно решение:\n" +
                        "x = %.4f\n\n" +
                        "Дискриминант: %.4f", x, discriminant);
                resultText.setText(result);
            } else {
                // Нет действительных решений
                resultText.setText(getString(R.string.no_solution) + 
                        "\n\nДискриминант: " + String.format("%.4f", discriminant) + 
                        " < 0");
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Пожалуйста, введите корректные числа", Toast.LENGTH_SHORT).show();
            resultText.setText("Ошибка: Введите корректные числа во всех полях");
        } catch (Exception e) {
            Toast.makeText(this, "Произошла ошибка при вычислениях", Toast.LENGTH_SHORT).show();
            resultText.setText("Ошибка: " + e.getMessage());
        }
    }
}
