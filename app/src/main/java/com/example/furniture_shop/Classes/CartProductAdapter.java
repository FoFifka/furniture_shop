package com.example.furniture_shop.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture_shop.Activities.CartActivity;
import com.example.furniture_shop.Activities.MainActivity;
import com.example.furniture_shop.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class CartProductAdapter extends ArrayAdapter<CartProducts> {
    Context context;
    int resource;

    public CartProductAdapter(@NonNull Context context, int resource, ArrayList<CartProducts> cartProducts) {
        super(context, resource, cartProducts);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CartProducts cartProduct = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView name = convertView.findViewById(R.id.textView_name_cart);
        TextView description = convertView.findViewById(R.id.textView_description_cart);
        TextView count = convertView.findViewById(R.id.textView_count_cart);
        TextView price = convertView.findViewById(R.id.textView_price_cart);
        ImageView image = convertView.findViewById(R.id.imageView_cart);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox_cart_product);
        checkBox.setTag(position);
        if(CartActivity.isActionMode) {
            checkBox.setVisibility(View.VISIBLE);
        } else  {
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int)buttonView.getTag();

                if(CartActivity.UserSelection.contains(CartActivity.cartProducts.get(position))) {
                    CartActivity.UserSelection.remove(CartActivity.cartProducts.get(position));
                } else {
                    CartActivity.UserSelection.add(CartActivity.cartProducts.get(position));
                }

                CartActivity.actionMode.setTitle(CartActivity.UserSelection.size()+" Предметов выбранно..");
            }
        });

        name.setText(cartProduct.getName());
        description.setText(cartProduct.getDescription());
        count.setText(cartProduct.getCount());
        price.setText(cartProduct.getPrice());
        getImageRequest(cartProduct.getImage(), image);


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
