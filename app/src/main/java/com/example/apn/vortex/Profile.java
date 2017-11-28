package com.example.apn.vortex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
  CircleImageView profilePic;
    String profilePicUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        profilePicUrl = bundle.getString("imgurl");
        profilePic = (CircleImageView) findViewById(R.id.profilepic);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setProfilePic();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProfilePic(){
        Picasso.with(getApplicationContext()).load(profilePicUrl).into(profilePic);

    }

}
