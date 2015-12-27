package com.example.popularmovies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Tarun on 12-12-2015.
 */
public class Fetchdata extends AsyncTask<String, Void, RetrievedValues[]> {

    public static URL[] urls;
    public static int FLAG = 0;     //Workaround for checking if the Asynctask is completed or not.
    public RetrievedValues[] allObjects;
    public int total_number_of_results = -1;
    Activity mActivity;

    public Fetchdata(Activity activity) {
        super();
        mActivity = activity;
    }

    public RetrievedValues[] getAllObject() {
        return this.allObjects;
    }

    protected RetrievedValues[] retrievefromJSON(String jsonStr) throws JSONException {

        JSONObject moviesJSON = new JSONObject(jsonStr);
        JSONArray movies_list = moviesJSON.getJSONArray("results");

        int num_results = movies_list.length();
        total_number_of_results = num_results;
        urls = new URL[num_results];

        RetrievedValues[] retrieved_values_objects = new RetrievedValues[num_results];
        allObjects = new RetrievedValues[num_results];

        String temp = "" + num_results + "!";


        for (int i = 0; i < num_results; i++) {
            JSONObject movie_details = movies_list.getJSONObject(i);
            String movie_title = movie_details.getString("title");
            String movie_poster_link = movie_details.getString("poster_path");
            String movie_overview = movie_details.getString("overview");
            String movie_rating = movie_details.getString("vote_average");
            String movie_release_date = movie_details.getString("release_date");

            retrieved_values_objects[i] = new RetrievedValues(movie_title, movie_poster_link, movie_overview, movie_rating, movie_release_date);
            retrieved_values_objects[i].set_total_items(num_results);

            allObjects[i] = new RetrievedValues(movie_title, movie_poster_link, movie_overview, movie_rating, movie_release_date);
            allObjects[i].set_total_items(num_results);

            try {
                urls[i] = new URL("http://image.tmdb.org/t/p/w342" + movie_details.getString("poster_path"));
            } catch (MalformedURLException u) {
                Log.e("URL construction", "Error");
            }

        }

        FLAG = 1;
        return retrieved_values_objects;
    }

    @Override
    protected RetrievedValues[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String fetchedJSONStr = null;

        try {

            String base_URL = "http://api.themoviedb.org/3/discover/movie";
            String api_key = "&api_key=" + BuildConfig.TMDb_KEY;


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
            String user_setting = sharedPreferences.getString("sortby", "Most Popular");

            URL url;

            if (user_setting.contentEquals("Most Popular")) {
                url = new URL(base_URL.concat("?sort_by=popularity.desc").concat(api_key));
            } else {
                url = new URL(base_URL.concat("?sort_by=vote_count.desc").concat(api_key));
            }


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }


            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");

            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            fetchedJSONStr = buffer.toString();
        } catch (IOException e) {
            Log.e("FetchWeather", "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    try {
                        return retrievefromJSON(fetchedJSONStr);
                    } catch (JSONException p) {
                        Log.e("JSON Error", "Error parsing JSON", p);
                    }
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }

}
