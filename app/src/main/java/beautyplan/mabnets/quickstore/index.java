package beautyplan.mabnets.quickstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    File cacheDir;
    DisplayImageOptions options;
    ImageLoaderConfiguration config;
    private static TextView carttext;
    public static int cart_count=0;
    public static ArrayList cartitems=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();

   /*     ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(index.this)
                .build();*/
        ImageLoader.getInstance().init(config);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragmentmain=new main();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        final MenuItem menuItem=menu.findItem(R.id.cart_action);
        View actionView= MenuItemCompat.getActionView(menuItem);
        carttext=(TextView)actionView.findViewById(R.id.cart_badge);

        setupbadge(cart_count);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(index.this,settings.class));
            return true;
        }else if(id == R.id.action_contactus){
            startActivity(new Intent(index.this,contactus.class));
            return true;
        }else if(id == R.id.action_aboutus){
            startActivity(new Intent(index.this,aboutus.class));
            return true;
        }else if(id==R.id.cart_action){
            if (cartitems.size()!=0) {
                Fragment fragmentmain=new cashout();
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
            }else{
                Toast.makeText(this, "You have to add product to cart", Toast.LENGTH_SHORT).show();
            }
            return true;

        }else if(id==R.id.search){
            Fragment fragmentmain=new Search();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragmentmain).addToBackStack(null).commit();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rates) {
            // Handle the camera action
            startActivity(new Intent(index.this,Login.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public static void setupbadge(int cart){
        /*carttext.setText(String.valueOf(cart));*/
        if(cart==0){
            if(carttext.getVisibility()!=View.GONE){
                carttext.setVisibility(View.GONE);
            }
        }else{
            carttext.setText(String.valueOf(Math.min(cart,99)));
            carttext.setText(String.valueOf(cart));
            if(carttext.getVisibility()!=View.VISIBLE){
                carttext.setVisibility(View.VISIBLE);
            }
        }
    }
}
