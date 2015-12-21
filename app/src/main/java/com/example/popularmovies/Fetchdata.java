package com.example.popularmovies;

import android.os.AsyncTask;
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

    public RetrievedValues[] allObjects;

    public int total_number_of_results = -1;

    public int getTotal_number_of_results(){
        return total_number_of_results;
    }

    public static URL[] urls;

    public RetrievedValues[] getAllObject(){
        return this.allObjects;
    }

    protected RetrievedValues[] retrievefromJSON(String jsonStr) throws JSONException{

        JSONObject moviesJSON = new JSONObject( jsonStr);
        JSONArray movies_list = moviesJSON.getJSONArray("results");

         int num_results = movies_list.length();
        total_number_of_results=num_results;
         urls = new URL[num_results];

        RetrievedValues[] retrieved_values_objects = new RetrievedValues[num_results];
        allObjects = new RetrievedValues[num_results];
        Log.e("Fetchd_RetrievedObj","Created");

        String temp = ""+num_results+"!";
       // retrieved_values_objects[0].set_total_items(num_results);

        Log.e("Total Items: ", temp);

        for (int i = 0; i < num_results; i++) {
            JSONObject movie_details =movies_list.getJSONObject(i);
            String movie_title = movie_details.getString("title");
            String movie_poster_link = movie_details.getString("backdrop_path");
            String movie_overview = movie_details.getString("overview");
            String movie_rating = movie_details.getString("vote_average");
            String movie_release_date = movie_details.getString("release_date");

           // Log.e("JSON Parsed: ", movie_title);



          /*  retrieved_values_objects[i].set_title(movie_title);
            retrieved_values_objects[i].set_poster_link(movie_poster_link);
            retrieved_values_objects[i].set_overview(movie_overview);
            retrieved_values_objects[i].set_rating(movie_rating);
            retrieved_values_objects[i].set_release_date(movie_release_date);  */

            retrieved_values_objects[i] = new RetrievedValues (movie_title, movie_poster_link, movie_overview, movie_rating, movie_release_date);
            retrieved_values_objects[i].set_total_items(num_results);

            allObjects[i] = new RetrievedValues(movie_title, movie_poster_link, movie_overview, movie_rating, movie_release_date);
            allObjects[i].set_total_items(num_results);

            try {
                urls[i] = new URL("http://image.tmdb.org/t/p/w185" + movie_details.getString("backdrop_path"));
            } catch (MalformedURLException u){
                Log.e("URL const Fetchdata","Error");
            }
            // Log.e("Check Url", urls[i].toString());

          //  Log.e("Values Written: ", movie_title);
        }

        Log.e("Fetchd_RetrievedObj","Loops entertained success");

       // allObjects = retrieved_values_objects;

        Log.e("Fetchd_RetrievedObj","objects assigned success");

        return retrieved_values_objects;
    }

    @Override
    protected RetrievedValues[] doInBackground(String... params){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        /*int numDays = 7;
        String format ="json";
        String units = "metric";  */
        // Will contain the raw JSON response as a string.
        String fetchedJSONStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7"+"&APPID=62cd37af3656ca4890ca1d5bddcee732");

            String base_URL = "http://api.themoviedb.org/3/discover/movie";


            //TODO Remove API KEY AND ADD SETTINGS Feature


            String api_key = "?api_key="+BuildConfig.TMDb_KEY;
            URL url = new URL(base_URL.concat(api_key));



            // Log.w("CHECK: ", url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");

            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            fetchedJSONStr = buffer.toString();
        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    try {
                        return retrievefromJSON(fetchedJSONStr);
                    }catch (JSONException p){
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
