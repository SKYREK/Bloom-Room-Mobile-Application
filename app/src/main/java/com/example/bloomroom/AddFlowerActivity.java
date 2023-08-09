package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bloomroom.Adaptors.CategorySpinnerAdapter;
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.Utils.ImageUploader;
import com.example.bloomroom.Utils.ImageUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddFlowerActivity extends AppCompatActivity {

    ImageButton backButton;
    public static final String NAMEFLAG = "flower_name";
    public static final String ABOUTFLAG = "flower_description";
    public static final String IMAGEFLAG = "flower_image";
    public static final String IDFLAG = "flower_id";
    public static final String PRICEFLAG = "flower_price";
    public static  final String CATEGORYFLAG = "flower_category";
    private String imgLink = "";
    private boolean isUpdating = false;

    private TextInputEditText nameField;
    private TextInputEditText aboutField;
    private TextInputEditText priceField;
    private Spinner spinner;
    String flowerId = "";

    ImageUploader imageUploader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flower);
        spinner = (Spinner) findViewById(R.id.categorySpinner);
        nameField = findViewById(R.id.nameField);
        aboutField = findViewById(R.id.aboutField);
        priceField = findViewById(R.id.priceField);
        imageUploader = new ImageUploader();
        Category.getAllCategories(new Category.CategoryCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categoryList) {
                CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(AddFlowerActivity.this, categoryList);
                spinner.setAdapter(adapter);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AddFlowerActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFlowerActivity.super.onBackPressed();
            }
        });
        if (getIntent().hasExtra(IDFLAG)) {

            isUpdating = true;

            flowerId = getIntent().getStringExtra(IDFLAG);
            nameField.setText(getIntent().getStringExtra(NAMEFLAG));
            aboutField.setText(getIntent().getStringExtra(ABOUTFLAG));
            priceField.setText(String.valueOf(getIntent().getStringExtra(PRICEFLAG)));
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
                ImageUtils.loadImageFromUrl(AddFlowerActivity.this, imageUrl, findViewById(R.id.imageView));
                // Do any other processing with the image URL here
                Toast.makeText(AddFlowerActivity.this, "Image uploaded successfully: " + imageUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImageUploadFailed(String errorMessage) {
                // Handle the error if the image upload fails
                Toast.makeText(AddFlowerActivity.this, "Image upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
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
            addFlower();
        }
    }
    public void addFlower() {
        String name = nameField.getText().toString();
        double price = 0;
        try{
            price = Double.parseDouble(priceField.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinner.getSelectedItem().toString();
        String about = aboutField.getText().toString();
        if (name.isEmpty()|| category.isEmpty() || about.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgLink.equals("")) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Flower flower = new Flower(name, imgLink, "", price, category, about);

        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference categoriesCollectionRef = firestore.collection("flowers");

        // Add the category object to Firestore
        categoriesCollectionRef.add(flower)
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
        double price = 0;
        try{
            price = Double.parseDouble(priceField.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinner.getSelectedItem().toString();
        if(category==null){
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        String about = aboutField.getText().toString();
        if (name.isEmpty()|| category.isEmpty() || about.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgLink.equals("")) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Flower flower = new Flower(name, imgLink, flowerId, price, category, about);

        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        CollectionReference categoriesCollectionRef = firestore.collection("flowers");

        // Add the category object to Firestore
        categoriesCollectionRef.document(flowerId).set(flower)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Flower updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AdminHomeActivity.class);
                    intent.putExtra("selectedTab", 0);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}