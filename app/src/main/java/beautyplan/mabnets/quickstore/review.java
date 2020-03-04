package beautyplan.mabnets.quickstore;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class review extends Fragment {
    private  String prodid;
    private FileCacher<String> detailcacher;
    final String Tag=this.getClass().getName();
    private String t;
    private EditText rview;
    private Button rviewbtn;
    private Mycommand mycommand;
    private ProgressDialog pd;

    public review() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rvv= inflater.inflate(R.layout.fragment_review, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            prodid=bundle.getString("prodid");
        }
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());
        detailcacher=new FileCacher<>(getContext(),"details.txt");
        rview=(EditText)rvv.findViewById(R.id.cmz);
        rviewbtn=(Button)rvv.findViewById(R.id.cmzbtn);

        if(detailcacher.hasCache()){
            try {
                t=detailcacher.readCache();
                /*cartcacher.clearCache();*/
                Log.d(Tag, t);
                rviewbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String com=rview.getText().toString().trim();
                        sendcom(com,prodid,t);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        return rvv;
    }
    private void sendcom(final String Comment,final  String prod,final String e){
        String url="http://quickstore.mabnets.com/android/commentsaver.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("success")){
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                        Fragment fragment=new main();
                        getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();
                    } else {
                        Log.d(Tag, response);
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
                        alert.setCancelable(false);
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
                        alert.setCancelable(false);
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        });
                        alert.show();
                        alert.setCancelable(false);
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
                params.put("comment",Comment);
                params.put("email",e);
                params.put("id",prod);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);

    }
}
