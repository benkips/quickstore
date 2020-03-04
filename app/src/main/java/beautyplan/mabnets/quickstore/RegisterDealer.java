package beautyplan.mabnets.quickstore;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterDealer extends AppCompatActivity {

    private EditText name,phone,email,idnumber;
    private Spinner spinnerDeals;
    private Button buttonRegister;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dealer);

        phone=(EditText)findViewById(R.id.dealerPhone);
        email=(EditText)findViewById(R.id.dealerMail);
        idnumber=(EditText)findViewById(R.id.dealerID);
        spinnerDeals=(Spinner)findViewById(R.id.spinnerDealer);
        buttonRegister=(Button)findViewById(R.id.buttonRegisterDeals);
        progressBar=(ProgressBar) findViewById(R.id.progressRegDealer);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.array_dealer,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeals.setAdapter(adapter);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fon = phone.getText().toString().trim();
                final String qmail = email.getText().toString().trim();
                final String idno = idnumber.getText().toString().trim();
                final String category = spinnerDeals.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(fon)) {
                    phone.setError("Phone number is required");
                    phone.requestFocus();
                }  else if (TextUtils.isEmpty(idno)) {
                    idnumber.setError("ID number required");
                    idnumber.requestFocus();
                }
             else if (TextUtils.isEmpty(qmail)) {
                email.setError("ID number required");
                email.requestFocus();
            }else {

                 progressBar.setVisibility(View.VISIBLE);

                    String dRegister = "http://quickstore.mabnets.com/android/accountrequest.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, dRegister, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(RegisterDealer.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterDealer.this,"An error occurred, try again", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {


                            Map<String, String> params = new HashMap<>();
                            params.put("fon", fon);
                            params.put("email", qmail);
                            params.put("idno", idno);
                            params.put("category", category);


                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterDealer.this);
                    requestQueue.add(stringRequest);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }
            }
        });



    }

}
