package com.example.myapplication.lab3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class FuelSelectionFragment extends Fragment {

    private Spinner spinnerFuelType;
    private EditText editTextQuantity;
    private TextView textViewPriceInfo;
    private TextView textViewTotalPrice;
    private Button buttonNewOrder;
    private Button buttonFinishShift;

    private FuelType selectedFuelType;
    private double quantity = 0;

    public interface OnFuelSelectionListener {
        void onNewOrder();
        void onFinishShift();
        void onFuelSale(FuelSale fuelSale);
    }

    private OnFuelSelectionListener listener;

    public void setOnFuelSelectionListener(OnFuelSelectionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel_selection, container, false);

        spinnerFuelType = view.findViewById(R.id.spinnerFuelType);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);
        textViewPriceInfo = view.findViewById(R.id.textViewPriceInfo);
        textViewTotalPrice = view.findViewById(R.id.textViewTotalPrice);
        buttonNewOrder = view.findViewById(R.id.buttonNewOrder);
        buttonFinishShift = view.findViewById(R.id.buttonFinishShift);

        setupFuelTypeSpinner();
        setupQuantityInput();
        setupButtons();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализируем форму при создании view
        clearInputs();
    }

    private void setupFuelTypeSpinner() {
        ArrayAdapter<FuelType> adapter = new ArrayAdapter<>(getContext(), 
                android.R.layout.simple_spinner_item, FuelType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(adapter);

        spinnerFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFuelType = (FuelType) parent.getItemAtPosition(position);
                updatePriceInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFuelType = null;
            }
        });
    }

    private void setupQuantityInput() {
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    quantity = s.toString().isEmpty() ? 0 : Double.parseDouble(s.toString());
                    updatePriceInfo();
                } catch (NumberFormatException e) {
                    quantity = 0;
                    updatePriceInfo();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupButtons() {
        buttonNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    FuelSale fuelSale = new FuelSale(selectedFuelType, quantity);
                    if (listener != null) {
                        listener.onFuelSale(fuelSale);
                    }
                    clearInputs();
                }
            }
        });

        buttonFinishShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    FuelSale fuelSale = new FuelSale(selectedFuelType, quantity);
                    if (listener != null) {
                        listener.onFuelSale(fuelSale);
                        listener.onFinishShift();
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        if (selectedFuelType == null) {
            Toast.makeText(getContext(), "Выберите вид топлива", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (quantity <= 0) {
            Toast.makeText(getContext(), "Введите количество топлива больше 0", Toast.LENGTH_SHORT).show();
            editTextQuantity.requestFocus();
            return false;
        }

        return true;
    }

    private void updatePriceInfo() {
        if (selectedFuelType != null) {
            textViewPriceInfo.setText("Цена: " + String.format("%.2f", selectedFuelType.getPrice()) + " руб/л");
            double total = selectedFuelType.getPrice() * quantity;
            textViewTotalPrice.setText("Итого: " + String.format("%.2f", total) + " руб");
        } else {
            textViewPriceInfo.setText("Цена: 0.00 руб/л");
            textViewTotalPrice.setText("Итого: 0.00 руб");
        }
    }

    private void clearInputs() {
        editTextQuantity.setText("");
        quantity = 0;
        // Явно выбираем первый вид топлива, чтобы не остаться в состоянии null,
        // когда дважды подряд выбирается один и тот же тип
        spinnerFuelType.setSelection(0); // Сбрасываем выбор в спиннере
        selectedFuelType = FuelType.values()[0];
        updatePriceInfo();
    }

    public void resetForm() {
        clearInputs();
    }
}
