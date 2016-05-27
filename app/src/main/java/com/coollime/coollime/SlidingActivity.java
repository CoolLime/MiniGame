package com.coollime.coollime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SlidingActivity extends AppCompatActivity {

    SlidingSurface mGameView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);

        mGameView = (SlidingSurface) findViewById(R.id.mGameView);

    }
    /*public void onBackPressed(){
        super.onBackPressed();
    }*/

}
