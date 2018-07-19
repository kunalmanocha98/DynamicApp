package com.vijayjaidewan01vivekrai.dynamic_app;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.vijayjaidewan01vivekrai.collapsingtoolbar_github.R;
import com.vijayjaidewan01vivekrai.dynamic_app.Adapters.CardAdapter;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Data;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Login;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Results;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TableRecord;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TestResults;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiService;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingActivity extends AppCompatActivity implements OnClickSet {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar mToolbar;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private LinearLayout linearLayout, mainLinear, no_internet_bar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout navigationView;
    private FrameLayout frame;
    private Button btn_retry;
    CardAdapter mCardAdapter;
    int searchValue = 1;
    static String BASE_URL = "http://bydegreestest.agnitioworld.com/test/mobile_app.php";
    String backUrl;
    ProgressBar progressBar;
    ArrayList<Data> mArrayList;
    DatabaseHelper db;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        /**
         *
         * @param drawerLayout - it is root layout of all the other layouts and items
         * @param mainLinear - it contains the whole layout excluding the nav drawer
         * @param coordinatorLayout - it contains the collapsing toolbar and the recycler view
         * @param linearLayout - it is used to show the other layout which is other than the coordinatorlayout (containing collapsing toolbar), it contains a normal toolbar and recycler view
         * @param appBarLayout -  it is used to access the properties if toolbar and collapsingToolbar
         */
        drawerLayout = findViewById(R.id.drawer_layout);
        mainLinear = findViewById(R.id.main_linear);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        linearLayout = findViewById(R.id.linear_layout);
        appBarLayout = findViewById(R.id.app_bar);
        frame = findViewById(R.id.frame);
        no_internet_bar =  findViewById(R.id.no_internet_bar);

        /**
         *
         * @param navigationView - this view is passed to the SetNavDrawer class to set the navigation drawer as per the api
         * @param toolbar - it is the toolbar of coordinatorLayout
         * @param mToolbar - it is the toolbar of linearLayout
         * @param progressBar - it is the progressBar which is displayed every time the data is loaded from a url
         */
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        mToolbar = findViewById(R.id.tool_bar);
        progressBar = findViewById(R.id.progressBar);
        btn_retry =  findViewById(R.id.btn_retry);

        /**
         *
         * @param mArraylist - it is the array list used for the search and it is passed to the CardAdapter class
         * @param db - it is instance of the DatabaseHelper class used for storing and retrieving data from SQLite Database
         */
        mArrayList = new ArrayList<>();
        db = new DatabaseHelper(ScrollingActivity.this, "Information", null, 1);

        showOfflineBar();
        //It checks if the network is available then get the data from the api, if not then retrieve it from the Database
        if (isNetworkAvailable()) {
            // notify user you are online
            setProgressBarIndeterminate(true);
            callHttp(BASE_URL);
            setProgressBarIndeterminate(false);

        } else {
            mainLinear.setVisibility(View.VISIBLE);
            progressBar.clearFocus();
            progressBar.setVisibility(View.GONE);

            offlineMode();
        }
    }

    //Offline Mode Function - It displays the data from the database, if present, else it displays offline fragment -----------------------------
    public void offlineMode() {

        // TableRecord is a model which stores URL, Data, and time
        TableRecord record = new TableRecord(BASE_URL);
        //It will fetch the data by mapping the url
        // present in the record and then set it to the record
        db.getRecord(record);

        /**
         * If the Data is found then convert it into the Object of TestResults
         * and call the setView() method to start creating the view as per the record
         * If the data is not found then replace the view with Offline fragment
         */
        if (record.getData() != null) {
            TestResults results = new Gson().fromJson(record.getData(), TestResults.class);
            setView(results.getResults());
        } else {
            offlineFragment();
        }
    }

    //SnackBar Function - to display the snackBar at the top (below the status  bar) ----------------------------------------------------------
    public void showOfflineBar() {

        CoordinatorLayout.LayoutParams params =  (CoordinatorLayout.LayoutParams) frame.getLayoutParams();

        if(isNetworkAvailable())
        {
            params.topMargin = 0;
            no_internet_bar.setVisibility(View.GONE);
        }
        else
        {
            params.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40.0f,getResources().getDisplayMetrics());
            no_internet_bar.setVisibility(View.VISIBLE);
        }
        frame.setLayoutParams(params);

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callHttp(BASE_URL);
            }
        });

//        Snackbar snackbar = Snackbar.make(frame,"Network not available",Snackbar.LENGTH_LONG)
//                                    .setDuration(Snackbar.LENGTH_INDEFINITE)
//                                    .setAction("RETRY", new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if(isNetworkAvailable())
//                                            {
//                                                callHttp(BASE_URL);
//                                            }
//                                            else
//                                            {
//                                                showSnackBar();
//                                            }
//                                        }
//                                    });
//
//        View view = snackbar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        params.topMargin = 50;
//        params.height = 150;
//        view.setLayoutParams(params);
//
//        snackbar.show();

    }

    //Offline Fragment Function - to replace the layout with offline fragment ---------------------------------------------------------------
    public void offlineFragment() {

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        coordinatorLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.drawer_layout, new Offline_fragment());
        ft.commit();
    }

    //CallHttp function - takes the input as the URL, then fetch the data from it and pass it to the setView() to create view -----------------
    public void callHttp(final String URL) {

        // It is assigned to BASE_URL, so that the other function
        // which wants to refresh the content can pass the same URL always
        BASE_URL = URL;

        showOfflineBar();
        mArrayList.clear();
        if (isNetworkAvailable()) {
            ApiService apiService = ApiUtils.getAPIService();

            // PROGRESS BAR TO BE SHOWN TO THE USER BEFORE THE DATA IS LOADED
            mainLinear.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.findFocus();

            apiService.results(URL).enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getMsg().equals("success")) {

                            //DISAPPEAR THE PROGRESS BAR SHOWN EARLIER
                            mainLinear.setVisibility(View.VISIBLE);
                            progressBar.clearFocus();
                            progressBar.setVisibility(View.GONE);

                            Log.d("messageJSON", new Gson().toJson(response.body()));

                            //STORE THE DATA IN THE DATABASE BY CONVERTING THE response IN TO JSON TO STORE IT IN THE DATABASE
                            Results results = response.body().getResults();
                            TableRecord record = new TableRecord(URL);
                            record.setData(new Gson().toJson(response.body()));
                            db.addRecord(record);

                            setView(results);
                        } else {
//                            Toast.makeText(ScrollingActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            Log.i("Message ", response.body().getMsg());
                        }
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Log.e("Url error", t.getLocalizedMessage());
                }
            });
        } else {
            offlineMode();
        }
    }

    // Set View Function - It takes input as Results object(It stores all the attributes required to adjust the view) ---------------------------
    // it segregates and assign it to the different variables or pass it to the different functions ---------------------------------------------
    void setView(Results results) {

        /**
         * @param drawerValue - it is used to decide whether to show the back button or navigation drawer
         * @param collapseValue - it is used to decide whether to show a collapsing toolbar or a normal toolbar
         */
        int drawerValue = Integer.parseInt(results.getToolBar().getIs_back());
        int collapseValue = Integer.parseInt(results.getToolBar().getTop_image());

        Log.d("Collapse", "" + collapseValue);
        Log.d("Drawer", "" + drawerValue);

        /**
         * @value drawerValue = 0 => Navigation Button
         * @value drawerValue = 1 => Back Button
         */
        if (drawerValue == 0)
            backUrl = null;
        else if (drawerValue == 1)
            backUrl = results.getToolBar().getBack_url();

        setCollapse(collapseValue, results);
        setNavigation(drawerValue);
        mArrayList.addAll(results.getData());

        int viewType = Integer.parseInt(results.getView_type());
        Log.d("View Type", "" + viewType);

        //ANY viewType TILL 4 WILL HAVE THE SAME CODE, THE ADAPTER WILL BE INTIALIZED WITH DATA CAME INTO THE JSON
        // AND THEN IT WILL BE ARRANGED IN THE RECYCLER VIEW AS PER THE viewType

        // viewType WILL DIFFERENTIATE BETWEEN THE FOUR CARDS CREATED, WHICH ONE SHOULD BE CHOSEN
        switch (viewType) {
            case 1:
            case 2:
            case 3:
            case 4:
                mCardAdapter = new CardAdapter(results.getData(), mArrayList, ScrollingActivity.this, viewType);
                recyclerView.setAdapter(mCardAdapter);
                mCardAdapter.notifyDataSetChanged();
                mCardAdapter.setClickListener(ScrollingActivity.this);
                break;
            case 5: //WEBVIEW
                if (isNetworkAvailable())
                    webView(results.getWeb_view_url());
                else
                    offlineFragment();
                break;
            case 6: //LOGIN
                if (isNetworkAvailable())
                    setLogin(results.getLogin());
                else
                    offlineFragment();
                break;
            default:
                Log.e("View Type", "Wrong view Type value - " + viewType);
        }
    }

    // Is Network Available Function - It will check whether there is the Internet connectivity present and returns a boolean -----------------
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    //Web View Function - It will replace the drawerLayout with webView Fragment for viewType = 5 ---------------------------------------------
    public void webView(String url) {
        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url_key", url);
        webViewFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_layout, webViewFragment);
        ft.addToBackStack("");
        ft.commit();
    }

    // Set Collapse Function - this function deals with the toolbar and its different attributes -----------------------------------------------
    private void setCollapse(int collapseValue, Results results) {

        /**
         * @value collapseValue = 1 => Normal toolbar
         * @value collapseValue = 2 => Collapsing toolbar
         */
        if (collapseValue == 1) {
            recyclerView = findViewById(R.id.recyclerViewLinear);
            swipeRefreshLayout = findViewById(R.id.swipe);

            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(results.getToolBar().getCollapsed_top_title());
            mToolbar.setTitleTextColor(Color.parseColor(results.getToolBar().getCollapsed_top_title_color()));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(results.getToolBar().getExtended_top_title_color())));

            coordinatorLayout.setVisibility(View.GONE);
        }
        if (collapseValue == 2) {
            recyclerView = findViewById(R.id.recycler_view);
            swipeRefreshLayout = findViewById(R.id.swipe_coordinator);

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(results.getToolBar().getExtended_top_title());

            /*
            Code to set the color of the collapsing toolbar (after it is collapsed)
            ***
//            collapsingToolbarLayout.setContentScrim(new ColorDrawable(Color.parseColor("#ff00ff")));
            ***
            */

            linearLayout.setVisibility(View.GONE);
            appBarLayout.setExpanded(true);
            mToolbar.setVisibility(View.GONE);

            //SET THE PROFILE IMAGE
            RoundedImage roundedImage = findViewById(R.id.rounded_image);
            Glide.with(this)
                    .load(results.getToolBar().getTop_image_fg())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(roundedImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });

            //SET THE BACKGROUND IMAGE OF THE COLLAPSING TOOLBAR
            AppCompatImageView background = findViewById(R.id.backImage);
            Glide.with(ScrollingActivity.this)
                    .load(results.getToolBar().getTop_image_bg())
                    .placeholder(R.drawable.grey)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(background);
        }

        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeColors(Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callHttp(BASE_URL);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /**
         *
         * @param col - it denotes the columns in the view
         * @param orientation - it denotes the scrolling orientation of the view, whether it is horizontal or vertical
         */
        int col = Integer.parseInt(results.getGrid_columns());
        int orientation = Integer.parseInt(results.getGrid_orientation());

        Log.d("Columns", "" + col);
        Log.d("Orientation", "" + orientation);

        setRecyclerView(col, orientation);
    }

    // Set RecyclerView - this function manages the layout of the recyclerView -----------------------------------------------------------------
    private void setRecyclerView(int columns, int orientation) {
        // Setting the recycler view
        recyclerView.setHasFixedSize(true);
        if (columns == 0)
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        else {
            switch (orientation) {
                case 1:
                    orientation = LinearLayoutManager.VERTICAL;
                    break;
                case 2:
                    orientation = LinearLayoutManager.HORIZONTAL;
                    break;
                default:
                    Log.e("Orientation", "Wrong orientation value provided.  -  " + orientation);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
            gridLayoutManager.setOrientation(orientation); // set Horizontal Orientation
            recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        }
    }

    // Set Navigation - this function checks the value of drawerValue and acts accordingly -----------------------------------------------------
    private void setNavigation(int drawerValue) {
        if (drawerValue == 0) {
            backUrl = null;
            drawerLayout = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(ScrollingActivity.this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //THIS WILL SET THE NAVIGATION DRAWER THROUGH SetNavDrawer class
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetNavDrawer navDrawer = new SetNavDrawer(navigationView, ScrollingActivity.this, db);
                    navDrawer.getJSON();
                    drawerLayout.closeDrawers();
                }
            }, 100);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                    Log.i("Back Url", backUrl);
                    callHttp(backUrl);
                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                    Log.i("Back Url", backUrl);
                    callHttp(backUrl);
                }
            });
        }
    }

    // Set Login - it will set the Login Fragment replacing the drawer layout for viewType = 6 -------------------------------------------------
    private void setLogin(Login login) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.frame, fragment);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Login", login);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

        setNavigation(0);
    }

    @Override
    public void onBackPressed() {

        // TO CLOSE THE SEARCH BAR IF IT IS OPEN
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        } else {
            // IF backUrl IS NOT PRESENT THEN CLOSE THE APPLICATION ELSE PASS THE URL TO THE callHttp() method
            if (backUrl == null)
                super.onBackPressed();
            else {
//                Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                Log.i("On back pressed", backUrl);
                callHttp(backUrl);
            }
        }
    }

    // On Click Function - it is the method of OnClickSet Interface,
    // which is used by the adapters to call and pass the url to callHttp()
    @Override
    public void onClickFunction(String url) {
        drawerLayout.closeDrawers();
        callHttp(url);
        Log.i("IN Scrolling", url);
    }

    // Search Bar controlled by searchValue = 0(Not Present), 1(Present)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        if (searchValue == 1) {
            inflater.inflate(R.menu.search_layout, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_view));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public static final String TAG = "TAG";

                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.d(TAG, "onQueryTextSubmit: called:");
                    mCardAdapter.getFilter().filter(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    mCardAdapter.getFilter().filter(s);
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    // SetLayout Interface - it is used to call the callHttp function from Login Fragment and Offline Fragment ---------------------------------
    public interface SetLayout {
        void setUrl(String url);
    }
}