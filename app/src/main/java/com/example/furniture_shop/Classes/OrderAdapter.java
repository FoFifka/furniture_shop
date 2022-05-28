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

public class OrderAdapter extends ArrayAdapter<Order> {
    private Context context;
    int resource;


    public OrderAdapter(@NonNull Context context, int resource, ArrayList<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = Integer.parseInt(getItem(position).getId());
        Order order = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textView_order_number = convertView.findViewById(R.id.textView_order_number);
        TextView textView_order_price = convertView.findViewById(R.id.textView_order_price);
        TextView textView_order_data = convertView.findViewById(R.id.textView_order_data);
        TextView textView_order_status = convertView.findViewById(R.id.textView_order_status);

        textView_order_number.setText("Заказ №"+order.getId());
        textView_order_price.setText(order.getTotalPrice()+" руб.");
        textView_order_data.setText(order.getData().substring(0, 10));
        if(order.getStatus().equals("1")) {
            textView_order_status.setText("Активен");
        } else {
            textView_order_status.setText("Закрыт");
        }


        return convertView;
    }
}
