package beautyplan.mabnets.quickstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import  java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText mail, password;
    private Button loginButton;
    ProgressBar progressBar;
    private Button regButton;

    final String urlLogin = "http://quickstore.mabnets.com/android/Api.php?apicall=login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.buttonLoginAdmin);
        mail = (EditText) findViewById(R.id.adminMail);
        password = (EditText) findViewById(R.id.adminPass);
        progressBar = (ProgressBar) findViewById(R.id.progressLogin);
        regButton=(Button)findViewById(R.id.buttonRegisterDealer);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(Login.this,RegisterDealer.class));
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }
            public  void  loginUser(){

                final String email = mail.getText().toString().trim();
                final String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mail.setError("Email is required");
                    mail.requestFocus();
                } else if (TextUtils.isEmpty(pass)) {
                    password.setError("Enter your password");
                    password.requestFocus();
                } else {

                    class UserLogin extends AsyncTask<Void,Void,String>{

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();

                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            progressBar.setVisibility(View.GONE);

                            try{

                                JSONObject jsonObject=new JSONObject(s);

                                if(!jsonObject.getBoolean("error")){
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                    JSONObject  userJson=jsonObject.getJSONObject("user");

                                    User user=new User(
                                            userJson.getInt("id"),
                                            userJson.getString("email")
                                    );

                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                    finish();
                                    startActivity(new Intent(Login.this,CommerceController.class));
                                    Toast.makeText(Login.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                                }else{

                                    Toast.makeText(Login.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();

                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        protected String doInBackground(Void... voids) {

                            RequestHandler requestHandler=new RequestHandler();

                            HashMap <String,String> params=new HashMap<>();
                            params.put("email",email);
                            params.put("pass",pass);

                            return requestHandler.sendPostRequest(urlLogin,params);
                        }
                    }

                    UserLogin userLogin=new UserLogin();
                    userLogin.execute();

                }
            }

}