package com.example.bloomroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.Models.Order;
import com.example.bloomroom.Models.User;
import com.example.bloomroom.Utils.ImageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;

public class ActivityProductView extends AppCompatActivity {
    public static final String NAMEFLAG = "name";
    public static final String CATEGORYFLAG = "category";

    public static final String PRICEFLAG = "price";
    public static final String ABOUTFLAG = "about";
    public static final String IMAGEFLAG = "image";
    public static final String IDFLAG = "id";
    public static final String TAG = "ActivityProductView";

    public  String flowerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        int qty =0;
        ImageView imageView = findViewById(R.id.imageView);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            //go back
            finish();
        });
        TextView nameField = findViewById(R.id.nameField);
        TextView categoryField = findViewById(R.id.categoryField);
        ImageView subButton = findViewById(R.id.subButton);
        ImageView addButton = findViewById(R.id.addButton);
        TextView quantityField = findViewById(R.id.qtyField);
        TextView priceField = findViewById(R.id.priceField);
        TextView aboutField = findViewById(R.id.aboutField);
        Button checkoutButton = findViewById(R.id.loadHome);
        //get flower name from intent
        String name = getIntent().getStringExtra(NAMEFLAG);
        //get flower category from intent
        String category = getIntent().getStringExtra(CATEGORYFLAG);
        //get flower quantity from intent

        //get flower price from intent
        String price = Flower.formatDouble(getIntent().getDoubleExtra(PRICEFLAG,0));
        //get flower about from intent
        String about = getIntent().getStringExtra(ABOUTFLAG);

        flowerId = getIntent().getStringExtra(IDFLAG);
        //set flower name
        nameField.setText(name);
        //set flower category
        categoryField.setText(category);
        //set flower price
        priceField.setText(price);
        //set flower about
        aboutField.setText(about);
        quantityField.setText(String.valueOf(qty));
        addButton.setOnClickListener(v -> {
            int cqty = Integer.parseInt(quantityField.getText().toString());
            cqty++;
            quantityField.setText(String.valueOf(cqty));
        });
        subButton.setOnClickListener(v -> {
            int cqty = Integer.parseInt(quantityField.getText().toString());
            if(cqty>0){
                cqty--;
            }
            quantityField.setText(String.valueOf(cqty));
        });
        ImageUtils.loadImageFromUrl(this,getIntent().getStringExtra(IMAGEFLAG),imageView);
        Button checkOutButton = findViewById(R.id.loadHome);
        checkOutButton.setOnClickListener(v -> {
            int fqty = Integer.parseInt(quantityField.getText().toString());
            if(fqty==0){
                Toast.makeText(this, "Please select quantity", Toast.LENGTH_SHORT).show();
                return;
            }
            //String userId, String orderId, String productName, String productImage, double productPrice, int productQuantity, String dateTime, String status
            String uid = FirebaseAuth.getInstance().getUid();
            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
            DocumentReference userDocumentRef = firestoreDB.collection("users").document(uid);

            // Retrieve the document using the document reference
            userDocumentRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert the document data to a User object
                        User user = document.toObject(User.class);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            Order order = new Order(
                                    uid,
                                    user.getName(),
                                    "",
                                    name,
                                    getIntent().getStringExtra(IMAGEFLAG),
                                    getIntent().getDoubleExtra(PRICEFLAG,0),
                                    fqty,
                                    LocalDateTime.now().toString(),
                                    Order.STATUS_PENDING
                            );
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                            CollectionReference categoriesCollectionRef = firestore.collection("orders");

                            // Add the category object to Firestore
                            categoriesCollectionRef.add(order)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Order added successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(
                                                ActivityProductView.this,
                                                CompleteActivity.class
                                        ));
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to add order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to retrieve user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            // Get the Firestore instance

        });
    }
}