package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private EditText etInput;
    private ArrayList<String> history = new ArrayList<>();
    private double currentResult = 0;
    private boolean isLastResultUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        etInput = findViewById(R.id.etInput);

        Button btnSum = findViewById(R.id.btnSum);
        Button btnSubtract = findViewById(R.id.btnSubtract);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnEqual = findViewById(R.id.btnEqual);


        btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperation("+");
            }
        });


        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperation("-");
            }
        });


        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperation("*");
            }
        });


        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperation("/");
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putStringArrayListExtra("history", history);
                startActivity(intent);
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCalculator();
            }
        });


        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = etInput.getText().toString();
                if (!inputText.isEmpty()) {
                    try {
                        currentResult = evaluateExpression(inputText);
                        tvResult.setText(formatResult(currentResult));
                        history.add(inputText + " = " + formatResult(currentResult));
                        etInput.setText("");
                        isLastResultUsed = true;
                    } catch (Exception e) {
                        etInput.setError("Operación inválida");
                    }
                } else {
                    etInput.setError("Ingresa una operación");
                }
            }
        });
    }


    private void handleOperation(String operator) {
        if (isLastResultUsed) {
            etInput.setText(formatResult(currentResult) + operator);
            isLastResultUsed = false;
        } else {

            etInput.append(operator);
        }
    }


    private String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }


    private void clearCalculator() {
        currentResult = 0;
        tvResult.setText(formatResult(currentResult));
        history.clear();
    }


    private double evaluateExpression(String expression) {
        try {
            expression = expression.replaceAll("\\s+", "");

            ArrayList<String> tokens = new ArrayList<>();
            StringBuilder currentNumber = new StringBuilder();

            for (char c : expression.toCharArray()) {
                if (Character.isDigit(c) || c == '.') {
                    currentNumber.append(c);
                } else {
                    if (currentNumber.length() > 0) {
                        tokens.add(currentNumber.toString());
                        currentNumber = new StringBuilder();
                    }
                    tokens.add(String.valueOf(c));
                }
            }
            if (currentNumber.length() > 0) {
                tokens.add(currentNumber.toString());
            }


            ArrayList<String> processedTokens = new ArrayList<>();
            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);
                if (token.equals("*") || token.equals("/")) {
                    double left = Double.parseDouble(processedTokens.remove(processedTokens.size() - 1));
                    double right = Double.parseDouble(tokens.get(++i));
                    double result = token.equals("*") ? left * right : left / right;
                    processedTokens.add(String.valueOf(result));
                } else {
                    processedTokens.add(token);
                }
            }


            double result = Double.parseDouble(processedTokens.get(0));
            for (int i = 1; i < processedTokens.size(); i += 2) {
                String operator = processedTokens.get(i);
                double value = Double.parseDouble(processedTokens.get(i + 1));
                if (operator.equals("+")) {
                    result += value;
                } else if (operator.equals("-")) {
                    result -= value;
                }
            }

            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Expresión inválida");
        }
    }
}
