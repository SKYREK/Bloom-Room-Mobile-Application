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
import com.example.bloomroom.Models.Category;
import com.example.bloomroom.R;
import com.example.bloomroom.Utils.ImageUtils;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {

    private List<Category> categoryList;
    private Context context;


    public AdminCategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_category_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryName;
        private ImageButton deleteButton;
        private ImageButton editButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.category_name);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public void bind(Category category) {
            categoryName.setText(category.getName());
            ImageUtils.loadImageFromUrl(context, category.getImage(), categoryImage);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category.deleteCategory(category.getId(), new Category.CategoryCallback() {
                        @Override
                        public void onCategoriesLoaded(List<Category> categories) {
                            categoryList.remove(category);
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
                    Intent intent = new Intent(context, AddCategoryActivity.class);
                    intent.putExtra(AddCategoryActivity.DESCRIPTIONFLAG,category.getDescription());
                    intent.putExtra(AddCategoryActivity.IMAGEFLAG,category.getImage());
                    intent.putExtra(AddCategoryActivity.NAMEFLAG,category.getName());
                    intent.putExtra(AddCategoryActivity.IDFLAG,category.getId());
                    context.startActivity(intent);

                }
            });
        }
    }
}
