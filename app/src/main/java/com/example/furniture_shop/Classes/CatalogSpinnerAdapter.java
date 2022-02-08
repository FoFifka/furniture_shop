package com.example.furniture_shop.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture_shop.R;

import java.util.ArrayList;

public class CatalogSpinnerAdapter extends ArrayAdapter<CatalogCategories> {

    public CatalogSpinnerAdapter(@NonNull Context context,ArrayList<CatalogCategories> categoriesArrayList) {
        super(context, 0, categoriesArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CatalogCategories category = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_item, parent, false);
        }

        TextView categoryNameTextView = convertView.findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category.getCategory_name());

        return  convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CatalogCategories category = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_item, parent, false);
        }

        TextView categoryNameTextView = convertView.findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(category.getCategory_name());

        return  convertView;
    }
}
