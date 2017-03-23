package me.azab.oa.galleryexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.azab.oa.galleryexample.adapters.RecyclerViewAdapter;
import me.azab.oa.galleryexample.models.Image;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewAdapter = new RecyclerViewAdapter(Image.getImages(),this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }
}
