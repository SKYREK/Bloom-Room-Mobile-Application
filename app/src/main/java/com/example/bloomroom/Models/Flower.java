package com.example.bloomroom.Models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class Flower {
    private String name;
    private String image;
    private String id;
    private double price;
    private String category;
    private String about;

    public Flower() {
    }

    //constructor
    public Flower(String name, String image, String id, double price, String category, String about) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.price = price;
        this.category = category;
        this.about = about;
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

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getAbout() {
        return about;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public String toString() {
        return "Flower{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", about='" + about + '\'' +
                '}';
    }

    private static final String COLLECTION_NAME = "flowers"; // Replace with your actual collection name

    public interface FlowerCallback {
        void onFlowersLoaded(List<Flower> flowerList);

        void onFailure(String errorMessage);
    }

    public static void getAllFlowers(FlowerCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference flowersCollectionRef = firestore.collection(COLLECTION_NAME);
        flowersCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Flower> flowerList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Flower flower = documentSnapshot.toObject(Flower.class);
                        flower.setId(documentSnapshot.getId());
                        flowerList.add(flower);
                    }
                    callback.onFlowersLoaded(flowerList);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to retrieve flowers: " + e.getMessage());
                });
    }

    public static void addFlower(Flower flower, FlowerCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference flowersCollectionRef = firestore.collection(COLLECTION_NAME);
        flower.setId(firestore.collection(COLLECTION_NAME).document().getId());
        flowersCollectionRef.document(flower.getId()).set(flower)
                .addOnSuccessListener(aVoid -> {
                    callback.onFlowersLoaded(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to add flower: " + e.getMessage());
                });
    }
    public static void deleteFlower(String flowerId, Flower.FlowerCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference flowersCollectionRef = firestore.collection(COLLECTION_NAME);

        flowersCollectionRef.document(flowerId).delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onFlowersLoaded(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to delete category: " + e.getMessage());
                });
    }
    public static String formatDouble(double value) {
        return String.format("%.2f LKR", value);
    }
    public static void getFlowersByCategory(String categoryName, FlowerCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference flowersCollectionRef = firestore.collection(COLLECTION_NAME);

        Query query = flowersCollectionRef.whereEqualTo("category", categoryName);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Flower> categoryFlowers = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Flower flower = documentSnapshot.toObject(Flower.class);
                        flower.setId(documentSnapshot.getId());
                        categoryFlowers.add(flower);
                    }
                    callback.onFlowersLoaded(categoryFlowers);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to retrieve flowers by category: " + e.getMessage());
                });
    }



}
