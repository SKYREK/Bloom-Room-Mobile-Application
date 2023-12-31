package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Utils.ImageUploader;
import com.example.bloomroom.Utils.ImageUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCategoryActivity extends AppCompatActivity {

    public static final String NAMEFLAG = "category_name";
    public static final String DESCRIPTIONFLAG = "category_description";
    public static final String IMAGEFLAG = "category_image";
    public static final String IDFLAG = "category_id";

    private ImageUploader imageUploader;
    private String imgLink = "";

    private TextInputEditText nameField;
    private TextInputEditText descriptionField;

    private ImageButton backButton;

    private boolean isUpdating = false;

    String categoryId = "";
    String categoryName = "";
    String categoryDescription = "";


    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        imageUploader = new ImageUploader();
        nameField = findViewById(R.id.nameField);
        descriptionField = findViewById(R.id.priceField);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryActivity.super.onBackPressed();
            }
        });

        // Check if the intent has an extra with the category ID
        if (getIntent().hasExtra(IDFLAG)) {
            // If it does, then we are updating an existing category
            isUpdating = true;
            // Get the category ID from the intent
            categoryId = getIntent().getStringExtra(IDFLAG);
            nameField.setText(getIntent().getStringExtra(NAMEFLAG));
            descriptionField.setText(getIntent().getStringExtra(DESCRIPTIONFLAG));
            imgLink = getIntent().getStringExtra(IMAGEFLAG);
            ImageUtils.loadImageFromUrl(this, getIntent().getStringExtra(IMAGEFLAG), findViewById(R.id.imageView));
        }
    }
    private void pickImage() {
        imageUploader.pickImageFromGallery(this);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUploader.handleImagePickerResult(requestCode, resultCode, data, new ImageUploader.OnImageUploadCompleteListener() {
            @Override
            public void onImageUploadComplete(String imageUrl) {
                // Update the imgLink variable with the image URL
                imgLink = imageUrl;
                ImageUtils.loadImageFromUrl(AddCategoryActivity.this, imageUrl, findViewById(R.id.imageView));
                // Do any other processing with the image URL here
                Toast.makeText(AddCategoryActivity.this, "Image uploaded successfully: " + imageUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImageUploadFailed(String errorMessage) {
                // Handle the error if the image upload fails
                Toast.makeText(AddCategoryActivity.this, "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadImage(View view) {
        pickImage();
    }
    public void onClick(View view) {
        if (isUpdating) {
            updateCategory();
        } else {
            addCategory();
        }
    }
    public void addCategory() {
        String name = nameField.getText().toString();
        String description = descriptionField.getText().toString();
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgLink.equals("")) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(name, imgLink, "", description);

        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Get the reference to the "categories" collection (replace "categories" with your desired collection name)
        CollectionReference categoriesCollectionRef = firestore.collection("categories");

        // Add the category object to Firestore
        categoriesCollectionRef.add(category)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void updateCategory(){
        String name = nameField.getText().toString();
        String description = descriptionField.getText().toString();
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgLink.equals("")) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(name, imgLink, "", description);

        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Get the reference to the "categories" collection (replace "categories" with your desired collection name)
        CollectionReference categoriesCollectionRef = firestore.collection("categories");

        // Add the category object to Firestore
        categoriesCollectionRef.document(categoryId).set(category)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminHomeActivity.class);
                    intent.putExtra("selectedTab", 1); // Assuming the 2nd tab has an index of 1 (0-indexed)
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}