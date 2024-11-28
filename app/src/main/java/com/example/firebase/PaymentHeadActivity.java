package com.example.firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PaymentHeadActivity extends AppCompatActivity {

    EditText amount;
    Button btn;
    String clientId = "AZU02EZsHW-yyt6DojNsJ6P498n2-QPh8jqBHiiCTY_vp-3IqdjdZ0NQOFrq7bS6bMZRQ3bM1Jccd4Qn";  // Replace with your actual PayPal client ID
    int PAYPAL_REQUEST_CODE = 123;
    public static PayPalConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Enable Edge-to-Edge feature (optional, based on Android version)
        setContentView(R.layout.activity_payment_head);

        // Set up PayPal configuration
        configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(clientId);

        amount = findViewById(R.id.amount);
        btn = findViewById(R.id.payment);

        // Set the OnClickListener for the payment button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();  // Call the payment method when the button is clicked
            }
        });
    }

    // Method to initiate payment
    private void getPayment() {
        String amounts = amount.getText().toString();

        // Validate that the amount is not empty and is a valid number
        if (amounts.isEmpty() || Double.parseDouble(amounts) <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create PayPal payment
        PayPalPayment payment = new PayPalPayment(new BigDecimal("5.00"), "USD", "Test Payment", PayPalPayment.PAYMENT_INTENT_SALE);


        // Start the payment activity
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            // Start the PayPal service with configuration
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
            startService(intent);
        } catch (Exception e) {
            Log.e("PayPalServiceError", "Error starting PayPal service: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        try {
            // Stop the PayPal service when the activity is destroyed
            stopService(new Intent(this, PayPalService.class));
        } catch (Exception e) {
            Log.e("PayPalServiceError", "Error stopping PayPal service: " + e.getMessage());
        }
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (paymentConfirmation != null) {
                try {
                    String paymentDetails = paymentConfirmation.toJSONObject().toString();
                    JSONObject object = new JSONObject(paymentDetails);
                    Log.d("PaymentSuccess", "Payment details: " + object.toString());
                    Toast.makeText(this, "Payment successful: " + object.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.e("PaymentError", "Error parsing payment confirmation: " + e.getLocalizedMessage());
                    Toast.makeText(this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("PaymentCanceled", "Payment canceled by user");
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("PaymentInvalid", "Invalid payment attempt");
                Toast.makeText(this, "Invalid payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
