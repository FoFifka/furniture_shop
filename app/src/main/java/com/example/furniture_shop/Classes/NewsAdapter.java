package com.example.furniture_shop.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture_shop.R;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context context;
    int resource;

    public NewsAdapter(@NonNull Context context, int resource, ArrayList<News> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = Integer.parseInt(getItem(position).getId());
        News news = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textView_news_title = convertView.findViewById(R.id.textView_news_title);
        TextView textView_news_text = convertView.findViewById(R.id.textView_news_text);

        textView_news_title.setText(news.getTitle());
        textView_news_text.setText(news.getText());

        return convertView;
    }
}
