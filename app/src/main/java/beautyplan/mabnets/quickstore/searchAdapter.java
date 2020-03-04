package beautyplan.mabnets.quickstore;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder> implements Filterable {
    private Context context;
    private List<productdata> searchlist;
    private List<productdata> searchlistcpy;

    public searchAdapter (Context context,List<productdata> searchlist){
        this.context=context;
        this.searchlist=searchlist;
        this.searchlistcpy=new ArrayList<>(searchlist);

    }
    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator=LayoutInflater.from(parent.getContext());
        View view=inflator.inflate(R.layout.prodinf,parent,false);
        searchHolder srch=new searchHolder(view);
        return srch;
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {
        final productdata productdata = (productdata) searchlist.get(position);
        holder.rbrates.setRating(Float.parseFloat(productdata.ratings));
        holder.tvname.setText(productdata.product);
        ImageLoader.getInstance().displayImage("http://quickstore.mabnets.com/products/" + productdata.photo, holder.ivprod);
        holder.tvprice.setText(productdata.cprice);
        holder.cvprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("id", productdata.id);
                bundle.putString("product", productdata.product);
                bundle.putString("category", productdata.category);
                bundle.putString("description", productdata.description);
                bundle.putString("ratings", productdata.ratings);
                bundle.putString("now", productdata.cprice);
                bundle.putString("photo", productdata.photo);
                bundle.putString("measure", productdata.measure);
                Fragment fragment = new moreinfo();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        if(searchlist!=null){
            return searchlist.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return  searchfilter;
    }
    private Filter searchfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<productdata> filteredlist=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredlist.addAll(searchlistcpy);
            }else{
                String Filteredstring=constraint.toString().toLowerCase().trim();
                for(productdata item: searchlistcpy){
                    if(item.product.toLowerCase().contains(constraint)){
                        filteredlist.add(item);
                    }else if(item.cprice.toLowerCase().contains(constraint)){
                        filteredlist.add(item);
                    }/*else {
                        Toast.makeText(context, "results not found", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredlist;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchlist.clear();
            searchlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
    public static class searchHolder extends RecyclerView.ViewHolder{
        private CardView cvprod;
        private ImageView ivprod;
        private TextView tvprice;
        private TextView tvname;
        private RatingBar rbrates;
        public searchHolder(@NonNull View itemView) {
            super(itemView);

            cvprod=(CardView)itemView.findViewById(R.id.cvprod);
            ivprod=(ImageView)itemView.findViewById(R.id.prodiv);
            tvprice=(TextView)itemView.findViewById(R.id.tvprce);
            rbrates=(RatingBar)itemView.findViewById(R.id.rbprod);
            tvname=(TextView)itemView.findViewById(R.id.prodname);
        }
    }
}