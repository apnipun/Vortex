package com.example.apn.vortex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apn.vortex.Common.Common;
import com.squareup.picasso.Picasso;

public class HomeEventDetalis extends AppCompatActivity {

    TextView description;
    ImageView imageForMe;
    TextView eventName,eventTime,eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_event_detalis);


        imageForMe = (ImageView) findViewById(R.id.imageView);
        description = (TextView) findViewById(R.id.description);
        eventName = (TextView) findViewById(R.id.eventname);
        eventDate = (TextView) findViewById(R.id.eventdate);
        eventTime = (TextView) findViewById(R.id.eventtime);

        if(getIntent() != null){
            int index = getIntent().getIntExtra("id",-1);
            if(index != -1){
                loadDetalis(index);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void loadDetalis(int index) {

        HomeEventsList homeEventsList = Common.homeEventsLists.get(index);
        Picasso.with(getBaseContext()).load(homeEventsList.getImageURL()).into(imageForMe);
        description.setText(homeEventsList.getDescriptopn());
        eventTime.setText(homeEventsList.getTime());
        eventDate.setText(homeEventsList.getDate());
        eventName.setText(homeEventsList.getName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
