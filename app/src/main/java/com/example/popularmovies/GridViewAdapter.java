package com.example.popularmovies;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by Tarun on 16-12-2015.
 */
public class GridViewAdapter extends ArrayAdapter<URL> {

    public  GridViewAdapter(Activity context, List<URL> imageList){
        super(context,0,imageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("In Adaper","Gridadapter"+position);


       if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_movie_posters,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(getItem(position).toString()).into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);




        return convertView;
    }
}
