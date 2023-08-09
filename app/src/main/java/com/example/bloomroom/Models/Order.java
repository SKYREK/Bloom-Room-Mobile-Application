package com.example.bloomroom.Models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    public static final  String STATUS_PENDING = "Pending";
    public static final  String STATUS_DELIVERED = "Delivered";
    public static final String STATUS_CANCELLED = "Cancelled";
    private String userId;

    private String userName;
    private String orderId;
    private String productName;
    private String productImage;
    private double productPrice;
    private int productQuantity;
    private String dateTime;
    private String status;

    public Order() {
    }

    public Order(String userId,String userName, String orderId, String productName, String productImage, double productPrice, int productQuantity, String dateTime, String status) {
        this.userId = userId;
        this.userName = userName;
        this.orderId = orderId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                ", productPrice=" + productPrice +
                ", productQuantity=" + productQuantity +
                ", dateTime='" + dateTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    public static void getAllOrders(Order.OrderCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference ordersCollectionRef = firestore.collection("orders");

        ordersCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> categoryList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setOrderId(documentSnapshot.getId());
                        categoryList.add(order);
                    }
                    callback.onOrdersLoaded(categoryList);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to retrieve orders: " + e.getMessage());
                });
    }
    public static void getOrdersByUserId(String userId, Order.OrderCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference ordersCollectionRef = firestore.collection("orders");

        Query query = ordersCollectionRef.whereEqualTo("userId", userId);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> userOrders = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setOrderId(documentSnapshot.getId());
                        userOrders.add(order);
                    }
                    callback.onOrdersLoaded(userOrders);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Failed to retrieve user orders: " + e.getMessage());
                });
    }

    public interface OrderCallback {
        void onOrdersLoaded(List<Order> categoryList);

        void onFailure(String errorMessage);
    }
}
