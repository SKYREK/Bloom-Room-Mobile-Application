package com.example.bloomroom.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloomroom.AddCategoryActivity;
import com.example.bloomroom.AdminHomeActivity;
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.Models.Order;
import com.example.bloomroom.R;
import com.example.bloomroom.Utils.ImageUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;


    public AdminOrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView orderImage;
        private TextView productName;

        private TextView productQuantity;
        private TextView productTotal;

        private TextView customerName;
        private Button acceptButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.imageView);
            productName = itemView.findViewById(R.id.productName);
            productTotal = itemView.findViewById(R.id.totalField);
            productQuantity = itemView.findViewById(R.id.qtyField);
            customerName = itemView.findViewById(R.id.cusNameField);
            acceptButton = itemView.findViewById(R.id.checkButton);
        }

        public void bind(Order order) {
            productName.setText(order.getProductName());
            customerName.setText("Customer - "+order.getUserName());
            productQuantity.setText("Qty - "+order.getProductQuantity());
            productTotal.setText("Total - "+Flower.formatDouble(order.getProductQuantity() * order.getProductPrice()));
            ImageUtils.loadImageFromUrl(context, order.getProductImage(), orderImage);
            if(order.getStatus().equals( Order.STATUS_DELIVERED)){
                acceptButton.setText("Delivered");
                acceptButton.setClickable(false);
                acceptButton.setBackgroundColor(context.getResources().getColor(R.color.primary_green));
            }else{
                acceptButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        order.setStatus(Order.STATUS_DELIVERED);


                        // Get the Firestore instance
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


                        CollectionReference categoriesCollectionRef = firestore.collection("orders");

                        // Add the category object to Firestore
                        categoriesCollectionRef.document(order.getOrderId()).set(order)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(context, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                                    acceptButton.setText("Delivered");
                                    acceptButton.setClickable(false);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to update order status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
            }
        }
    }
}
