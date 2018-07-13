package com.vijayjaidewan01vivekrai.dynamic_app;

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
    DatabaseHelper db = null;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.login_fragment,container,false);

        username = view.findViewById(R.id.login_username);
        password = view.findViewById(R.id.login_password);
        card = view.findViewById(R.id.login_card);
        relativeLayout = view.findViewById(R.id.card_coordinator);
        imageView = view.findViewById(R.id.appLogo);
        button = view.findViewById(R.id.login_button);
        backImage = view.findViewById(R.id.loginBackImage);
        login = (Login)getArguments().getSerializable("Login");


        username.setHint(login.getInput_box1());
        password.setHint(login.getInput_box2());
//            fragment.card.setCardBackgroundColor(Color.parseColor(login.getCard_bg_color()));
        Glide.with(this)
                .load(login.getProfile_image())
                .into(imageView);
        Glide.with(this)
                .load(login.getBackground_image())
                .into(backImage);
//            fragment.password.setHighlightColor(Color.parseColor(login.getEdit_text_bg()));
//            fragment.username.setHighlightColor(Color.parseColor(login.getEdit_text_bg()));
//            fragment.relativeLayout.setBackgroundColor(Color.parseColor(login.getActivity_bg_color()));
//            fragment.button.setBackgroundColor(Color.parseColor(login.getButton_bg_color()));
//            fragment.button.setTextColor(Color.parseColor(login.getButton_text_color()));
        button.setText(login.getButton_text());
        card.setAlpha(Float.parseFloat(login.getAlpha()));
        url = login.getLogin_url();
//        url = getArguments().getString("Login_url");
        //url = "http://bydegreestest.agnitioworld.com/test"
        ScrollingActivity instance = new ScrollingActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String box1 = username.getText().toString();
                String box2 = password.getText().toString();

                //check the parameters to login
                if(!box1.isEmpty() && !box2.isEmpty())
                {
                    UserLoginTask task = new UserLoginTask(box1,box2);
                    task.execute();
                    relativeLayout.animate().translationYBy(-2000f).setDuration(300).alphaBy(1f);
                    //startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

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

        @Override
        protected Boolean doInBackground(Void... voids) {

            ApiService apiService = ApiUtils.getAPIService();

            //apiService.results(url).enqueue();
//            User user = new User();
//            user.setInput_box1(username);
//            user.setInput_box2(password);
            HashMap<String,String> map=new HashMap<>();
            map.put("input_box1",username);
            map.put("input_box2",password);
//            Call<TestResults> call = apiService.getUser(new User(username,password));
            Call<TestResults> call=apiService.getUser(url, map);
            call.enqueue(new Callback<TestResults>() {
                @Override
                public void onResponse(Call<TestResults> call, Response<TestResults> response) {
                    if (response.body().getMsg().equalsIgnoreCase("success")) {
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        relativeLayout.animate().translationYBy(-2000f).setDuration(300).alphaBy(1f);
                        setUrl(response.body().getResults().getUrl());
                    } else {
                        Toast.makeText(getContext(), "Credentials doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TestResults> call, Throwable t) {
                    Toast.makeText(getContext(),"Make sure you entered correct credentials",Toast.LENGTH_SHORT);
                    Log.e("OnFailure",t.getLocalizedMessage());
                }
            });

//            try {
//                Response<TestResults> response = call.execute();
//                if(response.isSuccessful())
//                {
//                    Toast.makeText(getContext(),response.body().getCode(),Toast.LENGTH_LONG).show();
//                    return true;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return false;
        }
    }
}
