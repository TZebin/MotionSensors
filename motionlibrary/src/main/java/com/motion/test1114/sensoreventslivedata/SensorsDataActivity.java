package com.motion.test1114.sensoreventslivedata;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.motion.test1114.motionsensors.R;
import com.motion.test1114.service.MotionForeGroundService;
import com.motion.test1114.utils.Constants;


public class SensorsDataActivity extends AppCompatActivity  {

    private SensorsEventsAdapter sensorsEventsAdapter;
    private SensorsViewModel sensorsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        stopMotionSensorService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorsEventsAdapter = new SensorsEventsAdapter(this);

        sensorsViewModel = ViewModelProviders.of(this).get(SensorsViewModel.class);
        sensorsViewModel.getAllPosts().observe(this, posts -> sensorsEventsAdapter.setData(posts));

        RecyclerView recyclerView = findViewById(R.id.rvEventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sensorsEventsAdapter);
    }



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addPost) {
            sensorsViewModel.savePost(new Post("This is a post title", "This is a post content"));
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    public void onDeleteButtonClicked(Post post) {
        sensorsViewModel.deletePost(post);

    }*/

    private void stopMotionSensorService(){
        //Intent startIntent = new Intent(SensorsDataActivity.this, MotionSensorService.class);
        Intent startIntent = new Intent(SensorsDataActivity.this, MotionForeGroundService.class);

        startIntent.setAction(Constants.ACTION.STOP_ACTION);
        startService(startIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            //lower then Oreo, just start the service.
            startService(startIntent);
        }
    }
}
