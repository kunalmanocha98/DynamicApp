package com.vijayjaidewan01vivekrai.dynamic_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.vijayjaidewan01vivekrai.dynamic_app.Adapters.NavDrawerCardAdapter;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.NavDrawer;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TableRecord;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TestResults;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiService;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiUtils;
import com.vijayjaidewan01vivekrai.collapsingtoolbar_github.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNavDrawer {

    LinearLayout navigationView;
    AppCompatImageView navHeaderImage;
    TextView navHeaderText;
    Context context;
    NavDrawer navDrawer;
    RecyclerView recyclerView;
    DatabaseHelper db;
    String url = "http://bydegreestest.agnitioworld.com/test/menu.php";

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @param view - gets the navigation view from the constructor
     * @param context - gets the context of the ScrollingActivity (Because it is the single acitivity in our project)
     * @param db - database helper instance is passed from the Scrolling activity
     */
    public SetNavDrawer(LinearLayout view, Context context, DatabaseHelper db) {
        navigationView = view;
        this.context = context;
        this.db = db;
        navDrawer = new NavDrawer();
    }

    // -------------------------------------------------- getJSON() - this function fetches the data from the Internet if the client is online or from the database if it is offline --------------
    public void getJSON() {
        /**
         *
         * @param recyclerView - the navigation drawer is set with the recycler view, so that it can be completely dynamic and more designs and flexibility can be added to it
         * @param navHeaderImage - this displays the header image of the navigation drawer
         * @param navHeaderText - this is the text to be displayed on the Header Image
         */
        recyclerView = navigationView.findViewById(R.id.recycler_view_nav);
        navHeaderImage = navigationView.findViewById(R.id.nav_header_image);
        navHeaderText = navigationView.findViewById(R.id.nav_header_text);

        if (isNetworkAvailable()) {

            ApiService apiService = ApiUtils.getAPIService();
            apiService.results(url).enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if (response.isSuccessful()) {

                        //DATA RECEIVED FROM THE URL WILL BE SAVED IN THE DATABASE
                        TableRecord record = new TableRecord(url);
                        record.setData(new Gson().toJson(response.body()));
                        db.addRecord(record);

                        //THE response WILL BE PARSED TO GET THE navDrawer DATA IN ITS MODEL CLASS
                        navDrawer = response.body().getResults().getNavDrawer();
                        setDrawer();
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Log.e("URL error", t.getLocalizedMessage());
                }
            });
        } else {

            //DATA FETCHED FROM THE DATABASE AGAINST THE URL
            TableRecord record = new TableRecord(url);
            db.getRecord(record);
            TestResults results = new Gson().fromJson(record.getData(), TestResults.class);

            navDrawer = results.getResults().getNavDrawer();
            setDrawer();
        }

//        setDrawer();
    }

    // -------------------------------------------------- setDrawer() - this function will set all the values of the navigation drawer -----------------------------------------------------------
    public void setDrawer() {

        navHeaderText.setText(navDrawer.getHeader_layout().getText());

        Glide.with(context)
                .load(navDrawer.getHeader_layout().getImage())
                .placeholder(R.drawable.grey)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(navHeaderImage);

        //HERE RECYCLER VIEW IS SET, AND AN ADAPTER IS ATTACHED TO IT.
        //HERE YOU CAN APPLY DIFFERENT DESIGNS AS WE APPLIED IN THE SCROLLING ACTIVITY FOR MAIN RECYCLER VIEW TO DISPLAY THE DATA
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        NavDrawerCardAdapter cardAdapter = new NavDrawerCardAdapter(navDrawer.getMenu_items(), context);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();
        cardAdapter.setClickListener((OnClickSet) context);                 // This will set the onClickListener for the items in the menu

        navigationView.setBackgroundColor(Color.parseColor(navDrawer.getNav_drawer_bg_color()));
    }

    // -------------------------------------------------- is Network Available - function to check the internet connectivity ----------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}