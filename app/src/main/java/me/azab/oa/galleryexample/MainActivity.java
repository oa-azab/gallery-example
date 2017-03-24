package me.azab.oa.galleryexample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.azab.oa.galleryexample.adapters.RecyclerViewAdapter;
import me.azab.oa.galleryexample.models.Image;
import me.azab.oa.galleryexample.utils.PrefUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView mEmptyTextView;
    List<Image> mList = new ArrayList<>();
    OkHttpClient mClient = new OkHttpClient();
    String ApiURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=cca5c934cb35f3b62ad20ff75b5c3af0&format=json&nojsoncallback=1&extras=url_l&safe_search=for%20safe&per_page=20&tags=bird";
    final static String LAST_API_RESPONSE = "last_api_response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find UI
        mEmptyTextView = (TextView) findViewById(R.id.text_view_empty_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Set RecyclerView and the Adapter
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewAdapter = new RecyclerViewAdapter(mList, this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh list then call setRefreshing(false)
                makeApiCall(ApiURL);
            }
        });

        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            String lastResponse = PrefUtils.getFromPrefs(getApplication(), LAST_API_RESPONSE, null);
            if (lastResponse != null) {
                // get list of image objects from the response
                List<Image> mImages = parseJSON(lastResponse);

                // clear data set
                mList.clear();

                //add Images to data set
                for (Image image : mImages) {
                    mList.add(image);
                }
                // Call update ui from UI thread.
                updateUI();
            }else{
                makeApiCall(ApiURL);
            }
        } else {
            // Probably initialize members with default values for a new instance
            makeApiCall(ApiURL);
        }

        //Check if data set is empty
        checkIfDataSetIsEmpty();
    }

    /**
     * This Method make api request and then update the UI
     * if there are no connection it UI with last response
     *
     * @param url url of API rquest
     */
    public void makeApiCall(String url) {
        // build request
        Request mRequest = new Request.Builder()
                .url(url)
                .build();

        // make asynchronous call
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("TAGNOCON", "NOO CONNECTION");
                String lastResponse = PrefUtils.getFromPrefs(getApplication(), LAST_API_RESPONSE, null);
                if (lastResponse != null) {
                    // get list of image objects from the response
                    List<Image> mImages = parseJSON(lastResponse);

                    // clear data set
                    mList.clear();

                    //add Images to data set
                    for (Image image : mImages) {
                        mList.add(image);
                    }
                    // Call update ui from UI thread.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                }
                // Stop refreshing state
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // get response
                String mResponse = response.body().string();

                // save response to use it later if there are no connection
                PrefUtils.saveToPrefs(getApplication(), LAST_API_RESPONSE, mResponse);

                // get list of image objects from the response
                List<Image> mImages = parseJSON(mResponse);

                // clear data set
                mList.clear();

                //add Images to data set
                for (Image image : mImages) {
                    mList.add(image);
                }
                // Call update ui from UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }

    /**
     * This method update UI after fetching data from Api
     */
    private void updateUI() {
        mRecyclerViewAdapter.notifyDataSetChanged();
        checkIfDataSetIsEmpty();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * This method parse json string into list of images
     *
     * @param Json json response as string
     * @return list of Images
     */
    private List<Image> parseJSON(String Json) {
        List<Image> mImages = new ArrayList<>();
        try {
            JSONObject main = new JSONObject(Json);
            JSONObject photos = main.getJSONObject("photos");
            JSONArray array = photos.getJSONArray("photo");
            for (int i = 0; i < array.length(); i++) {
                JSONObject mObj = array.getJSONObject(i);
                String title = mObj.getString("title");
                String url = mObj.getString("url_l");
                Image mImage = new Image(url, title);
                Log.d("JSONTAG", title + " ::: " + url);
                mImages.add(mImage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mImages;
    }


    /**
     * This method check if data set is empty if so show message notify the user
     */
    public void checkIfDataSetIsEmpty() {
        // Check if data set is empty show message
        if (mList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyTextView.setVisibility(View.GONE);
        }
    }
}
