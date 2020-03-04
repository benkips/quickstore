package beautyplan.mabnets.quickstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadItem extends AppCompatActivity {

    private EditText pname,pprice,pdescription,pMeasure,Pquantity;
    private Button buttonSend;
    Spinner prodSpinner;
    private ImageView imageViewUpload;

    private static final int REQUEST_CODE_GALLERY=999;
    private static final int STORAGE_PERMISSION_CODE=123;
    Bitmap bitmap;



    int sid;
    String dealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        pname=(EditText)findViewById(R.id.prodName);
        pprice=(EditText)findViewById(R.id.prodPrice);
        pdescription=(EditText)findViewById(R.id.prodDescription);
        pname=(EditText)findViewById(R.id.prodName);
        pMeasure=(EditText)findViewById(R.id.prodMeasure);
        Pquantity=(EditText)findViewById(R.id.quantity);

        buttonSend=(Button)findViewById(R.id.buttonUpload);
        imageViewUpload=(ImageView)findViewById(R.id.imgUploader);

        if(!SharedPrefManager.getInstance(UploadItem.this).isLoggedIn()){
            Intent intent=new Intent(UploadItem.this,Login.class);

        }else{

            User user=SharedPrefManager.getInstance(UploadItem.this).getUser();
            sid=user.getId();
            getDealer();
        }

//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.products_array, android.R.layout.simple_spinner_dropdown_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        prodSpinner.setAdapter(adapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            String urlUpload="http://quickstore.mabnets.com/android/productsaver.php?sid="+sid;
            @Override
            public void onClick(View v) {
                final String name=pname.getText().toString().trim();
                final String price=pprice.getText().toString().trim();
                final String description=pdescription.getText().toString().trim();
                final String measure=pMeasure.getText().toString().trim();
                final String quantity=Pquantity.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    pname.setError("Product name required");
                    pname.requestFocus();
                }else if(TextUtils.isEmpty(price)){
                    pprice.setError("Enter the product price");
                    pprice.requestFocus();
                }else if(TextUtils.isEmpty(description)) {
                    pdescription.setError("Give a brief description");
                    pdescription.requestFocus();
                }
                else if(TextUtils.isEmpty(measure)){
                    pMeasure.setError("Provide unit measure");
                    pMeasure.requestFocus();
                } else if(TextUtils.isEmpty(quantity)) {
                    Pquantity.setError("Provide the Quantity");
                    Pquantity.requestFocus();
                }else{
                    ProgressBar progressBar;
                    progressBar=(ProgressBar)findViewById(R.id.progressUpload);
                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(UploadItem.this, response, Toast.LENGTH_SHORT).show();
                            ProgressBar progressBar;
                            progressBar=(ProgressBar)findViewById(R.id.progressUpload);
                            progressBar.setVisibility(View.GONE);

                            pname.setText("");
                            pprice.setText("");
                            pdescription.setText("");
                            pMeasure.setText("");
                            Pquantity.setText("");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Toast.makeText(getApplicationContext(), "Error: "+ error.toString(), Toast.LENGTH_SHORT).show();

                            ProgressBar progressBar1;
                            progressBar1=(ProgressBar)findViewById(R.id.progressUpload);
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(UploadItem.this, "Failed, try checking internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            final String name=pname.getText().toString().trim();
                            final String price=pprice.getText().toString().trim();
                            final String description=pdescription.getText().toString().trim();
                            final String measure=pMeasure.getText().toString().trim();
                            final String quantity=Pquantity.getText().toString().trim();


                            Map <String, String> params=new HashMap<>();

                            String imageData = imageToString(bitmap);
                            params.put("product",name);
                            params.put("price",price);
                            params.put("description",description);
                            params.put("measure",measure);
                            params.put("category",dealer);
                            params.put("quantity",quantity);
                            params.put("photo",imageData);



                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(UploadItem.this);
                    requestQueue.add(stringRequest);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                }


            }
        });

        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(UploadItem.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode== REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,REQUEST_CODE_GALLERY);
            }else{
                Toast.makeText(UploadItem.this, "Quickstore doesn't have permission", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data !=null){

            try {
                Uri imageUri=data.getData();


                InputStream inputStream=getContentResolver().openInputStream(imageUri);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imageViewUpload.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private  String  imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes=outputStream.toByteArray();

        String encodedimage= Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return  encodedimage;
    }

    public void getDealer(){
        String dURL="http://quickstore.mabnets.com/android/getdealer.php?sid="+sid;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, dURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  Toast.makeText(UploadItem.this, response, Toast.LENGTH_SHORT).show();
                dealer=response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UploadItem.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(UploadItem.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
