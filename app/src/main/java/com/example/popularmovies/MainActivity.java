package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static RetrievedValues[] obj;

    public RetrievedValues[] send_data() {
        return obj;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        com.example.popularmovies.Fetchdata fetchdata = new com.example.popularmovies.Fetchdata(this);
        setContentView(R.layout.activity_main);
        if (isConnected) {

            fetchdata.execute();
        } else {
            // Toast toast = new Toast(this);
            while (!isConnected) {
                int l = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), "No Internet! Retrying after 3 seconds...", l);
                toast.show();

                SystemClock.sleep(3000);
            }

        }


        GridView gridView = (GridView) findViewById(R.id.gridView);

        // Had some issues implementing onPostExecute() as I didn't use the Asynctask class
        // as inner class of the Main Activity. Got to know that it's a bad practise.
        // Working on the app again which will implement Asynctask and onPostExecute() method.

        while (Fetchdata.FLAG != 1) {
            SystemClock.sleep(200);
        }
        Fetchdata.FLAG = 0;

        int total =  fetchdata.getAllObject().length;

        List<URL> imageList = new ArrayList<URL>();
        obj = new RetrievedValues[total];

        for (int i =0 ; i<total; i++){
            obj[i]=new RetrievedValues(fetchdata.getAllObject()[i]);

        }

        if (total >20 || total <0){
            Log.e("MainActivity: ","Error retrieving total items");
        }



        for(int i = 0; i<total; i++){
          imageList.add(obj[i].get_poster_link());
        }


        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, imageList);
        gridViewAdapter.notifyDataSetChanged();


        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

                intent.putExtra("position", i);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.refresh) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
