package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloomroom.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ImageView logo_view;
    private ImageView flowerView;
    private LinearLayout login_layout;
    private TextView titleLogin;
    private EditText emailInput;
    private EditText passwordInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logo_view = findViewById(R.id.flower_login_logo);
        login_layout = findViewById(R.id.signup_form);
        flowerView =  findViewById(R.id.flower_two);
        emailInput = findViewById(R.id.emailInputLogin);
        passwordInput = findViewById(R.id.passwordInputLogin);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animateImagesAndTitle();


    }
    private void animateImagesAndTitle(){
        //get screen height and save in variable named screen
        int screen = (getResources().getDisplayMetrics().heightPixels /2)- dpToPx(this, 103);
        Animation animation1 = new TranslateAnimation(0, 0, screen, 0);
        animation1.setDuration(500);
        Animation animation2 = new TranslateAnimation(0,0,dpToPx(this,473),0);
        animation2.setDuration(500);
        Animation animation3 = new TranslateAnimation(0,0,dpToPx(this,473),0);
        animation3.setDuration(500);
        logo_view.startAnimation(animation1);
        login_layout.startAnimation(animation2);
        flowerView.startAnimation(animation3);
        changeLoginColor();

    }
    public static int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    public void changeLoginColor(){
        titleLogin = findViewById(R.id.login_head_text);
        String fullText = "Log in to your account";
        int startIndex = fullText.indexOf("Log in");

        // Create a SpannableString to apply color to the specific part
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_green)), startIndex, startIndex + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the modified SpannableString to the TextView
        titleLogin.setText(spannableString);

    }

    public void loadSignIn(View view) {
        //load signup activity
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    public void login(View view) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        loginUserWithEmailAndPassword(this, email, password);
    }
    public void loginUserWithEmailAndPassword(Activity activity, String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User.loadRelevantHome(user.getUid(),this); // Finish the current activity to prevent coming back to the login screen using the back button
                        }
                    } else {
                        // Login failed
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            // User with the provided email does not exist
                            Toast.makeText(activity, "User with this email does not exist.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // Invalid email or password provided by the user
                            Toast.makeText(activity, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // Generic error handling
                            Toast.makeText(activity, "Login failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}