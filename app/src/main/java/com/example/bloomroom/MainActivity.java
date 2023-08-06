package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView titleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = findViewById(R.id.flower_one);
        imageView2 = findViewById(R.id.flower_two);
        titleImageView = findViewById(R.id.flower_home);

        // Set the activity to full screen
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);

        animateImagesAndTitle();
        //got to login activity after 2 seconds
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    startActivity(new android.content.Intent(getApplicationContext(), LoginActivity.class));
                    finish(); // Finish this activity so that the user can't navigate back to it.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start(); // Start the thread
    }

    private void animateImagesAndTitle() {
        // Create TranslateAnimation for image1 (from left to original position)
        Animation animation1 = new TranslateAnimation(-300, 0, -300, 0);
        animation1.setDuration(1000);

        // Create TranslateAnimation for image2 (from right to original position)
        Animation animation2 = new TranslateAnimation(300, 0, 300, 0);
        animation2.setDuration(1000);

        // Create fade-in animation for the titleTextView
        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(1000);

        // Create an AnimationSetImg1 to combine all animations
        AnimationSet animationSetImg1 = new AnimationSet(true);
        animationSetImg1.addAnimation(animation1);
        AnimationSet animationSetImg2 = new AnimationSet(true);

        animationSetImg2.addAnimation(animation2);
        //animationSet.addAnimation(fadeInAnimation);

        // Start the animations for both images and the titleTextView
        imageView1.startAnimation(animationSetImg1);
        imageView2.startAnimation(animationSetImg2);
        titleImageView.startAnimation(fadeInAnimation);
    }
}
