package com.example.furniture_shop.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.R;

import java.util.ArrayList;

public class CatalogCategoryAdapter extends ArrayAdapter<CatalogCategories> {
    private Context context;
    int resource;

    public CatalogCategoryAdapter(Context context, int resource, ArrayList<CatalogCategories> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = Integer.parseInt(getItem(position).getCategory_id());
        CatalogCategories category = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textView_catalog_category = convertView.findViewById(R.id.textView_catalog_category);
        ImageView imageView_catalog_category = convertView.findViewById(R.id.imageView_catalog_category);

        textView_catalog_category.setText(category.getCategory_name());
        getImageRequest(category.getCategory_image(), imageView_catalog_category);

        return convertView;
    }

    void getImageRequest(String url, ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(imageRequest);
    }

}
