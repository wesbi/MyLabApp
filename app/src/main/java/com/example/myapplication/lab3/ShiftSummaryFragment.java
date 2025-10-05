package com.example.myapplication.lab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftSummaryFragment extends Fragment {

    private TextView textViewShiftSummary;
    private Button buttonNewShift;
    private Button buttonBack;

    public interface OnShiftSummaryListener {
        void onNewShift();
        void onBack();
    }

    private OnShiftSummaryListener listener;

    public void setOnShiftSummaryListener(OnShiftSummaryListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift_summary, container, false);

        textViewShiftSummary = view.findViewById(R.id.textViewShiftSummary);
        buttonNewShift = view.findViewById(R.id.buttonNewShift);
        buttonBack = view.findViewById(R.id.buttonBack);

        buttonNewShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNewShift();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBack();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Устанавливаем начальный текст
        if (textViewShiftSummary != null) {
            textViewShiftSummary.setText("Загрузка отчета...");
        }
    }

    public void displayShiftSummary(List<FuelSale> fuelSales) {
        // Проверяем, что TextView инициализирован и view создан
        if (textViewShiftSummary == null || getView() == null) {
            return;
        }
        
        if (fuelSales == null || fuelSales.isEmpty()) {
            textViewShiftSummary.setText("══════════════════════════\n" +
                    "ОТЧЕТ ПО СМЕНЕ\n" +
                    "══════════════════════════\n\n" +
                    "За смену не было продаж топлива\n\n" +
                    "══════════════════════════\n" +
                    "ОБЩАЯ ВЫРУЧКА: 0.00 руб\n" +
                    "══════════════════════════");
            return;
        }

        // Группируем продажи по видам топлива
        Map<FuelType, Double> fuelQuantities = new HashMap<>();
        Map<FuelType, Double> fuelRevenues = new HashMap<>();
        double totalRevenue = 0;
        int totalSales = fuelSales.size();

        for (FuelSale sale : fuelSales) {
            FuelType fuelType = sale.getFuelType();
            double quantity = sale.getQuantity();
            double revenue = sale.getTotalPrice();

            fuelQuantities.put(fuelType, fuelQuantities.getOrDefault(fuelType, 0.0) + quantity);
            fuelRevenues.put(fuelType, fuelRevenues.getOrDefault(fuelType, 0.0) + revenue);
            totalRevenue += revenue;
        }

        // Формируем отчет
        StringBuilder summary = new StringBuilder();
        summary.append("══════════════════════════\n");
        summary.append("ОТЧЕТ ПО СМЕНЕ\n");
        summary.append("══════════════════════════\n\n");
        summary.append("Общее количество заправок: ").append(totalSales).append("\n\n");

        // Показываем только те виды топлива, которые были проданы
        boolean hasSales = false;
        for (FuelType fuelType : FuelType.values()) {
            if (fuelQuantities.containsKey(fuelType)) {
                hasSales = true;
                double quantity = fuelQuantities.get(fuelType);
                double revenue = fuelRevenues.get(fuelType);
                
                summary.append("Топливо: ").append(fuelType.getName()).append("\n");
                summary.append("Количество проданного топлива: ").append(String.format("%.2f", quantity)).append(" л\n");
                summary.append("Цена за литр: ").append(String.format("%.2f", fuelType.getPrice())).append(" руб/л\n");
                summary.append("Сумма выручки за данный вид: ").append(String.format("%.2f", revenue)).append(" руб\n");
                summary.append("══════════════════════════\n");
            }
        }

        if (!hasSales) {
            summary.append("Нет продаж топлива\n");
            summary.append("──────────────────────────\n");
        }

        summary.append("\n");
        summary.append("══════════════════════════\n");
        summary.append("ИТОГОВАЯ СУММА ВЫРУЧКИ: ").append(String.format("%.2f", totalRevenue)).append(" руб\n");
        summary.append("══════════════════════════\n");

        textViewShiftSummary.setText(summary.toString());
    }
}
