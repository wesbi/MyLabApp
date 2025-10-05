package com.example.myapplication.lab4;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Lab4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Создаем простой TextView для отображения заголовка
        TextView textView = new TextView(this);
        textView.setText("Лабораторная работа №4\n\nВ разработке...");
        textView.setTextSize(18);
        textView.setPadding(32, 32, 32, 32);
        setContentView(textView);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
