package com.example.bloomroom.Adaptors;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bloomroom.Models.Category;

import java.util.List;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private List<Category> categoryList;
    private LayoutInflater inflater;

    public CategorySpinnerAdapter(Context context, List<Category> categoryList) {
        super(context, 0, categoryList);
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Category category = categoryList.get(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(category.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Category category = categoryList.get(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(category.getName());

        return convertView;
    }
}
