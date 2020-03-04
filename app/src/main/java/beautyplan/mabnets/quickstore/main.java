package beautyplan.mabnets.quickstore;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class main extends Fragment {
    ViewFlipper viewf;
    private RecyclerView rvcategory;
    private Mycommand mycommand;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();

    public main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mai= inflater.inflate(R.layout.fragment_main, container, false);


        pd=new ProgressDialog(getContext());
        pd.setMessage("Loading");
        mycommand=new Mycommand(getContext());

        viewf=(ViewFlipper)mai.findViewById(R.id.vf);
        rvcategory=(RecyclerView)mai.findViewById(R.id.rvcat);

        int images[]={R.drawable.clothesmen,R.drawable.clotheswomen,R.drawable.computer,R.drawable.kitchen,R.drawable.phones};
         for(int image: images){
             flipinmages(image);
         }


        LinearLayoutManager manager = new GridLayoutManager(getContext(), 2);
        /*  LinearLayoutManager manager=new GridLayoutManager(getContext(),2,rv.VERTICAL,false);*/
        rvcategory.setLayoutManager(manager);
        loadcat();


        return  mai;
    }
    private void flipinmages(int image){
        ImageView imageview=new ImageView(getContext());
        imageview.setBackgroundResource(image);

        viewf.addView(imageview);
        viewf.setFlipInterval(3000);
        viewf.setAutoStart(true);

        viewf.setInAnimation(getContext(),android.R.anim.slide_in_left);
        viewf.setOutAnimation(getContext(),android.R.anim.slide_out_right);

    }
    private  void loadcat(){
        String url="http://quickstore.mabnets.com/android/getcategories.php";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("no categories")){
                        Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, response);
                        ArrayList<cate> prodlist= new JsonConverter<cate>().toArrayList(response,cate.class);
                        categoryadapter adapter=new categoryadapter(prodlist,getContext());
                        rvcategory.setAdapter( adapter);

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
        });
        mycommand.add(request);
        pd.show();
        mycommand.execute();
        mycommand.remove(request);
    }
}
