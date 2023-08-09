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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;


    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
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
        private TextView date;

        private TextView customerName;

        private TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.imageView);
            productName = itemView.findViewById(R.id.productName);
            productTotal = itemView.findViewById(R.id.totalField);
            productQuantity = itemView.findViewById(R.id.qtyField);
            status = itemView.findViewById(R.id.statusField);
            date = itemView.findViewById(R.id.dateField);
        }
        public void bind(Order order) {
            productName.setText(order.getProductName());
            productQuantity.setText("Qty - "+order.getProductQuantity());
            productTotal.setText("Total - "+Flower.formatDouble(order.getProductQuantity() * order.getProductPrice()));
            date.setText("Date - "+order.getDateTime().substring(0,10));
            status.setText(order.getStatus());
            if(order.getStatus().equals("Pending"))
                status.setTextColor(context.getResources().getColor(R.color.red));
            else
                status.setTextColor(context.getResources().getColor(R.color.primary_green));
            ImageUtils.loadImageFromUrl(context, order.getProductImage(), orderImage);


        }
    }
}
