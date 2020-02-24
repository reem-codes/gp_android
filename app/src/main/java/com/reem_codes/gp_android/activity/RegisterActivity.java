package com.reem_codes.gp_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reem_codes.gp_android.R;
import com.reem_codes.gp_android.model.Created;
import com.reem_codes.gp_android.model.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.reem_codes.gp_android.activity.LoginActivity.isEmailValid;

public class RegisterActivity extends AppCompatActivity {
    TextView loginView;
    /** FIRST STEP: DEFINE THE CLIENT AND THE TYPE **/
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginView = (TextView) findViewById(R.id.loginPage);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void register(View view) {
        /** SECOND STEP: VALIDATE THE INPUT AND CALL THE API METHOD **/
        EditText emailView = (EditText) findViewById(R.id.email);
        EditText passwordView = (EditText) findViewById(R.id.password);
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String url = getString(R.string.api_url) + "/user";
        System.out.println("GPDEBUG url is " + url);
        if(!isEmailValid(email))
        {
            Toast.makeText(RegisterActivity.this, "email is incorrect", Toast.LENGTH_SHORT).show();
        }
        if(password.length() == 0){
            Toast.makeText(RegisterActivity.this, "You must enter a password", Toast.LENGTH_SHORT).show();
        }
        else {
            /** when using any thing that require permissions, wrap the method with a try catch**/
            try {
                postUserApi(url, email, password);
            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, "Login Failed, please check network and try again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    void postUserApi(String url, String email, final String password) throws IOException {
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
                        System.out.println("GPDEBUG while registering");
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
                // specify the object type: is the string a json representation of a command? a user? in our case: user
                TypeToken<Created<User>> typeToken = new TypeToken<Created<User>>(){};
                // create the user object using the response body string and gson parser
                final Created<User> user = gson.fromJson(result, typeToken.getType());
                // if there is an error message
                if(user.getObject() == null || user.getObject().getEmail() == "") {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // display the error message to the user
                            Toast.makeText(RegisterActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                // if the user is there i.e. success
                else if(!user.getObject().getEmail().equals("")) {
                    // display successful message to user
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, user.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    // direct user to login page
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("email", user.getObject().getEmail());
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                // final else: if there is no error message and no user: something is wrong. Tell the user to retry
                else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registering Failed, please check network and try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
