package beautyplan.mabnets.quickstore;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.kosalgeek.android.caching.FileCacher;
import com.kosalgeek.android.json.JsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ordernow extends Fragment {
    private EditText uemailz;
    private EditText ulocationz;
    private EditText uphonez;
    private Button btnfinshz;
    private  String bought="";
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private FileCacher<String> detailcacher;


    public ordernow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vorders=inflater.inflate(R.layout.fragment_ordernow, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = getArguments();
        if (bundle != null) {
            bought=bundle.getString("productbought");
        }
        detailcacher=new FileCacher<>(getContext(),"details.txt");
        pd=new ProgressDialog(getContext());
        pd.setMessage("completing your order...");
        mycommand=new Mycommand(getContext());
        uemailz=(EditText)vorders.findViewById(R.id.uemail);
        ulocationz=(EditText)vorders.findViewById(R.id.ulocation);
        uphonez=(EditText)vorders.findViewById(R.id.uphone);
        btnfinshz=(Button)vorders.findViewById(R.id.btnfinsh);

        btnfinshz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fnamez=uemailz.getText().toString().trim();
                final String locationz=ulocationz.getText().toString().trim();
                final String phoneno=uphonez.getText().toString().trim();
                if(!bought.equals("null")) {
                    loadorder(fnamez, phoneno, locationz);
                }
            }
        });



        return  vorders;
    }
    private void loadorder(final String em,final String p,final String adress){
        if( em.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(2)
                    .playOn(uemailz);
            uemailz.setError("invalid names");
            uemailz.requestFocus();
            return;
        }else if(p.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(2)
                    .playOn(uphonez);
            uphonez.setError("inavalid phone number");
            uphonez.requestFocus();
            return;
        }else  if(adress.isEmpty()){
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(2)
                    .playOn( ulocationz);
            ulocationz.setError("inavalid location");
            ulocationz.requestFocus();
            return;
        }else{
            if(!isvalidemail(em)){
                uemailz.setError("email is invalid");
                uemailz.requestFocus();
                return;
            }else if(!isphone(p) || (p.length()!=10 || !p.startsWith("07"))) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(2)
                        .playOn(uphonez);
                uphonez.setError("inavalid phone number");
                uphonez.requestFocus();
                return;
            }else
            {

                String url="http://quickstore.mabnets.com/android/ordersaver.php";
                StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        pd.dismiss();
                        Log.d(Tag,response);
                        if(!response.isEmpty()){

                            if(isStringInteger(response)){
                                index.setupbadge(0);
                                index.cartitems.clear();
                                ArrayList info=new ArrayList();
                                info.add(new details(em,p,adress));
                                Gson gson = new GsonBuilder().create();
                                JsonArray myCustomArrayz = gson.toJsonTree(info).getAsJsonArray();
                                String jsondata=String.valueOf(myCustomArrayz);
                                bought="null";
                                Log.d(Tag,response);
                                Log.d(Tag,em);
                                try {
                                    detailcacher.writeCache(em);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                                alert.setMessage("Your order has been received, you will be contacted for delivery.Do you wish to pay on delivery?");
                                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            Fragment fragment=new mpesa();
                                            Bundle bundle=new Bundle();
                                            bundle.putString("user",em);
                                            bundle.putInt("tid",Integer.parseInt(response));
                                            fragment.setArguments(bundle);
                                            getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();


                                    }
                                });
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(), "Thank you", Toast.LENGTH_SHORT).show();
                                        Fragment fragment=new main();
                                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();

                                    }
                                });
                                alert.show();
                            } else {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error!=null) {
                            Log.d(TAG, error.toString());
                            if (error instanceof TimeoutError) {
                                pd.dismiss();
                                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                                alert.setMessage("please check your internet connectivity");
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                    }
                                });
                                alert.show();
                            } else if (error instanceof NoConnectionError) {
                                pd.dismiss();
                                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                                alert.setMessage("please check your internet connectivity");
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                    }
                                });
                                alert.show();
                            } else if (error instanceof NetworkError) {
                                pd.dismiss();
                                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                                alert.setMessage("please check your internet connectivity");
                                alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.exit(0);
                                    }
                                });
                                alert.show();
                            }else if (error instanceof AuthFailureError) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "errorin Authentication", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ParseError) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "error while parsing", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ServerError) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "error  in server", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof ClientError) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "error with Client", Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                Toast.makeText(getContext(), "error while loading", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<>();
                        params.put("itemsbought",bought);
                        params.put("phone",p);
                        params.put("location",adress);
                        params.put("email",em);
                        return params;
                    }
                };
                mycommand.add(request);
                pd.show();
                mycommand.execute();
                mycommand.remove(request);

            }
        }
    }
    public static boolean isphone(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }
    private final boolean isvalidemail(String target){
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean isStringInteger(String number ){
        try{
            Integer.parseInt(number);
        }catch(Exception e ){
            return false;
        }
        return true;
    }
}
