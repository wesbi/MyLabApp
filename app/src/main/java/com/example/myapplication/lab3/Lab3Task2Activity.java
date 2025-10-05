package com.example.myapplication.lab3;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Lab3Task2Activity extends AppCompatActivity implements 
        FuelSelectionFragment.OnFuelSelectionListener, 
        ShiftSummaryFragment.OnShiftSummaryListener {

    private List<FuelSale> fuelSales;
    private FuelSelectionFragment fuelSelectionFragment;
    private ShiftSummaryFragment shiftSummaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_task2);

        // Добавляем стрелочку назад в ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        fuelSales = new ArrayList<>();
        
        // Создаем фрагменты
        fuelSelectionFragment = new FuelSelectionFragment();
        shiftSummaryFragment = new ShiftSummaryFragment();
        
        // Устанавливаем слушатели
        fuelSelectionFragment.setOnFuelSelectionListener(this);
        shiftSummaryFragment.setOnShiftSummaryListener(this);

        // Показываем фрагмент выбора топлива
        showFuelSelectionFragment();
    }

    private void showFuelSelectionFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fuelSelectionFragment);
        transaction.commit();
    }

    private void showShiftSummaryFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, shiftSummaryFragment);
        transaction.commit();
        
        // Обновляем данные в фрагменте сводки
        // Используем Handler для выполнения после завершения транзакции
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                shiftSummaryFragment.displayShiftSummary(fuelSales);
            }
        });
    }

    @Override
    public void onFuelSale(FuelSale fuelSale) {
        fuelSales.add(fuelSale);
        android.util.Log.d("GasStation", "Добавлена заправка: " + fuelSale.toString() + 
                ". Всего заправок: " + fuelSales.size());
    }

    @Override
    public void onNewOrder() {

    }

    @Override
    public void onFinishShift() {
        showShiftSummaryFragment();
    }

    @Override
    public void onNewShift() {
        android.util.Log.d("GasStation", "Создание новой смены. Очищаем " + fuelSales.size() + " заправок");
        fuelSales.clear();
        showFuelSelectionFragment();
        // Очищаем форму после небольшой задержки, чтобы фрагмент успел создаться
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (fuelSelectionFragment != null) {
                    fuelSelectionFragment.resetForm();
                    android.util.Log.d("GasStation", "Форма очищена");
                }
            }
        });
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
