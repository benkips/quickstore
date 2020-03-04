package beautyplan.mabnets.quickstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CommerceController extends AppCompatActivity {


    private EditText name;
    private ImageView imageViewAdd,imageViewDelete,imageViewSales,imageViewAnalysis,imageViewUsers,imageViewOrders;
    String url;
        int sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_controller);

        imageViewAdd=(ImageView)findViewById(R.id.imgAddItem);
        imageViewDelete=(ImageView)findViewById(R.id.imgDeleteItem);
        imageViewSales=(ImageView)findViewById(R.id.imgViewSales);
        imageViewAnalysis=(ImageView)findViewById(R.id.imgAnalysis);
        imageViewUsers=(ImageView)findViewById(R.id.imgUsers);
        imageViewOrders=(ImageView)findViewById(R.id.imgOrders);

        if(!SharedPrefManager.getInstance(CommerceController.this).isLoggedIn()){

            Intent intent=new Intent(CommerceController.this,Login.class);
            startActivity(intent);
        }else{
          User user=SharedPrefManager.getInstance(CommerceController.this).getUser();
          String email=user.getEmail();
           sid=user.getId();

        }

        getDealer();

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CommerceController.this,UploadItem.class);
                startActivity(intent);
            }
        });

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://quickstore.mabnets.com/android/productedit.php?sid="+sid;

                Bundle bundle=new Bundle();
                bundle.putString("webs",url);
                Intent intent=new Intent(CommerceController.this,WebLoader.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        imageViewSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://quickstore.mabnets.com/android/sales.php?sid="+sid;

                Bundle bundle=new Bundle();
                bundle.putString("webs",url);
                Intent intent=new Intent(CommerceController.this,WebLoader.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        imageViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://quickstore.mabnets.com/android/users.php?sid="+sid;

                Bundle bundle=new Bundle();
                bundle.putString("webs",url);
                Intent intent=new Intent(CommerceController.this,WebLoader.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        imageViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://quickstore.mabnets.com/android/orders.php?sid="+sid;

                Bundle bundle=new Bundle();
                bundle.putString("webs",url);
                Intent intent=new Intent(CommerceController.this,WebLoader.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        imageViewAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url="http://quickstore.mabnets.com/android/analysis.php?sid="+sid;

                Bundle bundle=new Bundle();
                bundle.putString("webs",url);

                Intent intent=new Intent(CommerceController.this,WebLoader.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(CommerceController.this, "Press again to exit", Toast.LENGTH_SHORT).show();
    }

    public void getDealer(){
        String dURL="http://quickstore.mabnets.com/android/getdealer.php?sid="+sid;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, dURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(CommerceController.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommerceController.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params=new HashMap<>();

                String ssid=String.format("%s",sid);
                params.put("sid",ssid);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CommerceController.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
