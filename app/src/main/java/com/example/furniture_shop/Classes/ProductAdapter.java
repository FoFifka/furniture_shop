package com.example.furniture_shop.Classes;

import android.content.Context;
import android.graphics.Bitmap;
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

public class ProductAdapter extends ArrayAdapter<Products> {
    private Context context;
    int resource;

    public ProductAdapter(Context context, int resource, ArrayList<Products> products) {
        super(context, resource, products);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Products product = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView name = convertView.findViewById(R.id.textView_name_cart);
        TextView description = convertView.findViewById(R.id.textView_description_cart);
        TextView price = convertView.findViewById(R.id.textView_price_cart);
        ImageView image = convertView.findViewById(R.id.imageView_cart);


        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice()+"р.");
        getImageRequest(product.getImage(), image);

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
