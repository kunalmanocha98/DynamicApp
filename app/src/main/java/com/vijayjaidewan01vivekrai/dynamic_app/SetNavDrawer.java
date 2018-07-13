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

    public SetNavDrawer(LinearLayout view, Context context, DatabaseHelper db) {
        navigationView = view;
        this.context = context;
        this.db = db;
        navDrawer = new NavDrawer();
    }

    public void getJSON() {
        recyclerView = navigationView.findViewById(R.id.recycler_view_nav);
        navHeaderImage = navigationView.findViewById(R.id.nav_header_image);
        navHeaderText = navigationView.findViewById(R.id.nav_header_text);
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            ApiService apiService = ApiUtils.getAPIService();
            apiService.results(url).enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if (response.isSuccessful()) {
                        TableRecord record = new TableRecord(url);
                        record.setData(new Gson().toJson(response.body()));
                        db.addRecord(record);
                        navDrawer = response.body().getResults().getNavDrawer();
//                        navigationView.setBackgroundColor(Color.parseColor(navDrawer.getNav_drawer_bg_color()));
                        setDrawer();
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Log.e("URL error", t.getLocalizedMessage());
                }
            });
        }
     else if(conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() ==NetworkInfo.State.DISCONNECTED
                ||conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==NetworkInfo.State.DISCONNECTED){
            TableRecord record = new TableRecord(url);
            db.getRecord(record);
            TestResults results = new Gson().fromJson(record.getData(),TestResults.class);
            navDrawer = results.getResults().getNavDrawer();
//            navigationView.setBackgroundColor(Color.parseColor(navDrawer.getNav_drawer_bg_color()));
            setDrawer();
    }
}

public void setDrawer()
{
    Glide.with(context)
            .load(navDrawer.getHeader_layout().getImage())
            .asBitmap()
            .placeholder(R.drawable.grey)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(new SimpleTarget<Bitmap>(200, 200) {
                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                }

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    navHeaderImage.setImageBitmap(resource);
                }
            });

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(context));
    navHeaderText.setText(navDrawer.getHeader_layout().getText());
    NavDrawerCardAdapter cardAdapter = new NavDrawerCardAdapter(navDrawer.getMenu_items(), context);
    recyclerView.setAdapter(cardAdapter);
    cardAdapter.notifyDataSetChanged();
    cardAdapter.setClickListener((OnClickSet) context);

    navigationView.setBackgroundColor(Color.parseColor(navDrawer.getNav_drawer_bg_color()));
}
    private OnClickSet onClickSetListener;

    public void setClickListener(OnClickSet onClickSet) {
        this.onClickSetListener = onClickSet;
    }
}

//                    new DownloadImage().execute();
//                    DatabaseHelper databaseHelper = new DatabaseHelper(context,"Toolbar",null,1);

// notify user you are online
//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

//navigationView = view.findViewById(R.id.nav_view);
//        Log.d("Header Count", "" + navigationView.getHeaderCount());

//                      menu.add("Menu");

//                    Log.d("Menu item 1",""+menu.getItem(0));

//                      ArrayList<Bitmap> bm = downloadImage();

                   /* navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            Log.d("Clicked item", "" + (item.getItemId()));
//                            item.setCheckable(true);
                            item.setChecked(true);
//                            item.setEnabled(true);
                            if (onClickSetListener != null)
                                onClickSetListener.onClickFunction(navDrawer.getMenu_items().get(item.getItemId() - 1).getUrl());
                            //context.startActivity(new Intent(context,ScrollingActivity.class));
                            //Toast.makeText(context,item.getTitle() + " : " + navDrawer.getMenu_items().get(item.getItemId()).getUrl(),Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
*/
//                    NavigationView.OnNavigationItemSelectedListener clickListener = new ScrollingActivity();
//                    clickListener.onNavigationItemSelected(navigationView.getMenu().getItem());
//menu.add(R.id.group1,R.id.item1,Menu.NONE,response.body().getResults().getNavDrawer().getMenu_items().get())

//        menu = navigationView.getMenu();
//        menu.clear();


/*  public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
*/

    /*public ArrayList<Bitmap> downloadImage()
    {
        final ArrayList <Bitmap> bm= new ArrayList<>();
        for(Menu_items items : navDrawer.getMenu_items()) {
            Glide.with(context)
                    .load(items.getIcon())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(50, 50) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                           bm.add(resource);
                        }
                    });
        }
        return bm;
    }*/

    /*class DownloadImage extends AsyncTask<Void, Void, Void> {
        ArrayList<Bitmap> bm = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            for (Menu_items items : navDrawer.getMenu_items()) {
                try {
                    URL url = new URL(items.getIcon());
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);

//                    Matrix matrix = new Matrix();
//                    matrix.postScale(30,30);
//                    bitmap = Bitmap.createBitmap(bitmap,0,0,50,50);
                    bm.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                        *//*Glide.with(context)
                                .load(items.getIcon())
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(50,50) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                     bitmap = resource;
                                     setbitmap();
                                    }
                                });*//*
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            i = Menu.FIRST - 1;
            while (i < navDrawer.getMenu_items().size()) {
                items = navDrawer.getMenu_items().get(i);
                Log.d("Nav Drawer", "Yes");

                Drawable drawable = new BitmapDrawable(context.getResources(), bm.get(i));

                menu.add(0, (Menu.FIRST + i), Menu.NONE, items.getItem()).setIcon(drawable);
                i++;
            }
        }
    }
}*/

//                            .asBitmap()
//                            .listener(new RequestListener<String, Bitmap>() {
//                                @Override
//                                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                    Log.d("in listener",model);
//                                    bitmap = resource;
//                                    return true;
//                                }
//


//                        try {
//                            Bitmap bitmap = Glide.with(context)
//                                                            .load(items.getUrl())
//                                                            .asBitmap()
//                                                            .into(50,50)
//                                                            .get();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }


//                        Bitmap bm = null;
//                        ApiService apiService1 = ApiUtils.getAPIService();
//                        Call call1 = apiService1.results(url);
//                        try {
//                            Response<ResponseBody> response1 = call1.execute();
//                            bm = BitmapFactory.decodeStream(response1.body().byteStream());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        /*InputStream iStream = null;
                        try {
                            iStream = (InputStream) new URL(url).getContent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Drawable drawable = Drawable.createFromStream(iStream,"icon");*/
//                            Drawable drawable = BitmapFactory.decodeStream((InputStream) u.getContent());


//    NavigationView view = findViewById(R.id.nav_view);
//                        view.setNavigationItemSelectedListener(ScrollingActivity.this);
//                                View head = view.getHeaderView(0);
//                                AppCompatImageView image = head.findViewById(R.id.nav_header_image);

