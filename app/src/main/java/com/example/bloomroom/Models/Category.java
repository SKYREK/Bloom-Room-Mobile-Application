package com.example.bloomroom.Models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private String image;
    private String id;
    private String description;


    public Category() {
    }
    //constructor
    public Category(String name, String image, String id, String description) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
    private static final String COLLECTION_NAME = "categories"; // Replace with your actual collection name

    public interface CategoryCallback {
        void onCategoriesLoaded(List<Category> categoryList);

        void onFailure(String errorMessage);
    }

    public static void getAllCategories(CategoryCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoriesCollectionRef = firestore.collection(COLLECTION_NAME);

        categoriesCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Category> categoryList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Category category = documentSnapshot.toObject(Category.class);
                        category.setId(documentSnapshot.getId());
                        categoryList.add(category);
                    }
                    callback.onCategoriesLoaded(categoryList);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to retrieve categories: " + e.getMessage());
                });
    }

    public static void deleteCategory(String categoryId, CategoryCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoriesCollectionRef = firestore.collection(COLLECTION_NAME);

        categoriesCollectionRef.document(categoryId).delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onCategoriesLoaded(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to delete category: " + e.getMessage());
                });
    }


    public static void updateCategory(String categoryId, Category category, CategoryCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference categoriesCollectionRef = firestore.collection(COLLECTION_NAME);

        categoriesCollectionRef.document(categoryId)
                .update(
                        "name", category.getName(),
                        "image", category.getImage(),
                        "description", category.getDescription()
                )
                .addOnSuccessListener(aVoid -> {
                    callback.onCategoriesLoaded(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to update category: " + e.getMessage());
                });
    }
}