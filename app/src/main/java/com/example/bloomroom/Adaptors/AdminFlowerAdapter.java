package com.example.bloomroom.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloomroom.AddCategoryActivity;
import com.example.bloomroom.AddFlowerActivity;
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.R;
import com.example.bloomroom.Utils.ImageUtils;

import java.util.List;

public class AdminFlowerAdapter extends RecyclerView.Adapter<AdminFlowerAdapter.ViewHolder> {

    private List<Flower> flowerList;
    private Context context;


    public AdminFlowerAdapter(List<Flower> flowerList, Context context) {
        this.flowerList = flowerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_flower_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flower flower = flowerList.get(position);
        holder.bind(flower);
    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView flowerImage;
        private TextView flowerName;
        private TextView price;
        private ImageButton deleteButton;
        private ImageButton editButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerImage = itemView.findViewById(R.id.imageView);
            flowerName = itemView.findViewById(R.id.nameField);
            price = itemView.findViewById(R.id.priceField);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public void bind(Flower flower) {
            flowerName.setText(flower.getName());
            ImageUtils.loadImageFromUrl(context, flower.getImage(), flowerImage);
            price.setText(Flower.formatDouble(flower.getPrice()));

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Flower.deleteFlower(flower.getId(), new Flower.FlowerCallback() {
                        @Override
                        public void onFlowersLoaded(List<Flower> categories) {
                            flowerList.remove(flower);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddFlowerActivity.class);
                    intent.putExtra(AddFlowerActivity.ABOUTFLAG,flower.getAbout());
                    intent.putExtra(AddFlowerActivity.IMAGEFLAG,flower.getImage());
                    intent.putExtra(AddFlowerActivity.NAMEFLAG,flower.getName());
                    intent.putExtra(AddFlowerActivity.IDFLAG,flower.getId());
                    intent.putExtra(AddFlowerActivity.PRICEFLAG,String.valueOf(flower.getPrice()));
                    intent.putExtra(AddFlowerActivity.CATEGORYFLAG,flower.getCategory());
                    context.startActivity(intent);
                }
            });
        }
    }
}
