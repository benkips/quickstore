package beautyplan.mabnets.quickstore;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.kosalgeek.android.json.JsonConverter;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static beautyplan.mabnets.quickstore.index.cartitems;


/**
 * A simple {@link Fragment} subclass.
 */
public class moreinfo extends Fragment {
    private String  id ;
    private String product;
    private String category;
    private String description;
    private Integer now;
    private String was;
    private String photo;
    private String measure;
    private Float ratings;

    final String Tag=this.getClass().getName();

    private TextView tvproduct;
    private TextView tvcategory;
    private TextView tvdescription;
    private TextView tvnow;
    private ImageView ivpphoto;
    private Button btnbuy;
    private RatingBar rrating;
    private RecyclerView rvcomm;
    private Mycommand mycommand;
    private ProgressDialog pd;
    private TextView tvcom;
    private TextView creview;




    public moreinfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View more= inflater.inflate(R.layout.fragment_moreinfo, container, false);
        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());

        Bundle bundle = getArguments();

        if (bundle != null) {
            id = bundle.getString("id");
            product= bundle.getString("product");
            category = bundle.getString("category");
            description = bundle.getString("description");
            now =Integer.parseInt( bundle.getString("now"));
            photo= bundle.getString("photo");
            ratings= Float.parseFloat(bundle.getString("ratings"));
            measure=bundle.getString("measure");
        }

        tvproduct=(TextView)more.findViewById(R.id.pproduct);
        tvcategory=(TextView)more.findViewById(R.id.pcategory);
        tvdescription=(TextView)more.findViewById(R.id.pdescription);
        tvnow=(TextView)more.findViewById(R.id.pprice);
        ivpphoto=(ImageView)more.findViewById(R.id.pimage);
        btnbuy=(Button)more.findViewById(R.id.pbuy);
        rrating=(RatingBar)more.findViewById(R.id.rprod);
        rvcomm=(RecyclerView)more.findViewById(R.id.rvpcomments);
        tvcom=(TextView)more.findViewById(R.id.availablecm);
        creview=(TextView)more.findViewById(R.id.cr);

        creview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new review();
                Bundle bundle=new Bundle();
                bundle.putString("prodid",id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();

            }
        });



        product= WordUtils.capitalize(product);
        category= WordUtils.capitalize(category);
        description= WordUtils.capitalize(description);

        ImageLoader.getInstance().displayImage("http://quickstore.mabnets.com/products/"+photo,ivpphoto);
        tvproduct.setText(product);
        tvcategory.setText(category);
        tvdescription.setText(description);
        tvnow.setText("Now "+now+"ksh"+"@ "+measure);
        rrating.setRating(ratings);

        rvcomm.setHasFixedSize(true);
        rvcomm.setLayoutManager(new LinearLayoutManager(getContext()));

        loadcomments(id);

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartitems.add(new cartz(id,product,photo,now));
                int ww=0;
                ww=index.cart_count;
                int crt=cartitems.size();
                ww=crt;
                ((index)getActivity()).setupbadge(crt);
                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });


       return more;
    }
    private  void loadcomments( final String prid){
        String url="http://quickstore.mabnets.com/android/getcomments.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("No comments")){
                        tvcom.setText(response);
                        /*Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();*/
                    } else {
                        Log.d(Tag, response);
                        ArrayList<commentdata> commlist= new JsonConverter<commentdata>().toArrayList(response,commentdata.class);
                        commentsadapter adapter=new commentsadapter(commlist,getContext());
                        rvcomm.setAdapter( adapter);

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
                params.put("cid",prid);
                return params;
            }
        };
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }

}
