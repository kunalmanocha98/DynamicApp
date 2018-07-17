package com.vijayjaidewan01vivekrai.dynamic_app;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.Login;
import com.vijayjaidewan01vivekrai.dynamic_app.Models.TestResults;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiService;
import com.vijayjaidewan01vivekrai.dynamic_app.Okhttpclient.ApiUtils;
import com.vijayjaidewan01vivekrai.collapsingtoolbar_github.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements ScrollingActivity.SetLayout {

    CardView card;
    TextInputEditText username, password;
    Button button;
    RelativeLayout relativeLayout;
    AppCompatImageView imageView,backImage;
    String url;
    Login login;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.login_fragment,container,false);

        /**
         * @param username - EditText, to take input from the input_box1
         * @param password - EditText, to take input from the input_box2
         * @param card - Card, containing both the input boxes
         * @param relativeLayout - it denotes the whole layout of the fragment
         * @param imageView - it will display the profile image that we will get from the url
         * @param button - the login button which will start the action of the fragment
         * @param backImage - it will display the background image that we will get from the url
         * @param login - it is Model Class object that stores the value of all the attributes
         */
        username = view.findViewById(R.id.login_username);
        password = view.findViewById(R.id.login_password);
        card = view.findViewById(R.id.login_card);
        relativeLayout = view.findViewById(R.id.card_coordinator);
        imageView = view.findViewById(R.id.appLogo);
        button = view.findViewById(R.id.login_button);
        backImage = view.findViewById(R.id.loginBackImage);
        login = (Login)getArguments().getSerializable("Login");

        // ALL THE VALUES ARE SET HERE
        username.setHint(login.getInput_box1());
        password.setHint(login.getInput_box2());
        button.setText(login.getButton_text());

        Glide.with(this)
                .load(login.getProfile_image())
                .into(imageView);
        Glide.with(this)
                .load(login.getBackground_image())
                .into(backImage);

        card.setCardBackgroundColor(Color.parseColor(login.getCard_bg_color()));
        password.setHighlightColor(Color.parseColor(login.getEdit_text_bg()));
        username.setHighlightColor(Color.parseColor(login.getEdit_text_bg()));
        relativeLayout.setBackgroundColor(Color.parseColor(login.getActivity_bg_color()));
        button.setBackgroundColor(Color.parseColor(login.getButton_bg_color()));
        button.setTextColor(Color.parseColor(login.getButton_text_color()));

        card.setAlpha(Float.parseFloat(login.getAlpha()));

        url = login.getLogin_url();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String box1 = username.getText().toString();
                String box2 = password.getText().toString();

                //check the parameters to login
                if(!box1.isEmpty() && !box2.isEmpty())
                {
                    //START A BACKGROUND TASK AS SOON AS IT RECEIVES THE VALUES OF THE INPUT BOXES
                    UserLoginTask task = new UserLoginTask(box1,box2);
                    task.execute();
                }
                else
                {
                    Toast.makeText(getContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // -------------------------------------------------- set Url function - function of SetLayout Interface to invoke the callHttp() -------------------------------------------------------------
    @Override
    public void setUrl(String url) {
        ((ScrollingActivity)getActivity()).callHttp(url);
    }


    class UserLoginTask extends AsyncTask<Void, Void, Boolean >
    {
        private final String username;
        private final String password;

        public UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // ---------------------------------------------- doInBackground function - it will post the data on the api and check the credentials if they are correct or not -------------------------
        @Override
        protected Boolean doInBackground(Void... voids) {

            ApiService apiService = ApiUtils.getAPIService();

            HashMap<String,String> map=new HashMap<>();
            map.put("input_box1",username);
            map.put("input_box2",password);

            Call<TestResults> call=apiService.getUser(url, map);
            call.enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if(response.isSuccessful()) {

                        // IF LOGIN IS SUCCESFUL THEN IN THE BODY WE WILL RECEIVE A MESSAGE = SUCCESS
                        if (response.body().getMsg().equalsIgnoreCase("success")) {

                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                            //ANIMATE THE LAYOUT TO MOVE UPWARD IF THE LOGIN IS SUCCESSFUL
                            relativeLayout.animate().translationYBy(-2000f).setDuration(300).alphaBy(1f);
                            setUrl(response.body().getResults().getUrl());
                        } else {
                            Toast.makeText(getContext(), "Credentials doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Toast.makeText(getContext(),"Make sure you entered correct credentials",Toast.LENGTH_SHORT);
                    Log.e("OnFailure",t.getLocalizedMessage());
                }
            });

            return false;
        }
    }
}
