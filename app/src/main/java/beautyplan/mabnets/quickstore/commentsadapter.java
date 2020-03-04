package beautyplan.mabnets.quickstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class commentsadapter extends RecyclerView.Adapter<commentsadapter.commentholder> {
    private ArrayList commentlist;
    private Context context;

    public commentsadapter(ArrayList commentlist, Context context) {
        this.commentlist = commentlist;
        this.context = context;
    }

    @NonNull
    @Override
    public commentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentinf, parent, false);
        return new commentholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentholder holder, int position) {
        commentdata commentdat=(commentdata)commentlist.get(position);
        holder.tvuser.setText(commentdat.user);
        holder.tvcomm.setText(commentdat.comment);
    }

    @Override
    public int getItemCount() {
        if(commentlist!=null){
            return commentlist.size();
        }
        return 0;
    }

    public static class commentholder extends RecyclerView.ViewHolder {
        private TextView tvuser;
        private TextView tvcomm;
        public commentholder(@NonNull View itemView) {
            super(itemView);
            tvuser=(TextView)itemView.findViewById(R.id.uname);
            tvcomm=(TextView)itemView.findViewById(R.id.commentz);

        }
    }
}
