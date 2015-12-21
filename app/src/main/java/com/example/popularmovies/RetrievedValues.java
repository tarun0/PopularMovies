package com.example.popularmovies;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tarun on 12-12-2015.
 */
public class RetrievedValues {
    private String original_title;
    private URL poster_link;
    private String overview;
    private String rating;
    private String release_date;
    public int total_items = -1;

    public RetrievedValues(RetrievedValues copy){
        original_title=copy.get_title();
        poster_link=copy.get_poster_link();
        overview=copy.get_overview();
        rating=copy.get_rating();
        release_date=copy.get_release_date();
        total_items=copy.get_total_items();
    }



    public RetrievedValues(String title, String poster_path, String over_view, String rate, String rel_date){
        original_title = title;
        overview=over_view;
        rating = rate;
        release_date=rel_date;

        set_poster_link(poster_path);
    }



    public  void set_title(String t){
        original_title = t;

    }

    public void set_poster_link (String string){
        String base_url = "http://image.tmdb.org/t/p/";
        String size = "w185";
        URL url = null;

        try {
            url = new URL("http://image.tmdb.org/t/p/w185" + string);
        } catch (MalformedURLException u){
            Log.e("Retrieved class","URL construct Error");
        }


        poster_link = url;
    }

    public void set_overview(String t){
        overview = t;
    }

    public void set_total_items(int i ) { total_items=i;}

    public void set_rating(String t){
        rating =t;
    }

    public void set_release_date(String t){
        release_date =t;
    }

    public String get_title(){
        return original_title;
    }

    public URL get_poster_link(){
        return poster_link;
    }

    public String get_overview(){
        return overview;
    }

    public String get_release_date(){
        return release_date;
    }

    public String get_rating(){
        return rating;
    }

    public int get_total_items(){return total_items;}

}
