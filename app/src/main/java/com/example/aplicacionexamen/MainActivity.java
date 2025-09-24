package com.example.aplicacionexamen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// IMPORTS PARA API REAL
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private EditText editTextAmount;
    private Spinner spinnerFromCurrency, spinnerToCurrency;
    private TextView textViewResult, textViewRate, textViewDate;
    private Button buttonSwap, buttonClear, buttonHistory, buttonClearHistory;
    private CardView cardViewHistory;
    private LinearLayout layoutHistory;

    // Data
    private String[] currencies = {"USD", "EUR", "MXN", "CAD", "GBP", "JPY", "AUD", "CHF"};
    private String[] currencyNames = {
            "USD - Dólar Estadounidense",
            "EUR - Euro",
            "MXN - Peso Mexicano",
            "CAD - Dólar Canadiense",
            "GBP - Libra Esterlina",
            "JPY - Yen Japonés",
            "AUD - Dólar Australiano",
            "CHF - Franco Suizo"
    };

    //  URL API

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    private OkHttpClient httpClient;
    private boolean usingRealRates = false;

    // Exchange rates (base USD = 1.0) - RESPALDO si falla la API
    private Map<String, Double> exchangeRates;

    // History
    private List<ConversionRecord> conversionHistory;
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private boolean isHistoryVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 🌍 INICIALIZAR CLIENTE HTTP
        httpClient = new OkHttpClient();

        initializeViews();
        setupFallbackExchangeRates(); // Tasas de respaldo
        setupSpinners();
        setupListeners();
        updateDate();

        conversionHistory = new ArrayList<>();

        // Set default values
        spinnerFromCurrency.setSelection(0); // USD
        spinnerToCurrency.setSelection(2);   // MXN

        // 🌍 OBTENER TASAS REALES AL INICIAR
        fetchRealExchangeRates();
    }

    private void initializeViews() {
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency);
        spinnerToCurrency = findViewById(R.id.spinnerToCurrency);
        textViewResult = findViewById(R.id.textViewResult);
        textViewRate = findViewById(R.id.textViewRate);
        textViewDate = findViewById(R.id.textViewDate);
        buttonSwap = findViewById(R.id.buttonSwap);
        buttonClear = findViewById(R.id.buttonClear);
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonClearHistory = findViewById(R.id.buttonClearHistory);
        cardViewHistory = findViewById(R.id.cardViewHistory);
        layoutHistory = findViewById(R.id.layoutHistory);
    }

    // 🌍 TASAS DE RESPALDO (por si falla la API)
    private void setupFallbackExchangeRates() {
        exchangeRates = new HashMap<>();
        // Base USD = 1.0
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        exchangeRates.put("MXN", 17.50);
        exchangeRates.put("CAD", 1.35);
        exchangeRates.put("GBP", 0.73);
        exchangeRates.put("JPY", 110.0);
        exchangeRates.put("AUD", 1.45);
        exchangeRates.put("CHF", 0.92);
    }

    // 🌍 MÉTODO PARA OBTENER TASAS REALES DE LA API
    private void fetchRealExchangeRates() {
        showToast("Actualizando tasas de cambio...");

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    showToast("Error de conexión. Usando tasas de respaldo.");
                    usingRealRates = false;
                    updateExchangeRateDisplay();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    runOnUiThread(() -> {
                        parseExchangeRates(jsonResponse);
                    });
                } else {
                    runOnUiThread(() -> {
                        showToast("Error al obtener tasas. Usando respaldo.");
                        usingRealRates = false;
                    });
                }
            }
        });
    }

    //  AQUÍ SE PROCESA EL JSON
    private void parseExchangeRates(String jsonResponse) {
        try {
            // EL JSON {"rates": {"MXN": 17.85, "EUR": 0.85, "GBP": 0.73}} SE PROCESA AQUÍ
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject rates = jsonObject.getJSONObject("rates");

            // Actualizar las tasas con datos reales
            exchangeRates.put("USD", 1.0); // Base currency

            if (rates.has("EUR")) exchangeRates.put("EUR", rates.getDouble("EUR"));
            if (rates.has("MXN")) exchangeRates.put("MXN", rates.getDouble("MXN"));
            if (rates.has("CAD")) exchangeRates.put("CAD", rates.getDouble("CAD"));
            if (rates.has("GBP")) exchangeRates.put("GBP", rates.getDouble("GBP"));
            if (rates.has("JPY")) exchangeRates.put("JPY", rates.getDouble("JPY"));
            if (rates.has("AUD")) exchangeRates.put("AUD", rates.getDouble("AUD"));
            if (rates.has("CHF")) exchangeRates.put("CHF", rates.getDouble("CHF"));

            usingRealRates = true;
            showToast("✅ Tasas actualizadas en tiempo real!");
            updateExchangeRateDisplay();
            performConversion(); // Reconvertir con nuevas tasas

        } catch (JSONException e) {
            showToast("Error procesando datos. Usando respaldo.");
            usingRealRates = false;
        }
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapter);
        spinnerToCurrency.setAdapter(adapter);
    }

    private void setupListeners() {
        // Amount text change listener
        editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performConversion();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Spinner change listeners
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performConversion();
                updateExchangeRateDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerFromCurrency.setOnItemSelectedListener(spinnerListener);
        spinnerToCurrency.setOnItemSelectedListener(spinnerListener);

        // Button listeners
        buttonSwap.setOnClickListener(v -> swapCurrencies());
        buttonClear.setOnClickListener(v -> clearFields());
        buttonHistory.setOnClickListener(v -> toggleHistory());
        buttonClearHistory.setOnClickListener(v -> clearHistory());
    }

    private void performConversion() {
        String amountStr = editTextAmount.getText().toString().trim();

        if (amountStr.isEmpty() || amountStr.equals(".")) {
            textViewResult.setText("0.00");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            String fromCurrency = currencies[spinnerFromCurrency.getSelectedItemPosition()];
            String toCurrency = currencies[spinnerToCurrency.getSelectedItemPosition()];

            double result = convertCurrency(amount, fromCurrency, toCurrency);
            textViewResult.setText(decimalFormat.format(result));

            // Add to history if amount > 0
            if (amount > 0) {
                addToHistory(amount, fromCurrency, result, toCurrency);
            }

        } catch (NumberFormatException e) {
            textViewResult.setText("Error");
        }
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double fromRate = exchangeRates.get(fromCurrency);
        double toRate = exchangeRates.get(toCurrency);

        // Convert to USD first, then to target currency
        double usdAmount = amount / fromRate;
        return usdAmount * toRate;
    }

    private void updateExchangeRateDisplay() {
        String fromCurrency = currencies[spinnerFromCurrency.getSelectedItemPosition()];
        String toCurrency = currencies[spinnerToCurrency.getSelectedItemPosition()];

        double rate = convertCurrency(1.0, fromCurrency, toCurrency);

        // 🌍 MOSTRAR SI ESTAMOS USANDO TASAS REALES O DE RESPALDO
        String rateSource = usingRealRates ? " (En tiempo real)" : " (Simulado)";
        String rateText = String.format("1 %s = %s %s%s", fromCurrency, decimalFormat.format(rate), toCurrency, rateSource);
        textViewRate.setText(rateText);
    }

    private void swapCurrencies() {
        int fromPosition = spinnerFromCurrency.getSelectedItemPosition();
        int toPosition = spinnerToCurrency.getSelectedItemPosition();

        spinnerFromCurrency.setSelection(toPosition);
        spinnerToCurrency.setSelection(fromPosition);

        showToast("Monedas intercambiadas");
    }

    private void clearFields() {
        editTextAmount.setText("");
        textViewResult.setText("0.00");
        showToast("Campos limpiados");
    }

    private void toggleHistory() {
        isHistoryVisible = !isHistoryVisible;
        cardViewHistory.setVisibility(isHistoryVisible ? View.VISIBLE : View.GONE);
        buttonHistory.setText(isHistoryVisible ? "Ocultar Historial" : "Historial");

        if (isHistoryVisible) {
            updateHistoryDisplay();
        }
    }

    private void addToHistory(double fromAmount, String fromCurrency, double toAmount, String toCurrency) {
        ConversionRecord record = new ConversionRecord(fromAmount, fromCurrency, toAmount, toCurrency);

        // Add to beginning and limit to 10 records
        conversionHistory.add(0, record);
        if (conversionHistory.size() > 10) {
            conversionHistory.remove(conversionHistory.size() - 1);
        }
    }

    private void updateHistoryDisplay() {
        layoutHistory.removeAllViews();

        if (conversionHistory.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No hay conversiones recientes");
            emptyText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            emptyText.setPadding(16, 16, 16, 16);
            layoutHistory.addView(emptyText);
            return;
        }

        for (ConversionRecord record : conversionHistory) {
            TextView historyItem = new TextView(this);
            String historyText = String.format("%s %s → %s %s",
                    decimalFormat.format(record.fromAmount), record.fromCurrency,
                    decimalFormat.format(record.toAmount), record.toCurrency);

            historyItem.setText(historyText);
            historyItem.setTextSize(14);
            historyItem.setPadding(16, 8, 16, 8);
            historyItem.setBackgroundResource(android.R.drawable.list_selector_background);

            layoutHistory.addView(historyItem);
        }
    }

    private void clearHistory() {
        conversionHistory.clear();
        updateHistoryDisplay();
        showToast("Historial limpiado");
    }

    private void updateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        String dateText = dateFormat.format(new Date());
        if (usingRealRates) {
            dateText += " • Tasas actualizadas";
        }
        textViewDate.setText(dateText);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Inner class for conversion records
    private static class ConversionRecord {
        double fromAmount;
        String fromCurrency;
        double toAmount;
        String toCurrency;

        ConversionRecord(double fromAmount, String fromCurrency, double toAmount, String toCurrency) {
            this.fromAmount = fromAmount;
            this.fromCurrency = fromCurrency;
            this.toAmount = toAmount;
            this.toCurrency = toCurrency;
        }
    }
}