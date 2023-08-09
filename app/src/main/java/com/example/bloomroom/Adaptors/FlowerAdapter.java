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
import com.example.bloomroom.ActivityProductView;
import com.example.bloomroom.AddCategoryActivity;
import com.example.bloomroom.AddFlowerActivity;
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.Models.Flower;
import com.example.bloomroom.R;
import com.example.bloomroom.Utils.ImageUtils;

import java.util.List;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.ViewHolder> {

    private List<Flower> flowerList;
    private Context context;


    public FlowerAdapter(List<Flower> flowerList, Context context) {
        this.flowerList = flowerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_card, parent, false);
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

        private ImageButton shopButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerImage = itemView.findViewById(R.id.imageView);
            flowerName = itemView.findViewById(R.id.nameField);
            price = itemView.findViewById(R.id.priceField);
            shopButton = itemView.findViewById(R.id.shopButton);

        }

        public void bind(Flower flower) {
            flowerName.setText(flower.getName());
            ImageUtils.loadImageFromUrl(context, flower.getImage(), flowerImage);
            price.setText(Flower.formatDouble(flower.getPrice()));
            shopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityProductView.class);
                    intent.putExtra(ActivityProductView.NAMEFLAG, flower.getName());
                    intent.putExtra(ActivityProductView.IMAGEFLAG, flower.getImage());
                    intent.putExtra(ActivityProductView.PRICEFLAG, flower.getPrice());
                    intent.putExtra(ActivityProductView.ABOUTFLAG, flower.getAbout());
                    intent.putExtra(ActivityProductView.CATEGORYFLAG, flower.getCategory());
                    intent.putExtra(ActivityProductView.IDFLAG, flower.getId());

                    context.startActivity(intent);
                }
            });
        }
    }
}
