package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.Button;


public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tvHistory = findViewById(R.id.tvHistory);
        Button btnBackToCalculator = findViewById(R.id.btnBackToCalculator);


        ArrayList<String> history = getIntent().getStringArrayListExtra("history");


        if (history != null && !history.isEmpty()) {
            StringBuilder historyText = new StringBuilder();
            for (String entry : history) {
                historyText.append(entry).append("\n");
            }
            tvHistory.setText(historyText.toString());
        } else {
            tvHistory.setText("No hay historial");
        }

        btnBackToCalculator.setOnClickListener(v -> {
            finish();
        });
    }
}