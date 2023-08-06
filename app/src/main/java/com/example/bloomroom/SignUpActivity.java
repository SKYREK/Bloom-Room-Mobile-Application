package com.example.bloomroom;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloomroom.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private TextView titleSignUp;
    private ImageView logo_view;
    private ImageView flowerView;
    private LinearLayout signup_layout;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private EditText nameInput;
    //private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        titleSignUp = findViewById(R.id.signup_head_text);
        changeSignUpColor();

    }
    //change color of sign up text
    public void changeSignUpColor(){
        String fullText = "Sign up for an account";
        int startIndex = fullText.indexOf("Sign up");
        Spannable spannable = new SpannableString(fullText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_green)), startIndex, startIndex + "Sign up".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSignUp.setText(spannable);
        logo_view = findViewById(R.id.flower_sign_up_logo);
        flowerView =  findViewById(R.id.flower_two);
        signup_layout = findViewById(R.id.signup_form);
        emailInput = findViewById(R.id.emailInputSignUp);
        passwordInput = findViewById(R.id.passwordInputSignUp);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInputSignUp);
        nameInput = findViewById(R.id.nameInputSignUp);
        animate();
    }
    public void animate(){
        Animation animation2 = new TranslateAnimation(0,0,LoginActivity.dpToPx(this,640),0);
        animation2.setDuration(500);
        Animation shrinkAnimation = new ScaleAnimation(1.5373f, 1f, 1.5266f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        shrinkAnimation.setDuration(500);
        signup_layout.startAnimation(animation2);
        flowerView.startAnimation(animation2);
        logo_view.startAnimation(shrinkAnimation);
    }

    public void loadLogin(View view) {

    }
    public void signIn(View view) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        String name = nameInput.getText().toString();
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()){
            //show toast
            Toast.makeText(this, "Please Fill all the inputs to continue", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword)){
            //show error
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        else{
            //sign up
            createUserWithEmailAndPassword(this, email, password , name);

        }
    }
    public void createUserWithEmailAndPassword(Activity activity, String email, String password , String name) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // User creation success
                        Log.d(TAG, "createUserWithEmailAndPassword:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        if (user != null) {
                            // You can perform additional actions here after successful user creation
                            // For example, update user profile, add user data to the database, etc.
                            Toast.makeText(activity, "User created successfully", Toast.LENGTH_SHORT).show();
                            createFirestoreUserDocument(activity, name, user.getUid().toString(), User.COMMON_IMAGE_LINK);
                        }
                    } else {
                        // User creation failed
                        Log.w(TAG, "createUserWithEmailAndPassword:failure", task.getException());
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            // Weak password provided by the user
                            Toast.makeText(activity, "Weak password. Please provide a stronger password.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            // Invalid email format provided by the user
                            Toast.makeText(activity, "Invalid email format. Please provide a valid email address.", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // User with the same email already exists
                            Toast.makeText(activity, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // Generic error handling
                            Toast.makeText(activity, "User creation failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void createFirestoreUserDocument(Activity activity, String name, String uid, String imageLink) {
        // Access the Firestore database instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Specify the collection and document reference
        DocumentReference docRef = db.collection("users").document(uid);

        // Create a data object with the name and imageLink
        User user = new User(name, imageLink,User.ROLE_USER);

        // Add the data to the Firestore document
        docRef.set(user)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Document creation success
                            //Toast.makeText(activity, "Firestore document created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Document creation failed
                            Toast.makeText(activity, "Firestore document creation failed. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}