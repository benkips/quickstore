package beautyplan.mabnets.quickstore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class categoryadapter extends RecyclerView.Adapter<categoryadapter.categoryholder> {
    private ArrayList catelist;
    private Context context;

    public categoryadapter(ArrayList catelist, Context context) {
        this.catelist = catelist;
        this.context = context;
    }

    @NonNull
    @Override
    public categoryholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.catinf,parent,false);
        return new categoryholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryholder holder, int position) {
        final cate cate=(cate)catelist.get(position);
        if(cate.category.equals("kitchen")){
            holder.tvcat.setText(cate.category+" utensilis");
        }else if(cate.category.equals("men") || cate.category.equals("women")){
            holder.tvcat.setText(cate.category+" wear");
        }else{
            holder.tvcat.setText(cate.category);
        }
        ImageLoader.getInstance().displayImage("http://quickstore.mabnets.com/android/iconz/"+cate.icon,holder.ivcat);
        holder.cvct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                        Bundle bundle=new Bundle();
                        bundle.putString("category",cate.category);
                        Fragment fragmentc=new products();
                        fragmentc.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.framelayout,fragmentc).addToBackStack(null).commit();
            }
        });
        holder.tvnum.setText("("+cate.total+")");


    }

    @Override
    public int getItemCount() {
        if(catelist!=null){
           return  catelist.size();
        }
        return 0;
    }

    public static class categoryholder extends RecyclerView.ViewHolder {
        private ImageView ivcat;
        private TextView tvcat;
        private CardView cvct;
        private  TextView tvnum;
        public categoryholder(@NonNull View itemView) {
            super(itemView);

            ivcat=(ImageView)itemView.findViewById(R.id.cativ);
            tvcat=(TextView)itemView.findViewById(R.id.tvcat);
            tvnum=(TextView)itemView.findViewById(R.id.tvpnum);
            cvct=(CardView) itemView.findViewById(R.id.cvcatt);
        }
    }
}
