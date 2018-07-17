package com.vijayjaidewan01vivekrai.dynamic_app;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vijayjaidewan01vivekrai.collapsingtoolbar_github.R;

public class Offline_fragment extends Fragment implements ScrollingActivity.SetLayout{


    public Offline_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_offline_fragment, container, false);

        Button button = view.findViewById(R.id.button);

        //RETRY BUTTON TO REFRESH THE PAGE IF INTERNET IS CONNECTED
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    setUrl(ScrollingActivity.BASE_URL);
                    view.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    // -------------------------------------------------- is Network Available - function to check the internet connectivity ----------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    // -------------------------------------------------- set Url function - function of SetLayout Interface to invoke the callHttp() -------------------------------------------------------------
    @Override
    public void setUrl(String url) {
        ((ScrollingActivity)getActivity()).callHttp(url);
    }
}
