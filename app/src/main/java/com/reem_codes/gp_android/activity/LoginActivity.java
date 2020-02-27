package com.reem_codes.gp_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Login;
import com.reem_codes.gp_android.model.User;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    TextView registerView;
    /** FIRST STEP: DEFINE THE CLIENT AND THE TYPE **/
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerView = (TextView) findViewById(R.id.registerPage);
        url = getString(R.string.api_url) + "/login";

        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        if(intent != null) {
            String registerEmail = intent.getStringExtra("email");
            String registerPassword = intent.getStringExtra("password");
            if(registerEmail != null && registerPassword != null && !registerEmail.equals("") && !registerPassword.equals("")){
                try {
                    postLoginApi(url, registerEmail, registerPassword);
                } catch (IOException e) {
                    Toast.makeText(LoginActivity.this, "Login Failed, please check network and try again", Toast.LENGTH_SHORT).show();
                }
            }

        }

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.reem_codes.gp_android", Context.MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        if(login != null) {
            intent = new Intent(this, RaspberryActivity.class);
            startActivity(intent);
        }
//        Gson gson = new Gson();
//        TypeToken<Login> token = new TypeToken<Login>(){};
//        Login currentLoggedUser = gson.fromJson(login, token.getType());

    }



    public void login(View view) {
        /** SECOND STEP: VALIDATE THE INPUT AND CALL THE API METHOD **/
        EditText emailView = (EditText) findViewById(R.id.email);
        EditText passwordView = (EditText) findViewById(R.id.password);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        System.out.println("GPDEBUG url is " + url);
        if(!isEmailValid(email))
        {
            Toast.makeText(LoginActivity.this, "email is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() == 0){
            Toast.makeText(LoginActivity.this, "You must enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        /** when using any thing that require permissions, wrap the method with a try catch**/
        try {
            postLoginApi(url, email, password);
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Login Failed, please check network and try again", Toast.LENGTH_SHORT).show();
        }
    }

    void postLoginApi(String url, String email, String password) throws IOException {
        // first, we construct the json body
        String json = "{\"email\": \"" + email + "\",\"password\":\"" + password + "\"}";
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("GPDEBUG json is " + json);

        // then, we build the request by provising the url, the method and the body
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // then we process the results coming from the web server
        client.newCall(request).enqueue(new Callback() {
            // worst case senario: a faliure either on te server or client's side
            @Override
            public void onFailure(final Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("GPDEBUG while logging in");
                    }
                });
            }

            // case we got a response
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // turn the response body into a string
                String result = response.body().string();
                System.out.println("GPDEBUG results are " + result);


                // use Gson to parse from a string to objects and lists
                // first create Gson object
                Gson gson = new Gson();
                // specify the object type: is the string a json representation of a command? a user? in our case: login
                TypeToken<Login> typeToken = new TypeToken<Login>(){};
                // create the login object using the response body string and gson parser
                final Login login = gson.fromJson(result, typeToken.getType());
                // if there is an error message
                if(login.getMessage() != null && !login.getMessage().equals("")) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // display the error message to the user
                            Toast.makeText(LoginActivity.this, login.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                // if the token is there i.e. success:
                else if(login.getAccess_token() != null && !login.getAccess_token().equals("")) {
                    // save the token in the shared preferences AKA the "cache"
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.reem_codes.gp_android", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("login", gson.toJson(login)).apply();

                    // direct user to main page
                    Intent intent = new Intent(getApplicationContext(), RaspberryActivity.class);
                    startActivity(intent);
                }
                // final else: if there is no error message and no token: something is wrong. Tell the user to retry
                else {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Login Failed, please check network and try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public static boolean isEmailValid(String email) {
        /* Email validation method using regular expression REGEX */
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
