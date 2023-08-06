package com.example.bloomroom.Models;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.example.bloomroom.AdminHomeActivity;
import com.example.bloomroom.HomeActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final String COMMON_IMAGE_LINK = "https://www.w3schools.com/w3css/img_avatar3.png";


    private String name;
    private String imageLink;
    private String role;
    //constructor
    public User(String name, String imageLink, String role){
        this.name = name;
        this.imageLink = imageLink;
        this.role = role;
    }
    public User(){
        System.out.println("User created");
    }
    //setter
    public void setName(String name){
        this.name = name;
    }
    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }
    //getter
    public String getName(){
        return name;
    }
    public String getImageLink(){
        return imageLink;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public static void loadRelevantHome(String documentId, Activity activity) {
        // Initialize Firestore instance
        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

        // Get the reference to the User document
        DocumentReference userDocumentRef = firestoreDB.collection("users").document(documentId);

        // Retrieve the document using the document reference
        userDocumentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Convert the document data to a User object
                    User user = document.toObject(User.class);

                    // Pass the User object to the listener
                    if (user != null) {
                        if(user.getRole().equals(User.ROLE_ADMIN)){
                            //go to admin home
                            Toast.makeText(activity, "Welcome Admin", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, AdminHomeActivity.class));
                            activity.finish();
                        }else{
                            //go to user home
                            Toast.makeText(activity, "Welcome User", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, HomeActivity.class));
                            activity.finish();
                        }
                    } else {
                        // User object is null, handle the error if needed
                        //toast saying user is null
                        Toast.makeText(activity, "Login error contact admin panel", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Document not found, handle the error if needed
                    Toast.makeText(activity, "Login error contact admin panel", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error occurred while retrieving the document, handle the error if needed
                Toast.makeText(activity, "Login error contact admin panel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Interface to define the callback for getting the User object
    public interface OnUserRetrievedListener {
        void onUserRetrieved(User user);
    }
}
