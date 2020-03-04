package beautyplan.mabnets.quickstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import static beautyplan.mabnets.quickstore.index.cartitems;


public class cashoutadapter extends RecyclerView.Adapter<cashoutadapter.cashouholder> {

    private ArrayList<cartz> cartzlist;
    private Context context;
    private cashout fragmentt;

    public cashoutadapter(Context context, ArrayList<cartz> cartzlis, cashout fragment) {
        this.cartzlist = cartzlis;
        this.context=context;
        this.fragmentt=fragment;
    }

    @NonNull
    @Override
    public cashouholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cashoutinf,parent,false);
        return new cashouholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cashouholder holder, final int position) {
        final cartz cartzitemz=cartzlist.get(position);
        ImageLoader.getInstance().displayImage("http://quickstore.mabnets.com/products/"+cartzitemz.getPhoto(),holder.ivcart);
        holder.tvcart.setText(cartzitemz.getProduct());
        holder.tvpcart.setText(cartzitemz.getPrice()+"ksh");

        holder.btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cartzlist.remove(position);
                 fragmentt.sendgson();
                notifyDataSetChanged();
                int crt=cartzlist.size();
                index.setupbadge(crt);
               /* int p=cartzitemz.getPrice();
                fragmentt.totalz-=p;
                fragmentt.retrivebal(fragmentt.totalz);*/
                if(crt==0){
                    cartitems.clear();
                    FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                    Fragment fragment=new main();
                    /*Fragment fragmentm = new main();*/
                    fragmentManager.beginTransaction().replace(R.id.framelayout,fragment).commit();

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return cartzlist.size();
    }

    public static  class cashouholder extends RecyclerView.ViewHolder {
        private ImageView ivcart;
        private Button btncart;
        private TextView tvcart;
        private TextView tvpcart;

        public cashouholder(@NonNull View itemView) {
            super(itemView);

            ivcart=(ImageView) itemView.findViewById(R.id.ivcart);
            tvcart=(TextView)itemView.findViewById(R.id.tvcart);
            btncart=(Button)itemView.findViewById(R.id.btncart);
            tvpcart=(TextView)itemView.findViewById(R.id.tvpcart);

        }
    }

}
