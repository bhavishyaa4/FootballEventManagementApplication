package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ImageView tickImageView;
    private TextView thankYouText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // Find the views
        tickImageView = findViewById(R.id.tickImageView);
        thankYouText = findViewById(R.id.thankYouText);

        // Start the tick animation
//        animateTick();

        // Display the thank you message
//        thankYouText.setText("Thank you for Registration !! See you in the field!");
    }

    // Method to animate the tick icon
//    private void animateTick() {
//        // Load the animation (you can use a drawable animation or a scale animation)
//        Animation tickAnimation = AnimationUtils.loadAnimation(this, R.anim.tick_animation);
//
//        // Set an animation listener for redirecting after animation ends
//        tickAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // You can add logic here if needed when the animation starts
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Redirect to another activity after the animation finishes
//                Intent intent = new Intent(PaymentSuccessActivity.this, PaymentActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // Do something when the animation repeats, if needed
//            }
//        });
//
//        // Start the animation on the tick image
//        tickImageView.startAnimation(tickAnimation);
//    }
}
