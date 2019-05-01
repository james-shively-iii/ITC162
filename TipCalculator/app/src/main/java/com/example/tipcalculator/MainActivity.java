package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    // Create variables for the Tip Calculator
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;
    private EditText billAmountEditText;
    private Button percentUpButton;
    private Button percentDownButton;

    // SharedPreferences object
    private SharedPreferences savedValues;

    // Variables for input data
    private String billString = "";
    private float defaultPercent = .2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the widgets
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentUpButton = (Button) findViewById(R.id.percentUpButton);
        percentDownButton = (Button) findViewById(R.id.percentDownButton);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        // Listeners
        billAmountEditText.setOnEditorActionListener((TextView.OnEditorActionListener) this);
        percentUpButton.setOnClickListener((View.OnClickListener) this);
        percentDownButton.setOnClickListener((View.OnClickListener) this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        // Instance variables for onPause() method
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("billString", billString);
        editor.putFloat("defaultPercent", defaultPercent);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Instance variables for onResume() method
        billString = savedValues.getString("billString", "");
        defaultPercent = savedValues.getFloat("defaultPercent", defaultPercent);

        // Set bill amount
        billAmountEditText.setText(billString);

        // Display
        calc();
    }

    public void calc() {
        // Get bill amount
        billString = billAmountEditText.getText().toString();
        float amount;
        if(billString.equals("")) {
            amount = 0;
        } else {
            amount = Float.parseFloat(billString);
        }

        // Calculate tip and total
        float tip = amount * defaultPercent;
        float total = amount + tip;

        // Display formatted results
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tip));
        totalTextView.setText(currency.format(total));
        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(defaultPercent));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calc();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.percentUpButton:
                defaultPercent = defaultPercent + .01f;
                calc();
                break;
            case R.id.percentDownButton:
                defaultPercent = defaultPercent - .01f;
                calc();
                break;
        }
    }
}
