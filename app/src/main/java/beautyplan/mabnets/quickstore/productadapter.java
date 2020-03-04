package beautyplan.mabnets.quickstore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class productadapter extends RecyclerView.Adapter<productadapter.productholder> {
    private ArrayList prodlist;
    private Context context;

    public productadapter(ArrayList prodlist, Context context) {
        this.prodlist = prodlist;
        this.context = context;
    }

    @NonNull
    @Override
    public productholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodinf, parent, false);
        return new productholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productholder holder, int position) {
        final productdata productdata=(productdata)prodlist.get(position);
        holder.rbrates.setRating(Float.parseFloat(productdata.ratings));
        holder.tvname.setText(productdata.product);
        ImageLoader.getInstance().displayImage("http://quickstore.mabnets.com/products/"+productdata.photo,holder.ivprod);
        holder.tvprice.setText(productdata.cprice+"Ksh");
        holder.cvprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("id",productdata.id);
                bundle.putString("product",productdata.product);
                bundle.putString("category",productdata.category);
                bundle.putString("description",productdata.description);
                bundle.putString("ratings",productdata.ratings);
                bundle.putString("now",productdata.cprice);
                bundle.putString("photo",productdata.photo);
                bundle.putString("measure",productdata.measure);
                Fragment fragment=new moreinfo();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayout,fragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        if(prodlist!=null){
            return  prodlist.size();
        }
        return 0;
    }

    public  static  class productholder extends RecyclerView.ViewHolder {
        private CardView cvprod;
        private ImageView ivprod;
        private TextView tvprice;
        private TextView tvname;
        private RatingBar rbrates;
        public productholder(@NonNull View itemView) {
            super(itemView);

         cvprod=(CardView)itemView.findViewById(R.id.cvprod);
         ivprod=(ImageView)itemView.findViewById(R.id.prodiv);
         tvprice=(TextView)itemView.findViewById(R.id.tvprce);
         rbrates=(RatingBar)itemView.findViewById(R.id.rbprod);
         tvname=(TextView)itemView.findViewById(R.id.prodname);
        }
    }
}
