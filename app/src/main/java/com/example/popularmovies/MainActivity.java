package com.example.popularmovies;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.example.popularmovies.Fetchdata fetchdata = new com.example.popularmovies.Fetchdata();
        fetchdata.execute();

        GridView gridView = (GridView) findViewById(R.id.gridView);
        List<URL> imageList = new ArrayList<URL>();
       // fetchdata.execute();

       // int total = 20;   //fetchdata.getTotal_number_of_results();

        SystemClock.sleep(4000);

        int total = fetchdata.getAllObject().length;


       // int total = 0;
        RetrievedValues[] obj = new RetrievedValues[total];

        for (int i =0 ; i<total; i++){
            obj[i]=new RetrievedValues(fetchdata.getAllObject()[i]);

        }


        Log.e("Total Item Main   ",total+"!");

        if (total >20 || total <0){
            Log.e("MainActivity: ","Error retrieving total items");
        }



        for(int i = 0; i<total; i++){
            Log.e("In main: ",obj[i].get_poster_link().toString());
          imageList.add(obj[i].get_poster_link());

        }

        Log.e("List","Formed");

        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, imageList);

        gridView.setAdapter(gridViewAdapter);

    }
}
