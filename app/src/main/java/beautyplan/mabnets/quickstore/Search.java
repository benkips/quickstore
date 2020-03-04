package beautyplan.mabnets.quickstore;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.EachExceptionsHandler;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Search extends Fragment {
    private RecyclerView rvsrch;
    private Handler handler;
    private searchAdapter searchAdapter;


    public Search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View s= inflater.inflate(R.layout.fragment_search, container, false);
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("Quickstore");
        setHasOptionsMenu(true);
        rvsrch=(RecyclerView)s.findViewById(R.id.rvsrchid);
        handler=new Handler();
        /*LinearLayoutManager manager=new LinearLayoutManager(getContext());*/
        LinearLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rvsrch.setLayoutManager(manager);
        rvsrch.setHasFixedSize(true);
        updator();
        return s;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_file,menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem=menu.findItem(R.id.android_search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private  void updator(){

        String url="http://quickstore.mabnets.com/android/products2.php";
        PostResponseAsyncTask housetask=new PostResponseAsyncTask(getContext(),new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()) {
                    List<productdata> srchdataList = new JsonConverter<productdata>().toArrayList(s, productdata.class);
                    searchAdapter = new searchAdapter(getContext(), srchdataList);
                    rvsrch.setAdapter(searchAdapter);
                }
            }
        });
        housetask.execute(url);
        housetask.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Toast.makeText(getContext(), "internet error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Toast.makeText(getContext(), " url error ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Toast.makeText(getContext(), " protocol error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Toast.makeText(getContext(), " encoding error ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
