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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private LinearLayout linearLayout, mainLinear;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private SwipeRefreshLayout swipeRefreshLayoutCoordinator;
//    private View login;
    private LinearLayout navigationView;
    //    private CollapsingToolbarLayout collapsingToolbarLayout;
    CardAdapter mCardAdapter;
    //    int drawerValue = 2;
    int searchValue = 1;
    String BASE_URL = "http://bydegreestest.agnitioworld.com/test/mobile_app.php";
    String backUrl;
    ProgressBar progressBar;
    ArrayList<Data> mArrayList;
    DatabaseHelper db;
    ConnectivityManager conMgr;
    SearchView searchView;

//    LruCache<String,Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        mainLinear = findViewById(R.id.main_linear);
        linearLayout = findViewById(R.id.linear_layout);
        appBarLayout = findViewById(R.id.app_bar);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        mToolbar = findViewById(R.id.tool_bar);
//        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        mArrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);

        db = new DatabaseHelper(ScrollingActivity.this, "Information", null, 1);

        //layout = findViewById(R.id.layout_content);

        //cardDataList = new ArrayList<>();
        if (isNetworkAvailable()) {
            // notify user you are online
            //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
            setProgressBarIndeterminate(true);
            callHttp(BASE_URL);
            setProgressBarIndeterminate(false);

        } else {
            mainLinear.setVisibility(View.VISIBLE);
            progressBar.clearFocus();
            progressBar.setVisibility(View.GONE);

            TableRecord record = new TableRecord(BASE_URL);
            db.getRecord(record);
            if(record.getData() == null)
            {
                offlineFragment();
            }
            else
            {
                    TestResults results = new Gson().fromJson(record.getData(),TestResults.class);
                    setView(results.getResults());
            }
        }
    }

 /*   public Results setValues(DatabaseHelper databaseHelper) {
        Results results = databaseHelper.readResults();
        results.setData(databaseHelper.readData());
        mArrayList.addAll(results.getData());
        results.setToolBar(databaseHelper.readToolbar());
        return results;
    }
*/
    public void offlineFragment() {
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, "Please ensure stable internet connectivity!", 10000).setAction("Action", null);
        snackbar.show();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        coordinatorLayout.setVisibility(View.GONE);
// collapsingToolbarLayout.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
//            toolbar.setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.drawer_layout, new Offline_fragment());
        ft.commit();
    }

    public void callHttp(final String URL) {
        BASE_URL = URL;
        if (isNetworkAvailable()) {
            ApiService apiService = ApiUtils.getAPIService();

            // ADD A PROGRESS BAR TO BE SHOWN TO THE USER BEFORE THE DATA IS LOADED
            mainLinear.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.findFocus();
            //progressBar.setProgressBa;

            apiService.results(URL).enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getMsg().equals("success")) {
                            mArrayList.clear();

                            //DISAPPEAR THE PROGRESS BAR SHOWN EARLIER
                            mainLinear.setVisibility(View.VISIBLE);
                            progressBar.clearFocus();
                            progressBar.setVisibility(View.GONE);

                            Log.d("messageJSON",new Gson().toJson(response.body()));

                            Results results = response.body().getResults();
                            TableRecord record = new TableRecord(URL);
                            record.setData(new Gson().toJson(response.body()));
                            db.addRecord(record);

                            setView(results);
                        } else {
                            Toast.makeText(ScrollingActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Log.e("Url error", t.getLocalizedMessage());
                }
            });
        } else {
            mArrayList.clear();
            TableRecord record = new TableRecord(BASE_URL);
            db.getRecord(record);
            if(record.getData() == null)
            {
                offlineFragment();
            }
            else
            {
                TestResults results = new Gson().fromJson(record.getData(),TestResults.class);
                setView(results.getResults());
            }

//            if (databaseHelper != null) {
////                Results results = setValues(databaseHelper);
////                setView(results);
//            }
//            else {
//                offlineFragment();
//            }
        }
    }

    void setView(Results results) {
        int collapseValue;
        int drawerValue = Integer.parseInt(results.getToolBar().getIs_back());
//                        drawerValue = 1;
        collapseValue = Integer.parseInt(results.getToolBar().getTop_image());

        Log.d("Collapse", "" + collapseValue);
        Log.d("Drawer", "" + drawerValue);

        if (drawerValue == 0)
            backUrl = null;
        else if (drawerValue == 1)
            backUrl = results.getToolBar().getBack_url();

        setCollapse(collapseValue, results);
        setNavigation(drawerValue);
        mArrayList.addAll(results.getData());

        int viewType = Integer.parseInt(results.getView_type());
        Log.d("View Type", "" + viewType);
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
            case 5: //webview
                if (isNetworkAvailable())
                    webView(results.getWeb_view_url());
                else
                    offlineFragment();
                break;
            case 6: //Login Fragment
                if (isNetworkAvailable())
                    setLogin(results.getLogin());
                else
                    offlineFragment();
                break;
            default:
                Log.e("View Type", "Wrong view Type value - " + viewType);
        }
    }

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

//
//    public void setAdapter()
//    {
//        adapter = new Card1Adapter(cardDataList, ScrollingActivity.this);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//    }
//
//    public void setAdapter(int pos)
//    {
//        adapter = new CardAdapter(cardDataList,ScrollingActivity.this,pos);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

    private void setCollapse(int collapseValue, Results results) {

        if (collapseValue == 1) {
            recyclerView = findViewById(R.id.recyclerViewLinear);
            swipeRefreshLayout = findViewById(R.id.swipe);

            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(results.getToolBar().getCollapsed_top_title());
            mToolbar.setTitleTextColor(Color.parseColor(results.getToolBar().getCollapsed_top_title_color()));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(results.getToolBar().getExtended_top_title_color())));
            //getSupportActionBar().setTitle(results.getTop_heading());

            coordinatorLayout.setVisibility(View.GONE);
            //linearLayout.setVisibility(View.VISIBLE);
            //layout.setVisibility(View.VISIBLE);
        }
        if (collapseValue == 2) {
            recyclerView = findViewById(R.id.recycler_view);
            swipeRefreshLayout = findViewById(R.id.swipe_coordinator);

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(results.getToolBar().getExtended_top_title());

//            collapsingToolbarLayout.setContentScrim(new ColorDrawable(Color.parseColor("#ff00ff")));

            linearLayout.setVisibility(View.GONE);
            appBarLayout.setExpanded(true);
            mToolbar.setVisibility(View.GONE);

//            setRecyclerViewMargins();
            //layout.setVisibility(View.GONE);
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

            AppCompatImageView background = findViewById(R.id.backImage);
            Glide.with(ScrollingActivity.this)
                    .load(results.getToolBar().getTop_image_bg())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(background) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                        }
                    });

           /* swipeRefreshLayoutCoordinator.setProgressBackgroundColorSchemeColor(Color.WHITE);
            swipeRefreshLayoutCoordinator.setColorSchemeColors(Color.MAGENTA, Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE);
            swipeRefreshLayoutCoordinator.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    callHttp(BASE_URL);
                    swipeRefreshLayoutCoordinator.setRefreshing(false);
                }
            });*/
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

        int col = Integer.parseInt(results.getGrid_columns());
        int orientation = Integer.parseInt(results.getGrid_orientation());

        Log.d("Columns", "" + col);
        Log.d("Orientation", "" + orientation);

        setRecyclerView(col, orientation);
        //setNavigation(drawerValue);
    }

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

    private void setNavigation(int drawerValue) {
        if (drawerValue == 0) {
            backUrl = null;
            drawerLayout = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(ScrollingActivity.this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetNavDrawer navDrawer = new SetNavDrawer(navigationView, ScrollingActivity.this, db);
                    navDrawer.getJSON();
                    drawerLayout.closeDrawers();
                    navDrawer.setClickListener(ScrollingActivity.this);
                }
            }, 100);
            //head.setBackground();
        } else {
            drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            toggle.syncState();
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                    callHttp(backUrl);
                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                    callHttp(backUrl);
                }
            });

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
    }

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
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        } else {
            if (backUrl == null)
                super.onBackPressed();
            else {
                Toast.makeText(getApplicationContext(), backUrl, Toast.LENGTH_SHORT).show();
                callHttp(backUrl);
            }
        }
    }

    @Override
    public void onClickFunction(String url) {
        drawerLayout.closeDrawers();
        callHttp(url);
        Log.i("IN Scrolling", url);
    }

    //Search Bar controlled by searchValue = 0(Not Present), 1(Present)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        MenuInflater inflater = getMenuInflater();

        if (searchValue == 1) {
            inflater.inflate(R.menu.search_layout, menu);
//            MenuItem item = menu.findItem(R.id.search_view);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_view));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);

//            searchView.setIconifiedByDefault(true);
//            searchView.setFocusable(true);
//            searchView.setIconified(true);
//            searchView.requestFocusFromTouch();
//            searchView.onActionViewExpanded();
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

        //  if (drawerValue == 0) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
//        }

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public interface SetLayout {
        void setUrl(String url);
    }
}